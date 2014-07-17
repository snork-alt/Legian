package com.pearmilk.legian.model;

import com.pearmilk.jsdom.*;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: matteopelati
 * Date: 21/11/13
 * Time: 10:46 AM
 * To change this template use File | Settings | File Templates.
 */
public class SendModel extends AbstractModel implements CodeProvider {

    String event;
    String eventExpr;

    public SendModel(String event, String eventExpr) {
        this.event = event;
        this.eventExpr = eventExpr;
    }

    @Override
    public JsToken getCode() {

        JsStatements s = new JsStatements();
        s.add(new JsRaw("var __eventparams = new java.util.HashMap()"));
        List<ParamModel> params = this.getChildren(new ParamModel[0]);
        for (ParamModel p : params) {
            s.add(new JsInvoke(new JsRef("__eventparams.put"), new JsGroup(new JsList(
                    new JsString(p.getName()), new JsRef(p.getExpr())
            ))));
        }

        JsToken param = new JsNew(
                new JsRef("net.bubbly.scxml.model.CustomEvent"),
                new JsGroup(
                        new JsList(
                            eventExpr.isEmpty() ? new JsString(event) : new JsRef(eventExpr),
                            new JsRef("__eventparams")
                        )
                )
        );


        JsToken r = new JsInvoke(
                new JsRef("this.__currentmodel.triggerEvent"),
                new JsGroup(param)
                );

        s.add(r);
        return s;

    }
}
