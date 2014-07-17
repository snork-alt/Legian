package com.pearmilk.legian.model;

import com.pearmilk.jsdom.JsFunction;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: matteopelati
 * Date: 31/7/13
 * Time: 3:18 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ScriptExecutor {
    Object invokeFunction(JsFunction fct, Object context, Class<?> retType, Object[] args, AbstractModel currentModel) throws Exception;
    void bind();
    void unbind();
    void addToGlobalContext(String id, Object o);
    Object createInstanceContext(Map<String,Object> contextVars);
}
