package org.apache.commons.chain.mailreader;

import org.apache.commons.chain.Context;

import java.util.Locale;

/**
 * Application interface for the Struts framework.
 */
public interface ViewContext extends Context {

    public static String PN_LOCALE = "locale";
    public boolean isLocale();
    public void setLocale(Locale locale);
    public Locale getLocale();

    public static String PN_INPUT = "input";
    public boolean isInput();
    public void setInput(Context context);
    public Context getInput();

}
