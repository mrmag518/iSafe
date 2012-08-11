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

package com.mrmag518.iSafe.Blacklists;

import java.util.ArrayList;
import java.util.List;

import com.mrmag518.iSafe.iSafe;

import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CommandBlacklist implements Listener {
    public static iSafe plugin;
    public CommandBlacklist(iSafe instance)
    {
        plugin = instance;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    int message = 0;
    
    @EventHandler
    public void CommandBlacklist(PlayerCommandPreprocessEvent event) {
        if (event.isCancelled()){
            return;
        }
        Player player = event.getPlayer();
        Server server = player.getServer();
        World world = player.getWorld();
        String command = event.getMessage().toLowerCase();
        String worldname = world.getName();
        String[] split = event.getMessage().split(" ");  
        String cmd = split[0].trim().substring(1).toLowerCase();
        
        final List<String> commands = new ArrayList<String>();
        
        if (plugin.getBlacklist().getList("Command.Blacklist", commands).contains(cmd.toLowerCase()) 
                || (plugin.getBlacklist().getList("Command.Blacklist", commands).contains(command.toLowerCase())))
        {
            if (!event.isCancelled())
            {
                final List<String> cmdworlds = plugin.getBlacklist().getStringList("Place.Worlds");
                
                if (plugin.getBlacklist().getList("Command.EnabledWorlds", cmdworlds).contains(worldname))
                {
                    event.setCancelled(true);
                    
                    if (plugin.getBlacklist().getBoolean("Command.Alert/log.ToConsole", true)){
                        if (event.isCancelled()) {
                            plugin.log.info("[iSafe]" + player.getName() + " was prevented from doing the blacklisted command: " + command);
                        }
                    }

                    if (plugin.getBlacklist().getBoolean("Command.Alert/log.ToPlayer", true)){
                        if (event.isCancelled()) {
                            player.sendMessage(plugin.blacklistCommandMsg(cmd, worldname));
                        }
                    }
                    
                    if (plugin.getBlacklist().getBoolean("Command.KickPlayer", true)){
                        if (event.isCancelled()) {
                            player.sendMessage(plugin.blacklistCommandKickMsg(cmd, worldname));
                        }
                    }

                    if(plugin.getBlacklist().getBoolean("Command.TotallyDisallowCommands", true)){
                        event.setCancelled(true);
                        player.sendMessage(ChatColor.RED + "Commands are disabled!");
                    }
                }
            }    
        }
    }
}
