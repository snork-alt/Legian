package com.pearmilk.legian.model;

import com.pearmilk.jsdom.*;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: matteopelati
 * Date: 30/7/13
 * Time: 6:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class RootStateModel extends StateModel implements Executable {

    JsFunction rootFct;
    JsStatements rootStatements;

    public RootStateModel() {
        super("");
        rootStatements = new JsStatements();
        rootFct = new JsFunction("", new JsGroup(new JsList()), new JsBlock(rootStatements));
    }

    @Override
    protected void activate(List<StateModel> path, Map<String, Object> params) {

        try {
            StateChartModel.getContext().executor.invokeFunction(
                    rootFct,  StateChartModel.getContext().getExecutionContext(), Object.class, new Object[0], this
            );

            super.activate(path, params);
        }
        catch(Exception e){
            triggerEvent(new ScriptExecutionExceptionEvent(rootStatements, e));
        }
    }

    @Override
    public void appendChild(Model child) throws InvalidModelException {

        if (child instanceof CodeProvider){
            rootStatements.add(((CodeProvider) child).getCode());
        }
        else {
            super.appendChild(child);
        }
    }
}
