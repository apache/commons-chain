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
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.chain2.web.MapEntry;

/**
 * <p>Private implementation of <code>Map</code> for HTTP session
 * attributes.</p>
 *
 */
final class ServletSessionScopeMap implements Map<String, Object> {

    public ServletSessionScopeMap(HttpServletRequest request) {
        this.request = request;
        sessionExists();
    }

    private HttpSession session;

    private HttpServletRequest request;

    public void clear() {
        if (sessionExists()) {
            for (String key : keySet()) {
                session.removeAttribute(key);
            }
        }
    }

    public boolean containsKey(Object key) {
        return sessionExists() && session.getAttribute(key(key)) != null;
    }

    public boolean containsValue(Object value) {
        if (value == null || !sessionExists()) {
            return false;
        }
        @SuppressWarnings( "unchecked" ) // it is known that attribute names are String
        Enumeration<String> keys = session.getAttributeNames();
        while (keys.hasMoreElements()) {
            Object next = session.getAttribute(keys.nextElement());
            if (value.equals(next)) {
                return true;
            }
        }
        return false;
    }

    public Set<Entry<String, Object>> entrySet() {
        Set<Entry<String, Object>> set = new HashSet<Entry<String, Object>>();
        if (sessionExists()) {
            @SuppressWarnings( "unchecked" ) // it is known that attribute names are String
            Enumeration<String> keys = session.getAttributeNames();
            String key;
            while (keys.hasMoreElements()) {
                key = keys.nextElement();
                set.add(new MapEntry<String, Object>(key, session.getAttribute(key), true));
            }
        }
        return set;
    }

    @Override
    public boolean equals(Object o) {
        return sessionExists() && session.equals(o);
    }

    public Object get(Object key) {
        if (sessionExists()) {
            return session.getAttribute(key(key));
        }
        return null;
    }

    @Override
    public int hashCode() {
        if (sessionExists()) {
            return session.hashCode();
        }
        return 0;
    }

    public boolean isEmpty() {
        return !sessionExists() || !session.getAttributeNames().hasMoreElements();
    }

    public Set<String> keySet() {
        Set<String> set = new HashSet<String>();
        if (sessionExists()) {
            @SuppressWarnings( "unchecked" ) // it is known that attribute names are String
            Enumeration<String> keys = session.getAttributeNames();
            while (keys.hasMoreElements()) {
                set.add(keys.nextElement());
            }
        }
        return set;
    }

    public Object put(String key, Object value) {
        if (value == null) {
            return remove(key);
        }

        // Ensure the Session is created, if it
        // doesn't exist
        if (session == null) {
            session = request.getSession();
            request = null;
        }

        Object previous = session.getAttribute(key);
        session.setAttribute(key, value);
        return previous;
    }

    public void putAll(Map<? extends String, ? extends Object> map) {
        for (Entry<? extends String, ? extends Object> entry : map.entrySet()) {
            put(key(entry.getKey()), entry.getValue());
        }
    }

    public Object remove(Object key) {
        if (sessionExists()) {
            String skey = key(key);
            Object previous = session.getAttribute(skey);
            session.removeAttribute(skey);
            return previous;
        }
        return null;
    }

    public int size() {
        int n = 0;
        if (sessionExists()) {
            @SuppressWarnings( "unchecked" ) // it is known that attribute names are String
            Enumeration<String> keys = session.getAttributeNames();
            while (keys.hasMoreElements()) {
                keys.nextElement();
                n++;
            }
        }
        return n;
    }

    public Collection<Object> values() {
        List<Object> list = new ArrayList<Object>();
        if (sessionExists()) {
            @SuppressWarnings( "unchecked" ) // it is known that attribute names are String
            Enumeration<String> keys = session.getAttributeNames();
            while (keys.hasMoreElements()) {
                list.add(session.getAttribute(keys.nextElement()));
            }
        }
        return list;
    }

    private String key(Object key) {
        if (key == null) {
            throw new IllegalArgumentException();
        }
        if (key instanceof String) {
            return (String) key;
        }
        return key.toString();
    }

    private boolean sessionExists() {
        if (session == null) {
            session = request.getSession(false);
            if (session != null) {
                request = null;
            }
        }
        return session != null;
    }

}
