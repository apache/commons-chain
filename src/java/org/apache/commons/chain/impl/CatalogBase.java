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
package org.apache.commons.chain.impl;


import java.util.HashMap;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import org.apache.commons.chain.Catalog;
import org.apache.commons.chain.Command;


/**
 * <p>Simple in-memory implementation of {@link Catalog}.  This class can
 * also be used as the basis for more advanced implementations.</p>
 *
 * <p>This implementation is thread-safe.</p>
 *
 * @author Craig R. McClanahan
 * @author Matthew J. Sgarlata
 * @version $Revision$ $Date$
 */

public class CatalogBase implements Catalog {


    // ----------------------------------------------------- Instance Variables


    /**
     * <p>The map of named {@link Command}s, keyed by name.
     */
    protected Map commands = Collections.synchronizedMap(new HashMap());


    // --------------------------------------------------------- Constructors

    /**
     * Create an empty catalog.
     */
    public CatalogBase() { }

    /**
     * <p>Create a catalog whose commands are those specified in the given <code>Map</code>.
     * All Map keys should be <code>String</code> and all values should be <code>Command</code>.</p>
     *
     * @param commands Map of Commands.
     *
     * @since Chain 1.1
     */
    public CatalogBase( Map commands ) {
        this.commands = Collections.synchronizedMap(commands);
    }

    // --------------------------------------------------------- Public Methods


    /**
     * <p>Add a new name and associated {@link Command}
     * to the set of named commands known to this {@link Catalog},
     * replacing any previous command for that name.
     *
     * @param name Name of the new command
     * @param command {@link Command} to be returned
     *  for later lookups on this name
     */
    public void addCommand(String name, Command command) {

        commands.put(name, command);

    }

    /**
     * <p>Return the {@link Command} associated with the
     * specified name, if any; otherwise, return <code>null</code>.</p>
     *
     * @param name Name for which a {@link Command}
     *  should be retrieved
     * @return The Command associated with the specified name.
     */
    public Command getCommand(String name) {

        return ((Command) commands.get(name));

    }


    /**
     * <p>Return an <code>Iterator</code> over the set of named commands
     * known to this {@link Catalog}.  If there are no known commands,
     * an empty Iterator is returned.</p>
     * @return An iterator of the names in this Catalog.
     */
    public Iterator getNames() {

        return (commands.keySet().iterator());

    }

    /**
     * Converts this Catalog to a String.  Useful for debugging purposes.
     * @return a representation of this catalog as a String
     */
    public String toString() {

        Iterator names = getNames();
        StringBuffer str =
            new StringBuffer("[" + this.getClass().getName() + ": ");

        while (names.hasNext()) {
            str.append(names.next());
            if (names.hasNext()) {
            str.append(", ");
            }
        }
        str.append("]");

        return str.toString();

    }
}
