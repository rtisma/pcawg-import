package org.icgc.dcc.pcawg.client.core;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.icgc.dcc.pcawg.client.data.FileSampleMetadataDAO;
import org.icgc.dcc.pcawg.client.data.SampleMetadataDAO;
import org.icgc.dcc.pcawg.client.download.MetadataContainer;
import org.icgc.dcc.pcawg.client.download.Portal;
import org.icgc.dcc.pcawg.client.download.PortalFileDownloader;
import org.icgc.dcc.pcawg.client.download.Storage;
import org.icgc.dcc.pcawg.client.model.metadata.project.SampleMetadata;
import org.icgc.dcc.pcawg.client.model.ssm.metadata.SSMMetadata;
import org.icgc.dcc.pcawg.client.model.ssm.metadata.impl.PcawgSSMMetadata;
import org.icgc.dcc.pcawg.client.model.ssm.primary.SSMPrimary;
import org.icgc.dcc.pcawg.client.tsv.TSVConverter;
import org.icgc.dcc.pcawg.client.tsv.impl.SSMMetadataTSVConverter;
import org.icgc.dcc.pcawg.client.tsv.impl.SSMPrimaryTSVConverter;
import org.icgc.dcc.pcawg.client.vcf.WorkflowTypes;

import java.nio.file.Paths;

import static lombok.AccessLevel.PRIVATE;
import static org.icgc.dcc.pcawg.client.config.ClientProperties.SAMPLE_SHEET_HAS_HEADER;
import static org.icgc.dcc.pcawg.client.config.ClientProperties.SAMPLE_SHEET_TSV_FILENAME;
import static org.icgc.dcc.pcawg.client.config.ClientProperties.SAMPLE_SHEET_TSV_URL;
import static org.icgc.dcc.pcawg.client.config.ClientProperties.STORAGE_BYPASS_MD5_CHECK;
import static org.icgc.dcc.pcawg.client.config.ClientProperties.STORAGE_OUTPUT_VCF_STORAGE_DIR;
import static org.icgc.dcc.pcawg.client.config.ClientProperties.STORAGE_PERSIST_MODE;
import static org.icgc.dcc.pcawg.client.config.ClientProperties.TOKEN;
import static org.icgc.dcc.pcawg.client.config.ClientProperties.UUID2BARCODE_SHEET_HAS_HEADER;
import static org.icgc.dcc.pcawg.client.config.ClientProperties.UUID2BARCODE_SHEET_TSV_FILENAME;
import static org.icgc.dcc.pcawg.client.config.ClientProperties.UUID2BARCODE_SHEET_TSV_URL;
import static org.icgc.dcc.pcawg.client.data.FileSampleMetadataDAO.newFileSampleMetadataDAO;
import static org.icgc.dcc.pcawg.client.download.PortalQueryCreator.newPcawgQueryCreator;
import static org.icgc.dcc.pcawg.client.download.Storage.downloadFileByURL;
import static org.icgc.dcc.pcawg.client.download.Storage.newStorage;
import static org.icgc.dcc.pcawg.client.vcf.WorkflowTypes.CONSENSUS;

@NoArgsConstructor(access = PRIVATE)
@Slf4j
public class Factory {

  private static final TSVConverter<SSMMetadata> SSM_METADATA_TSV_CONVERTER = new SSMMetadataTSVConverter();
  private static final TSVConverter<SSMPrimary> SSM_PRIMARY_TSV_CONVERTER = new SSMPrimaryTSVConverter();

  public static TransformerFactory<SSMMetadata> newSSMMetadataTransformerFactory(boolean useHdfs){
    return TransformerFactory.newTransformerFactory(SSM_METADATA_TSV_CONVERTER, useHdfs);
  }

  public static TransformerFactory<SSMPrimary> newSSMPrimaryTransformerFactory(boolean useHdfs){
    return TransformerFactory.newTransformerFactory(SSM_PRIMARY_TSV_CONVERTER, useHdfs);
  }

  public static Storage newDefaultStorage() {
    log.info("Creating storage instance with persistMode: {}, outputDir: {}, and md5BypassEnable: {}",
        STORAGE_PERSIST_MODE, STORAGE_OUTPUT_VCF_STORAGE_DIR, STORAGE_BYPASS_MD5_CHECK, TOKEN);
    return newStorage(STORAGE_PERSIST_MODE, STORAGE_OUTPUT_VCF_STORAGE_DIR, STORAGE_BYPASS_MD5_CHECK, TOKEN);
  }


  public static Portal newPortal(WorkflowTypes callerType){
    log.info("Creating new Portal instance for callerType [{}]", callerType.name());
    return Portal.builder()
        .jsonQueryGenerator(newPcawgQueryCreator(callerType))
        .build();
  }

  public static MetadataContainer newMetadataContainer(){
    val portal = newPortal(CONSENSUS);
    val sampleMetadataDAO = newSampleMetadataDAO();
    log.info("Creating MetadataContainer");
    return new MetadataContainer(portal, sampleMetadataDAO);
  }

  private static PortalFileDownloader newPortalFileDownloader(WorkflowTypes callerType){
    return PortalFileDownloader.newPortalFileDownloader(newPortal(callerType), newDefaultStorage());
  }

  public static PortalFileDownloader newConsensusPortalFileDownloader(){
    return newPortalFileDownloader(CONSENSUS);
  }

  public static SSMMetadata newSSMMetadata(SampleMetadata sampleMetadata){
    return PcawgSSMMetadata.newSSMMetadataImpl(
        sampleMetadata.getWorkflowType().getName(),
        sampleMetadata.getDataType().getName(),
        sampleMetadata.getMatchedSampleId(),
        sampleMetadata.getAnalysisId(),
        sampleMetadata.getAnalyzedSampleId(),
        sampleMetadata.isUsProject(),
        sampleMetadata.getAliquotId() );
  }

  private static void  downloadSheet(String url, String outputFilename){
    val outputDir = Paths.get("").toAbsolutePath().toString();
    if (Paths.get(outputFilename).toFile().exists()){
      log.info("Already downloaded [{}] to directory [{}] from url: {}", outputFilename, outputDir,url);
    } else {
      log.info("Downloading [{}] to directory [{}] from url: {}", outputFilename, outputDir,url);
      downloadFileByURL(url, outputFilename);
    }
  }

  public static void downloadSampleSheet(String outputFilename){
    downloadSheet(SAMPLE_SHEET_TSV_URL, outputFilename);
  }

  public static void downloadUUID2BarcodeSheet(String outputFilename){
    downloadSheet(UUID2BARCODE_SHEET_TSV_URL, outputFilename);
  }

  public static FileSampleMetadataDAO newFileSampleMetadataDAOAndDownload(){
    downloadSampleSheet(SAMPLE_SHEET_TSV_FILENAME);
    downloadUUID2BarcodeSheet(UUID2BARCODE_SHEET_TSV_FILENAME);
    log.info("Done downloading, creating FileSampleMetadataDAO");
    return newFileSampleMetadataDAO(SAMPLE_SHEET_TSV_FILENAME, SAMPLE_SHEET_HAS_HEADER,
        UUID2BARCODE_SHEET_TSV_FILENAME, UUID2BARCODE_SHEET_HAS_HEADER);
  }


  public static SampleMetadataDAO newSampleMetadataDAO(){
    return newFileSampleMetadataDAOAndDownload();
  }

//  public static FileWriterContextFactory newSSMPrimaryFWCtxFactory(String outputDirectory,
//      final boolean append, String hostname, String port){
//    return FileWriterContextFactory.builder()
//        .outputDirectory(outputDirectory)
//        .fileNamePrefix(SSM_P_TSV_FILENAME_PREFIX)
//        .fileExtension(SSM_P_TSV_FILENAME_EXTENSION)
//        .append(append)
//        .hostname(hostname)
//        .port(port)
//        .build();
//  }
//
//  public static FileWriterContextFactory newSSMMetadataFWCtxFactory(String outputDirectory,
//      final boolean append, String hostname, String port){
//    return FileWriterContextFactory.builder()
//        .outputDirectory(outputDirectory)
//        .fileNamePrefix(SSM_M_TSV_FILENAME_PREFIX)
//        .fileExtension(SSM_M_TSV_FILENAME_EXTENSION)
//        .append(append)
//        .hostname(hostname)
//        .port(port)
//        .build();
//  }

}
