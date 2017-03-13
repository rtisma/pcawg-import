package org.icgc.dcc.pcawg.client.model.metadata.project;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import static org.icgc.dcc.common.core.util.Joiners.UNDERSCORE;

@Value
@Builder
public class ProjectMetadata {

  @NonNull
  private final String aliquotId;

  @NonNull
  private final String analyzedSampleId;

  @NonNull
  private final String dccProjectCode;

  @NonNull
  private final String matchedSampleId;


  public String getAnalysisId(String workflow, String dataType){
    return UNDERSCORE.join(dccProjectCode, workflow,dataType);
  }

}
