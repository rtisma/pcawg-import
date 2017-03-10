package org.icgc.dcc.pcawg.client.data;

import org.icgc.dcc.pcawg.client.model.metadata.project.ProjectMetadata;

public interface ProjectMetadataDAO {

  static boolean isUSProject(String projectCode){
    return projectCode.matches("^.*-US$");
  }

  ProjectMetadata getProjectMetadataByAliquotId(String aliquotId);
}
