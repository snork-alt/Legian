package com.pearmilk.legian.taglib;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: matteopelati
 * Date: 9/9/13
 * Time: 6:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class Tag {

    String name;
    Map<String,String> params = new HashMap<String,String>();
    String code;

    public Tag(String name) {
        this.name = name;
    }

    public String addParameter(String key, String value) {
        return params.put(key, value);
    }

    public Set<Map.Entry<String,String>> getParameters() {
        return params.entrySet();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public String generateCode(Map<String, String> values) {

        String resCode = code;
        for (Map.Entry<String,String> e : params.entrySet()) {
            String value = values.containsKey(e.getKey()) ? values.get(e.getKey()) : e.getValue();
            resCode = resCode.replaceAll("\\$\\{" + e.getKey() + "\\}", value);
        }

        return resCode;
    }


}
