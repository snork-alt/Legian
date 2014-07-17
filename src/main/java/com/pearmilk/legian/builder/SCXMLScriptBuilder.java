package com.pearmilk.legian.builder;

import com.pearmilk.legian.model.Model;
import com.pearmilk.legian.model.ScriptModel;
import org.w3c.dom.Element;

/**
 * Created with IntelliJ IDEA.
 * User: matteopelati
 * Date: 26/7/13
 * Time: 5:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class SCXMLScriptBuilder implements SCXMLItemBuilder {
    @Override
    public String[] getTagNames() {
        return new String[] {"script"};
    }

    @Override
    public Model build(Element element, Model parent, SCXMLBuilder builder) throws SCXMLElementException {

        try
        {
            String code = element.getFirstChild().getNodeValue();
            return new ScriptModel(code);
        }
        catch(Exception e)
        {
            throw new SCXMLElementException(e);
        }
    }

    @Override
    public void complete(Model m) throws SCXMLElementException {

    }
}
