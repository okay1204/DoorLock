package me.screescree.doorlock;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;

public abstract class CustomCommand implements CommandExecutor {
    private static String NO_PERMS_MESSAGE = ChatColor.RED + "I'm sorry, but you do not have permission to perform this command. " +
    "Please contact the server administrators if you believe that this is a mistake.";
    private PluginCommand command;

    public CustomCommand(String commandName) {
        command = DoorLock.getInstance().getCommand(commandName);
        command.setExecutor(this);
    }

    public PluginCommand getCommand() {
        return command;
    }

    public String getNoPermissionMessage() {
        return command.getPermissionMessage() != null ? command.getPermissionMessage() : NO_PERMS_MESSAGE;
    }

    @Override
    public abstract boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3);
}
