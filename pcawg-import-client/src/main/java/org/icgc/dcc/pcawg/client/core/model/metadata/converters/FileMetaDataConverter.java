package org.icgc.dcc.pcawg.client.core.model.metadata.converters;

import org.icgc.dcc.pcawg.client.core.model.metadata.FileMetaData;

public interface FileMetaDataConverter<T> {

  T convertFromFileMetaData(FileMetaData fileMetaData);

}
