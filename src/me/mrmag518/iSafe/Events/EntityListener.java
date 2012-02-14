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
import org.bukkit.entity.*;
import org.bukkit.event.entity.*;
import org.bukkit.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.CreeperPowerEvent.PowerCause;
import org.bukkit.event.entity.EntityDamageEvent.*;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
import org.bukkit.event.entity.EntityTargetEvent.*;


public class EntityListener implements Listener {
    
    public static iSafe plugin;
    public EntityListener(iSafe instance)
    {
        plugin = instance;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    
    @EventHandler
    public void PlayerBowUsage(EntityShootBowEvent event, Player player) {
        if (event.isCancelled())
        {
            return;
        }
        
        if(plugin.getConfig().getBoolean("Player.Prevent-Bow-usage", true))
        {
            if (player.hasPermission("iSafe.bow.use")) {
                //access
            } else {
                event.setCancelled(true);
                player.sendMessage(ChatColor.RED + "You cannot use a bow.");
            }
        }
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void onExplosionPrime(ExplosionPrimeEvent event) {
        if (event.isCancelled())
        {
            return;
        }
        
        if(plugin.getConfig().getBoolean("Explosions.Disable-primed-explosions", true))
        {    
            event.getEntity().remove();
            event.setCancelled(true);
        }
        
        if(plugin.getConfig().getBoolean("Explosions.Prevent-creeper-death-on-explosion", true)) {
            if (event.isCancelled()) {
                NoCreeperDeathOnExplosion(event);
            }
        }
    }
    
    @EventHandler(priority = EventPriority.NORMAL)
        public void onEntityExplode(EntityExplodeEvent event) {
        if (event.isCancelled())
        {
            return;
        }
        
        Entity ent = event.getEntity();
        
        //Prevent all explosions
        if(plugin.getConfig().getBoolean("Explosions.Disable-explosions", true))
        {    
            event.blockList().clear();
            return;
        }
        //Prevent Creeper explosions
        if(plugin.getConfig().getBoolean("Explosions.Disable-Creeper-explosions", true))
        {
            if (ent instanceof Creeper) {
                event.blockList().clear();
                return;
            }
        }
        //Prevent EnderDragon block damage
        if(plugin.getConfig().getBoolean("Explosions.Disable-EnderDragon-blockdamage", true))
        {
            if (ent instanceof EnderDragon) {
                event.blockList().clear();
                return;
            }
        }
        if(plugin.getConfig().getBoolean("Explosions.Disable-TNT-explosions", true))
        {
            if (ent instanceof TNTPrimed) {
                event.blockList().clear();
                return;
            }
        }
        if(plugin.getConfig().getBoolean("Explosions.Disable-Fireball-explosions", true))
        {
            if (ent instanceof Fireball) {
                event.blockList().clear();
                return;
            }  
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        if (event.isCancelled())
        {
            return;
        }
        
        if(!plugin.getMobsConfig().getBoolean("Mobs-All.Spawn.Allow-Slime-spawn", true))
        {
            if (event.getCreatureType() == CreatureType.SLIME) {
                event.setCancelled(true);
                event.getEntity().remove();
            }
        }
        if(!plugin.getMobsConfig().getBoolean("Mobs-All.Spawn.Spawn.Allow-Ghast-spawn", true))
        {
            if (event.getCreatureType() == CreatureType.GHAST) {
                event.setCancelled(true);
                event.getEntity().remove();
            }
        }
        if(!plugin.getMobsConfig().getBoolean("Mobs-All.Spawn.Allow-Zombie-spawn", true))
        {
            if (event.getCreatureType() == CreatureType.ZOMBIE) {
                event.setCancelled(true);
                event.getEntity().remove();
            }
        }
        if(!plugin.getMobsConfig().getBoolean("Mobs-All.Spawn.Allow-Creeper-spawn", true))
        {
            if (event.getCreatureType() == CreatureType.CREEPER) {
                event.setCancelled(true);
                event.getEntity().remove();
            }
        }
        if(!plugin.getMobsConfig().getBoolean("Mobs-All.Spawn.Allow-Skeleton-spawn", true))
        {
            if (event.getCreatureType() == CreatureType.SKELETON) {
                event.setCancelled(true);
                event.getEntity().remove();
            }
        }
        if(!plugin.getMobsConfig().getBoolean("Mobs-All.Spawn.Allow-Enderman-spawn", true))
        {
            if (event.getCreatureType() == CreatureType.ENDERMAN) {
                event.setCancelled(true);
                event.getEntity().remove();
            }
        }
        if(!plugin.getMobsConfig().getBoolean("Mobs-All.Spawn.Allow-Silverfish-spawn", true))
        {
            if (event.getCreatureType() == CreatureType.SILVERFISH) {
                event.setCancelled(true);
                event.getEntity().remove();
            }
        }
        if(!plugin.getMobsConfig().getBoolean("Mobs-All.Spawn.Allow-PigZombie-spawn", true))
        {
            if (event.getCreatureType() == CreatureType.PIG_ZOMBIE) {
                event.setCancelled(true);
                event.getEntity().remove();
            }
        }
        if(!plugin.getMobsConfig().getBoolean("Mobs-All.Spawn.Allow-Spider-spawn", true))
        {
            if (event.getCreatureType() == CreatureType.SPIDER) {
                event.setCancelled(true);
                event.getEntity().remove();
            }
        }
        if(!plugin.getMobsConfig().getBoolean("Mobs-All.Spawn.Allow-Squid-spawn", true))
        {
            if (event.getCreatureType() == CreatureType.SQUID) {
                event.setCancelled(true);
                event.getEntity().remove();
            }
        }
        if(!plugin.getMobsConfig().getBoolean("Mobs-All.Spawn.Allow-Wolf-spawn", true))
        {
            if (event.getCreatureType() == CreatureType.WOLF) {
                event.setCancelled(true);
                event.getEntity().remove();
            }
        }
        if(!plugin.getMobsConfig().getBoolean("Mobs-All.Spawn.Allow-Pig-spawn", true))
        {
            if (event.getCreatureType() == CreatureType.PIG) {
                event.setCancelled(true);
                event.getEntity().remove();
            }
        }
        if(!plugin.getMobsConfig().getBoolean("Mobs-All.Spawn.Allow-Cow-spawn", true))
        {
            if (event.getCreatureType() == CreatureType.COW) {
                event.setCancelled(true);
                event.getEntity().remove();
            }
        }
        if(!plugin.getMobsConfig().getBoolean("Mobs-All.Spawn.Allow-EnderDragon-spawn", true))
        {
            if (event.getCreatureType() == CreatureType.ENDER_DRAGON) {
                event.setCancelled(true);
                event.getEntity().remove();
            }
        }
        if(!plugin.getMobsConfig().getBoolean("Mobs-All.Spawn.Allow-Sheep-spawn", true))
        {
            if (event.getCreatureType() == CreatureType.SHEEP) {
                event.setCancelled(true);
                event.getEntity().remove();
            }
        }
        if(!plugin.getMobsConfig().getBoolean("Mobs-All.Spawn..Allow-Blaze-spawn", true))
        {
            if (event.getCreatureType() == CreatureType.BLAZE) {
                event.setCancelled(true);
                event.getEntity().remove();
            }
        }
        if(!plugin.getMobsConfig().getBoolean("Mobs-All.Spawn.Allow-MagmaCube-spawn", true))
        {
            if (event.getCreatureType() == CreatureType.MAGMA_CUBE) {
                event.setCancelled(true);
                event.getEntity().remove();
            }
        }
        if(!plugin.getMobsConfig().getBoolean("Mobs-All.Spawn.Allow-CaveSpider-spawn", true))
        {
            if (event.getCreatureType() == CreatureType.CAVE_SPIDER) {
                event.setCancelled(true);
                event.getEntity().remove();
            }
        }
        if(!plugin.getMobsConfig().getBoolean("Mobs-All.Spawn.Allow-Chicken-spawn", true))
        {
            if (event.getCreatureType() == CreatureType.CHICKEN) {
                event.setCancelled(true);
                event.getEntity().remove();
            }
        }
        if(!plugin.getMobsConfig().getBoolean("Mobs-All.Spawn.Allow-Giant-spawn", true))
        {
            if (event.getCreatureType() == CreatureType.GIANT) {
                event.setCancelled(true);
                event.getEntity().remove();
            }
        }
        if(!plugin.getMobsConfig().getBoolean("Mobs-All.Spawn.Allow-MuchroomCow-spawn", true))
        {
            if (event.getCreatureType() == CreatureType.MUSHROOM_COW) {
                event.setCancelled(true);
                event.getEntity().remove();
            }
        }
        if(!plugin.getMobsConfig().getBoolean("Mobs-All-All.Spawn.Allow-Snowman-spawn", true))
        {
            if (event.getCreatureType() == CreatureType.SNOWMAN) {
                event.setCancelled(true);
                event.getEntity().remove();
            }
        }
        if(!plugin.getMobsConfig().getBoolean("Mobs-All.Spawn.Allow-Villager-spawn", true))
        {
            if (event.getCreatureType() == CreatureType.VILLAGER) {
                event.setCancelled(true);
                event.getEntity().remove();
            }
        }
        
        /**
         * SpawnReason = Natural
         */
        if(plugin.getMobsConfig().getBoolean("Mob-Spawn.SpawnReason.Natural.Prevent.Blazes", true)) 
        {
            if (event.getSpawnReason() == SpawnReason.NATURAL) 
            {
                if (event.getCreatureType() == CreatureType.BLAZE) 
                {
                    event.setCancelled(true);
                    event.getEntity().remove();
                }
            }
        }
        
        if(plugin.getMobsConfig().getBoolean("Mob-Spawn.SpawnReason.Natural.Prevent.Cave_Spiders", true)) 
        {
            if (event.getSpawnReason() == SpawnReason.NATURAL) 
            {
                if (event.getCreatureType() == CreatureType.CAVE_SPIDER) 
                {
                    event.setCancelled(true);
                    event.getEntity().remove();
                }
            }
        }
        
        if(plugin.getMobsConfig().getBoolean("Mob-Spawn.SpawnReason.Natural.Prevent.Chickens", true)) 
        {
            if (event.getSpawnReason() == SpawnReason.NATURAL) 
            {
                if (event.getCreatureType() == CreatureType.CHICKEN) 
                {
                    event.setCancelled(true);
                    event.getEntity().remove();
                }
            }
        }
        
        if(plugin.getMobsConfig().getBoolean("Mob-Spawn.SpawnReason.Natural.Prevent.Cows", true)) 
        {
            if (event.getSpawnReason() == SpawnReason.NATURAL) 
            {
                if (event.getCreatureType() == CreatureType.COW) 
                {
                    event.setCancelled(true);
                    event.getEntity().remove();
                }
            }
        }
        
        if(plugin.getMobsConfig().getBoolean("Mob-Spawn.SpawnReason.Natural.Prevent.Creepers", true)) 
        {
            if (event.getSpawnReason() == SpawnReason.NATURAL) 
            {
                if (event.getCreatureType() == CreatureType.CREEPER) 
                {
                    event.setCancelled(true);
                    event.getEntity().remove();
                }
            }
        }
        
        if(plugin.getMobsConfig().getBoolean("Mob-Spawn.SpawnReason.Natural.Prevent.Endermen", true)) 
        {
            if (event.getSpawnReason() == SpawnReason.NATURAL) 
            {
                if (event.getCreatureType() == CreatureType.ENDERMAN) 
                {
                    event.setCancelled(true);
                    event.getEntity().remove();
                }
            }
        }
        
        if(plugin.getMobsConfig().getBoolean("Mob-Spawn.SpawnReason.Natural.Prevent.EnderDragons", true)) 
        {
            if (event.getSpawnReason() == SpawnReason.NATURAL) 
            {
                if (event.getCreatureType() == CreatureType.ENDER_DRAGON) 
                {
                    event.setCancelled(true);
                    event.getEntity().remove();
                }
            }
        }
        
        if(plugin.getMobsConfig().getBoolean("Mob-Spawn.SpawnReason.Natural.Prevent.Ghasts", true)) 
        {
            if (event.getSpawnReason() == SpawnReason.NATURAL) 
            {
                if (event.getCreatureType() == CreatureType.GHAST) 
                {
                    event.setCancelled(true);
                    event.getEntity().remove();
                }
            }
        }
        
        if(plugin.getMobsConfig().getBoolean("Mob-Spawn.SpawnReason.Natural.Prevent.Giants", true))
        {
            if (event.getSpawnReason() == SpawnReason.NATURAL) 
            {
                if (event.getCreatureType() == CreatureType.GIANT) 
                {
                    event.setCancelled(true);
                    event.getEntity().remove();
                }
            }
        }
        
        if(plugin.getMobsConfig().getBoolean("Mob-Spawn.SpawnReason.Natural.Prevent.MagmaCubes", true))
        {
            if (event.getSpawnReason() == SpawnReason.NATURAL) 
            {
                if (event.getCreatureType() == CreatureType.MAGMA_CUBE) 
                {
                    event.setCancelled(true);
                    event.getEntity().remove();
                }
            }
        }
        
        if(plugin.getMobsConfig().getBoolean("Mob-Spawn.SpawnReason.Natural.Prevent.MushroomCows", true))
        {
            if (event.getSpawnReason() == SpawnReason.NATURAL) 
            {
                if (event.getCreatureType() == CreatureType.MUSHROOM_COW) 
                {
                    event.setCancelled(true);
                    event.getEntity().remove();
                }
            }
        }
        
        if(plugin.getMobsConfig().getBoolean("Mob-Spawn.SpawnReason.Natural.Prevent.Pigs", true)) 
        {
            if (event.getSpawnReason() == SpawnReason.NATURAL) 
            {
                if (event.getCreatureType() == CreatureType.PIG) 
                {
                    event.setCancelled(true);
                    event.getEntity().remove();
                }
            }
        }
        
        if(plugin.getMobsConfig().getBoolean("Mob-Spawn.SpawnReason.Natural.Prevent.PigZombie", true))
        {
            if (event.getSpawnReason() == SpawnReason.NATURAL) 
            {
                if (event.getCreatureType() == CreatureType.PIG_ZOMBIE) 
                {
                    event.setCancelled(true);
                    event.getEntity().remove();
                }
            }
        }
        
        if(plugin.getMobsConfig().getBoolean("Mob-Spawn.SpawnReason.Natural.Prevent.Sheeps", true)) 
        {
            if (event.getSpawnReason() == SpawnReason.NATURAL) 
            {
                if (event.getCreatureType() == CreatureType.SHEEP) 
                {
                    event.setCancelled(true);
                    event.getEntity().remove();
                }
            }
        }
        
        if(plugin.getMobsConfig().getBoolean("Mob-Spawn.SpawnReason.Natural.Prevent.Silverfishes", true)) 
        {
            if (event.getSpawnReason() == SpawnReason.NATURAL) 
            {
                if (event.getCreatureType() == CreatureType.SILVERFISH) 
                {
                    event.setCancelled(true);
                    event.getEntity().remove();
                }
            }
        }
        
        if(plugin.getMobsConfig().getBoolean("Mob-Spawn.SpawnReason.Natural.Prevent.Skeletons", true)) 
        {
            if (event.getSpawnReason() == SpawnReason.NATURAL) 
            {
                if (event.getCreatureType() == CreatureType.SKELETON) 
                {
                    event.setCancelled(true);
                    event.getEntity().remove();
                }
            }
        }
        
        if(plugin.getMobsConfig().getBoolean("Mob-Spawn.SpawnReason.Natural.Prevent.Slimes", true)) 
        {
            if (event.getSpawnReason() == SpawnReason.NATURAL) 
            {
                if (event.getCreatureType() == CreatureType.SLIME) 
                {
                    event.setCancelled(true);
                    event.getEntity().remove();
                }
            }
        }
        
        if(plugin.getMobsConfig().getBoolean("Mob-Spawn.SpawnReason.Natural.Prevent.Snowmen", true)) {
            if (event.getSpawnReason() == SpawnReason.NATURAL) 
            {
                if (event.getCreatureType() == CreatureType.SNOWMAN) 
                {
                    event.setCancelled(true);
                    event.getEntity().remove();
                }
            }
        }
        
        if(plugin.getMobsConfig().getBoolean("Mob-Spawn.SpawnReason.Natural.Prevent.Spiders", true)) 
        {
            if (event.getSpawnReason() == SpawnReason.NATURAL) 
            {
                if (event.getCreatureType() == CreatureType.SPIDER) 
                {
                    event.setCancelled(true);
                    event.getEntity().remove();
                }
            }
        }
        
        if(plugin.getMobsConfig().getBoolean("Mob-Spawn.SpawnReason.Natural.Prevent.Squids", true)) 
        {
            if (event.getSpawnReason() == SpawnReason.NATURAL) 
            {
                if (event.getCreatureType() == CreatureType.SQUID) 
                {
                    event.setCancelled(true);
                    event.getEntity().remove();
                }
            }
        }
        
        if(plugin.getMobsConfig().getBoolean("Mob-Spawn.SpawnReason.Natural.Prevent.Villagers", true))
        {
            if (event.getSpawnReason() == SpawnReason.NATURAL) 
            {
                if (event.getCreatureType() == CreatureType.VILLAGER) 
                {
                    event.setCancelled(true);
                    event.getEntity().remove();
                }
            }
        }
        
        if(plugin.getMobsConfig().getBoolean("Mob-Spawn.SpawnReason.Natural.Prevent.Wolfs", true))
        {
            if (event.getSpawnReason() == SpawnReason.NATURAL) 
            {
                if (event.getCreatureType() == CreatureType.WOLF) 
                {
                    event.setCancelled(true);
                    event.getEntity().remove();
                }
            }
        }
        
        if(plugin.getMobsConfig().getBoolean("Mob-Spawn.SpawnReason.Natural.Prevent.Zombies", true)) 
        {
            if (event.getSpawnReason() == SpawnReason.NATURAL) 
            {
                if (event.getCreatureType() == CreatureType.ZOMBIE) 
                {
                    event.setCancelled(true);
                    event.getEntity().remove();
                }
            }
        }
        
        /**
         * SpawnReason = Spawner
         */
        
        if(plugin.getMobsConfig().getBoolean("Mob-Spawn.SpawnReason.Spawner.Prevent.Blazes", true)) 
        {
            if (event.getSpawnReason() == SpawnReason.SPAWNER)
            {
                if (event.getCreatureType() == CreatureType.BLAZE) 
                {
                    event.setCancelled(true);
                    event.getEntity().remove();
                }
            }
        }
        
        if(plugin.getMobsConfig().getBoolean("Mob-Spawn.SpawnReason.Spawner.Prevent.CaveSpiders", true)) 
        {
            if (event.getSpawnReason() == SpawnReason.SPAWNER)
            {
                if (event.getCreatureType() == CreatureType.CAVE_SPIDER) 
                {
                    event.setCancelled(true);
                    event.getEntity().remove();
                }
            }
        }
        
        if(plugin.getMobsConfig().getBoolean("Mob-Spawn.SpawnReason.Spawner.Prevent.Chickens", true)) 
        {
            if (event.getSpawnReason() == SpawnReason.SPAWNER)
            {
                if (event.getCreatureType() == CreatureType.CHICKEN) 
                {
                    event.setCancelled(true);
                    event.getEntity().remove();
                }
            }
        }
        
        if(plugin.getMobsConfig().getBoolean("Mob-Spawn.SpawnReason.Spawner.Prevent.Cows", true)) 
        {
            if (event.getSpawnReason() == SpawnReason.SPAWNER)
            {
                if (event.getCreatureType() == CreatureType.COW) 
                {
                    event.setCancelled(true);
                    event.getEntity().remove();
                }
            }
        }
        
        if(plugin.getMobsConfig().getBoolean("Mob-Spawn.SpawnReason.Spawner.Prevent.Creepers", true)) 
        {
            if (event.getSpawnReason() == SpawnReason.SPAWNER)
            {
                if (event.getCreatureType() == CreatureType.CREEPER) 
                {
                    event.setCancelled(true);
                    event.getEntity().remove();
                }
            }
        }
        
        if(plugin.getMobsConfig().getBoolean("Mob-Spawn.SpawnReason.Spawner.Prevent.Endermen", true)) 
        {
            if (event.getSpawnReason() == SpawnReason.SPAWNER)
            {
                if (event.getCreatureType() == CreatureType.ENDERMAN) 
                {
                    event.setCancelled(true);
                    event.getEntity().remove();
                }
            }
        }
        
        if(plugin.getMobsConfig().getBoolean("Mob-Spawn.SpawnReason.Spawner.Prevent.EnderDragons", true)) 
        {
            if (event.getSpawnReason() == SpawnReason.SPAWNER)
            {
                if (event.getCreatureType() == CreatureType.ENDER_DRAGON) 
                {
                    event.setCancelled(true);
                    event.getEntity().remove();
                }
            }
        }
        
        if(plugin.getMobsConfig().getBoolean("Mob-Spawn.SpawnReason.Spawner.Prevent.Ghasts", true)) 
        {
            if (event.getSpawnReason() == SpawnReason.SPAWNER)
            {
                if (event.getCreatureType() == CreatureType.GHAST) 
                {
                    event.setCancelled(true);
                    event.getEntity().remove();
                }
            }
        }
        
        if(plugin.getMobsConfig().getBoolean("Mob-Spawn.SpawnReason.Spawner.Prevent.Giants", true)) 
        {
            if (event.getSpawnReason() == SpawnReason.SPAWNER)
            {
                if (event.getCreatureType() == CreatureType.GIANT) 
                {
                    event.setCancelled(true);
                    event.getEntity().remove();
                }
            }
        }
        
        if(plugin.getMobsConfig().getBoolean("Mob-Spawn.SpawnReason.Spawner.Prevent.MagmaCubes", true)) 
        {
            if (event.getSpawnReason() == SpawnReason.SPAWNER)
            {
                if (event.getCreatureType() == CreatureType.MAGMA_CUBE) 
                {
                    event.setCancelled(true);
                    event.getEntity().remove();
                }
            }
        }
        
        if(plugin.getMobsConfig().getBoolean("Mob-Spawn.SpawnReason.Spawner.Prevent.MushroomCows", true)) 
        {
            if (event.getSpawnReason() == SpawnReason.SPAWNER)
            {
                if (event.getCreatureType() == CreatureType.MUSHROOM_COW) 
                {
                    event.setCancelled(true);
                    event.getEntity().remove();
                }
            }
        }
        
        if(plugin.getMobsConfig().getBoolean("Mob-Spawn.SpawnReason.Spawner.Prevent.Pigs", true)) 
        {
            if (event.getSpawnReason() == SpawnReason.SPAWNER)
            {
                if (event.getCreatureType() == CreatureType.PIG) 
                {
                    event.setCancelled(true);
                    event.getEntity().remove();
                }
            }
        }
        
        if(plugin.getMobsConfig().getBoolean("Mob-Spawn.SpawnReason.Spawner.Prevent.PigZombies", true)) 
        {
            if (event.getSpawnReason() == SpawnReason.SPAWNER)
            {
                if (event.getCreatureType() == CreatureType.PIG_ZOMBIE) 
                {
                    event.setCancelled(true);
                    event.getEntity().remove();
                }
            }
        }
        
        if(plugin.getMobsConfig().getBoolean("Mob-Spawn.SpawnReason.Spawner.Prevent.Sheeps", true)) 
        {
            if (event.getSpawnReason() == SpawnReason.SPAWNER)
            {
                if (event.getCreatureType() == CreatureType.SHEEP) 
                {
                    event.setCancelled(true);
                    event.getEntity().remove();
                }
            }
        }
        
        if(plugin.getMobsConfig().getBoolean("Mob-Spawn.SpawnReason.Spawner.Prevent.Silverfishes", true)) 
        {
            if (event.getSpawnReason() == SpawnReason.SPAWNER)
            {
                if (event.getCreatureType() == CreatureType.SILVERFISH) 
                {
                    event.setCancelled(true);
                    event.getEntity().remove();
                }
            }
        }
        
        if(plugin.getMobsConfig().getBoolean("Mob-Spawn.SpawnReason.Spawner.Prevent.Skeletons", true)) 
        {
            if (event.getSpawnReason() == SpawnReason.SPAWNER)
            {
                if (event.getCreatureType() == CreatureType.SKELETON) 
                {
                    event.setCancelled(true);
                    event.getEntity().remove();
                }
            }
        }
        
        if(plugin.getMobsConfig().getBoolean("Mob-Spawn.SpawnReason.Spawner.Prevent.Slimes", true)) 
        {
            if (event.getSpawnReason() == SpawnReason.SPAWNER)
            {
                if (event.getCreatureType() == CreatureType.SLIME) 
                {
                    event.setCancelled(true);
                    event.getEntity().remove();
                }
            }
        }
        
        if(plugin.getMobsConfig().getBoolean("Mob-Spawn.SpawnReason.Spawner.Prevent.Snowmen", true)) 
        {
            if (event.getSpawnReason() == SpawnReason.SPAWNER)
            {
                if (event.getCreatureType() == CreatureType.SNOWMAN) 
                {
                    event.setCancelled(true);
                    event.getEntity().remove();
                }
            }
        }
        
        if(plugin.getMobsConfig().getBoolean("Mob-Spawn.SpawnReason.Spawner.Prevent.Spiders", true)) 
        {
            if (event.getSpawnReason() == SpawnReason.SPAWNER)
            {
                if (event.getCreatureType() == CreatureType.SPIDER) 
                {
                    event.setCancelled(true);
                    event.getEntity().remove();
                }
            }
        }
        
        if(plugin.getMobsConfig().getBoolean("Mob-Spawn.SpawnReason.Spawner.Prevent.Squids", true)) 
        {
            if (event.getSpawnReason() == SpawnReason.SPAWNER)
            {
                if (event.getCreatureType() == CreatureType.SQUID) 
                {
                    event.setCancelled(true);
                    event.getEntity().remove();
                }
            }
        }
        
        if(plugin.getMobsConfig().getBoolean("Mob-Spawn.SpawnReason.Spawner.Prevent.Villagers", true)) 
        {
            if (event.getSpawnReason() == SpawnReason.SPAWNER)
            {
                if (event.getCreatureType() == CreatureType.VILLAGER) 
                {
                    event.setCancelled(true);
                    event.getEntity().remove();
                }
            }
        }
        
        if(plugin.getMobsConfig().getBoolean("Mob-Spawn.SpawnReason.Spawner.Prevent.Wolfs", true)) 
        {
            if (event.getSpawnReason() == SpawnReason.SPAWNER)
            {
                if (event.getCreatureType() == CreatureType.WOLF) 
                {
                    event.setCancelled(true);
                    event.getEntity().remove();
                }
            }
        }
        
        if(plugin.getMobsConfig().getBoolean("Mob-Spawn.SpawnReason.Spawner.Prevent.Zombies", true)) 
        {
            if (event.getSpawnReason() == SpawnReason.SPAWNER)
            {
                if (event.getCreatureType() == CreatureType.ZOMBIE) 
                {
                    event.setCancelled(true);
                    event.getEntity().remove();
                }
            }
        }
        
        /**
         * SpawnReason = Custom
         */
        
        if(plugin.getMobsConfig().getBoolean("Mob-Spawn.SpawnReason.Custom.Prevent.Blazes", true)) 
        {
            if (event.getSpawnReason() == SpawnReason.CUSTOM)
            {
                if (event.getCreatureType() == CreatureType.BLAZE) 
                {
                    event.setCancelled(true);
                    event.getEntity().remove();
                }
            }
        }
        
        if(plugin.getMobsConfig().getBoolean("Mob-Spawn.SpawnReason.Custom.Prevent.CaveSpiders", true)) 
        {
            if (event.getSpawnReason() == SpawnReason.CUSTOM)
            {
                if (event.getCreatureType() == CreatureType.CAVE_SPIDER) 
                {
                    event.setCancelled(true);
                    event.getEntity().remove();
                }
            }
        }
        
        if(plugin.getMobsConfig().getBoolean("Mob-Spawn.SpawnReason.Custom.Prevent.Chickens", true)) 
        {
            if (event.getSpawnReason() == SpawnReason.CUSTOM)
            {
                if (event.getCreatureType() == CreatureType.CHICKEN) 
                {
                    event.setCancelled(true);
                    event.getEntity().remove();
                }
            }
        }
        
        if(plugin.getMobsConfig().getBoolean("Mob-Spawn.SpawnReason.Custom.Prevent.Cows", true)) 
        {
            if (event.getSpawnReason() == SpawnReason.CUSTOM)
            {
                if (event.getCreatureType() == CreatureType.COW) 
                {
                    event.setCancelled(true);
                    event.getEntity().remove();
                }
            }
        }
        
        if(plugin.getMobsConfig().getBoolean("Mob-Spawn.SpawnReason.Custom.Prevent.Creepers", true)) 
        {
            if (event.getSpawnReason() == SpawnReason.CUSTOM)
            {
                if (event.getCreatureType() == CreatureType.CREEPER) 
                {
                    event.setCancelled(true);
                    event.getEntity().remove();
                }
            }
        }
        
        if(plugin.getMobsConfig().getBoolean("Mob-Spawn.SpawnReason.Custom.Prevent.Endermen", true)) 
        {
            if (event.getSpawnReason() == SpawnReason.CUSTOM)
            {
                if (event.getCreatureType() == CreatureType.ENDERMAN) 
                {
                    event.setCancelled(true);
                    event.getEntity().remove();
                }
            }
        }
        
        if(plugin.getMobsConfig().getBoolean("Mob-Spawn.SpawnReason.Custom.Prevent.EnderDragons", true)) 
        {
            if (event.getSpawnReason() == SpawnReason.CUSTOM)
            {
                if (event.getCreatureType() == CreatureType.ENDER_DRAGON) 
                {
                    event.setCancelled(true);
                    event.getEntity().remove();
                }
            }
        }
        
        if(plugin.getMobsConfig().getBoolean("Mob-Spawn.SpawnReason.Custom.Prevent.Ghasts", true)) 
        {
            if (event.getSpawnReason() == SpawnReason.CUSTOM)
            {
                if (event.getCreatureType() == CreatureType.GHAST) 
                {
                    event.setCancelled(true);
                    event.getEntity().remove();
                }
            }
        }
        
        if(plugin.getMobsConfig().getBoolean("Mob-Spawn.SpawnReason.Custom.Prevent.Giants", true)) 
        {
            if (event.getSpawnReason() == SpawnReason.CUSTOM)
            {
                if (event.getCreatureType() == CreatureType.GIANT) 
                {
                    event.setCancelled(true);
                    event.getEntity().remove();
                }
            }
        }
        
        if(plugin.getMobsConfig().getBoolean("Mob-Spawn.SpawnReason.Custom.Prevent.MagmaCubes", true)) 
        {
            if (event.getSpawnReason() == SpawnReason.CUSTOM)
            {
                if (event.getCreatureType() == CreatureType.MAGMA_CUBE) 
                {
                    event.setCancelled(true);
                    event.getEntity().remove();
                }
            }
        }
        
        if(plugin.getMobsConfig().getBoolean("Mob-Spawn.SpawnReason.Custom.Prevent.MushroomCows", true)) 
        {
            if (event.getSpawnReason() == SpawnReason.CUSTOM)
            {
                if (event.getCreatureType() == CreatureType.MUSHROOM_COW) 
                {
                    event.setCancelled(true);
                    event.getEntity().remove();
                }
            }
        }
        
        if(plugin.getMobsConfig().getBoolean("Mob-Spawn.SpawnReason.Custom.Prevent.Pigs", true)) 
        {
            if (event.getSpawnReason() == SpawnReason.CUSTOM)
            {
                if (event.getCreatureType() == CreatureType.PIG) 
                {
                    event.setCancelled(true);
                    event.getEntity().remove();
                }
            }
        }
        
        if(plugin.getMobsConfig().getBoolean("Mob-Spawn.SpawnReason.Custom.Prevent.PigZombies", true)) 
        {
            if (event.getSpawnReason() == SpawnReason.CUSTOM)
            {
                if (event.getCreatureType() == CreatureType.PIG_ZOMBIE) 
                {
                    event.setCancelled(true);
                    event.getEntity().remove();
                }
            }
        }
        
        if(plugin.getMobsConfig().getBoolean("Mob-Spawn.SpawnReason.Custom.Prevent.Sheeps", true)) 
        {
            if (event.getSpawnReason() == SpawnReason.CUSTOM)
            {
                if (event.getCreatureType() == CreatureType.SHEEP) 
                {
                    event.setCancelled(true);
                    event.getEntity().remove();
                }
            }
        }
        
        if(plugin.getMobsConfig().getBoolean("Mob-Spawn.SpawnReason.Custom.Prevent.SilverFishes", true)) 
        {
            if (event.getSpawnReason() == SpawnReason.CUSTOM)
            {
                if (event.getCreatureType() == CreatureType.SILVERFISH) 
                {
                    event.setCancelled(true);
                    event.getEntity().remove();
                }
            }
        }
        
        if(plugin.getMobsConfig().getBoolean("Mob-Spawn.SpawnReason.Custom.Prevent.Skeletons", true)) 
        {
            if (event.getSpawnReason() == SpawnReason.CUSTOM)
            {
                if (event.getCreatureType() == CreatureType.SKELETON) 
                {
                    event.setCancelled(true);
                    event.getEntity().remove();
                }
            }
        }
        
        if(plugin.getMobsConfig().getBoolean("Mob-Spawn.SpawnReason.Custom.Prevent.Slimes", true)) 
        {
            if (event.getSpawnReason() == SpawnReason.CUSTOM)
            {
                if (event.getCreatureType() == CreatureType.SLIME) 
                {
                    event.setCancelled(true);
                    event.getEntity().remove();
                }
            }
        }
        
        if(plugin.getMobsConfig().getBoolean("Mob-Spawn.SpawnReason.Custom.Prevent.Snowmen", true)) 
        {
            if (event.getSpawnReason() == SpawnReason.CUSTOM)
            {
                if (event.getCreatureType() == CreatureType.SNOWMAN) 
                {
                    event.setCancelled(true);
                    event.getEntity().remove();
                }
            }
        }
        
        if(plugin.getMobsConfig().getBoolean("Mob-Spawn.SpawnReason.Custom.Prevent.Spiders", true)) 
        {
            if (event.getSpawnReason() == SpawnReason.CUSTOM)
            {
                if (event.getCreatureType() == CreatureType.SPIDER) 
                {
                    event.setCancelled(true);
                    event.getEntity().remove();
                }
            }
        }
        
        if(plugin.getMobsConfig().getBoolean("Mob-Spawn.SpawnReason.Custom.Prevent.Squids", true)) 
        {
            if (event.getSpawnReason() == SpawnReason.CUSTOM)
            {
                if (event.getCreatureType() == CreatureType.SQUID) 
                {
                    event.setCancelled(true);
                    event.getEntity().remove();
                }
            }
        }
        
        if(plugin.getMobsConfig().getBoolean("Mob-Spawn.SpawnReason.Custom.Prevent.Villagers", true)) 
        {
            if (event.getSpawnReason() == SpawnReason.CUSTOM)
            {
                if (event.getCreatureType() == CreatureType.VILLAGER) 
                {
                    event.setCancelled(true);
                    event.getEntity().remove();
                }
            }
        }
        
        if(plugin.getMobsConfig().getBoolean("Mob-Spawn.SpawnReason.Custom.Prevent.Wolfs", true)) 
        {
            if (event.getSpawnReason() == SpawnReason.CUSTOM)
            {
                if (event.getCreatureType() == CreatureType.WOLF) 
                {
                    event.setCancelled(true);
                    event.getEntity().remove();
                }
            }
        }
        
        if(plugin.getMobsConfig().getBoolean("Mob-Spawn.SpawnReason.Custom.Prevent.Zombies", true)) 
        {
            if (event.getSpawnReason() == SpawnReason.CUSTOM)
            {
                if (event.getCreatureType() == CreatureType.ZOMBIE) 
                {
                    event.setCancelled(true);
                    event.getEntity().remove();
                }
            }
        }
        
        /**
         * Spawn Reason = 
         */
        
        
    }
    
    @EventHandler(priority = EventPriority.NORMAL)
    public void onEndermanPickup(EntityChangeBlockEvent event) {
        if (event.isCancelled())
        {
            return;
        }
        
        Entity entity = event.getEntity();
        
        if(plugin.getMobsConfig().getBoolean("Mobs.Endermaen.Prevent-Endermen-griefing", true))
        {
            if (entity instanceof Enderman) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.isCancelled())
        {
            return;
        }
        
        Entity entity = event.getEntity();
        World world = entity.getWorld();
        
        if(plugin.getConfig().getBoolean("Entity-Damage.Disable-Fire-damage", true))
        {
            if(event.getCause().equals(DamageCause.FIRE) 
                    || event.getCause().equals(DamageCause.FIRE_TICK)) {
                if (entity instanceof Player) {
                    event.setCancelled(true);
                    event.setDamage(0);
                }
            }
            else {
                    if(plugin.getConfig().getBoolean("Entity-Damage.Disable-Fire-Creature-damage", true))
                    {
                        if(event.getCause().equals(DamageCause.FIRE) 
                            || event.getCause().equals(DamageCause.FIRE_TICK)) {
                        if (entity instanceof Creature) {
                            event.setCancelled(true);
                            event.setDamage(0);
                        }
                    }
                }
            }
        }
        
        if(plugin.getConfig().getBoolean("Entity-Damage.Disable-Lightning-damage", true))
        {
            if(event.getCause() == DamageCause.LIGHTNING) {
                if (entity instanceof Player) {
                    event.setCancelled(true);
                    event.setDamage(0);
                }
            }
            else {
                if(plugin.getConfig().getBoolean("Entity-Damage.Disable-Lightning-Creature-damage", true))
                {
                    if (entity instanceof Creature) {
                        event.setCancelled(true);
                        event.setDamage(0);
                    }
                }
            }
        }
        
        if(plugin.getConfig().getBoolean("Entity-Damage.Disable-Void-damage", true))
        {
            if(event.getCause() == DamageCause.VOID) {
                if (entity instanceof Player) {
                    event.setCancelled(true);
                    event.setDamage(0);
                    
                    if (plugin.getConfig().getBoolean("EntityTo-SpawnLocation.On-Void-fall(Player)", true)) {
                         entity.teleport(world.getSpawnLocation());
                    }
                }
            }
            else {
                    if(plugin.getConfig().getBoolean("Entity-Damage.Disable-Void-Creature-damage", true))
                    {
                        if (entity instanceof Creature) {
                            event.setCancelled(true);
                            event.setDamage(0);
                            
                            if (plugin.getConfig().getBoolean("EntityTo-SpawnLocation.On-Void-fall(Creature)", true)) {
                                 entity.teleport(world.getSpawnLocation());
                            }
                        }
                    }
                }
        }
        
        if(plugin.getConfig().getBoolean("Entity-Damage.Disable-Fall-damage", true))
        {
            if(event.getCause() == DamageCause.FALL) {
                if (entity instanceof Player) {
                    event.setCancelled(true);
                    event.setDamage(0);
                }
            }
            else {
                if(plugin.getConfig().getBoolean("Entity-Damage.Disable-Fall-Creature-damage", true))
                {
                    if (entity instanceof Creature) {
                        event.setCancelled(true);
                        event.setDamage(0);
                    }
                }
            }
        }
        
        if(plugin.getConfig().getBoolean("Entity-Damage.Disable-Suffocation-damage", true))
        {
            if(event.getCause() == DamageCause.SUFFOCATION) {
                if (entity instanceof Player) {
                    event.setCancelled(true);
                    event.setDamage(0);
                }
            }
            else {
                if(plugin.getConfig().getBoolean("Entity-Damage.Disable-Suffocation-Creature-damage", true))
                {
                    if (entity instanceof Creature) {
                        event.setCancelled(true);
                        event.setDamage(0);
                    }
                }
            }
        }
        
        if(plugin.getConfig().getBoolean("Entity-Damage.Disable-Drowning-damage", true))
        {
            if(event.getCause() == DamageCause.DROWNING) {
                if (entity instanceof Player) {
                    event.setCancelled(true);
                    event.setDamage(0);
                }
            }
            else {
                if(plugin.getConfig().getBoolean("Entity-Damage.Disable-Drowning-Creature-damage", true))
                {
                    if (entity instanceof Creature) {
                        event.setCancelled(true);
                        event.setDamage(0);
                    }
                }
            }
        }
        
        if(plugin.getConfig().getBoolean("Entity-Damage.Disable-Lava-damage", true))
        {
            if(event.getCause() == DamageCause.LAVA) {
                if (entity instanceof Player) {
                    event.setCancelled(true);
                    event.setDamage(0);
                }
            }
            else {
                if(plugin.getConfig().getBoolean("Entity-Damage.Disable-Lava-Creature-damage", true))
                {
                    if (entity instanceof Creature) {
                        event.setCancelled(true);
                        event.setDamage(0);
                    }
                }
            }
        }
        
        if(plugin.getConfig().getBoolean("Entity-Damage.Disable-Contact-damage", true))
        {
            if(event.getCause() == DamageCause.CONTACT) {
                if (entity instanceof Player) {
                    event.setCancelled(true);
                    event.setDamage(0);
                }
            }
            else {
                if(plugin.getConfig().getBoolean("Entity-Damage.Disable-Contact-Creature-damage", true))
                {
                    if (entity instanceof Creature) {
                        event.setCancelled(true);
                        event.setDamage(0);
                    }
                }
            }
        }
        
        if(plugin.getConfig().getBoolean("Entity-Damage.Disable-Projectile-damage", true))
        {
            if(event.getCause() == DamageCause.PROJECTILE) {
                if (entity instanceof Player) {
                    event.setCancelled(true);
                    event.setDamage(0);
                }
            }
            else {
                if(plugin.getConfig().getBoolean("Entity-Damage.Disable-Projectile-Creature-damage", true))
                {
                    if (entity instanceof Creature) {
                        event.setCancelled(true);
                        event.setDamage(0);
                    }
                }
            }
        }
        
        if(plugin.getConfig().getBoolean("Entity-Damage.Disable-Starvation-damage", true))
        {
            if(event.getCause() == DamageCause.STARVATION) {
                if (entity instanceof Player) {
                    event.setCancelled(true);
                    event.setDamage(0);
                }
            }
            else {
                if(plugin.getConfig().getBoolean("Entity-Damage.Disable-Starvation-Creature-damage", true))
                {
                    if (entity instanceof Creature) {
                        event.setCancelled(true);
                        event.setDamage(0);
                    }
                }
            }
        }
        
        if(plugin.getConfig().getBoolean("Entity-Damage.Disable-Suicide-damage", true))
        {
            if(event.getCause() == DamageCause.SUICIDE) {
                if (entity instanceof Player) {
                    event.setCancelled(true);
                    event.setDamage(0);
                }
            }
            else {
                if(plugin.getConfig().getBoolean("Entity-Damage.Disable-Suicide-Creature-damage", true))
                {
                    if (entity instanceof Creature) {
                        event.setCancelled(true);
                        event.setDamage(0);
                    }
                }
            }
        }
        
        if(plugin.getConfig().getBoolean("Entity-Damage.Disable-Entity_Attack-damage", true))
        {
            if(event.getCause() == DamageCause.ENTITY_ATTACK) {
                if (entity instanceof Player) {
                    event.setCancelled(true);
                    event.setDamage(0);
                }
            }
            else {
                if(plugin.getConfig().getBoolean("Entity-Damage.Disable-Entity_Attack-Creature-damage", true))
                {
                    if (entity instanceof Creature) {
                        event.setCancelled(true);
                        event.setDamage(0);
                    }
                }
            }
        }
        
        if(plugin.getConfig().getBoolean("Entity-Damage.Disable-Custom-damage", true))
        {
            if(event.getCause() == DamageCause.CUSTOM) {
                if (entity instanceof Player) {
                    event.setCancelled(true);
                    event.setDamage(0);
                } 
                else {
                    if(plugin.getConfig().getBoolean("Entity-Damage.Disable-Custom-Creature-damage", true))
                    {
                        if (entity instanceof Creature) {
                            event.setCancelled(true);
                            event.setDamage(0);
                        }
                    }
                }
            }
        }
        
        //new
        if(plugin.getConfig().getBoolean("Explosions.Disable-Disable-Block_Explosion-damage", true))
        {
            if(event.getCause() == DamageCause.BLOCK_EXPLOSION) {
                if (entity instanceof Player) {
                    event.setCancelled(true);
                    event.setDamage(0);
                }
            }
        }
        
        if(plugin.getConfig().getBoolean("Explosions.Disable-Entity_Explosion-damage", true))
        {
            if(event.getCause() == DamageCause.ENTITY_EXPLOSION) {
                if (entity instanceof Player) {
                    event.setCancelled(true);
                    event.setDamage(0);
                }
            }
        }
        
        if(plugin.getConfig().getBoolean("Entity-Damage.Disable-Magic-damage", true))
        {
            if(event.getCause() == DamageCause.MAGIC) {
                if (entity instanceof Player) {
                    event.setCancelled(true);
                    event.setDamage(0);
                }
                else {
                    if(plugin.getConfig().getBoolean("Entity-Damage.Disable-Magic-Creature-damage", true))
                    {
                        if (entity instanceof Creature) {
                            event.setCancelled(true);
                            event.setDamage(0);
                        }
                    }
                }
            }
        }
        
        if(plugin.getConfig().getBoolean("Entity-Damage.Disable-Poison-damage", true))
        {
            if(event.getCause() == DamageCause.POISON) {
                if (entity instanceof Player) {
                    event.setCancelled(true);
                    event.setDamage(0);
                }
            }
            else {
                if(plugin.getConfig().getBoolean("Entity-Damage.Disable-Poison-Creature-damage", true))
                {
                    if (entity instanceof Creature) {
                        event.setCancelled(true);
                        event.setDamage(0);
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        if (event.isCancelled())
        {
            return;
        }
        
        if(plugin.getConfig().getBoolean("Player.Disable-Hunger", true))
        {
            event.setCancelled(true);
            event.setFoodLevel(20);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityDeath(EntityDeathEvent event) {
        if(plugin.getConfig().getBoolean("World.Disable-ExpirienceOrbs-drop", true))
        {
            event.setDroppedExp(0);
        }
        
        if(plugin.getMobsConfig().getBoolean("Mobs.Prevent-Object-drop-on-death", true))
        {
            event.getDrops().clear();
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onSlimeSplit(SlimeSplitEvent event) {
        if (event.isCancelled())
        {
            return;
        }
        
        if(!plugin.getMobsConfig().getBoolean("Mobs.Allow-SlimeSplit", true))
        {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityTarget(EntityTargetEvent event) {
        if (event.isCancelled())
        {
            return;
        }
        
        if(plugin.getMobsConfig().getBoolean("Mobs.EntityTarget.Disable-closest_player-target", true))
        {
            if(event.getReason() == TargetReason.CLOSEST_PLAYER) {
                event.setCancelled(true);
            }
        }
        if(plugin.getMobsConfig().getBoolean("Mobs.EntityTarget.Disable-custom-target", true))
        {
            if(event.getReason() == TargetReason.CUSTOM) {
                event.setCancelled(true);
            }
        }
        if(plugin.getMobsConfig().getBoolean("Mobs.EntityTarget.Disable-forgot_target-target", true))
        {
            if(event.getReason() == TargetReason.FORGOT_TARGET) {
                event.setCancelled(true);
            }
        }
        if(plugin.getMobsConfig().getBoolean("Mobs.EntityTarget.Disable-owner_attacked_target-target", true))
        {
            if(event.getReason() == TargetReason.OWNER_ATTACKED_TARGET) {
                event.setCancelled(true);
            }
        }
        if(plugin.getMobsConfig().getBoolean("Mobs.EntityTarget.Disable-pig_zombie_target-target", true))
        {
            if(event.getReason() == TargetReason.PIG_ZOMBIE_TARGET) {
                event.setCancelled(true);
            }
        }
        if(plugin.getMobsConfig().getBoolean("Mobs.EntityTarget.Disable-random_target-target", true))
        {
            if(event.getReason() == TargetReason.RANDOM_TARGET) {
                event.setCancelled(true);
            }
        }
        if(plugin.getMobsConfig().getBoolean("Mobs.EntityTarget.Disable-target_attacked_entity-target", true))
        {
            if(event.getReason() == TargetReason.TARGET_ATTACKED_ENTITY) {
                event.setCancelled(true);
            }
        }
        if(plugin.getMobsConfig().getBoolean("Mobs.EntityTarget.Disable-target_attacked_owner-target", true))
        {
            if(event.getReason() == TargetReason.TARGET_ATTACKED_OWNER) {
                event.setCancelled(true);
            }
        }
        if(plugin.getMobsConfig().getBoolean("Mobs.EntityTarget.Disable-target_died-target", true))
        {
            if(event.getReason() == TargetReason.TARGET_DIED) {
                event.setCancelled(true);
            }
        }
    }
    
    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityInteract(EntityInteractEvent event) {
        if (event.isCancelled())
        {
            return;
        }
        
        if(plugin.getConfig().getBoolean("Misc.Prevent-crop-trampling-by-creature", true))
        {
            if (event.getBlock().getType() == Material.SOIL && event.getEntity() instanceof Creature) {
                event.setCancelled(true);
            }
        }
        if(plugin.getConfig().getBoolean("Misc.Prevent-crop-trampling-by-player", true))
        {
            if (event.getBlock().getType() == Material.SOIL && event.getEntity() instanceof Player) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPigZap(PigZapEvent event) {
        if (event.isCancelled())
        {
            return;
        }
        
        if(plugin.getMobsConfig().getBoolean("Mobs.Prevent-PigZap(Pig transformation to ZombiePig)", true))
        {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityTame(EntityTameEvent event) {
        if (event.isCancelled())
        {
            return;
        }
        
        if(plugin.getMobsConfig().getBoolean("Mobs.Tame.Prevent-taming", true))
        {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onItemSpawn(ItemSpawnEvent event) {
        if (event.isCancelled())
        {
            return;
        }
        
        Entity entity = event.getEntity();
        
        if(plugin.getConfig().getBoolean("World.Prevent-items/objects-to-spawn-into-the-world", true))
        {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onCreeperPower(CreeperPowerEvent event) {
        if (event.isCancelled())
        {
            return;
        }
        
        Entity entity = event.getEntity();
        
        if(plugin.getMobsConfig().getBoolean("Mobs.Powered-Creepers.Prevent-PowerCause.Lightning", true))
        {
            if (event.getCause() == PowerCause.LIGHTNING) {
                event.setCancelled(true);
            }
        }
        if(plugin.getMobsConfig().getBoolean("Mobs.Powered-Creepers.Prevent-PowerCause.Set-Off", true))
        {
            if (event.getCause() == PowerCause.SET_OFF) {
                event.setCancelled(true);
            }
        }
        if(plugin.getMobsConfig().getBoolean("Mobs.Powered-Creepers.Prevent-PowerCause.Set-On", true))
        {
            if (event.getCause() == PowerCause.SET_ON) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityCombust(EntityCombustEvent event) {
        if (event.isCancelled())
        {
            return;
        }
        
        Entity entity = event.getEntity();
        
        if(plugin.getMobsConfig().getBoolean("Mobs.Prevent-Entity-Combust", true))
        {
            event.setDuration(0);
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityRegainHealth(EntityRegainHealthEvent event) {
        if (event.isCancelled())
        {
            return;
        }
        
        
        if(plugin.getConfig().getBoolean("Entity/Player.Completely-Prevent.Health-Regeneration", true))
        {
            event.setCancelled(true);
        }
        if(plugin.getConfig().getBoolean("Entity/Player.Prevent.Custom-Health-Regeneration", true))
        {
            if (event.getRegainReason() == RegainReason.CUSTOM) {
                event.setCancelled(true);
            }
        }
        if(plugin.getConfig().getBoolean("Entity/Player.Prevent.Eating-Health-Regeneration", true))
        {
            if (event.getRegainReason() == RegainReason.EATING) {
                event.setCancelled(true);
            }
        }
        if(plugin.getConfig().getBoolean("Entity/Player.Prevent.Regen-Health-Regeneration", true))
        {
            if (event.getRegainReason() == RegainReason.REGEN) {
                event.setCancelled(true);
            }
        }
        if(plugin.getConfig().getBoolean("Entity/Player.Prevent.Satiated-Health-Regeneration", true))
        {
            if (event.getRegainReason() == RegainReason.SATIATED) {
                event.setCancelled(true);
            }
        }
    }
    
    @EventHandler
    public void SheepDyeWool(SheepDyeWoolEvent event) {
        if (event.isCancelled())
        {
            return;
        }
        
        if (plugin.getMobsConfig().getBoolean("Completely-Prevent-SheepDyeWool", true))
        {
            event.setCancelled(true);
        }    
        
        if (plugin.getMobsConfig().getBoolean("Prevent-SheepDyeWool-Color.Black", true))
        {
            if (event.getColor() == DyeColor.BLACK) 
            {
                event.setCancelled(true);
            }
        }
        
        if (plugin.getMobsConfig().getBoolean("Prevent-SheepDyeWool-Color.Blue", true))
        {
            if (event.getColor() == DyeColor.BLUE) 
            {
                event.setCancelled(true);
            }
        }
        
        if (plugin.getMobsConfig().getBoolean("Prevent-SheepDyeWool-Color.Brown", true))
        {
            if (event.getColor() == DyeColor.BROWN) 
            {
                event.setCancelled(true);
            }
        }
        
        if (plugin.getMobsConfig().getBoolean("Prevent-SheepDyeWool-Color.Cyan", true))
        {
            if (event.getColor() == DyeColor.CYAN) 
            {
                event.setCancelled(true);
            }
        }
        
        if (plugin.getMobsConfig().getBoolean("Prevent-SheepDyeWool-Color.Gray", true))
        {
            if (event.getColor() == DyeColor.GRAY) 
            {
                event.setCancelled(true);
            }
        }
        
        if (plugin.getMobsConfig().getBoolean("Prevent-SheepDyeWool-Color.Green", true))
        {
            if (event.getColor() == DyeColor.GREEN) 
            {
                event.setCancelled(true);
            }
        }
        
        if (plugin.getMobsConfig().getBoolean("Prevent-SheepDyeWool-Color.Light_Blue", true))
        {
            if (event.getColor() == DyeColor.LIGHT_BLUE) 
            {
                event.setCancelled(true);
            }
        }
        
        if (plugin.getMobsConfig().getBoolean("Prevent-SheepDyeWool-Color.Lime", true))
        {
            if (event.getColor() == DyeColor.LIME) 
            {
                event.setCancelled(true);
            }
        }
        
        if (plugin.getMobsConfig().getBoolean("Prevent-SheepDyeWool-Color.Magenta", true))
        {
            if (event.getColor() == DyeColor.MAGENTA) 
            {
                event.setCancelled(true);
            }
        }
        
        if (plugin.getMobsConfig().getBoolean("Prevent-SheepDyeWool-Color.Orange", true))
        {
            if (event.getColor() == DyeColor.ORANGE) 
            {
                event.setCancelled(true);
            }
        }
        
        if (plugin.getMobsConfig().getBoolean("Prevent-SheepDyeWool-Color.Pink", true))
        {
            if (event.getColor() == DyeColor.PINK) 
            {
                event.setCancelled(true);
            }
        }
        
        if (plugin.getMobsConfig().getBoolean("Prevent-SheepDyeWool-Color.Purple", true))
        {
            if (event.getColor() == DyeColor.PURPLE) 
            {
                event.setCancelled(true);
            }
        }
        
        if (plugin.getMobsConfig().getBoolean("Prevent-SheepDyeWool-Color.Red", true))
        {
            if (event.getColor() == DyeColor.RED) 
            {
                event.setCancelled(true);
            }
        }
        
        if (plugin.getMobsConfig().getBoolean("Prevent-SheepDyeWool-Color.Silver", true))
        {
            if (event.getColor() == DyeColor.SILVER) 
            {
                event.setCancelled(true);
            }
        }
        
        if (plugin.getMobsConfig().getBoolean("Prevent-SheepDyeWool-Color.White", true))
        {
            if (event.getColor() == DyeColor.WHITE) 
            {
                event.setCancelled(true);
            }
        }
        
        if (plugin.getMobsConfig().getBoolean("Prevent-SheepDyeWool-Color.Yellow", true))
        {
            if (event.getColor() == DyeColor.YELLOW) 
            {
                event.setCancelled(true);
            }
        }
    }
    
    public void NoCreeperDeathOnExplosion(ExplosionPrimeEvent event) {
        if ( event.getEntity() instanceof Creeper == false ) 
            return;
        
        Creeper creeper = (Creeper) event.getEntity();
         
        if ( creeper.getTarget() instanceof Player ) {
            event.setCancelled(true);
            creeper.setTarget(null);
        }
    }
}
