package org.apache.commons.chain.mailreader;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.chain.Catalog;
import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;
import org.apache.commons.chain.impl.ContextBase;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import java.util.HashMap;
import java.util.Map;

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
public abstract class CommandAction extends ContextAction {

    /**
     * <p>
     * Return the relevant command from the default
     * {@link org.apache.commons.chain.Catalog}.
     * </p>
     * @return Command for this helper
     */
    protected Command getCatalogCommand(ActionHelper helper){

        Catalog catalog = helper.getCatalog();
        String name = helper.getMapping().getName();
        return catalog.getCommand(name);

    }

    /**
     * <p>
     * Return the client context for this application.
     * Must be implemented by a subclass.
     * </p>
     * @param helper
     * @return
     */
    protected abstract ClientContext getContext(ActionHelper helper);

    /**
     * <p>
     * Operations to perform prior to executing business command.
     * </p>
     * @param helper Our ActionHelper
     * @param context Our ClientContext
     * @return ActionForward to follow, or null
     */
    protected ActionForward preExecute(ActionHelper helper, ClientContext context) {
        // override to provide functionality
        return null;
    }

    /**
     * <p>Convert ActionForm to Chain Context.</p>
     * @param form
     * @return
     */
    protected Context getInput(ActionForm form) {

        Map input;
        if (form instanceof DynaActionForm) {
            DynaActionForm dyna = (DynaActionForm) form;
            input = dyna.getMap();
        }
        else try {
            input = BeanUtils.describe(form);
        } catch (Throwable t) {
            input = new HashMap();
        }
        return new ContextBase(input);

    }

    /**
     * <p>Transfer input properties (back) to ActionForm.</p>
     * @param helper Our ActionHelper
     * @param context Our ClientContext
     */
    protected void conformInput(ActionHelper helper, ClientContext context) {

        Context input = context.getInput();
        helper.setInputToForm(input);

    }

    /**
     * <p>Transfer framework properties (back) to framework objects.</p>
     * @param helper Our ActionHelper
     * @param context Our ClientContext
     */
    protected void conformState(ActionHelper helper, ClientContext context) {

        helper.setLocale(context.getLocale());

    }


    /**
     * <p>
     * Operations to perform prior to executing business command.
     * </p>
     * @param helper Our ActionHelper
     * @param context Our ClientContext
     * @return ActionForward to follow, or null
     */
    protected ActionForward postExecute(ActionHelper helper, ClientContext context) {

        conformInput(helper,context);
        conformState(helper,context);

        // TODO: Expose any output
        // TODO: Expose any status messages,
        // TODO: Expose any error messages and find input

        return null;

    }

    /**
     * <p>Convenience method to return nominal location.</p>
     * @param mapping Our ActionMapping
     * @return ActionForward named "success" or null
     */
    protected ActionForward findSuccess(ActionMapping mapping) {
        return mapping.findForward("success");
    }

    // See interface for JavaDoc
    public ActionForward execute(ActionHelper helper) throws Exception {

        ActionForward location;
        ClientContext context = getContext(helper);

        location = preExecute(helper,context);
        if (location!=null) return location;

        boolean stop = getCatalogCommand(helper).execute(context);

        location = postExecute(helper,context);
        if (location!=null) return location;

        return findSuccess(helper);

    }

    // ModuleContext -> state for a module (mappings, messages) : ReadOnly
    // ClientContext -> runtime input for this request/client (locale,form) : ReadWrite
    // ActionContext -> ClientState, ModuleState

    // StrutsContext -> state for entire Struts application : ReadWrite
    // StrutsContext.createActionContext(request);

}
