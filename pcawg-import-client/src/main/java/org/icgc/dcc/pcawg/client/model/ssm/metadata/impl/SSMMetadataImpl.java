package org.icgc.dcc.pcawg.client.model.ssm.metadata.impl;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.icgc.dcc.pcawg.client.core.ProjectMetadataDAO;
import org.icgc.dcc.pcawg.client.model.ssm.metadata.SSMMetadata;
import org.icgc.dcc.pcawg.client.vcf.CallerTypes;

@RequiredArgsConstructor
public class SSMMetadataImpl implements SSMMetadata {
  private static final String DEFAULT_ASSEMBLY_VERSION = "GRCh37";
  private static final String DEFAULT_PLATFORM = "Illumina HiSeq";
  private static final String DEFAULT_SEQUENCING_STRATEGY = "WGS";

  @NonNull
  private final String aliquotId;

  @NonNull
  private final CallerTypes callerType;

  @NonNull
  private final ProjectMetadataDAO projectMetadataDAO;


  public String getMatchedSampleId() {
    return projectMetadataDAO.getMatchedSampleId(aliquotId);
  }

  public String getAnalysisId() {
    return projectMetadataDAO.getAnalysisId(aliquotId);
  }

  public String getAnalyzedSampleId() {
    return projectMetadataDAO.getAnalyzedSampleId(aliquotId);
  }


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
    return DEFAULT_EMPTY;
  }

  @Override
  public String getBaseCallingAlgorithm() {
    return DEFAULT_EMPTY;
  }

  @Override
  public String getAlignmentAlgorithm(){
    return DEFAULT_EMPTY;
  }

  @Override
  public CallerTypes getVariationCallingAlgorithm() {
    return callerType;
  }

  @Override
  public String getOtherAnalysisAlgorithm() {
    return DEFAULT_EMPTY;
  }

  @Override
  public String getSequencingStrategy() {
    return DEFAULT_SEQUENCING_STRATEGY;
  }

  @Override
  public String getSeqCoverage() {
    return DEFAULT_EMPTY;
  }

  @Override
  public String getRawDataRepository() {
    throw new IllegalStateException("No implementation for Raw Data Repository - us and non us");
  }

  @Override
  public String getRawDataAccession() {
    throw new IllegalStateException("No implementation for Raw Data Accession");
  }
}
