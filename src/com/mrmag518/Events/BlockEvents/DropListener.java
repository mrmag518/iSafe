package com.mrmag518.Events.BlockEvents;

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



import com.mrmag518.iSafe.iSafe;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class DropListener implements Listener {
    public static iSafe plugin;
    public DropListener(iSafe instance)
    {
        plugin = instance;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    
    @EventHandler
    public void onBlockPlace(BlockBreakEvent event) {
        if (event.isCancelled())
        {
            return;
        }
        
        Block block = event.getBlock();
        Player p = event.getPlayer();
        
        if (plugin.getConfig().getBoolean("ForceDrop.Glass", true)){
            if (block.getTypeId() == 20) {
                if(!(event.isCancelled())) {
                    if(plugin.hasPermission(p, "iSafe.forcedrop.glass")) {
                        ItemStack glass = new ItemStack(event.getBlock().getType(), 1);
                        event.getPlayer().getWorld().dropItemNaturally(event.getBlock().getLocation(), glass);
                        block.setType(Material.AIR);
                    }
                }
            }
        }
        
        if (plugin.getConfig().getBoolean("ForceDrop.MobSpawner", true)){
            if (block.getTypeId() == 52) {
                if(!(event.isCancelled())) {
                    if(plugin.hasPermission(p, "iSafe.forcedrop.mobspawner")) {
                        ItemStack mobspawner = new ItemStack(event.getBlock().getType(), 1);
                        event.getPlayer().getWorld().dropItemNaturally(event.getBlock().getLocation(), mobspawner);
                        block.setType(Material.AIR);
                    }
                }
            }
        }
        
        if (plugin.getConfig().getBoolean("ForceDrop.Ice", true)){
            if (block.getTypeId() == 79) {
                if(!(event.isCancelled())) {
                    if(plugin.hasPermission(p, "iSafe.forcedrop.ice")) {
                        ItemStack ice = new ItemStack(event.getBlock().getType(), 1);
                        event.getPlayer().getWorld().dropItemNaturally(event.getBlock().getLocation(), ice);
                        block.setType(Material.AIR);
                        block.setType(Material.AIR);
                    }
                }
            }
        }
        
        if (plugin.getConfig().getBoolean("ForceDrop.Bedrock", true)){
            if (block.getTypeId() == 7) {
                if(!(event.isCancelled())) {
                    if(plugin.hasPermission(p, "iSafe.forcedrop.bedrock")) {
                        ItemStack bedrock = new ItemStack(event.getBlock().getType(), 1);
                        event.getPlayer().getWorld().dropItemNaturally(event.getBlock().getLocation(), bedrock);
                        block.setType(Material.AIR);
                    }
                }
            }
        }
    }
}
