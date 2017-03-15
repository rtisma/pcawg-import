package org.icgc.dcc.pcawg.client;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.icgc.dcc.pcawg.client.data.FileSampleMetadataDAO;
import org.icgc.dcc.pcawg.client.data.SampleMetadataDAO;
import org.icgc.dcc.pcawg.client.download.MetadataContainer;
import org.icgc.dcc.pcawg.client.download.PortalFileDownloader;
import org.icgc.dcc.pcawg.client.download.PortalNew;
import org.icgc.dcc.pcawg.client.download.Storage;
import org.icgc.dcc.pcawg.client.model.metadata.project.SampleMetadata;
import org.icgc.dcc.pcawg.client.model.ssm.metadata.SSMMetadata;
import org.icgc.dcc.pcawg.client.model.ssm.metadata.impl.PcawgSSMMetadata;
import org.icgc.dcc.pcawg.client.model.ssm.primary.SSMPrimary;
import org.icgc.dcc.pcawg.client.tsv.impl.SSMMetadataTSVConverter;
import org.icgc.dcc.pcawg.client.tsv.impl.SSMPrimaryTSVConverter;
import org.icgc.dcc.pcawg.client.vcf.WorkflowTypes;

import java.nio.file.Paths;

import static lombok.AccessLevel.PRIVATE;
import static org.icgc.dcc.pcawg.client.config.ClientProperties.OUTPUT_TSV_DIRECTORY;
import static org.icgc.dcc.pcawg.client.config.ClientProperties.SAMPLE_SHEET_TSV_FILENAME;
import static org.icgc.dcc.pcawg.client.config.ClientProperties.SAMPLE_SHEET_TSV_URL;
import static org.icgc.dcc.pcawg.client.config.ClientProperties.SSM_M_TSV_FILENAME;
import static org.icgc.dcc.pcawg.client.config.ClientProperties.SSM_P_TSV_FILENAME;
import static org.icgc.dcc.pcawg.client.config.ClientProperties.STORAGE_BYPASS_MD5_CHECK;
import static org.icgc.dcc.pcawg.client.config.ClientProperties.STORAGE_OUTPUT_VCF_STORAGE_DIR;
import static org.icgc.dcc.pcawg.client.config.ClientProperties.STORAGE_PERSIST_MODE;
import static org.icgc.dcc.pcawg.client.config.ClientProperties.UUID2BARCODE_SHEET_TSV_FILENAME;
import static org.icgc.dcc.pcawg.client.config.ClientProperties.UUID2BARCODE_SHEET_TSV_URL;
import static org.icgc.dcc.pcawg.client.data.FileSampleMetadataDAO.newFileSampleMetadataDAO;
import static org.icgc.dcc.pcawg.client.download.PcawgVcfPortalAPIQueryCreator.newPcawgVcfPortalAPIQueryCreator;
import static org.icgc.dcc.pcawg.client.download.PortalFileDownloader.newPortalFileDownloader;
import static org.icgc.dcc.pcawg.client.download.Storage.downloadFileByURL;
import static org.icgc.dcc.pcawg.client.vcf.WorkflowTypes.CONSENSUS;

@NoArgsConstructor(access = PRIVATE)
@Slf4j
public class Factory {

  public static Storage newStorage() {
    log.info("Creating storage instance with persistMode: {}, outputDir: {}, and md5BypassEnable: {}",
        STORAGE_PERSIST_MODE, STORAGE_OUTPUT_VCF_STORAGE_DIR, STORAGE_BYPASS_MD5_CHECK);
    return new Storage(STORAGE_PERSIST_MODE, STORAGE_OUTPUT_VCF_STORAGE_DIR, STORAGE_BYPASS_MD5_CHECK);
  }

  public static PortalNew newPortal(WorkflowTypes callerType){
    log.info("Creating new Portal instance for callerType [{}]", callerType.name());
    return PortalNew.builder()
        .jsonQueryGenerator(newPcawgVcfPortalAPIQueryCreator(callerType))
        .build();
  }

  public static MetadataContainer newMetadataContainer(){
    val portal = newPortal(CONSENSUS);
    val sampleMetadataDAO = newSampleMetadataDAO();
    log.info("Creating MetadataContainer");
    return new MetadataContainer(portal, sampleMetadataDAO);
  }

  private static PortalFileDownloader newPortalFileDownloaderFromCallerType(WorkflowTypes callerType){
    return newPortalFileDownloader(newPortal(callerType), newStorage());
  }

  public static PortalFileDownloader newConsensusPortalFileDownloader(){
    return newPortalFileDownloaderFromCallerType(CONSENSUS);
  }

  public static SSMMetadata newSSMMetadata(SampleMetadata sampleMetadata){
    return PcawgSSMMetadata.newSSMMetadataImpl(
        sampleMetadata.getWorkflow(),
        sampleMetadata.getMatchedSampleId(),
        sampleMetadata.getAnalysisId(),
        sampleMetadata.getAnalyzedSampleId(),
        sampleMetadata.isUsProject(),
        sampleMetadata.getAliquotId() );
  }

  public static Transformer<SSMMetadata> newSSMMetadataTransformer(String dccProjectCode){
    log.info("Creating SSMMetadata Transformer for DccProjectCode [{}]", dccProjectCode);
    return new Transformer<SSMMetadata>(OUTPUT_TSV_DIRECTORY,dccProjectCode, SSM_M_TSV_FILENAME, new SSMMetadataTSVConverter());
  }

  public static Transformer<SSMPrimary> newSSMPrimaryTransformer(String dccProjectCode){
    log.info("Creating SSMPrimary Transformer for DccProjectCode [{}]", dccProjectCode);
    return new Transformer<SSMPrimary>(OUTPUT_TSV_DIRECTORY,dccProjectCode, SSM_P_TSV_FILENAME, new SSMPrimaryTSVConverter());
  }

  private static FileSampleMetadataDAO newFileSampleMetadataDAOAndDownload(){
    val outputDir = Paths.get("").toAbsolutePath().toString();
    if (Paths.get(SAMPLE_SHEET_TSV_FILENAME).toFile().exists()){
      log.info("Already downloaded [{}] to directory [{}] from url: {}", SAMPLE_SHEET_TSV_FILENAME, outputDir,SAMPLE_SHEET_TSV_URL);
    } else {
      log.info("Downloading [{}] to directory [{}] from url: {}", SAMPLE_SHEET_TSV_FILENAME, outputDir,SAMPLE_SHEET_TSV_URL);
      downloadFileByURL(SAMPLE_SHEET_TSV_URL, SAMPLE_SHEET_TSV_FILENAME);
    }

    if (Paths.get(UUID2BARCODE_SHEET_TSV_FILENAME).toFile().exists()){
      log.info("Already downloaded [{}] to directory [{}] from url: {}", UUID2BARCODE_SHEET_TSV_FILENAME, outputDir, UUID2BARCODE_SHEET_TSV_URL);
    } else {
      log.info("Downloading [{}] to directory [{}] from url: {}", UUID2BARCODE_SHEET_TSV_FILENAME, outputDir, UUID2BARCODE_SHEET_TSV_URL);
      downloadFileByURL(UUID2BARCODE_SHEET_TSV_URL, UUID2BARCODE_SHEET_TSV_FILENAME);
    }
    log.info("Done downloading, creating FileSampleMetadataDAO");
    return newFileSampleMetadataDAO(SAMPLE_SHEET_TSV_FILENAME, UUID2BARCODE_SHEET_TSV_FILENAME);
  }

  public static SampleMetadataDAO newSampleMetadataDAO(){
    return newFileSampleMetadataDAOAndDownload();
  }
}
