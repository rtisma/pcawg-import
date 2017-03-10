package org.icgc.dcc.pcawg.client.core;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkState;
import static org.icgc.dcc.pcawg.client.config.ClientProperties.SAMPLE_SHEET_TSV_FILENAME;
import static org.icgc.dcc.pcawg.client.config.ClientProperties.SAMPLE_SHEET_TSV_URL;
import static org.icgc.dcc.pcawg.client.config.ClientProperties.UUID2BARCODE_SHEET_TSV_FILENAME;
import static org.icgc.dcc.pcawg.client.config.ClientProperties.UUID2BARCODE_SHEET_TSV_URL;
import static org.icgc.dcc.pcawg.client.core.ProjectMetadataDAO.isUSProject;
import static org.icgc.dcc.pcawg.client.download.Storage.downloadFileByURL;

@Slf4j
public class FileProjectMetadataDAO implements ProjectMetadataDAO {

  private static final String WGS = "WGS";
  private static final String NORMAL = "normal";

  /**
   * - get dcc_project_code (https://raw.githubusercontent.com/ICGC-TCGA-PanCancer/pcawg-operations/develop/lists/sample_sheet/pcawg_sample_sheet.2016-10-18.tsv)  using aliquite id
   * - determine if US or non-US project
   * - if NON US project
   *      - from sample_sheet, extract "submitter_sample_id" for that aliquot_id, and this is the "analyzedSampleId"
   *      - from sample_sheet, extract "donor_unique_id" for that aliquot_id, and then aggregate all matching donor_unique_ids, then use criteria "dcc_specimen_type" CONTAINS string "normal" and "library_strategy" == WGS, you should then have 1 row remaining, then you should extract "submitter_sample_id" and this is the "matched_sample_id"
   *
   * -if US project
   *    - from sample_sheet, extract  "submitter_sample_id" for that aliquot_id, then map it to the TCGA barcode using (https://raw.githubusercontent.com/ICGC-TCGA-PanCancer/pcawg-operations/develop/lists/pc_annotation-tcga_uuid2barcode.tsv) and this gives you the "analyzedSampleId)
   *    - from sample_sheet, extract "donor_unique_id" for that aliquot_id, and then aggregate all matching donor_unique_ids, then use criteria "dcc_specimen_type" CONTAINS string "normal" and "library_strategy" == WGS, you should then have 1 row remaining, then you should extract "submitter_sample_id", then find TCGA BARCODE via uuid2barcode tsv, and this gives you matched_sample_id
   *
   *
   *
   */
  @NonNull
  private final String sampleSheetFilename;

  @NonNull
  private final String uuid2BarcodeSheetFilename;

  private final List<SampleSheetModel> sampleSheetList;
  private final List<Uuid2BarcodeSheetModel> uuid2BarcodeSheetList;

  public FileProjectMetadataDAO(String sampleSheetFilename, String uuid2BarcodeSheetFilename) {
    this.sampleSheetFilename = sampleSheetFilename;
    this.uuid2BarcodeSheetFilename = uuid2BarcodeSheetFilename;
    this.sampleSheetList = readSampleSheet();
    this.uuid2BarcodeSheetList = readUuid2BarcodeSheet();
  }

  private SampleSheetModel getFirstSampleSheetByAliquotId(String aliquotId){
    val aliquotIdStream= sampleSheetList.stream()
        .filter(s -> s.getAliquotId().equals(aliquotId));

    val aliquotIdResult = aliquotIdStream.findFirst();
    checkState(aliquotIdResult.isPresent(), "Could not find first SampleSheet for aliquot_id [%s]", aliquotId);
    return aliquotIdResult.get();
  }

  private String getAnalyzedSampleId(boolean isUsProject, String submitterSampleId ){
    if (isUsProject){
      val result = createSubmitterSampleIdStream(submitterSampleId).findFirst();
      checkState(result.isPresent(),
        "Could not find SampleSheet with submitter_sample_id [%s]", submitterSampleId );
      return result.get().getTcgaBarcode();
    } else {
      return submitterSampleId;
    }
  }

  private String getMatchedSampleId(boolean isUsProject, String donorUniqueId ){
    val result = createDonorUniqueIdStream(donorUniqueId).findFirst();
    checkState(result.isPresent(), "Could not find SampleSheet for with donor_unique_id [%s] and library_strategy [%s] and dcc_speciment_type containing [%s]", donorUniqueId, WGS, NORMAL);
    val submitterSampleId = result.get().getSubmitterSampleId();
    return getAnalyzedSampleId(isUsProject, submitterSampleId);
  }

  @Override
  public ProjectMetadata getProjectMetadataByAliquotId(String aliquotId){
    val sampleSheetByAliquotId = getFirstSampleSheetByAliquotId(aliquotId);
    val dccProjectCode = sampleSheetByAliquotId.getDccProjectCode();
    val submitterSampleId = sampleSheetByAliquotId.getSubmitterSampleId();
    val donorUniqueId = sampleSheetByAliquotId.getDonorUniqueId();
    val isUsProject =  isUSProject(dccProjectCode);

    val analyzedSampleId = getAnalyzedSampleId(isUsProject,submitterSampleId);
    val matchedSampleId = getMatchedSampleId(isUsProject,donorUniqueId);
    return ProjectMetadata.builder()
        .analyzedSampleId(analyzedSampleId)
        .dccProjectCode(dccProjectCode)
        .matchedSampleId(matchedSampleId)
        .build();
  }

  private Stream<Uuid2BarcodeSheetModel> createSubmitterSampleIdStream(String submitterSampleId){
    return uuid2BarcodeSheetList.stream()
        .filter(s -> s.getUuid().equals(submitterSampleId));
  }

  private Stream<SampleSheetModel> createDonorUniqueIdStream(String donorUniqueId){
    return sampleSheetList.stream()
        .filter(x -> x.getDonorUniqueId().equals(donorUniqueId))
        .filter(y -> y.getLibraryStrategy().equals(WGS))
        .filter(z -> z.getDccSpecimenType().toLowerCase().contains(NORMAL));
  }

  private List<SampleSheetModel> readSampleSheet(){
    return readTsv(sampleSheetFilename, true, SampleSheetModel::newSampleSheetModelFromTSVLine);
  }

  private List<Uuid2BarcodeSheetModel> readUuid2BarcodeSheet(){
    return readTsv(uuid2BarcodeSheetFilename, true, Uuid2BarcodeSheetModel::newUuid2BarcodeSheetModelFromTSVLine);
  }

  @SneakyThrows
  private static <T> List<T> readTsv(@NonNull  String filename,
      final boolean hasHeader,
      @NonNull Function<String, T > lineConvertionFunctor){
    val file = new File(filename);
    checkState(file.exists(), "File %s DNE", filename);
    val br = new BufferedReader(new FileReader(file));
    String line;
    val builder = ImmutableList.<T>builder();
    boolean skipLine = hasHeader;
    while((line = br.readLine()) != null){
      if (!skipLine) {
        builder.add(lineConvertionFunctor.apply(line));
      }
      skipLine = false;
    }
    br.close();
    return builder.build();
  }

  public static FileProjectMetadataDAO newFileProjectMetadataDAOAndDownload(){
    val outputDir = Paths.get("").toAbsolutePath().toString();
    log.info("Downloading [{}] to directory [{}] from url: {}", SAMPLE_SHEET_TSV_FILENAME, outputDir,SAMPLE_SHEET_TSV_URL);
    val sampleSheetFile = downloadFileByURL(SAMPLE_SHEET_TSV_URL, SAMPLE_SHEET_TSV_FILENAME);
    log.info("Downloading [{}] to directory [{}] from url: {}", UUID2BARCODE_SHEET_TSV_FILENAME, outputDir, UUID2BARCODE_SHEET_TSV_URL);
    val uuid2BarcodeSheetFile = downloadFileByURL(UUID2BARCODE_SHEET_TSV_URL, UUID2BARCODE_SHEET_TSV_FILENAME);
    log.info("Done downloading, creating FileProjectMetadataDAO");
    return new FileProjectMetadataDAO(SAMPLE_SHEET_TSV_FILENAME, UUID2BARCODE_SHEET_TSV_FILENAME);
  }



}
