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

@Slf4j
public class HdfsFileWriter extends Writer {

  private static final Configuration DEFAULT_CONF = new Configuration();
  private static final String HDFS = "hdfs";
  private static final String FS_PARAM_NAME = "fs.defaultFS";

  private final FileSystem hdfs;

  private final Writer internalWriter;

//  private final String hdfsAddress;
//  private final String hdfsPort;

  @SneakyThrows
  private static OutputStream createNewOutputStream(FileSystem fs, String filename){
    val file = new Path(filename);
    if( fs.exists(file)){
      fs.delete(file, true);
    } else {
      fs.mkdirs(file.getParent());
    }
    return fs.create(file);
  }

  @SneakyThrows
  public HdfsFileWriter(FileSystem hdfs, String outputFilename) throws IOException {
    log.info("Connecting to: {}", hdfs.getConf().get(FS_PARAM_NAME));
    val os = createNewOutputStream(hdfs, outputFilename);
    this.internalWriter = new BufferedWriter(new OutputStreamWriter(os));
    this.hdfs = hdfs;
  }

  @SneakyThrows
  public HdfsFileWriter(String outputFilename) throws IOException{
    log.info("Connecting to: {}", DEFAULT_CONF.get(FS_PARAM_NAME));
    val hdfs = FileSystem.getLocal(DEFAULT_CONF);
//    FileSystem.get(new URI(HDFS+":/"+hdfsAddress+":"+hdfsPort), DEFAULT_CONF);
    val os = createNewOutputStream(hdfs, outputFilename);
    this.internalWriter = new BufferedWriter(new OutputStreamWriter(os));
    this.hdfs = hdfs;
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
