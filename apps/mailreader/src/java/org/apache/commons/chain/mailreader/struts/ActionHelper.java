/*
 * $Header: /home/jerenkrantz/tmp/commons/commons-convert/cvs/home/cvs/jakarta-commons//chain/apps/mailreader/src/java/org/apache/commons/chain/mailreader/struts/Attic/ActionHelper.java,v 1.2 2004/03/29 02:34:19 husted Exp $
 * $Revision: 1.2 $
 * $Date: 2004/03/29 02:34:19 $
 *
 * Copyright 1999-2004 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.chain.mailreader.struts;


import org.apache.commons.chain.Catalog;
import org.apache.commons.chain.Context;
import org.apache.struts.action.*;
import org.apache.struts.upload.MultipartRequestWrapper;
import org.apache.struts.util.MessageResources;

import javax.sql.DataSource;
import java.util.Locale;


/**
 * <p>
 * [This class is based on the experimental ConfigHelper class,
 * and is under active development.]
 * <p>
 * A Context exposing the Struts shared resources,
 * which are be stored in the application, session, or
 * request contexts, as appropriate.
 * </p>
 * <p>
 * An instance should be created for each request
 * processed. The  methods which return resources from
 * the request or session contexts are not thread-safe.
 * </p>
 * <p>
 * Provided for use by Commands and other components in the
 * application so they can easily access the various Struts
 * shared resources. These resources may be stored under
 * attributes in the application, session, or request contexts,
 * but users of this class do not need to know what goes where.
 * </p>
 * <p>
 * The ActionHelperBase methods simply return the resources
 * from under the context and key used by the Struts
 * ActionServlet when the resources are created.
 * </p>
 */
public interface ActionHelper {


// ------------------------------------------------ Application Context

    /**
     * <p>
     * The default DataSource instance registered with the
     * ActionServlet, or <code>null</code> is none is registered.
     * </p>
     * @return The default DataSource instance or null
     */
    public DataSource getDataSource();

    /**
     * <p>
     * The collection of error messages for this request,
     * or null if none are available.
     * </p>
     */
    public ActionMessages getActionErrors();

    /**
     * <p>
     * The collection of general messages for this request,
     * or null if none are available.
     * </p>
     */
    public ActionMessages getActionMessages();

    /**
     * <p>The application resources for this application.</p>
     */
    public MessageResources getMessageResources();

    /**
     * <p>
     * The path-mapped pattern (<code>/action/*</code>) or
     * extension mapped pattern ((<code>*.do</code>)
     * used to determine our Action URIs in this application.
     * </p>
     */
    public String getServletMapping();


// ---------------------------------------------------- Session Context

    /**
     * <p>
     * Return the Locale associated with this client, or null if none.
     * </p>
     */
    public Locale getLocale();

    /**
     * <p>
     * Assign the Locale to associate with this client.
     * </p>
     */
    public void setLocale(Locale locale);

    /**
     * <p>
     * The transaction token stored in this session, if it is used.
     * </p>
     */
    public String getToken();


// ---------------------------------------------------- Request Context

    /**
     * <p>
     * The runtime JspException that may be been thrown by a Struts tag
     * extension, or compatible presentation extension, and placed
     * in the request.
     * </p>
     */
    public Throwable getException();


    /**
     * <p>The multipart object for this request.</p>
     */
    public MultipartRequestWrapper getMultipartRequestWrapper();


    /**
     * <p>
     * The <code>org.apache.struts.ActionMapping</code>
     * instance for this request.
     * </p>
     */
    public ActionMapping getMapping();


// ---------------------------------------------------- Utility Methods

    /**
     * <p>
     * Return true if a message string for the specified message key
     * is present for the clients's Locale.
     * </p>
     * @param key Message key
     */
    public boolean isMessage(String key);


    /**
     * <p>
     * Retrieve and return the <code>ActionForm</code> instance associated with
     * this mapping. If there is no ActionForm present, return <code>null</code>.
     * </p>
     */
    public ActionForm getActionForm();


    /**
     * <p>
     * Return the form bean definition associated with the specified
     * logical name, if any; otherwise return <code>null</code>.
     * </p>
     * @param name Logical name of the requested form bean definition
     */
    public ActionFormBean getFormBean(String name);


    /**
     * <p>
     * Return the forwarding associated with the specified logical name,
     * if any; otherwise return <code>null</code>.
     * </p>
     * @param name Logical name of the requested forwarding
     */
    public ActionForward getActionForward(String name);


    /**
     * <p>
     * Return the mapping associated with the specified request path, if any;
     * otherwise return <code>null</code>.
     * </p>
     * @param path Request path for which a mapping is requested
     */
    public ActionMapping getActionMapping(String path);


    /**
     * <p>
     * Return the form action converted into an action mapping path.  The
     * value of the <code>action</code> property is manipulated as follows in
     * computing the name of the requested mapping:
     * </p>
     * <ul>
     * <li>
     * Any filename extension is removed (on the theory that extension
     * mapping is being used to select the controller servlet).
     * </li>
     * <li>
     * If the resulting value does not start with a slash, then a
     * slash is prepended.
     * </li>
     * </ul>
     * <p>FIXME: Bad assumption =:o)</p>
     */
    public String getActionMappingName(String action);


    /**
     * <p>
     * Return the form action converted into a server-relative URL.
     * </p>
     */
    public String getActionMappingURL(String action);


    /**
     * <p>
     * Return the url encoded to maintain the user session, if any.
     * </p>
     */
    public String getEncodeURL(String url);


    /**
     * <p>Return attribute corresponding to key, searching request,
     * session, and application, respetively, or null if not found
     * in any scope.</p>
     * @param key Attribute name
     * @return Corresponding attribute or null
     */
    public Object getAttribute(String key);


// ----------------------------------------------- Catalog / Context

    /**
     * <p>Returns the default Command Catalog, if any.</p>
     * @return the default Command Catalog.
     */
    public Catalog getCatalog();


    /**
     * <p>
     * Replace the ActionForm instance with an Input context.
     * Useful in JSTL or Velocity environments.
     * </p>
     * @param input Input Context
     */
    public void setInputAsForm(Context input);


    /**
     * <p>
     * Copy input context attributes over matching ActionForm properties.
     * </p>
     * @param input Input Context
     */
    public void setInputToForm(Context input);


// ------------------------------------------------ Presentation API

    /**
     * <p>Renders the reference for a HTML base element.</p>
     */
    public String getOrigRef();


    /**
     * <p>Renders the reference for a HTML <base> element.</p>
     */
    public String getBaseRef();


    /**
     * <p>
     * Return the path for the specified forward,
     * otherwise return <code>null</code>.
     * </p>
     * @param name Name given to local or global forward.
     */
    public String getLink(String name);


    /**
     * <p>
     * Return the localized message for the specified key,
     * otherwise return <code>null</code>.
     * </p>
     * @param key Message key
     */
    public String getMessage(String key);


    /**
     * <p>
     * Look up and return a message string, based on the specified parameters.
     * </p>
     * @param key Message key to be looked up and returned
     * @param args Replacement parameters for this message
     */
    public String getMessage(String key, Object args[]);


    /**
     * <p>
     * Return the URL for the specified ActionMapping,
     * otherwise return <code>null</code>.
     * </p>
     * @param path Name given to local or global forward.
     */
    public String getAction(String path);

}
