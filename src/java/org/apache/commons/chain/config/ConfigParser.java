/*
 * $Header: /home/jerenkrantz/tmp/commons/commons-convert/cvs/home/cvs/jakarta-commons//chain/src/java/org/apache/commons/chain/config/ConfigParser.java,v 1.2 2003/10/12 09:10:54 rdonkin Exp $
 * $Revision: 1.2 $
 * $Date: 2003/10/12 09:10:54 $
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
 *    any, must include the following acknowledgement:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowledgement may appear in the software itself,
 *    if and wherever such third-party acknowledgements normally appear.
 *
 * 4. The names "The Jakarta Project", "Commons", and "Apache Software
 *    Foundation" must not be used to endorse or promote products derived
 *    from this software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache"
 *    nor may "Apache" appear in their names without prior written
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

package org.apache.commons.chain.config;


import java.net.URL;
import org.apache.commons.chain.Catalog;
import org.apache.commons.digester.Digester;
import org.apache.commons.digester.RuleSet;
import org.xml.sax.InputSource;


/**
 * <p>Class to parse the contents of an XML configuration file (using
 * Commons Digester) that defines and configures commands and command chains
 * to be registered in a {@link Catalog}.  Advanced users can configure the
 * detailed parsing behavior by configuring the properties of an instance
 * of this class prior to calling the <code>parse()</code> method.  It
 * is legal to call the <code>parse()</code> method more than once, in order
 * to parse more than one configuration document.</p>
 *
 * @author Craig R. McClanahan
 * @version $Revision: 1.2 $ $Date: 2003/10/12 09:10:54 $
 */
public class ConfigParser {


    // ----------------------------------------------------- Instance Variables


    /**
     * <p>The <code>Digester</code> to be used for parsing.</p>
     */
    private Digester digester = null;


    /**
     * <p>The <code>RuleSet</code> to be used for configuring our Digester
     * parsing rules.</p>
     */
    private RuleSet ruleSet = null;


    /**
     * <p>Should Digester use the context class loader?
     */
    private boolean useContextClassLoader = true;


    // ------------------------------------------------------------- Properties


    /**
     * <p>Return the <code>Digester</code> instance to be used for
     * parsing, creating one if necessary.</p>
     */
    public Digester getDigester() {

        if (digester == null) {
            digester = new Digester();
            RuleSet ruleSet = getRuleSet();
            digester.setNamespaceAware(ruleSet.getNamespaceURI() != null);
            digester.setUseContextClassLoader(getUseContextClassLoader());
            digester.setValidating(false);
            digester.addRuleSet(ruleSet);
        }
        return (digester);

    }


    /**
     * <p>Return the <code>RuleSet</code> to be used for configuring
     * our <code>Digester</code> parsing rules, creating one if necessary.</p>
     */
    public RuleSet getRuleSet() {

        if (ruleSet == null) {
            ruleSet = new ConfigRuleSet();
        }
        return (ruleSet);

    }


    /**
     * <p>Set the <code>RuleSet</code> to be used for configuring
     * our <code>Digester</code> parsing rules.</p>
     *
     * @param ruleSet The new RuleSet to use
     */
    public void setRuleSet(RuleSet ruleSet) {

        this.digester = null;
        this.ruleSet = ruleSet;

    }


    /**
     * <p>Return the "use context class loader" flag.  If set to
     * <code>true</code>, Digester will attempt to instantiate new
     * command and chain instances from the context class loader.</p>
     */
    public boolean getUseContextClassLoader() {

        return (this.useContextClassLoader);

    }


    /**
     * <p>Set the "use context class loader" flag.</p>
     *
     * @param useContextClassLoader The new flag value
     */
    public void setUseContextClassLoader(boolean useContextClassLoader) {

        this.useContextClassLoader = useContextClassLoader;

    }


    // --------------------------------------------------------- Public Methods


    /**
     * <p>Parse the XML document at the specified URL, using the configured
     * <code>RuleSet</code>, registering top level commands into the specified
     * {@link Catalog}.</p>
     *
     * @param catalog {@link Catalog} into which configured chains are
     *  to be registered
     * @param url <code>URL</code> of the XML document to be parsed
     *
     * @exception Exception if a parsing error occurs
     */
    public void parse(Catalog catalog, URL url) throws Exception {

        // Prepare our Digester instance
        Digester digester = getDigester();
        digester.clear();
        digester.push(catalog);

        // Prepare our InputSource
        InputSource source = new InputSource(url.toExternalForm());
        source.setByteStream(url.openStream());

        // Parse the configuration document
        digester.parse(source);

    }


}
