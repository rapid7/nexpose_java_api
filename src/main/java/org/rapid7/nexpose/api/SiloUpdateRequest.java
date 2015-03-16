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
import org.rapid7.nexpose.api.domain.SiloConfigStorage;
import org.rapid7.nexpose.api.generators.IContentGenerator;

/**
 * Represents the SiloUpdateRequest NeXpose API request.
 *
 * @author Leonardo Varela
 */
public class SiloUpdateRequest extends TemplateAPIRequest
{
   /////////////////////////////////////////////////////////////////////////
   // Public methods
   /////////////////////////////////////////////////////////////////////////

   /**
    * Creates a new SiloUpdateRequest NeXpose API Request. Sets the first API supported version to 1.2 and the last
    * supported version to 1.2.
    *
    * NOTE: All parameters are strings or generators, since we want to be able to test edge cases and simulate incorrect
    * usage of the tool for robustness
    *
    * @param sessionId the session to be used if different from the currently acquired one. This is a String of 40
    *        characters.
    * @param syncId The String that uniquely identifies the response associated with the request sent. This field is
    *        optional.
    * @param id the String that represents the id of the Silo.
    * @param name the name of the silo.
    * @param description the description of the silo.
    * @param siloProfileId the id of the silo profile associated with the silo.
    * @param maxExternalAssets the number of maximum external assets of the silo.
    * @param maxInternalAssets the number of max internal assets of the silo.
    * @param maxUsers the max number of users that the silo can create.
    * @param merchantGenerator the PCI merchant information of the silo.
    * @param organizationURL the URL of the organization of the silo.
    * @param storage the storage information of the silo.
    */
   public SiloUpdateRequest(
         String sessionId,
         String syncId,
         String id,
         String name,
         String description,
         String siloProfileId,
         String maxAssets,
         String maxUsers,
         String dbID,
         IContentGenerator merchantGenerator,
         IContentGenerator organizationURLGenerator,
         SiloConfigStorage storage)
   {
      super(sessionId, syncId);
      set("maxAssets", maxAssets);
      set("maxUsers", maxUsers);
      set("description", description);
      set("name", name);
      set("id", id);
      set("silo-profile-id", siloProfileId);
      set("db-id", dbID);
      set("merchantGenerator", merchantGenerator);
      set("organizationGenerator", organizationURLGenerator);
      set("storageUserID", storage != null? storage.getUserID() : null);
      set("storageDBMS", storage != null? storage.getDBMS() : null);
      set("storageRealm", storage != null? storage.getUserRealm() : null);
      set("storagePassword", storage != null? storage.getPassword() : null);
      set("storageURL", storage != null? storage.getURL() : null);
      set("storagePropertiesGenerator", storage != null? storage.getPropertiesGenerator() : null);
      m_firstSupportedVersion = APISupportedVersion.V1_2;
      m_lastSupportedVersion = APISupportedVersion.V1_2;
   }
}
