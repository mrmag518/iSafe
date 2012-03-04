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

import java.util.List;
import com.mrmag518.iSafe.iSafe;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class ClearDrops implements CommandExecutor {
    public static iSafe plugin;
    public ClearDrops(iSafe instance)
    {
        plugin = instance;
    }
    public boolean message = true;
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){  
        if(cmd.getName().equalsIgnoreCase("cleardrops")){
            if (args.length > 0) {
                 sender.sendMessage(ChatColor.RED + "Too many arguments!");
                 return false;
            }
            if (sender instanceof Player) {
                Player player = (Player)sender;
                World world = player.getWorld();
                List<org.bukkit.entity.Entity> entity = world.getEntities();
                if (hasCleardrops(player)) {
                    ClearAllDrops(player, world, entity);
                } else {
                    sender.sendMessage(ChatColor.RED + "You do not have access to that.");
                }
            } else {
                sender.sendMessage("Cannot do that command from here.");
            }
            return true;
        }
        return false;
    }
    
    public void ClearAllDrops(Player player, World world, List<org.bukkit.entity.Entity> ent) {
        List<org.bukkit.entity.Entity> dropsList = world.getEntities();
        int cleareddrops = 0;
        
        for(org.bukkit.entity.Entity drops : dropsList) {
            if (drops instanceof LivingEntity) {
                //nothing
            } else {
                drops.remove();
                cleareddrops++;
                player.sendMessage(ChatColor.GRAY + "Cleard" + ChatColor.AQUA + dropsList.size() + " drops.");
            }
        }
    }
    
    public boolean hasCleardrops(Player player) {
        if (player.hasPermission("iSafe.cleardrops")) {
            return true;
        } else if (player.hasPermission("iSafe.*")) {
            return true;
        }
        return false;
    }
}
