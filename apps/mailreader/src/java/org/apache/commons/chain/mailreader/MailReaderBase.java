/*
 * $Header: /home/jerenkrantz/tmp/commons/commons-convert/cvs/home/cvs/jakarta-commons//chain/apps/mailreader/src/java/org/apache/commons/chain/mailreader/Attic/MailReaderBase.java,v 1.2 2004/03/29 02:34:19 husted Exp $
 * $Revision: 1.2 $
 * $Date: 2004/03/29 02:34:19 $
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
package org.apache.commons.chain.mailreader;

import org.apache.commons.chain.Context;
import org.apache.commons.chain.impl.ContextBase;
import org.apache.struts.webapp.example.UserDatabase;
import org.apache.struts.webapp.example.User;

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
    public MailReaderBase(Locale locale, Context input, UserDatabase database) {
        super();
        this.locale = locale;
        this.input = input;
        this.database = database;
    }

    /**
     * <p>Field for Locale property.</p>
     */
    private Locale locale;

    // See ContextContext interface for JavaDoc
    public Locale getLocale() {
        return locale;
    }

    // See ContextContext interface for JavaDoc
    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    /**
     * <p>Field for Input property.</p>
     */
    private Context input;

    // See ContextContext interface for JavaDoc
    public Context getInput() {
        return input;
    }

    // See ContextContext interface for JavaDoc
    public void setInput(Context input) {
        this.input = input;
    }

    /**
     * <p>Field for database property.</p>
     */
    private UserDatabase database;

    // See MailReader interface for JavaDoc
    public UserDatabase getDatabase() {
        return database;
    }

    // See MailReader interface for JavaDoc
    public void setDatabase(UserDatabase database) {
        this.database = database;
    }

    /**
     * <p>Field for user property.</p>
     */
    private User user;

    // See MailReader interface for JavaDoc
    public User getUser() {
        return user;
    }

    // See MailReader interface for JavaDoc
    public void setUser(User user) {
        this.user = user;
    }

}
