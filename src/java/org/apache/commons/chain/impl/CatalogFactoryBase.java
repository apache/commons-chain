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

package org.apache.commons.chain.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.commons.chain.Catalog;
import org.apache.commons.chain.CatalogFactory;

/**
 * <p>A simple implementation of {@link CatalogFactory}.</p>
 *
 * @author Sean Schofield 
 * @version $Revision: 1.2 $ $Date: 2004/10/17 22:39:00 $
 */

public class CatalogFactoryBase implements CatalogFactory {
    

    // ------------------------------------------------------- Static Variables
    

    /**
     * <p>The singleton instance.</p>
     */
    private static CatalogFactoryBase instance = new CatalogFactoryBase();


    /**
     * <p>A default value to store an unamed {@link Catalog} in the local 
     * Map.</p>
     */
    private static String DEFAULT_CATALOG =
        "org.apache.commons.chain.DEFAULT_CATALOG";
    

    // ----------------------------------------------------------- Constructors
    
    
    /**
     * <p>Constructs an instance of CatalogFactoryBase.  NOTE: This is private 
     * because it is not meant to be created directly outside of the class.  
     * Use the <code>getInstance()</code> method instead.</p>
     */
    private CatalogFactoryBase() {}
    
    
    // ----------------------------------------------------- Instance Variables
    
    
    /**
     * <p>Map of Maps of {@link Catalog}s, keyed by the object's 
     * ClassLoader.</p>
     */
    protected Map loaderMap = new HashMap();
    
    
    // --------------------------------------------------------- Public Methods
    

    /**
     * <p>Gets the singleton instance of CatalogFactoryBase.</p>
     * 
     * @return the singleton instance of CatalogFactoryBase.
     */
    public static CatalogFactory getInstance() {
        
        return instance;    
        
    }
    

    // Documented in CatalogFactory interface
    public Catalog getCatalog() {
        
        return getCatalog(DEFAULT_CATALOG);        

    }


    // Documented in CatalogFactory interface
    public void setCatalog(Catalog catalog) {
        
        addCatalog(DEFAULT_CATALOG, catalog);
        
    }


    // Documented in CatalogFactory interface
    public Catalog getCatalog(String name) {
        
        /**
         * In the event that this class will be used by multiple applications 
         * with the same class loader (as with web applications), there will 
         * be a separate Map of Catalogs for each instance of 
         * ClassLoader.  That way each web application (which will always have 
         * its own) can operate without interfering with one another.
         */
        Map catalogMap = getCatalogMap();
        if (catalogMap != null) {
            synchronized (catalogMap) {
                return (Catalog) catalogMap.get(name);
            }
        } else {
            return null;
        }

    }


    // Documented in CatalogFactory interface
    public void addCatalog(String name, Catalog catalog) {

        // Retrieve or create the catalog map for the correct class loader
        Map catalogMap = getCatalogMap();
        if (catalogMap == null) {
            catalogMap = new HashMap();
            synchronized (loaderMap) {
                loaderMap.put(getLoaderMapKey(), catalogMap);
            }
        }

        // Add or replace the specified Catalog
        synchronized (catalogMap) {
            catalogMap.put(name, catalog);
        }

    }
    

    // Documented in CatalogFactory interface
    public Iterator getNames() {

        Map catalogMap = getCatalogMap();
        if (catalogMap != null) {
            synchronized (catalogMap) {
                return catalogMap.keySet().iterator();
            }
        } else {
            return Collections.EMPTY_LIST.iterator();
        }

    }


    // Documented in CatalogFactory interface
    public void clear() {

        Map catalogMap = getCatalogMap();
        if (catalogMap != null) {
            synchronized (catalogMap) {
                catalogMap.clear();
            }
        }

    }


    // ------------------------------------------------------- Private Methods


    /**
     * <p>Return the <code>Map</code> containing the relevant
     * {@link Catalog}s for an application, keyed by cataog name.
     * If there is no such <code>Map</code>, return <code>null</code>.</p>
     */
    private Map getCatalogMap() {

        ClassLoader cl = getLoaderMapKey();
        synchronized (loaderMap) {
            return (Map) loaderMap.get(cl);
        }

    }


    /**
     * <p>Return the <code>ClassLoader</code> to serve as the key
     * for a <code>loaderMap</code> entry that consists of the
     * <code>Map</code> of {@link Catalog}s for that class laoder.</p>
     */
    private ClassLoader getLoaderMapKey() {

        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        if (cl == null) { // If context class loader is not defined ...
            cl = this.getClass().getClassLoader(); // Get CL that loaded us
        }
        return cl;

    }


}
