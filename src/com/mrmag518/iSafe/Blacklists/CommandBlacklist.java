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
        if (event.isCancelled())
        {
            return;
        }
        
        Player player = event.getPlayer();
        Server server = player.getServer();
        World world = player.getWorld();
        
        String command_lowercase = event.getMessage().toLowerCase();
        String command_upercase = event.getMessage().toUpperCase();
        String command = event.getMessage().toString();
        String command_raw = event.getMessage();
        
        String worldname = world.getName();
        
        final List<String> commands = new ArrayList<String>();
        if (plugin.getBlacklist().getList("Command.Blacklist", commands).contains(command_lowercase)
                || plugin.getBlacklist().getList("Command.Blacklist", commands).contains(command_upercase)
                || plugin.getBlacklist().getList("Command.Blacklist", commands).contains(command)
                || plugin.getBlacklist().getList("Command.Blacklist", commands).contains(command_raw))
        {
            if (!event.isCancelled())
            {
                final List<String> cmdworlds = plugin.getBlacklist().getStringList("Place.Worlds");
                
                if (plugin.getBlacklist().getList("Command.Worlds", cmdworlds).contains(worldname))
                {
                    event.setCancelled(true);
                    
                    if (plugin.getBlacklist().getBoolean("Command.Alert/log.To-console", true))
                    {
                        if (event.isCancelled()) {
                            plugin.log.info("[iSafe] "+ player.getName() + " tried to do the command: "+ command);
                        }
                    }

                    if (plugin.getBlacklist().getBoolean("Command.Alert/log.To-player", true))
                    {
                        if (event.isCancelled()) {
                            player.sendMessage(ChatColor.RED + "You cannot do the command: "+ command);
                        }
                    }

                    if (plugin.getBlacklist().getBoolean("Command.Alert/log.To-server-chat", true))
                    {
                        if (event.isCancelled()) {
                            server.broadcastMessage(ChatColor.DARK_GRAY + player.getName() + " tried to do the command: "+ command);
                        }
                    }

                    if(plugin.getBlacklist().getBoolean("Command.Disallow-commands", true))
                    {
                        event.setCancelled(true);
                        player.sendMessage(ChatColor.RED + "All commands are disabled.");
                    }
                }
            }    
        }
    }
}
