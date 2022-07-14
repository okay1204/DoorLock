package me.screescree.doorlock;

import org.bukkit.plugin.java.JavaPlugin;

import com.jeff_media.customblockdata.CustomBlockData;

import me.screescree.doorlock.commands.CreateKey;

public class DoorLock extends JavaPlugin
{
    private static DoorLock instance;

    @Override
    public void onEnable() {
        instance = this;

        CustomBlockData.registerListener(this);

        // custom commands
        new CreateKey();
    }
    
    @Override
    public void onDisable() {

    }

    public static DoorLock getInstance() {
        return instance;
    }
}
