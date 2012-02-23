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
 * Generates Group content to add to a MultiTenantUserConfig.
 *
 * @author Leonardo Varela
 */
public class MultiTenantUserConfigGroupGenerator implements IContentGenerator
{
   /////////////////////////////////////////////////////////////////////////
   // Public methods
   /////////////////////////////////////////////////////////////////////////

   /**
    * Creates a new Group generator for the multi tenant silo config.
    */
   public MultiTenantUserConfigGroupGenerator()
   {
      m_groups = new ArrayList<String>();
   }

   /**
    * Knows how to print the xml output for Group elements inside of a <SiloAccess> element on the Multi Tenant User
    * Config.
    *
    * @see java.lang.Object#toString()
    */
   public String toString()
   {
      StringBuilder sb = new StringBuilder();
      Iterator<String> itGroups = m_groups.iterator();
      while(itGroups.hasNext())
      {
         String group = itGroups.next();
         sb.append("<AllowedGroup id=\"");
         sb.append(StringUtils.xmlEscape(group));
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
         final NodeList groups = (NodeList) XPathFactory.newInstance().newXPath().evaluate("AllowedGroup", contents, XPathConstants.NODESET);
         for (int i = 0; i < groups.getLength(); i++)
         {
            Element elementGroup = (Element) groups.item(i);
            String group = elementGroup.getAttribute("id");
            m_groups.add(group);
         }
      }
      catch (XPathExpressionException e)
      {
         System.out.println("Could not parse the generator for MultiTenantUserConfigGroupGenerator: " + e.toString());
         throw new RuntimeException("The AllowedGroup could not be generated: " + e.toString());
      }
   }

   /**
    * Retrieves the list of AllowedGroup elements associated with the Silo Config.
    *
    * @return the list of AllowedGroup properties associated with the Silo Config.
    */
   public List<String> getGroups()
   {
      return m_groups;
   }

   /**
    * Sets the list of AllowedGroup associated with the Multi Tenant User Config.
    *
    * @param groups The AllowedGroup to be set.
    */
   public void setGroups(List<String> groups)
   {
      this.m_groups = groups;
   }

   /////////////////////////////////////////////////////////////////////////
   // non-Public fields
   /////////////////////////////////////////////////////////////////////////

   /**The Groups associated with the Multi Tenant user Config*/
   private List<String> m_groups;
}
