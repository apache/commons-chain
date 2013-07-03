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

import org.apache.commons.chain2.Processing;

/**
 * Builder that allows continue adding a command in the target chain and execute it.
 *
 * @param <K> Context key type
 * @param <V> Context value type
 * @param <C> Type of the context associated with this chain executor
 * @version $Id$
 * @since 2.0
 */
public interface ChainExecutor<K, V, C extends Map<K, V>> extends CommandSetter<K, V, C, ChainExecutor<K, V, C>> {

    /**
     * Execute the processing represented by the target chain.
     *
     * @param context the context processed by the target chain
     * @return {@link Processing#FINISHED} if the processing of this context
     *  has been completed. Returns {@link Processing#CONTINUE} if the processing
     *  of this context should be delegated to a subsequent command in an
     *  enclosing chain.
     *  
     * @see Chain#execute(Map)
     */
    Processing execute(C context);

}
