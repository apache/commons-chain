/*
 * $Header: /home/jerenkrantz/tmp/commons/commons-convert/cvs/home/cvs/jakarta-commons//chain/src/java/org/apache/commons/chain/generic/LookupCommand.java,v 1.7 2003/11/09 01:57:21 craigmcc Exp $
 * $Revision: 1.7 $
 * $Date: 2003/11/09 01:57:21 $
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

package org.apache.commons.chain.generic;


import org.apache.commons.chain.Catalog;
import org.apache.commons.chain.Chain;
import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;
import org.apache.commons.chain.Filter;


/**
 * <p>Look up a specified {@link Command} (which could also be a {@link Chain})
 * in a {@link Catalog}, and delegate execution to it.  If the delegated-to
 * {@link Command} is also a {@link Filter}, its <code>postprocess()</code>
 * method will also be invoked at the appropriate time.</p>
 *
 * <p>The name of the {@link Command} can be specified either directly (via
 * the <code>name</code> property) or indirectly (via the <code>nameKey</code>
 * property).  Exactly one of these must be set.</p>
 *
 * <p>If the <code>optional</code> property is set to <code>true</code>,
 * failure to find the specified command in the specified catalog will be
 * silently ignored.  Otherwise, a lookup failure will trigger an
 * <code>IllegalArgumentException</code>.</p>
 *
 * @author Craig R. McClanahan
 * @version $Revision: 1.7 $ $Date: 2003/11/09 01:57:21 $
 */

public class LookupCommand implements Filter {


    // -------------------------------------------------------------- Properties


    private String catalogKey = "catalog";


    /**
     * <p>Return the context attribute key under which the {@link Catalog}
     * instance to be searched is stored.</p>
     */
    public String getCatalogKey() {

    return (this.catalogKey);

    }


    /**
     * <p>Set the context attribute key under which the {@link Catalog}
     * instance to be searched is stored.</p>
     *
     * @param catalogKey The new context attribute key
     */
    public void setCatalogKey(String catalogKey) {

    this.catalogKey = catalogKey;

    }


    private String name = null;


    /**
     * <p>Return the name of the {@link Command} that we will look up and
     * delegate execution to.</p>
     */
    public String getName() {

    return (this.name);

    }


    /**
     * <p>Set the name of the {@link Command} that we will look up and
     * delegate execution to.</p>
     *
     * @param name The new command name
     */
    public void setName(String name) {

    this.name = name;

    }


    private String nameKey = null;


    /**
     * <p>Return the context attribute key under which the {@link Command}
     * name is stored.</p>
     */
    public String getNameKey() {

    return (this.nameKey);

    }


    /**
     * <p>Set the context attribute key under which the {@link Command}
     * name is stored.</p>
     *
     * @param nameKey The new context attribute key
     */
    public void setNameKey(String nameKey) {

    this.nameKey = nameKey;

    }


    private boolean optional = false;


    /**
     * <p>Return <code>true</code> if locating the specified command
     * is optional.</p>
     */
    public boolean isOptional() {

        return (this.optional);

    }


    /**
     * <p>Set the optional flag for finding the specified command.</p>
     *
     * @param optional The new optional flag
     */
    public void setOptional(boolean optional) {

        this.optional = optional;

    }



    // ---------------------------------------------------------- Filter Methods


    /**
     * <p>Look up the specified command, and (if found) execute it.</p>
     *
     * @param context The context for this request
     *
     * @exception IllegalArgumentException if no such {@link Command}
     *  can be found and the <code>optional</code> property is set
     *  to <code>false</code>
     */
    public boolean execute(Context context) throws Exception {

    Command command = getCommand(context);
        if (command != null) {
            return (command.execute(context));
        } else {
            return (false);
        }

    }


    public boolean postprocess(Context context, Exception exception) {

    Command command = getCommand(context);
        if (command != null) {
            if (command instanceof Filter) {
                return (((Filter) command).postprocess(context, exception));
            }
        }
        return (false);

    }


    // --------------------------------------------------------- Private Methods


    /**
     * <p>Return the {@link Command} instance to be delegated to.</p>
     *
     * @param context {@link Context} for this request
     *
     * @exception IllegalArgumentException if no such {@link Command}
     *  can be found and the <code>optional</code> property is set
     *  to <code>false</code>
     */
    private Command getCommand(Context context) {

    Catalog catalog = (Catalog)
        context.get(getCatalogKey());
    if (catalog == null) {
        throw new IllegalArgumentException(getCatalogKey());
    }
    Command command = null;
    String name = getName();
    if (name == null) {
        name = (String) context.get(getNameKey());
    }
    if (name != null) {
        command = catalog.getCommand(name);
        if ((command == null) && !isOptional()) {
        throw new IllegalArgumentException(name);
        }
        return (command);
    } else {
        throw new IllegalArgumentException("No command name");
    }

    }


}
