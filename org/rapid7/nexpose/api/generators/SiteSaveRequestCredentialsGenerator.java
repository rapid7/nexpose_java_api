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
package org.rapid7.nexpose.api.generators;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.rapid7.nexpose.api.IContentGenerator;
import org.rapid7.nexpose.api.StringUtils;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Generates credentials content to add to a Site Save Request.
 *
 * @author Leonardo Varela
 */
public class SiteSaveRequestCredentialsGenerator implements IContentGenerator
{
   /**
    * Represents a credential contained in a Site Save Request
    *
    * @author Leonardo Varela
    */
   public static class SiteSaveRequestCredential
   {
      /////////////////////////////////////////////////////////////////////////
      // Public methods
      /////////////////////////////////////////////////////////////////////////

      /**
       * Creates a new save site request credential.
       *
       * @param service the service associated with the credential.
       * @param host the host associated with the credential.
       * @param port the port associated with the credential.
       * @param realm the realm associated with the credential.
       * @param userId the user id associated with the credential.
       * @param password the password associated with the credential.
       */
      public SiteSaveRequestCredential(String service, String host, String port, String realm, String userId, String password)
      {
         m_service = service;
         m_host = host;
         m_port = port;
         m_realm = realm;
         m_userId = userId;
         m_password = password;
      }

      /**
       * Retrieves the service associated with the credentials.
       *
       * @return the service associated with the credentials.
       */
      public String getService()
      {
         return m_service;
      }

      /**
       * Sets the service associated with the credentials.
       *
       * @param service the service to associate with the credentials
       */
      public void setService(String service)
      {
         this.m_service = service;
      }

      /**
       * Retrieves the host to apply the credentials to.
       *
       * @return the host that the credentials are valid for.
       */
      public String getHost()
      {
         return m_host;
      }

      /**
       * Sets the host that the credentials are valid for.
       *
       * @param host the host that the credentials are valid for
       */
      public void setHost(String host)
      {
         this.m_host = host;
      }

      /**
       * Retrieves the port in which to use the credentials.
       *
       * @return the port to use the credentials in.
       */
      public String getPort()
      {
         return m_port;
      }

      /**
       * Sets the port in which to use the credentials.
       *
       * @param port the port to use the credentials.
       */
      public void setPort(String port)
      {
         this.m_port = port;
      }

      /**
       * Retrieves the realm in which to use the credentials.
       *
       * @return the realm in which the credentials are used.
       */
      public String getRealm()
      {
         return m_realm;
      }

      /**
       * Sets the realm associated with the credentials
       *
       * @param realm the realm in which the credentials are used
       */
      public void setRealm(String realm)
      {
         this.m_realm = realm;
      }

      /**
       * Retrieves the id of the user in the credentials.
       *
       * @return the userId associated with the credentials.
       */
      public String getUserId()
      {
         return m_userId;
      }

      /**
       * Sets the user Id for the credentials.
       *
       * @param userId the user name to associated the credentials with
       */
      public void setUserId(String userId)
      {
         m_userId = userId;
      }

      /**
       * Retrieves the password associated with the credentials.
       *
       * @return the password associated with the credentials.
       */
      public String getPassword()
      {
         return m_password;
      }

      /**
       * Sets the password associated with the credentials.
       *
       * @param password the password associated with the credentials
       */
      public void setPassword(String password)
      {
         this.m_password = password;
      }

      /////////////////////////////////////////////////////////////////////////
      // non-Public fields
      /////////////////////////////////////////////////////////////////////////
   
      /**The service used for the credentials to work*/
      private String m_service;
      /**The host to apply the credentials against*/
      private String m_host;
      /**The port to use the credentials on*/
      private String m_port;
      /**The realm to use the credentials in*/
      private String m_realm;
      /**The user id associated with the credentials*/
      private String m_userId;
      /**The password associated with the credentials*/
      private String m_password;
   }
   /////////////////////////////////////////////////////////////////////////
   // Public methods
   /////////////////////////////////////////////////////////////////////////

   /**
    * Creates a new credentials generator for the site save request.
    */
   public SiteSaveRequestCredentialsGenerator()
   {
      m_credentials = new ArrayList<SiteSaveRequestCredential>();
   }

   /**
    * Knows how to create ranges inside of a site save request.
    *
    * @see java.lang.Object#toString()
    */
   public String toString()
   {
      StringBuilder sb = new StringBuilder();
      Iterator<SiteSaveRequestCredential> itRanges = m_credentials.iterator();
      while(itRanges.hasNext())
      {
         SiteSaveRequestCredential credential = itRanges.next();
         sb.append("<adminCredentials service=\"");
         sb.append(StringUtils.xmlEscape(credential.getService()));
         sb.append("\" host=\"");
         sb.append(StringUtils.xmlEscape(credential.getHost()));
         sb.append("\" port=\"");
         sb.append(StringUtils.xmlEscape(credential.getPort()));
         sb.append("\" userid=\"");
         sb.append(StringUtils.xmlEscape(credential.getUserId()));
         sb.append("\" password=\"");
         sb.append(StringUtils.xmlEscape(credential.getPassword()));
         sb.append("\" realm=\"");
         sb.append(StringUtils.xmlEscape(credential.getRealm()));
         sb.append("\"/>");
      }
      return sb.toString();
   }

   /* (non-Javadoc)
    * @see org.rapid7.nexpose.api.IContentGenerator#setContents(org.w3c.dom.Element)
    */
   @Override
   public void setContents(Element contents)
   {
      try
      {
         final NodeList ranges = (NodeList) XPathFactory.newInstance().newXPath().evaluate("adminCredentials", contents, XPathConstants.NODESET);
         for (int i = 0; i < ranges.getLength(); i++)
         {
            Element elementCredential = (Element) ranges.item(i);
            SiteSaveRequestCredential credential = 
               new SiteSaveRequestCredential(
                  elementCredential.getAttribute("service"),
                  elementCredential.getAttribute("host"),
                  elementCredential.getAttribute("port"),
                  elementCredential.getAttribute("realm"),
                  elementCredential.getAttribute("userId"),
                  elementCredential.getAttribute("password")
               );
            m_credentials.add(credential);
         }
      }
      catch (XPathExpressionException e)
      {
         System.out.println("Could not parse the Contents Generator for"
            + "SaveSiteRequestCredentialsGenerator: " + e.toString());
         throw new RuntimeException("The ranges could not be generated: "
            + e.toString());
      }
   }

   /**
    * Retrieves a list of credentials associated with the Site Save Request.
    *
    * @return the list of credentials associated with the Site Save Request.
    */
   public List<SiteSaveRequestCredential> getCredentials()
   {
      return m_credentials;
   }

   /**
    * Sets the list of credentials associated to the site save request.
    *
    * @param credentials the credentials to be set
    */
   public void setCredentials(List<SiteSaveRequestCredential> credentials)
   {
      this.m_credentials = credentials;
   }
   /////////////////////////////////////////////////////////////////////////
   // non-Public fields
   /////////////////////////////////////////////////////////////////////////

   /**The list or ranges associated with this generator.*/
   private List<SiteSaveRequestCredential> m_credentials;
}
