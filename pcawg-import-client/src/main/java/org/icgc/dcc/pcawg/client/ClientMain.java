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
import org.icgc.dcc.pcawg.client.config.ClientProperties;
import org.icgc.dcc.pcawg.client.download.fetcher.FetcherFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;

@Slf4j
@SpringBootApplication
public class ClientMain implements CommandLineRunner {

  @Override
  @SneakyThrows
  public void run(String... args) {
    log.info("****** PCAWG VCF Import Client ******");
    log.info("Passed arguments: {}", Arrays.toString(args));

    val fetcher = FetcherFactory.builder()
        .setAllFiles(ClientProperties.FETCHER_STORAGE_FILENAME, ClientProperties.FETCHER_FORCE_NEW_FILE)
        .setLimit(15)
        .build();

    val fileMetaDataContext = fetcher.fetch();
    val storage = Factory.newStorage();
    for (val fileMetaData : fileMetaDataContext){
      log.info("Downloading: {}", fileMetaData.getVcfFilenameParser().getFilename());
      val file = storage.downloadFile(fileMetaData);
    }



  }

  public static void main(String... args) {
    SpringApplication.run(ClientMain.class, args);
  }

}
