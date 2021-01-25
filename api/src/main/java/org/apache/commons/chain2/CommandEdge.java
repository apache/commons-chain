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

/**
 * An edge contains two command - left and right. In an edge A -> B, A is the left command and B is the right command.
 * List of edges are passed to the DAG executor which constructs the DAG and execute the commands in parallel.
 *
 * @author muhossain
 * @since 2020-11-30
 */
public class CommandEdge {

    private CommandNode leftCommandNode;
    private CommandNode rightCommandNode;

    public CommandEdge(DAGCommand leftCommandNode, DAGCommand rightCommandNode) {
        this.leftCommandNode = new CommandNode(leftCommandNode);
        this.rightCommandNode = new CommandNode(rightCommandNode);
    }

    public CommandEdge(CommandNode leftCommandNode, CommandNode rightCommandNode) {
        this.leftCommandNode = leftCommandNode;
        this.rightCommandNode = rightCommandNode;
    }

    public CommandNode getLeftCommandNode() {
        return leftCommandNode;
    }

    public void setLeftCommandNode(CommandNode leftCommandNode) {
        this.leftCommandNode = leftCommandNode;
    }

    public CommandNode getRightCommandNode() {
        return rightCommandNode;
    }

    public void setRightCommandNode(CommandNode rightCommandNode) {
        this.rightCommandNode = rightCommandNode;
    }

    @Override
    public String toString() {
        return "CommandEdge{" +
                "leftCommandNode=" + leftCommandNode +
                ", rightCommandNode=" + rightCommandNode +
                '}';
    }
}
