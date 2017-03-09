package org.icgc.dcc.pcawg.client.model.ssm.metadata;

import org.icgc.dcc.pcawg.client.model.ssm.Common;
import org.icgc.dcc.pcawg.client.vcf.CallerTypes;

public interface SSMMetadata extends Common {

  String getMatchedSampleId();
  String getAssemblyVersion();
  String getPlatform();
  String getExperimentalProtocol();
  String getBaseCallingAlgorithm();
  String getAlignmentAlgorithm();
  CallerTypes getVariationCallingAlgorithm();
  String getOtherAnalysisAlgorithm();
  String getSequencingStrategy();
  String getSeqCoverage();
  String getRawDataRepository();
  String getRawDataAccession();
}
