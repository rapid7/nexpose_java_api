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
package org.rapid7.nexpose.api;

import org.w3c.dom.Element;

/**
 * Represents a Ticket retrieved by the ticket listing API request.
 *
 * @author Murali Rongali
 */
public class TicketSummary extends BaseElement
{
   /////////////////////////////////////////////////////////////////////////
   // Public methods
   /////////////////////////////////////////////////////////////////////////

   /**
    * Retrieves the id of the ticket.
    * 
    * @return The id attribute of the ticket.
    */
   public int getId()
   {
      return m_id;
   }
   
   /**
    * Retrieves the name of the ticket.
    * 
    * @return The name of the ticket.
    */
   public String getName()
   {
      return m_name;
   }
   
   /**
    * Retrieves the state of the ticket.
    * 
    * @return The state of the ticket.
    */
   public String getState() 
   {
      return m_state;
   }
   
   /**
    * Retrieves the deviceId associated with the ticket.
    * 
    * @return The device Id of the ticket.
    */
   public int getDeviceId() 
   {
      return m_deviceId;
   }
   
   /**
    * Retrieves the created date of the ticket.
    * 
    * @return The created date of the ticket.
    */
   public String getCreatedOn() 
   {
      return m_createdOn;
   }
   
   /**
    * Retrieves the author of the ticket.
    * 
    * @return The author of the ticket.
    */
   public String getAuthor() 
   {
      return m_author;
   }
   
   /**
    * Retrieves the priority of the ticket.
    * 
    * @return The priority of the ticket.
    */
   public String getPriority() 
   {
      return m_priority;
   }
   
   /**
    * Retrieves the assigned user name of the ticket.
    * 
    * @return The assigned user name of the ticket.
    */
   public String getAssignedTo() 
   {
      return m_assignedTo;
   }
   
   /**
    * Creates a summary out of an element TicketSummary
    *
    * @throws APIException When there is a problem parsing the element's
    * attributes. 
    */
   public TicketSummary(Element siteSummaryElement) throws APIException
   {
      setResponseElement(siteSummaryElement);
      m_id = getInt("id");
      m_name = getString("name");
      m_state = getString("state");
      m_deviceId = getInt("device-id");
      m_createdOn = getString("created-on");
      m_author = getString("author");
      m_priority = getString("priority");
      m_assignedTo = getString("assigned-to");
   }

   /////////////////////////////////////////////////////////////////////////
   // Non-public fields
   /////////////////////////////////////////////////////////////////////////

   /**Represents the id of the ticket.*/
   private int m_id;
   /**Represents the name of the ticket.*/
   private String m_name;
   /**Represents the state of the ticket.*/
   private String m_state;
   /**Represents the devideId associated with the ticket.*/
   private int m_deviceId;
   /**Represents the create date the ticket.*/
   private String m_createdOn;
   /**Represents the author of the ticket.*/
   private String m_author;
   /**Represents the priority of the ticket.*/
   private String m_priority;
   /**Represents the assigned user name of the ticket.*/
   private String m_assignedTo;
}
