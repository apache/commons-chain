package org.apache.commons.chain.mailreader;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * Create ActionContextBase pass to Command corresponding to the ActionForm name.
 * On return, analyze Context, returning values in servlet contexts as
 * appropriate. The ActionContextBase is also exposed in the request under
 * the key "context".
 * </p>
 * <p>
 * The Struts ActionMapping should define exception handlers for any documented
 * exceptions thrown by a given Command object.
 * </p>
 * <p>
 * NOTE -- This class may be migrated to a later release of Struts
 * when support for Commons Chains is added.
 * </p>
 */
public class CommandAction extends Action {

    public ActionForward execute(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {

        throw new RuntimeException("TODO: Implement CommandAction");
    }

}
