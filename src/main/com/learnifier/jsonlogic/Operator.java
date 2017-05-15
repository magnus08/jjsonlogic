package com.learnifier.jsonlogic;

import java.util.List;

/**
 * @author Magnus Andersson (magnus.andersson@learnifier.com)
 */
public abstract class Operator {
    public abstract Comparable evalOp(Environment env, List<? extends Comparable> tree);
}
