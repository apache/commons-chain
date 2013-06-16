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


/**
 * <p>Implementation of {@link Command} that logs its identifier and
 * and delegates to the rest of the chain.</p>
 *
 * @version $Revision$ $Date$
 */

public class DelegatingCommand extends NonDelegatingCommand {


    // ------------------------------------------------------------ Constructor


    public DelegatingCommand() {
    this("");
    }


    // Construct an instance that will log the specified identifier
    public DelegatingCommand(String id) {
        super(id);
    }


    // -------------------------------------------------------- Command Methods


    // Execution method for this Command
    @Override
    public boolean execute(Context<String, Object> context) {

        super.execute(context);
        return (false);

    }


}
