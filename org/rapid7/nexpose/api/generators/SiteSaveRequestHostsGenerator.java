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
 * Generates hosts content to add to a Site Save Request.
 *
 * @author Leonardo Varela
 */
public class SiteSaveRequestHostsGenerator implements IContentGenerator
{
   /////////////////////////////////////////////////////////////////////////
   // Public methods
   /////////////////////////////////////////////////////////////////////////

   /**
    * Creates a new host generator for the site save request.
    */
   public SiteSaveRequestHostsGenerator()
   {
      m_hosts = new ArrayList<String>();
   }

   /**
    * Knows how to print the xml output for hosts inside of a <Hosts> tag on the 
    * site save request.
    *
    * @see java.lang.Object#toString()
    */
   public String toString()
   {
      StringBuilder sb = new StringBuilder();
      Iterator<String> itHosts = m_hosts.iterator();
      while(itHosts.hasNext())
      {
         String host = itHosts.next();
         sb.append("<host>");
         sb.append(StringUtils.xmlEscape(host));
         sb.append("</host>");
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
         final NodeList hosts = (NodeList) XPathFactory.newInstance().newXPath().evaluate("host", contents, XPathConstants.NODESET);
         for (int i = 0; i < hosts.getLength(); i++)
         {
            Element elementHost = (Element) hosts.item(i);
            String host = elementHost.getTextContent();
            m_hosts.add(host);
         }
      }
      catch (XPathExpressionException e)
      {
         System.out.println("Could not parse the Contents Generator for"
            + " SaveSiteRequestHostsGenerator: " + e.toString());
         throw new RuntimeException("The hosts could not be generated: "
            + e.toString());
      }
   }

   /**
    * Retrieves the list of hosts associated with the Site Save Request
    *
    * @return the hosts associated with the Site Save Request
    */
   public List<String> getHosts()
   {
      return m_hosts;
   }

   /**
    * Sets the list of hosts associated with the Site Save Request.
    * @param hosts the hosts to be set
    */
   public void setHosts(List<String> hosts)
   {
      this.m_hosts = hosts;
   }
   /////////////////////////////////////////////////////////////////////////
   // non-Public fields
   /////////////////////////////////////////////////////////////////////////

   /**The hosts associated with the*/
   private List<String> m_hosts;
}
