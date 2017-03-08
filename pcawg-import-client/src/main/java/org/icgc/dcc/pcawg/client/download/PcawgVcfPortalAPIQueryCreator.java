package org.icgc.dcc.pcawg.client.download;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.icgc.dcc.pcawg.client.core.ObjectNodeConverter;
import org.icgc.dcc.pcawg.client.vcf.CallerTypes;

import static lombok.AccessLevel.PRIVATE;
import static org.icgc.dcc.common.core.json.JsonNodeBuilders.array;
import static org.icgc.dcc.common.core.json.JsonNodeBuilders.object;
import static org.icgc.dcc.pcawg.client.utils.Strings.toStringArray;

@RequiredArgsConstructor(access = PRIVATE)
@Value
@Slf4j
public class PcawgVcfPortalAPIQueryCreator implements ObjectNodeConverter {

  public static final PcawgVcfPortalAPIQueryCreator newPcawgVcfPortalAPIQueryCreator(CallerTypes callerType){
    return new PcawgVcfPortalAPIQueryCreator(callerType);
  }

  @NonNull
  private final CallerTypes callerType;

  @Override
  public ObjectNode toObjectNode(){
    return object()
        .with("file",
            object()
                .with("repoName", createIs("Collaboratory - Toronto"))
                .with("dataType", createIs("SSM"))
                .with("study", createIs("PCAWG"))
                .with("fileFormat", createIs("VCF"))
                .with("software", createIs(toStringArray(callerType.getPortalSoftwareNames())))
                .with("experimentalStrategy", createIs("WGS"))
            )
        .end();
  }


  private ObjectNode createField(String name, String ... values){
    return object()
        .with(name, createIs(values))
        .end();
  }

  private ObjectNode createIs(String ... values){
    return object()
        .with("is",
            array()
              .with(values)
              .end())
        .end();

  }



}
