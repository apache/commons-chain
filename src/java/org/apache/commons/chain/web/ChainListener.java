/*
 * $Header: /home/jerenkrantz/tmp/commons/commons-convert/cvs/home/cvs/jakarta-commons//chain/src/java/org/apache/commons/chain/web/ChainListener.java,v 1.1 2003/10/01 04:23:00 craigmcc Exp $
 * $Revision: 1.1 $
 * $Date: 2003/10/01 04:23:00 $
 *
 * ====================================================================
 *
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 1999-2003 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution, if
 *    any, must include the following acknowlegement:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowlegement may appear in the software itself,
 *    if and wherever such third-party acknowlegements normally appear.
 *
 * 4. The names "The Jakarta Project", "Commons", and "Apache Software
 *    Foundation" must not be used to endorse or promote products derived
 *    from this software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache"
 *    nor may "Apache" appear in their names without prior written
 *    permission of the Apache Group.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */


package org.apache.commons.chain.web;


import java.io.InputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.apache.commons.chain.Catalog;
import org.apache.commons.chain.config.ConfigParser;
import org.apache.commons.chain.impl.CatalogBase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * <p>Optional <code>ServletContextListener</code> that automatically
 * scans chain configuration files in the current web application at
 * startup time, and exposes the result in a {@link Catalog} under a
 * specified servlet context attribute.  The following context init
 * parameters are utilized:</p>
 * <ul>
 * <li><strong>org.apache.commons.chain.CONFIG_RESOURCE</strong> -
 *     comma-deimited list of chain configuration webapp resources
 *     to be loaded.  If not specified, defaults to
 *     <code>/WEB-INF/chain-config.xml</code>.</li>
 * <li><strong>org.apache.commons.chain.CONFIG_CATALOG</strong> -
 *     Name of the servlet context attribute under which the
 *     resulting {@link Catalog} will be exposed.  If not specified,
 *     defaults to <code>catalog</code>.</li>
 * </ul>
 *
 * <p>When a web application that has configured this listener is
 * started, it will create a new {@link Catalog} (if needed) and populate it by
 * scanning configuration resources from the init parameter described
 * above.  If a {@link Catalog} instance already exists at the specified
 * attribute, any new parsed definitions will be merged into the existing
 * catalog (the last definition for a particular name wins).  In addition,
 * it will scan all of the JAR files in the <code>/WEB-INF/lib</code>
 * directory, and automatically scan any <code>META-INF/chain-config.xml</code>
 * files embedded in those JAR files.  In this way, it is easy to provide a
 * library JAR that includes command chain definitions, and have it
 * automatically get configured simply by dropping the JAR file into a
 * web application.</p>
 *
 * <p>This class requires Servlet 2.3 or later.  If you are running on
 * a Servlet 2.2 system, consider using {@link ChainServlet} instead.</p>
 *
 * @author Craig R. McClanahan
 * @version $Revision: 1.1 $ $Date: 2003/10/01 04:23:00 $
 */

public class ChainListener implements ServletContextListener {


    // ------------------------------------------------------ Manifest Constants


    /**
     * <p>The name of the context init parameter containing the name of the
     * servlet context attribute under which our resulting {@link Catalog}
     * will be stored.</p>
     */
    public static final String CONFIG_ATTR =
        "org.apache.commons.chain.CONFIG_ATTR";


    /**
     * <p>The name of the context init parameter containing a comma-delimited
     * list of web applicaton resources to be scanned.</p>
     */
    public static final String CONFIG_RESOURCE =
        "org.apache.commons.chain.CONFIG_RESOURCE";


    /**
     * <p>The default servlet context attribute key.</p>
     */
    private static final String CONFIG_ATTR_DEFAULT = "catalog";


    /**
     * <p>The default resource list.</p>
     */
    private static final String CONFIG_RESOURCE_DEFAULT =
        "/WEB-INF/chain-config.xml";


    // -------------------------------------------------------- Static Variables


    /**
     * <p>The <code>Log</code> instance for this class.</p>
     */
    private static final Log log = LogFactory.getLog(ChainListener.class);


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
        if (attr == null) {
            attr = CONFIG_ATTR_DEFAULT;
        }
        context.removeAttribute(attr);

    }


    /**
     * <p>Scan the required chain configuration resources, assemble the
     * configured chains into a {@link Catalog}, and expose it as a
     * servlet context attribute under the specified key.</p>
     *
     * @param event <code>ServletContextEvent</code> to be processed
     */
    public void contextInitialized(ServletContextEvent event) {

        ServletContext context = event.getServletContext();
        String attr = context.getInitParameter(CONFIG_ATTR);
        if (attr == null) {
            attr = CONFIG_ATTR_DEFAULT;
        }
        boolean defaulting = false;
        String resource = context.getInitParameter(CONFIG_RESOURCE);
        if (resource == null) {
            resource = CONFIG_RESOURCE_DEFAULT;
            defaulting = true;
        }

        // Retrieve or create the Catalog instance we will be updating
        Catalog catalog = (Catalog) context.getAttribute(attr);
        if (catalog == null) {
            catalog = new CatalogBase();
        }

        // Construct the configuration resource parser we will use
        // FIXME - Do we need to make this more configurable?
        ConfigParser parser = new ConfigParser();

        // Parse the resources specified in our init parameter (if any)
        String path = null;
        try {
            while (true) {
                int comma = resource.indexOf(",");
                if (comma < 0) {
                    path = resource.trim();
                    resource = "";
                } else {
                    path = resource.substring(0, comma);
                    resource = resource.substring(comma + 1);
                }
                if (path.length() < 1) {
                    break;
                }
                URL url = context.getResource(path);
                if (url == null) {
                    if (defaulting) {
                        continue; // It is OK not to have a default config file
                    } else {
                        throw new IllegalStateException
                            ("Missing chain config resource '" + path + "'");
                    }
                }
                if (log.isDebugEnabled()) {
                    log.debug("Loading chain config resource '" + path + "'");
                }
                parser.parse(catalog, url);
            }
        } catch (Exception e) {
            throw new RuntimeException
                ("Exception parsing chain config resource '" + path + "': " +
                 e.getMessage());
        }

        // Parse the resources in any embedded JAR files
        Set jars = context.getResourcePaths("/WEB-INF/lib");
        if (jars == null) {
            jars = new HashSet();
        }
        Iterator paths = jars.iterator();
        while (paths.hasNext()) {
            path = (String) paths.next();
            if (!path.endsWith(".jar")) {
                continue;
            }
            URL resourceURL = null;
            try {
                URL jarURL = context.getResource(path);
                resourceURL = new URL("jar:" +
                                      translate(jarURL.toExternalForm()) +
                                      "!/META-INF/chain-config.xml");
                parser.parse(catalog, resourceURL);
            } catch (Exception e) {
                throw new RuntimeException
                    ("Exception parsing chain config resource '" +
                     resourceURL.toExternalForm() + "': " +
                     e.getMessage());
            }
        }

        // Expose the completed catalog
        context.setAttribute(attr, catalog);

    }


    // --------------------------------------------------------- Private Methods


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
            value = value.substring(0, index) + value.substring(index + 1);
        }
        return (value);

    }


}
