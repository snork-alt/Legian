package com.pearmilk.legian.builder;

import com.pearmilk.legian.model.Model;
import com.pearmilk.legian.model.SendModel;
import org.w3c.dom.Element;

/**
 * Created with IntelliJ IDEA.
 * User: matteopelati
 * Date: 21/11/13
 * Time: 10:44 AM
 * To change this template use File | Settings | File Templates.
 */
public class SCXMLEventBuilder implements SCXMLItemBuilder {

    @Override
    public String[] getTagNames() {
        return new String[] {"send"};
    }

    @Override
    public Model build(Element element, Model parent, SCXMLBuilder builder) throws SCXMLElementException {
        return new SendModel(element.getAttribute("event"), element.getAttribute("eventexpr"));
    }

    @Override
    public void complete(Model m) throws SCXMLElementException {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
