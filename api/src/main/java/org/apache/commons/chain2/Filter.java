/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.chain2;

import java.util.Map;

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
 * even if execution is delegated to a subsequent {@link Command} via the
 * <code>execute()</code> returning <code>CONTINUE</code>.  A {@link Filter}
 * can reliably release such resources in the <code>postprocess()</code>
 * method, which is guaranteed to be called by the owning {@link Chain}.</p>
 *
 * @param <K> the type of keys maintained by the context associated with this command
 * @param <V> the type of mapped values
 * @param <C> Type of the context associated with this command
 *
 * @version $Id$
 */
public interface Filter<K, V, C extends Map<K, V>> extends Command<K, V, C> {

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
     * @throws IllegalArgumentException if <code>context</code>
     *  is <code>null</code>
     *
     * @return If a non-null <code>exception</code> was "handled" by this
     *  method (and therefore need not be rethrown), return <code>FINISHED</code>;
     *  otherwise return <code>CONTINUE</code>
     */
   boolean postprocess(C context, Exception exception);

   void undo(C context, Exception exception);

}
