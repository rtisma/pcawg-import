package org.icgc.dcc.pcawg.client.tsv;

import lombok.NonNull;

public abstract class FieldValue<E> {

  private final E fieldName;

  private final Object value;

  protected FieldValue(@NonNull E fieldName, @NonNull Object value){
    this.fieldName = fieldName;
    this.value = value;
  }

  public final Integer getIntValue() {
    return Integer.parseInt(getStringValue());
  }

  public final String getStringValue() {
    return getValue().toString();
  }

  public final Object getValue(){
    return value;
  }

  public final E getField() {
    return fieldName;
  }

}
