package org.icgc.dcc.pcawg.client.core;

import lombok.Getter;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.icgc.dcc.pcawg.client.tsv.TSVConverter;

import java.io.Closeable;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.icgc.dcc.common.core.util.Joiners.PATH;
import static org.icgc.dcc.pcawg.client.core.HdfsFileWriter.newHdfsFileWriter;
import static org.icgc.dcc.pcawg.client.core.LocalFileWriter.newLocalFileWriter;

@Slf4j
public class Transformer<T> implements Closeable{
  private static final boolean CREATE_DIRECTORY_IF_DNE = true;

  private final TSVConverter<T> tsvConverter;

  private final Writer writer;

  @Getter
  private boolean writeHeader;

  private static String getOutputFileName(String outputDirectory, String dccProjectCode, String outputTsvFilename){
    return PATH.join(outputDirectory, dccProjectCode, outputTsvFilename);
  }

  public static <T> Transformer<T> newTransformer( final TSVConverter<T> tsvConverter, final Writer writer, final boolean isNewFile){
    return new Transformer<T>(tsvConverter, writer, isNewFile);
  }

  //TODO: This should be in the factory
  @SneakyThrows
  public static <T> Transformer<T> newLocalFileTransformer(
    @NonNull String outputDirectory,
    @NonNull String dccProjectCode,
    @NonNull String outputTsvFilename,
    @NonNull TSVConverter<T> tsvConverter,
      final boolean createNewFile){
    val outputFilename = getOutputFileName(outputDirectory, dccProjectCode, outputTsvFilename);
    initParentDir(outputFilename);
    val appendMode = !createNewFile;
    val doesFileExist = checkIfFileExists(outputFilename);
    val writeHeaderLineInitially =  shouldWriteHeader(createNewFile, doesFileExist);
    val writer = newLocalFileWriter(outputFilename, appendMode, CREATE_DIRECTORY_IF_DNE);
    return newTransformer(tsvConverter, writer, writeHeaderLineInitially);
  }

  //TODO: This should be in the factory
  @SneakyThrows
  public static <T> Transformer<T> newHdfsTransformer(
      @NonNull String outputDirectory,
      @NonNull String dccProjectCode,
      @NonNull String outputTsvFilename,
      @NonNull TSVConverter<T> tsvConverter,
      @NonNull String hostname,
      @NonNull String port,
      final boolean createNewFile){
    val outputFilename = getOutputFileName(outputDirectory, dccProjectCode, outputTsvFilename);
    val appendMode = !createNewFile;
    val hdfsFileWriter= newHdfsFileWriter(hostname, port, outputFilename, appendMode, CREATE_DIRECTORY_IF_DNE);
    val doesFileExist = hdfsFileWriter.isFileExistedPreviously();
    val writeHeaderLineInitially =  shouldWriteHeader(createNewFile, doesFileExist);
    return newTransformer(tsvConverter, hdfsFileWriter, writeHeaderLineInitially);
  }

  private static boolean shouldWriteHeader(boolean createNewFile, boolean doesFileExist){
      return createNewFile || !doesFileExist;
  }

  @SneakyThrows
  private Transformer( final TSVConverter<T> tsvConverter, final Writer writer, final boolean writeHeader){
    this.tsvConverter = tsvConverter;
    this.writeHeader = writeHeader;
    log.info("WriterHeader: {}", writeHeader);
    this.writer = writer;
  }

  private static boolean checkIfFileExists(String outputFilename){
    return Paths.get(outputFilename).toFile().exists();
  }

  @SneakyThrows
  private static void initDir(@NonNull final Path dir) {
    val dirDoesNotExist = !Files.exists(dir);
    if (dirDoesNotExist) {
      Files.createDirectories(dir);
    }
  }

  // Used for subdirectories inside outputDir
  private static void initParentDir(@NonNull String filename) {
    val parentDir = Paths.get(filename).getParent();
    initDir(parentDir);
  }

  @SneakyThrows
  public void transform(T t){
    if(writeHeader){
      writer.write(tsvConverter.toTSVHeader()+"\n");
      writeHeader = false;
    }
    writer.write(tsvConverter.toTSVData(t)+"\n");
  }

  @Override
  public void close() throws IOException {
      writer.close();
  }

}
