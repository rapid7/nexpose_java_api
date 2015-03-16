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

import org.rapid7.nexpose.api.generators.IContentGenerator;

/**
 * Represents a Storage associated with a Silo Configuration.
 *
 * @author Leonardo Varela
 */
public class SiloConfigStorage
{

   /////////////////////////////////////////////////////////////////////////
   // Public methods
   /////////////////////////////////////////////////////////////////////////

   /**
    * Retrieves the storage's user id.
    *
    * @return The the storage's host value.
    */
   public String getUserID()
   {
      return m_userID;
   }

   /**
    * Sets the storage's user id.
    *
    * @param userID The new value of the storage's user id to set.
    */
   public void setUserID(String userID)
   {
      m_userID = userID;
   }

   /**
    * Retrieves the storage's DBMS value.
    *
    * @return The value of the storage's DBMS.
    */
   public String getDBMS()
   {
      return m_DBMS;
   }

   /**
    * Sets the storage's DBMS value.
    *
    * @param dbms The value of the storage's DBMS to set.
    */
   public void setDBMS(String dbms)
   {
      m_DBMS = dbms;
   }

   /**
    * Retrieves the storage's realm.
    *
    * @return The value of the storage's realm.
    */
   public String getUserRealm()
   {
      return m_userRealm;
   }

   /**
    * Sets the storage's realm.
    *
    * @param username The value of the storage's realm to set
    */
   public void setUserRealm(String realm)
   {
      m_userRealm = realm;
   }

   /**
    * Retrieves the storage's password.
    *
    * @return The value of the storage's passsword.
    */
   public String getPassword()
   {
      return m_password;
   }

   /**
    * Sets the storage's password.
    *
    * @param port The value of the storage's password to set.
    */
   public void setPassword(String password)
   {
      m_password = password;
   }

   /**
    * Retrieves the storage's URL.
    *
    * @return The value of the storage's URL.
    */
   public String getURL()
   {
      return m_url;
   }

   /**
    * Sets the storage's URL.
    *
    * @param url The value of the storage's URL to set.
    */
   public void setURL(String url)
   {
      m_url = url;
   }

   /**
    * Retrieves the storage's  properties generator.
    *
    * @return The storage's properties Generator.
    */
   public IContentGenerator getPropertiesGenerator()
   {
      return m_propertiesGenerator;
   }

   /**
    * Sets the storage's properties content generator.
    *
    * @param generator The storage's properties content generator to set.
    */
   public void setPropertiesGenerator(IContentGenerator generator)
   {
      m_propertiesGenerator = generator;
   }

   /**
    * Tells whether all the attributes are null or not.
    */
   public boolean areAllNull()
   {
      return (m_userID== null &&
              m_DBMS== null &&
              m_password == null &&
              m_url == null &&
              m_userRealm== null &&
              m_propertiesGenerator == null);
   }
   /////////////////////////////////////////////////////////////////////////
   // non-Public fields
   /////////////////////////////////////////////////////////////////////////

   /** Represents the host of the storage. */
   private String m_userID;
   /** Represents the dbms of the storage. */
   private String m_DBMS;
   /** Represents the name of the storage. */
   private String m_userRealm;
   /** Represents the port of the storage. */
   private String m_password;
   /**Represents the url of the storage. */
   private String m_url;
   /** Represents the content generator for the properties of the storage. */
   private IContentGenerator m_propertiesGenerator;
}
