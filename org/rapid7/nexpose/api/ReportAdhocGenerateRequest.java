/**
 * Copyright (C) 2012, Rapid7 LLC, Boston, MA, USA.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *    * Redistributions of source code must retain the above copyright
 *      notice, this list of conditions and the following disclaimer.
 *    * Redistributions in binary form must reproduce the above copyright
 *      notice, this list of conditions and the following disclaimer in the
 *      documentation and/or other materials provided with the distribution.
 *    * Neither the name of the <organization> nor the
 *      names of its contributors may be used to endorse or promote products
 *      derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.rapid7.nexpose.api;

import org.rapid7.nexpose.api.APISession.APISupportedVersion;
import org.rapid7.nexpose.api.generators.IContentGenerator;

/**
 * Encapsulates the ReportAdhocGenerateRequest NeXpose API request.
 *
 * @author Murali Rongali
 */
public class ReportAdhocGenerateRequest extends TemplateAPIRequest
{
   /////////////////////////////////////////////////////////////////////////
   // Public methods
   /////////////////////////////////////////////////////////////////////////

   /**
    * Constructs a ReportAdhocGenerateRequest with its associated API version
    * information.
    *
    * @param sessionId The session to submit the request with. May not be {@code null} nor empty and must
    * be a 40 character hex {@link String}.
    * @param syncId The sync id to identify the response. May be {@code null}.
    * @param reportFormat The format of the Adhoc report.
    * @param reportTemplateId The id of the report template.
    * @param compareTo The date to use as the baseline scan in ISO 8601 format, like YYYYMMDDTHHMMSSss.
    * @param filtersGenerator a Generator that knows how to output filter section of
    *        filter sections that are to be associated with the Adhoc report generate. e.g.
    *        &lt;Filter id="1" type="site"/&gt;. Please see
    *    {@link ReportFiltersContentGenerator} for a reference
    *    implementation. For QA testing you should construct your own
    *    {@link IContentGenerator} to generate all the edge test cases you
    *    can think of.
    */
   public ReportAdhocGenerateRequest(
      String sessionId,
      String syncId,
      String reportFormat,
      String reportTemplateId,
      String compareTo,
      IContentGenerator filtersGenerator)
   {
      super(sessionId, syncId);
      set("reportFormat", reportFormat);
      set("reportTemplateId", reportTemplateId);
      set("compareTo", compareTo);
      set("filtersGenerator", filtersGenerator);
      m_firstSupportedVersion = APISupportedVersion.V1_0;
      m_lastSupportedVersion = APISupportedVersion.V1_1;
   }
}
