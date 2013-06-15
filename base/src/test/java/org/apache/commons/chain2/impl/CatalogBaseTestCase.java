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
package org.apache.commons.chain2.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.chain2.Catalog;
import org.apache.commons.chain2.Command;
import org.apache.commons.chain2.Context;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


/**
 * <p>Test case for the <code>CatalogBase</code> class.</p>
 *
 * @version $Id$
 */

public class CatalogBaseTestCase {


    // ---------------------------------------------------- Instance Variables


    /**
     * The {@link Catalog} instance under test.
     */
    protected CatalogBase<String, Object, Context<String, Object>> catalog = null;


    // -------------------------------------------------- Overall Test Methods


    /**
     * Set up instance variables required by this test case.
     */
    @Before
    public void setUp() {
        catalog = new CatalogBase<String, Object, Context<String, Object>>();
    }

    /**
     * Tear down instance variables required by this test case.
     */
    @After
    public void tearDown() {
        catalog = null;
    }


    // ------------------------------------------------ Individual Test Methods


    // Test adding commands
    @Test
    public void testAddCommand() {
        addCommands();
        checkCommandCount(8);
    }


    // Test getting commands
    @Test
    public void testGetCommand() {

        addCommands();

        {
            AddingCommand command = catalog.getCommand("AddingCommand");
            assertNotNull(command);
        }

        {
            DelegatingCommand command = catalog.getCommand("DelegatingCommand");
            assertNotNull(command);
        }

        {
            DelegatingFilter command = catalog.getCommand("DelegatingFilter");
            assertNotNull(command);
        }

        {
            ExceptionCommand command = catalog.getCommand("ExceptionCommand");
            assertNotNull(command);
        }

        {
            ExceptionFilter command = catalog.getCommand("ExceptionFilter");
            assertNotNull(command);
        }

        {
            NonDelegatingCommand command = catalog.getCommand("NonDelegatingCommand");
            assertNotNull(command);
        }

        {
            NonDelegatingFilter command = catalog.getCommand("NonDelegatingFilter");
            assertNotNull(command);
        }

        {
            ChainBase<String, Object, Context<String, Object>> command = catalog.getCommand("ChainBase");
            assertNotNull(command);
        }
    }


    // The getNames() method is implicitly tested by checkCommandCount()


    // Test pristine instance
    @Test
    public void testPristine() {
        checkCommandCount(0);
        assertNull(catalog.getCommand("AddingCommand"));
        assertNull(catalog.getCommand("DelegatingCommand"));
        assertNull(catalog.getCommand("DelegatingFilter"));
        assertNull(catalog.getCommand("ExceptionCommand"));
        assertNull(catalog.getCommand("ExceptionFilter"));
        assertNull(catalog.getCommand("NonDelegatingCommand"));
        assertNull(catalog.getCommand("NonDelegatingFilter"));
        assertNull(catalog.getCommand("ChainBase"));
    }

    // Test construction with commands collection
    @Test
    public void testInstantiationWithMapOfCommands() {
        @SuppressWarnings("serial")
        Map<String, Command<String, Object, Context<String, Object>>>
            commands = new ConcurrentHashMap<String, Command<String, Object, Context<String, Object>>>() {
            {
                put("AddingCommand", new AddingCommand("", null));
            }
        };

        CatalogBase<String, Object, Context<String, Object>>
            catalog = new CatalogBase<String, Object, Context<String, Object>>(commands);

        assertEquals("Correct command count", 1, catalog.getCommands().size());
    }

    // Examine construction with null commands collection
    @Test(expected = IllegalArgumentException.class)
    public void testInstantiationWithNullMapOfCommands() {
        Map<String, Command<String, Object, Context<String, Object>>> commands = null;
        @SuppressWarnings("unused")
        CatalogBase<String, Object, Context<String, Object>>
            catalog = new CatalogBase<String, Object, Context<String, Object>>(commands);
    }


    // -------------------------------------------------------- Support Methods


    // Add an interesting set of commands to the catalog
    protected void addCommands() {
        catalog.addCommand("AddingCommand", new AddingCommand("", null));
        catalog.addCommand("DelegatingCommand", new DelegatingCommand(""));
        catalog.addCommand("DelegatingFilter", new DelegatingFilter("", ""));
        catalog.addCommand("ExceptionCommand", new ExceptionCommand(""));
        catalog.addCommand("ExceptionFilter", new ExceptionFilter("", ""));
        catalog.addCommand("NonDelegatingCommand", new NonDelegatingCommand(""));
        catalog.addCommand("NonDelegatingFilter", new NonDelegatingFilter("", ""));
        catalog.addCommand("ChainBase", new ChainBase<String, Object, Context<String, Object>>());
    }


    // Verify the number of configured commands
    protected void checkCommandCount(int expected) {
        int n = 0;
        Iterator<String> names = catalog.getNames();
        while (names.hasNext()) {
            String name = names.next();
            n++;
            assertNotNull(name + " exists", catalog.getCommand(name));
        }
        assertEquals("Correct command count", expected, n);
    }


}
