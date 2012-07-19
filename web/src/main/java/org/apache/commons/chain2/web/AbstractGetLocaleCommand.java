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
package org.apache.commons.chain2.web;

import org.apache.commons.chain2.Command;
import org.apache.commons.chain2.Context;

import java.util.Locale;

/**
 * <p>Abstract base {@link Command} implementation for retrieving the
 * requested Locale from our {@link Context}, and storing it under the
 * context attribute key returned by the <code>localeKey</code> property.</p>
 *
 * @param <C> Type of the context associated with this command
 *
 * @version $Id$
 */
public abstract class AbstractGetLocaleCommand<C extends WebContext>
        implements Command<String, Object, C> {

    // -------------------------------------------------------------- Properties

    /**
     * <p>The context attribute key used to store the <code>Locale</code>.</p>
     */
    private String localeKey = "locale";

    /**
     * <p>Return the context attribute key under which we will store
     * the request <code>Locale</code>.</p>
     *
     * @return The context attribute key of the request <code>Locale</code>.
     */
    public String getLocaleKey() {
        return (this.localeKey);
    }

    /**
     * <p>Set the context attribute key under which we will store
     * the request <code>Locale</code>.</p>
     *
     * @param localeKey The new context attribute key
     */
    public void setLocaleKey(String localeKey) {
        this.localeKey = localeKey;
    }

    // --------------------------------------------------------- Command Methods

    /**
     * <p>Retrieve the <code>Locale</code> for this request, and store it
     * under the specified context attribute.</p>
     *
     * @param context The {@link Context} we are operating on
     *
     * @return <code>false</code> so that processng will continue
     * @throws org.apache.commons.chain2.ChainException If an error occurs during execution.
     */
    public boolean execute(C context) {
        context.put(getLocaleKey(), getLocale(context));
        return (false);
    }

    // ------------------------------------------------------- Protected Methods

    /**
     * <p>Retrieve and return the <code>Locale</code> for this request.</p>
     * @param context The {@link Context} we are operating on.
     * @return The Locale for the request.
     */
    protected abstract Locale getLocale(C context);

}
