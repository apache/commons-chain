/*
 * $Header: /home/jerenkrantz/tmp/commons/commons-convert/cvs/home/cvs/jakarta-commons//chain/apps/mailreader/src/java/org/apache/commons/chain/mailreader/commands/Attic/MailReaderBase.java,v 1.2 2004/03/28 03:20:56 husted Exp $
 * $Revision: 1.2 $
 * $Date: 2004/03/28 03:20:56 $
 *
 * Copyright 2000-2004 Apache Software Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.chain.mailreader.commands;

import org.apache.commons.chain.Context;
import org.apache.commons.chain.impl.ContextBase;

import java.util.Locale;

/**
 * Implements MailReader API for a Struts application.
 */
public class MailReaderBase extends ContextBase implements MailReader {

    /**
     * <p>Default constructor.</p>
     */
    public MailReaderBase() {
        super();
    }

    /**
     * <p>Convenience constructor to create and populate instance.</p>
     * @param locale
     * @param input
     */
    public MailReaderBase(Locale locale, Context input) {
        super();
        this.locale = locale;
        this.input = input;
    }

    /**
     * <p>Field for Locale property.</p>
     */
    private Locale locale;

    /**
     * <p>Return Locale property</p>
     * @return This Locale property
     */
    public Locale getLocale() {
        return locale;
    }

    /**
     * <p>Assign Locale property</p>
     * @param locale New Locale
     */
    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    /**
     * <p>Field for Input property.</p>
     */
    private Context input;

    /**
     * <p>Return Input property.</p>
     * @return This Input property
     */
    public Context getInput() {
        return input;
    }

    /**
     * <p>Assign Input property</p>
     * @param input New Input context
     */
    public void setInput(Context input) {
        this.input = input;
    }
}
