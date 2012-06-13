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
 * Generates devices content to add to a SiteDevicesScanRequest.
 *
 * @author Murali Rongali.
 */
public class SiteDevicesScanRequestDevicesGenerator implements IContentGenerator
{
   /**
    * Represents a devices contained in a Site Devices Scan Request
    *
    * @author Murali Rongali
    */
   public static class SiteDevicesScanRequestDevice
   {
      /////////////////////////////////////////////////////////////////////////
      // Public methods
      /////////////////////////////////////////////////////////////////////////
   
      /**
       * Creates a new site devices scan request devices.
       *
       * @param address the address of the device.
       * @param deviceId the id of the device.
       * @param riskFactor the riskFactor of the device.
       * @param riskscore the riskScore of the device.
       * @param description the description of the device scan.
       */
      public SiteDevicesScanRequestDevice(String address, String deviceId, String riskFactor, String riskscore, String description)
      {
         m_address = address;
         m_deviceId = deviceId;
         m_riskFactor = riskFactor;
         m_riskScore = riskscore;
         m_description = description;
      }
   
      /**
       * Retrieves the address of the device.
       *
       * @return the address of the device.
       */
      public String getAddress()
      {
         return m_address;
      }
   
      /**
       * Sets the address of the device.
       *
       * @param address the address of the device
       */
      public void setAddress(String address)
      {
         m_address = address;
      }
   
      /**
       * Retrieves the Id of the device.
       *
       * @return the Id of the device.
       */
      public String getDeviceId()
      {
         return m_deviceId;
      }
   
      /**
       * Sets the Id of the device.
       *
       * @param Id the address of the device
       */
      public void setDeviceId(String id)
      {
         m_deviceId = id;
      }
   
      /**
       * Retrieves the m_riskFactor of the device.
       *
       * @return the m_riskFactor of the device.
       */
      public String getRiskFactor()
      {
         return m_riskFactor;
      }
   
      /**
       * Sets the m_riskFactor of the device.
       *
       * @param m_riskFactor the m_riskFactor of the device.
       */
      public void setRiskFactor(String riskFactor)
      {
         m_riskFactor = riskFactor;
      }
   
      /**
       * Retrieves the riskScore of the device.
       *
       * @return the riskScore of the device.
       */
      public String getRiskScore()
      {
         return m_riskScore;
      }
   
      /**
       * Sets the riskScore of the device.
       *
       * @param riskScore the riskScore of the device.
       */
      public void setRiskScore(String riskScore)
      {
         m_riskScore = riskScore;
      }
   
      /**
       * Retrieves the description of the device scan.
       *
       * @return the description of the device scan.
       */
      public String getDescription()
      {
         return m_description;
      }
   
      /**
       * Sets the description of the device scan.
       *
       * @param description the description of the device scan.
       */
      public void setDescription(String description)
      {
         m_description = description;
      }
   
      /////////////////////////////////////////////////////////////////////////
      // non-Public fields
      /////////////////////////////////////////////////////////////////////////
   
      /**The address of the device*/
      private String m_address;
      /**The id of the device*/
      private String m_deviceId;
      /**The risk factor of the device*/
      private String m_riskFactor;
      /**The risk score of the device*/
      private String m_riskScore;
      /**The description of the device scan*/
      private String m_description;
   }

   /////////////////////////////////////////////////////////////////////////
   // Public methods
   /////////////////////////////////////////////////////////////////////////

   /**
    * Creates a new devices generator for the site devices scan request.
    */
   public SiteDevicesScanRequestDevicesGenerator()
   {
      m_devices = new ArrayList<SiteDevicesScanRequestDevice>();
   }

   /**
    * Knows how to create devices inside of a site device scan request.
    *
    * @see java.lang.Object#toString()
    */
   @Override
   public String toString()
   {
      StringBuilder sb = new StringBuilder();
      Iterator<SiteDevicesScanRequestDevice> itRanges = m_devices.iterator();
      while(itRanges.hasNext())
      {
         SiteDevicesScanRequestDevice device = itRanges.next();
         sb.append("<Device address=\"");
         sb.append(StringUtils.xmlEscape(device.getAddress()));
         sb.append("\" id=\"");
         sb.append(StringUtils.xmlEscape(device.getDeviceId()));
         sb.append("\" riskfactor=\"");
         sb.append(StringUtils.xmlEscape(device.getRiskFactor()));
         sb.append("\" riskscore=\"");
         sb.append(StringUtils.xmlEscape(device.getRiskScore()));
         sb.append("\" description=\"");
         sb.append(StringUtils.xmlEscape(device.getDescription()));
         sb.append(">");
         sb.append("</Device>");
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
         final NodeList ranges = (NodeList) XPathFactory.newInstance().newXPath().evaluate("Device", contents, XPathConstants.NODESET);
         for (int i = 0; i < ranges.getLength(); i++)
         {
            Element elementDevice = (Element) ranges.item(i);
            SiteDevicesScanRequestDevice device =
               new SiteDevicesScanRequestDevice(
                  elementDevice.getAttribute("address"),
                  elementDevice.getAttribute("id"),
                  elementDevice.getAttribute("riskfactor"),
                  elementDevice.getAttribute("riskscore"),
                  elementDevice.getAttribute("description")
               );
            m_devices.add(device);
         }
      }
      catch (XPathExpressionException e)
      {
         System.out.println("Could not parse the Contents Generator for"
            + "SiteDevicesScanRequestDevicesGenerator: " + e.toString());
         throw new RuntimeException("The devices could not be generated: "
            + e.toString());
      }
   }

   /**
    * Retrieves a list of devices associated with the site device scan request.
    *
    * @return the list of devices associated with the site device scan request.
    */
   public List<SiteDevicesScanRequestDevice> getDevices()
   {
      return m_devices;
   }

   /**
    * Sets the list of devices associated to the site device scan request.
    *
    * @param devices the devices to be set.
    */
   public void setDevices(List<SiteDevicesScanRequestDevice> devices)
   {
      m_devices = devices;
   }

   /////////////////////////////////////////////////////////////////////////
   // non-Public fields
   /////////////////////////////////////////////////////////////////////////
   /**The list of devices associated with this generator.*/
   private List<SiteDevicesScanRequestDevice> m_devices;
}