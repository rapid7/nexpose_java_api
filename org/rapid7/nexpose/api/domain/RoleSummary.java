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
 * Represents a Role retrieved by the role listing API request.
 *
 * @author Meera Muthuswami
 */
public class RoleSummary extends BaseElement
{
   /////////////////////////////////////////////////////////////////////////
   // Public methods
   /////////////////////////////////////////////////////////////////////////

   /**
    * Retrieves the id of the role.
    *
    * @return The id attribute of the role.
    */
   public long getId()
   {
      return m_id;
   }

   /**
    * Retrieves the name of the role.
    *
    * @return The name of the role.
    */
   public String getName()
   {
      return m_name;
   }

   /**
    * Retrieves the full name of the role.
    *
    * @return The full name of the role.
    */
   public String getFullName()
   {
      return m_fullname;
   }

   /**
    * Retrieves the description of the role.
    *
    * @return The description of the role.
    */
   public String getDescription()
   {
      return m_description;
   }

   /**
    * Retrieves the state of the role.
    *
    * @return The state of the role.
    */
   public String getEnabled()
   {
      return m_enabled;
   }

   /**
    * Creates an element containing a role summary.
    *
    * @throws APIException When there is a problem parsing the element's
    * attributes.
    */
   public RoleSummary(Element roleSummaryElement) throws APIException
   {
      setResponseElement(roleSummaryElement);
      m_id = getInt("id");
      m_name = getString("name");
      m_enabled = getString("enabled");
      m_description = getString("description");
      m_fullname = getString("full-name");
   }

   /////////////////////////////////////////////////////////////////////////
   // Non-public fields
   /////////////////////////////////////////////////////////////////////////

   /** Represents the id of the role. */
   private long m_id;

   /** Represents the name of the role. */
   private String m_name;

   /** Represents if the role is enabled or not. */
   private String m_enabled;

   /** Represents the description of the role. */
   private String m_description;

   /** Represents the full name of the role. */
   private String m_fullname;
}
