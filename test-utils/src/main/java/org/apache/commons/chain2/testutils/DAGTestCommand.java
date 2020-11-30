package org.apache.commons.chain2.testutils;

import org.apache.commons.chain2.Context;
import org.apache.commons.chain2.DAGCommand;
import org.apache.commons.chain2.Processing;

import java.util.concurrent.ConcurrentHashMap;

public class DAGTestCommand implements DAGCommand<String, Object, Context<String, Object>> {
    private String commandId;

    private DAGTestCommand(){
        // private constructor
    }

    public DAGTestCommand(String commandId){
        this.commandId = commandId;
    }

    @Override
    public String getId() {
        return commandId;
    }

    @Override
    public Processing execute(Context<String, Object> context) {
        log(context, commandId);

        return Processing.CONTINUE;
    }

    /**
     * <p>Log the specified <code>id</code> into a StringBuffer attribute
     * named "log" in the specified <code>context</code>, creating it if
     * necessary.</p>
     *
     * @param context The {@link Context} into which we log the identifiers
     * @param id The identifier to be logged
     */
    protected void log(Context<String, Object> context, String id) {
        ConcurrentHashMap<String, Integer> sb = (ConcurrentHashMap<String, Integer>) context.get("log");
        if (sb == null) {
            sb = new ConcurrentHashMap<>();
            context.putIfAbsent("log", sb);
        }
        if(!sb.containsKey(id)){
            sb.put(id, 0);
        }
        sb.put(id, sb.get(id) + 1);
    }
}
