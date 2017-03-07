package org.icgc.dcc.pcawg.client.core.model.metadata.converters;

import org.icgc.dcc.pcawg.client.core.model.metadata.FileMetaDataContext;

public interface FileMetaDataContextConverter<T> {

  T convertFromFileMetaDataContext(FileMetaDataContext fileMetaDataContext);

}
