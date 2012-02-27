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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Locale;

import javax.portlet.PortletContext;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.PortletSession;

import org.apache.commons.chain2.web.portlet.PortletGetLocaleCommand;
import org.apache.commons.chain2.web.portlet.PortletWebContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


// Test case for org.apache.commons.chain2.web.portlet.PortletGetLocaleCommand

public class PortletGetLocaleCommandTestCase {


    // ----------------------------------------------------- Instance Variables


    protected Locale locale = null;

    // Portlet API Objects
    protected PortletContext pcontext = null;
    protected PortletRequest request = null;
    protected PortletResponse response = null;
    protected PortletSession session = null;

    // Chain API Objects
    protected PortletWebContext context = null;
    protected PortletGetLocaleCommand command = null;


    // -------------------------------------------------- Overall Test Methods


    /**
     * Set up instance variables required by this test case.
     */
    @Before
    public void setUp() {

        locale = new Locale("en", "US");

        // Set up Portlet API Objects
        pcontext = new MockPortletContext();
        session = new MockPortletSession(pcontext);
        request = new MockPortletRequest(null, pcontext, session);
        ((MockPortletRequest) request).setLocale(locale);

        // Set up Chain API Objects
        context = new PortletWebContext(pcontext, request, response);
        command = new PortletGetLocaleCommand();

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


    protected void check(PortletWebContext context, PortletGetLocaleCommand command)
        throws Exception {

        String localeKey = command.getLocaleKey();
        assertNotNull(localeKey);
        Object value = context.get(localeKey);
        assertNull(value);
        boolean result = command.execute(context);
        assertFalse(result);
        value = context.get(localeKey);
        assertNotNull(value);
        assertTrue(value instanceof Locale);
        assertEquals(locale, (Locale) value);

    }


}
