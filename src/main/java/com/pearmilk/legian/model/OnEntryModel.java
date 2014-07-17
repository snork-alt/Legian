package com.pearmilk.legian.model;

import com.pearmilk.jsdom.JsStatements;
import com.pearmilk.jsdom.JsToken;

/**
 * Created with IntelliJ IDEA.
 * User: matteopelati
 * Date: 1/8/13
 * Time: 5:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class OnEntryModel extends AbstractExecutableModel {

    public OnEntryModel(JsToken code, JsStatements childCode) {
        super("onentry", code, childCode);
    }
}
