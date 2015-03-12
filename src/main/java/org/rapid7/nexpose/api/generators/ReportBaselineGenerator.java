/**
 * Copyright (C) 2012, Rapid7 LLC, Boston, MA, USA.
 * All rights reserved.
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * * Neither the name of the <organization> nor the
 * names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
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

import org.w3c.dom.Element;
import org.rapid7.nexpose.utils.StringUtils;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

/**
 * Represents ReportBaselineGenerator associated with the Report save.
 *
 * @author Murali Rongali
 */
public class ReportBaselineGenerator  implements IContentGenerator
{

   // ///////////////////////////////////////////////////////////////////////
   // Public methods
   // ///////////////////////////////////////////////////////////////////////
   /**
    * Constructs a new ReportBaselineGenerator object.
    * 
    * @param compareTo a baseline string that needs to compare to.
    */
   public ReportBaselineGenerator(String compareTo)
   {
      m_compareTo = compareTo;
   }

   /**
    * Constructs a new ReportBaselineGenerator object.
    */
   public ReportBaselineGenerator()
   {
      m_compareTo = null;
   }

   @Override
   public String toString()
   {
      StringBuilder sb = new StringBuilder();
      if(m_compareTo != null) {
         sb.append("<Baseline compareTo=\"");
         sb.append(StringUtils.xmlEscape(m_compareTo));
         sb.append("\"/>");
      }
      return sb.toString();
   }

   @Override
   public void setContents(Element contents)
   {
      try
      {
         final Element elementOrganization = (Element) XPathFactory.newInstance().newXPath().evaluate("Baseline", contents,
            XPathConstants.NODE);
         String m_compareTo = elementOrganization.getAttribute("compareTo");
      }
      catch (XPathExpressionException e)
      {
         System.out.println("Could not parse the generator for ReportBaselineGenerator: " + e.toString());
         throw new RuntimeException("The Generate could not be generated: " + e.toString());
      }
   }

   /**
    * Gets the compareTo value for Baseline element in ReportSave.
    */
   public String getCompareTo()
   {
      return m_compareTo;
   }

   /**
    * Sets the compareTo value.
    *
    * @param compareTo the compareTo value for Baseline element in ReportSave.
    */
   public void setCompareTo(String compareTo)
   {
      m_compareTo = compareTo;
   }

   /////////////////////////////////////////////////////////////////////////
   // non-Public fields
   /////////////////////////////////////////////////////////////////////////

   // The string value for compareTo.
   private String m_compareTo;
}
