/*
 * $Header: /home/jerenkrantz/tmp/commons/commons-convert/cvs/home/cvs/jakarta-commons//chain/src/test/org/apache/commons/chain/impl/ContextBaseTestCase.java,v 1.4 2003/10/12 09:11:53 rdonkin Exp $
 * $Revision: 1.4 $
 * $Date: 2003/10/12 09:11:53 $
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

package org.apache.commons.chain.impl;


import java.util.HashMap;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.commons.chain.Context;



/**
 * <p>Test case for the <code>ContextBase</code> class.</p>
 *
 * @author Craig R. McClanahan
 * @version $Revision: 1.4 $ $Date: 2003/10/12 09:11:53 $
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
        context = createContext();
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


    // Test ability to get, put, and remove attributes
    public void testAttributes() {

        Object value = null;
        checkAttributeCount(0);

        context.put("foo", "This is foo");
        checkAttributeCount(1);
        value = context.get("foo");
        assertNotNull("Returned foo", value);
        assertTrue("Returned foo type", value instanceof String);
        assertEquals("Returned foo value", "This is foo",
                     (String) value);
        
        context.put("bar", "This is bar");
        checkAttributeCount(2);
        value = context.get("bar");
        assertNotNull("Returned bar", value);
        assertTrue("Returned bar type", value instanceof String);
        assertEquals("Returned bar value", "This is bar",
                     (String) value);
        
        context.put("baz", "This is baz");
        checkAttributeCount(3);
        value = context.get("baz");
        assertNotNull("Returned baz", value);
        assertTrue("Returned baz type", value instanceof String);
        assertEquals("Returned baz value", "This is baz",
                     (String) value);
        
        context.put("baz", "This is new baz");
        checkAttributeCount(3); // Replaced, not added
        value = context.get("baz");
        assertNotNull("Returned baz", value);
        assertTrue("Returned baz type", value instanceof String);
        assertEquals("Returned baz value", "This is new baz",
                     (String) value);
        
        context.remove("bar");
        checkAttributeCount(2);
        assertNull("Did not return bar",
                   context.get("bar"));
        assertNotNull("Still returned foo",
                      context.get("foo"));
        assertNotNull("Still returned baz",
                      context.get("baz"));

        context.clear();
        checkAttributeCount(0);
        assertNull("Did not return foo",
                   context.get("foo"));
        assertNull("Did not return bar",
                   context.get("bar"));
        assertNull("Did not return baz",
                   context.get("baz"));

    }


    // Test containsKey() and containsValue()
    public void testContains() {

        assertTrue(!context.containsKey("bop"));
        assertTrue(!context.containsValue("bop value"));
        context.put("bop", "bop value");
        assertTrue(context.containsKey("bop"));
        assertTrue(context.containsValue("bop value"));
        context.remove("bop");
        assertTrue(!context.containsKey("bop"));
        assertTrue(!context.containsValue("bop value"));

    }


    // Test equals() and hashCode()
    public void testEquals() {

        // Compare to self
        assertTrue(context.equals(context));
        assertTrue(context.hashCode() == context.hashCode());

        // Compare to equivalent instance
        Context other = createContext();
        assertTrue(context.equals(other));
        assertTrue(context.hashCode() == other.hashCode());

        // Compare to non-equivalent instance - other modified
        other.put("bop", "bop value");
        assertTrue(!context.equals(other));
        assertTrue(context.hashCode() != other.hashCode());

        // Compare to non-equivalent instance - self modified
        other = createContext(); // reset to equivalence
        context.put("bop", "bop value");
        assertTrue(!context.equals(other));
        assertTrue(context.hashCode() != other.hashCode());

    }        


    // Test keySet()
    public void testKeySet() {

        Set keySet = null;
        Collection all = new ArrayList();

        // Unsupported operations
        keySet = context.keySet();
        try {
            keySet.add("bop");
            fail("Should have thrown UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            ; // Expected result
        }
        try {
            Collection adds = new ArrayList();
            adds.add("bop");
            keySet.addAll(adds);
            fail("Should have thrown UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            ; // Expected result
        }

        // Before-modification checks
        keySet = context.keySet();
        assertEquals(createContext().size(), keySet.size());
        assertTrue(!keySet.contains("foo"));
        assertTrue(!keySet.contains("bar"));
        assertTrue(!keySet.contains("baz"));
        assertTrue(!keySet.contains("bop"));

        // Add the new elements
        context.put("foo", "foo value");
        context.put("bar", "bar value");
        context.put("baz", "baz value");
        all.add("foo");
        all.add("bar");
        all.add("baz");

        // After-modification checks
        keySet = context.keySet();
        assertEquals(expectedAttributeCount() + 3, keySet.size());
        assertTrue(keySet.contains("foo"));
        assertTrue(keySet.contains("bar"));
        assertTrue(keySet.contains("baz"));
        assertTrue(!keySet.contains("bop"));
        assertTrue(keySet.containsAll(all));

        // Remove a single element via remove()
        context.remove("bar");
        all.remove("bar");
        keySet = context.keySet();
        assertEquals(expectedAttributeCount() + 2, keySet.size());
        assertTrue(keySet.contains("foo"));
        assertTrue(!keySet.contains("bar"));
        assertTrue(keySet.contains("baz"));
        assertTrue(!keySet.contains("bop"));
        assertTrue(keySet.containsAll(all));

        // Remove a single element via keySet.remove()
        keySet.remove("baz");
        all.remove("baz");
        keySet = context.keySet();
        assertEquals(expectedAttributeCount() + 1, keySet.size());
        assertTrue(keySet.contains("foo"));
        assertTrue(!keySet.contains("bar"));
        assertTrue(!keySet.contains("baz"));
        assertTrue(!keySet.contains("bop"));
        assertTrue(keySet.containsAll(all));

        // Remove all elements via keySet.clear()
        keySet.clear();
        all.clear();
        assertEquals(expectedAttributeCount(), keySet.size());
        assertTrue(!keySet.contains("foo"));
        assertTrue(!keySet.contains("bar"));
        assertTrue(!keySet.contains("baz"));
        assertTrue(!keySet.contains("bop"));
        assertTrue(keySet.containsAll(all));

        // Add the new elements #2
        context.put("foo", "foo value");
        context.put("bar", "bar value");
        context.put("baz", "baz value");
        all.add("foo");
        all.add("bar");
        all.add("baz");

        // After-modification checks #2
        keySet = context.keySet();
        assertEquals(expectedAttributeCount() + 3, keySet.size());
        assertTrue(keySet.contains("foo"));
        assertTrue(keySet.contains("bar"));
        assertTrue(keySet.contains("baz"));
        assertTrue(!keySet.contains("bop"));
        assertTrue(keySet.containsAll(all));

    }


    // Test state of newly created instance
    public void testPristine() {

        checkAttributeCount(0);
        assertNull("No 'foo' attribute",
                   context.get("foo"));

    }


    // Test putAll()
    public void testPutAll() {

        // Check preconditions
        checkAttributeCount(0);
        assertNull(context.get("foo"));
        assertNull(context.get("bar"));
        assertNull(context.get("baz"));
        assertTrue(!context.containsKey("foo"));
        assertTrue(!context.containsKey("bar"));
        assertTrue(!context.containsKey("baz"));
        assertTrue(!context.containsValue("foo value"));
        assertTrue(!context.containsValue("bar value"));
        assertTrue(!context.containsValue("baz value"));

        // Call putAll()
        Map adds = new HashMap();
        adds.put("foo", "foo value");
        adds.put("bar", "bar value");
        adds.put("baz", "baz value");
        context.putAll(adds);

        // Check postconditions
        checkAttributeCount(3);
        assertEquals("foo value", (String) context.get("foo"));
        assertEquals("bar value", (String) context.get("bar"));
        assertEquals("baz value", (String) context.get("baz"));
        assertTrue(context.containsKey("foo"));
        assertTrue(context.containsKey("bar"));
        assertTrue(context.containsKey("baz"));
        assertTrue(context.containsValue("foo value"));
        assertTrue(context.containsValue("bar value"));
        assertTrue(context.containsValue("baz value"));

    }


    // -------------------------------------------------------- Support Methods


    // Verify the number of defined attributes
    protected void checkAttributeCount(int expected) {
        int actual = 0;
        Iterator keys = context.keySet().iterator();
        while (keys.hasNext()) {
            Object key = (Object) keys.next();
            actual++;
        }
        assertEquals("Correct attribute count",
                     expectedAttributeCount() + expected, actual);
        if (expected == 0) {
            assertTrue("Context should be empty", context.isEmpty());
        } else {
            assertTrue("Context should not be empty", !context.isEmpty());
        }
    }


    // Create a new instance of the appropriate Context type for this test case
    protected Context createContext() {
        return (new ContextBase());
    }


    // Return the expected size() for a Context for this test case
    protected int expectedAttributeCount() {
        return (createContext().size());
    }


}
