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
package com.mrmag518.iSafe.Util;

import com.mrmag518.iSafe.iSafe;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class SendUpdate implements Listener {
    public static iSafe plugin;
    public SendUpdate(iSafe instance) {
        plugin = instance;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void handleUpdate(PlayerJoinEvent event) {
        Player p = event.getPlayer();
        
        if(PermHandler.hasPermission(p, "iSafe.admin", false) || p.isOp()) {
            if(plugin.updateFound) {
                p.sendMessage(ChatColor.GREEN + "A new version of iSafe is out! ("+ ChatColor.WHITE +  plugin.versionFound + ChatColor.GREEN + ")");
                p.sendMessage(ChatColor.GREEN + "Current iSafe version running: " + ChatColor.WHITE + plugin.getDescription().getFullName() + ChatColor.GREEN + ".");
                p.sendMessage(ChatColor.GREEN + "It's highly recommended to update, as there may be important fixes or improvements to the plugin!");
            }
        }
    }
}
