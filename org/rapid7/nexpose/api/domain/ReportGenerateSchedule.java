/**
 * Copyright (C) 2012, Rapid7 LLC, Boston, MA, USA.
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
package org.rapid7.nexpose.api.domain;

/**
 * Represents generate element retrieved by the report save API request.
 *
 * @author Murali Rongali
 */
public class ReportGenerateSchedule
{
   /**
    * Creates a ReportGenerate schdule object.
    *
    * @param type the type of the schedule.
    * @param interval the interval time of the schedule. 
    * @param startDate the date that schedule starts. This should be in YYYYMMDDTHHMMSSsss format, such as: 19981231T00000000.
    * @param endDate the date that schedule ends. This should be in YYYYMMDDTHHMMSSsss format, such as: 19981231T00000000.
    */
   public ReportGenerateSchedule(String type, String interval, String startDate, String endDate)
   {
      m_type = type;
      m_interval = interval;
      m_start = startDate;
      m_notValidAfter = endDate;
   }

   /**
    * Retrieves the schedule type.
    *
    * @return the schedule type.
    */
   public String getType()
   {
      return m_type;
   }

   /**
    * Sets the schedule type.
    *
    * @param type The type of the schedule.
    */
   public void setType(String type)
   {
      m_type = type;
   }

   /**
    * Retrieves the interval of schedule.
    *
    * @return The interval of schedule.
    */
   public String getInterval()
   {
      return m_interval;
   }

   /**
    * Sets the interval of schedule.
    *
    * @param interval the interval of schedule.
    */
   public void setInterval(String interval)
   {
      m_interval = interval;
   }

   /**
    * Retrieves the start date of schedule.
    *
    * @return the start date of schedule.
    */
   public String getStart()
   {
      return m_start;
   }

   /**
    * Sets the start date of schedule.
    *
    * @param startDate The start date of schedule.
    */
   public void setStart(String startDate)
   {
      m_start = startDate;
   }

   /**
    * Retrieves the end date of the schedule.
    *
    * @return The end date of the schedule.
    */
   public String getNotValidAfter()
   {
      return m_notValidAfter;
   }

   /**
    * Sets the end date of the schedule.
    *
    * @param endDate the end date of the schedule.
    */
   public void setNotValidAfter(String endDate)
   {
      m_notValidAfter = endDate;
   }

   /////////////////////////////////////////////////////////////////////////
   // Non-public fields
   /////////////////////////////////////////////////////////////////////////

   /**Represents the type of the schedule.*/
   private String m_type;
   /**Represents the interval of the schedule.*/
   private String m_interval;
   /**Represents the start date of the schedule.*/
   private String m_start;
   /**Represents the end date of the schedule.*/
   private String m_notValidAfter;
}
