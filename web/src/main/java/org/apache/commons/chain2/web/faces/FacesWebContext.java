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
package org.apache.commons.chain2.web.faces;

import org.apache.commons.chain2.web.WebContextBase;

import javax.faces.context.FacesContext;
import javax.servlet.http.Cookie;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

/**
 * <p>Concrete implementation of {@link org.apache.commons.chain2.web.WebContext} suitable for use in
 * JavaServer Faces apps.  The abstract methods are mapped to the appropriate
 * collections of the underlying <code>FacesContext</code> instance
 * that is passed to the constructor (or the initialize method).</p>
 *
 * @version $Id$
 */
public class FacesWebContext extends WebContextBase {

    // ------------------------------------------------------------ Constructors

    /**
     *
     */
    private static final long serialVersionUID = 20120724L;

    /**
     * <p>Construct an uninitialized {@link FacesWebContext} instance.</p>
     */
    public FacesWebContext() {
    }

    /**
     * <p>Construct a {@link FacesWebContext} instance that is initialized
     * with the specified JavaServer Faces API objects.</p>
     *
     * @param context The <code>FacesContext</code> for this request
     */
    public FacesWebContext(FacesContext context) {
        initialize(context);
    }

    // ------------------------------------------------------ Instance Variables

    /**
     * <p>The <code>FacesContext</code> instance for the request represented
     * by this {@link org.apache.commons.chain2.web.WebContext}.</p>
     */
    private FacesContext context = null;

    // ---------------------------------------------------------- Public Methods

    /**
     * <p>Return the <code>FacesContext</code> instance for the request
     * associated with this {@link FacesWebContext}.</p>
     *
     * @return The <code>FacesContext</code> for this request
     */
    public FacesContext getContext() {
        return (this.context);
    }

    /**
     * <p>Initialize (or reinitialize) this {@link FacesWebContext} instance
     * for the specified JavaServer Faces API objects.</p>
     *
     * @param context The <code>FacesContext</code> for this request
     */
    public void initialize(FacesContext context) {
        this.context = context;
    }

    /**
     * <p>Release references to allocated resources acquired in
     * <code>initialize()</code> of via subsequent processing.  After this
     * method is called, subsequent calls to any other method than
     * <code>initialize()</code> will return undefined results.</p>
     */
    public void release() {
        context = null;
    }

    // ------------------------------------------------------ WebContextBase Methods

    /**
     * See the {@link org.apache.commons.chain2.web.WebContext}'s Javadoc.
     *
     * @return Application scope Map.
     */
    public Map<String, Object> getApplicationScope() {
        @SuppressWarnings("unchecked") // Assume faces is following contract
        Map<String, Object> scope = context.getExternalContext().getApplicationMap();
        return (scope);
    }

    /**
     * See the {@link org.apache.commons.chain2.web.WebContext}'s Javadoc.
     *
     * @return Header values Map.
     */
    public Map<String, String> getHeader() {
        @SuppressWarnings("unchecked") // Assume faces is following contract
        Map<String, String> headers = context.getExternalContext().getRequestHeaderMap();

        return (headers);
    }

    /**
     * See the {@link org.apache.commons.chain2.web.WebContext}'s Javadoc.
     *
     * @return Header values Map.
     */
    public Map<String, String[]> getHeaderValues() {
        @SuppressWarnings("unchecked") // Assume faces is following contract
        Map<String, String[]> headerValues = context.getExternalContext().getRequestHeaderValuesMap();

        return (headerValues);
    }

    /**
     * See the {@link org.apache.commons.chain2.web.WebContext}'s Javadoc.
     *
     * @return Initialization parameter Map.
     */
    public Map<String, String> getInitParam() {
        @SuppressWarnings("unchecked") // Assume faces is following contract
        Map<String, String> initParams = context.getExternalContext().getInitParameterMap();

        return (initParams);
    }

    /**
     * See the {@link org.apache.commons.chain2.web.WebContext}'s Javadoc.
     *
     * @return Request parameter Map.
     */
    public Map<String, String> getParam() {
        @SuppressWarnings("unchecked")
        Map<String, String> params = context.getExternalContext().getRequestParameterMap();

        return (params);
    }

    /**
     * See the {@link org.apache.commons.chain2.web.WebContext}'s Javadoc.
     *
     * @return Request parameter Map.
     */
    public Map<String, String[]> getParamValues() {
        @SuppressWarnings("unchecked") // Assume faces is following contract
        Map<String, String[]> paramValues = context.getExternalContext().getRequestParameterValuesMap();

        return (paramValues);
    }

    /**
     * See the {@link org.apache.commons.chain2.web.WebContext}'s Javadoc.
     *
     * @return Map of Cookies.
     * @since Chain 1.1
     */
    public Map<String, Cookie> getCookies() {
        @SuppressWarnings("unchecked") // Assume faces is following contract
        Map<String, Object> facesCookieMap = context.getExternalContext().getRequestCookieMap();

        /* This ugly hack was done because the faces API implements
         * the cookie collection using <String, Object> instead of <String, Cookie>.
         * So we painstakingly check for type safety and cast it as a Map<String, Cookie>.
         */
        Iterator<Object> itr = facesCookieMap.values().iterator();

        if (itr.hasNext()) {
            Object cookieObj = itr.next();

            if (cookieObj instanceof Cookie) {
                // See comment above about type safety check
                @SuppressWarnings("unchecked")
                Map<String, Cookie> cookieMap = Collections.checkedMap(
                        (Map) facesCookieMap, String.class, Cookie.class);

                return cookieMap;
            } else {
                String msg = "Could not cast cookie Map into <String, Cookie>. " +
                        "Actual Cookie type is: " + cookieObj.getClass().toString();
                throw new ClassCastException(msg);
            }
        } else {
            return Collections.emptyMap();
        }
    }

    /**
     * See the {@link org.apache.commons.chain2.web.WebContext}'s Javadoc.
     *
     * @return Request scope Map.
     */
    public Map<String, Object> getRequestScope() {
        @SuppressWarnings("unchecked")  // Assume faces is following contract
        Map<String, Object> scope = context.getExternalContext().getRequestMap();

        return (scope);
    }

    /**
     * See the {@link org.apache.commons.chain2.web.WebContext}'s Javadoc.
     *
     * @return Session scope Map.
     */
    public Map<String, Object> getSessionScope() {
        @SuppressWarnings("unchecked")  // Assume faces is following contract
        Map<String, Object> scope = context.getExternalContext().getSessionMap();

        return (scope);
    }

}
