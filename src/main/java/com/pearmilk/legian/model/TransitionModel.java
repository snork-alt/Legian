package com.pearmilk.legian.model;

import com.pearmilk.jsdom.*;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: matteopelati
 * Date: 25/7/13
 * Time: 6:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class TransitionModel extends AbstractExecutableModel {

    String target;
    JsFunction condition;
    Pattern event;
    boolean external;

    public TransitionModel(String target) {
        super("transition", null, null);
    }

    public TransitionModel(String target, String condition, String event, boolean external, JsStatements innerCode) {

        super(
                "transition",
                new JsFunction("", new JsGroup(new JsList(new JsRef("params"))), new JsBlock(innerCode)),
                innerCode
        );

        this.target = target;
        this.event = event != null ? Pattern.compile(event) : null;
        this.external = external;

        if (condition != null) {
            this.condition = new JsFunction(
                    "",
                    new JsGroup(new JsList(new JsRef("event"), new JsRef("params"), new JsRef("context"))),
                    new JsBlock(new JsStatements(new JsReturn(new JsRaw(condition))))
                    );
        }
    }

    public String getTarget() {
        return target;
    }

    public boolean matches(Message msg){
        try {

            if (event != null && (msg == null || msg.getName() == null))
                return false;
            if (event != null && !event.matcher(msg.getName()).matches())
                return false;
            if (condition == null)
                return true;

            Object ret = StateChartModel.getContext().executor.invokeFunction(
                    condition,  StateChartModel.getContext().getExecutionContext(), Object.class,
                    new Object[] {msg.getName(), msg.getParams(), StateChartModel.getContext().getExecutionContext()},
                    this
                    );

            if (ret instanceof Boolean) {
                return (Boolean) ret;
            }
            else {
                return ret != null;
            }

        }
        catch(Exception e){
            triggerEvent(new ScriptExecutionExceptionEvent(condition, e));
            return false;
        }

    }

    public boolean hasEvent() {
        return event != null;
    }

    private void execute( Map<String,Object> params) throws Exception {

        if (!hasCode())
            return;

        StateChartModel.getContext().getExecutor().invokeFunction(
                (JsFunction) getCode(),  StateChartModel.getContext().getExecutionContext(),
                Object.class, new Object[] {params}, this
        );

    }

    public void activate(List<StateModel> path, Map<String,Object> params) {

        ((StateModel)parent).deactivate(params);

        try {
            execute(params);
        }
        catch(Exception e){
            triggerEvent(new ScriptExecutionExceptionEvent(getCode(), e));
            return;
        }

        if (target != null) {
            StateModel smt = ((StateModel)getParent().getParent()).getSubState(target);
            smt.activate(path, params);
        }
    }

    public boolean eventMatches(String msg) {
        return event.matcher(msg).matches();
    }

    public boolean hasCondition() {
        return condition != null;
    }


}
