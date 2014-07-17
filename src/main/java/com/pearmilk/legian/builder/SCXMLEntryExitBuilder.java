package com.pearmilk.legian.builder;

import com.pearmilk.jsdom.JsStatements;
import com.pearmilk.legian.model.*;
import org.w3c.dom.Element;

/**
 * Created with IntelliJ IDEA.
 * User: matteopelati
 * Date: 1/8/13
 * Time: 5:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class SCXMLEntryExitBuilder implements SCXMLItemBuilder {
    @Override
    public String[] getTagNames() {
        return new String[] {"onentry", "onexit"};
    }

    @Override
    public Model build(Element element, Model parent, SCXMLBuilder builder) throws SCXMLElementException {

        if (!(parent instanceof StateModel))
            throw new SCXMLElementException("onentry and onexit can only be childfren of a state");
        if (parent instanceof RootStateModel)
            throw new SCXMLElementException("onentry and onexit cannot appear ar root elemnts in an scxml file");

        JsStatements s = new JsStatements();
        if (element.getTagName().toLowerCase().equals("onentry")){
            return new OnEntryModel(s,s);
        }
        else if (element.getTagName().toLowerCase().equals("onexit")) {
            return new OnExitModel(s,s);
        }
        else {
            throw new SCXMLElementException("invalid token");
        }
    }

    @Override
    public void complete(Model m) throws SCXMLElementException {

    }
}
