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
import org.rapid7.nexpose.api.generators.SiteSaveRequestAlertsGenerator;
import org.rapid7.nexpose.api.generators.SiteSaveRequestCredentialsGenerator;
import org.rapid7.nexpose.api.generators.SiteSaveRequestHostsGenerator;
import org.rapid7.nexpose.api.generators.SiteSaveRequestRangesGenerator;

/**
 * Represents the SiteSaveRequest NeXpose API request.
 *
 * @author Leonardo Varela
 */
public class SiteSaveRequest extends TemplateAPIRequest
{
   /////////////////////////////////////////////////////////////////////////
   // Public methods
   /////////////////////////////////////////////////////////////////////////

   /**
    * Creates a new SiteSaveRequest NeXpose API request. Sets the first API
    * supported version to 1.0 and the last supported version to 1.1.
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
    */
   public SiteSaveRequest(
      String sessionId,
      String syncId,
      String siteId,
      String siteName,
      String siteDescription,
      String siteRiskFactor,
      IContentGenerator hostsGenerator,
      IContentGenerator rangesGenerator,
      IContentGenerator credentialsGenerator,
      IContentGenerator alertsGenerator,
      String configName, 
      String configVersion, 
      String configId, 
      String configTemplateId,
      String configEngineId,
      String scheduleEnabled,
      String scheduleIncremental,
      String scheduleType,
      String scheduleInterval,
      String scheduleStart,
      String scheduleMaxDuration,
      String scheduleNotValidAfter
      )
   {
      super(sessionId, syncId);
            set("siteId", siteId);
      set("siteName", siteName);
      set("siteDescription", siteDescription);
      set("siteRiskFactor", siteRiskFactor);
      set("siteHostsHostGenerator", hostsGenerator);
      set("siteHostsRangeGenerator", rangesGenerator);
      set("credentialsGenerator", credentialsGenerator);
      set("alertsGenerator", alertsGenerator);
      set("siteScanConfigName", configName);
      set("siteScanConfigConfigVersion", configVersion);
      set("siteScanConfigConfigId", configId);
      set("siteScanConfigTemplateId", configTemplateId);
      set("siteScanConfigEngineID", configEngineId);
      set("scheduleEnabled", scheduleEnabled);
      set("scheduleIncremental", scheduleIncremental);
      set("scheduleType", scheduleType);
      set("scheduleInterval", scheduleInterval);
      set("scheduleStart", scheduleStart);
      set("scheduleMaxDuration", scheduleMaxDuration);
      set("scheduleNotValidAfter", scheduleNotValidAfter);
      m_firstSupportedVersion = APISupportedVersion.V1_0;
      m_lastSupportedVersion = APISupportedVersion.V1_1;
   }
}
