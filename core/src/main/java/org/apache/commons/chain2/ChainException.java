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
 * Runtime Exception that wraps an underlying exception thrown during the
 * execution of a {@link Command} or {@link Chain}.
 *
 * @author Elijah Zupancic
 * @version $Id$
 * @since 2.0
 */
public class ChainException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * Context used when exception occurred.
     */
    private final Map<?, ?> context;

    /**
     * Command that failed when exception occurred.
     */
    private final Command<?, ?, ?> failedCommand;

    /**
     * Create an exception object with a message.
     * @param message Message to associate with exception
     */
    public ChainException(String message) {
        super(message);
        this.context = null;
        this.failedCommand = null;
    }

    /**
     * Create an exception object with a message and chain it to another exception.
     * @param message Message to associate with exception
     * @param cause Exception to chain to this exception
     */
    public ChainException(String message, Throwable cause) {
        super(message, cause);
        this.context = null;
        this.failedCommand = null;
    }

    /**
     * Constructs a new ChainException with references to the {@link Context}
     * and {@link Command} associated with the exception being wrapped (cause).
     * @param <K> Context key type
     * @param <V> Context value type
     * @param <C> Type of the context associated with this command
     * @param   message   the detail message. The detail message is saved for
     *          later retrieval by the {@link #getMessage()} method.
     * @param cause the cause (which is saved for later retrieval by the
     *         {@link #getCause()} method).  (A <tt>null</tt> value is
     *         permitted, and indicates that the cause is nonexistent or
     *         unknown.)
     * @param context The Context object passed to the {@link Command} in
     *                which the exception occurred.
     * @param failedCommand The Command object in which the exception was
     *                      thrown.
     */
    public <K, V, C extends Map<K, V>> ChainException(String message, Throwable cause,
                                                      C context, Command<K, V, C> failedCommand) {
        super(message, cause);
        this.context = context;
        this.failedCommand = failedCommand;
    }

    /**
     * @return The context object passed when the {@link Command}
     * threw an exception.
     */
    public Map<?, ?> getContext() {
        return context;
    }

    /**
     * @return The {@link Command} object in which the original exception was
     * thrown.
     */
    public Command<?, ?, ?> getFailedCommand() {
        return failedCommand;
    }

}
