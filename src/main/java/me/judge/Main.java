package me.judge;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        File testJar = new File("testjar.jar");
        Context ctx = Context.newBuilder("java")
                .option("java.Properties.java.class.path", testJar.getAbsolutePath() + ":" + System.getProperty("java.class.path"))
                .allowAllAccess(true)
                .build();
        Value pluginClazz = ctx.getBindings("java").getMember("me.judge.Usage").getMember("class");
        JavaPlugin plugin = pluginClazz.as(JavaPlugin.class);
        plugin.onEnable();

        ctx.close();
    }

    public abstract static class JavaPlugin {
        public abstract void onEnable();
    }
}