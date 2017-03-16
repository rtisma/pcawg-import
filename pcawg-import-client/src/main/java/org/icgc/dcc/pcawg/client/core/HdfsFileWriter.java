package org.icgc.dcc.pcawg.client.core;

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
import java.util.Optional;

@Slf4j
public class HdfsFileWriter extends Writer {

  private static final String FS_PARAM_NAME = "fs.defaultFS";
  private static final String HDFS = "hdfs";

  private static final Configuration DEFAULT_CONF = new Configuration();
  {
    DEFAULT_CONF.set(FS_PARAM_NAME, HDFS+":///");
  }

  private final FileSystem hdfs;

  private final Writer internalWriter;

//  private final String hdfsAddress;
//  private final String hdfsPort;

  private static Configuration createConfiguration(Optional<String> optionalCoreConfigFilename, Optional<String> optionalHdfsConfigFilename){
    val conf = new Configuration();
    log.info("CoreConfigFilename: {}", optionalCoreConfigFilename.isPresent() ? optionalCoreConfigFilename.get() : "null");
    log.info("HdfsConfigFilename: {}", optionalHdfsConfigFilename.isPresent() ? optionalHdfsConfigFilename.get() : "null");
    optionalCoreConfigFilename.ifPresent(conf::addResource);
    optionalHdfsConfigFilename.ifPresent(conf::addResource);
    return conf;
  }


  @SneakyThrows
  private static OutputStream createNewOutputStream(Path file, Configuration conf){
    val fs = file.getFileSystem(conf);
    log.info("FileName1: {}", file.toString());
    if( fs.exists(file)){
      log.info("The file [{}] EXISTS! Deleting it ", file.toString() );
      fs.delete(file, true);
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


  @SneakyThrows
  private static OutputStream createNewOutputStream(FileSystem fs, String filename){
    val file = new Path(filename);
    log.info("FileName1: {}", file.toString());
    if( fs.exists(file)){
      log.info("The file [{}] EXISTS! Deleting it ", file.toString() );
      fs.delete(file, true);
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

  @SneakyThrows
  public HdfsFileWriter(FileSystem hdfs, String outputFilename) throws IOException {
    log.info("Connecting to: {}", hdfs.getConf().get(FS_PARAM_NAME));
    log.info("Outputting to: {} ", outputFilename);
    val os = createNewOutputStream(hdfs, outputFilename);
    this.internalWriter = new BufferedWriter(new OutputStreamWriter(os));
    this.hdfs = hdfs;
  }

  @SneakyThrows
  public HdfsFileWriter(String outputFilename, Optional<String> optionalCoreConfigFilename, Optional<String> optionalHdfsConfigFilename) throws IOException {
    val file = new Path(outputFilename);
    val conf = createConfiguration(optionalCoreConfigFilename,optionalHdfsConfigFilename);
    this.hdfs = file.getFileSystem(conf);
    log.info("Connecting to: {}", hdfs.getConf().get(FS_PARAM_NAME));
    log.info("Outputting to: {} ", outputFilename);
    val os = createNewOutputStream(file,conf  );
    this.internalWriter = new BufferedWriter(new OutputStreamWriter(os));
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
    hdfs.close();
  }
}
