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
package org.apache.commons.chain.web.servlet;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

import org.apache.commons.chain.web.MockEnumeration;

/**
 * Mock {@link ServletConfig} implementation.
 */
public class MockServletConfig implements ServletConfig {
    private final String servletName;
    private final ServletContext servletContext;
    private final Map parameters = new HashMap();

    /**
     * Default Constructor.
     */
    public MockServletConfig() {
        this("unspecified", new MockServletContext());
    }

    /**
     * Construct an instance with the specified name.
     *
     * @param servletName the servlet name
     */
    public MockServletConfig(String servletName) {
        this(servletName, new MockServletContext());
    }

    /**
     * Construct an instance with the specified name and context.
     *
     * @param servletName the servlet name
     * @param servletContext the servlet context
     */
    public MockServletConfig(String servletName, ServletContext servletContext) {
        this.servletName = servletName;
        this.servletContext = servletContext;
    }

    /**
     * Get a specified init parameter.
     *
     * @param name parameter name
     * @return the parameter value
     */
    public String getInitParameter(String name) {
        return (String)parameters.get(name);
    }

    /**
     * Get the init parameter names.
     *
     * @return the set of parameter names
     */
    public Enumeration getInitParameterNames() {
        return (new MockEnumeration(parameters.keySet().iterator()));
    }

    /**
     * Get the servlet context.
     *
     * @return the servlet context
     */
    public ServletContext getServletContext() {
        return servletContext;
    }

    /**
     * Return the servlet name.
     *
     * @return The servlet name
     */
    public String getServletName() {
        return servletName;
    }

    /**
     * Set a specified init parameter.
     *
     * @param name parameter name
     * @param value the parameter value
     */
    public void setInitParameter(String name, String value) {
        parameters.put(name, value);
    }

}
