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
package org.apache.commons.chain.web.portlet;


import org.apache.commons.chain.web.MockEnumeration;
import org.apache.commons.chain.web.MockPrincipal;

import javax.portlet.PortalContext;
import javax.portlet.PortletRequest;
import javax.portlet.PortletSession;
import javax.portlet.PortletContext;
import javax.portlet.PortletMode;
import javax.portlet.PortletPreferences;
import javax.portlet.WindowState;
import java.security.Principal;
import java.util.Map;
import java.util.HashMap;
import java.util.Enumeration;
import java.util.Locale;



// Mock Object for PortletRequest
public class MockPortletRequest implements PortletRequest {

    private String contextPath;
    private String authType;
    private Locale locale;
    private String scheme     = "http";
    private String serverName = "localhost";
    private int    serverPort = 8080;
    private PortalContext portalContext;
    private PortletContext context;
    private PortletSession session;
    private PortletMode portletMode;
    private PortletPreferences portletPreferences;
    private WindowState windowState;
    private Principal principal;
    private Map parameters = new HashMap();
    private Map attributes = new HashMap();
    private Map properties = new HashMap();


    public MockPortletRequest() {
        this(null, null, null);
    }

    public MockPortletRequest(String contextPath, PortletContext context, PortletSession session) {
        this.contextPath = contextPath;
        this.context = (context == null ? new MockPortletContext() : context);
        this.session = session;
    }

    // --------------------------------------------------------- Public Methods

    public void addParameter(String name, String value) {
        String values[] = (String[])parameters.get(name);
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

    public void addProperty(String name, String value) {
        String values[] = (String[])properties.get(name);
        if (values == null) {
            String results[] = new String[] { value };
            properties.put(name, results);
            return;
        }
        String results[] = new String[values.length + 1];
        System.arraycopy(values, 0, results, 0, values.length);
        results[values.length] = value;
        properties.put(name, results);
    }

    public void setAuthType(String authType) {
        this.authType = authType;
    }

    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public void setPortalContext(PortalContext portalContext) {
        this.portalContext = portalContext;
    }

    public void setPortletContext(PortletContext context) {
        this.context = context;
    }

    public void setPortletMode(PortletMode portletMode) {
        this.portletMode = portletMode;
    }

    public void setPortletPreferences(PortletPreferences portletPreferences) {
        this.portletPreferences = portletPreferences;
    }

    public void setPortletSession(PortletSession session) {
        this.session = session;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    public void setUserPrincipal(Principal principal) {
        this.principal = principal;
    }

    public void setUserPrincipal(WindowState windowState) {
        this.windowState = windowState;
    }


    // --------------------------------------------- PortletRequest Methods

    public Object getAttribute(String name) {
        return attributes.get(name);
    }

    public Enumeration getAttributeNames() {
        return new MockEnumeration(attributes.keySet().iterator());
    }

    public String getAuthType() {
        return authType;
    }

    public String getContextPath() {
        return contextPath;
    }

    public Locale getLocale() {
        return locale;
    }

    public Enumeration getLocales() {
        throw new UnsupportedOperationException();
    }

    public String getParameter(String name) {
        String values[] = (String[])parameters.get(name);
        if (values != null) {
            return values[0];
        } else {
            return null;
        }
    }

    public Map getParameterMap() {
        return parameters;
    }

    public Enumeration getParameterNames() {
        return new MockEnumeration(parameters.keySet().iterator());
    }

    public String[] getParameterValues(String name) {
        return (String[])parameters.get(name);
    }

    public PortalContext getPortalContext() {
        return portalContext; 
    }

    public PortletMode getPortletMode() {
        return portletMode; 
    }

    public PortletSession getPortletSession() {
        return getPortletSession(true);
    }

    public PortletSession getPortletSession(boolean create) {
        if (create && session == null) {
            session = new MockPortletSession(context);
        }
        return session;
    }

    public PortletPreferences getPreferences() {
        return portletPreferences; 
    }

    public Enumeration getProperties(String name) {
        throw new UnsupportedOperationException(); 
    }

    public String getProperty(String name) {
        String values[] = (String[])properties.get(name);
        if (values != null) {
            return values[0];
        } else {
            return null;
        }
     }

    public Enumeration getPropertyNames() {
        return new MockEnumeration(properties.keySet().iterator());
    }


    public String getRemoteUser() {
        if (principal != null) {
            return principal.getName();
        } else {
            return null;
        }
    }

    public String getRequestedSessionId() {
        throw new UnsupportedOperationException();
    }

    public String getResponseContentType() {
        throw new UnsupportedOperationException();
    }

    public Enumeration getResponseContentTypes() {
        throw new UnsupportedOperationException(); 
    }

    public String getScheme() {
        return scheme;
    }

    public String getServerName() {
        return serverName;
    }

    public int getServerPort() {
        return serverPort;
    }

    public Principal getUserPrincipal() {
        return principal;
    }

    public WindowState getWindowState() {
        return windowState;
    }

    public boolean isPortletModeAllowed(PortletMode mode) {
        throw new UnsupportedOperationException();
    }

    public boolean isRequestedSessionIdValid() {
        throw new UnsupportedOperationException();
    }

    public boolean isSecure() {
        return false;
    }

    public boolean isUserInRole(String role) {
        if ((principal != null) && (principal instanceof MockPrincipal)) {
            return ((MockPrincipal)principal).isUserInRole(role);
        } else {
            return false;
        }
    }

    public boolean isWindowStateAllowed(WindowState state) {
        throw new UnsupportedOperationException();
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

}
