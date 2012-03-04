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
                sender.sendMessage(ChatColor.RED + "Too many arguments!");
                return false;
            }
            if (sender instanceof Player) {
                Player player = (Player)sender;
                if (hasStopserver(player)) {
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
        Bukkit.getServer().broadcastMessage(ChatColor.GRAY + "Server shutting down ..");
        Bukkit.getServer().shutdown();
    }
}
