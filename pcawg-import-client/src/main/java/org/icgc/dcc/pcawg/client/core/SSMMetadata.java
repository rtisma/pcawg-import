package org.icgc.dcc.pcawg.client.core;

public interface SSMMetadata extends Common {

  String getMatchedSampleId();
  String getAssemblyVersion();
  String getPlatform();
  String getExperimentalProtocol();
  String getBaseCallingAlgorithm();
  String getAlignmentAlgorithm();
  String getVariationCallingAlgorithm();
  String getOtherAnalysisAlgorithm();
  String getSequencingStrategy();
  String getSeqCoverage();
  String getRawDataRepository();
  String getRawDataAccession();
}
