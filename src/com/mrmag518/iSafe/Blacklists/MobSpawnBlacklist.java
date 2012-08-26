package com.mrmag518.iSafe.Blacklists;

import com.mrmag518.iSafe.iSafe;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

public class MobSpawnBlacklist implements Listener {
    public static iSafe plugin;
    public MobSpawnBlacklist(iSafe instance)
    {
        plugin = instance;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    
    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        if (event.isCancelled()){
            return;
        }
        
        int entityID = event.getEntity().getEntityId();
        String eName = event.getEntity().getType().getName().toLowerCase();
        World world = event.getEntity().getWorld();
        Location loc = event.getEntity().getLocation();
        String worldname = world.getName();
        
        /**
         * Spawn reason: Natural.
         */
        
        if (event.getSpawnReason() == SpawnReason.NATURAL)
        {
            if(plugin.getCreatureManager().getList("MobSpawn.Natural.Blacklist").contains(entityID)
                || plugin.getCreatureManager().getList("MobSpawn.Natural.Blacklist").contains(eName.toLowerCase())) 
            {
                if(plugin.getCreatureManager().getList("MobSpawn.Natural.EnabledWorlds").contains(worldname)) 
                {
                    event.setCancelled(true);
                    event.getEntity().remove();
                    if (plugin.getCreatureManager().getBoolean("MobSpawn.Natural.Debug.ToConsole", true))
                    {
                        int x = (int) loc.getX();
                        int y = (int) loc.getY();
                        int z = (int) loc.getZ();
                        plugin.log.info("[iSafe]" + " A(n) " + eName + " was cancelled its spawn, for the spawn reason: Natural; at the location: "+ "X: "+ (x) + " Y: " + (y) + " Z: "+ (z));
                    }
                }
            }
        }
        
        /**
         * Spawn reason: Spawner.
         */
        if (event.getSpawnReason() == SpawnReason.SPAWNER)
        {
            if(plugin.getCreatureManager().getList("MobSpawn.Spawner.Blacklist").contains(entityID)
                || plugin.getCreatureManager().getList("MobSpawn.Spawner.Blacklist").contains(eName.toLowerCase())) 
            {
                if(plugin.getCreatureManager().getList("MobSpawn.Spawner.EnabledWorlds").contains(worldname)) 
                {
                    event.setCancelled(true);
                    event.getEntity().remove();
                    if (plugin.getCreatureManager().getBoolean("MobSpawn.Spawner.Debug.ToConsole", true))
                    {
                        int x = (int) loc.getX();
                        int y = (int) loc.getY();
                        int z = (int) loc.getZ();
                        plugin.log.info("[iSafe]" + " A(n) " + eName + " was cancelled its spawn, for the spawn reason: Spawner; at the location: "+ "X: "+ (x) + " Y: " + (y) + " Z: "+ (z));
                    }
                }
            }
        }
        
        /**
         * Spawn reason: Custom.
         */
        if (event.getSpawnReason() == SpawnReason.CUSTOM)
        {
            if(plugin.getCreatureManager().getList("MobSpawn.Custom.Blacklist").contains(entityID)
                || plugin.getCreatureManager().getList("MobSpawn.Custom.Blacklist").contains(eName.toLowerCase())) 
            {
                if(plugin.getCreatureManager().getList("MobSpawn.Custom.EnabledWorlds").contains(worldname)) 
                {
                    event.setCancelled(true);
                    event.getEntity().remove();
                    if (plugin.getCreatureManager().getBoolean("MobSpawn.Custom.Debug.ToConsole", true))
                    {
                        int x = (int) loc.getX();
                        int y = (int) loc.getY();
                        int z = (int) loc.getZ();
                        plugin.log.info("[iSafe]" + " A(n) " + eName + " was cancelled its spawn, for the spawn reason: Custom; at the location: "+ "X: "+ (x) + " Y: " + (y) + " Z: "+ (z));
                    }
                }
            }
        }
        
        /**
         * Spawn reason: SpawnerEgg.
         */
        if (event.getSpawnReason() == SpawnReason.SPAWNER_EGG)
        {
            if(plugin.getCreatureManager().getList("MobSpawn.SpawnerEgg.Blacklist").contains(entityID)
                || plugin.getCreatureManager().getList("MobSpawn.SpawnerEgg.Blacklist").contains(eName.toLowerCase())) 
            {
                if(plugin.getCreatureManager().getList("MobSpawn.SpawnerEgg.EnabledWorlds").contains(worldname)) 
                {
                    event.setCancelled(true);
                    event.getEntity().remove();
                    if (plugin.getCreatureManager().getBoolean("MobSpawn.SpawnerEgg.Debug.ToConsole", true))
                    {
                        int x = (int) loc.getX();
                        int y = (int) loc.getY();
                        int z = (int) loc.getZ();
                        plugin.log.info("[iSafe]" + " A(n) " + eName + " was cancelled its spawn, for the spawn reason: SpawnerEgg; at the location: "+ "X: "+ (x) + " Y: " + (y) + " Z: "+ (z));
                    }
                }
            }
        }
        
        /**
         * Spawn reason: ChunkGen.
         */
        if (event.getSpawnReason() == SpawnReason.CHUNK_GEN)
        {
            if(plugin.getCreatureManager().getList("MobSpawn.ChunkGen.Blacklist").contains(entityID)
                || plugin.getCreatureManager().getList("MobSpawn.ChunkGen.Blacklist").contains(eName.toLowerCase())) 
            {
                if(plugin.getCreatureManager().getList("MobSpawn.ChunkGen.EnabledWorlds").contains(worldname)) 
                {
                    event.setCancelled(true);
                    event.getEntity().remove();
                    if (plugin.getCreatureManager().getBoolean("MobSpawn.ChunkGen.Debug.ToConsole", true))
                    {
                        int x = (int) loc.getX();
                        int y = (int) loc.getY();
                        int z = (int) loc.getZ();
                        plugin.log.info("[iSafe]" + " A(n) " + eName + " was cancelled its spawn, for the spawn reason: ChunkGen; at the location: "+ "X: "+ (x) + " Y: " + (y) + " Z: "+ (z));
                    }
                }
            }
        }
        
        /**
         * Spawn reason: Breeding.
         */
        if (event.getSpawnReason() == SpawnReason.BREEDING)
        {
            if(plugin.getCreatureManager().getList("MobSpawn.Breeding.Blacklist").contains(entityID)
                || plugin.getCreatureManager().getList("MobSpawn.Breeding.Blacklist").contains(eName.toLowerCase())) 
            {
                if(plugin.getCreatureManager().getList("MobSpawn.Breeding.EnabledWorlds").contains(worldname)) 
                {
                    event.setCancelled(true);
                    event.getEntity().remove();
                    if (plugin.getCreatureManager().getBoolean("MobSpawn.Breeding.Debug.ToConsole", true))
                    {
                        int x = (int) loc.getX();
                        int y = (int) loc.getY();
                        int z = (int) loc.getZ();
                        plugin.log.info("[iSafe]" + " A(n) " + eName + " was cancelled its spawn, for the spawn reason: Breeding; at the location: "+ "X: "+ (x) + " Y: " + (y) + " Z: "+ (z));
                    }
                }
            }
        }
    }
}
