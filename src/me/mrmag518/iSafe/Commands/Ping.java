package me.mrmag518.iSafe.Commands;

import me.mrmag518.iSafe.iSafe;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Ping implements CommandExecutor {
    public static iSafe plugin;
    public Ping(iSafe instance)
    {
        plugin = instance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
        if(cmd.getName().equalsIgnoreCase("ping")){
            if (args.length > 0) {
                sender.sendMessage(ChatColor.RED + "To many arguments!");
                return false;
            }
            if (sender instanceof Player) { 
                Player player = (Player)sender;
                if (hasPing(player)) { //player
                    sender.sendMessage(ChatColor.AQUA + "PONG!");
                } else {
                    sender.sendMessage(ChatColor.RED + "You do not have access to that.");
                }
            } else {
                sender.sendMessage("PONG!");
            }
            return true;
        }
        return false;
    }
    
    public boolean hasPing(Player player) {
        if (player.hasPermission("iSafe.ping")) {
            return true;
        } else if  (player.hasPermission("iSafe.*")) {
            return true;
        }
        return false;
    }
}
