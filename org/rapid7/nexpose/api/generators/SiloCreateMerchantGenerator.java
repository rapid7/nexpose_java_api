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
import org.rapid7.nexpose.api.domain.SiloConfigPCIMerchant;
import org.rapid7.nexpose.utils.StringUtils;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Element;

/**
 * Generates Silo PCI Merchant element tags inside of the SiloCreate Silo Config
 *
 * @author Leonardo Varela
 */
public class SiloCreateMerchantGenerator implements IContentGenerator
{

   // ///////////////////////////////////////////////////////////////////////
   // Public methods
   // ///////////////////////////////////////////////////////////////////////

   /**
    * Creates a new Merchant generator for the silo config.
    */
   public SiloCreateMerchantGenerator()
   {
      m_pciMerchant = new SiloConfigPCIMerchant();
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
      sb.append("<Merchant ");
      IContentGenerator dbaGenerator = m_pciMerchant.getDBAGenerator();
      IContentGenerator otherIndustriesGenerator = m_pciMerchant.getOtherIndustriesGenerator();
      sb.append("acquirer-relationship=\"");
      sb.append(StringUtils.xmlEscape(m_pciMerchant.getAcquirerRelationship()));
      sb.append("\" agent-relationship=\"");
      sb.append(StringUtils.xmlEscape(m_pciMerchant.getAgentRelationship()));
      sb.append("\" ecommerce=\"");
      sb.append(StringUtils.xmlEscape(m_pciMerchant.getECommerce()));
      sb.append("\" grocery=\"");
      sb.append(StringUtils.xmlEscape(m_pciMerchant.getGrocery()));
      sb.append("\" mail-order=\"");
      sb.append(StringUtils.xmlEscape(m_pciMerchant.getMailOrder()));
      sb.append("\" payment-application=\"");
      sb.append(StringUtils.xmlEscape(m_pciMerchant.getPaymentApplication()));
      sb.append("\" payment-version=\"");
      sb.append(StringUtils.xmlEscape(m_pciMerchant.getPaymentVersion()));
      sb.append("\" petroleum=\"");
      sb.append(StringUtils.xmlEscape(m_pciMerchant.getPetroleum()));
      sb.append("\" retail=\"");
      sb.append(StringUtils.xmlEscape(m_pciMerchant.getRetail()));
      sb.append("\" telecommunication=\"");
      sb.append(StringUtils.xmlEscape(m_pciMerchant.getTelecommunication()));
      sb.append("\" travel=\"");
      sb.append(StringUtils.xmlEscape(m_pciMerchant.getTravel()));
      sb.append("\" company=\"");
      sb.append(StringUtils.xmlEscape(m_pciMerchant.getCompany()));
      sb.append("\" email-address=\"");
      sb.append(StringUtils.xmlEscape(m_pciMerchant.getEmailAddress()));
      sb.append("\" first-name=\"");
      sb.append(StringUtils.xmlEscape(m_pciMerchant.getFirstName()));
      sb.append("\" last-name=\"");
      sb.append(StringUtils.xmlEscape(m_pciMerchant.getLastName()));
      sb.append("\" phone-number=\"");
      sb.append(StringUtils.xmlEscape(m_pciMerchant.getPhoneNumber()));
      sb.append("\" title=\"");
      sb.append(StringUtils.xmlEscape(m_pciMerchant.getTitle()));
      sb.append("\">");
      ContactAddress address = m_pciMerchant.getAddress();
      if (address != null)
      {
         sb.append("<Address city=\"");
         sb.append(StringUtils.xmlEscape(address.getCity()));
         sb.append("\" country=\"");
         sb.append(StringUtils.xmlEscape(address.getCountry()));
         sb.append("\" line1=\"");
         sb.append(StringUtils.xmlEscape(address.getLine1()));
         sb.append("\" line2=\"");
         sb.append(StringUtils.xmlEscape(address.getLine2()));
         sb.append("\" state=\"");
         sb.append(StringUtils.xmlEscape(address.getState()));
         sb.append("\" zip=\"");
         sb.append(StringUtils.xmlEscape(address.getZip()));
         sb.append("\"/>");
      }
      if (dbaGenerator != null)
      {
         sb.append(dbaGenerator.toString());
      }
      if (otherIndustriesGenerator != null)
      {
         sb.append(otherIndustriesGenerator.toString());
      }
      sb.append("</Merchant>");
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
         final Element elementMerchant = (Element) XPathFactory.newInstance().newXPath().evaluate("Merchant", contents,
               XPathConstants.NODE);
         m_pciMerchant = new SiloConfigPCIMerchant();
         m_pciMerchant.setAcquirerRelationship(elementMerchant.getAttribute("acquirer-relationship"));
         m_pciMerchant.setAgentRelationship(elementMerchant.getAttribute("agent-relationship"));
         m_pciMerchant.setECommerce(elementMerchant.getAttribute("ecommerce"));
         m_pciMerchant.setGrocery(elementMerchant.getAttribute("grocery"));
         m_pciMerchant.setMailOrder(elementMerchant.getAttribute("mail-order"));
         m_pciMerchant.setPaymentApplication(elementMerchant.getAttribute("payment-application"));
         m_pciMerchant.setPaymentVersion(elementMerchant.getAttribute("payment-version"));
         m_pciMerchant.setPetroleum(elementMerchant.getAttribute("petroleum"));
         m_pciMerchant.setRetail(elementMerchant.getAttribute("retail"));
         m_pciMerchant.setTelecommunication(elementMerchant.getAttribute("telecommunication"));
         m_pciMerchant.setTravel(elementMerchant.getAttribute("travel"));
         m_pciMerchant.setCompany(elementMerchant.getAttribute("company"));
         m_pciMerchant.setEmailAddress(elementMerchant.getAttribute("email-address"));
         m_pciMerchant.setFirstName(elementMerchant.getAttribute("first-name"));
         m_pciMerchant.setLastName(elementMerchant.getAttribute("last-name"));
         m_pciMerchant.setPhoneNumber(elementMerchant.getAttribute("phone-number"));
         m_pciMerchant.setTitle(elementMerchant.getAttribute("title"));
         final Element addressElement = (Element) XPathFactory.newInstance().newXPath().evaluate("Address", elementMerchant,
               XPathConstants.NODE);
         ContactAddress address = null;
         if (addressElement != null)
         {
            address = new ContactAddress();
            address.setCity(addressElement.getAttribute("city"));
            address.setCountry(addressElement.getAttribute("country"));
            address.setLine1(addressElement.getAttribute("line1"));
            address.setLine2(addressElement.getAttribute("line2"));
            address.setState(addressElement.getAttribute("state"));
            address.setZip(addressElement.getAttribute("zip"));
         }
         m_pciMerchant.setAddress(address);
         final Element dbasElement = (Element) XPathFactory.newInstance().newXPath().evaluate("DBAs", elementMerchant,
               XPathConstants.NODE);
         if (dbasElement != null)
         {
            SiloConfigDBAGenerator dbaGenerator = new SiloConfigDBAGenerator();
            dbaGenerator.setContents(dbasElement);
            m_pciMerchant.setDBAGenerator(dbaGenerator);
         }
         final Element otherIndustries = (Element) XPathFactory.newInstance().newXPath().evaluate("Industries",
               elementMerchant, XPathConstants.NODE);
         if (otherIndustries != null)
         {
            SiloConfigOtherIndustriesGenerator industryGenerator = new SiloConfigOtherIndustriesGenerator();
            industryGenerator.setContents(otherIndustries);
            m_pciMerchant.setOtherIndustriesGenerator(industryGenerator);
         }
      }
      catch (XPathExpressionException e)
      {
         System.out.println("Could not parse the generator for SiloCreateMerchantGenerator: " + e.toString());
         throw new RuntimeException("The PCI Merchant could not be generated: " + e.toString());
      }
   }

   /**
    * Retrieves the PCI MErchant associated with the Silo Create config.
    *
    * @return the PCI Merchant associated with the Silo Create config.
    */
   public SiloConfigPCIMerchant getPCIMerchant()
   {
      return m_pciMerchant;
   }

   /**
    * Sets the PCI MErchant associated with the Silo Create config.
    *
    * @param merchat The PCI MErchant to be set.
    */
   public void setSilos(SiloConfigPCIMerchant merchant)
   {
      m_pciMerchant = merchant;
   }

   // ///////////////////////////////////////////////////////////////////////
   // non-Public fields
   // ///////////////////////////////////////////////////////////////////////

   /** The Merchant associated with the silo configuration. */
   private SiloConfigPCIMerchant m_pciMerchant;
}
