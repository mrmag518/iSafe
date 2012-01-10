package me.mrmag518.iSafe;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;

public class iSafeCommandExecutor implements CommandExecutor {
    //iSafe is a plugin yes. (get the main class)
    public static iSafe plugin;
    public iSafeCommandExecutor(iSafe instance)
    {
        plugin = instance;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
        //Reload the config
        if(cmd.getName().equalsIgnoreCase("iSafe-reload")){
            if (args.length > 0) {
                return false;
            }
            PluginDescriptionFile pdffile = plugin.getDescription();
            if (sender instanceof Player) { 
                Player player = (Player)sender;
                if (hasReload(player)) { //player
                    plugin.reloadConfig();
		    sender.sendMessage(ChatColor.AQUA + pdffile.getFullName() + ChatColor.GRAY + " reloaded succesfully");
		    plugin.log.info("[iSafe] " + pdffile.getFullName() + " reloaded succesfully");
                    System.out.println("[iSafe] "+ (sender.getName() + " reloaded" + (pdffile.getFullName())));
            } else { //no permission
                    sender.sendMessage(ChatColor.RED + "You do not have access to that." );
                }
            } else { //console
                plugin.reloadConfig();
		plugin.log.info("[iSafe] " + pdffile.getName() + " was succesfully reloaded");
            }
    		return true;
    	}
        
        //Info command
        if(cmd.getName().equalsIgnoreCase("iSafe-info")){
            if (args.length > 0) {
                return false;
            }
            PluginDescriptionFile pdffile = plugin.getDescription();
            if (sender instanceof Player) {
                Player player = (Player)sender;
                if (hasInfo(player)) { //player
                    sender.sendMessage(ChatColor.GRAY + "Name: "+ ChatColor.AQUA + pdffile.getName());
                    sender.sendMessage(ChatColor.GRAY + "Version: "+ ChatColor.AQUA + pdffile.getVersion());
                    sender.sendMessage(ChatColor.GRAY + "FullName: "+ ChatColor.AQUA + pdffile.getFullName());
                    sender.sendMessage(ChatColor.GRAY + "Authors: "+ ChatColor.AQUA + "mrmag518");
                    sender.sendMessage(ChatColor.GRAY + "CraftBukkit build:: "+ ChatColor.AQUA + "1.0.0-R1");
                    System.out.println("[iSafe] "+ (sender.getName() + " did the information command."));
                } else { //no permission
                    sender.sendMessage(ChatColor.RED + "You do not have access to that." );
                }
            } else { //console
                sender.sendMessage("Name: "+ pdffile.getName());
                sender.sendMessage("Version: "+ pdffile.getVersion());
                sender.sendMessage("FullName: "+ pdffile.getFullName());
                sender.sendMessage("Authors: "+ "mrmag518");
                sender.sendMessage("CraftBukkit build: "+ "1.0.1 -R1");
            }
            return true;
        }
        
        //Commands commands
        if(cmd.getName().equalsIgnoreCase("iSafe-commands")){
            if (args.length > 0) {
                return false;
            }
            if (sender instanceof Player) {
                Player player = (Player)sender;
                if (hasCommandslist(player)) { //player
                    sender.sendMessage(ChatColor.DARK_GRAY + "--------------------" + ChatColor.GREEN + "iSafe Commands" + ChatColor.DARK_GRAY + "--------------------");
                    sender.sendMessage(ChatColor.AQUA + "iSafe-reload" + ChatColor.GREEN + " - " + ChatColor.GRAY + "Reload the iSafe configuration file.");
                    sender.sendMessage(ChatColor.AQUA + "iSafe-info" + ChatColor.GREEN + " - " + ChatColor.GRAY + "Show information about iSafe.");
                    sender.sendMessage(ChatColor.AQUA + "iSafe-commands" + ChatColor.GREEN + " - " + ChatColor.GRAY + "Show the commands for iSafe.");
                    sender.sendMessage(ChatColor.AQUA + "serverinfo" + ChatColor.GREEN + " - " + ChatColor.GRAY + "Show info about the server.");
                    System.out.println("[iSafe] "+ (sender.getName() + " did the command list command."));
                } else { //no permission
                    sender.sendMessage(ChatColor.RED + "You do not have access to that." );
                }
            } else { //console
                    sender.sendMessage("--------------------" + "iSafe Commands" + "--------------------");
                    sender.sendMessage("iSafe-reload - " + "Reload the iSafe configuration file.");
                    sender.sendMessage("iSafe-info - " + "Show information about iSafe.");
                    sender.sendMessage("iSafe-commands - " + "Show the commands for iSafe.");
                    sender.sendMessage("serverinfo - " + "Show info about the server.");
            }
            return true;
        }
        
        //Server-info command
        if(cmd.getName().equalsIgnoreCase("serverinfo")){
            if (args.length > 0) {
                return false;
            }
            if (sender instanceof Player) {
                Player player = (Player)sender;
                if (hasServerinfo(player)) { //player
                    sender.sendMessage(ChatColor.GRAY + "Bukkit version: "+ ChatColor.AQUA + plugin.getServer().getBukkitVersion());
                    sender.sendMessage(ChatColor.GRAY + "Server IP: "+ ChatColor.AQUA + plugin.getServer().getIp());
                    sender.sendMessage(ChatColor.GRAY + "Server name: "+ ChatColor.AQUA + plugin.getServer().getName());
                    sender.sendMessage(ChatColor.GRAY + "Server ID: "+ ChatColor.AQUA + plugin.getServer().getServerId());
                    sender.sendMessage(ChatColor.GRAY + "Server version: "+ ChatColor.AQUA + plugin.getServer().getVersion());
                    sender.sendMessage(ChatColor.GRAY + "Default GameMode: "+ ChatColor.AQUA + plugin.getServer().getDefaultGameMode());
                    sender.sendMessage(ChatColor.GRAY + "Server port: "+ ChatColor.AQUA + plugin.getServer().getPort());
                    sender.sendMessage(ChatColor.GRAY + "Spawn radius: "+ ChatColor.AQUA + plugin.getServer().getSpawnRadius());
                    System.out.println("[iSafe] "+ (sender.getName() + " did the serverinfo command."));
                } else {
                    sender.sendMessage(ChatColor.RED + "You do not have access to that.");
                }
            } else {
                sender.sendMessage(ChatColor.GRAY + "Bukkit version: "+ ChatColor.AQUA + plugin.getServer().getBukkitVersion());
                    sender.sendMessage("Server IP: "+ plugin.getServer().getIp());
                    sender.sendMessage("Server name: "+ plugin.getServer().getName());
                    sender.sendMessage("Server ID: "+ plugin.getServer().getServerId());
                    sender.sendMessage("Server version: "+ plugin.getServer().getVersion());
                    sender.sendMessage("Default GameMode: "+ plugin.getServer().getDefaultGameMode());
                    sender.sendMessage("Server port: "+ plugin.getServer().getPort());
                    sender.sendMessage("Spawn radius: "+ plugin.getServer().getSpawnRadius());
            }
            return true;
        }
        
        //Superbreak command
        if(cmd.getName().equalsIgnoreCase("superbreak")){
            if (args.length > 0) {
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
        //healme command
        if(cmd.getName().equalsIgnoreCase("healme")){
            if (args.length > 0) {
                return false;
            }
            if (sender instanceof Player) {
                Player player = (Player)sender;
                if (hasHealme(player)) { //player
                    sender.sendMessage(ChatColor.GRAY + "You healed yourself");
                    player.setFoodLevel(20);
                    player.setHealth(20);
                    player.setFireTicks(0);
                } else {
                    sender.sendMessage(ChatColor.RED + "You do not have access to that.");
                }
            } else {
                sender.sendMessage("[iSafe] You cannot do that from here.");
            }
            return true;
        }
        return false;
    }
    
    //Permissions
    public boolean hasReload(Player player) {
            if (player.hasPermission("iSafe.reload")) {
                return true;
            } else if  (player.hasPermission("iSafe.*")) {
                return true;
            }
            return false;
        }
    public boolean hasInfo(Player player) {
            if (player.hasPermission("iSafe.info")) {
                return true;
            } else if (player.hasPermission("iSafe.*")) {
                return true;
            }
            return false;
        }
    public boolean hasCommandslist(Player player) {
            if (player.hasPermission("iSafe.commandlist")) {
               return true; 
            } else if (player.hasPermission("iSafe.*")) {
                return true;
            }
            return false;
        }
    public boolean hasServerinfo(Player player) {
            if (player.hasPermission("isafe.serverinfo")) {
                return true;
            } else if (player.hasPermission("iSafe.*")) {
                return true;
            }
            return false;
        }
    public boolean hasHealme(Player player) {
            if (player.hasPermission("iSafe.heal")) {
                return true;
            } else if  (player.hasPermission("iSafe.*")) {
                return true;
            }
            return false;
        }
}
