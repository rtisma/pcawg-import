package org.icgc.dcc.pcawg.client.tsv.converters;

import lombok.NoArgsConstructor;
import org.icgc.dcc.pcawg.client.model.ssm.primary.SSMPrimary;
import org.icgc.dcc.pcawg.client.model.ssm.primary.SSMPrimaryFieldMapping;

import static org.icgc.dcc.common.core.util.stream.Streams.stream;

@NoArgsConstructor
public class SSMPrimaryTSVConverter implements TSVConverter<SSMPrimary> {

  @Override
  public String toTSVData(SSMPrimary ssmPrimary) {
    return stream(SSMPrimaryFieldMapping.values())
        .map(x -> x.extractStringValue(ssmPrimary))
        .collect(getTsvCollectorInstance());
  }

  @Override
  public String toTSVHeader(SSMPrimary ssmPrimary) {
    return stream(SSMPrimaryFieldMapping.values())
        .map(SSMPrimaryFieldMapping::toString)
        .collect(getTsvCollectorInstance());
  }

}
