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
import org.apache.commons.chain2.Command;
import org.apache.commons.chain2.Processing;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CatalogTest {

    @Mock private ServletContext servletContext;
    @Mock private HttpSession session;
    @Mock private HttpServletRequest request;
    @Mock private Catalog<String, Object, MailReader> testCatalog;
    @Mock private Command<String, Object, MailReader> testCommand;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        when(testCatalog.getCommand(anyString())).thenReturn(testCommand);
        when(servletContext.getAttribute("catalog")).thenReturn(testCatalog);
        when(session.getServletContext()).thenReturn(servletContext);
        when(request.getSession()).thenReturn(session);
    }

    private Processing executeCatalogCommand(MailReader context,
                                             String name, HttpServletRequest request) {

        ServletContext servletContext =
                request.getSession().getServletContext();

        // Due to limitation with the servlet API we have to do a cast here
        @SuppressWarnings("unchecked")
        Catalog<String, Object, MailReader> catalog =
                (Catalog<String, Object, MailReader>) servletContext
                        .getAttribute("catalog");

        Command<String, Object, MailReader> command = catalog.getCommand(name);

        return command.execute(context);
    }

    @Test
    public void loadCatalogTest() {
        MailReader context = new MailReader();
        context.setLocale(Locale.CANADA);

        when(testCommand.execute(context)).thenReturn(Processing.FINISHED);

        assertEquals("Catalog execution did not complete as expected", Processing.FINISHED,
                executeCatalogCommand(context, "aCommand", request));

        verify(testCommand).execute(context);
    }
}
