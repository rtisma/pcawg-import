package org.icgc.dcc.pcawg.client.model.metadata;

import com.google.common.collect.ImmutableList;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import lombok.val;
import org.icgc.dcc.pcawg.client.data.ProjectMetadataDAO;
import org.icgc.dcc.pcawg.client.model.metadata.file.FileMetaData;
import org.icgc.dcc.pcawg.client.model.metadata.project.ProjectMetadata;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;

@Builder
@Value
public class MetadataContext {

  public static List<MetadataContext> buildMetadataContextCollection(List<FileMetaData> fileMetaDatas, ProjectMetadataDAO projectMetadataDAO){
    val builder = ImmutableList.<MetadataContext>builder();
    for (val fileMetaData : fileMetaDatas ){
      val aliquotId = fileMetaData.getVcfFilenameParser().getObjectId();
      val projectMetadata = projectMetadataDAO.getProjectMetadataByAliquotId(aliquotId);
      builder.add(MetadataContext.builder()
          .fileMetaData(fileMetaData)
          .projectMetadata(projectMetadata)
          .build());

    }
    return builder.build();
  }

  public static Map<String, List<MetadataContext>> groupByDccProjectCode(List<MetadataContext> metadataContexts){
    return metadataContexts
        .stream()
        .collect(groupingBy(x -> x.getProjectMetadata().getDccProjectCode()));
  }

  @NonNull
  private final FileMetaData fileMetaData;

  @NonNull
  private final ProjectMetadata projectMetadata;


}
