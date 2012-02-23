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
 * Generates Sites content to add to a Save Engine Request.
 *
 * @author Leonardo Varela
 */
public class SaveEngineRequestSiteContentGenerator implements IContentGenerator
{
   /**
    * Represents a site contained in an Engine Save Request
    *
    * @author Leonardo Varela
    */
   public static class SaveEngineRequestSite
   {
      /////////////////////////////////////////////////////////////////////////
      // Public methods
      /////////////////////////////////////////////////////////////////////////

      /**
       * Creates a new site associated to the engine save request.
       *
       * @param id the id of the site.
       * @param name the name of the site.
       */
      public SaveEngineRequestSite(String id, String name)
      {
         m_siteId = id;
         m_siteName = name;
      }

      /**
       * @return the site's id
       */
      public String getSiteId()
      {
         return m_siteId;
      }

      /**
       * @param siteId the siteId to set
       */
      public void setSiteId(String siteId)
      {
         this.m_siteId = siteId;
      }

      /**
       * @return the siteName
       */
      public String getSiteName()
      {
         return m_siteName;
      }

      /**
       * @param siteName the siteName to set
       */
      public void setSiteName(String siteName)
      {
         this.m_siteName = siteName;
      }
      /////////////////////////////////////////////////////////////////////////
      // non-Public fields
      /////////////////////////////////////////////////////////////////////////

      /**The name of the site*/
      private String m_siteName;
      /**The id of the site*/
      private String m_siteId;
   }

   public SaveEngineRequestSiteContentGenerator()
   {
      m_sites = new ArrayList<SaveEngineRequestSite>();
   }

   /* (non-Javadoc)
    * @see java.lang.Object#toString()
    */
   public String toString()
   {
      StringBuilder sb = new StringBuilder();
      Iterator<SaveEngineRequestSite> itSites = m_sites.iterator();
      while(itSites.hasNext())
      {
         SaveEngineRequestSite site = itSites.next();
         sb.append("<Site id=\"");
         sb.append(StringUtils.xmlEscape(site.getSiteId()));
         sb.append("\" name=\"");
         sb.append(StringUtils.xmlEscape(site.getSiteName()));
         sb.append("\">");
         sb.append("</Site>");
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
         final NodeList sites = (NodeList) XPathFactory.newInstance().newXPath().evaluate("Site", contents, XPathConstants.NODESET);
         for (int i = 0; i < sites.getLength(); i++)
         {
            Element elementSite = (Element) sites.item(i);
            SaveEngineRequestSite site = new SaveEngineRequestSite(elementSite.getAttribute("id"),elementSite.getAttribute("name"));
            m_sites.add(site);
         }
      }
      catch (XPathExpressionException e)
      {
         System.out.println("Could not parse the Contents Generator for SaveEngineRequestSiteContentGenerator: " + e.toString());
         throw new RuntimeException("The sites could not be generated: " + e.toString());
      }
   }

   /**
    * Retrieves a list of the sites associated with the Save Engine Request.
    *
    * @return the sites associated with the Save Engine request
    */
   public List<SaveEngineRequestSite> getSites()
   {
      return m_sites;
   }

   /**
    * Sets the list of sites associated with the Save Engine Request.
    * @param sites the sites to be set
    */
   public void setSites(List<SaveEngineRequestSite> sites)
   {
      this.m_sites = sites;
   }
   /////////////////////////////////////////////////////////////////////////
   // non-Public fields
   /////////////////////////////////////////////////////////////////////////

   private List<SaveEngineRequestSite> m_sites;
   
}
