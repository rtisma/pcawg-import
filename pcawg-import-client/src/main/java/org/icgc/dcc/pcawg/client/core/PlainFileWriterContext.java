package org.icgc.dcc.pcawg.client.core;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import org.icgc.dcc.pcawg.client.vcf.WorkflowTypes;

import static org.icgc.dcc.common.core.util.Joiners.DOT;
import static org.icgc.dcc.common.core.util.Joiners.PATH;

@Builder
@Value
public class PlainFileWriterContext implements FileWriterContext {

  @NonNull private final String outputDirectory;
  @NonNull private final String dccProjectCode;
  @NonNull private final String fileNamePrefix;
  @NonNull private final WorkflowTypes workflowType;
  @NonNull private final String fileExtension;

  private final boolean append;

  @NonNull
  private final String hostname;

  @NonNull
  private final String port;


  public String getOutputTsvFilename(){
    return DOT.join(getFileNamePrefix(), getWorkflowType().getName() , getFileExtension());
  }

  @Override public String getOutputFilename(){
    return PATH.join(getOutputDirectory(), getDccProjectCode(), getOutputTsvFilename());
  }

}


