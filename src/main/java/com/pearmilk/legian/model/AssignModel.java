package com.pearmilk.legian.model;

import com.pearmilk.jsdom.*;

/**
 * Created with IntelliJ IDEA.
 * User: matteopelati
 * Date: 9/9/13
 * Time: 4:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class AssignModel extends AbstractModel implements CodeProvider {
    String location;
    String expr;

    public AssignModel(String location, String expr) {
        this.location = location;
        this.expr = expr;
    }

    @Override
    public JsToken getCode() {

        return new JsAssignment(
                new JsRef(location, new JsSelfRef()),
                new JsRaw(expr)
                );

    }
}