package me.screescree.doorlock;

import org.bukkit.plugin.java.JavaPlugin;

public class DoorLock extends JavaPlugin
{
    private static DoorLock instance;

    @Override
    public void onEnable() {
        instance = this;
    }
    
    @Override
    public void onDisable() {

    }

    public static DoorLock getInstance() {
        return instance;
    }
}
