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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.chain.web.servlet.ServletWebContext;
import javax.servlet.RequestDispatcher;

/**
 * <p>Foo Command</p>
 *
 * @version $Revision$ $Date$
 */

public class ForwardCommand implements Command {


    private Log log = LogFactory.getLog(ForwardCommand.class);

    private String forward;


    /**
     * Return the uri to forward to.
     *
     * @return The uri to forward to
     */
    public String getForward() {
        return forward;
    }


    /**
     * Set the uri to forward to.
     *
     * @param forward The uri to forward to
     */
    public void setForward(String forward) {
        this.forward = forward;
    }

    /**
     * <p>Execute the command.</p>
     *
     * @param context The {@link Context} we are operating on
     * @return <code>false</code> so that processng will continue
     * @throws Exception If an error occurs during execution.
     */
    public boolean execute(Context context) throws Exception {

        String uri = getForward(context);
        if (uri != null) {
            if (log.isDebugEnabled()) {
                log.debug("Forwarding to " + uri);
            }
            ServletWebContext swcontext = (ServletWebContext)context;
            RequestDispatcher rd = swcontext.getContext().getRequestDispatcher(uri);
            rd.forward(swcontext.getRequest(), swcontext.getResponse());
            return true;
        } else {
            if (log.isDebugEnabled()) {
                log.debug("No forward found");
            }
            return false;
        }
    }

    /**
     * Return the uri to forward to.
     *
     * @param context The {@link Context} we are operating on
     * @return The uri to forward to
     */
    protected String getForward(Context context) {
        String uri = (String)context.get("forward");
        if (uri == null) {
            uri = getForward();
        }
        return uri;
    }

}
