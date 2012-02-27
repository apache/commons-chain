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
package org.apache.commons.chain2.web.portlet;

import static org.junit.Assert.*;

import org.apache.commons.chain2.Context;
import org.apache.commons.chain2.impl.ContextBaseTestCase;
import org.apache.commons.chain2.web.WebContext;
import org.apache.commons.chain2.web.portlet.PortletWebContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.portlet.PortletContext;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.PortletSession;
import javax.servlet.http.Cookie;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Collection;


/**
 * Extension of <code>ContextBaseTestCase</code> to validate the
 * extra functionality of this implementation.
 */

public class PortletWebContextTestCase extends ContextBaseTestCase {


    // ----------------------------------------------------- Instance Variables


    // Portlet API Objects
    protected PortletContext pcontext = null;
    protected PortletRequest request = null;
    protected PortletResponse response = null;
    protected PortletSession session = null;


    // -------------------------------------------------- Overall Test Methods


    /**
     * Set up instance variables required by this test case.
     */
    @Before
    public void setUp() {
        pcontext = new MockPortletContext();
        pcontext.setAttribute("akey1", "avalue1");
        pcontext.setAttribute("akey2", "avalue2");
        pcontext.setAttribute("akey3", "avalue3");
        pcontext.setAttribute("akey4", "avalue4");
        ((MockPortletContext) pcontext).addInitParameter("ikey1", "ivalue1");
        ((MockPortletContext) pcontext).addInitParameter("ikey2", "ivalue2");
        ((MockPortletContext) pcontext).addInitParameter("ikey3", "ivalue3");
        session = new MockPortletSession(pcontext);
        session.setAttribute("skey1", "svalue1");
        session.setAttribute("skey2", "svalue2");
        session.setAttribute("skey3", "svalue3");
        request = new MockPortletRequest(null, pcontext, session);
        request.setAttribute("rkey1", "rvalue1");
        request.setAttribute("rkey2", "rvalue2");
        ((MockPortletRequest) request).addParameter("pkey1", "pvalue1");
        ((MockPortletRequest) request).addParameter("pkey2", "pvalue2a");
        ((MockPortletRequest) request).addParameter("pkey2", "pvalue2b");
        context = createContext();
    }


    /**
     * Tear down instance variables required by this test case.
     */
    @After
    public void tearDown() {
        pcontext = null;
        session = null;
        request = null;
        response = null;
        context = null;
    }


    // ------------------------------------------------ Individual Test Methods


    // Test getApplicationScope()
    @Test
    public void testApplicationScope() {

        Map<String, Object> map = ((WebContext) context).getApplicationScope();
        assertNotNull(map);

        // Initial contents
        checkMapSize(map, 4);
        assertEquals("avalue1", map.get("akey1"));
        assertEquals("avalue2", map.get("akey2"));
        assertEquals("avalue3", map.get("akey3"));
        assertEquals("avalue4", map.get("akey4"));

        // Transparency - entrySet()
        checkEntrySet(map, true);

        // Transparency - removal via web object
        pcontext.removeAttribute("akey1");
        checkMapSize(map, 3);
        assertNull(map.get("akey1"));

        // Transparency - removal via map
        map.remove("akey2");
        checkMapSize(map, 2);
        assertNull(pcontext.getAttribute("akey2"));

        // Transparency - addition via web object
        pcontext.setAttribute("akeyA", "avalueA");
        checkMapSize(map, 3);
        assertEquals("avalueA", map.get("akeyA"));

        // Transparency - addition via map
        map.put("akeyB", "avalueB");
        checkMapSize(map, 4);
        assertEquals("avalueB", pcontext.getAttribute("akeyB"));

        // Transparency - replacement via web object
        pcontext.setAttribute("akeyA", "newvalueA");
        checkMapSize(map, 4);
        assertEquals("newvalueA", map.get("akeyA"));

        // Transparency - replacement via map
        map.put("akeyB", "newvalueB");
        assertEquals(4, map.size());
        assertEquals("newvalueB", pcontext.getAttribute("akeyB"));

        // Clearing the map
        map.clear();
        checkMapSize(map, 0);

        // Test putAll()
        Map<String, Object> values = new HashMap<String, Object>();
        values.put("1", "One");
        values.put("2", "Two");
        map.putAll(values);
        assertEquals("putAll(1)", "One", map.get("1"));
        assertEquals("putAll(2)", "Two", map.get("2"));
        checkMapSize(map, 2);

    }


    // Test equals() and hashCode()
    // Copied from ContextBaseTestCase with customized creation of "other"
    @Test
    public void testEquals() {

        // Compare to self
        assertTrue(context.equals(context));
        assertTrue(context.hashCode() == context.hashCode());

        // Compare to equivalent instance
        Context<String, Object> other = new PortletWebContext(pcontext, request, response);
        // assertTrue(context.equals(other));
        assertTrue(context.hashCode() == other.hashCode());

        // Compare to non-equivalent instance - other modified
        other.put("bop", "bop value");
        // assertTrue(!context.equals(other));
        assertTrue(context.hashCode() != other.hashCode());

        // Compare to non-equivalent instance - self modified
        other = new PortletWebContext(pcontext, request, response);
        context.put("bop", "bop value");
        // assertTrue(!context.equals(other));
        assertTrue(context.hashCode() != other.hashCode());

    }


    // Test getHeader()
    @Test
    public void testHeader() {

        Map<String, String> map = ((WebContext) context).getHeader();
        assertNotNull("Header Map Null", map);

        // Initial contents
        checkMapSize(map, 0);

        try {
            map.put("hkey3", "hvalue3");
            fail("Should have thrown UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            ; // expected result
        }

    }


    // Test getHeaderValues()
    @Test
    public void testHeaderValues() {

        Map<String, String[]> map = ((WebContext) context).getHeaderValues();
        assertNotNull("HeaderValues Map Null", map);

        // Initial contents
        checkMapSize(map, 0);

        /*
         FIXME
         This test is inconsistent once introduced generics
        try {
            map.put("hkey3", "ABC");
            fail("Should have thrown UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            ; // expected result
        }
        */
    }


    // Test getInitParam()
    @Test
    public void testInitParam() {

        Map<String, String> map = ((WebContext) context).getInitParam();
        assertNotNull(map);

        // Initial contents
        checkMapSize(map, 3);
        assertEquals("ivalue1", map.get("ikey1"));
        assertEquals("ivalue2", map.get("ikey2"));
        assertEquals("ivalue3", map.get("ikey3"));
        assertTrue(map.containsKey("ikey1"));
        assertTrue(map.containsKey("ikey2"));
        assertTrue(map.containsKey("ikey3"));
        assertTrue(map.containsValue("ivalue1"));
        assertTrue(map.containsValue("ivalue2"));
        assertTrue(map.containsValue("ivalue3"));

        // Transparency - entrySet()
        checkEntrySet(map, false);

        // Unsupported operations on read-only map
        try {
            map.clear();
            fail("Should have thrown UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            ; // expected result
        }
        try {
            map.put("ikey4", "ivalue4");
            fail("Should have thrown UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            ; // expected result
        }
        try {
            map.putAll(new HashMap<String, String>());
            fail("Should have thrown UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            ; // expected result
        }
        try {
            map.remove("ikey1");
            fail("Should have thrown UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            ; // expected result
        }

    }


    // Test getParam()
    @Test
    public void testParam() {

        Map<String, String> map = ((WebContext) context).getParam();
        assertNotNull(map);

        // Initial contents
        checkMapSize(map, 2);
        assertEquals("pvalue1", map.get("pkey1"));
        assertEquals("pvalue2a", map.get("pkey2"));
        assertTrue(map.containsKey("pkey1"));
        assertTrue(map.containsKey("pkey2"));
        assertTrue(map.containsValue("pvalue1"));
        assertTrue(map.containsValue("pvalue2a"));

        checkEntrySet(map, false);

        // Unsupported operations on read-only map
        try {
            map.clear();
            fail("Should have thrown UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            ; // expected result
        }
        try {
            map.put("pkey3", "pvalue3");
            fail("Should have thrown UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            ; // expected result
        }
        try {
            map.putAll(new HashMap<String, String>());
            fail("Should have thrown UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            ; // expected result
        }
        try {
            map.remove("pkey1");
            fail("Should have thrown UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            ; // expected result
        }

    }


    // Test getParamValues()
    @Test
    public void testParamValues() {

        Map<String, String[]> map = ((WebContext) context).getParamValues();
        assertNotNull(map);

        // Initial contents
        checkMapSize(map, 2);
        Object value1 = map.get("pkey1");
        assertNotNull(value1);
        assertTrue(value1 instanceof String[]);
        String values1[] = (String[]) value1;
        assertEquals(1, values1.length);
        assertEquals("pvalue1", values1[0]);
        Object value2 = map.get("pkey2");
        assertNotNull(value2);
        assertTrue(value2 instanceof String[]);
        String values2[] = (String[]) value2;
        assertEquals(2, values2.length);
        assertEquals("pvalue2a", values2[0]);
        assertEquals("pvalue2b", values2[1]);
        assertTrue(map.containsKey("pkey1"));
        assertTrue(map.containsKey("pkey2"));
        assertTrue(map.containsValue(values1));
        assertTrue(map.containsValue(values2));

        // Unsupported operations on read-only map
        try {
            map.clear();
            fail("Should have thrown UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            ; // expected result
        }
        try {
            map.put("pkey3", values2);
            fail("Should have thrown UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            ; // expected result
        }
        try {
            map.putAll(new HashMap<String, String[]>());
            fail("Should have thrown UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            ; // expected result
        }
        try {
            map.remove("pkey1");
            fail("Should have thrown UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            ; // expected result
        }

    }


    // Test getCookies()
    @Test
    public void testCookies() {

        Map<String, Cookie> map = ((WebContext) context).getCookies();
        assertNotNull(map);

        // Initial contents
        checkMapSize(map, 0);

        /*
         FIXME
         This test is inconsistent once introduced generics
        try {
            map.put("ckey3", "XXX");
            fail("map.put() Should have thrown UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            ; // expected result
        }
        */
    }

    // Test state of newly created instance
    @Test
    public void testPristine() {

        super.testPristine();
        PortletWebContext pwcontext = (PortletWebContext) context;

        // Properties should all be non-null
        assertNotNull(pwcontext.getApplicationScope());
        assertNotNull(pwcontext.getHeader());
        assertNotNull(pwcontext.getHeaderValues());
        assertNotNull(pwcontext.getInitParam());
        assertNotNull(pwcontext.getParam());
        assertNotNull(pwcontext.getParamValues());
        assertNotNull(pwcontext.getCookies());
        assertNotNull(pwcontext.getRequestScope());
        assertNotNull(pwcontext.getSessionScope());

        // Attribute-property transparency
        assertTrue(pwcontext.getApplicationScope() ==
                     pwcontext.get("applicationScope"));
        assertTrue(pwcontext.getHeader() ==
                     pwcontext.get("header"));
        assertTrue(pwcontext.getHeaderValues() ==
                     pwcontext.get("headerValues"));
        assertTrue(pwcontext.getInitParam() ==
                     pwcontext.get("initParam"));
        assertTrue(pwcontext.getParam() ==
                     pwcontext.get("param"));
        assertTrue(pwcontext.getParamValues() ==
                     pwcontext.get("paramValues"));
        assertTrue(pwcontext.getCookies() ==
                     pwcontext.get("cookies"));
        assertTrue(pwcontext.getRequestScope() ==
                     pwcontext.get("requestScope"));
        assertTrue(pwcontext.getSessionScope() ==
                     pwcontext.get("sessionScope"));

    }


    // Test release()
    @Test
    public void testRelease() {

        PortletWebContext pwcontext = (PortletWebContext) context;
        pwcontext.release();

        // Properties should all be null
        assertNull("getApplicationScope()", pwcontext.getApplicationScope());
        assertNull("getHeader()", pwcontext.getHeader());
        assertNull("getHeaderValues()", pwcontext.getHeaderValues());
        assertNull("getInitParam()", pwcontext.getInitParam());
        assertNull("getParam()", pwcontext.getParam());
        assertNull("getParamValues()", pwcontext.getParamValues());
        assertNull("getRequestScope()", pwcontext.getRequestScope());
        assertNull("getSessionScope()", pwcontext.getSessionScope());

        // Attributes should all be null
        assertNull("applicationScope", pwcontext.get("applicationScope"));
        assertNull("header", pwcontext.get("header"));
        assertNull("headerValues", pwcontext.get("headerValues"));
        assertNull("initParam", pwcontext.get("initParam"));
        assertNull("param", pwcontext.get("param"));
        assertNull("paramValues", pwcontext.get("paramValues"));
        assertNull("requestScope", pwcontext.get("requestScope"));
        assertNull("sessionScope", pwcontext.get("sessionScope"));

    }


    // Test getRequestScope()
    @Test
    public void testRequestScope() {

        Map<String, Object> map = ((WebContext) context).getRequestScope();
        assertNotNull(map);

        // Initial contents
        checkMapSize(map, 2);
        assertEquals("rvalue1", (String) map.get("rkey1"));
        assertEquals("rvalue2", (String) map.get("rkey2"));

        // Transparency - entrySet()
        checkEntrySet(map, true);

        // Transparency - removal via web object
        request.removeAttribute("rkey1");
        checkMapSize(map, 1);
        assertNull(map.get("rkey1"));

        // Transparency - removal via map
        map.remove("rkey2");
        checkMapSize(map, 0);
        assertNull(request.getAttribute("rkey2"));

        // Transparency - addition via web object
        request.setAttribute("rkeyA", "rvalueA");
        checkMapSize(map, 1);
        assertEquals("rvalueA", (String) map.get("rkeyA"));

        // Transparency - addition via map
        map.put("rkeyB", "rvalueB");
        checkMapSize(map, 2);
        assertEquals("rvalueB", (String) request.getAttribute("rkeyB"));

        // Transparency - replacement via web object
        request.setAttribute("rkeyA", "newvalueA");
        checkMapSize(map, 2);
        assertEquals("newvalueA", (String) map.get("rkeyA"));

        // Transparency - replacement via map
        map.put("rkeyB", "newvalueB");
        checkMapSize(map, 2);
        assertEquals("newvalueB", (String) request.getAttribute("rkeyB"));

        // Clearing the map
        map.clear();
        checkMapSize(map, 0);

        // Test putAll()
        Map<String, Object> values = new HashMap<String, Object>();
        values.put("1", "One");
        values.put("2", "Two");
        map.putAll(values);
        assertEquals("putAll(1)", "One", map.get("1"));
        assertEquals("putAll(2)", "Two", map.get("2"));
        checkMapSize(map, 2);

    }


    // Test getSessionScope()
    @Test
    public void testSessionScope() {

        Map<String, Object> map = ((WebContext) context).getSessionScope();
        assertNotNull(map);

        // Initial contents
        checkMapSize(map, 3);
        assertEquals("svalue1", (String) map.get("skey1"));
        assertEquals("svalue2", (String) map.get("skey2"));
        assertEquals("svalue3", (String) map.get("skey3"));

        // Transparency - entrySet()
        checkEntrySet(map, true);

        // Transparency - removal via web object
        session.removeAttribute("skey1");
        checkMapSize(map, 2);
        assertNull(map.get("skey1"));

        // Transparency - removal via map
        map.remove("skey2");
        checkMapSize(map, 1);
        assertNull(session.getAttribute("skey2"));

        // Transparency - addition via web object
        session.setAttribute("skeyA", "svalueA");
        checkMapSize(map, 2);
        assertEquals("svalueA", (String) map.get("skeyA"));

        // Transparency - addition via map
        map.put("skeyB", "svalueB");
        checkMapSize(map, 3);
        assertEquals("svalueB", (String) session.getAttribute("skeyB"));

        // Transparency - replacement via web object
        session.setAttribute("skeyA", "newvalueA");
        checkMapSize(map, 3);
        assertEquals("newvalueA", (String) map.get("skeyA"));

        // Transparency - replacement via map
        map.put("skeyB", "newvalueB");
        checkMapSize(map, 3);
        assertEquals("newvalueB", (String) session.getAttribute("skeyB"));

        // Clearing the map
        map.clear();
        checkMapSize(map, 0);

        // Test putAll()
        Map<String, Object> values = new HashMap<String, Object>();
        values.put("1", "One");
        values.put("2", "Two");
        map.putAll(values);
        assertEquals("putAll(1)", "One", map.get("1"));
        assertEquals("putAll(2)", "Two", map.get("2"));
        checkMapSize(map, 2);

    }


    // Test getSessionScope() without Session
    @Test
    public void testSessionScopeWithoutSession() {

        // Create a Context without a session
        PortletWebContext ctx = new PortletWebContext(pcontext,
           new MockPortletRequest(), response);
        assertNull("Session(A)", ctx.getRequest().getPortletSession(false));

        // Get the session Map & check session doesn't exist
        Map<String, Object> sessionMap = ctx.getSessionScope();
        assertNull("Session(B)", ctx.getRequest().getPortletSession(false));
        assertNotNull("Session Map(A)", sessionMap);

        // test clear()
        sessionMap.clear();
        assertNull("Session(C)", ctx.getRequest().getPortletSession(false));

        // test containsKey()
        assertFalse("containsKey()", sessionMap.containsKey("ABC"));
        assertNull("Session(D)", ctx.getRequest().getPortletSession(false));

        // test containsValue()
        assertFalse("containsValue()", sessionMap.containsValue("ABC"));
        assertNull("Session(E)", ctx.getRequest().getPortletSession(false));

        // test entrySet()
        Set<Entry<String, Object>> entrySet = sessionMap.entrySet();
        assertNotNull("entrySet", entrySet);
        assertEquals("entrySet Size", 0, entrySet.size());
        assertNull("Session(F)", ctx.getRequest().getPortletSession(false));

        // test equals()
        assertFalse("equals()", sessionMap.equals("ABC"));
        assertNull("Session(G)", ctx.getRequest().getPortletSession(false));

        // test get()
        assertNull("get()", sessionMap.get("ABC"));
        assertNull("Session(H)", ctx.getRequest().getPortletSession(false));

        // test hashCode()
        sessionMap.hashCode();
        assertNull("Session(I)", ctx.getRequest().getPortletSession(false));

        // test isEmpty()
        assertTrue("isEmpty()", sessionMap.isEmpty());
        assertNull("Session(J)", ctx.getRequest().getPortletSession(false));

        // test keySet()
        Set<String> keySet = sessionMap.keySet();
        assertNotNull("keySet", keySet);
        assertEquals("keySet Size", 0, keySet.size());
        assertNull("Session(K)", ctx.getRequest().getPortletSession(false));

        // test putAll() with an empty Map
        sessionMap.putAll(new HashMap<String, Object>());
        assertNull("Session(L)", ctx.getRequest().getPortletSession(false));

        // test remove()
        assertNull("remove()", sessionMap.remove("ABC"));
        assertNull("Session(M)", ctx.getRequest().getPortletSession(false));

        // test size()
        assertEquals("size() Size", 0, sessionMap.size());
        assertNull("Session(N)", ctx.getRequest().getPortletSession(false));

        // test values()
        Collection<Object> values = sessionMap.values();
        assertNotNull("values", values);
        assertEquals("values Size", 0, values.size());
        assertNull("Session(O)", ctx.getRequest().getPortletSession(false));

        // test put()
        try {
            assertNull("put()", sessionMap.put("ABC", "XYZ"));
            assertNotNull("Session(P)", ctx.getRequest().getPortletSession(false));
        } catch(UnsupportedOperationException ex) {
            // expected: currently MockPortletRequest throws this
            //           when trying to create a PortletSession
        }

    }


    // ------------------------------------------------------- Protected Methods


    protected void checkMapSize(Map<?, ?> map, int size) {
        // Check reported size of the map
        assertEquals("checkMapSize(A)", size, map.size());
        // Iterate over key set
        int nk = 0;
        Iterator<?> keys = map.keySet().iterator();
        while (keys.hasNext()) {
            keys.next();
            nk++;
        }
        assertEquals("checkMapSize(B)", size, nk);
        // Iterate over entry set
        int nv = 0;
        Iterator<?> values = map.entrySet().iterator();
        while (values.hasNext()) {
            values.next();
            nv++;
        }
        assertEquals("checkMapSize(C)", size, nv);
        // Count the values
        assertEquals("checkMapSize(D)", size, map.values().size());
    }

    // Test to ensure proper entrySet() and are modifiable optionally
    protected void checkEntrySet(Map map, boolean modifiable) {
        assertTrue("checkEntrySet(A)", map.size() > 1);
        Set entries = map.entrySet();
        assertTrue(map.size() == entries.size());
        Object o = entries.iterator().next();

        assertTrue("checkEntrySet(B)", o instanceof Map.Entry);

        if (!modifiable) {
            try {
                ((Map.Entry)o).setValue(new Object());
                fail("Should have thrown UnsupportedOperationException");
            } catch (UnsupportedOperationException e) {
                ; // expected result
            }
        } else {
            // Should pass and not throw UnsupportedOperationException
            Map.Entry e = (Map.Entry)o;
            e.setValue(e.setValue(new Object()));
        }
    }

    // Create a new instance of the appropriate Context type for this test case
    protected Context<String, Object> createContext() {
        return (new PortletWebContext(pcontext, request, response));
    }


}
