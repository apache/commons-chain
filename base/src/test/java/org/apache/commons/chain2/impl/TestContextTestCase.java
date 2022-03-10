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
package org.apache.commons.chain2.impl;

import static org.hamcrest.CoreMatchers.instanceOf;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

import org.apache.commons.chain2.Context;
import org.junit.Before;
import org.junit.Test;


/**
 * Extension of <code>ContextBaseTestCase</code> to validate property
 * delegation.
 *
 */
public class TestContextTestCase extends ContextBaseTestCase {

    @Override
    @Before
    public void setUp() {
        context = createContext();
    }

    @Override
    @Test
    public void testPristine() {
        super.testPristine();
        assertEquals("readOnly", context.get("readOnly"));
        assertEquals("readWrite", context.get("readWrite"));
        assertEquals("writeOnly", ((TestContext) context).returnWriteOnly());
    }

    @Test
    public void readOnlyIsInstanceOfString() {
        assertThat(context.get("readOnly"), instanceOf(String.class));
    }

    @Test
    public void readOnlyIsProvidedByContext() {
        assertEquals("readOnly", context.get("readOnly"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void putReadOnlyThrowsException() {
        context.put("readOnly", "new readOnly");
    }

    @Test
    public void writeOnlyIsInstanceOfString() {
        assertThat(((TestContext) context).returnWriteOnly(),
                instanceOf(String.class));
    }

    @Test
    public void writeOnlyIsNotProvidedByContext() {
        assertNull(context.get("writeOnly"));
    }

    @Test
    public void putNewWriteOnlyProperty() {
        context.put("writeOnly", "new writeOnly");
        assertEquals("new writeOnly", ((TestContext) context).returnWriteOnly());
    }

    @Test
    public void readWriteIsInstanceOfString() {
        assertThat(context.get("readWrite"), instanceOf(String.class));
    }

    @Test
    public void readWriteIsProvidedByContext() {
        assertEquals("readWrite", context.get("readWrite"));
    }

    @Test
    public void putNewReadWriteProperty() {
        context.put("readWrite", "new readWrite");
        assertEquals("new readWrite", context.get("readWrite"));
    }

    @Override
    protected Context<String, Object> createContext() {
        return (new TestContext());
    }

}
