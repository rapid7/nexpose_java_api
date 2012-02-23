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

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.xml.parsers.*;

import org.rapid7.nexpose.api.domain.*;
import org.rapid7.nexpose.api.generators.MultiTenantUserConfigSiloAccessGenerator;
import org.rapid7.nexpose.api.generators.SiteSaveRequestAlertsGenerator;
import org.rapid7.nexpose.api.generators.SiteSaveRequestCredentialsGenerator;
import org.rapid7.nexpose.api.generators.SiteSaveRequestHostsGenerator;
import org.rapid7.nexpose.api.generators.SiteSaveRequestRangesGenerator;
import org.rapid7.nexpose.api.generators.UserSaveRequestGroupsGenerator;
import org.rapid7.nexpose.api.generators.UserSaveRequestSitesGenerator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Encapsulates an API session to NeXpose that can be used to make requests.
 * Initializes the Secure Socket Layer subsystem used for all connections
 *
 * @author Chad Loder
 * @author Leonardo Varela
 */
public class APISession implements Session
{
   /////////////////////////////////////////////////////////////////////////
   // Public methods
   /////////////////////////////////////////////////////////////////////////

   /**
    * Enumeration type for the different versions of the APItool.'
    * NOTE: The order is important since that is how the {@link Comparable}
    * order works.
    *
    * @author Leonardo Varela
    */
   public enum APISupportedVersion
   {
      V1_0("1.0"), V1_1("1.1"), V1_2("1.2");
      private String m_version;
      
      APISupportedVersion(String value)
      {
         m_version = value;
      }
      String getVersion()
      {
         return m_version;
      }
   }

   /**
    * Constructs a new APISession that will connect to the given URL.
    *
    * @param nxURL The base URL of the NeXpose server's API endpoint.
    * @param username The user name for logging in.
    * @param password The password for logging in.
    * @throws MalformedURLException When the url/protocol/version provided are
    * malformed.
    */
   public APISession(
      final URL nxURL,
      final String protocol,
      APISupportedVersion version,
      final String username,
      String password) throws MalformedURLException
   {
      if (nxURL == null)
      {
         throw new IllegalArgumentException("nxURL cannot be null");
      }
      if (username == null)
      {
         throw new IllegalArgumentException("username cannot be null");
      }
      if (password == null)
      {
         throw new IllegalArgumentException("password cannot be null");
      }
      m_nxURL = nxURL;
      m_username = username;
      m_password = password;
      // all 1.0 api version should be redirected to 1.1
      if (version == APISupportedVersion.V1_0)
         version = APISupportedVersion.V1_1;
      m_apiVersion = version;
      m_apiProtocol = protocol;
      m_apiURL = new URL(m_nxURL.toString()
         + "/api/"
         + m_apiVersion.getVersion()
         + "/"
         + m_apiProtocol);
   }

   /**
    * Sets the error handler for this session.
    *
    * @param errorHandler The {@link IAPIErrorHandler} error handler to set.
    */
   @Override
   public void setErrorHandler(final IAPIErrorHandler errorHandler)
   {
      m_errorHandler = errorHandler;
   }

   /**
    * Clears the error handler and sets the default one
    */
   @Override
   public void clearErrorHandler()
   {
      m_errorHandler = new DefaultAPIErrorHandler();
   }

   /**
    * Retrieves the API response from the last listing operation
    * @return the APIResponse associated with the latest listing request:
    * <OL>
    *    <LI>UserListingRequest</LI>
    *    <LI>EngineListingRequest</LI>
    *    <LI>AssetGroupListingRequest</LI>
    *    <LI>SiteListingRequest</LI>
    * </OL>
    */
   @Override
   public APIResponse getListingAPIResponse()
   {
      return m_apiResponse;
   }

   /**
    * Opens a connection and sends a user defined XML string though the wire.
    *
    * @param rawXML The Raw XML string to be sent to the NeXpose instance in
    *        session
    * @param version the version of the API to be used.
    * @return APIResponse the response from the server.
    * @throws IOException when the connection to the NeXpose instance fails.
    * @throws APIException When there is a problem processing the API request.
    */
   @Override
   public APIResponse sendRawXMLRequest(
      String rawXML,
      APISupportedVersion version)
      throws IOException, APIException
   {
      final RawXMLAPIRequest request = new RawXMLAPIRequest(rawXML, version);
      final APIResponse response;
      response = new APIResponse(
         request(open(request), auth(request)),
         request.getRequestXML());
      return response;
   }

  /**
   * Opens a connection and sends a user defined XML string though the wire.
   *
   * @param rawXML The Raw XML string to be sent to the NeXpose instance in
   *        session
   * @param version the version of the API to be used.
   * @param outputStream stream to write the part after the API Response to. Concatinates if more than 1 part...
   * @return APIResponse the response from the server.
   * @throws IOException when the connection to the NeXpose instance fails.
   * @throws APIException When there is a problem processing the API request.
   */
  //@Override
  public APIResponse sendRawXMLRequest(
     String rawXML,
     APISupportedVersion version,
     OutputStream outputStream)
     throws IOException, APIException
  {
     final RawXMLAPIRequest request = new RawXMLAPIRequest(rawXML, version);
     final APIResponse response;
     Document responseDoc = multiPartRequest(open(request), auth(request), outputStream);

    response = new APIResponse(
       responseDoc,
       request.getRequestXML());
    return response;
  }


   /**
    * Opens a connection and logs in to the NeXpose API.
    *
    * @param syncId the synchronization id to identify the response associated 
    *        with the response in asynchronous environments. It can be any 
    *        string. This field is optional.
    * @return APIResponse the response from the server.
    * @throws IOException when the connection to the NeXpose instance fails.
    * @throws APIException When there is a problem processing the API request.
    */
   @Override
   public APIResponse login(String syncId) throws IOException, APIException
   {
      final LoginRequest request = new LoginRequest(
         syncId,
         m_username,
         m_password);
      final APIResponse response = new APIResponse(
         request(open(request), auth(request)),
         request.getRequestXML());
      if (response.grabNode("//Failure") != null)
      {
         m_errorHandler.handleError(request, response, this, "Login failed");
      }
      m_sessionID = response.grab("/LoginResponse/@session-id");
      return response;
   }

   /**
    * Opens a connection and logs in to the NeXpose API.
    *
    * @param syncId the synchronization id to identify the response associated
    *        with the response in asynchronous environments. It can be any
    *        string. This field is optional.
    * @param The silo id that the user is logging into
    * @return APIResponse the response from the server.
    * @throws IOException when the connection to the NeXpose instance fails.
    * @throws APIException When there is a problem processing the API request.
    */
   @Override
   public APIResponse login(String syncId, String siloId) throws IOException, APIException
   {
      final SiloLoginRequest request = new SiloLoginRequest(syncId,m_username,m_password, siloId);
      final APIResponse response = new APIResponse(request(open(request), auth(request)), request.getRequestXML());

      if (response.grabNode("//Failure") != null)
      {
         m_errorHandler.handleError(request, response, this, "Login failed");
      }

      m_sessionID = response.grab("/LoginResponse/@session-id");
      return response;
   }

   /**
    * Logs out of the NeXpose API.
    *
    * @param sessionId the session to be used if different from the current
    *        acquired one (You acquire one when you authenticate correctly with
    *        the login method in the {@link APISession} class). This is a
    *        String of 40 characters.
    * @param syncId the synchronization id to identify the response associated 
    *        with the response in asynchronous environments. It can be any 
    *        string. This field is optional.
    * @return APIResponse the response from the server.
    * @throws IOException when the connection to the NeXpose instance fails.
    * @throws APIException When there is a problem processing the API request.
    */
   @Override
   public APIResponse logout(
      String sessionId,
      String syncId)
      throws IOException, APIException
   {
      final TemplateAPIRequest request = new LogoutRequest(sessionId, syncId);
      final APIResponse response = new APIResponse(
         request(open(request), auth(request)),
         request.getRequestXML());
      m_sessionID = null;
      if (response.grabNode("//Failure") != null)
      {
         m_errorHandler.handleError(request, response, this, "Logout failed");
      }
      return response;
   }

   /**
    * Lists all of the sites.
    * 
    * @param sessionId the session to be used if different from the current
    *        acquired one (You acquire one when you authenticate correctly with
    *        the login method in the {@link APISession} class). This is a
    *        String of 40 characters.
    * @param syncId the synchronization id to identify the response associated 
    *        with the response in asynchronous environments. It can be any 
    *        string. This field is optional.
    * @return a list of sites of type (@link SiteSummary}.
    * @throws IOException When the API call cannot be performed.
    * @throws APIException if the API call is not successful or the parsing of
    *         the response is not correct.
    */
   @Override
   public Iterable<SiteSummary> listSites(
      String sessionId,
      String syncId)
      throws IOException, APIException
   {
      List<SiteSummary> sitesList = null;
      final TemplateAPIRequest request = new SiteListingRequest(
         sessionId,
         syncId);
      final APIResponse response = new APIResponse(
         request(open(request), auth(request)),
         request.getRequestXML());
      m_apiResponse = response;
      if (response.grabNode("//Failure") != null)
      {
         m_errorHandler.handleError(
            request,
            response,
            this,
            "SiteListingRequest failed");
         return null;
      }
      final NodeList sitesNodes = 
         response.grabNodes("/SiteListingResponse/SiteSummary");
      if (sitesNodes != null)
      {
         sitesList = new ArrayList<SiteSummary>();
         for (int i = 0; i < sitesNodes.getLength(); i++)
         {
            Element siteNode = (Element) sitesNodes.item(i);
            SiteSummary siteSummary = new SiteSummary(siteNode);
            sitesList.add(siteSummary);
         }
      }
      return sitesList;
   }

  /**
   * Request Site Configuration.
   *
   * @param sessionId the session to be used if different from the current
   *        acquired one (You acquire one when you authenticate correctly with
   *        the login method in the {@link APISession} class). This is a
   *        String of 40 characters.
   * @param syncId the synchronization id to identify the response associated
   *        with the response in asynchronous environments. It can be any
   *        string. This field is optional.
   * @param siteId The id of the site.
   *
   * @return The configuration of the site.
   * @throws IOException When the API call cannot be performed.
   * @throws APIException if the API call is not successful or the parsing of
   *         the response is not correct.
   */
  @Override
  public APIResponse siteConfigRequest(String sessionId, String syncId, String siteId) throws IOException, APIException
  {
    final TemplateAPIRequest request = new SiteConfigRequest(sessionId,syncId,siteId);
    return new APIResponse(request(open(request), auth(request)),request.getRequestXML());
  }

  /**
    * Lists all of the sites.
    *
    * @param sessionId the session to be used if different from the current
    *        acquired one (You acquire one when you authenticate correctly with
    *        the login method in the {@link APISession} class). This is a
    *        String of 40 characters.
    * @param syncId the synchronization id to identify the response associated 
    *        with the response in asynchronous environments. It can be any 
    *        string. This field is optional.
    * @return a list of sites of type (@link SiteSummary}.
    * @throws IOException When the API call cannot be performed.
    * @throws APIException if the API call is not successful or the parsing of
    *         the response is not correct.
    */
   @Override
   public Iterable<AssetGroupSummary> listAssetGroups(
      String sessionId,
      String syncId)
      throws IOException, APIException
   {
      List<AssetGroupSummary> assetGroupList = null;
      final TemplateAPIRequest request = new AssetGroupListingRequest(
         sessionId,
         syncId);
      final APIResponse response = new APIResponse(
         request(open(request), auth(request)),
         request.getRequestXML());
      m_apiResponse = response;
      if (response.grabNode("//Failure") != null)
      {
         m_errorHandler.handleError(
            request,
            response,
            this,
            "AssetGroupListingRequest failed");
         return null;
      }
      final NodeList assetGroupNodes = 
         response.grabNodes("/AssetGroupListingResponse/AssetGroupSummary");
      if (assetGroupNodes != null)
      {
         assetGroupList = new ArrayList<AssetGroupSummary>();
         for (int i = 0; i < assetGroupNodes.getLength(); i++)
         {
            Element assetGroupNode = (Element) assetGroupNodes.item(i);
            AssetGroupSummary assetGroupSummary = 
               new AssetGroupSummary(assetGroupNode);
            assetGroupList.add(assetGroupSummary);
         }
      }
      return assetGroupList;
   }

   /**
    * Lists all of the users.
    *
    * @param sessionId the session to be used if different from the current
    *        acquired one (You acquire one when you authenticate correctly with
    *        the login method in the {@link APISession} class). This is a
    *        String of 40 characters.
    * @param syncId the synchronization id to identify the response associated 
    *        with the response in asynchronous environments. It can be any 
    *        string. This field is optional.
    * @return a list of users of type (@link UserSummary}.
    * @throws IOException When the API call cannot be performed.
    * @throws APIException if the API call is not successful or the parsing of
    *         the response is not correct.
    */
   @Override
   public Iterable<UserSummary> listUsers(
      String sessionId,
      String syncId)
      throws IOException, APIException
   {
      final TemplateAPIRequest request = new UserListingRequest(
         sessionId,
         syncId);
      List<UserSummary> usersList = null;
      final APIResponse response = new APIResponse(
         request(open(request), auth(request)),
         request.getRequestXML());
      m_apiResponse = response;
      if (response.grabNode("//Failure") != null)
      {
         m_errorHandler.handleError(
            request,
            response,
            this,
            "UserListingRequest failed");
      }
      final NodeList users =
         response.grabNodes("/UserListingResponse/UserSummary");
      if (users != null)
      {
         usersList = new ArrayList<UserSummary>();
         for (int i = 0; i < users.getLength(); i++)
         {
            Element userNode = (Element) users.item(i);
            UserSummary userSummary = new UserSummary(userNode);
            usersList.add(userSummary);
         }
      }
      return usersList;
   }

   /**
    * Lists all of the engines.
    * 
    * @param sessionId the session to be used if different from the current
    *        acquired one (You acquire one when you authenticate correctly with
    *        the login method in the {@link APISession} class). This is a
    *        String of 40 characters.
    * @param syncId the synchronization id to identify the response associated 
    *        with the response in asynchronous environments. It can be any 
    *        string. This field is optional.
    * @return a list of engines of type (@link EngineSummary}.
    * @throws IOException When the API call cannot be performed.
    * @throws APIException if the API call is not successful or the parsing of
    *         the response is not correct.
    */
   @Override
   public Iterable<EngineSummary> listEngines(
      String sessionId,
      String syncId)
      throws IOException, APIException
   {
      final TemplateAPIRequest request = new EngineListingRequest(
         sessionId,
         syncId);
      List<EngineSummary> enginesList = null;
      final APIResponse response = new APIResponse(
         request(open(request), auth(request)),
         request.getRequestXML());
      m_apiResponse = response;
      if (response.grabNode("//Failure") != null)
      {
         m_errorHandler.handleError(
            request,
            response,
            this,
            "EngineListingRequest failed");
      }
      final NodeList engines =
         response.grabNodes("/EngineListingResponse/EngineSummary");
      if (engines != null)
      {
         enginesList = new ArrayList<EngineSummary>();
         for (int i = 0; i < engines.getLength(); i++)
         {
            Element engineNode = (Element) engines.item(i);
            EngineSummary engineSummary = new EngineSummary(engineNode);
            enginesList.add(engineSummary);
         }
      }
      return enginesList;
   }

   /**
    * Saves an Engine through the NeXpose API.
    *
    * @param sessionId the session to be used if different from the current
    *        acquired one (You acquire one when you authenticate correctly with
    *        the login method in the {@link APISession} class). This is a
    *        String of 40 characters.
    * @param syncId the synchronization id to identify the response associated 
    *        with the response in asynchronous environments. It can be any 
    *        string. This field is optional.
    * @param engineConfigId the engine configuration id.
    * @param engineConfigName the engine configuration name.
    * @param engineConfigAddress the engine configuration address.
    * @param engineConfigPort the engine configuration port.
    * @param engineConfigPriority the engine configuration priority.
    * @param sitesGenerator the {@link IContentGenerator} of the sites within
    *        the engine save request.
    * @return APIResponse the response from the server.
    * @throws IOException When the API call cannot be performed.
    * @throws APIException if the API call is not successful or the parsing of
    *         the response is not correct.
    */
   @Override
   public APIResponse engineSaveRequest(
      String sessionId,
      String syncId,
      String engineConfigId,
      String engineConfigName,
      String engineConfigAddress,
      String engineConfigPort,
      String engineConfigPriority,
      String engineConfigScope,
      IContentGenerator sitesGenerator)
      throws IOException, APIException
   {
      final TemplateAPIRequest request = new EngineSaveRequest(
         sessionId,
         syncId,
         engineConfigId,
         engineConfigName,
         engineConfigAddress,
         engineConfigPort,
         engineConfigPriority,
         engineConfigScope,
         sitesGenerator);
      final APIResponse response = new APIResponse(
         request(open(request), auth(request)),
         request.getRequestXML());
      if (response.grabNode("//Failure") != null)
      {
         m_errorHandler.handleError(
            request,
            response,
            this,
            "EngineSaveRequest failed");
      }
      return response;
   }

   /**
    * Saves an Asset Group through the NeXpose API.
    *
    * @param sessionId the session to be used if different from the current
    *        acquired one (You acquire one when you authenticate correctly with
    *        the login method in the {@link APISession} class). This is a
    *        String of 40 characters.
    * @param syncId the synchronization id to identify the response associated 
    *        with the response in asynchronous environments. It can be any 
    *        string. This field is optional.
    * @param assetGroupId the id of the asset group to save.
    * @param assetGroupName the name of the asset group to save.
    * @param assetGroupDescription the description of the group of the asset
    *        group to save.
    * @param assetGroupRiskScore the risk score associated to the asset group to
    *        save.
    * @param devicesGenerator the {@link IContentGenerator} that knows how to 
    *        include devices inside of the AssetGroupSaveRequest.
    * @return APIResponse the response from the server.
    * @throws IOException When the API call cannot be performed.
    * @throws APIException if the API call is not successful or the parsing of
    *         the response is not correct.
    */
   @Override
   public APIResponse assetGroupSaveRequest(
      String sessionId,
      String syncId,
      String assetGroupId,
      String assetGroupName,
      String assetGroupDescription,
      String assetGroupRiskScore,
      IContentGenerator devicesGenerator)
      throws IOException, APIException
   {
      TemplateAPIRequest request = new AssetGroupSaveRequest(
         sessionId,
         syncId,
         assetGroupId,
         assetGroupName,
         assetGroupDescription,
         assetGroupRiskScore,
         devicesGenerator);
      final APIResponse response = new APIResponse(
         request(open(request), auth(request)),
         request.getRequestXML());
      if (response.grabNode("//Failure") != null)
      {
         m_errorHandler.handleError(
            request,
            response,
            this,
            "AssetGroupSaveRequest failed");
      }
      return response;
   }

   /**
    * Requests the activity associated with an Engine through the NeXpose API.
    *
    * @param sessionId the session to be used if different from the current
    *        acquired one (You acquire one when you authenticate correctly with
    *        the login method in the {@link APISession} class). This is a
    *        String of 40 characters.
    * @param syncId the synchronization id to identify the response associated 
    *        with the response in asynchronous environments. It can be any 
    *        string. This field is optional.
    * @param engineId The id of the engine to request the activity from.
    * @return APIResponse the response from the server.
    * @throws IOException When the API call cannot be performed.
    * @throws APIException if the API call is not successful or the parsing of
    *         the response is not correct.
    */
   @Override
   public APIResponse engineActivityRequest(
      String sessionId,
      String syncId,
      String engineId)
      throws IOException, APIException
   {
      final TemplateAPIRequest request = new EngineActivityRequest(
         sessionId,
         syncId,
         engineId);
      final APIResponse response = new APIResponse(
         request(open(request), auth(request)),
         request.getRequestXML());
      if (response.grabNode("//Failure") != null)
      {
         m_errorHandler.handleError(
            request,
            response,
            this,
            "EngineActivityRequest failed");
      }
      return response;
   }

   /**
    * Requests the configuration of an Engine through the NeXpose API.
    * 
    * @param sessionId the session to be used if different from the current
    *        acquired one (You acquire one when you authenticate correctly with
    *        the login method in the {@link APISession} class). This is a
    *        String of 40 characters.
    * @param syncId the synchronization id to identify the response associated 
    *        with the response in asynchronous environments. It can be any 
    *        string. This field is optional.
    * @param engineId The id of the engine to request the configuration from.
    * @return APIResponse the response from the server.
    * @throws IOException When the API call cannot be performed.
    * @throws APIException if the API call is not successful or the parsing of
    *         the response is not correct.
    */
   @Override
   public APIResponse engineConfigRequest(
      String sessionId,
      String syncId,
      String engineId)
      throws IOException, APIException
   {
      final TemplateAPIRequest request = new EngineConfigRequest(
         sessionId,
         syncId,
         engineId);
      final APIResponse response = new APIResponse(
         request(open(request), auth(request)),
         request.getRequestXML());
      if (response.grabNode("//Failure") != null)
      {
         m_errorHandler.handleError(
            request,
            response,
            this,
            "EngineConfigRequest failed");
      }
      return response;
   }

   /**
    * Requests to delete an Engine through the NeXpose API.
    * 
    * @param sessionId the session to be used if different from the current
    *        acquired one (You acquire one when you authenticate correctly with
    *        the login method in the {@link APISession} class). This is a
    *        String of 40 characters.
    * @param syncId the synchronization id to identify the response associated 
    *        with the response in asynchronous environments. It can be any 
    *        string. This field is optional.
    * @param engineId The id of the engine to request to delete.
    * @return APIResponse the response from the server.
    * @throws IOException When the API call cannot be performed.
    * @throws APIException if the API call is not successful or the parsing of
    *         the response is not correct.
    */
   @Override
   public APIResponse engineDeleteRequest(
      String sessionId,
      String syncId,
      String engineId)
      throws IOException, APIException
   {
      final TemplateAPIRequest request = new EngineDeleteRequest(
         sessionId,
         syncId,
         engineId);
      final APIResponse response = new APIResponse(
         request(open(request), auth(request)),
         request.getRequestXML());
      if (response.grabNode("//Failure") != null)
      {
         m_errorHandler.handleError(
            request,
            response,
            this,
            "EngineDeleteRequest failed");
      }
      return response;
   }
   
   /**
    * Requests to delete a Ticket through the NeXpose API.
    * 
    * @param sessionId the session to submit the request with. May not be {@code null} nor empty and must
    * be a 40 character hex {@link String}.
    * @param syncId the sync id to identify the response. May be {@code null}.
    * @param ticketsGenerator The id's of the tickets to request to delete. May be {@code null}.
    * 
    * @return The response from the server.
    * 
    * @throws IOException If an IO error occurs.
    * @throws APIException If the API call is not successful or the parsing of the response is not correct.
    */
   @Override
   public APIResponse ticketDeleteRequest(String sessionId, String syncId, IContentGenerator ticketsGenerator)
      throws IOException, APIException
   {
      final TemplateAPIRequest request = new TicketDeleteRequest(sessionId, syncId, ticketsGenerator);
      final APIResponse response = new APIResponse(request(open(request), auth(request)), request.getRequestXML());
      if (response.grabNode("//Failure") != null)
      {
         m_errorHandler.handleError(request, response, this, "TicketDeleteRequest failed");
      }
      return response;
   }
   
   /**
    * Requests a ticket's detail through the NeXpose API.
    * 
    * @param sessionId the session to submit the request with. May not be {@code null} nor empty and must
    * be a 40 character hex {@link String}.
    * @param syncId the sync id to identify the response. May be {@code null}.
    * @param ticketsGenerator The id's of the tickets to request to delete. May be {@code null}.
    * 
    * @return The response from the server.
    * 
    * @throws IOException If an IO error occurs.
    * @throws APIException If the API call is not successful or the parsing of the response is not correct.
    */
   
   @Override
   public APIResponse ticketDetailsRequest(String sessionId, String syncId, IContentGenerator ticketsGenerator)
      throws IOException, APIException
   {
      final TemplateAPIRequest request = new TicketDetailsRequest(sessionId, syncId, ticketsGenerator);
      final APIResponse response = new APIResponse(request(open(request), auth(request)), request.getRequestXML());
      if (response.grabNode("//Failure") != null)
      {
         m_errorHandler.handleError(request, response, this, "TicketDetailsRequest failed");
      }
      return response;
   }
   
   /**
    * List tickets given a set of filter.
    * 
    * @param sessionId the session to submit the request with. May not be {@code null} nor empty and must
    * be a 40 character hex {@link String}.
    * @param syncId the sync id to identify the response. May be {@code null}.
    * @param filtersGenerator the content generator instance. May be {@code null}.
    * 
    * @return a list of Tickets of type (@link TicketSummary}.
    * 
    * @throws IOException If an IO error occurs.
    * @throws APIException If the API call is not successful or the parsing of the response is not correct.
    */
   @Override
   public Iterable<TicketSummary> ticketListRequest(String sessionId,
      String syncId, 
      IContentGenerator filtersGenerator)
      throws IOException, APIException
   {
	      
      final TemplateAPIRequest request = new TicketListingRequest(sessionId, syncId, filtersGenerator);
      List<TicketSummary> ticketsList = null;
      final APIResponse response = new APIResponse(request(open(request), auth(request)), request.getRequestXML());
      m_apiResponse = response;
      if (response.grabNode("//Failure") != null)
      {
         m_errorHandler.handleError(request, response, this, "TicketListingRequest failed");
      }
      final NodeList tickets = response.grabNodes("/TicketListingResponse/TicketSummary");
      if (tickets != null)
      {
         ticketsList = new ArrayList<TicketSummary>();
         for (int i = 0; i < tickets.getLength(); i++)
         {
            Element ticketNode = (Element) tickets.item(i);
            TicketSummary ticketSummary = new TicketSummary(ticketNode);
            ticketsList.add(ticketSummary);
         }
      }
      return ticketsList;
   }
   
   /**
    * Creates a Ticket through the NeXpose API.
    * 
    * @param sessionId the session to submit the request with. May not be {@code null} nor empty and must
    * be a 40 character hex {@link String}.
    * @param syncId the sync id to identify the response. May be {@code null}.
    * @param ticketName the name of the ticket. May not be {@code null} nor empty and must be less than 255 character.
    * @param ticketPriority the priority of the ticket. May not be {@code null} nor empty and 
    * expected values are low, moderate, normal, high, critical.
    * @param ticketDeviceId  the device id of the ticket. May not be {@code null} nor empty.
    * @param ticketAssignedTo the name of the person to whom the ticket is assigned. May not be {@code null} nor empty.
    * @param vulnerabilitiesGenerator the content generator for the ticket vulnerabilities. May not be {@code null} nor empty.
    * @param commentsGenerator the content generator for the ticket comments. May be {@code null}.
    * 
    * @return The response from the server.
    * 
    * @throws IOException If an IO error occurs.
    * @throws APIException If the API call is not successful or the parsing of the response is not correct.
    */
   @Override
   public APIResponse ticketCreateRequest(
      String sessionId, 
      String syncId, 
      String ticketName, 
      String ticketPriority,
      String ticketDeviceId, 
      String ticketAssignedTo, 
      IContentGenerator vulnerabilitiesGenerator, 
      IContentGenerator commentsGenerator)
      throws IOException, APIException
   {
      TemplateAPIRequest request;
      request = new TicketCreateRequest(sessionId, syncId, ticketName, ticketPriority,
         ticketDeviceId, ticketAssignedTo, vulnerabilitiesGenerator, commentsGenerator);
      final APIResponse response = new APIResponse(request(open(request), auth(request)), request.getRequestXML());
      if (response.grabNode("//Failure") != null)
      {
         m_errorHandler.handleError(request, response, this, "TicketSaveRequest failed");
      }
      return response;
   }
   /**
    * Requests to Save a Site through the NeXpose API.
    *
    * NOTE: All parameters are strings or generators, since we want to be able
    * to test edge cases and simulate incorrect usage of the tool for robustness
    *
    * @param sessionId the session to be used if different from the current
    *        acquired one (You acquire one when you authenticate correctly with
    *        the login method in the {@link APISession} class). This is a
    *        String of 40 characters.
    * @param syncId the synchronization id to identify the response associated 
    *        with the response in asynchronous environments. It can be any 
    *        string. This field is optional.
    * @param siteId an integer that represents the id of the site to save. Use
    *        -1 for to create a new site, NeXpose will assign an ID.
    * @param siteName the String that represents the name of the site to save.
    * @param siteDescription the String that represents the description of the
    *        site to save. This field is optional.
    * @param siteRiskFactor the floating value that represents the risk factor
    *        of the site to save.
    * @param hostsGenerator a Generator that knows how to output hosts
    *        associated with the site save. e.g.
    *        &lt;host&gt;hostName&lt;host/&gt; where hostName is the name of the
    *        host to assign to the site. Please see
    *        {@link SiteSaveRequestHostsGenerator} for a reference
    *        implementation. For QA testing you should construct your own
    *        {@link IContentGenerator} to generate all the edge test cases you
    *        can think of.
    * @param rangesGenerator a Generator that knows how to output ranges of
    *        hosts that are to be associated with the site save. e.g.
    *        &lt;range from="127.0.0.1" to="127.0.0.2"/&gt;. Please see
    *        {@link SiteSaveRequestRangesGenerator} for a reference
    *        implementation. For QA testing you should construct your own
    *        {@link IContentGenerator} to generate all the edge test cases you
    *        can think of.
    * @param credentialsGenerator a Generator that knows how to output
    *        credentials for sites which that are to be associated with the site
    *        save. e.g. &lt;adminCredentials service"SomeService"
    *        host="someHost" port="somePort" userId="userID" password="password"
    *        realm="someRealm"/&gt;. Please see
    *        {@link SiteSaveRequestCredentialsGenerator} for a reference
    *        implementation. For QA testing you should construct your own
    *        {@link IContentGenerator} to generate all the edge test cases you
    *        can think of.
    * @param alertsGenerator a Generator that knows how to output
    *        alerts for sites which that are to be associated with the site
    *        save. Alerts have one of the following xml structures:
    *        <OL>
    *           <LI>
    *           &lt;Alert name="alertName" enabled="true" maxAlerts="10"&gt;<BR>
    *           &nbsp;&nbsp;&nbsp;&lt;scanFilter scanStart="false" scanStop="false" scanFailed="false"/&gt;<BR>
    *           &nbsp;&nbsp;&nbsp;&lt;vulnFilter severityThreshold="true" confirmed="true" unconfirmed="false"/&gt;<BR>
    *           &nbsp;&nbsp;&nbsp; &lt;smtpAlert sender="someone@zzz.com" server="smtpServer" port="12345" limitText="true"&gt;<BR>
    *           &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;Recipient&gt;email@xxx.com&lt;/Recipient&gt;<BR>
    *           &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;Recipient&gt;email@yyy.com&lt;/Recipient&gt;<BR>
    *           &nbsp;&nbsp;&nbsp;&lt;/smtpAlert&gt;<BR>&lt;/Alert&gt;
    *           </LI>
    *           <LI>
    *           &lt;Alert name="alert2" enabled="true" maxAlerts="10"&gt;<BR>
    *           &nbsp;&nbsp;&nbsp;&lt;scanFilter scanStart="true" scanStop="false" scanFailed="true"/&gt;<BR>
    *           &nbsp;&nbsp;&nbsp;&lt;vulnFilter severityThreshold="false" confirmed="true" unconfirmed="false"/&gt;<BR>
    *           &nbsp;&nbsp;&nbsp;&lt;snmpAlert community="community" server="snmpServer" port="13456"/&gt;<BR>
    *           &lt;/Alert&gt;
    *           </LI>
    *           <LI>
    *           &lt;Alert name="alert3" enabled="true" maxAlerts="10"&gt;<BR>
    *           &nbsp;&nbsp;&nbsp;&lt;scanFilter scanStart="true" scanStop="true" scanFailed="false"/&gt;<BR>
    *           &nbsp;&nbsp;&nbsp;&lt;vulnFilter severityThreshold="false" confirmed="true" unconfirmed="true"/&gt;<BR>
    *           &nbsp;&nbsp;&nbsp;&lt;sysLogAlert server="syslogServer" port="13457"/&gt;<BR>
    *           &lt;/Alert&gt;
    *           </LI>
    *        </OL>
    *        More than one alert can be specified at the time of the site save.
    *        Please see {@link SiteSaveRequestAlertsGenerator} for more details
    *        and for a reference implementation. For QA testing you should
    *        construct your own {@link IContentGenerator} to generate all the
    *        edge test cases you can think of.
    * @param configName a String that represents the name of the scan 
    *        configuration for the site to be saved.
    * @param configVersion a positive integer that represents the scan
    *        configuration version of the site to be saved.
    * @param configId a positive integer that represents the scan
    *        configuration's id of the site to be saved.
    * @param configTemplateId a String that represents the template id
    *        associated with the scan configuration of the site to be saved.
    * @param configEngineId a String that represents the engine id associated
    *        with the scan configuration of the site to be saved. This field is
    *        optional.
    * @param scheduleEnabled 1 to enable the schedule 0 to disable it. This is
    *        optional and only present if the Scan Configuration of the site is
    *        to have a schedule associated with it.
    * @param scheduleIncremental non-negative integer that tells the incremental
    *        scan nature of the Scan configuration associated with the site.
    *        This is optional and only present if the Scan Configuration of the
    *        site is to have a schedule associated with it.
    * @param scheduleType the type of schedule associated with the scan
    *        configuration. One of: <br>daily<br>hourly<br>monthly-date<br>
    *        monthly-day<br>weekly. This is optional but required if the Scan
    *        Configuration of the site is to have schedule associated with it.
    * @param scheduleInterval a non negative integer representing the interval
    *        of the schedule.This is optional but required if the Scan
    *        Configuration of the site is to have a schedule associated with it.
    * @param scheduleStart a String that represents the schedule's start date.
    *        This is optional but required if the Scan Configuration of the site
    *        is to have a schedule associated with it.
    * @param scheduleMaxDuration a String that represents the maximum duration
    *        allowed for the scan. This is optional and only present if the Scan
    *        Configuration of the site is to have a schedule associated with it.
    * @param scheduleNotValidAfter a String that represents the expiration of 
    *        the schedule. This is optional and only present if the Scan
    *        Configuration of the site is to have a schedule associated with it.
    * @return The response from the API.
    * @throws IOException When the API call cannot be performed.
    * @throws APIException if the API call is not successful or the parsing of
    *         the response is not correct.
    */
   @Override
   public APIResponse siteSaveRequest(
      String sessionId,
      String syncId,
      String siteId,
      String siteName,
      String siteDescription,
      String siteRiskFactor,
      IContentGenerator siteHostsHostGenerator,
      IContentGenerator siteHostsRangeGenerator,
      IContentGenerator credentialsGenerator,
      IContentGenerator alertsGenerator,
      String siteScanConfigName,
      String siteScanConfigConfigVersion,
      String siteScanConfigConfigId,
      String siteScanConfigTemplateId,
      String siteScanConfigEngineId,
      String scheduleEnabled,
      String scheduleIncremental,
      String scheduleType,
      String scheduleInterval,
      String scheduleStart,
      String scheduleMaxDuration,
      String scheduleNotValidAfter) throws IOException, APIException
   {
      return baiscSiteSaveRequest(
         sessionId,
         syncId,
         siteId,
         siteName,
         siteDescription,
         siteRiskFactor,
         siteHostsHostGenerator,
         siteHostsRangeGenerator,
         credentialsGenerator,
         alertsGenerator,
         siteScanConfigName,
         siteScanConfigConfigVersion,
         siteScanConfigConfigId,
         siteScanConfigTemplateId,
         siteScanConfigEngineId,
         scheduleEnabled,
         scheduleIncremental,
         scheduleType,
         scheduleInterval,
         scheduleStart,
         scheduleMaxDuration,
         scheduleNotValidAfter);
   }
   /**
    * Requests to Save a Site through the NeXpose API. Accepts the default
    * generators.
    *
    * NOTE: All parameters are strings or generators, since we want to be able
    * to test edge cases and simulate incorrect usage of the tool for robustness
    *
    * @param sessionId the session to be used if different from the current
    *        acquired one (You acquire one when you authenticate correctly with
    *        the login method in the {@link APISession} class). This is a
    *        String of 40 characters.
    * @param syncId the synchronization id to identify the response associated 
    *        with the response in asynchronous environments. It can be any 
    *        string. This field is optional.
    * @param siteId an integer that represents the id of the site to save. Use
    *        -1 for to create a new site, NeXpose will assign an ID.
    * @param siteName the String that represents the name of the site to save.
    * @param siteDescription the String that represents the description of the
    *        site to save. This field is optional.
    * @param siteRiskFactor the floating value that represents the risk factor
    *        of the site to save.
    * @param hostsGenerator a Generator that knows how to output hosts
    *        associated with the site save. e.g.
    *        &lt;host&gt;hostName&lt;host/&gt; where hostName is the name of the
    *        host to assign to the site.
    * @param rangesGenerator a Generator that knows how to output ranges of
    *        hosts that are to be associated with the site save. e.g.
    *        &lt;range from="127.0.0.1" to="127.0.0.2"/&gt;.
    * @param credentialsGenerator a Generator that knows how to output
    *        credentials for sites which that are to be associated with the site
    *        save. e.g. &lt;adminCredentials service"SomeService"
    *        host="someHost" port="somePort" userId="userID" password="password"
    *        realm="someRealm"/&gt;.
    * @param alertsGenerator a Generator that knows how to output
    *        alerts for sites which that are to be associated with the site
    *        save. Alerts have one of the following xml structures:
    *        <OL>
    *           <LI>
    *           &lt;Alert name="alertName" enabled="true" maxAlerts="10"&gt;<BR>
    *           &nbsp;&nbsp;&nbsp;&lt;scanFilter scanStart="false" scanStop="false" scanFailed="false"/&gt;<BR>
    *           &nbsp;&nbsp;&nbsp;&lt;vulnFilter severityThreshold="true" confirmed="true" unconfirmed="false"/&gt;<BR>
    *           &nbsp;&nbsp;&nbsp; &lt;smtpAlert sender="someone@zzz.com" server="smtpServer" port="12345" limitText="true"&gt;<BR>
    *           &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;Recipient&gt;email@xxx.com&lt;/Recipient&gt;<BR>
    *           &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;Recipient&gt;email@yyy.com&lt;/Recipient&gt;<BR>
    *           &nbsp;&nbsp;&nbsp;&lt;/smtpAlert&gt;<BR>&lt;/Alert&gt;
    *           </LI>
    *           <LI>
    *           &lt;Alert name="alert2" enabled="true" maxAlerts="10"&gt;<BR>
    *           &nbsp;&nbsp;&nbsp;&lt;scanFilter scanStart="true" scanStop="false" scanFailed="true"/&gt;<BR>
    *           &nbsp;&nbsp;&nbsp;&lt;vulnFilter severityThreshold="false" confirmed="true" unconfirmed="false"/&gt;<BR>
    *           &nbsp;&nbsp;&nbsp;&lt;snmpAlert community="community" server="snmpServer" port="13456"/&gt;<BR>
    *           &lt;/Alert&gt;
    *           </LI>
    *           <LI>
    *           &lt;Alert name="alert3" enabled="true" maxAlerts="10"&gt;<BR>
    *           &nbsp;&nbsp;&nbsp;&lt;scanFilter scanStart="true" scanStop="true" scanFailed="false"/&gt;<BR>
    *           &nbsp;&nbsp;&nbsp;&lt;vulnFilter severityThreshold="false" confirmed="true" unconfirmed="true"/&gt;<BR>
    *           &nbsp;&nbsp;&nbsp;&lt;sysLogAlert server="syslogServer" port="13457"/&gt;<BR>
    *           &lt;/Alert&gt;
    *           </LI>
    *        </OL>
    * @param configName a String that represents the name of the scan 
    *        configuration for the site to be saved.
    * @param configVersion a positive integer that represents the scan
    *        configuration version of the site to be saved.
    * @param configId a positive integer that represents the scan
    *        configuration's id of the site to be saved.
    * @param configTemplateId a String that represents the template id
    *        associated with the scan configuration of the site to be saved.
    * @param configEngineId a String that represents the engine id associated
    *        with the scan configuration of the site to be saved. This field is
    *        optional.
    * @param scheduleEnabled 1 to enable the schedule 0 to disable it. This is
    *        optional and only present if the Scan Configuration of the site is
    *        to have a schedule associated with it.
    * @param scheduleIncremental non-negative integer that tells the incremental
    *        scan nature of the Scan configuration associated with the site.
    *        This is optional and only present if the Scan Configuration of the
    *        site is to have a schedule associated with it.
    * @param scheduleType the type of schedule associated with the scan
    *        configuration. One of: <br>daily<br>hourly<br>monthly-date<br>
    *        monthly-day<br>weekly. This is optional but required if the Scan
    *        Configuration of the site is to have schedule associated with it.
    * @param scheduleInterval a non negative integer representing the interval
    *        of the schedule.This is optional but required if the Scan
    *        Configuration of the site is to have a schedule associated with it.
    * @param scheduleStart a String that represents the schedule's start date.
    *        This is optional but required if the Scan Configuration of the site
    *        is to have a schedule associated with it.
    * @param scheduleMaxDuration a String that represents the maximum duration
    *        allowed for the scan. This is optional and only present if the Scan
    *        Configuration of the site is to have a schedule associated with it.
    * @param scheduleNotValidAfter a String that represents the expiration of 
    *        the schedule. This is optional and only present if the Scan
    *        Configuration of the site is to have a schedule associated with it.
    */
   @Override
   public APIResponse siteSaveRequest(
      String sessionId,
      String syncId,
      String siteId,
      String siteName,
      String siteDescription,
      String siteRiskFactor,
      SiteSaveRequestHostsGenerator siteHostsHostGenerator,
      SiteSaveRequestRangesGenerator siteHostsRangeGenerator,
      SiteSaveRequestCredentialsGenerator credentialsGenerator,
      SiteSaveRequestAlertsGenerator alertsGenerator,
      String siteScanConfigName,
      String siteScanConfigConfigVersion,
      String siteScanConfigConfigId,
      String siteScanConfigTemplateId,
      String siteScanConfigEngineId,
      String scheduleEnabled,
      String scheduleIncremental,
      String scheduleType,
      String scheduleInterval,
      String scheduleStart,
      String scheduleMaxDuration,
      String scheduleNotValidAfter) throws IOException, APIException
   {
      return baiscSiteSaveRequest(
               sessionId,
               syncId,
               siteId,
               siteName,
               siteDescription,
               siteRiskFactor,
               siteHostsHostGenerator,
               siteHostsRangeGenerator,
               credentialsGenerator,
               alertsGenerator,
               siteScanConfigName,
               siteScanConfigConfigVersion,
               siteScanConfigConfigId,
               siteScanConfigTemplateId,
               siteScanConfigEngineId,
               scheduleEnabled,
               scheduleIncremental,
               scheduleType,
               scheduleInterval,
               scheduleStart,
               scheduleMaxDuration,
               scheduleNotValidAfter);
            }

   /**
    * Requests to Site scan request through the NeXpose API.
    *
    * @param sessionId the session to be used if different from the current
    *        acquired one (You acquire one when you authenticate correctly with
    *        the login method in the {@link APISession} class). This is a
    *        String of 40 characters.
    * @param syncId the synchronization id to identify the response associated 
    *        with the response in asynchronous environments. It can be any 
    *        string. This field is optional.
    * @param siteId the id of the site
    * @return The response from the API.
    * @throws IOException When the API call cannot be performed.
    * @throws APIException if the API call is not successful or the parsing of
    *         the response is not correct.
    */
   @Override
   public APIResponse siteScanRequest(
      String sessionId,
      String syncId,
      String siteId)
      throws IOException, APIException
   {
      final TemplateAPIRequest request = new SiteScanRequest(
         sessionId,
         syncId,
         siteId);
      final APIResponse response = new APIResponse(
         request(open(request), auth(request)),
         request.getRequestXML());
      if (response.grabNode("//Failure") != null)
      {
         m_errorHandler.handleError(
            request,
            response,
            this,
            "SiteScanRequest failed");
      }
      return response;
   }

   /**
    * Requests to delete a Site through the NeXpose API.
    *
    * @param sessionId the session to be used if different from the current
    *        acquired one (You acquire one when you authenticate correctly with
    *        the login method in the {@link APISession} class). This is a
    *        String of 40 characters.
    * @param syncId the synchronization id to identify the response associated 
    *        with the response in asynchronous environments. It can be any 
    *        string. This field is optional.
    * @return The response from the API.
    * @throws IOException When the API call cannot be performed.
    * @throws APIException if the API call is not successful or the parsing of
    *         the response is not correct.
    */
   @Override
   public APIResponse siteDeleteRequest(
      String sessionId,
      String syncId,
      String siteId)
      throws IOException, APIException
   {
      final TemplateAPIRequest request = new SiteDeleteRequest(
         sessionId,
         siteId,
         syncId);
      final APIResponse response = new APIResponse(
         request(open(request), auth(request)),
         request.getRequestXML());
      if (response.grabNode("//Failure") != null)
      {
         m_errorHandler.handleError(
            request,
            response,
            this,
            "SiteDeleteRequest failed");
      }
      return response;
   }

   /**
    * Requests to delete an Asset Group through the NeXpose API.
    *
    * @param sessionId the session to be used if different from the current
    *        acquired one (You acquire one when you authenticate correctly with
    *        the login method in the {@link APISession} class). This is a
    *        String of 40 characters.
    * @param syncId the synchronization id to identify the response associated 
    *        with the response in asynchronous environments. It can be any 
    *        string. This field is optional.
    * @param assetGroupId the id of the asset group to delete
    * @return The response from the API.
    * @throws IOException When the API call cannot be performed.
    * @throws APIException if the API call is not successful or the parsing of
    *         the response is not correct.
    */
   @Override
   public APIResponse assetGroupDeleteRequest(
      String sessionId,
      String syncId,
      String assetGroupId)
      throws IOException, APIException
   {
      final TemplateAPIRequest request = new AssetGroupDeleteRequest(
         sessionId,
         syncId,
         assetGroupId);
      final APIResponse response = new APIResponse(
         request(open(request), auth(request)),
         request.getRequestXML());
      if (response.grabNode("//Failure") != null)
      {
         m_errorHandler.handleError(
            request,
            response,
            this,
            "AssetGroupDeleteRequest failed");
      }
      return response;
   }

   /**
    * Requests to stop a scan through the NeXpose API.
    * 
    * @param sessionId the session to be used if different from the current
    *        acquired one (You acquire one when you authenticate correctly with
    *        the login method in the {@link APISession} class). This is a
    *        String of 40 characters.
    * @param syncId the synchronization id to identify the response associated 
    *        with the response in asynchronous environments. It can be any 
    *        string. This field is optional.
    * @param scanId the id of the scan to stop
    * @return The response from the API.
    * @throws IOException When the API call cannot be performed.
    * @throws APIException if the API call is not successful or the parsing of
    *         the response is not correct.
    */
   @Override
   public APIResponse scanStopRequest(
      String sessionId,
      String syncId,
      String scanId)
      throws IOException, APIException
   {
      final TemplateAPIRequest request = new ScanStopRequest(
         sessionId,
         syncId,
         scanId);
      final APIResponse response = new APIResponse(
         request(open(request), auth(request)),
         request.getRequestXML());
      if (response.grabNode("//Failure") != null)
      {
         m_errorHandler.handleError(
            request,
            response,
            this,
            "ScanStopRequest failed");
      }
      return response;
   }
   /**
    * Requests to save a user through the NeXpose API.
    *
    * NOTE: All parameters are strings or generators, since we want to be able
    * to test edge cases and simulate incorrect usage of the tool for robustness
    *
    * @param sessionId the session to be used if different from the current
    *        acquired one (You acquire one when you use correct credentials in
    *        with the login method in the {@link APISession} class). This is a
    *        String of 40 characters.
    * @param syncId the synchronization id to identify the response associated
    *        with the response in asynchronous environments. It can be any 
    *        string.
    * @param allGroups true if the user has access to all groups, false
    *        otherwise (specified when creating/saving a config and Group
    *        elements are not specified)
    * @param allSites true if the user has access to all sites, false otherwise
    *        (specified when creating/saving a config and Site elements are not
    *        specified)
    * @param authSrcId the positive integer that identifies the authentication
    *        source to be used to authenticate the user. Should be one of the
    *        existing authentication for correctness.
    * @param email the email of the user, should have the right email format. 
    *        This field is optional.
    * @param enabled true (or 1) if the user is enabled, false (or 0) otherwise.
    *        This field is optional.
    * @param fullname a String that represents the full name of the user.
    * @param id the non negative integer that identifies the user, -1 to create
    *        a new user.
    * @param name the String that represents the name of the user. This field
    *        may not be updated, so only used for new users. You can still
    *        specify it and NeXpose will throw an Exception.
    * @param password the String that represents the password of the user. This
    *        field is optional, if present, the user's new password.
    * @param roleName the role name of the user, should be one of
    *        global-admin for Global Administrator.
    *        security-manager for Security Manager.
    *        site-admin for Site Administrator.
    *        system-admin for System Administrator.
    *        user for User.
    *        custom for Custom. NOTE: Be aware that there is not role management
    *        enabled to date (1/6/2010)
    * @param sitesGenerator a Generator that knows how to output user sites with
    *        IDs associated to it e.g. &lt;Site id="X"/&gt; where X is a non
    *        negative Integer.
    * @param groupsGenerator a Generator that knows how to output user groups 
    *        with IDs associated to it e.g. &lt;Group id="X"/&gt; where X is a
    *        non negative Integer.
    * @return APIResponse the response that came from the NeXpose instance. 
    * @throws IOException When the API call cannot be performed.
    * @throws APIException if the API call is not successful or the parsing of
    *         the response is not correct.
    */
   @Override
   public APIResponse userSaveRequest(
      String sessionId,
      String syncId,
      String allGroups,
      String allSites,
      String authSrcId,
      String email,
      String enabled,
      String fullname,
      String id,
      String name,
      String password,
      String roleName,
      UserSaveRequestSitesGenerator sitesGenerator,
      UserSaveRequestGroupsGenerator groupsGenerator)
      throws IOException, APIException
   {
      return basicUserSaveRequest(
         sessionId,
         syncId,
         allGroups,
         allSites,
         authSrcId,
         email,
         enabled,
         fullname,
         id,
         name,
         password,
         roleName,
         sitesGenerator,
         groupsGenerator);
   }
   /**
    * Requests to save a user through the NeXpose API.
    *
    * NOTE: All parameters are strings or generators, since we want to be able
    * to test edge cases and simulate incorrect usage of the tool for robustness
    *
    * @param sessionId the session to be used if different from the current
    *        acquired one (You acquire one when you use correct credentials in
    *        with the login method in the {@link APISession} class). This is a
    *        String of 40 characters.
    * @param syncId the synchronization id to identify the response associated
    *        with the response in asynchronous environments. It can be any
    *        string.
    * @param allGroups true if the user has access to all groups, false
    *        otherwise (specified when creating/saving a config and Group
    *        elements are not specified)
    * @param allSites true if the user has access to all sites, false otherwise
    *        (specified when creating/saving a config and Site elements are not
    *        specified)
    * @param authSrcId the positive integer that identifies the authentication
    *        source to be used to authenticate the user. Should be one of the
    *        existing authentication for correctness.
    * @param email the email of the user, should have the right email format.
    *        This field is optional.
    * @param enabled true (or 1) if the user is enabled, false (or 0) otherwise.
    *        This field is optional.
    * @param fullname a String that represents the full name of the user.
    * @param id the non negative integer that identifies the user, -1 to create
    *        a new user.
    * @param name the String that represents the name of the user. This field
    *        may not be updated, so only used for new users. You can still
    *        specify it and NeXpose will throw an Exception.
    * @param roleName the role name of the user, should be one of
    *        global-admin for Global Administrator.
    *        security-manager for Security Manager.
    *        site-admin for Site Administrator.
    *        system-admin for System Administrator.
    *        user for User.
    *        custom for Custom. NOTE: Be aware that there is not role management
    *        enabled to date (1/6/2010)
    * @param sitesGenerator a Generator that knows how to output user sites with
    *        IDs associated to it e.g. &lt;Site id="X"/&gt; where X is a non
    *        negative Integer.
    * @param groupsGenerator a Generator that knows how to output user groups
    *        with IDs associated to it e.g. &lt;Group id="X"/&gt; where X is a
    *        non negative Integer.
    * @return APIResponse the response that came from the NeXpose instance.
    * @throws IOException When the API call cannot be performed.
    * @throws APIException if the API call is not successful or the parsing of
    *         the response is not correct.
    */
   @Override
   public APIResponse userSaveRequest(
      String sessionId,
      String syncId,
      String allGroups,
      String allSites,
      String authSrcId,
      String email,
      String enabled,
      String fullname,
      String id,
      String name,
      String roleName,
      UserSaveRequestSitesGenerator sitesGenerator,
      UserSaveRequestGroupsGenerator groupsGenerator)
      throws IOException, APIException
   {
      return basicUserSaveRequest(
         sessionId,
         syncId,
         allGroups,
         allSites,
         authSrcId,
         email,
         enabled,
         fullname,
         id,
         name,
         roleName,
         sitesGenerator,
         groupsGenerator);
   }
   /**
    * Creates a new User Save Request with any given sites content generator and
    * any given group content generator. Sets the first API supported version to
    * 1.0 and the last supported version to 1.1.
    *
    * NOTE: All parameters are strings or generators, since we want to be able
    * to test edge cases and simulate incorrect usage of the tool for robustness
    *
    * @param sessionId the session to be used if different from the current
    *        acquired one (You acquire one when you authenticate correctly with
    *        the login method in the {@link APISession} class). This is a
    *        String of 40 characters.
    * @param syncId the synchronization id to identify the response associated 
    *        with the response in asynchronous environments. It can be any 
    *        string. This field is optional.
    * @param allGroups true if the user has access to all groups, false
    *        otherwise (specified when creating/saving a config and Group
    *        elements are not specified)
    * @param allSites true if the user has access to all sites, false otherwise
    *        (specified when creating/saving a config and Site elements are not
    *        specified)
    * @param authSrcId the positive integer that identifies the authentication 
    *        source to be used to authenticate the user. Should be one of the
    *        existing authentication for correctness.
    * @param email the email of the user, should have the right email format. 
    *        This field is optional.
    * @param enabled true (or 1) if the user is enabled, false (or 0) otherwise.
    *        This field is optional.
    * @param fullname a String that represents the full name of the user.
    * @param id the non negative integer that identifies the user, -1 to create
    *        a new user.
    * @param name the String that represents the name of the user. This field
    *        may not be updated, so only used for new users. You can still
    *        specify it and NeXpose will throw an Exception.
    * @param password the String that represents the password of the user.\
    *        This field is optional, if present, the user's new password.
    * @param roleName the role name of the user, should be one of
    *        global-admin for Global Administrator.
    *        security-manager for Security Manager.
    *        site-admin for Site Administrator.
    *        system-admin for System Administrator.
    *        user for User.
    *        custom for Custom. NOTE: Be aware that there is not role management
    *        enabled to date (1/6/2010)
    * @param sitesGenerator a Generator that knows how to output user sites with
    *        IDs associated to it e.g. &lt;Site id="X"/&gt; where X is a non
    *        negative Integer. Please see {@link UserSaveRequestSitesGenerator}
    *        for a reference implementation. For QA testing you should construct
    *        your own {@link IContentGenerator} to generate all the edge test
    *        cases you can think of.
    * @param groupsGenerator a Generator that knows how to output user groups 
    *        with IDs associated to it e.g. &lt;Group id="X"/&gt; where X is a
    *        non negative Integer. Please see
    *        {@link UserSaveRequestGroupsGenerator} for a reference
    *        implementation. For QA testing you should construct your own
    *        {@link IContentGenerator} to generate all the edge test cases you
    *        can think of.
    * @return APIResponse the response that came from the NeXpose instance.
    * @throws IOException When the API call cannot be performed.
    * @throws APIException if the API call is not successful or the parsing of
    *         the response is not correct.
    */
   @Override
   public APIResponse userSaveRequest(
            String sessionId,
            String syncId,
            String allGroups,
            String allSites,
            String authSrcId,
            String email,
            String enabled,
            String fullname,
            String id,
            String name,
            String password,
            String roleName,
            IContentGenerator sitesGenerator,
            IContentGenerator groupsGenerator)
   throws IOException, APIException
   {
      return basicUserSaveRequest(
         sessionId,
         syncId,
         allGroups,
         allSites,
         authSrcId,
         email,
         enabled,
         fullname,
         id,
         name,
         password,
         roleName,
         sitesGenerator,
         groupsGenerator);
   }

   /**
    * Sends a UserDeleteREquest NeXpose API request to attempt to delete a user
    * with the specified id.
    *
    * @param sessionId the session to be used if different from the current
    *        acquired one (You acquire one when you authenticate correctly with
    *        the login method in the {@link APISession} class). This is a
    *        String of 40 characters.
    * @param syncId the synchronization id to identify the response associated 
    *        with the response in asynchronous environments. It can be any 
    *        string. This field is optional.
    * @param userId the id of the user to delete.
    * @return a response from the NeXpose console in session.
    * @throws IOException When the API call cannot be performed.
    * @throws APIException if the API call is not successful or the parsing of
    *         the response is not correct.    */
   @Override
   public APIResponse userDeleteRequest(
      String sessionId,
      String syncId,
      String userId)
      throws IOException, APIException
   {
      final TemplateAPIRequest request = new UserDeleteRequest(
         sessionId,
         syncId,
         userId);
      final APIResponse response = new APIResponse(
         request(open(request), auth(request)),
         request.getRequestXML());
      if (response.grabNode("//Failure") != null)
      {
         m_errorHandler.handleError(
            request,
            response,
            this,
            "UserDeleteRequestFailed");
      }
      return response;
   }

   /**
    * Lists all of the silos.
    * 
    * @param sessionId the session to be used if different from the current acquired one (You acquire one when you
    *        authenticate correctly with the login method in the {@link APISession} class). This is a String of 40
    *        characters.
    * @param syncId the synchronization id to identify the response associated with the response in asynchronous
    *        environments. It can be any string. This field is optional.
    * 
    * @return a list of engines of type (@link SiloSummary}.
    * 
    * @throws IOException when the connection to the NeXpose instance fails.
    * @throws APIException When there is a problem processing the API request.
    */
   @Override
   public APIResponse listSilos(String sessionId, String syncId) throws IOException, APIException
   {
      final TemplateAPIRequest request = new SiloListingRequest(sessionId, syncId);
      final APIResponse response = new APIResponse(request(open(request), auth(request)), request.getRequestXML());
      m_apiResponse = response;
      if (response.grabNode("//Failure") != null)
      {
         m_errorHandler.handleError(request, response, this, "SiloListingRequest failed");
      }
      return m_apiResponse;
   }

   /**
    * Sends a SiloCreateRequest NeXpose API request to attempt to create a silo.
    *
    * @param sessionId the session to be used if different from the currently acquired one. This is a String of 40
    *        characters.
    * @param syncId The String that uniquely identifies the response associated with the request sent. This field is
    *        optional.
    * @param maxExternalAssets the number of maximum external assets of the silo.
    * @param maxInternalAssets the number of max internal assets of the silo.
    * @param maxUsers the max number of users that the silo can create.
    * @param merchantGenerator the PCI merchant information of the silo.
    * @param organizationURLGenerator the URL of the organization of the silo.
    * @param storage the storage information of the silo.
    *
    * @return a response from the NeXpose console in session.
    *
    * @throws IOException when the connection to the NeXpose instance fails.
    * @throws APIException When there is a problem processing the API request.
    */
   @Override
   public APIResponse siloCreateRequest(
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
      throws IOException, APIException
   {
      final TemplateAPIRequest request = new SiloCreateRequest(
         sessionId,
         syncId,
         id,
         name,
         description,
         siloProfileId,
         maxAssets,
         maxUsers,
         dbID,
         merchantGenerator,
         organizationURLGenerator,
         storage);
      final APIResponse response = new APIResponse(request(open(request), auth(request)), request.getRequestXML());
      if (response.grabNode("//Failure") != null)
      {
         m_errorHandler.handleError(request, response, this, "SiloCreateRequest failed");
      }
      return response;
   }

   /**
    * Sends a SiloUpdateRequest NeXpose API request to attempt to update a silo.
    *
    * @param sessionId the session to be used if different from the currently acquired one. This is a String of 40
    *        characters.
    * @param syncId The String that uniquely identifies the response associated with the request sent. This field is
    *        optional.
    * @param maxExternalAssets the number of maximum external assets of the silo.
    * @param maxInternalAssets the number of max internal assets of the silo.
    * @param maxUsers the max number of users that the silo can create.
    * @param merchantGenerator the PCI merchant information of the silo.
    * @param organizationURLGenerator the URL of the organization of the silo.
    * @param storage the storage information of the silo.
    *
    * @return a response from the NeXpose console in session.
    *
    * @throws IOException when the connection to the NeXpose instance fails.
    * @throws APIException When there is a problem processing the API request.
    */
   @Override
   public APIResponse siloUpdateRequest(
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
      throws IOException, APIException
   {
      final TemplateAPIRequest request = new SiloUpdateRequest(
            sessionId,
            syncId,
            id,
            name,
            description,
            siloProfileId,
            maxAssets,
            maxUsers,
            dbID,
            merchantGenerator,
            organizationURLGenerator,
            storage);
      final APIResponse response = new APIResponse(request(open(request), auth(request)), request.getRequestXML());
      if (response.grabNode("//Failure") != null)
      {
         m_errorHandler.handleError(request, response, this, "SiloCreateRequest failed");
      }
      return response;
   }

   /**
    * Requests to delete a Silo through the NeXpose API.
    * 
    * @param sessionId the session to be used if different from the currently acquired one. This is a String of 40
    *        characters.
    * @param syncId The String that uniquely identifies the response associated with the request sent. This field is
    *        optional.
    * @param siloId The id of the silo to request to delete.
    *
    * @return a response from the NeXpose console in session.
    *
    * @throws IOException when the connection to the NeXpose instance fails.
    * @throws APIException When there is a problem processing the API request.
    */
   @Override
   public APIResponse siloDeleteRequest(String sessionId, String syncId, String siloId) throws IOException, APIException
   {
      final TemplateAPIRequest request = new SiloDeleteRequest(sessionId,  syncId, siloId);
      final APIResponse response = new APIResponse( request(open(request), auth(request)), request.getRequestXML());
      if (response.grabNode("//Failure") != null)
      {
         m_errorHandler.handleError(request, response, this, "SiloDeleteRequest failed");
      }
      return response;
   }

   /**
    * Requests the configuration of a Silo through the NeXpose API.
    * 
    * @param sessionId the session to be used if different from the currently acquired one. This is a String of 40
    *        characters.
    * @param syncId The String that uniquely identifies the response associated with the request sent. This field is
    *        optional.
    * @param siloId The id of the silo to request the config from.
    *
    * @return a response from the NeXpose console in session.
    *
    * @throws IOException when the connection to the NeXpose instance fails.
    * @throws APIException When there is a problem processing the API request.
    */
   @Override
   public APIResponse siloConfigRequest(String sessionId, String syncId, String siloId) throws IOException, APIException
   {
      final TemplateAPIRequest request = new SiloConfigRequest(sessionId,  syncId, siloId);
      final APIResponse response = new APIResponse( request(open(request), auth(request)), request.getRequestXML());
      if (response.grabNode("//Failure") != null)
      {
         m_errorHandler.handleError(request, response, this, "SiloConfigRequest failed");
      }
      return response;
   }

   /**
    * Requests to save a multi-tenant user through the NeXpose API.
    *
    * NOTE: All parameters are strings or generators, since we want to be able to test edge cases and simulate incorrect
    * usage of the tool for robustness.
    *
    * @param sessionId the session to be used if different from the currently acquired one. This is a String of 40
    *        characters.
    * @param syncId The String that uniquely identifies the response associated with the request sent. This field is
    *        optional.
    * @param authSrcId the positive integer that identifies the authentication source to be used to authenticate the
    *        user. Should be one of the existing authentication for correctness.
    * @param email the email of the user, should have the right email format. This field is optional.
    * @param enabled true (or 1) if the user is enabled, false (or 0) otherwise. This field is optional.
    * @param fullname a String that represents the full name of the user.
    * @param username the String that represents the username of the user.
    * @param superuser a flag that tells whether the Multi Tenant user is a super user or not.
    * @param password the String that represents the password of the user. This field is optional, if present, the
    *        user's new password.
    * @param multiTenantUserConfigSiloAccessGenerator a Generator that knows how to output silo access elements
    *        containing all the information of in a silo per silo basis, pertinent to the Multi Tenant User. Please see
    *        {@link MultiTenantUserConfigSiloAccessGenerator} for a detailed implementation.
    *
    * @return APIResponse the response that came from the NeXpose instance.
    *
    * @throws IOException When the API call cannot be performed.
    * @throws APIException if the API call is not successful or the parsing of the response is not correct.
    */
   @Override
   public APIResponse multiTenantUserCreateRequest(
      String sessionId,
      String syncId,
      String authSrcId,
      String email,
      String enabled,
      String fullname,
      String username,
      String superuser,
      String password,
      IContentGenerator multiTenantUserConfigSiloAccessGenerator)
      throws IOException, APIException
   {
      final TemplateAPIRequest request = new MultiTenantUserCreateRequest(
            sessionId,
            syncId,
            fullname,
            authSrcId,
            email,
            password,
            enabled,
            username,
            superuser,
            multiTenantUserConfigSiloAccessGenerator);
      final APIResponse response = new APIResponse(request(open(request),auth(request)), request.getRequestXML());
      if (response.grabNode("//Failure") != null)
      {
         m_errorHandler.handleError( request, response, this, "MultiTenantUserCreateRequest failed");
      }
      return response;
   }

   /**
    * Requests to save a multi-tenant user through the NeXpose API.
    *
    * NOTE: All parameters are strings or generators, since we want to be able to test edge cases and simulate incorrect
    * usage of the tool for robustness.
    *
    * @param sessionId the session to be used if different from the currently acquired one. This is a String of 40
    *        characters.
    * @param syncId The String that uniquely identifies the response associated with the request sent. This field is
    *        optional.
    * @param authSrcId the positive integer that identifies the authentication source to be used to authenticate the
    *        user. Should be one of the existing authentication for correctness.
    * @param email the email of the user, should have the right email format. This field is optional.
    * @param enabled true (or 1) if the user is enabled, false (or 0) otherwise. This field is optional.
    * @param fullname a String that represents the full name of the user.
    * @param username the String that represents the username of the user.
    * @param superuser a flag that tells whether the Multi Tenant user is a super user or not.
    * @param password the String that represents the password of the user. This field is optional, if present, the
    *        user's new password.
    * @param multiTenantUserConfigSiloAccessGenerator a Generator that knows how to output silo access elements
    *        containing all the information of in a silo per silo basis, pertinent to the Multi Tenant User. Please see
    *        {@link MultiTenantUserConfigSiloAccessGenerator} for a detailed implementation.
    *
    * @return APIResponse the response that came from the NeXpose instance.
    *
    * @throws IOException When the API call cannot be performed.
    * @throws APIException if the API call is not successful or the parsing of the response is not correct.
    */
   @Override
   public APIResponse multiTenantUserCreateRequest(
      String sessionId,
      String syncId,
      String authSrcId,
      String email,
      String enabled,
      String fullname,
      String username,
      String superuser,
      IContentGenerator multiTenantUserConfigSiloAccessGenerator)
      throws IOException, APIException
   {
      final TemplateAPIRequest request = new MultiTenantExternalUserCreateRequest(
            sessionId,
            syncId,
            fullname,
            authSrcId,
            email,
            enabled,
            username,
            superuser,
            multiTenantUserConfigSiloAccessGenerator);
      final APIResponse response = new APIResponse(request(open(request),auth(request)), request.getRequestXML());
      if (response.grabNode("//Failure") != null)
      {
         m_errorHandler.handleError( request, response, this, "MultiTenantUserCreateRequest failed");
      }
      return response;
   }

   /**
    * Requests to update a multi-tenant user through the NeXpose API.
    *
    * NOTE: All parameters are strings or generators, since we want to be able to test edge cases and simulate incorrect
    * usage of the tool for robustness.
    *
    * @param sessionId the session to be used if different from the currently acquired one. This is a String of 40
    *        characters.
    * @param syncId The String that uniquely identifies the response associated with the request sent. This field is
    *        optional.
    * @param authSrcId the positive integer that identifies the authentication source to be used to authenticate the
    *        user. Should be one of the existing authentication for correctness.
    * @param email the email of the user, should have the right email format. This field is optional.
    * @param enabled true (or 1) if the user is enabled, false (or 0) otherwise. This field is optional.
    * @param fullname a String that represents the full name of the user.
    * @param id the the id of the multi-tenant user.
    * @param username the String that represents the username of the user.
    * @param superuser a flag that tells whether the Multi Tenant user is a super user or not.
    * @param password the String that represents the password of the user. This field is optional, if present, the
    *        user's new password.
    * @param multiTenantUserConfigSiloAccessGenerator a Generator that knows how to output silo access elements
    *        containing all the information of in a silo per silo basis, pertinent to the Multi Tenant User. Please see
    *        {@link MultiTenantUserConfigSiloAccessGenerator} for a detailed implementation.
    *
    * @return APIResponse the response that came from the NeXpose instance.
    *
    * @throws IOException When the API call cannot be performed.
    * @throws APIException if the API call is not successful or the parsing of the response is not correct.
    */
   @Override
   public APIResponse multiTenantUserUpdateRequest(
         String sessionId,
         String syncId,
         String authSrcId,
         String email,
         String enabled,
         String fullname,
         String id,
         String username,
         String superuser,
         String password,
         IContentGenerator multiTenantUserConfigSiloAccessGenerator)
   throws IOException, APIException
   {
      final TemplateAPIRequest request = new MultiTenantUserUpdateRequest(
            sessionId,
            syncId,
            id,
            fullname,
            authSrcId,
            email,
            password,
            enabled,
            username,
            superuser,
            multiTenantUserConfigSiloAccessGenerator);
      final APIResponse response = new APIResponse(request(open(request),auth(request)), request.getRequestXML());
      if (response.grabNode("//Failure") != null)
      {
         m_errorHandler.handleError( request, response, this, "MultiTenantUserUpdateRequest failed");
      }
      return response;
   }

   /////////////////////////////////////////////////////////////////////////
   // non-Public methods
   /////////////////////////////////////////////////////////////////////////

   /**
    * Requests to Save a Site through the NeXpose API.
    *
    * NOTE: All parameters are strings or generators, since we want to be able
    * to test edge cases and simulate incorrect usage of the tool for robustness
    *
    * @param sessionId the session to be used if different from the current
    *        acquired one (You acquire one when you authenticate correctly with
    *        the login method in the {@link APISession} class). This is a
    *        String of 40 characters.
    * @param syncId the synchronization id to identify the response associated 
    *        with the response in asynchronous environments. It can be any 
    *        string. This field is optional.
    * @param siteId an integer that represents the id of the site to save. Use
    *        -1 for to create a new site, NeXpose will assign an ID.
    * @param siteName the String that represents the name of the site to save.
    * @param siteDescription the String that represents the description of the
    *        site to save. This field is optional.
    * @param siteRiskFactor the floating value that represents the risk factor
    *        of the site to save.
    * @param hostsGenerator a Generator that knows how to output hosts
    *        associated with the site save. e.g.
    *        &lt;host&gt;hostName&lt;host/&gt; where hostName is the name of the
    *        host to assign to the site. Please see
    *        {@link SiteSaveRequestHostsGenerator} for a reference
    *        implementation. For QA testing you should construct your own
    *        {@link IContentGenerator} to generate all the edge test cases you
    *        can think of.
    * @param rangesGenerator a Generator that knows how to output ranges of
    *        hosts that are to be associated with the site save. e.g.
    *        &lt;range from="127.0.0.1" to="127.0.0.2"/&gt;. Please see
    *        {@link SiteSaveRequestRangesGenerator} for a reference
    *        implementation. For QA testing you should construct your own
    *        {@link IContentGenerator} to generate all the edge test cases you
    *        can think of.
    * @param credentialsGenerator a Generator that knows how to output
    *        credentials for sites which that are to be associated with the site
    *        save. e.g. &lt;adminCredentials service"SomeService"
    *        host="someHost" port="somePort" userId="userID" password="password"
    *        realm="someRealm"/&gt;. Please see
    *        {@link SiteSaveRequestCredentialsGenerator} for a reference
    *        implementation. For QA testing you should construct your own
    *        {@link IContentGenerator} to generate all the edge test cases you
    *        can think of.
    * @param alertsGenerator a Generator that knows how to output
    *        alerts for sites which that are to be associated with the site
    *        save. Alerts have one of the following xml structures:
    *        <OL>
    *           <LI>
    *           &lt;Alert name="alertName" enabled="true" maxAlerts="10"&gt;<BR>
    *           &nbsp;&nbsp;&nbsp;&lt;scanFilter scanStart="false" scanStop="false" scanFailed="false"/&gt;<BR>
    *           &nbsp;&nbsp;&nbsp;&lt;vulnFilter severityThreshold="true" confirmed="true" unconfirmed="false"/&gt;<BR>
    *           &nbsp;&nbsp;&nbsp; &lt;smtpAlert sender="someone@zzz.com" server="smtpServer" port="12345" limitText="true"&gt;<BR>
    *           &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;Recipient&gt;email@xxx.com&lt;/Recipient&gt;<BR>
    *           &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;Recipient&gt;email@yyy.com&lt;/Recipient&gt;<BR>
    *           &nbsp;&nbsp;&nbsp;&lt;/smtpAlert&gt;<BR>&lt;/Alert&gt;
    *           </LI>
    *           <LI>
    *           &lt;Alert name="alert2" enabled="true" maxAlerts="10"&gt;<BR>
    *           &nbsp;&nbsp;&nbsp;&lt;scanFilter scanStart="true" scanStop="false" scanFailed="true"/&gt;<BR>
    *           &nbsp;&nbsp;&nbsp;&lt;vulnFilter severityThreshold="false" confirmed="true" unconfirmed="false"/&gt;<BR>
    *           &nbsp;&nbsp;&nbsp;&lt;snmpAlert community="community" server="snmpServer" port="13456"/&gt;<BR>
    *           &lt;/Alert&gt;
    *           </LI>
    *           <LI>
    *           &lt;Alert name="alert3" enabled="true" maxAlerts="10"&gt;<BR>
    *           &nbsp;&nbsp;&nbsp;&lt;scanFilter scanStart="true" scanStop="true" scanFailed="false"/&gt;<BR>
    *           &nbsp;&nbsp;&nbsp;&lt;vulnFilter severityThreshold="false" confirmed="true" unconfirmed="true"/&gt;<BR>
    *           &nbsp;&nbsp;&nbsp;&lt;sysLogAlert server="syslogServer" port="13457"/&gt;<BR>
    *           &lt;/Alert&gt;
    *           </LI>
    *        </OL>
    *        More than one alert can be specified at the time of the site save.
    *        Please see {@link SiteSaveRequestAlertsGenerator} for more details
    *        and for a reference implementation. For QA testing you should
    *        construct your own {@link IContentGenerator} to generate all the
    *        edge test cases you can think of.
    * @param configName a String that represents the name of the scan 
    *        configuration for the site to be saved.
    * @param configVersion a positive integer that represents the scan
    *        configuration version of the site to be saved.
    * @param configId a positive integer that represents the scan
    *        configuration's id of the site to be saved.
    * @param configTemplateId a String that represents the template id
    *        associated with the scan configuration of the site to be saved.
    * @param configEngineId a String that represents the engine id associated
    *        with the scan configuration of the site to be saved. This field is
    *        optional.
    * @param scheduleEnabled 1 to enable the schedule 0 to disable it. This is
    *        optional and only present if the Scan Configuration of the site is
    *        to have a schedule associated with it.
    * @param scheduleIncremental non-negative integer that tells the incremental
    *        scan nature of the Scan configuration associated with the site.
    *        This is optional and only present if the Scan Configuration of the
    *        site is to have a schedule associated with it.
    * @param scheduleType the type of schedule associated with the scan
    *        configuration. One of: <br>daily<br>hourly<br>monthly-date<br>
    *        monthly-day<br>weekly. This is optional but required if the Scan
    *        Configuration of the site is to have schedule associated with it.
    * @param scheduleInterval a non negative integer representing the interval
    *        of the schedule.This is optional but required if the Scan
    *        Configuration of the site is to have a schedule associated with it.
    * @param scheduleStart a String that represents the schedule's start date.
    *        This is optional but required if the Scan Configuration of the site
    *        is to have a schedule associated with it.
    * @param scheduleMaxDuration a String that represents the maximum duration
    *        allowed for the scan. This is optional and only present if the Scan
    *        Configuration of the site is to have a schedule associated with it.
    * @param scheduleNotValidAfter a String that represents the expiration of 
    *        the schedule. This is optional and only present if the Scan
    *        Configuration of the site is to have a schedule associated with it.
    * @return The response from the API.
    * @throws IOException When the API call cannot be performed.
    * @throws APIException if the API call is not successful or the parsing of
    *         the response is not correct.
    */
   private APIResponse baiscSiteSaveRequest(
      String sessionId,
      String syncId,
      String siteId,
      String siteName,
      String siteDescription,
      String siteRiskFactor,
      IContentGenerator siteHostsHostGenerator,
      IContentGenerator siteHostsRangeGenerator,
      IContentGenerator credentialsGenerator,
      IContentGenerator alertsGenerator,
      String siteScanConfigName,
      String siteScanConfigConfigVersion,
      String siteScanConfigConfigId,
      String siteScanConfigTemplateId,
      String siteScanConfigEngineId,
      String scheduleEnabled,
      String scheduleIncremental,
      String scheduleType,
      String scheduleInterval,
      String scheduleStart,
      String scheduleMaxDuration,
      String scheduleNotValidAfter)
      throws IOException, APIException
   {
      final TemplateAPIRequest request = new SiteSaveRequest(
         sessionId,
         syncId,
         siteId,
         siteName,
         siteDescription,
         siteRiskFactor,
         siteHostsHostGenerator,
         siteHostsRangeGenerator,
         credentialsGenerator,
         alertsGenerator,
         siteScanConfigName,
         siteScanConfigConfigVersion,
         siteScanConfigConfigId,
         siteScanConfigTemplateId,
         siteScanConfigEngineId,
         scheduleEnabled,
         scheduleIncremental,
         scheduleType,
         scheduleInterval,
         scheduleStart,
         scheduleMaxDuration,
         scheduleNotValidAfter
         );
      final APIResponse response = new APIResponse(
         request(open(request), auth(request)),
         request.getRequestXML());
      if (response.grabNode("//Failure") != null)
      {
         m_errorHandler.handleError(
            request,
            response,
            this,
            "SiteSaveRequest failed");
      }
      return response;
   }

   /**
    * Creates a new User Save Request with any given sites content generator and
    * any given group content generator. Sets the first API supported version to
    * 1.0 and the last supported version to 1.1.
    *
    * NOTE: All parameters are strings or generators, since we want to be able
    * to test edge cases and simulate incorrect usage of the tool for robustness
    *
    * @param sessionId the session to be used if different from the current
    *        acquired one (You acquire one when you authenticate correctly with
    *        the login method in the {@link APISession} class). This is a
    *        String of 40 characters.
    * @param syncId the synchronization id to identify the response associated 
    *        with the response in asynchronous environments. It can be any 
    *        string. This field is optional.
    * @param allGroups true if the user has access to all groups, false
    *        otherwise (specified when creating/saving a config and Group
    *        elements are not specified)
    * @param allSites true if the user has access to all sites, false otherwise
    *        (specified when creating/saving a config and Site elements are not
    *        specified)
    * @param authSrcId the positive integer that identifies the authentication 
    *        source to be used to authenticate the user. Should be one of the
    *        existing authentication for correctness.
    * @param email the email of the user, should have the right email format. 
    *        This field is optional.
    * @param enabled true (or 1) if the user is enabled, false (or 0) otherwise.
    *        This field is optional.
    * @param fullname a String that represents the full name of the user.
    * @param id the non negative integer that identifies the user, -1 to create
    *        a new user.
    * @param name the String that represents the name of the user. This field
    *        may not be updated, so only used for new users. You can still
    *        specify it and NeXpose will throw an Exception.
    * @param password the String that represents the password of the user.\
    *        This field is optional, if present, the user's new password.
    * @param roleName the role name of the user, should be one of
    *        global-admin for Global Administrator.
    *        security-manager for Security Manager.
    *        site-admin for Site Administrator.
    *        system-admin for System Administrator.
    *        user for User.
    *        custom for Custom. NOTE: Be aware that there is not role management
    *        enabled to date (1/6/2010)
    * @param sitesGenerator a Generator that knows how to output user sites with
    *        IDs associated to it e.g. &lt;Site id="X"/&gt; where X is a non
    *        negative Integer. Please see {@link UserSaveRequestSitesGenerator}
    *        for a reference implementation. For QA testing you should construct
    *        your own {@link IContentGenerator} to generate all the edge test
    *        cases you can think of.
    * @param groupsGenerator a Generator that knows how to output user groups 
    *        with IDs associated to it e.g. &lt;Group id="X"/&gt; where X is a
    *        non negative Integer. Please see
    *        {@link UserSaveRequestGroupsGenerator} for a reference
    *        implementation. For QA testing you should construct your own
    *        {@link IContentGenerator} to generate all the edge test cases you
    *        can think of.
    * @return APIResponse the response that came from the NeXpose instance.
    * @throws IOException When the API call cannot be performed.
    * @throws APIException if the API call is not successful or the parsing of
    *         the response is not correct.
    */
   private APIResponse basicUserSaveRequest(
      String sessionId,
      String syncId,
      String allGroups,
      String allSites,
      String authSrcId,
      String email,
      String enabled,
      String fullname,
      String id,
      String name,
      String password,
      String roleName,
      IContentGenerator sitesGenerator,
      IContentGenerator groupsGenerator)
      throws IOException, APIException
   {
      final TemplateAPIRequest request = new UserSaveRequest(
               sessionId,
               syncId,
               allGroups,
               allSites,
               authSrcId,
               email,
               enabled,
               fullname,
               id,
               name,
               password,
               roleName,
               sitesGenerator,
               groupsGenerator);
      final APIResponse response = new APIResponse(
               request(open(request),auth(request)),
               request.getRequestXML());
      if (response.grabNode("//Failure") != null)
      {
         m_errorHandler.handleError(
            request,
            response,
            this,
            "UserSaveRequest failed");
      }
      return response;
   }

   /**
    * Creates a new User Save Request with any given sites content generator and
    * any given group content generator. Sets the first API supported version to
    * 1.0 and the last supported version to 1.1.
    *
    * NOTE: All parameters are strings or generators, since we want to be able
    * to test edge cases and simulate incorrect usage of the tool for robustness
    *
    * @param sessionId the session to be used if different from the current
    *        acquired one (You acquire one when you authenticate correctly with
    *        the login method in the {@link APISession} class). This is a
    *        String of 40 characters.
    * @param syncId the synchronization id to identify the response associated
    *        with the response in asynchronous environments. It can be any
    *        string. This field is optional.
    * @param allGroups true if the user has access to all groups, false
    *        otherwise (specified when creating/saving a config and Group
    *        elements are not specified)
    * @param allSites true if the user has access to all sites, false otherwise
    *        (specified when creating/saving a config and Site elements are not
    *        specified)
    * @param authSrcId the positive integer that identifies the authentication
    *        source to be used to authenticate the user. Should be one of the
    *        existing authentication for correctness.
    * @param email the email of the user, should have the right email format.
    *        This field is optional.
    * @param enabled true (or 1) if the user is enabled, false (or 0) otherwise.
    *        This field is optional.
    * @param fullname a String that represents the full name of the user.
    * @param id the non negative integer that identifies the user, -1 to create
    *        a new user.
    * @param name the String that represents the name of the user. This field
    *        may not be updated, so only used for new users. You can still
    *        specify it and NeXpose will throw an Exception.
    * @param roleName the role name of the user, should be one of
    *        global-admin for Global Administrator.
    *        security-manager for Security Manager.
    *        site-admin for Site Administrator.
    *        system-admin for System Administrator.
    *        user for User.
    *        custom for Custom. NOTE: Be aware that there is not role management
    *        enabled to date (1/6/2010)
    * @param sitesGenerator a Generator that knows how to output user sites with
    *        IDs associated to it e.g. &lt;Site id="X"/&gt; where X is a non
    *        negative Integer. Please see {@link UserSaveRequestSitesGenerator}
    *        for a reference implementation. For QA testing you should construct
    *        your own {@link IContentGenerator} to generate all the edge test
    *        cases you can think of.
    * @param groupsGenerator a Generator that knows how to output user groups
    *        with IDs associated to it e.g. &lt;Group id="X"/&gt; where X is a
    *        non negative Integer. Please see
    *        {@link UserSaveRequestGroupsGenerator} for a reference
    *        implementation. For QA testing you should construct your own
    *        {@link IContentGenerator} to generate all the edge test cases you
    *        can think of.
    * @return APIResponse the response that came from the NeXpose instance.
    * @throws IOException When the API call cannot be performed.
    * @throws APIException if the API call is not successful or the parsing of
    *         the response is not correct.
    */
   private APIResponse basicUserSaveRequest(
      String sessionId,
      String syncId,
      String allGroups,
      String allSites,
      String authSrcId,
      String email,
      String enabled,
      String fullname,
      String id,
      String name,
      String roleName,
      IContentGenerator sitesGenerator,
      IContentGenerator groupsGenerator)
      throws IOException, APIException
   {
      final TemplateAPIRequest request = new ExternalUserSaveRequest(
               sessionId,
               syncId,
               allGroups,
               allSites,
               authSrcId,
               email,
               enabled,
               fullname,
               id,
               name,
               roleName,
               sitesGenerator,
               groupsGenerator);
      final APIResponse response = new APIResponse(
               request(open(request),auth(request)),
               request.getRequestXML());
      if (response.grabNode("//Failure") != null)
      {
         m_errorHandler.handleError(
            request,
            response,
            this,
            "UserSaveRequest failed");
      }
      return response;
   }

   /**
    * Changes the API URL to use the protocol version specified as a parameter. 
    * If the API call was not supported on the version the session was created 
    * for an {@link APIException} is thrown.
    *
    * @param lastSupportedVersion the last supported version of the API call
    * @param firstSupportedVersion the first supported version of the API call.
    * @throws APIException When the first supported version of the API call is
    *         greater than the version the session was created for.
    */
   private boolean adjustAPIURL(
      APISupportedVersion lastSupportedVersion,
      APISupportedVersion firstSupportedVersion)
      throws APIException
   {
      if (m_adjustVersions)
      {
         if (m_apiVersion.compareTo(lastSupportedVersion) > 0)
         {
            try
            {
               m_apiURL = new URL(m_nxURL.toString()
                  + "/api/" + lastSupportedVersion.getVersion() 
                  + "/"
                  + m_apiProtocol);
               return true;
            } catch (MalformedURLException e)
            {
               // This is never thrown, the original error would have been
               // thrown on the APISession constructor method.
            }
         } else if (m_apiVersion.compareTo(firstSupportedVersion) < 0)
         {
            throw new APIException("API call is not avilable until version : "
               + firstSupportedVersion);
         }
      }
      return false;
   }

   /**
    * Request to get the multi-tenant user listing.
    *
    * @param sessionID the session to use if different from the one obtained 
    * by logging in, useful to test edge cases.
    * @param syncID the synchronization id to identify the response.
    * 
    * @return The response from the API.
    * 
    * @throws IOException When the API call cannot be performed.
    * @throws APIException if the API call is not successful or the parsing of
    *         the response is not correct.
    */
    @Override
    public Iterable<MultiTenantUserSummary> multiTenantUserListingRequest(String sessionID, String syncID)
       throws IOException, APIException
    {
    	List<MultiTenantUserSummary> multiTenantList = null;
    	final TemplateAPIRequest request = new MultiTenantUserListingRequest(sessionID, syncID);
    	final APIResponse response = new APIResponse(request(open(request), auth(request)),request.getRequestXML());
    	m_apiResponse = response;
    	if (response.grabNode("//Failure") != null)
    	{
    		m_errorHandler.handleError(request, response, this, "MultiTenantUserListingRequest failed");
    	}
    	final NodeList multiTenantNodes = response.grabNodes("/MultiTenantUserListingResponse/MultiTenantUserSummaries/MultiTenantUserSummary");
      
    	if (multiTenantNodes != null)
    	{
    		multiTenantList = new ArrayList<MultiTenantUserSummary>();
    		for (int i = 0; i < multiTenantNodes.getLength(); i++)
    		{
    			Element multiTenantNode = (Element) multiTenantNodes.item(i);
    			MultiTenantUserSummary multiTenantSummary = new MultiTenantUserSummary(multiTenantNode);
    			multiTenantList.add(multiTenantSummary);
    		}
    	}
    	return multiTenantList;
   }

   /**
    * Request to get the multi-tenant user config.
    *
    * @param sessionID the session to use if different from the one obtained
    *        by logging in, useful to test edge cases.
    * @param syncID the synchronization id to identify the response.
    * @param userID The multi-tenant user ID.
    * 
    * @return The response from the API.
    * 
    * @throws IOException When the API call cannot be performed.
    * @throws APIException if the API call is not successful or the parsing of
    *         the response is not correct.
    */
   @Override
   public APIResponse multiTenantUserConfigRequest(String sessionID, String syncID, String userID)
      throws IOException, APIException
   {
      final TemplateAPIRequest request = new MultiTenantUserConfigRequest(sessionID, syncID, userID);
      final APIResponse response = new APIResponse(request(open(request), auth(request)),request.getRequestXML());
      if (response.grabNode("//Failure") != null)
      {
         m_errorHandler.handleError(request, response, this, "MultiTenantUserConfigRequest failed");
      }
      return response;
   }

   /**
    * Request to delete the multi-tenant user.
    *
    * @param sessionID the session to use if different from the one obtained
    *        by logging in, useful to test edge cases.
    * @param syncID the synchronization id to identify the response.
    * @param userID The multi-tenant user ID.
    * 
    * @return The response from the API.
    * 
    * @throws IOException When the API call cannot be performed.
    * @throws APIException if the API call is not successful or the parsing of
    *         the response is not correct.
    */
   @Override
   public APIResponse multiTenantUserDeleteRequest(String sessionID, String syncID, String userID)
      throws IOException, APIException
   {
      final TemplateAPIRequest request = new MultiTenantUserDeleteRequest(sessionID, syncID, userID);
      final APIResponse response = new APIResponse(request(open(request), auth(request)),request.getRequestXML());
      if (response.grabNode("//Failure") != null)
      {
         m_errorHandler.handleError(request, response, this, "MultiTenantUserDeleteRequest failed");
      }
      return response;
   }

   /**
    * Request to get a silo profile config.
    *
    * @param siloID the id of the silo
    * @param sessionID the session to use if different from the one obtained
    *        by logging in, useful to test edge cases.
    * @param syncID the synchronization id to identify the response.
    * 
    * @return The response from the API.
    * 
    * @throws IOException When the API call cannot be performed.
    * @throws APIException if the API call is not successful or the parsing of
    *         the response is not correct.
    */
   @Override
   public APIResponse siloProfileConfigRequest(String sessionID, String syncID, String siloProfileID)
      throws IOException, APIException
   {
      final TemplateAPIRequest request = new SiloProfileConfigRequest(sessionID, syncID, siloProfileID);
      final APIResponse response = new APIResponse(request(open(request), auth(request)),request.getRequestXML());
      if (response.grabNode("//Failure") != null)
      {
         m_errorHandler.handleError(request, response, this, "SiloProfileConfigRequest failed");
      }
      return response;
   }

   /**
    * Request to create a silo profile.
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
    * @param globalReportTemplate The global report template
    * @param globalScanEngine The global scan engine.
    * @param globalScanTemplate The global scan template.
    * @param licensedModule The license module.
    * 
    * @return The response from the API.
    * 
    * @throws IOException When the API call cannot be performed.
    * @throws APIException if the API call is not successful or the parsing of
    *         the response is not correct.
    */
   @Override
   public APIResponse siloProfileCreateRequest(
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
         throws IOException, APIException
   {
      final TemplateAPIRequest request =
         new SiloProfileCreateRequest(
            sessionID, 
            syncID, 
            hasGlobalReportTemplates,
            hasGlobalEngines,
            hasGlobalScanTemplates,
            hasLicensedModules,
            description,
            id,
            name,
            globalReportTemplates,
            globalScanEngines,
            globalScanTemplates,
            licensedModules);
      final APIResponse response = new APIResponse(request(open(request), auth(request)),request.getRequestXML());
      if (response.grabNode("//Failure") != null)
      {
         m_errorHandler.handleError(request, response, this, "SiloProfileCreateRequest failed");
      }
      return response;
   }

   /**
    * Request to delete a silo profile.
    *
    * @param siloProfileID the id of the silo
    * @param sessionID the session to use if different from the one obtained
    *        by logging in, useful to test edge cases.
    * @param syncID the synchronization id to identify the response.
    * 
    * @return The response from the API.
    * 
    * @throws IOException When the API call cannot be performed.
    * @throws APIException if the API call is not successful or the parsing of
    *         the response is not correct.
    */
   @Override
   public APIResponse siloProfileDeleteRequest(
      String sessionID, 
      String syncID,
      String siloProfileID)
         throws IOException, APIException
   {
      final TemplateAPIRequest request = new SiloProfileDeleteRequest(sessionID, syncID, siloProfileID);
      final APIResponse response = new APIResponse(request(open(request), auth(request)),request.getRequestXML());
      if (response.grabNode("//Failure") != null)
      {
         m_errorHandler.handleError(request, response, this, "SiloProfileDeleteRequest failed");
      }
      return response;
   }
   
   /**
    * Request to list all silo profiles.
    *
    * @param sessionID the session to use if different from the one obtained
    *        by logging in, useful to test edge cases.
    * @param syncID the synchronization id to identify the response.
    * 
    * @return The response from the API.
    * 
    * @throws IOException When the API call cannot be performed.
    * @throws APIException if the API call is not successful or the parsing of
    *         the response is not correct.
    */
   @Override
   public APIResponse siloProfileListingRequest(
      String sessionID, 
      String syncID)
         throws IOException, APIException
   {
      final TemplateAPIRequest request = new SiloProfileListingRequest(sessionID, syncID);
      final APIResponse response = new APIResponse(request(open(request), auth(request)), request.getRequestXML());
      if (response.grabNode("//Failure") != null)
      {
         m_errorHandler.handleError(request, response, this, "SiloProfileListingRequest failed");
      }

      return response;
   }

   /**
    * Request to update a silo profile.
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
    * @param globalReportTemplate The global report template
    * @param globalScanEngine The global scan engine.
    * @param globalScanTemplate The global scan template.
    * @param licensedModule The license module.
    * 
    * @return The response from the API.
    * 
    * @throws IOException When the API call cannot be performed.
    * @throws APIException if the API call is not successful or the parsing of
    *         the response is not correct.
    */
   @Override
   public APIResponse siloProfileUpdateRequest(
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
         throws IOException, APIException
   {
      final TemplateAPIRequest request = 
         new SiloProfileUpdateRequest(
            sessionID, 
            syncID, 
            hasGlobalReportTemplates,
            hasGlobalEngines,
            hasGlobalScanTemplates,
            hasLicensedModules,
            description,
            id,
            name,
            globalReportTemplates,
            globalScanEngines,
            globalScanTemplates,
            licensedModules);
            
      final APIResponse response = new APIResponse(request(open(request), auth(request)),request.getRequestXML());
      if (response.grabNode("//Failure") != null)
      {
         m_errorHandler.handleError(request, response, this, "SiloProfileUpdateRequest failed");
      }
      return response;
   }

   /**
    * Creates a Role through the NeXpose API.
    * 
    * @param sessionId The session to submit the request with. May not be {@code null} nor empty and must
    * be a 40 character hex {@link String}.
    * @param syncId The sync id to identify the response. May be {@code null}.
    * @param roleName The name of the role. May not be {@code null} nor empty and must be less than 255 character.
    * @param roleDescription The description of the role. May be {@code null} or empty.
    * @param roleEnabled The flag to enable the role. May not be {@code null} nor empty.
    * @param createReportEnabled The flag to enable the createReportEnabled privilege. May not be {@code null} nor empty.
    * @param configureSiteSettingsEnabled The flag to enable the configureSiteSettingsEnabled privilege. May not be {@code null} nor empty.
    * @param configureTargetsEnabled The flag to enable the configureTargetsEnabled privilege. May not be {@code null} nor empty.
    * @param configureEnginesEnabled The flag to enable the configureEnginesEnabled privilege. May not be {@code null} nor empty.
    * @param configureScanTemplatesEnabled The flag to enable the configureScanTemplatesEnabled privilege. May not be {@code null} nor empty. 
    * @param configureAlertsEnabled The flag to enable the configureAlertsEnabled privilege. May not be {@code null} nor empty.
    * @param configureScheduleScansEnabled The flag to enable the configureScheduleScansEnabled privilege. May not be {@code null} nor empty.
    * @param configureCredentialsEnabled The flag to enable the configureCredentialsEnabled privilege. May not be {@code null} nor empty.  
    * @param manualScansEnabled The flag to enable the manualScansEnabled privilege. May not be {@code null} nor empty.
    * @param purgeDataEnabled The flag to enable the purgeDataEnabled privilege. May not be {@code null} nor empty.
    * @param viewGroupAssetDataEnabled The flag to enable the viewGroupAssetDataEnabled privilege. May not be {@code null} nor empty.
    * @param configureAssetsEnabled The flag to enable the configureAssetsEnabled privilege. May not be {@code null} nor empty.
    *  
    * @return The response from the server.
    * 
    * @throws IOException If an IO error occurs.
    * @throws APIException If the API call is not successful or the parsing of the response is not correct.
    */
   @Override
   public APIResponse roleCreateRequest(String sessionId, String syncId, String roleName, String roleFullName, String roleDescription,
      String roleEnabled,String scope, String createReportEnabled, String configureGlobalSettingsEnabled, String manageSitesEnabled,String manageAssetGroupsEnabled,String manageScanTemplatesEnabled,String manageReportTemplatesEnabled,String manageScanEnginesEnabled,String submitVulnExceptionsEnabled,String approveVulnExceptionsEnabled,String deleteVulnExceptionsEnabled,String addUsersToSiteEnabled, String addUsersToGroupEnabled, String createTicketEnabled, String closeTicketEnabled, String ticketAssigneeEnabled, String viewAssetDataEnabled, String configureSiteSettingsEnabled, 
      String configureTargetsEnabled, String configureEnginesEnabled, String configureScanTemplatesEnabled, String configureAlertsEnabled,
      String configureScheduleScansEnabled, String configureCredentialsEnabled, String manualScansEnabled, String purgeDataEnabled, 
      String viewGroupAssetDataEnabled, String configureAssetsEnabled)
      throws IOException, APIException
   {
      final TemplateAPIRequest request;
      request = new RoleCreateRequest(sessionId, syncId, roleName, roleFullName, roleDescription, roleEnabled,scope,
         createReportEnabled,configureGlobalSettingsEnabled, manageSitesEnabled,manageAssetGroupsEnabled,manageScanTemplatesEnabled,manageReportTemplatesEnabled,manageScanEnginesEnabled,submitVulnExceptionsEnabled,approveVulnExceptionsEnabled,deleteVulnExceptionsEnabled,addUsersToSiteEnabled,addUsersToGroupEnabled,createTicketEnabled,closeTicketEnabled,ticketAssigneeEnabled,viewAssetDataEnabled, configureSiteSettingsEnabled, configureTargetsEnabled, 
         configureEnginesEnabled, configureScanTemplatesEnabled, configureAlertsEnabled, configureScheduleScansEnabled,
         configureCredentialsEnabled, manualScansEnabled, purgeDataEnabled, viewGroupAssetDataEnabled, configureAssetsEnabled);
      final APIResponse response = new APIResponse(request(open(request), auth(request)), request.getRequestXML());
      if (response.grabNode("//Failure") != null)
      {
         m_errorHandler.handleError(request, response, this, "RoleCreateRequest failed");
      }
      return response;
   }
   
   /**
    * Updates a Role through the NeXpose API.
    * 
    * @param sessionId The session to submit the request with. May not be {@code null} nor empty and must
    * be a 40 character hex {@link String}.
    * @param syncId The sync id to identify the response. May be {@code null}.
    * @param roleName The name of the role. May not be {@code null} nor empty and must be less than 255 character.
	* @param roleID The id of the role. May not be {@code null} nor empty . 
    * @param roleDescription The description of the role. May be {@code null} or empty.
    * @param roleEnabled The flag to enable the role. May not be {@code null} nor empty.
    * @param createReportEnabled The flag to enable the createReportEnabled privilege. May not be {@code null} nor empty.
    * @param configureSiteSettingsEnabled The flag to enable the configureSiteSettingsEnabled privilege. May not be {@code null} nor empty.
    * @param configureTargetsEnabled The flag to enable the configureTargetsEnabled privilege. May not be {@code null} nor empty.
    * @param configureEnginesEnabled The flag to enable the configureEnginesEnabled privilege. May not be {@code null} nor empty.
    * @param configureScanTemplatesEnabled The flag to enable the configureScanTemplatesEnabled privilege. May not be {@code null} nor empty. 
    * @param configureAlertsEnabled The flag to enable the configureAlertsEnabled privilege. May not be {@code null} nor empty.
    * @param configureScheduleScansEnabled The flag to enable the configureScheduleScansEnabled privilege. May not be {@code null} nor empty.
    * @param configureCredentialsEnabled The flag to enable the configureCredentialsEnabled privilege. May not be {@code null} nor empty.  
    * @param manualScansEnabled The flag to enable the manualScansEnabled privilege. May not be {@code null} nor empty.
    * @param purgeDataEnabled The flag to enable the purgeDataEnabled privilege. May not be {@code null} nor empty.
    * @param viewGroupAssetDataEnabled The flag to enable the viewGroupAssetDataEnabled privilege. May not be {@code null} nor empty.
    * @param configureAssetsEnabled The flag to enable the configureAssetsEnabled privilege. May not be {@code null} nor empty.
    *  
    * @return The response from the server.
    * 
    * @throws IOException If an IO error occurs.
    * @throws APIException If the API call is not successful or the parsing of the response is not correct.
    */
   @Override
   public APIResponse roleUpdateRequest(String sessionId, String syncId, String roleName, String roleID, String roleFullName,
      String roleDescription, String roleEnabled,String scope, String createReportEnabled, String configureGlobalSettingsEnabled, String manageSitesEnabled,
      String manageAssetGroupsEnabled,String manageScanTemplatesEnabled,String manageReportTemplatesEnabled,String manageScanEnginesEnabled,
      String submitVulnExceptionsEnabled,String approveVulnExceptionsEnabled,String deleteVulnExceptionsEnabled,String addUsersToSiteEnabled, 
      String addUsersToGroupEnabled, String createTicketEnabled, String closeTicketEnabled, String ticketAssigneeEnabled,
      String viewAssetDataEnabled, String configureSiteSettingsEnabled,
      String  configureTargetsEnabled,String configureEnginesEnabled, String configureScanTemplatesEnabled, String configureAlertsEnabled,
      String configureScheduleScansEnabled, String configureCredentialsEnabled, String manualScansEnabled, String purgeDataEnabled,
      String viewGroupAssetDataEnabled, String configureAssetsEnabled) throws IOException, APIException
   {
      final TemplateAPIRequest request;
      request = new RoleUpdateRequest(sessionId, syncId, roleName, roleID, roleFullName, roleDescription, roleEnabled, scope, createReportEnabled,
    		  configureGlobalSettingsEnabled, manageSitesEnabled,manageAssetGroupsEnabled,manageScanTemplatesEnabled, 
    		  manageReportTemplatesEnabled,manageScanEnginesEnabled,submitVulnExceptionsEnabled,approveVulnExceptionsEnabled,
    		  deleteVulnExceptionsEnabled,addUsersToSiteEnabled,addUsersToGroupEnabled,createTicketEnabled, closeTicketEnabled, ticketAssigneeEnabled,
         viewAssetDataEnabled, configureSiteSettingsEnabled, configureTargetsEnabled,configureEnginesEnabled, configureScanTemplatesEnabled,
         configureAlertsEnabled, configureScheduleScansEnabled, configureCredentialsEnabled, manualScansEnabled,
         purgeDataEnabled, viewGroupAssetDataEnabled, configureAssetsEnabled);
      final APIResponse response = new APIResponse(request(open(request), auth(request)), request.getRequestXML());
      if (response.grabNode("//Failure") != null)
      {
         m_errorHandler.handleError(request, response, this, "RoleUpdateRequest failed");
      }
      return response;
   }
   
   /**
    * Requests a role's detail through the NeXpose API.
    * 
    * @param sessionId the session to submit the request with. May not be {@code null} nor empty and must
    * be a 40 character hex {@link String}.
    * @param syncId the sync id to identify the response. May be {@code null}.
    * @param rolesGenerator The names of the roles to request to get details. May be {@code null}.
    * 
    * @return The response from the server.
    * 
    * @throws IOException If an IO error occurs.
    * @throws APIException If the API call is not successful or the parsing of the response is not correct.
    */
   @Override
   public APIResponse roleDetailsRequest(String sessionId, String syncId, IContentGenerator rolesGenerator)
      throws IOException, APIException
   {
      final TemplateAPIRequest request;
      request = new RoleDetailsRequest(sessionId, syncId, rolesGenerator);
      final APIResponse response = new APIResponse(request(open(request), auth(request)), request.getRequestXML());
      if (response.grabNode("//Failure") != null)
      {
         m_errorHandler.handleError(request, response, this, "RoleDetailsRequest failed");
      }
      return response;
   }
   
   /**
    * Requests to delete a role through the NeXpose API.
    * 
    * @param sessionId the session to submit the request with. May not be {@code null} nor empty and must
    * be a 40 character hex {@link String}.
    * @param syncId the sync id to identify the response. May be {@code null}.
    * @param roleName The name of the role to request to delete. May be {@code null}.
    * 
    * @return The response from the server.
    * 
    * @throws IOException If an IO error occurs.
    * @throws APIException If the API call is not successful or the parsing of the response is not correct.
    */
   @Override
   public APIResponse roleDeleteRequest(String sessionId, String syncId, String roleName,String scope)
      throws IOException, APIException
   {
      final TemplateAPIRequest request = new RoleDeleteRequest(sessionId, syncId, roleName,scope);
      final APIResponse response = new APIResponse(request(open(request), auth(request)), request.getRequestXML());
      if (response.grabNode("//Failure") != null)
      {
         m_errorHandler.handleError(request, response, this, "RoleDeleteRequest failed");
      }
      return response;
   }   
   
   /**
    * Lists roles.
    * 
    * @param sessionId the session to submit the request with. May not be {@code null} nor empty and must
    * be a 40 character hex {@link String}.
    * @param syncId the sync id to identify the response. May be {@code null}.
    * 
    * @return a list of roles of type (@link RoleSummary}.
    * 
    * @throws IOException If an IO error occurs.
    * @throws APIException If the API call is not successful or the parsing of the response is not correct.
    */
   @Override
   public Iterable<RoleSummary> roleListRequest(String sessionId, String syncId) throws IOException, APIException
   {
      final TemplateAPIRequest request = new RoleListingRequest(sessionId, syncId);
      List<RoleSummary> rolesList = null;
      final APIResponse response = new APIResponse(request(open(request), auth(request)), request.getRequestXML());
      m_apiResponse = response;
      if (response.grabNode("//Failure") != null)
      {
         m_errorHandler.handleError(request, response, this, "RoleListingRequest failed");
      }
      final NodeList roles = response.grabNodes("/RoleListingResponse/RoleSummary");
      if (roles != null)
      {
         rolesList = new ArrayList<RoleSummary>();
         for (int i = 0; i < roles.getLength(); i++)
         {
            Element roleNode = (Element) roles.item(i);
            RoleSummary roleSummary = new RoleSummary(roleNode);
            rolesList.add(roleSummary);
         }
      }
      return rolesList;
   }

    public APIResponse reportSaveRequest(String sessionId, String syncId,
                                  String generateNow,
                                  String reportId,
                                  String name,
                                  String templateId,
                                  String format,
                                  String owner,
                                  String timeZone,
                                  String description,
                                  IContentGenerator filtersGenerator,
                                  IContentGenerator baselineGenerator,
                                  IContentGenerator generateGenerator,
                                  IContentGenerator deliveryGenerator,
                                  IContentGenerator dbExportGenerator) throws IOException, APIException
  {
        final TemplateAPIRequest request = new ReportSaveRequest(sessionId, syncId,
                                                                 generateNow,
                                                                 reportId,
                                                                 name,
                                                                 templateId,
                                                                 format,
                                                                 owner,
                                                                 timeZone,
                                                                 description,
                                                                 filtersGenerator,
                                                                 baselineGenerator,
                                                                 generateGenerator,
                                                                 deliveryGenerator,
                                                                 dbExportGenerator);
      final APIResponse response = new APIResponse(request(open(request), auth(request)), request.getRequestXML());
      if (response.grabNode("//Failure") != null)
      {
         m_errorHandler.handleError(request, response, this, "ReportDeleteRequest failed");
      }
      return response;
  }

  public APIResponse reportGenerateRequest(String sessionId, String syncId, String reportId) throws IOException, APIException
  {
        final TemplateAPIRequest request = new ReportGenerateRequest(sessionId, syncId, reportId);
      final APIResponse response = new APIResponse(request(open(request), auth(request)), request.getRequestXML());
      if (response.grabNode("//Failure") != null)
      {
         m_errorHandler.handleError(request, response, this, "ReportDeleteRequest failed");
      }
      return response;
  }

  public APIResponse reportDeleteRequest(String sessionId, String syncId, String reportId, String reportCfgId) throws IOException, APIException
  {
        final TemplateAPIRequest request = new ReportDeleteRequest(sessionId, syncId, reportId, reportCfgId);
      final APIResponse response = new APIResponse(request(open(request), auth(request)), request.getRequestXML());
      if (response.grabNode("//Failure") != null)
      {
         m_errorHandler.handleError(request, response, this, "ReportDeleteRequest failed");
      }
      return response;
  }
   /**
    * Resets the API url to the original value for the session
    */
   private void resetAPIURL()
   {
      try
      {
         m_apiURL = new URL(
            m_nxURL.toString()
            + "/api/"
            + m_apiVersion.getVersion()
            + "/" + m_apiProtocol);
      } catch (MalformedURLException e)
      {
         // This is never thrown since the original error would have been thrown
         // on the APISession constructor method.
      }
   }
   
   /**
    * Provides an APIRequest object with the current session ID if not set in
    * the parameters. If set in the parameters, the session-id is left alone.
    *
    * @param request The request to be modified by adding the session ID to it.
    * @throws APIException if the API call is not successful or the parsing of
    *         the response is not correct.
    */
   private TemplateAPIRequest auth(TemplateAPIRequest request)
      throws APIException
   {
      if (!request.isSet("session-id") && m_sessionID != null)
         request.set("session-id", m_sessionID);
      if (request instanceof RawXMLAPIRequest)
      {
         request.set("session-id", m_sessionID );
      }
      return request;
   }

   /**
    * Sends the given API request to the NeXpose server's API endpoint and
    * returns the response.
    *
    * @param connection The connection to the NeXpose server
    * @param request The API request to send
    * @return The response body
    * @throws IOException When the API call cannot be performed.
    * @throws APIException if the API call is not successful or the parsing of
    *         the response is not correct.
    */
   private Document request(URLConnection connection, APIRequest request)
      throws IOException, APIException
   {
      Reader reader = post(connection, request.toXML());
      try
      {
         return DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(reader));
      } catch (SAXException e)
      {
         throw new APIException("Error parsing API response", e);
      } catch (ParserConfigurationException e)
      {
         throw new APIException("Error parsing API response", e);
      } finally
      {
         reader.close();
      }
   }


     /**
    * Sends the given API request to the NeXpose server's API endpoint and
    * returns the response.
    *
    * @param connection The connection to the NeXpose server
    * @param request The API request to send
    * @return The response body
    * @throws IOException When the API call cannot be performed.
    * @throws APIException if the API call is not successful or the parsing of
    *         the response is not correct.
    */
   private Document multiPartRequest(URLConnection connection, APIRequest request, OutputStream outputStream)
      throws IOException, APIException
   {
      Reader reader = post(connection, request.toXML());
      try
      {
        DocumentBuilder documentBuilder =   DocumentBuilderFactory.newInstance().newDocumentBuilder();
        MimeReader mimeReader = new MimeReader(reader);
        Document responseDoc = documentBuilder.parse(mimeReader.copyParts(outputStream));
         return responseDoc;
      } catch (SAXException e)
      {
         throw new APIException("Error parsing API response", e);
      } catch (ParserConfigurationException e)
      {
         throw new APIException("Error parsing API response", e);
      } finally
      {
         reader.close();
      }
   }


  
   /**
    * POSTs the given XML request content to the NeXpose server's API endpoint
    * and returns the response.
    *
    * @param connection The connection to the NeXpose server
    * @param xml The XML content to POST
    * @return A reader over the response body
    * @throws IOException When the system cannot write to the output stream.
    */
   private BufferedReader post(URLConnection connection, String xml)
      throws IOException
   {
      OutputStream out = connection.getOutputStream();
      out.write(xml.getBytes("UTF-8"));
      out.flush();
      return new BufferedReader(
         new InputStreamReader(
            new BufferedInputStream(
               connection.getInputStream()),
               "UTF-8")
         );
   }
   
   /**
    * Opens a connection to the NeXpose server.
    *
    * @param request the request containing the version of the api to use.
    * @return An open URLConnection used for making a request.
    * @throws IOException when the connection cannot be established.
    * @throws APIException Thrown when the version of the api does not support
    *         the request.
    */
   private URLConnection open(APIRequest request) throws IOException, APIException
   {
      adjustAPIURL(
         request.getLastSupportedVersion(),
         request.getFirstSupportedVersion());
      final HttpsURLConnection conn =
         (HttpsURLConnection) m_apiURL.openConnection();
      conn.setSSLSocketFactory(ms_sslContext.getSocketFactory());
      // Create empty HostnameVerifier
      conn.setHostnameVerifier(new javax.net.ssl.HostnameVerifier()
      {
         public boolean verify(String urlHostName, SSLSession session)
         {
            return true;
         }
      });
      conn.setDoInput(true);
      conn.setDoOutput(true);
      conn.setUseCaches(false);
      conn.setRequestProperty("Content-Type", "text/xml");
      conn.setConnectTimeout(20000); // TODO: parameterize
      resetAPIURL();
      return conn;
   }
   
   /**
    * Initializes the SSL subsystem used for all connections. This method must
    * be called before trying to open a connection.
    *
    * @throws NoSuchAlgorithmException
    * @throws KeyManagementException
    */
   private static void initializeSSL()
      throws NoSuchAlgorithmException, KeyManagementException
   {
      if (ms_sslContext != null)
      {
         return; // SSL has already been initialized
      }
      // XXX XXX XXX WARNING! This code trusts all certs XXX XXX XXX
      TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager()
      {
         public X509Certificate[] getAcceptedIssuers()
         {
            return new X509Certificate[0];
         }
         public void checkClientTrusted(X509Certificate[] certs, String authType)
            throws java.security.cert.CertificateException
         {
         }
         public void checkServerTrusted(X509Certificate[] certs, String authType)
            throws java.security.cert.CertificateException
         {
         }
      } };
      ms_sslContext = SSLContext.getInstance("SSL");
      ms_sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
      HttpsURLConnection.setDefaultSSLSocketFactory(
         ms_sslContext.getSocketFactory());
   }
   /**
    * Retrieves the current session id.
    * @return the current session id.
    */
   @Override
   public String getSessionID()
   {
      return m_sessionID;
   }

   // ///////////////////////////////////////////////////////////////////////
   // non-Public fields
   // ///////////////////////////////////////////////////////////////////////
   /** The base URL of the NeXpose server, e.g. "https://ip:3780" */
   private URL m_nxURL;
   /** The api URL to send the requests */
   private URL m_apiURL;
   /** The user name for logging in to the API */
   private String m_username;
   /** The password for logging in to the API */
   private String m_password;
   /** The NeXpose API session ID, may be <code>null</code> */
   private String m_sessionID;
   /** The SSL context used for all connections */
   private static SSLContext ms_sslContext;
   /** API Error handler */
   private IAPIErrorHandler m_errorHandler;
   /** API Version */
   private APISupportedVersion m_apiVersion;
   /** Tells whether the API should adjust versions or not for API calls */
   private boolean m_adjustVersions = true;
   /** API Protocol */
   private String m_apiProtocol;
   /**Response gets saved here for listing operations*/
   private APIResponse m_apiResponse;
   static
   {
      try
      {
         initializeSSL();
      } catch (KeyManagementException e)
      {
         throw new RuntimeException("Unable to initialize SSL", e);
      } catch (NoSuchAlgorithmException e)
      {
         throw new RuntimeException("Unable to initialize SSL", e);
      }
   }
}
