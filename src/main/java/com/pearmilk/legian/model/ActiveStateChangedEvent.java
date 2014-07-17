package com.pearmilk.legian.model;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: matteopelati
 * Date: 29/7/13
 * Time: 3:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class ActiveStateChangedEvent implements Event {

    private List<StateModel> path;
    private StateModel active;
    private boolean transitional;

    public ActiveStateChangedEvent(List<StateModel> path, StateModel active, boolean transitional) {
        this.path = path;
        this.active = active;
        this.transitional = transitional;
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        for (StateModel m : path)
            b.append(m.getId() + "/");

        return "ActiveStateChangedEvent: " + b.toString() + active.getId() + (transitional ? " TRANSITIONAL" : "");
    }

    public String getPath() {
        StringBuilder b = new StringBuilder();
        for (StateModel m : path)
            b.append(m.getId() + "/");
        return b.toString() + active.getId();
    }
}
