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
// TODO - Replace all of this with a real mime reader.....

package org.rapid7.nexpose.api.domain;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.StringTokenizer;
import org.xml.sax.InputSource;

public class MimeReader
{
  private Reader in = null;
  private String separator = "";
  private String lastSeparator = "";
  private String lastHeader = "";
  private String docHeader = "";
  private String firstLine = "";
  private boolean justGotPart = false;
  private ByteArrayOutputStream readBuffer = new ByteArrayOutputStream(1024);

  public MimeReader(Reader reader)
  {
    in = reader;
    getMimeSeparator();
  }

  public InputSource copyParts(OutputStream os)
  {
    InputSource inputSource = new InputSource(new StringReader(firstLine + getPartDataAsString()));

    while (nextPart())
    {
      getPartData(os);
    }
    return inputSource;
  }

 /**
  * Advances to the next part of the message, if there is a
  * next part. When you create an instance of a SimpleMimeReader,
  * you need to call nextPart() before you start getting data.
  *
  * @return    true if there is a next part, false if there isn't
  *            (which generally means you're at the end of the
  *            message)
  */

  public boolean nextPart()
  {
    // if the last separator we got was the separator plus a "--",
    // then the message is officially over (the RFC allows for
    // epilogues after this last separator, but they're supposed
    // to be ignored)
    if (lastSeparator.equals(separator + "--"))
    {
      String tempSeparator = separator;
      separator = "";
      justGotPart = false;
      getPartDataAsBytes();
      separator = tempSeparator;
      lastSeparator = "";
      lastHeader = "";
      return false;
    }

    // we need to advance to the next separator, unless we've
    // already got the previous part's data (which means we're
    // already there)
    if (!justGotPart)
    {
      getPartData(null);
    }

    // special consideration if we never found a separator
    // (set the lastHeader to the docHeader on the first
    // call to this function)
    if ((separator.length() == 0) && (lastHeader.length() == 0))
    {
      lastHeader = docHeader;
    }
    else
    {
      lastHeader = getHeader();
    }

    // reset our justGotPart flag
    justGotPart = false;

    // if our lastHeader variable has any data at all, we
    // should be at the next section; otherwise, we're at
    // the end of the input stream and should return false
    return (lastHeader.length() > 0);

  }


  /**
   * Gets the data contained in the current message part as
   * a byte array (this will return an empty byte array if you've already
   * got the data from this message part)
   *
   * @return a byte array containing the data in this message part,
   *         or an empty byte array if you've already read this data
   */
  public byte[] getPartDataAsBytes()
  {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    getPartData(baos);
    return baos.toByteArray();
  }


  /**
   * Gets the data contained in the current message part as
   * a String (this will return an empty String if you've already
   * got the data from this message part)
   *
   * @return a String containing the data in this message part,
   *         or an empty String if you've already read this data
   */
  public String getPartDataAsString()
  {
    return new String(getPartDataAsBytes());
  }


  /**
   * Writes the data contained in the current message part to
   * the OutputStream of your choice (this will return zero and
   * write nothing if you've already got the data from this
   * message part)
   *
   * @param outStream the OutputStream to write data to
   * @return a long value indicating the number of bytes
   *         written to your output stream
   */
  public int getPartData(OutputStream outStream)
  {
    int count = 0;
    String line;

    // if we've already got the data for this part, don't
    // even try to read any further (because we should be
    // at the next separator, or at the end of the stream)
    if (justGotPart)
    {
      return 0;
    }

    // make sure we're buffering our output, for efficiency
    BufferedOutputStream out = null;
    if (outStream != null)
    {
      out = new BufferedOutputStream(outStream, 1024);

      // start getting data -- this is going to seem a little cumbersome because
      // technically the CRLF (\r\n) that is supposed to appear just before the
      // separator actually belongs to the separator, not to the body data (if the
      // body is binary, an extra CRLF at the end could screw it up), so we're
      // always writing the previous line until we find the separator
      byte[] blineLast = new byte[0];
      byte[] bline = readLine(in);

      while (bline.length > 0)
      {
        line = new String(bline);
        if ((separator.length() > 0) && (line.startsWith(separator)))
        {
          // once we've found the next separator, make sure we write the
          // data in the last line, minus the CRLF that's supposed to be
          // at the end (just to be nice, we'll even try to act properly
          // if the line terminates with a \n instead of a \r\n)
          if (blineLast.length > 1)
          {
            int len = (blineLast[blineLast.length - 2] == (byte) '\r') ?
                      blineLast.length - 2 : blineLast.length - 1;
            count += writeOut(out, blineLast, len);
          }
          lastSeparator = line.trim();
          break;
        }
        else
        {
          // once we've found the next separator, make sure we write the
          // data in the last line, minus the CRLF that's supposed to be
          // at the end (just to be nice, we'll even try to act properly
          // if the line terminates with a \n instead of a \r\n)
          if (blineLast.length > 1)
          {
            int len = (blineLast[blineLast.length - 2] == (byte) '\r') ?
                      blineLast.length - 1 : blineLast.length;
            blineLast[len - 1] = (byte) '\n';
            count += writeOut(out, blineLast, len);
          }
          blineLast = bline;
          // read the next line
          bline = readLine(in);
        }
      }

      // if we hit the end of the file, make sure we write the blineLast
      // data before we finish up
      if ((bline.length == 0) && (blineLast.length > 0))
      {
        count += writeOut(out, blineLast, blineLast.length);
      }

      // flush the buffered stream, to make sure the original
      // output stream gets everything
      if (out != null)
      {
        try
        {
          out.flush();
        }
        catch (Exception e)
        {
          // Do nothing - not much we can do....
        }
      }

      justGotPart = true;

    }
    return count;
  }


  /*
   * A private method that tries to write a byte array to an OutputStream,
   * and returns the number of bytes that were written (0 if there was an error).
   * It's just a way of checking the stream and catching the exceptions in
   * one place, so we don't have to keep duplicating this logic in different
   * places in our code.
   */

  private int writeOut(OutputStream out, byte[] data, int len)
  {
    // don't even try if the OutputStream is null
    if (out == null)
    {
      return 0;
    }

    try
    {
      out.write(data, 0, len);
      return len;
    }
    catch (Exception e)
    {
      return 0;
    }
  }


  /**
   * A convenience method to get the Content-Type for the current
   * message part
   *
   * @return a String containing the Content-Type entry of the header,
   *         if it's available; null if it's not
   */
  public String getPartType()
  {
    return getHeaderValue(lastHeader, "Content-Type");
  }


  /**
   * A convenience method to get the Content-Transfer-Encoding for the current
   * message part
   *
   * @return a String containing the Content-Transfer-Encoding entry of the header,
   *         if it's available; null if it's not
   */
  public String getPartEncoding()
  {
    return getHeaderValue(lastHeader, "Content-Transfer-Encoding");
  }


  /**
   * A convenience method to get the Content-ID for the current
   * message part
   *
   * @return a String containing the Content-ID entry of the header,
   *         if it's available; null if it's not
   */
  public String getPartID()
  {
    return getHeaderValue(lastHeader, "Content-ID");
  }


  /**
   * Gets the specified value from a specified header, or null if
   * the entry does not exist
   *
   * @param header the header to look at
   * @param entry the name of the entry you're looking for
   * @return a String containing the value you're looking for,
   *         or null if the entry cannot be found
   */
  public static String getHeaderValue(String header, String entry)
  {
    String line = "";
    StringBuilder value = new StringBuilder();
    boolean gotit = false;
    // use the lowercase version of the name, to avoid any case issues
    String entryString = entry.toLowerCase();

    if (!entryString.endsWith(":"))
    {
      entryString = entryString + ":";
    }

    StringTokenizer st = new StringTokenizer(header, "\r\n");
    while (st.hasMoreTokens())
    {
      line = st.nextToken();
      if (line.toLowerCase().startsWith(entryString))
      {
        value = new StringBuilder(line.substring(entryString.length()).trim());
        gotit = true;
      }
      else if ((gotit) && (line.length() > 0))
      {
        // headers can actually span multiple lines, as long as
        // the next line starts with whitespace
        if (Character.isWhitespace(line.charAt(0)))
        {
          value.append( " " );
          value.append(line.trim());
        }
        else
        {
          gotit = false;
        }
      }
    }

    return value.toString();
  }


  /*
   * A private method to get the next header block on the InputStream.
   * For our purposes, a header is a block of text that ends with a
   * blank line.
   */

  private String getHeader()
  {
    StringBuilder header = new StringBuilder("");
    String line;
    byte[] bline = readLine(in);
    while (bline.length > 0)
    {
      line = new String(bline);
      if (line.trim().length() == 0)
      {
        break;
      }
      else
      {
        header.append(line);
        bline = readLine(in);
      }
    }

    return header.toString();
  }
  /*
   * A private method to get the next header block on the InputStream.
   * For our purposes, a header is a block of text that ends with a
   * blank line.
   */

  private String getSeparator()
  {
    StringBuilder newSeparator = new StringBuilder("");
    String line;
    byte[] bline = readLine(in);
    line = new String(bline);
    newSeparator.append(line);
    return newSeparator.toString();
  }


  /*
   * A private method to attempt to read the MIME separator from the
   * Content-Type entry in the first header it finds. This should be
   * called once, when the class is first instantiated.
   */

  private void getMimeSeparator()
  {

    // get the Content-Type entry in the header, and read the
    // separator (if any)
    // Rapid7 starts off with the separator - then follows with the content type - encoding is strange....
    //value = getHeaderValue(docHeader, "content-type");
    separator = getSeparator();

    separator = separator.trim();

    // Rapid7 is strange - no separator if it failed..... Straight to the response!
    if (!separator.startsWith("--"))
    {
      firstLine = separator;
      separator = "";

    }

    // if we didn't find a separator, we'll treat this as a
    // single-part message (which means we set justGotPart
    // to true so we don't go looking for anything when we
    // call nextPart() the first time)
    if (separator.length() == 0)
    {
      justGotPart = false;
    }
    else
    {
      //separator = "--" + separator;
      while (docHeader.trim().length() == 0)
      {
        docHeader += getHeader();
      }
    }
  }


  /*
   * A way to read a single "line" of bytes from an InputStream.
   * The byte array that is returned will include the line
   * terminator (\n), unless we reached the end of the stream.
   */

  private byte[] readLine(Reader in)
  {
    // we made readBuffer global, so we don't have to keep recreating it
    //ByteArrayOutputStream readBuffer = new ByteArrayOutputStream(1024);
    readBuffer.reset();
    int c;

    try
    {
      // read the bytes one-by-one until we hit a line terminator
      // or the end of the file (we're only checking for \n here,
      // although if we really wanted to be picky we'd probably
      // check for \r and \0 as well)
      while ((c = in.read()) != -1)
      {
        readBuffer.write(c);
        if ((char) c == '\n')
        {
          break;
        }
      }

    }
    catch (Exception e)
    {
      // we're not reporting any exceptions here
    }

    // and return what we have
    return readBuffer.toByteArray();
  }

}
