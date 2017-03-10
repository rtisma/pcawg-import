package org.icgc.dcc.pcawg.client.download;

import com.google.common.collect.ImmutableList;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import lombok.experimental.NonFinal;
import lombok.val;
import org.icgc.dcc.pcawg.client.data.ProjectMetadataDAO;
import org.icgc.dcc.pcawg.client.model.metadata.MetadataContext;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.stream.Collectors.groupingBy;
import static org.icgc.dcc.pcawg.client.model.metadata.file.FileMetaData.buildFileMetaData;

@Value
@Builder
public class MetadataContainer {

  @NonNull
  private final PortalNew portal;

  @NonNull
  private final ProjectMetadataDAO projectMetadataDAO;

  @NonFinal
  private List<MetadataContext> metadataContextList = null;

  @NonFinal
  private Map<String, List<MetadataContext>> dccProjectCodeMap = null;

  //Lazy loading
  public List<MetadataContext> getMetadataContexts(){
    if(metadataContextList == null){
      val builder = ImmutableList.<MetadataContext>builder();
      for (val fileMeta : portal.getFileMetas()){
        val fileMetaData = buildFileMetaData(fileMeta);
        val aliquotId = fileMetaData.getVcfFilenameParser().getObjectId();
        val projectMetadata = projectMetadataDAO.getProjectMetadataByAliquotId(aliquotId);
        builder.add(MetadataContext.builder()
            .projectMetadata(projectMetadata)
            .fileMetaData(fileMetaData)
            .build());
      }
      metadataContextList = builder.build();
      dccProjectCodeMap = groupByDccProjectCode(metadataContextList);
    }
    return metadataContextList;
  }

  public Set<String> getDccProjectCodes(){
    return dccProjectCodeMap.keySet();
  }

  public List<MetadataContext> getMetadataContextsForDccProjectCode(String dccProjectCode){
    checkArgument(dccProjectCodeMap.containsKey(dccProjectCode), "The dccProjectCode [%s] does not exist", dccProjectCode);
    return dccProjectCodeMap.get(dccProjectCode);
  }

  public static Map<String, List<MetadataContext>> groupByDccProjectCode(List<MetadataContext> metadataContexts){
    return metadataContexts
        .stream()
        .collect(groupingBy(x -> x.getProjectMetadata().getDccProjectCode()));
  }

}
