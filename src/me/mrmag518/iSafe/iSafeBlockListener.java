package me.mrmag518.iSafe;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockIgniteEvent.IgniteCause;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.LeavesDecayEvent;


public class iSafeBlockListener extends BlockListener {
    public static iSafe plugin;
    public iSafeBlockListener(iSafe instance)
    {
        plugin = instance;
    }
    
    @Override
    public void onBlockPlace(BlockPlaceEvent event) {
        if (event.isCancelled())
        {
            return;
        }
        
        Player player = event.getPlayer();
        Block block = event.getBlock();
        Server server = player.getServer();
        
        if(!plugin.config.getBoolean("Placement.Allow-TNT-placement", true))
        {
            if(player.hasPermission("iSafe.place.tnt")) {
                //access
            } else {
                if (block.getTypeId() == 46) {
                    event.setCancelled(true);
                    player.sendMessage(ChatColor.RED + "You do not have access to place that.");
                }
            }
        }
        
        if(!plugin.config.getBoolean("Placement.Allow-MobSpawner-placement", true))
        {
            if(player.hasPermission("iSafe.place.mobspawner")) {
                //access
            } else {
                if (block.getTypeId() == 52) {
                    event.setCancelled(true);
                    player.sendMessage(ChatColor.RED + "You do not have access to place that.");
                }
            }
        }
        if(!plugin.config.getBoolean("Placement.Allow-Bedrock-placement", true))
        {
            if(player.hasPermission("iSafe.place.bedrock")) {
                //access
            } else {
                if (block.getTypeId() == 7) {
                    event.setCancelled(true);
                    player.sendMessage(ChatColor.RED + "You do not have access to place that.");
                }
            }
        }
        if(!plugin.config.getBoolean("Placement.Allow-FireBlock-placement", true))
        {
            if(player.hasPermission("iSafe.place.fireblock")) {
                //access
            } else {
                if (block.getTypeId() == 51) {
                    event.setCancelled(true);
                    player.sendMessage(ChatColor.RED + "You do not have access to place that.");
                }
            }
        }
        if(!plugin.config.getBoolean("Placement.Allow-Obsidian-placement", true))
        {
            if(player.hasPermission("iSafe.place.obsidian")) {
                //access
            } else {
                if (block.getTypeId() == 49) {
                    event.setCancelled(true);
                    player.sendMessage(ChatColor.RED + "You do not have access to place that.");
                }
            }
        }
        if(!plugin.config.getBoolean("Placement.Allow-Water-blocks", true))
        {
            if(player.hasPermission("iSafe.place.waterblock")) {
                //access
            } else {
                if (block.getTypeId() == 8 || block.getTypeId() == 9) {
                    event.setCancelled(true);
                    player.sendMessage(ChatColor.RED + "You do not have access to place that.");
                }
            }
        }
        if(!plugin.config.getBoolean("Placement.Allow-Lava-blocks", true))
        {
            if(player.hasPermission("iSafe.place.lavablock")) {
                //access
            } else {
                if (block.getTypeId() == 10 || block.getTypeId() == 11) {
                    event.setCancelled(true);
                    player.sendMessage(ChatColor.RED + "You do not have access to place that.");
                }
            }
        }
        if(!plugin.config.getBoolean("Placement.Allow-Pistons-placement", true))
        {
            if(player.hasPermission("iSafe.place.piston")) {
                //access
            } else {
                if (block.getTypeId() == 33 || block.getTypeId() == 34 || block.getTypeId() == 29) {
                    event.setCancelled(true);
                    player.sendMessage(ChatColor.RED + "You do not have access to place that.");
                }
            }
        }
        if(!plugin.config.getBoolean("Placement.Allow-Sponge-placement", true))
        {
            if(player.hasPermission("iSafe.place.sponge")) {
                //access
            } else {
                if (block.getTypeId() == 19) {
                    event.setCancelled(true);
                    player.sendMessage(ChatColor.RED + "You do not have access to place that.");
                }
            }
        }
        if(!plugin.config.getBoolean("Placement.Allow-Ice-placement", true))
        {
            if(player.hasPermission("iSafe.place.ice")) {
                //access
            } else {
                if (block.getTypeId() == 79) {
                    event.setCancelled(true);
                    player.sendMessage(ChatColor.RED + "You do not have access to place that.");
                }
            }
        }
        if(!plugin.config.getBoolean("Placement.Allow-un_natural_portal-placement", true))
        {
            if(player.hasPermission("iSafe.place.un-naturalportal")) {
                //access
            } else {
                if (block.getTypeId() == 90) {
                    event.setCancelled(true);
                    player.sendMessage(ChatColor.RED + "You do not have access to place that.");
                }
            }
        }
        if(!plugin.config.getBoolean("Placement.Allow-SoulSand-placement", true))
        {
            if(player.hasPermission("iSafe.place.soulsand")) {
                //access
            } else {
                if (block.getTypeId() == 88) {
                    event.setCancelled(true);
                    player.sendMessage(ChatColor.RED + "You do not have access to place that.");
                }
            }
        }
    }
    
    @Override
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.isCancelled())
        {
            return;
        }
        
        Player player = event.getPlayer();
        Block block = event.getBlock();
        Server server = player.getServer();
        
        if(!plugin.config.getBoolean("Breaking.Allow-TNT-breaking", true))
        {
            if(player.hasPermission("iSafe.break.tnt")) {
                //access
            } else {
                if (block.getTypeId() == 46) {
                    event.setCancelled(true);
                    player.sendMessage(ChatColor.RED + "You do not have access to break that.");
                }
            }
        }
        
        if(!plugin.config.getBoolean("Breaking.Allow-MobSpawner-breaking", true))
        {
            if(player.hasPermission("iSafe.break.mobspawner")) {
                //access
            } else {
                if (block.getTypeId() == 52) {
                    event.setCancelled(true);
                    player.sendMessage(ChatColor.RED + "You do not have access to break that.");
                }
            }
       }
        if(!plugin.config.getBoolean("Breaking.Allow-Obsidian-breaking", true))
        {
            if(player.hasPermission("iSafe.break.obsidian")) {
                //access
            } else {
                if (block.getTypeId() == 49) {
                    event.setCancelled(true);
                    player.sendMessage(ChatColor.RED + "You do not have access to break that.");
                }
            }
        }
        if(!plugin.config.getBoolean("Breaking.Allow-Pistons-breaking", true))
        {
            if(player.hasPermission("iSafe.break.piston")) {
                //access
            } else {
                if (block.getTypeId() == 33 || block.getTypeId() == 34 || block.getTypeId() == 29) {
                    event.setCancelled(true);
                    player.sendMessage(ChatColor.RED + "You do not have access to break that.");
                }
            }
        }
        if(!plugin.config.getBoolean("Breaking.Allow-Sponge-breaking", true))
        {
            if(player.hasPermission("iSafe.break.sponge")) {
                //access
            } else {
                if (block.getTypeId() == 19) {
                    event.setCancelled(true);
                    player.sendMessage(ChatColor.RED + "You do not have access to break that.");
                }
            }
        }
        if(!plugin.config.getBoolean("Breaking.Allow-Ice-breaking", true))
        {
            if(player.hasPermission("iSafe.break.ice")) {
                //access
            } else {
                if (block.getTypeId() == 79) {
                    event.setCancelled(true);
                    player.sendMessage(ChatColor.RED + "You do not have access to break that.");
                }
            }
        }
        if(!plugin.config.getBoolean("Breaking.Allow-SoulSand-breaking", true))
        {
            if(player.hasPermission("iSafe.break.soulsand")) {
                //access
            } else {
                if (block.getTypeId() == 88) {
                    event.setCancelled(true);
                    player.sendMessage(ChatColor.RED + "You do not have access to break that.");
                }
            }
        }
    }

    @Override
    public void onBlockIgnite(BlockIgniteEvent event) {
        if (event.isCancelled())
        {
            return;
        }
        
        IgniteCause cause = event.getCause();
        Player player = event.getPlayer();
        Block block = event.getBlock();
        
        boolean isFireSpread = cause == IgniteCause.SPREAD;
        
        if(!plugin.config.getBoolean("Enviroment-Damage.Allow-Fire-spread", true))
        {
            if(isFireSpread) {
                event.setCancelled(true);
                return;
            }
        }
        if(!plugin.config.getBoolean("Enviroment-Damage.Allow-Flint_and_steel-usage", true))
        {
            if(event.getCause() == IgniteCause.FLINT_AND_STEEL) {
                event.setCancelled(true);
                player.sendMessage(ChatColor.RED + "You cannot use a lighter.");
            }
        }
        if(!plugin.config.getBoolean("Enviroment-Damage.Allow-Enviroment-ignition", true))
        {
            if(event.getCause() == IgniteCause.LAVA || event.getCause() == IgniteCause.LIGHTNING) {
                event.setCancelled(true);
            }
        }
        if(plugin.config.getBoolean("Misc.Prevent-portal-creation", true))
        {
            if(event.getBlock().getTypeId() == 49 || event.getBlock().getRelative(BlockFace.DOWN).getTypeId() == 49) {
                event.setCancelled(true);
                player.sendMessage(ChatColor.RED + "You do not have access to create a portal.");
            }
        }
    }

    @Override
    public void onBlockFromTo(BlockFromToEvent event) {
        if (event.isCancelled())
        {
            return;
        }
        
        Block block = event.getBlock();
        
        if(plugin.config.getBoolean("Flow.Disable-water-flow", true))
        {
            if (block.getType() == Material.WATER || block.getType() == Material.STATIONARY_WATER) {
                event.setCancelled(true);
            }
        }
        if(plugin.config.getBoolean("Flow.Disable-lava-flow", true))
        {
            if (block.getType() == Material.LAVA || block.getType() == Material.STATIONARY_LAVA) {
                event.setCancelled(true);
            }
        }
        if(plugin.config.getBoolean("Flow.Disable-air-flow", true))
        {
            if (block.getType() == Material.AIR) {
                event.setCancelled(true);
            }
        }
    }

    @Override
    public void onBlockPistonExtend(BlockPistonExtendEvent event) {
        if (event.isCancelled())
        {
            return;
        }
        
        Block block = event.getBlock();
        
        if(plugin.config.getBoolean("Piston.Prevent-piston-Extend", true))
        {
            event.setCancelled(true);
        }
    }

    @Override
    public void onBlockPistonRetract(BlockPistonRetractEvent event) {
        if (event.isCancelled())
        {
            return;
        }
        
        Block block = event.getBlock();
        
        if(plugin.config.getBoolean("Piston.Prevent-piston-Retract", true))
        {
            event.setCancelled(true);
        }
    }
    
    @Override
    public void onBlockBurn(BlockBurnEvent event) {
        if (event.isCancelled())
        {
            return;
        }
        
        Block block = event.getBlock();
        
        if(!plugin.config.getBoolean("Enviroment-Damage.Allow-Fire-spread", true))
        {
            event.setCancelled(true);
        }
    }

    @Override
    public void onBlockForm(BlockFormEvent event) {
        if (event.isCancelled())
        {
            return;
        }
        
        Block block = event.getBlock();
        
        if(!plugin.config.getBoolean("Enviroment-Damage.Allow-Fire-spread", true))
        {
            if(event.getBlock().getType() == Material.FIRE) {
                event.setCancelled(true);
            }
        }
    }

    @Override
    public void onLeavesDecay(LeavesDecayEvent event) {
        if (event.isCancelled())
        {
            return;
        }
        
        Block block = event.getBlock();
        
        if(plugin.config.getBoolean("Misc.Disable-LeavesDecay", true))
        {
            event.setCancelled(true);
        }
    }

    @Override
    public void onBlockPhysics(BlockPhysicsEvent event) {
        if (event.isCancelled())
        {
            return;
        }
        
        Block block = event.getBlock();
        
        int ID = event.getChangedTypeId();
        
        if(plugin.config.getBoolean("Physics.Disable.sand-physics", true))
        {
            if (ID == 12) {
                event.setCancelled(true);
            }
        }
        if(plugin.config.getBoolean("Physics.Disable.gravel-physics", true))
        {
            if (ID == 13) {
                event.setCancelled(true);
            }
        }
    }

    @Override
    public void onBlockFade(BlockFadeEvent event) {
        if (event.isCancelled())
        {
            return;
        }
        
        Block block = event.getBlock();
        
        if(plugin.config.getBoolean("Fade.Prevent-Ice-melting", true))
        {
            if (block.getTypeId() == 79) {
            event.setCancelled(true);
            }
        }
        if(plugin.config.getBoolean("Fade.Prevent-Snow-melting", true))
        {
            if (block.getTypeId() == 80) {
            event.setCancelled(true);
            }
        }
    }

    @Override
    public void onBlockDamage(BlockDamageEvent event) {
        if (event.isCancelled())
        {
            return;
        }
        
        Block block = event.getBlock();
        
        if (plugin.superbreak.contains(event.getPlayer())) {
            event.setInstaBreak(true);
        }
    }
}
