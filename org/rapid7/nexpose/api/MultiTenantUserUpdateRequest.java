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

/**
 * Represents the MultiTenantUserUpdateRequest NeXpose API request.
 *
 * @author Leonardo Varela
 */
public class MultiTenantUserUpdateRequest extends TemplateAPIRequest
{
   /////////////////////////////////////////////////////////////////////////
   // Public methods
   /////////////////////////////////////////////////////////////////////////

   /**
    * Creates a new MultiTenantUserUpdateRequest NeXpose API Request. Sets the first API supported version to 1.2 and
    * the last supported version to 1.2.
    *
    * NOTE: All parameters are strings or generators, since we want to be able to test edge cases and simulate incorrect
    * usage of the tool for robustness
    *
    * @param sessionId the session to be used if different from the currently acquired one. This is a String of 40
    *        characters.
    * @param syncId The String that uniquely identifies the response associated with the request sent. This field is
    *        optional.
    * @param id the String that represents the id of the Multi Tenant User.
    * @param fullName The full name of the Multi Tenant User.
    * @param authsrcid The authentication source used by the Multi Tenant User.
    * @param email The email of the Multi Tenant User.
    * @param password The password of the Multi Tenant User.
    * @param enabled whether the Multi Tenant User is enabled or not.
    * @param userName the user name or diplay name of the Multi Tenant User.
    * @param superuser the flag that tells whether a Multi Tenant User is a super user or not.
    * @param siloAccessGenerator the generator of the silos accesses.
    */
   public MultiTenantUserUpdateRequest(
      String sessionId,
      String syncId,
      String id,
      String fullName,
      String authsrcid,
      String email,
      String password,
      String enabled,
      String userName,
      String superuser,
      IContentGenerator siloAccessGenerator)
   {
      super(sessionId, syncId);
      set("authSrcId", authsrcid);
      set("email", email);
      set("password", password);
      set("enabled", enabled);
      set("full-name", fullName);
      set("id", id);
      set("user-name", userName);
      set("superuser", superuser);
      set("siloAccessGenerator", siloAccessGenerator);
      m_firstSupportedVersion = APISupportedVersion.V1_2;
      m_lastSupportedVersion = APISupportedVersion.V1_2;
   }
}
