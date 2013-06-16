package org.apache.commons.chain2.testutils;

import java.util.Iterator;

import org.apache.commons.chain2.Catalog;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

/**
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
