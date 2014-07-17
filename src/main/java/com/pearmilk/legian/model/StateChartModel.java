package com.pearmilk.legian.model;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: matteopelati
 * Date: 25/7/13
 * Time: 6:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class StateChartModel {

    private static ThreadLocal<StateChartContext> stateChartContext =
            new ThreadLocal<StateChartContext>();

    public static void setContext(StateChartContext context) {
        stateChartContext.set(context);
    }
    public static StateChartContext getContext() {
        return stateChartContext.get();
    }


    StateModel model;
    Set<String> modules;

    public StateChartModel(StateModel m, Set<String> modules) {
        this.model = m;
        this.modules = modules;
    }

    public Set<String> getModules() {
        return modules;
    }

    public void initialize(Map<String,Object> params) {
        model.activate(new ArrayList<StateModel>(), params);
    }

    public void send(Message m){
        model.handleMessage(m, new ArrayList<StateModel>());
    }

    public StateModel getRootModel() {
        return model;
    }


}
