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

import org.rapid7.nexpose.api.domain.ContactAddress;
import org.rapid7.nexpose.utils.StringUtils;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Element;

/**
 * Generates Silo Organization element tags inside of the SiloCreate Silo Config
 *
 * @author Leonardo Varela
 */
public class SiloCreateOrganizationGenerator implements IContentGenerator
{

   // ///////////////////////////////////////////////////////////////////////
   // Public methods
   // ///////////////////////////////////////////////////////////////////////

   /**
    * Creates a new Storage Properties generator for the silo config.
    */
   public SiloCreateOrganizationGenerator()
   {
      m_url = new String();
   }

   /**
    * Knows how to print the xml output for properties elements inside of a <SiloConfig> element on the silo Config.
    *
    * @see java.lang.Object#toString()
    */
   @Override
   public String toString()
   {
      StringBuilder sb = new StringBuilder();
      sb.append("<Organization ");
      sb.append("url=\"");
      sb.append(StringUtils.xmlEscape(m_url));
      sb.append("\" company=\"");
      sb.append(StringUtils.xmlEscape(m_company));
      sb.append("\" email-address=\"");
      sb.append(StringUtils.xmlEscape(m_emailAddress));
      sb.append("\" first-name=\"");
      sb.append(StringUtils.xmlEscape(m_firstName));
      sb.append("\" last-name=\"");
      sb.append(StringUtils.xmlEscape(m_lastName));
      sb.append("\" phone-number=\"");
      sb.append(StringUtils.xmlEscape(m_phoneNumber));
      sb.append("\" title=\"");
      sb.append(StringUtils.xmlEscape(m_title));
      if (m_address != null)
      {
         sb.append("\"><Address city=\"");
         sb.append(StringUtils.xmlEscape(m_address.getCity()));
         sb.append("\" country=\"");
         sb.append(StringUtils.xmlEscape(m_address.getCountry()));
         sb.append("\" line1=\"");
         sb.append(StringUtils.xmlEscape(m_address.getLine1()));
         sb.append("\" line2=\"");
         sb.append(StringUtils.xmlEscape(m_address.getLine2()));
         sb.append("\" state=\"");
         sb.append(StringUtils.xmlEscape(m_address.getState()));
         sb.append("\" zip=\"");
         sb.append(StringUtils.xmlEscape(m_address.getZip()));
         sb.append("\"/></Organization>");
      }
      else
      {
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
         final Element elementOrganization = (Element) XPathFactory.newInstance().newXPath().evaluate("Organization", contents,
               XPathConstants.NODE);
         m_url = elementOrganization.getAttribute("url");
         m_company = elementOrganization.getAttribute("company");
         m_emailAddress = elementOrganization.getAttribute("email-address");
         m_firstName = elementOrganization.getAttribute("first-name");
         m_lastName = elementOrganization.getAttribute("last-name");
         m_phoneNumber = elementOrganization.getAttribute("phone-number");
         m_title = elementOrganization.getAttribute("title");
         final Element addressElement = (Element) XPathFactory.newInstance().newXPath().evaluate("Address", elementOrganization,
               XPathConstants.NODE);
         if (addressElement != null)
         {
            m_address.setCity(addressElement.getAttribute("city"));
            m_address.setCountry(addressElement.getAttribute("country"));
            m_address.setLine1(addressElement.getAttribute("line1"));
            m_address.setLine2(addressElement.getAttribute("line2"));
            m_address.setState(addressElement.getAttribute("state"));
            m_address.setZip(addressElement.getAttribute("zip"));
         }
      }
      catch (XPathExpressionException e)
      {
         System.out.println("Could not parse the generator for SiloCreateMerchantGenerator: " + e.toString());
         throw new RuntimeException("The PCI Merchant could not be generated: " + e.toString());
      }
   }

   /**
    * Retrieves the Organization's url associated with the Silo Create config.
    *
    * @return the url associated with the Silo Create config.
    */
   public String getURL()
   {
      return m_url;
   }

   /**
    * Sets the URL of the organization associated with the Silo Create config.
    *
    * @param url The URL to be set.
    */
   public void setURL(String url)
   {
      m_url = url;
   }

   /**
    * Sets the address associated with this organization generator.
    *
    * @param address The address to set.
    */
   public void setAddress(ContactAddress address)
   {
      m_address = address;
   }

   /**
    * Retrieves the address associated with this organization.
    *
    * @return The address of this organization.
    */
   public ContactAddress getAddress()
   {
      return m_address;
   }

   /**
    * Sets the company associated with the organization.
    *
    * @param company The company to set.
    */
   public void setCompany(String company)
   {
      m_company = company;
   }

   /**
    * Retrieves the company associated with the organization.
    *
    * @return The company associated with the organization.
    */
   public String getCompany()
   {
      return m_company;
   }

   /**
    * Sets the email address associated with the organization.
    *
    * @param emaiAddress The email address to set.
    */
   public void setEmailAddress(String emaiAddress)
   {
      m_emailAddress = emaiAddress;
   }

   /**
    * Retrieves the email address associated with the organization.
    *
    * @return The email address associated with the organization.
    */
   public String getEmailAddress()
   {
      return m_emailAddress;
   }

   /**
    * Sets the first name associated with the organization.
    *
    * @param firstName The first name to set.
    */
   public void setFirstName(String firstName)
   {
      m_firstName = firstName;
   }

   /**
    * Retrieves the first name associated with the organization.
    *
    * @return The first name associated with the organization.
    */
   public String getFirstName()
   {
      return m_firstName;
   }

   /**
    * Sets the last name associated with the organization.
    *
    * @param lastName The last name to set.
    */
   public void setLastName(String lastName)
   {
      m_lastName = lastName;
   }

   /**
    * Retrieves the last name associated with the organization.
    *
    * @return The last name associated with the organization.
    */
   public String getLastName()
   {
      return m_lastName;
   }

   /**
    * Sets the phone number associated with the organization.
    *
    * @param phoneNumber The phone number to set.
    */
   public void setPhoneNumber(String phoneNumber)
   {
      m_phoneNumber = phoneNumber;
   }

   /**
    * Retrieves the phone number associated with the organization.
    *
    * @return The phone number associated with the organization.
    */
   public String getPhoneNumber()
   {
      return m_phoneNumber;
   }

   /**
    * Sets the title associated with the organization.
    *
    * @param title The title to set
    */
   public void setTitle(String title)
   {
      m_title = title;
   }

   /**
    * Retrieves the title associated with the organization.
    *
    * @return The title
    */
   public String getTitle()
   {
      return m_title;
   }

   // ///////////////////////////////////////////////////////////////////////
   // non-Public fields
   // ///////////////////////////////////////////////////////////////////////

   /** The url associated with the organization. */
   private String m_url;
   /** The Address associated with the organization */
   private ContactAddress m_address;
   /** The name of the company for the organization */
   private String m_company;
   /** The email address associated with the organization */
   private String m_emailAddress;
   /** The first name associated with the organization */
   private String m_firstName;
   /** The last name associated with the organization */
   private String m_lastName;
   /** The phone number associated with the organization. */
   private String m_phoneNumber;
   /** The title associated with the organization */
   private String m_title;
}
