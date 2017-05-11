package com.learnifier.jsonlogic;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class Main {

    private static final String rule = "{\">=\": [ { \"var\" : \"now\" }, {\"+\": [ { \"var\" : \"course-start-date\" }, 3 ]}]}";
//    private static final String rule = "{\">=\": [ { \"var\": \"x\" }, 1 ]}";

    private static ImmutableMap<String, ? extends Serializable> vals;

    public static void main(String[] args) throws IOException {
        vals = ImmutableMap.of(
                "now", 12,
                "x", 42,
                "course-start-date", 10
        );

        ObjectMapper mapper = new ObjectMapper();

        final Map<String, Object> tree = mapper.readValue(rule, new TypeReference<Map<String, Object>>() {
        });

        System.out.println("Res: " + eval(tree));

    }

    public static Object eval(Object obj) {
        if(obj instanceof Map) {
            Map<String, Object> tree = (Map)obj;
            final String op = tree.keySet().stream().findFirst().orElseThrow(() -> new IllegalStateException("Parse error, multiple keys in " + tree));
            switch(op) {
                case ">=":
                    return gte(eval(((List)tree.get(op)).get(0)), eval(((List)tree.get(op)).get(1)));
                case "var":
                    return var(tree.get(op));
                case "+":
                    return plus(eval(((List)tree.get(op)).get(0)), eval(((List)tree.get(op)).get(1)));
                default:
                    throw new IllegalStateException("Operator " + op + " not implemented.");
            }
        }
        if(obj instanceof Integer) {
            return obj;
        }

        throw new IllegalStateException("Unknown type");

    }

    static Object gte(Object arg1, Object arg2) {

        if(arg1 instanceof Integer && arg2 instanceof Integer) {
            return (Integer)arg1 >= (Integer)arg2;
        }
        throw new IllegalStateException("Incorrect type in gte");
    }

    static Object plus(Object arg1, Object arg2) {

        if(arg1 instanceof Integer && arg2 instanceof Integer) {
            return (Integer)arg1 + (Integer)arg2;
        }
        throw new IllegalStateException("Incorrect type in gte");
    }

    static Object var(Object name) {
        if(name instanceof String) {
            final Object value = vals.get(name);
            return value;
        }
        throw new IllegalStateException("Variable name must be a string");
    }

}
