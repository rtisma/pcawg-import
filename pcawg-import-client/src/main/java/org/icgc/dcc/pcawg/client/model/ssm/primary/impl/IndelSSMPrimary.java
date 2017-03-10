package org.icgc.dcc.pcawg.client.model.ssm.primary.impl;

import htsjdk.variant.variantcontext.VariantContext;

public class IndelSSMPrimary extends AbstractSSMPrimaryBase {

  public static final IndelSSMPrimary newIndelSSMPrimary(final VariantContext variant, final String analysisId, final String analyzedSampleId) throws AttributeDoesNotExistException {
    return new IndelSSMPrimary(variant, analysisId, analyzedSampleId);
  }

  private IndelSSMPrimary(final VariantContext variant, final String analysisId, final String analyzedSampleId) throws AttributeDoesNotExistException {
    super(variant, analysisId, analyzedSampleId);
  }

  @Override
  public String getMutationType() {
    /*
    if len(ALT)>len(REF): insertion of <=200bp
    if len(ALT)<len(REF): deletion of <=200bp
     */
//    getVariant().getReference().getBaseString().length()
    return "NEED_TO_IMPL";
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
