
package org.icgc.dcc.pcawg.client.download.fetcher;
import org.icgc.dcc.pcawg.client.core.model.metadata.FileMetaDataContext;


import java.io.IOException;

public interface Fetcher {
  FileMetaDataContext fetch() throws IOException, ClassNotFoundException;
}
