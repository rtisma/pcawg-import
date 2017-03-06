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
package org.icgc.dcc.pcawg.client.download;

import com.fasterxml.jackson.databind.JsonNode;
import static com.google.common.net.HttpHeaders.AUTHORIZATION;
import static org.icgc.dcc.pcawg.client.config.ClientProperties.TOKEN;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.val;
import static org.icgc.dcc.common.core.json.Jackson.DEFAULT;

import java.net.HttpURLConnection;
import java.net.URL;

public class Storage {

  @SneakyThrows
  public static URL getObjectUrl(@NonNull final String api, @NonNull final String objectId) {
    val storageUrl = new URL(api + "/download/" + objectId + "?offset=0&length=-1&external=true");
    val connection = (HttpURLConnection) storageUrl.openConnection();
    connection.setRequestProperty(AUTHORIZATION, "Bearer " + TOKEN);
    val object = readObject(connection);
    return getUrl(object);
  }

  @SneakyThrows
  private static URL getUrl(JsonNode object) {
    return new URL(object.get("parts").get(0).get("url").textValue());
  }

  @SneakyThrows
  private static JsonNode readObject(@NonNull final HttpURLConnection connection) {
    return DEFAULT.readTree(connection.getInputStream());
  }

}
