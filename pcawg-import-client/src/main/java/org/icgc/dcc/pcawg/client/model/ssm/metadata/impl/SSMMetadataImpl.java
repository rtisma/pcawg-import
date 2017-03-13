package org.icgc.dcc.pcawg.client.model.ssm.metadata.impl;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.icgc.dcc.pcawg.client.model.ssm.NACodes;
import org.icgc.dcc.pcawg.client.model.ssm.metadata.SSMMetadata;

import static lombok.AccessLevel.PRIVATE;

@RequiredArgsConstructor(access = PRIVATE)
public class SSMMetadataImpl implements SSMMetadata {
  private static final String DEFAULT_ASSEMBLY_VERSION = "GRCh37";
  private static final String DEFAULT_PLATFORM = "Illumina HiSeq";
  private static final String DEFAULT_SEQUENCING_STRATEGY = "WGS";

  public static final SSMMetadataImpl newSSMMetadataImpl(String variationCallingAlgorithm,
      String matchedSampleId,
      String analysisId,
      String analyzedSampleId){
    return new SSMMetadataImpl(variationCallingAlgorithm, matchedSampleId, analysisId, analyzedSampleId);
  }

  @NonNull
  private final String variationCallingAlgorithm;

  @NonNull
  @Getter
  private final String matchedSampleId;

  @NonNull
  @Getter
  private final String analysisId;

  @NonNull
  @Getter
  private final String analyzedSampleId;

  @Override
  public String getAssemblyVersion() {
    return DEFAULT_ASSEMBLY_VERSION;
  }

  @Override
  public String getPlatform() {
    return DEFAULT_PLATFORM;
  }

  @Override
  public String getExperimentalProtocol() {
    return NACodes.DATA_VERIFIED_TO_BE_UNKNOWN.toString();
  }

  @Override
  public String getBaseCallingAlgorithm() {
    return NACodes.DATA_VERIFIED_TO_BE_UNKNOWN.toString();
  }

  @Override
  public String getAlignmentAlgorithm(){
    return NACodes.DATA_VERIFIED_TO_BE_UNKNOWN.toString();
  }

  @Override
  public String getVariationCallingAlgorithm() {
    return variationCallingAlgorithm;
  }

  @Override
  public String getOtherAnalysisAlgorithm() {
    return NACodes.DATA_VERIFIED_TO_BE_UNKNOWN.toString();
  }

  @Override
  public String getSequencingStrategy() {
    return DEFAULT_SEQUENCING_STRATEGY;
  }

  @Override
  public String getSeqCoverage() {
    return NACodes.DATA_VERIFIED_TO_BE_UNKNOWN.toString();
  }

  //TODO: need to implement
  @Override
  public String getRawDataRepository() {
    return "NEED_TO_IMPL";
  }

  //TODO: need to implement
  @Override
  public String getRawDataAccession() {
    return "NEED_TO_IMPL";
  }

  //For andy, just a placeholder
  @Override
  public boolean getPcawgFlag() {
    return true;
  }
}
