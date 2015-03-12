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
 * Generates Filters content to add to a Ticket Listing Request.
 *
 * @author Murali Rongali
 */
public class TicketRequestFilterContentGenerator implements IContentGenerator
{
   /**
    * Represents a filter contained in an Ticket Listing Request.
    *
    * @author Murali Rongali
    */
   public static class TicketListingRequestFilter
   {

      /////////////////////////////////////////////////////////////////////////
      // Public methods
      /////////////////////////////////////////////////////////////////////////

      /**
       * Constructs a new TicketListingRequestFilter object.
       *
       * @param type The filter type.
       * @param value The value of the filter.
       */
      public TicketListingRequestFilter(String type, String value)
      {
         m_type = type;
         m_value = value;
      }

      /**
       * Returns the type of the filter.
       *
       * @return the filter's type.
       */
      public String getType()
      {
         return m_type;
      }

      /**
       * Sets the type of the filter.
       *
       * @param type the filter type to set.
       */
      public void setType(String type)
      {
         m_type = type;
      }

      /**
       * Returns the value of the filter.
       *
       * @return the filter value.
       */
      public String getValue()
      {
         return m_value;
      }

      /**
       * Sets the value of the filter.
       *
       * @param value the filter value to set.
       */
      public void setValue(String value)
      {
         m_value = value;
      }

      /////////////////////////////////////////////////////////////////////////
      // Non-public fields
      /////////////////////////////////////////////////////////////////////////

      /**The type of the Filter*/
      private String m_type;

      /**The value of the filter*/
      private String m_value;
   }

   /**
    * Constructs a new TicketRequestFilterContentGenerator object.
    */
   public TicketRequestFilterContentGenerator()
   {
      m_filters = new ArrayList<TicketListingRequestFilter>();
   }

   @Override
   public String toString()
   {
      StringBuilder sb = new StringBuilder();
      for (TicketListingRequestFilter filter : m_filters)
      {
         sb.append("<Filter type=\"");
         sb.append(StringUtils.xmlEscape(filter.getType()));
         sb.append("\" value=\"");
         sb.append(StringUtils.xmlEscape(filter.getValue()));
         sb.append("\"></Filter>");
      }
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
         final NodeList filters =
            (NodeList) XPathFactory.newInstance().newXPath().evaluate("Filter", contents, XPathConstants.NODESET);
         for (int i = 0; i < filters.getLength(); i++)
         {
            Element elementFilter = (Element) filters.item(i);
            TicketListingRequestFilter filter =
               new TicketListingRequestFilter(elementFilter.getAttribute("type"),elementFilter.getAttribute("value"));
            m_filters.add(filter);
         }
      }
      catch (XPathExpressionException e)
      {
         System.out.println("Could not parse the Contents Generator for " +
            "TicketRequestFilterContentGenerator: " + e.toString());
         throw new RuntimeException("The filters could not be generated: " + e.toString());
      }
   }

   /**
    * Returns the list of filters which were sent with the Ticket Listing request.
    *
    * @return The list of filters.
    */
   public List<TicketListingRequestFilter> getFilters()
   {
      return m_filters;
   }

   /**
    * Sets the list of filters.
    *
    * @param filters The list of filters.
    */
   public void setFilters(List<TicketListingRequestFilter> filters)
   {
      m_filters = filters;
   }

   /////////////////////////////////////////////////////////////////////////
   // Non-public fields
   /////////////////////////////////////////////////////////////////////////

   /** List of filters */
   private List<TicketListingRequestFilter> m_filters;

}
