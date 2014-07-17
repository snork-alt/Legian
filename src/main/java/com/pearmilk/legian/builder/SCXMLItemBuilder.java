package com.pearmilk.legian.builder;

import com.pearmilk.legian.model.Model;
import org.w3c.dom.Element;

/**
 * Created with IntelliJ IDEA.
 * User: matteopelati
 * Date: 25/7/13
 * Time: 7:00 PM
 * To change this template use File | Settings | File Templates.
 */
public interface SCXMLItemBuilder {

    String[] getTagNames();
    Model build(Element element, Model parent, SCXMLBuilder builder) throws SCXMLElementException;
    void complete(Model m) throws SCXMLElementException;
}
