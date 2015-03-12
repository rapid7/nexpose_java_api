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
 * Encapsulates the ReportTemplateSaveRequest NeXpose API request.
 *
 * @author Murali Rongali
 */
public class ReportTemplateSaveRequest extends TemplateAPIRequest
{
   /////////////////////////////////////////////////////////////////////////
   // Public methods
   /////////////////////////////////////////////////////////////////////////

   /**
    * Constructs a ReportTemplateSaveRequest with its associated API version
    * information.
    *
    * @param sessionId The session to submit the request with. May not be {@code null} nor empty and must
    * be a 40 character hex {@link String}.
    * @param syncId The sync id to identify the response. May be {@code null}.
    * @param reportTemplateId The id of the report template.
    * @param reportTemplateName The name of the report template.
    * @param reportTemplateScope The scope for the report template.
    * @param description The description for the report template.
    * @param showDeviceNames The setting for the report template whether to show the device names in reports or not.
    * @param reportSectionsGenerator a Generator that knows how to output report section of
    *        report sections that are to be associated with the report template save. e.g.
    *        &lt;ReportSection name="DatabaseListing"/&gt;. Please see
    *    {@link ReportTemplateSaveRequestSectionsGenerator} for a reference
    *    implementation. For QA testing you should construct your own
    *    {@link IContentGenerator} to generate all the edge test cases you
    *    can think of.
    */
   public ReportTemplateSaveRequest(
      String sessionId,
      String syncId,
      String reportTemplateId,
      String reportTemplateName,
      String reportTemplateScope,
      String description,
      String showDeviceNames,
      IContentGenerator reportSectionsGenerator)
   {
      super(sessionId, syncId);
      set("reportTemplateId", reportTemplateId);
      set("reportTemplateName", reportTemplateName);
      set("reportTemplateScope", reportTemplateScope);
      set("description", description);
      set("showDeviceNames", showDeviceNames);
      set("reportSectionsGenerator", reportSectionsGenerator);
      m_firstSupportedVersion = APISupportedVersion.V1_0;
      m_lastSupportedVersion = APISupportedVersion.V1_1;
   }
}
