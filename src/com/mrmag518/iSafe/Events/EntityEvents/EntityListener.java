package com.mrmag518.iSafe.Events.EntityEvents;

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

import com.mrmag518.iSafe.Files.CreatureManager;
import com.mrmag518.iSafe.Util.Log;
import org.bukkit.entity.*;
import org.bukkit.event.entity.*;
import org.bukkit.*;
import org.bukkit.block.Block;
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
    public void managePortalCreation(EntityCreatePortalEvent event) {
        if (event.isCancelled()){
            return;
        }
        
        if(plugin.getConfig().getBoolean("World.EnablePortalCreationPerms")) {
            if(event.getEntity() instanceof Player) {
                Player p = (Player)event.getEntity();
                if(!(plugin.hasPermission(p, "iSafe.createportal"))) {
                    event.setCancelled(true);
                }
            }
        }
    }
    
    @EventHandler
    public void onExplosionPrime(ExplosionPrimeEvent event) {
        if (event.isCancelled()){
            return;
        }
        
        if(plugin.getConfig().getBoolean("Explosions.DisablePrimedExplosions", true))
        {
            if(plugin.getConfig().getBoolean("Explosions.DebugExplosions", true)){
                float radius = event.getRadius();
                boolean fire = event.getFire();
                Log.debug("A PrimedExposion was prevented. | Radius: " + radius + " | Fire? " + fire);
            }
            event.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        if (event.isCancelled()){
            return;
        }
        
        Entity ent = event.getEntity();
        int x = event.getLocation().getBlockX();
        int y = event.getLocation().getBlockY();
        int z =  event.getLocation().getBlockZ();
        String worldname = ent.getWorld().getName();
        

        if(plugin.getConfig().getBoolean("Explosions.DisableAllExplosions", true)) {
            if(plugin.getConfig().getBoolean("Explosions.DebugExplosions", true)){
                int blocks = event.blockList().size();
                Log.debug("An explosion was prevented at the location: X: "+ x + " Y: "+ y + " Z: "+ z + " | Yield: "+ event.getYield()
                + " | Amount of blocks: " + blocks + " | World: "+ worldname + " | Caused by: " + ent.getType().name().toLowerCase());
            }
            event.blockList().clear();
            return;
        }

        if(plugin.getConfig().getBoolean("Explosions.DisableCreeperExplosions", true)){
            if (ent instanceof Creeper) {
                if(plugin.getConfig().getBoolean("Explosions.DebugExplosions", true)){
                    int blocks = event.blockList().size();
                    Log.debug("An explosion was prevented at the location: X: "+ x + " Y: "+ y + " Z: "+ z + " | Yield: "+ event.getYield()
                    + " | Amount of blocks: " + blocks + " | World: "+ worldname + " | Caused by: " + ent.getType().name().toLowerCase());
                }
                event.blockList().clear();
                return;
            }
        }

        if(plugin.getConfig().getBoolean("Explosions.DisableEnderdragonBlockDamage", true)){
            if (ent instanceof EnderDragon) {
                if(plugin.getConfig().getBoolean("Explosions.DebugExplosions", true)){
                    int blocks = event.blockList().size();
                    Log.debug("An explosion was prevented at the location: X: "+ x + " Y: "+ y + " Z: "+ z + " | Yield: "+ event.getYield()
                    + " | Amount of blocks: " + blocks + " | World: "+ worldname + " | Caused by: " + ent.getType().name().toLowerCase());
                }
                event.blockList().clear();
                return;
            }
        }

        if(plugin.getConfig().getBoolean("Explosions.DisableTntExplosions", true)){
            if (ent instanceof TNTPrimed) {
                if(plugin.getConfig().getBoolean("Explosions.DebugExplosions", true)){
                    int blocks = event.blockList().size();
                    Log.debug("An explosion was prevented at the location: X: "+ x + " Y: "+ y + " Z: "+ z + " | Yield: "+ event.getYield()
                    + " | Amount of blocks: " + blocks + " | World: "+ worldname + " | Caused by: " + ent.getType().name().toLowerCase());
                }
                event.blockList().clear();
                return;
            }
        }

        if(plugin.getConfig().getBoolean("Explosions.DisableFireballExplosions", true)){
            if (ent instanceof Fireball) {
                if(plugin.getConfig().getBoolean("Explosions.DebugExplosions", true)){
                    int blocks = event.blockList().size();
                    Log.debug("An explosion was prevented at the location: X: "+ x + " Y: "+ y + " Z: "+ z + " | Yield: "+ event.getYield()
                    + " | Amount of blocks: " + blocks + " | World: "+ worldname + " | Caused by: " + ent.getType().name().toLowerCase());
                }
                event.blockList().clear();
                return;
            }  
        }

        if(plugin.getConfig().getBoolean("Explosions.DisableEnderCrystalExplosions", true)){
            if (ent instanceof EnderCrystal) {
                if(plugin.getConfig().getBoolean("Explosions.DebugExplosions", true)){
                    int blocks = event.blockList().size();
                    Log.debug("An explosion was prevented at the location: X: "+ x + " Y: "+ y + " Z: "+ z + " | Yield: "+ event.getYield()
                    + " | Amount of blocks: " + blocks + " | World: "+ worldname + " | Caused by: " + ent.getType().name().toLowerCase());
                }
                event.blockList().clear();
                return;
            }  
        }
    }
    
    @EventHandler
    public void expBottleManagement(ExpBottleEvent event) {
        if(plugin.getConfig().getBoolean("Miscellaneous.PreventExpBottleThrow", true)) {
            event.getEntity().remove();
            event.setExperience(0);
            event.setShowEffect(false);
        }
    }
    
    @EventHandler
    public void EndermenGriefing(EntityChangeBlockEvent event) {
        if (event.isCancelled()){
            return;
        }
        Entity entity = event.getEntity();

        if(CreatureManager.getCreatureManager().getBoolean("Creatures.Endermen.PreventEndermenGriefing", true))
        {
            if (entity instanceof Enderman) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        Entity entity = event.getEntity();
        World world = entity.getWorld();
        
        if(event.getCause() == DamageCause.VOID) {
            if(entity instanceof Player) {
                Player p = (Player)entity;
                if(event.isCancelled() || !event.isCancelled()) {
                    if(plugin.getConfig().getBoolean("VoidFall.TeleportPlayerToSpawn", true)) {
                        p.teleport(world.getSpawnLocation());
                    } else if(plugin.getConfig().getBoolean("VoidFall.TeleportPlayerBackAndFixHole", true)) {
                        int highestY = p.getWorld().getHighestBlockYAt(p.getLocation());
                        Location loc = new Location(p.getWorld(), p.getLocation().getX(), highestY+5, p.getLocation().getZ());
                        Block b = p.getWorld().getBlockAt(loc.getBlockX(), 0, loc.getBlockZ());
                        if(plugin.getConfig().getBoolean("VoidFall.FixHoleWithGlass", true)) {
                            b.setTypeId(20);
                        } else if(plugin.getConfig().getBoolean("VoidFall.FixHoleWithBedrock", true)) {
                            b.setTypeId(7);
                        } else {
                            plugin.getConfig().set("VoidFall.FixHoleWithGlass", true);
                            plugin.saveConfig();
                            b.setTypeId(20);
                        }
                        p.teleport(loc);
                    }
                }
            }
        }
        
        if(plugin.getConfig().getBoolean("Damage.DisableVillagerDamage", true)){
            if(entity instanceof Villager) 
            {
                event.setCancelled(true);
            }
        }
        
        if(plugin.getConfig().getBoolean("Damage.DisablePlayerDamage", true)){
            if(entity instanceof Player) 
            {
                event.setCancelled(true);
            }
        }
        
        if(plugin.getConfig().getBoolean("Damage.DisableExplosionDamage", true)){
            if(event.getCause() == DamageCause.BLOCK_EXPLOSION) {
                if (entity instanceof Player) {
                    event.setCancelled(true);
                }
            }
        }
        
        if(plugin.getConfig().getBoolean("Damage.DisableFireDamage", true)){
            if(event.getCause().equals(DamageCause.FIRE) || event.getCause().equals(DamageCause.FIRE_TICK)) {
                if (entity instanceof Player) {
                    if(plugin.getConfig().getBoolean("Damage.EnablePermissions", true)) {
                        Player player = (Player)entity;
                        if(plugin.hasPermission(player, "iSafe.canceldamage.fire")) {
                            event.setCancelled(true);
                        }
                    } else {
                        event.setCancelled(true);
                    }
                }
            }
        }
        if(CreatureManager.getCreatureManager().getBoolean("Creatures.Damage.DisableFireDamage", true)){
            if(event.getCause().equals(DamageCause.FIRE) || event.getCause().equals(DamageCause.FIRE_TICK)) {
                if (entity instanceof Creature || entity instanceof Animals) {
                    event.setCancelled(true);
                }
            }
        }
        
        if(plugin.getConfig().getBoolean("Damage.DisableContactDamage", true)){
            if(event.getCause().equals(DamageCause.CONTACT)) {
                if (entity instanceof Player) {
                    if(plugin.getConfig().getBoolean("Damage.EnablePermissions", true)) {
                        Player player = (Player)entity;
                        if(plugin.hasPermission(player, "iSafe.canceldamage.contact")) {
                            event.setCancelled(true);
                        }
                    } else {
                        event.setCancelled(true);
                    }
                }
            }
        }
        if(CreatureManager.getCreatureManager().getBoolean("Creatures.Damage.DisableContactDamage", true)){
            if(event.getCause().equals(DamageCause.CONTACT)) {
                if (entity instanceof Creature || entity instanceof Animals) {
                    event.setCancelled(true);
                }
            }
        }
        
        if(plugin.getConfig().getBoolean("Damage.DisableCustomDamage", true)){
            if(event.getCause().equals(DamageCause.CUSTOM)) {
                if (entity instanceof Player) {
                    if(plugin.getConfig().getBoolean("Damage.EnablePermissions", true)) {
                        Player player = (Player)entity;
                        if(plugin.hasPermission(player, "iSafe.canceldamage.custom")) {
                            event.setCancelled(true);
                        }
                    } else {
                        event.setCancelled(true);
                    }
                }
            }
        }
        if(CreatureManager.getCreatureManager().getBoolean("Creatures.Damage.DisableCustomDamage", true)){
            if(event.getCause().equals(DamageCause.CUSTOM)) {
                if (entity instanceof Creature || entity instanceof Animals) {
                    event.setCancelled(true);
                }
            }
        }
        
        if(plugin.getConfig().getBoolean("Damage.DisableDrowningDamage", true)){
            if(event.getCause().equals(DamageCause.DROWNING)) {
                if (entity instanceof Player) {
                    if(plugin.getConfig().getBoolean("Damage.EnablePermissions", true)) {
                        Player player = (Player)entity;
                        if(plugin.hasPermission(player, "iSafe.canceldamage.drowning")) {
                            event.setCancelled(true);
                        }
                    } else {
                        event.setCancelled(true);
                    }
                }
            }
        }
        if(CreatureManager.getCreatureManager().getBoolean("Creatures.Damage.DisableDrowningDamage", true)){
            if(event.getCause().equals(DamageCause.DROWNING)) {
                if (entity instanceof Creature || entity instanceof Animals) {
                    event.setCancelled(true);
                }
            }
        }
        
        if(plugin.getConfig().getBoolean("Damage.DisableEntityAttackDamage", true)){
            if(event.getCause().equals(DamageCause.ENTITY_ATTACK)) {
                if (entity instanceof Player) {
                    if(plugin.getConfig().getBoolean("Damage.EnablePermissions", true)) {
                        Player player = (Player)entity;
                        if(plugin.hasPermission(player, "iSafe.canceldamage.entityattack")) {
                            event.setCancelled(true);
                        }
                    } else {
                        event.setCancelled(true);
                    }
                }
            }
        }
        if(CreatureManager.getCreatureManager().getBoolean("Creatures.Damage.DisableEntityAttackDamage", true)){
            if(event.getCause().equals(DamageCause.ENTITY_ATTACK)) {
                if (entity instanceof Creature || entity instanceof Animals) {
                    event.setCancelled(true);
                }
            }
        }
        
        if(plugin.getConfig().getBoolean("Damage.DisableFallDamage", true)){
            if(event.getCause().equals(DamageCause.FALL)) {
                if (entity instanceof Player) {
                    if(plugin.getConfig().getBoolean("Damage.EnablePermissions", true)) {
                        Player player = (Player)entity;
                        if(plugin.hasPermission(player, "iSafe.canceldamage.fall")) {
                            event.setCancelled(true);
                        }
                    } else {
                        event.setCancelled(true);
                    }
                }
            }
        }
        if(CreatureManager.getCreatureManager().getBoolean("Creatures.Damage.DisableFallDamage", true)){
            if(event.getCause().equals(DamageCause.FALL)) {
                if (entity instanceof Creature || entity instanceof Animals) {
                    event.setCancelled(true);
                }
            }
        }
        
        if(plugin.getConfig().getBoolean("Damage.DisableLavaDamage", true)){
            if(event.getCause().equals(DamageCause.LAVA)) {
                if (entity instanceof Player) {
                    if(plugin.getConfig().getBoolean("Damage.EnablePermissions", true)) {
                        Player player = (Player)entity;
                        if(plugin.hasPermission(player, "iSafe.canceldamage.lava")) {
                            event.setCancelled(true);
                        }
                    } else {
                        event.setCancelled(true);
                    }
                }
            }
        }
        if(CreatureManager.getCreatureManager().getBoolean("Creatures.Damage.DisableLavaDamage", true)){
            if(event.getCause().equals(DamageCause.LAVA)) {
                if (entity instanceof Creature || entity instanceof Animals) {
                    event.setCancelled(true);
                }
            }
        }
        
        if(plugin.getConfig().getBoolean("Damage.DisableLavaDamage", true)){
            if(event.getCause().equals(DamageCause.LIGHTNING)) {
                if (entity instanceof Player) {
                    if(plugin.getConfig().getBoolean("Damage.EnablePermissions", true)) {
                        Player player = (Player)entity;
                        if(plugin.hasPermission(player, "iSafe.canceldamage.lightning")) {
                            event.setCancelled(true);
                        }
                    } else {
                        event.setCancelled(true);
                    }
                }
            }
        }
        if(CreatureManager.getCreatureManager().getBoolean("Creatures.Damage.DisableLightningDamage", true)){
            if(event.getCause().equals(DamageCause.LIGHTNING)) {
                if (entity instanceof Creature || entity instanceof Animals) {
                    event.setCancelled(true);
                }
            }
        }
        
        if(plugin.getConfig().getBoolean("Damage.DisableMagicDamage", true)){
            if(event.getCause().equals(DamageCause.MAGIC)) {
                if (entity instanceof Player) {
                    if(plugin.getConfig().getBoolean("Damage.EnablePermissions", true)) {
                        Player player = (Player)entity;
                        if(plugin.hasPermission(player, "iSafe.canceldamage.magic")) {
                            event.setCancelled(true);
                        }
                    } else {
                        event.setCancelled(true);
                    }
                }
            }
        }
        if(CreatureManager.getCreatureManager().getBoolean("Creatures.Damage.DisableMagicDamage", true)){
            if(event.getCause().equals(DamageCause.MAGIC)) {
                if (entity instanceof Creature || entity instanceof Animals) {
                    event.setCancelled(true);
                }
            }
        }
        
        if(plugin.getConfig().getBoolean("Damage.DisablePoisonDamage", true)){
            if(event.getCause().equals(DamageCause.POISON)) {
                if (entity instanceof Player) {
                    if(plugin.getConfig().getBoolean("Damage.EnablePermissions", true)) {
                        Player player = (Player)entity;
                        if(plugin.hasPermission(player, "iSafe.canceldamage.poison")) {
                            event.setCancelled(true);
                        }
                    } else {
                        event.setCancelled(true);
                    }
                }
            }
        }
        if(CreatureManager.getCreatureManager().getBoolean("Creatures.Damage.DisablePoisonDamage", true)){
            if(event.getCause().equals(DamageCause.POISON)) {
                if (entity instanceof Creature || entity instanceof Animals) {
                    event.setCancelled(true);
                }
            }
        }
        
        if(plugin.getConfig().getBoolean("Damage.DisableProjectileDamage", true)){
            if(event.getCause().equals(DamageCause.PROJECTILE)) {
                if (entity instanceof Player) {
                    if(plugin.getConfig().getBoolean("Damage.EnablePermissions", true)) {
                        Player player = (Player)entity;
                        if(plugin.hasPermission(player, "iSafe.canceldamage.projectile")) {
                            event.setCancelled(true);
                        }
                    } else {
                        event.setCancelled(true);
                    }
                }
            }
        }
        if(CreatureManager.getCreatureManager().getBoolean("Creatures.Damage.DisableProjectileDamage", true)){
            if(event.getCause().equals(DamageCause.PROJECTILE)) {
                if (entity instanceof Creature || entity instanceof Animals) {
                    event.setCancelled(true);
                }
            }
        }
        
        if(plugin.getConfig().getBoolean("Damage.DisableStarvationDamage", true)){
            if(event.getCause().equals(DamageCause.STARVATION)) {
                if (entity instanceof Player) {
                    if(plugin.getConfig().getBoolean("Damage.EnablePermissions", true)) {
                        Player player = (Player)entity;
                        if(plugin.hasPermission(player, "iSafe.canceldamage.starvation")) {
                            event.setCancelled(true);
                        }
                    } else {
                        event.setCancelled(true);
                    }
                }
            }
        }
        if(CreatureManager.getCreatureManager().getBoolean("Creatures.Damage.DisableStarvationDamage", true)){
            if(event.getCause().equals(DamageCause.STARVATION)) {
                if (entity instanceof Creature || entity instanceof Animals) {
                    event.setCancelled(true);
                }
            }
        }
        
        if(plugin.getConfig().getBoolean("Damage.DisableSuffocationDamage", true)){
            if(event.getCause().equals(DamageCause.SUFFOCATION)) {
                if (entity instanceof Player) {
                    if(plugin.getConfig().getBoolean("Damage.EnablePermissions", true)) {
                        Player player = (Player)entity;
                        if(plugin.hasPermission(player, "iSafe.canceldamage.suffocation")) {
                            event.setCancelled(true);
                        }
                    } else {
                        event.setCancelled(true);
                    }
                }
            }
        }
        if(CreatureManager.getCreatureManager().getBoolean("Creatures.Damage.DisableSuffocationDamage", true)){
            if(event.getCause().equals(DamageCause.SUFFOCATION)) {
                if (entity instanceof Creature || entity instanceof Animals) {
                    event.setCancelled(true);
                }
            }
        }
        
        if(plugin.getConfig().getBoolean("Damage.DisableSuicideDamage", true)){
            if(event.getCause().equals(DamageCause.SUICIDE)) {
                if (entity instanceof Player) {
                    if(plugin.getConfig().getBoolean("Damage.EnablePermissions", true)) {
                        Player player = (Player)entity;
                        if(plugin.hasPermission(player, "iSafe.canceldamage.suicide")) {
                            event.setCancelled(true);
                        }
                    } else {
                        event.setCancelled(true);
                    }
                }
            }
        }
        if(CreatureManager.getCreatureManager().getBoolean("Creatures.Damage.DisableSuicideDamage", true)){
            if(event.getCause().equals(DamageCause.SUICIDE)) {
                if (entity instanceof Creature || entity instanceof Animals) {
                    event.setCancelled(true);
                }
            }
        }
        
        if(plugin.getConfig().getBoolean("Damage.DisableVoidDamage", true)){
            if(event.getCause().equals(DamageCause.VOID)) {
                if (entity instanceof Player) {
                    if(plugin.getConfig().getBoolean("Damage.EnablePermissions", true)) {
                        Player player = (Player)entity;
                        if(plugin.hasPermission(player, "iSafe.canceldamage.void")) {
                            event.setCancelled(true);
                        }
                    } else {
                        event.setCancelled(true);
                    }
                }
            }
        }
        if(CreatureManager.getCreatureManager().getBoolean("Creatures.Damage.DisableVoidDamage", true)){
            if(event.getCause().equals(DamageCause.VOID)) {
                if (entity instanceof Creature || entity instanceof Animals) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        if (event.isCancelled()){
            return;
        }
        
        if(plugin.getConfig().getBoolean("Miscellaneous.DisableHunger", true)){
            if(event.getEntity() instanceof Player) {
                Player p = (Player)event.getEntity();
                if(plugin.hasPermission(p, "iSafe.bypass.hunger")) {
                    event.setCancelled(true);
                    event.setFoodLevel(20);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onEntityDeath(EntityDeathEvent event) {
        LivingEntity entity = event.getEntity();
        
        if(plugin.getConfig().getBoolean("World.DisableExpDrop", true)){
            event.setDroppedExp(0);
        }
        
        if(CreatureManager.getCreatureManager().getBoolean("Creatures.Death.DisableDrops", true)){
            if(!(entity instanceof Player)) {
                event.getDrops().clear();
            }
        }
    }

    @EventHandler
    public void onSlimeSplit(SlimeSplitEvent event) {
        if (event.isCancelled()){
            return;
        }
        
        if(CreatureManager.getCreatureManager().getBoolean("Creatures.Slime.DisableSlimeSplit", true)){
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityTarget(EntityTargetEvent event) {
        if (event.isCancelled()){
            return;
        }
        
        if(CreatureManager.getCreatureManager().getBoolean("Creatures.CreatureTarget.Disable-closest_player-target", true)){
            if(event.getReason() == TargetReason.CLOSEST_PLAYER) {
                if(event.getEntity() instanceof Player) {
                    Player target = (Player)event.getTarget();
                    if(plugin.hasPermission(target, "iSafe.canceltarget.closestplayer")) {
                        event.setCancelled(true);
                    }
                }
            }
        }
        
        if(CreatureManager.getCreatureManager().getBoolean("Creatures.CreatureTarget.Disable-custom-target", true)){
            if(event.getReason() == TargetReason.CUSTOM) 
            {
                if(event.getEntity() instanceof Player) {
                    Player target = (Player)event.getTarget();
                    if(plugin.hasPermission(target, "iSafe.canceltarget.custom")) {
                        event.setCancelled(true);
                    }
                }
            }
        }
        
        if(CreatureManager.getCreatureManager().getBoolean("Creatures.CreatureTarget.Disable-forgot_target-target", true)){
            if(event.getReason() == TargetReason.FORGOT_TARGET) 
            {
                if(event.getEntity() instanceof Player) {
                    Player target = (Player)event.getTarget();
                    if(plugin.hasPermission(target, "iSafe.canceltarget.forgot")) {
                        event.setCancelled(true);
                    }
                }
            }
        }
        
        if(CreatureManager.getCreatureManager().getBoolean("Creatures.CreatureTarget.Disable-owner_attacked_target-target", true)){
            if(event.getReason() == TargetReason.OWNER_ATTACKED_TARGET) {
                if(event.getEntity() instanceof Player) {
                    Player target = (Player)event.getTarget();
                    if(plugin.hasPermission(target, "iSafe.canceltarget.ownerattacked")) {
                        event.setCancelled(true);
                    }
                }
            }
        }
        
        if(CreatureManager.getCreatureManager().getBoolean("Creatures.CreatureTarget.Disable-pig_zombie_target-target", true)){
            if(event.getReason() == TargetReason.PIG_ZOMBIE_TARGET) {
                if(event.getEntity() instanceof Player) {
                    Player target = (Player)event.getTarget();
                    if(plugin.hasPermission(target, "iSafe.canceltarget.pigzombie")) {
                        event.setCancelled(true);
                    }
                }
            }
        }
        
        if(CreatureManager.getCreatureManager().getBoolean("Creatures.CreatureTarget.Disable-random_target-target", true)){
            if(event.getReason() == TargetReason.RANDOM_TARGET) {
                if(event.getEntity() instanceof Player) {
                    Player target = (Player)event.getTarget();
                    if(plugin.hasPermission(target, "iSafe.canceltarget.random")) {
                        event.setCancelled(true);
                    }
                }
            }
        }
        
        if(CreatureManager.getCreatureManager().getBoolean("Creatures.CreatureTarget.Disable-target_attacked_entity-target", true)){
            if(event.getReason() == TargetReason.TARGET_ATTACKED_ENTITY) {
                if(event.getEntity() instanceof Player) {
                    Player target = (Player)event.getTarget();
                    if(plugin.hasPermission(target, "iSafe.canceltarget.targetattackedentity")) {
                        event.setCancelled(true);
                    }
                }
            }
        }
        
        if(CreatureManager.getCreatureManager().getBoolean("Creatures.CreatureTarget.Disable-target_attacked_owner-target", true)){
            if(event.getReason() == TargetReason.TARGET_ATTACKED_OWNER) {
                if(event.getEntity() instanceof Player) {
                    Player target = (Player)event.getTarget();
                    if(plugin.hasPermission(target, "iSafe.canceltarget.targetattackedowner")) {
                        event.setCancelled(true);
                    }
                }
            }
        }
        
        if(CreatureManager.getCreatureManager().getBoolean("Creatures.CreatureTarget.Disable-target_died-target", true)){
            if(event.getReason() == TargetReason.TARGET_DIED) {
                if(event.getEntity() instanceof Player) {
                    Player target = (Player)event.getTarget();
                    if(plugin.hasPermission(target, "iSafe.canceltarget.targetdied")) {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }
    
    @EventHandler(priority = EventPriority.NORMAL)
    public void manageCropTrampling(EntityInteractEvent event) {
        if (event.isCancelled()){
            return;
        }
        Entity entity = event.getEntity();
        
        if(CreatureManager.getCreatureManager().getBoolean("Creatures.DisableCropTrampling", true)){
            if(entity instanceof LivingEntity) {
                if(entity instanceof Creature && !(entity instanceof Player)) {
                    event.setCancelled(true);
                }
            }
        }
        
        if(plugin.getConfig().getBoolean("Movement.PreventCropTrampling", true)){
            if(entity instanceof LivingEntity) {
                if(entity instanceof Player && !(entity instanceof Creature)) {
                    Player p = (Player)entity;
                    if(!(plugin.hasPermission(p, "iSafe.bypass.croptrampling"))) {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPigZap(PigZapEvent event) {
        if (event.isCancelled()){
            return;
        }
        
        if(CreatureManager.getCreatureManager().getBoolean("Creatures.Pig.DisabletPigZap", true)){
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityTame(EntityTameEvent event) {
        if (event.isCancelled()){
            return;
        }
        
        if(CreatureManager.getCreatureManager().getBoolean("Creatures.Tame.DisableTaming", true)){
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onItemSpawn(ItemSpawnEvent event) {
        if (event.isCancelled()){
            return;
        }
        
        if(plugin.getConfig().getBoolean("World.DisableItemSpawn", true)){
            event.setCancelled(true);
            event.getEntity().remove();
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onCreeperPower(CreeperPowerEvent event) {
        if (event.isCancelled()){
            return;
        }
        
        if(CreatureManager.getCreatureManager().getBoolean("Creatures.PoweredCreepers.DisableLightningCause", true)){
            if (event.getCause() == PowerCause.LIGHTNING) {
                event.setCancelled(true);
            }
        }
        if(CreatureManager.getCreatureManager().getBoolean("Creatures.PoweredCreepers.DisableSetOffCause", true)){
            if (event.getCause() == PowerCause.SET_OFF) {
                event.setCancelled(true);
            }
        }
        if(CreatureManager.getCreatureManager().getBoolean("Creatures.PoweredCreepers.DisableSetOnCause", true)){
            if (event.getCause() == PowerCause.SET_ON) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityCombust(EntityCombustEvent event) {
        if (event.isCancelled()){
            return;
        }
        Entity entity = event.getEntity();
        
        if(CreatureManager.getCreatureManager().getBoolean("Creatures.Combusting.DisableFor-allCreatures", true)){
            event.setCancelled(true);
        }
        
        if(CreatureManager.getCreatureManager().getBoolean("Creatures.Combusting.DisableFor.Giant", true)) {
            if(entity instanceof Giant) {
                event.setCancelled(true);
            }
        }
        
        if(CreatureManager.getCreatureManager().getBoolean("Creatures.Combusting.DisableFor.PigZombie", true)) {
            if(entity instanceof PigZombie) {
                event.setCancelled(true);
            }
        }
        
        if(CreatureManager.getCreatureManager().getBoolean("Creatures.Combusting.DisableFor.Skeleton", true)) {
            if(entity instanceof Skeleton) {
                event.setCancelled(true);
            }
        }
        
        if(CreatureManager.getCreatureManager().getBoolean("Creatures.Combusting.DisableFor.Zombie", true)) {
            if(entity instanceof Zombie) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityRegainHealth(EntityRegainHealthEvent event) {
        if (event.isCancelled())
        {
            return;
        }
        
        if(plugin.getConfig().getBoolean("HealthRegen.DisableHealthRegeneration", true))
        {
            event.setCancelled(true);
        }
        if(plugin.getConfig().getBoolean("HealthRegen.DisableCustomHealthRegen", true))
        {
            if (event.getRegainReason() == RegainReason.CUSTOM) 
            {
                event.setCancelled(true);
            }
        }
        if(plugin.getConfig().getBoolean("HealthRegen.DisableEatingHealthRegen", true))
        {
            if (event.getRegainReason() == RegainReason.EATING) 
            {
                event.setCancelled(true);
            }
        }
        if(plugin.getConfig().getBoolean("HealthRegen.DisableNaturalHealthRegen", true))
        {
            if (event.getRegainReason() == RegainReason.REGEN) 
            {
                event.setCancelled(true);
            }
        }
        if(plugin.getConfig().getBoolean("HealthRegen.DisableSatiatedHealthRegen", true))
        {
            if (event.getRegainReason() == RegainReason.SATIATED) 
            {
                event.setCancelled(true);
            }
        }
        if(plugin.getConfig().getBoolean("HealthRegen.DisableMagicHealthRegen", true))
        {
            if (event.getRegainReason() == RegainReason.MAGIC || event.getRegainReason() == RegainReason.MAGIC_REGEN) 
            {
                event.setCancelled(true);
            }
        }
    }
    
    @EventHandler
    public void SheepDyeWool(SheepDyeWoolEvent event) {
        if (event.isCancelled()) {
            return;
        }
        
        if (CreatureManager.getCreatureManager().getBoolean("Creatures.SheepDyeWool.TotallyDisable", true)){
            event.setCancelled(true);
        }    
        
        if (CreatureManager.getCreatureManager().getBoolean("Creatures.SheepDyeWool.DisableColor.Black", true)){
            if (event.getColor() == DyeColor.BLACK) {
                event.setCancelled(true);
            }
        }
        
        if (CreatureManager.getCreatureManager().getBoolean("Creatures.SheepDyeWool.DisableColor.Blue", true)){
            if (event.getColor() == DyeColor.BLUE) {
                event.setCancelled(true);
            }
        }
        
        if (CreatureManager.getCreatureManager().getBoolean("Creatures.SheepDyeWool.DisableColor.Brown", true)){
            if (event.getColor() == DyeColor.BROWN) {
                event.setCancelled(true);
            }
        }
        
        if (CreatureManager.getCreatureManager().getBoolean("Creatures.SheepDyeWool.DisableColor.Cyan", true)){
            if (event.getColor() == DyeColor.CYAN) {
                event.setCancelled(true);
            }
        }
        
        if (CreatureManager.getCreatureManager().getBoolean("Creatures.SheepDyeWool.DisableColor.Gray", true)){
            if (event.getColor() == DyeColor.GRAY) {
                event.setCancelled(true);
            }
        }
        
        if (CreatureManager.getCreatureManager().getBoolean("Creatures.SheepDyeWool.DisableColor.Green", true)){
            if (event.getColor() == DyeColor.GREEN) {
                event.setCancelled(true);
            }
        }
        
        if (CreatureManager.getCreatureManager().getBoolean("Creatures.SheepDyeWool.DisableColor.Light_Blue", true)){
            if (event.getColor() == DyeColor.LIGHT_BLUE) {
                event.setCancelled(true);
            }
        }
        
        if (CreatureManager.getCreatureManager().getBoolean("Creatures.SheepDyeWool.DisableColor.Lime", true)){
            if (event.getColor() == DyeColor.LIME) {
                event.setCancelled(true);
            }
        }
        
        if (CreatureManager.getCreatureManager().getBoolean("Creatures.SheepDyeWool.DisableColor.Magenta", true)){
            if (event.getColor() == DyeColor.MAGENTA) {
                event.setCancelled(true);
            }
        }
        
        if (CreatureManager.getCreatureManager().getBoolean("Creatures.SheepDyeWool.DisableColor.Orange", true)){
            if (event.getColor() == DyeColor.ORANGE) {
                event.setCancelled(true);
            }
        }
        
        if (CreatureManager.getCreatureManager().getBoolean("Creatures.SheepDyeWool.DisableColor.Pink", true)){
            if (event.getColor() == DyeColor.PINK) {
                event.setCancelled(true);
            }
        }
        
        if (CreatureManager.getCreatureManager().getBoolean("Creatures.SheepDyeWool.DisableColor.Purple", true)){
            if (event.getColor() == DyeColor.PURPLE) {
                event.setCancelled(true);
            }
        }
        
        if (CreatureManager.getCreatureManager().getBoolean("Creatures.SheepDyeWool.DisableColor.Red", true)){
            if (event.getColor() == DyeColor.RED) {
                event.setCancelled(true);
            }
        }
        
        if (CreatureManager.getCreatureManager().getBoolean("Creatures.SheepDyeWool.DisableColor.Silver", true)){
            if (event.getColor() == DyeColor.SILVER) {
                event.setCancelled(true);
            }
        }
        
        if (CreatureManager.getCreatureManager().getBoolean("Creatures.SheepDyeWool.DisableColor.White", true)){
            if (event.getColor() == DyeColor.WHITE) {
                event.setCancelled(true);
            }
        }
        
        if (CreatureManager.getCreatureManager().getBoolean("Creatures.SheepDyeWool.DisableColor.Yellow", true)){
            if (event.getColor() == DyeColor.YELLOW) {
                event.setCancelled(true);
            }
        }
    }
    
    @EventHandler
    public void EntityBreakDoor(EntityBreakDoorEvent event) {
        if(event.isCancelled()) {
            return;
        }
        
        if(CreatureManager.getCreatureManager().getBoolean("Creatures.Zombie.DisableDoorBreak", true)) {
            event.setCancelled(true);
        }
    }
}
