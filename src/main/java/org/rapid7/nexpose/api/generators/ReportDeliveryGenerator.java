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

import org.rapid7.nexpose.api.domain.ReportDeliveryStorage;
import org.rapid7.nexpose.utils.StringUtils;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class ReportDeliveryGenerator  implements IContentGenerator
{
  // ///////////////////////////////////////////////////////////////////////
  // Public methods
  // ///////////////////////////////////////////////////////////////////////
  public ReportDeliveryGenerator()
  {
    m_storage = new ReportDeliveryStorage("1",null);
  }

  @Override
  public String toString()
  {
     String location;
     StringBuilder sb = new StringBuilder();
     sb.append("<Delivery>");

     sb.append("<Storage storeOnServer=\"");
     sb.append(StringUtils.xmlEscape(m_storage.getStoreOnServer()));
     sb.append("\">");
     location = m_storage.getLocation();
     if(location != null && !location.isEmpty() )
     {
       sb.append("<Location>");
       sb.append(StringUtils.xmlEscape(location));
       sb.append("</location>");
     }
     sb.append("</Storage>");
     sb.append("</Delivery>");
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
       m_storage =
             new ReportDeliveryStorage(contents.getAttribute("storeOnServer"),location);
      // TODO - implement locations if needed in future for other report types than DBExport
    }
    catch (XPathExpressionException e)
    {
       System.out.println("Could not parse the Contents Generator for " +
          "ReportDeliveryGenerator: " + e.toString());
       throw new RuntimeException("The Delivery location could not be generated: " + e.toString());
    }

  }

  public ReportDeliveryStorage getStorage()
  {
    return m_storage;
  }

  public void setStorage(ReportDeliveryStorage storage)
  {
    m_storage = storage;
  }

  private ReportDeliveryStorage m_storage;
}
