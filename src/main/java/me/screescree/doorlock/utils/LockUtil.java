package me.screescree.doorlock.utils;

import java.util.UUID;

import javax.annotation.Nullable;

import org.bukkit.NamespacedKey;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Bisected.Half;
import org.bukkit.block.data.type.Door;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;

import me.screescree.doorlock.DoorLock;
import me.screescree.doorlock.persistenttype.PersistentDataType_UUID;

public class LockUtil {
    @Nullable
    public static Block getBottomDoorHalfLookingAt(Player player) {
        Block block = player.getTargetBlockExact(5);
        return getBottomDoorHalf(block);
    }

    @Nullable
    public static Block getBottomDoorHalf(Block block) {
        if (block == null || !Tag.WOODEN_DOORS.isTagged(block.getType())) {
            return null;
        }

        Door door = (Door) block.getBlockData();
        if (door.getHalf() == Half.TOP) {
            block = block.getRelative(BlockFace.DOWN);

            if (!Tag.WOODEN_DOORS.isTagged(block.getType())) {
                return null;
            }
        }

        return block;
    }

    @Nullable
    public static UUID getKeyLockUuid(ItemStack itemStack) {
        if (itemStack == null) {
            return null;
        }

        if (itemStack.getItemMeta() == null) {
            return null;
        }

        PersistentDataContainer container = itemStack.getItemMeta().getPersistentDataContainer();
        if (!container.has(new NamespacedKey(DoorLock.getInstance(), "LockUuid"), new PersistentDataType_UUID())) {
            return null;
        }
        else {
            return container.get(new NamespacedKey(DoorLock.getInstance(), "LockUuid"), new PersistentDataType_UUID());
        }
    }
}
