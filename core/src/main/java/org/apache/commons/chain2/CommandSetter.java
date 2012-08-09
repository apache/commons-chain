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
 * Generic builder that allows adding commands to the target {@link Chain} has to be executed.
 *
 * @param <K> Context key type
 * @param <V> Context value type
 * @param <C> Type of the context associated with this command setter
 * @param <R> Type of the next chain builder
 * @since 2.0
 * @version $Id$
 */
public interface CommandSetter<K, V, C extends Map<K, V>, R> {

    /**
     * Add the given command to the target {@link Chain} has to be executed.
     *
     * @param <CMD> Type of the command has to be added
     * @param command the command has to be added in the target chain
     * @return next chain builder
     * @see Chain#addCommand(Command)
     */
    <CMD extends Command<K, V, C>> R addCommand(CMD command);

    /**
     * Add the given filter to the target {@link Chain} has to be executed.
     *
     * @param <F> Type of the filter has to be added
     * @param command the command has to be added in the target chain
     * @return next chain builder
     * @see Chain#addCommand(Command)
     */
    <F extends Filter<K, V, C>> R addFilter(F filter);

}
