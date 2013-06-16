package org.apache.commons.chain2.testutils;

import static org.apache.commons.chain2.testutils.HasCommandCount.hasCommandCount;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.apache.commons.chain2.Catalog;
import org.apache.commons.chain2.Context;
import org.hamcrest.StringDescription;
import org.junit.Before;
import org.junit.Test;

/**
 * @version $Id$
 */
public class HasCommandCountTest {

    private Catalog<String, Object, Context<String, Object>> catalog;
    private HasCommandCount matcher;

    @Before
    public void setUp() throws Exception {
        catalog = new TestCatalog<String, Object, Context<String, Object>>();
        matcher = new HasCommandCount(2);
    }

    @Test
    public void wrongCountFails() throws Exception {
        assertFalse(matcher.matchesSafely(catalog, new StringDescription()));
    }

    @Test
    public void correctCount() throws Exception {
        catalog.addCommand("addingCMD", new AddingCommand());
        catalog.addCommand("DelegatingCMD", new DelegatingCommand());

        assertTrue(matcher.matchesSafely(catalog, new StringDescription()));
    }

    @Test(expected = IllegalStateException.class)
    public void inconsistentCatalogThrowsException() throws Exception {
        catalog.addCommand("key for null", null);

        matcher.matchesSafely(catalog, new StringDescription());
    }
}
