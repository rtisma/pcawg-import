package org.icgc.dcc.pcawg.client.download;

import lombok.val;
import org.icgc.dcc.pcawg.client.core.FileProjectMetadataDAO;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.icgc.dcc.pcawg.client.core.FileProjectMetadataDAO.SampleSheetModel.newSampleSheetModelFromTSVLine;
import static org.icgc.dcc.pcawg.client.core.FileProjectMetadataDAO.Uuid2BarcodeSheetModel.newUuid2BarcodeSheetModelFromTSVLine;

public class ProjectMetadataDAOTest {

  @Test
  public void testSampleSheetParser(){
    val tsvLine = "  \ta spaceA 1\tb spaceB 2\tc spaceC 3\td spaceD 4\te spaceE 5\tf spaceF 6\tg spaceG 7\th spaceH 8\ti spaceI 9\tj spaceJ 10\tk spaceK 11\tl spaceL 12\t         ";

    val sampleSheet = newSampleSheetModelFromTSVLine(tsvLine);
    assertThat(sampleSheet.getAliquotId()).isEqualTo("f spaceF 6");
    assertThat(sampleSheet.getDccProjectCode()).isEqualTo("e spaceE 5");
    assertThat(sampleSheet.getDccSpecimenType()).isEqualTo("k spaceK 11");
    assertThat(sampleSheet.getDonorUniqueId()).isEqualTo("a spaceA 1");
    assertThat(sampleSheet.getLibraryStrategy()).isEqualTo("l spaceL 12");
    assertThat(sampleSheet.getSubmitterSampleId()).isEqualTo("i spaceI 9");
  }

  @Test
  public void testUuid2BarcodeSheetParser() {
    val tsvLine = "  \ta spaceA 1\tb spaceB 2\tc spaceC 3\td spaceD 4\t   ";
    val sheet = newUuid2BarcodeSheetModelFromTSVLine(tsvLine);
    assertThat(sheet.getUuid()).isEqualTo("c spaceC 3");
    assertThat(sheet.getTcgaBarcode()).isEqualTo("d spaceD 4");
  }

  @Test
  public void testDownload(){
    val projectMetadataDAO = FileProjectMetadataDAO.newFileProjectMetadataDAOAndDownload();
    val nonUsId = "10cb8ac6-c622-11e3-bf01-24c6515278c0";
    val usId = "9c70688d-6e43-4520-9262-eaae4e4d597d";
    //Non-US
    assertThat(projectMetadataDAO.getDccProjectCode(nonUsId)).isEqualTo("LIRI-JP");
    assertThat(projectMetadataDAO.getAnalyzedSampleId(nonUsId)).isEqualTo("RK001_C01");
    assertThat(projectMetadataDAO.getMatchedSampleId(nonUsId)).isEqualTo("RK001_B01");

    //US
    assertThat(projectMetadataDAO.getDccProjectCode(usId)).isEqualTo("BRCA-US");
    assertThat(projectMetadataDAO.getAnalyzedSampleId(usId)).isEqualTo("TCGA-BH-A18R-01A-11D-A19H-09");
    assertThat(projectMetadataDAO.getMatchedSampleId(usId)).isEqualTo("TCGA-BH-A18R-11A-42D-A19H-09");

  }

}
