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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Locale;

import static org.junit.Assert.*;


// Test case for org.apache.commons.chain2.web.servlet.ServletSetLocaleCommand

public class ServletSetLocaleCommandTestCase {


    // ----------------------------------------------------- Instance Variables


    protected Locale locale = null;

    // Servlet API Objects
    protected ServletContext scontext = null;
    protected HttpServletRequest request = null;
    protected HttpServletResponse response = null;
    protected HttpSession session = null;

    // Chain API Objects
    protected ServletWebContextBase context = null;
    protected ServletSetLocaleCommand command = null;


    // -------------------------------------------------- Overall Test Methods


    /**
     * Set up instance variables required by this test case.
     */
    @Before
    public void setUp() {

    locale = new Locale("en", "US");

    // Set up Servlet API Objects
        scontext = new MockServletContext();
        session = new MockHttpSession(scontext);
        request = new MockHttpServletRequest("/context", "/servlet",
                                             "/path/info", "a=b&c=d",
                                             session);
        response = new MockHttpServletResponse();

    // Set up Chain API Objects
        context = new ServletWebContextBase(scontext, request, response);
    command = new ServletSetLocaleCommand();

    }


    /**
     * Tear down instance variables required by this test case.
     */
    @After
    public void tearDown() {

        scontext = null;
        session = null;
        request = null;
        response = null;

        context = null;
    command = null;

    }


    // ------------------------------------------------- Individual Test Methods


    // Test configured behavior
    @Test
    public void testConfigured() throws Exception {

    command.setLocaleKey("special");
    assertEquals("special", command.getLocaleKey());
    check(context, command);

    }


    // Test default behavior
    @Test
    public void testDefault() throws Exception {

    assertEquals("locale", command.getLocaleKey());
    check(context, command);

    }


    // --------------------------------------------------------- Support Methods


    protected void check(ServletWebContextBase context, ServletSetLocaleCommand command)
            throws Exception {

        String localeKey = command.getLocaleKey();
        assertNotNull(localeKey);
        Object value = context.get(localeKey);
        assertNull(value);
        context.put(localeKey, locale);
        assertNotNull(context.get(localeKey));
        assertNull(response.getLocale());
        boolean result = command.execute(context);
        assertFalse(result);
        assertNotNull(response.getLocale());
        assertEquals(locale, response.getLocale());
    }

}
