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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import org.rapid7.nexpose.api.APISession.APISupportedVersion;

/**
 * Simple base class for templating NeXpose API requests using an XML
 * template. Template parameters are specified using $(paramName) syntax.
 *
 * @author Chad Loder
 */
public class TemplateAPIRequest implements APIRequest, Cloneable
{
   /////////////////////////////////////////////////////////////////////////
   // Public methods
   /////////////////////////////////////////////////////////////////////////

   /**
    * Constructs a new template API request with the given XML template as a
    * String. Variable substitutions will happen when the toXML method is
    * called.
    *
    * @param template The XML template
    */
   public TemplateAPIRequest(String template)
   {
      if (template == null)
      {
         throw new IllegalArgumentException("template cannot be null");
      }
      m_template = template;
      m_params = new HashMap<String, IContentGenerator>();
   }

   /**
    * Constructs a new TemplateAPIRequest, automatically looking for a Java
    * resource file named &lt;classname&gt;.xml, where classname is the short
    * name (unqualified by a package name) of the derived class. For example,
    * a class named org.rapid7.nexpose.LoginRequest should have a file in the
    * package dir named LoginRequest.xml, which should contain the templated
    * XML content.
    */
   protected TemplateAPIRequest()
   {
      m_template = getTemplateResource(getClass());
      m_params = new HashMap<String, IContentGenerator>();
   }

   /**
    * Constructs a new TemplateAPIRequest, automatically looking for a Java
    * resource file named &lt;classname&gt;.xml, where classname is the short
    * name (unqualified by a package name) of the derived class. For example,
    * a class named org.rapid7.nexpose.LoginRequest should have a file in the
    * package dir named LoginRequest.xml, which should contain the templated
    * XML content. It also sets the session-id and sync-id for the request.
    */
   protected TemplateAPIRequest(String sessionId, String syncId)
   {
      m_template = getTemplateResource(getClass());
      m_params = new HashMap<String, IContentGenerator>();
      if (sessionId != null && !sessionId.isEmpty())
      {
         set("session-id", sessionId);
      }
      if (syncId != null && !syncId.isEmpty())
      {
         set("sync-id", syncId);
      }
   }

   /**
    * Sets the named parameter to the given value. The parameter will be used
    * for template substitution.
    *
    * @param param the named parameter.
    * @param value the value to be set.
    */
   public void set(String param, String value)
   {
      StringContentGenerator stringContent = new StringContentGenerator(StringUtils.xmlEscape(value));
      if (stringContent.toString() != null)
         m_params.put(param, stringContent);
      else
         m_params.put(param, null);
   }

   /**
    * Sets the named parameter to the given generator. The generator will be
    * used for template substitution.
    *
    * @param param the parameter key to put in the map
    * @param generator the content generator to associate the param with.
    */
   public void set(String param, IContentGenerator generator)
   {
      m_params.put(param, generator);
   }

   /**
    * Performs templated parameter substitution on the template XML string and
    * returns the result, which should be suitable for POSTing to the NeXpose
    * API.
    */
   public String toXML()
   {
      m_requestXML = StringUtils.expandVariables(m_template, m_params); 
      return m_requestXML;
   }

   /**
    * Tells whether the parameter is set or not by looking into the params Map
    *
    * @param param the parameter to look for in the map
    * @return true if the param exists, false otherwise.
    */
   public boolean isSet(String param)
   {
      if (m_params != null && m_params.containsKey(param))
         return true;
      return false;
   }

   /**
    * Clones this TemplateAPIRequest
    *
    * @see java.lang.Object#clone()
    * @throws CloneNotSupportedException when the object cannot be cloned
    */
   @SuppressWarnings("unchecked")
   public Object clone() throws CloneNotSupportedException
   {
      TemplateAPIRequest req = (TemplateAPIRequest)super.clone();
      req.m_params = (HashMap<String,IContentGenerator>)(m_params.clone());
      if (m_requestXML != null )
         req.m_requestXML = m_requestXML;
      req.m_firstSupportedVersion = m_firstSupportedVersion;
      req.m_lastSupportedVersion = m_lastSupportedVersion;
      return req;
   }

   /**
    * Loads the template XML corresponding to the current class. For example,
    * if the current class is named LoginRequest, it will load a resource file
    * named LoginRequest.xml.
    *
    * @param c The class of the current request.
    * @return The templated XML.
    */
   protected static String getTemplateResource(
      Class<? extends TemplateAPIRequest> c)
   {
      String filename = c.getName();
      int period = filename.lastIndexOf('.');
      if (period != -1)
      {
         filename = filename.substring(period + 1);
      }
      filename = filename + ".xml";
      InputStream in = c.getResourceAsStream(filename);
      if (in == null)
      {
         throw new RuntimeException("Resource file " 
            + filename
            + " does not exist");
      }
      StringBuilder sb = new StringBuilder();
      try
      {
         BufferedReader br = new BufferedReader(new InputStreamReader(in));
         for (String line = br.readLine(); line != null; line = br.readLine())
         {
            sb.append(line);
            sb.append("\n");
         }
         br.close();
         return sb.toString();
      }
      catch (IOException e)
      {
         throw new RuntimeException("Error reading " + filename, e);
      }
      finally
      {
         try
         {
            in.close();
         }
         catch (IOException e)
         { /* ignore */
         }
      }
   }

   /* (non-Javadoc)
    * @see org.rapid7.nexpose.api.APIRequest#getFirstSupportedVersion()
    */
   @Override
   public APISupportedVersion getFirstSupportedVersion()
   {
      return m_firstSupportedVersion;
   }

   /* (non-Javadoc)
    * @see org.rapid7.nexpose.api.APIRequest#getLastSupportedVersion()
    */
   @Override
   public APISupportedVersion getLastSupportedVersion()
   {
      return m_lastSupportedVersion;
   }

   /**
    * Retrieves the parsed xml that resulted from applying the dynamic values
    * to the parameter received. This is exactly the same XML that is being
    * sent through the wire.
    *
    * @return the xml that is being sent through the wire.
    */
   public String getRequestXML()
   {
      return m_requestXML;
   }

   /////////////////////////////////////////////////////////////////////////
   // non-Public fields
   /////////////////////////////////////////////////////////////////////////

   /** Represents the XML template for this request */
   private final String m_template;
   /** The key:value pairs used for replacement of templated params */
   private HashMap<String, IContentGenerator> m_params;
   /**The first supported version for the Request*/
   protected APISupportedVersion m_firstSupportedVersion;
   /**The last supported version for the Request*/
   protected APISupportedVersion m_lastSupportedVersion;
   /**The parsed xml that is being sent through the wire*/
   private String m_requestXML;
}
