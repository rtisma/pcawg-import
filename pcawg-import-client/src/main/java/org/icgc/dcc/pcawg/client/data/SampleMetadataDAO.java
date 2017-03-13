package org.icgc.dcc.pcawg.client.data;

import org.icgc.dcc.pcawg.client.model.metadata.file.FilenameParser;
import org.icgc.dcc.pcawg.client.model.metadata.project.SampleMetadata;

public interface SampleMetadataDAO {

  static boolean isUSProject(String projectCode){
    return projectCode.matches("^.*-US$");
  }

  SampleMetadata getSampleMetadataByFilenameParser(FilenameParser filenameParser);
}
