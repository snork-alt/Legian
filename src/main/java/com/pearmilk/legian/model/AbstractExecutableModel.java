package com.pearmilk.legian.model;

import com.pearmilk.jsdom.JsStatements;
import com.pearmilk.jsdom.JsToken;

/**
 * Created with IntelliJ IDEA.
 * User: matteopelati
 * Date: 1/8/13
 * Time: 10:22 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractExecutableModel extends AbstractModel implements CodeProvider {

    String type;
    JsToken code;
    JsStatements childCode;
    boolean hasCode = false;

    public AbstractExecutableModel(String type, JsToken code, JsStatements childCode) {

        this.type = type;
        this.code = code;
        this.childCode = childCode;
    }

    public AbstractExecutableModel(String type, JsToken code) {

        this.type = type;
        this.code = code;
    }

    @Override
    public void appendChild(Model child) throws InvalidModelException {

        if (child instanceof CodeProvider) {

            if (childCode == null) {
                throw new InvalidModelException("<" + type + "> does not allow children");
            }

            childCode.add(((CodeProvider)child).getCode());
            hasCode = true;
        }
        else {
            super.appendChild(child);
        }
    }

    @Override
    public JsToken getCode() {
        return code;
    }

    boolean hasCode() {
        return hasCode;
    }
}
