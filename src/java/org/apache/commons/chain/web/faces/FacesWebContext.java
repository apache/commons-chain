/*
 * $Header: /home/jerenkrantz/tmp/commons/commons-convert/cvs/home/cvs/jakarta-commons//chain/src/java/org/apache/commons/chain/web/faces/FacesWebContext.java,v 1.4 2003/10/18 05:30:19 martinc Exp $
 * $Revision: 1.4 $
 * $Date: 2003/10/18 05:30:19 $
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


package org.apache.commons.chain.web.faces;


import java.util.Map;
import javax.faces.context.FacesContext;
import org.apache.commons.chain.web.WebContext;


/**
 * <p>Concrete implementation of {@link WebContext} suitable for use in
 * JavaServer Faces apps.  The abstract methods are mapped to the appropriate
 * collections of the underlying <code>FacesContext</code> instance
 * that is passed to the constructor (or the initialize method).</p>
 *
 * @author Craig R. McClanahan
 * @version $Revision: 1.4 $ $Date: 2003/10/18 05:30:19 $
 */

public class FacesWebContext extends WebContext {


    // ------------------------------------------------------------ Constructors


    /**
     * <p>Construct an uninitialized {@link FacesWebContext} instance.</p>
     */
    public FacesWebContext() {

        ;

    }


    /**
     * <p>Construct a {@link FacesWebContext} instance that is initialized
     * with the specified JavaServer Faces API objects.</p>
     *
     * @param context The <code>FacesContext</code> for this request
     */
    public FacesWebContext(FacesContext context) {

        initialize(context);

    }


    // ------------------------------------------------------ Instance Variables


    /**
     * <p>The <code>FacesContext</code> instance for the request represented
     * by this {@link WebContext}.</p>
     */
    private FacesContext context = null;


    // ---------------------------------------------------------- Public Methods


    /**
     * <p>Return the <code>FacesContext</code> instance for the request
     * associated with this {@link FacesWebContext}.</p>
     */
    public FacesContext getContext() {

    return (this.context);

    }


    /**
     * <p>Initialize (or reinitialize) this {@link FacesWebContext} instance
     * for the specified JavaServer Faces API objects.</p>
     *
     * @param context The <code>FacesContext</code> for this request
     */
    public void initialize(FacesContext context) {

        this.context = context;

    }


    /**
     * <p>Release references to allocated resources acquired in
     * <code>initialize()</code> of via subsequent processing.  After this
     * method is called, subsequent calls to any other method than
     * <code>initialize()</code> will return undefined results.</p>
     */
    public void release() {

        context = null;

    }



    // ------------------------------------------------------ WebContext Methods


    public Map getApplicationScope() {

    return (context.getExternalContext().getApplicationMap());

    }


    public Map getHeader() {

    return (context.getExternalContext().getRequestHeaderMap());

    }


    public Map getHeaderValues() {

    return (context.getExternalContext().getRequestHeaderValuesMap());

    }


    public Map getInitParam() {

    return (context.getExternalContext().getInitParameterMap());

    }


    public Map getParam() {

    return (context.getExternalContext().getRequestParameterMap());

    }


    public Map getParamValues() {

    return (context.getExternalContext().getRequestParameterValuesMap());

    }


    public Map getRequestScope() {

    return (context.getExternalContext().getRequestMap());

    }


    public Map getSessionScope() {

    return (context.getExternalContext().getSessionMap());

    }



}
