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
package org.apache.commons.chain2.config.xml;

import org.apache.commons.chain2.CatalogFactory;
import org.apache.commons.chain2.config.ChainConfigurationException;
import org.apache.commons.chain2.config.ConfigParser;
import org.apache.commons.digester3.Digester;
import org.apache.commons.digester3.RuleSet;

import java.net.URL;
import java.util.Map;

/**
 * <p>Class to parse the contents of an XML configuration file (using
 * Commons Digester) that defines and configures commands and command chains
 * to be registered in a {@link org.apache.commons.chain2.Catalog}.  Advanced users can configure the
 * detailed parsing behavior by configuring the properties of an instance
 * of this class prior to calling the <code>parse()</code> method.  It
 * is legal to call the <code>parse()</code> method more than once, in order
 * to parse more than one configuration document.</p>
 *
 * @version $Id: ConfigParser.java 1364104 2012-07-21 14:25:54Z elijah $
 */
public class XmlConfigParser implements ConfigParser {
    // ----------------------------------------------------- Instance Variables

    /**
     * <p>The <code>RuleSet</code> to be used for configuring our Digester
     * parsing rules.</p>
     */
    private RuleSet ruleSet = new ConfigRuleSet();

    /**
     * <p>Should Digester use the context class loader?
     */
    private boolean useContextClassLoader = true;

    // ------------------------------------------------------------- Constructor

    public XmlConfigParser() {
    }

    public XmlConfigParser(String ruleSet, ClassLoader loader) {
        if (ruleSet == null) {
            throw new IllegalArgumentException("ConfigParser can't be " +
                    "instantiated with a null ruleSet class name");
        }
        if (loader == null) {
            throw new IllegalArgumentException("ConfigParser can't be " +
                    "instantiated with a null class loader reference");
        }

        try {
            Class<?> clazz = loader.loadClass(ruleSet);
            setRuleSet((RuleSet) clazz.newInstance());
        } catch (Exception e) {
            throw new RuntimeException("Exception initializing RuleSet '"
                    + ruleSet + "' instance: "
                    + e.getMessage());
        }
    }

    // ------------------------------------------------------------- Properties

    /**
     * <p>Return the <code>Digester</code> instance to be used for
     * parsing, creating one if necessary.</p>
     * @return A Digester instance.
     */
    public Digester getDigester() {
        Digester digester = new Digester();
        RuleSet ruleSet = getRuleSet();
        digester.setNamespaceAware(ruleSet.getNamespaceURI() != null);
        digester.setUseContextClassLoader(getUseContextClassLoader());
        digester.setValidating(false);
        digester.addRuleSet(ruleSet);
        return (digester);
    }

    /**
     * <p>Return the <code>RuleSet</code> to be used for configuring
     * our <code>Digester</code> parsing rules, creating one if necessary.</p>
     * @return The RuleSet for configuring a Digester instance.
     */
    public RuleSet getRuleSet() {
        return (ruleSet);
    }

    /**
     * <p>Set the <code>RuleSet</code> to be used for configuring
     * our <code>Digester</code> parsing rules.</p>
     *
     * @param ruleSet The new RuleSet to use
     */
    public void setRuleSet(RuleSet ruleSet) {
        this.ruleSet = ruleSet;
    }

    /**
     * <p>Return the "use context class loader" flag.  If set to
     * <code>true</code>, Digester will attempt to instantiate new
     * command and chain instances from the context class loader.</p>
     * @return <code>true</code> if Digester should use the context class loader.
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
     * <p>Parse the XML document at the specified URL using the configured
     * <code>RuleSet</code>, registering catalogs with nested chains and
     * commands as they are encountered.  Use this method <strong>only</strong>
     * if you have included one or more <code>factory</code> elements in your
     * configuration resource.</p>
     *
     * @param <K> the type of keys maintained by the context associated with this command
     * @param <V> the type of mapped values
     * @param <C> Type of the context associated with this command
     * @param url <code>URL</code> of the XML document to be parsed
     * @return a CatalogFactory instance parsed from the given location
     * @exception ChainConfigurationException if a parsing error occurs
     */
    public <K, V, C extends Map<K, V>> CatalogFactory<K, V, C> parse(URL url) {
        // Prepare our Digester instance
        Digester digester = getDigester();
        digester.clear();

        // Parse the configuration document
        try {
            digester.parse(url);
        } catch (Exception e) {
            String msg = String.format(
                    "Error parsing digester configuration at url: %s",
                    url);
            throw new ChainConfigurationException(msg, e);
        }
        // FIXME get rid of singleton pattern and create a new instance here
        return CatalogFactory.getInstance();
    }

}
