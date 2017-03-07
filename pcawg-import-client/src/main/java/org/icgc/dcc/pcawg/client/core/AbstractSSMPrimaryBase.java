package org.icgc.dcc.pcawg.client.core;

/**
 * Common place for
 */
public abstract class AbstractSSMPrimaryBase extends AbstractCommon implements SSMPrimary {

  public AbstractSSMPrimaryBase(ProjectMetadataDAO projectMetadataDAO, String aliquotId) {
    super(projectMetadataDAO, aliquotId);
  }

}
