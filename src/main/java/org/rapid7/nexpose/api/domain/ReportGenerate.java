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
public class ReportGenerate
{
   /////////////////////////////////////////////////////////////////////////
   // Public methods
   /////////////////////////////////////////////////////////////////////////

   /**
    * Creates a ReportGenerate object.
    *
    * @param afterScan the flag to generate a report after every scan or not.
    * @param schedule the flag to set schedule.
    */
   public ReportGenerate(String afterScan, String schedule)
   {
      m_afterScan = afterScan;
      m_schedule = schedule;
      m_genSchedule = null;
   }

   /**
    * Creates a ReportGenerate object.
    *
    * @param afterScan the flag to generate a report after every scan or not.
    * @param schedule the flag to set schedule.
    * @param genSchedule the schedule element data.
    */
   public ReportGenerate(String afterScan, String schedule, ReportGenerateSchedule genSchedule)
   {
      m_afterScan = afterScan;
      m_schedule = schedule;
      m_genSchedule = genSchedule;
   }

   /**
    * Retrieves the after scan flag.
    *
    * @return The after scan flag to generate report.
    */
   public String getAfterScan()
   {
      return m_afterScan;
   }

   /**
    * Sets the afterScan flag.
    *
    * @param afterScan The afterScan flag to set.
    */
   public void setAfterScan(String afterScan)
   {
      m_afterScan = afterScan;
   }

   /**
    * Retrieves the schedule is enabled or not.
    *
    * @return The schedule flag to determine whether schedule is enabled or not.
    */
   public String getSchedule()
   {
      return m_schedule;
   }

   /**
    * Sets the schedule flag in 0|1 format.
    *
    * @param schedule the schedule flag for report generate.
    */
   public void setSchedule(String schedule)
   {
      m_schedule = schedule;
   }

   /**
    * Retrieves the GenerateSchedule object.
    *
    * @return The object of ReportGenerateSchedule.
    */
   public ReportGenerateSchedule getGenerateSchedule()
   {
      return m_genSchedule;
   }

   /**
    * Sets the GenerateSchedule object.
    *
    * @param genSchedule the schedule object that represents schedule element.
    */
   public void setGenerateSchedule(ReportGenerateSchedule genSchedule)
   {
      m_genSchedule = genSchedule;
   }
   /////////////////////////////////////////////////////////////////////////
   // Non-public fields
   /////////////////////////////////////////////////////////////////////////

   /**Represents the report generation after every scan.*/
   private String m_afterScan;
   /**Represents the report generation schedule.*/
   private String m_schedule;
   /**Represents the schedule element.*/
   private ReportGenerateSchedule m_genSchedule;
   
}
