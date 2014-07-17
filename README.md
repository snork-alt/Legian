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

##Features

- *All the following tags are supported*: `<assign>`, `<if>`, `<elseif>`, `<else>`, `<onentry>`,
  `<onexit>`, `<log>`, `<script>`, `<state>`, `<initial>`, `<final>`, `<scxml>`, `<transition>`.
  Not all parameters are supported for all events. To check which params are supported look into the
  src/builder folder.

- *Support for custom tag libraries*: It is veary easy to add custom tag libraries implemented
 in javascript. An example is provided in the sample code. The custom library can be included using the
 `<taglib>` custom tag.

- *Support for scxml includes*: It is possible to include an SCXML file into a state of another
  SCXML file. An example is included in the sample code. In order to include an SCXML file use th
  `<include>` custom tag.

If you are interested in extending the functionalities to fully support the W3C specs, please let
me know. The code is quite stable and has been used in production running an IVR system handling 300+
concurrent calls.
