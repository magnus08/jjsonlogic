package com.learnifier.jsonlogic;

/**
 * @author Magnus Andersson (magnus.andersson@learnifier.com)
 */
public abstract class Operator {


    private final String op;

    public Operator(String op, int precedence) {
        this.op = op;
    }

    public Operator(String op) {
        this(op, 0);
    }

    public abstract Object eval(Object arg1, Object arg2);
}
