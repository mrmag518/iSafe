package com.mrmag518.iSafe.Events.BlockEvents;

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
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.block.BlockIgniteEvent.IgniteCause;


public class BlockListener implements Listener {
    public static iSafe plugin;
    public BlockListener(iSafe instance)
    {
        plugin = instance;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    
    @EventHandler
    public void blockGrowManager(BlockGrowEvent event) {
        if (event.isCancelled()){
            return;
        }
        
        if(plugin.getConfig().getBoolean("Miscellaneous.DisableBlockGrow") == true) {
            event.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.isCancelled()){
            return;
        }
        Block b = event.getBlock();
        Player p = event.getPlayer();
        Location loc = p.getLocation();
        byte level = p.getLocation().getBlock().getLightLevel();
        
        int detectionLvl = plugin.getConfig().getInt("AntiCheat/Security.LightLevel.MinimumLevelBeforeDetection");
        boolean checkGMC = plugin.getConfig().getBoolean("AntiCheat/Security.LightLevel.CheckCreativeMode");
        boolean checkNight = plugin.getConfig().getBoolean("AntiCheat/Security.LightLevel.CheckAtNight");
        
        if(plugin.getConfig().getBoolean("AntiCheat/Sucurity.ForceLightLevel(Fullbright)", true)) {
            if(p.getWorld().getTime() > 17999 && checkNight == false) {
                return;
            }
            if(p.getGameMode().equals(GameMode.CREATIVE) && checkGMC == false) {
                return;
            }
            if(level <= detectionLvl && !b.isLiquid() && !loc.getBlock().isLiquid()) {
                plugin.checkingFullbrightPerms = true;
                if(!(plugin.hasPermission(p, "iSafe.bypass.fullbright"))) {
                    event.setCancelled(true);
                    p.sendMessage(ChatColor.YELLOW + "Place a torch! (light source)");
                }
            }
        }
    }
    
    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockIgnite(BlockIgniteEvent event) {
        if (event.isCancelled()){
            return;
        }
        IgniteCause cause = event.getCause();
        
        if(cause == IgniteCause.SPREAD) {
            if(plugin.getConfig().getBoolean("Fire.DisableFireSpread", true)) {
                event.setCancelled(true);
            }
        } else if (cause == IgniteCause.FLINT_AND_STEEL) {
            if(plugin.getConfig().getBoolean("Fire.PreventFlintAndSteelUsage", true)) {
                Player p = event.getPlayer();
                if(!(plugin.hasPermission(p, "iSafe.use.flintandsteel"))) {
                    event.setCancelled(true);
                }
            }
        } else if (cause == IgniteCause.LAVA) {
            if(plugin.getConfig().getBoolean("Fire.DisableLavaIgnition", true)) {
                event.setCancelled(true);
            }
        } else if (cause == IgniteCause.FIREBALL) {
            if(plugin.getConfig().getBoolean("Fire.DisableFireballIgnition", true)) {
                event.setCancelled(true);
            }
        } else if (cause == IgniteCause.LIGHTNING) {
            if(plugin.getConfig().getBoolean("Fire.DisableLightningIgnition", true)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockFromTo(BlockFromToEvent event) {
        if (event.isCancelled())
        {
            return;
        }
        Block block = event.getBlock();
        
        if(plugin.getConfig().getBoolean("Flow.DisableWaterFlow", true))
        {
            if (block.getType() == Material.WATER || block.getType() == Material.STATIONARY_WATER) 
            {
                event.setCancelled(true);
            }
        }
        if(plugin.getConfig().getBoolean("Flow.DisableLavaFlow", true))
        {
            if (block.getType() == Material.LAVA || block.getType() == Material.STATIONARY_LAVA) {
                
                event.setCancelled(true);
            }
        }
        if(plugin.getConfig().getBoolean("Flow.DisableAirFlow", true))
        {
            if (block.getType() == Material.AIR) 
            {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockPistonExtend(BlockPistonExtendEvent event) {
        if (event.isCancelled()){
            return;
        }
        
        if(plugin.getConfig().getBoolean("Pistons.DisablePistonExtend", true)){
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockPistonRetract(BlockPistonRetractEvent event) {
        if (event.isCancelled()){
            return;
        }
        
        if(plugin.getConfig().getBoolean("Pistons.DisablePistonRetract", true)){
            event.setCancelled(true);
        }
    }
    
    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockBurn(BlockBurnEvent event) {
        if (event.isCancelled()){
            return;
        }
        
        if(plugin.getConfig().getBoolean("Fire.PreventBlockBurn", true)){
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onLeavesDecay(LeavesDecayEvent event) {
        if (event.isCancelled()){
            return;
        }
        
        if(plugin.getConfig().getBoolean("Miscellaneous.DisableLeavesDecay", true)){
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockPhysics(BlockPhysicsEvent event) {
        if (event.isCancelled()){
            return;
        }
        Block b = event.getBlock();
        
        if(plugin.getConfig().getBoolean("BlockPhysics.DisableSandPhysics", true)){
            if (b.getType() == Material.SAND) {
                event.setCancelled(true);
            }
        }
        if(plugin.getConfig().getBoolean("BlockPhysics.DisableGravelPhysics", true)){
            if (b.getType() == Material.GRAVEL) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockFade(BlockFadeEvent event) {
        if (event.isCancelled()){
            return;
        }
        Block block = event.getBlock();
        
        if(plugin.getConfig().getBoolean("BlockFade.DisableIceMelting", true)){
            if (block.getTypeId() == 79) {
                event.setCancelled(true);
            }
        }
        if(plugin.getConfig().getBoolean("BlockFade.DisableSnowMelting", true)){
            if (block.getTypeId() == 80) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockCanBuild(BlockCanBuildEvent event) {
        if(plugin.getConfig().getBoolean("Miscellaneous.ForceBlocksToBeBuildable", true)){
            if (!event.isBuildable()) {
                event.setBuildable(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockSpread(BlockSpreadEvent event) {
        if (event.isCancelled()){
            return;
        }
        
        if(plugin.getConfig().getBoolean("Miscellaneous.DisableBlockSpreading", true)){
            event.setCancelled(true);
        }
        
        if(event.getSource().getType() == Material.FIRE) {
            if(plugin.getConfig().getBoolean("Fire.DisableFireSpread", true)) {
                event.setCancelled(true);
            }
        }
    }
}
