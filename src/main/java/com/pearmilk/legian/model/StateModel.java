package com.pearmilk.legian.model;

import com.pearmilk.jsdom.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: matteopelati
 * Date: 25/7/13
 * Time: 6:33 PM
 * To change this template use File | Settings | File Templates.
 */

public class StateModel extends AbstractModel {

    String id;
    boolean parentState = false;
    Map<String, StateModel> subStates = new HashMap<String, StateModel>();
    JsFunction onentry = null;
    IsDefined hasOnEntry = IsDefined.Uknown;
    JsFunction onexit = null;
    IsDefined hasOnExit = IsDefined.Uknown;

    public StateModel(String id) {
        super();
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public boolean isActive() {
        return StateChartModel.getContext().isStateActive(this);
    }

    protected void executeOnExit(Map<String,Object> params) throws Exception {

        if (hasOnExit == IsDefined.No) {
            return;
        }

        if (hasOnExit == IsDefined.Uknown) {

            List<OnExitModel> handlers = getChildren(new OnExitModel[0]);
            if (handlers.size() == 0) {
                hasOnExit = IsDefined.No;
                return;
            }

            JsStatements statements = new JsStatements();
            for (OnExitModel handler : handlers)
                statements.add(handler.getCode());

            onexit = new JsFunction("", new JsGroup(new JsList(new JsRef("params"))), new JsBlock(statements));
            hasOnExit = IsDefined.Yes;
        }

        StateChartModel.getContext().getExecutor().invokeFunction(
                onexit,  StateChartModel.getContext().getExecutionContext(),
                Object.class, new Object[] {params}, this
        );

    }

    protected void deactivate(Map<String,Object> params) {

        try {
            executeOnExit(params);
        }
        catch(Exception e){
            triggerEvent(new ScriptExecutionExceptionEvent(onentry, e));
            return;
        }

        StateChartModel.getContext().deactivateState(this);
    }

    protected void executeOnEntry(Map<String,Object> params) throws Exception {

        if (hasOnEntry == IsDefined.No) {
            return;
        }

        if (hasOnEntry == IsDefined.Uknown) {

            List<OnEntryModel> handlers = getChildren(new OnEntryModel[0]);
            if (handlers.size() == 0) {
                hasOnEntry = IsDefined.No;
                return;
            }

            JsStatements statements = new JsStatements();
            for (OnEntryModel handler : handlers)
                statements.add(handler.getCode());

            onentry = new JsFunction("", new JsGroup(new JsList(new JsRef("params"))), new JsBlock(statements));
            hasOnEntry = IsDefined.Yes;
        }

        StateChartModel.getContext().getExecutor().invokeFunction(
            onentry,  StateChartModel.getContext().getExecutionContext(),
            Object.class, new Object[] {params}, this
        );

    }

    protected void activate(List<StateModel> path, Map<String,Object> params) {

        try {
            executeOnEntry(params);
        }
        catch(Exception e){
            triggerEvent(new ScriptExecutionExceptionEvent(onentry, e));
            return;
        }

        StateChartModel.getContext().activateState(this);

        if (parentState)  {
            if (!(this instanceof RootStateModel))
                path.add(this);
            getChildren(new InitialStateModel[0]).get(0).activate(path, params);
        }
        else {
            boolean fwd = forwardActivation(path, params);
            if (!fwd)
                triggerEvent(new ActiveStateChangedEvent(path, this, false));
        }

    }

    protected boolean forwardActivation(List<StateModel> path, Map<String,Object> params) {
        for (TransitionModel t : getChildren(new TransitionModel[0])){
            if (!t.hasEvent() && t.matches(new Message(null,params))){
                t.activate(path, params);
                return true;
            }
        }
        return false;
    }

    @Override
    public void appendChild(Model child) throws InvalidModelException {

        if (StateModel.class.isInstance(child)) {
            parentState = true;
            subStates.put(((StateModel)child).getId(), (StateModel)child);
        }

        super.appendChild(child);
    }

    public StateModel getSubState(String id){
        return subStates.get(id);
    }

    private boolean handleLocalMessage(Message m, List<StateModel> path){

        for (TransitionModel t : getChildren(new TransitionModel[0])){
            if (t.matches(m)){
                t.activate(path, m.getParams());
                return true;
            }
        }

        return false;
    }

    public boolean handleMessage(Message m, List<StateModel> path){

        if (!StateChartModel.getContext().isStateActive(this))
            return false;

        if (!parentState) {
            handleLocalMessage(m, path);
            return true;
        }
        else {
            if (!(this instanceof RootStateModel))
                path.add(this);
            for (StateModel model : getChildren(new StateModel[0])){
                if (model.handleMessage(m, path))
                    return true;
            }
            return false;
        }
    }

    @Override
    public void triggerEvent(Event e) {

        boolean handled = false;

        if (e instanceof ExecutionCompletedEvent){
            if (!(this instanceof RootStateModel))
                ((ExecutionCompletedEvent)e).getPath().remove(((ExecutionCompletedEvent)e).getPath().size()-1);
            handled = handleLocalMessage(new Message("done"), ((ExecutionCompletedEvent)e).getPath());
        }

        if (!handled)
            super.triggerEvent(e);

    }
}
