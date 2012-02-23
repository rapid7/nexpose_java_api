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
 * Generates DBA content to add to a SiloConfig.
 *
 * @author Leonardo Varela
 */
public class SiloConfigDBAGenerator implements IContentGenerator
{
   /////////////////////////////////////////////////////////////////////////
   // Public methods
   /////////////////////////////////////////////////////////////////////////

   /**
    * Creates a new DBA generator for the silo config.
    */
   public SiloConfigDBAGenerator()
   {
      m_dbas = new ArrayList<String>();
   }

   /**
    * Knows how to print the xml output for DBA elements inside of a <SiloConfig> element on the silo Config.
    *
    * @see java.lang.Object#toString()
    */
   public String toString()
   {
      StringBuilder sb = new StringBuilder();
      Iterator<String> itDBAs = m_dbas.iterator();
      sb.append("<DBAs>");
      while(itDBAs.hasNext())
      {
         String dba = itDBAs.next();
         sb.append("<DBA name=\"");
         sb.append(StringUtils.xmlEscape(dba));
         sb.append("\"/>");
      }
      sb.append("</DBAs>");
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
         final NodeList dbas = (NodeList) XPathFactory.newInstance().newXPath().evaluate("DBA", contents, XPathConstants.NODESET);
         for (int i = 0; i < dbas.getLength(); i++)
         {
            Element elementDBA = (Element) dbas.item(i);
            String dba = elementDBA.getTextContent();
            m_dbas.add(dba);
         }
      }
      catch (XPathExpressionException e)
      {
         System.out.println("Could not parse the generator for SiloConfigOtherTypesGenerator: " + e.toString());
         throw new RuntimeException("The DBA could not be generated: " + e.toString());
      }
   }

   /**
    * Retrieves the list of other-type elements associated with the Silo Config.
    *
    * @return the list of other-type properties associated with the Silo Config.
    */
   public List<String> getDBAs()
   {
      return m_dbas;
   }

   /**
    * Sets the list of other-type associated with the Silo Config.
    *
    * @param otherTypes The other-types to be set.
    */
   public void setDBAs(List<String> otherTypes)
   {
      this.m_dbas = otherTypes;
   }

   /////////////////////////////////////////////////////////////////////////
   // non-Public fields
   /////////////////////////////////////////////////////////////////////////

   /**The DBAs associated with the Silo Config*/
   private List<String> m_dbas;
}
