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

package me.mrmag518.iSafe.Events;

import me.mrmag518.iSafe.*;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.TreeType;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.event.world.StructureGrowEvent;
import org.bukkit.event.world.WorldInitEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldSaveEvent;
import org.bukkit.event.world.WorldUnloadEvent;

public class WorldListener implements Listener {
    public static iSafe plugin;
    public WorldListener(iSafe instance)
    {
        plugin = instance;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    
    @EventHandler(priority = EventPriority.NORMAL)
    public void onWorldUnload(WorldUnloadEvent event) {
        World world = event.getWorld();
        
        if(plugin.getConfig().getBoolean("World.Register-world(s)-unload", true))
        {
            System.out.println(("[iSafe] Unloaded "+ (world.getName() + " succsesfully.")));
        }
    }
    
    @EventHandler(priority = EventPriority.NORMAL)
    public void onWorldSave(WorldSaveEvent event) {
        World world = event.getWorld();
        if(plugin.getConfig().getBoolean("World.Register-world(s)-save", true))
        {
            System.out.println(("[iSafe] Saved "+ (world.getName() + " succsesfully.")));
        }     
    }
    
    @EventHandler(priority = EventPriority.NORMAL)
    public void onWorldLoad(WorldLoadEvent event) {
        World world = event.getWorld();
        
        if(plugin.getConfig().getBoolean("World.Register-world(s)-load", true))
        {
            System.out.println(("[iSafe] Loaded "+ (world.getName() + " succsesfully.")));
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onWorldInit(WorldInitEvent event) {
        World world = event.getWorld();
        
        if(plugin.getConfig().getBoolean("World.Register-world(s)-init", true))
        {
            System.out.println(("[iSafe] Init "+ (world.getName() + " succsesfully.")));
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onChunkUnload(ChunkUnloadEvent event) {
        if (event.isCancelled())
        {
            return;
        }
        
        if(plugin.getConfig().getBoolean("Chunk.Prevent.unload-chunks(Use with caution)", true))
        {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onChunkLoad(ChunkLoadEvent event) {
        if(!plugin.getConfig().getBoolean("Chunk.Enable-Chunk-emergency-loader", true))
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
        
        Player player = event.getPlayer();
        
        if(plugin.getConfig().getBoolean("Structure.Prevent-strcuture-growth", true))
        {
            event.setCancelled(true);
        }
        
        if(plugin.getConfig().getBoolean("Structure.Prevent-bonemeal-usage", true))
        {
            if (event.isFromBonemeal()) {
                event.setCancelled(true);
                player.sendMessage(ChatColor.RED + "You cannot use bonemeal.");
            }
        }
        
        if(plugin.getConfig().getBoolean("Structure.Prevent-structure-growth.BIG_TREE", true))
        {
            if (event.getSpecies() == TreeType.BIG_TREE)
            {
                event.setCancelled(true);
            }
        }
        
        if(plugin.getConfig().getBoolean("Structure.Prevent-structure-growth.BIRCH", true))
        {
            if (event.getSpecies() == TreeType.BIRCH)
            {
                event.setCancelled(true);
            }
        }
        
        if(plugin.getConfig().getBoolean("Structure.Prevent-structure-growth.BROWN_MUSHROOM", true))
        {
            if (event.getSpecies() == TreeType.BROWN_MUSHROOM)
            {
                event.setCancelled(true);
            }
        }
        
        if(plugin.getConfig().getBoolean("Structure.Prevent-structure-growth.REDWOOD", true))
        {
            if (event.getSpecies() == TreeType.REDWOOD)
            {
                event.setCancelled(true);
            }
        }
        
        if(plugin.getConfig().getBoolean("Structure.Prevent-structure-growth.RED_MUSHROOM", true))
        {
            if (event.getSpecies() == TreeType.RED_MUSHROOM)
            {
                event.setCancelled(true);
            }
        }
        
        if(plugin.getConfig().getBoolean("Structure.Prevent-structure-growth.TALL_REDWOOD", true))
        {
            if (event.getSpecies() == TreeType.TALL_REDWOOD)
            {
                event.setCancelled(true);
            }
        }
        
        if(plugin.getConfig().getBoolean("Structure.Prevent-structure-growth.TREE", true))
        {
            if (event.getSpecies() == TreeType.TREE)
            {
                event.setCancelled(true);
            }
        }
    }
}
