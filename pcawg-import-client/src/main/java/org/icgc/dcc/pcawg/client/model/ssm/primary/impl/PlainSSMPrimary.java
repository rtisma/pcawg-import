package org.icgc.dcc.pcawg.client.model.ssm.primary.impl;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import org.icgc.dcc.pcawg.client.model.ssm.primary.SSMPrimary;

@Builder
@Value
public class PlainSSMPrimary implements SSMPrimary {

 @NonNull private final String   analysisId;
 @NonNull private final String   analyzedSampleId;
 @NonNull private final String   mutationType;
          private final boolean  pcawgFlag;
 @NonNull private final String   chromosome;
          private final int      chromosomeStart;
          private final int      chromosomeEnd;
          private final int      chromosomeStrand;
 @NonNull private final String   referenceGenomeAllele;
 @NonNull private final String   controlGenotype;
 @NonNull private final String   mutatedFromAllele;
 @NonNull private final String   tumorGenotype;
 @NonNull private final String   mutatedToAllele;
 @NonNull private final String   expressedAllele;
 @NonNull private final String   qualityScore;
 @NonNull private final String   probability;
          private final int      totalReadCount;
          private final int      mutantAlleleReadCount;
 @NonNull private final String   verificationStatus;
 @NonNull private final String   verificationPlatform;
 @NonNull private final String   biologicalValidationStatus;
 @NonNull private final String   biologicalValidationPlatform;
 @NonNull private final String   note;

 public boolean getPcawgFlag(){
  return pcawgFlag;
 }

}
