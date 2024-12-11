package me.judge;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        File testJar = new File("testjar.jar");
        File apiJar = new File("api.jar");
        Context ctx = Context.newBuilder("java")
                .option("java.Properties.java.class.path", testJar.getAbsolutePath() + ":" + apiJar.getAbsolutePath())
                .option("java.PolyglotInterfaceMappings", "me.judge.Main$JavaPlugin;")
                .allowAllAccess(true)
                .build();
        Value pluginClazz = ctx.getBindings("java").getMember("java.lang.Class").invokeMember("forName", "me.judge.Usage");
        JavaPlugin plugin = pluginClazz.invokeMember("getDeclaredConstructor")
                .invokeMember("newInstance").as(JavaPlugin.class);
        plugin.onEnable();

        ctx.close();
    }

    public interface JavaPlugin {
        void onEnable();
    }
}