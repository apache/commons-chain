/*
 * $Header: /home/jerenkrantz/tmp/commons/commons-convert/cvs/home/cvs/jakarta-commons//chain/apps/agility/src/java/org/apache/commons/agility/impl/ControllerCatalog.java,v 1.1 2004/06/01 00:55:50 husted Exp $
 * $Revision$
 * $Date$
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.agility.impl;

import org.apache.commons.agility.Controller;
import org.apache.commons.agility.ProcessException;
import org.apache.commons.agility.Request;
import org.apache.commons.agility.RequestHandler;
import org.apache.commons.chain.Command;
import org.apache.commons.chain.impl.CatalogBase;

/**
 * Concrete implemenation of Controller utilizing a Common Chain Catalog.
 */
public class ControllerCatalog extends CatalogBase implements Controller {

    // See interface for Javadoc
    public void addHandler(RequestHandler handler) {
        this.addCommand(handler.getName(), (Command) handler);
    }

    // See interface for Javadoc
    public RequestHandler getHandler(String name) {
        return null;
    }

    // See interface for Javadoc
    public void process(Request request) throws ProcessException {
        RequestHandler handler = (RequestHandler) getCommand(request.getName());
        if (handler != null) handler.handle(request);
    }
}
