package org.icgc.dcc.pcawg.client.tsv;

import org.icgc.dcc.pcawg.client.model.ssm.primary.SSMPrimaryFieldMapping;

public class SSMPrimaryFieldValue extends FieldValue<SSMPrimaryFieldMapping> {

  public static SSMPrimaryFieldValue newSSMPrimaryFieldValue(final SSMPrimaryFieldMapping field, final Object value){
    return new SSMPrimaryFieldValue(field, value);
  }

  private SSMPrimaryFieldValue(SSMPrimaryFieldMapping fieldName, Object value) {
    super(fieldName, value);
  }

}
