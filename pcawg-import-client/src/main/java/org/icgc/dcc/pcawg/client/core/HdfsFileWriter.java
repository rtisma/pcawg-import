package org.icgc.dcc.pcawg.client.core;

import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

@Slf4j
public class HdfsFileWriter extends Writer {

  private static final String FS_PARAM_NAME = "fs.defaultFS";
  private static final String HDFS = "hdfs";

  private final Writer internalWriter;

  @Getter
  private final boolean append;

  private static String createUrl(String fileSystemName, String hostname, String port){
    return fileSystemName+"://"+hostname+":"+port;
  }

  private static Configuration createConfiguration(String hostname, String port){
    val conf = new Configuration();
    val baseUrl = createUrl(HDFS, hostname, port);
    conf.set(FS_PARAM_NAME, baseUrl);
    return conf;
  }

  @SneakyThrows
  private static FileSystem createFileSystem(String hostname, String port){
    val conf = createConfiguration(hostname, port);
    return FileSystem.get(conf);
  }

  //TODO: clean this up of all logs
  @SneakyThrows
  private static OutputStream createNewOutputStream(FileSystem fs, Path file, boolean append){
    log.info("FileName: {}", file.toString());
    if( fs.exists(file)){
      if (!append){
        log.info("The file [{}] EXISTS! Deleting it ", file.toString() );
        fs.delete(file, true);
      } else {
        log.info("The file [{}] EXISTS! Appending to it ", file.toString() );
      }
    } else {
      log.info("The file [{}] DNE ", file.toUri().toString() );
      val parent = file.getParent();
      if (! fs.exists(parent)){
        log.info("The parent dir [{}] doesnt exist, will create...  ", parent.toUri().toString() );
        fs.mkdirs(parent);
      }
    }
    return fs.create(file);
  }

  public HdfsFileWriter(String hostname, String port, String outputFilename, boolean append) throws IOException {
    val file = new Path(outputFilename);
    val hdfs = createFileSystem(hostname,port);
    log.info("Connecting to: {}", hdfs.getConf().get(FS_PARAM_NAME));
    log.info("Outputting to: {} ", outputFilename);
    val os = createNewOutputStream(hdfs, file, append);
    this.internalWriter = new BufferedWriter(new OutputStreamWriter(os));
    this.append = append;
  }


  @Override
  public void write(char[] cbuf, int off, int len) throws IOException {
    internalWriter.write(cbuf,off, len);
  }

  @Override
  public void flush() throws IOException {
    internalWriter.flush();
  }

  @Override
  public void close() throws IOException {
    internalWriter.close();
  }
}
