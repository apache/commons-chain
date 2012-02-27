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
package org.apache.commons.chain2.web;


import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.ServletContext;

import org.apache.commons.chain2.Catalog;
import org.apache.commons.chain2.config.ConfigParser;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * <p>Utility methods for loading class loader and web application resources
 * to configure a {@link Catalog}.  These methods are shared between
 * <code>ChainListener</code> and <code>ChainServlet</code>.</p>
 *
 * @author Craig R. McClanahan
 * @author Ted Husted
 */

final class ChainResources {

    /**
     * This class uses a private constructor because it is a utility class.
     */
    private ChainResources() {
    }


    // ---------------------------------------------------------- Static Methods


    /**
     * <p>Parse the specified class loader resources.</p>
     *
     * @param resources Comma-delimited list of resources (or <code>null</code>)
     * @param parser {@link ConfigParser} to use for parsing
     */
    static void parseClassResources(String resources,
                                    ConfigParser parser) {

        if (resources == null) {
            return;
        }
        Log log = LogFactory.getLog(ChainResources.class);
        ClassLoader loader =
            Thread.currentThread().getContextClassLoader();
        if (loader == null) {
            loader = ChainResources.class.getClassLoader();
        }
        String[] paths = getResourcePaths(resources);
        String path = null;
        try {
            for (int i = 0; i < paths.length; i++) {
                path = paths[i];
                URL url = loader.getResource(path);
                if (url == null) {
                    throw new IllegalStateException
                        ("Missing chain config resource '" + path + "'");
                }
                if (log.isDebugEnabled()) {
                    log.debug("Loading chain config resource '" + path + "'");
                }
                parser.parse(url);
            }
        } catch (Exception e) {
            throw new RuntimeException
                ("Exception parsing chain config resource '" + path + "': "
                 + e.getMessage());
        }

    }


    /**
     * <p>Parse the specified class loader resources.</p>
     *
     * @param catalog {@link Catalog} we are populating
     * @param resources Comma-delimited list of resources (or <code>null</code>)
     * @param parser {@link ConfigParser} to use for parsing
     *
     * @deprecated Use the variant that does not take a catalog, on a
     *  configuration resource containing "catalog" element(s)
     */
    @Deprecated
    static void parseClassResources(Catalog catalog, String resources,
                                    ConfigParser parser) {

        if (resources == null) {
            return;
        }
        Log log = LogFactory.getLog(ChainResources.class);
        ClassLoader loader =
            Thread.currentThread().getContextClassLoader();
        if (loader == null) {
            loader = ChainResources.class.getClassLoader();
        }
        String[] paths = getResourcePaths(resources);
        String path = null;
        try {
            for (int i = 0; i < paths.length; i++) {
                path = paths[i];
                URL url = loader.getResource(path);
                if (url == null) {
                    throw new IllegalStateException
                        ("Missing chain config resource '" + path + "'");
                }
                if (log.isDebugEnabled()) {
                    log.debug("Loading chain config resource '" + path + "'");
                }
                parser.parse(catalog, url);
            }
        } catch (Exception e) {
            throw new RuntimeException
                ("Exception parsing chain config resource '" + path + "': "
                 + e.getMessage());
        }

    }


    /**
     * <p>Parse the specified web application resources.</p>
     *
     * @param context <code>ServletContext</code> for this web application
     * @param resources Comma-delimited list of resources (or <code>null</code>)
     * @param parser {@link ConfigParser} to use for parsing
     */
    static void parseWebResources(ServletContext context,
                                  String resources,
                                  ConfigParser parser) {

        if (resources == null) {
            return;
        }
        Log log = LogFactory.getLog(ChainResources.class);
        String[] paths = getResourcePaths(resources);
        String path = null;
        try {
            for (int i = 0; i < paths.length; i++) {
                path = paths[i];
                URL url = context.getResource(path);
                if (url == null) {
                    throw new IllegalStateException
                        ("Missing chain config resource '" + path + "'");
                }
                if (log.isDebugEnabled()) {
                    log.debug("Loading chain config resource '" + path + "'");
                }
                parser.parse(url);
            }
        } catch (Exception e) {
            throw new RuntimeException
                ("Exception parsing chain config resource '" + path + "': "
                 + e.getMessage());
        }

    }


    /**
     * <p>Parse the specified web application resources.</p>
     *
     * @param catalog {@link Catalog} we are populating
     * @param context <code>ServletContext</code> for this web application
     * @param resources Comma-delimited list of resources (or <code>null</code>)
     * @param parser {@link ConfigParser} to use for parsing
     *
     * @deprecated Use the variant that does not take a catalog, on a
     *  configuration resource containing "catalog" element(s)
     */
    @Deprecated
    static void parseWebResources(Catalog catalog, ServletContext context,
                                  String resources,
                                  ConfigParser parser) {

        if (resources == null) {
            return;
        }
        Log log = LogFactory.getLog(ChainResources.class);
        String[] paths = getResourcePaths(resources);
        String path = null;
        try {
            for (int i = 0; i < paths.length; i++) {
                path = paths[i];
                URL url = context.getResource(path);
                if (url == null) {
                    throw new IllegalStateException
                        ("Missing chain config resource '" + path + "'");
                }
                if (log.isDebugEnabled()) {
                    log.debug("Loading chain config resource '" + path + "'");
                }
                parser.parse(catalog, url);
            }
        } catch (Exception e) {
            throw new RuntimeException
                ("Exception parsing chain config resource '" + path + "': "
                 + e.getMessage());
        }

    }


    /**
     * <p> Parse the resource string into an array of paths. Empty entries will
     * be skipped. (That is, all entries in the array are non-empty paths.)</p>
     *
     * @param resources A comma-delimited list of resource paths (or
     *                  <code>null</code>).
     *
     * @return An array of non-empty paths. The array itself may be empty.
     *
     * @since Chain 1.1
     */
    static String[] getResourcePaths(String resources) {
        if (resources == null || resources.length() == 0) {
            return new String[0];
        }

        StringTokenizer resourcesTokenizer = new StringTokenizer(resources, ",");
        List<String> paths = new ArrayList<String>(resourcesTokenizer.countTokens());

        while (resourcesTokenizer.hasMoreTokens()) {
            String path = resourcesTokenizer.nextToken().trim();
            if (path.length() > 0) {
                paths.add(path);
            }
        }

        return paths.toArray(new String[paths.size()]);
    }


}
