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

import org.apache.commons.chain2.Catalog;
import org.apache.commons.chain2.CatalogFactory;
import org.apache.commons.chain2.config.ConfigParser;
import org.apache.commons.chain2.impl.CatalogBase;
import org.apache.commons.chain2.web.servlet.ServletWebContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.InputStream;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

/**
 * <p><code>ServletContextListener</code> that automatically
 * scans chain configuration files in the current web application at
 * startup time, and exposes the result in a {@link Catalog} under a
 * specified servlet context attribute.  The following <em>context</em> init
 * parameters are utilized:</p>
 * <ul>
 * <li><strong>org.apache.commons.chain2.CONFIG_CLASS_RESOURCE</strong> -
 *     comma-delimited list of chain configuration resources to be loaded
 *     via <code>ClassLoader.getResource()</code> calls.  If not specified,
 *     no class loader resources will be loaded.</li>
 * <li><strong>org.apache.commons.chain2.CONFIG_WEB_RESOURCE</strong> -
 *     comma-delimited list of chain configuration webapp resources
 *     to be loaded.  If not specified, no web application resources
 *     will be loaded.</li>
 * <li><strong>org.apache.commons.chain2.CONFIG_ATTR</strong> -
 *     Name of the servlet context attribute under which the
 *     resulting {@link Catalog} will be created or updated.
 *     If not specified, it is expected that parsed resources will
 *     contain <code>&lt;catalog&gt;</code> elements (which will
 *     cause registration of the created {@link Catalog}s into
 *     the {@link CatalogFactory} for this application, and no
 *     servlet context attribute will be created.
 *     <strong>NOTE</strong> - This parameter is deprecated.</p>
 * <li><strong>org.apache.commons.chain2.RULE_SET</strong> -
 *     Fully qualified class name of a Digester <code>RuleSet</code>
 *     implementation to use for parsing configuration resources (this
 *     class must have a public zero-args constructor).  If not defined,
 *     the standard <code>RuleSet</code> implementation will be used.</li>
 * </ul>
 *
 * <p>When a web application that has configured this listener is
 * started, it will acquire the {@link Catalog} under the specified servlet
 * context attribute key, creating a new one if there is none already there.
 * This {@link Catalog} will then be populated by scanning configuration
 * resources from the following sources (loaded in this order):</p>
 * <ul>
 * <li>Resources loaded from any <code>META-INF/chain-config.xml</code>
 *     resource found in a JAR file in <code>/WEB-INF/lib</code>.</li>
 * <li>Resources loaded from specified resource paths from the
 *     webapp's class loader (via <code>ClassLoader.getResource()</code>).</li>
 * <li>Resources loaded from specified resource paths in the web application
 *     archive (via <code>ServletContext.getResource()</code>).</li>
 * </ul>
 *
 * <p>If no attribute key is specified, on the other hand, parsed configuration
 * resources are expected to contain <code>&lt;catalog&gt;</code> elements,
 * and the catalogs will be registered with the {@link CatalogFactory}
 * for this web application.</p>
 *
 * <p>This class requires Servlet 2.3 or later.  If you are running on
 * Servlet 2.2 system, consider using {@link ChainServlet} instead.
 * Note that {@link ChainServlet} uses parameters of the
 * same names, but they are <em>servlet</em> init parameters instead
 * of <em>context</em> init parameters.  Because of this, you can use
 * both facilities in the same application, if desired.</p>
 *
 * @version $Id$
 */
public class ChainListener implements ServletContextListener {
    {
        CatalogFactory.checkForValidConfigurationModule();
    }

    // ------------------------------------------------------ Manifest Constants

    /**
     * <p>The name of the context init parameter containing the name of the
     * servlet context attribute under which our resulting {@link Catalog}
     * will be stored.</p>
     */
    public static final String CONFIG_ATTR =
        "org.apache.commons.chain2.CONFIG_ATTR";

    /**
     * <p>The name of the context init parameter containing a comma-delimited
     * list of class loader resources to be scanned.</p>
     */
    public static final String CONFIG_CLASS_RESOURCE =
        "org.apache.commons.chain2.CONFIG_CLASS_RESOURCE";

    /**
     * <p>The name of the context init parameter containing a comma-delimited
     * list of web applicaton resources to be scanned.</p>
     */
    public static final String CONFIG_WEB_RESOURCE =
        "org.apache.commons.chain2.CONFIG_WEB_RESOURCE";

    /**
     * <p>The name of the context init parameter containing the fully
     * qualified class name of the <code>RuleSet</code> implementation
     * for configuring our {@link ConfigParser}.</p>
     */
    public static final String RULE_SET =
        "org.apache.commons.chain2.RULE_SET";

    // ------------------------------------------ ServletContextListener Methods

    /**
     * <p>Remove the configured {@link Catalog} from the servlet context
     * attributes for this web application.</p>
     *
     * @param event <code>ServletContextEvent</code> to be processed
     */
    public void contextDestroyed(ServletContextEvent event) {
        ServletContext context = event.getServletContext();
        String attr = context.getInitParameter(CONFIG_ATTR);
        if (attr != null) {
            context.removeAttribute(attr);
        }
        CatalogFactory.clear();
    }

    /**
     * <p>Scan the required chain configuration resources, assemble the
     * configured chains into a {@link Catalog}, and expose it as a
     * servlet context attribute under the specified key.</p>
     *
     * @param event <code>ServletContextEvent</code> to be processed
     */
    public void contextInitialized(ServletContextEvent event) {
        Log log = LogFactory.getLog(ChainListener.class);
        if (log.isInfoEnabled()) {
            log.info("Initializing chain listener");
        }
        ServletContext context = event.getServletContext();

        // Retrieve context init parameters that we need
        String attr = context.getInitParameter(CONFIG_ATTR);
        String classResources =
            context.getInitParameter(CONFIG_CLASS_RESOURCE);
        String ruleSet = context.getInitParameter(RULE_SET);
        String webResources = context.getInitParameter(CONFIG_WEB_RESOURCE);

        // Retrieve or create the Catalog instance we may be updating
        Catalog<String, Object, ServletWebContext<String, Object>> catalog = null;
        if (attr != null) {
            Object catalogRef = context.getAttribute(attr);

            if (catalogRef == null || !(catalogRef instanceof Catalog) ) {
                catalog = new CatalogBase<String, Object, ServletWebContext<String, Object>>();
            } else {
                /* Assume that we are getting an object of the type Catalog from the context's
                 * attribute because that is the historical contract. */
                catalog = (Catalog<String, Object, ServletWebContext<String, Object>>)catalogRef;
            }
        }

        // Construct the configuration resource parser we will use
        ClassLoader cl = Thread.currentThread().getContextClassLoader() == null ?
                this.getClass().getClassLoader() :
                Thread.currentThread().getContextClassLoader();

        ConfigParser parser = ruleSet == null ?
                new ConfigParser() : new ConfigParser(ruleSet, cl);

        // Parse the resources specified in our init parameters (if any)
        if (attr == null) {
            parseJarResources(context, parser, log);
            ChainResources.parseClassResources
                (classResources, parser);
            ChainResources.parseWebResources
                (context, webResources, parser);
        } else {
            parseJarResources(context, parser, log);
            ChainResources.parseClassResources
                (classResources, parser);
            ChainResources.parseWebResources
                (context, webResources, parser);
        }

        // Expose the completed catalog (if requested)
        if (attr != null) {
            context.setAttribute(attr, catalog);
        }
    }

    // --------------------------------------------------------- Private Methods

    /**
     * <p>Parse resources found in JAR files in the <code>/WEB-INF/lib</code>
     * subdirectory (if any).</p>
     *
     * @param context <code>ServletContext</code> for this web application
     * @param parser {@link ConfigParser} to use for parsing
     */
    private void parseJarResources(ServletContext context,
                                   ConfigParser parser, Log log) {
        @SuppressWarnings( "unchecked" ) // it is known that always returns String inside
        Set<String> jars = context.getResourcePaths("/WEB-INF/lib");
        if (jars == null) {
            jars = new HashSet<String>();
        }
        String path;

        for (String jar : jars) {

            path = jar;
            if (!path.endsWith(".jar")) {
                continue;
            }
            URL resourceURL = null;
            try {
                URL jarURL = context.getResource(path);
                resourceURL = new URL("jar:"
                        + translate(jarURL.toExternalForm())
                        + "!/META-INF/chain-config.xml");
                InputStream is = null;
                try {
                    is = resourceURL.openStream();
                } catch (Exception e) {
                    // means there is no such resource
                }
                if (is == null) {
                    if (log.isDebugEnabled()) {
                        log.debug("Not Found: " + resourceURL);
                    }
                    continue;
                } else {
                    is.close();
                }
                if (log.isDebugEnabled()) {
                    log.debug("Parsing: " + resourceURL);
                }
                parser.parse(resourceURL);
            } catch (Exception e) {
                String externalURL = "null";
                if (resourceURL != null) {
                    externalURL = resourceURL.toExternalForm();
                }
                throw new RuntimeException
                        ("Exception parsing chain config resource '"
                                + externalURL + "': "
                                + e.getMessage());
            }
        }
    }

    /**
     * <p>Translate space character into <code>&pct;20</code> to avoid problems
     * with paths that contain spaces on some JVMs.</p>
     *
     * @param value Value to translate
     */
    private String translate(String value) {
        while (true) {
            int index = value.indexOf(' ');
            if (index < 0) {
                break;
            }
            value = value.substring(0, index) + "%20" + value.substring(index + 1);
        }
        return (value);
    }

}
