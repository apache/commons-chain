/*
 * $Header: /home/jerenkrantz/tmp/commons/commons-convert/cvs/home/cvs/jakarta-commons//chain/src/java/org/apache/commons/chain/impl/ChainBase.java,v 1.4 2003/10/12 09:10:55 rdonkin Exp $
 * $Revision: 1.4 $
 * $Date: 2003/10/12 09:10:55 $
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


package org.apache.commons.chain.impl;


import java.util.Collection;
import java.util.Iterator;
import org.apache.commons.chain.Chain;
import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;
import org.apache.commons.chain.Filter;


/**
 * <p>Convenience base class for {@link Chain} implementations.</p>
 *
 * @author Craig R. McClanahan
 * @version $Revision: 1.4 $ $Date: 2003/10/12 09:10:55 $
 */

public class ChainBase implements Chain {


    // ----------------------------------------------------------- Constructors


    /**
     * <p>Construct a {@link Chain} with no configured {@link Command}s.</p>
     */
    public ChainBase() {

    }


    /**
     * <p>Construct a {@link Chain} configured with the specified
     * {@link Command}.</p>
     *
     * @param command The {@link Command} to be configured
     *
     * @exception IllegalArgumentException if <code>command</code>
     *  is <code>null</code>
     */
    public ChainBase(Command command) {

        addCommand(command);

    }


    /**
     * <p>Construct a {@link Chain} configured with the specified
     * {@link Command}s.</p>
     *
     * @param commands The {@link Command}s to be configured
     *
     * @exception IllegalArgumentException if <code>commands</code>,
     *  or one of the individual {@link Command} elements,
     *  is <code>null</code>
     */
    public ChainBase(Command commands[]) {

        if (commands == null) {
            throw new IllegalArgumentException();
        }
        for (int i = 0; i < commands.length; i++) {
            addCommand(commands[i]);
        }

    }


    /**
     * <p>Construct a {@link Chain} configured with the specified
     * {@link Command}s.</p>
     *
     * @param commands The {@link Command}s to be configured
     *
     * @exception IllegalArgumentException if <code>commands</code>,
     *  or one of the individual {@link Command} elements,
     *  is <code>null</code>
     */
    public ChainBase(Collection commands) {

        if (commands == null) {
            throw new IllegalArgumentException();
        }
        Iterator elements = commands.iterator();
        while (elements.hasNext()) {
            addCommand((Command) elements.next());
        }

    }


    // ----------------------------------------------------- Instance Variables


    /**
     * <p>The list of {@link Command}s configured for this {@link Chain}, in
     * the order in which they may delegate processing to the remainder of
     * the {@link Chain}.</p>
     */
    protected Command commands[] = new Command[0];


    /**
     * <p>Flag indicating whether the configuration of our commands list
     * has been frozen by a call to the <code>execute()</code> method.</p>
     */
    protected boolean frozen = false;


    // ---------------------------------------------------------- Chain Methods


    // Documented in Chain interface
    public void addCommand(Command command) {

        if (command == null) {
            throw new IllegalArgumentException();
        }
        if (frozen) {
            throw new IllegalStateException();
        }
        Command results[] = new Command[commands.length + 1];
        System.arraycopy(commands, 0, results, 0, commands.length);
        results[commands.length] = command;
        commands = results;

    }


    // Documented in Chain interface
    public boolean execute(Context context) throws Exception {

        // Verify our parameters
        if (context == null) {
            throw new IllegalArgumentException();
        }

        // Freeze the configuration of the command list
        frozen = true;

        // Execute the commands in this list until one returns true
        // or throws an exception
        boolean saveResult = false;
        Exception saveException = null;
        int i = 0;
        int n = commands.length;;
        for (i = 0; i < n; i++) {
            try {
                saveResult = commands[i].execute(context);
                if (saveResult) {
                    break;
                }
            } catch (Exception e) {
                saveException = e;
                break;
            }   
        }

        // Call postprocess methods on Filters in reverse order
        if (i >= n) { // Fell off the end of the chain
            i--;
        }
        boolean handled = false;
        boolean result = false;
        for (int j = i; j >= 0; j--) {
            if (commands[j] instanceof Filter) {
                try {
                    result =
                        ((Filter) commands[j]).postprocess(context,
                                                           saveException);
                    if (result) {
                        handled = true;
                    }
                } catch (Exception e) {
                    ; // Silently ignore
                }
            }
        }

        // Return the exception or result state from the last execute()
        if ((saveException != null) && !handled) {
            throw saveException;
        } else {
            return (saveResult);
        }

    }


    // -------------------------------------------------------- Package Methods


    /**
     * <p>Return an array of the configured {@link Command}s for this
     * {@link Chain}.  This method is package private, and is used only
     * for the unit tests.</p>
     */
    Command[] getCommands() {

        return (commands);

    }


}
