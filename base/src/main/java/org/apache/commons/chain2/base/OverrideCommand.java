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

package org.apache.commons.chain2.base;

import java.util.Map;

import org.apache.commons.chain2.Command;
import org.apache.commons.chain2.Processing;

/**
 * <p>Override any context attribute stored under the <code>key</code> with <code>value</code>.</p>
 *
 * @param <K> the type of keys maintained by the context associated with this catalog
 * @param <V> the type of mapped values
 * @param <C> Type of the context associated with this command
 *
 */
public class OverrideCommand<K, V, C extends Map<K, V>> implements Command<K, V, C> {

    // -------------------------------------------------------------- Properties

    private K key;
    private V value;

    /**
     * <p>Return the context attribute key for the attribute to override.</p>
     * @return The context attribute key.
     */
    public K getKey() {
        return key;
    }

    /**
     * <p>Set the context attribute key for the attribute to override.</p>
     *
     * @param key The new key
     */
    public void setKey(K key) {
        this.key = key;
    }

    /**
     * <p>Return the value that should override context attribute with key <code>key</code>.</p>
     * @return The value.
     */
    public V getValue() {
        return value;
    }

    /**
     * <p>Set the value that should override context attribute with key <code>key</code>.</p>
     *
     * @param value The new value
     */
    public void setValue(V value) {
        this.value = value;
    }

    // ---------------------------------------------------------- Filter Methods

    /**
     * <p>Override the attribute specified by <code>key</code> with <code>value</code>.</p>
     *
     * @param context {@link org.apache.commons.chain2.Context} in which we are operating
     *
     * @return {@link Processing#CONTINUE} so that {@link Processing} will continue.
     * @throws org.apache.commons.chain2.ChainException if and error occurs.
     */
    public Processing execute(C context) {
        if (context.containsKey(getKey())) {
            context.put(getKey(), getValue());
        }
        return Processing.CONTINUE;
    }

}
