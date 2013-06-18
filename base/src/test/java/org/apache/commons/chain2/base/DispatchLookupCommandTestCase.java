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
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.apache.commons.chain2.Catalog;
import org.apache.commons.chain2.CatalogFactory;
import org.apache.commons.chain2.Context;
import org.apache.commons.chain2.impl.CatalogBase;
import org.apache.commons.chain2.impl.CatalogFactoryBase;
import org.apache.commons.chain2.impl.ContextBase;
import org.apache.commons.chain2.testutils.NonDelegatingCommand;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * <p>Test case for the <code>DispatchLookupCommand</code> class.</p>
 *
 * @version $Revision$
 */

public class DispatchLookupCommandTestCase {


    // ---------------------------------------------------- Instance Variables

    /**
     * The instance of {@link Catalog} to use when looking up commands
     */
    protected Catalog<String, Object, Context<String, Object>> catalog;

    /**
     * The {@link DispatchLookupCommand} instance under test.
     */
    protected DispatchLookupCommand<String, Object, Context<String, Object>> command;

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
        command = new DispatchLookupCommand<String, Object, Context<String, Object>>();
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


    // Test ability to lookup and execute a dispatch method on a single
    // non-delegating command
    @Test
    public void testExecuteDispatchLookup_1a() {

        // use default catalog
        catalog.addCommand("fooCommand", new TestCommand<Context<String, Object>>("1"));

        // command should lookup the fooCommand and execute the fooMethod
        command.setName("fooCommand");
        command.setMethod("fooMethod");

        try {
            assertTrue("Command should return true",
                       command.execute(context));
        } catch (Exception e) {

            fail("Threw exception: " + e);
        }

        // command should lookup the fooCommand and execute the barMethod
        command.setMethod("barMethod");

        try {
            assertTrue("Command should return true",
                       command.execute(context));
        } catch (Exception e) {
            fail("Threw exception: " + e);
        }

        assertThat(context, hasLog("1/1"));

    }

    // Test IllegalArgumentException when incorrect command name specified
    @Test
    public void testExecuteDispatchLookup_2() {

        // use default catalog
        catalog.addCommand("barCommand", new TestCommand<Context<String, Object>>("2"));

        // command should lookup the fooCommand and execute the fooMethod
        command.setName("fooCommand");
        command.setMethod("fooMethod");

        try {
            command.execute(context);
        } catch (IllegalArgumentException e) {
            // test passed
            return;
        } catch (Exception e) {
            // this is a failure
        }

        fail("Expected IllegalArgumentException");
    }

    // Test ability to lookup and execute a dispatch method on a single
    // non-delegating command (using context to specify method name)
    @Test
    public void testExecuteDispatchLookup_3() {

        // use default catalog
        catalog.addCommand("fooCommand", new TestCommand<Context<String, Object>>("3"));

        // command should lookup the fooCommand and execute the fooMethod
        command.setName("fooCommand");
        command.setMethodKey("methodKey");
        context.put("methodKey", "fooMethod");

        try {
            assertTrue("Command should return true",
                       command.execute(context));
        } catch (Exception e) {
            fail("Threw exception: " + e);
        }

        // command should lookup the fooCommand and execute the barMethod
        command.setMethodKey("methodKey");
        context.put("methodKey", "barMethod");


        try {
            assertTrue("Command should return true",
                       command.execute(context));
        } catch (Exception e) {
            fail("Threw exception: " + e);
        }

        assertThat(context, hasLog("3/3"));

    }


    // ---------------------------------------------------------- Inner Classes


    class TestCommand<C extends Context<String, Object>> extends NonDelegatingCommand {

        public TestCommand(String id)
        {
            super(id);
        }

        public boolean fooMethod(C context) {
            log(context, id);
            return true;
        }

        public boolean barMethod(C context) {
            log(context, id);
            return true;
        }

    }

}
