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

import org.apache.commons.chain2.CommandEdge;
import org.apache.commons.chain2.DAGChain;
import org.apache.commons.chain2.DAGCommand;
import org.apache.commons.chain2.Processing;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>Convenience base class for {@link DAGChain} implementations.</p>
 *
 * @param <K> the type of keys maintained by the context associated with this chain
 * @param <V> the type of mapped values
 * @param <C> Type of the context associated with this chain
 * @author muhossain
 * @since 2020-12-03
 */

public class DAGCommandBase<K, V, C extends Map<K, V>> implements DAGChain<K, V, C> {

    private List<CommandEdge> commandEdgeList = new ArrayList<>();

    private int concurrencyLevel;

    public DAGCommandBase() {
        this.concurrencyLevel = Runtime.getRuntime().availableProcessors();
    }

    /*
     * @param concurrencyLevel determines the number of Thread in the ForkJoinPool
     */
    public DAGCommandBase(int concurrencyLevel) {
        this.concurrencyLevel = concurrencyLevel;
    }

    @Override
    public <DAGCMD extends DAGCommand<K, V, C>> void addCommandEdge(DAGCMD commandLeft, DAGCMD commandRight) {
        commandEdgeList.add(new CommandEdge(commandLeft, commandRight));
    }

    @Override
    public Processing execute(C context) {
        validate();

        DagExecutor dagExecutor = new DagExecutor<>(commandEdgeList, context, concurrencyLevel);
        dagExecutor.execute();

        return Processing.FINISHED;
    }

    @Override
    public String getId() {
        return null;
    }

    /**
     * Performs validations on the input DAG chain of commands
     */
    private void validate() {
        Map<String, Set<String>> dependencyMap = new HashMap<>();

        commandEdgeList.forEach(
                edge -> {
                    String rightId = edge.getRightCommandNode().getCommand().getId();
                    String leftId = edge.getLeftCommandNode().getCommand().getId();

                    dependencyMap.computeIfAbsent(leftId, k -> new HashSet<>());
                    dependencyMap.get(leftId).add(rightId);
                }
        );

        Collection<String> startNodeIds = dependencyMap.keySet().stream().filter(strings -> {
            Optional<Set<String>> foundIds = dependencyMap.values().stream()
                    .filter(values -> values.contains(strings)).findAny();
            return !foundIds.isPresent();
        }).collect(Collectors.toList());

        startNodeIds.forEach(startingId -> checkCycle(startingId, dependencyMap, new HashMap<>()));
    }

    /*
     * Check if the DAG chain contains any cycle. If there is any back-edge in the graph, it can be concluded that
     * the DAG contains a cycle and it's not possible to execute the commands
     */
    private void checkCycle(String currentNode, Map<String, Set<String>> dependencyMap,
                            Map<String, Boolean> presentInCurrentStackMap) {

        if (dependencyMap.get(currentNode) == null) {
            return;
        }

        presentInCurrentStackMap.put(currentNode, true);

        for (String adj : dependencyMap.get(currentNode)) {
            if (Boolean.TRUE.equals(presentInCurrentStackMap.get(adj))) {
                throw new IllegalStateException("Detected cycle with the edge " + currentNode + " -> " + adj);
            }

            checkCycle(adj, dependencyMap, presentInCurrentStackMap);
        }

        presentInCurrentStackMap.put(currentNode, false);
    }
}
