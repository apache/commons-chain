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
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsMapContaining.hasEntry;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @version $Id$
 */
public class CopyCommandTestCase {

    private CopyCommand<String, Object, Map<String, Object>> command;
    private Map<String, Object> context;
    private Map<String, Object> originalContext;

    @Before
    public void setUp() throws Exception {
        command = new CopyCommand<String, Object, Map<String, Object>>();

        context = new HashMap<String, Object>();
        context.put("one", "one");
        context.put("two", "two");
        context.put("three", "three");

        originalContext = Collections.unmodifiableMap(new HashMap<String, Object>(context));
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
    public void noToKeyDoesNotAlterContext() throws Exception {
        command.setFromKey("one");
        execute();

        assertEquals(originalContext, context);
    }

    @Test
    public void noToFromKeyDoesNotAlterContext() throws Exception {
        command.setToKey("one");
        execute();

        assertEquals(originalContext, context);
    }

    @Test
    public void bothKeysExistsValueIsCopied() throws Exception {
        command.setFromKey("one");
        command.setToKey("two");

        execute();

        assertThat(context, hasEntry("one", (Object) "one"));
        assertThat(context, hasEntry("two", (Object) "one"));
        assertThat(context, hasEntry("three", (Object) "three"));
        assertThat(context.keySet(), hasSize(3));
    }

    private void execute() {
        // make sure execute always returns false
        assertFalse(command.execute(context));
    }

}
