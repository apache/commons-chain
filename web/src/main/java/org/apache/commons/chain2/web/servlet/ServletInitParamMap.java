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
import javax.servlet.ServletContext;

import org.apache.commons.chain2.web.MapEntry;

/**
 * <p>Private implementation of <code>Map</code> for servlet context
 * init parameters.</p>
 *
 * @version $Id$
 */
final class ServletInitParamMap implements Map<String, String> {

    public ServletInitParamMap(ServletContext context) {
        this.context = context;
    }

    private ServletContext context = null;

    public void clear() {
        throw new UnsupportedOperationException();
    }

    public boolean containsKey(Object key) {
        return (context.getInitParameter(key(key)) != null);
    }

    public boolean containsValue(Object value) {
        return values().contains(value);
    }

    public Set<Entry<String, String>> entrySet() {
        Set<Entry<String, String>> set = new HashSet<Entry<String, String>>();
        @SuppressWarnings( "unchecked" ) // it is known that header names are String
        Enumeration<String> keys = context.getInitParameterNames();
        String key;
        while (keys.hasMoreElements()) {
            key = keys.nextElement();
            set.add(new MapEntry<String, String>(key, context.getInitParameter(key), false));
        }
        return (set);
    }

    public boolean equals(Object o) {
        return (context.equals(o));
    }

    public String get(Object key) {
        return (context.getInitParameter(key(key)));
    }

    public int hashCode() {
        return (context.hashCode());
    }

    public boolean isEmpty() {
        return (size() < 1);
    }

    public Set<String> keySet() {
        Set<String> set = new HashSet<String>();
        @SuppressWarnings( "unchecked" ) // it is known that header names are String
        Enumeration<String> keys = context.getInitParameterNames();

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
        Enumeration<String> keys = context.getInitParameterNames();
        while (keys.hasMoreElements()) {
            keys.nextElement();
            n++;
        }
        return (n);
    }

    public Collection<String> values() {
        List<String> list = new ArrayList<String>();
        @SuppressWarnings( "unchecked" ) // it is known that header names are String
        Enumeration<String> keys = context.getInitParameterNames();
        while (keys.hasMoreElements()) {
            list.add(context.getInitParameter(keys.nextElement()));
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
