/***************************************************************************
 * COPYRIGHT (C) 2015, Rapid7 LLC, Boston, MA, USA.
 * All rights reserved. This material contains unpublished, copyrighted
 * work including confidential and proprietary information of Rapid7.
 **************************************************************************/

package org.rapid7.nexpose.api.domain;

import org.rapid7.nexpose.api.APIException;
import org.rapid7.nexpose.api.BaseElement;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import java.util.ArrayList;
import java.util.List;

/**
 * Scan summary object
 */
public class ScanSummary extends BaseElement
{
   public ScanSummary(Element element) throws APIException
   {
      setResponseElement(element);
      m_startTime = getString("startTime");
      m_endTime = getString("endTime");
      m_engineID = getInt("engine-id");
      m_name = getString("name");
      m_scanID = getLong("scan-id");
      m_siteID = getLong("site-id");
      m_status = getString("status");
      NodeList children = element.getChildNodes();
      for (int i = 0; i < children.getLength(); i++)
      {
         Node child = children.item(i);
         String name = child.getNodeName();
         if (name.equals("tasks"))
         {
            setResponseElement((Element) child);
            m_activeTasks = getInt("active");
            m_completedTasks = getInt("completed");
            m_pendingTasks = getInt("pending");
         }
         else if (name.equals("nodes"))
         {
            setResponseElement((Element) child);
            m_deadNodes = getInt("dead");
            m_filteredNodes = getInt("filtered");
            m_liveNodes = getInt("live");
            m_otherNodes = getInt("other");
            m_unresolvedNodes = getInt("unresolved");
         }
         else if (name.equals("vulnerabilities"))
         {
            setResponseElement((Element) child);
            int count = getInt("count");
            String status = getString("status");
            int severity = -1;
            if (status.startsWith("vuln"))
               severity = getInt("severity");
            Vulnerabilities vulnerability = new Vulnerabilities(count, severity, status);
            m_vulnerabilities.add(vulnerability);
         }
      }
   }
   public String getStartTime()
   {
      return m_startTime;
   }
   public String getEndTime()
   {
      return m_endTime;
   }
   public int getEngineID()
   {
      return m_engineID;
   }
   public String getName()
   {
      return m_name;
   }
   public long getScanID()
   {
      return m_scanID;
   }
   public long getSiteID()
   {
      return m_siteID;
   }
   public String getStatus()
   {
      return m_status;
   }
   public int getActiveTasks()
   {
      return m_activeTasks;
   }
   public int getCompletedTasks()
   {
      return m_completedTasks;
   }
   public int getPendingTasks()
   {
      return m_pendingTasks;
   }
   public int getDeadNodes()
   {
      return m_deadNodes;
   }
   public int getFilteredNodes()
   {
      return m_filteredNodes;
   }
   public int getLiveNodes()
   {
      return m_liveNodes;
   }
   public int getOtherNodes()
   {
      return m_otherNodes;
   }
   public int getUnresolvedNodes()
   {
      return m_unresolvedNodes;
   }
   public List<Vulnerabilities> getVulnerabilities()
   {
      return m_vulnerabilities;
   }
   public class Vulnerabilities
   {
      Vulnerabilities(int count, int severity, String status)
      {
         m_count = count;
         m_severity = severity >= 0? severity: null;
         m_status = status;
      }
      private final int m_count;
      private final Integer m_severity;
      private final String m_status;
   }
   private final String m_startTime;
   private final String m_endTime;
   private final int m_engineID;
   private final String m_name;
   private final long m_scanID;
   private final long m_siteID;
   private final String m_status;
   private int m_activeTasks;
   private int m_completedTasks;
   private int m_pendingTasks;
   private int m_deadNodes;
   private int m_filteredNodes;
   private int m_liveNodes;
   private int m_otherNodes;
   private int m_unresolvedNodes;
   private List<Vulnerabilities> m_vulnerabilities = new ArrayList<>();
}
