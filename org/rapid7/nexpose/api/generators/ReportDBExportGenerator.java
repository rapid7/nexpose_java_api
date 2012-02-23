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

import java.util.*;

import javax.xml.xpath.*;

import org.rapid7.nexpose.api.*;
import org.rapid7.nexpose.api.domain.*;
import org.w3c.dom.*;

public class ReportDBExportGenerator  implements IContentGenerator
{

  @Override
  public String toString()
  {
     StringBuilder sb = new StringBuilder();
     sb.append("<DBExport type=\"");
     sb.append(StringUtils.xmlEscape(m_reportDBExport.getType()));
     sb.append("\">");

     sb.append("<credentials userid=\"");
     sb.append(StringUtils.xmlEscape(m_reportDBExport.getUserID()));
     sb.append("\" password=\"");
     sb.append(StringUtils.xmlEscape(m_reportDBExport.getPassword()));
     // TODO - Realm

     sb.append("\"/>");
     sb.append(m_reportDBExport.getParamGenerator().toString());
     sb.append("</DBExport>");
     return sb.toString();
  }

  @Override
  public void setContents(Element contents)
  {
    try
    {
       final NodeList delivery =
          (NodeList) XPathFactory.newInstance().newXPath().evaluate("DBExport", contents, XPathConstants.NODESET);
       String userid = null;
      String password = null;
      String realm = null;
      List<ReportDBExportParamsGenerator.DBParam> params = new ArrayList<ReportDBExportParamsGenerator.DBParam>();
      for(int itemNo = 0; itemNo < delivery.getLength(); ++itemNo)
       {
          Element element = (Element) delivery.item(itemNo);
         if("credentials".equals(element.getNodeName()))
         {
            userid = element.getAttribute("userid");
            password = element.getAttribute("password");
            realm = element.getAttribute("realm");
         }
         if("param".equals(element.getNodeName()))
         {
           ReportDBExportParamsGenerator.DBParam param =
             new ReportDBExportParamsGenerator.DBParam(element.getAttribute("name"), element.getNodeValue());
           params.add(param);
         }

       }
       m_reportDBExport =
             new ReportDBExport(contents.getAttribute("type"),userid, password, realm);
      ReportDBExportParamsGenerator paramGenerator = (ReportDBExportParamsGenerator) m_reportDBExport.getParamGenerator();
      paramGenerator.setParams(params);
    }
    catch (XPathExpressionException e)
    {
       System.out.println("Could not parse the Contents Generator for " +
          "ReportDeliveryGenerator: " + e.toString());
       throw new RuntimeException("The Delivery location could not be generated: " + e.toString());
    }

  }
  public ReportDBExport getReportDBExport()
  {
    return m_reportDBExport;
  }

  public void setReportDBExport(ReportDBExport m_reportDBExport)
  {
    this.m_reportDBExport = m_reportDBExport;
  }

  private ReportDBExport m_reportDBExport;
}
