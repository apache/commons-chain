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
package org.apache.commons.chain2.impl;

import org.apache.commons.chain2.Context;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.AbstractCollection;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * <p>Convenience base class for {@link Context} implementations.</p>
 *
 * <p>In addition to the minimal functionality required by the {@link Context}
 * interface, this class implements the recommended support for
 * <em>Attribute-Property Transparency</em>. This is implemented by
 * analyzing the available JavaBeans properties of this class (or its
 * subclass), exposes them as key-value pairs in the <code>Map</code>,
 * with the key being the name of the property itself.</p>
 *
 * <p><strong>IMPLEMENTATION NOTE</strong> - Because <code>empty</code> is a
 * read-only property defined by the <code>Map</code> interface, it may not
 * be utilized as an attribute key or property name.</p>
 *
 * @version $Id$
 */
public class ContextBase extends ContextMap<String, Object> {

    // ------------------------------------------------------------ Constructors

    /**
     *
     */
    private static final long serialVersionUID = 20120724L;

    /**
     * Default, no argument constructor.
     */
    public ContextBase() {
        super();
        initialize();
    }

    /**
     * <p>Initialize the contents of this {@link Context} by copying the
     * values from the specified <code>Map</code>.  Any keys in <code>map</code>
     * that correspond to local properties will cause the setter method for
     * that property to be called.</p>
     *
     * @param map Map whose key-value pairs are added
     *
     * @exception IllegalArgumentException if an exception is thrown
     *  writing a local property value
     * @exception UnsupportedOperationException if a local property does not
     *  have a write method.
     */
    public ContextBase(Map<? extends String, ? extends Object> map) {
        super(map);
        initialize();
        putAll(map);
    }

    // ------------------------------------------------------ Instance Variables

    // NOTE - PropertyDescriptor instances are not Serializable, so the
    // following variables must be declared as transient.  When a ContextBase
    // instance is deserialized, the no-arguments constructor is called,
    // and the initialize() method called there will repoopulate them.
    // Therefore, no special restoration activity is required.

    /**
     * <p>The <code>PropertyDescriptor</code>s for all JavaBeans properties
     * of this {@link Context} implementation class, keyed by property name.
     * This collection is allocated only if there are any JavaBeans
     * properties.</p>
     */
    private transient Map<String, PropertyDescriptor> descriptors = null;

    /**
     * <p>The same <code>PropertyDescriptor</code>s as an array.</p>
     */
    private transient PropertyDescriptor[] pd = null;

    /**
     * <p>Distinguished singleton value that is stored in the map for each
     * key that is actually a property.  This value is used to ensure that
     * <code>equals()</code> comparisons will always fail.</p>
     */
    private static final Object singleton;

    static {
        singleton = new Serializable() {
                private static final long serialVersionUID = 20120724L;

                @Override
                public boolean equals(Object object) {
                    return false;
                }

                @Override
                public int hashCode() {
                    return super.hashCode();
                }
            };
    }

    /**
     * <p>Zero-length array of parameter values for calling property getters.
     * </p>
     */
    private static Object[] zeroParams = new Object[0];

    // ------------------------------------------------------------- Map Methods

    /**
     * <p>Override the default <code>Map</code> behavior to clear all keys and
     * values except those corresponding to JavaBeans properties.</p>
     */
    @Override
    public void clear() {
        if (descriptors == null) {
            super.clear();
        } else {
            Iterator<String> keys = keySet().iterator();
            while (keys.hasNext()) {
                String key = keys.next();
                if (!descriptors.containsKey(key)) {
                    keys.remove();
                }
            }
        }
    }

    /**
     * <p>Override the default <code>Map</code> behavior to return
     * <code>true</code> if the specified value is present in either the
     * underlying <code>Map</code> or one of the local property values.</p>
     *
     * @param value the value look for in the context.
     * @return <code>true</code> if found in this context otherwise
     *  <code>false</code>.
     * @exception IllegalArgumentException if a property getter
     *  throws an exception
     */
    @Override
    public boolean containsValue(Object value) {
        // Case 1 -- no local properties
        if (descriptors == null) {
            return super.containsValue(value);

        // Case 2 -- value found in the underlying Map
        } else if (super.containsValue(value)) {
            return true;
        }

        // Case 3 -- check the values of our readable properties
        for (PropertyDescriptor aPd : pd) {
            if (aPd.getReadMethod() != null) {
                Object prop = readProperty(aPd);
                if (value == null) {
                    if (prop == null) {
                        return true;
                    }
                } else if (value.equals(prop)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * <p>Override the default <code>Map</code> behavior to return a
     * <code>Set</code> that meets the specified default behavior except
     * for attempts to remove the key for a property of the {@link Context}
     * implementation class, which will throw
     * <code>UnsupportedOperationException</code>.</p>
     *
     * @return Set of entries in the Context.
     */
    @Override
    public Set<Entry<String, Object>> entrySet() {
        return new EntrySetImpl();
    }

    /**
     * <p>Override the default <code>Map</code> behavior to return the value
     * of a local property if the specified key matches a local property name.
     * </p>
     *
     * <p><strong>IMPLEMENTATION NOTE</strong> - If the specified
     * <code>key</code> identifies a write-only property, <code>null</code>
     * will arbitrarily be returned, in order to avoid difficulties implementing
     * the contracts of the <code>Map</code> interface.</p>
     *
     * @param key Key of the value to be returned
     * @return The value for the specified key.
     *
     * @exception IllegalArgumentException if an exception is thrown
     *  reading this local property value
     * @exception UnsupportedOperationException if this local property does not
     *  have a read method.
     */
    @Override
    public Object get(Object key) {
        // Case 1 -- no local properties
        if (descriptors == null) {
            return super.get(key);
        }

        // Case 2 -- this is a local property
        if (key != null) {
            PropertyDescriptor descriptor = descriptors.get(key);
            if (descriptor != null) {
                if (descriptor.getReadMethod() != null) {
                    return readProperty(descriptor);
                } else {
                    return null;
                }
            }
        }

        // Case 3 -- retrieve value from our underlying Map
        return super.get(key);
    }

    /**
     * <p>Override the default <code>Map</code> behavior to return
     * <code>true</code> if the underlying <code>Map</code> only contains
     * key-value pairs for local properties (if any).</p>
     *
     * @return <code>true</code> if this Context is empty, otherwise
     *  <code>false</code>.
     */
    @Override
    public boolean isEmpty() {
        // Case 1 -- no local properties
        if (descriptors == null) {
            return super.isEmpty();
        }

        // Case 2 -- compare key count to property count
        return super.size() <= descriptors.size();
    }

    /**
     * <p>Override the default <code>Map</code> behavior to return a
     * <code>Set</code> that meets the specified default behavior except
     * for attempts to remove the key for a property of the {@link Context}
     * implementation class, which will throw
     * <code>UnsupportedOperationException</code>.</p>
     *
     * @return The set of keys for objects in this Context.
     */
    @Override
    public Set<String> keySet() {
        return super.keySet();
    }

    /**
     * <p>Override the default <code>Map</code> behavior to set the value
     * of a local property if the specified key matches a local property name.
     * </p>
     *
     * @param key Key of the value to be stored or replaced
     * @param value New value to be stored
     * @return The value added to the Context.
     *
     * @exception IllegalArgumentException if an exception is thrown
     *  reading or writing this local property value
     * @exception UnsupportedOperationException if this local property does not
     *  have both a read method and a write method
     */
    @Override
    public Object put(String key, Object value) {
        /*
         * ConcurrentHashMap doesn't accept null values, see
         * http://download.oracle.com/javase/1.5.0/docs/api/java/util/concurrent/ConcurrentHashMap.html#put(String, Object)
         */
        if (value == null) {
            if (containsKey(key)) {
                remove(key);
            }

            return null;
        }

        // Case 1 -- no local properties
        if (descriptors == null) {
            return super.put(key, value);
        }

        // Case 2 -- this is a local property
        if (key != null) {
            PropertyDescriptor descriptor = descriptors.get(key);
            if (descriptor != null) {
                Object previous = null;
                if (descriptor.getReadMethod() != null) {
                    previous = readProperty(descriptor);
                }
                writeProperty(descriptor, value);
                return previous;
            }
        }

        // Case 3 -- store or replace value in our underlying map
        return super.put(key, value);
    }

    /**
     * <p>Override the default <code>Map</code> behavior to call the
     * <code>put()</code> method individually for each key-value pair
     * in the specified <code>Map</code>.</p>
     *
     * @param map <code>Map</code> containing key-value pairs to store
     *  (or replace)
     *
     * @exception IllegalArgumentException if an exception is thrown
     *  reading or writing a local property value
     * @exception UnsupportedOperationException if a local property does not
     *  have both a read method and a write method
     */
    @Override
    public void putAll(Map<? extends String, ? extends Object> map) {
        for (Entry<? extends String, ? extends Object> pair : map.entrySet()) {
            put(pair.getKey(), pair.getValue());
        }
    }

    /**
     * <p>Override the default <code>Map</code> behavior to throw
     * <code>UnsupportedOperationException</code> on any attempt to
     * remove a key that is the name of a local property.</p>
     *
     * @param key Key to be removed
     * @return The value removed from the Context.
     *
     * @exception UnsupportedOperationException if the specified
     *  <code>key</code> matches the name of a local property
     */
    @Override
    public Object remove(Object key) {
        // Case 1 -- no local properties
        if (descriptors == null) {
            return super.remove(key);
        }

        // Case 2 -- this is a local property
        if (key != null) {
            PropertyDescriptor descriptor = descriptors.get(key);
            if (descriptor != null) {
                throw new UnsupportedOperationException
                    ("Local property '" + key + "' cannot be removed");
            }
        }

        // Case 3 -- remove from underlying Map
        return super.remove(key);
    }

    /**
     * <p>Override the default <code>Map</code> behavior to return a
     * <code>Collection</code> that meets the specified default behavior except
     * for attempts to remove the key for a property of the {@link Context}
     * implementation class, which will throw
     * <code>UnsupportedOperationException</code>.</p>
     *
     * @return The collection of values in this Context.
     */
    @Override
    public Collection<Object> values() {
        return new ValuesImpl();
    }

    // --------------------------------------------------------- Private Methods

    /**
     * <p>Return an <code>Iterator</code> over the set of <code>Map.Entry</code>
     * objects representing our key-value pairs.</p>
     */
    private Iterator<Entry<String, Object>> entriesIterator() {
        return new EntrySetIterator();
    }

    /**
     * <p>Return a <code>Map.Entry</code> for the specified key value, if it
     * is present; otherwise, return <code>null</code>.</p>
     *
     * @param key Attribute key or property name
     */
    private Map.Entry<String, Object> entry(String key) {
        if (containsKey(key)) {
            return new MapEntryImpl(key, get(key));
        }
        return null;
    }

    /**
     * <p>Customize the contents of our underlying <code>Map</code> so that
     * it contains keys corresponding to all of the JavaBeans properties of
     * the {@link Context} implementation class.</p>
     *
     *
     * @exception IllegalArgumentException if an exception is thrown
     *  writing this local property value
     * @exception UnsupportedOperationException if this local property does not
     *  have a write method.
     */
    private void initialize() {
        // Retrieve the set of property descriptors for this Context class
        try {
            pd = Introspector.getBeanInfo
                (getClass()).getPropertyDescriptors();
        } catch (IntrospectionException e) {
            pd = new PropertyDescriptor[0]; // Should never happen
        }

        // Initialize the underlying Map contents
        for (PropertyDescriptor propertyDescriptor : pd) {
            String name = propertyDescriptor.getName();

            // Add descriptor (ignoring getClass() and isEmpty())
            if (!("class".equals(name) || "empty".equals(name))) {
                if (descriptors == null) {
                    descriptors = new HashMap<String, PropertyDescriptor>(pd.length - 2);
                }
                descriptors.put(name, propertyDescriptor);
                super.put(name, singleton);
            }
        }
    }

    /**
     * <p>Get and return the value for the specified property.</p>
     *
     * @param descriptor <code>PropertyDescriptor</code> for the
     *  specified property
     *
     * @exception IllegalArgumentException if an exception is thrown
     *  reading this local property value
     * @exception UnsupportedOperationException if this local property does not
     *  have a read method.
     */
    private Object readProperty(PropertyDescriptor descriptor) {
        try {
            Method method = descriptor.getReadMethod();
            if (method == null) {
                throw new UnsupportedOperationException
                    ("Property '" + descriptor.getName()
                     + "' is not readable");
            }
            return method.invoke(this, zeroParams);
        } catch (Exception e) {
            throw new UnsupportedOperationException
                ("Exception reading property '" + descriptor.getName()
                 + "': " + e.getMessage());
        }
    }

    /**
     * <p>Remove the specified key-value pair, if it exists, and return
     * <code>true</code>.  If this pair does not exist, return
     * <code>false</code>.</p>
     *
     * @param entry Key-value pair to be removed
     *
     * @exception UnsupportedOperationException if the specified key
     *  identifies a property instead of an attribute
     */
    private boolean remove(Map.Entry<String, Object> entry) {
        Map.Entry<String, Object> actual = entry(entry.getKey());
        if (actual == null) {
            return false;
        } else if (!entry.equals(actual)) {
            return false;
        } else {
            remove(entry.getKey());
            return true;
        }
    }

    /**
     * <p>Return an <code>Iterator</code> over the set of values in this
     * <code>Map</code>.</p>
     */
    private Iterator<Object> valuesIterator() {
        return new ValuesIterator();
    }

    /**
     * <p>Set the value for the specified property.</p>
     *
     * @param descriptor <code>PropertyDescriptor</code> for the
     *  specified property
     * @param value The new value for this property (must be of the
     *  correct type)
     *
     * @exception IllegalArgumentException if an exception is thrown
     *  writing this local property value
     * @exception UnsupportedOperationException if this local property does not
     *  have a write method.
     */
    private void writeProperty(PropertyDescriptor descriptor, Object value) {
        try {
            Method method = descriptor.getWriteMethod();
            if (method == null) {
                throw new UnsupportedOperationException
                    ("Property '" + descriptor.getName()
                     + "' is not writeable");
            }
            method.invoke(this, new Object[] {value});
        } catch (Exception e) {
            throw new UnsupportedOperationException
                ("Exception writing property '" + descriptor.getName()
                 + "': " + e.getMessage());
        }
    }

    // --------------------------------------------------------- Private Classes

    /**
     * <p>Private implementation of <code>Set</code> that implements the
     * semantics required for the value returned by <code>entrySet()</code>.</p>
     */
    private class EntrySetImpl extends AbstractSet<Entry<String, Object>> {

        @Override
        public void clear() {
            ContextBase.this.clear();
        }

        @Override
        public boolean contains(Object obj) {
            if (!(obj instanceof Map.Entry)) {
                return false;
            }

            /* The contains method is expecting the search type to be of the
             * same type stored. This contract is enforced as a precondition.
             * So we can safely suppress type safety warnings below. */
            @SuppressWarnings("unchecked")
            Map.Entry<String, Object> entry = (Map.Entry<String, Object>) obj;
            Entry<String, Object> actual = ContextBase.this.entry(entry.getKey());
            if (actual != null) {
                return actual.equals(entry);
            }
            return false;
        }

        @Override
        public boolean isEmpty() {
            return ContextBase.this.isEmpty();
        }

        @Override
        public Iterator<Entry<String, Object>> iterator() {
            return ContextBase.this.entriesIterator();
        }

        @Override
        public boolean remove(Object obj) {
            if (obj instanceof Map.Entry) {

                /* The remove method is expecting an input of the the same
                 * type as the entry set. This precondition is checked above,
                 * so we can safely suppress the unchecked warnings. */
                @SuppressWarnings("unchecked")
                Map.Entry<String, Object> entry = (Map.Entry<String, Object>) obj;
                return ContextBase.this.remove(entry);
            }
            return false;
        }

        @Override
        public int size() {
            return ContextBase.this.size();
        }

    }

    /**
     * <p>Private implementation of <code>Iterator</code> for the
     * <code>Set</code> returned by <code>entrySet()</code>.</p>
     */
    private class EntrySetIterator implements Iterator<Entry<String, Object>> {

        private Map.Entry<String, Object> entry = null;

        private Iterator<String> keys = ContextBase.this.keySet().iterator();

        public boolean hasNext() {
            return keys.hasNext();
        }

        public Entry<String, Object> next() {
            entry = ContextBase.this.entry(keys.next());
            return entry;
        }

        public void remove() {
            ContextBase.this.remove(entry);
        }

    }

    /**
     * <p>Private implementation of <code>Map.Entry</code> for each item in
     * <code>EntrySetImpl</code>.</p>
     */
    private class MapEntryImpl implements Map.Entry<String, Object> {

        MapEntryImpl(String key, Object value) {
            this.key = key;
            this.value = value;
        }

        private String key;

        private Object value;

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            } else if (!(obj instanceof Map.Entry)) {
                return false;
            }
            Map.Entry<?, ?> entry = (Map.Entry<?, ?>) obj;
            if (key == null) {
                return entry.getKey() == null;
            }
            if (key.equals(entry.getKey())) {
                if (value == null) {
                    return entry.getValue() == null;
                }
                return value.equals(entry.getValue());
            }
            return false;
        }

        public String getKey() {
            return this.key;
        }

        public Object getValue() {
            return this.value;
        }

        @Override
        public int hashCode() {
            return (key == null ? 0 : key.hashCode())
                   ^ (value == null ? 0 : value.hashCode());
        }

        public Object setValue(Object value) {
            Object previous = this.value;
            ContextBase.this.put(this.key, value);
            this.value = value;
            return previous;
        }

        @Override
        public String toString() {
            return getKey() + "=" + getValue();
        }
    }


    /**
     * <p>Private implementation of <code>Collection</code> that implements the
     * semantics required for the value returned by <code>values()</code>.</p>
     */
    private class ValuesImpl extends AbstractCollection<Object> {

        @Override
        public void clear() {
            ContextBase.this.clear();
        }

        @Override
        public boolean contains(Object obj) {
            if (!(obj instanceof Map.Entry)) {
                return false;
            }
            Map.Entry<?, ?> entry = (Map.Entry<?, ?>) obj;
            return ContextBase.this.containsValue(entry.getValue());
        }

        @Override
        public boolean isEmpty() {
            return ContextBase.this.isEmpty();
        }

        @Override
        public Iterator<Object> iterator() {
            return ContextBase.this.valuesIterator();
        }

        @Override
        public boolean remove(Object obj) {
            if (obj instanceof Map.Entry) {

                /* We are expecting the passed entry to be of a type
                 * Entry<String, Object>. This is checked in the precondition
                 * above, so we can safely suppress unchecked warnings. */
                @SuppressWarnings("unchecked")
                Map.Entry<String, Object> entry = (Map.Entry<String, Object>) obj;
                return ContextBase.this.remove(entry);
            }
            return false;
        }

        @Override
        public int size() {
            return ContextBase.this.size();
        }

    }


    /**
     * <p>Private implementation of <code>Iterator</code> for the
     * <code>Collection</code> returned by <code>values()</code>.</p>
     */
    private class ValuesIterator implements Iterator<Object> {

        private Map.Entry<String, Object> entry = null;

        private Iterator<String> keys = ContextBase.this.keySet().iterator();

        public boolean hasNext() {
            return keys.hasNext();
        }

        public Object next() {
            entry = ContextBase.this.entry(keys.next());
            return entry.getValue();
        }

        public void remove() {
            ContextBase.this.remove(entry);
        }

    }

}
