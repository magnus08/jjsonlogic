package com.learnifier.jsonlogic;

import java.util.Map;

/**
 * Simple class to keep variables.
 *
 * @author Magnus Andersson (magnus.andersson@learnifier.com)
 */
public class Environment {

    private Map<String, Object> variableMap;

    Environment(Map<String, Object> variableMap) {
        this.variableMap = variableMap;
    }

    public static Environment from(Map<String, Object> variableMap) {
        return new Environment(variableMap);
    }

    public Object getValue(String name) {
        return this.variableMap.get(name);
    }
}
