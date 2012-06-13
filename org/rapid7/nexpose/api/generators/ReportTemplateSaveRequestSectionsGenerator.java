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
package org.rapid7.nexpose.api.generators;

import org.rapid7.nexpose.utils.StringUtils;
import java.util.ArrayList;
import java.util.List;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Report template sections generator.
 *
 * @author Murali Rongali
 */
public class ReportTemplateSaveRequestSectionsGenerator implements IContentGenerator
{
   /////////////////////////////////////////////////////////////////////////
   // Public methods
   /////////////////////////////////////////////////////////////////////////

   /**
    * Constructs the ReportTemplateSaveRequestSections Generator.
    */
   public ReportTemplateSaveRequestSectionsGenerator()
   {
      m_sections = new ArrayList<String>();
   }

   @Override
   public String toString()
   {
      final StringBuilder sb = new StringBuilder("<ReportSections>");
      for(String section : m_sections)
      {
         sb.append("<ReportSection name=\"");
         sb.append(StringUtils.xmlEscape(section) + "\">");
         sb.append("</ReportSection>");
      }
      sb.append("</ReportSections>");
      return sb.toString();
   }

   @Override
   public void setContents(Element contents)
   {
      try
      {
         final NodeList sections =
            (NodeList) XPathFactory.newInstance().newXPath().evaluate("ReportSection", contents, XPathConstants.NODESET);
         final int count = sections.getLength();
         for (int i = 0; i < count; i++)
         {
            Element element = (Element)sections.item(i);
            String section = element.getTextContent();
            m_sections.add(section);
         }
      }
      catch (XPathExpressionException e)
      {
         System.out.println("Could not parse the generator for LicensedModuleGenerator: " + e.toString());
         throw new RuntimeException("The LicensedModule could not be generated: " + e.toString());
      }
   }

   /**
    * Retrieves the list of sections to be generated.
    *
    * @return A list of the sections to be generated.
    */
   public List<String> getSections()
   {
      return m_sections;
   }

   /**
    * Sets the list of sections to be generated.
    *
    * @param modules The sections to be generated.
    */
   public void setSections(List<String> sections)
   {
      m_sections = sections;
   }


   /////////////////////////////////////////////////////////////////////////
   // Non-Public fields
   /////////////////////////////////////////////////////////////////////////

   /**The sections associated with the ReportTemplateSave.*/
   private List<String> m_sections;
}
