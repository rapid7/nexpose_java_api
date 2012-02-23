/**
 * Copyright (C) 2010, Rapid7 LLC, Boston, MA, USA.
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
import org.rapid7.nexpose.api.generators.*;

/**
 * The report save request template.
 *
 * @author Allen Jensen.
 */
public class ReportSaveRequest extends TemplateAPIRequest
{
  /**
   * Constructs the report delete request.
   *
   * @param reportID The report ID.
   * @param sessionID The session ID.
   * @param syncID The sync ID.
   */
  public ReportSaveRequest(String sessionID, String syncID,
                           String generateNow,
                           String reportID,
                           String name,
                           String templateID,
                           String format,
                           String owner,
                           String timezone,
                           String description,
                           IContentGenerator filtersGenerator,
                           IContentGenerator baselineGenerator,
                           IContentGenerator generateGenerator,
                           IContentGenerator deliveryGenerator,
                           IContentGenerator dbExportGenerator)
  {
    super(sessionID, syncID);
    ReportTemplateIDGenerator templateIDGenerator = new ReportTemplateIDGenerator(templateID);
    set("generate-now",generateNow);
    set("id", reportID);
    set("name", name);

    // Special case - must not be empty string, so have to use a generator.
    set("templateIDGenerator", templateIDGenerator);

    set("format", format);
    set("owner", owner);
    set("timezone", timezone);
    set("description", description);

    set("filtersGenerator", filtersGenerator);
    set("baselineGenerator", baselineGenerator);
    set("generateGenerator", generateGenerator);
    set("deliveryGenerator", deliveryGenerator);
    set("dbExportGenerator", dbExportGenerator);

    m_firstSupportedVersion = APISupportedVersion.V1_0;
    m_lastSupportedVersion = APISupportedVersion.V1_1;
  }
}
