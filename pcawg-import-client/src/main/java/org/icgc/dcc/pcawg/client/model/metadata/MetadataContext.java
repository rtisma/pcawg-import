package org.icgc.dcc.pcawg.client.model.metadata;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import lombok.val;
import org.icgc.dcc.pcawg.client.model.metadata.file.FileMetaData;
import org.icgc.dcc.pcawg.client.model.metadata.project.ProjectMetadata;

import static org.icgc.dcc.common.core.util.Joiners.UNDERSCORE;

@Builder
@Value
public class MetadataContext {

  @NonNull
  private final FileMetaData fileMetaData;

  @NonNull
  private final ProjectMetadata projectMetadata;

  public String getAnalysisId(){
    val dccProjectCode = projectMetadata.getDccProjectCode();
    val workflow = fileMetaData.getVcfFilenameParser().getObjectId();
    val dataType = fileMetaData.getVcfFilenameParser().getSubMutationType();
    return UNDERSCORE.join(dccProjectCode, workflow, dataType);
  }


}
