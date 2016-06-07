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
package org.apache.commons.chain2.web.servlet;

import org.apache.commons.chain2.Catalog;
import org.apache.commons.chain2.Command;
import org.apache.commons.chain2.Context;
import org.apache.commons.chain2.base.LookupCommand;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>{@link Command} that uses the "servlet path" component of the request URI
 * to select a {@link Command} from the appropriate {@link Catalog}, and
 * execute it.  To use this command, you would typically map an instance
 * of {@link ChainProcessor} to an extension pattern like "*.execute" and
 * then arrange that this is the default command to be executed.  In such
 * an environment, a request for a context relative URI of "/foo.execute"
 * would cause the "/foo.execute" command to be loaded and executed.</p>
 *
 * @version $Id$
 */
public class ServletPathMapper extends LookupCommand<String, Object, ServletWebContext<String, Object>> {

    // --------------------------------------------------------- Command Methods

    /**
     * <p>Look up the servlet path information for this request, and use it to
     * select an appropriate {@link Command} to be executed.
     *
     * @param swcontext Context for the current request
     * @return The name of the {@link Command} instance
     *
     * @since Chain 1.2
     */
    @Override
    protected String getCommandName(ServletWebContext<String, Object> swcontext) {
        // Look up the servlet path for this request
        HttpServletRequest request = swcontext.getRequest();
        String servletPath = (String)
            request.getAttribute("javax.servlet.include.servlet_path");
        if (servletPath == null) {
            servletPath = request.getServletPath();
        }

        return servletPath;
    }

    /**
     * <p>Return the {@link Catalog} to look up the {@link Command} in.</p>
     *
     * @param context {@link Context} for this request
     * @return The catalog.
     * @throws IllegalArgumentException if no {@link Catalog}
     *  can be found
     *
     * @since Chain 1.2
     */
    @Override
    protected Catalog<String, Object, ServletWebContext<String, Object>>
            getCatalog(ServletWebContext<String, Object> context) {
        /* If the object returned from the passed context is not a valid catalog
         * then we use the super class's catalog extraction logic to pull it
         * or to error gracefully.
         */
        Object testCatalog = context.get(getCatalogName());

        /* Assume that the underlying implementation is following convention and
         * returning a catalog with the current context.
         */
        @SuppressWarnings("unchecked")
        Catalog<String, Object, ServletWebContext<String, Object>> catalog =
                testCatalog != null && testCatalog instanceof Catalog ?
                    (Catalog<String, Object, ServletWebContext<String, Object>>) testCatalog :
                    super.getCatalog(context);

        return catalog;
    }

}
