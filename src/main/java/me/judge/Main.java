package me.judge;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.TypeLiteral;
import org.graalvm.polyglot.Value;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        File testJar = new File("testjar.jar");
        File apiJar = new File("api.jar");
        Context ctx = Context.newBuilder("java")
                .option("java.Properties.java.class.path", testJar.getAbsolutePath() + ":" + apiJar.getAbsolutePath())
                .allowAllAccess(true)
                .build();
        Value pluginClazz = ctx.getBindings("java").getMember("java.lang.Class").invokeMember("forName", "me.judge.Usage");
        JavaPlugin plugin = (JavaPlugin) pluginClazz.invokeMember("getDeclaredConstructor")
                .invokeMember("newInstance").as(Object.class);
        plugin.onEnable();

        ctx.close();
    }

    public abstract static class JavaPlugin {
        public abstract void onEnable();
    }
}