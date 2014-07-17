package com.pearmilk.legian.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: matteopelati
 * Date: 21/11/13
 * Time: 11:36 AM
 * To change this template use File | Settings | File Templates.
 */
public class CustomEvent implements Event {

    String name;
    Map<String,Object> params = new HashMap<String, Object>();

    public CustomEvent(String name) {
        this.name = name;
    }

    public CustomEvent(String name, Map<String, Object> params) {
        this.name = name;
        this.params.putAll(params);
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

    public Map<String, Object> getParams() {
        return params;
    }
}
