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

public class Info implements CommandExecutor {
public static iSafe plugin;
    public Info(iSafe instance)
    {
        plugin = instance;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
        if(cmd.getName().equalsIgnoreCase("iSafe-info")){
            if (args.length > 0) {
                sender.sendMessage(ChatColor.RED + "Too many arguments!");
                return false;
            }
            PluginDescriptionFile pdffile = plugin.getDescription();
            if (sender instanceof Player) {
                Player player = (Player)sender;
                if (hasInfo(player)) {
                    sender.sendMessage(ChatColor.GRAY + "Name: "+ ChatColor.AQUA + pdffile.getName());
                    sender.sendMessage(ChatColor.GRAY + "Version: "+ ChatColor.AQUA + pdffile.getVersion());
                    sender.sendMessage(ChatColor.GRAY + "FullName: "+ ChatColor.AQUA + pdffile.getFullName());
                    sender.sendMessage(ChatColor.GRAY + "Authors: "+ ChatColor.AQUA + "mrmag518");
                    sender.sendMessage(ChatColor.GRAY + "Minecraft compitability: "+ ChatColor.AQUA + "1.1");
                    System.out.println("[iSafe] "+ (sender.getName() + " did the information command."));
                } else { 
                    sender.sendMessage(ChatColor.RED + "You do not have access to that." );
                }
            } else {
                sender.sendMessage("Name: "+ pdffile.getName());
                sender.sendMessage("Version: "+ pdffile.getVersion());
                sender.sendMessage("FullName: "+ pdffile.getFullName());
                sender.sendMessage("Authors: "+ "mrmag518");
                sender.sendMessage("Minecraft compitability: "+ "1.1");
            }
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
}
