package me.mrmag518.iSafe.Blacklists;

import java.util.ArrayList;
import java.util.List;
import me.mrmag518.iSafe.iSafe;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Entity;
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
        World world = entity.getWorld();
        Location loc = entity.getLocation();
        String worldname = world.getName();
        
        /**
         * Spawn reason: Natural.
         */
        final List<Entity> naturalSpawnedMobs = new ArrayList<Entity>();
        if (plugin.getMobsConfig().getList("MobSpawn.Natural.Blacklist", naturalSpawnedMobs).contains(entityID)) 
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
    }
}
