package org.icgc.dcc.pcawg.client.core;

public interface ProjectMetadataDAO {

  String getMatchedSampleId(String aliquot_id);
  String getAnalysisId(String aliquot_id);
  String getAnalyzedSampleId(String aliquot_id);
}
