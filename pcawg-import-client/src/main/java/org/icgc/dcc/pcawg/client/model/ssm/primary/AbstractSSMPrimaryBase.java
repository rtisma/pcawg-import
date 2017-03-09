package org.icgc.dcc.pcawg.client.model.ssm.primary;

import htsjdk.variant.variantcontext.VariantContext;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.icgc.dcc.pcawg.client.core.ProjectMetadataDAO;

import static com.google.common.base.Preconditions.checkArgument;
import static lombok.AccessLevel.PROTECTED;

/**
 * Common implementations for all subclasses of AbstractSSMPrimaryBase
 */
@Getter(PROTECTED)
@RequiredArgsConstructor
public abstract class AbstractSSMPrimaryBase implements SSMPrimary {

  private static final int DEFAULT_STRAND = 1;
  private static final String DEFAULT_VERIFICATION_STATUS = "not tested";

  @NonNull
  private final String aliquotId;

  @NonNull
  private final VariantContext variant;

  @NonNull
  private final ProjectMetadataDAO projectMetadataDAO;

  @Override
  public String getAnalysisId() {
    return projectMetadataDAO.getAnalysisId(aliquotId);
  }

  @Override
  public String getAnalyzedSampleId() {
    return projectMetadataDAO.getAnalyzedSampleId(aliquotId);
  }

  @Override
  public String getChromosome() {
    return variant.getContig();
  }

  @Override
  public int getChromosomeStrand() {
    return DEFAULT_STRAND;
  }

  @Override
  public String getExpressedAllele() {
    return DEFAULT_EMPTY;
  }

  @Override
  public String getQualityScore() {
    return DEFAULT_EMPTY;
  }

  @Override
  public String getProbability() {
    return DEFAULT_EMPTY;
  }

  private int getIntAttribute(String attr){
    val info = variant.getCommonInfo();
    checkArgument(info.hasAttribute(attr));
    return info.getAttributeAsInt(attr, -1);
  }

  private int getAltCount(){
    return getIntAttribute("t_alt_count");
  }

  private int getRefCount(){
    return getIntAttribute("t_ref_count");
  }

  //TODO: this is only for consensus and is baked in for now. Will need CallProcessors that implement specific ways to get the data needed, such as total_read_count, mutant_allele_read_count...etc
  @Override
  public int getTotalReadCount() {
    return getAltCount()+getRefCount();
  }

  @Override
  public int getMutantAlleleReadCount() {
    return getAltCount();
  }

  @Override
  public String getVerificationStatus() {
    return DEFAULT_VERIFICATION_STATUS;
  }

  @Override
  public String getVerificationPlatform() {
    return DEFAULT_EMPTY;
  }

  @Override
  public String getBiologicalValidationStatus() {
    return DEFAULT_EMPTY;
  }

  @Override
  public String getBiologicalValidationPlatform() {
    return DEFAULT_EMPTY;
  }


  @Override
  public String getNote() {
    return DEFAULT_EMPTY;
  }

}

