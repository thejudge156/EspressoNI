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

            Value loaderClazzVal = bindings.getMember("java.net.URLClassLoader");
            Value loaderMethodVal = loaderClazzVal.invokeMember("getMethod", bindings.getMember("[Ljava.net.URL"), bindings.getMember("java.lang.ClassLoader"));
            Value loaderVal = loaderMethodVal.invokeMember("invoke", testJar.toURI().toURL(), ClassLoader.getSystemClassLoader());
            Value pluginClazz = loaderVal.invokeMember("loadClass", "me.judge.Usage");
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