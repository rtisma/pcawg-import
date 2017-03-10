package org.icgc.dcc.pcawg.client.model.ssm;

public interface Common  {

  static final String DEFAULT_EMPTY = "-";
  String getAnalysisId();
  String getAnalyzedSampleId();
  boolean getPcawgFlag();

}
