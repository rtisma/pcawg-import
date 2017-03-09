package org.icgc.dcc.pcawg.client.download;

import lombok.val;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import static org.icgc.dcc.pcawg.client.core.FileProjectMetaDataDAO.SampleSheetModel.newSampleSheetModelFromTSVLine;

public class ProjectMetadataDAOTest {

  @Test
  public void testSampleSheetModel(){
    val sampleSheetTSVLine = "  \ta spaceA 1\tb spaceB 2\tc spaceC 3\td spaceD 4\te spaceE 5\tf spaceF 6\tg spaceG 7\th spaceH 8\ti spaceI 9\tj spaceJ 10\tk spaceK 11\tl spaceL 12\t         ";

    val sampleSheet = newSampleSheetModelFromTSVLine(sampleSheetTSVLine);
    Assertions.assertThat(sampleSheet.getAliquotId()).isEqualTo("f spaceF 6");
    Assertions.assertThat(sampleSheet.getDccProjectCode()).isEqualTo("e spaceE 5");
    Assertions.assertThat(sampleSheet.getDccSpecimenType()).isEqualTo("k spaceK 11");
    Assertions.assertThat(sampleSheet.getDonorUniqueId()).isEqualTo("a spaceA 1");
    Assertions.assertThat(sampleSheet.getLibraryStrategy()).isEqualTo("l spaceL 12");
    Assertions.assertThat(sampleSheet.getSubmitterSampleId()).isEqualTo("i spaceI 9");


  }

}
