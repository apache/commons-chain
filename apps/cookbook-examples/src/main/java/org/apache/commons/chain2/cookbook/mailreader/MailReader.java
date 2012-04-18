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
package org.apache.commons.chain2.cookbook.mailreader;

import org.apache.commons.chain2.impl.ContextBase;

import java.io.Writer;
import java.util.Locale;

public class MailReader extends ContextBase {

    public static String LOCALE_KEY = "locale";

    private Writer logger;
    private Locale locale;

    public MailReader() {}

    public Locale getLocale() {
        return locale;
    }
    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public Writer getLogger() {
        return logger;
    }

    public void setLogger(Writer logger) {
        this.logger = logger;
    }
}