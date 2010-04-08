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
package org.apache.commons.chain.config;


import org.apache.commons.digester.Digester;
import org.apache.commons.digester.RuleSetBase;


/**
 * <p>Digester <code>RuleSet</code> for configuring <em>Chain of
 * Responsibility</em> command chains, and adding them to an appropriate
 * {@link org.apache.commons.chain.Catalog}.  The following properties
 * may be configured prior to executing the <code>addRuleInstance()</code>
 * method in order to influence the rules that get added, with default
 * values in square brackets:</p>
 * <ul>
 * <li><strong>catalogClass</strong> -- Fully qualified name of the
 *     implementation class used to create new
 *     {@link org.apache.commons.chain.Catalog} instances.
 *     If not specified, the default value is
 *     <code>org.apache.commons.chain.impl.CatalogBsae</code>.</li>
 * <li><strong>catalogElement</strong> -- Name of the XML element representing
 *     the addition of a {@link org.apache.commons.chain.Catalog}.
 *     Any such catalog that is created will be registered with the
 *     {@link org.apache.commons.chain.CatalogFactory} instance for our
 *     application, under the name specified by the <code>nameAttribute</code>
 *     attribute (if present), or as the default {@link org.apache.commons.chain.Catalog}.
 *     If not specified, the default value is <code>catalog</code>.</li>
 * <li><strong>chainClass</strong> -- Fully qualified name of the implementation
 *     class used to create new {@link org.apache.commons.chain.Chain} instances.
 *     If not specified, the default value is
 *     <code>org.apache.commons.chain.impl.ChainBase</code>.
 *     </li>
 * <li><strong>chainElement</strong> -- Name of the XML element representing
 *     the addition of a {@link org.apache.commons.chain.Chain}.  A chain
 *     element has the same functionality as a command element, except that
 *     it defaults the implementation class to
 *     <code>org.apache.commons.chain.impl.ChainBase</code>.  [chain]</li>
 * <li><strong>classAttribute</strong> -- Attribute on a chain (optional) or
 *     command (required) element that specifies the fully qualified class
 *     name of the implementation class that should be instantiated.
 *     [className]</li>
 * <li><strong>commandElement</strong> -- Name of the XML element
 *     representing the addition of a {@link org.apache.commons.chain.Command}.
 *     An implementation class name must be provided on the attribute named by the
 *     <code>classAttribute</code> property.  [command]</li>
 * <li><strong>defineElement</strong> -- Name of the XML element
 *     that associates the element specified by the <code>nameAttribute</code>
 *     attributes with a {@link org.apache.commons.chain.Command} or
 *     {@link org.apache.commons.chain.Chain} implementation class
 *     named by the <code>classAttribute</code> attribute.  [define]</li>
 * <li><strong>nameAttribute</strong> -- Attribute on an outermost chain or
 *     command element that will be used to register this command with the
 *     associated {@link org.apache.commons.chain.Catalog} instance on the stack.
 *     [name]</li>
 * <li><strong>namespaceURI</strong> -- The XML namespace URI with which these
 *     rules will be associated, or <code>null</code> for no namespace.
 *     [null]</li>
 * </ul>
 *
 * @author Craig R. McClanahan
 * @version $Revision$ $Date$
 */

public class ConfigRuleSet extends RuleSetBase {


    // ----------------------------------------------------- Instance Variables


    private String catalogClass = "org.apache.commons.chain.impl.CatalogBase";
    private String catalogElement = "catalog";
    private String chainClass = "org.apache.commons.chain.impl.ChainBase";
    private String chainElement = "chain";
    private String classAttribute = "className";
    private String commandElement = "command";
    private String defineElement = "define";
    private String nameAttribute = "name";


    // ------------------------------------------------------------- Properties


    /**
     * <p>Return the fully qualified {@link org.apache.commons.chain.Catalog}
     *  implementation class.</p>
     * @return The Catalog's class name.
     */
    public String getCatalogClass() {
        return (this.catalogClass);
    }


    /**
     * <p>Set the fully qualified {@link org.apache.commons.chain.Catalog}
     * implementation class.</p>
     *
     * @param catalogClass The new {@link org.apache.commons.chain.Catalog}
     *  implementation class
     */
    public void setCatalogClass(String catalogClass) {
        this.catalogClass = catalogClass;
    }


    /**
     * <p>Return the element name of a catalog element.</p>
     * @return The element name of a catalog element.
     */
    public String getCatalogElement() {
        return (this.catalogElement);
    }


    /**
     * <p>Set the element name of a catalog element.</p>
     *
     * @param catalogElement The new element name
     */
    public void setCatalogElement(String catalogElement) {
        this.catalogElement = catalogElement;
    }


    /**
     * <p>Return the fully qualified {@link org.apache.commons.chain.Chain}
     * implementation class.</p>
     * @return The Chain's class name.
     */
    public String getChainClass() {
        return (this.chainClass);
    }


    /**
     * <p>Set the fully qualified {@link org.apache.commons.chain.Chain}
     * implementation class.</p>
     *
     * @param chainClass The new {@link org.apache.commons.chain.Chain}
     * implementation class
     */
    public void setChainClass(String chainClass) {
        this.chainClass = chainClass;
    }


    /**
     * <p>Return the element name of a chain element.</p>
     * @return The element name of a catalog element.
     */
    public String getChainElement() {
        return (this.chainElement);
    }


    /**
     * <p>Set the element name of a chain element.</p>
     *
     * @param chainElement The new element name
     */
    public void setChainElement(String chainElement) {
        this.chainElement = chainElement;
    }


    /**
     * <p>Return the attribute name of a class attribute.</p>
     * @return The attribute name of a class attribute.
     */
    public String getClassAttribute() {
        return (this.classAttribute);
    }


    /**
     * <p>Set the attribute name of a class attribute.</p>
     *
     * @param classAttribute The new attribute name
     */
    public void setClassAttribute(String classAttribute) {
        this.classAttribute = classAttribute;
    }


    /**
     * <p>Return the element name of a command element.</p>
     * @return The element name of a command element.
     */
    public String getCommandElement() {
        return (this.commandElement);
    }


    /**
     * <p>Set the element name of a command element.</p>
     *
     * @param commandElement The new element name
     */
    public void setCommandElement(String commandElement) {
        this.commandElement = commandElement;
    }


    /**
     * <p>Return the element name of a define element.</p>
     * @return The element name of a define element.
     */
    public String getDefineElement() {
        return (this.defineElement);
    }


    /**
     * <p>Set the element name of a define element.</p>
     *
     * @param defineElement The new element name
     */
    public void setDefineElement(String defineElement) {
        this.defineElement = defineElement;
    }


    /**
     * <p>Return the attribute name of a name attribute.</p>
     * @return The attribute name of an attribute element.
     */
    public String getNameAttribute() {
        return (this.nameAttribute);
    }


    /**
     * <p>Set the attribute name of a name attribute.</p>
     *
     * @param nameAttribute The new attribute name
     */
    public void setNameAttribute(String nameAttribute) {
        this.nameAttribute = nameAttribute;
    }


    // --------------------------------------------------------- Public Methods


    /**
     * <p>Add the set of Rule instances defined in this RuleSet to the
     * specified <code>Digester</code> instance, associating them with
     * our namespace URI (if any).  This method should only be called
     * by a Digester instance.</p>
     *
     * @param digester Digester instance to which the new Rule instances
     *  should be added.
     */
    public void addRuleInstances(Digester digester) {

        // Add rules for a catalog element
        digester.addRule("*/" + getCatalogElement(),
                         new ConfigCatalogRule(nameAttribute, catalogClass));
        digester.addSetProperties("*/" + getCatalogElement());

        // Add rules for a chain element
        digester.addObjectCreate("*/" + getChainElement(),
                                 getChainClass(),
                                 getClassAttribute());
        digester.addSetProperties("*/" + getChainElement());
        digester.addRule("*/" + getChainElement(),
                         new ConfigRegisterRule(nameAttribute));

        // Add rules for a command element
        digester.addObjectCreate("*/" + getCommandElement(),
                                 null,
                                 getClassAttribute());
        digester.addSetProperties("*/" + getCommandElement());
        digester.addRule("*/" + getCommandElement(),
                         new ConfigRegisterRule(nameAttribute));

        // Add rules for a define element
        digester.addRule("*/" + getDefineElement(),
                         new ConfigDefineRule(getNameAttribute(),
                                              getClassAttribute()));

    }


}
