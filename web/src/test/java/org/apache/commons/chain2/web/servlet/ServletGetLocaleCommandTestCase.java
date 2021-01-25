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

import org.apache.commons.chain2.Processing;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Locale;

import static org.junit.Assert.*;


// Test case for org.apache.commons.chain2.web.servlet.ServletGetLocaleCommand

public class ServletGetLocaleCommandTestCase {


    // ----------------------------------------------------- Instance Variables


    protected Locale locale;

    // Servlet API Objects
    protected ServletContext scontext;
    protected HttpServletRequest request;
    protected HttpServletResponse response;
    protected HttpSession session;

    // Chain API Objects
    protected ServletWebContext<String, Object> context;
    protected ServletGetLocaleCommand command;


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
    ((MockHttpServletRequest) request).setLocale(locale);
        response = new MockHttpServletResponse();

    // Set up Chain API Objects
        context = new ServletWebContextBase(scontext, request, response);
    command = new ServletGetLocaleCommand();

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


    protected void check(ServletWebContext context, ServletGetLocaleCommand command)
    throws Exception {

    String localeKey = command.getLocaleKey();
    assertNotNull(localeKey);
    Object value = context.get(localeKey);
    assertNull(value);
    Processing result = command.execute(context);
    assertEquals(Processing.CONTINUE, result);
    value = context.get(localeKey);
    assertNotNull(value);
    assertTrue(value instanceof Locale);
    assertEquals(locale, (Locale) value);

    }


}
