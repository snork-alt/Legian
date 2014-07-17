package com.pearmilk.legian.builder;

import com.pearmilk.legian.model.ExecuteModel;
import com.pearmilk.legian.model.Model;
import org.w3c.dom.Element;

/**
 * Created with IntelliJ IDEA.
 * User: matteopelati
 * Date: 9/9/13
 * Time: 5:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class SCXMLExecuteBuilder implements SCXMLItemBuilder {
    @Override
    public String[] getTagNames() {
        return new String[] {"exec"};
    }

    @Override
    public Model build(Element element, Model parent, SCXMLBuilder builder) throws SCXMLElementException {
        return new ExecuteModel(element.getAttribute("expr"));
    }

    @Override
    public void complete(Model m) throws SCXMLElementException {

    }
}