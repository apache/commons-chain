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

import org.apache.commons.chain2.Context;

import javax.servlet.http.Cookie;
import java.io.Serializable;
import java.util.Map;

/**
 *
 * @version $Id: $
 */
public interface WebContext<K, V> extends Context<K, V>, Serializable {
    /**
     * <p>Return a mutable <code>Map</code> that maps application scope
     * attribute names to their values.</p>
     *
     * @return Application scope Map.
     */
    Map<String, Object> getApplicationScope();

    /**
     * <p>Return an immutable <code>Map</code> that maps header names to
     * the first (or only) header value (as a String).  Header names must
     * be matched in a case-insensitive manner.</p>
     *
     * @return Header values Map.
     */
    Map<String, String> getHeader();

    /**
     * <p>Return an immutable <code>Map</code> that maps header names to
     * the set of all values specified in the request (as a String array).
     * Header names must be matched in a case-insensitive manner.</p>
     *
     * @return Header values Map.
     */
    Map<String, String[]> getHeaderValues();

    /**
     * <p>Return an immutable <code>Map</code> that maps context application
     * initialization parameters to their values.</p>
     *
     * @return Initialization parameter Map.
     */
    Map<String, String> getInitParam();

    /**
     * <p>Return an immutable <code>Map</code> that maps request parameter
     * names to the first (or only) value (as a String).</p>
     *
     * @return Request parameter Map.
     */
    Map<String, String> getParam();

    /**
     * <p>Return an immutable <code>Map</code> that maps request parameter
     * names to the set of all values (as a String array).</p>
     *
     * @return Request parameter Map.
     */
    Map<String, String[]> getParamValues();

    /**
     * <p>Return an immutable <code>Map</code> that maps cookie names to
     * the set of cookies specified in the request.
     *
     * @return Map of Cookies.
     * @since Chain 1.1
     */
    Map<String, Cookie> getCookies();

    /**
     * <p>Return a mutable <code>Map</code> that maps request scope
     * attribute names to their values.</p>
     *
     * @return Request scope Map.
     */
    Map<String, Object> getRequestScope();

    /**
     * <p>Return a mutable <code>Map</code> that maps session scope
     * attribute names to their values.</p>
     *
     * @return Session scope Map.
     */
    Map<String, Object> getSessionScope();
}
