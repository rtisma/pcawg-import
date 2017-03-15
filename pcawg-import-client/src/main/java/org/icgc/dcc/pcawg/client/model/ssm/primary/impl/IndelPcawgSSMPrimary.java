package org.icgc.dcc.pcawg.client.model.ssm.primary.impl;

import htsjdk.variant.variantcontext.VariantContext;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.icgc.dcc.pcawg.client.model.NACodes;
import org.icgc.dcc.pcawg.client.vcf.MutationTypes;

@Slf4j
public class IndelPcawgSSMPrimary extends AbstractPcawgSSMPrimaryBase {


  public static final IndelPcawgSSMPrimary newIndelSSMPrimary(final VariantContext variant, final String analysisId, final String analyzedSampleId)  {
    return new IndelPcawgSSMPrimary(variant, analysisId, analyzedSampleId);
  }

  private final MutationTypes mutationType;

  public IndelPcawgSSMPrimary(VariantContext variant, String analysisId, String analyzedSampleId) {
    super(variant, analysisId, analyzedSampleId);
    this.mutationType = calcMutationType();
  }


  @Override public int hashCode() {
    return getMutationType() != null ? getMutationType().hashCode() : 0;
  }

  private MutationTypes calcMutationType(){
    val refLength = getReferenceAlleleLength();
    val altLength = getAlternativeAlleleLength();
    if(altLength >refLength){
      return MutationTypes.INSERTION_LTE_200BP;
    } else if(altLength < refLength){
      return MutationTypes.DELETION_LTE_200BP;
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
    return getVariant().getStart()+1;
  }

  @Override
  public int getChromosomeEnd() {
    int end = NACodes.CORRUPTED_DATA.toInt();
    if (mutationType == MutationTypes.INSERTION_LTE_200BP){
      end = getVariant().getStart()+1;
    } else if (mutationType == MutationTypes.DELETION_LTE_200BP){
      end = getVariant().getStart()+getReferenceAlleleLength()-1;
    }
    return end;
  }

  @Override
  public String getReferenceGenomeAllele() {
    return getValueBasedOnMutationType("-", getReferenceAlleleSubstring());
  }

  @Override
  public String getControlGenotype() {
    val allele = getReferenceAlleleSubstring();
    return getValueBasedOnMutationType(
        joinAlleles("-","-"),
        joinAlleles(allele, allele));
  }

  private String getReferenceAlleleSubstring(){
    return getReferenceAlleleString().substring(1);
  }

  /**
   * TODO: Assumption is there there is ONLY ONE alternative allele.
   * @throws IllegalStateException for when there is more than one alternative allele
   */
  private String getAlternativeAlleleSubstring(){
    return getAlternativeAlleleString().substring(1);
  }

  @Override
  public String getMutatedFromAllele() {
    return getValueBasedOnMutationType("-", getReferenceAlleleSubstring());
  }

  @Override
  public String getTumorGenotype() {
    return getValueBasedOnMutationType(
        joinAlleles("-", getAlternativeAlleleSubstring()),
        joinAlleles(getReferenceAlleleSubstring(),"-" ));
  }

  @Override
  public String getMutatedToAllele() {
    return getValueBasedOnMutationType(getAlternativeAlleleSubstring(), "-");
  }

  private String getValueBasedOnMutationType(String insertionOption, String deletionOption){
    String out = NACodes.CORRUPTED_DATA.toString();
    if (mutationType == MutationTypes.INSERTION_LTE_200BP){
      out = insertionOption;
    } else if (mutationType == MutationTypes.DELETION_LTE_200BP){
      out = deletionOption;
    }
    return out;
  }
}
