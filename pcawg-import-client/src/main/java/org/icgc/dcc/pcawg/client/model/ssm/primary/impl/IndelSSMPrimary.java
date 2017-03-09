package org.icgc.dcc.pcawg.client.model.ssm.primary.impl;

import htsjdk.variant.variantcontext.VariantContext;

public class IndelSSMPrimary extends AbstractSSMPrimaryBase {

  public IndelSSMPrimary(VariantContext variant, String analysisId, String analyzedSampleId) {
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
