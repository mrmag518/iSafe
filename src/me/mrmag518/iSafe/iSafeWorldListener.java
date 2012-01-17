package me.mrmag518.iSafe;

import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.TreeType;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.event.world.StructureGrowEvent;
import org.bukkit.event.world.WorldInitEvent;
import org.bukkit.event.world.WorldListener;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldSaveEvent;
import org.bukkit.event.world.WorldUnloadEvent;

public class iSafeWorldListener extends WorldListener {
    public static iSafe plugin;
    public iSafeWorldListener(iSafe instance)
    {
        plugin = instance;
    }
    
    @Override
    public void onWorldUnload(WorldUnloadEvent event) {
        World world = event.getWorld();
        
        if(plugin.config.getBoolean("World.Register-world(s)-unload", true))
        {
            System.out.println(("[iSafe] Unloaded "+ (world.getName() + " succsesfully.")));
        }
    }
    
    @Override
    public void onWorldSave(WorldSaveEvent event) {
        World world = event.getWorld();
        if(plugin.config.getBoolean("World.Register-world(s)-save", true))
        {
            System.out.println(("[iSafe] Saved "+ (world.getName() + " succsesfully.")));
        }     
    }
    
    @Override
    public void onWorldLoad(WorldLoadEvent event) {
        World world = event.getWorld();
        
        if(plugin.config.getBoolean("World.Register-world(s)-load", true))
        {
            System.out.println(("[iSafe] Loaded "+ (world.getName() + " succsesfully.")));
        }
    }

    @Override
    public void onWorldInit(WorldInitEvent event) {
        World world = event.getWorld();
        
        if(plugin.config.getBoolean("World.Register-world(s)-init", true))
        {
            System.out.println(("[iSafe] Init "+ (world.getName() + " succsesfully.")));
        }
    }

    @Override
    public void onChunkUnload(ChunkUnloadEvent event) {
        if (event.isCancelled())
        {
            return;
        }
        
        World world = event.getWorld();
        Chunk chunk = event.getChunk();
        
        if(plugin.config.getBoolean("Chunk.Prevent.unload-chunks(Use with caution)", true))
        {
            event.setCancelled(true);
        }
    }

    @Override
    public void onChunkLoad(ChunkLoadEvent event) {
        if(!plugin.config.getBoolean("Chunk.Enable-Chunk-emergency-loader", true))
        {
            event.getChunk().load();
        }
    }

    @Override
    public void onStructureGrow(StructureGrowEvent event) {
        if (event.isCancelled())
        {
            return;
        }
        
        Player player = event.getPlayer();
        World world = event.getWorld();
        Location loc = event.getLocation();
        
        if(plugin.config.getBoolean("Structure.Prevent-strcuture-growth", true))
        {
            event.setCancelled(true);
        }
        
        if(plugin.config.getBoolean("Structure.Prevent-bonemeal-usage", true))
        {
            if (event.isFromBonemeal()) {
                event.setCancelled(true);
                player.sendMessage(ChatColor.RED + "You cannot use bonemeal.");
            }
        }
        
        if(plugin.config.getBoolean("Structure.Prevent-structure-growth.BIG_TREE", true))
        {
            if (event.getSpecies() == TreeType.BIG_TREE)
            {
                event.setCancelled(true);
            }
        }
        
        if(plugin.config.getBoolean("Structure.Prevent-structure-growth.BIRCH", true))
        {
            if (event.getSpecies() == TreeType.BIRCH)
            {
                event.setCancelled(true);
            }
        }
        
        if(plugin.config.getBoolean("Structure.Prevent-structure-growth.BROWN_MUSHROOM", true))
        {
            if (event.getSpecies() == TreeType.BROWN_MUSHROOM)
            {
                event.setCancelled(true);
            }
        }
        
        if(plugin.config.getBoolean("Structure.Prevent-structure-growth.REDWOOD", true))
        {
            if (event.getSpecies() == TreeType.REDWOOD)
            {
                event.setCancelled(true);
            }
        }
        
        if(plugin.config.getBoolean("Structure.Prevent-structure-growth.RED_MUSHROOM", true))
        {
            if (event.getSpecies() == TreeType.RED_MUSHROOM)
            {
                event.setCancelled(true);
            }
        }
        
        if(plugin.config.getBoolean("Structure.Prevent-structure-growth.TALL_REDWOOD", true))
        {
            if (event.getSpecies() == TreeType.TALL_REDWOOD)
            {
                event.setCancelled(true);
            }
        }
        
        if(plugin.config.getBoolean("Structure.Prevent-structure-growth.TREE", true))
        {
            if (event.getSpecies() == TreeType.TREE)
            {
                event.setCancelled(true);
            }
        }
    }
}
