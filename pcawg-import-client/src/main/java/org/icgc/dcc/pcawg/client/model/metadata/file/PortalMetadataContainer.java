package org.icgc.dcc.pcawg.client.model.metadata.file;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import lombok.Builder;
import lombok.NonNull;
import lombok.Singular;
import lombok.Value;
import lombok.val;
import org.icgc.dcc.pcawg.client.utils.ObjectPersistance;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collector;

import static java.util.stream.Collectors.groupingBy;
import static org.icgc.dcc.common.core.util.stream.Streams.stream;

/**
 * TODO: Too much going on here. Remove unused methods, and create a filter for each
 */
@Builder
@Value
public class PortalMetadataContainer implements Serializable, Iterable<PortalMetadata> {

  private static final long serialVersionUID = 1486673032L;
  private static final PortalMetadata.FilenameComparator FILENAME_COMPARATOR = new PortalMetadata.FilenameComparator();
  private static final PortalMetadata.FileSizeComparator FILE_SIZE_COMPARATOR = new PortalMetadata.FileSizeComparator();

  @Singular
  private final List<PortalMetadata> portalMetadatas;

  public void store(String filename) throws IOException {
    ObjectPersistance.store(this, filename);
  }

  public static PortalMetadataContainer restore(String filename) throws IOException, ClassNotFoundException {
    return (PortalMetadataContainer) ObjectPersistance.restore(filename);
  }

  public PortalMetadataContainer filter(@NonNull final Predicate<? super PortalMetadata> predicate) {
    val builder = PortalMetadataContainer.builder();
    portalMetadatas.stream().filter(predicate).forEach(builder::portalMetadata);
    return builder.build();
  }

  public static PortalMetadataContainer buildPortalMetadataContainer(@NonNull final Iterable<ObjectNode> objectNodes) {
    val builder = PortalMetadataContainer.builder();
    stream(objectNodes)
        .map(PortalMetadata::buildPortalMetadata)
        .forEach(builder::portalMetadata);
    return builder.build();
  }

  public Map<String, PortalMetadataContainer> groupPortalMetadataBySample() {
    return groupPortalMetadataContainer(PortalMetadata::getSampleId);
  }

  public Map<String, PortalMetadataContainer> groupPortalMetadataByCaller() {
    return groupPortalMetadataContainer(x -> x.getPortalFilename().getWorkflow());
  }

  public Map<String, PortalMetadataContainer> groupPortalMetadatasByDonor() {
    return groupPortalMetadataContainer(PortalMetadata::getDonorId);
  }

  public Map<String, PortalMetadataContainer> groupPortalMetadatasByDataType() {
    return groupPortalMetadataContainer(PortalMetadata::getDataType);
  }

  public Map<String, PortalMetadataContainer> groupPortalMetadatasByMutationType() {
    return groupPortalMetadataContainer(x -> x.getPortalFilename().getMutationType());
  }

  public Map<String, PortalMetadataContainer> groupPortalMetadatasBySubMutationType() {
    return groupPortalMetadataContainer(x -> x.getPortalFilename().getDataType());
  }

  public PortalMetadataContainer sortByFileSize(final boolean ascending) {
    val list = Lists.newArrayList(portalMetadatas);
    Collections.sort(list, FILE_SIZE_COMPARATOR);

    if (! ascending){
      Collections.reverse(list);
    }
    return new PortalMetadataContainer(ImmutableList.copyOf(list));
  }

  public PortalMetadataContainer sortByFilename(final boolean ascending) {
    val list = Lists.newArrayList(portalMetadatas);
    Collections.sort(list, FILENAME_COMPARATOR);
    val portalMetadataList = ImmutableList.copyOf(list);
    return PortalMetadataContainer.builder()
        .portalMetadatas(portalMetadataList)
        .build();
  }

  public Map<String, PortalMetadataContainer> groupPortalMetadataContainer(
      final Function<? super PortalMetadata, ? extends String> functor) {
    return ImmutableMap.copyOf(portalMetadatas.stream().collect(groupingBy(functor, toPortalMetadataContainer())));
  }

  public static Collector<PortalMetadata, ImmutableList.Builder<PortalMetadata>, PortalMetadataContainer> toPortalMetadataContainer() {
    return Collector.of(
        ImmutableList.Builder::new,
        (builder, e) -> builder.add(e),
        (b1, b2) -> b1.addAll(b2.build()),
        (builder) -> new PortalMetadataContainer(builder.build()));
  }

  @Override
  public Iterator<PortalMetadata> iterator() {
    return portalMetadatas.iterator();
  }

  public int size() {
    return portalMetadatas.size();
  }

  public PortalMetadataContainer shuffle(final long seed){
    val rand = new Random();
    rand.setSeed(seed);
    val list = Lists.<PortalMetadata> newArrayList(getPortalMetadatas());
    Collections.shuffle(list, rand);
    return PortalMetadataContainer.builder().portalMetadatas(ImmutableList.copyOf(list)).build();
  }

}
