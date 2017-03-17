package org.icgc.dcc.pcawg.client.core;

import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.val;
import org.icgc.dcc.pcawg.client.tsv.TSVConverter;

import java.io.Closeable;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static org.icgc.dcc.common.core.util.Joiners.PATH;

public class Transformer<T> implements Closeable{

  private static final boolean APPEND_MODE_ENABLED = true;

  private final TSVConverter<T> tsvConverter;

  private final Writer writer;

  private boolean isNewFile;

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
    @NonNull TSVConverter<T> tsvConverter ){
    val outputFilename = getOutputFileName(outputDirectory, dccProjectCode, outputTsvFilename);
    initParentDir(outputFilename);
    val writer = new FileWriter(outputFilename, APPEND_MODE_ENABLED);
    val isNewFile = checkIfNewFile(outputFilename);
    return newTransformer(tsvConverter, writer, isNewFile);
  }

  //TODO: This should be in the factory
  @SneakyThrows
  public static <T> Transformer<T> newHdfsTransformer(
      @NonNull String outputDirectory,
      @NonNull String dccProjectCode,
      @NonNull String outputTsvFilename,
      @NonNull TSVConverter<T> tsvConverter,
      @NonNull Optional<String> optionalCoreConfigFilename,
      @NonNull Optional<String> optionalHdfsConfigFilename ){
    val outputFilename = getOutputFileName(outputDirectory, dccProjectCode, outputTsvFilename);
    val writer = new HdfsFileWriter(outputFilename, optionalCoreConfigFilename, optionalHdfsConfigFilename);
    val isNewFile = true; //always true
    return newTransformer(tsvConverter, writer, isNewFile );
  }

  @SneakyThrows
  private Transformer( final TSVConverter<T> tsvConverter, final Writer writer, final boolean isNewFile){
    this.tsvConverter = tsvConverter;
    this.isNewFile = isNewFile;
    this.writer = writer;
  }

  private static boolean checkIfNewFile(String outputFilename){
    return !Paths.get(outputFilename).toFile().exists();
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
    if(isNewFile){
      writer.write(tsvConverter.toTSVHeader()+"\n");
      isNewFile = false;
    }
    writer.write(tsvConverter.toTSVData(t)+"\n");
  }

  @Override
  public void close() throws IOException {
      writer.close();
  }

}
