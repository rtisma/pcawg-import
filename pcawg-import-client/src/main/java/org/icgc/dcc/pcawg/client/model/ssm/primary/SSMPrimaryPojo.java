package org.icgc.dcc.pcawg.client.model.ssm.primary;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Builder
@Value
public class SSMPrimaryPojo implements SSMPrimary{

 @NonNull private final String   analysisId;
 @NonNull private final String   analyzedSampleId;
 @NonNull private final String   mutationType;
 @NonNull private final boolean  pcawgFlag;
 @NonNull private final String   chromosome;
 @NonNull private final int      chromosomeStart;
 @NonNull private final int      chromosomeEnd;
 @NonNull private final int      chromosomeStrand;
 @NonNull private final String   referenceGenomeAllele;
 @NonNull private final String   controlGenotype;
 @NonNull private final String   mutatedFromAllele;
 @NonNull private final String   tumorGenotype;
 @NonNull private final String   mutatedToAllele;
 @NonNull private final String   expressedAllele;
 @NonNull private final String   qualityScore;
 @NonNull private final String   probability;
 @NonNull private final int      totalReadCount;
 @NonNull private final int      mutantAlleleReadCount;
 @NonNull private final String   verificationStatus;
 @NonNull private final String   verificationPlatform;
 @NonNull private final String   biologicalValidationStatus;
 @NonNull private final String   biologicalValidationPlatform;
 @NonNull private final String   note;

 public boolean getPcawgFlag(){
  return pcawgFlag;
 }

}
