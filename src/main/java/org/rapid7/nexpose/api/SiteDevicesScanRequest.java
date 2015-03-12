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
 * Represents the SiteDevicesScanRequest NeXpose API request.
 *
 * @author Murali Rognali.
 */
public class SiteDevicesScanRequest extends TemplateAPIRequest
{
   /////////////////////////////////////////////////////////////////////////
   // Public methods
   /////////////////////////////////////////////////////////////////////////

   /**
    * Creates a new SiteDevicesScanRequest NeXpose API request. Sets the first API
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
    * @param devicesGenerator a Generator that knows how to output
    *        devices for sitedevices scan which that are to be associated with the device scan
    */
   public SiteDevicesScanRequest(
      String sessionId,
      String syncId,
      String siteId,
      IContentGenerator hostsGenerator,
      IContentGenerator rangesGenerator,
      IContentGenerator devicesGenerator)
   {
      super(sessionId, syncId);
      set("siteId", siteId);
      set("siteHostsHostGenerator", hostsGenerator);
      set("siteHostsRangeGenerator", rangesGenerator);
      set("credentialsGenerator", devicesGenerator);
      m_firstSupportedVersion = APISupportedVersion.V1_0;
      m_lastSupportedVersion = APISupportedVersion.V1_1;
   }
}
