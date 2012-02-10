package me.mrmag518.iSafe.Commands;

import me.mrmag518.iSafe.iSafe;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Magictxt implements CommandExecutor {
    public static iSafe plugin;
    public Magictxt(iSafe instance)
    {
        plugin = instance;
    }
    int message = 0;
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
        if(cmd.getName().equalsIgnoreCase("magictxt")){
            if (args.length > 0) {
                sender.sendMessage(ChatColor.RED + "To many arguments!");
                return false;
            }
            if (sender instanceof Player) { 
                Player player = (Player)sender;
                if (hasMagictxt(player)) { //player
                    sender.sendMessage(ChatColor.MAGIC + "This text is to magicly written to be known!");
                    message = 10;
                } else {
                    sender.sendMessage(ChatColor.RED + "You do not have access to that .");
                }
            } else {
                sender.sendMessage("Console no contain magic powerz :(");
            }
            return true;
        }
        return false;
    }
    
    public boolean hasMagictxt(Player player) {
        if (player.hasPermission("iSafe.magictxt")) {
            return true;
        } else if  (player.hasPermission("iSafe.*")) {
            return true;
        }
        return false;
    }
}
