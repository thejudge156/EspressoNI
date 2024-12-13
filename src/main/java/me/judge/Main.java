package me.judge;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        File testJar = new File("testjar.jar");
        Context ctx = Context.newBuilder("java")
                .option("java.Classpath", testJar.getAbsolutePath())
                .allowAllAccess(true)
                .build();
        Value pluginClazz = ctx.getBindings("java.lang.Class").invokeMember("forName", "me.judge.Usage", false, ClassLoader.getPlatformClassLoader());
        System.out.println(pluginClazz.getMemberKeys());

        ctx.close(true);
    }

    public interface JavaPlugin {
        void onEnable();
    }
}