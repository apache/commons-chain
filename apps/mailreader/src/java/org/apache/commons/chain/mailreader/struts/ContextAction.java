/*
 * $Header: /home/jerenkrantz/tmp/commons/commons-convert/cvs/home/cvs/jakarta-commons//chain/apps/mailreader/src/java/org/apache/commons/chain/mailreader/struts/Attic/ContextAction.java,v 1.1 2004/03/29 00:52:09 husted Exp $
 * $Revision: 1.1 $
 * $Date: 2004/03/29 00:52:09 $
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

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * Create ActionHelper from standard <code>execute</code> call
 * and pass to a helper form of <code>execute</code>,
 * to be extended by a subclass.
 * </p>
 */
public abstract class ContextAction extends Action {

    /**
     * <p>
     * Token representing a nominal outcome ["success"].
     * </p>
     */
    private static String SUCCESS = "success";


    /**
     * <p>
     * Convenience method to find a forward named "success".
     * </p>
     * @param helper Our ActionHelper
     * @return a forward named "success" or null.
     */
    protected ActionForward findSuccess(ActionHelper helper) {
        return helper.getMapping().findForward(SUCCESS);
    }


    /**
     * <p>
     * Convenience method to return the Input forward.
     * Assumes the InputForward option and input property is set.
     * Otherwise, returns <code>null</code>.
     * </p>
     * @param helper Our ActionHelper
     * @return a forward named "success" or null.
     */
    protected ActionForward findInput(ActionHelper helper) {
        return helper.getMapping().getInputForward();
    }


    /**
     * <p>
     * Process the request represented by the {@link ActionHelper}, and return an
     * {@link ActionForward} representing the resource that will create the
     * corresonding response (or create the response directly and return null).
     * Exception-handling can be managed here or through the Struts configuration,
     * the Struts configuration being preferred.
     * </p>
     * @param helper The ActionHelper we are processing
     * @exception java.lang.Exception if the application business logic throws
     *  an exception
     */
    public abstract ActionForward execute(ActionHelper helper) throws Exception;


    /**
     * <p>
     * Create {@link ActionHelper} and return result of <code>execute(ActionHelper)</code>.
     * Concrete subclasses must implement <code>execute(ActionHelper)</code>.
     * See {@link CommandAction} for an example.
     * </p>
     * @param mapping The ActionMapping used to select this instance
     * @param form The optional ActionForm bean for this request (if any)
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     * @exception java.lang.Exception if the application business logic throws
     *  an exception
     */
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        ActionHelper helper = new ActionHelperBase(request, response);
        return execute(helper);

    }

}
