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
import java.util.Iterator;
import java.util.List;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Creates the Recipients associated to an SMTP Alert.
 *
 * @author Leonardo Varela
 */
public class SMTPAlertRecipientGenerator implements IContentGenerator
{
   /////////////////////////////////////////////////////////////////////////
   // Public methods
   /////////////////////////////////////////////////////////////////////////

   /**
    * Creates a sites generator for the user save request.
    */
   public SMTPAlertRecipientGenerator()
   {
      m_recipients = new ArrayList<String>();
   }

   /**
    * Knows how to output a site inside of a user save request.
    *
    * @see java.lang.Object#toString()
    */
   @Override
   public String toString()
   {
      StringBuilder sb = new StringBuilder();
      Iterator<String> itRecipient = m_recipients.iterator();
      while(itRecipient.hasNext())
      {
         String recipient = itRecipient.next();
         sb.append("<Recipient>");
         sb.append(StringUtils.xmlEscape(recipient));
         sb.append("</Recipient>");
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
         final NodeList recipients = (NodeList) XPathFactory.newInstance().newXPath().evaluate("Recipient", contents, XPathConstants.NODESET);
         for (int i = 0; i < recipients.getLength(); i++)
         {
            Element elementRecipient = (Element) recipients.item(i);
            String recipient = elementRecipient.getTextContent();
            m_recipients.add(recipient);
         }
      }
      catch (XPathExpressionException e)
      {
         System.out.println("Could not parse the Contents Generator for"
            + " SaveEngineRequestSiteContentGenerator: " + e.toString());
         throw new RuntimeException("The sites could not be generated: "
            + e.toString());
      }
   }

   /**
    * Retrieves a list of recipients associated with an SMTP Alert
    *
    * @return the recipients associated with an SMTP Alert.
    */
   public List<String> getRecipients()
   {
      return m_recipients;
   }

   /**
    * Sets the list of recipients associated to an SMTP Alert.
    *
    * @param recipients the recipients to be set.
    */
   public void setSites(List<String> recipients)
   {
      m_recipients = recipients;
   }
   /////////////////////////////////////////////////////////////////////////
   // non-Public fields
   /////////////////////////////////////////////////////////////////////////

   /**The recipients associated with this generator.*/
   private List<String> m_recipients;
}
