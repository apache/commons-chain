/*
 * $Header: /home/jerenkrantz/tmp/commons/commons-convert/cvs/home/cvs/jakarta-commons//chain/src/test/org/apache/commons/chain/config/ConfigParserTestCase.java,v 1.4 2003/10/12 09:11:52 rdonkin Exp $
 * $Revision: 1.4 $
 * $Date: 2003/10/12 09:11:52 $
 *
 * ====================================================================
 *
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 1999-2003 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution, if
 *    any, must include the following acknowledgement:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowledgement may appear in the software itself,
 *    if and wherever such third-party acknowledgements normally appear.
 *
 * 4. The names "The Jakarta Project", "Commons", and "Apache Software
 *    Foundation" must not be used to endorse or promote products derived
 *    from this software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache"
 *    nor may "Apache" appear in their names without prior written
 *    permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

package org.apache.commons.chain.config;


import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.commons.chain.Catalog;
import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;
import org.apache.commons.chain.impl.*;
import org.apache.commons.digester.Digester;

import java.util.Iterator;


/**
 * <p>Test Case for <code>org.apache.commons.chain.config.ConfigParser</code>.</p>
 */

public class ConfigParserTestCase extends TestCase {


    private static final String DEFAULT_XML =
	"/org/apache/commons/chain/config/test-config.xml";


    // ------------------------------------------------------------ Constructors


    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public ConfigParserTestCase(String name) {
        super(name);
    }


    // ------------------------------------------------------ Instance Variables


    /**
     * <p>The <code>Catalog</code> to contain our configured commands.</p>
     */
    protected Catalog catalog = null;


    /**
     * <p>The <code>Context</code> to use for execution tests.</p>
     */
    protected Context context = null;


    /**
     * <p>The <code>ConfigParser</code> instance under test.</p>
     */
    protected ConfigParser parser = null;


    // ---------------------------------------------------- Overall Test Methods


    /**
     * Set up instance variables required by this test case.
     */
    public void setUp() {
	catalog = new CatalogBase();
	context = new ContextBase();
	parser = new ConfigParser();
    }


    /**
     * Return the tests included in this test suite.
     */
    public static Test suite() {
        return (new TestSuite(ConfigParserTestCase.class));
    }


    /**
     * Tear down instance variables required by this test case.
     */
    public void tearDown() {
	parser = null;
	context = null;
	catalog = null;
    }


    // ------------------------------------------------ Individual Test Methods


    // Load the default test-config.xml file and examine the results
    public void testDefaut() throws Exception {

	// Check overall command count
	load(DEFAULT_XML);
	checkCommandCount(17);

	// Check individual single command instances
        Command command = null;

        command = catalog.getCommand("AddingCommand");
        assertNotNull(command);
        assertTrue(command instanceof AddingCommand);

        command = catalog.getCommand("DelegatingCommand");
        assertNotNull(command);
        assertTrue(command instanceof DelegatingCommand);

        command = catalog.getCommand("DelegatingFilter");
        assertNotNull(command);
        assertTrue(command instanceof DelegatingFilter);

        command = catalog.getCommand("ExceptionCommand");
        assertNotNull(command);
        assertTrue(command instanceof ExceptionCommand);

        command = catalog.getCommand("ExceptionFilter");
        assertNotNull(command);
        assertTrue(command instanceof ExceptionFilter);

        command = catalog.getCommand("NonDelegatingCommand");
        assertNotNull(command);
        assertTrue(command instanceof NonDelegatingCommand);

        command = catalog.getCommand("NonDelegatingFilter");
        assertNotNull(command);
        assertTrue(command instanceof NonDelegatingFilter);

        command = catalog.getCommand("ChainBase");
        assertNotNull(command);
        assertTrue(command instanceof ChainBase);
	assertTrue(command instanceof TestChain);

	// Check configurable properties instance
	TestCommand tcommand = (TestCommand) catalog.getCommand("Configurable");
	assertNotNull(tcommand);
	assertEquals("Foo Value", tcommand.getFoo());
	assertEquals("Bar Value", tcommand.getBar());

    }


    // Test execution of chain "Execute2a"
    public void testExecute2a() throws Exception {

	load(DEFAULT_XML);
	assertTrue("Chain returned true",
		   catalog.getCommand("Execute2a").execute(context));
	checkExecuteLog("1/2/3");

    }


    // Test execution of chain "Execute2b"
    public void testExecute2b() throws Exception {

	load(DEFAULT_XML);
	assertTrue("Chain returned false",
		   !catalog.getCommand("Execute2b").execute(context));
	checkExecuteLog("1/2/3");

    }


    // Test execution of chain "Execute2c"
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
    public void testExecute4a() throws Exception {

	load(DEFAULT_XML);
	assertTrue("Chain returned true",
		   catalog.getCommand("Execute4a").execute(context));
	checkExecuteLog("1/2/3/c/a");

    }


    // Test execution of chain "Execute2b"
    public void testExecute4b() throws Exception {

	load(DEFAULT_XML);
	assertTrue("Chain returned false",
		   !catalog.getCommand("Execute4b").execute(context));
	checkExecuteLog("1/2/3/b");

    }


    // Test execution of chain "Execute4c"
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
        Iterator names = catalog.getNames();
        while (names.hasNext()) {
            String name = (String) names.next();
            n++;
            assertNotNull(name + " exists", catalog.getCommand(name));
        }
        assertEquals("Correct command count", expected, n);
    }


    // Verify the contents of the execution log
    protected void checkExecuteLog(String expected) {
        StringBuffer log = (StringBuffer) context.get("log");
        assertNotNull("Context returned log");
        assertEquals("Context returned correct log",
                     expected, log.toString());
    }


    // Load the specified catalog from the specified resource path
    protected void load(String path) throws Exception {
	parser.parse(catalog, this.getClass().getResource(path));
    }


}
