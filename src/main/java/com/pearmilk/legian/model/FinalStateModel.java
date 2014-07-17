package com.pearmilk.legian.model;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: matteopelati
 * Date: 26/7/13
 * Time: 3:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class FinalStateModel extends StateModel {

    public FinalStateModel(String id) {
        super(id);
    }

    @Override
    protected void activate(List<StateModel> path, Map<String, Object> params) {

        try {
            executeOnEntry(params);
        }
        catch(Exception e){
            triggerEvent(new ScriptExecutionExceptionEvent(onentry, e));
            return;
        }


        try {
            executeOnExit(params);
        }
        catch(Exception e){
            triggerEvent(new ScriptExecutionExceptionEvent(onentry, e));
            return;
        }

        this.parent.triggerEvent(new ExecutionCompletedEvent(path));
    }
}