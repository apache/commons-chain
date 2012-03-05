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
package org.apache.commons.chain2.config;


import org.apache.commons.chain2.Command;
import org.apache.commons.chain2.Context;


/**
 * <p>Test implementation of <code>Command</code> that exposes
 * configurable properties.</p>
 */

public class TestCommand implements Command<String, Object, Context<String, Object>> {


    private String bar = null;
    public String getBar() {
    return (this.bar);
    }
    public void setBar(String bar) {
    this.bar = bar;
    }


    private String foo = null;
    public String getFoo() {
    return (this.foo);
    }
    public void setFoo(String foo) {
    this.foo = foo;
    }


    public boolean execute(Context<String, Object> context) {
    return (false);
    }


}
