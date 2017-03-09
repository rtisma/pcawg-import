package org.icgc.dcc.pcawg.client.tsv.converters;

import com.google.common.collect.Maps;
import lombok.Getter;

import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static lombok.AccessLevel.PROTECTED;

public abstract class AbstractTSVConverterTemplate<E extends Enum> implements  TSVConverter{

  private static final Collector<CharSequence,?, String> TSV_STRING_COLLECTOR = Collectors.joining("\t");


  @Override
  public final String toTSVData() {
//    return .stream().map(f -> f).collect(TSV_STRING_COLLECTOR).trim();
    return null;
  }

  @Override
  public final String toTSVHeader() {
    return getFieldValueList().stream().map(Object::toString).collect(TSV_STRING_COLLECTOR).trim();
  }

  public void check(){


  }



  protected abstract List<E> getEnumMap();

}
