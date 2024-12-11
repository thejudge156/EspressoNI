package me.judge;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class Main {
    public static void main(String[] args) {
        File testJar = new File("testjar.jar");
        try(Context ctx = Context.newBuilder("java")
                .allowAllAccess(true)
                .build()) {
            Value bindings = ctx.getBindings("java");

            Value loaderVal = bindings.getMember("java.net.URLClassLoader");
            Value loaderInstanceVal = loaderVal.invokeMember("newInstance", ctx.asValue(new URL[]{testJar.toURI().toURL()}));
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