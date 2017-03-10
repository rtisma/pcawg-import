package org.icgc.dcc.pcawg.client;

import lombok.NoArgsConstructor;
import lombok.val;
import org.icgc.dcc.pcawg.client.data.ProjectMetadataDAO;
import org.icgc.dcc.pcawg.client.download.MetadataContainer;
import org.icgc.dcc.pcawg.client.download.PortalFileDownloader;
import org.icgc.dcc.pcawg.client.download.PortalNew;
import org.icgc.dcc.pcawg.client.download.Storage;
import org.icgc.dcc.pcawg.client.model.metadata.MetadataContext;
import org.icgc.dcc.pcawg.client.model.ssm.metadata.SSMMetadata;
import org.icgc.dcc.pcawg.client.model.ssm.metadata.impl.SSMMetadataImpl;
import org.icgc.dcc.pcawg.client.model.ssm.primary.SSMPrimary;
import org.icgc.dcc.pcawg.client.tsv.impl.SSMMetadataTSVConverter;
import org.icgc.dcc.pcawg.client.tsv.impl.SSMPrimaryTSVConverter;
import org.icgc.dcc.pcawg.client.vcf.CallerTypes;

import static lombok.AccessLevel.PRIVATE;
import static org.icgc.dcc.pcawg.client.config.ClientProperties.OUTPUT_TSV_DIRECTORY;
import static org.icgc.dcc.pcawg.client.config.ClientProperties.SSM_M_TSV_FILENAME;
import static org.icgc.dcc.pcawg.client.config.ClientProperties.SSM_P_TSV_FILENAME;
import static org.icgc.dcc.pcawg.client.config.ClientProperties.STORAGE_BYPASS_MD5_CHECK;
import static org.icgc.dcc.pcawg.client.config.ClientProperties.STORAGE_OUTPUT_VCF_STORAGE_DIR;
import static org.icgc.dcc.pcawg.client.config.ClientProperties.STORAGE_PERSIST_MODE;
import static org.icgc.dcc.pcawg.client.data.FileProjectMetadataDAO.newFileProjectMetadataDAOAndDownload;
import static org.icgc.dcc.pcawg.client.download.PcawgVcfPortalAPIQueryCreator.newPcawgVcfPortalAPIQueryCreator;
import static org.icgc.dcc.pcawg.client.download.PortalFileDownloader.newPortalFileDownloader;
import static org.icgc.dcc.pcawg.client.vcf.CallerTypes.CONSENSUS;

@NoArgsConstructor(access = PRIVATE)
public class Factory {

  public static Storage newStorage() {
    return new Storage(STORAGE_PERSIST_MODE, STORAGE_OUTPUT_VCF_STORAGE_DIR, STORAGE_BYPASS_MD5_CHECK);
  }

  public static PortalNew newPortal(CallerTypes callerType){
    return PortalNew.builder()
        .jsonQueryGenerator(newPcawgVcfPortalAPIQueryCreator(callerType))
        .build();
  }

  public static MetadataContainer newMetadataContainer(){
    return new MetadataContainer(newPortal(CONSENSUS), newProjectMetadataDAO());
  }

  private static PortalFileDownloader newPortalFileDownloaderFromCallerType(CallerTypes callerType){
    return newPortalFileDownloader(newPortal(callerType), newStorage());
  }

  public static PortalFileDownloader newConsensusPortalFileDownloader(){
    return newPortalFileDownloaderFromCallerType(CONSENSUS);
  }

  public static Transformer newTransformer(){
    return null;
//    return new Transformer();
  }

  public static ProjectMetadataDAO newProjectMetadataDAO(){
    return newFileProjectMetadataDAOAndDownload();
  }

  public static SSMMetadata newSSMMetadata(MetadataContext context){
    val fileMetaData  = context.getFileMetaData();
    val projectMetadata = context.getProjectMetadata();
    val variationCallingAlgorithm = fileMetaData.getVcfFilenameParser().getCallerId();
    val dataType = fileMetaData.getVcfFilenameParser().getSubMutationType();
    val dccProjectCode = projectMetadata.getDccProjectCode();
    return SSMMetadataImpl.newSSMMetadataImpl(variationCallingAlgorithm,
        projectMetadata.getMatchedSampleId(),
        projectMetadata.getAnalysisId(variationCallingAlgorithm, dataType),
        projectMetadata.getAnalyzedSampleId());
  }

  public static Transformer<SSMMetadata> newSSMMetadataTransformer(String dccProjectCode){
    return new Transformer<SSMMetadata>(OUTPUT_TSV_DIRECTORY,dccProjectCode, SSM_M_TSV_FILENAME, new SSMMetadataTSVConverter());
  }

  public static Transformer<SSMPrimary> newSSMPrimaryTransformer(String dccProjectCode){
    return new Transformer<SSMPrimary>(OUTPUT_TSV_DIRECTORY,dccProjectCode, SSM_P_TSV_FILENAME, new SSMPrimaryTSVConverter());
  }


}
