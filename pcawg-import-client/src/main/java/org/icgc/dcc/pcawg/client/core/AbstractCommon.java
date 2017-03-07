package org.icgc.dcc.pcawg.client.core;

import lombok.NonNull;
import lombok.Value;
import lombok.experimental.NonFinal;

@Value
@NonFinal
public abstract class AbstractCommon implements Common {

  @NonNull
  private final ProjectMetadataDAO projectMetadataDAO;

  @NonNull
  private final String aliquotId;

  @Override
  public String getAnalysisId() {
    return projectMetadataDAO.getAnalysisId(aliquotId);
  }

  @Override
  public String getAnalyzedSampleId() {
    return projectMetadataDAO.getAnalyzedSampleId(aliquotId);
  }

}
