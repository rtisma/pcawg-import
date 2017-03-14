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
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;

import static org.icgc.dcc.pcawg.client.Factory.newMetadataContainer;
import static org.icgc.dcc.pcawg.client.Factory.newSSMMetadata;
import static org.icgc.dcc.pcawg.client.Factory.newSSMMetadataTransformer;
import static org.icgc.dcc.pcawg.client.Factory.newSSMPrimaryTransformer;
import static org.icgc.dcc.pcawg.client.Factory.newStorage;
import static org.icgc.dcc.pcawg.client.model.ssm.primary.impl.IndelPcawgSSMPrimary.newIndelSSMPrimary;

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

    val metadataContainer = newMetadataContainer();
    val storage = newStorage();
    for (val dccProjectCode : metadataContainer.getDccProjectCodes()){
      val ssmPrimaryTransformer = newSSMPrimaryTransformer(dccProjectCode);
      val ssmMetadataTransformer = newSSMMetadataTransformer(dccProjectCode);
      for (val metadataContext : metadataContainer.getMetadataContextsForDccProjectCode(dccProjectCode)){
        val sampleMetadata = metadataContext.getSampleMetadata();
        val fileMetaData = metadataContext.getFileMetaData();
        val file = storage.downloadFile(fileMetaData);
        val dataType = sampleMetadata.getDataType();
        log.info("File: {}", file.getAbsoluteFile().toString());
        log.info("ProjectCode: {}", sampleMetadata);
        log.info("FileMetaData: {}", fileMetaData);

        val ssmMetadata = newSSMMetadata(sampleMetadata);
        ssmMetadataTransformer.transform(ssmMetadata);
        val vcf = new VCFFileReader(file, REQUIRE_INDEX_CFG);
        for (val variant : vcf){
            if (dataType.toLowerCase().contains("indel")){
                val ssmPrimary = newIndelSSMPrimary(
                          variant,
                          sampleMetadata.getAnalysisId(),
                          sampleMetadata.getAnalyzedSampleId());
                ssmPrimaryTransformer.transform(ssmPrimary);
            } else if(dataType.toLowerCase().contains("snv_mnv")){


            } else {
              throw new IllegalStateException("The dataType "+dataType+" is unrecognized");
            }
        }
      }
      ssmPrimaryTransformer.close();
      ssmMetadataTransformer.close();
    }
  }


  public static void main(String... args) {
    SpringApplication.run(ClientMain.class, args);
  }

}
