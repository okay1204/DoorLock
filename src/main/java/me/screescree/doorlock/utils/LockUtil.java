package me.screescree.doorlock.utils;

import javax.annotation.Nullable;

import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Bisected.Half;
import org.bukkit.block.data.type.Door;
import org.bukkit.entity.Player;

public class LockUtil {
    @Nullable
    public static Block getBottomDoorHalfLookingAt(Player player) {
        Block block = player.getTargetBlockExact(5);
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
}
