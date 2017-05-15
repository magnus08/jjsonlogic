package com.learnifier.jsonlogic;

/**
 * @author Magnus Andersson (magnus.andersson@learnifier.com)
 */
public abstract class Operator<T> {
    public abstract Object evalOp(Environment env, T tree);
}
