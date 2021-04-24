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

import org.apache.commons.chain2.web.WebContextBase;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * <p>Concrete implementation of {@link org.apache.commons.chain2.web.WebContext} suitable for use in
 * Servlets and JSP pages.  The abstract methods are mapped to the appropriate
 * collections of the underlying servlet context, request, and response
 * instances that are passed to the constructor (or the initialize method).</p>
 *
 * @version $Id$
 */
public class ServletWebContextBase extends WebContextBase
        implements ServletWebContext<String, Object> {

    private static final long serialVersionUID = 20120724L;

    // ------------------------------------------------------------ Constructors

    /**
     * <p>Construct an uninitialized {@link ServletWebContextBase} instance.</p>
     */
    public ServletWebContextBase() {
    }

    /**
     * <p>Construct a {@link ServletWebContextBase} instance that is initialized
     * with the specified Servlet API objects.</p>
     *
     * @param context The <code>ServletContext</code> for this web application
     * @param request The <code>HttpServletRequest</code> for this request
     * @param response The <code>HttpServletResponse</code> for this request
     */
    public ServletWebContextBase(ServletContext context,
                                 HttpServletRequest request,
                                 HttpServletResponse response) {
        initialize(context, request, response);
    }

    // ------------------------------------------------------ Instance Variables

    /**
     * <p>The lazily instantiated <code>Map</code> of application scope
     * attributes.</p>
     */
    private Map<String, Object> applicationScope;

    /**
     * <p>The <code>ServletContext</code> for this web application.</p>
     */
    private ServletContext context;

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
     * <p>The lazily instantiated <code>Map</code> of cookies.</p>
     */
    private Map<String, Cookie> cookieValues;

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
     * <p>The <code>HttpServletRequest</code> for this request.</p>
     */
    private HttpServletRequest request;

    /**
     * <p>The lazily instantiated <code>Map</code> of request scope
     * attributes.</p>
     */
    private Map<String, Object> requestScope;

    /**
     * <p>The <code>HttpServletResponse</code> for this request.</p>
     */
    private HttpServletResponse response;

    /**
     * <p>The lazily instantiated <code>Map</code> of session scope
     * attributes.</p>
     */
    private Map<String, Object> sessionScope;

    // ---------------------------------------------------------- Public Methods

    public ServletContext getContext() {
        return (this.context);
    }

    public HttpServletRequest getRequest() {
        return (this.request);
    }

    public HttpServletResponse getResponse() {
        return (this.response);
    }

    public void initialize(ServletContext context,
                           HttpServletRequest request,
                           HttpServletResponse response) {
        // Save the specified Servlet API object references
        this.context = context;
        this.request = request;
        this.response = response;

        // Perform other setup as needed
    }

    public void release() {
        // Release references to allocated collections
        applicationScope = null;
        header = null;
        headerValues = null;
        initParam = null;
        param = null;
        paramValues = null;
        cookieValues = null;
        requestScope = null;
        sessionScope = null;

        // Release references to Servlet API objects
        context = null;
        request = null;
        response = null;
    }

    // ------------------------------------------------------ WebContextBase Methods

    public Map<String, Object> getApplicationScope() {
        if ((applicationScope == null) && (context != null)) {
            applicationScope = new ServletApplicationScopeMap(context);
        }
        return (applicationScope);
    }

    public Map<String, String> getHeader() {
        if ((header == null) && (request != null)) {
            header = new ServletHeaderMap(request);
        }
        return (header);
    }

    public Map<String, String[]> getHeaderValues() {
        if ((headerValues == null) && (request != null)) {
            headerValues = new ServletHeaderValuesMap(request);
        }
        return (headerValues);
    }

    public Map<String, String> getInitParam() {
        if ((initParam == null) && (context != null)) {
            initParam = new ServletInitParamMap(context);
        }
        return (initParam);
    }

    public Map<String, String> getParam() {
        if ((param == null) && (request != null)) {
            param = new ServletParamMap(request);
        }
        return (param);
    }

    public Map<String, String[]> getParamValues() {
        if ((paramValues == null) && (request != null)) {
            paramValues = new ServletParamValuesMap(request);
        }
        return (paramValues);
    }

    public Map<String, Cookie> getCookies() {
        if ((cookieValues == null) && (request != null)) {
            cookieValues = new ServletCookieMap(request);
        }
        return (cookieValues);
    }

    public Map<String, Object> getRequestScope() {
        if ((requestScope == null) && (request != null)) {
            requestScope = new ServletRequestScopeMap(request);
        }
        return (requestScope);
    }


    public Map<String, Object> getSessionScope() {
        if ((sessionScope == null) && (request != null)) {
            sessionScope = new ServletSessionScopeMap(request);
        }
        return (sessionScope);
    }

}
