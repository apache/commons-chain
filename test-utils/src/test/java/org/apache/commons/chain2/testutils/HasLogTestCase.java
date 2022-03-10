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

package org.apache.commons.chain2.testutils;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.apache.commons.chain2.Context;
import org.hamcrest.StringDescription;
import org.junit.Before;
import org.junit.Test;

/**
 */
public class HasLogTestCase {

    private Context<String, Object> context;
    private HasLog matcher;

    @Before
    public void setUp() throws Exception {
        context = new TestContext<String, Object>();
        matcher = new HasLog("content");
    }

    @Test
    public void noLogFails() throws Exception {
        assertFalse(matcher.matchesSafely(context, new StringDescription()));
    }

    @Test
    public void logWithWrongContentFails() throws Exception {
        context.put("log", new StringBuilder("wrong content"));
        assertFalse(matcher.matchesSafely(context, new StringDescription()));
    }

    @Test
    public void correctContent() throws Exception {
        context.put("log", new StringBuilder("content"));
        assertTrue(matcher.matchesSafely(context, new StringDescription()));
    }

}
