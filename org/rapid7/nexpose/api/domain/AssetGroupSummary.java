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

import org.rapid7.nexpose.api.APIException;
import org.rapid7.nexpose.api.BaseElement;
import org.w3c.dom.Element;

/**
 * Represents an asset group retrieved by the asset group listing API request.
 *
 * @author Leonardo Varela
 */
public class AssetGroupSummary extends BaseElement
{
   /////////////////////////////////////////////////////////////////////////
   // Public methods
   /////////////////////////////////////////////////////////////////////////

   /**
    * Creates an Empty Asset Group summary
    */
   public AssetGroupSummary()
   {
      super();
   }
   /**
    * Sets the id of the Asset Group
    * @param value the new value of the id of the asset group
    */
   public void setId(int value)
   {
      m_id = value;
   }

   /**
    * Retrieves the id of the Asset Group.
    * @return the id of the asset group.
    */
   public int getId()
   {
      return m_id;
   }

   /**
    * Sets the name of the Asset Group.
    * @param value the new name of the asset group.
    */
   public void setName(String value)
   {
      m_name = value;
   }
   /**
    * Retrieves the name of the Asset Group
    * @return the name of the asset group.
    */
   public String getName()
   {
      return m_name;
   }
   /**
    * Retrieves the description of the Asset Group.
    * @return the description of the asset group.
    */
   public String getDescription()
   {
      return m_description;
   }
   /**
    * Retrieves the risk score of the Asset Group
    * @return the risk score associated with the asset group.
    */
   public float getRiskScore()
   {
      return m_riskScore;
   }

   /**
    * Creates a summary out of an element AssetGroupSummary
    *
    * @param siteSummaryElement the {@link Element} that contains the
    *        information of the Asset group.
    * @throws APIException When there is a problem parsing the element's attributes.
    */
   public AssetGroupSummary(Element siteSummaryElement) throws APIException
   {
      setResponseElement(siteSummaryElement);
      m_id = getInt("id");
      m_name = getString("name");
      m_description = getString("description");
      m_riskScore = getFloat("riskscore");
   }

   /////////////////////////////////////////////////////////////////////////
   // non-Public fileds
   /////////////////////////////////////////////////////////////////////////

   /**Represents the id of the asset group.*/
   private int m_id;
   /**Represents the name of the asset group.*/
   private String m_name;
   /**Represents the description of the asset group.*/
   private String m_description;
   /**Represents the risk score associated with the asset group.*/
   private float m_riskScore;
}
