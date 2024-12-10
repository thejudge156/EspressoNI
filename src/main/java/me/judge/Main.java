package me.judge;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;

public class Main {
    public static void main(String[] args) {
        Context ctx = Context.newBuilder("java")
                .allowAllAccess(true)
                .build();
        File testJar = new File("testjar.jar");
        try(URLClassLoader loader = URLClassLoader.newInstance(new URL[]{testJar.toURI().toURL()})) {
            Value loaderVal = ctx.asValue(loader);
            Value pluginClazz = loaderVal.invokeMember("loadClass", "me.judge.Usage");
            Value pluginInstance = pluginClazz.invokeMember("getDeclaredConstructor").invokeMember("newInstance");
            JavaPlugin plugin = pluginInstance.as(JavaPlugin.class);
            plugin.onEnable();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        ctx.close();
    }

    public abstract static class JavaPlugin {
        public abstract void onEnable();
    }
}