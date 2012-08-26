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
        World world = player.getWorld();
        String sentence = event.getMessage().toLowerCase();
        String worldname = world.getName();
        
        
        for(String command : plugin.getBlacklist().getStringList("Command.Blacklist")) 
        {
            if(sentence.startsWith(command.toLowerCase())) 
            {
                if(!plugin.hasBlacklistPermission(player, "iSafe.bypass.blacklist.command")) 
                {
                    if(plugin.getBlacklist().getList("Command.EnabledWorlds").contains(worldname)) 
                    {
                        if (plugin.getBlacklist().getBoolean("Command.Alert/log.ToConsole", true)){
                            if (event.isCancelled()) {
                                plugin.log.info("[iSafe]" + player.getName() + " was prevented from doing the blacklisted command: " + command);
                            }
                        }

                        if (plugin.getBlacklist().getBoolean("Command.Alert/log.ToPlayer", true)){
                            if (event.isCancelled()) {
                                player.sendMessage(plugin.blacklistCommandMsg(command, worldname));
                            }
                        }

                        if (plugin.getBlacklist().getBoolean("Command.KickPlayer", true)){
                            if (event.isCancelled()) {
                                player.sendMessage(plugin.blacklistCommandKickMsg(command, worldname));
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
}
