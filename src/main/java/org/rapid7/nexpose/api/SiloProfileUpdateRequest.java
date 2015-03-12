/**
 * Copyright (C) 2012, Rapid7 LLC, Boston, MA, USA.
 * All rights reserved.
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * * Neither the name of the <organization> nor the
 * names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
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
 * Silo profile update request template.
 *
 * @author Christopher Lee.
 *
 */
public class SiloProfileUpdateRequest extends TemplateAPIRequest
{
   /**
    * Constructs the silo profile update request.
    *
    * @param sessionID The session ID.
    * @param syncID The sync ID.
    * @param hasGlobalReportTemplates "true" iff the silo should have global report templates.
    * @param hasGlobalEngines "true" iff the silo has access to global scan engines.
    * @param hasGlobalScanTemplates "true" iff the silo has access to global scan templates.
    * @param hasLicensedModules "true" iff the silo has licensed modules.
    * @param description The silo description. The description for the silo being defined.
    * @param id The silo id.
    * @param name The silo name.
    * @param globalReportTemplates The global report template
    * @param globalScanEngines The global scan engine.
    * @param globalScanTemplates The global scan template.
    * @param licensedModules The license module.
    */
   public SiloProfileUpdateRequest(
      String sessionID,
      String syncID,
      String hasGlobalReportTemplates,
      String hasGlobalEngines,
      String hasGlobalScanTemplates,
      String hasLicensedModules,
      String description,
      String id,
      String name,
      IContentGenerator globalReportTemplates,
      IContentGenerator globalScanEngines,
      IContentGenerator globalScanTemplates,
      IContentGenerator licensedModules)
   {
      super(sessionID, syncID);
      set("hasGlobalReportTemplates", hasGlobalReportTemplates);
      set("hasGlobalEngines", hasGlobalEngines);
      set("hasGlobalScanTemplates", hasGlobalScanTemplates);
      set("hasLicensedModules", hasLicensedModules);
      set("description", description);
      set("id", id);
      set("name", name);
      set("globalReportTemplate", globalReportTemplates);
      set("globalScanEngine", globalScanEngines);
      set("globalScanTemplate", globalScanTemplates);
      set("licensedModule", licensedModules);
      m_firstSupportedVersion = APISupportedVersion.V1_2;
      m_lastSupportedVersion = APISupportedVersion.V1_2;
   }
}
