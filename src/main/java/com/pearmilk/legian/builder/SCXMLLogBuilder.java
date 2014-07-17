package com.pearmilk.legian.builder;

import com.pearmilk.legian.model.LogModel;
import com.pearmilk.legian.model.Model;
import org.w3c.dom.Element;

/**
 * Created with IntelliJ IDEA.
 * User: matteopelati
 * Date: 1/8/13
 * Time: 7:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class SCXMLLogBuilder implements SCXMLItemBuilder {
    @Override
    public String[] getTagNames() {
        return new String[] {"log"};
    }

    @Override
    public Model build(Element element, Model parent, SCXMLBuilder builder) throws SCXMLElementException {
        return new LogModel(element.getAttribute("label"), element.getAttribute("expr"));
    }

    @Override
    public void complete(Model m) throws SCXMLElementException {

    }
}
