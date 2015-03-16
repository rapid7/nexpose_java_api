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
package org.rapid7.nexpose.utils;

import java.io.Writer;
import java.util.Map;

/**
 * Utility class for String parsing and manipulation.
 *
 * @author Chad Loder
 */
public abstract class StringUtils
{
   /////////////////////////////////////////////////////////////////////////
   // Public methods
   /////////////////////////////////////////////////////////////////////////

   /**
    * Expands variable references in the source string according to their
    * values from the given Map object (variable names are keys into the Map
    * which yield Strings).
    * <P>
    * ${varname} expands to the value of the variable, curly brackets must be
    * present and balanced, or no substitution will occur.
    * <P>
    * Expansion is NOT recursive; that is, variable values which themselves
    * contain things like ${this} will be literally expanded into the
    * resulting string WITHOUT further processing. However, it is very easy to
    * modify this method to do recursive substitution (see below) (just make
    * sure you keep a stack of current substitutions so you don't get into
    * infinite loops).
    */
   public static String expandVariables(String source, Map<String, ?> vars)
   {
      if (source == null)
      {
         return null;
      }
      StringBuilder buf = new StringBuilder(source);
      for (int pos = 0; pos < buf.length(); pos++)
      {
         char c = buf.charAt(pos);
         if (c == '$')
         {
            int startVar = pos;
            pos++;
            c = buf.charAt(pos);
            if (c == '{')
            {
               int endVar;
               for (endVar = pos + 1; endVar < buf.length(); endVar++)
               {
                  c = buf.charAt(endVar);
                  if (c == '}')
                  {
                     break;
                  }
               }
               
               if (c == '}')
               {
                  String varName = buf.substring(startVar + 2, endVar);
                  Object varValue = vars.get(varName);
                  String value = "";
                  if (varValue != null)
                  {
                     value = varValue.toString();
                  }
                  
                  buf.replace(startVar, endVar + 1, value);
                  pos = startVar - 1;
               }
            }
         }
      }
      
      String retVal = buf.toString();
      return retVal;
   }

   /**
    * Converts special characters to entity references. This method is
    * suitable for quoting XML attributes (such a function is usually called
    * <code>quoteattr</code> in XML libraries). The characters will be
    * converted according to this scheme:
    * <TABLE BORDER=1>
    * <TR>
    * <TD>&amp;</TD>
    * <TD>&amp;amp;</TD>
    * </TR>
    * <TR>
    * <TD>&lt;</TD>
    * <TD>&amp;lt;</TD>
    * </TR>
    * <TR>
    * <TD>&gt;</TD>
    * <TD>&amp;gt;</TD>
    * </TR>
    * <TR>
    * <TD>&apos;</TD>
    * <TD>&amp;apos;</TD>
    * </TR>
    * <TR>
    * <TD>&quot;</TD>
    * <TD>&amp;quot;</TD>
    * </TR>
    * </TABLE>
    * 
    * @param input The input string to be converted.
    * @return A String with all special characters transformed into their
    * escaped form.
    */
   public static String xmlEscape(String input)
   {
      try
      {
         if (input != null)
         {
            java.io.StringWriter writer = new java.io.StringWriter((int)(input.length() * 1.5));
            xmlEscape(input, writer);
            return writer.toString();
         }
         return null; 
      }
      catch (java.io.IOException e)
      {
         // this should never happen
         throw new RuntimeException(e);
      }
   }

   /**
    * Converts special characters to entity references. This method is
    * suitable for quoting XML attributes (such a function is usually called
    * <code>quoteattr</code> in XML libraries). The characters will be
    * converted according to this scheme:
    * <TABLE BORDER=1>
    * <TR>
    * <TD>&amp;</TD>
    * <TD>&amp;amp;</TD>
    * </TR>
    * <TR>
    * <TD>&lt;</TD>
    * <TD>&amp;lt;</TD>
    * </TR>
    * <TR>
    * <TD>&gt;</TD>
    * <TD>&amp;gt;</TD>
    * </TR>
    * <TR>
    * <TD>&apos;</TD>
    * <TD>&amp;apos;</TD>
    * </TR>
    * <TR>
    * <TD>&quot;</TD>
    * <TD>&amp;quot;</TD>
    * </TR>
    * </TABLE>
    * 
    * @param input The input string to be converted.
    * @param out The writer to which the converted string is written.
    */
   public static void xmlEscape(String input, Writer out) throws java.io.IOException
   {
      if ((input == null) || (input.length() == 0))
      {
         return;
      }
      /*
       * This implementation should be fairly efficient in that it minimizes
       * the number of function calls. Hence, we operate on an array rather
       * than a String object. We collect the output in a string buffer, and
       * here too we try to minimize the number of function calls. We do not
       * add each normal character to the output buffer one at a time.
       * Instead, we build a "run" of normal characters and, upon encountering
       * a special character, we write the previous normal run before we write
       * the special replacement.
       */
      char[] chars = input.toCharArray();
      int len = chars.length;
      char c;
      
      // the start of the latest run of normal characters
      int startNormal = 0;
      int i = 0;
      while (true)
      {
         if (i == len)
         {
            if (startNormal != i)
            {
               out.write(chars, startNormal, i - startNormal);
            }
            break;
         }
         c = chars[i];
         switch (c)
         {
            case '&':
               if (startNormal != i)
               {
                  out.write(chars, startNormal, i - startNormal);
               }
               startNormal = i + 1;
               out.write("&amp;");
               break;
            case '<':
               if (startNormal != i)
               {
                  out.write(chars, startNormal, i - startNormal);
               }
               startNormal = i + 1;
               out.write("&lt;");
               break;
            case '>':
               if (startNormal != i)
               {
                  out.write(chars, startNormal, i - startNormal);
               }
               startNormal = i + 1;
               out.write("&gt;");
               break;
            case '\'':
               if (startNormal != i)
               {
                  out.write(chars, startNormal, i - startNormal);
               }
               startNormal = i + 1;
               // to stay compatible with HTML and XHTML, we don't
               // use the apos entity because it's XML-only...the
               // Unicode character reference works with both
               // X/HTML and XML
               out.write("&#39;");
               // out.write("&apos;");
               break;
            case '"':
               if (startNormal != i)
               {
                  out.write(chars, startNormal, i - startNormal);
               }
               startNormal = i + 1;
               out.write("&quot;");
               break;
            default:
               int num = c;
               if ((num < 32 && c != '\n' && c != '\r' && c != '\t') || num > 126)
               {
                  // this is some kind of funky character
                  if (startNormal != i)
                  {
                     out.write(chars, startNormal, i - startNormal);
                  }
                  startNormal = i + 1;
                  
                  if (!isXmlRestricted(c))
                  {
                     // RestrictedChars are not valid even in numeric
                     // character references, so we skip them
                     out.write("&#");
                     out.write(Integer.toString(num));
                     out.write(";");
                  }
               }
               
               // ...else this char becomes part of the normal run
               break;
         }
         i++;
      }
   }

   /**
    * Returns true if and only if c matches the RestrictedChars production
    * from http://www.w3.org/TR/2004/REC-xml11-20040204/#NT-Char
    */
   private static boolean isXmlRestricted(char c)
   {
      return (c <= 0x8 || (c == 0xb || c == 0xc) || (c >= 0xe && c <= 0x1f) || (c >= 0x7f && c <= 0x84) || (c >= 0x86 && c <= 0x9f));
   }
}
