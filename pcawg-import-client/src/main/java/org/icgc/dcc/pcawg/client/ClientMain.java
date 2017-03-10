/*
 * Copyright (c) 2017 The Ontario Institute for Cancer Research. All rights reserved.
 *
 * This program and the accompanying materials are made available under the terms of the GNU Public License v3.0.
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT
 * SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED
 * TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER
 * IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN
 * ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.icgc.dcc.pcawg.client;

import htsjdk.variant.vcf.VCFFileReader;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.icgc.dcc.common.core.util.Joiners;
import org.icgc.dcc.pcawg.client.model.ssm.primary.impl.IndelSSMPrimary;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;

import static org.icgc.dcc.pcawg.client.Factory.newConsensusPortalFileDownloader;
import static org.icgc.dcc.pcawg.client.Factory.newProjectMetadataDAO;
import static org.icgc.dcc.pcawg.client.Factory.newTransformer;
import static org.icgc.dcc.pcawg.client.model.ssm.metadata.impl.SSMMetadataImpl.newSSMMetadataImpl;
import static org.icgc.dcc.pcawg.client.tsv.impl.SSMMetadataTSVConverter.newSSMMetadataTSVConverter;
import static org.icgc.dcc.pcawg.client.tsv.impl.SSMPrimaryTSVConverter.newSSMPrimaryTSVConverter;

@Slf4j
@SpringBootApplication
public class ClientMain implements CommandLineRunner {

  private static final boolean REQUIRE_INDEX_CFG = false;
  private static final boolean ALLOW_MISSING_FIELDS_IN_HEADER_CFG = true;
  private static final boolean OUTPUT_TRAILING_FORMAT_FIELDS_CFG = true;

  @Override
  @SneakyThrows
  public void run(String... args) {
    log.info("****** PCAWG VCF Import Client ******");
    log.info("Passed arguments: {}", Arrays.toString(args));

    val projectMetadataDAO = newProjectMetadataDAO();
    val consensusPortalFileDownloader = newConsensusPortalFileDownloader();
    val ssmMetadataTsvConverter = newSSMMetadataTSVConverter();
    val ssmPrimaryTsvConverter = newSSMPrimaryTSVConverter();
    val transformer = newTransformer();
    val tsvPrimaryHeader = ssmPrimaryTsvConverter.toTSVHeader();
    val tsvMetadataHeader = ssmMetadataTsvConverter.toTSVHeader();
    log.info("MetaDataHeader: {}", tsvMetadataHeader);
    log.info("PrimaryHeader: {}", tsvPrimaryHeader);
    int count = 6;
    for(val fileContext : consensusPortalFileDownloader){
      if (count++ > 5){
        break;
      }
      val file = fileContext.getFile();
      val fileMetaData = fileContext.getFileMetaData();
      val atiquotId =  fileMetaData.getVcfFilenameParser().getObjectId();
      val projectData = projectMetadataDAO.getProjectDataByAliquotId(atiquotId);
      val dccProjectCode =   projectData.getDccProjectCode();
      val matchedSampleId =  projectData.getMatchedSampleId();
      val analyzedSampleId = projectData.getAnalyzedSampleId();
      val workflow = fileMetaData.getVcfFilenameParser().getCallerId();
      val dataType = fileMetaData.getVcfFilenameParser().getSubMutationType();
      val analysisId = Joiners.UNDERSCORE.join(dccProjectCode, workflow, dataType );

      val ssmMetadata = newSSMMetadataImpl(atiquotId,workflow, matchedSampleId, analysisId,analyzedSampleId);
      val tsvMetadataData = ssmMetadataTsvConverter.toTSVData(ssmMetadata);


      log.info("AbsFile: {}\t\tFMD: {}", file.getAbsoluteFile().toString(), fileMetaData);
      val vcf = new VCFFileReader(file, REQUIRE_INDEX_CFG );
      for (val variantContext : vcf){
        val ssmPrimary = IndelSSMPrimary.newIndelSSMPrimary(variantContext, analysisId,analyzedSampleId);
        val tsvPrimaryData = ssmPrimaryTsvConverter.toTSVData(ssmPrimary);
      }


    }
//    consensusPortalFileDownloader.stream().forEach(f -> log.info(f.getAbsoluteFile().toString()));
//    consensusPortalFileDownloader.stream().forEach(f -> transformer.transform(CONSENSUS));



  }

  public static void main(String... args) {
    SpringApplication.run(ClientMain.class, args);
  }

}
