package org.icgc.dcc.pcawg.client;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.icgc.dcc.pcawg.client.data.ProjectMetadataDAO;
import org.icgc.dcc.pcawg.client.model.ssm.metadata.SSMMetadata;
import org.icgc.dcc.pcawg.client.model.ssm.primary.SSMPrimary;
import org.icgc.dcc.pcawg.client.tsv.TSVConverter;

@RequiredArgsConstructor
public class Transformer {

  @NonNull
  private final String inputVcfFilename;

  @NonNull
  private final String outputTsvFilename;

  @NonNull
  private final TSVConverter<SSMPrimary> ssmPrimaryTSVConverter;

  @NonNull
  private final TSVConverter<SSMMetadata> ssmMetadataTSVConverter;

  @NonNull
  private final ProjectMetadataDAO projectMetadataDAO;

  // open file, and read with htk lib
  // Parse filename
  // extract Mutation type (indel or snv_mnv)
  // stream through VariantContexts
  // for each variantContext ...
  // construct SSMMetadata object
  // if (indel) constuct IndelSMMPrimary object
  // if (snv_mnv) construct SnvMnvSSMPrimary object
  // convert SSMMetadata to local disk
  // convert SSMPrimary to local disk

}
