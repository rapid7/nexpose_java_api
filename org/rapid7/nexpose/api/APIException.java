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

/**
 * General NeXpose API exception that is used to report API errors.
 *
 * @author Chad Loder
 */
@SuppressWarnings("serial")
public class APIException extends Exception
{
  /**
   * Place holder for the response associated with the exception.  Null if the exception is not
   * associated with a response.
   */
  private final String response;

  /**
   * Place holder for the request associated with the exception.  Null if the exception is not
   * associated with a response.
   */
  private final String request;

  /**
   * Constructs a new exception with the specified detail message.  The
   * cause is not initialized, and may subsequently be initialized by
   * a call to {@link #initCause}.
   *
   * @param message the detail message. The detail message is saved for
   * later retrieval by the {@link #getMessage()} method.
   */
  public APIException(String message, String request, String response)
  {
    super(message);
    this.request = request;
    this.response = response;
  }

  /**
   * Constructs a new exception with the specified detail message and
   * cause.  <p>Note that the detail message associated with
   * <code>cause</code> is <i>not</i> automatically incorporated in
   * this exception's detail message.
   *
   * @param message the detail message (which is saved for later retrieval
   * by the {@link #getMessage()} method).
   * @param cause the cause (which is saved for later retrieval by the
   * {@link #getCause()} method).  (A <tt>null</tt> value is
   * permitted, and indicates that the cause is nonexistent or
   * unknown.)
   * @since 1.4
   */
  public APIException(String message, Throwable cause, String request, String response)
  {
    super(message, cause);
    this.request = request;
    this.response = response;
  }

  /////////////////////////////////////////////////////////////////////////
   // Public methods
   /////////////////////////////////////////////////////////////////////////

   /**
    * Constructs a new {@link APIException} with the given error message.
    *
    * @param msg the message to create the {@link Exception} with.
    */
   public APIException(String msg)
   {
      this(msg, (String)null, (String)null);
   }

   /**
    * Constructs a new {@link APIException} with the given error message and root
    * cause.
    *
    * @param msg the message to create the exception with.
    * @param cause the cause of the {@link Exception}
    */
   public APIException(String msg, Throwable cause)
   {
      this(msg, cause, null, null);
   }

   /**
    * @return Returns the API Response associated with this error.  Null if the error is not associated with a response.
    */
   public String getResponse()
   {
     return response;
   }

  /**
   * @return Returns the API Request associated with this error.  Null if the error is not associated with a response.
   */
  public String getRequest()
  {
    return request;
  }
}
