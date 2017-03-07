package org.icgc.dcc.pcawg.client.download;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.assertj.core.api.Assertions;
import org.icgc.dcc.pcawg.client.model.metadata.FileMetaDataContext;
import org.icgc.dcc.pcawg.client.download.fetcher.FetcherFactory;
import org.junit.Test;

@Slf4j
public class FetcherTest {

  @SneakyThrows
  @Test
  public void testDonorLimitFetcher(){
    val fetcher  = FetcherFactory.builder()
        .setNumDonors(10)
        .setLimit(5)
        .build();
    val ctx = fetcher.fetch();
    Assertions.assertThat(ctx).hasSize(5);
  }

  @SneakyThrows
  @Test
  public void testPersistance(){
    val fetcher  = FetcherFactory.builder()
        .setNumDonors(10)
        .setLimit(5)
        .build();
    val ctx = fetcher.fetch();
    val storageFilename = "target/testPersistance.dat";
    ctx.store(storageFilename);
    val ctxRestored = FileMetaDataContext.restore(storageFilename);
    Assertions.assertThat(ctxRestored.equals(ctx));
  }

}
