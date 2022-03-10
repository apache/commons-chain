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

import static org.apache.commons.chain2.testutils.HasLog.hasLog;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsMapContaining.hasKey;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertEquals;

import java.util.UUID;

import org.apache.commons.chain2.Context;
import org.apache.commons.chain2.Processing;
import org.junit.Before;
import org.junit.Test;

/**
 */
public class NonDelegatingCommandTestCase {

    private static final String ID = UUID.randomUUID().toString();

    private NonDelegatingCommand command;
    private Context<String, Object> context;

    @Before
    public void setUp() throws Exception {
        command = new NonDelegatingCommand(ID);
        context = new TestContext<String, Object>();
    }

    @Test(expected = IllegalArgumentException.class)
    public void executeNullThrowsException() throws Exception {
        command.execute(null);
    }

    @Test
    public void createsLogInEmptyContext() throws Exception {
        execute();

        assertThat(context.keySet(), hasSize(1));
        assertThat(context, hasKey("log"));
        assertThat(context, hasLog(ID));
    }

    @Test
    public void existingLogIsReused() throws Exception {
        context.put("log", new StringBuilder("some content"));
        execute();

        assertThat(context, hasLog("some content/" + ID));
    }

    private void execute() {
        assertEquals(Processing.FINISHED, command.execute(context));
    }
}
