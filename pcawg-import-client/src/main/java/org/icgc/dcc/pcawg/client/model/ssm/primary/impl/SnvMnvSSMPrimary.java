package org.icgc.dcc.pcawg.client.model.ssm.primary.impl;

import htsjdk.variant.variantcontext.VariantContext;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.icgc.dcc.pcawg.client.model.ssm.NACodes;
import org.icgc.dcc.pcawg.client.vcf.MutationTypes;

@Slf4j
public class SnvMnvSSMPrimary extends AbstractSSMPrimaryBase {


  public static final SnvMnvSSMPrimary newSnvMnvSSMPrimary(final VariantContext variant, final String analysisId, final String analyzedSampleId)  {
    return new SnvMnvSSMPrimary(variant, analysisId, analyzedSampleId);
  }

  private final MutationTypes mutationType;

  public SnvMnvSSMPrimary(VariantContext variant, String analysisId, String analyzedSampleId) {
    super(variant, analysisId, analyzedSampleId);
    this.mutationType = calcMutationType();
  }

  private MutationTypes calcMutationType(){
    val refLength = getReferenceAlleleLength();
    if(refLength == 1){
      return MutationTypes.SINGLE_BASE_SUBSTITUTION;
    } else if(refLength > 1){
      return MutationTypes.MULTIPLE_BASE_SUBSTITUTION;
    } else {
      return MutationTypes.UNKNOWN;
    }
  }

  @Override
  public String getMutationType()  {
    if (mutationType == MutationTypes.UNKNOWN){
      return NACodes.CORRUPTED_DATA.toString();
    } else {
      return mutationType.toString();
    }
  }

  @Override
  public int getChromosomeStart() {
    return getVariant().getStart();
  }

  @Override
  public int getChromosomeEnd() {
    return getVariant().getStart() + getReferenceAlleleLength() - 1;
  }

  @Override
  public String getReferenceGenomeAllele() {
    return getReferenceAlleleString();
  }

  @Override
  public String getControlGenotype() {
    return joinAlleles(getReferenceAlleleString(),getReferenceGenomeAllele());
  }

  @Override
  public String getMutatedFromAllele() {
    return getReferenceAlleleString();
  }

  /**
   * TODO: Assumption is there there is ONLY ONE alternative allele.
   * @throws IllegalStateException for when there is more than one alternative allele
   */
  @Override
  public String getTumorGenotype() {
    return joinAlleles(getReferenceAlleleString(),getAlternativeAlleleString());
  }


  /**
   * TODO: Assumption is there there is ONLY ONE alternative allele.
   * @throws IllegalStateException for when there is more than one alternative allele
   */
  @Override
  public String getMutatedToAllele() {
    return getAlternativeAlleleString();
  }

}
