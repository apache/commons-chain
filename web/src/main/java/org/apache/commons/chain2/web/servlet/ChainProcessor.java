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
import org.apache.commons.chain2.impl.CatalogFactoryBase;
import org.apache.commons.chain2.web.ChainServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * <p>Custom subclass of {@link ChainServlet} that also dispatches incoming
 * requests to a configurable {@link Command} loaded from the specified
 * {@link Catalog}.</p>
 *
 * <p>In addition to the <em>servlet</em> init parameters supported by
 * {@link ChainServlet}, this class supports the following additional
 * parameters:</p>
 * <ul>
 * <li><strong>org.apache.commons.chain2.CATALOG</strong> - Name of the
 *     catalog from which to acquire commands to be executed.  If not
 *     specified, the default catalog for this application will be used.</li>
 * <li><strong>org.apache.commons.chain2.COMMAND</strong> - Name of the
 *     {@link Command} (looked up in our configured {@link Catalog} used
 *     to process all incoming servlet requests.  If not specified,
 *     defaults to <code>command</code>.</li>
 * </ul>
 *
 * <p>Also, the <code>org.apache.commons.chain2.CONFIG_ATTR</code>
 * init parameter is also used to identify the
 * {@link org.apache.commons.chain2.Context} attribute under
 * which our configured {@link Catalog} will be made available to
 * {@link Command}s processing our requests, in addition to its definition
 * of the <code>ServletContext</code> attribute key under which the
 * {@link Catalog} is available.</p>
 *
 */
public class ChainProcessor extends ChainServlet {

    // ------------------------------------------------------ Manifest Constants

    /**
     *
     */
    private static final long serialVersionUID = 20120724L;

    /**
     * <p>The name of the servlet init parameter containing the name of the
     * {@link Catalog} to use for processing incoming requests.</p>
     */
    public static final String CATALOG =
        "org.apache.commons.chain2.CATALOG";

    /**
     * <p>The default request attribute under which we expose the
     * {@link Catalog} being used to subordinate {@link Command}s.</p>
     */
    public static final String CATALOG_DEFAULT =
        "org.apache.commons.chain2.CATALOG";

    /**
     * <p>The name of the servlet init parameter containing the name of the
     * {@link Command} (loaded from our configured {@link Catalog} to use
     * for processing each incoming request.</p>
     */
    public static final String COMMAND =
        "org.apache.commons.chain2.COMMAND";

    /**
     * <p>The default command name.</p>
     */
    private static final String COMMAND_DEFAULT = "command";

    // ------------------------------------------------------ Instance Variables

    /**
     * <p>The name of the context attribute under which our {@link Catalog}
     * is stored.  This value is also used as the name of the
     * context attribute under which the catalog is exposed to commands.
     * If not specified, we will look up commands in the appropriate
     * {@link Catalog} retrieved from our {@link CatalogFactoryBase}.</p>
     */
    private String attribute;

    /**
     * <p>The name of the {@link Catalog} to retrieve from the
     * {@link CatalogFactoryBase} for this application, or <code>null</code>
     * to select the default {@link Catalog}.</p>
     */
    private String catalog;

    /**
     * <p>The name of the {@link Command} to be executed for each incoming
     * request.</p>
     */
    private String command;

    // --------------------------------------------------------- Servlet Methods

    /**
     * <p>Clean up as this application is shut down.</p>
     */
    @Override
    public void destroy() {
        super.destroy();
        attribute = null;
        catalog = null;
        command = null;
    }

    /**
     * <p>Cache the name of the command we should execute for each request.</p>
     *
     * @throws ServletException if an initialization error occurs
     */
    @Override
    public void init() throws ServletException {
        super.init();
        attribute = getServletConfig().getInitParameter(CONFIG_ATTR);
        catalog = getServletConfig().getInitParameter(CATALOG);
        command = getServletConfig().getInitParameter(COMMAND);
        if (command == null) {
            command = COMMAND_DEFAULT;
        }
    }

    /**
     * <p>Configure a {@link ServletWebContext} for the current request, and
     * pass it to the <code>execute()</code> method of the specified
     * {@link Command}, loaded from our configured {@link Catalog}.</p>
     *
     * @param request The request we are processing
     * @param response The response we are creating
     *
     * @throws IOException if an input/output error occurs
     * @throws ServletException if a servlet exception occurs
     */
    @Override
    public void service(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {

        ServletWebContext<String, Object> context =
            new ServletWebContextBase(getServletContext(), request, response);
        Catalog<String, Object, ServletWebContext<String, Object>> theCatalog;

        if (attribute != null) {
            ServletContext servletContext = getServletContext();
            Object testAttribute = servletContext.getAttribute(this.attribute);

            if (testAttribute instanceof Catalog) {
                /* "attribute" should always contain an instance of a
                 * Catalog class according to the historical chain convention.
                 * Although, we now double-check that it is in fact a catalog
                 * here, we still need to suppress warnings because of the
                 * type erasure of generics. */
                @SuppressWarnings("unchecked")
                Catalog<String, Object, ServletWebContext<String, Object>> attributeCatalog =
                     (Catalog<String, Object, ServletWebContext<String, Object>>)testAttribute;
                theCatalog = attributeCatalog;
            } else {
                String msg = "The object stored as the attribute [" +
                        attribute + "] was not of the expected type [" +
                        "Catalog]";
                throw new IllegalArgumentException(msg);
            }
        } else if (catalog != null) {
            theCatalog = CatalogFactoryBase.<String, Object, ServletWebContext<String, Object>>getInstance()
                    .getCatalog(catalog);
        } else {
            theCatalog = CatalogFactoryBase.<String, Object, ServletWebContext<String, Object>>getInstance()
                    .getCatalog();
        }
        if (attribute == null) {
            request.setAttribute(CATALOG_DEFAULT, theCatalog);
        }
        Command<String, Object, ServletWebContext<String, Object>> cmd = theCatalog
                .<Command<String, Object, ServletWebContext<String, Object>>>getCommand(this.command);
        try {
            cmd.execute(context);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

}
