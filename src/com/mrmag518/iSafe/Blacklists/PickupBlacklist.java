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
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;

public class PickupBlacklist implements Listener {
    public static iSafe plugin;
    public PickupBlacklist(iSafe instance)
    {
        plugin = instance;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    int message = 0;
    
    @EventHandler
    public void BlacklistPickup(PlayerPickupItemEvent event) {
        if (event.isCancelled())
        {
            return;
        }
        
        Player player = event.getPlayer();
        Server server = player.getServer();
        World world = player.getWorld();
        
        int itemID = event.getItem().getItemStack().getTypeId();
        String BlockNAME = event.getItem().getItemStack().getType().name().toLowerCase();
        
        Location loc = player.getLocation();
        String worldname = world.getName();
        
        //Blacklist
        final List<Item> pickupedblocks = new ArrayList<Item>();
        if (plugin.getBlacklist().getList("Pickup.Blacklist", pickupedblocks).contains(itemID)
                || plugin.getBlacklist().getList("Pickup.Blacklist", pickupedblocks).contains(BlockNAME.toLowerCase()))
        {
            if(!plugin.hasBlacklistPermission(player, "iSafe.bypass.blacklist.pickup")) 
            {
                if (!event.isCancelled()) 
                {
                    final List<String> Pickupworlds = plugin.getBlacklist().getStringList("Pickup.Worlds");
                    
                    if (plugin.getBlacklist().getList("Pickup.EnabledWorlds", Pickupworlds).contains(worldname))
                    {
                        if (plugin.getBlacklist().getBoolean("Pickup.Gamemode.PreventFor.Survival", true)) {
                            if(player.getGameMode().equals(GameMode.SURVIVAL)) {
                                event.setCancelled(true);
                            }
                        } else if (plugin.getBlacklist().getBoolean("Pickup.Gamemode.PreventFor.Creative", true)) {
                            if(player.getGameMode().equals(GameMode.CREATIVE)) {
                                event.setCancelled(true);
                            }
                        }
                        if (plugin.getBlacklist().getBoolean("Pickup.Kick-Player", true))
                        {
                            if (event.isCancelled())
                            {
                                player.kickPlayer(ChatColor.RED + "You got kicked for attempting to pickup: "+ ChatColor.GRAY + event.getItem().getItemStack().getType().name().toLowerCase());
                            }    
                        }

                        if (plugin.getBlacklist().getBoolean("Pickup.Alert/log.To-console", true))
                        {
                            if (event.isCancelled()) 
                            {
                                AlertConsole(player, event, worldname, loc);
                            }
                        }

                        if (plugin.getBlacklist().getBoolean("Pickup.Alert/log.To-server-chat", true))
                        {
                            if (event.isCancelled()) 
                            {
                                if (message == 0) {
                                    server.broadcastMessage(ChatColor.DARK_GRAY + player.getName() + " tried to pickup: "+ event.getItem().getItemStack().getType().name().toLowerCase());
                                    message = 1;
                                }
                            }
                        }
                    }
                }
            }
        }
        
        if (plugin.getBlacklist().getBoolean("Pickup.TotallyDisableBlockPickup", true))
        {
            if(!plugin.hasPermission(player, "iSafe.bypass.pickup")) {
                event.setCancelled(true);
            }
        }
    }
    
    private void AlertConsole(Player player, PlayerPickupItemEvent event, String worldname, Location loc) {
        if (message == 0) {
            plugin.log.info("[iSafe] "+ player.getName() + " tried to pickup: "+ event.getItem().getItemStack().getType().name().toLowerCase() + ", At the location: "+ " X: "+ loc.getBlockX() +" Y: "+ loc.getBlockY() +" Z: "+ loc.getBlockZ()+ ", In the world: "+ worldname);
            message = 1;
        }
    }
}
