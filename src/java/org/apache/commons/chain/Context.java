/*
 * $Header: /home/jerenkrantz/tmp/commons/commons-convert/cvs/home/cvs/jakarta-commons//chain/src/java/org/apache/commons/chain/Context.java,v 1.2 2003/09/17 15:16:08 husted Exp $
 * $Revision: 1.2 $
 * $Date: 2003/09/17 15:16:08 $
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


package org.apache.commons.chain;


import java.io.Serializable;
import java.util.Map;


/**
 * <p>A {@link Context} represents the state information that is
 * accessed an  manipulated by the execution of a {@link Command} or a
 * {@link Chain}.  Specialized implementations of {@link Context} will
 * typically add JavaBeans properties that contain typesafe accessors
 * to information that is relevant to a particular use case for this
 * context, and/or add operations that affect the state information
 * that is saved in the context.</p>
 *
 * <p>It is strongly recommended, but not required, that JavaBeans
 * properties added to a particular {@link Context} implementation exhibit
 * <em>Attribute-Property Transparency</em>.  In other words,
 * a value stored via a call to <code>setFoo(value)</code> should be visible
 * by calling <code>getAttributes().get("foo")</code>, and a value stored
 * via a call to <code>getAttributes().put("foo", value)</code> should be
 * visible by calling <code>getFoo()</code>.  If your {@link Context}
 * implementation class exhibits this featue, it becomes easier to reuse the
 * implementation in multiple environments, without the need to cast to a
 * particular implementation class in order to access the property getter
 * and setter methods.</p>
 *
 * <p>Attribute-Property Transparency can be easily achieved by at least two
 * different approaches:</p>
 * <ul>
 * <li>Implementing the JavaBeans property getter and setter methods in the
 *     usual way (storing the saved value in an instance variable), but making
 *     the <code>Map</code> returned by <code>getAttributes()</code> smart
 *     enough to automatically call the getters and setters via reflection
 *     if an attribute name matches the name of a supported JavaBeans property.
 *     The provided {@link org.apache.commons.chain.impl.ContextBase} base
 *     class is implemented in this fashion.</li>
 * <li>Implement the <code>Map</code> returned by <code>getAttributes()</code>
 *     using a standard Java collection class (such as <code>HashMap</code>,
 *     and implement the property getter and setter methods to retrieve and
 *     store the values directly into the map.  For example, for a String
 *     property named <code>foo</code>, you could implement the property getter
 *     and setter methods as follows:
 * <pre>
 *     public String getFoo() {
 *       return ((String) getAttributes().get("foo"));
 *     }
 *     public void setFoo(String foo) {
 *       getAttributes().put("foo", foo);
 *     }
 * </pre></li>
 * </ul>
 *
 * <p>To protect applications from evolution of this interface, specialized
 * implementations of {@link Context} should generally be created by extending
 * the provided base class ({@link org.apache.commons.chain.impl.ContextBase})
 * rather than directly implementing this interface.</p>
 *
 * <p>Applications should <strong>NOT</strong> assume that
 * {@link Context} implementations, or the values stored in its
 * attributes, may be accessed from multiple threads
 * simultaneously unless this is explicitly documented for a particular
 * implementation.</p>
 *
 * @author Craig R. McClanahan
 * @version $Revision: 1.2 $ $Date: 2003/09/17 15:16:08 $
 */

public interface Context {


    /**
     * <p>Return an implementation of <code>java.util.Map</code> that
     * applications can use to manipulate a general purpose collection
     * of key-value pairs that maintain the state information associated
     * with the processing of the transaction that is represented by
     * this {@link Context} instance.</p>
     *
     * @return The state information for this context as a Map
     */
    public Map getAttributes();


}
