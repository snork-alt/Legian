package com.pearmilk.legian;

import com.pearmilk.legian.builder.SCXMLBuilder;
import com.pearmilk.legian.model.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created with IntelliJ IDEA.
 * User: matteopelati
 * Date: 16/7/14
 * Time: 3:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class SampleConsole {

    static StateChartModel stateChartModel;
    static ScriptExecutor scriptExecutor;
    static StateChartExecutor stateChartExecutor;
    static StateChartInstance stateChartInstance;
    static Executor taskExecutors = Executors.newCachedThreadPool();

    public static void main(String[] args) throws Exception {

        stateChartModel = new SCXMLBuilder().build(new File(args[0]));
        scriptExecutor = new RhinoScriptExecutor();
        scriptExecutor.addToGlobalContext("out", System.out);

        stateChartExecutor = new StateChartExecutor(stateChartModel, scriptExecutor);

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        newInstance(null);

        while (true){

            String cmd = br.readLine();
            if (cmd.startsWith("bye")) {
                System.exit(0);
            }
            else {

                final String[] p = cmd.split(" +");
                final Map<String,Object> margs = parseArgs(p, 1);

                taskExecutors.execute(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println();
                        stateChartInstance.send(new Message(p[0], margs));
                    }
                });


            }
            System.out.print(">");

        }
    }


    private static Map<String,Object> parseArgs(String[] p, int start) {
        final Map<String,Object> margs = new HashMap<String, Object>();
        for (int ctr=start;ctr<p.length;ctr++) {
            String[] kv = p[ctr].split("=");
            if (kv.length == 2)
                margs.put(kv[0].trim(), kv[1].trim());
        }
        return margs;
    }

    private static void newInstance(final Map<String,Object> args) {

        stateChartInstance = stateChartExecutor.createInstance(args);
        stateChartInstance.getListeners().add(new StateChartEventListener() {
            @Override
            public void eventTriggered(Event e) {
                System.out.println("EVENT -> " + e.toString());
                System.out.flush();
            }
        });

        taskExecutors.execute(new Runnable() {
            @Override
            public void run() {
                stateChartInstance.initialize(args);
            }
        });

    }



}
