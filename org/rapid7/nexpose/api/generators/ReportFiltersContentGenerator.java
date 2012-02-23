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
import org.w3c.dom.*;

public class ReportFiltersContentGenerator implements IContentGenerator
{
   /**
    * Represents a filter contained in an Report Save Request.
    *
    * @author Murali Rongali
    */
   public static class ReportFilter
   {

      /////////////////////////////////////////////////////////////////////////
      // Public methods
      /////////////////////////////////////////////////////////////////////////

      /**
       * Constructs a new ReportFilter object.
       *
       * @param type The filter type.
       * @param id The id of the filter.
       */
      public ReportFilter(String type, String id)
      {
         m_type = type;
         m_ID = id;
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
       * Returns the ID of the filter.
       *
       * @return the filter ID.
       */
      public String getID()
      {
         return m_ID;
      }

      /**
       * Sets the value of the filter.
       *
       * @param value the filter value to set.
       */
      public void setID(String id)
      {
         m_ID = id;
      }

      /////////////////////////////////////////////////////////////////////////
      // Non-public fields
      /////////////////////////////////////////////////////////////////////////

      /**The type of the Filter*/
      private String m_type;

      /**The ID of the filter*/
      private String m_ID;
   }

   /**
    * Constructs a new TicketRequestFilterContentGenerator object.
    */
   public ReportFiltersContentGenerator()
   {
      m_filters = new ArrayList<ReportFilter>();
   }

   @Override
   public String toString()
   {
      StringBuilder sb = new StringBuilder();
      sb.append("<Filters>");
      for (ReportFilter filter : m_filters)
      {
         sb.append("<filter type=\"");
         sb.append(StringUtils.xmlEscape(filter.getType()));
         sb.append("\" id=\"");
         sb.append(StringUtils.xmlEscape(filter.getID()));
         sb.append("\"/>");
      }
      sb.append("</Filters>");
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
            ReportFilter filter =
               new ReportFilter(elementFilter.getAttribute("type"),elementFilter.getAttribute("id"));
            m_filters.add(filter);
         }
      }
      catch (XPathExpressionException e)
      {
         System.out.println("Could not parse the Contents Generator for " +
            "ReportFiltersContentGenerator: " + e.toString());
         throw new RuntimeException("The filters could not be generated: " + e.toString());
      }
   }

   /**
    * Returns the list of filters which were sent with the Ticket Listing request.
    *
    * @return The list of filters.
    */
   public List<ReportFilter> getFilters()
   {
      return m_filters;
   }

   /**
    * Sets the list of filters.
    *
    * @param filters The list of filters.
    */
   public void setFilters(List<ReportFilter> filters)
   {
      m_filters = filters;
   }

   /////////////////////////////////////////////////////////////////////////
   // Non-public fields
   /////////////////////////////////////////////////////////////////////////

   /** List of filters */
   private List<ReportFilter> m_filters;
}
