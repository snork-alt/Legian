package com.pearmilk.legian.model;

import com.pearmilk.jsdom.JsRaw;
import com.pearmilk.jsdom.JsToken;

/**
 * Created with IntelliJ IDEA.
 * User: matteopelati
 * Date: 26/7/13
 * Time: 6:00 PM
 * To change this template use File | Settings | File Templates.
 */
public class ScriptModel extends AbstractModel implements CodeProvider {

    private String code;

    public ScriptModel(String code) {
        this.code = code;
    }

    @Override
    public JsToken getCode() {
        return new JsRaw(code);
    }

    @Override
    public void appendChild(Model child) {
        throw new IllegalStateException("Tag <script> cannot have children");
    }
}
