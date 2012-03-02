package me.mrmag518.iSafe.Blacklists;

import java.util.ArrayList;
import java.util.List;
import me.mrmag518.iSafe.iSafe;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
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
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        if (event.isCancelled())
        {
            return;
        }
        Entity entity = event.getEntity();
        int entityID = entity.getEntityId();
        String entityNameLowercase = entity.getType().getName().toLowerCase();
        String entityNameUppercase = entity.getType().getName().toUpperCase();
        String entityName = entity.getType().getName();
        String entityNameString = entity.getType().getName().toString();        
        World world = entity.getWorld();
        Location loc = entity.getLocation();
        String worldname = world.getName();
        
        /**
         * Spawn reason: Natural.
         */
        final List<Entity> naturalSpawnedMobs = new ArrayList<Entity>();
        if (plugin.getMobsConfig().getList("MobSpawn.Natural.Blacklist", naturalSpawnedMobs).contains(entityID)
                || plugin.getMobsConfig().getList("MobSpawn.Natural.Blacklist", naturalSpawnedMobs).contains(entityNameLowercase)
                || plugin.getMobsConfig().getList("MobSpawn.Natural.Blacklist", naturalSpawnedMobs).contains(entityNameUppercase)
                || plugin.getMobsConfig().getList("MobSpawn.Natural.Blacklist", naturalSpawnedMobs).contains(entityName)
                || plugin.getMobsConfig().getList("MobSpawn.Natural.Blacklist", naturalSpawnedMobs).contains(entityNameString))
        {
            if (!event.isCancelled())
            {
                final List<String> worlds1 = plugin.getMobsConfig().getStringList("MobSpawn.Natural.Worlds");
                if (plugin.getMobsConfig().getList("MobSpawn.Natural.Worlds", worlds1).contains(worldname))
                {
                    if (event.getSpawnReason() == SpawnReason.NATURAL)
                    {
                        event.setCancelled(true);
                        entity.remove();
                    } else {
                        event.setCancelled(false);
                    }
                } else {
                    event.setCancelled(false);
                }
            }
            
            if (plugin.getMobsConfig().getBoolean("MobSpawn.Natural.Alert/log.To-console", true))
            {
                int x = (int) loc.getX();
                int y = (int) loc.getY();
                int z = (int) loc.getZ();
                if (event.isCancelled()) 
                {
                    plugin.log.info("[iSafe]" + entity.toString() + " was cancelled its spawn, for the spawn reason: Natural; at the location: "+ "X: "+ (x) + " Y: " + (y) + " Z: "+ (z));
                }
            }
        }
        
        /**
         * Spawn reason: Spawner.
         */
        final List<Entity> spawnerSpawnedMobs = new ArrayList<Entity>();
        if (plugin.getMobsConfig().getList("MobSpawn.Spawner.Blacklist", spawnerSpawnedMobs).contains(entityID)) 
        {
            if (!event.isCancelled())
            {
                final List<String> worlds2 = plugin.getMobsConfig().getStringList("MobSpawn.Spawner.Worlds");
                if (plugin.getMobsConfig().getList("MobSpawn.Spawner.Worlds", worlds2).contains(worldname))
                {
                    if (event.getSpawnReason() == SpawnReason.SPAWNER)
                    {
                        event.setCancelled(true);
                        entity.remove();
                    } else {
                        event.setCancelled(false);
                    }
                } else {
                    event.setCancelled(false);
                }
            }
            
            if (plugin.getMobsConfig().getBoolean("MobSpawn.Spawner.Alert/log.To-console", true))
            {
                int x = (int) loc.getX();
                int y = (int) loc.getY();
                int z = (int) loc.getZ();
                if (event.isCancelled()) 
                {
                    plugin.log.info("[iSafe]" + entity.toString() + " was cancelled its spawn, for the spawn reason: Spawner; at the location: "+ "X: "+ (x) + " Y: " + (y) + " Z: "+ (z));
                }
            }
        }
    }
}
