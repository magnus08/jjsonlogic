package com.learnifier.jsonlogic;

import java.util.Map;

/**
 * Simple class to keep variables.
 *
 * @author Magnus Andersson (magnus.andersson@learnifier.com)
 */
public class Variables {

    private Map<String, Object> variableMap;

    Variables(Map<String, Object> variableMap) {
        this.variableMap = variableMap;
    }

    public static Variables from(Map<String, Object> variableMap) {
        return new Variables(variableMap);
    }

    public Object getValue(String name) {
        return this.variableMap.get(name);
    }
}
