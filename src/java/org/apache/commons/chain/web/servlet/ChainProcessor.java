/*
 * $Header: /home/jerenkrantz/tmp/commons/commons-convert/cvs/home/cvs/jakarta-commons//chain/src/java/org/apache/commons/chain/web/servlet/ChainProcessor.java,v 1.1 2003/10/10 03:55:51 craigmcc Exp $
 * $Revision: 1.1 $
 * $Date: 2003/10/10 03:55:51 $
 *
 * ====================================================================
 *
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 1999-2003 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution, if
 *    any, must include the following acknowlegement:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowlegement may appear in the software itself,
 *    if and wherever such third-party acknowlegements normally appear.
 *
 * 4. The names "The Jakarta Project", "Commons", and "Apache Software
 *    Foundation" must not be used to endorse or promote products derived
 *    from this software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache"
 *    nor may "Apache" appear in their names without prior written
 *    permission of the Apache Group.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */


package org.apache.commons.chain.web.servlet;


import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.chain.Catalog;
import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;
import org.apache.commons.chain.web.ChainServlet;


/**
 * <p>Custom subclass of {@link ChainServlet} that also dispatches incoming
 * requests to a configurable {@link Command} loaded from the configured
 * {@link Catalog}.</p>
 *
 * <p>In addition to the <em>servlet</em> init parameters supported by
 * {@link ChainServlet}, this class supports the following additional
 * parameters:</p>
 * <ul>
 * <li><strong>org.apache.commons.chain.COMMAND</strong> - Name of the
 *     {@link Command} (looked up in our configured {@link Catalog} used
 *     to process all incoming servlet requests.  If not specified,
 *     defaults to <code>command</code>.</li>
 * </ul>
 *
 * <p>Also, the <code>org.apache.commons.chain.CONFIG_ATTR</code>
 * init parameter is also used to identify the {@link Context} attribute under
 * which our configured {@link Catalog} will be made available to
 * {@link Command}s processing our requests, in addition to its definition
 * of the <code>ServletContext</code> attribute key under which the
 * {@link Catalog} is available.</p>
 */

public class ChainProcessor extends ChainServlet {


    // ------------------------------------------------------ Manifest Constants


    /**
     * <p>The name of the servlet init parameter containing the name of the
     * {@link Command} (loaded from our configured {@link Catalog} to use
     * for processing each incoming request.</p>
     */
    public static final String COMMAND =
        "org.apache.commons.chain.COMMAND";


    /**
     * <p>The default command name.</p>
     */
    private static final String COMMAND_DEFAULT = "command";


    // ------------------------------------------------------ Instance Variables


    /**
     * <p>The name of the context attribute under which our {@link Catalog}
     * is stored.  This value is also used as the name of the
     * context attribute under which the catalog is exposed to commands.</p>
     */
    private String attribute = null;


    /**
     * <p>The name of the {@link Command} to be executed for each incoming
     * request.</p>
     */
    private String command = null;


    // --------------------------------------------------------- Servlet Methods


    /**
     * <p>Cache the name of the command we should execute for each request.</p>
     *
     * @exception ServletException if an initialization error occurs
     */
    public void init() throws ServletException {

        super.init();
        attribute = getServletConfig().getInitParameter(CONFIG_ATTR);
        if (attribute == null) {
            attribute = CONFIG_ATTR_DEFAULT;
        }
        command = getServletConfig().getInitParameter(COMMAND);
        if (command == null) {
            command = COMMAND_DEFAULT;
        }

    }


    /**
     * <p>Configure a {@link ServletWebContext} for the current request, and
     * pass it to the <code>execute()</code> method of the specified
     * {@link Command}, loaded from our configured {@link Catalog}.</p>
     *
     * @param request The request we are processing
     * @param response The response we are creating
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet exception occurs
     */
    public void service(HttpServletRequest request,
                        HttpServletResponse response)
        throws IOException, ServletException {

        ServletWebContext context =
            new ServletWebContext(getServletContext(), request, response);
        Catalog catalog =
            (Catalog) getServletContext().getAttribute(this.attribute);
        context.put(this.attribute, catalog);
        Command command = catalog.getCommand(this.command);
        try {
            command.execute(context);
        } catch (Exception e) {
            throw new ServletException(e);
        }

    }


}
