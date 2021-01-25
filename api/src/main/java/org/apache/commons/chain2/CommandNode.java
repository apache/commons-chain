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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Is a wrapper of DAGCommand which facilitate the execution of a DAGCommand in parallel. It extends RecursiveAction
 * and overrides the compute method. On completion of the execution it reduces the in-degree of connected nodes by 1.
 * If the in-degree of a connected node becomes 0, it indicates that all the dependencies are met for that command's
 * execution. It filters all the node by in-degree 0 and invokes them in parallel.
 *
 * @author muhossain
 * @since 2020-11-30
 */
public class CommandNode<C> extends RecursiveAction {

    private C context;
    private DAGCommand command;
    private List<CommandNode> dependentNodeList = new ArrayList<>();
    private AtomicInteger inDegree = new AtomicInteger(0);

    public CommandNode(DAGCommand command) {
        this.command = command;
    }

    public DAGCommand getCommand() {
        return command;
    }

    public void setCommand(DAGCommand command) {
        this.command = command;
    }

    public void addDependentNode(CommandNode commandNode) {
        dependentNodeList.add(commandNode);
        commandNode.inDegree.incrementAndGet();
    }

    public C getContext() {
        return context;
    }

    public void setContext(C context) {
        this.context = context;
    }

    @Override
    protected void compute() {
        command.execute((Map) context);

        onComplete();
    }

    /*
     * Reduce in-degree of adjacent nodes by 1 and invokes all the nodes for which in-degree become 0. In-degree 0
     * indicates that all the pre-requisite nodes already completed their execution.
     */
    private void onComplete() {
        dependentNodeList.forEach(dt -> dt.inDegree.decrementAndGet());

        List<CommandNode> eligibleTasks = dependentNodeList.stream().filter(dt -> dt.inDegree.intValue() == 0)
                .collect(Collectors.toList());

        invokeAll(eligibleTasks);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CommandNode<?> that = (CommandNode<?>) o;

        return this.command.getId().equals(that.getCommand().getId());
    }

    @Override
    public int hashCode() {
        return command.getId().hashCode();
    }

    @Override
    public String toString() {
        return "CommandNode{" +
                "commandId=" + command.getId() +
                '}';
    }
}
