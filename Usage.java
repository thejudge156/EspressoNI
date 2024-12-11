package me.judge;

import me.judge.Main.JavaPlugin;

public class Usage implements JavaPlugin {
    public void onEnable() {
        System.out.println("Hello! This is a JavaPlugin instance in a different jar!");
    }
}