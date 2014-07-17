package com.pearmilk.legian.model;

import com.pearmilk.jsdom.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: matteopelati
 * Date: 2/8/13
 * Time: 10:31 AM
 * To change this template use File | Settings | File Templates.
 */
public class ConditionModel extends AbstractModel implements CodeProvider{

    JsRaw ifCond;
    JsStatements ifBlock = new JsStatements();
    Map<JsRaw, JsStatements> elseIfBlocks = new LinkedHashMap<JsRaw, JsStatements>();
    JsStatements elseBlock;
    JsStatements current;

    public ConditionModel(String cond) {
        ifCond = new JsRaw(cond);
        current = ifBlock;
    }

    public void newCondition(String cond) {
        JsStatements condBlock = new JsStatements();
        elseIfBlocks.put(new JsRaw(cond), condBlock);
        current = condBlock;
    }

    public void elseCondition() {
        elseBlock = new JsStatements();
        current = elseBlock;
    }

    @Override
    public void appendChild(Model child) throws InvalidModelException {
        if (!(child instanceof CodeProvider))
            throw new InvalidModelException("The child of a conditional block must be an executable block");
        current.add(((CodeProvider)child).getCode());
    }

    @Override
    public JsToken getCode() {

        List<JsToken> conds = new ArrayList<JsToken>();

        conds.add(new JsIf(ifCond, new JsBlock(ifBlock)));

        for (Map.Entry<JsRaw,JsStatements> e : elseIfBlocks.entrySet()) {
            conds.add(new JsElseIf(e.getKey(), new JsBlock(e.getValue())));
        }

        if (elseBlock != null) {
            conds.add(new JsElse(new JsBlock(elseBlock)));
        }

        return new JsIfList(conds);


    }
}
