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

import static java.lang.String.format;

import java.util.Map;

/**
 * Simple fluent chain EDSL to simplify {@link Chain} instances invocation.
 *
 * @since 2.0
 * @version $Id$
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
     * Defines the target {@link Catalog} has to be invoked.
     *
     * @param <K> Context key type
     * @param <V> Context value type
     * @param <C> Type of the context associated with this command
     * @param <CH> Type of the {@link Chain} to execute
     * @param catalog the catalog instance reference to be setup
     * @return next chain builder
     */
    public static <K, V, C extends Map<K, V>, CA extends Catalog<K, V, C>> NamedCommandSetter<K, V, C> on(CA catalog) {
        return new DefaultNamedCommandSetter<K, V, C>(checkNotNullArgument(catalog, "Null Catalog can not be setup"));
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

    private static final class DefaultNamedCommandSetter<K, V, C extends Map<K, V>>
        implements NamedCommandSetter<K, V, C> {

        private final Catalog<K, V, C> catalog;

        public DefaultNamedCommandSetter(Catalog<K, V, C> catalog) {
            this.catalog = catalog;
        }

        public <CMD extends Command<K, V, C>> NameSetter<K, V, C> addCommand(CMD command) {
            CMD checkedCommand = checkNotNullArgument( command, "Catalog does not accept null Command instances" );
            return new DefaultNameSetter<K, V, C>(catalog, checkedCommand);
        }

    }

    private static final class DefaultNameSetter<K, V, C extends Map<K, V>> implements NameSetter<K, V, C> {

        private final Catalog<K, V, C> catalog;

        private final Command<K, V, C> command;

        public DefaultNameSetter(Catalog<K, V, C> catalog, Command<K, V, C> command) {
            this.catalog = catalog;
            this.command = command;
        }

        public NamedCommandSetter<K, V, C> identifiedBy(String name) {
            catalog.addCommand(checkNotNullArgument(name, "Command <%s> cannot be identified by a null name", command),
                               command);
            return new DefaultNamedCommandSetter<K, V, C>(catalog);
        }

    }

    private static <T> T checkNotNullArgument(T reference, String message, Object...args) {
        if (reference == null) {
            throw new IllegalArgumentException(format(message, args));
        }
        return reference;
    }

}
