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
package org.apache.commons.chain2.base;

import org.apache.commons.chain2.Command;
import org.apache.commons.chain2.Context;
import org.apache.commons.chain2.Processing;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * An abstract base command which uses introspection to look up a method to execute.
 * For use by developers who prefer to group related functionality into a single class
 * rather than an inheritance family.
 *
 * @param <K> the type of keys maintained by the context associated with this command
 * @param <V> the type of mapped values
 * @param <C> Type of the context associated with this command
 *
 * @since Chain 1.1
 */
public abstract class DispatchCommand<K, V, C extends Context<K, V>> implements Command<K, V, C> {

    /** Cache of methods */
    private final Map<String, Method> methods = new WeakHashMap<String, Method>();

    /** Method name */
    private String method = null;

    /** Method key */
    private String methodKey = null;

    /**
     * The base implementation expects dispatch methods to take a <code>Context</code>
     * as their only argument.
     */
    protected static final Class<?>[] DEFAULT_SIGNATURE = new Class<?>[] {Context.class};

    /**
     * Look up the method specified by either "method" or "methodKey" and invoke it,
     * returning a {@link Processing} value as interpreted by <code>evaluateResult</code>.
     * @param context The Context to be processed by this Command.
     * @return the result of method being dispatched to.
     * @throws IllegalStateException if neither 'method' nor 'methodKey' properties are defined
     * @throws DispatchException if any is thrown by the invocation. Note that if invoking the method
     * results in an InvocationTargetException, the cause of that exception is thrown instead of
     * the exception itself, unless the cause is an <code>Error</code> or other <code>Throwable</code>
     * which is not an <code>Exception</code>.
     */
    public Processing execute(C context) {
        if (this.getMethod() == null && this.getMethodKey() == null) {
            throw new IllegalStateException("Neither 'method' nor 'methodKey' properties are defined ");
        }

        try {
            Method methodObject = extractMethod(context);
            return evaluateResult(methodObject.invoke(this,
                    getArguments(context)));

        } catch (NoSuchMethodException e) {
            throw new DispatchException("Error extracting method from context", e, context, this);
        } catch (IllegalAccessException e) {
            throw new DispatchException("Error accessing method", e, context, this);
        } catch (InvocationTargetException e) {
            Throwable cause = e.getTargetException();
            throw new DispatchException("Error in reflected dispatched command", cause, context, this);
        }
    }

    /**
     * Extract the dispatch method.  The base implementation uses the command's
     * <code>method</code> property as the name of a method to look up, or, if that is not defined,
     * looks up the the method name in the Context using the <code>methodKey</code>.
     *
     * @param context The Context being processed by this Command.
     * @return The method to execute
     * @throws NoSuchMethodException if no method can be found under the specified name.
     * @throws NullPointerException if no methodName cannot be determined
     */
    protected Method extractMethod(C context) throws NoSuchMethodException {
        String methodName = this.getMethod();

        if (methodName == null) {
            Object methodContextObj = context.get(this.getMethodKey());
            if (methodContextObj == null) {
                throw new NullPointerException("No value found in context under " + this.getMethodKey());
            }
            methodName = methodContextObj.toString();
        }

        Method theMethod = null;

        synchronized (methods) {
            theMethod = methods.get(methodName);

            if (theMethod == null) {
                theMethod = getClass().getMethod(methodName, getSignature());
                methods.put(methodName, theMethod);
            }
        }

        return theMethod;
    }

    /**
     * Evaluate the result of the method invocation as a processing value. Base implementation
     * expects that the invoked method returns processing, but subclasses might
     * implement other interpretations.
     * @param obj The result of the method execution
     * @return The evaluated result/
     */
    protected Processing evaluateResult(Object obj) {
        if(obj instanceof Processing) {
            Processing result = (Processing) obj;
            return result;
        } else {
            return Processing.CONTINUE;
        }
    }

    /**
     * Return a <code>Class[]</code> describing the expected signature of the method.
     * @return The method signature.
     */
    protected Class<?>[] getSignature() {
        return DEFAULT_SIGNATURE;
    }

    /**
     * Get the arguments to be passed into the dispatch method.
     * Default implementation simply returns the context which was passed in, but subclasses
     * could use this to wrap the context in some other type, or extract key values from the
     * context to pass in.  The length and types of values returned by this must coordinate
     * with the return value of <code>getSignature()</code>
     * @param context The Context being processed by this Command.
     * @return The method arguments.
     */
    protected Object[] getArguments(C context) {
        return new Object[] {context};
    }

    /**
     * Return the method name.
     * @return The method name.
     */
    public String getMethod() {
        return method;
    }

    /**
     * Return the Context key for the method name.
     * @return The Context key for the method name.
     */
    public String getMethodKey() {
        return methodKey;
    }

    /**
     * Set the method name.
     * @param method The method name.
     */
    public void setMethod(String method) {
        this.method = method;
    }

    /**
     * Set the Context key for the method name.
     * @param methodKey The Context key for the method name.
     */
    public void setMethodKey(String methodKey) {
        this.methodKey = methodKey;
    }

}
