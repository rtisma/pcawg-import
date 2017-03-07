
package org.icgc.dcc.pcawg.client.download.fetcher.decorators;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.val;
import org.icgc.dcc.pcawg.client.model.metadata.FileMetaDataContext;
import org.icgc.dcc.pcawg.client.download.fetcher.Fetcher;
import org.icgc.dcc.pcawg.client.vcf.CallerTypes;

import static lombok.AccessLevel.PRIVATE;

@RequiredArgsConstructor(access = PRIVATE)
public class ConsensusOnlyFetcherDecorator implements Fetcher {

  public static ConsensusOnlyFetcherDecorator newConsensusOnlyFetcherDecorator(final Fetcher fetcher){
    return new ConsensusOnlyFetcherDecorator(fetcher);
  }

  @NonNull
  private final Fetcher fetcher;

  @Override
  @SneakyThrows
  public FileMetaDataContext fetch() {
    val fileMetaDataContext = fetcher.fetch();
    return fileMetaDataContext.filter(x -> x.getVcfFilenameParser().getCallerType() == CallerTypes.consensus);
  }
}
