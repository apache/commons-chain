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
 * <p>Copy a specified literal value, or a context attribute stored under
 * the <code>fromKey</code> (if any), to the <code>toKey</code>.</p>
 *
 * @param <K> the type of keys maintained by the context associated with this command
 * @param <V> the type of mapped values
 * @param <C> Type of the context associated with this command
 *
 */
public class CopyCommand<K, V, C extends Map<K, V>> implements Command<K, V, C> {

    // -------------------------------------------------------------- Properties

    private K fromKey = null;

    /**
     * <p>Return the context attribute key for the source attribute.</p>
     * @return The source attribute key.
     */
    public K getFromKey() {
        return this.fromKey;
    }

    /**
     * <p>Set the context attribute key for the source attribute.</p>
     *
     * @param fromKey The new key
     */
    public void setFromKey(K fromKey) {
        this.fromKey = fromKey;
    }

    private K toKey = null;

    /**
     * <p>Return the context attribute key for the destination attribute.</p>
     * @return The destination attribute key.
     */
    public K getToKey() {
        return this.toKey;
    }

    /**
     * <p>Set the context attribute key for the destination attribute.</p>
     *
     * @param toKey The new key
     */
    public void setToKey(K toKey) {
        this.toKey = toKey;
    }

    // ---------------------------------------------------------- Filter Methods

    /**
     * <p>Copy a specified literal value, or a context attribute stored under
     * the <code>fromKey</code> (if any), to the <code>toKey</code>.</p>
     *
     * @param context {@link org.apache.commons.chain2.Context Context} in which we are operating.
     *
     * @return {@link Processing#CONTINUE} so that processing will continue.
     * @throws org.apache.commons.chain2.ChainException in the if an error occurs during execution.
     */
    public Processing execute(C context) {
        if (containsKeys(context)) {
            V value = context.get(getFromKey());
            context.put(getToKey(), value);
        }
        return Processing.CONTINUE;
    }

    private boolean containsKeys(C context) {
        return context.containsKey(getFromKey()) && context.containsKey(getToKey());
    }

}
