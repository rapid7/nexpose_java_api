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

import org.rapid7.nexpose.api.domain.AssetGroupSummary;
import org.rapid7.nexpose.api.domain.EngineSummary;
import org.rapid7.nexpose.api.domain.SiteSummary;
import org.rapid7.nexpose.api.domain.TicketSummary;
import org.rapid7.nexpose.api.domain.UserSummary;
import org.rapid7.nexpose.api.generators.IContentGenerator;
import org.rapid7.nexpose.api.generators.SiteSaveRequestAlertsGenerator;
import org.rapid7.nexpose.api.generators.SiteSaveRequestCredentialsGenerator;
import org.rapid7.nexpose.api.generators.SiteSaveRequestHostsGenerator;
import org.rapid7.nexpose.api.generators.SiteSaveRequestRangesGenerator;
import org.rapid7.nexpose.api.generators.UserSaveRequestGroupsGenerator;
import org.rapid7.nexpose.api.generators.UserSaveRequestSitesGenerator;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
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
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
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
public class APISession
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
   public void setErrorHandler(final IAPIErrorHandler errorHandler)
   {
      m_errorHandler = errorHandler;
   }

   /**
    * Clears the error handler and sets the default one
    */
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
    * Opens a connection and logs in to the NeXpose API.
    *
    * @param syncId the synchronization id to identify the response associated
    *        with the response in asynchronous environments. It can be any
    *        string. This field is optional.
    * @return APIResponse the response from the server.
    * @throws IOException when the connection to the NeXpose instance fails.
    * @throws APIException When there is a problem processing the API request.
    */
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
   public APIResponse engineSaveRequest(
      String sessionId,
      String syncId,
      String engineConfigId,
      String engineConfigName,
      String engineConfigAddress,
      String engineConfigPort,
      String engineConfigPriority,
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
      return executeAPIRequest(request);
   }

   /**
    * Executes any API Request.
    *
    * @param request the {@link TemplateAPIRequest} to execute.
    * @param the node that contains the error (usually Failure)
    * @param the error Message to send to the default error handler.
    *
    * @return an {@link APIResponse} with the request's associated response.
    *
    * @throws IOException
    * @throws APIException
    */
   public APIResponse executeAPIRequest(TemplateAPIRequest request)
      throws IOException, APIException
   {
      final APIResponse response = new APIResponse(
         request(open(request), auth(request)),
         request.getRequestXML());
      if (response.grabNode("//Failure") != null)
      {
         m_errorHandler.handleError(
            request,
            response,
            this,
            "The request failed.");
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
