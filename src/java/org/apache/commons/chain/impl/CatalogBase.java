/*
 * $Header: /home/jerenkrantz/tmp/commons/commons-convert/cvs/home/cvs/jakarta-commons//chain/src/java/org/apache/commons/chain/impl/CatalogBase.java,v 1.6 2003/10/12 09:10:55 rdonkin Exp $
 * $Revision: 1.6 $
 * $Date: 2003/10/12 09:10:55 $
 *
 * ====================================================================
 *
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 1999-2003 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution, if
 *    any, must include the following acknowledgement:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowledgement may appear in the software itself,
 *    if and wherever such third-party acknowledgements normally appear.
 *
 * 4. The names "The Jakarta Project", "Commons", and "Apache Software
 *    Foundation" must not be used to endorse or promote products derived
 *    from this software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache"
 *    nor may "Apache" appear in their names without prior written
 *    permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

package org.apache.commons.chain.impl;


import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.commons.chain.Catalog;
import org.apache.commons.chain.Chain;
import org.apache.commons.chain.Command;


/**
 * <p>Simple in-memory implementation of {@link Catalog}.  This class can
 * also be used as the basis for more advanced implementations.</p>
 *
 * @author Craig R. McClanahan
 * @author Matthew J. Sgarlata
 * @version $Revision: 1.6 $ $Date: 2003/10/12 09:10:55 $
 */

public class CatalogBase implements Catalog {


    // ----------------------------------------------------- Instance Variables


    /**
     * <p>The map of named {@link Command}s, keyed by name.
     */
    protected Map commands = new HashMap();


    // --------------------------------------------------------- Public Methods


    // Documented in Catalog interface
    public void addCommand(String name, Command command) {

        commands.put(name, command);

    }

    // Documented in Catalog interface
    public Command getCommand(String name) {

        return ((Command) commands.get(name));

    }


    // Documented in Catalog interface
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
