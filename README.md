Legian
======

An SCXML parser and executor built in Java using Rhino to execute
Javascript code. It is not fully compliant to the W3C specs, but
it supports some addional features that I'm extensively using in
 my production code.

In src/test you will find some sample SCXMLs file and a SampleConsole.java
that demonstrates how to use the engine.

The following code creates an instance of the SCXML engine and register and event handler:

```
StateChartModel stateChartModel = new SCXMLBuilder().build(new File(args[0]));
ScriptExecutor scriptExecutor = new RhinoScriptExecutor();
scriptExecutor.addToGlobalContext("out", System.out);
StateChartExecutor stateChartExecutor = new StateChartExecutor(stateChartModel, scriptExecutor);
StateChartInstance stateChartInstance = stateChartExecutor.createInstance(null);
stateChartInstance.getListeners().add(new StateChartEventListener() {
    @Override
    public void eventTriggered(Event e) {
        System.out.println("EVENT -> " + e.toString());
        System.out.flush();
    }
});
stateChartInstance.initialize(null);
```

Whenever you need to send a message to the state chart just use `stateChartInstance.send()`


