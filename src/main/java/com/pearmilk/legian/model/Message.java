package com.pearmilk.legian.model;

import java.util.Collections;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: matteopelati
 * Date: 26/7/13
 * Time: 6:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class Message {
    String name;
    Map<String,Object> params;

    public Message(String name, Map<String, Object> params) {
        this.params = params;
        this.name = name;
    }

    public Message(String name) {
        this.name = name;
        this.params = Collections.EMPTY_MAP;
    }

    public String getName() {
        return name;
    }

    public Map<String, Object> getParams() {
        return params;
    }
}
