package com.learnifier.jsonlogic;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Magnus Andersson (magnus.andersson@learnifier.com)
 */
public class JsonLogic {

    final ObjectMapper mapper = new ObjectMapper();
    final Map<String, Operator> operators = new HashMap<>();

    public JsonLogic() {
        operators.put("+",
                new Operator() {
                    @Override
                    public Object eval(Environment env, Object arg1, Object arg2) {
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
                        throw new IllegalStateException("Incorrect type in +, must be integer or date.");
                    }
                });
        operators.put(">",
                new Operator() {
                    @Override
                    public Object eval(Environment env, Object arg1, Object arg2) {
                        if(arg1 instanceof Integer && arg2 instanceof Integer) {
                            return (Integer)arg1 >= (Integer)arg2;
                        }
                        if(arg1 instanceof Date && arg2 instanceof Date) {
                            Date d1 = (Date)arg1;
                            Date d2 = (Date)arg2;

                            System.out.println(">= " + d1 + " " + d2);
                            return d1.after(d2) || d1.equals(d2);
                        }
                        throw new IllegalStateException("Incorrect type in >");
                    }
                });
        operators.put("var",
                new Operator() {
                    @Override
                    public Object eval(Environment env, Object arg1, Object arg2) {
                        if (arg1 instanceof String) {
                            final Object value = env.getValue((String) arg2);
                            return value;
                        }
                        throw new IllegalStateException("Variable name must be a string");
                    }
                });
    }


    public Object evalSt(Environment env, String rule) throws IOException {
        return eval(env, mapper.readValue(rule, new TypeReference<Map<String, Object>>() {
        }));
    }

    public Object eval(Environment env, Object obj) {
        if(obj instanceof String) {
            return obj;
        }
        if(obj instanceof Map) {
            Map<String, Object> tree = (Map)obj;
            final String op = tree.keySet().stream().findFirst().orElseThrow(() -> new IllegalStateException("Parse error, multiple keys in " + tree));
            if(operators.containsKey(op)) {
                operators.get(op).eval(env, eval(env, ((List)tree.get(op)).get(0)), eval(env, ((List)tree.get(op)).get(1)));
            } else {
                throw new JsonLogicException("Operator " + op + " not implemented.");
            }
        }
        if(obj instanceof Integer) {
            return obj;
        }

        throw new IllegalStateException("Unknown type");

    }
}
