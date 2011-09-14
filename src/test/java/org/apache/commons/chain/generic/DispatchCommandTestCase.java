/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.chain.generic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.chain.Context;
import org.apache.commons.chain.impl.ContextBase;
import org.junit.Test;

/* JUnitTest case for class: org.apache.commons.chain.generic.DispatchCommand */
public class DispatchCommandTestCase {

    @Test
    public void testMethodDispatch() throws Exception {
        TestCommand test = new TestCommand();

        test.setMethod("testMethod");
        Context context = new ContextBase();
        assertNull(context.get("foo"));
        boolean result = test.execute(context);
        assertTrue(result);
        assertNotNull(context.get("foo"));
        assertEquals("foo", context.get("foo"));


    }


    @Test
    public void testMethodKeyDispatch() throws Exception {
        TestCommand test = new TestCommand();

        test.setMethodKey("foo");
        Context context = new ContextBase();
        context.put("foo", "testMethodKey");
        assertNull(context.get("bar"));
        boolean result = test.execute(context);
        assertFalse(result);
        assertNotNull(context.get("bar"));
        assertEquals("bar", context.get("bar"));


    }

    @Test
    public void testAlternateContext() throws Exception {
        TestAlternateContextCommand test = new TestAlternateContextCommand();

        test.setMethod("foo");
        Context context = new ContextBase();
        assertNull(context.get("elephant"));
        boolean result = test.execute(context);
        assertTrue(result);
        assertNotNull(context.get("elephant"));
        assertEquals("elephant", context.get("elephant"));


    }

    
    class TestCommand extends DispatchCommand {
        

        public boolean testMethod(Context context) {
            context.put("foo", "foo");
            return true;
        }

        public boolean testMethodKey(Context context) {
            
            context.put("bar", "bar");
            return false;
        }

    }

    /**
     * Command which uses alternate method signature.
     * @author germuska
     * @version 0.2-dev
     */
    class TestAlternateContextCommand extends DispatchCommand {


        protected Class[] getSignature() {
            return new Class[] { TestAlternateContext.class };
        }

        protected Object[] getArguments(Context context) {
            return new Object[] { new TestAlternateContext(context) };
        }

        public boolean foo(TestAlternateContext context) {
            context.put("elephant", "elephant");
            return true;
        }
        
    }


    class TestAlternateContext extends java.util.HashMap<String, Object> implements Context {
        Context wrappedContext = null;
         TestAlternateContext(Context context) {
            this.wrappedContext = context;
        }

        @Override
        public Object get(Object o) {
            return this.wrappedContext.get(o);
        }

        public <T> T retrieve(String key) {
            return wrappedContext.<T>retrieve(key);
        }

        @Override
        public Object put(String key, Object value) {
            return this.wrappedContext.put(key, value);
        }

    }
}
