/*
 * $Header: /home/jerenkrantz/tmp/commons/commons-convert/cvs/home/cvs/jakarta-commons//chain/src/java/org/apache/commons/chain/impl/Attic/ContextBase2.java,v 1.1 2003/09/17 15:17:58 husted Exp $
 * $Revision: 1.1 $
 * $Date: 2003/09/17 15:17:58 $
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


package org.apache.commons.chain.impl;


/**
 * <p><em>WARNING: This is an EXPERIMENTAL class and may be removed at
 * any time.</em></p>
 *
 * <p>Convenience extension of {@link ContextBase} class.</p>
 *
 * <p>Provides support for using the internal map to store the  fields for any
 * JavaBean style properties defined by a sublcass</p>
 *
 * @author Ted Husted
 * @version $Revision: 1.1 $ $Date: 2003/09/17 15:17:58 $
 */

public class ContextBase2 extends ContextBase {


    /**
     * <p>Return our collection of name-value pairs as a ContextBaseAttributes
     * to allow direct access to the entries, bypassing any JavaBean-style
     * getters or setters defined by a subclass.</p>
     *
     * @return The state information for this context as a ContextBaseAttributes
     */
    protected ContextBaseAttributes getFields() {

        return (ContextBaseAttributes) attributes;

    }


    /**
     * Returns the value mapped to the specified key,  bypassing any JavaBean
     * style getters. This method allows a JavaBean getter to be defined for any
     * additional processing but still use the internal map for storage, if desired.
     * Other classes should associate values through
     * <code>getAttributes().put(Object,Object)</code>.
     *
     * @param key key whose associated value is to be returned.
     * @return the value to which this map maps the specified key, or
     *	       <tt>null</tt> if the map contains no mapping for this key.
     *
     * @throws ClassCastException if the key is of an inappropriate type for
     * 		  this map (optional).
     * @throws NullPointerException key is <tt>null</tt> and this map does not
     *		  not permit <tt>null</tt> keys (optional).
     *
     * @see #getAttributes()
     */
    public Object getField(Object key) {

        return getFields().getField(key);

    }


    /**
     * Associates the value to the specified key, bypassing any JavaBean style
     * getters. This method allows a JavaBean getter to be defined by a subclass
     * to allow additional processing but still use the internal map for storage,
     * if desired. Other classes should associate values through
     * <code>getAttributes().put(Object,Object)</code>.
     *
     * @param key key with which the specified value is to be associated.
     * @param value value to be associated with the specified key.
     * @return previous value associated with specified key, or <tt>null</tt>
     *	       if there was no mapping for key.  A <tt>null</tt> return can
     *	       also indicate that the map previously associated <tt>null</tt>
     *	       with the specified key, if the implementation supports
     *	       <tt>null</tt> values.
     *
     * @throws UnsupportedOperationException if the <tt>put</tt> operation is
     *	          not supported by this map.
     * @throws ClassCastException if the class of the specified key or value
     * 	          prevents it from being stored in this map.
     * @throws IllegalArgumentException if some aspect of this key or value
     *	          prevents it from being stored in this map.
     * @throws NullPointerException this map does not permit <tt>null</tt>
     *            keys or values, and the specified key or value is
     *            <tt>null</tt>.
     *
     * @see #getAttributes()
     */
    public Object putField(Object key, Object value) {

        return getFields().putField(key,value);

    }

}
