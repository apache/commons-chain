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
package org.apache.commons.chain2.web.portlet;

import org.apache.commons.chain2.web.WebContextBase;

import javax.portlet.PortletContext;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.servlet.http.Cookie;
import java.util.Collections;
import java.util.Map;

/**
 * <p>Concrete implementation of {@link org.apache.commons.chain2.web.WebContext} suitable for use in
 * portlets.  The abstract methods are mapped to the appropriate
 * collections of the underlying portlet context, request, and response
 * instances that are passed to the constructor (or the initialize method).</p>
 *
 */
public class PortletWebContext extends WebContextBase {

    /**
     *
     */
    private static final long serialVersionUID = 20120724L;

    // ------------------------------------------------------------ Constructors

    /**
     * <p>Construct an uninitialized {@link PortletWebContext} instance.</p>
     */
    public PortletWebContext() {
    }

    /**
     * <p>Construct a {@link PortletWebContext} instance that is initialized
     * with the specified Portlet API objects.</p>
     *
     * @param context The <code>PortletContext</code> for this web application
     * @param request The <code>PortletRequest</code> for this request
     * @param response The <code>PortletResponse</code> for this request
     */
    public PortletWebContext(PortletContext context,
                             PortletRequest request,
                             PortletResponse response) {
        initialize(context, request, response);
    }

    // ------------------------------------------------------ Instance Variables

    /**
     * <p>The lazily instantiated <code>Map</code> of application scope
     * attributes.</p>
     */
    private Map<String, Object> applicationScope;

    /**
     * <p>The <code>PortletContext</code> for this web application.</p>
     */
    private PortletContext context;

    /**
     * <p>The lazily instantiated <code>Map</code> of header name-value
     * combinations (immutable).</p>
     */
    private Map<String, String> header;

    /**
     * <p>The lazily instantitated <code>Map</code> of header name-values
     * combinations (immutable).</p>
     */
    private Map<String, String[]> headerValues;

    /**
     * <p>The lazily instantiated <code>Map</code> of context initialization
     * parameters.</p>
     */
    private Map<String, String> initParam;

    /**
     * <p>The lazily instantiated <code>Map</code> of request
     * parameter name-value.</p>
     */
    private Map<String, String> param;

    /**
     * <p>The lazily instantiated <code>Map</code> of request
     * parameter name-values.</p>
     */
    private Map<String, String[]> paramValues;

    /**
     * <p>The <code>PortletRequest</code> for this request.</p>
     */
    private PortletRequest request;

    /**
     * <p>The lazily instantiated <code>Map</code> of request scope
     * attributes.</p>
     */
    private Map<String, Object> requestScope;

    /**
     * <p>The <code>PortletResponse</code> for this request.</p>
     */
    private PortletResponse response;

    /**
     * <p>The lazily instantiated <code>Map</code> of session scope
     * attributes.</p>
     */
    private Map<String, Object> sessionScope;

    // ---------------------------------------------------------- Public Methods

    /**
     * <p>Return the {@link PortletContext} for this context.</p>
     *
     * @return The <code>PortletContext</code> for this request
     */
    public PortletContext getContext() {
        return (this.context);
    }

    /**
     * <p>Return the {@link PortletRequest} for this context.</p>
     *
     * @return The <code>PortletRequest</code> for this context.
     */
    public PortletRequest getRequest() {
        return (this.request);
    }

    /**
     * <p>Return the {@link PortletResponse} for this context.</p>
     *
     * @return The <code>PortletResponse</code> for this context.
     */
    public PortletResponse getResponse() {
        return (this.response);
    }

    /**
     * <p>Initialize (or reinitialize) this {@link PortletWebContext} instance
     * for the specified Portlet API objects.</p>
     *
     * @param context The <code>PortletContext</code> for this web application
     * @param request The <code>PortletRequest</code> for this request
     * @param response The <code>PortletResponse</code> for this request
     */
    public void initialize(PortletContext context,
                           PortletRequest request,
                           PortletResponse response) {
        // Save the specified Portlet API object references
        this.context = context;
        this.request = request;
        this.response = response;

        // Perform other setup as needed
    }

    /**
     * <p>Release references to allocated resources acquired in
     * <code>initialize()</code> of via subsequent processing.  After this
     * method is called, subsequent calls to any other method than
     * <code>initialize()</code> will return undefined results.</p>
     */
    public void release() {
        // Release references to allocated collections
        applicationScope = null;
        header = null;
        headerValues = null;
        initParam = null;
        param = null;
        paramValues = null;
        requestScope = null;
        sessionScope = null;

        // Release references to Portlet API objects
        context = null;
        request = null;
        response = null;
    }

    // ------------------------------------------------------ WebContextBase Methods

    /**
     * See the {@link org.apache.commons.chain2.web.WebContext}'s Javadoc.
     *
     * @return Application scope Map.
     */
    public Map<String, Object> getApplicationScope() {
        if ((applicationScope == null) && (context != null)) {
            applicationScope = new PortletApplicationScopeMap(context);
        }
        return (applicationScope);
    }

    /**
     * See the {@link org.apache.commons.chain2.web.WebContext}'s Javadoc.
     *
     * @return Header values Map.
     */
    public Map<String, String> getHeader() {
        if ((header == null) && (request != null)) {
            // header = new PortletHeaderMap(request);
            header = Collections.emptyMap();
        }
        return (header);
    }

    /**
     * See the {@link org.apache.commons.chain2.web.WebContext}'s Javadoc.
     *
     * @return Header values Map.
     */
    public Map<String, String[]> getHeaderValues() {
        if ((headerValues == null) && (request != null)) {
            // headerValues = new PortletHeaderValuesMap(request);
            headerValues = Collections.emptyMap();
        }
        return (headerValues);
    }

    /**
     * See the {@link org.apache.commons.chain2.web.WebContext}'s Javadoc.
     *
     * @return Initialization parameter Map.
     */
    public Map<String, String> getInitParam() {
        if ((initParam == null) && (context != null)) {
            initParam = new PortletInitParamMap(context);
        }
        return (initParam);
    }

    /**
     * See the {@link org.apache.commons.chain2.web.WebContext}'s Javadoc.
     *
     * @return Request parameter Map.
     */
    public Map<String, String> getParam() {
        if ((param == null) && (request != null)) {
            param = new PortletParamMap(request);
        }
        return (param);
    }

    /**
     * See the {@link org.apache.commons.chain2.web.WebContext}'s Javadoc.
     *
     * @return Request parameter Map.
     */
    public Map<String, String[]> getParamValues() {
        if ((paramValues == null) && (request != null)) {
            paramValues = new PortletParamValuesMap(request);
        }
        return (paramValues);
    }

    /**
     * Returns an empty Map - portlets don't support Cookies.
     *
     * @return An empty Map.
     * @since Chain 1.1
     */
    public Map<String, Cookie> getCookies() {
        return Collections.emptyMap();
    }

    /**
     * See the {@link org.apache.commons.chain2.web.WebContext}'s Javadoc.
     *
     * @return Request scope Map.
     */
    public Map<String, Object> getRequestScope() {
        if ((requestScope == null) && (request != null)) {
            requestScope = new PortletRequestScopeMap(request);
        }
        return (requestScope);
    }

    /**
     * See the {@link org.apache.commons.chain2.web.WebContext}'s Javadoc.
     *
     * @return Session scope Map.
     */
    public Map<String, Object> getSessionScope() {
        if ((sessionScope == null) && (request != null)) {
            sessionScope =
            new PortletSessionScopeMap(request);
        }
        return (sessionScope);
    }

}
