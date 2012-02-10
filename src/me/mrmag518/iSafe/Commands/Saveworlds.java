package me.mrmag518.iSafe.Commands;

import me.mrmag518.iSafe.iSafe;

import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Saveworlds implements CommandExecutor {
    public static iSafe plugin;
    public Saveworlds(iSafe instance)
    {
        plugin = instance;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
        if(cmd.getName().equalsIgnoreCase("save-worlds")){
            if (args.length > 0) {
                sender.sendMessage(ChatColor.RED + "To many arguments!");
                return false;
            }
            if (sender instanceof Player) {
                Player player = (Player)sender;
                World world = player.getWorld();
                Server server = player.getServer();
                if (hasSaveWorlds(player)) { //player
                    world.save();
                    sender.sendMessage(ChatColor.GRAY + "Saved worlds successfully");
                } else {
                    sender.sendMessage(ChatColor.RED + "You do not have access to that.");
                }
            } else {
                sender.sendMessage("[iSafe] Cannot save worlds trough console.");
            }
            return true;
        }
        return false;
    }
    
    public boolean hasSaveWorlds(Player player) {
        if (player.hasPermission("iSafe.saveworlds")) {
            return true;
        } else if  (player.hasPermission("iSafe.*")) {
            return true;
        }
        return false;
    }
}
