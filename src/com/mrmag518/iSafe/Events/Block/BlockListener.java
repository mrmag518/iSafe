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

package com.mrmag518.iSafe.Events.Block;

import com.mrmag518.iSafe.*;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockCanBuildEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockIgniteEvent.IgniteCause;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.inventory.ItemStack;


public class BlockListener implements Listener {
    public static iSafe plugin;
    public BlockListener(iSafe instance)
    {
        plugin = instance;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    
    @EventHandler
    public void blockGrowManager(BlockGrowEvent event) {
        if (event.isCancelled())
        {
            return;
        }
        if(plugin.getConfig().getBoolean("Misc.Prevent-BlockGrow", true)) {
            event.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.isCancelled())
        {
            return;
        }
        Block b = event.getBlock();
        Player p = event.getPlayer();
        Location loc = p.getLocation();
        byte level = p.getLocation().getBlock().getLightLevel();
        
        
        if(plugin.getConfig().getBoolean("Player.Prevent-fullbright-hacking(force lightlevel)", true)) {
            if(level < 1 && !b.isLiquid() && !loc.getBlock().isLiquid()) {
                event.setCancelled(true);
                p.sendMessage(ChatColor.YELLOW + "Place a torch!");
            }
        }
    }
    
    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockPlace(BlockPlaceEvent event) {
        if (event.isCancelled())
        {
            return;
        }
        Player player = event.getPlayer();
        
        if(plugin.getConfig().getBoolean("Player.Infinite-itemtacks", true))
        {
            ItemStack itst = player.getItemInHand();
            itst.setAmount(-1);
        }
    }
    
    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockIgnite(BlockIgniteEvent event) {
        if (event.isCancelled())
        {
            return;
        }
        IgniteCause cause = event.getCause();
        Player player = event.getPlayer();
        
        if(cause == IgniteCause.SPREAD) {
            if(plugin.getConfig().getBoolean("Fire.DisableFireSpread", true)) {
                event.setCancelled(true);
                return;
            }
        } else if (cause == IgniteCause.FLINT_AND_STEEL) {
            if(plugin.getConfig().getBoolean("Fire.PreventFlintAndSteelUsage", true)) {
                Player p = event.getPlayer();
                if(!p.hasPermission("iSafe.use.flintandsteel")) {
                    event.setCancelled(true);
                    Data.sendNoPermission(p);
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
        
        if(plugin.getConfig().getBoolean("Misc.Prevent-portal-creation", true))
        {
            if(event.getBlock().getTypeId() == 49 || event.getBlock().getRelative(BlockFace.DOWN).getTypeId() == 49) 
            {
                event.setCancelled(true);
                player.sendMessage(ChatColor.RED + "You do not have access to create a portal.");
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
        
        if(plugin.getConfig().getBoolean("Flow.Disable-water-flow", true))
        {
            if (block.getType() == Material.WATER || block.getType() == Material.STATIONARY_WATER) 
            {
                event.setCancelled(true);
            }
        }
        if(plugin.getConfig().getBoolean("Flow.Disable-lava-flow", true))
        {
            if (block.getType() == Material.LAVA || block.getType() == Material.STATIONARY_LAVA) {
                
                event.setCancelled(true);
            }
        }
        if(plugin.getConfig().getBoolean("Flow.Disable-air-flow", true))
        {
            if (block.getType() == Material.AIR) 
            {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockPistonExtend(BlockPistonExtendEvent event) {
        if (event.isCancelled())
        {
            return;
        }
        
        if(plugin.getConfig().getBoolean("Piston.Prevent-piston-Extend", true))
        {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockPistonRetract(BlockPistonRetractEvent event) {
        if (event.isCancelled())
        {
            return;
        }
        
        if(plugin.getConfig().getBoolean("Piston.Prevent-piston-Retract", true))
        {
            event.setCancelled(true);
        }
    }
    
    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockBurn(BlockBurnEvent event) {
        if (event.isCancelled())
        {
            return;
        }
        
        if(plugin.getConfig().getBoolean("Enviroment-Damage.Prevent-Fire-spread", true))
        {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockForm(BlockFormEvent event) {
        if (event.isCancelled())
        {
            return;
        }
        
        if(plugin.getConfig().getBoolean("Enviroment-Damage.Prevent-Fire-spread", true))
        {
            if(event.getBlock().getType() == Material.FIRE) 
            {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onLeavesDecay(LeavesDecayEvent event) {
        if (event.isCancelled())
        {
            return;
        }
        
        if(plugin.getConfig().getBoolean("Misc.Disable-LeavesDecay", true))
        {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockPhysics(BlockPhysicsEvent event) {
        if (event.isCancelled())
        {
            return;
        }
        Block block = event.getBlock();
        int ID = block.getTypeId();
        
        if(plugin.getConfig().getBoolean("Physics.Disable-sand-physics", true))
        {
            if (ID == 12) {
                event.setCancelled(true);
            }
        }
        if(plugin.getConfig().getBoolean("Physics.Disable-gravel-physics", true))
        {
            if (ID == 13) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockFade(BlockFadeEvent event) {
        if (event.isCancelled())
        {
            return;
        }
        Block block = event.getBlock();
        
        if(plugin.getConfig().getBoolean("Fade.Prevent-Ice-melting", true))
        {
            if (block.getTypeId() == 79) {
                event.setCancelled(true);
            }
        }
        if(plugin.getConfig().getBoolean("Fade.Prevent-Snow-melting", true))
        {
            if (block.getTypeId() == 80) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockDamage(BlockDamageEvent event) {
        if (event.isCancelled())
        {
            return;
        }
        
        if (plugin.superbreak.contains(event.getPlayer())) 
        {
            event.setInstaBreak(true);
        }
        
        if(plugin.getConfig().getBoolean("Player.Instantbreak", true))
        {
            event.setInstaBreak(true);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockDispense(BlockDispenseEvent event) {
        if (event.isCancelled())
        {
            return;
        }
        
        if(plugin.getConfig().getBoolean("World.Prevent-naturally-object-dispensing", true))
        {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockCanBuild(BlockCanBuildEvent event) {
        if(plugin.getConfig().getBoolean("World.Force-blocks-to-be-buildable", true))
        {
            if (!event.isBuildable()) 
            {
                event.setBuildable(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockSpread(BlockSpreadEvent event) {
        if (event.isCancelled())
        {
            return;
        }
        
        if(plugin.getConfig().getBoolean("World.Prevent-blocks-spreading", true))
        {
            event.setCancelled(true);
        }
    }
}
