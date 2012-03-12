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
 * Generates Comments content to add to a Create Ticket request.
 *
 * @author Murali Rongali
 */
public class TicketRequestCommentsContentGenerator implements IContentGenerator
{
   /////////////////////////////////////////////////////////////////////////
   // Public methods
   /////////////////////////////////////////////////////////////////////////

   /**
    * Constructs a new SaveTicketRequestCommentsContentGenerator object.
    */
   public TicketRequestCommentsContentGenerator()
   {
      m_comments = new ArrayList<String>();
   }

   @Override
   public String toString()
   {
      StringBuilder sb = new StringBuilder();
      sb.append("<Comments>");
      for (String comment : m_comments)
      {
         sb.append("<Comment>");
         sb.append(StringUtils.xmlEscape(comment));
         sb.append("</Comment>");
      }
      sb.append("</Comments>");
      return sb.toString();
   }

   /**
    * Sets the contents of the generator that come as a parameter.
    *
    * @param contents Elements of the xml request.
    */
   @Override
   public void setContents(Element contents)
   {
      try
      {
         final NodeList comments =
            (NodeList) XPathFactory.newInstance().newXPath().evaluate("Comment", contents, XPathConstants.NODESET);
         for (int i = 0; i < comments.getLength(); i++)
         {
            Element elementComment = (Element) comments.item(i);
            String comment = new String(elementComment.getTextContent());
            m_comments.add(comment);
         }
      }
      catch (XPathExpressionException e)
      {
         System.out.println("Could not parse the Contents Generator for " +
            "TicketRequestCommentsContentGenerator: " + e.toString());
         throw new RuntimeException("The sites could not be generated: " + e.toString());
      }
   }

   /**
    * Returns the list of comments which were sent with the Create Ticket request.
    *
    * @return The list of Comments.
    */
   public List<String> getComments()
   {
      return m_comments;
   }

   /**
    * Sets the list of comments.
    *
    * @param comments The list of Comments.
    */
   public void setComments(List<String> comments)
   {
      m_comments = comments;
   }

   /////////////////////////////////////////////////////////////////////////
   // Non-public fields
   /////////////////////////////////////////////////////////////////////////

   /** List of comments to be saved */
   private List<String> m_comments;

}
