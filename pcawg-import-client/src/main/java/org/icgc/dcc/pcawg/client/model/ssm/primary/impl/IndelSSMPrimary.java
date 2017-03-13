package org.icgc.dcc.pcawg.client.model.ssm.primary.impl;

import htsjdk.variant.variantcontext.VariantContext;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.icgc.dcc.pcawg.client.model.ssm.NACodes;
import org.icgc.dcc.pcawg.client.vcf.MutationTypes;

@Slf4j
public class IndelSSMPrimary extends AbstractSSMPrimaryBase {


  public static final IndelSSMPrimary newIndelSSMPrimary(final VariantContext variant, final String analysisId, final String analyzedSampleId)  {
    return new IndelSSMPrimary(variant, analysisId, analyzedSampleId);
  }

  public IndelSSMPrimary(VariantContext variant, String analysisId, String analyzedSampleId) {
    super(variant, analysisId, analyzedSampleId);
  }

  @Override
  public String getMutationType()  {
    val refLength = getReferanceAlleleLength();
    val altLength = getAlternativeAlleleLength();
    if(altLength > refLength){
      return MutationTypes.INSERTION_LTE_200BP.toString();
    } else if(altLength <  refLength){
      return MutationTypes.DELETION_LTE_200BP.toString();
    } else {
      return NACodes.DATA_VERIFIED_TO_BE_UNKNOWN.toString();
    }
  }

  @Override
  public int getChromosomeStart() {
    return getVariant().getStart()+1;
  }

  @Override
  public int getChromosomeEnd() {
    return 0;
  }

  @Override
  public String getReferenceGenomeAllele() {
    return "NEED_TO_IMPL";
  }

  @Override
  public String getControlGenotype() {
    return "NEED_TO_IMPL";
  }

  @Override
  public String getMutatedFromAllele() {
    return "NEED_TO_IMPL";
  }

  @Override
  public String getTumorGenotype() {
    return "NEED_TO_IMPL";
  }

  @Override
  public String getMutatedToAllele() {
    return "NEED_TO_IMPL";
  }

}
