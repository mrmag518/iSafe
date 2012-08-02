package com.mrmag518.iSafe.Blacklists;

import java.util.ArrayList;
import java.util.List;
import com.mrmag518.iSafe.iSafe;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
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
        if (event.isCancelled())
        {
            return;
        }
        int entityID = event.getEntity().getEntityId();
        String entityName = event.getEntity().getType().getName().toLowerCase();
        
        World world = event.getEntity().getWorld();
        Location loc = event.getEntity().getLocation();
        String worldname = world.getName();
        
        /**
         * Spawn reason: Natural.
         */
        final List<Entity> naturalSpawnedMobs = new ArrayList<Entity>();
        if (plugin.getCreatureManager().getList("MobSpawn.Natural.Blacklist", naturalSpawnedMobs).contains(entityID)
                || plugin.getCreatureManager().getList("MobSpawn.Natural.Blacklist", naturalSpawnedMobs).contains(entityName.toLowerCase()))
        {
            if (!event.isCancelled())
            {
                final List<String> worlds1 = plugin.getCreatureManager().getStringList("MobSpawn.Natural.Worlds");
                if (plugin.getCreatureManager().getList("MobSpawn.Natural.Worlds", worlds1).contains(worldname))
                {
                    if (event.getSpawnReason() == SpawnReason.NATURAL)
                    {
                        event.setCancelled(true);
                        event.getEntity().remove();
                    } else {
                        event.setCancelled(false);
                    }
                } else {
                    event.setCancelled(false);
                }
            }
            
            if (plugin.getCreatureManager().getBoolean("MobSpawn.Natural.Debug.To-console", true))
            {
                int x = (int) loc.getX();
                int y = (int) loc.getY();
                int z = (int) loc.getZ();
                if (event.isCancelled()) 
                {
                    plugin.log.info("[iSafe]" + " A(n) " + entityName.toLowerCase() + "was cancelled its spawn, for the spawn reason: Natural; at the location: "+ "X: "+ (x) + " Y: " + (y) + " Z: "+ (z));
                }
            }
        }
        
        /**
         * Spawn reason: Spawner.
         */
        final List<Entity> spawnerSpawnedMobs = new ArrayList<Entity>();
        if (plugin.getCreatureManager().getList("MobSpawn.Spawner.Blacklist", spawnerSpawnedMobs).contains(entityID)
                || plugin.getCreatureManager().getList("MobSpawn.Spawner.Blacklist", spawnerSpawnedMobs).contains(entityName.toLowerCase()))
        {
            if (!event.isCancelled())
            {
                final List<String> worlds2 = plugin.getCreatureManager().getStringList("MobSpawn.Spawner.Worlds");
                if (plugin.getCreatureManager().getList("MobSpawn.Spawner.Worlds", worlds2).contains(worldname))
                {
                    if (event.getSpawnReason() == SpawnReason.SPAWNER)
                    {
                        event.setCancelled(true);
                        event.getEntity().remove();
                    } else {
                        event.setCancelled(false);
                    }
                } else {
                    event.setCancelled(false);
                }
            }
            
            if (plugin.getCreatureManager().getBoolean("MobSpawn.Spawner.Debug.To-console", true))
            {
                int x = (int) loc.getX();
                int y = (int) loc.getY();
                int z = (int) loc.getZ();
                if (event.isCancelled()) 
                {
                    plugin.log.info("[iSafe]" + " A(n) " + entityName.toLowerCase() + " was cancelled its spawn, for the spawn reason: Spawner; at the location: "+ "X: "+ (x) + " Y: " + (y) + " Z: "+ (z));
                }
            }
        }
        
        /**
         * Spawn reason: Custom.
         */
        final List<Entity> customSpawnedMobs = new ArrayList<Entity>();
        if (plugin.getCreatureManager().getList("MobSpawn.Spawner.Blacklist", customSpawnedMobs).contains(entityID)
                || plugin.getCreatureManager().getList("MobSpawn.Custom.Blacklist", customSpawnedMobs).contains(entityName.toLowerCase()))
        {
            if (!event.isCancelled())
            {
                final List<String> worlds3 = plugin.getCreatureManager().getStringList("MobSpawn.Custom.Worlds");
                if (plugin.getCreatureManager().getList("MobSpawn.Custom.Worlds", worlds3).contains(worldname))
                {
                    if (event.getSpawnReason() == SpawnReason.CUSTOM)
                    {
                        event.setCancelled(true);
                        event.getEntity().remove();
                    } else {
                        event.setCancelled(false);
                    }
                } else {
                    event.setCancelled(false);
                }
            }
            
            if (plugin.getCreatureManager().getBoolean("MobSpawn.Custom.Debug.To-console", true))
            {
                int x = (int) loc.getX();
                int y = (int) loc.getY();
                int z = (int) loc.getZ();
                if (event.isCancelled()) 
                {
                    plugin.log.info("[iSafe]" + " A(n) " + entityName.toLowerCase() + " was cancelled its spawn, for the spawn reason: Custom; at the location: "+ "X: "+ (x) + " Y: " + (y) + " Z: "+ (z));
                }
            }
        }
        
        /**
         * Spawn reason: Egg.
         */
        final List<Entity> eggSpawnedMobs = new ArrayList<Entity>();
        if (plugin.getCreatureManager().getList("MobSpawn.Egg.Blacklist", eggSpawnedMobs).contains(entityID)
                || plugin.getCreatureManager().getList("MobSpawn.Egg.Blacklist", eggSpawnedMobs).contains(entityName.toLowerCase()))
        {
            if (!event.isCancelled())
            {
                final List<String> worlds4 = plugin.getCreatureManager().getStringList("MobSpawn.Egg.Worlds");
                if (plugin.getCreatureManager().getList("MobSpawn.Egg.Worlds", worlds4).contains(worldname))
                {
                    if (event.getSpawnReason() == SpawnReason.EGG)
                    {
                        event.setCancelled(true);
                        event.getEntity().remove();
                    } else {
                        event.setCancelled(false);
                    }
                } else {
                    event.setCancelled(false);
                }
            }
            
            if (plugin.getCreatureManager().getBoolean("MobSpawn.Egg.Debug.To-console", true))
            {
                int x = (int) loc.getX();
                int y = (int) loc.getY();
                int z = (int) loc.getZ();
                if (event.isCancelled()) 
                {
                    plugin.log.info("[iSafe]" + " A(n) " + entityName.toLowerCase() + " was cancelled its spawn, for the spawn reason: Egg; at the location: "+ "X: "+ (x) + " Y: " + (y) + " Z: "+ (z));
                }
            }
        }
        
        /**
         * Spawn reason: SpawnerEgg.
         */
        final List<Entity> spawnereggSpawnedMobs = new ArrayList<Entity>();
        if (plugin.getCreatureManager().getList("MobSpawn.SpawnerEgg.Blacklist", spawnereggSpawnedMobs).contains(entityID)
                || plugin.getCreatureManager().getList("MobSpawn.SpawnerEgg.Blacklist", spawnereggSpawnedMobs).contains(entityName.toLowerCase()))
        {
            if (!event.isCancelled())
            {
                final List<String> worlds5 = plugin.getCreatureManager().getStringList("MobSpawn.SpawnerEgg.Worlds");
                if (plugin.getCreatureManager().getList("MobSpawn.SpawnerEgg.Worlds", worlds5).contains(worldname))
                {
                    if (event.getSpawnReason() == SpawnReason.SPAWNER_EGG)
                    {
                        event.setCancelled(true);
                        event.getEntity().remove();
                    } else {
                        event.setCancelled(false);
                    }
                } else {
                    event.setCancelled(false);
                }
            }
            
            if (plugin.getCreatureManager().getBoolean("MobSpawn.SpawnerEgg.Debug.To-console", true))
            {
                int x = (int) loc.getX();
                int y = (int) loc.getY();
                int z = (int) loc.getZ();
                if (event.isCancelled()) 
                {
                    plugin.log.info("[iSafe]" + " A(n) " + entityName.toLowerCase() + " was cancelled its spawn, for the spawn reason: SpawnerEgg; at the location: "+ "X: "+ (x) + " Y: " + (y) + " Z: "+ (z));
                }
            }
        }
    }
}
