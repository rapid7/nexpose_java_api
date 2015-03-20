/***************************************************************************
 * COPYRIGHT (C) 2015, Rapid7 LLC, Boston, MA, USA.
 * All rights reserved. This material contains unpublished, copyrighted
 * work including confidential and proprietary information of Rapid7.
 **************************************************************************/

package org.rapid7.nexpose.api.examples;

import org.rapid7.nexpose.api.APIException;
import org.rapid7.nexpose.api.APISession;
import org.rapid7.nexpose.api.domain.DiscoveryConfig;
import org.rapid7.nexpose.api.domain.ScanSummary;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Demonstrate the DiscoveryConnectionListingRequest
 */
public class ListDiscoveryConfigs
{
   public void listDiscoveryConfigs(String[] args)
      throws IOException, APIException
   {
      // ALWAYS LOGIN BEFORE OPERATIONS.
      APISession session = getSession(args);
      String sessionID = session.getSessionID();

      List<DiscoveryConfig> discoveryConfigs = session.discoveryConnectionListingRequest(sessionID, null);

      System.out.println("\n*** CONFIG_ID NAME ADDRESS PROTOCOL***");
      for (DiscoveryConfig discoveryConfig: discoveryConfigs)
      {
         System.out.print(discoveryConfig.getConfigID());
         System.out.print(", " + discoveryConfig.getName());
         System.out.print(", " + discoveryConfig.getAddress());
         System.out.print(", " + discoveryConfig.getProtocol() + "\n");
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

      new ListDiscoveryConfigs().listDiscoveryConfigs(args);
   }
}
