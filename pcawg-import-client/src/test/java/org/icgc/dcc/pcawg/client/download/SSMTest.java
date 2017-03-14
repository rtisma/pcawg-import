package org.icgc.dcc.pcawg.client.download;

import com.google.common.io.Resources;
import htsjdk.variant.vcf.VCFFileReader;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.icgc.dcc.pcawg.client.model.ssm.primary.SSMPrimary;
import org.icgc.dcc.pcawg.client.model.ssm.primary.impl.SSMPrimaryPojo;
import org.junit.Test;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;
import static org.icgc.dcc.pcawg.client.model.ssm.primary.impl.IndelSSMPrimary.newIndelSSMPrimary;
import static org.icgc.dcc.pcawg.client.model.ssm.primary.impl.SnvMnvSSMPrimary.newSnvMnvSSMPrimary;

@Slf4j
public class SSMTest {
  // load vcf and iterate through each variantContext element
  private static final boolean REQUIRE_INDEX_CFG = false;
  private static final String INDEL_FIXTURE_FILENAME = "f9c4e06c-e8a6-613b-e040-11ac0d4828ba.consensus.20160830.somatic.indel.vcf.gz";
  private static final String DUMMY_ANALYSIS_ID = "myAnalysisId";
  private static final String DUMMY_ANALYZED_SAMPLE_ID= "myAnalyzedSampleId";
  private static final String INDEL_INSERTION_VCF_FILENAME = "fixtures/test_indel_insertion.vcf";
  private static final String INDEL_DELETION_VCF_FILENAME = "fixtures/test_indel_deletion.vcf";
  private static final String SNV_MNV_SINGLE_BASE_VCF_FILENAME = "fixtures/test_snv_mnv_single_base.vcf";
  private static final String SNV_MNV_MULTIPLE_BASE_VCF_FILENAME = "fixtures/test_snv_mnv_multiple_base.vcf";

  private static final String INSERTION_MUTATION_TYPE = "insertion of <=200bp";
  private static final String DELETION_MUTATION_TYPE= "deletion of <=200bp";
  private static final String SINGLE_BASE_SUBSTITUTION_MUTATION_TYPE= "single base substitution";
  private static final String MULTIPLE_BASE_SUBSTITUTION_MUTATION_TYPE = "multiple base substitution (>=2bp and <=200bp)";

  private static SSMPrimary getFirstSSMIndelPrimary(String vcfFilename){
    val vcf = readVCF(vcfFilename);
    SSMPrimary ssmPrimary = null;
    for (val variant : vcf){
      ssmPrimary = newIndelSSMPrimary(
          variant,
          DUMMY_ANALYSIS_ID,
          DUMMY_ANALYZED_SAMPLE_ID);
      break; //only first line
    }
    return ssmPrimary;
  }

  private static SSMPrimary getFirstSSMSnvMnvPrimary(String vcfFilename){
    val vcf = readVCF(vcfFilename);
    SSMPrimary ssmPrimary = null;
    for (val variant : vcf){
      ssmPrimary = newSnvMnvSSMPrimary(variant,DUMMY_ANALYSIS_ID,DUMMY_ANALYZED_SAMPLE_ID);
      break; //only first line
    }
    return ssmPrimary;
  }

  private static void assertSSMPrimary(SSMPrimary exp, SSMPrimary act){
    assertThat(exp.getAnalysisId()).isEqualTo(act.getAnalysisId());
    assertThat(exp.getAnalyzedSampleId()).isEqualTo(act.getAnalyzedSampleId());
    assertThat(exp.getPcawgFlag()).isEqualTo(act.getPcawgFlag());
    assertThat(exp.getMutationType()).isEqualTo(act.getMutationType());
    assertThat(exp.getChromosome()).isEqualTo(act.getChromosome());
    assertThat(exp.getChromosomeStart()).isEqualTo(act.getChromosomeStart());
    assertThat(exp.getChromosomeEnd()).isEqualTo(act.getChromosomeEnd());
    assertThat(exp.getChromosomeStrand()).isEqualTo(act.getChromosomeStrand());
    assertThat(exp.getReferenceGenomeAllele()).isEqualTo(act.getReferenceGenomeAllele());
    assertThat(exp.getControlGenotype()).isEqualTo(act.getControlGenotype());
    assertThat(exp.getMutatedFromAllele()).isEqualTo(act.getMutatedFromAllele());
    assertThat(exp.getTumorGenotype()).isEqualTo(act.getTumorGenotype());
    assertThat(exp.getMutatedToAllele()).isEqualTo(act.getMutatedToAllele());
    assertThat(exp.getExpressedAllele()).isEqualTo(act.getExpressedAllele());
    assertThat(exp.getQualityScore()).isEqualTo(act.getQualityScore());
    assertThat(exp.getProbability()).isEqualTo(act.getProbability());
    assertThat(exp.getTotalReadCount()).isEqualTo(act.getTotalReadCount());
    assertThat(exp.getMutantAlleleReadCount()).isEqualTo(act.getMutantAlleleReadCount());
    assertThat(exp.getVerificationStatus()).isEqualTo(act.getVerificationStatus());
    assertThat(exp.getVerificationPlatform()).isEqualTo(act.getVerificationPlatform());
    assertThat(exp.getBiologicalValidationStatus()).isEqualTo(act.getBiologicalValidationStatus());
    assertThat(exp.getBiologicalValidationPlatform()).isEqualTo(act.getBiologicalValidationPlatform());
    assertThat(exp.getNote()).isEqualTo(act.getNote());
  }


  private static SSMPrimary createDeletion(String chromosome, int pos, String ref, String alt, int tRefCount, int tAltCount){
    val refLength = ref.length();
    val altLength = alt.length();
    val refFirstUpstreamRemoved = ref.substring(1);
    assertThat(refLength -1).isEqualTo(refFirstUpstreamRemoved.length());
    return SSMPrimaryPojo.builder()
        .analysisId(DUMMY_ANALYSIS_ID)
        .analyzedSampleId(DUMMY_ANALYZED_SAMPLE_ID)
        .mutationType(DELETION_MUTATION_TYPE)
        .chromosome(chromosome)
        .chromosomeStart(pos+1)
        .chromosomeEnd(pos+refLength-1)
        .chromosomeStrand(1)
        .referenceGenomeAllele(refFirstUpstreamRemoved)
        .controlGenotype(refFirstUpstreamRemoved+" / "+refFirstUpstreamRemoved)
        .mutatedFromAllele(refFirstUpstreamRemoved)
        .tumorGenotype(refFirstUpstreamRemoved+" / -")
        .mutatedToAllele("-")
        .expressedAllele("-777")
        .qualityScore("-777")
        .probability("-777")
        .totalReadCount(tAltCount+tRefCount)
        .mutantAlleleReadCount(tAltCount)
        .verificationStatus("not tested")
        .verificationPlatform("-777")
        .biologicalValidationStatus("-777")
        .biologicalValidationPlatform("-777")
        .note("-777")
        .pcawgFlag(true)
        .build();
  }

  private static SSMPrimary createInsertion(String chromosome, int pos, String ref, String alt, int tRefCount, int tAltCount){
    val refLength = ref.length();
    val altLength = alt.length();
    val refFirstUpstreamRemoved = ref.substring(1);
    val altFirstUpstreamRemoved = alt.substring(1);
    assertThat(refLength -1).isEqualTo(refFirstUpstreamRemoved.length());
    assertThat(altLength -1).isEqualTo(altFirstUpstreamRemoved.length());
    return SSMPrimaryPojo.builder()
        .analysisId(DUMMY_ANALYSIS_ID)
        .analyzedSampleId(DUMMY_ANALYZED_SAMPLE_ID)
        .mutationType(INSERTION_MUTATION_TYPE)
        .chromosome(chromosome)
        .chromosomeStart(pos+1)
        .chromosomeEnd(pos+1)
        .chromosomeStrand(1)
        .referenceGenomeAllele("-")
        .controlGenotype("- / -")
        .mutatedFromAllele("-")
        .tumorGenotype("- / "+altFirstUpstreamRemoved)
        .mutatedToAllele(altFirstUpstreamRemoved)
        .expressedAllele("-777")
        .qualityScore("-777")
        .probability("-777")
        .totalReadCount(tAltCount+tRefCount)
        .mutantAlleleReadCount(tAltCount)
        .verificationStatus("not tested")
        .verificationPlatform("-777")
        .biologicalValidationStatus("-777")
        .biologicalValidationPlatform("-777")
        .note("-777")
        .pcawgFlag(true)
        .build();
  }

  private static SSMPrimary createSingleBase(String chromosome, int pos, String ref, String alt, int tRefCount, int tAltCount){
    val refLength = ref.length();
    return SSMPrimaryPojo.builder()
        .analysisId(DUMMY_ANALYSIS_ID)
        .analyzedSampleId(DUMMY_ANALYZED_SAMPLE_ID)
        .mutationType(SINGLE_BASE_SUBSTITUTION_MUTATION_TYPE)
        .chromosome(chromosome)
        .chromosomeStart(pos)
        .chromosomeEnd(pos+refLength-1)
        .chromosomeStrand(1)
        .referenceGenomeAllele(ref)
        .controlGenotype(ref+" / "+ref)
        .mutatedFromAllele(ref)
        .tumorGenotype(ref+" / "+alt)
        .mutatedToAllele(alt)
        .expressedAllele("-777")
        .qualityScore("-777")
        .probability("-777")
        .totalReadCount(tAltCount+tRefCount)
        .mutantAlleleReadCount(tAltCount)
        .verificationStatus("not tested")
        .verificationPlatform("-777")
        .biologicalValidationStatus("-777")
        .biologicalValidationPlatform("-777")
        .note("-777")
        .pcawgFlag(true)
        .build();
  }

  private static SSMPrimary createMultipleBase(String chromosome, int pos, String ref, String alt, int tRefCount, int tAltCount){
    val refLength = ref.length();
    return SSMPrimaryPojo.builder()
        .analysisId(DUMMY_ANALYSIS_ID)
        .analyzedSampleId(DUMMY_ANALYZED_SAMPLE_ID)
        .mutationType(MULTIPLE_BASE_SUBSTITUTION_MUTATION_TYPE)
        .chromosome(chromosome)
        .chromosomeStart(pos)
        .chromosomeEnd(pos+refLength-1)
        .chromosomeStrand(1)
        .referenceGenomeAllele(ref)
        .controlGenotype(ref+" / "+ref)
        .mutatedFromAllele(ref)
        .tumorGenotype(ref+" / "+alt)
        .mutatedToAllele(alt)
        .expressedAllele("-777")
        .qualityScore("-777")
        .probability("-777")
        .totalReadCount(tAltCount+tRefCount)
        .mutantAlleleReadCount(tAltCount)
        .verificationStatus("not tested")
        .verificationPlatform("-777")
        .biologicalValidationStatus("-777")
        .biologicalValidationPlatform("-777")
        .note("-777")
        .pcawgFlag(true)
        .build();
  }

  @SneakyThrows
  private static VCFFileReader readVCF(String filename){
    val url = Resources.getResource(filename);
    val file = new File(url.toURI());
    return new VCFFileReader(file, REQUIRE_INDEX_CFG );
  }

  @Test
  public void testIndelInsertion(){
    val ssmIndelPrimaryActual = getFirstSSMIndelPrimary(INDEL_INSERTION_VCF_FILENAME);
    val pos = 2277483;
    val ref = "A";
    val alt = "ATG";
    val chromosome = "1";
    val t_alt_count =4;
    val t_ref_count = 44;

    val ssmIndelPrimaryExpected = createInsertion(chromosome, pos, ref, alt, t_ref_count, t_alt_count);
    assertThat(ssmIndelPrimaryActual).isNotNull();
    assertSSMPrimary(ssmIndelPrimaryActual, ssmIndelPrimaryExpected);
  }

  @Test
  public void testIndelDeletion(){
    val ssmIndelPrimaryActual = getFirstSSMIndelPrimary(INDEL_DELETION_VCF_FILENAME);
    val pos = 2897557;
    val ref = "CAACTTATATATT";
    val alt = "C";
    val chromosome = "1";
    val t_alt_count =1;
    val t_ref_count = 52;
    val ssmIndelPrimaryExpected = createDeletion(chromosome, pos, ref, alt, t_ref_count, t_alt_count);
    assertThat(ssmIndelPrimaryActual).isNotNull();
    assertSSMPrimary(ssmIndelPrimaryActual, ssmIndelPrimaryExpected);
  }

  @Test
  public void testSnvMnvSingleBase(){
    val ssmSnvMnvPrimaryActual = getFirstSSMSnvMnvPrimary(SNV_MNV_SINGLE_BASE_VCF_FILENAME);
    val pos = 2897557;
    val ref = "A";
    val alt = "C";
    val chromosome = "1";
    val t_alt_count =1;
    val t_ref_count = 52;
    val ssmSnvMnvPrimaryExpected = createSingleBase(chromosome, pos, ref, alt, t_ref_count, t_alt_count);
    assertThat(ssmSnvMnvPrimaryActual).isNotNull();
    assertSSMPrimary(ssmSnvMnvPrimaryActual, ssmSnvMnvPrimaryExpected);
  }

  @Test
  public void testSnvMnvMultipleBase(){
    val ssmSnvMnvPrimaryActual = getFirstSSMSnvMnvPrimary(SNV_MNV_MULTIPLE_BASE_VCF_FILENAME);
    val pos = 2897557;
    val ref = "ATG";
    val alt = "AGT";
    val chromosome = "1";
    val t_alt_count =1;
    val t_ref_count = 52;
    val ssmSnvMnvPrimaryExpected = createMultipleBase(chromosome, pos, ref, alt, t_ref_count, t_alt_count);
    assertThat(ssmSnvMnvPrimaryActual).isNotNull();
    assertSSMPrimary(ssmSnvMnvPrimaryActual, ssmSnvMnvPrimaryExpected);
  }



}
