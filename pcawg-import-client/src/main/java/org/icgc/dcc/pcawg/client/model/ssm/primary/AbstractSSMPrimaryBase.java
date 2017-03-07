package org.icgc.dcc.pcawg.client.model.ssm.primary;

import org.icgc.dcc.pcawg.client.core.ProjectMetadataDAO;
import org.icgc.dcc.pcawg.client.model.ssm.AbstractCommon;

/**
 * Common place for
 */
public abstract class AbstractSSMPrimaryBase extends AbstractCommon implements SSMPrimary {

  public AbstractSSMPrimaryBase(ProjectMetadataDAO projectMetadataDAO, String aliquotId) {
    super(projectMetadataDAO, aliquotId);
  }

}
