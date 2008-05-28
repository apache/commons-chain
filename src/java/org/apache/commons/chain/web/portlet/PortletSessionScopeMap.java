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
package org.apache.commons.chain.web.portlet;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.portlet.PortletSession;
import javax.portlet.PortletRequest;
import org.apache.commons.chain.web.MapEntry;


/**
 * <p>Private implementation of <code>Map</code> for portlet session
 * attributes.</p>
 *
 * @author Craig R. McClanahan
 * @version $Revision$ $Date$
 */

final class PortletSessionScopeMap implements Map {


    public PortletSessionScopeMap(PortletRequest request) {
        this.request = request;
        sessionExists();
    }


    private PortletSession session = null;
    private PortletRequest request = null;


    public void clear() {
        if (sessionExists()) {
            Iterator keys = keySet().iterator();
            while (keys.hasNext()) {
                session.removeAttribute((String) keys.next());
            }
        }
    }


    public boolean containsKey(Object key) {
        if (sessionExists()) {
            return (session.getAttribute(key(key)) != null);
        } else {
            return false;
        }
    }


    public boolean containsValue(Object value) {
        if (value == null || !sessionExists()) {
            return (false);
        }
        Enumeration keys =
        session.getAttributeNames(PortletSession.PORTLET_SCOPE);
        while (keys.hasMoreElements()) {
            Object next = session.getAttribute((String) keys.nextElement());
            if (value.equals(next)) {
                return (true);
            }
        }
        return (false);
    }


    public Set entrySet() {
        Set set = new HashSet();
        if (sessionExists()) {
            Enumeration keys =
            session.getAttributeNames(PortletSession.PORTLET_SCOPE);
            String key;
            while (keys.hasMoreElements()) {
                key = (String) keys.nextElement();
                set.add(new MapEntry(key, session.getAttribute(key), true));
            }
        }
        return (set);
    }


    public boolean equals(Object o) {
        if (sessionExists()) {
            return (session.equals(o));
        } else {
            return false;
        }
    }


    public Object get(Object key) {
        if (sessionExists()) {
            return (session.getAttribute(key(key)));
        } else {
            return null;
        }
    }


    public int hashCode() {
        if (sessionExists()) {
            return (session.hashCode());
        } else {
            return 0;
        }
    }


    public boolean isEmpty() {
        if (sessionExists() &&
            session.getAttributeNames().hasMoreElements()) {
            return false;
        } else {
            return true;
        }
    }


    public Set keySet() {
        Set set = new HashSet();
        if (sessionExists()) {
            Enumeration keys =
            session.getAttributeNames(PortletSession.PORTLET_SCOPE);
            while (keys.hasMoreElements()) {
                set.add(keys.nextElement());
            }
        }
        return (set);
    }


    public Object put(Object key, Object value) {
        if (value == null) {
            return (remove(key));
        }

        // Ensure the Session is created, if it
        // doesn't exist
        if (session == null) {
            session = request.getPortletSession();
            request = null;
        }

        String skey = key(key);
        Object previous = session.getAttribute(skey);
        session.setAttribute(skey, value);
        return (previous);
    }


    public void putAll(Map map) {
        Iterator entries = map.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry entry = (Map.Entry)entries.next();
            put(entry.getKey(), entry.getValue());
        }
    }


    public Object remove(Object key) {
        if (sessionExists()) {
            String skey = key(key);
            Object previous = session.getAttribute(skey);
            session.removeAttribute(skey);
            return (previous);
        } else {
            return (null);
        }
    }


    public int size() {
        int n = 0;
        if (sessionExists()) {
            Enumeration keys =
            session.getAttributeNames(PortletSession.PORTLET_SCOPE);
            while (keys.hasMoreElements()) {
                keys.nextElement();
                n++;
            }
        }
        return (n);
    }


    public Collection values() {
        List list = new ArrayList();
        if (sessionExists()) {
            Enumeration keys =
            session.getAttributeNames(PortletSession.PORTLET_SCOPE);
            while (keys.hasMoreElements()) {
                list.add(session.getAttribute((String) keys.nextElement()));
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

    private boolean sessionExists() {
        if (session == null) {
            session = request.getPortletSession(false);
            if (session != null) {
                request = null;
            }
        }
        if (session != null) {
            return true;
        } else {
            return false;
        }
    }

}
