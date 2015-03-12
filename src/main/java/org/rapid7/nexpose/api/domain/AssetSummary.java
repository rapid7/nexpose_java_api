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
 * Represents an asset retrieved by the site device listing listing API request.
 *
 * @author Leonardo Varela
 */
public class AssetSummary extends BaseElement
{
   /////////////////////////////////////////////////////////////////////////
   // Public methods
   /////////////////////////////////////////////////////////////////////////

   /**
    * Creates an Empty Asset summary
    */
   public AssetSummary()
   {
      super();
   }

   /**
    * Sets the id of the Asset
    *
    * @param id the new value of the id of the asset
    */
   public void setId(int id)
   {
      m_id = id;
   }

   /**
    * Retrieves the id of the Asset.
    *
    * @return the id of the asset.
    */
   public int getId()
   {
      return m_id;
   }

   /**
    * Sets the address of the Asset.
    *
    * @param address the new address of the asset.
    */
   public void setAddress(String address)
   {
      m_address = address;
   }

   /**
    * Retrieves the address of the Asset.
    *
    * @return the address of the asset.
    */
   public String getAddress()
   {
      return m_address;
   }

   /**
    * Retrieves the site ID of the Asset.
    *
    * @return the site id of the asset.
    */
   public String getSiteID()
   {
      return m_siteID;
   }

   /**
    * Retrieves the risk score of the Asset
    * @return the risk score associated with the asset.
    */
   public float getRiskScore()
   {
      return m_riskScore;
   }

   /**
    * Retrieves the risk factor of the Asset Group
    * @return the risk score associated with the asset group.
    */
   public float getRiskFactor()
   {
      return m_riskFactor;
   }

   /**
    * Creates a summary out of an element AssetGroupSummary
    *
    * @param siteSummaryElement the {@link Element} that contains the
    *        information of the Asset group.
    * @throws APIException When there is a problem parsing the element's attributes.
    */
   public AssetSummary(Element siteSummaryElement) throws APIException
   {
      setResponseElement(siteSummaryElement);
      m_id = getInt("id");
      m_siteID = getString("site-id");
      m_address = getString("address");
      m_riskScore = getFloat("riskscore");
      m_riskFactor = getFloat("riskfactor");
   }

   /////////////////////////////////////////////////////////////////////////
   // non-Public fileds
   /////////////////////////////////////////////////////////////////////////

   /**Represents the id of the asset.*/
   private int m_id;
   /**Represents the site id of the asset.*/
   private String m_siteID;
   /**Represents the IP address of the asset.*/
   private String m_address;
   /**Represents the risk factor associated with the asset.*/
   private float m_riskFactor;
   /**Represents the risk score associated with the asset.*/
   private float m_riskScore;
}
