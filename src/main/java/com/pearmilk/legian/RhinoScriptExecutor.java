package com.pearmilk.legian;

import com.pearmilk.jsdom.JavascriptWriter;
import com.pearmilk.jsdom.JsFunction;
import com.pearmilk.jsdom.JsToken;
import com.pearmilk.legian.model.AbstractModel;
import com.pearmilk.legian.model.ScriptExecutor;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

import java.io.StringWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created with IntelliJ IDEA.
 * User: matteopelati
 * Date: 29/7/13
 * Time: 6:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class RhinoScriptExecutor implements ScriptExecutor {

    static ThreadLocal<Context> context = new ThreadLocal<Context>();

    ReadWriteLock cacheLock = new ReentrantReadWriteLock();
    Map<JsToken, Function> cache = new HashMap<JsToken, Function>();
    ScriptableObject globalScope;

    public RhinoScriptExecutor() {
        Context ctx = Context.enter();
        globalScope = ctx.initStandardObjects(null, true);
        ctx.exit();
    }

    private Scriptable map2Scriptable(Map<String,Object> o, Scriptable scope) {
        Scriptable nmap = context.get().newObject(scope);
        for (Map.Entry<String,Object> e : o.entrySet()) {
            nmap.put(e.getKey(), nmap, e.getValue());
        }
        return nmap;
    }

    private Object java2JS(Object o, Scriptable scope) {

        if (o instanceof Map) {
            return map2Scriptable((Map<String,Object>) o, scope);
        }
        else {
            return context.get().javaToJS(o, scope);
        }

    }

    private Object[] convertDataTypes(Object[] args, Scriptable scope) {

        Object[] ret = new Object[args.length];
        for (int ctr=0;ctr<args.length;ctr++) {
            ret[ctr] = java2JS(args[ctr], scope);
        }
        return ret;
    }

    private Function compileAndCache(JsFunction fct) throws Exception {
        cacheLock.writeLock().lock();
        try {
            StringWriter swriter = new StringWriter();
            JavascriptWriter jsWriter = new JavascriptWriter(swriter, true);
            fct.generateCode(jsWriter);
            Function binary = context.get().compileFunction(globalScope, swriter.toString(), "", 0, null);
            cache.put(fct, binary);
            return binary;
        }
        finally {
            cacheLock.writeLock().unlock();
        }
    }

    @Override
    public Object invokeFunction(JsFunction fct, Object scope, Class<?> retType, Object[] args, AbstractModel currentModel) throws Exception {

        Function binary = null;

        ((Scriptable)scope).put("__currentmodel", (Scriptable)scope, currentModel);

        cacheLock.readLock().lock();
        binary = cache.get(fct);
        cacheLock.readLock().unlock();

        if (binary == null)
            binary = compileAndCache(fct);

        Object r = binary.call(
                context.get(),
                (Scriptable)scope,
                (Scriptable)scope,
                convertDataTypes(args, (Scriptable)scope)
        );

        return context.get().jsToJava(r, retType);
    }

    @Override
    public void bind() {
        context.set(Context.enter());
    }

    @Override
    public void unbind() {
        context.get().exit();
    }

    @Override
    public void addToGlobalContext(String id, Object o) {
        Context ctx = Context.enter();
        //globalScope.put(id, globalScope, ctx.javaToJS(o, globalScope));
        setContextObject(ctx, globalScope, id, o);
        ctx.exit();
    }

    public void sealGlobalContext() {
        Context ctx = Context.enter();
        globalScope.sealObject();
        ctx.exit();
    }

    private void setContextObject(Context ctx, Scriptable scope, String id, Object val) {

        String[] tokens = id.split("\\.");
        String[] path = Arrays.copyOfRange(tokens, 0, tokens.length-1);
        String var = tokens[tokens.length-1];

        Scriptable current = scope;
        for (int ctr=0;ctr<path.length;ctr++) {
            if (!current.has(path[ctr],current)) {
                Scriptable newObject = ctx.newObject(current);
                current.put(path[ctr], current, newObject);
            }
            current = (Scriptable) current.get(path[ctr], current);
        }
        current.put(var, current, ctx.javaToJS(val, current));
    }

    public Object createInstanceContext(Map<String,Object> contextVars) {
        Context ctx = Context.enter();
        try {
            Scriptable instanceScope = ctx.newObject(globalScope);
            for (Map.Entry<String,Object> e: contextVars.entrySet()){
                setContextObject(ctx, instanceScope, e.getKey(), e.getValue());
               // instanceScope.put(e.getKey(), instanceScope, e.getValue());
            }

            instanceScope.setPrototype(globalScope);
            instanceScope.setParentScope(null);
            return instanceScope;
        }
        finally {
            ctx.exit();
        }
    }
}
