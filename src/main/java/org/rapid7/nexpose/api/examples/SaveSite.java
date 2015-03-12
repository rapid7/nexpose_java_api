/***************************************************************************
 * COPYRIGHT (C) 2015, Rapid7 LLC, Boston, MA, USA.
 * All rights reserved. This material contains unpublished, copyrighted
 * work including confidential and proprietary information of Rapid7.
 **************************************************************************/

package org.rapid7.nexpose.api.examples;

import org.rapid7.nexpose.api.APIException;
import org.rapid7.nexpose.api.APIResponse;
import org.rapid7.nexpose.api.APISession;
import org.rapid7.nexpose.api.domain.EngineSummary;
import org.rapid7.nexpose.api.domain.SiteSummary;
import org.rapid7.nexpose.api.generators.IContentGenerator;
import java.io.IOException;
import java.net.URL;
import java.util.List;

/**
 * Save a site
 */
public class SaveSite
{
   APISession session = null;

   /**
    * Body of the API call.
    */
   public void saveSite(String[] args) throws IOException, APIException
   {
      // ALWAYS LOGIN BEFORE OPERATIONS.
      APISession session = getSession(args);
      session.clearErrorHandler();
      String sessionID = session.getSessionID();
      //list all the sites
      Iterable<SiteSummary> summaries = session.listSites(sessionID, null);
      //if there are any, re-save one
      SiteSummary summary = summaries.iterator().next();
      if (summary != null)
      {
         APIResponse response = session.siteSaveRequest(
            session.getSessionID(),
            null,
            Integer.valueOf(summary.getId()).toString(),
            summary.getName(),
            summary.getDescription(),
            Float.valueOf(summary.getRiskFactor()).toString(),
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            "full-audit",
            null,
            null,
            null,
            "weekly",
            null,
            "20150307T120000000",
            null,
            null
         );
      }
      // ALWAYS LOGOUT WHEN FINISHED.
      session.logout(sessionID, null);
   }

   /*
    * Gets authenticated session object
    *
    */
   private APISession getSession(String[] args) throws IOException, APIException
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
         System.out.println("USAGE: java ScanEnginesAndStatus nexpose_netaddress, port, username, password");
         System.exit(1);
      }
      return session;
   }

   public static void main(String[] args) throws IOException, APIException
   {
      if (args.length < 4)
      {
         System.out.println("\n*** INVALID INPUT ***");
         System.out.println("USAGE: java SaveSite nexpose_netaddress, port, username, password");
         return;
      }

      new SaveSite().saveSite(args);
   }
}
