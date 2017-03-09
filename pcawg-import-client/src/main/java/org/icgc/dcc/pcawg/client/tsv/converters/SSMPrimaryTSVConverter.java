package org.icgc.dcc.pcawg.client.tsv.converters;

import com.google.common.collect.ImmutableList;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.icgc.dcc.pcawg.client.model.ssm.primary.SSMPrimary;
import org.icgc.dcc.pcawg.client.tsv.SSMPrimaryFieldValue;

import java.util.List;

import static org.icgc.dcc.pcawg.client.model.ssm.primary.SSMPrimaryFieldMapping.ANALYSIS_ID;
import static org.icgc.dcc.pcawg.client.model.ssm.primary.SSMPrimaryFieldMapping.ANALYZED_SAMPLE_ID;
import static org.icgc.dcc.pcawg.client.model.ssm.primary.SSMPrimaryFieldMapping.BIOLOGICAL_VALIDATION_PLATFORM;
import static org.icgc.dcc.pcawg.client.model.ssm.primary.SSMPrimaryFieldMapping.BIOLOGICAL_VALIDATION_STATUS;
import static org.icgc.dcc.pcawg.client.model.ssm.primary.SSMPrimaryFieldMapping.CHROMOSOME;
import static org.icgc.dcc.pcawg.client.model.ssm.primary.SSMPrimaryFieldMapping.CHROMOSOME_END;
import static org.icgc.dcc.pcawg.client.model.ssm.primary.SSMPrimaryFieldMapping.CHROMOSOME_START;
import static org.icgc.dcc.pcawg.client.model.ssm.primary.SSMPrimaryFieldMapping.CHROMOSOME_STRAND;
import static org.icgc.dcc.pcawg.client.model.ssm.primary.SSMPrimaryFieldMapping.CONTROL_GENOTYPE;
import static org.icgc.dcc.pcawg.client.model.ssm.primary.SSMPrimaryFieldMapping.EXPRESSED_ALLELE;
import static org.icgc.dcc.pcawg.client.model.ssm.primary.SSMPrimaryFieldMapping.MUTANT_ALLELE_READ_COUNT;
import static org.icgc.dcc.pcawg.client.model.ssm.primary.SSMPrimaryFieldMapping.MUTATED_FROM_ALLELE;
import static org.icgc.dcc.pcawg.client.model.ssm.primary.SSMPrimaryFieldMapping.MUTATED_TO_ALLELE;
import static org.icgc.dcc.pcawg.client.model.ssm.primary.SSMPrimaryFieldMapping.MUTATION_TYPE;
import static org.icgc.dcc.pcawg.client.model.ssm.primary.SSMPrimaryFieldMapping.PROBABILITY;
import static org.icgc.dcc.pcawg.client.model.ssm.primary.SSMPrimaryFieldMapping.QUALITY_SCORE;
import static org.icgc.dcc.pcawg.client.model.ssm.primary.SSMPrimaryFieldMapping.REFERENCE_GENOME_ALLELE;
import static org.icgc.dcc.pcawg.client.model.ssm.primary.SSMPrimaryFieldMapping.TOTAL_READ_COUNT;
import static org.icgc.dcc.pcawg.client.model.ssm.primary.SSMPrimaryFieldMapping.TUMOUR_GENOTYPE;
import static org.icgc.dcc.pcawg.client.model.ssm.primary.SSMPrimaryFieldMapping.VERIFICATION_PLATFORM;
import static org.icgc.dcc.pcawg.client.model.ssm.primary.SSMPrimaryFieldMapping.VERIFICATION_STATUS;
import static org.icgc.dcc.pcawg.client.tsv.SSMPrimaryFieldValue.newSSMPrimaryFieldValue;

@RequiredArgsConstructor
public class SSMPrimaryTSVConverter extends AbstractTSVConverterTemplate<SSMPrimaryFieldValue> {

  @NonNull
  private final SSMPrimary data;

  @Override
  public List<SSMPrimaryFieldValue> getFieldValueList() {
    return ImmutableList.of(
      newSSMPrimaryFieldValue(ANALYSIS_ID,                     data.getAnalysisId()),
      newSSMPrimaryFieldValue(ANALYZED_SAMPLE_ID,              data.getAnalyzedSampleId()),
      newSSMPrimaryFieldValue(MUTATION_TYPE,                   data.getMutationType()),
      newSSMPrimaryFieldValue(CHROMOSOME,                      data.getChromosome()),
      newSSMPrimaryFieldValue(CHROMOSOME_START,                data.getChromosomeStart()),
      newSSMPrimaryFieldValue(CHROMOSOME_END,                  data.getChromosomeEnd()),
      newSSMPrimaryFieldValue(CHROMOSOME_STRAND,               data.getChromosomeStrand()),
      newSSMPrimaryFieldValue(REFERENCE_GENOME_ALLELE,         data.getReferenceGenomeAllele()),
      newSSMPrimaryFieldValue(CONTROL_GENOTYPE,                data.getControlGenotype()),
      newSSMPrimaryFieldValue(MUTATED_FROM_ALLELE,             data.getMutatedFromAllele()),
      newSSMPrimaryFieldValue(MUTATED_TO_ALLELE,               data.getMutatedToAllele()),
      newSSMPrimaryFieldValue(TUMOUR_GENOTYPE,                 data.getTumorGenotype()),
      newSSMPrimaryFieldValue(EXPRESSED_ALLELE,                data.getExpressedAllele()),
      newSSMPrimaryFieldValue(QUALITY_SCORE,                   data.getQualityScore()),
      newSSMPrimaryFieldValue(PROBABILITY,                     data.getProbability()),
      newSSMPrimaryFieldValue(TOTAL_READ_COUNT,                data.getTotalReadCount()),
      newSSMPrimaryFieldValue(MUTANT_ALLELE_READ_COUNT,        data.getMutantAlleleReadCount()),
      newSSMPrimaryFieldValue(VERIFICATION_STATUS,             data.getVerificationStatus()),
      newSSMPrimaryFieldValue(VERIFICATION_PLATFORM,           data.getVerificationPlatform()),
      newSSMPrimaryFieldValue(BIOLOGICAL_VALIDATION_STATUS,    data.getBiologicalValidationStatus()),
      newSSMPrimaryFieldValue(BIOLOGICAL_VALIDATION_PLATFORM,  data.getBiologicalValidationPlatform())
    );

  }

}
