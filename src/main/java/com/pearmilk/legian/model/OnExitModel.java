package com.pearmilk.legian.model;

import com.pearmilk.jsdom.JsStatements;
import com.pearmilk.jsdom.JsToken;

/**
 * Created with IntelliJ IDEA.
 * User: matteopelati
 * Date: 1/8/13
 * Time: 5:17 PM
 * To change this template use File | Settings | File Templates.
 */
public class OnExitModel extends AbstractExecutableModel {
    public OnExitModel(JsToken code, JsStatements childCode) {
        super("onexit", code, childCode);
    }
}
