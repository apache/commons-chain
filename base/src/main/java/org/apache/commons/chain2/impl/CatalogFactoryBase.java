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

package org.apache.commons.chain2.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.chain2.Catalog;
import org.apache.commons.chain2.CatalogFactory;
import org.apache.commons.chain2.Command;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <p>A simple implementation of {@link CatalogFactory}.</p>
 *
 * @param <K> Context key type
 * @param <V> Context value type
 * @param <C> Type of the context associated with this command
 * @version $Id$
 */
public class CatalogFactoryBase<K, V, C extends Map<K, V>> implements CatalogFactory<K, V, C> {

    // ----------------------------------------------------------- Constructors

    /**
     * <p>Construct an empty instance of {@link CatalogFactoryBase}.  This
     * constructor is intended solely for use by {@link CatalogFactory}.</p>
     */
    public CatalogFactoryBase() {
    }

    // ----------------------------------------------------- Instance Variables

    /**
     * <p>The default {@link Catalog} for this {@link CatalogFactoryBase}.</p>
     */
    private Catalog<K, V, C> catalog = null;

    /**
     * <p>Map of named {@link Catalog}s, keyed by catalog name.</p>
     */
    private final Map<String, Catalog<K, V, C>> catalogs = new ConcurrentHashMap<String, Catalog<K, V, C>>();

    // --------------------------------------------------------- Public Methods

    /**
     * <p>Gets the default instance of Catalog associated with the factory
     * (if any); otherwise, return <code>null</code>.</p>
     *
     * @return the default Catalog instance
     */
    @Override
    public Catalog<K, V, C> getCatalog() {
        return catalog;
    }

    /**
     * <p>Sets the default instance of Catalog associated with the factory.</p>
     *
     * @param catalog the default Catalog instance
     */
    @Override
    public void setCatalog(Catalog<K, V, C> catalog) {
        this.catalog = catalog;
    }

    /**
     * <p>Retrieves a Catalog instance by name (if any); otherwise
     * return <code>null</code>.</p>
     *
     * @param name the name of the Catalog to retrieve
     * @return the specified Catalog
     */
    @Override
    public Catalog<K, V, C> getCatalog(String name) {
        return catalogs.get(name);
    }

    /**
     * <p>Adds a named instance of Catalog to the factory (for subsequent
     * retrieval later).</p>
     *
     * @param name    the name of the Catalog to add
     * @param catalog the Catalog to add
     */
    @Override
    public void addCatalog(String name, Catalog<K, V, C> catalog) {
        catalogs.put(name, catalog);
    }

    /**
     * <p>Return an <code>Iterator</code> over the set of named
     * {@link Catalog}s known to this {@link CatalogFactoryBase}.
     * If there are no known catalogs, an empty Iterator is returned.</p>
     *
     * @return An Iterator of the names of the Catalogs known by this factory.
     */
    @Override
    public Iterator<String> getNames() {
        return catalogs.keySet().iterator();
    }


    public <CMD extends Command<K, V, C>> CMD getCommand(String commandID) {
        String commandName = commandID;
        String catalogName = null;
        Catalog<K, V, C> catalog;

        if (commandID != null) {
            int splitPos = commandID.indexOf(DELIMITER);
            if (splitPos != -1) {
                catalogName = commandID.substring(0, splitPos);
                commandName = commandID.substring(splitPos + DELIMITER.length());
                if (commandName.contains(DELIMITER)) {
                    throw new IllegalArgumentException("commandID [" +
                                                       commandID +
                                                       "] has too many delimiters (reserved for future use)");
                }
            }
        }

        if (catalogName != null) {
            catalog = this.getCatalog(catalogName);
            if (catalog == null) {
                Log log = LogFactory.getLog(CatalogFactoryBase.class);
                log.warn("No catalog found for name: " + catalogName + ".");
                return null;
            }
        } else {
            catalog = this.getCatalog();
            if (catalog == null) {
                Log log = LogFactory.getLog(CatalogFactoryBase.class);
                log.warn("No default catalog found.");
                return null;
            }
        }

        return catalog.<CMD>getCommand(commandName);
    }

    // ------------------------------------------------------- Static Variables

    /**
     * <p>The set of registered {@link CatalogFactoryBase} instances,
     * keyed by the relevant class loader.</p>
     */
    private static final Map<ClassLoader, CatalogFactoryBase<?, ?, ? extends Map<?, ?>>> factories =
            new HashMap<ClassLoader, CatalogFactoryBase<?, ?, ? extends Map<?, ?>>>();

    // -------------------------------------------------------- Static Methods

    /**
     * <p>Return the singleton {@link CatalogFactoryBase} instance
     * for the relevant <code>ClassLoader</code>.  For applications
     * that use a thread context class loader (such as web applications
     * running inside a servet container), this will return a separate
     * instance for each application, even if this class is loaded from
     * a shared parent class loader.</p>
     *
     * @param <K> Context key type
     * @param <V> Context value type
     * @param <C> Type of the context associated with this command
     * @return the per-application singleton instance of {@link CatalogFactoryBase}
     */
    public static <K, V, C extends Map<K, V>> CatalogFactory<K, V, C> getInstance() {
        CatalogFactoryBase<?, ?, ? extends Map<?, ?>> factory;
        ClassLoader cl = getClassLoader();
        synchronized (factories) {
            factory = factories.get(cl);
            if (factory == null) {
                factory = new CatalogFactoryBase<K, V, C>();
                factories.put(cl, factory);
            }
        }

        /* This should always convert cleanly because we are using the
         * base most generic for definition. */
        @SuppressWarnings("unchecked")
        CatalogFactory<K, V, C> result = (CatalogFactory<K, V, C>) factory;

        return result;
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
            cl = getClassLoader();
        }
        return cl;
    }

    /**
     * Check to see if we have an implementation of a valid configuration
     * parsing class loaded at runtime. If not, we throw an
     * IllegalStateException.
     */
    public static void checkForValidConfigurationModule() {
        try {
            ClassLoader cl = getClassLoader();
            cl.loadClass("org.apache.commons.chain2.config.ConfigParser");
        } catch (ClassNotFoundException e) {
            String msg = "Couldn't not find a configuration implementation. " +
                    "Load a chain configuration module such as xml-configuration " +
                    "into the classpath and try again.";
            throw new IllegalStateException(msg, e);
        }
    }

}
