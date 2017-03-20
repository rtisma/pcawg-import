package org.icgc.dcc.pcawg.client.core;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.Setter;

import static org.icgc.dcc.common.core.util.Joiners.PATH;

@Builder
@Data
@Setter(AccessLevel.PROTECTED)
public class FileWriterContext {

  @NonNull private final String outputDirectory;
  @NonNull private final String dccProjectCode;
  @NonNull private final String outputTsvFilename;
  private final boolean append;

  @NonNull
  private final String hostname;

  @NonNull
  private final String port;


  public String getOutputFilename(){
    return PATH.join(outputDirectory, dccProjectCode, outputTsvFilename);
  }

}


