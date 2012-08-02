package com.mrmag518.Events.WorldEvents;

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



import com.mrmag518.iSafe.*;

import org.bukkit.TreeType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.event.world.PortalCreateEvent;
import org.bukkit.event.world.PortalCreateEvent.CreateReason;
import org.bukkit.event.world.StructureGrowEvent;

public class WorldListener implements Listener {
    public static iSafe plugin;
    public WorldListener(iSafe instance)
    {
        plugin = instance;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    
    @EventHandler
    public void portalManagement(PortalCreateEvent event) {
        if(plugin.getConfig().getBoolean("World.DisablePortalGeneration", true)) {
            if(event.getReason() == CreateReason.OBC_DESTINATION) {
                event.setCancelled(true);
            }
        }
    }
    
    @EventHandler(priority = EventPriority.NORMAL)
    public void onChunkUnload(ChunkUnloadEvent event) {
        if (event.isCancelled())
        {
            return;
        }
        
        if(plugin.getConfig().getBoolean("World.PreventChunkUnload", true))
        {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onChunkLoad(ChunkLoadEvent event) {
        if(!plugin.getConfig().getBoolean("World.MakeISafeLoadChunks", true))
        {
            event.getChunk().load();
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onStructureGrow(StructureGrowEvent event) {
        if (event.isCancelled())
        {
            return;
        }
        
        if(plugin.getConfig().getBoolean("World.DisableStructureGrowth", true)) {
            event.setCancelled(true);
        }
        
        if(plugin.getConfig().getBoolean("World.PreventBonemealUsage", true)) {
            if (event.isFromBonemeal()) {
                Player p = event.getPlayer();
                if(!p.hasPermission("iSafe.use.bonemeal")) {
                    event.setCancelled(true);
                    plugin.noPermission(p);
                }
            }
        }
        
        if(plugin.getConfig().getBoolean("TreeGrowth.DisableFor.BigTree", true)) {
            if (event.getSpecies() == TreeType.BIG_TREE)
            {
                event.setCancelled(true);
            }
        }
        
        if(plugin.getConfig().getBoolean("TreeGrowth.DisableFor.Birch", true)) {
            if (event.getSpecies() == TreeType.BIRCH)
            {
                event.setCancelled(true);
            }
        }
        
        if(plugin.getConfig().getBoolean("TreeGrowth.DisableFor.BrownMushroom", true)) {
            if (event.getSpecies() == TreeType.BROWN_MUSHROOM)
            {
                event.setCancelled(true);
            }
        }
        
        if(plugin.getConfig().getBoolean("TreeGrowth.DisableFor.Redwood", true))
        {
            if (event.getSpecies() == TreeType.REDWOOD)
            {
                event.setCancelled(true);
            }
        }
        
        if(plugin.getConfig().getBoolean("TreeGrowth.DisableFor.RedMushroom", true))
        {
            if (event.getSpecies() == TreeType.RED_MUSHROOM)
            {
                event.setCancelled(true);
            }
        }
        
        if(plugin.getConfig().getBoolean("TreeGrowth.DisableFor.TallRedwood", true))
        {
            if (event.getSpecies() == TreeType.TALL_REDWOOD)
            {
                event.setCancelled(true);
            }
        }
        
        if(plugin.getConfig().getBoolean("TreeGrowth.DisableFor.Tree", true))
        {
            if (event.getSpecies() == TreeType.TREE)
            {
                event.setCancelled(true);
            }
        }
        
        if(plugin.getConfig().getBoolean("TreeGrowth.DisableFor.Jungle", true))
        {
            if (event.getSpecies() == TreeType.JUNGLE)
            {
                event.setCancelled(true);
            }
        }
    }
}
