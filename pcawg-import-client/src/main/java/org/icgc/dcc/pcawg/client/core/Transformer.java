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

  @SneakyThrows
  public static <T> Transformer<T> newLocalFileTransformer(
    @NonNull String outputDirectory,
    @NonNull String dccProjectCode,
    @NonNull String outputTsvFilename,
    @NonNull TSVConverter<T> tsvConverter,
      final boolean createNewFile){
    val appendMode = !createNewFile;
    val outputFilename = getOutputFileName(outputDirectory, dccProjectCode, outputTsvFilename);
    val localFileWriter = newLocalFileWriter(outputFilename, appendMode, CREATE_DIRECTORY_IF_DNE);
    val doesFileExist = localFileWriter.isFileExistedPreviously();
    val writeHeaderLineInitially =  shouldWriteHeader(createNewFile, doesFileExist);
    return newTransformer(tsvConverter, localFileWriter, writeHeaderLineInitially);
  }

  @SneakyThrows
  public static <T> Transformer<T> newHdfsTransformer(
      @NonNull String outputDirectory,
      @NonNull String dccProjectCode,
      @NonNull String outputTsvFilename,
      @NonNull TSVConverter<T> tsvConverter,
      @NonNull String hostname,
      @NonNull String port,
      final boolean createNewFile){
    val appendMode = !createNewFile;
    val outputFilename = getOutputFileName(outputDirectory, dccProjectCode, outputTsvFilename);
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
