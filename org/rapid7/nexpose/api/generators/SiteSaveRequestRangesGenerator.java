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
 * Generates hosts content to add to a Site Save Request.
 *
 * @author Leonardo Varela
 */
public class SiteSaveRequestRangesGenerator implements IContentGenerator
{
   /**
    * Represents a range contained in a Site Save Request
    *
    * @author Leonardo Varela
    */
   public static class SiteSaveRequestRange
   {
      /////////////////////////////////////////////////////////////////////////
      // Public methods
      /////////////////////////////////////////////////////////////////////////

      /**
       * Creates a new save site request range.
       *
       * @param from the from address of the range
       * @param to the to address of the range.
       */
      public SiteSaveRequestRange(String from, String to)
      {
         m_to = to;
         m_from = from;
      }

      /**
       * @return the to address of the range
       */
      public String getToAddress()
      {
         return m_to;
      }

      /**
       * @param toAddress the siteId to set
       */
      public void setToAddress(String toAddress)
      {
         m_to = toAddress;
      }

      /**
       * @return the from address of the range
       */
      public String getFromAddress()
      {
         return m_from;
      }

      /**
       * @param fromAddress the from address to set
       */
      public void setFromAddress(String fromAddress)
      {
         m_from = fromAddress;
      }
      /////////////////////////////////////////////////////////////////////////
      // non-Public fields
      /////////////////////////////////////////////////////////////////////////

      /**The from address of the range*/
      private String m_from;
      /**The to address of the range*/
      private String m_to;
   }
   /////////////////////////////////////////////////////////////////////////
   // Public methods
   /////////////////////////////////////////////////////////////////////////

   public SiteSaveRequestRangesGenerator()
   {
      m_ranges = new ArrayList<SiteSaveRequestRange>();
   }
   /**
    * Knows how to create ranges inside of a site save request.
    *
    * @see java.lang.Object#toString()
    */
   @Override
   public String toString()
   {
      StringBuilder sb = new StringBuilder();
      Iterator<SiteSaveRequestRange> itRanges = m_ranges.iterator();
      while(itRanges.hasNext())
      {
         SiteSaveRequestRange range = itRanges.next();
         sb.append("<range from=\"");
         sb.append(StringUtils.xmlEscape(range.getFromAddress()));
         sb.append("\" to=\"");
         sb.append(StringUtils.xmlEscape(range.getToAddress()));
         sb.append("\"/>");
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
         final NodeList ranges = (NodeList) XPathFactory.newInstance().newXPath().evaluate("range", contents, XPathConstants.NODESET);
         for (int i = 0; i < ranges.getLength(); i++)
         {
            Element elementRange = (Element) ranges.item(i);
            SiteSaveRequestRange range = new SiteSaveRequestRange(elementRange.getAttribute("from"), elementRange.getAttribute("to"));
            m_ranges.add(range);
         }
      }
      catch (XPathExpressionException e)
      {
         System.out.println("Could not parse the Contents Generator for SaveSiteRequestRangesGenerator: " + e.toString());
         throw new RuntimeException("The ranges could not be generated: " + e.toString());
      }
   }

   /**
    * Retrieves a list of ranges associated with the Site Save Request.
    * @return the list of ranges associated with the Site Save Request.
    */
   public List<SiteSaveRequestRange> getRanges()
   {
      return m_ranges;
   }

   /**
    * Sets the list of ranges associated to the site save request.
    *
    * @param ranges the ranges to beset
    */
   public void setRanges(List<SiteSaveRequestRange> ranges)
   {
      m_ranges = ranges;
   }
   /////////////////////////////////////////////////////////////////////////
   // non-Public fields
   /////////////////////////////////////////////////////////////////////////

   /**The list or ranges associated with this generator.*/
   private List<SiteSaveRequestRange> m_ranges;
}
