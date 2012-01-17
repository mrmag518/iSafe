package me.mrmag518.iSafe;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockCanBuildEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockDispenseEvent;
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
import org.bukkit.inventory.ItemStack;


public class iSafeBlockListener extends BlockListener {
    public static iSafe plugin;
    public iSafeBlockListener(iSafe instance)
    {
        plugin = instance;
    }
    public int message = 0;
    
    
    @Override
    public void onBlockPlace(BlockPlaceEvent event) {
        if (event.isCancelled())
        {
            return;
        }
        
        Player player = event.getPlayer();
        Block block = event.getBlock();
        Server server = player.getServer();
        int blockID = event.getBlock().getTypeId();
        World world = player.getWorld();
        Location loc = player.getLocation();
        
        
        /**
         * Need improvements!
         */
        //Blacklist
        final List<Block> placedblocks = new ArrayList<Block>();
        if (plugin.config.getList("Place.Blacklist", placedblocks).contains(blockID))
        {
            if(player.hasPermission("iSafe.place.blacklist.bypass")) {
                //access
            } else {
                event.setCancelled(true);
                player.sendMessage(ChatColor.RED + "You cannot place: "+ ChatColor.GRAY + (block.getType().name().toLowerCase()));
            }
            if (plugin.config.getBoolean("Place.Alert/log.To-console", true))
            {
                plugin.log.info("[iSafe] "+ player.getName() + " tried to place: "+ block.getType().name().toLowerCase() + ", At the location: "+ " X: "+ loc.getBlockX() +" Y: "+ loc.getBlockY() +" Z: "+ loc.getBlockZ()+ ", In the world: "+ world.getName());
            }
            if (plugin.config.getBoolean("Place.Alert/log.To-player", true))
            {
                player.sendMessage(ChatColor.RED + "You cannot break: "+ ChatColor.GRAY + (block.getType().name().toLowerCase()));
            }
            if (plugin.config.getBoolean("Place.Alert/log.To-server-chat", true))
            {
                server.broadcastMessage(ChatColor.DARK_GRAY + player.getName() + " tried to break: "+ block.getType().name().toLowerCase());
            }
        }
        
        //Infinte itemstacks.
        if(plugin.config.getBoolean("Player.Infinite-itemtacks", true))
        {
            ItemStack itst = player.getItemInHand();
            itst.setAmount(65);
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
        int blockID = event.getBlock().getTypeId();
        World world = player.getWorld();
        Location loc = player.getLocation();
        
        /**
         * Need improvements!
         */
        //Blacklist
        final List<Block> brokenblocks = new ArrayList<Block>();
        if (plugin.config.getList("Break.Blacklist", brokenblocks).contains(blockID))
        {
            if(player.hasPermission("iSafe.break.blacklist.bypass")) {
                //access
            } else {
                event.setCancelled(true);
            }
            if (plugin.config.getBoolean("Break.Alert/log.To-console", true))
            {
                plugin.log.info("[iSafe] "+ player.getName() + " tried to break: "+ block.getType().name().toLowerCase() + ", At the location: "+ " X: "+ loc.getBlockX() +" Y: "+ loc.getBlockY() +" Z: "+ loc.getBlockZ()+ ", In the world: "+ world.getName());
            }
            if (plugin.config.getBoolean("Break.Alert/log.To-player", true))
            {
                player.sendMessage(ChatColor.RED + "You cannot break: "+ ChatColor.GRAY + (block.getType().name().toLowerCase()));
            }
            if (plugin.config.getBoolean("Break.Alert/log.To-server-chat", true))
            {
                server.broadcastMessage(ChatColor.DARK_GRAY + player.getName() + " tried to break: "+ block.getType().name().toLowerCase());
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
        
        if(plugin.config.getBoolean("Enviroment-Damage.Prevent-Fire-spread", true))
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
        
        if(plugin.config.getBoolean("Enviroment-Damage.Prevent-Fire-spread", true))
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
        
        if(plugin.config.getBoolean("Enviroment-Damage.Prevent-Fire-spread", true))
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
        
        int ID = block.getTypeId();
        
        if(plugin.config.getBoolean("Physics.Disable-sand-physics", true))
        {
            if (ID == 12) {
                event.setCancelled(true);
            }
        }
        if(plugin.config.getBoolean("Physics.Disable-gravel-physics", true))
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
        
        if(plugin.config.getBoolean("Player.Instantbreak", true))
        {
            event.setInstaBreak(true);
        }
    }

    @Override
    public void onBlockDispense(BlockDispenseEvent event) {
        if (event.isCancelled())
        {
            return;
        }
        
        Block block = event.getBlock();
        ItemStack item = event.getItem();
        
        if(plugin.config.getBoolean("World.Prevent-naturally-object-dispensing", true))
        {
            event.setCancelled(true);
        }
    }

    @Override
    public void onBlockCanBuild(BlockCanBuildEvent event) {
        Block block = event.getBlock();
        World world = block.getWorld();
        Location loc = block.getLocation();
        
        if(plugin.config.getBoolean("World.Force-blocks-to-be-buildable", true))
        {
            if (!event.isBuildable()) {
                event.setBuildable(true);
            }
        }
    }
}
