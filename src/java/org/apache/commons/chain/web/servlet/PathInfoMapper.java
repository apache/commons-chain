/*
 * $Header: /home/jerenkrantz/tmp/commons/commons-convert/cvs/home/cvs/jakarta-commons//chain/src/java/org/apache/commons/chain/web/servlet/PathInfoMapper.java,v 1.2 2003/10/12 09:10:41 rdonkin Exp $
 * $Revision: 1.2 $
 * $Date: 2003/10/12 09:10:41 $
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
 *    any, must include the following acknowledgement:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowledgement may appear in the software itself,
 *    if and wherever such third-party acknowledgements normally appear.
 *
 * 4. The names "The Jakarta Project", "Commons", and "Apache Software
 *    Foundation" must not be used to endorse or promote products derived
 *    from this software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache"
 *    nor may "Apache" appear in their names without prior written
 *    permission of the Apache Software Foundation.
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


import javax.servlet.http.HttpServletRequest;
import org.apache.commons.chain.Catalog;
import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;


/**
 * <p>{@link Command} that uses the "path info" component of the request URI
 * to select a {@link Command} from the appropriate {@link Catalog}, and
 * execute it.  To use this command, you would typically map an instance
 * of {@link ChainProcessor} to a wildcard pattern like "/execute/*" and
 * then arrange that this is the default command to be executed.  In such
 * an environment, a request for the context-relative URI "/execute/foo"
 * would cause the "/foo" command to be loaded and executed.</p>
 *
 * @author Craig R. McClanahan
 */

public class PathInfoMapper implements Command {


    // ------------------------------------------------------ Instance Variables


    private String catalogKey = ChainProcessor.CONFIG_ATTR_DEFAULT;


    // -------------------------------------------------------------- Properties


    /**
     * <p>Return the context key under which our {@link Catalog} has been
     * stored.</p>
     */
    public String getCatalogKey() {

        return (this.catalogKey);

    }


    /**
     * <p>Set the context key under which our {@link Catalog} has been
     * stored.</p>
     *
     * @param catalogKey The new catalog key
     */
    public void setCatalogKey(String catalogKey) {

        this.catalogKey = catalogKey;

    }


    // --------------------------------------------------------- Command Methods


    /**
     * <p>Look up the extra path information for this request, and use it to
     * select an appropriate {@link Command} to be executed.
     *
     * @param context Context for the current request
     */
    public boolean execute(Context context) throws Exception {

        // Look up the extra path information for this request
        ServletWebContext swcontext = (ServletWebContext) context;
        HttpServletRequest request = swcontext.getRequest();
        String pathInfo = (String)
            request.getAttribute("javax.servlet.include.path_info");
        if (pathInfo == null) {
            pathInfo = request.getPathInfo();
        }

        // Map to the Command specified by the extra path info
        Catalog catalog = (Catalog) context.get(getCatalogKey());
        Command command = catalog.getCommand(pathInfo);
        return (command.execute(context));

    }


}
