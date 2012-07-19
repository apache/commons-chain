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
package org.apache.commons.chain2.apps.example;


import org.apache.commons.chain2.Command;
import org.apache.commons.chain2.Context;
import org.apache.commons.chain2.web.servlet.ServletWebContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import javax.servlet.ServletException;
import javax.servlet.RequestDispatcher;
import java.io.IOException;
import java.lang.RuntimeException;

/**
 * <p>Foo Command</p>
 *
 * @version $Id$
 */

public class ForwardCommand implements Command<String, Object, ServletWebContext> {


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
     */
    public boolean execute(ServletWebContext context) {
        try {
            String uri = getForward(context);
            if (uri != null) {
                if (log.isDebugEnabled()) {
                    log.debug("Forwarding to " + uri);
                }
                RequestDispatcher rd = context.getContext().getRequestDispatcher(uri);
                rd.forward(context.getRequest(), context.getResponse());
                return true;
            } else {
                if (log.isDebugEnabled()) {
                    log.debug("No forward found");
                }
                return false;
            }
        } catch (ServletException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Return the uri to forward to.
     *
     * @param context The {@link Context} we are operating on
     * @return The uri to forward to
     */
    protected String getForward(ServletWebContext context) {
        String uri = (String)context.get("forward");
        if (uri == null) {
            uri = getForward();
        }
        return uri;
    }

}
