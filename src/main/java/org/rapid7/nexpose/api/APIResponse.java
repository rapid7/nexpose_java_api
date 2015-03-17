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
package org.rapid7.nexpose.api;

import java.io.StringWriter;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * A convenience class for encapsulating and parsing API responses.
 *
 * @author Chad Loder
 * @author Leonardo Varela
 */
public class APIResponse
{
   /////////////////////////////////////////////////////////////////////////
   // Public methods
   /////////////////////////////////////////////////////////////////////////

   /**
    * Constructs a new API response from the given XML document.
    *
    * @param doc The XML document containing the parsed API response
    */
   public APIResponse(Document doc)
   {
      if (doc == null)
      {
         throw new IllegalArgumentException("doc cannot be null");
      }

      m_doc = doc;
   }
   /**
    * Constructs a new API response from the given XML document and sets the
    * xml that made NeXpose send this response
    *
    * @param doc The XML document containing the parsed API response
    */
   public APIResponse(Document doc, String finalXML)
   {
      if (doc == null)
      {
         throw new IllegalArgumentException("doc cannot be null");
      }

      m_doc = doc;
      m_finalXML = finalXML;
   }

   /**
    * Grabs a single XPath expression out of the response.
    *
    * @param xpath The XPath expression
    * @return a String with the value of the evaluated xPath expression or
    *         null if it is not found.
    * @throws APIException when the Xpath expression is invalid.
    */
   public String grab(String xpath) throws APIException
   {
      try
      {
         return XPathFactory.newInstance().newXPath().evaluate(xpath, m_doc);
      }
      catch (XPathExpressionException e)
      {
         throw new APIException("Error parsing response", e);
      }
   }

   /**
    * Grabs a single XPath expression out of the response and parses it as an
    * int.
    *
    * @param xpath The XPath expression
    * @return an int with the value of the evaluated xPath expression or
    *         null if it is not found.
    * @throws APIException when the Xpath expression is invalid.
    */
   public int grabInt(String xpath) throws APIException
   {
      return Integer.parseInt(grab(xpath));
   }

   /**
    * Grabs a single XPath expression out of the response and parses it as a
    * boolean.
    *
    * @param xpath The XPath expression
    * @return a boolean with the value of the evaluated xPath expression or
    *         null if it is not found.
    * @throws APIException when the Xpath expression is invalid.
    */
   public boolean grabBoolean(String xpath) throws APIException
   {
      return Boolean.parseBoolean(grab(xpath));
   }

   /**
    * Grabs a single XPath expression out of the response and parses it as a
    * long
    *
    * @param xpath The XPath expression
    * @return a long with the value of the evaluated xPath expression or
    *         null if it is not found.
    * @throws APIException when the Xpath expression is invalid.
    */
   public long grabLong(String xpath) throws APIException
   {
      return Long.parseLong(grab(xpath));
   }

   /**
    * Grabs a list of DOM nodes matching the given expression out of the
    * response.
    *
    * @param xpath The XPath expression
    * @return a NodeList list of nodes with the values of the evaluated xPath
    *         expression or null if none found.
    * @throws APIException when the Xpath expression is invalid.
    */
   public NodeList grabNodes(String xpath) throws APIException
   {
      try
      {
         return (NodeList)XPathFactory.newInstance().newXPath().evaluate(xpath, m_doc, XPathConstants.NODESET);
      }
      catch (XPathExpressionException e)
      {
         throw new APIException("Error parsing response", e);
      }
   }

   /**
    * Grabs a node matching the given expression out of the response.
    *
    * @param xpath The XPath expression
    * @return a Node with the values of the evaluated xPath expression or null
    *         if not found.
    * @throws APIException when the Xpath expression is invalid.
    */
   public Node grabNode(String xpath) throws APIException
   {
      try
      {
         return (Node)XPathFactory.newInstance().newXPath().evaluate(xpath, m_doc, XPathConstants.NODE);
      }
      catch (XPathExpressionException e)
      {
         throw new APIException("Error parsing response", e);
      }
   }

   /**
    * Retrieves the xml representation of the Document associated with this
    * response.
    *
    * @return the String that represents the Response XML associated with the latest API request.
    * @throws APIException When the Document m_doc cannot be parsed into a String
    */
   public String getResponse() throws APIException
   {
      try
      {
         final Source source = new DOMSource(m_doc);
         final StringWriter stringWriter = new StringWriter();
         final Result result = new StreamResult(stringWriter);
         final TransformerFactory factory = TransformerFactory.newInstance();
         final Transformer transformer = factory.newTransformer();
         transformer.transform(source, result);
         return stringWriter.getBuffer().toString();
      }
      catch (TransformerConfigurationException e)
      {
         throw new APIException ("Could not retrieve the String representation of the API's XML response due to a misconfiguration of the Transformer: " + e.toString());
      }
      catch (TransformerException e)
      {
         throw new APIException ("Could not retrieve the String representation of the API's XML response due to the Transformer: " + e.toString());
      }
   }
   /**
    * @return the final XML associated with the response
    */
   public String getFinalXML()
   {
      return m_finalXML;
   }

   /**
    * @param finalXML the final XML associated with the response to set
    */
   public void setFinalXML(String finalXML)
   {
      m_finalXML = finalXML;
   }

   /////////////////////////////////////////////////////////////////////////
   // non-Public fields
   /////////////////////////////////////////////////////////////////////////

   /** The API response document */
   private Document m_doc;
   /**The request that was sent to obtain this response*/
   private String m_finalXML;
}
