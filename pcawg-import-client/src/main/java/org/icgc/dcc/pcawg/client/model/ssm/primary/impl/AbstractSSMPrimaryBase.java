package org.icgc.dcc.pcawg.client.model.ssm.primary.impl;

import htsjdk.variant.variantcontext.VariantContext;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.icgc.dcc.pcawg.client.model.ssm.NACodes;
import org.icgc.dcc.pcawg.client.model.ssm.primary.SSMPrimary;

import java.util.Optional;

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

  private Optional<Integer> getIntAttribute(String attr) {
    val info = variant.getCommonInfo();
    if (!info.hasAttribute(attr)){
      return Optional.empty();
    }
    return Optional.of(info.getAttributeAsInt(attr, -1));
  }

  private Optional<Integer> getAltCount() {
    return getIntAttribute("t_alt_count");
  }

  private Optional<Integer> getRefCount() {
    return getIntAttribute("t_ref_count");
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

  //For Andy, just a placeholder
  @Override
  public boolean getPcawgFlag() {
    return true;
  }

  protected int getReferanceAlleleLength(){
    return variant.getReference().length();
  }

  /**
   * TODO: Assume only ONE alternative allele for PCAWG data
   */
  protected int getAlternativeAlleleLength(){
    return variant.getAlternateAllele(0).length();
  }
}

