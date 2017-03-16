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
import lombok.val;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static org.icgc.dcc.pcawg.client.config.ClientProperties.OUTPUT_TSV_DIRECTORY;
import static org.icgc.dcc.pcawg.client.config.ClientProperties.STORAGE_BYPASS_MD5_CHECK;
import static org.icgc.dcc.pcawg.client.config.ClientProperties.STORAGE_OUTPUT_VCF_STORAGE_DIR;
import static org.icgc.dcc.pcawg.client.config.ClientProperties.STORAGE_PERSIST_MODE;
import static org.icgc.dcc.pcawg.client.config.ClientProperties.TOKEN;
import static org.icgc.dcc.pcawg.client.config.ClientProperties.USE_HDFS;

@SpringBootApplication
public class ClientMain implements CommandLineRunner {

  private static final boolean REQUIRE_INDEX_CFG = false;

  @Override
  @SneakyThrows
  public void run(String... args) {

    /*
      TODO: skip using -D, start using cmd line
      --token (string)
      --hdfs (true/false)
      --output-dir (string, default tsv.epoch)
      --persist-downloads  (true/false)
      --bypass-md5 (true/false)
      --output-storage-dir (string)
     */
    val importer = Importer.builder()
        .token(TOKEN)
        .hdfsEnabled(USE_HDFS)
        .outputVcfDir(STORAGE_OUTPUT_VCF_STORAGE_DIR)
        .persistVcfDownloads(STORAGE_PERSIST_MODE)
        .bypassMD5Check(STORAGE_BYPASS_MD5_CHECK)
        .outputTsvDir(OUTPUT_TSV_DIRECTORY)
        .build();
    importer.run();

  }


  public static void main(String... args) {
    SpringApplication.run(ClientMain.class, args);
  }

}
