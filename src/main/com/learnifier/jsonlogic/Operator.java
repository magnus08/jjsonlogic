package com.learnifier.jsonlogic;

/**
 * @author Magnus Andersson (magnus.andersson@learnifier.com)
 */
public abstract class Operator {
    public abstract Object eval(Environment env, Object arg1, Object arg2);
}
