/*
 * $Header: /home/jerenkrantz/tmp/commons/commons-convert/cvs/home/cvs/jakarta-commons//chain/src/test/org/apache/commons/chain/impl/NonDelegatingCommand.java,v 1.2 2003/08/12 20:33:25 husted Exp $
 * $Revision: 1.2 $
 * $Date: 2003/08/12 20:33:25 $
 *
 * ====================================================================
 *
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 1999-2002 The Apache Software Foundation.  All rights
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

package org.apache.commons.chain.impl;


import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;


/**
 * <p>Implementation of {@link Command} that simply logs its identifier
 * and returns.</p>
 *
 * @author Craig R. McClanahan
 * @version $Revision: 1.2 $ $Date: 2003/08/12 20:33:25 $
 */

public class NonDelegatingCommand implements Command {


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
    protected String id = null;

    String getId() {
        return (this.id);
    }

    public void setId(String id) {
	this.id = id;
    }


    // -------------------------------------------------------- Command Methods


    // Execution method for this Command
    public boolean execute(Context context) throws Exception {

        if (context == null) {
            throw new IllegalArgumentException();
        }
        log(context, id);
        return (true);

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
    protected void log(Context context, String id) {
        StringBuffer sb = (StringBuffer) context.getAttributes().get("log");
        if (sb == null) {
            sb = new StringBuffer();
            context.getAttributes().put("log", sb);
        }
        if (sb.length() > 0) {
            sb.append('/');
        }
        sb.append(id);
    }


}
