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


import javax.portlet.Portlet;
import javax.portlet.PortletContext;
import javax.portlet.PortletRequestDispatcher;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Set;


// Mock Object for PortletContext
public class MockPortletContext implements PortletContext {


    private int majorVersion = 1;
    private int minorVersion = 0;
    private String portletContextName = "MockPortletContext";
    private String serverInfo = portletContextName;
    private Hashtable parameters = new Hashtable();
    private Hashtable attributes = new Hashtable();


    // --------------------------------------------------------- Public Methods


    public void setPortletContextName(String portletContextName) {
        this.portletContextName = portletContextName;
    }

    public void setServerInfo(String serverInfo) {
        this.serverInfo = serverInfo;
    }

    public void addInitParameter(String name, String value) {
        parameters.put(name, value);
    }


    // ------------------------------------------------- PortletContext Methods


    public Object getAttribute(String name) {
        return attributes.get(name);
    }

    public Enumeration getAttributeNames() {
        return attributes.keys();
    }

    public String getInitParameter(String name) {
        return (String)parameters.get(name);
    }

    public Enumeration getInitParameterNames() {
        return parameters.keys();
    }

    public int getMajorVersion() {
        return majorVersion;
    }

    public String getMimeType(String path) {
        throw new UnsupportedOperationException();
    }

    public int getMinorVersion() {
        return minorVersion;
    }

    public PortletRequestDispatcher getNamedDispatcher(String name) {
        throw new UnsupportedOperationException();
    }

    public String getPortletContextName() {
        return portletContextName;
    }

    public String getRealPath(String path) {
        throw new UnsupportedOperationException();
    }

    public PortletRequestDispatcher getRequestDispatcher(String path) {
        throw new UnsupportedOperationException();
    }

    public URL getResource(String path) throws MalformedURLException {
        throw new UnsupportedOperationException();
    }

    public InputStream getResourceAsStream(String path) {
        throw new UnsupportedOperationException();
    }

    public Set getResourcePaths(String path) {
        throw new UnsupportedOperationException();
    }

    public String getServerInfo() {
        return serverInfo;
    }

    public void log(String message) {
        throw new UnsupportedOperationException();
    }

    public void log(String message, Throwable exception) {
        throw new UnsupportedOperationException();
    }

    public void removeAttribute(String name) {
        attributes.remove(name);
    }

    public void setAttribute(String name, Object value) {
        attributes.put(name, value);
    }

}
