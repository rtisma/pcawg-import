package org.icgc.dcc.pcawg.client.core;

public interface ProjectMetadataDAO {

  static boolean isUSProject(String projectCode){
    return projectCode.matches("^.*-US$");
  }

  ProjectMetadata getProjectMetadataByAliquotId(String aliquotId);
}
