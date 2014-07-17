package com.pearmilk.legian;

import com.pearmilk.legian.model.ScriptExecutor;
import com.pearmilk.legian.model.StateChartInstance;
import com.pearmilk.legian.model.StateChartModel;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: matteopelati
 * Date: 26/7/13
 * Time: 5:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class StateChartExecutor {

    StateChartModel sc;
    ScriptExecutor executor;

    public StateChartExecutor(StateChartModel sc, ScriptExecutor executor){
        this.sc = sc;
        this.executor = executor;

    }

    public synchronized StateChartInstance createInstance(Map<String,Object> vars) {

        if (vars == null)
            vars = new HashMap<String,Object>();

        return new StateChartInstance(sc, executor, vars);
    }

    public synchronized void updateStateChart(StateChartModel newModel) {
        sc = newModel;
    }



}
