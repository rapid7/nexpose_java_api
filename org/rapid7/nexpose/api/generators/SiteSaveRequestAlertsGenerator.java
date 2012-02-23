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
 * Generates alerts content to add to a Site Save Request.
 *
 * @author Leonardo Varela
 */
public class SiteSaveRequestAlertsGenerator implements IContentGenerator
{
   /**
    * An Alert of type sysLog associated to this alerts generator for the site
    * save request.
    *
    * @author Leonardo Varela
    */
   public static class SysLogAlert
   {
      /**
       * Retrieves the server associated with the SysLog Alert
       *
       * @return the server associated with the SysLog Alert
       */
      public String getServer()
      {
         return m_server;
      }

      /**
       * Sets the server associated with the SysLog Alert
       *
       * @param server the server associated with the SysLog Alert
       */
      public void setServer(String server)
      {
         this.m_server = server;
      }

      /**
       * Retrieves the port associated with the SysLog Alert
       *
       * @return the port associated with the SysLog Alert
       */
      public String getPort()
      {
         return m_port;
      }

      /**
       * Sets the port associated with the SysLog Alert
       *
       * @param port the port associated with the SysLog Alert
       */
      public void setPort(String port)
      {
         this.m_port = port;
      }
      /**
       * Creates a new SysLog alert to be used in the site save request.
       *
       * @param server the server associated with the SysLog Alert
       * @param port the port associated with the SysLog Alert.
       */
      public SysLogAlert(String server, String port)
      {
         m_server = server;
         m_port = port;
      }
      /**The server associated with the SysLog Alert*/
      private String m_server;
      /**The port associated with the SysLog Alert*/
      private String m_port;
   }

   /**
    * An alert of type SNMP associated to this alerts generator for the site
    * save request.
    *
    * @author Leonardo Varela
    */
   public static class SNMPAlert
   {
      /////////////////////////////////////////////////////////////////////////
      // Public methods
      /////////////////////////////////////////////////////////////////////////

      /**
       * Retrieves the community associated with the SNMP Alert
       *
       * @return the community associated with the SNMP.
       */
      public String getCommunity()
      {
         return m_community;
      }

      /**
       * Sets the community associated with the SNMP Alert
       *
       * @param community the community associated with the SNMP Alert
       */
      public void setCommunity(String community)
      {
         this.m_community = community;
      }

      /**
       * Retrieves the server associated with the SNMP Alert
       *
       * @return the server associated with the SNMP Alert
       */
      public String getServer()
      {
         return m_server;
      }

      /**
       * Sets the server associated with the SNMP Alert
       *
       * @param server the server associated with the SNMP Alert
       */
      public void setServer(String server)
      {
         this.m_server = server;
      }

      /**
       * Retrieves the port associated with the SNMP Alert
       *
       * @return the port associated with the SNMP Alert
       */
      public String getPort()
      {
         return m_port;
      }

      /**
       * Sets the port associated with the SNMP Alert
       *
       * @param port the port associated with the SNMP Alert
       */
      public void setPort(String port)
      {
         this.m_port = port;
      }

      /**
       * Creates a new SNMP Alert to associate to the alert being generated.
       *
       * @param community the community associated with the SNMP alert
       * @param server the server of the SNMP
       * @param port the port of the SNMP
       */
      public SNMPAlert(String community, String server, String port)
      {
         m_community = community;
         m_server = server;
         m_port = port;
      }
      /////////////////////////////////////////////////////////////////////////
      // non-Public fields
      /////////////////////////////////////////////////////////////////////////

      /**The community associated with the SNMP Alert*/
      private String m_community;
      /**The server associated with the SNMP Alert*/
      private String m_server;
      /**The port associated with the SNMP Alert*/
      private String m_port;
      
   }
   /**
    * An alert of type SMTP associated to this alerts generator for the site
    * save request.
    *
    * @author Leonardo Varela
    */
   public static class SMTPAlert
   {
      /////////////////////////////////////////////////////////////////////////
      // Public methods
      /////////////////////////////////////////////////////////////////////////

      /**
       * Retrieves the community associated with the SMTP Alert
       *
       * @return the community associated with the SMTP.
       */
      public String getSender()
      {
         return m_sender;
      }

      /**
       * Sets the community associated with the SMTP Alert
       *
       * @param community the community associated with the SMTP Alert
       */
      public void setSender(String community)
      {
         this.m_sender = community;
      }

      /**
       * Retrieves the server associated with the SMTP Alert
       *
       * @return the server associated with the SMTP Alert
       */
      public String getServer()
      {
         return m_server;
      }

      /**
       * Sets the server associated with the SMTP Alert
       *
       * @param server the server associated with the SMTP Alert
       */
      public void setServer(String server)
      {
         this.m_server = server;
      }

      /**
       * Retrieves the port associated with the SMTP Alert
       *
       * @return the port associated with the SMTP Alert
       */
      public String getPort()
      {
         return m_port;
      }

      /**
       * Sets the port associated with the SMTP Alert
       *
       * @param port the port associated with the SMTP Alert
       */
      public void setPort(String port)
      {
         this.m_port = port;
      }

      /**
       * Creates a new SMTP Alert to associate to the alert being generated.
       *
       * @param sender the community associated with the SMTP alert
       * @param server the server of the SMTP
       * @param port the port of the SMTP
       * @param limitText the fact that we are limiting the text of the alert.
       */
      public SMTPAlert(String sender, String server, String port, String limitText)
      {
         m_sender = sender;
         m_server = server;
         m_port = port;
         m_limitText = limitText;
      }

      /**
       * Retrieves the limit Text of the SMTP Alert.
       *
       * @return the fact that the alert limits the text or not.
       */
      public String getLimitText()
      {
         return m_limitText;
      }

      /**
       * Sets the fact that the SMTP alert limits the text or not.
       *
       * @param text the fact to limit the text on the SMTP alert.
       */
      public void setLimitText(String text)
      {
         m_limitText = text;
      }

      /////////////////////////////////////////////////////////////////////////
      // non-Public fields
      /////////////////////////////////////////////////////////////////////////

      /**The sender associated with the SMTP Alert*/
      private String m_sender;
      /**The server associated with the SMTP Alert*/
      private String m_server;
      /**The port associated with the SMTP Alert*/
      private String m_port;
      /**Tells whether to limit to text or not*/
      private String m_limitText;
   }
   /**
    * Creates an alert instance for this class to generate.
    *
    * @author Leonardo Varela
    */
   public static class SiteSaveRequestAlert
   {
      /////////////////////////////////////////////////////////////////////////
      // Public methods
      /////////////////////////////////////////////////////////////////////////
      /**
       * Creates a new alert for the site save request.
       *
       * @param name the name of the alert
       * @param enabled the fact that the alert is enabled or not
       * @param maxAlerts the maximum number of alerts.
       * @param filterScanStart the filter for started scans.
       * @param filterScanStop the filter for stopped scans.
       * @param filterScanFailed the filter for failed scans.
       * @param filterSeverityThreshold the filter for vuln severity threshold
       * @param filterVulnConfirmed the filter for confirmed vulns
       * @param filterVulnUnconfirmed the filter for unconfirmed vulns
       */
      public SiteSaveRequestAlert(
         String name,
         String enabled,
         String maxAlerts,
         String filterScanStart,
         String filterScanStop,
         String filterScanFailed,
         String filterSeverityThreshold,
         String filterVulnConfirmed,
         String filterVulnUnconfirmed)
      {
         m_name = name;
         m_enabled = enabled;
         m_maxAlerts = maxAlerts;
         m_filterScanStart = filterScanStart;
         m_filterScanStop = filterScanStop;
         m_filterScanFailed = filterScanFailed;
         m_filterSeverityThreshold = filterSeverityThreshold;
         m_filterVulnConfirmed = filterVulnConfirmed;
         m_filterVulnUnconfirmed = filterVulnUnconfirmed;
      }
      /**
       * Retrieves the name of the alert.
       *
       * @return the name of the alert.
       */
      public String getName()
      {
         return m_name;
      }

      /**
       * Sets the name of the alert.
       *
       * @param name the name to set
       */
      public void setName(String name)
      {
         this.m_name = name;
      }

      /**
       * Retrieves the fact that the alert is enabled or not.
       *
       * @return true if the alert is enabled, false otherwise.
       */
      public String getEnabled()
      {
         return m_enabled;
      }

      /**
       * Sets the fact that the alert is enabled or not.
       *
       * @param enabled true to enable the alert, false to disable the alert
       */
      public void setEnabled(String enabled)
      {
         this.m_enabled = enabled;
      }

      /**
       * Retrieves the maximum number of alerts
       *
       * @return the maximum number of alerts.
       */
      public String getMaxAlerts()
      {
         return m_maxAlerts;
      }

      /**
       * Sets the maximum number of alerts.
       *
       * @param maxAlerts the maximum number of alerts to be set.
       */
      public void setMaxAlerts(String maxAlerts)
      {
         m_maxAlerts = maxAlerts;
      }

      /**
       * Retrieves the fact that an alert is filtered by starting a scan.
       *
       * @return true if it is filtered by a scan being started, false otherwise
       */
      public String getFilterScanStart()
      {
         return m_filterScanStart;
      }

      /**
       * Sets the fact that an alert is filtered by starting a scan.
       *
       * @param scanStart true to filter by a scan being started, false
       *        otherwise
       */
      public void setFilterScanStart(String scanStart)
      {
         m_filterScanStart = scanStart;
      }

      /**
       * Retrieves the fact that an alert is filtered by stopping a scan.
       *
       * @return true if the alert is filtered by a scan being stopped, false
       *         otherwise
       */
      public String getFilterScanStop()
      {
         return m_filterScanStop;
      }

      /**
       * Sets the fact that an alert is filtered by stopping a scan.
       *
       * @param scanStop true to filter by a scan being stopped, false
       *        otherwise.
       */
      public void setFilterScanStop(String scanStop)
      {
         m_filterScanStop = scanStop;
      }

      /**
       * Retrieves the fact that an alert is filtered by severity threshold.
       *
       * @return true if it is filtered by severity threshold, false otherwise.
       */
      public String getFilterSeverityThreshold()
      {
         return m_filterSeverityThreshold;
      }

      /**
       * Sets the filter severity threshold.
       *
       * @param severityThreshold tru to set it to filter by severity, false
       *        otherwise.
       */
      public void setFilterSeverityThreshold(String severityThreshold)
      {
         m_filterSeverityThreshold = severityThreshold;
      }

      /**
       * Sets the filter for a failed scan
       *
       * @return true if the alert is filtered by failing scans, false otherwise
       */
      public String getFilterScanFailed()
      {
         return m_filterScanFailed;
      }

      /**
       * Sets the fact that an alert is filtered by a failed scan.
       *
       * @param scanFailed true to filter the alert by a failed scan, false
       *        otherwise.
       */
      public void setFilterScanFailed(String scanFailed)
      {
         m_filterScanFailed = scanFailed;
      }

      /**
       * Retrieves the fact that an alert is filter by confirmed vulns.
       *
       * @return true if it is filtered, false otherwise.
       */
      public String getFilterVulnConfirmed()
      {
         return m_filterVulnConfirmed;
      }

      /**
       * Sets the fact that an alert is filtered by a confirmed vuln
       *
       * @param vulnConfirmed true to filter, false otherwise.
       */
      public void setFilterVulnConfirmed(String vulnConfirmed)
      {
         m_filterVulnConfirmed = vulnConfirmed;
      }

      /**
       * Retrieves the fact that an alert is filtered by an unconfirmed vuln
       *
       * @return true if it is filtered, false otherwise.
       */
      public String getFilterVulnUnconfirmed()
      {
         return m_filterVulnUnconfirmed;
      }

      /**
       * Sets a filter for unconfirmed vulnerabilities.
       *
       * @param vulnUnconfirmed true to set the filter, false otherwise.
       */
      public void setFilterVulnUnconfirmed(String vulnUnconfirmed)
      {
         m_filterVulnUnconfirmed = vulnUnconfirmed;
      }

      /**
       * Sets the SNMP alert information.
       *
       * @param alert the snmp alert to be set.
       */
      public void setSNMPAlert(SNMPAlert alert)
      {
         m_snmpAlert = alert;
      }

      /**
       * Retrieves the SNMP alert information.
       *
       * @return the snmp alert associated with the generator.
       */
      public SNMPAlert getSNMPAlert()
      {
         return m_snmpAlert;
      }

      /**
       * Sets the sysLog Alert information
       *
       *@param alert the syslog alert information.
       */
      public void setSysLogAlert(SysLogAlert alert)
      {
         m_sysLogAlert = alert;
      }
      /**
       * Retrives the sysLog Alert information
       *
       *@return the syslog alert information.
       */
      public SysLogAlert getSysLogAlert()
      {
         return m_sysLogAlert;
      }

      /**
       * Sets the recipients generator for this alert generator.
       *
       * @param recipientGenerator the recipient Generator to be associated with
       *        the alert.
       */
      public void setRecipientsGenerator(IContentGenerator recipientGenerator)
      {
         m_recipientsGenerator = recipientGenerator;
      }

      /**
       * Retrieves the recipients generator for this alert generator.
       *
       * @return the recipient Generator for this alert.
       */
      public IContentGenerator getRecipientsGenerator()
      {
         return m_recipientsGenerator;
      }

      /**
       * Sets the SMTP information associated with the alert if any.
       *
       * @param alert the SMTP alert information
       */
      public void setSMTPAlert(SMTPAlert alert)
      {
         m_smtpAlert = alert;
      }

      /**
       * Retrieves the SMTP information associated with the alert if any.
       *
       * @return the SMTP alert information
       */
      public SMTPAlert getSMTPAlert()
      {
         return m_smtpAlert;
      }

      /////////////////////////////////////////////////////////////////////////
      // non-Public fields
      /////////////////////////////////////////////////////////////////////////
      /**The information regarding the SMTP alert*/
      private SMTPAlert m_smtpAlert;
      /**The name of the alert*/
      private String m_name;
      /**The fact that the alert is enabled or not.*/
      private String m_enabled;
      /**The maximum number of alerts.*/
      private String m_maxAlerts;
      /**alert filter for started scans*/
      private String m_filterScanStart;
      /**alert filter for started scans*/
      private String m_filterScanStop;
      /**alert filter for vulnerability severity threshold */
      private String m_filterSeverityThreshold;
      /**alert filter for failed scans */
      private String m_filterScanFailed;
      /**alert filter for confirmed vulnerabilities*/
      private String m_filterVulnConfirmed;
      /**alert filter for unconfirmed vulnerabilities*/
      private String m_filterVulnUnconfirmed;
      /**the SNMP alert information if any. null for no SNMP alerts.*/
      private SNMPAlert m_snmpAlert;
      /**the SysLog alert information if any. null for no SNMP alerts.*/
      private SysLogAlert m_sysLogAlert;
      /**The generator for recipients*/
      private IContentGenerator m_recipientsGenerator;
   }
   /////////////////////////////////////////////////////////////////////////
   // Public methods
   /////////////////////////////////////////////////////////////////////////

   /**
    * Creates a new Alerts generator for the site save request.
    */
   public SiteSaveRequestAlertsGenerator()
   {
      m_alerts = new ArrayList<SiteSaveRequestAlert>();
   }

   /**
    * Knows how to print the xml output for hosts inside of a <Hosts> tag on the 
    * site save request.
    *
    * @see java.lang.Object#toString()
    */
   public String toString()
   {
      StringBuilder sb = new StringBuilder();
      Iterator<SiteSaveRequestAlert> itAlerts = m_alerts.iterator();
      while(itAlerts.hasNext())
      {
         SiteSaveRequestAlert alert = itAlerts.next();
         sb.append("<Alert name=\"");
         sb.append(StringUtils.xmlEscape(alert.getName()));
         sb.append("\" enabled=\"");
         sb.append(StringUtils.xmlEscape(alert.getEnabled()));
         sb.append("\" maxAlerts=\"");
         sb.append(StringUtils.xmlEscape(alert.getMaxAlerts()));
         sb.append("\">");
         sb.append("<scanFilter scanStart=\"");
         sb.append(StringUtils.xmlEscape(alert.getFilterScanStart()));
         sb.append("\" scanStop=\"");
         sb.append(StringUtils.xmlEscape(alert.getFilterScanStop()));
         sb.append("\" scanFailed=\"");
         sb.append(StringUtils.xmlEscape(alert.getFilterScanFailed()));
         sb.append("\"/>");
         sb.append("<vulnFilter severityThreshold=\"");
         sb.append(StringUtils.xmlEscape(alert.getFilterSeverityThreshold()));
         sb.append("\" confirmed=\"");
         sb.append(StringUtils.xmlEscape(alert.getFilterVulnConfirmed()));
         sb.append("\" unconfirmed=\"");
         sb.append(StringUtils.xmlEscape(alert.getFilterVulnUnconfirmed()));
         sb.append("\"/>");
         if (alert.getSMTPAlert() != null)
         {
            SMTPAlert smtpInfo = alert.getSMTPAlert();
            sb.append("<smtpAlert sender=\"");
            sb.append(StringUtils.xmlEscape(smtpInfo.getSender()));
            sb.append("\" server=\"");
            sb.append(StringUtils.xmlEscape(smtpInfo.getServer()));
            sb.append("\" port=\"");
            sb.append(StringUtils.xmlEscape(smtpInfo.getPort()));
            sb.append("\" limitText=\"");
            sb.append(StringUtils.xmlEscape(smtpInfo.getLimitText()));
            sb.append("\">");
            if (alert.getRecipientsGenerator() != null )
               sb.append(alert.getRecipientsGenerator().toString());
            sb.append("</smtpAlert>");
         }
         else if (alert.getSNMPAlert() != null)
         {
            SNMPAlert snmpInfo = alert.getSNMPAlert();
            sb.append("<snmpAlert community=\"");
            sb.append(StringUtils.xmlEscape(snmpInfo.getCommunity()));
            sb.append("\" server=\"");
            sb.append(StringUtils.xmlEscape(snmpInfo.getServer()));
            sb.append("\" port=\"");
            sb.append(StringUtils.xmlEscape(snmpInfo.getPort()));
            sb.append("\"/>");
         }
         else if (alert.getSysLogAlert() != null)
         {
            SysLogAlert sysLogInfo = alert.getSysLogAlert();
            sb.append("<sysLogAlert server=\"");
            sb.append(StringUtils.xmlEscape(sysLogInfo.getServer()));
            sb.append("\" port=\"");
            sb.append(StringUtils.xmlEscape(sysLogInfo.getPort()));
            sb.append("\"/>");
         }
         sb.append("</Alert>");
      }
      return sb.toString();
   }

   /* (non-Javadoc)
    * @see org.rapid7.nexpose.api.IContentGenerator#setContents(org.w3c.dom.Element)
    */
   @SuppressWarnings("unchecked")
   @Override
   public void setContents(Element contents)
   {
      try
      {
         final NodeList alerts = (NodeList) XPathFactory.newInstance().newXPath().evaluate("Alert", contents, XPathConstants.NODESET);
         for (int i = 0; i < alerts.getLength(); i++)
         {
            Element elementAlert = (Element) alerts.item(i);
            String alertName = elementAlert.getAttribute("name");
            String enabled = elementAlert.getAttribute("enabled");
            String maxAlerts = elementAlert.getAttribute("maxAlerts");
            String scanFilterScanStart = elementAlert.getAttribute("scanFilterScanStart");
            String scanFilterScanStop = elementAlert.getAttribute("scanFilterScanStop");
            String scanFilterScanFailed = elementAlert.getAttribute("scanFilterScanFailed");
            String vulnFilterSeverityThreshold = elementAlert.getAttribute("vulnFilterSeverityThreshold");
            String vulnFilterConfirmed = elementAlert.getAttribute("vulnFilterConfirmed");
            String vulnFilterUnconfirmed = elementAlert.getAttribute("vulnFilterUnconfirmed");
            SiteSaveRequestAlert alert = new SiteSaveRequestAlert(
               alertName,
               enabled,
               maxAlerts,
               scanFilterScanStart,
               scanFilterScanStop,
               scanFilterScanFailed,
               vulnFilterSeverityThreshold,
               vulnFilterConfirmed,
               vulnFilterUnconfirmed);
            final NodeList alertParams = (NodeList) XPathFactory.newInstance().newXPath().evaluate("AlertParam", elementAlert, XPathConstants.NODESET);
            for (int j = 0; j < alertParams.getLength(); j++)
            {
               Element param = (Element) alertParams.item(i);
               if (param.hasAttribute("generator"))
               {
                  try
                  {
                     Class<IContentGenerator> generator = (Class<IContentGenerator>) Class.forName("org.rapid7.nexpose.api.generators."+param.getAttribute("generator"));
                     SMTPAlertRecipientGenerator recipientGenerator = (SMTPAlertRecipientGenerator) generator.newInstance(); 
                     recipientGenerator.setContents(param);
                     alert.setRecipientsGenerator(recipientGenerator);
                  }
                  catch (ClassNotFoundException e)
                  {
                     throw new RuntimeException("Could not parse the Contents Generator: " + e.toString());
                  }
                  catch (InstantiationException e)
                  {
                     throw new RuntimeException("Could not Instantiate the Contents Generator: " + e.toString());
                  }
                  catch (IllegalAccessException e)
                  {
                     throw new RuntimeException("Could not access the Contents Generator: " + e.toString());
                  }
               }
            }
            final Element smtpAlert = (Element) XPathFactory.newInstance().newXPath().evaluate("smtpAlert", elementAlert, XPathConstants.NODE);
            if (smtpAlert != null)
            {
               String smtpAlertSender = smtpAlert.getAttribute("sender");
               String smtpAlertServer = smtpAlert.getAttribute("server");
               String smtpAlertPort = smtpAlert.getAttribute("port");
               String smtpAlertLimitText = smtpAlert.getAttribute("limitText");
               alert.setSMTPAlert(new SMTPAlert(smtpAlertSender, smtpAlertServer, smtpAlertPort, smtpAlertLimitText));
            }
            final Element snmpAlert = (Element) XPathFactory.newInstance().newXPath().evaluate("snmpAlert", elementAlert, XPathConstants.NODE);
            if (snmpAlert != null)
            {
               String snmpAlertCommunity = snmpAlert.getAttribute("community");
               String snmpAlertServer = snmpAlert.getAttribute("server");
               String snmpAlertPort = snmpAlert.getAttribute("port");
               alert.setSNMPAlert(new SNMPAlert(snmpAlertCommunity, snmpAlertServer, snmpAlertPort));
            }
            final Element sysLogAlert = (Element) XPathFactory.newInstance().newXPath().evaluate("sysLogAlert", elementAlert, XPathConstants.NODE);
            if (sysLogAlert != null)
            {
               String sysLogAlertServer = sysLogAlert.getAttribute("server");
               String sysLogAlertPort = sysLogAlert.getAttribute("port");
               alert.setSysLogAlert(new SysLogAlert(sysLogAlertServer, sysLogAlertPort));
            }
            m_alerts.add(alert);
         }
      }
      catch (XPathExpressionException e)
      {
         System.out.println("Could not parse the Contents Generator for"
            + " SaveSiteRequestAlertsGenerator: " + e.toString());
         throw new RuntimeException("The alerts could not be generated: "
            + e.toString());
      }
   }

   /**
    * Retrieves the list of alerts associated with the Site Save Request
    *
    * @return the alerts associated with the Site Save Request
    */
   public List<SiteSaveRequestAlert> getAlerts()
   {
      return m_alerts;
   }

   /**
    * Sets the list of alerts associated with the Site Save Request.
    * @param alerts the alerts to be set
    */
   public void setHosts(List<SiteSaveRequestAlert> alerts)
   {
      this.m_alerts = alerts;
   }
   /////////////////////////////////////////////////////////////////////////
   // non-Public fields
   /////////////////////////////////////////////////////////////////////////

   /**The hosts associated with the*/
   private List<SiteSaveRequestAlert> m_alerts;
}
