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
import lombok.Builder;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.icgc.dcc.pcawg.client.core.FileWriterContext;
import org.icgc.dcc.pcawg.client.download.Storage;
import org.icgc.dcc.pcawg.client.model.ssm.primary.SSMPrimary;
import org.icgc.dcc.pcawg.client.model.ssm.primary.impl.SnvMnvPcawgSSMPrimary;
import org.icgc.dcc.pcawg.client.vcf.DataTypes;

import static org.icgc.dcc.pcawg.client.config.ClientProperties.SSM_M_TSV_FILENAME;
import static org.icgc.dcc.pcawg.client.config.ClientProperties.SSM_P_TSV_FILENAME;
import static org.icgc.dcc.pcawg.client.core.Factory.newMetadataContainer;
import static org.icgc.dcc.pcawg.client.core.Factory.newSSMMetadata;
import static org.icgc.dcc.pcawg.client.core.Factory.newSSMMetadataTransformer;
import static org.icgc.dcc.pcawg.client.core.Factory.newSSMPrimaryTransformer;
import static org.icgc.dcc.pcawg.client.model.ssm.primary.impl.IndelPcawgSSMPrimary.newIndelSSMPrimary;

@Slf4j
@Builder
public class Importer implements Runnable {

  private static final boolean REQUIRE_INDEX_CFG = false;

  @NonNull
  private final String token;
  private final boolean hdfsEnabled;

  @NonNull
  private final String outputVcfDir;
  private final boolean persistVcfDownloads;
  private final boolean bypassMD5Check;

  @NonNull
  private final String outputTsvDir;

  @NonNull
  private final String hdfsHostname;

  @NonNull
  private final String hdfsPort;

  private final boolean append;

  private FileWriterContext.FileWriterContextBuilder createFileWriterContextBuilder(String dccProjectCode){
    return FileWriterContext.builder()
        .dccProjectCode(dccProjectCode)
        .outputDirectory(outputTsvDir)
        .hostname(hdfsHostname)
        .port(hdfsPort)
        .append(append);
  }
  private FileWriterContext createSSMPrimaryFileWriterContext(String dccProjectCode){
    return createFileWriterContextBuilder(dccProjectCode)
        .outputTsvFilename(SSM_P_TSV_FILENAME)
        .build();
  }

  private FileWriterContext createSSMMetadataFileWriterContext(String dccProjectCode){
    return createFileWriterContextBuilder(dccProjectCode)
        .outputTsvFilename(SSM_M_TSV_FILENAME)
        .build();
  }

  @Override
  @SneakyThrows
  public void run() {
    // Create container with all MetadataContexts
    val metadataContainer = newMetadataContainer();

    // Create storage manager for downloading files
    val storage = Storage.newStorage(persistVcfDownloads, outputVcfDir, bypassMD5Check, token);

    val totalMetadataContexts = metadataContainer.getTotalMetadataContexts();
    int countMetadataContexts = 0;

    val totalDccProjectCodes = metadataContainer.getDccProjectCodes().size();
    int countDccProjectCodes  = 0;

    for (val dccProjectCode : metadataContainer.getDccProjectCodes()){
      log.info("Processing DccProjectCode ( {} / {} ): {}", ++countDccProjectCodes, totalDccProjectCodes, dccProjectCode);

      //Create FileWriterContexts for this dccProjectCode
      val ssmPrimaryFileWriterContext = createSSMPrimaryFileWriterContext(dccProjectCode);
      val ssmMetadataFileWriterContext = createSSMMetadataFileWriterContext(dccProjectCode);

      //Create transformers for this dccProjectCode
      val ssmPrimaryTransformer  = newSSMPrimaryTransformer(ssmPrimaryFileWriterContext, hdfsEnabled);
      val ssmMetadataTransformer = newSSMMetadataTransformer(ssmMetadataFileWriterContext,hdfsEnabled);

      for (val metadataContext : metadataContainer.getMetadataContexts(dccProjectCode)){
        val sampleMetadata = metadataContext.getSampleMetadata();
        val portalMetadata = metadataContext.getPortalMetadata();

        // Download file
        val file = storage.downloadFile(portalMetadata);

        val dataType = sampleMetadata.getDataType();

        log.info("Loading File ( {} / {} ): {}", ++countMetadataContexts, totalMetadataContexts, portalMetadata.getPortalFilename().getFilename());

        //Write SSM Metadata to file
        val ssmMetadata = newSSMMetadata(sampleMetadata);
        ssmMetadataTransformer.transform(ssmMetadata);

        // Write SSM Primary to file
        val vcf = new VCFFileReader(file, REQUIRE_INDEX_CFG);
        for (val variant : vcf){
          SSMPrimary ssmPrimary = null;
          //TODO: clean up this hardcoding. Create VCF class that does this conversion and processing, and ecapsulated this logic
            if (dataType == DataTypes.INDEL){
              ssmPrimary = newIndelSSMPrimary(
                          variant,
                          sampleMetadata.getAnalysisId(),
                          sampleMetadata.getAnalyzedSampleId());
            } else if(dataType == DataTypes.SNV_MNV){
              ssmPrimary = SnvMnvPcawgSSMPrimary.newSnvMnvSSMPrimary(
                  variant,
                  sampleMetadata.getAnalysisId(),
                  sampleMetadata.getAnalyzedSampleId());
            } else {
              throw new IllegalStateException("The dataType "+dataType.getName()+" is unrecognized");
            }
          ssmPrimaryTransformer.transform(ssmPrimary);
        }
      }
      ssmPrimaryTransformer.close();
      ssmMetadataTransformer.close();
    }
  }

}
