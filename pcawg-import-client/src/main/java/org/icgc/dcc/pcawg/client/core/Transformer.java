package org.icgc.dcc.pcawg.client.core;

import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.val;
import org.icgc.dcc.pcawg.client.tsv.TSVConverter;

import java.io.Closeable;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.icgc.dcc.common.core.util.Joiners.PATH;

public class Transformer<T> implements Closeable{

  private static final boolean APPEND_MODE_ENABLED = true;

  private final TSVConverter<T> tsvConverter;

  private final FileWriter writer;

  private boolean isNewFile;

  private static String getOutputFileName(String outputDirectory, String dccProjectCode, String outputTsvFilename){
    return PATH.join(outputDirectory, dccProjectCode, outputTsvFilename);
  }

  @SneakyThrows
  public Transformer(
      @NonNull String outputDirectory,
      @NonNull String dccProjectCode,
      @NonNull String outputTsvFilename,
      @NonNull TSVConverter<T> tsvConverter ){
    val outputFilename = getOutputFileName(outputDirectory, dccProjectCode, outputTsvFilename);
    initParentDir(outputFilename);
    this.tsvConverter = tsvConverter;
    this.isNewFile = isNewFile(outputFilename);
    this.writer = new FileWriter(outputFilename, APPEND_MODE_ENABLED);
  }

  private static boolean isNewFile(String outputFilename){
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
  public static void initParentDir(@NonNull String filename) {
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
      writer.flush();
      writer.close();
  }

}
