/*
 * $Header: /home/jerenkrantz/tmp/commons/commons-convert/cvs/home/cvs/jakarta-commons//chain/src/test/org/apache/commons/chain/impl/TestContextTestCase.java,v 1.1 2003/08/11 04:44:18 craigmcc Exp $
 * $Revision: 1.1 $
 * $Date: 2003/08/11 04:44:18 $
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


/**
 * Extension of <code>ContextBaseTestCase</code> to validate property
 * delegation.
 */

public class TestContextTestCase extends ContextBaseTestCase {


    // ---------------------------------------------------------- Constructors

    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public TestContextTestCase(String name) {
        super(name);
    }


    // -------------------------------------------------- Overall Test Methods


    /**
     * Set up instance variables required by this test case.
     */
    public void setUp() {
        context = new TestContext();
    }


    /**
     * Return the tests included in this test suite.
     */
    public static Test suite() {
        return (new TestSuite(TestContextTestCase.class));
    }


    // ------------------------------------------------ Individual Test Methods


    // Test a read only property on the Context implementation class
    public void testReadOnly() {

        Object readOnly = context.getAttributes().get("readOnly");
        assertNotNull("readOnly found", readOnly);
        assertTrue("readOnly String",
                   readOnly instanceof String);
        assertEquals("readOnly value", "readOnly", readOnly);

        try {
            context.getAttributes().put("readOnly", "new readOnly");
            fail("Should have thrown UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            ; // Expected result
        }
        assertEquals("readOnly unchanged", "readOnly",
                     (String) context.getAttributes().get("readOnly"));

    }


    // Test a read write property on the Context implementation class
    public void testReadWrite() {

        Object readWrite = context.getAttributes().get("readWrite");
        assertNotNull("readWrite found", readWrite);
        assertTrue("readWrite String",
                   readWrite instanceof String);
        assertEquals("readWrite value", "readWrite", readWrite);

        context.getAttributes().put("readWrite", "new readWrite");
        readWrite = context.getAttributes().get("readWrite");
        assertNotNull("readWrite found", readWrite);
        assertTrue("readWrite String",
                   readWrite instanceof String);
        assertEquals("readWrite value", "new readWrite", readWrite);

    }


    // Test a write only property on the Context implementation class
    public void testWriteOnly() {

        Object writeOnly = ((TestContext) context).returnWriteOnly();
        assertNotNull("writeOnly found", writeOnly);
        assertTrue("writeOnly String",
                   writeOnly instanceof String);
        assertEquals("writeOnly value", "writeOnly", writeOnly);

        context.getAttributes().put("writeOnly", "new writeOnly");
        writeOnly = ((TestContext) context).returnWriteOnly();
        assertNotNull("writeOnly found", writeOnly);
        assertTrue("writeOnly String",
                   writeOnly instanceof String);
        assertEquals("writeOnly value", "new writeOnly", writeOnly);

    }


}
