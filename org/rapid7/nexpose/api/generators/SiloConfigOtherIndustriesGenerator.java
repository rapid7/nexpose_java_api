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
 * Generates other-types content to add to a SiloConfig.
 *
 * @author Leonardo Varela
 */
public class SiloConfigOtherIndustriesGenerator implements IContentGenerator
{
   /////////////////////////////////////////////////////////////////////////
   // Public methods
   /////////////////////////////////////////////////////////////////////////

   /**
    * Creates a new other-type generator for the silo config.
    */
   public SiloConfigOtherIndustriesGenerator()
   {
      m_otherIndustries = new ArrayList<String>();
   }

   /**
    * Knows how to print the xml output for other-types inside of a <SiloConfig> element on the silo Config.
    *
    * @see java.lang.Object#toString()
    */
   public String toString()
   {
      StringBuilder sb = new StringBuilder();
      Iterator<String> itOtherIndustries = m_otherIndustries.iterator();
      sb.append("<OtherIndustries>");
      while(itOtherIndustries.hasNext())
      {
         String otherIndustry = itOtherIndustries.next();
         sb.append("<Industry name=\"");
         sb.append(StringUtils.xmlEscape(otherIndustry));
         sb.append("\"/>");
      }
      sb.append("</OtherIndustries>");
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
         final NodeList otherIndustries = (NodeList) XPathFactory.newInstance().newXPath().evaluate("Industry", contents, XPathConstants.NODESET);
         for (int i = 0; i < otherIndustries.getLength(); i++)
         {
            Element elementOtherIndustry = (Element) otherIndustries.item(i);
            String otherIndustry = elementOtherIndustry.getTextContent();
            m_otherIndustries.add(otherIndustry);
         }
      }
      catch (XPathExpressionException e)
      {
         System.out.println("Could not parse the generator for SiloConfigOtherIndustriesGenerator: " + e.toString());
         throw new RuntimeException("The Industry could not be generated: " + e.toString());
      }
   }

   /**
    * Retrieves the list of other-industry elements associated with the Silo Config.
    *
    * @return the list of other-industry properties associated with the Silo Config.
    */
   public List<String> getOtherIndustry()
   {
      return m_otherIndustries;
   }

   /**
    * Sets the list of other-type associated with the Silo Config.
    *
    * @param otherIndustries The other-types to be set.
    */
   public void setOtherIndustry(List<String> otherIndustries)
   {
      this.m_otherIndustries = otherIndustries;
   }

   /////////////////////////////////////////////////////////////////////////
   // non-Public fields
   /////////////////////////////////////////////////////////////////////////

   /**The other-industries associated with the Silo Config*/
   private List<String> m_otherIndustries;
}
