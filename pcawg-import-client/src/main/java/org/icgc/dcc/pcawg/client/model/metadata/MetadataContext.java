package org.icgc.dcc.pcawg.client.model.metadata;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import org.icgc.dcc.pcawg.client.model.metadata.file.PortalMetadata;
import org.icgc.dcc.pcawg.client.model.metadata.project.SampleMetadata;

@Builder
@Value
public class MetadataContext {

  @NonNull
  private final PortalMetadata portalMetadata;

  @NonNull
  private final SampleMetadata sampleMetadata;

}
