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


import java.util.Iterator;


/**
 * <p>A {@link CatalogFactory} is a class used to store and retrieve
 * {@link Catalog}s.  The factory allows for a default {@link Catalog}
 * as well as {@link Catalog}s stored with a name key.  Follows the
 * Factory pattern (see GoF).</p>
 *
 * @author Sean Schofield 
 * @version $Revision: 1.2 $ $Date: 2004/10/17 01:23:01 $
 */

public interface CatalogFactory {


    /**
     * <p>Gets the default instance of Catalog associated with the factory.</p>
     *
     * @return the default Catalog instance
     */
    public Catalog getCatalog();


    /**
     * <p>Sets the default instance of Catalog associated with the factory.</p>
     *
     * @param catalog the default Catalog instance
     */
    public void setCatalog(Catalog catalog);


    /**
     * <p>Retrieves a Catalog instance by name.</p>
     *
     * @param name the name of the Catalog to retrieve
     * @return the specified Catalog
     */
    public Catalog getCatalog(String name);


    /**
     * <p>Adds a named instance of Catalog to the factory (for subsequent
     * retrieval later).</p>
     *
     * @param name the name of the Catalog to add
     * @param catalog the Catalog to add
     */
    public void addCatalog(String name, Catalog catalog);


    /**
     * <p>Return an <code>Iterator</code> over the set of named
     * {@link Catalog}s known to this {@link CatalogFactory}.
     * If there are no known catalogs, an empty Iterator is returned.</p>
     */
    public Iterator getNames();


}


