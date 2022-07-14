package me.screescree.doorlock;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.jeff_media.customblockdata.CustomBlockData;

import me.screescree.doorlock.commands.CreateKey;
import me.screescree.doorlock.listeners.NoKeyPlace;

public class DoorLock extends JavaPlugin
{
    private static DoorLock instance;

    @Override
    public void onEnable() {
        instance = this;

        CustomBlockData.registerListener(this);

        // listeners
        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new NoKeyPlace(), this);

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
