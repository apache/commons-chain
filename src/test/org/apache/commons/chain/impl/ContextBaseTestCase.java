/*
 * $Header: /home/jerenkrantz/tmp/commons/commons-convert/cvs/home/cvs/jakarta-commons//chain/src/test/org/apache/commons/chain/impl/ContextBaseTestCase.java,v 1.2 2003/08/12 20:33:25 husted Exp $
 * $Revision: 1.2 $
 * $Date: 2003/08/12 20:33:25 $
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
import org.apache.commons.chain.Context;

import java.util.Iterator;


/**
 * <p>Test case for the <code>ContextBase</code> class.</p>
 *
 * @author Craig R. McClanahan
 * @version $Revision: 1.2 $ $Date: 2003/08/12 20:33:25 $
 */

public class ContextBaseTestCase extends TestCase {


    // ---------------------------------------------------- Instance Variables


    /**
     * The {@link Context} instance under test.
     */
    protected Context context = null;



    // ---------------------------------------------------------- Constructors

    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public ContextBaseTestCase(String name) {
        super(name);
    }


    // -------------------------------------------------- Overall Test Methods


    /**
     * Set up instance variables required by this test case.
     */
    public void setUp() {
        context = new ContextBase();
    }


    /**
     * Return the tests included in this test suite.
     */
    public static Test suite() {
        return (new TestSuite(ContextBaseTestCase.class));
    }

    /**
     * Tear down instance variables required by this test case.
     */
    public void tearDown() {
        context = null;
    }


    // ------------------------------------------------ Individual Test Methods


    // Test ability to get and set attributes
    public void testAttributes() {

        Object value = null;
        checkAttributeCount(0);

        context.getAttributes().put("foo", "This is foo");
        checkAttributeCount(1);
        value = context.getAttributes().get("foo");
        assertNotNull("Returned foo", value);
        assertTrue("Returned foo type", value instanceof String);
        assertEquals("Returned foo value", "This is foo",
                     (String) value);
        

        context.getAttributes().put("bar", "This is bar");
        checkAttributeCount(2);
        value = context.getAttributes().get("bar");
        assertNotNull("Returned bar", value);
        assertTrue("Returned bar type", value instanceof String);
        assertEquals("Returned bar value", "This is bar",
                     (String) value);
        

        context.getAttributes().put("baz", "This is baz");
        checkAttributeCount(3);
        value = context.getAttributes().get("baz");
        assertNotNull("Returned baz", value);
        assertTrue("Returned baz type", value instanceof String);
        assertEquals("Returned baz value", "This is baz",
                     (String) value);
        

        context.getAttributes().put("baz", "This is new baz");
        checkAttributeCount(3); // Replaced, not added
        value = context.getAttributes().get("baz");
        assertNotNull("Returned baz", value);
        assertTrue("Returned baz type", value instanceof String);
        assertEquals("Returned baz value", "This is new baz",
                     (String) value);
        
        context.getAttributes().remove("bar");
        checkAttributeCount(2);
        assertNull("Did not return bar",
                   context.getAttributes().get("bar"));
        assertNotNull("Still returned foo",
                      context.getAttributes().get("foo"));
        assertNotNull("Still returned baz",
                      context.getAttributes().get("baz"));

        context.getAttributes().clear();
        checkAttributeCount(0);
        assertNull("Did not return foo",
                   context.getAttributes().get("foo"));
        assertNull("Did not return bar",
                   context.getAttributes().get("bar"));
        assertNull("Did not return baz",
                   context.getAttributes().get("baz"));


    }


    // Test state of newly created instance
    public void testPristine() {

        checkAttributeCount(0);
        assertNull("No 'foo' attribute",
                   context.getAttributes().get("foo"));

    }


    // -------------------------------------------------------- Support Methods


    // Verify the number of defined attributes
    protected void checkAttributeCount(int expected) {
        int actual = 0;
        Iterator keys = context.getAttributes().keySet().iterator();
        while (keys.hasNext()) {
            Object key = (Object) keys.next();
            actual++;
        }
        assertEquals("Correct attribute count", expected, actual);
    }


}
