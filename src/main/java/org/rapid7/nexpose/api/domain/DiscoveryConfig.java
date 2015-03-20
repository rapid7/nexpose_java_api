/***************************************************************************
 * COPYRIGHT (C) 2015, Rapid7 LLC, Boston, MA, USA.
 * All rights reserved. This material contains unpublished, copyrighted
 * work including confidential and proprietary information of Rapid7.
 **************************************************************************/

package org.rapid7.nexpose.api.domain;

import org.rapid7.nexpose.api.APIException;
import org.rapid7.nexpose.api.BaseElement;
import org.w3c.dom.Element;

/**
 * Holds a discovery configuration
 */
public class DiscoveryConfig extends BaseElement
{
   public DiscoveryConfig(Element element)
      throws APIException
   {
      setResponseElement(element);
      m_address = getString("address");
      m_connectionStatus = getString("connection-status");
      m_engineID = getInt("engine-id");
      m_configID = getLong("id");
      m_name = getString("name");
      m_port = getInt("port");
      m_protocol = getString("protocol");
      m_userName = getString("user-name");

   }
   public String getAddress()
   {
      return m_address;
   }
   public String getConnectionStatus()
   {
      return m_connectionStatus;
   }
   public int getEngineID()
   {
      return m_engineID;
   }
   public long getConfigID()
   {
      return m_configID;
   }
   public String getName()
   {
      return m_name;
   }
   public int getPort()
   {
      return m_port;
   }
   public String getProtocol()
   {
      return m_protocol;
   }
   public String getUserName()
   {
      return m_userName;
   }
   private String m_address;
   private String m_connectionStatus;
   private int m_engineID;
   private long m_configID;
   private String m_name;
   private int m_port;
   private String m_protocol;
   private String m_userName;
}