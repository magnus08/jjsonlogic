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

    private final ObjectMapper mapper = new ObjectMapper();
    private final Map<String, Operator> operators = new HashMap<>();

    private JsonLogic() {
        operators.put("&&",
                new Operator() {
                    @Override
                    public Boolean evalOp(Environment env, List<? extends Comparable> tree) {
                        if(tree.size() == 2) {
                            Comparable arg1 = evalInner(env, tree.get(0));
                            Comparable arg2 = evalInner(env, tree.get(1));
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
                    public Boolean evalOp(Environment env, List<? extends Comparable> tree) {
                        if(tree.size() == 2) {
                            Comparable arg1 = evalInner(env, tree.get(0));
                            Object arg2 = evalInner(env, tree.get(1));
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
                    public Comparable evalOp(Environment env, List<? extends Comparable> tree) {
                        if(tree.size() == 2) {
                            Object arg1 = evalInner(env, tree.get(0));
                            Object arg2 = evalInner(env, tree.get(1));
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
                    public Boolean evalOp(Environment env, List<? extends Comparable> tree) {
                        if(tree.size() == 2) {
                            Object arg1 = evalInner(env, tree.get(0));
                            Object arg2 = evalInner(env, tree.get(1));



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
                    public Comparable evalOp(Environment env, List<? extends Comparable> tree) {
                        String arg1 = (String)evalInner(env, tree.get(0));
                        return env.getValue(arg1);
                    }
                });
    }


    private Comparable evalSt(Environment env, String rule) throws IOException {
        return evalInner(env, mapper.readValue(rule, new TypeReference<Map<String, Object>>() {
        }));
    }

    private Comparable evalInner(Environment env, Object obj) {
        if(obj instanceof String || obj instanceof Integer || obj instanceof Boolean) {
            return (Comparable)obj;
        }
        if(obj instanceof Map) {
            Map<String, List> tree = (Map) obj;
            final String opName = tree.keySet().stream().findFirst().orElseThrow(() -> new ParseException("Parse error, multiple keys in " + tree));
            Operator op = operators.get(opName);
            if(op != null) {
                return op.evalOp(env, tree.get(opName));
            } else {
                throw new ParseException("Operator " + opName + " not implemented.");
            }
        }

        throw new ParseException("Unknown type");
    }


    public static Comparable eval(Environment env, String rule) throws IOException {
        return new JsonLogic().evalSt(env, rule);
    }
}
