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

import org.rapid7.nexpose.api.domain.ReportGenerate;
import org.rapid7.nexpose.api.domain.ReportGenerateSchedule;
import org.rapid7.nexpose.utils.StringUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Represents generate element retrieved by the report save API request.
 *
 * @author Murali Rongali
 */
public class ReportGenerateGenerator implements IContentGenerator
{

   /////////////////////////////////////////////////////////////////////////
   // Public methods
   /////////////////////////////////////////////////////////////////////////

   /**
    * Creates a ReportGenerateGenerator for the report save.
    */
   public ReportGenerateGenerator() {}

   /**
    * Knows how to print the xml output for Generate element on the report save.
    *
    * @see java.lang.Object#toString()
    */
   @Override
   public String toString()
   {
      StringBuilder sb = new StringBuilder();
      sb.append("<Generate after-scan=\"");
         sb.append(StringUtils.xmlEscape(m_reportGenrate.getAfterScan()));
         sb.append("\" schedule=\"");
         sb.append(StringUtils.xmlEscape(m_reportGenrate.getSchedule()));
         ReportGenerateSchedule genSchedule = m_reportGenrate.getGenerateSchedule();
         if (genSchedule == null)
            sb.append("\"/>");
         else {
            sb.append("\"> <Schedule type=\"");
            sb.append(StringUtils.xmlEscape(genSchedule.getType()));
            sb.append("\" interval=\"");
            sb.append(StringUtils.xmlEscape(genSchedule.getInterval()));
            sb.append("\" start=\"");
            sb.append(StringUtils.xmlEscape(genSchedule.getStart()));
            if (genSchedule.getStart() != null && genSchedule.getStart().equalsIgnoreCase("")) {
               sb.append("\" notValidAfter=\"");
               sb.append(StringUtils.xmlEscape(genSchedule.getType()));
            }
            sb.append("\"/> </Generate>");
         }
      return sb.toString();
   }

   @Override
   public void setContents(Element contents)
   {
      try
      {
         final Element elementGenerate = (Element) XPathFactory.newInstance().newXPath().evaluate("Generate", contents,
            XPathConstants.NODE);
         String afterScan = elementGenerate.getAttribute("after-scan");
         String schedule = elementGenerate.getAttribute("schedule");
         final Element elementSchedule = (Element) XPathFactory.newInstance().newXPath().evaluate("Schedule", contents,
            XPathConstants.NODE);
         String type = elementSchedule.getAttribute("type");
         String interval = elementSchedule.getAttribute("interval");
         String startDate = elementSchedule.getAttribute("start");
         String notValidAfter = elementSchedule.getAttribute("notValidAfter");
         ReportGenerateSchedule genSchedule = null;
         if (type != null)
            genSchedule = new ReportGenerateSchedule(type, interval, startDate, notValidAfter);
         m_reportGenrate = new ReportGenerate(afterScan, schedule);
         if (genSchedule != null)
            m_reportGenrate.setGenerateSchedule(genSchedule);
      }
      catch (XPathExpressionException e)
      {
         throw new RuntimeException("The Generate could not be generated: " + e.toString());
      }
   }

   /**
    * Retrieves a list of the sites associated with the Save Engine Request.
    *
    * @return the sites associated with the Save Engine request
    */
   public ReportGenerate getReportGenerate()
   {
      return m_reportGenrate;
   }

   /**
    * Sets the list of sites associated with the Save Engine Request.
    * @param sites the sites to be set
    */
   public void setReportGenerate(ReportGenerate reportGenerate)
   {
      m_reportGenrate = reportGenerate;
   }

   /////////////////////////////////////////////////////////////////////////
   // non-Public fields
   /////////////////////////////////////////////////////////////////////////

   private ReportGenerate m_reportGenrate;
}
