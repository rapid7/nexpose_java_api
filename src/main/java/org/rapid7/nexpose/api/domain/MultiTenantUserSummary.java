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
package org.rapid7.nexpose.api.domain;

import org.rapid7.nexpose.api.APIException;
import org.rapid7.nexpose.api.BaseElement;
import org.w3c.dom.Element;

/**
 * Represents a MultiTenant retrieved by the MultiTenant listing API request.
 *
 * @author Chetan Ramaiah
 */
public class MultiTenantUserSummary extends BaseElement
{

   /////////////////////////////////////////////////////////////////////////
   // Public methods
   /////////////////////////////////////////////////////////////////////////

   /**
    * Retrieves the id of the multiTenant user.
    *
    * @return The id attribute of the multiTenant user.
    */
   public int getId()
   {
      return m_id;
   }

   /**
    * Retrieves the name of the multiTenant user.
    *
    * @return The name of the multiTenant user.
    */
   public String getName()
   {
      return m_name;
   }

   /**
    * Retrieves the silo count of the multiTenant user.
    *
    * @return The  silo count of the multiTenant user.
    */
   public int getSiloCount()
   {
      return m_siloCount;
   }

   /**
    * Retrieves the author source of the multiTenant user.
    *
    * @return The author source of the multiTenant user.
    */
   public String getAuthorSource()
   {
      return m_authSource;
   }

   /**
    * Retrieves the author module of the multiTenant user.
    *
    * @return The author module of the multiTenant user.
    */
   public String getAuthorModule()
   {
      return m_authModule;
   }

   /**
    * Retrieves the lock of the multitenant user.
    *
    * @return whether lock is applied or not.
    */
   public boolean getLocked()
   {
      return m_locked;
   }

   /**
    * Retrieves the superuser of the multiTenant user.
    *
    * @return The superuser of the multiTenant user.
    */
   public String getSuperuser()
   {
	   return m_superuser;
   }

   /**
    * Retrieves the full name of the multiTenant user.
    *
    * @return The full name of the multiTenant user.
    */
   public String getFullname()
   {
	   return m_fullname;
   }

   /**
    * Retrieves the enabled of the multiTenant user.
    *
    * @return whether enabled or not.
    */
   public boolean isEnabled()
   {
	   return m_enabled;
   }


   /**
    * Creates a summary out of an element MultiTenantUser Summary
    *
    * @throws APIException When there is a problem parsing the element's
    * attributes.
    */
   public MultiTenantUserSummary(Element multiTenantUserElement) throws APIException
   {
      setResponseElement(multiTenantUserElement);
      m_id = getInt("id");
      m_name = getString("user-name");
      m_authSource = getString("auth-source");
      m_authModule = getString("auth-module");
      m_locked = getBoolean("locked");
      m_siloCount = getInt("silo-count");
      m_fullname = getString("full-name");
      m_superuser = getString("superuser");
      m_enabled = getBoolean("enabled");
   }

   /////////////////////////////////////////////////////////////////////////
   // Non-public fields
   /////////////////////////////////////////////////////////////////////////

   /**Represents the id of the multiTenant user.*/
   private int m_id;

   /**Represents the name of the multiTenant user.*/
   private String m_name;

   /** The authentication source used to authenticate this user. */
   private String m_authSource;

   /** The authentication module used to authenticate this user. */
   private String m_authModule;

   /** Whether or not this user account is locked. */
   private boolean m_locked;

   /** The number of silos to which this user has access. */
   private int m_siloCount;

   /**Represents the superuser of the multiTenant user.*/
   private String m_superuser;

   /**Represents the name of the multiTenant user.*/
   private String m_fullname;

   /** Whether or not this user account is enabled. */
   private boolean m_enabled;
}
