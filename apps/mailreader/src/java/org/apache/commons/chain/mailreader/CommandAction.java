package org.apache.commons.chain.mailreader;

import org.apache.struts.action.*;
import org.apache.commons.chain.Catalog;
import org.apache.commons.chain.Context;
import org.apache.commons.chain.impl.ContextBase;
import org.apache.commons.chain.mailreader.commands.MailReader;
import org.apache.commons.chain.mailreader.commands.MailReaderBase;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

/**
 * <p>
 * Create ActionHelperBase pass to Command corresponding to the ActionForm name.
 * On return, analyze Context, returning values in servlet contexts as
 * appropriate. The ActionHelperBase is also exposed in the request under
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

    /**
     * <p>
     * Return the default {@link org.apache.commons.chain.Catalog}.
     * </p>
     * @return Default Catalog
     * @throws java.lang.UnsupportedOperationException if ControllerContext is
     * null.
     */
    public Catalog getCatalog(HttpServletRequest request){

        return (Catalog) request.getSession().getServletContext().getAttribute(Catalog.CATALOG_KEY);

    }

    public ViewContext getContext(ActionHelper helper, ActionForm form) {

        Locale locale = helper.getLocale();
        Context input = getInput(form);
        return new MailReaderBase(locale,input);

    }


    public Context getInput(ActionForm form) {
        DynaActionForm dyna = (DynaActionForm) form;
        return new ContextBase(dyna.getMap());
    }

    public void conformInput(ActionHelper helper, ViewContext context) {
        Context input = context.getInput();
        ActionMapping mapping = helper.getMapping();
        HttpServletRequest request = helper.getRequest();
        String formScope = mapping.getScope();
        String name = mapping.getName();
        if (helper.isRequestScope()) request.setAttribute(name,input);
        else request.getSession().setAttribute(name,input);
    }

    public void conformState(ActionHelper helper, ViewContext context) {
        helper.setLocale(context.getLocale());
    }

    public ActionForward findSuccess(ActionMapping mapping) {
        return mapping.findForward("success");
    }

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        // create ActionHelper
        ActionHelper helper = new ActionHelperBase(request,response);
        // return execute(helper); // ContextAction

        // TODO: obtain database reference
        // forward = preCommand(helper); if (forward!=null) return forward;

        // create mailreader context, using ActionHelper methods
        ViewContext context = getContext(helper,form);

        // execute command
        String name = mapping.getName();
        boolean stop = getCatalog(request).getCommand(name).execute(context);

        // update state from mailreader context
        conformInput(helper,context);
        conformState(helper,context);

        // TODO: Expose any output
        // TODO: Expose any status messages,
        // TODO: Expose any error messages and find input
        // location = postCommand(helper,context);

        // find success
        return helper.findSuccess();
    }

    // ModuleContext -> state for a module (mappings, messages) : ReadOnly
    // ClientContext -> runtime input for this request/client (locale,form) : ReadWrite
    // ActionContext -> ClientState, ModuleState

    // StrutsContext -> state for entire Struts application : ReadWrite
    // StrutsContext.createActionContext(request);
}
