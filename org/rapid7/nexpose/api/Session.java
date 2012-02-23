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

import org.rapid7.nexpose.api.domain.*;
import org.rapid7.nexpose.api.generators.*;

public interface Session
{
  void setErrorHandler(IAPIErrorHandler errorHandler);

  void clearErrorHandler();

  APIResponse getListingAPIResponse();

  APIResponse sendRawXMLRequest(
      String rawXML,
      APISession.APISupportedVersion version)
      throws IOException, APIException;

  public APIResponse sendRawXMLRequest(
     String rawXML,
     APISession.APISupportedVersion version,
     OutputStream outputStream)
     throws IOException, APIException;
  
  APIResponse login(String syncId) throws IOException, APIException;

  APIResponse login(String syncId, String siloId) throws IOException, APIException;

  APIResponse logout(
      String sessionId,
      String syncId)
      throws IOException, APIException;

  Iterable<SiteSummary> listSites(
         String sessionId,
         String syncId)
         throws IOException, APIException;

  APIResponse siteConfigRequest(String sessionId, String syncId, String siteId)
    throws IOException, APIException;

  Iterable<AssetGroupSummary> listAssetGroups(
            String sessionId,
            String syncId)
            throws IOException, APIException;

  Iterable<UserSummary> listUsers(
               String sessionId,
               String syncId)
               throws IOException, APIException;

  Iterable<EngineSummary> listEngines(
                  String sessionId,
                  String syncId)
                  throws IOException, APIException;

  APIResponse engineSaveRequest(
                     String sessionId,
                     String syncId,
                     String engineConfigId,
                     String engineConfigName,
                     String engineConfigAddress,
                     String engineConfigPort,
                     String engineConfigPriority,
                     String engineConfigScope,
                     IContentGenerator sitesGenerator)
                     throws IOException, APIException;

  APIResponse assetGroupSaveRequest(
                        String sessionId,
                        String syncId,
                        String assetGroupId,
                        String assetGroupName,
                        String assetGroupDescription,
                        String assetGroupRiskScore,
                        IContentGenerator devicesGenerator)
                        throws IOException, APIException;

  APIResponse engineActivityRequest(
                           String sessionId,
                           String syncId,
                           String engineId)
                           throws IOException, APIException;

  APIResponse engineConfigRequest(
                              String sessionId,
                              String syncId,
                              String engineId)
                              throws IOException, APIException;

  APIResponse engineDeleteRequest(
                                 String sessionId,
                                 String syncId,
                                 String engineId)
                                 throws IOException, APIException;

  APIResponse ticketDeleteRequest(String sessionId, String syncId, IContentGenerator ticketsGenerator)
                                    throws IOException, APIException;

  APIResponse ticketDetailsRequest(String sessionId, String syncId, IContentGenerator ticketsGenerator)
                                       throws IOException, APIException;

  Iterable<TicketSummary> ticketListRequest(String sessionId,
                                          String syncId,
                                          IContentGenerator filtersGenerator)
                                          throws IOException, APIException;

  APIResponse ticketCreateRequest(
                                             String sessionId,
                                             String syncId,
                                             String ticketName,
                                             String ticketPriority,
                                             String ticketDeviceId,
                                             String ticketAssignedTo,
                                             IContentGenerator vulnerabilitiesGenerator,
                                             IContentGenerator commentsGenerator)
                                             throws IOException, APIException;

  APIResponse siteSaveRequest(
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
                                                String scheduleNotValidAfter) throws IOException, APIException;

  APIResponse siteSaveRequest(
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
                                                   String scheduleNotValidAfter) throws IOException, APIException;

  APIResponse siteScanRequest(
                                                      String sessionId,
                                                      String syncId,
                                                      String siteId)
                                                      throws IOException, APIException;

  APIResponse siteDeleteRequest(
                                                         String sessionId,
                                                         String syncId,
                                                         String siteId)
                                                         throws IOException, APIException;

  APIResponse assetGroupDeleteRequest(
                                                            String sessionId,
                                                            String syncId,
                                                            String assetGroupId)
                                                            throws IOException, APIException;

  APIResponse scanStopRequest(
                                                               String sessionId,
                                                               String syncId,
                                                               String scanId)
                                                               throws IOException, APIException;

  APIResponse userSaveRequest(
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
                                                                  throws IOException, APIException;

  APIResponse userSaveRequest(
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
                                                                     throws IOException, APIException;

  APIResponse userSaveRequest(
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
                                                                     throws IOException, APIException;

  APIResponse userDeleteRequest(
                                                                        String sessionId,
                                                                        String syncId,
                                                                        String userId)
                                                                        throws IOException, APIException;

  APIResponse listSilos(String sessionId, String syncId) throws IOException, APIException;

  APIResponse siloCreateRequest(
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
      throws IOException, APIException;

  APIResponse siloUpdateRequest(
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
         throws IOException, APIException;

  APIResponse siloDeleteRequest(String sessionId, String syncId, String siloId) throws IOException, APIException;

  APIResponse siloConfigRequest(String sessionId, String syncId, String siloId) throws IOException, APIException;

  APIResponse multiTenantUserCreateRequest(
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
      throws IOException, APIException;

  APIResponse multiTenantUserCreateRequest(
         String sessionId,
         String syncId,
         String authSrcId,
         String email,
         String enabled,
         String fullname,
         String username,
         String superuser,
         IContentGenerator multiTenantUserConfigSiloAccessGenerator)
         throws IOException, APIException;

  APIResponse multiTenantUserUpdateRequest(
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
         throws IOException, APIException;

  Iterable<MultiTenantUserSummary> multiTenantUserListingRequest(String sessionID, String syncID)
             throws IOException, APIException;

  APIResponse multiTenantUserConfigRequest(String sessionID, String syncID, String userID)
                throws IOException, APIException;

  APIResponse multiTenantUserDeleteRequest(String sessionID, String syncID, String userID)
                   throws IOException, APIException;

  APIResponse siloProfileConfigRequest(String sessionID, String syncID, String siloProfileID)
                      throws IOException, APIException;

  APIResponse siloProfileCreateRequest(
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
                            throws IOException, APIException;

  APIResponse siloProfileDeleteRequest(
                               String sessionID,
                               String syncID,
                               String siloProfileID)
                                  throws IOException, APIException;

  APIResponse siloProfileListingRequest(
                                     String sessionID,
                                     String syncID)
                                        throws IOException, APIException;

  APIResponse siloProfileUpdateRequest(
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
                                              throws IOException, APIException;

  APIResponse roleCreateRequest(String sessionId, String syncId, String roleName, String roleFullName, String roleDescription,
                                                 String roleEnabled,String scope, String createReportEnabled, String configureGlobalSettingsEnabled, String manageSitesEnabled,String manageAssetGroupsEnabled,String manageScanTemplatesEnabled,String manageReportTemplatesEnabled,String manageScanEnginesEnabled,String submitVulnExceptionsEnabled,String approveVulnExceptionsEnabled,String deleteVulnExceptionsEnabled,String addUsersToSiteEnabled, String addUsersToGroupEnabled, String createTicketEnabled, String closeTicketEnabled, String ticketAssigneeEnabled, String viewAssetDataEnabled, String configureSiteSettingsEnabled,
                                                 String configureTargetsEnabled, String configureEnginesEnabled, String configureScanTemplatesEnabled, String configureAlertsEnabled,
                                                 String configureScheduleScansEnabled, String configureCredentialsEnabled, String manualScansEnabled, String purgeDataEnabled,
                                                 String viewGroupAssetDataEnabled, String configureAssetsEnabled)
                                                 throws IOException, APIException;

  APIResponse roleUpdateRequest(String sessionId, String syncId, String roleName, String roleID, String roleFullName,
                                                    String roleDescription, String roleEnabled,String scope, String createReportEnabled, String configureGlobalSettingsEnabled, String manageSitesEnabled,
                                                    String manageAssetGroupsEnabled,String manageScanTemplatesEnabled,String manageReportTemplatesEnabled,String manageScanEnginesEnabled,
                                                    String submitVulnExceptionsEnabled,String approveVulnExceptionsEnabled,String deleteVulnExceptionsEnabled,String addUsersToSiteEnabled,
                                                    String addUsersToGroupEnabled, String createTicketEnabled, String closeTicketEnabled, String ticketAssigneeEnabled,
                                                    String viewAssetDataEnabled, String configureSiteSettingsEnabled,
                                                    String  configureTargetsEnabled,String configureEnginesEnabled, String configureScanTemplatesEnabled, String configureAlertsEnabled,
                                                    String configureScheduleScansEnabled, String configureCredentialsEnabled, String manualScansEnabled, String purgeDataEnabled,
                                                    String viewGroupAssetDataEnabled, String configureAssetsEnabled) throws IOException, APIException;

  APIResponse roleDetailsRequest(String sessionId, String syncId, IContentGenerator rolesGenerator)
                                                       throws IOException, APIException;

  APIResponse roleDeleteRequest(String sessionId, String syncId, String roleName,String scope)
                                                          throws IOException, APIException;

  Iterable<RoleSummary> roleListRequest(String sessionId, String syncId) throws IOException, APIException;

  APIResponse reportSaveRequest(String sessionId, String syncId,
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
                                  IContentGenerator dbExportGenerator) throws IOException, APIException;

  APIResponse reportGenerateRequest(String sessionId, String syncId, String reportId) throws IOException, APIException;

  APIResponse reportDeleteRequest(String sessionId, String syncId, String reportId, String reportCfgId) throws IOException, APIException;

  String getSessionID();
}
