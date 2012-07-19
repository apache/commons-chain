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
package org.apache.commons.chain2.web;

import java.util.Map;
import javax.servlet.http.Cookie;

import org.apache.commons.chain2.impl.ContextBase;

/**
 * <p>Abstract base implementation of {@link org.apache.commons.chain2.Context} that
 * provides web based applications that use it a "generic" view of HTTP related
 * requests and responses, without tying the application to a particular underlying
 * Java API (such as servlets).  It is expected that a concrete subclass
 * of {@link WebContext} for each API (such as
 * {@link org.apache.commons.chain2.web.servlet.ServletWebContext})
 * will support adapting that particular API's implementation of request
 * and response objects into this generic framework.</p>
 *
 * <p>The characteristics of a web request/response are made visible via
 * a series of JavaBeans properties (and mapped to read-only attributes
 * of the same name, as supported by {@link ContextBase}.</p>
 *
 * @version $Id$
 */
public abstract class WebContext extends ContextBase {

    /**
     *
     */
    private static final long serialVersionUID = 6804961872140299027L;

    // ---------------------------------------------------------- Public Methods

    /**
     * <p>Return a mutable <code>Map</code> that maps application scope
     * attribute names to their values.</p>
     *
     * @return Application scope Map.
     */
    public abstract Map<String, Object> getApplicationScope();

    /**
     * <p>Return an immutable <code>Map</code> that maps header names to
     * the first (or only) header value (as a String).  Header names must
     * be matched in a case-insensitive manner.</p>
     *
     * @return Header values Map.
     */
    public abstract Map<String, String> getHeader();

    /**
     * <p>Return an immutable <code>Map</code> that maps header names to
     * the set of all values specified in the request (as a String array).
     * Header names must be matched in a case-insensitive manner.</p>
     *
     * @return Header values Map.
     */
    public abstract Map<String, String[]> getHeaderValues();

    /**
     * <p>Return an immutable <code>Map</code> that maps context application
     * initialization parameters to their values.</p>
     *
     * @return Initialization parameter Map.
     */
    public abstract Map<String, String> getInitParam();

    /**
     * <p>Return an immutable <code>Map</code> that maps request parameter
     * names to the first (or only) value (as a String).</p>
     *
     * @return Request parameter Map.
     */
    public abstract Map<String, String> getParam();

    /**
     * <p>Return an immutable <code>Map</code> that maps request parameter
     * names to the set of all values (as a String array).</p>
     *
     * @return Request parameter Map.
     */
    public abstract Map<String, String[]> getParamValues();

    /**
     * <p>Return an immutable <code>Map</code> that maps cookie names to
     * the set of cookies specified in the request.
     *
     * @return Map of Cookies.
     * @since Chain 1.1
     */
    public abstract Map<String, Cookie> getCookies();

    /**
     * <p>Return a mutable <code>Map</code> that maps request scope
     * attribute names to their values.</p>
     *
     * @return Request scope Map.
     */
    public abstract Map<String, Object> getRequestScope();

    /**
     * <p>Return a mutable <code>Map</code> that maps session scope
     * attribute names to their values.</p>
     *
     * @return Session scope Map.
     */
    public abstract Map<String, Object> getSessionScope();

}
