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
import java.util.List;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.rapid7.nexpose.api.IContentGenerator;
import org.rapid7.nexpose.api.StringUtils;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Global scan template generator.
 * 
 * @author Christopher Lee.
 */
public class GlobalScanTemplateGenerator implements IContentGenerator
{
   /////////////////////////////////////////////////////////////////////////
   // Public methods
   /////////////////////////////////////////////////////////////////////////

   /**
    * Constructs the generator.
    */
   public GlobalScanTemplateGenerator()
   {
      m_templates = new ArrayList<String>();
   }

   @Override
   public String toString()
   {
      final StringBuilder sb = new StringBuilder("<GlobalScanTemplates>");
      for(String template : m_templates)
      {
         sb.append("<GlobalScanTemplate name=\"");
         sb.append(StringUtils.xmlEscape(template) + "\">");
         sb.append("</GlobalScanTemplate>");
      }
      sb.append("</GlobalScanTemplates>");
      return sb.toString();
   }

   @Override
   public void setContents(Element contents)
   {
      try
      {
         final NodeList templates = 
            (NodeList) XPathFactory.newInstance().newXPath().evaluate("GlobalScanTemplate", contents, XPathConstants.NODESET);
         final int count = templates.getLength();
         for (int i = 0; i < count; i++)
         {
            Element element = (Element)templates.item(i);
            String template = element.getTextContent();
            m_templates.add(template);
         }
      }
      catch (XPathExpressionException e)
      {
         System.out.println("Could not parse the generator for GlobalScanTemplateGenerator: " + e.toString());
         throw new RuntimeException("The GlobalScanTemplate could not be generated: " + e.toString());
      }
   }

   /**
    * Retreives the list of scan templates to be generated.
    *
    * @return A list of the templates associated with the generator.
    */
   public List<String> getTemplates()
   {
      return m_templates;
   }

   /**
    * Sets the list of scan templates to be generated.
    *
    * @param templates The templates to be generated.
    */
   public void setTemplates(List<String> templates)
   {
      m_templates = templates;
   }


   /////////////////////////////////////////////////////////////////////////
   // Non-Public fields
   /////////////////////////////////////////////////////////////////////////

   /**The templates associated with the Silo Config*/
   private List<String> m_templates;
}
