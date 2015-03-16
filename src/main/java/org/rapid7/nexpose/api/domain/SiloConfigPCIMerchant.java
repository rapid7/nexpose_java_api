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
package org.rapid7.nexpose.api.domain;

import org.rapid7.nexpose.api.generators.IContentGenerator;

/**
 * Represents a PCI Merchant associated to a Silo Configuration.
 *
 * @author Leonardo Varela
 */
public class SiloConfigPCIMerchant
{

   // ///////////////////////////////////////////////////////////////////////
   // Public methods
   // ///////////////////////////////////////////////////////////////////////

   /**
    * @return The m_address
    */
   public ContactAddress getAddress()
   {
      return m_address;
   }

   /**
    * @param m_address The m_address to set
    */
   public void setAddress(ContactAddress m_address)
   {
      this.m_address = m_address;
   }

   /**
    * @return The m_company
    */
   public String getCompany()
   {
      return m_company;
   }

   /**
    * @param m_company The m_company to set
    */
   public void setCompany(String m_company)
   {
      this.m_company = m_company;
   }

   /**
    * @return The m_emailAddress
    */
   public String getEmailAddress()
   {
      return m_emailAddress;
   }

   /**
    * @param address The m_emailAddress to set
    */
   public void setEmailAddress(String address)
   {
      m_emailAddress = address;
   }

   /**
    * @return The m_firstName
    */
   public String getFirstName()
   {
      return m_firstName;
   }

   /**
    * @param name The m_firstName to set
    */
   public void setFirstName(String name)
   {
      m_firstName = name;
   }

   /**
    * @return The m_lastName
    */
   public String getLastName()
   {
      return m_lastName;
   }

   /**
    * @param name The m_lastName to set
    */
   public void setLastName(String name)
   {
      m_lastName = name;
   }

   /**
    * @return The m_phoneNumber
    */
   public String getPhoneNumber()
   {
      return m_phoneNumber;
   }

   /**
    * @param number The m_phoneNumber to set
    */
   public void setPhoneNumber(String number)
   {
      m_phoneNumber = number;
   }

   /**
    * @return The m_title
    */
   public String getTitle()
   {
      return m_title;
   }

   /**
    * @param m_title The m_title to set
    */
   public void setTitle(String m_title)
   {
      this.m_title = m_title;
   }

   /**
    * Retrieves the acquirer relationship of the PCI Merchant.
    *
    * @return The relationship with the acquirer of the PCI Merchant.
    */
   public String getAcquirerRelationship()
   {
      return m_acquirerRelationship;
   }

   /**
    * Sets the PCI Merchants's acquirer relationship.
    *
    * @param relationship The relationship with the PCI Merchants's acquirer.
    */
   public void setAcquirerRelationship(String relationship)
   {
      m_acquirerRelationship = relationship;
   }

   /**
    * Retrieves the PCI Merchants's agent relationship.
    *
    * @return The PCI Merchants's agen relationship.
    */
   public String getAgentRelationship()
   {
      return m_agentRelationship;
   }

   /**
    * Sets the PCI Merchants's agent relationship.
    *
    * @param relationship The new value of PCI Merchants's agent relationship.
    */
   public void setAgentRelationship(String relationship)
   {
      m_agentRelationship = relationship;
   }

   /**
    * Retrieves the content generator for the DBAs associated with the PCI Merchants.
    *
    * @return The content generator of the PCI Merchants's DBAs.
    */
   public IContentGenerator getDBAGenerator()
   {
      return m_dba;
   }

   /**
    * Sets the PCI Merchants's DBA content generator.
    *
    * @param dbaContentGenerator The new value of the PCI Merchants's dba content generator.
    */
   public void setDBAGenerator(IContentGenerator dbaContentGenerator)
   {
      m_dba = dbaContentGenerator;
   }

   /**
    * Retrieves the PCI Merchants's eCommerce String.
    *
    * @return The value of the PCI Merchants's eCommerce.
    */
   public String getECommerce()
   {
      return m_eCommerce;
   }

   /**
    * Sets the PCI Merchants's eCommerce value.
    *
    * @param eCommerce The new value of the PCI Merchants's eCommerce.
    */
   public void setECommerce(String eCommerce)
   {
      m_eCommerce = eCommerce;
   }

   /**
    * Retrieves the PCI Merchants's grocery attribute.
    *
    * @return The grocery attribute of the PCI Merchants.
    */
   public String getGrocery()
   {
      return m_grocery;
   }

   /**
    * Sets the PCI Merchants's grocery value.
    *
    * @param grocery The new value for the PCI Merchants's grocery.
    */
   public void setGrocery(String grocery)
   {
      m_grocery = grocery;
   }

   /**
    * Retrieves the PCI Merchants's mail order.
    *
    * @return The value of the PCI Merchants's mail order.
    */
   public String getMailOrder()
   {
      return m_mailOrder;
   }

   /**
    * Sets the PCI Merchants's mail order.
    *
    * @param mailOrder The new value of the PCI Merchants's mail order.
    */
   public void setMailOrder(String mailOrder)
   {
      m_mailOrder = mailOrder;
   }

   /**
    * Retrieves the PCI Merchants's payment application.
    *
    * @return The value of the PCI Merchants's payment application.
    */
   public String getPaymentApplication()
   {
      return m_paymentApplication;
   }

   /**
    * Sets the PCI Merchants's payment application.
    *
    * @param application The new value of the PCI Merchants's payment application.
    */
   public void setPaymentApplication(String application)
   {
      m_paymentApplication = application;
   }

   /**
    * Retrieves the PCI Merchants's payment version.
    *
    * @return The value of the the PCI Merchants's payment version.
    */
   public String getPaymentVersion()
   {
      return m_paymentVersion;
   }

   /**
    * Sets the PCI Merchants's payment version.
    *
    * @param version The new value of the the PCI Merchants's payment version.
    */
   public void setPaymentVersion(String version)
   {
      m_paymentVersion = version;
   }

   /**
    * Retrieves the the PCI Merchants's petroleum value.
    *
    * @return The value of the PCI Merchants's petroleum.
    */
   public String getPetroleum()
   {
      return m_petroleum;
   }

   /**
    * Sets the PCI Merchants's petroleum value.
    *
    * @param petroleum The new value of the PCI Merchants's petroleum.
    */
   public void setPetroleum(String petroleum)
   {
      m_petroleum = petroleum;
   }

   /**
    * Retrieves the PCI Merchants's retail value.
    *
    * @return The value of the PCI Merchants's retail.
    */
   public String getRetail()
   {
      return m_retail;
   }

   /**
    * Sets the PCI Merchants's retail value.
    *
    * @param retail The new value of the PCI Merchants's retail.
    */
   public void setRetail(String retail)
   {
      m_retail = retail;
   }

   /**
    * Retrieves the PCI Merchants's telecommunication value.
    *
    * @return The value of the PCI Merchants's telecommunication.
    */
   public String getTelecommunication()
   {
      return m_telecommunication;
   }

   /**
    * Sets the PCI Merchants's telecommunication value.
    *
    * @param telecommunication The
    */
   public void setTelecommunication(String telecommunication)
   {
      m_telecommunication = telecommunication;
   }

   /**
    * Retrieves the PCI Merchants's travel value.
    *
    * @return The value of the PCI Merchants's travel.
    */
   public String getTravel()
   {
      return m_travel;
   }

   /**
    * Sets the PCI Merchants's travel value.
    *
    * @param travel The new value of the PCI Merchants's travel.
    */
   public void setTravel(String travel)
   {
      m_travel = travel;
   }

   /**
    * Retrieves the PCI Merchants's otherIndustries content generator.
    *
    * @return the value of the PCI Merchants's otherIndustries content generator.
    */
   public IContentGenerator getOtherIndustriesGenerator()
   {
      return m_otherIndustriesGenerator;
   }

   /**
    * Sets the PCI Merchants's otherIndustries content generator.
    *
    * @param otherIndustriesGenerator The new content generator for the PCI Merchants's otherTypes.
    */
   public void setOtherIndustriesGenerator(IContentGenerator otherIndustriesGenerator)
   {
      m_otherIndustriesGenerator = otherIndustriesGenerator;
   }

   /**
    * Tells whether all the attributes are null or not.
    */
   public boolean areAllNull()
   {
      return (m_acquirerRelationship == null &&
              m_agentRelationship == null &&
              m_dba == null &&
              m_eCommerce == null &&
              m_grocery == null &&
              m_mailOrder == null &&
              m_paymentApplication == null &&
              m_paymentVersion == null &&
              m_petroleum == null &&
              m_retail == null &&
              m_telecommunication == null &&
              m_travel == null &&
              m_otherIndustriesGenerator == null);
   }
   // ///////////////////////////////////////////////////////////////////////
   // Non-Public fields
   // ///////////////////////////////////////////////////////////////////////

   /** Represents the relationship with the PCI merchant acquirer. */
   private String m_acquirerRelationship;
   /** Represents the relationship with the agent. */
   private String m_agentRelationship;
   /** Represents the DBA. */
   private IContentGenerator m_dba;
   /** Represents the e-commerce associated with the merchant. */
   private String m_eCommerce;
   /** Represents the merchant's grocery. */
   private String m_grocery;
   /** Represents the merchant's mail order. */
   private String m_mailOrder;
   /** Represents the merchant's payment application. */
   private String m_paymentApplication;
   /** Represents the merchant's payment version. */
   private String m_paymentVersion;
   /** Represents the merchant's petroleum. */
   private String m_petroleum;
   /** Represents the merchant's retail. */
   private String m_retail;
   /** Represents the merchant's telecommunication. */
   private String m_telecommunication;
   /** Represents the merchant's travel. */
   private String m_travel;
   /** Represents the contentGenerator for the 'other-industries' element. */
   private IContentGenerator m_otherIndustriesGenerator;
   /** Represents the merchant's company name. */
   private String m_company;
   /** Represents the merchant's email Address. */
   private String m_emailAddress;
   /** Represents the merchant's first name. */
   private String m_firstName;
   /** Represents the merchant's last Name. */
   private String m_lastName;
   /** Represents the merchant's phone Number. */
   private String m_phoneNumber;
   /** Represents the merchant's title. */
   private String m_title;
   /** Represents the merchant's address. */
   private ContactAddress m_address;
}
