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
 * Represents an Engine retrieved by the engine listing API request.
 *
 * @author Leonardo Varela
 */
public class EngineSummary extends BaseElement
{
   /////////////////////////////////////////////////////////////////////////
   // Public methods
   /////////////////////////////////////////////////////////////////////////

   /**
    * Retrieves the id of the Engine
    * @return the m_id attribute which represents the id of the engine.
    */
   public int getId()
   {
      return m_id;
   }
   /**
    * Retrieves the name of the Engine
    * @return the m_name attribute which represents the name of the engine.
    */
   public String getName()
   {
      return m_name;
   }
   /**
    * Retrieves the address of the engine
    * @return the m_address attribute which represents the address of the engine.
    */
   public String getAddress()
   {
      return m_address;
   }
   /**
    * Retrieves the port of the engine
    * @return the m_port attribute which represents the port associated with the
    *         engine.
    */
   public int getPort()
   {
      return m_port;
   }
   /**
    * Retrieves the status of the engine
    * @return the m_status attribute which represents the status associated with
    *         the engine.
    */
   public String getStatus()
   {
      return m_status;
   }

   /**
    * Creates a summary out of an element SiteSummary
    *
    * @throws APIException When there is a problem parsing the element's
    *         attributes.
    */
   public EngineSummary(Element siteSummaryElement) throws APIException
   {
      setResponseElement(siteSummaryElement);
      m_id = getInt("id");
      m_name = getString("name");
      m_address = getString("address");
      m_port = getInt("port");
      m_status = getString("status");
   }

   /////////////////////////////////////////////////////////////////////////
   // non-Public fields
   /////////////////////////////////////////////////////////////////////////

   /**Represents the id of the engine.*/
   private int m_id;
   /**Represents the name of the engine.*/
   private String m_name;
   /**Represents the address of the engine.*/
   private String m_address;
   /**Represents the port associated with the engine.*/
   private int m_port;
   /**Represents the status associated with the engine.*/
   private String m_status;
}
