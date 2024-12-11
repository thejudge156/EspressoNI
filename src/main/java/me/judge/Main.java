package me.judge;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Engine;
import org.graalvm.polyglot.Value;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        File testJar = new File("testjar.jar");
        Engine engine = Engine.newBuilder()
                .allowExperimentalOptions(true)
                .option("compiler.Inlining", "false")
                .option("engine.WarnInterpreterOnly", "false")
                .build();
        try(Context ctx = Context.newBuilder("java")
                .allowAllAccess(true)
                .option("java.Properties.java.class.path", testJar.getAbsolutePath())
                .engine(engine)
                .build()) {
            ctx.getBindings("java").putMember("me.judge.JavaPlugin", JavaPlugin.class);
            Value pluginInstance = ctx.getBindings("java").getMember("me.judge.Usage").invokeMember("getDeclaredConstructor").invokeMember("newInstance");
            JavaPlugin plugin = pluginInstance.as(JavaPlugin.class);
            plugin.onEnable();
        }
    }

    public abstract static class JavaPlugin {
        public abstract void onEnable();
    }
}