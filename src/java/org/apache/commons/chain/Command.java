/*
 * $Header: /home/jerenkrantz/tmp/commons/commons-convert/cvs/home/cvs/jakarta-commons//chain/src/java/org/apache/commons/chain/Command.java,v 1.4 2003/10/22 06:21:24 martinc Exp $
 * $Revision: 1.4 $
 * $Date: 2003/10/22 06:21:24 $
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
 * <p>A {@link Command} encapsulates a unit of processing work to be
 * performed, whose purpose is to examine and/or modify the state of a
 * transaction that is represented by a {@link Context}.  Individual
 * {@link Command}s can be assembled into a {@link Chain}, which allows
 * them to either complete the required processing or delegate further
 * processing to the next {@link Command} in the {@link Chain}.</p>
 *
 * <p>{@link Command} implementations should be designed in a thread-safe
 * manner, suitable for inclusion in multiple {@link Chain}s that might be
 * processed by different threads simultaneously.  In general, this implies
 * that {@link Command} classes should not maintain state information in
 * instance variables.  Instead, state information should be maintained via
 * suitable modifications to the attributes of the {@link Context} that is
 * passed to the <code>execute()</code> command.</p>
 *
 * <p>{@link Command} implementations typically retrieve and store state
 * information in the {@link Context} instance that is passed as a parameter
 * to the <code>execute()</code> method, using particular keys into the
 * <code>Map</code> that can be acquired via
 * <code>Context.getAttributes()</code>.  To improve interoperability of
 * {@link Command} implementations, a useful design pattern is to expose the
 * key values used as JavaBeans properties of the {@link Command}
 * implementation class itself.  For example, a {@link Command} that requires
 * an input and an output key might implement the following properties:</p>
 *
 * <pre>
 *   private String inputKey = "input";
 *   public String getInputKey() {
 *     return (this.inputKey);
 *   }
 *   public void setInputKey(String inputKey) {
 *     this.inputKey = inputKey;
 *   }
 *
 *   private String outputKey = "output";
 *   public String getOutputKey() {
 *     return (this.outputKey);
 *   }
 *   public void setOutputKey(String outputKey) {
 *     this.outputKey = outputKey;
 *   }
 * </pre>
 *
 * <p>And the operation of accessing the "input" information in the context
 * would be executed by calling:</p>
 *
 * <pre>
 *   String input = (String) context.get(getInputKey());
 * </pre>
 *
 * <p>instead of hard coding the attribute name.  The use of the "Key"
 * suffix on such property names is a useful convention to identify properties
 * being used in this fashion, as opposed to JavaBeans properties that simply
 * configure the internal operation of this {@link Command}.</p>
 *
 * @author Craig R. McClanahan
 * @version $Revision: 1.4 $ $Date: 2003/10/22 06:21:24 $
 */

public interface Command {


    /**
     * <p>Execute a unit of processing work to be performed.  This
     * {@link Command} may either complete the required processing
     * and return <code>true</code>, or delegate remaining processing
     * to the next {@link Command} in a {@link Chain} containing this
     * {@link Command} by returning <code>false</code>
     *
     * @param context The {@link Context} to be processed by this
     *  {@link Command}
     *
     * @exception Exception general purpose exception return
     *  to indicate abnormal termination
     * @exception IllegalArgumentException if <code>context</code>
     *  is <code>null</code>
     *
     * @return <code>true</code> if the processing of this {@link Context}
     *  has been completed, or <code>false</code> if the processing
     *  of this {@link Context} should be delegated to a subsequent
     *  {@link Command} in an enclosing {@link Chain}
     */
    boolean execute(Context context) throws Exception;


}
