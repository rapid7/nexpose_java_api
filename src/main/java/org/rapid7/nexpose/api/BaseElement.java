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
package org.rapid7.nexpose.api;

import org.w3c.dom.Element;

/**
 * Offers basic data retrieval convenience methods used across the Responses.
 *
 * @author Leonardo Varela
 */
public class BaseElement
{
   /////////////////////////////////////////////////////////////////////////
   // Public methods
   /////////////////////////////////////////////////////////////////////////

   /**
    * Sets the response element to work with.
    *
    * @param element the element to work with.
    */
   public void setResponseElement(Element element)
   {
      m_responseElement = element;
   }

   /**
    * Retrieves an integer representing the attribute value associated with the
    * parameter of the method.
    *
    * @param attributeName the name of the attribute to be retrieved.
    * @return the integer value of the attribute in the element of this response.
    * @throws APIException when the attribute does not contain an integer as value 
    *         or when the attributeName or m_responseElement are null.
    */
   public int getInt(String attributeName) throws APIException
   {
      String attributeValue = null;
      if (attributeName != null && m_responseElement != null)
      {
         try
         {
            attributeValue = m_responseElement.getAttribute(attributeName);
            return Integer.parseInt(attributeValue);
         } catch (NumberFormatException nfe)
         {
            throw new APIException("Cannot parse the response, the attribute: "
                                      + attributeName
                                      + " is not an integer: "
                                      + attributeValue);
         }
      }
      throw new APIException("The response element and the attribute name should not be null");
   }

   /**
    * Retrieves a long representing the attribute value associated with the
    * parameter of the method.
    *
    * @param attributeName the name of the attribute to be retrieved.
    * @return the long value of the attribute in the element of this response.
    * @throws APIException when the attribute does not contain a long as value
    *         or when the attributeName or m_responseElement are null.
    */
   public long getLong(String attributeName) throws APIException
   {
      String attributeValue = null;
      if (attributeName != null && m_responseElement != null)
      {
         try
         {
            attributeValue = m_responseElement.getAttribute(attributeName);
            return Long.parseLong(attributeValue);
         }
         catch (NumberFormatException nfe)
         {
            throw new APIException("Cannot parse the response, the attribute: "
                                      + attributeName
                                      + " is not a long: "
                                      + attributeValue);
         }
      }
      throw new APIException("The response element and the attribute name should not be null");
   }

   /**
    * Retrieves a float representing the attribute value associated with the
    * parameter of the method.
    *
    * @param attributeName the name of the attribute to be retrieved.
    * @return the float value of the attribute in the element of this response.
    * @throws APIException when the attribute does not contain an integer as value 
    *         or when the attributeName or m_responseElement are null.
    */
   public float getFloat(String attributeName) throws APIException
   {
      String attributeValue = null;
      if (attributeName != null && m_responseElement != null)
      {
         try
         {
            attributeValue = m_responseElement.getAttribute(attributeName);
            return Float.parseFloat(attributeValue);
         } 
         catch (NumberFormatException nfe)
         {
            throw new APIException("Cannot parse the response, the attribute: "
                     + attributeName 
                     + " is not a float: "
                     + attributeValue);
         }
      }
      throw new APIException("The response element and the attribute name should not be null");
   }

   /**
    * Retrieves a boolean representing the attribute value associated with the
    * parameter of the method.
    *
    * @param attributeName the name of the attribute to be retrieved.
    * @return the boolean value of the attribute in the element of this response.
    * @throws APIException when the attribute does not contain an integer as value 
    *         or when the attributeName or m_responseElement are null.
    */
   public boolean getBoolean(String attributeName) throws APIException
   {
      String attributeValue = null;
      if (attributeName != null && m_responseElement != null)
      {
         try
         {
            attributeValue = m_responseElement.getAttribute(attributeName);
            if ("1".equalsIgnoreCase(attributeValue))
            {
               return true;
            }
            else if ("0".equalsIgnoreCase(attributeValue))
            {
               return false;
            }
            else if ("true".equalsIgnoreCase(attributeValue))
            {
               return false;
            }
            else if ("false".equalsIgnoreCase(attributeValue))
            {
               return false;
            }
            else
            {
               throw new APIException("The attribute: " 
                  + attributeName
                  + " is not a boolean(0 or 1) value: "
                  + attributeValue);
            }
         } 
         catch (NumberFormatException nfe)
         {
            throw new APIException("Cannot parse the response, the attribute: "
                     + attributeName 
                     + " is not a float: "
                     + attributeValue);
         }
      }
      throw new APIException("The response element and the attribute name "
         + "should not be null");
   }

   /**
    * Retrieves a String representing the attribute value associated with the parameter of the method.
    * 
    * @param attributeName the name of the attribute to be retrieved.
    * @return the String  value of the attribute in the element of this response.
    * @throws APIException when the attribute does not contain an integer as value 
    *         or when the attributeName or m_responseElement are null.
    */
   public String getString(String attributeName) throws APIException
   {
      if (attributeName != null && m_responseElement != null)
      {
         return m_responseElement.getAttribute(attributeName);
      }
      throw new APIException("The response element and the attribute "
         + "name should not be null");
   }

   /////////////////////////////////////////////////////////////////////////
   // non-Public fields
   /////////////////////////////////////////////////////////////////////////

   /**Represents the base element for this response*/
   private Element m_responseElement;
}
