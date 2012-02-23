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
 * Generates Sites content to add to a Save Engine Request.
 *
 * @author Leonardo Varela
 */
public class AssetGroupSaveRequestDevicesGenerator implements IContentGenerator
{
   /**
    * Represents a site contained in an Engine Save Request 
    * @author Leonardo Varela
    */
   public static class AssetGroupDevice
   {
      /////////////////////////////////////////////////////////////////////////
      // Public methods
      /////////////////////////////////////////////////////////////////////////
   
      /**
       * Creates a new device associated to an asset group save request.
       *
       * @param id the id of the device.
       * @param siteId the id of the site that the device is associated with.
       * @param address the address of the device.
       * @param riskFactor the risk factor assciated with the device.
       * @param riskScore the risk score associated with the device.
       * @param description the description assciated with the device.
       */
      public AssetGroupDevice(String id, String siteId, String address, String riskFactor, String riskScore, String description )
      {
         m_deviceId = id;
         m_siteId = siteId;
         m_address = address;
         m_riskFactor = riskFactor;
         m_riskScore = riskScore;
         m_description = description;
      }

      /**
       * @return the devices' site's id
       */
      public String getSiteId()
      {
         return m_siteId;
      }

      /**
       * @param siteId the devices' siteId to set
       */
      public void setSiteId(String siteId)
      {
         this.m_siteId = siteId;
      }

      /**
       * @return the device id
       */
      public String getDeviceId()
      {
         return m_deviceId;
      }

      /**
       * @param deviceId the deviceId to set
       */
      public void setDeviceId(String deviceId)
      {
         this.m_deviceId = deviceId;
      }

      /**
       * @return the address set for the device
       */
      public String getAddress()
      {
         return m_address;
      }

      /**
       * @param address the address to set for the device.
       */
      public void setAddress(String address)
      {
         this.m_address = address;
      }

      /**
       * @return the risk Factor of the device
       */
      public String getRiskFactor()
      {
         return m_riskFactor;
      }

      /**
       * @param factor the risk Factor to set
       */
      public void setRiskFactor(String factor)
      {
         m_riskFactor = factor;
      }

      /**
       * @return the risk Score of the device
       */
      public String getRiskScore()
      {
         return m_riskScore;
      }

      /**
       * @param score the risk Score to set
       */
      public void setRiskScore(String score)
      {
         m_riskScore = score;
      }

      /**
       * @return the description of the device
       */
      public String getDescription()
      {
         return m_description;
      }

      /**
       * @param description the description of the device to set
       */
      public void setDescription(String description)
      {
         this.m_description = description;
      }
      /////////////////////////////////////////////////////////////////////////
      // non-Public fields
      /////////////////////////////////////////////////////////////////////////

      /**The id of the site associated to the device*/
      private String m_siteId;
      /**The id of the device*/
      private String m_deviceId;
      /**The address associated with the device*/
      private String m_address;
      /**The risk factor associated with the device*/
      private String m_riskFactor;
      /**The risk score associated with the device*/
      private String m_riskScore;
      /**The description associated with the device*/
      private String m_description;
   }
   /////////////////////////////////////////////////////////////////////////
   // Public methods
   /////////////////////////////////////////////////////////////////////////

   /**
    * Creates a new device generator for the asset group save request.
    */
   public AssetGroupSaveRequestDevicesGenerator()
   {
      m_devices = new ArrayList<AssetGroupDevice>();
   }

   /* (non-Javadoc)
    * @see java.lang.Object#toString()
    */
   public String toString()
   {
      StringBuilder sb = new StringBuilder();
      Iterator<AssetGroupDevice> itSites = m_devices.iterator();
      while(itSites.hasNext())
      {
         AssetGroupDevice device = itSites.next();
         if (device.getDeviceId() == null)
            throw new RuntimeException("Error parsing devices generator,"
               + "a device id cannot accept null." + device.toString());
         sb.append("<device id=\"");
         sb.append(StringUtils.xmlEscape(device.getDeviceId()));
         sb.append("\"");
         if (device.getSiteId() != null)
         {
            sb.append(" site-id=\"");
            sb.append(StringUtils.xmlEscape(device.getSiteId()));
            sb.append("\"");
         }
         if (device.getAddress() != null)
         {
            sb.append(" address=\"");
            sb.append(StringUtils.xmlEscape(device.getAddress()));
            sb.append("\"");
         }
         if (device.getRiskFactor() != null)
         {
            sb.append(" riskfactor=\"");
            sb.append(StringUtils.xmlEscape(device.getRiskFactor()));
            sb.append("\"");
         }
         if (device.getRiskScore() != null)
         {
            sb.append(" riskscore=\"");
            sb.append(StringUtils.xmlEscape(device.getRiskScore()));
            sb.append("\"");
         }
         if (device.getDescription() != null)
         {
            sb.append(" description=\"");
            sb.append(StringUtils.xmlEscape(device.getDescription()));
            sb.append("\"");
         }
         sb.append(">");
         sb.append("</device>");
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
         final NodeList devices = (NodeList) XPathFactory.newInstance().newXPath().evaluate("device", contents, XPathConstants.NODESET);
         for (int i = 0; i < devices.getLength(); i++)
         {
            Element elementDevice = (Element) devices.item(i);
            AssetGroupDevice device = new AssetGroupDevice(
               elementDevice.getAttribute("id"),
               elementDevice.getAttribute("site-id"),
               elementDevice.getAttribute("address"),
               elementDevice.getAttribute("riskfactor"),
               elementDevice.getAttribute("riskscore"),
               elementDevice.getAttribute("description"));
            m_devices.add(device);
         }
      }
      catch (XPathExpressionException e)
      {
         System.out.println("Could not parse the Contents Generator for"
            + " AssetGroupSaveRequestGenerator: " + e.toString());
         throw new RuntimeException("The devices could not be generated: "
            + e.toString());
      }
   }

   /**
    * @return the devices associated with the asset group
    */
   public List<AssetGroupDevice> getDevices()
   {
      return m_devices;
   }

   /**
    * @param devices the devices to be set
    */
   public void setDevices(List<AssetGroupDevice> devices)
   {
      this.m_devices = devices;
   }
   /////////////////////////////////////////////////////////////////////////
   // non-Public fields
   /////////////////////////////////////////////////////////////////////////

   /**The list of devices associated to an asset group save request.*/
   private List<AssetGroupDevice> m_devices;
}
