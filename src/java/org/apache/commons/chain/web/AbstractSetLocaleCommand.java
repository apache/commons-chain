/*
 * $Header: /home/jerenkrantz/tmp/commons/commons-convert/cvs/home/cvs/jakarta-commons//chain/src/java/org/apache/commons/chain/web/AbstractSetLocaleCommand.java,v 1.2 2003/09/29 06:02:13 craigmcc Exp $
 * $Revision: 1.2 $
 * $Date: 2003/09/29 06:02:13 $
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


package org.apache.commons.chain.web;


import java.util.Locale;
import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;


/**
 * <p>Abstract base {@link Command} implementation for setting the
 * response locale for this response to the <code>Locale</code> stored
 * under the context attribute key returned by the <code>localeKey</code>
 * property.</p>
 *
 * @author Craig R. McClanahan
 * @version $Revision: 1.2 $ $Date: 2003/09/29 06:02:13 $
 */

public abstract class AbstractSetLocaleCommand implements Command {


    // -------------------------------------------------------------- Properties


    /**
     * <p>The context attribute key used to retrieve the <code>Locale</code>.</p>
     */
    private String localeKey = "locale";


    /**
     * <p>Return the context attribute key under which we will retrieve
     * the response <code>Locale</code>.</p>
     */
    public String getLocaleKey() {

	return (this.localeKey);

    }


    /**
     * <p>Set the context attribute key under which we will retrieve
     * the response <code>Locale</code>.</p>
     *
     * @param localeKey The new context attribute key
     */
    public void setLocaleKey(String localeKey) {

	this.localeKey = localeKey;

    }


    // --------------------------------------------------------- Command Methods


    /**
     * <p>Retrieve the <code>Locale</code> stored under the specified
     * context attribute key, and establish it on this response.</p>
     *
     * @param context The {@link Context} we are operating on
     *
     * @return <code>false</code> so that processng will continue
     */
    public boolean execute(Context context) throws Exception {

	setLocale(context,
		  (Locale) context.get(getLocaleKey()));
	return (false);

    }


    // ------------------------------------------------------- Protected Methods


    /**
     * <p>Establish the specified <code>Locale</code> for this response.</p>
     */
    protected abstract void setLocale(Context context, Locale locale);


}
