/*
 * $Header: /home/jerenkrantz/tmp/commons/commons-convert/cvs/home/cvs/jakarta-commons//chain/src/java/org/apache/commons/chain/config/ConfigRuleSet.java,v 1.3 2003/10/20 05:25:41 martinc Exp $
 * $Revision: 1.3 $
 * $Date: 2003/10/20 05:25:41 $
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


import org.apache.commons.digester.Digester;
import org.apache.commons.digester.RuleSetBase;


/**
 * <p>Digester <code>RuleSet</code> for configuring <em>Chain of
 * Responsibility</em> command chains, and adding them to an appropriate
 * {@link Catalog}.  The following properties may be configured prior to
 * executing the <code>addRuleInstance()</code> method in order to influence
 * the rules that get added, with default values in square brackets:</p>
 * <ul>
 * <li><strong>chainElement</strong> -- Name of the XML element representing
 *     the addition of a {@link Chain}.  A chain element has the same
 *     functionality as a command element, except that it defaults the
 *     implementation class to
 *     <code>org.apache.commons.chain.impl.ChainBase</code>.  [chain]</li>
 * <li><strong>classAttribute</strong> -- Attribute on a chain (optional) or
 *     command (required) element that specifies the fully qualified class
 *     name of the implementation class that should be instantiated.
 *     [className]</li> 
 * <li><strong>commandElement</strong> -- Name of the XML element
 *     representing the addition of a {@link Command}.  An implementation
 *     class name must be provided on the attribute named by the
 *     <code>classAttribute</code> property.  [command]</li>
 * <li><strong>nameAttribute</strong> -- Attribute on an outermost chain or
 *     command element that will be used to register this command with the
 *     associated {@link Catalog} instance on the stack.  [name]</li>
 * <li><strong>namespaceURI</strong> -- The XML namespace URI with which these
 *     rules will be associated, or <code>null</code> for no namespace.
 *     [null]</li>
 * </ul>
 * 
 * @author Craig R. McClanahan
 * @version $Revision: 1.3 $ $Date: 2003/10/20 05:25:41 $
 */

public class ConfigRuleSet extends RuleSetBase {


    // ----------------------------------------------------- Instance Variables


    private String chainElement = "chain";
    private String classAttribute = "className";
    private String commandElement = "command";
    private String nameAttribute = "name";


    // ------------------------------------------------------------- Properties


    /**
     * <p>Return the element name of a chain element.</p>
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
     * <p>Return the attribute name of a name attribute.</p>
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

        // Add rules for a chain element
        digester.addObjectCreate("*/" + getChainElement(),
                                 "org.apache.commons.chain.impl.ChainBase",
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

    }


}
