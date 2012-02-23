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
import org.rapid7.nexpose.api.UserSummary;

/**
 * Deletes a user by id or user name.
 *
 * @author Christopher Lee
 */
public class DeleteUserByNameOrID
{
   APISession session = null;
   
   /**
    * Body of the API call.
    */
   public void deleteUserByNameOrID(String[] args) throws IOException, APIException
   {      
      // ALWAYS LOGIN BEFORE OPERATIONS.
      APISession session = getSession(args);
      String sessionID = session.getSessionID();
      
      // Check if an integer was passed in
      String user = args[4].trim();
      boolean isUserInt = false;
      try
      {
         Integer.parseInt(user);
         isUserInt = true;
         
         // Passed in value was integer ... no need to do lookup
         session.userDeleteRequest(sessionID, null, user);         
         System.out.println("\n*** User with ID: " + user + " was deleted successfully. ***");
      }
      catch (NumberFormatException nfe)
      {
         // Do nothing ...
      }
      catch (Exception e)
      {
         System.out.println("\n*** The user with ID: " + user + " could not be deleted ***");
      }
           
      if (!isUserInt)
      {
         boolean foundUser = false; 
         List<UserSummary> userSummaries = (List<UserSummary>)session.listUsers(sessionID, null);

         for (UserSummary userSummary : userSummaries)
         {
            if(user.equalsIgnoreCase(userSummary.getUsername()) || user.equalsIgnoreCase(userSummary.getFullname()))
            {
               foundUser = true;
               try
               {
                  session.userDeleteRequest(sessionID, null, String.valueOf(userSummary.getId()));
                  System.out.println("\n*** Deleted user: " + user + " ***");;
                  break;
               }
               catch (Exception e)
               {
                  System.out.println("\n*** The user: " + user + " could not be deleted ***");
               }
            }
         }
         
         if (!foundUser)
         {
            System.out.println("\n*** Could not find user: " + user );
         }
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
         System.out.println("USAGE: java DeleteUserByNameOrID nexpose_netaddress, port, username, password");
         System.exit(1);
      }
      return session;
   }
   
   public static void main(String[] args) throws IOException, APIException
   {
      if (args.length < 5)
      {
         System.out.println("\n*** INVALID INPUT ***");
         System.out.println("USAGE: java DeleteUserByNameOrID nexpose_netaddress, port, username, password, user");
         return;
      }
      
      new DeleteUserByNameOrID().deleteUserByNameOrID(args);
   }
}
