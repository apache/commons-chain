/*
 * $Header: /home/jerenkrantz/tmp/commons/commons-convert/cvs/home/cvs/jakarta-commons//chain/apps/mailreader/src/java/org/apache/commons/chain/mailreader/commands/Attic/MailReader.java,v 1.1 2004/03/27 03:58:02 husted Exp $
 * $Revision: 1.1 $
 * $Date: 2004/03/27 03:58:02 $
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
import org.apache.commons.chain.mailreader.ViewContext;

import java.util.Locale;

/**
 * Application interface for MailReader Commands.
 */
public interface MailReader extends ViewContext {

    static String PN_COUNTRY = "country";
    static String PN_LANGUAGE = "language";

}
