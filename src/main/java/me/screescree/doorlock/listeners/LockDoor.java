package me.screescree.doorlock.listeners;

import java.util.UUID;

import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.persistence.PersistentDataContainer;

import com.jeff_media.customblockdata.CustomBlockData;

import me.screescree.doorlock.DoorLock;
import me.screescree.doorlock.persistenttype.PersistentDataType_BOOLEAN;
import me.screescree.doorlock.persistenttype.PersistentDataType_UUID;
import me.screescree.doorlock.utils.ColorFormat;
import me.screescree.doorlock.utils.LockUtil;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class LockDoor implements Listener {
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        // ignore if it wasn't a right click block
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        // ignore if player is not looking at a door
        Block doorBlock = LockUtil.getBottomDoorHalf(event.getClickedBlock());
        if (doorBlock == null) {
            return;
        }

        UUID keyLockUuid = event.getItem() != null ? LockUtil.getKeyLockUuid(event.getItem()) : null;
        // ignore second event call if player has a key in both hands
        if (event.getHand() == EquipmentSlot.OFF_HAND && LockUtil.getKeyLockUuid(event.getPlayer().getInventory().getItemInMainHand()) != null) {
            return;
        }

        if (keyLockUuid != null && event.getPlayer().isSneaking()) {
            PersistentDataContainer doorContainer = new CustomBlockData(doorBlock, DoorLock.getInstance());
            if (
                !doorContainer.has(new NamespacedKey(DoorLock.getInstance(), "LockUuid"), new PersistentDataType_UUID()) ||
                !doorContainer.get(new NamespacedKey(DoorLock.getInstance(), "LockUuid"), new PersistentDataType_UUID()).equals(keyLockUuid)
            ) {
                event.getPlayer().sendMessage(ColorFormat.colorize("&cThis key does not match the door."));
                event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                return;
            }

            // lock/unlock the door
            boolean locked = !doorContainer.get(new NamespacedKey(DoorLock.getInstance(), "Locked"), new PersistentDataType_BOOLEAN());
            doorContainer.set(new NamespacedKey(DoorLock.getInstance(), "Locked"), new PersistentDataType_BOOLEAN(), locked);

            // send action bar to player
            event.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ColorFormat.colorize("&eDoor is " + (locked ? "&cLocked" : "&aUnlocked"))));
            event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.BLOCK_CHEST_LOCKED, 1, 1);
        }
        else {
            PersistentDataContainer doorContainer = new CustomBlockData(doorBlock, DoorLock.getInstance());

            // if the Locked key is not set, the door isn't claimed by as player so we can ignore
            if (!doorContainer.has(new NamespacedKey(DoorLock.getInstance(), "Locked"), new PersistentDataType_BOOLEAN())) {
                return;
            }

            boolean locked = doorContainer.get(new NamespacedKey(DoorLock.getInstance(), "Locked"), new PersistentDataType_BOOLEAN());
            UUID lockUuid = doorContainer.get(new NamespacedKey(DoorLock.getInstance(), "LockUuid"), new PersistentDataType_UUID());

            // cancel the event if the door is locked and the player has no key
            if (locked && (keyLockUuid == null || !keyLockUuid.equals(lockUuid))) {
                event.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ColorFormat.colorize("&eDoor is &cLocked")));
                event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.BLOCK_CHEST_LOCKED, 1, 1);
                event.setCancelled(true);
            }
        }
    }
}