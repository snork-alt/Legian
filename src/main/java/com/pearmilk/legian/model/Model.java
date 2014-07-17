package com.pearmilk.legian.model;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: matteopelati
 * Date: 29/7/13
 * Time: 10:29 AM
 * To change this template use File | Settings | File Templates.
 */
public interface Model extends ModelEventListener {
    public <T> List<T> getChildren(T[] type);
    public List<Model> getChildren();
    void appendChild(Model m) throws InvalidModelException;
    void setParent(Model m);
    Model getParent();
}
