package com.pearmilk.legian.model;

import com.pearmilk.jsdom.*;

/**
 * Created with IntelliJ IDEA.
 * User: matteopelati
 * Date: 1/8/13
 * Time: 7:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class LogModel extends AbstractModel implements CodeProvider {
    String label;
    String expr;

    public LogModel(String label, String expr) {
        this.label = label;
        this.expr = expr;
    }

    @Override
    public JsToken getCode() {
        return new JsInvoke(
                new JsRef("info", new JsRef("logger")),
                new JsGroup(new JsList(new JsRef(expr)))
        );

    }
}
