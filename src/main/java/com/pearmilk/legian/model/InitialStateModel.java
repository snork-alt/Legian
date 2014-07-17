package com.pearmilk.legian.model;

/**
 * Created with IntelliJ IDEA.
 * User: matteopelati
 * Date: 25/7/13
 * Time: 7:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class InitialStateModel extends StateModel {

    public InitialStateModel() {
        super("<INITIAL>");
    }

    /*
    @Override
    protected void activate(List<StateModel> path, Map<String,Object> params) {

        TransitionModel m = getChildren(new TransitionModel[0]).get(0);
        m.activate(path, params);
    } */

}
