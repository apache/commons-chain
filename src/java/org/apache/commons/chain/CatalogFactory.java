/*
 * Copyright 1999-2004 The Apache Software Foundation
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
package org.apache.commons.chain;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.commons.chain.impl.CatalogFactoryBase;

/**
 * <p>A {@link CatalogFactory} is a class used to store and retrieve
 * {@link Catalog}s.  The factory allows for a default {@link Catalog}
 * as well as {@link Catalog}s stored with a name key.  Follows the
 * Factory pattern (see GoF).</p>
 *
 * @author Sean Schofield
 * @version $Revision: 1.5 $ $Date: 2004/11/30 05:52:22 $
 */

public abstract class CatalogFactory {


    // --------------------------------------------------------- Public Methods


    /**
     * <p>Gets the default instance of Catalog associated with the factory
     * (if any); otherwise, return <code>null</code>.</p>
     *
     * @return the default Catalog instance
     */
    public abstract Catalog getCatalog();


    /**
     * <p>Sets the default instance of Catalog associated with the factory.</p>
     *
     * @param catalog the default Catalog instance
     */
    public abstract void setCatalog(Catalog catalog);


    /**
     * <p>Retrieves a Catalog instance by name (if any); otherwise
     * return <code>null</code>.</p>
     *
     * @param name the name of the Catalog to retrieve
     * @return the specified Catalog
     */
    public abstract Catalog getCatalog(String name);


    /**
     * <p>Adds a named instance of Catalog to the factory (for subsequent
     * retrieval later).</p>
     *
     * @param name the name of the Catalog to add
     * @param catalog the Catalog to add
     */
    public abstract void addCatalog(String name, Catalog catalog);


    /**
     * <p>Return an <code>Iterator</code> over the set of named
     * {@link Catalog}s known to this {@link CatalogFactory}.
     * If there are no known catalogs, an empty Iterator is returned.</p>
     */
    public abstract Iterator getNames();


    // ------------------------------------------------------- Static Variables


    /**
     * <p>The set of registered {@link CatalogFactory} instances,
     * keyed by the relevant class loader.</p>
     */
    private static Map factories = new HashMap();


    // -------------------------------------------------------- Static Methods


    /**
     * <p>Return the singleton {@link CatalogFactory} instance
     * for the relevant <code>ClassLoader</code>.  For applications
     * that use a thread context class loader (such as web applications
     * running inside a servet container), this will return a separate
     * instance for each application, even if this class is loaded from
     * a shared parent class loader.</p>
     *
     * @return the per-application singleton instance of {@link CatalogFactory}
     */
    public static CatalogFactory getInstance() {

        CatalogFactory factory = null;
        ClassLoader cl = getClassLoader();
        synchronized (factories) {
            factory = (CatalogFactory) factories.get(cl);
            if (factory == null) {
                factory = new CatalogFactoryBase();
                factories.put(cl, factory);
            }
        }
        return factory;

    }


    /**
     * <p>Clear all references to registered catalogs, as well as to the
     * relevant class loader.  This method should be called, for example,
     * when a web application utilizing this class is removed from
     * service, to allow for garbage collection.</p>
     */
    public static void clear() {

        synchronized (factories) {
            factories.remove(getClassLoader());
        }

    }


    // ------------------------------------------------------- Private Methods


    /**
     * <p>Return the relevant <code>ClassLoader</code> to use as a Map key
     * for this request.  If there is a thread context class loader, return
     * that; otherwise, return the class loader that loaded this class.</p>
     */
    private static ClassLoader getClassLoader() {

        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        if (cl == null) {
            cl = CatalogFactory.class.getClassLoader();
        }
        return cl;

    }


}
