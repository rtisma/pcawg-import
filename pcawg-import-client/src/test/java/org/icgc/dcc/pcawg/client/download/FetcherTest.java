package org.icgc.dcc.pcawg.client.download;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.assertj.core.api.Assertions;
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

}
