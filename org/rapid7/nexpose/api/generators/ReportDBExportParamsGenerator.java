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

public class ReportDBExportParamsGenerator implements IContentGenerator
{

   /**
    * Represents a DBProperty associated with the storage configuration of a Silo Config.
    *
    * @author Leonardo Varela
    */
   public static class DBParam
   {
      /**
       * Retrieves the name of the property.
       *
       * @return the name of the property.
       */
      public String getName()
      {
         return m_name;
      }

      /**
       * Sets the name of the property.
       *
       * @param name The name of the property.
       */
      public void setName(String name)
      {
         this.m_name = name;
      }

      /**
       * Retrieves the value of the property
       *
       * @return The value of the property.
       */
      public String getValue()
      {
         return m_value;
      }

      /**
       * Sets the value of the property
       *
       * @param value The value to set.
       */
      public void setValue(String value)
      {
         this.m_value = value;
      }
      /**
       * Creates a new Property.
       *
       * @param name The name of the property
       * @param value The value of the property.
       */
      public DBParam(String name, String value)
      {
         m_name = name;
         m_value = value;
      }
      /**The name of the property. */
      private String m_name;
      /**The value of the property. */
      private String m_value;
   }

   /////////////////////////////////////////////////////////////////////////
   // Public methods
   /////////////////////////////////////////////////////////////////////////

   /**
    * Creates a new Storage Properties generator for the silo config.
    */
   public ReportDBExportParamsGenerator()
   {
      m_param = new ArrayList<DBParam>();
   }

   /**
    * Knows how to print the xml output for properties elements inside of a <SiloConfig> element on the silo Config.
    *
    * @see java.lang.Object#toString()
    */
   public String toString()
   {
      StringBuilder sb = new StringBuilder();
      Iterator<DBParam> itProperties = m_param.iterator();
      while(itProperties.hasNext())
      {
         DBParam property = itProperties.next();
         sb.append("<param name=\"");
         sb.append(StringUtils.xmlEscape(property.getName()));
         sb.append("\">");
         sb.append(StringUtils.xmlEscape(property.getValue()));
         sb.append("</param>");
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
         final NodeList properties = (NodeList) XPathFactory.newInstance().newXPath().evaluate("DBExport", contents, XPathConstants.NODESET);
         for (int i = 0; i < properties.getLength(); i++)
         {
            Element elementProperty = (Element) properties.item(i);
            String name = elementProperty.getAttribute("name");
            String value = elementProperty.getNodeValue();
            DBParam param = new DBParam(name, value);
            m_param.add(param);
         }
      }
      catch (XPathExpressionException e)
      {
         System.out.println("Could not parse the generator for SiloConfigStoragePropertiesGenerator: " + e.toString());
         throw new RuntimeException("The DBPRoperty could not be generated: " + e.toString());
      }
   }

   /**
    * Retrieves the list of DBProperty elements associated with the Silo Config.
    *
    * @return the list of DBProperty properties associated with the Silo Config.
    */
   public List<DBParam> getParams()
   {
      return m_param;
   }

   /**
    * Sets the list of other-type associated with the Silo Config.
    *
    * @param properties The other-types to be set.
    */
   public void setParams(List<DBParam> param)
   {
      this.m_param = param;
   }

   /////////////////////////////////////////////////////////////////////////
   // non-Public fields
   /////////////////////////////////////////////////////////////////////////

   /**The Properties associated with the Silo Config*/
   private List<DBParam> m_param;
}
