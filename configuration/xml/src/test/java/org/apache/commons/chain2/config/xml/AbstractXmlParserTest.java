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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.net.URL;
import java.util.Iterator;

import org.apache.commons.chain2.Catalog;
import org.apache.commons.chain2.CatalogFactory;
import org.apache.commons.chain2.Context;
import org.apache.commons.chain2.impl.CatalogBase;
import org.apache.commons.chain2.impl.CatalogFactoryBase;
import org.apache.commons.chain2.impl.ContextBase;
import org.junit.After;
import org.junit.Before;

/**
 * @version $Id$
 */
public abstract class AbstractXmlParserTest {

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


    // --------------------------------------------------------- Private Methods


    // Verify the number of configured commands
    protected void checkCommandCount(int expected) {
        int n = 0;
        Iterator<String> names = catalog.getNames();
        while (names.hasNext()) {
            String name = names.next();
            n++;
            assertNotNull(name + " does not exist", catalog.getCommand(name));
        }
        assertEquals("Command count is not correct", expected, n);
    }

    // Verify the contents of the execution log
    protected void checkExecuteLog(String expected) {
        StringBuilder log = (StringBuilder) context.get("log");
        assertNotNull("Context did not return log", log);
        assertEquals("Context did not return correct log",
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
        catalog = catalogFactory.getCatalog("foo");
    }

}
