/*
 * $Header: /home/jerenkrantz/tmp/commons/commons-convert/cvs/home/cvs/jakarta-commons//chain/apps/mailreader/src/java/org/apache/commons/chain/mailreader/Attic/ContextAction.java,v 1.1 2004/03/28 03:20:55 husted Exp $
 * $Revision: 1.1 $
 * $Date: 2004/03/28 03:20:55 $
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
package org.apache.commons.chain.mailreader;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * Create ActionContext from standard execute call and pass to a context
 * form of execute, to be extended by a subclass.
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
     * Process the request represented by the ActionHelper, and return an
     * ActionForward representing the resource that will create the corresonding
     * response (or create the response directly and reutrn null), with provision
     * for handling exceptions thrown by the business logic.
     * </p>
     * @param helper The ActionHelper we are processing
     * @exception Exception if the application business logic throws
     *  an exception
     */
    public abstract ActionForward execute(ActionHelper helper) throws Exception;


    /**
     * <p>
     * Create ActionHelper and return result of execute(ActionHelper).
     * Subclasses are expected to implement the execute(ActionHelper)
     * and provide the needed functionality there.
     * </p>
     * @param mapping The ActionMapping used to select this instance
     * @param form The optional ActionForm bean for this request (if any)
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     * @exception Exception if the application business logic throws
     *  an exception
     */
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        ActionHelper helper = new ActionHelperBase(request,response);
        return execute(helper);

    }

}
