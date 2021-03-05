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

package org.apache.commons.chain2.testutils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.chain2.Catalog;
import org.apache.commons.chain2.Command;
import org.apache.commons.chain2.Context;

/**
 * @version $Id$
 */
public class TestCatalog<K, V, C extends Context<K, V>> implements Catalog<K, V, C> {

    Map<String, Command> commands = new HashMap<String, Command>();

    @Override
    public <CMD extends Command<K, V, C>> void addCommand(String name, CMD command) {
        commands.put(name, command);
    }

    @Override
    public <CMD extends Command<K, V, C>> CMD getCommand(String name) {
        return (CMD) commands.get(name);
    }

    @Override
    public Iterator<String> getNames() {
        return commands.keySet().iterator();
    }

}
