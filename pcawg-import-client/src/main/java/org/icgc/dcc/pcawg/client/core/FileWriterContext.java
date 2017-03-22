package org.icgc.dcc.pcawg.client.core;

public interface FileWriterContext {

  String getOutputFilename();

  boolean isAppend();

  String getHostname();

  String getPort();
}
