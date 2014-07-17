package com.pearmilk.legian;

import com.pearmilk.legian.RhinoScriptExecutor;
import com.pearmilk.legian.StateChartExecutor;
import com.pearmilk.legian.builder.SCXMLBuilder;
import com.pearmilk.legian.model.*;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created with IntelliJ IDEA.
 * User: matteopelati
 * Date: 9/9/13
 * Time: 12:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class LoadTest {

    public static class UserManager {

        public String getBlogName(String msisdn) {
            return "pippo";
        }

        public List<String> getCelebList(String shortcode) {

            ArrayList<String> celebs = new ArrayList<String>();
            celebs.add("Test1");
            celebs.add("Test2");
            celebs.add("Test3");
            return celebs;
        }

    }

    public static class CallControlObject {

        public void play(int callid, String filename) {
            System.out.println("Playing " + filename + " for callid " + callid);

        }

    }

    public static class Logger {
        public void info(String str) {
            System.out.println(str);
        }
    }

    private static StateChartExecutor executor;
    private static final AtomicInteger completed = new AtomicInteger();
    private static int instances;
    private static ScheduledExecutorService exec = Executors.newScheduledThreadPool(100);
    private static Random rand = new Random();

    public static void main(String[] args) throws Exception {

        instances = 1;

        DocumentBuilderFactory xmlFactory = DocumentBuilderFactory.newInstance();
        xmlFactory.setCoalescing(true);
        DocumentBuilder builder = xmlFactory.newDocumentBuilder();

        InputStream content = new FileInputStream("src/test/disc2.scxml");
        Document doc =  builder.parse(content);

        StateChartModel scm = new SCXMLBuilder().build(doc);
        RhinoScriptExecutor scriptExecutor = new RhinoScriptExecutor();
        scriptExecutor.addToGlobalContext("out", System.out);
        scriptExecutor.addToGlobalContext("um", new UserManager());
        scriptExecutor.addToGlobalContext("callcontrol", new CallControlObject());
        scriptExecutor.addToGlobalContext("logger", new Logger());
        scriptExecutor.sealGlobalContext();
        executor = new StateChartExecutor(scm, scriptExecutor);

        for (int ctr=0;ctr<instances;ctr++) {

            exec.schedule(new Runnable() {
                @Override
                public void run() {
                    newInstance();
                }
            }, rand.nextInt(6000), TimeUnit.MILLISECONDS);


        }

        while(true){
            Thread.sleep(1000);
        }

    }

    private static void newInstance() {

        final StateChartInstance sci = executor.createInstance(null);
        sci.getListeners().add(new StateChartEventListener() {
            @Override
            public void eventTriggered(Event e) {
                if (e instanceof ExecutionCompletedEvent) {
                    int i = completed.incrementAndGet();
                    System.out.println("Completed: " + i);
                    if (i == instances)
                        System.exit(0);
                }
                else {

                    final Map<String,Object> params = new HashMap<String,Object>();
                    params.put("dtmf", "6");

                    exec.schedule(new Runnable() {
                        @Override
                        public void run() {
                            sci.send(new Message("playcomplete", params));
                        }
                    }, rand.nextInt(6000), TimeUnit.MILLISECONDS);

                }
            }
        });
        sci.initialize(Collections.EMPTY_MAP);

        final Map<String,Object> params = new HashMap<String,Object>();
        params.put("dtmf", "6");

     //   for (int ctr=0;ctr<4;ctr++){
            exec.schedule(new Runnable() {
                @Override
                public void run() {
                    sci.send(new Message("playcomplete", params));
                }
            }, rand.nextInt(5), TimeUnit.MILLISECONDS);
     //   }

    }

}
