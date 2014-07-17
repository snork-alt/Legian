package com.pearmilk.legian.model;

import com.pearmilk.jsdom.JsToken;

/**
 * Created with IntelliJ IDEA.
 * User: matteopelati
 * Date: 31/7/13
 * Time: 5:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class ScriptExecutionExceptionEvent implements Event {

    JsToken scriptBody;
    Exception e;

    public ScriptExecutionExceptionEvent(JsToken scriptBody, Exception e) {
        this.scriptBody = scriptBody;
        this.e = e;
    }

    @Override
    public String toString() {
        return e.toString();
    }

    public Exception getException() {
        return e;
    }
}
