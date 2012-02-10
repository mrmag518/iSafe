package me.mrmag518.iSafe.Commands;

import me.mrmag518.iSafe.iSafe;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Superbreak implements CommandExecutor {
    public static iSafe plugin;
    public Superbreak(iSafe instance)
    {
        plugin = instance;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){       
        if(cmd.getName().equalsIgnoreCase("superbreak")){
            if (args.length > 0) {
                sender.sendMessage(ChatColor.RED + "To many arguments!");
                return false;
            }
            //No suitable boolean permission. Use the normal one.
            if (sender.hasPermission("iSafe.superbreak")) {
                if (plugin.superbreak.contains((Player) sender)) {
                    sender.sendMessage(ChatColor.GRAY + "SuperBreak: "+ ChatColor.AQUA + "OFF");
                    plugin.superbreak.remove((Player) sender);
                } else {
                    sender.sendMessage(ChatColor.GRAY + "SuperBreak: "+ ChatColor.AQUA + "ON");
                    plugin.superbreak.add((Player) sender);
                }
            } else {
                sender.sendMessage(ChatColor.RED + "You do not have access to that.");
                plugin.superbreak.remove((Player) sender);
            }
            return true;
        }
        return false;
    }
}
