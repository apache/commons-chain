package org.apache.commons.chain2.impl;

import org.apache.commons.chain2.CommandEdge;
import org.apache.commons.chain2.CommandNode;
import org.apache.commons.chain2.Context;
import org.apache.commons.chain2.testutils.DAGTestCommand;
import org.apache.commons.chain2.testutils.DAGUseCases;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Test case for the <code>DagExecutor</code> class.</p>
 * Validates the execution of nodes
 *
 * @version $Id$
 */
public class DagExecutorTestCase {
    // ---------------------------------------------------- Instance Variables


    /**
     * The {@link Context} instance on which to execute the chain.
     */
    protected Context<String, Object> context = null;


    // -------------------------------------------------- Overall Test Methods


    /**
     * Set up instance variables required by this test case.
     */
    @Before
    public void setUp() {
        context = new ContextBase();
    }

    /**
     * Tear down instance variables required by this test case.
     */
    @After
    public void tearDown() {
        context = null;
    }


    // ------------------------------------------------ Individual Test Methods

    @Test
    public void testSerialFlow(){
        /*
         * Simple Use case:
         *  A -> B -> C
         */
        List<CommandEdge> commandEdgeList = new ArrayList<>();
        List<DAGTestCommand> commandList = DAGUseCases.createSerialFlow(commandEdgeList);

        DagExecutor dagExecutor = new DagExecutor<>(commandEdgeList, context, 2);
        dagExecutor.execute();

        // Validation
        validateNodeExecution("commandA", commandEdgeList.get(0).getLeftCommandNode());
        validateNodeExecution("commandB", commandEdgeList.get(0).getRightCommandNode());
        validateNodeExecution("commandC", commandEdgeList.get(1).getRightCommandNode());
    }

    @Test
    public void testSimpleParallelFlow1(){
        /*
         * Parallel Use case - 1:
         *  A -> {B  C} -> D
         */
        List<CommandEdge> commandEdgeList = new ArrayList<>();
        List<DAGTestCommand> commandList = DAGUseCases.createSimpleParallelFlow1(commandEdgeList);

        DagExecutor dagExecutor = new DagExecutor<>(commandEdgeList, context, 2);
        dagExecutor.execute();


        // Validation
        validateNodeExecution("commandA", commandEdgeList.get(0).getLeftCommandNode());
        validateNodeExecution("commandB", commandEdgeList.get(0).getRightCommandNode());
        validateNodeExecution("commandC", commandEdgeList.get(1).getRightCommandNode());
        validateNodeExecution("commandD", commandEdgeList.get(2).getRightCommandNode());
    }

    @Test
    public void testSimpleParallelFlow2(){
        /*
         * Parallel Use case - 2:
         *  A -> {B -> C -> D E -> F -> G} -> H
         */
        List<CommandEdge> commandEdgeList = new ArrayList<>();
        List<DAGTestCommand> commandList = DAGUseCases.createSimpleParallelFlow2(commandEdgeList);

        DagExecutor dagExecutor = new DagExecutor<>(commandEdgeList, context, 2);
        dagExecutor.execute();


        // Validation
        validateNodeExecution("commandA", commandEdgeList.get(0).getLeftCommandNode());
        validateNodeExecution("commandB", commandEdgeList.get(0).getRightCommandNode());
        validateNodeExecution("commandC", commandEdgeList.get(1).getRightCommandNode());
        validateNodeExecution("commandD", commandEdgeList.get(2).getRightCommandNode());
        validateNodeExecution("commandE", commandEdgeList.get(3).getRightCommandNode());
        validateNodeExecution("commandF", commandEdgeList.get(4).getRightCommandNode());
        validateNodeExecution("commandG", commandEdgeList.get(5).getRightCommandNode());
        validateNodeExecution("commandH", commandEdgeList.get(commandEdgeList.size() - 2).getRightCommandNode());
    }


    private static void validateNodeExecution(String expectedCommand, CommandNode actualNode){
        Assert.assertEquals(expectedCommand, actualNode.getCommand().getId());
        Assert.assertTrue(actualNode.isCompletedNormally());
    }
}
