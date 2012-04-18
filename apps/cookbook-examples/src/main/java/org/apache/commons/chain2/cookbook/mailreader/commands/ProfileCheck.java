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
package org.apache.commons.chain2.cookbook.mailreader.commands;

import org.apache.commons.chain2.Command;
import org.apache.commons.chain2.cookbook.mailreader.MailReader;

import java.io.IOException;

public class ProfileCheck implements Command<String, Object, MailReader> {
    public ProfileCheck() {
    }

    public boolean execute(MailReader mailReader) {
        try {
            mailReader.getLogger().write("ProfileCheck.execute\n");
        }
        catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }

        return CONTINUE_PROCESSING;
    }
}
