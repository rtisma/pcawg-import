package org.icgc.dcc.pcawg.client.download;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.NonNull;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.icgc.dcc.common.core.util.stream.Streams;
import org.icgc.dcc.pcawg.client.vcf.CallerTypes;

import java.util.function.Function;

import static org.icgc.dcc.common.core.json.JsonNodeBuilders.array;
import static org.icgc.dcc.common.core.json.JsonNodeBuilders.object;

@Value
@Slf4j
public class PcawgVcfPortalAPIQueryCreator {

  public static final PcawgVcfPortalAPIQueryCreator newPcawgVcfPortalAPIQueryCreator(CallerTypes callerType){
    return new PcawgVcfPortalAPIQueryCreator(callerType);
  }

  @NonNull
  private final CallerTypes callerType;

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

  private static <T> String[] toStringArray(final Iterable<T> objects, Function<T, ? extends String> mapping){
    return Streams.stream(objects)
        .map(mapping)
        .toArray(String[]::new);
  }

  private static <T> String[] toStringArray(final Iterable<T> objects){
    return toStringArray(objects, Object::toString);
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
