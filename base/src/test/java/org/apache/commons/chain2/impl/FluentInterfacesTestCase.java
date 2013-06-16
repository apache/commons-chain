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

import static org.apache.commons.chain2.Chains.on;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.chain2.Catalog;
import org.apache.commons.chain2.Chain;
import org.apache.commons.chain2.Command;
import org.apache.commons.chain2.Context;
import org.apache.commons.chain2.testutils.AddingCommand;
import org.apache.commons.chain2.testutils.DelegatingCommand;
import org.apache.commons.chain2.testutils.DelegatingFilter;
import org.apache.commons.chain2.testutils.ExceptionCommand;
import org.apache.commons.chain2.testutils.ExceptionFilter;
import org.apache.commons.chain2.testutils.NonDelegatingCommand;
import org.apache.commons.chain2.testutils.NonDelegatingFilter;
import org.junit.Test;

public final class FluentInterfacesTestCase {

    @Test(expected = IllegalArgumentException.class)
    public void doesNotAcceptNullChain() {
        on((Chain<String, Object, Context<String, Object>>) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void doesNotAcceptNullCatalog() {
        on((Catalog<String, Object, Context<String, Object>>) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void doesNotAcceptNullCommand() {
        on(new ChainBase<String, Object, Context<String, Object>>())
        .add(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void doesNotAcceptNullContext() {
        on(new ChainBase<String, Object, Context<String, Object>>())
        .add(new NonDelegatingFilter("3", "c"))
        .execute(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void doesNotAcceptNullCommandInCatalog() {
        on(new CatalogBase<String, Object, Context<String, Object>>())
        .add((Command<String, Object, Context<String, Object>>) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void doesNotAcceptNullName() {
        on(new CatalogBase<String, Object, Context<String, Object>>())
        .add(new DelegatingFilter("1", "a")).identifiedBy(null);
    }

    @Test
    public void justMakeSureChainIsExecuted() {
        ContextBase context = new ContextBase();

        on(new ChainBase<String, Object, Context<String, Object>>())
        .add(new DelegatingFilter("1", "a"))
        .add(new ExceptionFilter("2", "b"))
        .add(new NonDelegatingFilter("3", "c"))
        .execute(context);

        assertTrue(context.containsKey("log"));
    }

    @Test
    public void justMakeSureCatalogIsSetup() {
        CatalogBase<String, Object, Context<String, Object>> catalog =
            new CatalogBase<String, Object, Context<String, Object>>();

        on(catalog)
        .add(new AddingCommand("", null)).identifiedBy("AddingCommand")
        .add(new DelegatingCommand("")).identifiedBy("DelegatingCommand")
        .add(new DelegatingFilter("", "")).identifiedBy("DelegatingFilter")
        .add(new ExceptionCommand("")).identifiedBy("ExceptionCommand")
        .add(new ExceptionFilter("", "")).identifiedBy("ExceptionFilter")
        .add(new NonDelegatingCommand("")).identifiedBy("NonDelegatingCommand")
        .add(new NonDelegatingFilter("", "")).identifiedBy("NonDelegatingFilter")
        .add(new ChainBase<String, Object, Context<String, Object>>()).identifiedBy("ChainBase");

        assertNotNull(catalog.getCommand("AddingCommand"));
        assertNotNull(catalog.getCommand("DelegatingCommand"));
        assertNotNull(catalog.getCommand("DelegatingFilter"));
        assertNotNull(catalog.getCommand("ExceptionCommand"));
        assertNotNull(catalog.getCommand("ExceptionFilter"));
        assertNotNull(catalog.getCommand("NonDelegatingCommand"));
        assertNotNull(catalog.getCommand("NonDelegatingFilter"));
        assertNotNull(catalog.getCommand("ChainBase"));
    }

}
