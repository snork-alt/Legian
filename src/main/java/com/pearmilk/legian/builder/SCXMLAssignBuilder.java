package com.pearmilk.legian.builder;

import com.pearmilk.legian.model.AssignModel;
import com.pearmilk.legian.model.Model;
import org.w3c.dom.Element;

/**
 * Created with IntelliJ IDEA.
 * User: matteopelati
 * Date: 9/9/13
 * Time: 4:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class SCXMLAssignBuilder implements SCXMLItemBuilder {
    @Override
    public String[] getTagNames() {
        return new String[] {"assign"};
    }

    @Override
    public Model build(Element element, Model parent, SCXMLBuilder builder) throws SCXMLElementException {
        return new AssignModel(element.getAttribute("location"), element.getAttribute("expr"));
    }

    @Override
    public void complete(Model m) throws SCXMLElementException {

    }
}

