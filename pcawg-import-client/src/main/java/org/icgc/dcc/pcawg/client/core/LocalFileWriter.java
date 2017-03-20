package org.icgc.dcc.pcawg.client.core;

import lombok.Getter;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.val;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Extends FileWriter to create all parent directories first, before create Writer object
 */
public class LocalFileWriter extends FileWriter {

  public static LocalFileWriter newLocalFileWriter(String filename, boolean append, boolean createNonExistentDirectories) throws IOException{
    val fileAlreadyExists= Paths.get(filename).toFile().exists();
    return new LocalFileWriter(filename, append, createNonExistentDirectories, fileAlreadyExists);
  }

  @Getter
  private final String filename;

  @Getter
  private final boolean append;

  @Getter
  private final boolean createNonExistentDirectories;

  @Getter
  private final boolean fileExistedPreviously;

  private LocalFileWriter(@NonNull String filename, final boolean append,
      final boolean createNonExistentDirectories, final boolean fileExistedPreviously) throws IOException {
    super(createParentDirectoriesFirst(filename, createNonExistentDirectories), append);
    this.filename = filename;
    this.append = append;
    this.fileExistedPreviously = fileExistedPreviously;
    this.createNonExistentDirectories = createNonExistentDirectories;
  }

  @SneakyThrows
  private static String createParentDirectoriesFirst(String filename, boolean createNonExistentDirectories){

    if(createNonExistentDirectories){
      val filePath = Paths.get(filename);
      val parent = filePath.toFile().getParentFile();
      val parentPath = Paths.get(parent.toURI());
      if (! parent.exists()){
        Files.createDirectories(parentPath);
      }
    }
    return filename;
  }
}
