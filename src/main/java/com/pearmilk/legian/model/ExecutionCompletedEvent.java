package com.pearmilk.legian.model;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: matteopelati
 * Date: 30/7/13
 * Time: 3:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class ExecutionCompletedEvent implements Event {

    List<StateModel> path;

    public ExecutionCompletedEvent(List<StateModel> path) {
        this.path = path;
    }

    public List<StateModel> getPath() {
        return path;
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        for (StateModel m : path)
            b.append(m.getId() + "/");

        return "ExecutionCompletedEvent: " + b.toString();
    }
}
