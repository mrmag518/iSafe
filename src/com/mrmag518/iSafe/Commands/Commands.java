/*
 * iSafe
 * Copyright (C) 2011-2012 mrmag518 <magnusaub@yahoo.no>
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.mrmag518.iSafe.Commands;

import com.mrmag518.iSafe.Util.PermHandler;
import com.mrmag518.iSafe.iSafe;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;

public class Commands implements CommandExecutor {
    public static iSafe plugin;
    public Commands(iSafe instance)
    {
        plugin = instance;
    }
    public ChatColor A = ChatColor.AQUA;
    public ChatColor G = ChatColor.GREEN;
    public ChatColor GR = ChatColor.GRAY;
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String l, String[] args){
        if(l.equalsIgnoreCase("iSafe")) {
            if(sender instanceof Player) {
                if(args.length == 0) {
                    sender.sendMessage(A + "---iSafe Commands---");
                    sender.sendMessage(G + "/iSafe reload" + GR + " Reloads all the iSafe configuration files.");
                    sender.sendMessage(G + "/iSafe info " + GR + " Returns information about iSafe.");
                    sender.sendMessage(G + "/iSafe serverinfo " + GR + " Returns information about the server.");
                } else if (args.length >= 1) {
                    if(args[0].equalsIgnoreCase("reload")) {
                        if(PermHandler.hasPermission(sender, "iSafe.command.reload")) {
                            reload(sender);
                        }
                    } else if(args[0].equalsIgnoreCase("info")) {
                        if(PermHandler.hasPermission(sender, "iSafe.command.info")) {
                            displayInfo(sender);
                        }
                    } else if(args[0].equalsIgnoreCase("serverinfo")) {
                        if(PermHandler.hasPermission(sender, "iSafe.command.serverinfo")) {
                            displayServerinfo(sender);
                        }
                    } else if(args[0].equalsIgnoreCase("updatecheck")) {
                        if(PermHandler.hasPermission(sender, "iSafe.command.updatecheck")) {
                            updateCheck(sender);
                        }
                    }
                }
            } else {
                if(args.length == 0) {
                    sender.sendMessage(A + "---iSafe Commands---");
                    sender.sendMessage(G + "/iSafe reload" + GR + " Reload all the iSafe configuration files.");
                    sender.sendMessage(G + "/iSafe info " + GR + " Returns information about iSafe.");
                    sender.sendMessage(G + "/iSafe serverinfo " + GR + " Returns information about the server.");
                } else {
                    if (args.length >= 1) {
                        if(args[0].equalsIgnoreCase("reload")) {
                            reload(sender);
                        } else if(args[0].equalsIgnoreCase("info")) {
                            displayInfo(sender);
                        } else if(args[0].equalsIgnoreCase("serverinfo")) {
                            displayServerinfo(sender);
                        } else if(args[0].equalsIgnoreCase("updatecheck")) {
                            updateCheck(sender);
                        }
                    }
                }
            }
            return true;
        }
        return false;
    }
    
    private void updateCheck(CommandSender sender) {
        if(plugin.updateFound) {
            sender.sendMessage(ChatColor.RED + "-----------------------------------------------------");
            sender.sendMessage(ChatColor.GREEN + "There's a new version of iSafe available!");
            sender.sendMessage(ChatColor.GREEN + "Version running: " + ChatColor.WHITE + plugin.currentVersion);
            sender.sendMessage(ChatColor.GREEN + "Version found: " + ChatColor.WHITE + plugin.versionFound);
            sender.sendMessage(ChatColor.RED + "-----------------------------------------------------");
        } else {
            sender.sendMessage(ChatColor.RED + "-----------------------------------------------------");
            sender.sendMessage(ChatColor.GREEN + "No new version of iSafe was found.");
            sender.sendMessage(ChatColor.RED + "-----------------------------------------------------");
        }
    }
    
    private void reload(CommandSender sender) {
        String v = plugin.getDescription().getVersion();
        sender.sendMessage(G + "Reloading all iSafe " + v + " files ..");
        
        try {
            plugin.fileLoadManagement();
        } catch(Exception e) {
            sender.sendMessage(ChatColor.RED + "An issue ocurred while reloading iSafe!");
            e.printStackTrace();
        }
        
        sender.sendMessage(G + "Reloaded all iSafe " + v + " files.");
    }
    
    private void displayInfo(CommandSender sender) {
        ChatColor AQ = ChatColor.AQUA;
        ChatColor W = ChatColor.WHITE;
        PluginDescriptionFile pdf = plugin.getDescription();
        sender.sendMessage(AQ + "--- iSafe Information ---");
        sender.sendMessage(AQ + "Author: " + W + "mrmag518");
        sender.sendMessage(AQ + "Testers: " + W + "domingo15 and Gunnerrrrr");
        sender.sendMessage(AQ + "Version: " + W + pdf.getFullName());
        sender.sendMessage(AQ + "Minecraft version: " + W + plugin.MCVersion);
        sender.sendMessage(AQ + "BukkitDev link: " + W + "http://dev.bukkit.org/server-mods/blockthattnt/");
    }
    
    private void displayServerinfo(CommandSender sender) {
        ChatColor AQ = ChatColor.AQUA;
        ChatColor W = ChatColor.WHITE;
        sender.sendMessage(AQ + "Bukkit version: "+ W + plugin.getServer().getBukkitVersion());
        sender.sendMessage(AQ + "Server address: "+ W + plugin.getServer().getIp());
        sender.sendMessage(AQ + "Server name: "+ W + plugin.getServer().getName());
        sender.sendMessage(AQ + "Default gamemode: "+ W + plugin.getServer().getDefaultGameMode().name().toLowerCase());
        sender.sendMessage(AQ + "Server port: "+ W + plugin.getServer().getPort());
        sender.sendMessage(AQ + "Spawn radius: "+ W + plugin.getServer().getSpawnRadius());
        sender.sendMessage(AQ + "Loaded worlds: "+ W + plugin.getServer().getWorlds().size());
    }
}
