/*
 * $Header: /home/jerenkrantz/tmp/commons/commons-convert/cvs/home/cvs/jakarta-commons//chain/src/java/org/apache/commons/chain/impl/Attic/ContextBaseAttributes.java,v 1.3 2003/09/27 18:15:12 craigmcc Exp $
 * $Revision: 1.3 $
 * $Date: 2003/09/27 18:15:12 $
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


import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


/**
 * <p><code>Map</code> implementation for the attributes of the associated
 * {@link ContextBase} instance, which will delegate attribute getter and
 * setter calls to the JavaBeans property getter and setter methods on the
 * associated {@link ContextBase} when there is such a property.</p>
 *
 * @author Craig R. McClanahan
 * @version $Revision: 1.3 $ $Date: 2003/09/27 18:15:12 $
 */

class ContextBaseAttributes implements Map {


    // ----------------------------------------------------------- Constructors


    /**
     * <p>Construct a new {@link ContextBaseAttributes} instance associated
     * with the specified {@link ContextBase} instance.</p>
     *
     * @param context {@link ContextBase} with which we are associated
     *
     * @exception NullPointerException if <code>context</code>
     *  is <code>null</code>
     */
    public ContextBaseAttributes(ContextBase context) {

        if (context == null) {
            throw new NullPointerException();
        }
        this.context = context;
        try {
            descriptors =
                Introspector.getBeanInfo(context.getClass()).
                                         getPropertyDescriptors();
        } catch (IntrospectionException e) {
            descriptors = new PropertyDescriptor[0]; // Should never happen
        }

    }


    // ----------------------------------------------------- Instance Variables


    /**
     * <p>The underlying <code>Map</code> that contains our attributes.</p>
     */
    private Map attributes = new HashMap();


    /**
     * <p>The {@link ContextBase} instance (normally a subclass) with which
     * we are associated.</p>
     */
    private ContextBase context = null;


    /**
     * <p>The <code>PropertyDescriptor</code>s for all properties of the
     * {@link ContextBase} instance (normally a subclass) with which we
     * are associated.</p>
     */
    private PropertyDescriptor descriptors[] = null;


    // ------------------------------------------------------------ Map Methods


    public void clear() {
        attributes.clear(); // FIXME - include properties?
    }


    public boolean containsKey(Object key) {
        return (attributes.containsKey(key)); // FIXME - include properties?
    }


    public boolean containsValue(Object value) {
        return (attributes.containsValue(value)); // FIXME - include properties?
    }


    // FIXME - do we need to implement equals()?


    public Set entrySet() {
        return (attributes.entrySet()); // FIXME - include properties?
    }


    public Object get(Object key) {

        if (key != null) {
            PropertyDescriptor descriptor = getDescriptor(key.toString());
            if (descriptor != null) {
                Method method = descriptor.getReadMethod();
                if (method == null) {
                    throw new UnsupportedOperationException
                        ("Property '" + key.toString() + "' cannot be read");
                } else {
                    try {
                        return (method.invoke(context, new Object[0]));
                    } catch (Exception e) {
                        throw new UnsupportedOperationException
                            ("Exception reading property '" +
                             key.toString() + "':  " + e.getMessage());
                    }
                }
            }
        }
        return (attributes.get(key));

    }


    // FIXME - do we need to implement hashCode()?


    public boolean isEmpty() {
        return (attributes.isEmpty()); // FIME - include properties?
    }


    public Set keySet() {
        return (attributes.keySet()); // FIXME - include properties?
    }


    public Object put(Object key, Object value) {

        if (key != null) {
            PropertyDescriptor descriptor = getDescriptor(key.toString());
            if (descriptor != null) {
                Method rmethod = descriptor.getReadMethod();
                Method wmethod = descriptor.getWriteMethod();
                if (wmethod == null) {
                    throw new UnsupportedOperationException
                        ("Property '" + key.toString() +
                         "' cannot be written");
                } else {
                    try {
                        Object oldValue = null;
                        if (rmethod != null) {
                            oldValue = rmethod.invoke(context, new Object[0]);
                        }
                        wmethod.invoke(context,
                                       new Object[] { value });
                        return (oldValue);
                    } catch (Exception e) {
                        throw new UnsupportedOperationException
                            ("Exception reading property '" +
                             key.toString() + "':  " + e.getMessage());
                    }
                }
            }
        }
        return (attributes.put(key, value));

    }


    public void putAll(Map map) {
        Iterator keys = map.keySet().iterator();
        while (keys.hasNext()) {
            Object key = keys.next();
            put(key, map.get(key));
        }
    }


    public Object remove(Object key) {

        if (key != null) {
            PropertyDescriptor descriptor = getDescriptor(key.toString());
            if (descriptor != null) {
                throw new UnsupportedOperationException
                    ("Property '" + key.toString() +
                     "' cannot be removed");
            }
        }
        return (attributes.remove(key));

    }


    public int size() {
        return (attributes.size()); // FIXME - include properties?
    }


    public Collection values() {
        return (attributes.values()); // FIXME - include properties?
    }


    // --------------------------------------------------------- Public Methods


    /**
     * <p>Returns the value to which the internal map maps the specified key,
     * bypassing any JavaBean style getters. This method allows a
     * JavaBean getter to be defined for any additional processing but still
     * use the internal map for storage, if desired. Since this class is not
     * public, this method can only be accessed by another member of hte
     * <code>org.apache.commons.chain.impl</code> package.</p>
     *
     * <p><em>WARNING: This is an EXPERIMENTAL feature and may be removed at
     * any time.</em></p>
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
     * @see #get(Object)
     */
    public Object getField(Object key) {

        return attributes.get(key);

    }

    /**
     * <p>Associates the key/value pair within the internal map,
     * bypassing any JavaBean style getters. This method allows a
     * JavaBean getter to be defined for any additional processing but still
     * use the internal map for storage, if desired. Since this class is not
     * public, this method can only be accessed by another member of hte
     * <code>org.apache.commons.chain.impl</code> package.</p>
     *
     * <p><em>WARNING: This is an EXPERIMENTAL feature and may be removed at
     * any time.</em></p>
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
     * @see #put(Object, Object)
     */
    public Object putField(Object key, Object value) {

        return attributes.put(key,value);

    }


    // -------------------------------------------------------- Private Methods


    /**
     * <p>Return a <code>PropertyDescriptor</code> associated with the property
     * of the specified name on our associated {@link ContextBase} instance,
     * if there is one; otherwise, return <code>null</code>.</p>
     *
     * @param name Name of the property to return a descriptor for
     */
    private PropertyDescriptor getDescriptor(String name) {

        for (int i = 0; i < descriptors.length; i++) {
            if (name.equals(descriptors[i].getName())) {
                return (descriptors[i]);
            }
        }
        return (null);

    }


}
