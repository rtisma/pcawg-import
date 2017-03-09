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

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;

import static org.icgc.dcc.pcawg.client.Factory.newConsensusPortalFileDownloader;
import static org.icgc.dcc.pcawg.client.Factory.newProjectMetadataDAO;
import static org.icgc.dcc.pcawg.client.Factory.newTransformer;

@Slf4j
@SpringBootApplication
public class ClientMain implements CommandLineRunner {

  @Override
  @SneakyThrows
  public void run(String... args) {
    log.info("****** PCAWG VCF Import Client ******");
    log.info("Passed arguments: {}", Arrays.toString(args));

    val projectMetadataDAO = newProjectMetadataDAO();
    val consensusPortalFileDownloader = newConsensusPortalFileDownloader();
    val transformer = newTransformer();
    int count = 0;
    for(val fileContext : consensusPortalFileDownloader){
      if (count++ > 5){
        break;
      }
      val file = fileContext.getFile();
      val fileMetaData = fileContext.getFileMetaData();
      log.info("AbsFile: {}\t\tFMD: {}", file.getAbsoluteFile().toString(), fileMetaData);

    }
//    consensusPortalFileDownloader.stream().forEach(f -> log.info(f.getAbsoluteFile().toString()));
//    consensusPortalFileDownloader.stream().forEach(f -> transformer.transform(CONSENSUS));



  }

  public static void main(String... args) {
    SpringApplication.run(ClientMain.class, args);
  }

}
