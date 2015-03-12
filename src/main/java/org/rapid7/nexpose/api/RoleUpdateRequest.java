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

/**
 * Encapsulates the RoleUpdateRequest NeXpose API request.
 *
 * @author Meera Muthuswami
 */
public class RoleUpdateRequest extends TemplateAPIRequest
{
   /////////////////////////////////////////////////////////////////////////
   // Public methods
   /////////////////////////////////////////////////////////////////////////

   /**
    * Constructs a role update request with its associated API version information.
    *
    * @param sessionId The session to submit the request with. May not be {@code null} nor empty and must
    * be a 40 character hex {@link String}.
    * @param syncId The sync id to identify the response. May be {@code null}.
    * @param roleName The name of the role. May not be {@code null} nor empty and must be less than 65 character.
    * @param roleID the id of The role. May not be {@code null}.
    * @param roleFullName The name of the role. May not be {@code null} nor empty and must be less than 255 character.
    * @param roleDescription The description of the role. May be {@code null} empty.
    * @param roleEnabled  The flag which indicates if the role is enabled. May not be {@code null} nor empty.
    * @param createReportEnabled The flag which indicates if the createreport privilege is enabled. May not be {@code null} nor empty.
    * @param viewAssetDataEnabled The flag which indicates if the viewassetsata privilege is enabled. May not be {@code null} nor empty.
    * @param configureSiteSettingsEnabled The flag which indicates if the configuresitesettings privilege is enabled. May not be {@code null} nor empty.
    * @param configureTargetsEnabled The flag which indicates if the configuretargets privilege is enabled. May not be {@code null} nor empty.
    * @param configureEnginesEnabled The flag which indicates if the configureengines privilege is enabled. May not be {@code null} nor empty.
    * @param configureScanTemplatesEnabled The flag which indicates if the configurescantemplates privilege is enabled. May not be {@code null} nor empty.
    * @param configureAlertsEnabled The flag which indicates if the configurealerts privilege is enabled. May not be {@code null} nor empty.
    * @param configureCredentialsEnabled The flag which indicates if the configurecredentials privilege is enabled. May not be {@code null} nor empty.
    * @param configureScheduleScansEnabled The flag which indicates if the configureschedulescans privilege is enabled. May not be {@code null} nor empty.
    * @param manualScansEnabled The flag which indicates if the createReport privilege is enabled. May not be {@code null} nor empty.
    * @param purgeDataEnabled The flag which indicates if the purgedata privilege is enabled. May not be {@code null} nor empty.
    * @param viewGroupAssetDataEnabled The flag which indicates if the viewgroupassetdata privilege is enabled. May not be {@code null} nor empty.
    * @param configureAssetsEnabled The flag which indicates if the configureassets privilege is enabled. May not be {@code null} nor empty.
    */
   public RoleUpdateRequest(String sessionId, String syncId, String roleName, String roleID, String roleFullName, String roleDescription,
      String roleEnabled,String scope, String createReportEnabled, String configureGlobalSettingsEnabled, String manageSitesEnabled,
      String manageAssetGroupsEnabled,String manageScanTemplatesEnabled,String manageReportTemplatesEnabled,String manageScanEnginesEnabled,
      String submitVulnExceptionsEnabled,String approveVulnExceptionsEnabled,String deleteVulnExceptionsEnabled,String addUsersToSiteEnabled,
      String addUsersToGroupEnabled, String createTicketEnabled, String closeTicketEnabled, String ticketAssigneeEnabled, String viewAssetDataEnabled,
      String configureSiteSettingsEnabled, String  configureTargetsEnabled,String configureEnginesEnabled, String configureScanTemplatesEnabled,
      String configureAlertsEnabled, String configureScheduleScansEnabled, String configureCredentialsEnabled,
      String manualScansEnabled, String purgeDataEnabled, String viewGroupAssetDataEnabled,
      String configureAssetsEnabled)
   {
      super(sessionId, syncId);
      set("roleName", roleName);
      set("roleFullName", roleFullName);
      set("roleID", roleID);
      set("roleEnabled", roleEnabled);
      if (scope == null)
    	  scope="silo";
      set("scope", scope);
      set("roleDescription", roleDescription);
      set("createReportEnabled", createReportEnabled);
      set("configureGlobalSettingsEnabled", configureGlobalSettingsEnabled);
      set("manageSitesEnabled", manageSitesEnabled);
      set("manageAssetGroupsEnabled", manageAssetGroupsEnabled);
      set("manageScanTemplatesEnabled", manageScanTemplatesEnabled);
      set("manageReportTemplatesEnabled", manageReportTemplatesEnabled);
      set("manageScanEnginesEnabled", manageScanEnginesEnabled);
      set("submitVulnExceptionsEnabled", submitVulnExceptionsEnabled);
      set("approveVulnExceptionsEnabled", approveVulnExceptionsEnabled);
      set("deleteVulnExceptionsEnabled", deleteVulnExceptionsEnabled);
      set("addUsersToSiteEnabled", addUsersToSiteEnabled);
      set("addUsersToGroupEnabled", addUsersToGroupEnabled);
      set("createTicketEnabled", createTicketEnabled);
      set("closeTicketEnabled", closeTicketEnabled);
      set("ticketAssigneeEnabled", ticketAssigneeEnabled);
      set("viewAssetDataEnabled", viewAssetDataEnabled);
      set("configureSiteSettingsEnabled", configureSiteSettingsEnabled);
      set("configureTargetsEnabled", configureTargetsEnabled);
      set("configureEnginesEnabled", configureEnginesEnabled);
      set("configureScanTemplatesEnabled", configureScanTemplatesEnabled);
      set("configureAlertsEnabled", configureAlertsEnabled);
      set("configureCredentialsEnabled", configureCredentialsEnabled);
      set("configureScheduleScansEnabled", configureScheduleScansEnabled);
      set("manualScansEnabled", manualScansEnabled);
      set("purgeDataEnabled", purgeDataEnabled);
      set("viewGroupAssetDataEnabled", viewGroupAssetDataEnabled);
      set("configureAssetsEnabled", configureAssetsEnabled);
      m_firstSupportedVersion = APISupportedVersion.V1_2;
      m_lastSupportedVersion = APISupportedVersion.V1_2;
   }
}
