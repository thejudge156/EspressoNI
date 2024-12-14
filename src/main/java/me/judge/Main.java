package me.judge;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        File testJar = new File("testjar.jar");
        Context ctx = Context.newBuilder("java")
                .option("java.Classpath", testJar.getAbsolutePath())
                .option("engine.RelaxStaticObjectSafetyChecks", "true")
                .allowAllAccess(true)
                .build();
        ctx.getBindings("java").getMember("java.lang.Class").invokeMember("forName", "me.judge.Usage", true, Main.class.getClassLoader());
        Value pluginClazz = ctx.getBindings("java").getMember("java.lang.Class").invokeMember("forName", "me.judge.Usage", false, ctx.asValue(ClassLoader.getPlatformClassLoader()));
        System.out.println(pluginClazz.getMemberKeys());

        ctx.close(true);
    }

    public interface JavaPlugin {
        void onEnable();
    }
}