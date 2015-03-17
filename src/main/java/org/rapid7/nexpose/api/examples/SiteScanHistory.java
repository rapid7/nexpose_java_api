/***************************************************************************
 * COPYRIGHT (C) 2015, Rapid7 LLC, Boston, MA, USA.
 * All rights reserved. This material contains unpublished, copyrighted
 * work including confidential and proprietary information of Rapid7.
 **************************************************************************/

package org.rapid7.nexpose.api.examples;

import org.rapid7.nexpose.api.APIException;
import org.rapid7.nexpose.api.APIResponse;
import org.rapid7.nexpose.api.APISession;
import org.rapid7.nexpose.api.domain.ScanSummary;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Demonstrate getting a site scan history
 */
public class SiteScanHistory
{
   public void siteScanHistory(String[] args)
      throws IOException, APIException
   {
      // ALWAYS LOGIN BEFORE OPERATIONS.
      APISession session = getSession(args);
      String sessionID = session.getSessionID();

      List<ScanSummary> summaries = session.siteScanHistoryRequest(sessionID, null, "1");

      System.out.println("\n***SCAN_ID START_TIME END_TIME STATUS ***");
      for (ScanSummary scanSummary: summaries)
      {
         System.out.print(scanSummary.getScanID());
         System.out.print(", " + scanSummary.getStartTime());
         System.out.print(", " + scanSummary.getEndTime());
         System.out.print(", " + scanSummary.getStatus() + "\n");
      }

      // ALWAYS LOGOUT WHEN FINISHED.
      session.logout(sessionID, null);
   }
   /**
   * Gets authenticated session object
   *
   */
   private APISession getSession(String[] args) throws MalformedURLException
   {
      URL url = new URL("https://" + args[0].trim() + ":" + args[1].trim());
      APISession session = new APISession(url, "xml", APISession.APISupportedVersion.V1_2, args[2].trim(), args[3].trim());

      try
      {
         session.login(null);
      }
      catch (Exception e)
      {
         System.out.println("\n*** LOGIN FAILED ***");
         System.out.println("Message: " + e.getLocalizedMessage());
         System.out.println("USAGE: java SiteScanHistory nexpose_netaddress, port, username, password");
         System.exit(1);
      }
      return session;
   }

   public static void main(String[] args) throws IOException, APIException
   {
      if (args.length < 4)
      {
         System.out.println("\n*** INVALID INPUT ***");
         System.out.println("USAGE: java SiteScanHistory nexpose_netaddress, port, username, password");
         return;
      }

      new SiteScanHistory().siteScanHistory(args);
   }
}
