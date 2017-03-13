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
package org.icgc.dcc.pcawg.client.vcf;

import com.google.common.collect.ImmutableSet;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@RequiredArgsConstructor
@Getter
public enum WorkflowTypes {

  CONSENSUS("consensus", ImmutableSet.of(" PCAWG SNV-MNV callers", "PCAWG InDel callers")),
  SANGER("sanger", ImmutableSet.of("Sanger variant call pipeline")),
  DKFZ_EMBL("dkfz/embl", ImmutableSet.of("DKFZ/EMBL variant call pipeline")),
  BROAD("broad", ImmutableSet.of("Broad variant call pipeline")),
  MUSE("muse", ImmutableSet.of("MUSE variant call pipeline") );

  @NonNull
  private String realName;

  @NonNull
  private Set<String> portalSoftwareNames;

  public boolean equals(@NonNull final String name) {
    return getRealName().equals(name);
  }

  public boolean isIn(@NonNull final String name) {
    return name.matches("^" + getRealName() + ".*");
  }

  @Override
  public String toString() {
    return getRealName();
  }
}
