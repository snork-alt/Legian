package com.pearmilk.legian.model;

import com.pearmilk.jsdom.*;

/**
 * Created with IntelliJ IDEA.
 * User: matteopelati
 * Date: 9/9/13
 * Time: 5:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class ExecuteModel extends AbstractModel implements CodeProvider {
    String expr;

    public ExecuteModel(String expr) {
        this.expr = expr;
    }

    @Override
    public JsToken getCode() {

        return new JsRaw(expr);

    }
}
