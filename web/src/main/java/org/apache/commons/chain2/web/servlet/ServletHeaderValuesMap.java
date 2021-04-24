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
 * name-values[].</p>
 *
 * @version $Id$
 */
final class ServletHeaderValuesMap implements Map<String, String[]> {

    public ServletHeaderValuesMap(HttpServletRequest request) {
        this.request = request;
    }

    private final HttpServletRequest request;

    public void clear() {
        throw new UnsupportedOperationException();
    }

    public boolean containsKey(Object key) {
        return (request.getHeader(key(key)) != null);
    }

    /**
     * An empty immutable {@code String} array.
     */
    public static final String[] EMPTY_STRING_ARRAY = new String[0];

    public boolean containsValue(Object value) {
        if (!(value instanceof String[])) {
            return (false);
        }
        String[] test = (String[]) value;

        for (String[] actual : values()) {
            if (test.length == actual.length) {
                boolean matched = true;
                for (int i = 0; i < test.length; i++) {
                    if (!test[i].equals(actual[i])) {
                        matched = false;
                        break;
                    }
                }
                if (matched) {
                    return (true);
                }
            }
        }

        return (false);
    }

    public Set<Entry<String, String[]>> entrySet() {
        Set<Entry<String, String[]>> set = new HashSet<Entry<String, String[]>>();
        @SuppressWarnings( "unchecked" ) // it is known that header names are String
        Enumeration<String> keys = request.getHeaderNames();
        String key;
        while (keys.hasMoreElements()) {
            key = keys.nextElement();
            @SuppressWarnings( "unchecked" ) // it is known that header values are String
            Enumeration<String> values = request.getHeaders(key);
            String[] valuesArray = enumerationToArray(values);
            /* Previously the API was returning an Set<Entry<String, Enumeration<String>>
             * I'm assuming that this was a bug because it violates the contract of how the
             * mapping class behaves. So, I fixed it right here.
             */
            set.add(new MapEntry<String, String[]>(key, valuesArray, false));
        }
        return (set);
    }

    @Override
    public boolean equals(Object o) {
        return (request.equals(o));
    }

    public String[] get(Object key) {
        @SuppressWarnings( "unchecked" ) // it is known that header names are String
        Enumeration<String> values = request.getHeaders(key(key));
        String[] valuesArray = enumerationToArray(values);

        return valuesArray;
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

    public String[] put(String key, String[] value) {
        throw new UnsupportedOperationException();
    }

    public void putAll(Map<? extends String, ? extends String[]> map) {
        throw new UnsupportedOperationException();
    }

    public String[] remove(Object key) {
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

    public Collection<String[]> values() {
        List<String[]> list = new ArrayList<String[]>();
        @SuppressWarnings( "unchecked" ) // it is known that header names are String
        Enumeration<String> keys = request.getHeaderNames();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            @SuppressWarnings( "unchecked" ) // it is known that header values are String
            Enumeration<String> values = request.getHeaders(key);
            String[] valuesArray = enumerationToArray(values);
            list.add(valuesArray);
        }
        return (list);
    }

    /**
     * Simple utility method that converts a string enumeration into a string array.
     * @param values enumeration of strings
     * @return enumeration represented as a string array
     */
    private static String[] enumerationToArray(Enumeration<String> values) {
        ArrayList<String> list = new ArrayList<String>();

        while (values.hasMoreElements()) {
            list.add(values.nextElement());
        }

        return list.toArray(EMPTY_STRING_ARRAY);
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
