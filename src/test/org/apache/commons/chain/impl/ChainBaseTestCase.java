/*
 * $Header: /home/jerenkrantz/tmp/commons/commons-convert/cvs/home/cvs/jakarta-commons//chain/src/test/org/apache/commons/chain/impl/ChainBaseTestCase.java,v 1.3 2003/09/29 06:02:14 craigmcc Exp $
 * $Revision: 1.3 $
 * $Date: 2003/09/29 06:02:14 $
 *
 * ====================================================================
 *
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 1999-2002 The Apache Software Foundation.  All rights
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
 *    any, must include the following acknowlegement:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowlegement may appear in the software itself,
 *    if and wherever such third-party acknowlegements normally appear.
 *
 * 4. The names "The Jakarta Project", "Commons", and "Apache Software
 *    Foundation" must not be used to endorse or promote products derived
 *    from this software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache"
 *    nor may "Apache" appear in their names without prior written
 *    permission of the Apache Group.
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

package org.apache.commons.chain.impl;


import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.commons.chain.Chain;
import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;


/**
 * <p>Test case for the <code>ChainBase</code> class.</p>
 *
 * @author Craig R. McClanahan
 * @version $Revision: 1.3 $ $Date: 2003/09/29 06:02:14 $
 */

public class ChainBaseTestCase extends TestCase {


    // ---------------------------------------------------- Instance Variables


    /**
     * The {@link Chain} instance under test.
     */
    protected Chain chain = null;


    /**
     * The {@link Context} instance on which to execute the chain.
     */
    protected Context context = null;


    // ---------------------------------------------------------- Constructors

    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public ChainBaseTestCase(String name) {
        super(name);
    }


    // -------------------------------------------------- Overall Test Methods


    /**
     * Set up instance variables required by this test case.
     */
    public void setUp() {
        chain = new ChainBase();
        context = new ContextBase();
    }


    /**
     * Return the tests included in this test suite.
     */
    public static Test suite() {
        return (new TestSuite(ChainBaseTestCase.class));
    }

    /**
     * Tear down instance variables required by this test case.
     */
    public void tearDown() {
        chain = null;
        context = null;
    }


    // ------------------------------------------------ Individual Test Methods


    // Test the ability to add commands
    public void testCommands() {

        checkCommandCount(0);

        Command command1 = new NonDelegatingCommand("1");
        chain.addCommand(command1);
        checkCommandCount(1);

        Command command2 = new DelegatingCommand("2");
        chain.addCommand(command2);
        checkCommandCount(2);

        Command command3 = new ExceptionCommand("3");
        chain.addCommand(command3);
        checkCommandCount(3);

    }


    // Test execution of a single non-delegating command
    public void testExecute1a() {
        chain.addCommand(new NonDelegatingCommand("1"));
        try {
            assertTrue("Chain returned true",
                       chain.execute(context));
        } catch (Exception e) {
            fail("Threw exception: " + e);
        }
        checkExecuteLog("1");
    }


    // Test execution of a single delegating command
    public void testExecute1b() {
        chain.addCommand(new DelegatingCommand("1"));
        try {
            assertTrue("Chain returned false",
                       !chain.execute(context));
        } catch (Exception e) {
            fail("Threw exception: " + e);
        }
        checkExecuteLog("1");
    }


    // Test execution of a single exception-throwing command
    public void testExecute1c() {
        chain.addCommand(new ExceptionCommand("1"));
        try {
            chain.execute(context);
        } catch (ArithmeticException e) {
            assertEquals("Correct exception id", "1", e.getMessage());
        } catch (Exception e) {
            fail("Threw exception: " + e);
        }
        checkExecuteLog("1");
    }


    // Test execution of an attempt to add a new Command while executing
    public void testExecute1d() {
        chain.addCommand(new AddingCommand("1", chain));
        try {
            chain.execute(context);
        } catch (IllegalStateException e) {
            ; // Expected result
        } catch (Exception e) {
            fail("Threw exception: " + e);
        }
        checkExecuteLog("1");
    }


    // Test execution of a chain that should return true
    public void testExecute2a() {
        chain.addCommand(new DelegatingCommand("1"));
        chain.addCommand(new DelegatingCommand("2"));
        chain.addCommand(new NonDelegatingCommand("3"));
        try {
            assertTrue("Chain returned true",
                       chain.execute(context));
        } catch (Exception e) {
            fail("Threw exception: " + e);
        }
        checkExecuteLog("1/2/3");
    }


    // Test execution of a chain that should return false
    public void testExecute2b() {
        chain.addCommand(new DelegatingCommand("1"));
        chain.addCommand(new DelegatingCommand("2"));
        chain.addCommand(new DelegatingCommand("3"));
        try {
            assertTrue("Chain returned false",
                       !chain.execute(context));
        } catch (Exception e) {
            fail("Threw exception: " + e);
        }
        checkExecuteLog("1/2/3");
    }


    // Test execution of a chain that should throw an exception
    public void testExecute2c() {
        chain.addCommand(new DelegatingCommand("1"));
        chain.addCommand(new DelegatingCommand("2"));
        chain.addCommand(new ExceptionCommand("3"));
        try {
            chain.execute(context);
        } catch (ArithmeticException e) {
            assertEquals("Correct exception id", "3", e.getMessage());
        } catch (Exception e) {
            fail("Threw exception: " + e);
        }
        checkExecuteLog("1/2/3");
    }


    // Test execution of a chain that should throw an exception in the middle
    public void testExecute2d() {
        chain.addCommand(new DelegatingCommand("1"));
        chain.addCommand(new ExceptionCommand("2"));
        chain.addCommand(new NonDelegatingCommand("3"));
        try {
            chain.execute(context);
        } catch (ArithmeticException e) {
            assertEquals("Correct exception id", "2", e.getMessage());
        } catch (Exception e) {
            fail("Threw exception: " + e);
        }
        checkExecuteLog("1/2");
    }


    // Test execution of a single non-delegating filter
    public void testExecute3a() {
        chain.addCommand(new NonDelegatingFilter("1", "a"));
        try {
            assertTrue("Chain returned true",
                       chain.execute(context));
        } catch (Exception e) {
            fail("Threw exception: " + e);
        }
        checkExecuteLog("1/a");
    }


    // Test execution of a single delegating filter
    public void testExecute3b() {
        chain.addCommand(new DelegatingFilter("1", "a"));
        try {
            assertTrue("Chain returned false",
                       !chain.execute(context));
        } catch (Exception e) {
            fail("Threw exception: " + e);
        }
        checkExecuteLog("1/a");
    }


    // Test execution of a single exception-throwing filter
    public void testExecute3c() {
        chain.addCommand(new ExceptionFilter("1", "a"));
        try {
            chain.execute(context);
        } catch (ArithmeticException e) {
            assertEquals("Correct exception id", "1", e.getMessage());
        } catch (Exception e) {
            fail("Threw exception: " + e);
        }
        checkExecuteLog("1/a");
    }


    // Test execution of a chain that should return true
    public void testExecute4a() {
        chain.addCommand(new DelegatingFilter("1", "a"));
        chain.addCommand(new DelegatingCommand("2"));
        chain.addCommand(new NonDelegatingFilter("3", "c"));
        try {
            assertTrue("Chain returned true",
                       chain.execute(context));
        } catch (Exception e) {
            fail("Threw exception: " + e);
        }
        checkExecuteLog("1/2/3/c/a");
    }


    // Test execution of a chain that should return false
    public void testExecute4b() {
        chain.addCommand(new DelegatingCommand("1"));
        chain.addCommand(new DelegatingFilter("2", "b"));
        chain.addCommand(new DelegatingCommand("3"));
        try {
            assertTrue("Chain returned false",
                       !chain.execute(context));
        } catch (Exception e) {
            fail("Threw exception: " + e);
        }
        checkExecuteLog("1/2/3/b");
    }


    // Test execution of a chain that should throw an exception
    public void testExecute4c() {
        chain.addCommand(new DelegatingFilter("1", "a"));
        chain.addCommand(new DelegatingFilter("2", "b"));
        chain.addCommand(new ExceptionFilter("3", "c"));
        try {
            chain.execute(context);
        } catch (ArithmeticException e) {
            assertEquals("Correct exception id", "3", e.getMessage());
        } catch (Exception e) {
            fail("Threw exception: " + e);
        }
        checkExecuteLog("1/2/3/c/b/a");
    }


    // Test execution of a chain that should throw an exception in the middle
    public void testExecute4d() {
        chain.addCommand(new DelegatingFilter("1", "a"));
        chain.addCommand(new ExceptionFilter("2", "b"));
        chain.addCommand(new NonDelegatingFilter("3", "c"));
        try {
            chain.execute(context);
        } catch (ArithmeticException e) {
            assertEquals("Correct exception id", "2", e.getMessage());
        } catch (Exception e) {
            fail("Threw exception: " + e);
        }
        checkExecuteLog("1/2/b/a");
    }


    // Test state of newly created instance
    public void testNewInstance() {
        checkCommandCount(0);
    }


    // -------------------------------------------------------- Support Methods


    // Verify the number of configured commands
    protected void checkCommandCount(int expected) {
        if (chain instanceof ChainBase) {
            Command commands[] = ((ChainBase) chain).getCommands();
            assertNotNull("getCommands() returned a non-null array",
                          commands);
            assertEquals("Correct command count", expected, commands.length);
        }
    }


    // Verify the contents of the execution log
    protected void checkExecuteLog(String expected) {
        StringBuffer log = (StringBuffer) context.get("log");
        assertNotNull("Context returned log");
        assertEquals("Context returned correct log",
                     expected, log.toString());
    }


}
