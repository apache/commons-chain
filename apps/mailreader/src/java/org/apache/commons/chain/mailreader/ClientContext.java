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
 * See {@link org.apache.commons.chain.mailreader.MailReaderBase}
 * for an implementation.
 * </p>
 */
public interface ClientContext extends Context {

    /**
     * Client {@link Locale} property.
     */
    public static String PN_LOCALE = "locale";

    public void setLocale(Locale locale);

    public Locale getLocale();

    /**
     * Input {@link Context} property.
     */
    public static String PN_INPUT = "input";

    public void setInput(Context context);

    public Context getInput();

}
