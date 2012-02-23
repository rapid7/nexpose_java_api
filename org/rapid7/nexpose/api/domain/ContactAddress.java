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
package org.rapid7.nexpose.api.domain;

/**
 * Represents an address associated with a PCI Merchant.
 *
 * @author Leonardo Varela
 */
public class ContactAddress
{
   /////////////////////////////////////////////////////////////////////////
   // Public methods
   /////////////////////////////////////////////////////////////////////////

   /**
    * Retreives the city of the Address.
    * 
    * @return The city of the address.
    */
   public String getCity()
   {
      return m_city;
   }

   /**
    * Sets the city of the Address.
    *
    * @param city The city to set.
    */
   public void setCity(String city)
   {
      m_city = city;
   }

   /**
    * Retrieves the country of the address.
    *
    * @return The country of the address.
    */
   public String getCountry()
   {
      return m_country;
   }

   /**
    * Sets the country of thre address.
    * 
    * @param country The country to set.
    */
   public void setCountry(String country)
   {
      m_country = country;
   }

   /**
    * Retrieves the line 1 of the address.
    *
    * @return The the line 1 of the address.
    */
   public String getLine1()
   {
      return m_line1;
   }

   /**
    * Sets the line1 of the address.
    *
    * @param line1 The line1 to set.
    */
   public void setLine1(String line1)
   {
      m_line1 = line1;
   }

   /**
    * Retrieves the line 2 of the address.
    *
    * @return The the line 2 of the address.
    */
   public String getLine2()
   {
      return m_line2;
   }

   /**
    * Sets the line2 of the address.
    *
    * @param line2 The line2 to set.
    */
   public void setLine2(String line2)
   {
      m_line2 = line2;
   }

   /**
    * Retrieves the state of the address.
    *
    * @return The state of the address.
    */
   public String getState()
   {
      return m_state;
   }

   /**
    * Sets the state of the address.
    *
    * @param state The state to set.
    */
   public void setState(String state)
   {
      m_state = state;
   }

   /**
    * Retrieves the zip code of the address.
    * 
    * @return The zip of the address.
    */
   public String getZip()
   {
      return m_zip;
   }

   /**
    * Sets the Zip of the address.
    *
    * @param zip The zip to set.
    */
   public void setZip(String zip)
   {
      m_zip = zip;
   }
   /////////////////////////////////////////////////////////////////////////
   // Non-public fields
   /////////////////////////////////////////////////////////////////////////

   /**Represents the city of the address.*/
   private String m_city;
   /**Represents the country of the address.*/
   private String m_country;
   /**Represents the first line of the address.*/
   private String m_line1;
   /**Represents the second line of the address.*/
   private String m_line2;
   /**Represents the state of the address.*/
   private String m_state;
   /**Represents the zip code of the address.*/
   private String m_zip;
}
