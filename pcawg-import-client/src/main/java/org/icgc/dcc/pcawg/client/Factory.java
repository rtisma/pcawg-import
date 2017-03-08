package org.icgc.dcc.pcawg.client;

import lombok.NoArgsConstructor;
import org.icgc.dcc.pcawg.client.download.PortalFileDownloader;
import org.icgc.dcc.pcawg.client.download.PortalNew;
import org.icgc.dcc.pcawg.client.download.Storage;
import org.icgc.dcc.pcawg.client.vcf.CallerTypes;

import static lombok.AccessLevel.PRIVATE;
import static org.icgc.dcc.pcawg.client.config.ClientProperties.STORAGE_BYPASS_MD5_CHECK;
import static org.icgc.dcc.pcawg.client.config.ClientProperties.STORAGE_OUTPUT_VCF_STORAGE_DIR;
import static org.icgc.dcc.pcawg.client.config.ClientProperties.STORAGE_PERSIST_MODE;
import static org.icgc.dcc.pcawg.client.download.PortalFileDownloader.newPortalFileDownloader;
import static org.icgc.dcc.pcawg.client.download.PcawgVcfPortalAPIQueryCreator.newPcawgVcfPortalAPIQueryCreator;
import static org.icgc.dcc.pcawg.client.vcf.CallerTypes.CONSENSUS;

@NoArgsConstructor(access = PRIVATE)
public class Factory {

  public static Storage newStorage() {
    return new Storage(STORAGE_PERSIST_MODE, STORAGE_OUTPUT_VCF_STORAGE_DIR, STORAGE_BYPASS_MD5_CHECK);
  }

  public static PortalNew newPortal(CallerTypes callerType){
    return PortalNew.builder()
        .jsonQueryGenerator(newPcawgVcfPortalAPIQueryCreator(callerType))
        .build();
  }

  private static PortalFileDownloader newPortalFileDownloaderFromCallerType(CallerTypes callerType){
    return newPortalFileDownloader(newPortal(callerType), newStorage());
  }

  public static PortalFileDownloader newConsensusPortalFileDownloader(){
    return newPortalFileDownloaderFromCallerType(CONSENSUS);
  }

  public static Transformer newTransformer(){
    return new Transformer();
  }

}
