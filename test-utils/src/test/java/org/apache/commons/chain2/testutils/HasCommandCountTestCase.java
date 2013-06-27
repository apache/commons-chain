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

import static org.apache.commons.chain2.testutils.HasCommandCount.hasCommandCount;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.apache.commons.chain2.Catalog;
import org.apache.commons.chain2.Context;
import org.hamcrest.StringDescription;
import org.junit.Before;
import org.junit.Test;

/**
 * @version $Id$
 */
public class HasCommandCountTestCase {

    private Catalog<String, Object, Context<String, Object>> catalog;
    private HasCommandCount matcher;

    @Before
    public void setUp() throws Exception {
        catalog = new TestCatalog<String, Object, Context<String, Object>>();
        matcher = new HasCommandCount(2);
    }

    @Test
    public void wrongCountFails() throws Exception {
        assertFalse(matcher.matchesSafely(catalog, new StringDescription()));
    }

    @Test
    public void correctCount() throws Exception {
        catalog.addCommand("addingCMD", new AddingCommand());
        catalog.addCommand("DelegatingCMD", new DelegatingCommand());

        assertTrue(matcher.matchesSafely(catalog, new StringDescription()));
    }

    @Test(expected = IllegalStateException.class)
    public void inconsistentCatalogThrowsException() throws Exception {
        catalog.addCommand("key for null", null);

        matcher.matchesSafely(catalog, new StringDescription());
    }
}
