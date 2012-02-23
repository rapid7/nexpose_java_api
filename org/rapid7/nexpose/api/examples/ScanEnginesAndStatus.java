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
package org.rapid7.nexpose.api.examples;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import org.rapid7.nexpose.api.APIException;
import org.rapid7.nexpose.api.APISession;
import org.rapid7.nexpose.api.APISession.APISupportedVersion;
import org.rapid7.nexpose.api.EngineSummary;

/**
 * Prints out engine status.
 *
 * @author Christopher Lee
 */
public class ScanEnginesAndStatus
{
   APISession session = null;
   
   /**
    * Body of the API call.
    */
   public void scanEnginesAndStatus(String[] args) throws IOException, APIException
   {      
      // ALWAYS LOGIN BEFORE OPERATIONS.
      APISession session = getSession(args);
      String sessionID = session.getSessionID();
      
      List<EngineSummary> engineSummaries = (List<EngineSummary>)session.listEngines(sessionID, null);
       
      System.out.println("\n*** NAME, ADDRESS, STATUS ***");
      for (EngineSummary engineSummary : engineSummaries)
      {
         System.out.print(engineSummary.getName());
         System.out.print(", " + engineSummary.getAddress());
         System.out.print(", " + engineSummary.getStatus());
         System.out.print("\n");         
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
      APISession session = new APISession(url, "xml", APISupportedVersion.V1_2, args[2].trim(), args[3].trim());

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
         System.out.println("USAGE: java ScanEnginesAndStatus nexpose_netaddress, port, username, password");
         return;
      }
      
      new ScanEnginesAndStatus().scanEnginesAndStatus(args);
   }
}
