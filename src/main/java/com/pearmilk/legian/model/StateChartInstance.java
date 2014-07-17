package com.pearmilk.legian.model;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created with IntelliJ IDEA.
 * User: matteopelati
 * Date: 31/7/13
 * Time: 11:18 AM
 * To change this template use File | Settings | File Templates.
 */
public class StateChartInstance {

    StateChartModel model;
    String id;
    StateChartContext context;
    List<StateChartEventListener> listeners = new ArrayList<StateChartEventListener>();
    Queue<Message> internalQueue = new ConcurrentLinkedQueue<Message>();
    Queue<Message> externalQueue = new ConcurrentLinkedQueue<Message>();
    AtomicBoolean processing = new AtomicBoolean(false);

    public StateChartInstance(
            StateChartModel model, ScriptExecutor executor, Map<String,Object> vars) {
        this.id = UUID.randomUUID().toString();
        vars.put("__instanceId", this.id);
        this.model = model;
        this.context = new StateChartContext(
                executor, executor.createInstanceContext(vars),
                new StateChartEventListener() {
                    @Override
                    public void eventTriggered(Event e) {
                        handleInternalEvent(e);
                        if (!(e instanceof InternalEvent)) {
                            for (StateChartEventListener l : listeners)
                                l.eventTriggered(e);
                        }
                    }
                });
    }

    protected void handleInternalEvent(Event e) {

    }

    public String getId() {
        return id;
    }

    public void initialize(Map<String,Object> params) {
        context.bind();
        try {
            processing.set(true);
            model.initialize(params);
        }
        finally {
            context.unbind();
            processing.set(false);
            consumeQueue();
        }
    }

    public List<StateChartEventListener> getListeners() {
        return listeners;
    }

    private void execute(Message message) {

        context.bind();
        try {
            model.send(message);
        }
        finally {
            context.unbind();
        }
    }

    private void consumeQueue() {

        boolean res = processing.compareAndSet(false, true);
        if (!res) return;

        while (!internalQueue.isEmpty() || !externalQueue.isEmpty()) {

            if (!internalQueue.isEmpty())
                execute(internalQueue.poll());
            if (!externalQueue.isEmpty())
                execute(externalQueue.poll());
        }

        processing.set(false);
    }

    public void send(Message message) {

        externalQueue.add(message);
        consumeQueue();
    }


}
