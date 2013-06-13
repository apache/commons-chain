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

package org.apache.commons.chain2.config.xml;

import org.apache.commons.chain2.Catalog;
import org.apache.commons.chain2.CatalogFactory;
import org.apache.commons.chain2.Context;
import org.apache.commons.chain2.config.xml.ConfigRuleSet;
import org.apache.commons.chain2.config.xml.XmlConfigParser;
import org.apache.commons.chain2.impl.*;
import org.apache.commons.digester3.Digester;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.net.URL;
import java.util.Iterator;

import static org.junit.Assert.*;


/**
 * <p>Test Case for <code>org.apache.commons.chain2.config.ConfigParser</code>.</p>
 */

public class XmlConfigParserTestCase {


    private static final String DEFAULT_XML =
        "/org/apache/commons/chain2/config/xml/test-config.xml";


    // ------------------------------------------------------ Instance Variables


    /**
     * <p>The <code>Catalog</code> to contain our configured commands.</p>
     */
    protected Catalog<String, Object, Context<String, Object>> catalog = null;


    /**
     * <p>The <code>Context</code> to use for execution tests.</p>
     */
    protected Context<String, Object> context = null;


    /**
     * <p>The <code>ConfigParser</code> instance under test.</p>
     */
    protected XmlConfigParser parser = null;


    // ---------------------------------------------------- Overall Test Methods


    /**
     * Set up instance variables required by this test case.
     */
    @Before
    public void setUp() {
        CatalogFactory.clear();
        catalog = new CatalogBase<String, Object, Context<String, Object>>();
        context = new ContextBase();
        parser = new XmlConfigParser();
    }


    /**
     * Tear down instance variables required by this test case.
     */
    @After
    public void tearDown() {
        parser = null;
        context = null;
        catalog = null;
    }


    // ------------------------------------------------ Individual Test Methods


    // Load the default test-config.xml file and examine the results
    @Test
    public void testDefault() throws Exception {

        // Check overall command count
        load(DEFAULT_XML);
        checkCommandCount(17);

        // Check individual single command instances
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

        ChainBase chain = catalog.getCommand("ChainBase");
        assertNotNull(chain);
        assertTrue(chain instanceof TestChain);

        // Check configurable properties instance
        TestCommand tcommand = catalog.getCommand("Configurable");
        assertNotNull(tcommand);
        assertEquals("Foo Value", tcommand.getFoo());
        assertEquals("Bar Value", tcommand.getBar());

    }


    // Test execution of chain "Execute2a"
    @Test
    public void testExecute2a() throws Exception {

        load(DEFAULT_XML);
        assertTrue("Chain returned true",
                   catalog.getCommand("Execute2a").execute(context));
        checkExecuteLog("1/2/3");

    }


    // Test execution of chain "Execute2b"
    @Test
    public void testExecute2b() throws Exception {

        load(DEFAULT_XML);
        assertTrue("Chain returned false",
                   !catalog.getCommand("Execute2b").execute(context));
        checkExecuteLog("1/2/3");

    }


    // Test execution of chain "Execute2c"
    @Test
    public void testExecute2c() throws Exception {

        load(DEFAULT_XML);
        try {
            catalog.getCommand("Execute2c").execute(context);
        } catch (ArithmeticException e) {
            assertEquals("Correct exception id",
                         "3", e.getMessage());
        }
        checkExecuteLog("1/2/3");

    }


    // Test execution of chain "Execute2d"
    @Test
    public void testExecute2d() throws Exception {

        load(DEFAULT_XML);
        try {
            catalog.getCommand("Execute2d").execute(context);
        } catch (ArithmeticException e) {
            assertEquals("Correct exception id",
                         "2", e.getMessage());
        }
        checkExecuteLog("1/2");

    }


    // Test execution of chain "Execute4a"
    @Test
    public void testExecute4a() throws Exception {

        load(DEFAULT_XML);
        assertTrue("Chain returned true",
                   catalog.getCommand("Execute4a").execute(context));
        checkExecuteLog("1/2/3/c/a");

    }


    // Test execution of chain "Execute2b"
    @Test
    public void testExecute4b() throws Exception {

        load(DEFAULT_XML);
        assertTrue("Chain returned false",
                   !catalog.getCommand("Execute4b").execute(context));
        checkExecuteLog("1/2/3/b");

    }


    // Test execution of chain "Execute4c"
    @Test
    public void testExecute4c() throws Exception {

        load(DEFAULT_XML);
        try {
            catalog.getCommand("Execute4c").execute(context);
        } catch (ArithmeticException e) {
            assertEquals("Correct exception id",
                         "3", e.getMessage());
        }
        checkExecuteLog("1/2/3/c/b/a");

    }


    // Test execution of chain "Execute4d"
    @Test
    public void testExecute4d() throws Exception {

        load(DEFAULT_XML);
        try {
            catalog.getCommand("Execute4d").execute(context);
        } catch (ArithmeticException e) {
            assertEquals("Correct exception id",
                         "2", e.getMessage());
        }
        checkExecuteLog("1/2/b/a");

    }


    // Test a pristine ConfigParser instance
    @Test
    public void testPristine() {

        // Validate the "digester" property
        Digester digester = parser.getDigester();
        assertNotNull("Returned a Digester instance", digester);
        assertTrue("Default namespaceAware",
                   !digester.getNamespaceAware());
        assertTrue("Default useContextClassLoader",
                   digester.getUseContextClassLoader());
        assertTrue("Default validating",
                   !digester.getValidating());

        // Validate the "ruleSet" property
        ConfigRuleSet ruleSet = (ConfigRuleSet) parser.getRuleSet();
        assertNotNull("Returned a RuleSet instance", ruleSet);
        assertEquals("Default chainElement",
                     "chain", ruleSet.getChainElement());
        assertEquals("Default classAttribute",
                     "className", ruleSet.getClassAttribute());
        assertEquals("Default commandElement",
                     "command", ruleSet.getCommandElement());
        assertEquals("Default nameAttribute",
                     "name", ruleSet.getNameAttribute());
        assertNull("Default namespaceURI",
                   ruleSet.getNamespaceURI());

        // Validate the "useContextClassLoader" property
        assertTrue("Defaults to use context class loader",
                   parser.getUseContextClassLoader());

        // Ensure that there are no preconfigured commands in the catalog
        checkCommandCount(0);

    }


    // --------------------------------------------------------- Private Methods


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


    // Verify the contents of the execution log
    protected void checkExecuteLog(String expected) {
        StringBuilder log = (StringBuilder) context.get("log");
        assertNotNull("Context returned log", log);
        assertEquals("Context returned correct log",
                     expected, log.toString());
    }


    // Load the specified catalog from the specified resource path
    protected void load(String path) throws Exception {
        URL url = getClass().getResource(path);

        if (url == null) {
            String msg = String.format("Can't find resource for path: %s", path);
            throw new IllegalArgumentException(msg);
        }

        parser.parse(url);
        CatalogFactory<String, Object, Context<String, Object>> catalogFactory
            = CatalogFactoryBase.getInstance();
        catalog = catalogFactory.getCatalog();
    }


}
