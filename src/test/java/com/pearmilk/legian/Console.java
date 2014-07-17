package com.pearmilk.legian;

import com.pearmilk.legian.RhinoScriptExecutor;
import com.pearmilk.legian.StateChartExecutor;
import com.pearmilk.legian.builder.SCXMLBuilder;
import com.pearmilk.legian.model.*;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: matteopelati
 * Date: 26/7/13
 * Time: 5:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class Console {

    public static class UserManager {

        public String getBlogName(String msisdn) {

            return "pippo";

        }

        public List<String> getCelebList(String shortcode) {

            try {
                Thread.sleep(15000);
            } catch (InterruptedException e) {

            }

            ArrayList<String> celebs = new ArrayList<String>();
            celebs.add("Test1");
            celebs.add("Test2");
         //   celebs.add("Test3");
            return celebs;


        }

    }

    public static class Logger {
        public void info(String str) {
            System.out.println(str);
        }
    }

    public static class CallControlObject {

        public void play(int callid, String filename) {
            System.out.println("Playing " + filename + " for callid " + callid);

        }


    }



    private static StateChartExecutor executor;
    private static List<StateChartInstance> instances = new ArrayList<StateChartInstance>();

    public static void main(String[] args) throws Exception {

        DocumentBuilderFactory xmlFactory = DocumentBuilderFactory.newInstance();
        xmlFactory.setCoalescing(true);
        DocumentBuilder builder = xmlFactory.newDocumentBuilder();

        InputStream content = new FileInputStream("src/test/disc2.scxml");
        Document doc =  builder.parse(content);


        StateChartModel scm = new SCXMLBuilder().build(new File("src/test/disc2.scxml"));
        RhinoScriptExecutor scriptExecutor = new RhinoScriptExecutor();
        scriptExecutor.addToGlobalContext("out", System.out);
        scriptExecutor.addToGlobalContext("um", new UserManager());
        scriptExecutor.addToGlobalContext("callcontrol", new CallControlObject());
        scriptExecutor.addToGlobalContext("logger", new Logger());
        scriptExecutor.addToGlobalContext("logging.logger1", new Logger());
        scriptExecutor.addToGlobalContext("logging.logger2", new Logger());
        scriptExecutor.sealGlobalContext();
        executor = new StateChartExecutor(scm, scriptExecutor);

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        while (true){

            System.out.print(">");

            String cmd = br.readLine();
            if (cmd.startsWith("new")) {

                String[] p = cmd.split(" +");

                Map<String,Object> margs = new HashMap<String, Object>();

                for (int ctr=1;ctr<p.length;ctr++) {
                    String[] kv = p[ctr].split("=");
                    if (kv.length == 2)
                        margs.put(kv[0].trim(), kv[1].trim());
                }

               newInstance(margs);
            }
            else if (cmd.startsWith("send")) {
                final String[] p = cmd.split(" +");

                final Map<String,Object> margs = new HashMap<String, Object>();

                for (int ctr=3;ctr<p.length;ctr++) {
                    String[] kv = p[ctr].split("=");
                    if (kv.length == 2)
                        margs.put(kv[0].trim(), kv[1].trim());
                }

                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        instances.get(Integer.parseInt(p[1])).send(new Message(p[2], margs));
                    }
                });
                t.start();
              //  t.join();


            }
            else if (cmd.startsWith("exit")) {
                System.exit(0);
            }
        }

    }


    private static void newInstance(final Map<String,Object> args) {

        final StateChartInstance sci = executor.createInstance(null);
        sci.getListeners().add(new StateChartEventListener() {
            @Override
            public void eventTriggered(Event e) {
                System.out.println("EVENT: " + e.toString());
                System.out.flush();
            }
        });

        instances.add(sci);
        System.out.println(instances.size()-1);

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                sci.initialize(args);
            }
        });
        t.start();

    }



}
