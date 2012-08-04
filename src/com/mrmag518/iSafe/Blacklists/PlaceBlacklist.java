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
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class PlaceBlacklist implements Listener {
    public static iSafe plugin;
    public PlaceBlacklist(iSafe instance)
    {
        plugin = instance;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    int message = 0;
    
    @EventHandler
    public void PlaceBlacklist(BlockPlaceEvent event) {
        if (event.isCancelled())
        {
            return;
        }
        Player p = event.getPlayer();
        Block block = event.getBlock();
        Server server = p.getServer();
        
        int blockID = event.getBlock().getTypeId();
        String BlockNAME = event.getBlock().getType().name().toLowerCase();
        
        World world = p.getWorld();
        Location loc = p.getLocation();
        String worldname = world.getName();
        
        //Blacklist
        final List<Block> placedblocks = new ArrayList<Block>();
        
        if (plugin.getBlacklist().getList("Place.Blacklist", placedblocks).contains(blockID)
                || plugin.getBlacklist().getList("Place.Blacklist", placedblocks).contains(BlockNAME.toLowerCase()))
        {
            if(!plugin.hasBlacklistPermission(p, "iSafe.bypass.blacklist.place")) 
            {
                if (!event.isCancelled()) 
                {
                    final List<String> worlds = plugin.getBlacklist().getStringList("Place.Worlds");
                    
                    if (plugin.getBlacklist().getList("Place.EnabledWorlds", worlds).contains(worldname))
                    {
                        if (plugin.getBlacklist().getBoolean("Place.Gamemode.PreventFor.Survival", true)) {
                            if(p.getGameMode().equals(GameMode.SURVIVAL)) {
                                if(!(block == null)) {
                                    event.setCancelled(true);
                                }
                            }
                        } else if (plugin.getBlacklist().getBoolean("Place.Gamemode.PreventFor.Creative", true)) {
                            if(p.getGameMode().equals(GameMode.CREATIVE)) {
                                if(!(block == null)) {
                                    event.setCancelled(true);
                                }
                            }
                        }
                        
                        if (plugin.getBlacklist().getBoolean("Place.Kick-Player", true)){
                            if (event.isCancelled()) {
                                p.kickPlayer(plugin.blacklistPlaceKickMsg(block));
                            }    
                        }

                        if (plugin.getBlacklist().getBoolean("Place.Alert/log.To-console", true)){
                            if (event.isCancelled()) {
                                //AlertConsole(p, block, loc, worldname);
                            }
                        }

                        if (plugin.getBlacklist().getBoolean("Place.Alert/log.To-player", true)){
                            if (event.isCancelled()) {
                                p.sendMessage(plugin.blacklistPlaceMsg(block));
                            }
                        }

                        if (plugin.getBlacklist().getBoolean("Place.Alert/log.To-server-chat", true)){
                            if (event.isCancelled()) {
                                //AlertServer(server, block, worldname, p);
                            }
                        }
                    }
                }
            }
        }
        
        if (plugin.getBlacklist().getBoolean("Place.TotallyDisableBlockPlace", true))
        {
            if(!plugin.hasPermission(p, "iSafe.bypass.place")) {
                event.setCancelled(true);
            }
        }
    }
}
