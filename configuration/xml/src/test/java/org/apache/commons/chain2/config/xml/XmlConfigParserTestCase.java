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

import static org.apache.commons.chain2.testutils.HasCommandCount.hasCommandCount;
import static org.apache.commons.chain2.testutils.HasLog.hasLog;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.net.URL;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.chain2.Catalog;
import org.apache.commons.chain2.CatalogFactory;
import org.apache.commons.chain2.Context;
import org.apache.commons.chain2.Processing;
import org.apache.commons.chain2.testutils.AddingCommand;
import org.apache.commons.chain2.impl.CatalogBase;
import org.apache.commons.chain2.impl.CatalogFactoryBase;
import org.apache.commons.chain2.impl.ChainBase;
import org.apache.commons.chain2.impl.ContextBase;
import org.apache.commons.chain2.testutils.DelegatingCommand;
import org.apache.commons.chain2.testutils.DelegatingFilter;
import org.apache.commons.chain2.testutils.ExceptionCommand;
import org.apache.commons.chain2.testutils.ExceptionFilter;
import org.apache.commons.chain2.testutils.NonDelegatingCommand;
import org.apache.commons.chain2.testutils.NonDelegatingFilter;
import org.apache.commons.digester3.Digester;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

/**
 * Parameterized test case for {@link XmlConfigParser}, that uses config locations as data points.
 *
 * <p><strong>Note:</strong> This test case assumes, that all config files will be parsed to the same catalog
 * and command instances.</p>
 *
 */
@RunWith(Parameterized.class)
public class XmlConfigParserTestCase {

    private Catalog<String, Object, Context<String, Object>> catalog = null;
    private Context<String, Object> context = null;
    private XmlConfigParser parser = null;
    private final String configLocation;

    @Parameterized.Parameters
    public static List<Object[]> data() {
        return Arrays.asList(new Object[][]
            {
                {"/org/apache/commons/chain2/config/xml/test-config.xml"},
                {"/org/apache/commons/chain2/config/xml/test-config-2.xml"}
            }
        );
    }

    public XmlConfigParserTestCase(String configLocation) {
        this.configLocation = configLocation;
    }

    @Before
    public void setUp() throws Exception {
        init();
        load(configLocation);
    }

    private void init() {
        CatalogFactoryBase.clear();
        catalog = new CatalogBase<String, Object, Context<String, Object>>();
        context = new ContextBase();
        parser = new XmlConfigParser();
    }

    @After
    public void tearDown() {
        parser = null;
        context = null;
        catalog = null;
    }

    // Load the default test-config.xml file and examine the results
    @Test
    public void testDefault() throws Exception {

        // Check overall command count
        assertThat(catalog, hasCommandCount(17));

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

        assertEquals(Processing.FINISHED, catalog.getCommand("Execute2a").execute(context));
        assertThat(context, hasLog("1/2/3"));

    }


    // Test execution of chain "Execute2b"
    @Test
    public void testExecute2b() throws Exception {

        assertEquals(Processing.CONTINUE, catalog.getCommand("Execute2b").execute(context));
        assertThat(context, hasLog("1/2/3"));

    }


    // Test execution of chain "Execute2c"
    @Test
    public void testExecute2c() throws Exception {

        try {
            catalog.getCommand("Execute2c").execute(context);
        } catch (ArithmeticException e) {
            assertEquals("Correct exception id",
                    "3", e.getMessage());
        }
        assertThat(context, hasLog("1/2/3"));

    }


    // Test execution of chain "Execute2d"
    @Test
    public void testExecute2d() throws Exception {

        try {
            catalog.getCommand("Execute2d").execute(context);
        } catch (ArithmeticException e) {
            assertEquals("Correct exception id",
                    "2", e.getMessage());
        }
        assertThat(context, hasLog("1/2"));

    }


    // Test execution of chain "Execute4a"
    @Test
    public void testExecute4a() throws Exception {

        assertEquals(Processing.FINISHED, catalog.getCommand("Execute4a").execute(context));
        assertThat(context, hasLog("1/2/3/c/a"));

    }


    // Test execution of chain "Execute2b"
    @Test
    public void testExecute4b() throws Exception {

        assertEquals(Processing.CONTINUE, catalog.getCommand("Execute4b").execute(context));
        assertThat(context, hasLog("1/2/3/b"));

    }


    // Test execution of chain "Execute4c"
    @Test
    public void testExecute4c() throws Exception {

        try {
            catalog.getCommand("Execute4c").execute(context);
        } catch (ArithmeticException e) {
            assertEquals("Correct exception id",
                    "3", e.getMessage());
        }
        assertThat(context, hasLog("1/2/3/c/b/a"));

    }


    // Test execution of chain "Execute4d"
    @Test
    public void testExecute4d() throws Exception {

        try {
            catalog.getCommand("Execute4d").execute(context);
        } catch (ArithmeticException e) {
            assertEquals("Correct exception id",
                    "2", e.getMessage());
        }
        assertThat(context, hasLog("1/2/b/a"));

    }


    // Test a pristine ConfigParser instance
    @Test
    public void testPristine() throws Exception {

        init();
        // Validate the "digester" property
        Digester digester = parser.getDigester();
        assertNotNull("Returned a Digester instance", digester);
        assertFalse("Default namespaceAware",
                digester.getNamespaceAware());
        assertTrue("Default useContextClassLoader",
                digester.getUseContextClassLoader());
        assertFalse("Default validating",
                digester.getValidating());

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
        assertThat(catalog, hasCommandCount(0));

    }

    // Load the specified catalog from the specified resource path
    private void load(String path) throws Exception {
        URL url = getClass().getResource(path);

        if (url == null) {
            String msg = String.format("Can't find resource for path: %s", path);
            throw new IllegalArgumentException(msg);
        }

        CatalogFactory<String, Object, Context<String, Object>> catalogFactory = parser.parse(url);
        catalog = catalogFactory.getCatalog("foo");
    }

}
