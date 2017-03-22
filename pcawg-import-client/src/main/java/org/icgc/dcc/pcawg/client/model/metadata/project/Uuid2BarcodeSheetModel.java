package org.icgc.dcc.pcawg.client.model.metadata.project;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import lombok.val;

import static com.google.common.base.Preconditions.checkArgument;

@Value
@Builder
public class Uuid2BarcodeSheetModel {
  private static final int UUID_POS = 2;
  private static final int TCGA_BARCODE_POS = 3;
  private static final int MAX_NUM_COLUMNS = 4;

  @NonNull
  private final String uuid;

  @NonNull
  private final String tcgaBarcode;

  public static Uuid2BarcodeSheetModel newUuid2BarcodeSheetModel(String tsvLine){
    val array = tsvLine.trim().split("\t");
    checkArgument(array.length == MAX_NUM_COLUMNS, "Max allowed columns is %s, but input columns is %s", MAX_NUM_COLUMNS, array.length);
    return Uuid2BarcodeSheetModel.builder()
        .tcgaBarcode(array[TCGA_BARCODE_POS])
        .uuid(array[UUID_POS])
        .build();
  }

}
