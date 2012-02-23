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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.rapid7.nexpose.api.IContentGenerator;
import org.rapid7.nexpose.api.StringUtils;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Generates Storage properties content to add to a SiloConfig.
 *
 * @author Leonardo Varela
 */
public class SiloConfigStoragePropertiesGenerator implements IContentGenerator
{

   /**
    * Represents a DBProperty associated with the storage configuration of a Silo Config.
    *
    * @author Leonardo Varela
    */
   public static class DBProperty
   {
      /**
       * Retrieves the key of the property.
       *
       * @return the key of the property.
       */
      public String getKey()
      {
         return m_key;
      }

      /**
       * Sets the key of the property.
       *
       * @param key The key of the property.
       */
      public void setKey(String key)
      {
         this.m_key = key;
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
       * @param key The key of the property 
       * @param value The value of the property.
       */
      public DBProperty(String key, String value)
      {
         m_key = key;
         m_value = value;
      }
      /**The key of the property. */
      private String m_key;
      /**The value of the property. */
      private String m_value;
   }

   /////////////////////////////////////////////////////////////////////////
   // Public methods
   /////////////////////////////////////////////////////////////////////////

   /**
    * Creates a new Storage Properties generator for the silo config.
    */
   public SiloConfigStoragePropertiesGenerator()
   {
      m_properties = new ArrayList<DBProperty>();
   }

   /**
    * Knows how to print the xml output for properties elements inside of a <SiloConfig> element on the silo Config.
    *
    * @see java.lang.Object#toString()
    */
   public String toString()
   {
      StringBuilder sb = new StringBuilder();
      Iterator<DBProperty> itProperties = m_properties.iterator();
      sb.append("<StorageProperties>");
      while(itProperties.hasNext())
      {
         DBProperty property = itProperties.next();
         sb.append("<StorageProperty key=\"");
         sb.append(StringUtils.xmlEscape(property.getKey()));
         sb.append("\" value=\"");
         sb.append(StringUtils.xmlEscape(property.getValue()));
         sb.append("\"/>");
      }
      sb.append("</StorageProperties>");
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
         final NodeList properties = (NodeList) XPathFactory.newInstance().newXPath().evaluate("StorageProperty", contents, XPathConstants.NODESET);
         for (int i = 0; i < properties.getLength(); i++)
         {
            Element elementProperty = (Element) properties.item(i);
            String key = elementProperty.getAttribute("key");
            String value = elementProperty.getAttribute("value");
            DBProperty property = new DBProperty(key, value);
            m_properties.add(property);
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
   public List<DBProperty> getProperties()
   {
      return m_properties;
   }

   /**
    * Sets the list of other-type associated with the Silo Config.
    *
    * @param properties The other-types to be set.
    */
   public void setPropertiess(List<DBProperty> properties)
   {
      this.m_properties = properties;
   }

   /////////////////////////////////////////////////////////////////////////
   // non-Public fields
   /////////////////////////////////////////////////////////////////////////

   /**The Properties associated with the Silo Config*/
   private List<DBProperty> m_properties;
}
