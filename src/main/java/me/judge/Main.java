package me.judge;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        File testJar = new File("testjar.jar");
        File apiJar = new File("api.jar");
        Context ctx = Context.newBuilder("java")
                .option("java.Classpath", testJar.getAbsolutePath() + ":" + apiJar.getAbsolutePath())
                .allowAllAccess(true)
                .build();
        Value pluginClazz = ctx.getBindings("java").getMember("me.judge.Usage").getMember("static");
        System.out.println(pluginClazz.getMember("mirror").getMemberKeys());

        ctx.close();
    }

    public interface JavaPlugin {
        void onEnable();
    }
}