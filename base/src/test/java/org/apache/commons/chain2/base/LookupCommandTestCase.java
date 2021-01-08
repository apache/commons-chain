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
package org.apache.commons.chain2.base;

import static org.apache.commons.chain2.testutils.HasLog.hasLog;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import org.apache.commons.chain2.Catalog;
import org.apache.commons.chain2.CatalogFactory;
import org.apache.commons.chain2.Chain;
import org.apache.commons.chain2.Context;
import org.apache.commons.chain2.Processing;
import org.apache.commons.chain2.impl.CatalogBase;
import org.apache.commons.chain2.impl.CatalogFactoryBase;
import org.apache.commons.chain2.impl.ChainBase;
import org.apache.commons.chain2.impl.ContextBase;
import org.apache.commons.chain2.testutils.DelegatingCommand;
import org.apache.commons.chain2.testutils.NonDelegatingCommand;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * <p>Test case for the <code>LookupCommand</code> class.</p>
 *
 * @version $Id$
 */

public class LookupCommandTestCase {


    // ---------------------------------------------------- Instance Variables

    /**
     * The instance of {@link Catalog} to use when looking up commands
     */
    protected Catalog<String, Object, Context<String, Object>> catalog;

    /**
     * The {@link LookupCommand} instance under test.
     */
    protected LookupCommand<String, Object, Context<String, Object>> command;

    /**
     * The {@link Context} instance on which to execute the chain.
     */
    protected Context<String, Object> context = null;


    // -------------------------------------------------- Overall Test Methods


    /**
     * Set up instance variables required by this test case.
     */
    @Before
    public void setUp() {
        catalog = new CatalogBase<String, Object, Context<String, Object>>();
        CatalogFactory<String, Object, Context<String, Object>> catalogFactory = CatalogFactoryBase.getInstance();
        catalogFactory.setCatalog(catalog);
        command = new LookupCommand<String, Object, Context<String, Object>>();
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
            assertEquals("Command should return finished",
        	    Processing.FINISHED, command.execute(context));
        } catch (Exception e) {
            fail("Threw exception: " + e);
        }
        assertThat(context, hasLog("1a"));
    }

    // Test ability to lookup and execute a chain
    @Test
    public void testExecuteMethodLookup_1b() {

        ChainBase<String, Object, Context<String, Object>> chain =
            new ChainBase<String, Object, Context<String, Object>>();
        chain.addCommand(new DelegatingCommand("1b1"));
        chain.addCommand(new DelegatingCommand("1b2"));
        chain.addCommand(new NonDelegatingCommand("1b3"));

        // use default catalog
        catalog.addCommand("foo", chain);
        command.setName("foo");

        try {
            assertEquals("Command should return finished",
        	    Processing.FINISHED, command.execute(context));
        } catch (Exception e) {
            fail("Threw exception: " + e);
        }
        assertThat(context, hasLog("1b1/1b2/1b3"));
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
            assertEquals("Command should return finished",
                Processing.FINISHED, command.execute(context));
        } catch (Exception e) {
            fail("Threw exception: " + e);
        }
        assertThat(context, hasLog("2a"));
    }

    // Test ability to lookup and execute a chain using the context
    @Test
    public void testExecuteMethodLookup_2b() {

        Chain<String, Object, Context<String, Object>> chain =
            new ChainBase<String, Object, Context<String, Object>>();
        chain.addCommand(new DelegatingCommand("2b1"));
        chain.addCommand(new DelegatingCommand("2b2"));
        chain.addCommand(new NonDelegatingCommand("2b3"));

        // use default catalog
        catalog.addCommand("foo", chain);
        command.setNameKey("nameKey");
        context.put("nameKey", "foo");

        try {
            assertEquals("Command should return finished",
                Processing.FINISHED, command.execute(context));
        } catch (Exception e) {
            fail("Threw exception: " + e);
        }
        assertThat(context, hasLog("2b1/2b2/2b3"));
    }

    // Test ability to lookup and execute single non-delegating command, ignoring its result
    @Test
    public void testExecuteMethodLookup_3a() {

        // use default catalog
        catalog.addCommand("foo",
                new NonDelegatingCommand("3a"));
        command.setIgnoreExecuteResult(true);
        command.setName("foo");

        try {
            assertEquals("Command should return continue",
                Processing.CONTINUE, command.execute(context));
        } catch (Exception e) {
            fail("Threw exception: " + e);
        }
        assertThat(context, hasLog("3a"));
    }

}
