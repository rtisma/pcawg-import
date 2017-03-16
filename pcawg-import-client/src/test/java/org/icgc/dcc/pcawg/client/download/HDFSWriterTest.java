package org.icgc.dcc.pcawg.client.download;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.icgc.dcc.pcawg.client.config.ClientProperties;
import org.icgc.dcc.pcawg.client.core.HdfsFileWriter;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.util.Optional;

@Slf4j
public class HDFSWriterTest {

  @Ignore
  @Test
  public void testWrite() throws IOException{
    val start = "hdfs://"+ClientProperties.HDFS_ADDRESS+":"+ClientProperties.HDFS_PORT;
    val filename = "/hdfs/tmp/rob_test.txt";
    val outputFilename = start+filename;
    System.out.println("outputFilename: "+outputFilename);
    val writer = new HdfsFileWriter(outputFilename, Optional.empty(), Optional.empty());
      System.out.println("writing");
      writer.write("hello this is rob");
  }

}
