/*
 * $Header: /home/jerenkrantz/tmp/commons/commons-convert/cvs/home/cvs/jakarta-commons//chain/src/java/org/apache/commons/chain/web/servlet/config/Attic/ChainServlet.java,v 1.1 2003/09/29 15:34:45 husted Exp $
 * $Revision: 1.1 $
 * $Date: 2003/09/29 15:34:45 $
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
package org.apache.commons.chain.web.servlet.config;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.servlet.*;

import org.apache.commons.chain.Catalog;
import org.apache.commons.chain.config.ConfigRuleSet;
import org.apache.commons.chain.impl.CatalogBase;
import org.apache.commons.digester.Digester;
import org.apache.commons.digester.RuleSet;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.InputSource;

/**
 * <p>Servlet that configures a Catalog based on an XML configuration file.  The
 * catalog is stored in a servlet context attribute so that it is accessible to
 * other servlets.</p>
 * 
 * <p>The servlet has three different initialization parameters, as described
 * below:</p>
 * <ul>
 *     <li><code>config</code> specifies the location of the XML file used by
 *     the Digester to create the Catalog.  The locations of files are relative
 *     to the webapp's root context directory, so a
 *     <code>/WEB-INF/chain-config.xml</code> is an example location for the
 *     XML file.</li>
 *     <li><code>ruleset</code> (optional) specifies the fully qualified class
 *     name of the rule set the Digester should use to create the Catalog.  If
 *     this value is not specified the {@link
 *     org.apache.commons.chain.config.ConfigRuleSet} will be used.</li>
 *     <li><code>attribute</code> (optional) specifies the servlet context
 *     attribute in which the Catalog should be stored.  If this value is not
 *     specified the attribute used will be
 *     <code>Catalog.CATALOG_KEY</code>.</li>
 * </ul>
 * 
 * <p>One final note is that if a Catalog is found to already exist in the
 * context attribute to which the Catalog should be saved, the two catalogs will
 * be <em>merged</em> as described here. (TODO create this documentation and
 * link it here)</p>
 *
 * @author Matthew J. Sgarlata
 * @author Craig R. McClanahan
 */
public class ChainServlet extends GenericServlet {
    
    protected static final Log log = LogFactory.getLog(ChainServlet.class);
    
    /**
     * The servlet initialization parameter which specifies the location of the
     * XML file that describes the servlet chain we wish to load.
     */
    public static final String INIT_PARAM_CONFIG = "config";
    
    /**
     * The optional servlet initialization parameter that specifies the fully
     * qualified class name of the rule set that will be used to configure the
     * Digester.
     */
    public static final String INIT_PARAM_RULE_SET = "ruleset";
    
    /**
     * The optional servlet initialization parameter that specifies the
     * servlet context attribute in which the catalog will be stored.  If this
     * parameter is not specified, the catalog will be stored under the
     * attribute "org.apache.commons.chain.CATALOG".
     */
    public static final String INIT_PARAM_ATTRIBUTE = "attribute";
    
    /**
     * The servlet context attribute in which the catalog will be stored.
     */
    protected String catalogAttr = Catalog.CATALOG_KEY; ;

    /**
     * <p>The <code>RuleSet</code> to be used for configuring our Digester
     * parsing rules.</p>
     */
    protected RuleSet ruleSet = new ConfigRuleSet();
    
    /**
     * <p>The <code>Digester</code> to be used for parsing.</p>
     */
    protected Digester digester = null;


    /**
     * <p>Return the <code>Digester</code> instance to be used for
     * parsing, creating one if necessary.</p>
     */
    public Digester getDigester() {

        if (digester == null) {
            digester = new Digester();
            digester.setNamespaceAware(ruleSet.getNamespaceURI() != null);
            digester.setValidating(false);
            digester.addRuleSet(ruleSet);
        }
        return (digester);

    }

    /**
     * Use the Digester to create a {@link org.apache.commons.chain.Catalog}
     * based on the information in an XML file and store the catalog in the
     * servlet context.
     * @param servletConfig the configuration information supplied with this
     * servlet
     * @throws javax.servlet.ServletException if the servlet could not be initialized
     */
    public void init(ServletConfig servletConfig) throws ServletException {
        
        String configParam;
        String rulesetParam;
        String attributeParam;
        
        super.init(servletConfig);
        if (log.isInfoEnabled()) {
            log.info("Initializing chain servlet '" +
                servletConfig.getServletName() + "'");
        }
        
        configParam = servletConfig.getInitParameter(INIT_PARAM_CONFIG); 
        if (configParam == null) {
            log.error("The " + INIT_PARAM_CONFIG +
                " init-param must be specified");
            throw new ServletException("The " + INIT_PARAM_CONFIG +
                " init-param must be specified");
        }
        
        rulesetParam = servletConfig.getInitParameter(INIT_PARAM_RULE_SET);
        if (rulesetParam != null) {
            try {
                ruleSet = (RuleSet) Class.forName(rulesetParam).newInstance();
            }
            catch (Exception e) {
                log.error("Error creating RuleSet", e);
                throw new ServletException("Error creating RuleSet", e);
            }
        }
        
        attributeParam = servletConfig.getInitParameter(INIT_PARAM_ATTRIBUTE);
        if (attributeParam != null) {
            catalogAttr = attributeParam;
        }
        
        try {
            Catalog catalog;
            if (getServletContext().getAttribute(catalogAttr) != null) {
                catalog =
                    (Catalog) getServletContext().getAttribute(catalogAttr);
                if (log.isInfoEnabled()) {
                    log.info("Merging catalog with existing catalog " + catalog);
                }
            }
            else {
                catalog = new CatalogBase();
                if (log.isInfoEnabled()) {
                    log.info("Creating new catalog");
                }
            }
            Digester digester = getDigester();
            InputStream input = null;
            URL url = getServletContext().getResource(configParam);
            InputSource is = new InputSource(url.toExternalForm());
            input = getServletContext().getResourceAsStream(configParam);
            is.setByteStream(input);
            digester.push(catalog);
            digester.parse(is);
            getServletContext().setAttribute(catalogAttr, catalog);
        }
        catch (Exception e) {
            log.error("Error initializing the ChainServlet", e);
            throw new ServletException("Error initializing the ChainServlet",
                e);
        }
        
    }

    /**
     * Does nothing; this servlet's only purpose is to initialize a Chain
     * and store it in the servlet context.
     * @param request the request issued by the client
     * @param response the response to be returned to the cliengt
     * @throws javax.servlet.ServletException (this exception is never thrown)
     * @throws java.io.IOException (this exception is never thrown)
     */
    public void service(ServletRequest request, ServletResponse response)
        throws ServletException, IOException {

        ; // do nothing
    }
}