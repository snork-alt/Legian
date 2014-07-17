package com.pearmilk.legian.model;

/**
 * Created with IntelliJ IDEA.
 * User: matteopelati
 * Date: 21/11/13
 * Time: 10:41 AM
 * To change this template use File | Settings | File Templates.
 */
public class ParamModel extends AbstractModel {

    String name;
    String expr;

    public ParamModel(String name, String expr) {
        this.name = name;
        this.expr = expr;
    }

    public String getName() {
        return name;
    }

    public String getExpr() {
        return expr;
    }
}
