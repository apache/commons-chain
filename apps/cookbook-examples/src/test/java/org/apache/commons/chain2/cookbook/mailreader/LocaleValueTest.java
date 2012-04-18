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

import org.junit.Before;
import org.junit.Test;
import java.util.Locale;
import static org.junit.Assert.*;

public class LocaleValueTest {

    MailReader context;

    @Before
    public void setUp() {
        context = new MailReader();
    }

    @Test
    public void testLocaleSetPropertyGetMap() {
        Locale expected = Locale.CANADA_FRENCH;
        context.setLocale(expected);
        // the retrieve() method is a nice type-safe alternative to get()
        Locale locale = context.retrieve(MailReader.LOCALE_KEY);
        assertNotNull(locale);
        assertEquals(expected, locale);
    }

    @Test
    public void testLocalePutMapGetProperty() {
        Locale expected = Locale.ITALIAN;
        context.put(MailReader.LOCALE_KEY, expected);
        Locale locale = context.getLocale();
        assertNotNull(locale);
        assertEquals(expected, locale);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testLocaleSetTypedWithStringException() {
        String localeString = Locale.US.toString();
        // Expected 'argument type mismatch' error
        context.put(MailReader.LOCALE_KEY, localeString);
    }
}