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

import static org.junit.Assert.assertTrue;
import static org.apache.commons.chain2.Chains.on;

import org.apache.commons.chain2.Context;
import org.junit.Test;

import org.apache.commons.chain2.Chain;

public final class FluentInterfacesTestCase {

    @Test(expected = IllegalArgumentException.class)
    public void doesNotAcceptNullChain() {
        on((Chain<String, Object, Context<String, Object>>) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void doesNotAcceptNullCommand() {
        on(new ChainBase<String, Object, Context<String, Object>>())
        .addCommand(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void doesNotAcceptNullContext() {
        on(new ChainBase<String, Object, Context<String, Object>>())
        .addCommand(new NonDelegatingFilter("3", "c"))
        .execute(null);
    }

    @Test
    public void justMakeSureChainIsExecuted() {
        ContextBase context = new ContextBase();

        on(new ChainBase<String, Object, Context<String, Object>>())
        .addCommand(new DelegatingFilter("1", "a"))
        .addCommand(new ExceptionFilter("2", "b"))
        .addCommand(new NonDelegatingFilter("3", "c"))
        .execute(context);

        assertTrue(context.containsKey("log"));
    }

}
