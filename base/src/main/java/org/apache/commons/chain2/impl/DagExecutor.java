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
import org.apache.commons.chain2.CommandNode;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ForkJoinPool;

/**
 * DagExecutor handles execution of DAG of commands. It creates a ForkJoinPool and invokes the starting nodes first.
 * Upon completion of each command node, it reduces the in-degree of connected nodes by 1. If the in-degree becomes 0
 * it indicates that the node doesn't have any other dependencies and it is ready to be invoked.
 *
 * The execution algorithm of DAG is a classic topological sort problem. The algorithm that is implemented here is
 * commonly known as Kahn's algorithm.
 *
 * @author muhossain
 * @since 2020-12-03
 */
public class DagExecutor<C> {

    private Log log = LogFactory.getLog(DagExecutor.class);

    private List<CommandNode> startNodes = new ArrayList<>();
    private int concurrencyLevel;

    private C context;

    DagExecutor(List<CommandEdge> edgeList, C context, int concurrencyLevel) {
        this.context = context;
        this.concurrencyLevel = concurrencyLevel;

        init(edgeList);
    }

    public void execute() {
        ForkJoinPool forkJoinPool = new ForkJoinPool(concurrencyLevel);
        startNodes.forEach(forkJoinPool::invoke);
    }

    private void init(List<CommandEdge> commandEdgeList) {
        log.debug("Initializing DAG Executor");

        Map<CommandNode, Integer> inDegreeMap = new HashMap<>();

        // A node might be present in multiple edges. Use only the first occurance one for the dependency graph
        Map<CommandNode, CommandNode> seen = new HashMap<>();

        for (CommandEdge edge : commandEdgeList) {

            CommandNode leftCommandNode = (seen.containsKey(edge.getLeftCommandNode())) ?
                    seen.get(edge.getLeftCommandNode()) : edge.getLeftCommandNode();

            CommandNode rightCommandNode = (seen.containsKey(edge.getRightCommandNode())) ?
                    seen.get(edge.getRightCommandNode()) : edge.getRightCommandNode();

            seen.put(leftCommandNode, leftCommandNode);
            seen.put(rightCommandNode, rightCommandNode);

            rightCommandNode.setContext(context);
            leftCommandNode.setContext(context);

            leftCommandNode.addDependentNode(rightCommandNode);

            inDegreeMap.putIfAbsent(leftCommandNode, 0);
            inDegreeMap.putIfAbsent(rightCommandNode, 0);

            inDegreeMap.put(rightCommandNode, inDegreeMap.get(rightCommandNode) + 1);
        }


        inDegreeMap.forEach((commandNode, inDegree) -> {
            if (inDegree == 0) {
                startNodes.add(commandNode);
            }
        });

        log.debug("Identified start node: " + startNodes);
    }
}
