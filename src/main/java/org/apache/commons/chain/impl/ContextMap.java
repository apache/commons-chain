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
package org.apache.commons.chain.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.chain.Context;

/**
 * Convenience base class for {@link Context} implementations.
 *
 * @param <K> the type of keys maintained by the context associated with this context
 * @param <V> the type of mapped values
 *
 * @since 2.0
 */
public class ContextMap<K, V> extends ConcurrentHashMap<K, V> implements Context<K, V> {

    /**
     *
     */
    private static final long serialVersionUID = 6980950395387220980L;

    /**
     * Creates a new, empty Context with a default initial capacity, load factor, and concurrencyLevel.
     */
    public ContextMap() {
        super();
    }

    /**
     * Creates a new, empty Context with the specified initial capacity, and with default load factor and concurrencyLevel
     *
     * @param initialCapacity the initial capacity.
     */
    public ContextMap(int initialCapacity) {
        super(initialCapacity);
    }

    /**
     * Creates a new, empty Context with the specified initial capacity, load factor, and concurrency level.
     *
     * @param initialCapacity the initial capacity.
     * @param loadFactor the load factor threshold, used to control resizing.
     * @param concurrencyLevel the estimated number of concurrently updating threads.
     */
    public ContextMap(int initialCapacity, float loadFactor, int concurrencyLevel) {
        super(initialCapacity, loadFactor, concurrencyLevel);
    }

    /**
     * Creates a new Context with the same mappings as the given map.
     *
     * @param t Map whose key-value pairs are added
     */
    public ContextMap(Map<? extends K, ? extends V> t) {
        super(t);
    }

    /**
     * {@inheritDoc}
     */
    public <T extends V> T retrieve(K key) {
        V valueObject = get(key);
        if (valueObject == null) {
            return null;
        }
        // will throw ClassCastException if type are not assignable anyway
        @SuppressWarnings("unchecked")
        T value = (T) valueObject;
        return value;
    }

}
