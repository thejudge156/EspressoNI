package me.judge;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Engine;
import org.graalvm.polyglot.Value;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class Main {
    public static void main(String[] args) {
        File testJar = new File("testjar.jar");
        Engine engine = Engine.newBuilder()
                .option("engine.Inlining", "false")
                .option("engine.WarnInterpreterOnly", "false")
                .build();
        try(Context ctx = Context.newBuilder("java")
                .allowAllAccess(true)
                .engine(engine)
                .build()) {
            Value bindings = ctx.getBindings("java");

            Value loaderVal = bindings.getMember("java.net.URLClassLoader");
            Value loaderInstanceVal = loaderVal.invokeMember("newInstance", ctx.asValue(new URL[]{testJar.toURI().toURL()}), ctx.asValue(Main.class.getClassLoader()));
            Value pluginClazz = loaderInstanceVal.invokeMember("loadClass", "me.judge.Usage");
            Value pluginInstance = pluginClazz.invokeMember("getDeclaredConstructor").invokeMember("newInstance");
            JavaPlugin plugin = pluginInstance.as(JavaPlugin.class);
            plugin.onEnable();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public abstract static class JavaPlugin {
        public abstract void onEnable();
    }
}