package org.icgc.dcc.pcawg.client.model.metadata;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import org.icgc.dcc.pcawg.client.model.metadata.file.FileMetaData;

import java.io.File;

@Value
@Builder
public class FileContext {

  @NonNull
  private final File file;

  @NonNull
  private final FileMetaData fileMetaData;

}
