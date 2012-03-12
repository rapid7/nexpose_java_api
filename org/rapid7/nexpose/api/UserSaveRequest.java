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
 * Represents the UserSaveRequest NeXpose API request.
 *
 * @author Leonardo Varela
 */
public class UserSaveRequest extends TemplateAPIRequest
{
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
    */
   public UserSaveRequest(
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
   {
      super(sessionId, syncId);
      set("allGroups", allGroups);
      set("allSites", allSites);
      set("authSrcId", authSrcId);
      set("email", email);
      set("enabled", enabled);
      set("fullname", fullname);
      set("id", id);
      set("name", name);
      set("password", password);
      set("roleName", roleName);
      set("sitesGenerator", sitesGenerator);
      set("groupsGenerator", groupsGenerator);
      m_firstSupportedVersion = APISupportedVersion.V1_0;
      m_lastSupportedVersion = APISupportedVersion.V1_1;
   }
}
