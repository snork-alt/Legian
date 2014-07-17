package com.pearmilk.legian.model;

import java.util.HashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: matteopelati
 * Date: 31/7/13
 * Time: 11:21 AM
 * To change this template use File | Settings | File Templates.
 */
public class StateChartContext {

    Set<StateModel> activeStates = new HashSet<StateModel>();
    StateChartEventListener listener;
    ScriptExecutor executor;
    Object executionContext;

    public StateChartContext(ScriptExecutor executor, Object executionContext, StateChartEventListener listener) {
        this.listener = listener;
        this.executor = executor;
        this.executionContext = executionContext;
    }

    public boolean isStateActive(StateModel m) {
        return activeStates.contains(m);
    }

    public void activateState(StateModel m) {
        activeStates.add(m);
    }

    public void deactivateState(StateModel m) {
        activeStates.remove(m);
    }

    public void triggerEvent(Event e) {
        if (listener != null)
            listener.eventTriggered(e);
    }

    void bind() {
        StateChartModel.setContext(this);
        executor.bind();
    }

    void unbind() {
        StateChartModel.setContext(null);
        executor.unbind();
    }

    public Object getExecutionContext() {
        return executionContext;
    }

    public ScriptExecutor getExecutor() {
        return executor;
    }
}
