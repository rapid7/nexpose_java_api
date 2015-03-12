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
 * Encapsulates the EnginePoolUpdateRequest NeXpose API request.
 *
 * @author Meera Muthuswami
 */
public class EnginePoolUpdateRequest extends TemplateAPIRequest
{
   /////////////////////////////////////////////////////////////////////////
   // Public methods
   /////////////////////////////////////////////////////////////////////////

   /**
    * Constructs a engine pool update request with its associated API version
    * information.
    *
    * @param sessionId The session to submit the request with. May not be {@code null} nor empty and must
    * be a 40 character hex {@link String}.
    * @param syncId The sync id to identify the response. May be {@code null}.
    * @param enginePoolId The id of the engine pool. May not be {@code null} nor empty and must be a positive number.
    * @param enginePoolName The name of the engine pool. May not be {@code null} nor empty and must be less than 255 character.
    * @param enginePoolScope The scope of the engine pool. May not be {@code null} nor empty and must be less than 255 character.
    * @param enginesGenerator The content generator instance. May not be {@code null} nor empty.
    */
   public EnginePoolUpdateRequest(String sessionId, String syncId, String enginePoolId, String enginePoolName, String enginePoolScope,
      IContentGenerator enginesGenerator)
   {
      super(sessionId, syncId);
      set("enginePoolID", enginePoolId);
      set("enginePoolName", enginePoolName);
      set("enginePoolScope", enginePoolScope);
      set("enginesGenerator", enginesGenerator);
      m_firstSupportedVersion = APISupportedVersion.V1_2;
      m_lastSupportedVersion = APISupportedVersion.V1_2;
   }
}
