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
import org.apache.commons.chain2.generic.LookupCommand;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>{@link Command} that uses the "path info" component of the request URI
 * to select a {@link Command} from the appropriate {@link Catalog}, and
 * execute it.  To use this command, you would typically map an instance
 * of {@link ChainProcessor} to a wildcard pattern like "/execute/*" and
 * then arrange that this is the default command to be executed.  In such
 * an environment, a request for the context-relative URI "/execute/foo"
 * would cause the "/foo" command to be loaded and executed.</p>
 *
 * @version $Id$
 */
public class PathInfoMapper extends LookupCommand<String, Object, ServletWebContext> {

    // ------------------------------------------------------ Instance Variables

    private String catalogKey = ChainProcessor.CATALOG_DEFAULT;

    // -------------------------------------------------------------- Properties

    /**
     * <p>Return the context key under which our {@link Catalog} has been
     * stored.</p>
     *
     * @return The context key for the Catalog.
     *
     * @deprecated Use catalogName to specify the name of the catalog in the
     *  catalog factory
     */
    @Deprecated
    public String getCatalogKey() {
        return (this.catalogKey);
    }

    /**
     * <p>Set the context key under which our {@link Catalog} has been
     * stored.</p>
     *
     * @param catalogKey The new catalog key
     *
     * @deprecated Use catalogName to specify the name of the catalog in the
     *  catalog factory
     */
    @Deprecated
    public void setCatalogKey(String catalogKey) {
        this.catalogKey = catalogKey;
    }

    // --------------------------------------------------------- Command Methods

    /**
     * <p>Look up the extra path information for this request, and use it to
     * select an appropriate {@link Command} to be executed.
     *
     * @param context Context for the current request
     * @return The name of the {@link Command} instance
     *
     * @since Chain 1.2
     */
    @Override
    protected String getCommandName(ServletWebContext context) {
        // Look up the extra path information for this request
        HttpServletRequest request = context.getRequest();
        String pathInfo = (String)
            request.getAttribute("javax.servlet.include.path_info");
        if (pathInfo == null) {
            pathInfo = request.getPathInfo();
        }

        return pathInfo;
    }

    /**
     * <p>Return the {@link Catalog} to look up the {@link Command} in.</p>
     *
     * @param context {@link Context} for this request
     * @return The catalog.
     * @exception IllegalArgumentException if no {@link Catalog}
     *  can be found
     *
     * @since Chain 1.2
     */
    @Override
    protected Catalog<String, Object, ServletWebContext> getCatalog(ServletWebContext context) {
        /* Assume that the object returned will be a catalog because of chain's
         * historical contract. */
        @SuppressWarnings("unchecked")
        Catalog<String, Object, ServletWebContext> catalog =
                (Catalog<String, Object, ServletWebContext>) context.get(getCatalogName());
        if (catalog == null) {
            catalog = super.getCatalog(context);
        }
        return catalog;
    }

}
