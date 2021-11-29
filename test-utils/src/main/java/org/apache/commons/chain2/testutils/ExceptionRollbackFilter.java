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

import org.apache.commons.chain2.Context;
import org.apache.commons.chain2.Filter;
import org.apache.commons.chain2.Processing;

public class ExceptionRollbackFilter implements Filter<String, Object, Context<String, Object>> {

    protected String id = null;

    // Construct an instance that will log the specified identifier
    public ExceptionRollbackFilter(String id) {
        this.id = id;
    }

    // Postprocess command for this Filter
    public boolean postprocess(Context<String, Object> context, Exception exception) {
        return (false);
    }

    // Undo operation for this Filter
    public void undo(Context<String, Object> context, Exception exception) {
        log(context, id);
    }

    @Override
    public Processing execute(Context<String, Object> context) {
        throw new ArithmeticException(this.id);
    }

    protected void log(Context<String, Object> context, String id) {
        context.put("log", id);
    }
}
