package com.learnifier.jsonlogic;

/**
 * @author Magnus Andersson (magnus.andersson@learnifier.com)
 */
public abstract class Operator {
    public abstract Object evalOp(Environment env, Object tree);
}
