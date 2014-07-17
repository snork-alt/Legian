package com.pearmilk.legian.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: matteopelati
 * Date: 25/7/13
 * Time: 7:02 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractModel implements Model {

    static enum IsDefined {Yes, No, Uknown};

    Model parent = null;
    List<Model> children = new ArrayList<Model>();

    public void appendChild(Model child) throws InvalidModelException {
        children.add(child);
        child.setParent(this);
    }

    public Model getParent() {
        return parent;
    }

    public void setParent(Model parent) {
        this.parent = parent;
    }

    public <T> List<T> getChildren(T[] type) {

        List<T> ret = new ArrayList<T>();
        Class componentType = (type).getClass().getComponentType();

        for (Model i : children){
            if (componentType.isInstance(i))
                ret.add((T) i);
        }

        return ret;

    }

    public List<Model> getChildren(){
        return children;
    }


    @Override
    public void triggerEvent(Event e) {
        if (parent != null) {
            parent.triggerEvent(e);
        }
        else {
            StateChartModel.getContext().triggerEvent(e);
        }
    }


}
