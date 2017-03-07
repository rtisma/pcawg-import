package org.icgc.dcc.pcawg.client;

import lombok.NoArgsConstructor;
import org.icgc.dcc.pcawg.client.download.Storage;

import static lombok.AccessLevel.PRIVATE;
import static org.icgc.dcc.pcawg.client.config.ClientProperties.STORAGE_BYPASS_MD5_CHECK;
import static org.icgc.dcc.pcawg.client.config.ClientProperties.STORAGE_OUTPUT_VCF_STORAGE_DIR;
import static org.icgc.dcc.pcawg.client.config.ClientProperties.STORAGE_PERSIST_MODE;

@NoArgsConstructor(access = PRIVATE)
public class Factory {

  public static Storage newStorage() {
    return new Storage(STORAGE_PERSIST_MODE, STORAGE_OUTPUT_VCF_STORAGE_DIR, STORAGE_BYPASS_MD5_CHECK);
  }

}
