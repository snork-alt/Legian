package com.pearmilk.legian.builder;

import com.pearmilk.legian.model.ConditionModel;
import com.pearmilk.legian.model.Model;
import org.w3c.dom.Element;

/**
 * Created with IntelliJ IDEA.
 * User: matteopelati
 * Date: 2/8/13
 * Time: 10:23 AM
 * To change this template use File | Settings | File Templates.
 */
public class SCXMLConditionBuilder implements SCXMLItemBuilder {
    @Override
    public String[] getTagNames() {
        return new String[] {"if", "elseif", "else"};
    }

    @Override
    public Model build(Element element, Model parent, SCXMLBuilder builder) throws SCXMLElementException {
        String tag = element.getTagName().toLowerCase();

        if (tag.equals("if")) {
            if (element.getAttribute("cond").isEmpty())
                throw new SCXMLElementException("<if> tag must be cond attribute");
            return new ConditionModel(element.getAttribute("cond"));
        }
        else if (tag.equals("elseif")) {
            if (!(parent instanceof ConditionModel))
                throw new SCXMLElementException("<elseif> tag must be a child of an <if> tag");
            if (element.getAttribute("cond").isEmpty())
                throw new SCXMLElementException("<elseif> tag must be cond attribute");

            ((ConditionModel)parent).newCondition(element.getAttribute("cond"));

        }
        else {
            if (!(parent instanceof ConditionModel))
                throw new SCXMLElementException("<else> tag must be a child of an <if> tag");

            ((ConditionModel)parent).elseCondition();

        }
        return null;
    }

    @Override
    public void complete(Model m) throws SCXMLElementException {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
