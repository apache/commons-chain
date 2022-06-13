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

import org.apache.commons.chain2.Context;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

/**
 * A matcher that checks if a {@link Context} contains a log with the given contents.
 * This matcher assumes that the context has a context value of type {@link StringBuilder}
 * registered for key {@code log}.
 *
 */
public class HasLog extends TypeSafeDiagnosingMatcher<Context> {

    private final String expectedContent;

    public HasLog(String expectedContent) {
        this.expectedContent = expectedContent;
    }

    @Factory
    public static Matcher<? super Context> hasLog(final String logContent) {
        return new HasLog(logContent);
    }

    @Override
    protected boolean matchesSafely(Context item, Description mismatchDescription) {
        StringBuilder log = (StringBuilder) item.get("log");
        if (log == null) {
            mismatchDescription.appendText("context has no log ");
            return false;
        }
        String actualContent = log.toString();
        if (!actualContent.equals(expectedContent)) {
            mismatchDescription.appendText("log has content ").appendValue(actualContent);
            return false;
        }
        return true;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("log has content ").appendValue(expectedContent);
    }

}
