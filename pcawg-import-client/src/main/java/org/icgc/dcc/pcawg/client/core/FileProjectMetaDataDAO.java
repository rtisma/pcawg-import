package org.icgc.dcc.pcawg.client.core;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FileProjectMetaDataDAO implements ProjectMetadataDAO {
  public static enum YO{
   FIRST,
    SECOND;
  }


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
  private final String tcgaBarcodeMappingFilename;

  @Override
  public String getMatchedSampleId(String aliquot_id) {
    return null;
  }

  @Override
  public String getAnalyzedSampleId(String aliquot_id) {
    return null;
  }

  @Override
  public String getDccProjectCode(String aliquot_id) {
    return null;
  }

}
