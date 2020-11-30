package org.apache.commons.chain2.impl;

import org.apache.commons.chain2.Context;
import org.apache.commons.chain2.DAGChain;
import org.apache.commons.chain2.Processing;
import org.apache.commons.chain2.testutils.DAGTestCommand;
import org.apache.commons.chain2.testutils.DAGUseCases;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>Test case for the <code>DAGCommandBase</code> class.</p>
 * Validates the existence of cyclic flow, various input flows & end status of the execution
 *
 * @version $Id$
 */
public class DAGCommandBaseTestCase {

    // ---------------------------------------------------- Instance Variables


    /**
     * The {@link DAGChain} instance under test.
     */
    protected DAGChain<String, Object, Context<String, Object>> chain = null;


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
        chain = new DAGCommandBase<>();
        context = new ContextBase();
    }

    /**
     * Tear down instance variables required by this test case.
     */
    @After
    public void tearDown() {
        chain = null;
        context = null;
    }


    // ------------------------------------------------ Individual Test Methods

    @Test
    public void testSerialFlow(){
        /*
         * Simple Use case:
         *  A -> B -> C
         */
        List<DAGTestCommand> commandList = DAGUseCases.createSerialFlow(chain);

        Processing result = chain.execute(context);

        // Validation
        Assert.assertEquals(Processing.FINISHED, result);
        validateContextLog(context, commandList);
    }

    @Test
    public void testSimpleParallelFlow1(){
        /*
         * Parallel Use case - 1:
         *  A -> {B  C} -> D
         */
        List<DAGTestCommand> commandList = DAGUseCases.createSimpleParallelFlow1(chain);

        Processing result = chain.execute(context);

        // Validation
        Assert.assertEquals(Processing.FINISHED, result);
        validateContextLog(context, commandList);
    }

    @Test
    public void testSimpleParallelFlow2(){
        /*
         * Parallel Use case - 2:
         *  A -> {B -> C -> D E -> F -> G} -> H
         */
        List<DAGTestCommand> commandList = DAGUseCases.createSimpleParallelFlow2(chain);

        Processing result = chain.execute(context);

        // Validation
        Assert.assertEquals(Processing.FINISHED, result);
        validateContextLog(context, commandList);
    }

    @Test(expected = IllegalStateException.class)
    public void testCyclicFlowThrowIllegalStateExpection(){
        /*
         * Cycle Use case:
         *  A -> B -> C -> B -> D
         */
        List<DAGTestCommand> commandList = DAGUseCases.createCyclicFlow(chain);

        chain.execute(context);
    }

    private void validateContextLog(Context<String, Object> context, List<DAGTestCommand> expectedCommands){
        Assert.assertTrue(context.containsKey("log"));
        ConcurrentHashMap<String, Integer> log = (ConcurrentHashMap<String, Integer>) context.get("log");

        Assert.assertEquals(expectedCommands.size(), log.keySet().size());

        for (DAGTestCommand expectedCmd: expectedCommands) {
            String expectedCmdId = expectedCmd.getId();

            Assert.assertTrue(Objects.nonNull(log.get(expectedCmdId)) && log.get(expectedCmdId) == 1);
        }

    }
}
