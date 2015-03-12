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
package org.rapid7.nexpose.api.domain;

import org.rapid7.nexpose.api.APIException;
import org.rapid7.nexpose.api.BaseElement;
import org.w3c.dom.Element;

/**
 * Represents a user retrieved by the user listing API request.
 *
 * @author Leonardo Varela
 */
public class UserSummary extends BaseElement
{
   /////////////////////////////////////////////////////////////////////////
   // Public methods
   /////////////////////////////////////////////////////////////////////////

   /**
    * Retrieves the id of the user
    * @return the m_id attribute which represents the id of the user.
    */
   public int getId()
   {
      return m_id;
   }

   /**
    * Retrieves the name of the user
    * @return the m_name attribute which represents the name of the user.
    */
   public String getUsername()
   {
      return m_username;
   }

   /**
    * Retrieves the authentication source for the user.
    * @return the m_authSource attribute which represents the authentication source associated with the user.
    */
   public String getAuthSource()
   {
      return m_authSource;
   }

   /**
    * Retrieves the authentication module for the user.
    * @return the m_authModule attribute which represents the authentication module associated with the user.
    */
   public String getAuthModule()
   {
      return m_authModule;
   }

   /**
    * Retrieves the full name of the user.
    * @return the m_fullname attribute which represents the full name of the user.
    */
   public String getFullname()
   {
      return m_fullname;
   }

   /**
    * Retrieves the email of the user
    * @return the m_email attribute which represents the email of the user.
    */
   public String getEmail()
   {
      return m_email;
   }

   /**
    * Retrieves whether the user is an administrator(true) or not(false).
    * @return the m_administrator attribute which represents the administrative nature of the user.
    */
   public boolean isAdministrator()
   {
      return m_administrator;
   }

   /**
    * Retrieves whether the user is disabled(true) or not(false)
    * @return the m_disabled attribute which represents the fact that the user is disabled or not.
    */
   public boolean isDisabled()
   {
      return m_disabled;
   }

   /**
    * Retrieves whether the user is locked(true) or not(false)
    * @return the m_locked attribute which represents the fact that the user is locked or not.
    */
   public boolean isLocked()
   {
      return m_locked;
   }

   /**
    * Retrieves the site count of the user.
    * @return the m_siteCount attribute which represents the count of sites associated with the user.
    */
   public int getSiteCount()
   {
      return m_siteCount;
   }

   /**
    * Retrieves the group count of the user.
    * @return the m_groupCount attribute which represents the count of groups associated with the user.
    */
   public int getGroupCount()
   {
      return m_groupCount;
   }

   /**
    * Creates a summary out of an element SiteSummary
    *
    * @throws APIException When there is a problem parsing the element's attributes.
    */
   public UserSummary(Element siteSummaryElement) throws APIException
   {
      setResponseElement(siteSummaryElement);
      m_id = getInt("id");
      m_username = getString("username");
      m_authSource = getString("authSource");
      m_authModule = getString("authModule");
      m_username = getString("userName");
      m_fullname = getString("fullName");
      m_email = getString("email");
      m_administrator = getBoolean("administrator");
      m_disabled = getBoolean("disabled");
      //This attribute exists in the schema but is not currently being sent from the API.
      //m_locked = getBoolean("locked");
      m_siteCount = getInt("siteCount");
      m_groupCount = getInt("groupCount");
   }

   /////////////////////////////////////////////////////////////////////////
   // non-Public fields
   /////////////////////////////////////////////////////////////////////////

   /**Represents the id of the user.*/
   private int m_id;
   /**Represents the username of the user.*/
   private String m_username;
   /**Represents the authentication source associated with the user.*/
   private String m_authSource;
   /**Represents the authentication module associated with the user*/
   private String m_authModule;
   /**Represents the fullName of the user*/
   private String m_fullname;
   /**Represents the email of the user*/
   private String m_email;
   /**Represents the administrative nature of the user: true for admin, false otherwise*/
   private boolean m_administrator;
   /**Represents the fact that the user is disabled or not*/
   private boolean m_disabled;
   /**Represents the fact that the user is locked or not*/
   private boolean m_locked;
   /**Represents the count of sites associated with the user*/
   private int m_siteCount;
   /**Represents the count of groups associated with the user*/
   private int m_groupCount;
}
