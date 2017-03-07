package org.icgc.dcc.pcawg.client.download.fetcher.decorators;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.val;
import org.icgc.dcc.pcawg.client.model.metadata.FileMetaDataContext;
import org.icgc.dcc.pcawg.client.download.fetcher.Fetcher;

import static lombok.AccessLevel.PRIVATE;

/**
 * TODO: ditch Fetcher dependancy, and directly depend on FileMetaDataContext, and just make this decorator a decorator for FileMetaDataContext
 */
@RequiredArgsConstructor(access = PRIVATE)
public class LimitFetcherDecorator implements Fetcher {

  public static LimitFetcherDecorator newLimitFetcherDecorator(final Fetcher fetcher, final int limit){
    return new LimitFetcherDecorator(fetcher, limit);
  }

  @NonNull
  private final Fetcher fetcher;

  private final int limit;

  @Override
  @SneakyThrows
  public FileMetaDataContext fetch() {
    val fileMetaDataContext = fetcher.fetch();
    val size = fileMetaDataContext.size();
    val subFileMetaDataList = fileMetaDataContext.getFileMetaDatas().subList(0, Math.min(limit, size));
    return FileMetaDataContext.builder().fileMetaDatas(subFileMetaDataList).build();
  }
}
