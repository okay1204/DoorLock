package me.screescree.doorlock.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import me.screescree.doorlock.utils.LockUtil;

public class NoKeyPlace implements Listener {
    @EventHandler(ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
        // prevents players from placing the tripwire hook down
        if (LockUtil.getKeyLockUuid(event.getItemInHand()) != null) {
            event.setCancelled(true);
        }
    }
}
