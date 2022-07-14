package me.screescree.doorlock.commands;

import java.util.List;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;

import com.jeff_media.customblockdata.CustomBlockData;

import me.screescree.doorlock.CustomCommand;
import me.screescree.doorlock.DoorLock;
import me.screescree.doorlock.persistenttype.PersistentDataType_UUID;
import me.screescree.doorlock.utils.ColorFormat;
import me.screescree.doorlock.utils.LockUtil;

public class CreateKey extends CustomCommand {
    public CreateKey() {
        super("createkey");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // get the door that the player is looking at
        Player player;
        if (sender instanceof Player) {
            player = (Player) sender;
        }
        else {
            sender.sendMessage(ColorFormat.colorize("&cThis command can only be used by a player."));
            return true;
        }

        Block doorBlock = LockUtil.getBottomDoorHalfLookingAt(player);
        if (doorBlock == null) {
            sender.sendMessage(ColorFormat.colorize("&cYou must be looking at a door."));
            return true;
        }

        int amount = 1;
        if (args.length > 0) {
            try {
                amount = Integer.parseInt(args[0]);
            }
            catch (NumberFormatException e) {
                sender.sendMessage(ColorFormat.colorize("&cThe amount must be an integer between 1 and 64."));
                return true;
            }

            if (amount < 1 || amount > 64) {
                sender.sendMessage(ColorFormat.colorize("&cThe amount must be an integer between 1 and 64."));
                return true;
            }
        }


        // get the LockUuid from the door, otherwise assign the door a LockUuid if it doesn't already have one
        UUID lockUUID;
        
        PersistentDataContainer container = new CustomBlockData(doorBlock, DoorLock.getInstance());
        if (container.has(new NamespacedKey(DoorLock.getInstance(), "LockUuid"), new PersistentDataType_UUID())) {
            // if the door owner is not the executor of the command, then they can't create a key for the door
            if (!container.get(new NamespacedKey(DoorLock.getInstance(), "OwnerUuid"), new PersistentDataType_UUID()).equals(player.getUniqueId())) {
                sender.sendMessage(ColorFormat.colorize("&cA key created by another player already exists for this door."));
                return true;
            }

            lockUUID = container.get(new NamespacedKey(DoorLock.getInstance(), "LockUuid"), new PersistentDataType_UUID());
        }
        else {
            lockUUID = UUID.randomUUID();
            container.set(new NamespacedKey(DoorLock.getInstance(), "LockUuid"), new PersistentDataType_UUID(), lockUUID);
            container.set(new NamespacedKey(DoorLock.getInstance(), "OwnerUuid"), new PersistentDataType_UUID(), player.getUniqueId());
        }

        // create a key for the door
        ItemStack key = new ItemStack(Material.TRIPWIRE_HOOK, amount);
        
        ItemMeta keyMeta = key.getItemMeta();
        keyMeta.setDisplayName(ColorFormat.colorize("&eKey"));
        keyMeta.setLore(List.of(ColorFormat.colorize("&7A key for a door.")));
        keyMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
        keyMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        
        PersistentDataContainer keyContainer = keyMeta.getPersistentDataContainer();
        keyContainer.set(new NamespacedKey(DoorLock.getInstance(), "LockUuid"), new PersistentDataType_UUID(), lockUUID);

        key.setItemMeta(keyMeta);

        // give the key to the player
        player.getInventory().addItem(key);

        sender.sendMessage(ColorFormat.colorize("&a" + (amount == 1 ? "A key" : amount + " keys") + " for this door has been placed in your inventory."));

        return true;
    }
}
