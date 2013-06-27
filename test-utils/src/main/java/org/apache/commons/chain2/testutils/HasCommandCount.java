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

package org.apache.commons.chain2.testutils;

import java.util.Iterator;

import org.apache.commons.chain2.Catalog;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

/**
 * A matchers that checks if a catalog has an expected count of commands.
 *
 * @version $Id$
 */
public class HasCommandCount extends TypeSafeDiagnosingMatcher<Catalog> {

    private int expected;

    public HasCommandCount(int count) {
        this.expected = count;
    }

    @Factory
    public static Matcher<Catalog> hasCommandCount(int expectedCount) {
        return new HasCommandCount(expectedCount);
    }

    @Override
    protected boolean matchesSafely(Catalog catalog, Description mismatchDescription) {
        int actual = countCommands(catalog);
        if (actual != expected) {
            mismatchDescription.appendText(" catalog contains ").appendValue(actual).appendText(" commands ");
            return false;
        }
        return true;
    }

    private int countCommands(Catalog catalog) {
        int count = 0;
        Iterator<String> names = catalog.getNames();
        while (names.hasNext()) {
            checkExists(catalog, names.next());
            count++;
        }
        return count;
    }

    private void checkExists(Catalog catalog, String name) {
        if (catalog.getCommand(name) == null) {
            String msg = String.format("Catalog contains a command with name %s but getCommand(%s) returned null!", name, name);
            throw new IllegalStateException(msg);
        }
    }

    @Override
    public void describeTo(Description description) {
        description.appendText(" catalog contains ").appendValue(expected).appendText(" commands ");
    }

}
