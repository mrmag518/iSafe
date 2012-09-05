package com.mrmag518.iSafe.Commands;

import com.mrmag518.iSafe.iSafe;
import java.io.File;
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
                    sender.sendMessage(G + "/iSafe resetfile <filename> " + GR + " Reset the specified file to it's default settings.");
                } else if (args.length >= 1) {
                    if(args[0].equalsIgnoreCase("reload")) {
                        if(plugin.hasPermission(sender, "iSafe.command.reload")) {
                            return reload(sender);
                        }
                    } else if(args[0].equalsIgnoreCase("info")) {
                        if(plugin.hasPermission(sender, "iSafe.command.info")) {
                            return info(sender);
                        }
                    } else if(args[0].equalsIgnoreCase("serverinfo")) {
                        if(plugin.hasPermission(sender, "iSafe.command.serverinfo")) {
                            return serverinfo(sender);
                        }
                    } else if(args[0].equalsIgnoreCase("resetFile")) {
                        if(plugin.hasPermission(sender, "iSafe.command.resetfile")) {
                            return resetFile(sender, args);
                        }
                    }
                }
            } else {
                if(args.length == 0) {
                    sender.sendMessage(A + "---iSafe Commands---");
                    sender.sendMessage(G + "/iSafe reload" + GR + " Reload all the iSafe configuration files.");
                    sender.sendMessage(G + "/iSafe info " + GR + " Returns information about iSafe.");
                    sender.sendMessage(G + "/iSafe serverinfo " + GR + " Returns information about the server.");
                    sender.sendMessage(G + "/iSafe resetfile <filename> " + GR + " Reset the specified file to it's default settings.");
                } else {
                    if (args.length >= 1) {
                        if(args[0].equalsIgnoreCase("reload")) {
                            return reload(sender);
                        } else if(args[0].equalsIgnoreCase("info")) {
                            return info(sender);
                        } else if(args[0].equalsIgnoreCase("serverinfo")) {
                            return serverinfo(sender);
                        } else if(args[0].equalsIgnoreCase("resetFile")) {
                            return resetFile(sender, args);
                        }
                    }
                }
            }
        }
        return false;
    }
    
    private boolean resetFile(CommandSender sender, String[] args) {
        if(args.length <= 1) {
            sender.sendMessage(ChatColor.RED + "Invalid usage of arguments.");
        } else {
            if(args.length == 2) {
                if(args[1].equalsIgnoreCase("config.yml")) {
                    File file = new File(plugin.getDataFolder(), "config.yml");
                    sender.sendMessage(ChatColor.GREEN + "Resetting config.yml ..");
                    if(file.exists()) {
                        file.delete();
                    } else {
                        sender.sendMessage(ChatColor.RED + "The file config.yml was not found!");
                        return true;
                    }
                } else if(args[1].equalsIgnoreCase("blacklists.yml")) {
                    File file = new File(plugin.getDataFolder(), "blacklists.yml");
                    sender.sendMessage(ChatColor.GREEN + "Resetting blacklists.yml ..");
                    if(file.exists()) {
                        file.delete();
                    } else {
                        sender.sendMessage(ChatColor.RED + "The file blacklists.yml was not found!");
                        return true;
                    }
                } else if(args[1].equalsIgnoreCase("creatureManager.yml")) {
                    File file = new File(plugin.getDataFolder(), "creatureManager.yml");
                    sender.sendMessage(ChatColor.GREEN + "Resetting creatureManager.yml ..");
                    if(file.exists()) {
                        file.delete();
                    } else {
                        sender.sendMessage(ChatColor.RED + "The file creatureManager.yml was not found!");
                        return true;
                    }
                } else if(args[1].equalsIgnoreCase("ISafeConfig.yml")) {
                    File file = new File(plugin.getDataFolder(), "ISafeConfig.yml");
                    sender.sendMessage(ChatColor.GREEN + "Resetting ISafeConfig.yml ..");
                    if(file.exists()) {
                        file.delete();
                    } else {
                        sender.sendMessage(ChatColor.RED + "The file ISafeConfig.yml was not found!");
                        return true;
                    }
                } else if(args[1].equalsIgnoreCase("Messages.yml")) {
                    File file = new File(plugin.getDataFolder(), "Messages.yml");
                    sender.sendMessage(ChatColor.GREEN + "Resetting Messages.yml ..");
                    if(file.exists()) {
                        file.delete();
                    } else {
                        sender.sendMessage(ChatColor.RED + "The file Messages.yml was not found!");
                        return true;
                    }
                }
            }
            plugin.fileLoadManagement();
            sender.sendMessage(ChatColor.GREEN + "Resetted File!");
        }
        return true;
    }
    
    private boolean reload(CommandSender sender) {
        String v = plugin.getDescription().getVersion();
        sender.sendMessage(G + "Reloading all iSafe " + v + " files ..");
        
        try{
            plugin.fileLoadManagement();
        }catch(Exception e) {
            sender.sendMessage(ChatColor.RED + "An issue ocurred while reloading iSafe!");
            e.printStackTrace();
        }
        
        sender.sendMessage(G + "Reloaded all iSafe " + v + " files.");
        return true;
    }
    
    private boolean info(CommandSender sender) {
        ChatColor AQ = ChatColor.AQUA;
        ChatColor W = ChatColor.WHITE;
        PluginDescriptionFile pdf = plugin.getDescription();
        sender.sendMessage(AQ + "--- iSafe Information ---");
        sender.sendMessage(AQ + "Author: " + W + "mrmag518");
        sender.sendMessage(AQ + "Testers: " + W + "domingo15 and Gunnerrrrr");
        sender.sendMessage(AQ + "Version: " + W + pdf.getFullName());
        sender.sendMessage(AQ + "Minecraft version: " + W + "1.3.*");
        sender.sendMessage(AQ + "BukkitDev link: " + W + "http://dev.bukkit.org/server-mods/blockthattnt/");
        return true;
    }
    
    private boolean serverinfo(CommandSender sender) {
        ChatColor AQ = ChatColor.AQUA;
        ChatColor W = ChatColor.WHITE;
        sender.sendMessage(AQ + "Bukkit version: "+ W + plugin.getServer().getBukkitVersion().toString());
        sender.sendMessage(AQ + "Server Address: "+ W + plugin.getServer().getIp().toString());
        sender.sendMessage(AQ + "Server name: "+ W + plugin.getServer().getName().toString());
        sender.sendMessage(AQ + "Default GameMode: "+ W + plugin.getServer().getDefaultGameMode().toString());
        sender.sendMessage(AQ + "Server port: "+ W + plugin.getServer().getPort());
        sender.sendMessage(AQ + "Spawn radius: "+ W + plugin.getServer().getSpawnRadius());
        sender.sendMessage(AQ + "Number of worlds: "+ W + plugin.getServer().getWorlds().size());
        return true;
    }
}
