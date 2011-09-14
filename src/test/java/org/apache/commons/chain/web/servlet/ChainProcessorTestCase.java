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
package org.apache.commons.chain.web.servlet;

import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.servlet.ServletConfig;

import org.junit.Test;

/**
 *  Test case for {@link ChainProcessor}.
 */
public class ChainProcessorTestCase {

    /**
     * Test serialization.
     */
    @Test
    public void testSerialize() {

        // Initialize a ChainProcessor
        ChainProcessor processor = initServlet(new ChainProcessor(), new MockServletConfig("test"));

        // Serialize/Deserialize & test value
        processor = (ChainProcessor)serializeDeserialize(processor, "First Test");
    }

    /**
     * Initialize the ChainProcessor.
     *
     * @param processor The chain processor instance
     * @param config The servlet config to initialize with
     * @return The chain processor
     */
    private ChainProcessor initServlet(ChainProcessor processor, ServletConfig config) {
        try {
            processor.init(config);
        } catch (Throwable t) {
            t.printStackTrace();
            fail("ChainProcessor init() threw " + t);
        }
        return processor;
    }

    /**
     * Do serialization and deserialization.
     */
    private Object serializeDeserialize(Object target, String text) {

        // Serialize the test object
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(target);
            oos.flush();
            oos.close();
        } catch (Exception e) {
            fail(text + ": Exception during serialization: " + e);
        }

        // Deserialize the test object
        Object result = null;
        try {
            ByteArrayInputStream bais =
                new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            result = ois.readObject();
            bais.close();
        } catch (Exception e) {
            fail(text + ": Exception during deserialization: " + e);
        }
        return result;

    }
}
