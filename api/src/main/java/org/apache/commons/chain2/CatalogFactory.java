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
package org.apache.commons.chain2;

import java.util.Iterator;
import java.util.Map;

/**
 * A <code>CatalogFactory</code> stores and retrieves catalogs. 
 * <p>
 * The factory allows for a default {@link Catalog} as well as Catalog's 
 * stored with a name key. It follows the Factory pattern (see GoF).
 * <p>
 * The base implementation provides command lookup based on a single
 * String. This String encodes both the catalog and command names.
 *
 * @param <K> the type of keys maintained by the context associated with this {@link Command}
 * @param <V> the type of mapped values
 * @param <C> Type of the context associated with this command
 *
 * @version $Id: CatalogFactory.java 1486528 2013-05-27 07:38:38Z simonetripodi $
 */
public interface CatalogFactory<K, V, C extends Map<K, V>> {

    /**
     * <p>Values passed to the <code>getCommand(String)</code> method should
     * use this as the delimiter between the "catalog" name and the "command"
     * name.</p>
     */
    public static final String DELIMITER = ":";

    /**
     * <p>Gets the default instance of Catalog associated with the factory
     * (if any); otherwise, return <code>null</code>.</p>
     *
     * @return the default Catalog instance
     */
    public abstract Catalog<K, V, C> getCatalog();

    /**
     * <p>Sets the default instance of Catalog associated with the factory.</p>
     *
     * @param catalog the default Catalog instance
     */
    public abstract void setCatalog(Catalog<K, V, C> catalog);

    /**
     * <p>Retrieves a Catalog instance by name (if any); otherwise
     * return <code>null</code>.</p>
     *
     * @param name the name of the Catalog to retrieve
     * @return the specified Catalog
     */
    public abstract Catalog<K, V, C> getCatalog(String name);

    /**
     * <p>Adds a named instance of Catalog to the factory (for subsequent
     * retrieval later).</p>
     *
     * @param name the name of the Catalog to add
     * @param catalog the Catalog to add
     */
    public abstract void addCatalog(String name, Catalog<K, V, C> catalog);

    /**
     * <p>Return an <code>Iterator</code> over the set of named
     * {@link Catalog}s known to this instance.
     * If there are no known catalogs, an empty Iterator is returned.</p>
     * @return An Iterator of the names of the Catalogs known by this factory.
     */
    public abstract Iterator<String> getNames();

    /**
     * <p>Return a <code>Command</code> based on the given commandID.</p>
     *
     * <p>At this time, the structure of commandID is relatively simple:  if the
     * commandID contains a DELIMITER, treat the segment of the commandID
     * up to (but not including) the DELIMITER as the name of a catalog, and the
     * segment following the DELIMITER as a command name within that catalog.
     * If the commandID contains no DELIMITER, treat the commandID as the name
     * of a command in the default catalog.</p>
     *
     * <p>To preserve the possibility of future extensions to this lookup
     * mechanism, the DELIMITER string should be considered reserved, and
     * should not be used in command names.  commandID values which contain
     * more than one DELIMITER will cause an
     * <code>IllegalArgumentException</code> to be thrown.</p>
     *
     * @param <CMD> the expected {@link Command} type to be returned
     * @param commandID the identifier of the command to return
     * @return the command located with commandID, or <code>null</code>
     *  if either the command name or the catalog name cannot be resolved
     * @throws IllegalArgumentException if the commandID contains more than
     *  one DELIMITER
     *
     * @since Chain 1.1
     */
    public abstract <CMD extends Command<K, V, C>> CMD getCommand(
            String commandID);

}
