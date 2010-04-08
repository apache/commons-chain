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


import org.apache.commons.chain.web.MockEnumeration;

import javax.portlet.PortletContext;
import javax.portlet.PortletSession;
import javax.portlet.PortletContext;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Date;



// Mock Object for PortletSession
public class MockPortletSession implements PortletSession {


    private Date creationTime     = new Date();
    private Date lastAccessedTime = creationTime;

    private PortletContext context = null;
    private int maxInactiveInterval = 100;
    private boolean newSession = true;
    private String id = "mockId" + creationTime.getTime();
    private Map portletScope = new HashMap();
    private Map applicationScope = new HashMap();


    public MockPortletSession() {
        this(null);
    }


    public MockPortletSession(PortletContext context) {
        this.context = (context == null ? new MockPortletContext() : context);
    }


    // --------------------------------------------------------- Public Methods


    public void setPortletContext(PortletContext context) {
        this.context = context;
    }

    public void setNew(boolean newSession) {
        this.newSession = newSession;
    }

    public void setNew(String id) {
        this.id = id;
    }



    // ---------------------------------------------------- PortletSession Methods


    public Object getAttribute(String name) {
        accessed();
        return getAttribute(name, PortletSession.PORTLET_SCOPE);
    }

    public Object getAttribute(String name, int scope) {
        accessed();
        return getScope(scope).get(name);
    }


    public Enumeration getAttributeNames() {
        accessed();
        return getAttributeNames(PortletSession.PORTLET_SCOPE);
    }

    public Enumeration getAttributeNames(int scope) {
        accessed();
        return new MockEnumeration(getScope(scope).keySet().iterator());
    }


    public long getCreationTime() {
        accessed();
        return creationTime.getTime();
    }


    public String getId() {
        accessed();
        return id;
    }


    public long getLastAccessedTime() {
        return lastAccessedTime.getTime();
    }


    public int getMaxInactiveInterval() {
        accessed();
        return maxInactiveInterval;
    }


    public PortletContext getPortletContext() {
        accessed();
        return context;
    }

    public void invalidate() {
        throw new UnsupportedOperationException();
    }

    public boolean isNew() {
        accessed();
        return newSession;
    }

    public void removeAttribute(String name) {
        accessed();
        removeAttribute(name, PortletSession.PORTLET_SCOPE);
    }

    public void removeAttribute(String name, int scope) {
        accessed();
        getScope(scope).remove(name);
    }

    public void setAttribute(String name, Object value) {
        accessed();
        setAttribute(name, value, PortletSession.PORTLET_SCOPE);
    }

    public void setAttribute(String name, Object value, int scope) {
        accessed();
        getScope(scope).put(name, value);
    }

    public void setMaxInactiveInterval(int interval) {
        accessed();
        this.maxInactiveInterval = interval;
    }

    private void accessed() {
        lastAccessedTime = new Date();
    }

    private Map getScope(int scope) {
        if (scope == PortletSession.PORTLET_SCOPE) {
            return portletScope;
        } else if (scope == PortletSession.APPLICATION_SCOPE) {
           return applicationScope;
        } else {
           throw new IllegalArgumentException("Invalid scope: " + scope);
        }
    }

}
