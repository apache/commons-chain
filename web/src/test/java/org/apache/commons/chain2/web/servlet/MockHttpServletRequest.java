/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.chain2.web.servlet;


import org.apache.commons.chain2.web.MockEnumeration;
import org.apache.commons.chain2.web.MockPrincipal;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.security.Principal;
import java.util.*;


// Mock Object for HttpServletRequest (Version 2.3)
public class MockHttpServletRequest implements HttpServletRequest {


    public MockHttpServletRequest() {
    }


    public MockHttpServletRequest(HttpSession session) {
        setHttpSession(session);
    }


    public MockHttpServletRequest(String contextPath, String servletPath,
                                  String pathInfo, String queryString) {
        setPathElements(contextPath, servletPath, pathInfo, queryString);
    }



    public MockHttpServletRequest(String contextPath, String servletPath,
                                  String pathInfo, String queryString,
                                  HttpSession session) {
        setPathElements(contextPath, servletPath, pathInfo, queryString);
        setHttpSession(session);
    }



    protected Map<String, Object> attributes = new HashMap<String, Object>();
    protected String contextPath;
    protected Map<String, String[]> headers = new HashMap<String, String[]>();
    protected Cookie[] cookies = new Cookie[0];
    protected Locale locale;
    protected Map<String, String[]> parameters = new HashMap<String, String[]>();
    protected String pathInfo;
    protected Principal principal;
    protected String queryString;
    protected String servletPath;
    protected HttpSession session;


    // --------------------------------------------------------- Public Methods


    public void addHeader(String name, String value) {
        String values[] = headers.get(name);
        if (values == null) {
            String results[] = new String[] { value };
            headers.put(name, results);
            return;
        }
        String results[] = new String[values.length + 1];
        System.arraycopy(values, 0, results, 0, values.length);
        results[values.length] = value;
        headers.put(name, results);
    }


    public void addParameter(String name, String value) {
        String values[] = (String[]) parameters.get(name);
        if (values == null) {
            String results[] = new String[] { value };
            parameters.put(name, results);
            return;
        }
        String results[] = new String[values.length + 1];
        System.arraycopy(values, 0, results, 0, values.length);
        results[values.length] = value;
        parameters.put(name, results);
    }

    public void addCookie(String name, String value) {
        addCookie(new Cookie(name, value));
    }

    public void addCookie(Cookie cookie) {
        Cookie[] newValues = new Cookie[cookies.length + 1];
        System.arraycopy(cookies, 0, newValues, 0, cookies.length);
        cookies = newValues;
        cookies[cookies.length - 1] = cookie;
    }


    public void setHttpSession(HttpSession session) {
        this.session = session;
    }


    public void setLocale(Locale locale) {
        this.locale = locale;
    }


    public void setPathElements(String contextPath, String servletPath,
                                String pathInfo, String queryString) {

        this.contextPath = contextPath;
        this.servletPath = servletPath;
        this.pathInfo = pathInfo;
        this.queryString = queryString;

    }


    public void setUserPrincipal(Principal principal) {
        this.principal = principal;
    }



    // --------------------------------------------- HttpServletRequest Methods


    public String getAuthType() {
        throw new UnsupportedOperationException();
    }


    public String getContextPath() {
        return (contextPath);
    }


    public Cookie[] getCookies() {
        return cookies;
    }


    public long getDateHeader(String name) {
        throw new UnsupportedOperationException();
    }


    public String getHeader(String name) {
        String values[] = (String[]) headers.get(name);
        if (values != null) {
            return (values[0]);
        } else {
            return (null);
        }
    }


    public Enumeration<String> getHeaderNames() {
        return (new MockEnumeration<String>(headers.keySet().iterator()));
    }


    public Enumeration<String> getHeaders(String name) {
        String headers[] = this.headers.get(name);
        if (headers == null) {
            headers = new String[0];
        }
        return (new MockEnumeration<String>(Arrays.asList(headers).iterator()));
    }


    public int getIntHeader(String name) {
        throw new UnsupportedOperationException();
    }


    public String getMethod() {
        throw new UnsupportedOperationException();
    }


    public String getPathInfo() {
        return (pathInfo);
    }


    public String getPathTranslated() {
        throw new UnsupportedOperationException();
    }


    public String getQueryString() {
        return (queryString);
    }


    public String getRemoteUser() {
        if (principal != null) {
            return (principal.getName());
        } else {
            return (null);
        }
    }


    public String getRequestedSessionId() {
        throw new UnsupportedOperationException();
    }


    public String getRequestURI() {
        StringBuilder sb = new StringBuilder();
        if (contextPath != null) {
            sb.append(contextPath);
        }
        if (servletPath != null) {
            sb.append(servletPath);
        }
        if (pathInfo != null) {
            sb.append(pathInfo);
        }
        if (sb.length() > 0) {
            return (sb.toString());
        }
        throw new UnsupportedOperationException();
    }


    public StringBuffer getRequestURL() {
        throw new UnsupportedOperationException();
    }


    public String getServletPath() {
        return (servletPath);
    }


    public HttpSession getSession() {
        return (getSession(true));
    }


    public HttpSession getSession(boolean create) {
        if (create && (session == null)) {
            throw new UnsupportedOperationException();
        }
        return (session);
    }


    public Principal getUserPrincipal() {
        return (principal);
    }


    public boolean isRequestedSessionIdFromCookie() {
        throw new UnsupportedOperationException();
    }


    public boolean isRequestedSessionIdFromUrl() {
        throw new UnsupportedOperationException();
    }


    public boolean isRequestedSessionIdFromURL() {
        throw new UnsupportedOperationException();
    }


    public boolean isRequestedSessionIdValid() {
        throw new UnsupportedOperationException();
    }


    public boolean isUserInRole(String role) {
        if ((principal != null) && (principal instanceof MockPrincipal)) {
            return (((MockPrincipal) principal).isUserInRole(role));
        } else {
            return (false);
        }
    }


    // ------------------------------------------------- ServletRequest Methods


    public Object getAttribute(String name) {
        return (attributes.get(name));
    }


    public Enumeration<String> getAttributeNames() {
        return (new MockEnumeration<String>(attributes.keySet().iterator()));
    }


    public String getCharacterEncoding() {
        throw new UnsupportedOperationException();
    }


    public int getContentLength() {
        throw new UnsupportedOperationException();
    }


    public String getContentType() {
        throw new UnsupportedOperationException();
    }


    public ServletInputStream getInputStream() {
        throw new UnsupportedOperationException();
    }


    public Locale getLocale() {
        return (locale);
    }


    public Enumeration<Locale> getLocales() {
        throw new UnsupportedOperationException();
    }


    public String getLocalAddr() {
        throw new UnsupportedOperationException();
    }


    public String getLocalName() {
    throw new UnsupportedOperationException();
    }


    public int getLocalPort() {
    throw new UnsupportedOperationException();
    }


    public String getParameter(String name) {
        String values[] = parameters.get(name);
        if (values != null) {
            return (values[0]);
        } else {
            return (null);
        }
    }


    public Map<String, String[]> getParameterMap() {
        return (parameters);
    }


    public Enumeration<String> getParameterNames() {
        return (new MockEnumeration<String>(parameters.keySet().iterator()));
    }


    public String[] getParameterValues(String name) {
        return ((String[]) parameters.get(name));
    }


    public String getProtocol() {
        throw new UnsupportedOperationException();
    }


    public BufferedReader getReader() {
        throw new UnsupportedOperationException();
    }


    public String getRealPath(String path) {
        throw new UnsupportedOperationException();
    }


    public String getRemoteAddr() {
        throw new UnsupportedOperationException();
    }


    public String getRemoteHost() {
        throw new UnsupportedOperationException();
    }


    public int getRemotePort() {
    throw new UnsupportedOperationException();
    }


    public RequestDispatcher getRequestDispatcher(String path) {
        throw new UnsupportedOperationException();
    }


    public String getScheme() {
        return ("http");
    }


    public String getServerName() {
        return ("localhost");
    }


    public int getServerPort() {
        return (8080);
    }


    public boolean isSecure() {
        return (false);
    }


    public void removeAttribute(String name) {
        attributes.remove(name);
    }


    public void setAttribute(String name, Object value) {
        if (value == null) {
            attributes.remove(name);
        } else {
            attributes.put(name, value);
        }
    }


    public void setCharacterEncoding(String name) {
        throw new UnsupportedOperationException();
    }


}
