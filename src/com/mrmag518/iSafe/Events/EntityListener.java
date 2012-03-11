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

package com.mrmag518.iSafe.Events;

import com.mrmag518.iSafe.*;

import org.bukkit.entity.*;
import org.bukkit.event.entity.*;
import org.bukkit.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
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
            if (event.isCancelled()) 
            {
                NoCreeperDeathOnExplosion(event);
            }
        }
    }
    
    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        if (event.isCancelled())
        {
            return;
        }
        
        Entity ent = event.getEntity();
        int x = (int) event.getLocation().getX();
        int y = (int) event.getLocation().getY();
        int z = (int) event.getLocation().getZ();
        String world = ent.getWorld().getName();
        
        if(plugin.getConfig().getBoolean("Explosions.Disable-explosions", true))
        {
            int blocks = event.blockList().size();
            event.blockList().clear();
            if(plugin.getConfig().getBoolean("Explosions.Debug-explosions", true))
            {
                plugin.log.info("[iSafe](Debug)" + " An explosion was prevented at the location: X: "+ x + " Y: "+ y 
                        + " Z: "+ z + " | Yield: "+ event.getYield()
                        + " | Amount of blocks: " + blocks + " | World: "+ world + " | Caused by: Unknown.");
            }
            return;
        }
        
        if(plugin.getConfig().getBoolean("Explosions.Disable-Creeper-explosions", true))
        {
            int blocks = event.blockList().size();
            if (ent instanceof Creeper) 
            {
                event.blockList().clear();
                
                if(plugin.getConfig().getBoolean("Explosions.Debug-explosions", true))
                {
                plugin.log.info("[iSafe](Debug)" + " An explosion was prevented at the location: X: "+ x + " Y: "+ y 
                        + " Z: "+ z + " | Yield: "+ event.getYield()
                        + " | Amount of blocks: " + blocks + " | World: "+ world + " | Caused by: Creeper.");
                }
                return;
            }
        }
        
        if(plugin.getConfig().getBoolean("Explosions.Disable-EnderDragon-blockdamage", true))
        {
            int blocks = event.blockList().size();
            if (ent instanceof EnderDragon) 
            {
                event.blockList().clear();
                
                if(plugin.getConfig().getBoolean("Explosions.Debug-explosions", true))
                {
                plugin.log.info("[iSafe](Debug)" + " An explosion was prevented at the location: X: "+ x + " Y: "+ y 
                        + " Z: "+ z + " | Yield: "+ event.getYield()
                        + " | Amount of blocks: " + blocks + " | World: "+ world + " | Caused by: EnderDragon.");
                }
                return;
            }
        }
        
        if(plugin.getConfig().getBoolean("Explosions.Disable-TNT-explosions", true))
        {
            int blocks = event.blockList().size();
            if (ent instanceof TNTPrimed) 
            {
                event.blockList().clear();
                
                if(plugin.getConfig().getBoolean("Explosions.Debug-explosions", true))
                {
                plugin.log.info("[iSafe](Debug)" + " An explosion was prevented at the location: X: "+ x + " Y: "+ y 
                        + " Z: "+ z + " | Yield: "+ event.getYield()
                        + " | Amount of blocks: " + blocks + " | World: "+ world + " | Caused by: TNT.");
                }
                return;
            }
        }
        
        if(plugin.getConfig().getBoolean("Explosions.Disable-Fireball-explosions", true))
        {
            int blocks = event.blockList().size();
            if (ent instanceof Fireball) 
            {
                event.blockList().clear();
                
                if(plugin.getConfig().getBoolean("Explosions.Debug-explosions", true))
                {
                plugin.log.info("[iSafe](Debug)" + " An explosion was prevented at the location: X: "+ x + " Y: "+ y 
                        + " Z: "+ z + " | Yield: "+ event.getYield()
                        + " | Amount of blocks: " + blocks + " | World: "+ world + " | Caused by: Fireball.");
                }
                return;
            }  
        }
        
        if(plugin.getConfig().getBoolean("Explosions.Disable-EnderCrystal-explosions", true))
        {
            int blocks = event.blockList().size();
            if (ent instanceof EnderCrystal) 
            {
                event.blockList().clear();
                
                if(plugin.getConfig().getBoolean("Explosions.Debug-explosions", true))
                {
                    plugin.log.info("[iSafe](Debug)" + " An explosion was prevented at the location: X: "+ x + " Y: "+ y 
                            + " Z: "+ z + " | Yield: "+ event.getYield()
                            + " | Amount of blocks: " + blocks + " | World: "+ world + " | Caused by: EnderCrystal.");
                }
                return;
            }  
        }
    }
    
    @EventHandler
    public void onEndermanPickup(EntityChangeBlockEvent event) {
        if (event.isCancelled())
        {
            return;
        }
        
        Entity entity = event.getEntity();
        
        if(plugin.getMobsConfig().getBoolean("Mobs.Endermen.Prevent-Endermen-griefing", true))
        {
            if (entity instanceof Enderman) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.isCancelled())
        {
            return;
        }
        Entity entity = event.getEntity();
        World world = entity.getWorld();
        
        if (plugin.getConfig().getBoolean("EntityTo-SpawnLocation.On-Void-fall(Player)", true)) 
        {
            entity.teleport(world.getSpawnLocation());
        }
        if (plugin.getConfig().getBoolean("EntityTo-SpawnLocation.On-Void-fall(Creature)", true)) 
        {
            entity.teleport(world.getSpawnLocation());
        }
        
        if(plugin.getConfig().getBoolean("Entity-Damage.Disable-npc(Villagers)-death/damage", true))
        {
            if(entity instanceof Villager) 
            {
                event.setDamage(0);
                event.setCancelled(true);
            }
        }
        if(plugin.getConfig().getBoolean("Entity-Damage.Disable-player-death/damage", true))
        {
            if(entity instanceof Player) 
            {
                event.setDamage(0);
                event.setCancelled(true);
            }
        }
        
        if(plugin.getConfig().getBoolean("Explosions.Disable-Block_Explosion-damage", true))
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
        
        if(plugin.getConfig().getBoolean("Entity-Damage.Players.Disable-Fire-damage", true))
        {
            if(event.getCause().equals(DamageCause.FIRE) || event.getCause().equals(DamageCause.FIRE_TICK)) {
                if (entity instanceof Player) {
                    if(plugin.getConfig().getBoolean("Entity-Damage.Enable-permissions", true)) {
                        Player player = (Player)entity;
                        if(player.hasPermission("iSafe.canceldamage.fire")) {
                            event.setDamage(0);
                            event.setCancelled(true);
                        }
                    } else {
                        event.setDamage(0);
                        event.setCancelled(true);
                    }
                }
            }
        }
        if(plugin.getConfig().getBoolean("Entity-Damage.Creatures.Disable-Fire-damage", true))
        {
            if(event.getCause().equals(DamageCause.FIRE) || event.getCause().equals(DamageCause.FIRE_TICK)) {
                if (entity instanceof Creature || entity instanceof Animals) {
                    event.setCancelled(true);
                    event.setDamage(0);
                }
            }
        }
        
        if(plugin.getConfig().getBoolean("Entity-Damage.Players.Disable-Contact-damage", true))
        {
            if(event.getCause().equals(DamageCause.CONTACT)) {
                if (entity instanceof Player) {
                    if(plugin.getConfig().getBoolean("Entity-Damage.Enable-permissions", true)) {
                        Player player = (Player)entity;
                        if(player.hasPermission("iSafe.canceldamage.contact")) {
                            event.setDamage(0);
                            event.setCancelled(true);
                        }
                    } else {
                        event.setDamage(0);
                        event.setCancelled(true);
                    }
                }
            }
        }
        if(plugin.getConfig().getBoolean("Entity-Damage.Creatures.Disable-Contact-damage", true))
        {
            if(event.getCause().equals(DamageCause.CONTACT)) {
                if (entity instanceof Creature || entity instanceof Animals) {
                    event.setDamage(0);
                    event.setCancelled(true);
                }
            }
        }
        
        if(plugin.getConfig().getBoolean("Entity-Damage.Players.Disable-Custom-damage", true))
        {
            if(event.getCause().equals(DamageCause.CUSTOM)) {
                if (entity instanceof Player) {
                    if(plugin.getConfig().getBoolean("Entity-Damage.Enable-permissions", true)) {
                        Player player = (Player)entity;
                        if(player.hasPermission("iSafe.canceldamage.custom")) {
                            event.setDamage(0);
                            event.setCancelled(true);
                        }
                    } else {
                        event.setDamage(0);
                        event.setCancelled(true);
                    }
                }
            }
        }
        if(plugin.getConfig().getBoolean("Entity-Damage.Creatures.Disable-Custom-damage", true))
        {
            if(event.getCause().equals(DamageCause.CUSTOM)) {
                if (entity instanceof Creature || entity instanceof Animals) {
                    event.setDamage(0);
                    event.setCancelled(true);
                }
            }
        }
        
        if(plugin.getConfig().getBoolean("Entity-Damage.Players.Disable-Drowning-damage", true))
        {
            if(event.getCause().equals(DamageCause.DROWNING)) {
                if (entity instanceof Player) {
                    if(plugin.getConfig().getBoolean("Entity-Damage.Enable-permissions", true)) {
                        Player player = (Player)entity;
                        if(player.hasPermission("iSafe.canceldamage.drowning")) {
                            event.setDamage(0);
                            event.setCancelled(true);
                        }
                    } else {
                        event.setDamage(0);
                        event.setCancelled(true);
                    }
                }
            }
        }
        if(plugin.getConfig().getBoolean("Entity-Damage.Creatures.Disable-Drowning-damage", true))
        {
            if(event.getCause().equals(DamageCause.DROWNING)) {
                if (entity instanceof Creature || entity instanceof Animals) {
                    event.setDamage(0);
                    event.setCancelled(true);
                }
            }
        }
        
        if(plugin.getConfig().getBoolean("Entity-Damage.Players.Disable-EntityAttack-damage", true))
        {
            if(event.getCause().equals(DamageCause.ENTITY_ATTACK)) {
                if (entity instanceof Player) {
                    if(plugin.getConfig().getBoolean("Entity-Damage.Enable-permissions", true)) {
                        Player player = (Player)entity;
                        if(player.hasPermission("iSafe.canceldamage.entityattack")) {
                            event.setDamage(0);
                            event.setCancelled(true);
                        }
                    } else {
                        event.setDamage(0);
                        event.setCancelled(true);
                    }
                }
            }
        }
        if(plugin.getConfig().getBoolean("Entity-Damage.Creatures.Disable-EntityAttack-damage", true))
        {
            if(event.getCause().equals(DamageCause.ENTITY_ATTACK)) {
                if (entity instanceof Creature || entity instanceof Animals) {
                    event.setDamage(0);
                    event.setCancelled(true);
                }
            }
        }
        
        if(plugin.getConfig().getBoolean("Entity-Damage.Players.Disable-Fall-damage", true))
        {
            if(event.getCause().equals(DamageCause.FALL)) {
                if (entity instanceof Player) {
                    if(plugin.getConfig().getBoolean("Entity-Damage.Enable-permissions", true)) {
                        Player player = (Player)entity;
                        if(player.hasPermission("iSafe.canceldamage.fall")) {
                            event.setDamage(0);
                            event.setCancelled(true);
                        }
                    } else {
                        event.setDamage(0);
                        event.setCancelled(true);
                    }
                }
            }
        }
        if(plugin.getConfig().getBoolean("Entity-Damage.Creatures.Disable-Fall-damage", true))
        {
            if(event.getCause().equals(DamageCause.FALL)) {
                if (entity instanceof Creature || entity instanceof Animals) {
                    event.setDamage(0);
                    event.setCancelled(true);
                }
            }
        }
        
        if(plugin.getConfig().getBoolean("Entity-Damage.Players.Disable-Lava-damage", true))
        {
            if(event.getCause().equals(DamageCause.LAVA)) {
                if (entity instanceof Player) {
                    if(plugin.getConfig().getBoolean("Entity-Damage.Enable-permissions", true)) {
                        Player player = (Player)entity;
                        if(player.hasPermission("iSafe.canceldamage.lava")) {
                            event.setDamage(0);
                            event.setCancelled(true);
                        }
                    } else {
                        event.setDamage(0);
                        event.setCancelled(true);
                    }
                }
            }
        }
        if(plugin.getConfig().getBoolean("Entity-Damage.Creatures.Disable-Lava-damage", true))
        {
            if(event.getCause().equals(DamageCause.LAVA)) {
                if (entity instanceof Creature || entity instanceof Animals) {
                    event.setDamage(0);
                    event.setCancelled(true);
                }
            }
        }
        
        if(plugin.getConfig().getBoolean("Entity-Damage.Players.Disable-Lightning-damage", true))
        {
            if(event.getCause().equals(DamageCause.LIGHTNING)) {
                if (entity instanceof Player) {
                    if(plugin.getConfig().getBoolean("Entity-Damage.Enable-permissions", true)) {
                        Player player = (Player)entity;
                        if(player.hasPermission("iSafe.canceldamage.lightning")) {
                            event.setDamage(0);
                            event.setCancelled(true);
                        }
                    } else {
                        event.setDamage(0);
                        event.setCancelled(true);
                    }
                }
            }
        }
        if(plugin.getConfig().getBoolean("Entity-Damage.Creatures.Disable-Lightning-damage", true))
        {
            if(event.getCause().equals(DamageCause.LIGHTNING)) {
                if (entity instanceof Creature || entity instanceof Animals) {
                    event.setDamage(0);
                    event.setCancelled(true);
                }
            }
        }
        
        if(plugin.getConfig().getBoolean("Entity-Damage.Players.Disable-Magic-damage", true))
        {
            if(event.getCause().equals(DamageCause.MAGIC)) {
                if (entity instanceof Player) {
                    if(plugin.getConfig().getBoolean("Entity-Damage.Enable-permissions", true)) {
                        Player player = (Player)entity;
                        if(player.hasPermission("iSafe.canceldamage.magic")) {
                            event.setDamage(0);
                            event.setCancelled(true);
                        }
                    } else {
                        event.setDamage(0);
                        event.setCancelled(true);
                    }
                }
            }
        }
        if(plugin.getConfig().getBoolean("Entity-Damage.Creatures.Disable-Magic-damage", true))
        {
            if(event.getCause().equals(DamageCause.MAGIC)) {
                if (entity instanceof Creature || entity instanceof Animals) {
                    event.setDamage(0);
                    event.setCancelled(true);
                }
            }
        }
        
        if(plugin.getConfig().getBoolean("Entity-Damage.Players.Disable-Poison-damage", true))
        {
            if(event.getCause().equals(DamageCause.POISON)) {
                if (entity instanceof Player) {
                    if(plugin.getConfig().getBoolean("Entity-Damage.Enable-permissions", true)) {
                        Player player = (Player)entity;
                        if(player.hasPermission("iSafe.canceldamage.poison")) {
                            event.setDamage(0);
                            event.setCancelled(true);
                        }
                    } else {
                        event.setDamage(0);
                        event.setCancelled(true);
                    }
                }
            }
        }
        if(plugin.getConfig().getBoolean("Entity-Damage.Creatures.Disable-Poison-damage", true))
        {
            if(event.getCause().equals(DamageCause.POISON)) {
                if (entity instanceof Creature || entity instanceof Animals) {
                    event.setDamage(0);
                    event.setCancelled(true);
                }
            }
        }
        
        if(plugin.getConfig().getBoolean("Entity-Damage.Players.Disable-Projectile-damage", true))
        {
            if(event.getCause().equals(DamageCause.PROJECTILE)) {
                if (entity instanceof Player) {
                    if(plugin.getConfig().getBoolean("Entity-Damage.Enable-permissions", true)) {
                        Player player = (Player)entity;
                        if(player.hasPermission("iSafe.canceldamage.projectile")) {
                            event.setDamage(0);
                            event.setCancelled(true);
                        }
                    } else {
                        event.setDamage(0);
                        event.setCancelled(true);
                    }
                }
            }
        }
        if(plugin.getConfig().getBoolean("Entity-Damage.Creatures.Disable-Projectile-damage", true))
        {
            if(event.getCause().equals(DamageCause.PROJECTILE)) {
                if (entity instanceof Creature || entity instanceof Animals) {
                    event.setDamage(0);
                    event.setCancelled(true);
                }
            }
        }
        
        if(plugin.getConfig().getBoolean("Entity-Damage.Players.Disable-Starvation-damage", true))
        {
            if(event.getCause().equals(DamageCause.STARVATION)) {
                if (entity instanceof Player) {
                    if(plugin.getConfig().getBoolean("Entity-Damage.Enable-permissions", true)) {
                        Player player = (Player)entity;
                        if(player.hasPermission("iSafe.canceldamage.starvation")) {
                            event.setDamage(0);
                            event.setCancelled(true);
                        }
                    } else {
                        event.setDamage(0);
                        event.setCancelled(true);
                    }
                }
            }
        }
        if(plugin.getConfig().getBoolean("Entity-Damage.Creatures.Disable-Starvation-damage", true))
        {
            if(event.getCause().equals(DamageCause.STARVATION)) {
                if (entity instanceof Creature || entity instanceof Animals) {
                    event.setDamage(0);
                    event.setCancelled(true);
                }
            }
        }
        
        if(plugin.getConfig().getBoolean("Entity-Damage.Players.Disable-Suffocation-damage", true))
        {
            if(event.getCause().equals(DamageCause.SUFFOCATION)) {
                if (entity instanceof Player) {
                    if(plugin.getConfig().getBoolean("Entity-Damage.Enable-permissions", true)) {
                        Player player = (Player)entity;
                        if(player.hasPermission("iSafe.canceldamage.suffocation")) {
                            event.setDamage(0);
                            event.setCancelled(true);
                        }
                    } else {
                        event.setDamage(0);
                        event.setCancelled(true);
                    }
                }
            }
        }
        if(plugin.getConfig().getBoolean("Entity-Damage.Creatures.Disable-Suffocation-damage", true))
        {
            if(event.getCause().equals(DamageCause.SUFFOCATION)) {
                if (entity instanceof Creature || entity instanceof Animals) {
                    event.setDamage(0);
                    event.setCancelled(true);
                }
            }
        }
        
        if(plugin.getConfig().getBoolean("Entity-Damage.Players.Disable-Suicide-damage", true))
        {
            if(event.getCause().equals(DamageCause.SUICIDE)) {
                if (entity instanceof Player) {
                    if(plugin.getConfig().getBoolean("Entity-Damage.Enable-permissions", true)) {
                        Player player = (Player)entity;
                        if(player.hasPermission("iSafe.canceldamage.suicide")) {
                            event.setDamage(0);
                            event.setCancelled(true);
                        }
                    } else {
                        event.setDamage(0);
                        event.setCancelled(true);
                    }
                }
            }
        }
        if(plugin.getConfig().getBoolean("Entity-Damage.Creatures.Disable-Suicide-damage", true))
        {
            if(event.getCause().equals(DamageCause.SUICIDE)) {
                if (entity instanceof Creature || entity instanceof Animals) {
                    event.setDamage(0);
                    event.setCancelled(true);
                }
            }
        }
        
        if(plugin.getConfig().getBoolean("Entity-Damage.Players.Disable-Void-damage", true))
        {
            if(event.getCause().equals(DamageCause.VOID)) {
                if (entity instanceof Player) {
                    if(plugin.getConfig().getBoolean("Entity-Damage.Enable-permissions", true)) {
                        Player player = (Player)entity;
                        if(player.hasPermission("iSafe.canceldamage.void")) {
                            event.setDamage(0);
                            event.setCancelled(true);
                        }
                    } else {
                        event.setDamage(0);
                        event.setCancelled(true);
                    }
                }
            }
        }
        if(plugin.getConfig().getBoolean("Entity-Damage.Creatures.Disable-Void-damage", true))
        {
            if(event.getCause().equals(DamageCause.VOID)) {
                if (entity instanceof Creature || entity instanceof Animals) {
                    event.setDamage(0);
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
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

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if(plugin.getConfig().getBoolean("World.Disable-ExpirienceOrbs-drop", true))
        {
            event.setDroppedExp(0);
        }
        
        if(plugin.getMobsConfig().getBoolean("Misc.Prevent-Object-drop-on-death", true))
        {
            event.getDrops().clear();
        }
    }

    @EventHandler
    public void onSlimeSplit(SlimeSplitEvent event) {
        if (event.isCancelled())
        {
            return;
        }
        
        if(plugin.getMobsConfig().getBoolean("Misc.Prevent-SlimeSplit", true))
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
        
        if(plugin.getMobsConfig().getBoolean("EntityTarget.Disable-closest_player-target", true))
        {
            if(event.getReason() == TargetReason.CLOSEST_PLAYER) 
            {
                event.setCancelled(true);
            }
        }
        if(plugin.getMobsConfig().getBoolean("EntityTarget.Disable-custom-target", true))
        {
            if(event.getReason() == TargetReason.CUSTOM) 
            {
                event.setCancelled(true);
            }
        }
        if(plugin.getMobsConfig().getBoolean("EntityTarget.Disable-forgot_target-target", true))
        {
            if(event.getReason() == TargetReason.FORGOT_TARGET) 
            {
                event.setCancelled(true);
            }
        }
        if(plugin.getMobsConfig().getBoolean("EntityTarget.Disable-owner_attacked_target-target", true))
        {
            if(event.getReason() == TargetReason.OWNER_ATTACKED_TARGET) 
            {
                event.setCancelled(true);
            }
        }
        if(plugin.getMobsConfig().getBoolean("EntityTarget.Disable-pig_zombie_target-target", true))
        {
            if(event.getReason() == TargetReason.PIG_ZOMBIE_TARGET) 
            {
                event.setCancelled(true);
            }
        }
        if(plugin.getMobsConfig().getBoolean("EntityTarget.Disable-random_target-target", true))
        {
            if(event.getReason() == TargetReason.RANDOM_TARGET) 
            {
                event.setCancelled(true);
            }
        }
        if(plugin.getMobsConfig().getBoolean("EntityTarget.Disable-target_attacked_entity-target", true))
        {
            if(event.getReason() == TargetReason.TARGET_ATTACKED_ENTITY) 
            {
                event.setCancelled(true);
            }
        }
        if(plugin.getMobsConfig().getBoolean("EntityTarget.Disable-target_attacked_owner-target", true))
        {
            if(event.getReason() == TargetReason.TARGET_ATTACKED_OWNER) 
            {
                event.setCancelled(true);
            }
        }
        if(plugin.getMobsConfig().getBoolean("EntityTarget.Disable-target_died-target", true))
        {
            if(event.getReason() == TargetReason.TARGET_DIED) 
            {
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
        
        if(plugin.getMobsConfig().getBoolean("Misc.Prevent-PigZap", true))
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
        
        if(plugin.getMobsConfig().getBoolean("Misc.Tame.Prevent-taming", true))
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
        
        if(plugin.getMobsConfig().getBoolean("Powered-Creepers.Prevent-PowerCause.Lightning", true))
        {
            if (event.getCause() == PowerCause.LIGHTNING) {
                event.setCancelled(true);
            }
        }
        if(plugin.getMobsConfig().getBoolean("Powered-Creepers.Prevent-PowerCause.Set-Off", true))
        {
            if (event.getCause() == PowerCause.SET_OFF) {
                event.setCancelled(true);
            }
        }
        if(plugin.getMobsConfig().getBoolean("Powered-Creepers.Prevent-PowerCause.Set-On", true))
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
        
        if(plugin.getMobsConfig().getBoolean("Misc.Prevent-Entity-Combust", true))
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
            if (event.getRegainReason() == RegainReason.CUSTOM) 
            {
                event.setCancelled(true);
            }
        }
        if(plugin.getConfig().getBoolean("Entity/Player.Prevent.Eating-Health-Regeneration", true))
        {
            if (event.getRegainReason() == RegainReason.EATING) 
            {
                event.setCancelled(true);
            }
        }
        if(plugin.getConfig().getBoolean("Entity/Player.Prevent.Regen-Health-Regeneration", true))
        {
            if (event.getRegainReason() == RegainReason.REGEN) 
            {
                event.setCancelled(true);
            }
        }
        if(plugin.getConfig().getBoolean("Entity/Player.Prevent.Satiated-Health-Regeneration", true))
        {
            if (event.getRegainReason() == RegainReason.SATIATED) 
            {
                event.setCancelled(true);
            }
        }
        if(plugin.getConfig().getBoolean("Entity/Player.Prevent.Magic-Health-Regeneration", true))
        {
            if (event.getRegainReason() == RegainReason.MAGIC) 
            {
                event.setCancelled(true);
            }
        }
        if(plugin.getConfig().getBoolean("Entity/Player.Prevent.MagicRegen-Health-Regeneration", true))
        {
            if (event.getRegainReason() == RegainReason.MAGIC_REGEN) 
            {
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
         
        if (creeper.getTarget() instanceof Player ) {
            event.setCancelled(true);
            creeper.setTarget(null);
        }
    }
}
