package org.icgc.dcc.pcawg.client.tsv.converters;

import com.google.common.collect.ImmutableList;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.icgc.dcc.pcawg.client.model.ssm.metadata.SSMMetadata;
import org.icgc.dcc.pcawg.client.tsv.SSMMetadataFieldValue;

import java.util.List;

import static org.icgc.dcc.pcawg.client.model.ssm.metadata.SSMMetadataFieldMapping.ALIGNMENT_ALGORITHM;
import static org.icgc.dcc.pcawg.client.model.ssm.metadata.SSMMetadataFieldMapping.ANALYSIS_ID;
import static org.icgc.dcc.pcawg.client.model.ssm.metadata.SSMMetadataFieldMapping.ANALYZED_SAMPLE_ID;
import static org.icgc.dcc.pcawg.client.model.ssm.metadata.SSMMetadataFieldMapping.ASSEMBLY_VERSION;
import static org.icgc.dcc.pcawg.client.model.ssm.metadata.SSMMetadataFieldMapping.BASE_CALLING_ALGORITHM;
import static org.icgc.dcc.pcawg.client.model.ssm.metadata.SSMMetadataFieldMapping.EXPERIMENTAL_PROTOCOL;
import static org.icgc.dcc.pcawg.client.model.ssm.metadata.SSMMetadataFieldMapping.MATCHED_SAMPLE_ID;
import static org.icgc.dcc.pcawg.client.model.ssm.metadata.SSMMetadataFieldMapping.OTHER_ANALYSIS_ALGORITHM;
import static org.icgc.dcc.pcawg.client.model.ssm.metadata.SSMMetadataFieldMapping.PLATFORM;
import static org.icgc.dcc.pcawg.client.model.ssm.metadata.SSMMetadataFieldMapping.RAW_DATA_ACCESSION;
import static org.icgc.dcc.pcawg.client.model.ssm.metadata.SSMMetadataFieldMapping.RAW_DATA_REPOSITORY;
import static org.icgc.dcc.pcawg.client.model.ssm.metadata.SSMMetadataFieldMapping.SEQUENCING_STRATEGY;
import static org.icgc.dcc.pcawg.client.model.ssm.metadata.SSMMetadataFieldMapping.SEQ_COVERAGE;
import static org.icgc.dcc.pcawg.client.model.ssm.metadata.SSMMetadataFieldMapping.VARIATION_CALLING_ALGORITHM;
import static org.icgc.dcc.pcawg.client.tsv.SSMMetadataFieldValue.newSSMMetadataFieldValue;

@RequiredArgsConstructor
public class SSMMetadataTSVConverter extends AbstractTSVConverterTemplate<SSMMetadataFieldValue> {

  @NonNull
  private final SSMMetadata data;

  @Override
  public List<SSMMetadataFieldValue> getFieldValueList() {
    return ImmutableList.of(
      newSSMMetadataFieldValue(ANALYSIS_ID, data.getAnalysisId()),
      newSSMMetadataFieldValue(ANALYZED_SAMPLE_ID, data.getAnalyzedSampleId()),
      newSSMMetadataFieldValue(MATCHED_SAMPLE_ID, data.getMatchedSampleId()),
      newSSMMetadataFieldValue(ASSEMBLY_VERSION, data.getAssemblyVersion()),
      newSSMMetadataFieldValue(PLATFORM, data.getPlatform()),
      newSSMMetadataFieldValue(EXPERIMENTAL_PROTOCOL, data.getExperimentalProtocol()),
      newSSMMetadataFieldValue(BASE_CALLING_ALGORITHM, data.getBaseCallingAlgorithm()),
      newSSMMetadataFieldValue(ALIGNMENT_ALGORITHM, data.getAlignmentAlgorithm()),
      newSSMMetadataFieldValue(VARIATION_CALLING_ALGORITHM, data.getVariationCallingAlgorithm()),
      newSSMMetadataFieldValue(OTHER_ANALYSIS_ALGORITHM, data.getOtherAnalysisAlgorithm()),
      newSSMMetadataFieldValue(SEQUENCING_STRATEGY, data.getSequencingStrategy()),
      newSSMMetadataFieldValue(SEQ_COVERAGE, data.getSeqCoverage()),
      newSSMMetadataFieldValue(RAW_DATA_REPOSITORY, data.getRawDataRepository()),
      newSSMMetadataFieldValue(RAW_DATA_ACCESSION, data.getRawDataAccession())
    );

  }

}
