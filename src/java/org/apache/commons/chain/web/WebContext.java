/*
 * $Header: /home/jerenkrantz/tmp/commons/commons-convert/cvs/home/cvs/jakarta-commons//chain/src/java/org/apache/commons/chain/web/WebContext.java,v 1.2 2003/10/12 09:10:40 rdonkin Exp $
 * $Revision: 1.2 $
 * $Date: 2003/10/12 09:10:40 $
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


package org.apache.commons.chain.web;


import java.util.Map;
import org.apache.commons.chain.Chain;
import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;
import org.apache.commons.chain.impl.ContextBase;


/**
 * <p>Abstract base implementation of {@link Context} that provides web
 * based applications that use it a "generic" view of HTTP related requests
 * and responses, without tying the application to a particular underlying
 * Java API (such as servlets).  It is expected that a concrete subclass
 * of {@link WebContext} for each API (such as
 * {@link org.apache.commons.chain.web.servlet.ServletWebContext})
 * will support adapting that particular API's implementation of request
 * and response objects into this generic framework.</p>
 *
 * <p>The characteristics of a web request/response are made visible via
 * a series of JavaBeans properties (and mapped to read-only attributes
 * of the same name, as supported by {@link ContextBase}.</p>
 *
 * @author Craig R. McClanahan
 * @version $Revision: 1.2 $ $Date: 2003/10/12 09:10:40 $
 */

public abstract class WebContext extends ContextBase {


    // ---------------------------------------------------------- Public Methods


    /**
     * <p>Return a mutable <code>Map</code> that maps application scope
     * attribute names to their values.</p>
     */
    public abstract Map getApplicationScope();


    /**
     * <p>Return an immutable <code>Map</code> that maps header names to
     * the first (or only) header value (as a String).  Header names must
     * be matched in a case-insensitive manner.</p>
     */
    public abstract Map getHeader();


    /**
     * <p>Return an immutable <code>Map</code> that maps header names to
     * the set of all values specified in the request (as a String array).
     * Header names must be matched in a case-insensitive manner.</p>
     */
    public abstract Map getHeaderValues();


    /**
     * <p>Return an immutable <code>Map</code> that maps context application
     * initialization parameters to their values.</p>
     */
    public abstract Map getInitParam();


    /**
     * <p>Return an immutable <code>Map</code> that maps request parameter
     * names to the first (or only) value (as a String).</p>
     */
    public abstract Map getParam();


    /**
     * <p>Return an immutable <code>Map</code> that maps request parameter
     * names to the set of all values (as a String array).</p>
     */
    public abstract Map getParamValues();


    /**
     * <p>Return a mutable <code>Map</code> that maps request scope
     * attribute names to their values.</p>
     */
    public abstract Map getRequestScope();


    /**
     * <p>Return a mutable <code>Map</code> that maps session scope
     * attribute names to their values.</p>
     */
    public abstract Map getSessionScope();


}
