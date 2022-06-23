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


import org.apache.commons.chain2.Command;
import org.apache.commons.chain2.Context;
import org.apache.commons.chain2.Processing;


/**
 * <p>Implementation of {@link Command} that simply logs its identifier
 * and returns.</p>
 *
 */

public class NonDelegatingCommand implements Command<String, Object, Context<String, Object>> {


    // ------------------------------------------------------------ Constructor


    public NonDelegatingCommand() {
        this("");
    }


    // Construct an instance that will log the specified identifier
    public NonDelegatingCommand(String id) {
        this.id = id;
    }


    // ----------------------------------------------------- Instance Variables


    // The identifier to log for this Command instance
    protected String id;

    String getId() {
        return (this.id);
    }

    public void setId(String id) {
    this.id = id;
    }


    // -------------------------------------------------------- Command Methods


    // Execution method for this Command
    public Processing execute(Context<String, Object> context) {

        if (context == null) {
            throw new IllegalArgumentException();
        }
        log(context, id);
        return Processing.FINISHED;

    }



    // ------------------------------------------------------ Protected Methods


    /**
     * <p>Log the specified <code>id</code> into a StringBuffer attribute
     * named "log" in the specified <code>context</code>, creating it if
     * necessary.</p>
     *
     * @param context The {@link Context} into which we log the identifiers
     * @param id The identifier to be logged
     */
    protected void log(Context<String, Object> context, String id) {
        StringBuilder sb = (StringBuilder) context.get("log");
        if (sb == null) {
            sb = new StringBuilder();
            context.put("log", sb);
        }
        if (sb.length() > 0) {
            sb.append('/');
        }
        sb.append(id);
    }


}
