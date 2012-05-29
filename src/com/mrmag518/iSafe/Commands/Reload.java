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

public class Reload implements CommandExecutor {
    public static iSafe plugin;
    public Reload(iSafe instance)
    {
        plugin = instance;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
        if(cmd.getName().equalsIgnoreCase("iSafe-reload")){
            if (args.length > 0) {
                sender.sendMessage(ChatColor.RED + "Too many arguments!");
                return false;
            }
            String v = plugin.getDescription().getVersion();
            if (sender instanceof Player) { 
                Player player = (Player)sender;
                if (hasReload(player)) { 
                    if(!(plugin.getDataFolder().exists())) {
                        plugin.getDataFolder().mkdirs();
                        sender.sendMessage(ChatColor.GRAY + "iSafe folder not found, created a new one.");
                    }
                    
                    plugin.reloadBlacklist();
                    plugin.reloadConfig();
                    plugin.reloadMobsConfig();
                    sender.sendMessage(ChatColor.GOLD + "Reloaded iSafe" + ChatColor.DARK_PURPLE + " (v" + v + ")");
            } else { 
                    sender.sendMessage(ChatColor.RED + "You do not have access to that." );
               }
            } else {
                if(!(plugin.getDataFolder().exists())) {
                    plugin.getDataFolder().mkdirs();
                    sender.sendMessage("iSafe folder not found, created a new one.");
                }
                
                plugin.reloadBlacklist();
                plugin.reloadConfig();
                plugin.reloadMobsConfig();
                sender.sendMessage("Reloaded iSafe" +" (v" + v + ")");
            }
            return true;
    	}
        return false;
    }
    
    public boolean hasReload(Player player) {
        if (player.hasPermission("iSafe.reload")) {
            return true;
        } else if  (player.hasPermission("iSafe.*")) {
            return true;
        }
        return false;
    }
}
