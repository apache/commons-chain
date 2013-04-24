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
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.chain2.web.MapEntry;

/**
 * <p>Private implementation of <code>Map</code> for servlet request
 * name-value.</p>
 *
 * @version $Id$
 */
final class ServletHeaderMap implements Map<String, String> {

    public ServletHeaderMap(HttpServletRequest request) {
        this.request = request;
    }

    private final HttpServletRequest request;

    public void clear() {
        throw new UnsupportedOperationException();
    }

    public boolean containsKey(Object key) {
        return (request.getHeader(key(key)) != null);
    }

    public boolean containsValue(Object value) {
        return values().contains(value);
    }

    public Set<Entry<String, String>> entrySet() {
        Set<Entry<String, String>> set = new HashSet<Entry<String, String>>();
        @SuppressWarnings( "unchecked" ) // it is known that header names are String
        Enumeration<String> keys = request.getHeaderNames();
        String key;
        while (keys.hasMoreElements()) {
            key = keys.nextElement();
            set.add(new MapEntry<String, String>(key, request.getHeader(key), false));
        }
        return (set);
    }

    @Override
    public boolean equals(Object o) {
        return (request.equals(o));
    }

    public String get(Object key) {
        return (request.getHeader(key(key)));
    }

    @Override
    public int hashCode() {
        return (request.hashCode());
    }

    public boolean isEmpty() {
        return (size() < 1);
    }

    public Set<String> keySet() {
        Set<String> set = new HashSet<String>();
        @SuppressWarnings( "unchecked" ) // it is known that header names are String
        Enumeration<String> keys = request.getHeaderNames();
        while (keys.hasMoreElements()) {
            set.add(keys.nextElement());
        }
        return (set);
    }

    public String put(String key, String value) {
        throw new UnsupportedOperationException();
    }

    public void putAll(Map<? extends String, ? extends String> map) {
        throw new UnsupportedOperationException();
    }

    public String remove(Object key) {
        throw new UnsupportedOperationException();
    }

    public int size() {
        int n = 0;
        @SuppressWarnings( "unchecked" ) // it is known that header names are String
        Enumeration<String> keys = request.getHeaderNames();
        while (keys.hasMoreElements()) {
            keys.nextElement();
            n++;
        }
        return (n);
    }

    public Collection<String> values() {
        List<String> list = new ArrayList<String>();
        @SuppressWarnings( "unchecked" ) // it is known that header names are String
        Enumeration<String> keys = request.getHeaderNames();
        while (keys.hasMoreElements()) {
            list.add(request.getHeader(keys.nextElement()));
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
