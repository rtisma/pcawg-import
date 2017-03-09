package org.icgc.dcc.pcawg.client.tsv;

import org.icgc.dcc.pcawg.client.model.ssm.metadata.SSMMetadataFieldMapping;

public class SSMMetadataFieldValue extends FieldValue<SSMMetadataFieldMapping> {

  public static SSMMetadataFieldValue newSSMMetadataFieldValue(final SSMMetadataFieldMapping fieldName, final Object value){
    return new SSMMetadataFieldValue(fieldName, value);
  }

  public SSMMetadataFieldValue(SSMMetadataFieldMapping fieldName, Object value) {
    super(fieldName, value);
  }

}
