package me.mrmag518.iSafe.Commands;

import java.util.Iterator;
import java.util.List;
import me.mrmag518.iSafe.iSafe;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Rules implements CommandExecutor {
    public static iSafe plugin;
    public Rules(iSafe instance)
    {
        plugin = instance;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
        if(cmd.getName().equalsIgnoreCase("therules")){
            if (args.length > 0) {
                sender.sendMessage(ChatColor.RED + "To many arguments!");
                return false;
            }
            if (sender instanceof Player) { 
                Player player = (Player)sender;
                if (hasRules(player)) { //player
                    List<String> rules = plugin.getRules().getStringList("Rules");
                    Iterator<String> iter = rules.iterator();
                    while (iter.hasNext()) {
                        sender.sendMessage(ChatColor.GRAY + iter.next());
                    }
                    return true;
                } else { //no permission
                    sender.sendMessage(ChatColor.RED + "You do not have access to that.");
                }
            } else { //console
                List<String> rules = plugin.getRules().getStringList("Rules");
                Iterator<String> iter = rules.iterator();
                while (iter.hasNext()) {
                    sender.sendMessage(iter.next());
                }
            }
            return true;
        }
        return false;
    }
    
    public boolean hasRules(Player player) {
        if (player.hasPermission("iSafe.rules")) {
            return true;
        } else if  (player.hasPermission("iSafe.*")) {
            return true;
        }
        return false;
    }
}
