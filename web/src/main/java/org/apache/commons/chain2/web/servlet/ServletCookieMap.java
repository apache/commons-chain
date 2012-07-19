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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Cookie;

import org.apache.commons.chain2.web.MapEntry;

/**
 * <p>Private implementation of <code>Map</code> for servlet cookies</p>
 *
 * @version $Id$
 * @since Chain 1.1
 */
final class ServletCookieMap implements Map<String, Cookie> {

    public ServletCookieMap(HttpServletRequest request) {
        this.request = request;
    }

    private HttpServletRequest request = null;

    public void clear() {
        throw new UnsupportedOperationException();
    }

    public boolean containsKey(Object key) {
        return (get(key) != null);
    }

    public boolean containsValue(Object value) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
                if (cookies[i].equals(value)) {
                    return true;
                }
            }
        }
        return (false);
    }

    public Set<Entry<String, Cookie>> entrySet() {
        Set<Entry<String, Cookie>> set = new HashSet<Entry<String, Cookie>>();
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
                set.add(new MapEntry<String, Cookie>(cookies[i].getName(), cookies[i], false));
            }
        }
        return (set);
    }

    public boolean equals(Object o) {
        return (request.equals(o));
    }

    public Cookie get(Object key) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
                if (cookies[i].getName().equals(key(key))) {
                    return cookies[i];
                }
            }
        }
        return null;
    }

    public int hashCode() {
        return (request.hashCode());
    }

    public boolean isEmpty() {
        return (size() < 1);
    }

    public Set<String> keySet() {
        Set<String> set = new HashSet<String>();
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
                set.add(cookies[i].getName());
            }
        }
        return (set);
    }

    public Cookie put(String key, Cookie value) {
        throw new UnsupportedOperationException();
    }

    public void putAll(Map<? extends String, ? extends Cookie> map) {
        throw new UnsupportedOperationException();
    }

    public Cookie remove(Object key) {
        throw new UnsupportedOperationException();
    }

    public int size() {
        Cookie[] cookies = request.getCookies();
        return (cookies == null ?  0 : cookies.length);
    }

    public Collection<Cookie> values() {
        List<Cookie> list = new ArrayList<Cookie>(size());
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
                list.add(cookies[i]);
            }
        }
        return (list);
    }

    private String key(Object key) {
        if (key == null) {
            throw new IllegalArgumentException();
        } else if (key instanceof String) {
            return ((String) key);
        } else {
            return (key.toString());
        }
    }

}
