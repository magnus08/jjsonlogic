package com.learnifier.jsonlogic;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import sun.rmi.runtime.Log;

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

    private final ObjectMapper mapper = new ObjectMapper();
    private final Map<String, Operator> operators = new HashMap<>();

    private JsonLogic() {
        operators.put("&&",
                new Operator() {
                    @Override
                    public Object evalOp(Environment env, Object tree) {
                        if(tree instanceof List && ((List)tree).size() == 2) {
                            Object arg1 = evalInner(env, ((List)tree).get(0));
                            Object arg2 = evalInner(env, ((List)tree).get(1));
                            if(arg1 instanceof Boolean && arg2 instanceof Boolean) {
                                return (boolean)arg1 && (boolean)arg2;
                            }
                            throw new ParseException("Incorrect type in &&, both arguments must be boolean");
                        }
                        throw new ParseException("&& takes two arguments.");
                    }
                });
        operators.put("||",
                new Operator() {
                    @Override
                    public Object evalOp(Environment env, Object tree) {
                        if(tree instanceof List && ((List)tree).size() == 2) {
                            Object arg1 = evalInner(env, ((List)tree).get(0));
                            Object arg2 = evalInner(env, ((List)tree).get(1));
                            if(arg1 instanceof Boolean && arg2 instanceof Boolean) {
                                return (boolean)arg1 || (boolean)arg2;
                            }
                            throw new ParseException("Incorrect type in ||, both arguments must be boolean");
                        }
                        throw new ParseException("|| takes two arguments.");
                    }
                });
        operators.put("+",
                new Operator() {
                    @Override
                    public Object evalOp(Environment env, Object tree) {
                        if(tree instanceof List && ((List)tree).size() == 2) {
                            Object arg1 = evalInner(env, ((List)tree).get(0));
                            Object arg2 = evalInner(env, ((List)tree).get(1));
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
                            throw new ParseException("Incorrect type in +, must be integer and integer, or data and integer");
                        }
                        throw new ParseException("+ takes two arguments.");
                    }
                });
        operators.put(">",
                new Operator() {
                    @Override
                    public Object evalOp(Environment env, Object tree) {
                        if(tree instanceof List && ((List)tree).size() == 2) {
                            Object arg1 = evalInner(env, ((List)tree).get(0));
                            Object arg2 = evalInner(env, ((List)tree).get(1));
                            if(arg1 instanceof Integer && arg2 instanceof Integer) {
                                return (Integer)arg1 >= (Integer)arg2;
                            }
                            if(arg1 instanceof Date && arg2 instanceof Date) {
                                Date d1 = (Date)arg1;
                                Date d2 = (Date)arg2;

                                System.out.println(">= " + d1 + " " + d2);
                                return d1.after(d2) || d1.equals(d2);
                            }
                            throw new ParseException("Incorrect type in >, must be integer or date and both must be of same type.");
                        }
                        throw new ParseException("> takes two arguments.");
                    }

                });
        operators.put("var",
                new Operator() {
                    @Override
                    public Object evalOp(Environment env, Object arg) {
                        if (arg instanceof String) {
                            final Object value = env.getValue((String) arg);
                            return value;
                        }
                        throw new ParseException("Variable name must be a string");
                    }
                });
    }


    private Object evalSt(Environment env, String rule) throws IOException {
        return evalInner(env, mapper.readValue(rule, new TypeReference<Map<String, Object>>() {
        }));
    }

    private Object evalInner(Environment env, Object obj) {
        if(obj instanceof String) {
            return obj;
        }
        if(obj instanceof Map) {
            Map<String, Object> tree = (Map)obj;
            final String op = tree.keySet().stream().findFirst().orElseThrow(() -> new ParseException("Parse error, multiple keys in " + tree));
            if(operators.containsKey(op)) {
                return operators.get(op).evalOp(env, tree.get(op));
            } else {
                throw new ParseException("Operator " + op + " not implemented.");
            }
        }
        if(obj instanceof Integer) {
            return obj;
        }

        if(obj instanceof Boolean) {
            return obj;
        }

        throw new ParseException("Unknown type");
    }


    public static Object eval(Environment env, String rule) throws IOException {
        return new JsonLogic().evalSt(env, rule);
    }
}
