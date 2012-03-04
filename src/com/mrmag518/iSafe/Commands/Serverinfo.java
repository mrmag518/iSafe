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

import com.mrmag518.iSafe.iSafe;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Serverinfo implements CommandExecutor {
    public static iSafe plugin;
    public Serverinfo(iSafe instance)
    {
        plugin = instance;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){       
        if(cmd.getName().equalsIgnoreCase("serverinfo")){
            if (args.length > 0) {
                sender.sendMessage(ChatColor.RED + "Too many arguments!");
                return false;
            }
            if (sender instanceof Player) {
                Player player = (Player)sender;
                if (hasServerinfo(player)) { 
                    sender.sendMessage(ChatColor.GRAY + "Bukkit version: "+ ChatColor.AQUA + plugin.getServer().getBukkitVersion().toString());
                    sender.sendMessage(ChatColor.GRAY + "Server IP: "+ ChatColor.AQUA + plugin.getServer().getIp().toString());
                    sender.sendMessage(ChatColor.GRAY + "Server name: "+ ChatColor.AQUA + plugin.getServer().getName().toString());
                    sender.sendMessage(ChatColor.GRAY + "Server ID: "+ ChatColor.AQUA + plugin.getServer().getServerId().toString());
                    sender.sendMessage(ChatColor.GRAY + "Server version: "+ ChatColor.AQUA + plugin.getServer().getVersion().toString());
                    sender.sendMessage(ChatColor.GRAY + "Default GameMode: "+ ChatColor.AQUA + plugin.getServer().getDefaultGameMode().toString());
                    sender.sendMessage(ChatColor.GRAY + "Server port: "+ ChatColor.AQUA + plugin.getServer().getPort());
                    sender.sendMessage(ChatColor.GRAY + "Spawn radius: "+ ChatColor.AQUA + plugin.getServer().getSpawnRadius());
                } else {
                    sender.sendMessage(ChatColor.RED + "You do not have access to that.");
                }
            } else {
                sender.sendMessage(ChatColor.GRAY + "Bukkit version: "+ ChatColor.AQUA + plugin.getServer().getBukkitVersion().toString());
                    sender.sendMessage("Server IP: "+ plugin.getServer().getIp().toString());
                    sender.sendMessage("Server name: "+ plugin.getServer().getName().toString());
                    sender.sendMessage("Server ID: "+ plugin.getServer().getServerId().toString());
                    sender.sendMessage("Server version: "+ plugin.getServer().getVersion().toString());
                    sender.sendMessage("Default GameMode: "+ plugin.getServer().getDefaultGameMode().toString());
                    sender.sendMessage("Server port: "+ plugin.getServer().getPort());
                    sender.sendMessage("Spawn radius: "+ plugin.getServer().getSpawnRadius());
            }
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
}
