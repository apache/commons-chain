package org.apache.commons.chain.mailreader;

import org.apache.commons.chain.Context;

import java.util.Locale;

/**
 * <p>
 * A "disconnected" representation of the framework state for the client
 * making a request. An instance of this interface may be passed to
 * other components. An instance may also be used to update the "connected"
 * representation.
 * </p>
 * <p>
 * See {@link org.apache.commons.chain.mailreader.commands.MailReaderBase}
 * for an implementation.
 * </p>
 */
public interface ClientContext extends Context {

    public static String PN_LOCALE = "locale";
    public boolean isLocale();
    public void setLocale(Locale locale);
    public Locale getLocale();

    public static String PN_INPUT = "input";
    public boolean isInput();
    public void setInput(Context context);
    public Context getInput();

}
