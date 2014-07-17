package com.pearmilk.legian.builder;

import com.pearmilk.jsdom.JsStatements;
import com.pearmilk.legian.model.Model;
import com.pearmilk.legian.model.TransitionModel;
import org.w3c.dom.Element;

/**
 * Created with IntelliJ IDEA.
 * User: matteopelati
 * Date: 26/7/13
 * Time: 4:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class SCXMLTransitionBuilder implements SCXMLItemBuilder {

    @Override
    public String[] getTagNames() {
        return new String[] {"transition"};
    }

    @Override
    public Model build(Element element, Model parent, SCXMLBuilder builder) {

        TransitionModel transition = new TransitionModel(
                (!element.getAttribute("target").isEmpty()) ? element.getAttribute("target") : null,
                (!element.getAttribute("cond").isEmpty()) ? element.getAttribute("cond") : null,
                (!element.getAttribute("event").isEmpty()) ? element.getAttribute("event") : null,
                (element.getAttribute("type").equals("external")),
                new JsStatements()
        );

        return transition;

    }

    @Override
    public void complete(Model m) throws SCXMLElementException {

    }
}
