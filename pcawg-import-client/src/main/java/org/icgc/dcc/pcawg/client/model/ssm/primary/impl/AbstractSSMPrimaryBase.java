package org.icgc.dcc.pcawg.client.model.ssm.primary.impl;

import htsjdk.variant.variantcontext.VariantContext;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.icgc.dcc.pcawg.client.model.ssm.NACodes;
import org.icgc.dcc.pcawg.client.model.ssm.primary.SSMPrimary;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkState;
import static lombok.AccessLevel.PROTECTED;

/**
 * Common implementations for all subclasses of AbstractSSMPrimaryBase
 */
@Slf4j
public abstract class AbstractSSMPrimaryBase implements SSMPrimary {

  private static final int DEFAULT_STRAND = 1;
  private static final String DEFAULT_VERIFICATION_STATUS = "not tested";
  private static final String T_REF_COUNT = "t_ref_count";
  private static final String T_ALT_COUNT = "t_alt_count";



  @NonNull
  @Getter(PROTECTED)
  private final VariantContext variant;

  @NonNull
  @Getter
  private final String analysisId;

  @NonNull
  @Getter
  private final String analyzedSampleId;

  public AbstractSSMPrimaryBase(VariantContext variant, String analysisId, String analyzedSampleId) {
    this.variant = variant;
    this.analysisId = analysisId;
    this.analyzedSampleId = analyzedSampleId;
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
    return NACodes.DATA_VERIFIED_TO_BE_UNKNOWN.toString();
  }

  @Override
  public String getQualityScore() {
    return NACodes.DATA_VERIFIED_TO_BE_UNKNOWN.toString();
  }

  @Override
  public String getProbability() {
    return NACodes.DATA_VERIFIED_TO_BE_UNKNOWN.toString();
  }

  private Optional<Integer> getIntAttribute(String attr) {
    val info = variant.getCommonInfo();
    if (!info.hasAttribute(attr)){
      return Optional.empty();
    }
    return Optional.of(info.getAttributeAsInt(attr, -1));
  }

  private Optional<Integer> getAltCount() {
    return getIntAttribute(T_ALT_COUNT);
  }

  private Optional<Integer> getRefCount() {
    return getIntAttribute(T_REF_COUNT);
  }

  //TODO: this is only for consensus and is baked in for now. Will need CallProcessors that implement specific ways to get the data needed, such as total_read_count, mutant_allele_read_count...etc
  @Override
  public int getTotalReadCount() {
    val altCount = getAltCount();
    val refCount = getRefCount();
    if (altCount.isPresent() && refCount.isPresent()){
      return altCount.get()+refCount.get();
    } else {
      return NACodes.CORRUPTED_DATA.toInt();
    }
  }

  @Override
  public int getMutantAlleleReadCount() {
    val altCount = getAltCount();
    if (altCount.isPresent()){
      return altCount.get();
    } else {
      return NACodes.CORRUPTED_DATA.toInt();
    }
  }

  @Override
  public String getVerificationStatus() {
    return DEFAULT_VERIFICATION_STATUS;
  }

  @Override
  public String getVerificationPlatform() {
    return NACodes.DATA_VERIFIED_TO_BE_UNKNOWN.toString();
  }

  @Override
  public String getBiologicalValidationStatus() {
    return NACodes.DATA_VERIFIED_TO_BE_UNKNOWN.toString();
  }

  @Override
  public String getBiologicalValidationPlatform() {
    return NACodes.DATA_VERIFIED_TO_BE_UNKNOWN.toString();
  }

  @Override
  public String getNote() {
    return NACodes.DATA_VERIFIED_TO_BE_UNKNOWN.toString();
  }

  //For Andy, just a placeholder
  @Override
  public boolean getPcawgFlag() {
    return true;
  }

  protected int getReferenceAlleleLength(){
    return variant.getReference().length();
  }

  protected String getReferenceAlleleString(){
    return variant.getReference().getBaseString();
  }

  /**
   * TODO: Assumption is there there is ONLY ONE alternative allele.
   * @throws IllegalStateException for when there is more than one alternative allele
   */
  protected int getAlternativeAlleleLength(){
    checkState(variant.getAlternateAlleles().size() == 1, "There is more than one alternative allele");
    return variant.getAlternateAllele(0).length();
  }

  /**
   * TODO: Assumption is there there is ONLY ONE alternative allele.
   * @throws IllegalStateException for when there is more than one alternative allele
   */
  protected String getAlternativeAlleleString() {
    checkState(getVariant().getAlternateAlleles().size() == 1, "There is more than one alternative allele");
    return getVariant().getAlternateAllele(0).getBaseString(); //get first alternative allele
  }
}

