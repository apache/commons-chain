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

import org.apache.commons.chain2.Catalog;
import org.apache.commons.chain2.CatalogFactory;
import org.apache.commons.chain2.Command;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @version $Id$
 */
public class MailReaderServlet extends HttpServlet {

    public MailReaderServlet() {
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/plain");

        MailReader context = new MailReader();
        context.setLocale(request.getLocale());
        context.setLogger(response.getWriter());

        CatalogFactory<String, Object, MailReader> catalogFactory =
                CatalogFactory.getInstance();

        Catalog<String, Object, MailReader> catalog =
                catalogFactory.getCatalog();

        if (catalog == null) {
            String msg = String.format("No catalog returned from factory: %s",
                    catalogFactory.getClass().getName());
            throw new IllegalArgumentException(msg);
        }

        Command<String, Object, MailReader> profileCheckCmd =
                catalog.<Command<String, Object, MailReader>>getCommand("LocaleChange");

        profileCheckCmd.execute(context);
    }

}
