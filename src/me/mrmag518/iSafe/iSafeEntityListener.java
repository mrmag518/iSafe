package me.mrmag518.iSafe;

import org.bukkit.entity.*;
import org.bukkit.event.entity.*;
import org.bukkit.*;
import org.bukkit.event.entity.CreeperPowerEvent.PowerCause;
import org.bukkit.event.entity.EntityDamageEvent.*;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
import org.bukkit.event.entity.EntityTargetEvent.*;


public class iSafeEntityListener extends EntityListener {
    public static iSafe plugin;
    public iSafeEntityListener(iSafe instance)
    {
        plugin = instance;
    }
    
    @Override
    public void onExplosionPrime(ExplosionPrimeEvent event) {
        if (event.isCancelled())
        {
            return;
        }
        
        if(plugin.config.getBoolean("Explosions.Prevent-primed-explosions", true))
        {    
        event.getEntity().remove();
        event.setCancelled(true);
    }
 }
    
    @Override
        public void onEntityExplode(EntityExplodeEvent event) {
        if (event.isCancelled())
        {
            return;
        }
        
        Location loc = event.getLocation();
        Entity ent = event.getEntity();
        World world = loc.getWorld();
        
        //Prevent all explosions
        if(plugin.config.getBoolean("Explosions.Prevent-explosions", true))
        {    
            event.setCancelled(true);
            ent.remove();
        }
        //Prevent Creeper explosions
        if(plugin.config.getBoolean("Explosions.Prevent-Creeper-explosions", true))
        {
            if (ent instanceof Creeper) {
                event.setCancelled(true);
            }
        }
        //Prevent EnderDragon block damage
        if(plugin.config.getBoolean("Explosions.Prevent-EnderDragon-blockdamage", true))
        {
            } else if (ent instanceof EnderDragon) {
                event.setCancelled(true);
        }
        if(plugin.config.getBoolean("Explosions.Prevent-TNT-explosions", true))
        {
            } else if (ent instanceof TNTPrimed) {
                event.setCancelled(true);
                ent.remove();
        }
        if(plugin.config.getBoolean("Explosions.Prevent-Fireball-explosions", true))
        {
            } else if (ent instanceof Fireball) {
                event.setCancelled(true);
                ent.remove();
        }
    }

    @Override
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        if (event.isCancelled())
        {
            return;
        }
        
        if(!plugin.config.getBoolean("Mobs.Spawn.Allow-Slime-spawn", true))
        {
            if (event.getCreatureType() == CreatureType.SLIME) {
                event.setCancelled(true);
                event.getEntity().remove();
            }
        }
        if(!plugin.config.getBoolean("Mobs.Spawn.Spawn.Allow-Ghast-spawn", true))
        {
            if (event.getCreatureType() == CreatureType.GHAST) {
                event.setCancelled(true);
                event.getEntity().remove();
            }
        }
        if(!plugin.config.getBoolean("Mobs.Spawn.Allow-Zombie-spawn", true))
        {
            if (event.getCreatureType() == CreatureType.ZOMBIE) {
                event.setCancelled(true);
                event.getEntity().remove();
            }
        }
        if(!plugin.config.getBoolean("Mobs.Spawn.Allow-Creeper-spawn", true))
        {
            if (event.getCreatureType() == CreatureType.CREEPER) {
                event.setCancelled(true);
                event.getEntity().remove();
            }
        }
        if(!plugin.config.getBoolean("Mobs.Spawn.Allow-Skeleton-spawn", true))
        {
            if (event.getCreatureType() == CreatureType.SKELETON) {
                event.setCancelled(true);
                event.getEntity().remove();
            }
        }
        if(!plugin.config.getBoolean("Mobs.Spawn.Allow-Enderman-spawn", true))
        {
            if (event.getCreatureType() == CreatureType.ENDERMAN) {
                event.setCancelled(true);
                event.getEntity().remove();
            }
        }
        if(!plugin.config.getBoolean("Mobs.Spawn.Allow-Silverfish-spawn", true))
        {
            if (event.getCreatureType() == CreatureType.SILVERFISH) {
                event.setCancelled(true);
                event.getEntity().remove();
            }
        }
        if(!plugin.config.getBoolean("Mobs.Spawn.Allow-PigZombie-spawn", true))
        {
            if (event.getCreatureType() == CreatureType.PIG_ZOMBIE) {
                event.setCancelled(true);
                event.getEntity().remove();
            }
        }
        if(!plugin.config.getBoolean("Mobs.Spawn.Allow-Spider-spawn", true))
        {
            if (event.getCreatureType() == CreatureType.SPIDER) {
                event.setCancelled(true);
                event.getEntity().remove();
            }
        }
        if(!plugin.config.getBoolean("Mobs.Spawn.Allow-Squid-spawn", true))
        {
            if (event.getCreatureType() == CreatureType.SQUID) {
                event.setCancelled(true);
                event.getEntity().remove();
            }
        }
        if(!plugin.config.getBoolean("Mobs.Spawn.Allow-Wolf-spawn", true))
        {
            if (event.getCreatureType() == CreatureType.WOLF) {
                event.setCancelled(true);
                event.getEntity().remove();
            }
        }
        if(!plugin.config.getBoolean("Mobs.Spawn.Allow-Pig-spawn", true))
        {
            if (event.getCreatureType() == CreatureType.PIG) {
                event.setCancelled(true);
                event.getEntity().remove();
            }
        }
        if(!plugin.config.getBoolean("Mobs.Spawn.Allow-Cow-spawn", true))
        {
            if (event.getCreatureType() == CreatureType.COW) {
                event.setCancelled(true);
                event.getEntity().remove();
            }
        }
        if(!plugin.config.getBoolean("Mobs.Spawn.Allow-EnderDragon-spawn", true))
        {
            if (event.getCreatureType() == CreatureType.ENDER_DRAGON) {
                event.setCancelled(true);
                event.getEntity().remove();
            }
        }
        if(!plugin.config.getBoolean("Mobs.Spawn.Allow-Sheep-spawn", true))
        {
            if (event.getCreatureType() == CreatureType.SHEEP) {
                event.setCancelled(true);
                event.getEntity().remove();
            }
        }
        if(!plugin.config.getBoolean("Mobs.Spawn..Allow-Blaze-spawn", true))
        {
            if (event.getCreatureType() == CreatureType.BLAZE) {
                event.setCancelled(true);
                event.getEntity().remove();
            }
        }
        if(!plugin.config.getBoolean("Mobs.Spawn.Allow-MagmaCube-spawn", true))
        {
            if (event.getCreatureType() == CreatureType.MAGMA_CUBE) {
                event.setCancelled(true);
                event.getEntity().remove();
            }
        }
        if(!plugin.config.getBoolean("Mobs.Spawn.Allow-CaveSpider-spawn", true))
        {
            if (event.getCreatureType() == CreatureType.CAVE_SPIDER) {
                event.setCancelled(true);
                event.getEntity().remove();
            }
        }
        if(!plugin.config.getBoolean("Mobs.Spawn.Allow-Chicken-spawn", true))
        {
            if (event.getCreatureType() == CreatureType.CHICKEN) {
                event.setCancelled(true);
                event.getEntity().remove();
            }
        }
        if(!plugin.config.getBoolean("Mobs.Spawn.Allow-Giant-spawn", true))
        {
            if (event.getCreatureType() == CreatureType.GIANT) {
                event.setCancelled(true);
                event.getEntity().remove();
            }
        }
        if(!plugin.config.getBoolean("Mobs.Spawn.Allow-Monster/human-spawn", true))
        {
            if (event.getCreatureType() == CreatureType.MONSTER) {
                event.setCancelled(true);
                event.getEntity().remove();
            }
        }
        if(!plugin.config.getBoolean("Mobs.Spawn.Allow-MuchroomCow-spawn", true))
        {
            if (event.getCreatureType() == CreatureType.MUSHROOM_COW) {
                event.setCancelled(true);
                event.getEntity().remove();
            }
        }
        if(!plugin.config.getBoolean("Mobs.Spawn.Allow-Snowman-spawn", true))
        {
            if (event.getCreatureType() == CreatureType.SNOWMAN) {
                event.setCancelled(true);
                event.getEntity().remove();
            }
        }
        if(!plugin.config.getBoolean("Mobs.Spawn.Allow-Squid-spawn", true))
        {
            if (event.getCreatureType() == CreatureType.SQUID) {
                event.setCancelled(true);
                event.getEntity().remove();
            }
        }
        if(!plugin.config.getBoolean("Mobs.Spawn.Allow-Villager-spawn", true))
        {
            if (event.getCreatureType() == CreatureType.VILLAGER) {
                event.setCancelled(true);
                event.getEntity().remove();
            }
        }
    }
    
    @Override
    public void onEndermanPickup(EndermanPickupEvent event) {
        if (event.isCancelled())
        {
            return;
        }
        
        if(plugin.config.getBoolean("Mobs.Enderman-grief.Prevent-Enderman-Pickup", true))
        {
            event.setCancelled(true);
        }
    }
    
    @Override
    public void onEndermanPlace(EndermanPlaceEvent event) {
        if (event.isCancelled())
        {
            return;
        }
        
        if(plugin.config.getBoolean("Mobs.Enderman-grief.Prevent-Enderman-Place", true))
        {
            event.setCancelled(true);
        }
    }

    @Override
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.isCancelled())
        {
            return;
        }
        
        if(plugin.config.getBoolean("Entity-Damage.Disable-Fire-player-damage", true))
        {
            if(event.getCause() == DamageCause.FIRE || event.getCause() == DamageCause.FIRE_TICK) {
                event.setCancelled(true);
            }
        }
        if(plugin.config.getBoolean("Entity-Damage.Disable-Lightning-damage", true))
        {
            if(event.getCause() == DamageCause.LIGHTNING) {
                event.setCancelled(true);
            }
        }
        if(plugin.config.getBoolean("Entity-Damage.Disable-Void-damage", true))
        {
            if(event.getCause() == DamageCause.VOID) {
                event.setCancelled(true);
            }
        }
        if(plugin.config.getBoolean("Entity-Damage.Disable-Fall-damage", true))
        {
            if(event.getCause() == DamageCause.FALL) {
                event.setCancelled(true);
            }
        }
        if(plugin.config.getBoolean("Entity-Damage.Disable-Suffocation-damage", true))
        {
            if(event.getCause() == DamageCause.SUFFOCATION) {
                event.setCancelled(true);
            }
        }
        if(plugin.config.getBoolean("Entity-Damage.Disable-Drowning-damage", true))
        {
            if(event.getCause() == DamageCause.DROWNING) {
                event.setCancelled(true);
            }
        }
        if(plugin.config.getBoolean("Entity-Damage.Disable-Lava-damage", true))
        {
            if(event.getCause() == DamageCause.LAVA) {
                event.setCancelled(true);
            }
        }
        if(plugin.config.getBoolean("Entity-Damage.Disable-Contact-damage", true))
        {
            if(event.getCause() == DamageCause.CONTACT) {
                event.setCancelled(true);
            }
        }
        if(plugin.config.getBoolean("Entity-Damage.Disable-Projectile-damage", true))
        {
            if(event.getCause() == DamageCause.PROJECTILE) {
                event.setCancelled(true);
            }
        }
        if(plugin.config.getBoolean("Entity-Damage.Disable-Starvation-damage", true))
        {
            if(event.getCause() == DamageCause.STARVATION) {
                event.setCancelled(true);
            }
        }
        if(plugin.config.getBoolean("Entity-Damage.Disable-Suicide-damage", true))
        {
            if(event.getCause() == DamageCause.SUICIDE) {
                event.setCancelled(true);
            }
        }
        if(plugin.config.getBoolean("Entity-Damage.Disable-Entity_Attack-damage", true))
        {
            if(event.getCause() == DamageCause.ENTITY_ATTACK) {
                event.setCancelled(true);
            }
        }
        if(plugin.config.getBoolean("Entity-Damage.Disable-Custom-damage", true))
        {
            if(event.getCause() == DamageCause.CUSTOM) {
                event.setCancelled(true);
            }
        }
    }

    @Override
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        if (event.isCancelled())
        {
            return;
        }
        
        if(plugin.config.getBoolean("Player.Disable-Hunger", true))
        {
            event.setCancelled(true);
            event.setFoodLevel(20);
        }
    }

    @Override
    public void onEntityDeath(EntityDeathEvent event) {
        Entity entity = event.getEntity();
        
        if(plugin.config.getBoolean("World.Disable-ExpirienceOrbs-drop", true))
        {
            event.setDroppedExp(0);
        }
        if(plugin.config.getBoolean("Mobs.Prevent-Object-drop-on-death", true))
        {
            event.getDrops().clear();
        }
    }

    @Override
    public void onSlimeSplit(SlimeSplitEvent event) {
        if (event.isCancelled())
        {
            return;
        }
        
        if(!plugin.config.getBoolean("Mobs.Allow-SlimeSplit", true))
        {
            event.setCancelled(true);
        }
    }

    @Override
    public void onEntityTarget(EntityTargetEvent event) {
        if (event.isCancelled())
        {
            return;
        }
        
        if(plugin.config.getBoolean("Mobs.EntityTarget.Disable-closest_player-target", true))
        {
            if(event.getReason() == TargetReason.CLOSEST_PLAYER) {
                event.setCancelled(true);
            }
        }
        if(plugin.config.getBoolean("Mobs.EntityTarget.Disable-custom-target", true))
        {
            if(event.getReason() == TargetReason.CUSTOM) {
                event.setCancelled(true);
            }
        }
        if(plugin.config.getBoolean("Mobs.EntityTarget.Disable-forgot_target-target", true))
        {
            if(event.getReason() == TargetReason.FORGOT_TARGET) {
                event.setCancelled(true);
            }
        }
        if(plugin.config.getBoolean("Mobs.EntityTarget.Disable-owner_attacked_target-target", true))
        {
            if(event.getReason() == TargetReason.OWNER_ATTACKED_TARGET) {
                event.setCancelled(true);
            }
        }
        if(plugin.config.getBoolean("Mobs.EntityTarget.Disable-pig_zombie_target-target", true))
        {
            if(event.getReason() == TargetReason.PIG_ZOMBIE_TARGET) {
                event.setCancelled(true);
            }
        }
        if(plugin.config.getBoolean("Mobs.EntityTarget.Disable-random_target-target", true))
        {
            if(event.getReason() == TargetReason.RANDOM_TARGET) {
                event.setCancelled(true);
            }
        }
        if(plugin.config.getBoolean("Mobs.EntityTarget.Disable-target_attacked_entity-target", true))
        {
            if(event.getReason() == TargetReason.TARGET_ATTACKED_ENTITY) {
                event.setCancelled(true);
            }
        }
        if(plugin.config.getBoolean("Mobs.EntityTarget.Disable-target_attacked_owner-target", true))
        {
            if(event.getReason() == TargetReason.TARGET_ATTACKED_OWNER) {
                event.setCancelled(true);
            }
        }
        if(plugin.config.getBoolean("Mobs.EntityTarget.Disable-target_died-target", true))
        {
            if(event.getReason() == TargetReason.TARGET_DIED) {
                event.setCancelled(true);
            }
        }
    }

    @Override
    public void onEntityInteract(EntityInteractEvent event) {
        if (event.isCancelled())
        {
            return;
        }
        
        if(plugin.config.getBoolean("Misc.Prevent-crop-trampling", true))
        {
            if (event.getBlock().getType() == Material.SOIL && event.getEntity() instanceof Creature)
                event.setCancelled(true);
        }
        
    }

    @Override
    public void onPigZap(PigZapEvent event) {
        if (event.isCancelled())
        {
            return;
        }
        
        Entity entity = event.getEntity();
        
        if(plugin.config.getBoolean("Mobs.Prevent-PigZap(Pig transformation to ZombiePig)", true))
        {
            event.setCancelled(true);
        }
    }

    @Override
    public void onEntityTame(EntityTameEvent event) {
        if (event.isCancelled())
        {
            return;
        }
        
        Entity entity = event.getEntity();
        
        if(plugin.config.getBoolean("Mobs.Tame.Prevent-taming", true))
        {
            event.setCancelled(true);
        }
    }

    @Override
    public void onItemSpawn(ItemSpawnEvent event) {
        if (event.isCancelled())
        {
            return;
        }
        
        Entity entity = event.getEntity();
        
        if(plugin.config.getBoolean("World.Prevent-items/objects-to-spawn-into-the-world", true))
        {
            event.setCancelled(true);
        }
    }

    @Override
    public void onCreeperPower(CreeperPowerEvent event) {
        if (event.isCancelled())
        {
            return;
        }
        
        Entity entity = event.getEntity();
        
        if(plugin.config.getBoolean("Mobs.Powered-Creepers.Prevent-PowerCause.Lightning", true))
        {
            if (event.getCause() == PowerCause.LIGHTNING) {
                event.setCancelled(true);
            }
        }
        if(plugin.config.getBoolean("Mobs.Powered-Creepers.Prevent-PowerCause.Set-Off", true))
        {
            if (event.getCause() == PowerCause.SET_OFF) {
                event.setCancelled(true);
            }
        }
        if(plugin.config.getBoolean("Mobs.Powered-Creepers.Prevent-PowerCause.Set-On", true))
        {
            if (event.getCause() == PowerCause.SET_ON) {
                event.setCancelled(true);
            }
        }
    }

    @Override
    public void onEntityCombust(EntityCombustEvent event) {
        if (event.isCancelled())
        {
            return;
        }
        
        Entity entity = event.getEntity();
        
        if(plugin.config.getBoolean("Mobs.Prevent-Entity-Combust", true))
        {
            event.setDuration(0);
            event.setCancelled(true);
        }
    }

    @Override
    public void onEntityRegainHealth(EntityRegainHealthEvent event) {
        if (event.isCancelled())
        {
            return;
        }
        
        Entity entity = event.getEntity();
        
        if(plugin.config.getBoolean("Player.Entity/Player.Completely-Prevent.Health-Regeneration", true))
        {
            event.setCancelled(true);
        }
        if(plugin.config.getBoolean("Player.Entity/Player.Prevent.Custom-Health-Regeneration", true))
        {
            if (event.getRegainReason() == RegainReason.CUSTOM) {
                event.setCancelled(true);
            }
        }
        if(plugin.config.getBoolean("Player.Entity/Player.Prevent.Eating-Health-Regeneration", true))
        {
            if (event.getRegainReason() == RegainReason.EATING) {
                event.setCancelled(true);
            }
        }
        if(plugin.config.getBoolean("Player.Entity/Player.Prevent.Regen-Health-Regeneration", true))
        {
            if (event.getRegainReason() == RegainReason.REGEN) {
                event.setCancelled(true);
            }
        }
        if(plugin.config.getBoolean("Player.Entity/Player.Prevent.Satiated-Health-Regeneration", true))
        {
            if (event.getRegainReason() == RegainReason.SATIATED) {
                event.setCancelled(true);
            }
        }
    }
}
