package org.icgc.dcc.pcawg.client.model.ssm.primary.impl;

import htsjdk.variant.variantcontext.VariantContext;

public class IndelSSMPrimary extends AbstractSSMPrimaryBase {

  public static final IndelSSMPrimary newIndelSSMPrimary(final VariantContext variant, final String analysisId, final String analyzedSampleId) {
    return new IndelSSMPrimary(variant, analysisId, analyzedSampleId);
  }

  private IndelSSMPrimary(final VariantContext variant, final String analysisId, final String analyzedSampleId) {
    super(variant, analysisId, analyzedSampleId);
  }

  @Override
  public String getMutationType() {
    /*
    if len(ALT)>len(REF): insertion of <=200bp
    if len(ALT)<len(REF): deletion of <=200bp
     */
    return null;
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
    return null;
  }

  @Override
  public String getControlGenotype() {
    return null;
  }

  @Override
  public String getMutatedFromAllele() {
    return null;
  }

  @Override
  public String getTumorGenotype() {
    return null;
  }

  @Override
  public String getMutatedToAllele() {
    return null;
  }
}
