/*
 * $Header: /home/jerenkrantz/tmp/commons/commons-convert/cvs/home/cvs/jakarta-commons//chain/src/java/org/apache/commons/chain/Filter.java,v 1.3 2003/10/01 12:32:03 husted Exp $
 * $Revision: 1.3 $
 * $Date: 2003/10/01 12:32:03 $
 *
/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2003 The Apache Software Foundation.  All rights
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
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "The Jakarta Project", "Commons", and "Apache Software
 *    Foundation" must not be used to endorse or promote products derived
 *    from this software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache",
 *    nor may "Apache" appear in their name, without prior written
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

package org.apache.commons.chain;


/**
 * <p>A {@link Filter} is a specialized {@link Command} that also expects
 * the {@link Chain} that is executing it to call the
 * <code>postprocess()</code> method if it called the <code>execute()</code>
 * method.  This promise must be fulfilled in spite of any possible
 * exceptions thrown by the <code>execute()</code> method of this
 * {@link Command}, or any subsequent {@link Command} whose
 * <code>execute()</code> method was called.  The owning {@link Chain}
 * must call the <code>postprocess()</code> method of each {@link Filter}
 * in a {@link Chain} in reverse order of the invocation of their
 * <code>execute()</code> methods.</p>
 *
 * <p>The most common use case for a {@link Filter}, as opposed to a
 * {@link Command}, is where potentially expensive resources must be acquired
 * and held until the processing of a particular request has been completed,
 * even it execution is delegated to a subsequent {@link Command} via the
 * <code>execute()</code> returning <code>false</code>.  A {@link Filter}
 * can reliably release such resources in the <code>postprocess()</code>
 * method, which is guaranteed to be called by the owning {@link Chain}.</p>
 *
 * @author Craig R. McClanahan
 * @version $Revision: 1.3 $ $Date: 2003/10/01 12:32:03 $
 */

public interface Filter extends Command {


    /**
     * <p>Execute any cleanup activities, such as releasing resources that
     * were acquired during the <code>execute()</code> method of this
     * {@link Filter} instance.</p>
     *
     * @param context The {@link Context} to be processed by this
     *  {@link Filter}
     * @param exception The <code>Exception</code> (if any) that was thrown
     *  by the last {@link Command} that was executed; otherwise
     *  <code>null</code>
     *
     * @exception IllegalArgumentException if <code>context</code>
     *  is <code>null</code>
     *
     * @return If a non-null <code>exception</code> was "handled" by this
     *  method (and therefore need not be rethrown), return <code>true</code>;
     *  otherwise return <code>false</code>
     */ 
   public boolean postprocess(Context context, Exception exception);


}
