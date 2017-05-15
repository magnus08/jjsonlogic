package com.learnifier.jsonlogic;

import java.util.Map;

/**
 * Simple class to keep variables.
 *
 * @author Magnus Andersson (magnus.andersson@learnifier.com)
 */
public class Environment {

    private Map<String, ? extends Comparable> variableMap;

    Environment(Map<String, ? extends Comparable> variableMap) {
        this.variableMap = variableMap;
    }

    public static Environment from(Map<String, ? extends Comparable> variableMap) {
        return new Environment(variableMap);
    }

    public Comparable getValue(String name) {
        return this.variableMap.get(name);
    }
}
