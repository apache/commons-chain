/*
 * $Header: /home/jerenkrantz/tmp/commons/commons-convert/cvs/home/cvs/jakarta-commons//chain/src/java/org/apache/commons/chain/impl/Attic/ContextMap2.java,v 1.2 2003/09/27 18:15:12 craigmcc Exp $
 * $Revision: 1.2 $
 * $Date: 2003/09/27 18:15:12 $
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
 *    any, must include the following acknowlegement:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowlegement may appear in the software itself,
 *    if and wherever such third-party acknowlegements normally appear.
 *
 * 4. The names "The Jakarta Project", "Commons", and "Apache Software
 *    Foundation" must not be used to endorse or promote products derived
 *    from this software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache"
 *    nor may "Apache" appear in their names without prior written
 *    permission of the Apache Group.
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

import java.util.Map;
import java.util.Set;
import java.util.Collection;


/**
 * <p>Simple extension of {@link ContextBase2} class to provide a Map interface to the attributes Map.</p>
 *
 * <p><em>WARNING: This is an EXPERIMENTAL class and may be removed at
 * any time.</em></p>
 *
 * @author Ted Husted
 * @version $Revision: 1.2 $ $Date: 2003/09/27 18:15:12 $
 */

public class ContextMap2 extends ContextBase2 implements Map {

    // -- Map methods (delegate to getAttributes)
    // -- Could (should?) use getAttributes() here, but attributes was protected, so ..

    public int size() {
        return attributes.size();
    }

    public boolean isEmpty() {
        return attributes.isEmpty();
    }

    public boolean containsKey(Object key) {
        return attributes.containsKey(key);
    }

    public boolean containsValue(Object value) {
        return attributes.containsValue(value);
    }

    public Object get(Object key) {
        return attributes.get(key);
    }

    public Object put(Object key, Object value) {
        return attributes.put(key,value);
    }

    public Object remove(Object key) {
        return attributes.remove(key);
    }

    public void putAll(Map t) {
        attributes.putAll(t);
    }

    public void clear() {
        attributes.clear();
    }

    public Set keySet() {
        return attributes.keySet();
    }

    public Collection values() {
        return attributes.values();
    }

    public Set entrySet() {
        return attributes.entrySet();
    }


}
