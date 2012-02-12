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

package me.mrmag518.iSafe.Blacklists;

import me.mrmag518.iSafe.iSafe;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class DropBlacklist implements Listener {
    public DropBlacklist() {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    
    public static iSafe plugin;
    public DropBlacklist(iSafe instance)
    {
        plugin = instance;
    }
    
    @EventHandler
    public void BlacklistDrop(PlayerDropItemEvent event) {
        if (event.isCancelled())
        {
            return;
        }
        
        Player player = event.getPlayer();
        Server server = player.getServer();
        World world = player.getWorld();
        
        int itemID = event.getItemDrop().getItemStack().getTypeId();
        String BlockNAME_Lowercase = event.getItemDrop().getItemStack().getType().name().toLowerCase();
        String BlockNAME_Uppercase = event.getItemDrop().getItemStack().getType().name().toUpperCase();
        String BlockNAME_Name = event.getItemDrop().getItemStack().getType().name();
        String BlockNAME = event.getItemDrop().getItemStack().toString();
        
        Location loc = player.getLocation();
        String worldname = world.getName();
        
        //Blacklist
        final List<Item> dropedblocks = new ArrayList<Item>();
        if (plugin.getBlacklist().getList("Drop.Blacklist", dropedblocks).contains(itemID)
                || plugin.getBlacklist().getList("Drop.Blacklist", dropedblocks).contains(BlockNAME_Lowercase)
                || plugin.getBlacklist().getList("Drop.Blacklist", dropedblocks).contains(BlockNAME_Uppercase)
                || plugin.getBlacklist().getList("Drop.Blacklist", dropedblocks).contains(BlockNAME)
                || plugin.getBlacklist().getList("Drop.Blacklist", dropedblocks).contains(BlockNAME_Name))
        {
            if(player.hasPermission("iSafe.drop.blacklist.bypass")) {
                //access
            } else {
                if (!event.isCancelled()) 
                {
                    final List<String> Dropworlds = plugin.getBlacklist().getStringList("Drop.Worlds");
                
                    if (plugin.getBlacklist().getList("Drop.Worlds", Dropworlds).contains(worldname))
                    {
                        event.setCancelled(true);
                    } else {
                        event.setCancelled(false);
                    }
                }
            }
            
            if (plugin.getBlacklist().getBoolean("Drop.Kick-Player", true))
            {
                if (event.isCancelled())
                {
                    player.kickPlayer(ChatColor.RED + "You got kicked for attempting to drop: "+ ChatColor.GRAY + event.getItemDrop().getItemStack().getType().name().toLowerCase());
                }    
            }
            
            if (plugin.getBlacklist().getBoolean("Place.Kill-Player", true))
            {
                if (event.isCancelled())
                {
                    player.setHealth(0);
                    KillAlertPlayer(player, event, worldname);
                }    
            }
            
            if (plugin.getBlacklist().getBoolean("Drop.Alert/log.To-console", true))
            {
                if (event.isCancelled()) 
                {
                    AlertConsole(player, event, loc, worldname);
                }
            }
            
            if (plugin.getBlacklist().getBoolean("Drop.Alert/log.To-player", true))
            {
                if (event.isCancelled()) 
                {
                    AlertPlayer(player, event, worldname);
                }
            }
            
            if (plugin.getBlacklist().getBoolean("Drop.Alert/log.To-server-chat", true))
            {
                if (event.isCancelled()) 
                {
                    AlertServer(server, event, worldname, player);
                }
            }
        }
        
        if (plugin.getBlacklist().getBoolean("Drop.Complete-Disallow-droping", true))
        {
            if (player.hasPermission("iSafe.drop")) {
                //access
            } else {
                event.setCancelled(true);
                player.sendMessage(ChatColor.RED + "You cannot drop objects.");
            }
        }
    }
    
    public void KillAlertPlayer(Player player, PlayerDropItemEvent event, String worldname) {
        player.sendMessage(ChatColor.RED + "You got killed for attempting to break: "+ ChatColor.GRAY + event.getItemDrop().getItemStack().getType().name().toLowerCase());
    }
    
    public void AlertPlayer(Player player, PlayerDropItemEvent event, String worldname) {
        player.sendMessage(ChatColor.RED + "You cannot drop: "+ ChatColor.GRAY + event.getItemDrop().getItemStack().getType().name().toLowerCase() + ChatColor.RED + " In the world: "+ ChatColor.GRAY + worldname);
    }
    
    public void AlertServer(Server server, PlayerDropItemEvent event, String worldname, Player player) {
        server.broadcastMessage(ChatColor.DARK_GRAY + player.getName() + " tried to drop: "+ ChatColor.RED + event.getItemDrop().getItemStack().getType().name().toLowerCase() + ChatColor.DARK_GRAY + " In the world: "+ ChatColor.RED + worldname);
    }
    
    public void AlertConsole(Player player, PlayerDropItemEvent event, Location loc, String worldname) {
        plugin.log.info("[iSafe] "+ player.getName() + " tried to drop: "+ event.getItemDrop().getItemStack().getType().name().toLowerCase() + ", At the location: "+ " X: "+ loc.getBlockX() +" Y: "+ loc.getBlockY() +" Z: "+ loc.getBlockZ()+ ", In the world: "+ worldname);
    }
}
