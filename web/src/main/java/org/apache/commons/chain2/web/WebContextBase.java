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
package org.apache.commons.chain2.web;

import org.apache.commons.chain2.impl.ContextBase;

/**
 * <p>Abstract base implementation of {@link org.apache.commons.chain2.Context} that
 * provides web based applications that use it a "generic" view of HTTP related
 * requests and responses, without tying the application to a particular underlying
 * Java API (such as servlets).  It is expected that a concrete subclass
 * of {@link WebContextBase} for each API (such as
 * {@link org.apache.commons.chain2.web.servlet.ServletWebContext})
 * will support adapting that particular API's implementation of request
 * and response objects into this generic framework.</p>
 *
 * <p>The characteristics of a web request/response are made visible via
 * a series of JavaBeans properties (and mapped to read-only attributes
 * of the same name, as supported by {@link ContextBase}.</p>
 *
 * @version $Id$
 */
public abstract class WebContextBase extends ContextBase
        implements WebContext<String, Object> {

    /**
     *
     */
    private static final long serialVersionUID = 20120724L;

    // ---------------------------------------------------------- Public Methods

}
