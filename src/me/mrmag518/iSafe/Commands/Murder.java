package me.mrmag518.iSafe.Commands;

import me.mrmag518.iSafe.iSafe;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Murder implements CommandExecutor {
    public static iSafe plugin;
    public Murder(iSafe instance)
    {
        plugin = instance;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
        Player player = (Player)sender;
        if(cmd.getName().equalsIgnoreCase("murder")) {
            
            if(args.length == 0) {
                if (hasSelf(player)) {
                    player.setHealth(0);
                    player.sendMessage(ChatColor.GRAY + "You have committed suicide.");
                } else {
                    player.sendMessage(ChatColor.RED + "You do not have access to that.");
                }
            } else if (args.length == 1) {
                if (player.getServer().getPlayer(args[0]) != null){
                    if (hasOthers(player)) {
                        Player aimPlayer = player.getServer().getPlayer(args[0]);
                        aimPlayer.setHealth(0);
                        player.sendMessage(ChatColor.GREEN + "You murdered "+ ChatColor.DARK_GRAY + aimPlayer.getName() + "!");
                        aimPlayer.sendMessage(ChatColor.RED + "You got murdered by "+ ChatColor.DARK_GRAY + sender.getName());
                    } else {
                        player.sendMessage(ChatColor.RED + "That player is offline.");
                    }
                } else if (args.length > 1) {
                    player.sendMessage(ChatColor.RED + "Too many arguments!");
                }
            }
            return true;
        }
        return false;
    }
    
    public boolean hasSelf(Player player) {
        if (player.hasPermission("isafe.murder.self")) {
            return true;
        } else if  (player.hasPermission("iSafe.*")) {
            return true;
        }
        return false;
    }
    
    public boolean hasOthers(Player player) {
        if (player.hasPermission("isafe.murder.others")) {
            return true;
        } else if  (player.hasPermission("iSafe.*")) {
            return true;
        }
        return false;
    }
}
