/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.chain.web;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

// Test case for org.apache.commons.chain.web.ChainResources

@RunWith(Parameterized.class)
public class ChainResourcesTestCase {


    // ----------------------------------------------------- Test data source

    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
            { "a,b,c",            new String[] {"a", "b", "c"} },
            { "a , b , c ",       new String[] {"a", "b", "c"} },
            { "a,\tb,\tc ",       new String[] {"a", "b", "c"} },
            { "\na,\nb,\nc\n",    new String[] {"a", "b", "c"} },
            { "a,,b,c ",          new String[] {"a", "b", "c"} },
            { ",a,b,,c,,",        new String[] {"a", "b", "c"} },
            { null,               new String[] {} },
            { "",                 new String[] {} },
            { ",",                new String[] {} },
            { ",,",               new String[] {} }
        });
    }

    // ----------------------------------------------------- Public constructor

    private final String input;

    private final String[] expected;

    public ChainResourcesTestCase(String input, String[] expected)
    {
        this.input = input;
        this.expected = expected;
    }

    // ------------------------------------------------ Individual Test Methods


    @Test
    public void testGetPaths() throws Exception {
        String[] actual = ChainResources.getResourcePaths(input);

        assertNotNull(actual);
        assertArrayEquals(expected, actual);
    }

}
