package org.icgc.dcc.pcawg.client.download;

import lombok.val;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.icgc.dcc.pcawg.client.core.Factory.newSampleMetadataDAO;
import static org.icgc.dcc.pcawg.client.model.metadata.file.PortalFilename.newPortalFilename;
import static org.icgc.dcc.pcawg.client.model.metadata.project.SampleSheetModel.newSampleSheetModel;
import static org.icgc.dcc.pcawg.client.model.metadata.project.Uuid2BarcodeSheetModel.newUuid2BarcodeSheetModel;

public class SampleMetadataDAOTest {

  @Test
  public void testSampleSheetParser(){
    val tsvLine = "  \ta spaceA 1\tb spaceB 2\tc spaceC 3\td spaceD 4\te spaceE 5\tf spaceF 6\tg spaceG 7\th spaceH 8\ti spaceI 9\tj spaceJ 10\tk spaceK 11\tl spaceL 12\t         ";

    val sampleSheet = newSampleSheetModel(tsvLine);
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
    val sheet = newUuid2BarcodeSheetModel(tsvLine);
    assertThat(sheet.getUuid()).isEqualTo("c spaceC 3");
    assertThat(sheet.getTcgaBarcode()).isEqualTo("d spaceD 4");
  }

  @Test
  public void testFetchSampleMetadata(){
    val sampleMetadataDAO = newSampleMetadataDAO();
    val nonUsFilename = "10cb8ac6-c622-11e3-bf01-24c6515278c0.dkfz-copyNumberEstimation_1-0-189-hpc-fix.1508271624.somatic.cnv.vcf.gz";
    val usFilename = "9c70688d-6e43-4520-9262-eaae4e4d597d.broad-snowman.20150827.somatic.sv.vcf.gz";
    val nonUsId = newPortalFilename(nonUsFilename);
    val usId = newPortalFilename(usFilename);

    //Non-US
    val nonUsProjectData = sampleMetadataDAO.fetchSampleMetadata(nonUsId);
    assertThat(nonUsProjectData.getDccProjectCode()).isEqualTo("LIRI-JP");
    assertThat(nonUsProjectData.getAnalyzedSampleId()).isEqualTo("RK001_C01");
    assertThat(nonUsProjectData.getMatchedSampleId()).isEqualTo("RK001_B01");

    //US
    val usProjectData = sampleMetadataDAO.fetchSampleMetadata(usId);
    assertThat(usProjectData.getDccProjectCode()).isEqualTo("BRCA-US");
    assertThat(usProjectData.getAnalyzedSampleId()).isEqualTo("TCGA-BH-A18R-01A-11D-A19H-09");
    assertThat(usProjectData.getMatchedSampleId()).isEqualTo("TCGA-BH-A18R-11A-42D-A19H-09");

  }

}
