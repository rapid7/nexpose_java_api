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
 * Represents a site retrieved by the site listing API request.
 *
 * @author Leonardo Varela
 */
public class SiteSummary extends BaseElement
{
   /////////////////////////////////////////////////////////////////////////
   // Public methods
   /////////////////////////////////////////////////////////////////////////

   /**
    * Creates an Empty Site summary
    */
   public SiteSummary()
   {
      super();
   }
   /**
    * Sets the id of the Site
    * @param value the new value of the id of the site
    */
   public void setId(int value)
   {
      m_id = value;
   }

   /**
    * Retrieves the id of the Site
    * @return the m_id attribute which represents the id of the site.
    */
   public int getId()
   {
      return m_id;
   }

   /**
    * Sets the name of the Site
    * @param value the new name of the site
    */
   public void setName(String value)
   {
      m_name = value;
   }

   /**
    * Retrieves the name of the Site
    * @return the m_name attribute which represents the name of the site.
    */
   public String getName()
   {
      return m_name;
   }
   /**
    * Retrieves the description of the Site
    * @return the m_description attribute which represents the description of the site.
    */
   public String getDescription()
   {
      return m_description;
   }
   /**
    * Retrieves the risk factor of the Site
    * @return the m_riskFactor attribute which represents the risk factor associated with the site.
    */
   public float getRiskFactor()
   {
      return m_riskFactor;
   }
   /**
    * Retrieves the risk score of the Site
    * @return the m_riskScore attribute which represents the risk score associated with the site.
    */
   public float getRiskScore()
   {
      return m_riskScore;
   }
   
   /**
    * Creates a summary out of an element SiteSummary
    * 
    * @throws APIException When there is a problem parsing the element's attributes. 
    */
   public SiteSummary(Element siteSummaryElement) throws APIException
   {
      setResponseElement(siteSummaryElement);
      m_id = getInt("id");
      m_name = getString("name");
      m_description = getString("description");
      m_riskFactor = getFloat("riskfactor");
      m_riskScore = getFloat("riskscore");
   }

   /////////////////////////////////////////////////////////////////////////
   // non-Public fileds
   /////////////////////////////////////////////////////////////////////////

   /**Represents the id of the site.*/
   private int m_id;
   /**Represents the name of the site.*/
   private String m_name;
   /**Represents the description of the site.*/
   private String m_description;
   /**Represents the risk factor associated with the site.*/
   private float m_riskFactor;
   /**Represents the risk score associated with the site.*/
   private float m_riskScore;
}
