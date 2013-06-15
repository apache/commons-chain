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

package org.apache.commons.chain2.base;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @version $Id$
 */
public class CopyCommandTest {

    private CopyCommand<String, Object, Map<String, Object>> command;
    private Map<String, Object> context;

    @Before
    public void setUp() throws Exception {
        command = new CopyCommand<String, Object, Map<String, Object>>();

        context = new HashMap<String, Object>();
        context.put("one", "one");
        context.put("two", "two");
        context.put("three", "three");
    }

    @After
    public void tearDown() throws Exception {
        command = null;
        context = null;
    }

    @Test
    public void executeWithEmptyContextReturnsFalse() throws Exception {
        context.clear();
        execute();
    }

    @Test
    public void noToKeyAssociatedValueToNull() throws Exception {
        command.setValue("five");
        execute();

        assertEquals("five", context.get(null));
    }

    @Test
    public void noToKeyAssociatesValueFromFromKeyToNull() throws Exception {
        command.setFromKey("one");
        execute();

        String expected = (String) context.get(command.getFromKey());
        assertEquals(expected, context.get(null));
    }

    @Test
    public void noValueAndNoFromKeyRemovesToKey() throws Exception {
        command.setToKey("one");
        execute();

        assertNull(context.get("one"));
    }

    @Test
    public void givenFromAndToKeyValueIsCopied() throws Exception {
        command.setFromKey("one");
        command.setToKey("three");
        execute();

        assertEquals("one", context.get("three"));
    }

    @Test
    public void givenFromAndToKeyAndValueTheValueIsCopied() throws Exception {
        command.setFromKey("one");
        command.setToKey("three");
        command.setValue("five");
        execute();

        assertEquals("five", context.get("three"));
    }

    private void execute() {
        // make sure execute always returns false
        assertFalse(command.execute(context));
    }

}
