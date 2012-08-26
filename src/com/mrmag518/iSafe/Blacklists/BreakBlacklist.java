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

import com.mrmag518.iSafe.iSafe;

import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BreakBlacklist implements Listener {
    public static iSafe plugin;
    public BreakBlacklist(iSafe instance)
    {
        plugin = instance;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    
    @EventHandler
    public void BreakBlacklist(BlockBreakEvent event) {
        if (event.isCancelled()){
            return;
        }
        
        Player p = event.getPlayer();
        Block b = event.getBlock();
        int blockID = b.getTypeId();
        String BlockNAME = b.getType().name().toLowerCase();
        World world = p.getWorld();
        String worldname = world.getName(); 
        
        if(plugin.getBlacklist().getList("Break.Blacklist").contains(blockID)
            || plugin.getBlacklist().getList("Break.Blacklist").contains(BlockNAME.toLowerCase())) 
        {
            if(!plugin.hasBlacklistPermission(p, "iSafe.bypass.blacklist.break")) 
            {
                if(plugin.getBlacklist().getList("Break.EnabledWorlds").contains(worldname)) 
                {
                    if (plugin.getBlacklist().getBoolean("Break.Gamemode.PreventFor.Survival", true)) {
                        if(p.getGameMode().equals(GameMode.SURVIVAL)) {
                            event.setCancelled(true);
                        }
                    }

                    if (plugin.getBlacklist().getBoolean("Break.Gamemode.PreventFor.Creative", true)) {
                        if(p.getGameMode().equals(GameMode.CREATIVE)) {
                            event.setCancelled(true);
                        }
                    }

                    if (plugin.getBlacklist().getBoolean("Break.KickPlayer", true)){
                        if (event.isCancelled()){
                            p.kickPlayer(plugin.blacklistBreakKickMsg(b));
                        }    
                    }

                    if(plugin.getBlacklist().getBoolean("Break.Alert/log.ToPlayer", true)) {
                        if(event.isCancelled()) {
                            p.sendMessage(plugin.blacklistBreakMsg(b));
                        }
                    }

                    if (plugin.getBlacklist().getBoolean("Break.Alert/log.ToConsole", true)){
                        if (event.isCancelled()) {
                            plugin.log.info("[iSafe]" + p.getName() + " was prevented from breaking the blacklisted block: " + BlockNAME);
                        }
                    }
                }
            }
        }
        
        if (plugin.getBlacklist().getBoolean("Break.TotallyDisableBlockBreak", true))
        {
            if(!plugin.hasPermission(p, "iSafe.bypass.break")) {
                event.setCancelled(true);
            }
        }
    }
}
