/*
 * $Header: /home/jerenkrantz/tmp/commons/commons-convert/cvs/home/cvs/jakarta-commons//chain/apps/mailreader/src/java/org/apache/commons/chain/mailreader/commands/Attic/MailReader.java,v 1.2 2004/03/28 03:20:56 husted Exp $
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

import org.apache.commons.chain.mailreader.ClientContext;

/**
 * Application interface for MailReader Commands.
 */
public interface MailReader extends ClientContext {

    /**
     * Property name for the country field of a Locale.
     */
    static String PN_COUNTRY = "country";

    /**
     * Property name for the language field of a Locale.
     */
    static String PN_LANGUAGE = "language";


    // Database
    // User

}
