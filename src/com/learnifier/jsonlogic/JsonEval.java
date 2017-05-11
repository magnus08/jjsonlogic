package com.learnifier.jsonlogic;

import java.io.Serializable;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

/**
 * @author Magnus Andersson (magnus.andersson@learnifier.com)
 */
public class JsonEval {


    private Map<String, ? extends Serializable> vals;

    public JsonEval(Map<String, ? extends Serializable> vals) {
        this.vals = vals;
    }

    public Object eval(Object obj) {
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

    private Boolean gte(Object arg1, Object arg2) {

        if(arg1 instanceof Integer && arg2 instanceof Integer) {
            return (Integer)arg1 >= (Integer)arg2;
        }
        if(arg1 instanceof Date && arg2 instanceof Date) {
            Date d1 = (Date)arg1;
            Date d2 = (Date)arg2;

            System.out.println(">= " + d1 + " " + d2);
            return d1.after(d2) || d1.equals(d2);
        }
        throw new IllegalStateException("Incorrect type in gte");
    }

    private Object plus(Object arg1, Object arg2) {
        if(arg1 instanceof Date && arg2 instanceof Integer) {
            Date date = (Date)arg1;
            Integer val = (Integer)arg2;
            final java.util.Calendar cal = GregorianCalendar.getInstance();
            cal.setTime(date);
            cal.add(GregorianCalendar.DATE, val);
            return cal.getTime();
        }
        if(arg1 instanceof Integer && arg2 instanceof Integer) {
            return (Integer)arg1 + (Integer)arg2;
        }
        throw new IllegalStateException("Incorrect type in gte");
    }

    private Object var(Object name) {
        if(name instanceof String) {
            final Object value = vals.get(name);
            return value;
        }
        throw new IllegalStateException("Variable name must be a string");
    }

}
