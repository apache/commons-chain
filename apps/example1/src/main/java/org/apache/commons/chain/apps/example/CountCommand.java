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
package org.apache.commons.chain.apps.example;


import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;
import org.apache.commons.chain.web.WebContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * <p>Bar Command</p>
 *
 * @version $Revision$ $Date$
 */

public class CountCommand implements Command {


    private Log log = LogFactory.getLog(CountCommand.class);

    private int count;

    private String attribute = "count";


    /**
     * Return the request attribute name to store the count under.
     *
     * @return The name of the request attribute
     */
    public String getAttribute() {
        return attribute;
    }


    /**
     * Set the request attribute name to store the count under.
     *
     * @param attribute The name of the request attribute
     */
    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }


    /**
     * <p>Execute the command.</p>
     *
     * @param context The {@link Context} we are operating on
     * @return <code>false</code> so that processng will continue
     * @throws Exception If an error occurs during execution.
     */
    public boolean execute(Context context) throws Exception {

        count++;
        log.info("Executing: " + attribute + "=" + count);

        WebContext webContext = (WebContext)context;
        webContext.getSessionScope().put(attribute, new Integer(count));

        return false;

    }


}
