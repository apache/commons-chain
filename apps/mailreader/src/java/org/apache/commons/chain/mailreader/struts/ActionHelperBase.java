/*
 * $Header: /home/jerenkrantz/tmp/commons/commons-convert/cvs/home/cvs/jakarta-commons//chain/apps/mailreader/src/java/org/apache/commons/chain/mailreader/struts/Attic/ActionHelperBase.java,v 1.2 2004/03/29 02:34:19 husted Exp $
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

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.chain.Catalog;
import org.apache.commons.chain.Context;
import org.apache.struts.Globals;
import org.apache.struts.action.*;
import org.apache.struts.upload.MultipartRequestWrapper;
import org.apache.struts.util.MessageResources;
import org.apache.struts.util.RequestUtils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.util.Locale;

/**
 * <p>
 * NOTE -- This implementation was designed to work with the
 * default module only. Many methods won't work with modular
 * applications.
 * </p>
 * <p>
 * NOTE -- In the next version, a "ClientContext" interface
 * and implementation will be added to this class to allow
 * access to operations business Commands might use without
 * exposing Http signatures or other implementation details.
 * </p>
 * <p>
 * NOTE -- At some point, a "disconnected" implementation should be
 * available so that the Http resources do not have be cached
 * as class members.
 * </p>
 * <p>
 * NOTE -- This class may be migrated to a later release of Struts
 * when support for Commons Chains is added.
 * </p>
 */
public class ActionHelperBase implements ActionHelper {

// --------------------------------------------------------  Properties

    /**
     * The application associated with this instance.
     */
    private ServletContext application = null;

    /**
     * Set the application associated with this instance.
     * [servlet.getServletContext()]
     */
    public void setApplication(ServletContext application) {
        this.application = application;
    }

    /**
     * The session associated with this instance.
     */
    private HttpSession session = null;

    /**
     * Set the session associated with this instance.
     */
    public void setSession(HttpSession session) {
        this.session = session;
    }

    /**
     * The request associated with this instance.
     */
    private HttpServletRequest request = null;

    /**
     * Set the request associated with this object.
     * Session object is also set or cleared.
     */
    public void setRequest(HttpServletRequest request) {
        this.request = request;
        if (this.request == null)
            setSession(null);
        else
            setSession(this.request.getSession());
    }

    /**
     * The response associated with this instance.
     */
    private HttpServletResponse response = null;

    /**
     * Set the response associated with this isntance.
     * Session object is also set or cleared.
     */
    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }

    /**
     * The forward associated with this instance.
     */
    private ActionForward forward = null;

    /**
     * Set the forward associated with this instance.
     */
    public void setForward(ActionForward forward) {
        this.forward = forward;
    }

    /**
     * Set the application and request for this object instance.
     * The ServletContext can be set by any servlet in the application.
     * The request should be the instant request.
     * Most of the other methods retrieve their own objects
     * by reference to the application, request, or session
     * attributes.
     * Do not call other methods without setting these first!
     * This is also called by the convenience constructor.
     *
     * @param request - The associated HTTP request.
     * @param response - The associated HTTP response.
     */
    public void setResources(
            HttpServletRequest request,
            HttpServletResponse response) {

        ServletContext application = request.getSession().getServletContext();
        setApplication(application);
        setRequest(request);
        setResponse(response);
    }

    public ActionHelperBase() {
        super();
    }

    public ActionHelperBase(
            HttpServletRequest request,
            HttpServletResponse response) {

        super();
        this.setResources(request, response);
    }


    // ------------------------------------------------ Application Context

    // See ActionHelper interface for JavaDoc
    public DataSource getDataSource() {

        if (this.application == null)
            return null;
        return (DataSource) this.application.getAttribute(Globals.DATA_SOURCE_KEY);

    }

    // See ActionHelper interface for JavaDoc
    public ActionMessages getActionErrors() {

        if (this.application == null)
            return null;
        return (ActionMessages) this.application.getAttribute(Globals.ERROR_KEY);

    }

    // See ActionHelper interface for JavaDoc
    public ActionMessages getActionMessages() {

        if (this.application == null)
            return null;
        return (ActionMessages) this.application.getAttribute(Globals.MESSAGE_KEY);

    }

    // See ActionHelper interface for JavaDoc
    public MessageResources getMessageResources() {

        if (this.application == null) {
            return null;
        }
        return (MessageResources) this.application.getAttribute(Globals.MESSAGES_KEY);

    }

    // See ActionHelper interface for JavaDoc
    public String getServletMapping() {

        if (this.application == null) {
            return null;
        }
        return (String) this.application.getAttribute(Globals.SERVLET_KEY);

    }

    // ---------------------------------------------------- Session Context

    // See ActionHelper interface for JavaDoc
    public Locale getLocale() {

        if (this.session == null) {
            return null;
        }
        return (Locale) session.getAttribute(Globals.LOCALE_KEY);

    }

    // See ActionHelper interface for JavaDoc
    public void setLocale(Locale locale) {

        session.setAttribute(Globals.LOCALE_KEY, locale);

    }

    // See ActionHelper interface for JavaDoc
    public String getToken() {

        if (this.session == null) {
            return null;
        }
        return (String) session.getAttribute(Globals.TRANSACTION_TOKEN_KEY);

    }

    // ---------------------------------------------------- Request Context

    // See ActionHelper interface for JavaDoc
    public Throwable getException() {

        if (this.request == null) {
            return null;
        }
        return (Throwable) this.request.getAttribute(Globals.EXCEPTION_KEY);

    }

    // See ActionHelper interface for JavaDoc
    public MultipartRequestWrapper getMultipartRequestWrapper() {

        if (this.request == null) {
            return null;
        }
        return (MultipartRequestWrapper) this.request.getAttribute(Globals.MULTIPART_KEY);
    }

    // See ActionHelper interface for JavaDoc
    public ActionMapping getMapping() {

        if (this.request == null) {
            return null;
        }
        return (ActionMapping) this.request.getAttribute(Globals.MAPPING_KEY);

    }

    // ---------------------------------------------------- Utility Methods

    // See ActionHelper interface for JavaDoc
    public boolean isMessage(String key) {

        // Look up the requested MessageResources
        MessageResources resources = getMessageResources();

        if (resources == null) {
            return false;
        }

        // Return the requested message presence indicator
        return resources.isPresent(RequestUtils.getUserLocale(request, null), key);

    }

    // See ActionHelper interface for JavaDoc
    public ActionForm getActionForm() {

        // Is there a mapping associated with this request?
        ActionMapping mapping = getMapping();
        if (mapping == null)
            return (null);

        // Is there a form bean associated with this mapping?
        String attribute = mapping.getAttribute();
        if (attribute == null)
            return (null);

        // Look up the existing form bean, if any
        ActionForm instance = null;
        if ("request".equals(mapping.getScope())) {
            instance = (ActionForm) this.request.getAttribute(attribute);
        } else {
            instance = (ActionForm) this.session.getAttribute(attribute);
        }

        return instance;
    }

    // See ActionHelper interface for JavaDoc
    // TODO:
    public ActionFormBean getFormBean(String name) {
        return null;
    }


    // See ActionHelper interface for JavaDoc
    // TODO:
    public ActionForward getActionForward(String name) {
        return null;
    }

    // See ActionHelper interface for JavaDoc
    // TODO:
    public ActionMapping getActionMapping(String path) {
        return null;
    }

    // See ActionHelper interface for JavaDoc
    public String getActionMappingName(String action) {

        String value = action;
        int question = action.indexOf("?");
        if (question >= 0)
            value = value.substring(0, question);
        int slash = value.lastIndexOf("/");
        int period = value.lastIndexOf(".");
        if ((period >= 0) && (period > slash))
            value = value.substring(0, period);
        if (value.startsWith("/"))
            return (value);
        else
            return ("/" + value);

    }

    // See ActionHelper interface for JavaDoc
    public String getActionMappingURL(String action) {

        StringBuffer value = new StringBuffer(this.request.getContextPath());

        // Use our servlet mapping, if one is specified
        String servletMapping = getServletMapping();

        if (servletMapping != null) {
            String queryString = null;
            int question = action.indexOf("?");
            if (question >= 0)
                queryString = action.substring(question);
            String actionMapping = getActionMappingName(action);
            if (servletMapping.startsWith("*.")) {
                value.append(actionMapping);
                value.append(servletMapping.substring(1));
            } else if (servletMapping.endsWith("/*")) {
                value.append(servletMapping.substring(0, servletMapping.length() - 2));
                value.append(actionMapping);
            }
            if (queryString != null)
                value.append(queryString);
        }

        // Otherwise, assume extension mapping is in use and extension is
        // already included in the action property
        else {
            if (!action.startsWith("/"))
                value.append("/");
            value.append(action);
        }

        // Return the completed value
        return (value.toString());

    }

    // See ActionHelper interface for JavaDoc
    public String getEncodeURL(String url) {

        if ((session != null) && (response != null)) {

            boolean redirect = false;
            if (forward != null)
                redirect = forward.getRedirect();

            if (redirect)
                return response.encodeRedirectURL(url);
            else
                return response.encodeURL(url);
        } else
            return (url);
    }

    // See ActionHelper interface for JavaDoc
    public Object getAttribute(String key) {
        Object o = request.getAttribute(key);
        if (null==o) o = session.getAttribute(key);
        if (null==o) o = application.getAttribute(key);
        return o;
    }

    // ------------------------------------------------ Presentation API

    // See ActionHelper interface for JavaDoc
    public String getOrigRef() {

        // HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();

        if (request == null)
            return null;
        StringBuffer result = RequestUtils.requestToServerUriStringBuffer(request);
        return result.toString();
    }

    // See ActionHelper interface for JavaDoc
    public String getBaseRef() {

        if (request == null)
            return null;

        StringBuffer result = RequestUtils.requestToServerStringBuffer(request);
        String path = null;
        if (forward == null)
            path = request.getRequestURI();
        else
            path = request.getContextPath() + forward.getPath();
        result.append(path);

        return result.toString();
    }

    // See ActionHelper interface for JavaDoc
    public String getLink(String name) {

        ActionForward forward = getActionForward(name);
        if (forward == null)
            return null;

        StringBuffer path = new StringBuffer(this.request.getContextPath());
        path.append(forward.getPath());

        // TODO: What about runtime parameters?

        return getEncodeURL(path.toString());

    }

    // See ActionHelper interface for JavaDoc
    public String getMessage(String key) {

        MessageResources resources = getMessageResources();
        if (resources == null)
            return null;

        return resources.getMessage(RequestUtils.getUserLocale(request, null), key);

    }

    // See ActionHelper interface for JavaDoc
    public String getMessage(String key, Object args[]) {

        MessageResources resources = getMessageResources();

        if (resources == null)
            return null;

        // Return the requested message
        if (args == null)
            return resources.getMessage(
                    RequestUtils.getUserLocale(request, null),
                    key);
        else
            return resources.getMessage(
                    RequestUtils.getUserLocale(request, null),
                    key,
                    args);

    }

    // See ActionHelper interface for JavaDoc
    public String getAction(String path) {
        return getEncodeURL(getActionMappingURL(path));
    }

    // ------------------------------------------------- Catalog / Context

    // See ActionHelper interface for JavaDoc
    public Catalog getCatalog() {

        return (Catalog) application.getAttribute("catalog");

    }

    // See ActionHelper interface for JavaDoc
    public void setInputAsForm(Context input) {
        ActionMapping mapping = getMapping();
        String formScope = mapping.getScope();
        String name = mapping.getName();
        if ("request".equals(formScope))
            request.setAttribute(name, input);
        else
            request.getSession().setAttribute(name, input);
    }

    // See ActionHelper interface for JavaDoc
    public void setInputToForm(Context input) {
        ActionForm form = getActionForm();
        try {
            BeanUtils.copyProperties(form, input);
        } catch (Throwable t) {
            // FIXME: Now what? Log and Throw?
        }
    }

    // --------------------------------------------- Presentation Wrappers


    /**
     * <p>Wrapper for getLink(String)</p>
     * @param name Name given to local or global forward.
     */
    public String link(String name) {
        return getLink(name);
    }

    /**
     * <p>Wrapper for getMessage(String)</p>
     * @param key Message key
     */
    public String message(String key) {
        return getMessage(key);
    }

    /**
     * <p>Wrapper for getMessage(String,Object[])</p>
     * @param key Message key to be looked up and returned
     * @param args Replacement parameters for this message
     */
    public String message(String key, Object args[]) {
        return getMessage(key, args);
    }

    /**
     * <p>Wrapper for getAction(String)</p>
     * @param path Name given to local or global forward.
     */
    public String action(String path) {
        return getAction(path);
    }

}
