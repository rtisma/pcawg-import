package org.icgc.dcc.pcawg.client.core;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import lombok.Builder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.Value;
import lombok.val;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;
import static org.icgc.dcc.pcawg.client.core.FileProjectMetaDataDAO.SampleSheetModel.newSampleSheetModelFromTSVLine;

@RequiredArgsConstructor
public class FileProjectMetaDataDAO implements ProjectMetadataDAO {

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

  // create pojo for each file
      // need following fields:
      // - subitter_sample_id, donor_uniqie_id, aliquot_id, dcc_specimen_type, library_strategy
  // open files, read everyline and split by tab, and load into respecive pojos
  //

  /**
   * Use opencsv to bind (or marshal) TSVs (or CSVs that use tabs) to Beans, and can parse whole csv file into list of beans, where each bean can represent a row, and then can store List of beans in memory and do what ever aggrigations needed
   * Refer to http://opencsv.sourceforge.net/
   */
  @Builder
  @Value
  public static class SampleSheetModel {

    private static final int ALIQUOT_ID_POS = 5;
    private static final int DONOR_UNIQUE_ID_POS = 0;
    private static final int SUBMITTER_SAMPLE_ID_POS = 8;
    private static final int DCC_SPECIMEN_TYPE = 10;
    private static final int LIBRARY_STRATEGY = 11;
    private static final int MAX_NUM_COLUMNS = 12;


    @NonNull
    private final String donorUniqueId;

    @NonNull
    private final String aliquotId;

    @NonNull
    private final String submitterSampleId;

    @NonNull
    private final String dccSpecimenType;

    @NonNull
    private final String libraryStrategy;


    public static SampleSheetModel newSampleSheetModelFromTSVLine(String tsvLine){
      val array = tsvLine.split("\t");
      checkArgument(array.length == MAX_NUM_COLUMNS);
      return SampleSheetModel.builder()
          .aliquotId(array[ALIQUOT_ID_POS])
          .dccSpecimenType(array[DCC_SPECIMEN_TYPE])
          .donorUniqueId(array[DONOR_UNIQUE_ID_POS])
          .libraryStrategy(array[LIBRARY_STRATEGY])
          .submitterSampleId(array[SUBMITTER_SAMPLE_ID_POS])
          .build();
    }

  }

  @Builder
  @Value
  public static class Uuid2BarcodeSheetModel{
    private static final int UUID_POS = 2;
    private static final int TCGA_BARCODE_POS = 3;
    private static final int MAX_NUM_COLUMNS = 4;

    @NonNull
    private final String uuid;

    @NonNull
    private final String tcgaBarcode;

    public static Uuid2BarcodeSheetModel newUuid2BarcodeSheetModelFromTSVLine(String tsvLine){
      val array = tsvLine.split("\t");
      checkArgument(array.length == MAX_NUM_COLUMNS);
      return Uuid2BarcodeSheetModel.builder()
          .tcgaBarcode(array[TCGA_BARCODE_POS])
          .uuid(array[UUID_POS])
          .build();
    }

  }

  @NonNull
  private final String sampleSheetFilename;

  @NonNull
  private final String uuid2BarcodeSheetFilename;

  private final List<SampleSheetModel> sampleSheetList;
  private final List<Uuid2BarcodeSheetModel> uuid2BarcodeSheetList;

  public FileProjectMetaDataDAO(String sampleSheetFilename, String uuid2BarcodeSheetFilename) {
    this.sampleSheetFilename = sampleSheetFilename;
    this.uuid2BarcodeSheetFilename = uuid2BarcodeSheetFilename;
    this.sampleSheetList = readSampleSheet();
    this.uuid2BarcodeSheetList = readUuid2BarcodeSheet();
  }

  @Override
  public String getAnalyzedSampleId(String aliquot_id) {
    return null;
  }

  @Override
  public String getMatchedSampleId(String aliquot_id) {
    return null;
  }


  @Override
  public String getDccProjectCode(String aliquot_id) {
    return null;
  }

  @SneakyThrows
  private List<SampleSheetModel> readSampleSheet2(){
    // check filename exists
    // for each line, create model and store in List
    val file = new File(sampleSheetFilename);
    checkState(file.exists(), "File %s DNE", sampleSheetFilename);
    val br = new BufferedReader(new FileReader(file));
    String line;
    val builder = ImmutableList.<SampleSheetModel>builder();
    while((line = br.readLine()) != null){
      builder.add(newSampleSheetModelFromTSVLine(line));
    }
    br.close();
    return builder.build();
  }

  private List<SampleSheetModel> readSampleSheet(){
    return readTsv(sampleSheetFilename, SampleSheetModel::newSampleSheetModelFromTSVLine);
  }

  private List<Uuid2BarcodeSheetModel> readUuid2BarcodeSheet(){
    return readTsv(uuid2BarcodeSheetFilename, Uuid2BarcodeSheetModel::newUuid2BarcodeSheetModelFromTSVLine);
  }

  @SneakyThrows
  private static <T> List<T> readTsv(String filename, Function<String, T > lineConvertionFunctor){
    // check filename exists
    // for each line, create model and store in List
    val file = new File(filename);
    checkState(file.exists(), "File %s DNE", filename);
    val br = new BufferedReader(new FileReader(file));
    String line;
    val builder = ImmutableList.<T>builder();
    while((line = br.readLine()) != null){
      builder.add(lineConvertionFunctor.apply(line));
    }
    br.close();
    return builder.build();
  }


}
