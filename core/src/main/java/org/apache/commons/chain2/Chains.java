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
package org.apache.commons.chain2;

import java.util.Map;

/**
 * Simple fluent chain EDSL to simplify {@link Chain} instances invocation.
 *
 * @since 2.0
 */
public final class Chains {

    /**
     * Defines the target chain has to be invoked.
     *
     * @param <K> Context key type
     * @param <V> Context value type
     * @param <C> Type of the context associated with this command
     * @param <CH> Type of the {@link Chain} to execute
     * @param chain the chain instance reference to execute
     * @return next chain builder
     */
    public static <K, V, C extends Map<K, V>, CH extends Chain<K, V, C>> ToExecutorCommandSetter<K, V, C> on(CH chain) {
        return new DefaultCommandSetter<K, V, C>(checkNotNullArgument(chain, "Null Chain can not be executed"));
    }

    /**
     * Private constructor, this class cannot be instantiated directly.
     */
    private Chains() {
        // do nothing
    }

    private static class DefaultCommandSetter<K, V, C extends Map<K, V>> implements ToExecutorCommandSetter<K, V, C> {

        private final Chain<K, V, C> chain;

        public DefaultCommandSetter(Chain<K, V, C> chain) {
            this.chain = chain;
        }

        public <CMD extends Command<K, V, C>> ChainExecutor<K, V, C> addCommand(CMD command) {
            chain.addCommand(checkNotNullArgument(command, "Chain does not accept null Command instances"));
            return new DefaultChainExecutor<K, V, C>(chain);
        }

    }

    private static final class DefaultChainExecutor<K, V, C extends Map<K, V>> implements ChainExecutor<K, V, C> {

        private final Chain<K, V, C> chain;

        public DefaultChainExecutor(Chain<K, V, C> chain) {
            this.chain = chain;
        }

        public <CMD extends Command<K, V, C>> ChainExecutor<K, V, C> addCommand(CMD command) {
            chain.addCommand(checkNotNullArgument(command, "Chain does not accept null Command instances"));
            return this;
        }

        public boolean execute(C context) {
            return chain.execute(checkNotNullArgument(context, "Chain cannot be applied to a null context."));
        }

    }

    private static <T> T checkNotNullArgument(T reference, String message) {
        if (reference == null) {
            throw new IllegalArgumentException(message);
        }
        return reference;
    }

}
