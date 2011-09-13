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
package org.apache.commons.chain.generic;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

import org.apache.commons.chain.Catalog;
import org.apache.commons.chain.Chain;
import org.apache.commons.chain.Context;
import org.apache.commons.chain.impl.CatalogBase;
import org.apache.commons.chain.impl.CatalogFactoryBase;
import org.apache.commons.chain.impl.ChainBase;
import org.apache.commons.chain.impl.ContextBase;
import org.apache.commons.chain.impl.DelegatingCommand;
import org.apache.commons.chain.impl.NonDelegatingCommand;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * <p>Test case for the <code>LookupCommand</code> class.</p>
 *
 * @author Sean Schofield
 * @version $Revision$
 */

public class LookupCommandTestCase {


    // ---------------------------------------------------- Instance Variables

    /**
     * The instance of {@link Catalog} to use when looking up commands
     */
    protected Catalog catalog;

    /**
     * The {@link LookupCommand} instance under test.
     */
    protected LookupCommand<Context> command;

    /**
     * The {@link Context} instance on which to execute the chain.
     */
    protected Context context = null;


    // -------------------------------------------------- Overall Test Methods


    /**
     * Set up instance variables required by this test case.
     */
    @Before
    public void setUp() {
        catalog = new CatalogBase();
        CatalogFactoryBase.getInstance().setCatalog(catalog);
        command = new LookupCommand<Context>();
        context = new ContextBase();
    }

    /**
     * Tear down instance variables required by this test case.
     */
    @After
    public void tearDown() {
        catalog = null;
        CatalogFactoryBase.clear();
        command = null;
        context = null;
    }


    // ------------------------------------------------ Individual Test Methods


    // Test ability to lookup and execute single non-delegating command
    @Test
    public void testExecuteMethodLookup_1a() {

        // use default catalog
        catalog.addCommand("foo", new NonDelegatingCommand("1a"));
        command.setName("foo");

        try {
            assertTrue("Command should return true",
                       command.execute(context));
        } catch (Exception e) {
            fail("Threw exception: " + e);
        }
        checkExecuteLog("1a");
    }

    // Test ability to lookup and execute a chain
    @Test
    public void testExecuteMethodLookup_1b() {

        ChainBase<Context> chain = new ChainBase<Context>();
        chain.addCommand(new DelegatingCommand("1b1"));
        chain.addCommand(new DelegatingCommand("1b2"));
        chain.addCommand(new NonDelegatingCommand("1b3"));
        
        // use default catalog
        catalog.addCommand("foo", chain);
        command.setName("foo");

        try {
            assertTrue("Command should return true",
                       command.execute(context));
        } catch (Exception e) {
            fail("Threw exception: " + e);
        }
        checkExecuteLog("1b1/1b2/1b3");
    }

    // Test ability to lookup and execute single non-delegating command
    // using the context
    @Test
    public void testExecuteMethodLookup_2a() {

        // use default catalog
        catalog.addCommand("foo", new NonDelegatingCommand("2a"));
        command.setNameKey("nameKey");
        context.put("nameKey", "foo");

        try {
            assertTrue("Command should return true",
                       command.execute(context));
        } catch (Exception e) {
            fail("Threw exception: " + e);
        }
        checkExecuteLog("2a");
    }

    // Test ability to lookup and execute a chain using the context 
    @Test
    public void testExecuteMethodLookup_2b() {

        Chain<Context> chain = new ChainBase<Context>();
        chain.addCommand(new DelegatingCommand("2b1"));
        chain.addCommand(new DelegatingCommand("2b2"));
        chain.addCommand(new NonDelegatingCommand("2b3"));

        // use default catalog
        catalog.addCommand("foo", chain);
        command.setNameKey("nameKey");
        context.put("nameKey", "foo");

        try {
            assertTrue("Command should return true",
                       command.execute(context));
        } catch (Exception e) {
            fail("Threw exception: " + e);
        }
        checkExecuteLog("2b1/2b2/2b3");
    }

    // Test ability to lookup and execute single non-delegating command, ignoring its result
    @Test
    public void testExecuteMethodLookup_3a() {

        // use default catalog
        catalog.addCommand("foo", new NonDelegatingCommand<Context>("3a"));
        command.setIgnoreExecuteResult(true);
        command.setName("foo");

        try {
            assertFalse("Command should return false",
                       command.execute(context));
        } catch (Exception e) {
            fail("Threw exception: " + e);
        }
        checkExecuteLog("3a");
    }


    // -------------------------------------------------------- Support Methods


    // Verify the contents of the execution log
    protected void checkExecuteLog(String expected) {
        StringBuffer log = (StringBuffer) context.get("log");
        assertNotNull("Context failed to return log", log);
        assertEquals("Context returned correct log",
                     expected, log.toString());
    }


}
