package me.judge;

import org.apache.fury.Fury;
import org.apache.fury.config.Language;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;

import java.io.File;
import java.lang.reflect.InvocationTargetException;

public class Main {
    static Fury fury;

    static {
        fury = Fury.builder()
                .requireClassRegistration(false)
                .withLanguage(Language.JAVA)
                .build();
    }

    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        File testJar = new File("testjar.jar");
        Context ctx = Context.newBuilder("java")
                .option("java.Classpath", testJar.getAbsolutePath() + ":" + System.getProperty("java.class.path"))
                .allowAllAccess(true)
                .build();
        Value furyVal = ctx.getBindings("java").getMember("org.apache.fury.Fury");
        Value furyInstVal = furyVal.invokeMember("builder")
                .invokeMember("withLanguage", ctx.getBindings("java")
                        .getMember("org.apache.fury.config.Language")
                        .getMember("JAVA"))
                .invokeMember("build");
        Value clazz = ctx.getBindings("java").getMember("me.judge.Usage");
        Value byteVal = furyInstVal.invokeMember("serialize", clazz.getMember("class"));
        Class<?> pluginClazz = (Class<?>) fury.deserialize(byteVal.as(byte[].class));

        JavaPlugin plugin = (JavaPlugin) pluginClazz.getDeclaredConstructor().newInstance();
        plugin.onEnable();

        ctx.close();
    }

    public interface JavaPlugin {
        void onEnable();
    }
}