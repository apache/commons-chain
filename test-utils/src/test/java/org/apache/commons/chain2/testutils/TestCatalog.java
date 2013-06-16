package org.apache.commons.chain2.testutils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.chain2.Catalog;
import org.apache.commons.chain2.Command;
import org.apache.commons.chain2.Context;

/**
 * @version $Id$
 */
public class TestCatalog<K, V, C extends Context<K, V>> implements Catalog<K, V, C> {

    Map<String, Command> commands = new HashMap<String, Command>();

    @Override
    public <CMD extends Command<K, V, C>> void addCommand(String name, CMD command) {
        commands.put(name, command);
    }

    @Override
    public <CMD extends Command<K, V, C>> CMD getCommand(String name) {
        return (CMD) commands.get(name);
    }

    @Override
    public Iterator<String> getNames() {
        return commands.keySet().iterator();
    }

}
