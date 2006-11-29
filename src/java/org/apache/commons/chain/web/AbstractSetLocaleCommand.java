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
package org.apache.commons.chain.web;


import java.util.Locale;
import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;


/**
 * <p>Abstract base {@link Command} implementation for setting the
 * response locale for this response to the <code>Locale</code> stored
 * under the context attribute key returned by the <code>localeKey</code>
 * property.</p>
 *
 * @author Craig R. McClanahan
 * @version $Revision$ $Date$
 */

public abstract class AbstractSetLocaleCommand implements Command {


    // -------------------------------------------------------------- Properties


    /**
     * <p>The context attribute key used to retrieve the <code>Locale</code>.</p>
     */
    private String localeKey = "locale";


    /**
     * <p>Return the context attribute key under which we will retrieve
     * the response <code>Locale</code>.</p>
     *
     * @return The context attribute key of the request <code>Locale</code>.
     */
    public String getLocaleKey() {

    return (this.localeKey);

    }


    /**
     * <p>Set the context attribute key under which we will retrieve
     * the response <code>Locale</code>.</p>
     *
     * @param localeKey The new context attribute key
     */
    public void setLocaleKey(String localeKey) {

    this.localeKey = localeKey;

    }


    // --------------------------------------------------------- Command Methods


    /**
     * <p>Retrieve the <code>Locale</code> stored under the specified
     * context attribute key, and establish it on this response.</p>
     *
     * @param context The {@link Context} we are operating on
     *
     * @return <code>false</code> so that processng will continue
     * @throws Exception If an error occurs during execution.
     */
    public boolean execute(Context context) throws Exception {

    setLocale(context,
          (Locale) context.get(getLocaleKey()));
    return (false);

    }


    // ------------------------------------------------------- Protected Methods


    /**
     * <p>Establish the specified <code>Locale</code> for this response.</p>
     *
     * @param context The {@link Context} we are operating on.
     * @param locale The Locale for the request.
     */
    protected abstract void setLocale(Context context, Locale locale);


}
