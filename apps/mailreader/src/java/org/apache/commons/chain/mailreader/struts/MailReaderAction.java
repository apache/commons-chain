/*
 * $Header: /home/jerenkrantz/tmp/commons/commons-convert/cvs/home/cvs/jakarta-commons//chain/apps/mailreader/src/java/org/apache/commons/chain/mailreader/struts/MailReaderAction.java,v 1.2 2004/03/29 02:34:19 husted Exp $
 * $Revision: 1.2 $
 * $Date: 2004/03/29 02:34:19 $
 *
 * Copyright 1999-2004 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.chain.mailreader.struts;

import org.apache.commons.chain.Context;
import org.apache.commons.chain.mailreader.ClientContext;
import org.apache.commons.chain.mailreader.MailReaderBase;
import org.apache.struts.webapp.example.UserDatabase;
import org.apache.struts.webapp.example.Constants;

import java.util.Locale;

/**
 * <p>Process Commands using a {@link org.apache.commons.chain.mailreader.MailReader}
 * {@link ClientContext}.</p>
 */
public class MailReaderAction extends CommandAction {

    // See interface for JavaDoc
    public ClientContext getContext(ActionHelper helper) {

        Locale locale = helper.getLocale();
        Context input = getInput(helper.getActionForm());
        UserDatabase database = (UserDatabase)
                helper.getAttribute(Constants.DATABASE_KEY);
        return new MailReaderBase(locale, input, database);

    }

}
