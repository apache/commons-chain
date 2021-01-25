package org.apache.commons.chain2.testutils;

import org.apache.commons.chain2.CommandEdge;
import org.apache.commons.chain2.Context;
import org.apache.commons.chain2.DAGChain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DAGUseCases {

    public static List<DAGTestCommand> createSerialFlow(DAGChain<String, Object, Context<String, Object>> chain){
        return createSerialFlow(chain, null);
    }

    public static List<DAGTestCommand> createSerialFlow(List<CommandEdge> commandEdgeList){
        return createSerialFlow(null, commandEdgeList);
    }

    public static List<DAGTestCommand> createSerialFlow(DAGChain<String, Object, Context<String, Object>> chain,
                                                        List<CommandEdge> commandEdgeList){
        /*
         * Simple Use case:
         *  A -> B -> C
         */
        int nodeCount = 3;

        List<DAGTestCommand> commandList = new ArrayList<>();
        for(int i = 0; i < nodeCount; i++){
            // Command ID added with alphabets to ease readability with documentation
            DAGTestCommand dagCommand = new DAGTestCommand("command"+(char)(i+65));
            commandList.add(dagCommand);
        }

        addEdge(0, 1, commandList, chain, commandEdgeList); // A->B
        addEdge(1, 2, commandList, chain, commandEdgeList); // B->C

        return commandList;
    }

    public static List<DAGTestCommand> createSimpleParallelFlow1(DAGChain<String, Object, Context<String, Object>> chain){
        return createSimpleParallelFlow1(chain, null);
    }
    public static List<DAGTestCommand> createSimpleParallelFlow1(List<CommandEdge> commandEdgeList){
        return createSimpleParallelFlow1(null, commandEdgeList);
    }
    public static List<DAGTestCommand> createSimpleParallelFlow1(DAGChain<String, Object, Context<String, Object>> chain,
                                                                 List<CommandEdge> commandEdgeList){
        /*
         * Parallel Use case - 1:
         *  A -> {B  C} -> D
         */
        int nodeCount = 4;

        List<DAGTestCommand> commandList = new ArrayList<>();
        for(int i = 0; i < nodeCount; i++){
            // Command ID added with alphabets to ease readability with documentation
            DAGTestCommand dagCommand = new DAGTestCommand("command"+(char)(i+65));
            commandList.add(dagCommand);
        }

        addEdge(0, 1, commandList, chain, commandEdgeList);     // A->B
        addEdge(0, 2, commandList, chain, commandEdgeList);     // A->C
        addEdge(1, 3, commandList, chain, commandEdgeList);     // B->D
        addEdge(2, 3, commandList, chain, commandEdgeList);     // C->D

        return commandList;
    }

    public static List<DAGTestCommand> createSimpleParallelFlow2(DAGChain<String, Object, Context<String, Object>> chain){
        return createSimpleParallelFlow2(chain, null);
    }
    public static List<DAGTestCommand> createSimpleParallelFlow2(List<CommandEdge> commandEdgeList){
        return createSimpleParallelFlow2(null, commandEdgeList);
    }
    public static List<DAGTestCommand> createSimpleParallelFlow2(DAGChain<String, Object, Context<String, Object>> chain,
                                                                 List<CommandEdge> commandEdgeList){
        /*
         * Parallel Use case - 2:
         *  A -> {B -> C -> D E -> F -> G} -> H
         */

        int nodeCount = 8;

        List<DAGTestCommand> commandList = new ArrayList<>();
        for(int i = 0; i < nodeCount; i++){
            DAGTestCommand dagCommand = new DAGTestCommand("command"+(char)(i+65));
            commandList.add(dagCommand);
        }

        addEdge(0, 1, commandList, chain, commandEdgeList);   // A->B
        addEdge(0, 2, commandList, chain, commandEdgeList);   // A->C
        addEdge(0, 3, commandList, chain, commandEdgeList);   // A->D
        addEdge(0, 4, commandList, chain, commandEdgeList);   // A->E
        addEdge(0, 5, commandList, chain, commandEdgeList);   // A->F
        addEdge(0, 6, commandList, chain, commandEdgeList);   // A->G

        addEdge(1, 2, commandList, chain, commandEdgeList);   // B->C
        addEdge(2, 3, commandList, chain, commandEdgeList);   // C->D

        addEdge(4, 5, commandList, chain, commandEdgeList);   // E->F
        addEdge(5, 6, commandList, chain, commandEdgeList);   // F->G

        addEdge(3, 7, commandList, chain, commandEdgeList);   // D->H
        addEdge(6, 7, commandList, chain, commandEdgeList);   // G->H

        return commandList;
    }

    public static List<DAGTestCommand> createCyclicFlow(DAGChain<String, Object, Context<String, Object>> chain){
        /*
         * Cycle Use case:
         *  A -> B -> C -> B -> D
         */
        int nodeCount = 4;

        List<DAGTestCommand> commandList = new ArrayList<>();
        for(int i = 0; i < nodeCount; i++){
            // Command ID added with alphabets to ease readability with documentation
            DAGTestCommand dagCommand = new DAGTestCommand("command"+(char)(i+65));
            commandList.add(dagCommand);
        }

        chain.addCommandEdge(commandList.get(0), commandList.get(1));   // A->B
        chain.addCommandEdge(commandList.get(1), commandList.get(2));   // B->C
        chain.addCommandEdge(commandList.get(2), commandList.get(1));   // C->B
        chain.addCommandEdge(commandList.get(1), commandList.get(3));   // B->D

        return commandList;
    }




    private static void addEdge(int left, int right, List<DAGTestCommand> commandList,
                                DAGChain<String, Object, Context<String, Object>> chain, List<CommandEdge> commandEdgeList){

        if(Objects.nonNull(chain)){
            chain.addCommandEdge(commandList.get(left), commandList.get(right));
        }

        // Since getter method of chain.commandEdgeList is not exposed, created this check for DAGExecutor test
        if(Objects.nonNull(commandEdgeList)){
            commandEdgeList.add(new CommandEdge(commandList.get(left), commandList.get(right)));
        }
    }



}
