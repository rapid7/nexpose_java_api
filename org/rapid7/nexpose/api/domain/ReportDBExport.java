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
package org.rapid7.nexpose.api.domain;

import org.rapid7.nexpose.api.*;
import org.rapid7.nexpose.api.generators.*;

public class ReportDBExport
{
  public ReportDBExport(String type, String userID, String password, String realm)
  {
    m_type = type;
    m_userid = userID;
    m_password = password;
    m_realm = realm;
    m_paramGenerator = new ReportDBExportParamsGenerator();
  }
  public String getType()
  {
    return m_type;
  }

  public void setType(String type)
  {
    this.m_type = type;
  }

  public String getUserID()
  {
    return m_userid;
  }

  public void setUserID(String userid)
  {
    this.m_userid = userid;
  }

  public String getPassword()
  {
    return m_password;
  }

  public void setPassword(String password)
  {
    this.m_password = password;
  }

  public String getRealm()
  {
    return m_realm;
  }

  public void setRealm(String realm)
  {
    this.m_realm = realm;
  }

  public IContentGenerator getParamGenerator()
  {
    return m_paramGenerator;
  }

  public void setParamGenerator(IContentGenerator paramGenerator)
  {
    this.m_paramGenerator = paramGenerator;
  }

  private String m_type = null;
  private String m_userid = null;
  private String m_password = null;
  private String m_realm = null;
  private IContentGenerator m_paramGenerator;
}
