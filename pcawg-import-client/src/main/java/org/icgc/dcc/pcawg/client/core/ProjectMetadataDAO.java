package org.icgc.dcc.pcawg.client.core;

public interface ProjectMetadataDAO {

  static boolean isUSProject(String projectCode){
    return projectCode.matches("^.*-US$");
  }

  String getMatchedSampleId(String aliquot_id);
  String getAnalyzedSampleId(String aliquot_id);
  String getDccProjectCode(String aliquot_id);
}
