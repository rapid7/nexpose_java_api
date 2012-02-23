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

import javax.xml.xpath.*;

import org.rapid7.nexpose.api.*;
import org.w3c.dom.*;

public class ReportTemplateIDGenerator    implements IContentGenerator
{
  // ///////////////////////////////////////////////////////////////////////
  // Public methods
  // ///////////////////////////////////////////////////////////////////////
  public ReportTemplateIDGenerator(String templateID)
  {
    this.m_templateID = templateID;
  }

  public ReportTemplateIDGenerator()
  {
    this.m_templateID = null;
  }

  @Override
  public String toString()
  {
    StringBuilder sb = new StringBuilder();

    if(m_templateID != null)
    {
     sb.append("template-id=\"");
     sb.append(StringUtils.xmlEscape(m_templateID));
     sb.append("\"");
    }
     return sb.toString();
  }

  @Override
  public void setContents(Element contents)
  {
    try
    {
       final NodeList delivery =
          (NodeList) XPathFactory.newInstance().newXPath().evaluate("Delivery", contents, XPathConstants.NODESET);
       String location = null;
       if(delivery.getLength() > 0)
       {
          Element elementLocation = (Element) delivery.item(0);

          location = elementLocation.getTextContent();
       }
       m_templateID = null;
    }
    catch (XPathExpressionException e)
    {
       System.out.println("Could not parse the Contents Generator for " +
          "ReportTemplateIDGenerator: " + e.toString());
       throw new RuntimeException("The template-id could not be generated: " + e.toString());
    }

  }

  public String getTemplateID()
  {
    return m_templateID;
  }

  public void setTemplateID(String templateID)
  {
    this.m_templateID = templateID;
  }

  private String m_templateID;

}
