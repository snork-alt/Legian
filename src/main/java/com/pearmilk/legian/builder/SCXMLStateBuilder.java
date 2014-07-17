package com.pearmilk.legian.builder;

import com.pearmilk.legian.model.*;
import org.w3c.dom.Element;

/**
 * Created with IntelliJ IDEA.
 * User: matteopelati
 * Date: 25/7/13
 * Time: 7:33 PM
 * To change this template use File | Settings | File Templates.
 */

public class SCXMLStateBuilder  implements SCXMLItemBuilder {
    @Override
    public String[] getTagNames() {
        return new String[] {"state", "initial", "final", "scxml"};
    }

    @Override
    public Model build(Element element, Model parent, SCXMLBuilder builder) throws SCXMLElementException {
        try {

            if (element.getTagName().toLowerCase().equals("initial")){
                return new InitialStateModel();
            }
            else if (element.getTagName().toLowerCase().equals("final")){
                return new FinalStateModel(element.getAttribute("id"));
            }
            else {

                StateModel state = element.getTagName().equals("scxml") ?
                        new RootStateModel() :
                        new StateModel(element.getAttribute("id"));

                if (state instanceof RootStateModel && !element.getAttribute("name").isEmpty())
                    builder.getModules().add(element.getAttribute("name"));

                if (!element.getAttribute("initial").isEmpty()) {
                    InitialStateModel initial = new InitialStateModel();
                    TransitionModel transition = new TransitionModel(element.getAttribute("initial"));
                    initial.appendChild(transition);
                    state.appendChild(initial);
                }

                return state;
            }
        }
        catch(InvalidModelException me){
            throw new SCXMLElementException(me.getMessage());
        }
    }

    @Override
    public void complete(Model m) throws SCXMLElementException {

        boolean init = false;
        boolean fin = false;
        boolean sub = false;
        boolean doneTr = m instanceof RootStateModel;

        for (Model child : m.getChildren()) {
            if (child instanceof InitialStateModel)
                init = true;
            else if (child instanceof FinalStateModel)
                fin = true;
            else if (child instanceof StateModel)
                sub = true;
            else if (child instanceof TransitionModel &&
                    ((TransitionModel) child).hasEvent() &&
                    ((TransitionModel) child).eventMatches("done") &&
                    !((TransitionModel) child).hasCondition()
                    )
                doneTr = true;

        }

        if (sub && !init)
            throw new SCXMLElementException("Initial attribute or <initial> tag must be specified in any <scxml> or <state> block");
        if (sub && !fin)
            throw new SCXMLElementException("A <final> tag must be specified in any <scxml> or <state> block");
      //  if (sub && !doneTr)
      //      throw new SCXMLElementException("A <transition event=\"done\"> pointing to the returning state must be specified in a substate");

    }
}
