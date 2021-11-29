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

import org.apache.commons.chain2.Chain;
import org.apache.commons.chain2.ChainException;
import org.apache.commons.chain2.Context;
import org.apache.commons.chain2.testutils.ExceptionRollbackFilter;
import org.apache.commons.chain2.testutils.NonDelegatingCommand;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.commons.chain2.testutils.HasLog.hasLog;
import static org.junit.Assert.assertThat;

public class ChainBaseRollbackTestCase {

    /**
     * The {@link Chain} instance under test.
     */
    protected Chain<String, Object, Context<String, Object>> chain = null;


    /**
     * The {@link Context} instance on which to execute the chain.
     */
    protected Context<String, Object> context = null;


    // -------------------------------------------------- Overall Test Methods


    /**
     * Set up instance variables required by this test case.
     */
    @Before
    public void setUp() {
        chain = new ChainBase<String, Object, Context<String, Object>>();
        context = new ContextBase();
    }

    /**
     * Tear down instance variables required by this test case.
     */
    @After
    public void tearDown() {
        chain = null;
        context = null;
    }


    @Test(expected = ChainException.class)
    public void nullReturningCommandForcesException() {
        chain.addCommand(new ExceptionRollbackFilter("exceptionRollbackFilter"));
        chain.addCommand(new NonDelegatingCommand("non delegating command"));
        chain.execute(context);

        assertThat(context, hasLog("2"));
    }
}
