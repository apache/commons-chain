/*
 * $Header: /home/jerenkrantz/tmp/commons/commons-convert/cvs/home/cvs/jakarta-commons//chain/src/java/org/apache/commons/chain/web/ChainServlet.java,v 1.5 2003/10/20 05:25:41 martinc Exp $
 * $Revision: 1.5 $
 * $Date: 2003/10/20 05:25:41 $
 *
/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2003 The Apache Software Foundation.  All rights
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
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "The Jakarta Project", "Commons", and "Apache Software
 *    Foundation" must not be used to endorse or promote products derived
 *    from this software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache",
 *    nor may "Apache" appear in their name, without prior written
 *    permission of the Apache Software Foundation.
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


import java.io.IOException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.chain.Catalog;
import org.apache.commons.chain.config.ConfigParser;
import org.apache.commons.chain.impl.CatalogBase;
import org.apache.commons.digester.RuleSet;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * <p><code>Servlet</code> that automatically scans chain configuration files
 * in the current web application at startup time, and exposes the result in a
 * {@link Catalog} under a specified servlet context attribute.  The following
 * <em>servlet</em> init parameters are utilized:</p>
 * <ul>
 * <li><strong>org.apache.commons.chain.CONFIG_CLASS_RESOURCE</strong> -
 *     comma-delimited list of chain configuration resources to be loaded
 *     via <code>ClassLoader.getResource()</code> calls.  If not specified,
 *     no class loader resources will be loaded.</li>
 * <li><strong>org.apache.commons.chain.CONFIG_WEB_RESOURCE</strong> -
 *     comma-delimited list of chain configuration webapp resources
 *     to be loaded.  If not specified, no web application resources
 *     will be loaded.</li>
 * <li><strong>org.apache.commons.chain.CONFIG_ATTR</strong> -
 *     Name of the servlet context attribute under which the
 *     resulting {@link Catalog} will be created or updated.  If not specified,
 *     defaults to <code>catalog</code>.</li>
 * <li><strong>org.apache.commons.chain.RULE_SET</strong> -
 *     Fully qualified class name of a Digester <code>RuleSet</code>
 *     implementation to use for parsing configuration resources (this
 *     class must have a public zero-args constructor).  If not defined,
 *     the standard <code>RuleSet</code> implementation will be used.</li>
 * </ul>
 *
 * <p>When a web application that has configured this servlet is
 * started, it will acquire the {@link Catalog} under the specified servlet
 * context attribute key, creating a new one if there is none already there.
 * This {@link Catalog} will then be populated by scanning configuration
 * resources from the following sources (loaded in this order):</p>
 * <ul>
 * <li>Resources loaded from specified resource paths from the
 *     webapp's class loader (via <code>ClassLoader.getResource()</code>).</li>
 * <li>Resources loaded from specified resource paths in the web application
 *     archive (via <code>ServetContext.getResource()</code>).</li>
 * </ul>
 *
 * <p>This class runs on Servlet 2.2 or later.  If you are running on a
 * Servlet 2.3 or later system, you should also consider using
 * {@link ChainListener} to initialize your {@link Catalog}.  Note that
 * {@link ChainListener} uses parameters of the same names, but they are
 * <em>context</em> init parameters instead of <em>servlet</em> init
 * parameters. Because of this, you can use both facilities in the
 * same application, if desired.</p>
 *
 * @author Matthew J. Sgarlata
 * @author Craig R. McClanahan
 * @author Ted Husted
 */

public class ChainServlet extends HttpServlet {
    

    // ------------------------------------------------------ Manifest Constants


    /**
     * <p>The name of the context init parameter containing the name of the
     * servlet context attribute under which our resulting {@link Catalog}
     * will be stored.</p>
     */
    public static final String CONFIG_ATTR =
        "org.apache.commons.chain.CONFIG_ATTR";


    /**
     * <p>The default servlet context attribute key.</p>
     */
    public static final String CONFIG_ATTR_DEFAULT = "catalog";


    /**
     * <p>The name of the context init parameter containing a comma-delimited
     * list of class loader resources to be scanned.</p>
     */
    public static final String CONFIG_CLASS_RESOURCE =
        "org.apache.commons.chain.CONFIG_CLASS_RESOURCE";


    /**
     * <p>The name of the context init parameter containing a comma-delimited
     * list of web applicaton resources to be scanned.</p>
     */
    public static final String CONFIG_WEB_RESOURCE =
        "org.apache.commons.chain.CONFIG_WEB_RESOURCE";


    /**
     * <p>The name of the context init parameter containing the fully
     * qualified class name of the <code>RuleSet</code> implementation
     * for configuring our {@link ConfigParser}.</p>
     */
    public static final String RULE_SET =
        "org.apache.commons.chain.RULE_SET";


    // -------------------------------------------------------- Static Variables


    /**
     * <p>The <code>Log</code> instance to use with this class.</p>
     */
    protected static final Log log = LogFactory.getLog(ChainServlet.class);


    // --------------------------------------------------------- Servlet Methods


    /**
     * <p>Create (if necessary) and configure a {@link Catalog} from the
     * servlet init parameters that have been specified.</p>
     *
     * @throws ServletException if the servlet could not be initialized
     */
    public void init() throws ServletException {
        
        ServletConfig config = getServletConfig();
        ServletContext context = getServletContext();
        if (log.isInfoEnabled()) {
            log.info("Initializing chain servlet '" +
                     config.getServletName() + "'");
        }

        // Retrieve servlet init parameters that we need
        String attr = config.getInitParameter(CONFIG_ATTR);
        if (attr == null) {
            attr = CONFIG_ATTR_DEFAULT;
        }
        String classResources =
            context.getInitParameter(CONFIG_CLASS_RESOURCE);
        String ruleSet = context.getInitParameter(RULE_SET);
        String webResources = context.getInitParameter(CONFIG_WEB_RESOURCE);

        // Retrieve or create the Catalog instance we will be updating
        Catalog catalog = (Catalog) context.getAttribute(attr);
        if (catalog == null) {
            catalog = new CatalogBase();
        }

        // Construct the configuration resource parser we will use
        ConfigParser parser = new ConfigParser();
        if (ruleSet != null) {
            try {
                ClassLoader loader =
                    Thread.currentThread().getContextClassLoader();
                if (loader == null) {
                    loader = this.getClass().getClassLoader();
                }
                Class clazz = loader.loadClass(ruleSet);
                parser.setRuleSet((RuleSet) clazz.newInstance());
            } catch (Exception e) {
                throw new ServletException("Exception initalizing RuleSet '" +
                                           ruleSet + "' instance", e);
            }
        }

        // Parse the resources specified in our init parameters (if any)
        ChainResources.parseClassResources
            (catalog, classResources, parser);
        ChainResources.parseWebResources
            (catalog, context, webResources, parser);

        // Expose the completed catalog
        context.setAttribute(attr, catalog);
        
    }


    /**
     * <p>Does nothing; this servlet's only purpose is to initialize a Chain
     * and store it in the servlet context.</p>
     *
     * @param request the request issued by the client
     * @param response the response to be returned to the cliengt
     *
     * @throws javax.servlet.ServletException (this exception is never thrown)
     * @throws java.io.IOException (this exception is never thrown)
     */
    public void service(HttpServletRequest request,
                        HttpServletResponse response)
        throws ServletException, IOException {

        ; // do nothing

    }


}
