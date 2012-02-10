package me.mrmag518.iSafe.Commands;

import me.mrmag518.iSafe.iSafe;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Stopserver implements CommandExecutor {
    public static iSafe plugin;
    public Stopserver(iSafe instance)
    {
        plugin = instance;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){  
        if(cmd.getName().equalsIgnoreCase("stopserver")){
            if (args.length > 0) {
                sender.sendMessage(ChatColor.RED + "To many arguments!");
                return false;
            }
            
            if (sender instanceof Player) {
                Player player = (Player)sender;
                if (hasStopserver(player)) { //player
                    stopServer();
                } else {
                    player.sendMessage(ChatColor.RED + "You do not have access to that.");
                }
            } else {
                stopServer();
            }
            return true;
        }
        return false;
    }
    
    public boolean hasStopserver(Player player) {
        if (player.hasPermission("iSafe.stopserver")) {
            return true;
        } else if (player.hasPermission("iSafe.*")) {
            return true;
        }
        return false;
    }
    
    public void stopServer() {
        Bukkit.getServer().broadcastMessage(ChatColor.DARK_AQUA + "Server shutting down ..");
        Bukkit.getServer().shutdown();
    }
}
