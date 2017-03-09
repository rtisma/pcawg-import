package org.icgc.dcc.pcawg.client.model.ssm.metadata.impl;

import com.google.common.collect.Maps;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.icgc.dcc.pcawg.client.core.ProjectMetadataDAO;
import org.icgc.dcc.pcawg.client.tsv.SSMMetadataFieldValue;
import org.icgc.dcc.pcawg.client.model.ssm.metadata.SSMMetadata;
import org.icgc.dcc.pcawg.client.model.ssm.metadata.SSMMetadataFieldMapping;
import org.icgc.dcc.pcawg.client.vcf.CallerTypes;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static org.icgc.dcc.pcawg.client.tsv.SSMMetadataFieldValue.newSSMMetadataFieldValue;
import static org.icgc.dcc.pcawg.client.model.ssm.metadata.SSMMetadataFieldMapping.ANALYSIS_ID;
import static org.icgc.dcc.pcawg.client.model.ssm.metadata.SSMMetadataFieldMapping.ANALYZED_SAMPLE_ID;
import static org.icgc.dcc.pcawg.client.model.ssm.metadata.SSMMetadataFieldMapping.MATCHED_SAMPLE_ID;

@RequiredArgsConstructor
public class SSMMetadataImpl implements SSMMetadata {
  private static final String DEFAULT_ASSEMBLY_VERSION = "GRCh37";
  private static final String DEFAULT_PLATFORM = "Illumina HiSeq";
  private static final String DEFAULT_SEQUENCING_STRATEGY = "WGS";

  private static final TreeMap<SSMMetaDataFields, String> DEFAULT_DATA = Maps.newTreeMap()
      .put(SSMMetaDataFields.A,  )

  @NonNull
  private final String aliquotId;

  @NonNull
  private final CallerTypes callerType;

  @NonNull
  private final ProjectMetadataDAO projectMetadataDAO;

  private final Map<SSMMetadataFieldMapping, SSMMetadataFieldValue> data = Maps.newEnumMap(SSMMetadataFieldMapping.class);

  public List<SSMMetadataFieldValue> getValues(){
    addValues(
        newSSMMetadataFieldValue(MATCHED_SAMPLE_ID, getMatchedSampleId()),
        newSSMMetadataFieldValue(ANALYSIS_ID, getAnalysisId()),
        newSSMMetadataFieldValue(ANALYZED_SAMPLE_ID, getAnalyzedSampleId()),

    );



  }

  private void addValue(SSMMetadataFieldValue value ){
    data.put(value.getField(), value);
  }

  private void addValues(SSMMetadataFieldValue... values ){
    for (val value : values){
      addValue(value);
    }
  }

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
