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
package org.apache.commons.chain2.impl;

/**
 * Subclass of <code>ContextBase</code> to exercize the automatic
 * delegation to properties of the <code>Context</code> class.
 *
 */
public class TestContext extends ContextBase {

    private static final long serialVersionUID = 20120724L;

    // Read-only property
    private final String readOnly = "readOnly";

    public String getReadOnly() {
        return (this.readOnly);
    }

    // Read-write property
    private String readWrite = "readWrite";

    public String getReadWrite() {
        return (this.readWrite);
    }

    public void setReadWrite(String readWrite) {
        this.readWrite = readWrite;
    }

    // Write-only property
    private String writeOnly = "writeOnly";

    // Not a JavaBeans getter
    public String returnWriteOnly() {
        return (this.writeOnly);
    }

    public void setWriteOnly(String writeOnly) {
        this.writeOnly = writeOnly;
    }

}

