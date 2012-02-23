package org.rapid7.nexpose.api;

import org.rapid7.nexpose.api.APISession.APISupportedVersion;

/**
 * Encapsulates the EnginePoolListingRequest NeXpose API request.
 *
 * @author Meera Muthuswami
 */
public class EnginePoolListingRequest extends TemplateAPIRequest
{
   /////////////////////////////////////////////////////////////////////////
   // Public methods
   /////////////////////////////////////////////////////////////////////////

   /**
    * Constructs a engine pool listing request with its associated API version
    * information.
    * 
    * @param sessionId The session to submit the request with. May not be {@code null} nor empty and must
    * be a 40 character hex {@link String}.
    * @param syncId The sync id to identify the response. May be {@code null}.
    */
   public EnginePoolListingRequest(String sessionId, String syncId)
   {
      super(sessionId, syncId);
      m_firstSupportedVersion = APISupportedVersion.V1_2;
      m_lastSupportedVersion = APISupportedVersion.V1_2;
   }
}
