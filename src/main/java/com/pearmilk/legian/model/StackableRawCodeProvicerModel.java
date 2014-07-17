package com.pearmilk.legian.model;

import com.pearmilk.jsdom.*;

/**
 * Created with IntelliJ IDEA.
 * User: matteopelati
 * Date: 16/10/13
 * Time: 4:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class StackableRawCodeProvicerModel extends AbstractModel implements CodeProvider {

    String expr;

    @Override
    public void appendChild(Model child) throws InvalidModelException {
        super.appendChild(child);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public StackableRawCodeProvicerModel(String expr) {
        this.expr = expr;
    }

    @Override
    public JsToken getCode() {
        if (getChildren().isEmpty()) {
            return new JsRaw(expr);
        }
        else {
            JsList children = new JsList();
            for (Model child : getChildren()) {
                if (child instanceof CodeProvider)
                    children.add(((CodeProvider)child).getCode());
            }
            JsStatements s = new JsStatements();
            s.add(new JsVar("$children"));
            s.add(new JsAssignment(
                    new JsRef("$children"),
                    new JsStaticArray(children)
            ));
            s.add(new JsRaw(expr));
            return s;
        }
    }
}
