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

package me.mrmag518.iSafe.Commands;

import me.mrmag518.iSafe.iSafe;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;

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
                sender.sendMessage(ChatColor.RED + "To many arguments!");
                return false;
            }
            PluginDescriptionFile pdffile = plugin.getDescription();
            if (sender instanceof Player) { 
                Player player = (Player)sender;
                if (hasReload(player)) { //player
                    plugin.reloadConfig();
                    plugin.reloadBlacklist();
                    plugin.reloadMobsConfig();
		    sender.sendMessage(ChatColor.AQUA + pdffile.getFullName() + ChatColor.GRAY + " reloaded files succesfully");
                    System.out.println("[iSafe] "+ (sender.getName() + " reloaded" + (pdffile.getFullName())));
            } else { //no permission
                    sender.sendMessage(ChatColor.RED + "You do not have access to that." );
                }
            } else { //console
                plugin.reloadConfig();
                plugin.reloadBlacklist();
                plugin.reloadMobsConfig();
		plugin.log.info("[iSafe] " + pdffile.getName() + " was succesfully reloaded");
            }
    		return true;
    	}
        if(cmd.getName().equalsIgnoreCase("iSafe-reload-blacklist")){
            if (args.length > 0) {
                sender.sendMessage(ChatColor.RED + "To many arguments!");
                return false;
            }
            PluginDescriptionFile pdffile = plugin.getDescription();
            if (sender instanceof Player) { 
                Player player = (Player)sender;
                if (hasReload(player)) { //player
                    plugin.reloadBlacklist();
		    sender.sendMessage(ChatColor.AQUA + pdffile.getFullName() + ChatColor.GRAY + " succesfully reloaded the blacklist file.");
                    System.out.println("[iSafe] "+ sender.getName() + " reloaded the blacklist file.");
            } else { //no permission
                    sender.sendMessage(ChatColor.RED + "You do not have access to that." );
                }
            } else { //console
                plugin.reloadBlacklist();
		plugin.log.info("[iSafe] " + pdffile.getName() + " succesfully reloaded the blacklist file.");
            }
    		return true;
        }
        if(cmd.getName().equalsIgnoreCase("iSafe-reload-mobsconfig")){
            if (args.length > 0) {
                sender.sendMessage(ChatColor.RED + "To many arguments!");
                return false;
            }
            PluginDescriptionFile pdffile = plugin.getDescription();
            if (sender instanceof Player) { 
                Player player = (Player)sender;
                if (hasReload(player)) { //player
                    plugin.reloadMobsConfig();
		    sender.sendMessage(ChatColor.AQUA + pdffile.getFullName() + ChatColor.GRAY + " succesfully reloaded the mobsConfig file.");
                    System.out.println("[iSafe] "+ sender.getName() + " reloaded the mobsConfig file.");
            } else { //no permission
                    sender.sendMessage(ChatColor.RED + "You do not have access to that." );
                }
            } else { //console
                plugin.reloadMobsConfig();
		plugin.log.info("[iSafe] " + pdffile.getName() + " succesfully reloaded the mobsConfig file.");
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
