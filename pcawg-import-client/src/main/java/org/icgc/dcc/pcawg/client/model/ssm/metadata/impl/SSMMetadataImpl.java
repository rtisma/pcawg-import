package org.icgc.dcc.pcawg.client.model.ssm.metadata.impl;

import org.icgc.dcc.pcawg.client.core.ProjectMetadataDAO;
import org.icgc.dcc.pcawg.client.model.ssm.AbstractCommon;
import org.icgc.dcc.pcawg.client.model.ssm.metadata.SSMMetadata;

public class SSMMetadataImpl extends AbstractCommon implements SSMMetadata {
  private static final String DEFAULT_ASSEMBLY_VERSION = "GRCh37";
  private static final String DEFAULT_PLATFORM = "Illumina HiSeq";
  private static final String DEFAULT_SEQUENCING_STRATEGY = "WGS";
  private static final String DEFAULT_EMPTY = "-";

  public SSMMetadataImpl(ProjectMetadataDAO projectMetadataDAO, String aliquotId) {
    super(projectMetadataDAO, aliquotId);
  }

  @Override
  public String getMatchedSampleId() {
    return null;
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
  public String getVariationCallingAlgorithm() {
    throw new IllegalStateException("No implementation for VariantionCalling Algorithm yet");
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
    throw new IllegalStateException("No implementation for Seq Coverage");
  }

  @Override
  public String getRawDataRepository() {
    throw new IllegalStateException("No implementation for Raw Data Repository - us and non us");
  }

  @Override
  public String getRawDataAccession() {
    return null;
  }
}
