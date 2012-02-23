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
 * Generates SiloAccess tags inside of the MultiTEnantUserConfig
 *
 * @author Leonardo Varela
 */
public class MultiTenantUserConfigSiloAccessGenerator implements IContentGenerator
{

   /**
    * Represents a SiloAccess associated with the Multi Tenant User Config.
    *
    * @author Leonardo Varela
    */
   public static class SiloAccess
   {

      /////////////////////////////////////////////////////////////////////////
      // Public methods
      /////////////////////////////////////////////////////////////////////////

      /**
       * Retrieves the flag whether the allgroups flags is turned on or not.
       *
       * @return The m_allGroups the all groups flag.
       */
      public String getAllGroups()
      {
         return m_allGroups;
      }

      /**
       * Sets the allgroups flag for the silo.
       *
       * @param groups The m_allGroups flag to set
       */
      public void setAllGroups(String groups)
      {
         m_allGroups = groups;
      }

      /**
       * Retrieves the all sites flag for the silo.
       *
       * @return The m_allSites flag.
       */
      public String getAllSites()
      {
         return m_allSites;
      }

      /**
       * Sets the allsites flag of the silo
       *
       * @param sites The m_allSites flag to set
       */
      public void setAllSites(String sites)
      {
         m_allSites = sites;
      }

      /**
       * Retrieves the flag default silo.
       *
       * @return The m_defaultSilo flag
       */
      public String getDefaultSilo()
      {
         return m_defaultSilo;
      }

      /**
       * Sets the defaultsilo flag.
       *
       * @param silo The m_defaultSilo to set
       */
      public void setDefaultSilo(String silo)
      {
         m_defaultSilo = silo;
      }

      /**
       * Retrieves the role name for this silo.
       *
       * @return The m_roleName for this silo.
       */
      public String getRoleName()
      {
         return m_roleName;
      }

      /**
       * Sets the role name for the multitenant user on this silo.
       *
       * @param name The m_roleName to set.
       */
      public void setRoleName(String name)
      {
         m_roleName = name;
      }

      /**
       * Retrieves the id of the silo id.
       *
       * @return The m_siloId
       */
      public String getSiloId()
      {
         return m_siloId;
      }

      /**
       * Sets the id of the silo.
       *
       * @param id The m_siloId to set
       */
      public void setSiloId(String id)
      {
         m_siloId = id;
      }

      /**
       * Retrieves the group content generator.
       *
       * @return The m_groupContentGenerator
       */
      public IContentGenerator getGroupContentGenerator()
      {
         return m_groupContentGenerator;
      }

      /**
       * Sets the group content generator.
       *
       * @param contentGenerator The m_groupContentGenerator to set
       */
      public void setGroupContentGenerator(IContentGenerator contentGenerator)
      {
         m_groupContentGenerator = contentGenerator;
      }

      /**
       * Retrieves the sites content generator.
       *
       * @return The m_siteContentGenerator
       */
      public IContentGenerator getSiteContentGenerator()
      {
         return m_siteContentGenerator;
      }

      /**
       * Sets the sites of the content generator.
       *
       * @param contentGenerator The m_siteContentGenerator to set
       */
      public void setSiteContentGenerator(IContentGenerator contentGenerator)
      {
         m_siteContentGenerator = contentGenerator;
      }

      /**
       * Creates a new Silo Access.
       *
       * @param allGroups whether the Multi Tenant User has access to all groups on the silo or not.
       * @param allSites whether the Multi Tenant User has access to all sites on the silo or not.
       * @param defaultSilo whether this silo is the default one for the Multi Tenant User.
       * @param roleName the role of the user on the silo.
       * @param siloId the id of the silo.
       * @param groupContentGenerator the generator of groups for the silo.
       * @param siteContentGenerator the generator of sites for the silo.
       */
      public SiloAccess(String allGroups,
         String allSites,
         String defaultSilo,
         String roleName,
         String siloId)
      {
         m_allGroups = allGroups;
         m_allSites = allSites;
         m_defaultSilo = defaultSilo;
         m_roleName = roleName;
         m_siloId = siloId;
      }

      /////////////////////////////////////////////////////////////////////////
      // Non-Public fields
      /////////////////////////////////////////////////////////////////////////

      /** All groups within a silo flag */
      private String m_allGroups;
      /** All sites within a silo flag */
      private String m_allSites;
      /** The default silo of the multi tenant user.*/
      private String m_defaultSilo;
      /** The role name on the silo.*/
      private String m_roleName;
      /**The id of the silo*/
      private String m_siloId;
      /** the content Generator for the Group IDs*/
      private IContentGenerator m_groupContentGenerator;
      /** the content Generator for the Site IDs*/
      private IContentGenerator m_siteContentGenerator;
   }

   /////////////////////////////////////////////////////////////////////////
   // Public methods
   /////////////////////////////////////////////////////////////////////////

   /**
    * Creates a new Storage Properties generator for the silo config.
    */
   public MultiTenantUserConfigSiloAccessGenerator()
   {
      m_silos = new ArrayList<SiloAccess>();
   }

   /**
    * Knows how to print the xml output for properties elements inside of a <SiloConfig> element on the silo Config.
    *
    * @see java.lang.Object#toString()
    */
   public String toString()
   {
      StringBuilder sb = new StringBuilder();
      Iterator<SiloAccess> itSiloAccesses = m_silos.iterator();
      sb.append("<SiloAccesses>");
      while(itSiloAccesses.hasNext())
      {
         SiloAccess siloAccess = itSiloAccesses.next();
         IContentGenerator groupGenerator = siloAccess.getGroupContentGenerator();
         IContentGenerator siteGenerator = siloAccess.getSiteContentGenerator();
         sb.append("<SiloAccess all-groups=\"");
         sb.append(StringUtils.xmlEscape(siloAccess.getAllGroups()));
         sb.append("\" all-sites=\"");
         sb.append(StringUtils.xmlEscape(siloAccess.getAllSites()));
         sb.append("\" default-silo=\"");
         sb.append(StringUtils.xmlEscape(siloAccess.getDefaultSilo()));
         sb.append("\" role-name=\"");
         sb.append(StringUtils.xmlEscape(siloAccess.getRoleName()));
         sb.append("\" silo-id=\"");
         sb.append(StringUtils.xmlEscape(siloAccess.getSiloId()));
         sb.append("\">");
         if (groupGenerator != null)
         {
            sb.append("<AllowedGroups>");
            sb.append(groupGenerator.toString());
            sb.append("</AllowedGroups>");
         }
         if (siteGenerator != null)
         {
            sb.append("<AllowedSites>");
            sb.append(siteGenerator.toString());
            sb.append("</AllowedSites>");
         }
         sb.append("</SiloAccess>");
      }
      sb.append("</SiloAccesses>");
      return sb.toString();
   }

   /* (non-Javadoc)
    * @see org.rapid7.nexpose.api.IContentGenerator#setContents(org.w3c.dom.Element)
    */
   @SuppressWarnings("unchecked")
   @Override
   public void setContents(Element contents)
   {
      try
      {
         final NodeList silos = (NodeList) XPathFactory.newInstance().newXPath().evaluate("SiloAccess", contents, XPathConstants.NODESET);
         for (int i = 0; i < silos.getLength(); i++)
         {
            Element elementSilo = (Element) silos.item(i);
            String allGroups = elementSilo.getAttribute("all-groups");
            String allSites = elementSilo.getAttribute("all-sites");
            String defaultSilo = elementSilo.getAttribute("default-silo");
            String roleName = elementSilo.getAttribute("role-name");
            String siloId = elementSilo.getAttribute("silo-id");
            SiloAccess silo = new SiloAccess(allGroups, allSites, defaultSilo, roleName, siloId);
            final NodeList groupIds = (NodeList) XPathFactory.newInstance().newXPath().evaluate("AllowedGroups", elementSilo, XPathConstants.NODESET);
            if (groupIds != null)
            {
	            for (int j = 0; j < groupIds.getLength(); j++)
	            {
	               Element group = (Element) groupIds.item(j);
	               if (group.hasAttribute("generator"))
	               {
	                  Class<IContentGenerator> generator = (Class<IContentGenerator>) Class.forName("org.rapid7.nexpose.api.generators." + group.getAttribute("generator"));
	                  MultiTenantUserConfigGroupGenerator groupGenerator = (MultiTenantUserConfigGroupGenerator) generator.newInstance(); 
	                  groupGenerator.setContents(group);
	                  silo.setGroupContentGenerator(groupGenerator);
	               }
	            }
            }
            final NodeList siteIDs = (NodeList) XPathFactory.newInstance().newXPath().evaluate("AllowedSites", elementSilo, XPathConstants.NODESET);
            if (siteIDs != null)
            {
	            for (int j = 0; j < siteIDs.getLength(); j++)
	            {
	               Element site = (Element) siteIDs.item(j);
	               if (site.hasAttribute("generator"))
	               {
	                  Class<IContentGenerator> generator = (Class<IContentGenerator>) Class.forName("org.rapid7.nexpose.api.generators." + site.getAttribute("generator"));
	                  MultiTenantUserConfigSiteGenerator siteGenerator = (MultiTenantUserConfigSiteGenerator) generator.newInstance(); 
	                  siteGenerator.setContents(site);
	                  silo.setSiteContentGenerator(siteGenerator);
	               }
	            }
            }
            m_silos.add(silo);
         }
      }
      catch (XPathExpressionException e)
      {
         System.out.println("Could not parse the generator for SiloConfigStoragePropertiesGenerator: " + e.toString());
         throw new RuntimeException("The DBPRoperty could not be generated: " + e.toString());
      }
      catch (ClassNotFoundException e)
      {
         System.out.println("Could not parse the Contents Generator: " + e.toString());
         System.exit(1);      
      }
      catch (InstantiationException e)
      {
         System.out.println("Could not Instantiate the Contents Generator: " + e.toString());
         System.exit(1);
      }
      catch (IllegalAccessException e)
      {
         System.out.println("Could not access the Contents Generator: " + e.toString());
         System.exit(1);
      }

   }

   /**
    * Retrieves the list of SiloAccess elements associated with the Multi Tenant User Silo Config.
    *
    * @return the list of SiloAccess associated with the Multi Tenant User Silo Config.
    */
   public List<SiloAccess> getSilos()
   {
      return m_silos;
   }

   /**
    * Sets the list of silos associated with the Multi Tenant User Silo Config.
    *
    * @param silos The other-types to be set.
    */
   public void setSilos(List<SiloAccess> silos)
   {
      this.m_silos = silos;
   }

   /////////////////////////////////////////////////////////////////////////
   // non-Public fields
   /////////////////////////////////////////////////////////////////////////

   /**The Silos associated with the multi tenant user silo configuration.*/
   private List<SiloAccess> m_silos;
}
