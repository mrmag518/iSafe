package me.mrmag518.iSafe.Blacklists;

import java.util.ArrayList;
import java.util.List;

import me.mrmag518.iSafe.iSafe;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BreakBlacklist implements Listener {
    public BreakBlacklist() {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    
    public static iSafe plugin;
    public BreakBlacklist(iSafe instance)
    {
        plugin = instance;
    }
    
    @EventHandler
    public void BreakBlacklist(BlockBreakEvent event) {
        if (event.isCancelled())
        {
            return;
        }
        
        Player player = event.getPlayer();
        Block block = event.getBlock();
        Server server = player.getServer();
        
        int blockID = event.getBlock().getTypeId();
        String BlockNAME_Lowercase = event.getBlock().getType().name().toLowerCase();
        String BlockNAME_Uppercase = event.getBlock().getType().name().toUpperCase();
        String BlockNAME_Name = event.getBlock().getType().name();
        String BlockNAME = event.getBlock().toString();
        
        World world = player.getWorld();
        Location loc = player.getLocation();
        String worldname = world.getName(); 
        
        //Blacklist
        final List<Block> brokenblocks = new ArrayList<Block>();
        if (plugin.getBlacklist().getList("Break.Blacklist", brokenblocks).contains(blockID)
                || plugin.getBlacklist().getList("Break.Blacklist", brokenblocks).contains(BlockNAME_Lowercase)
                || plugin.getBlacklist().getList("Break.Blacklist", brokenblocks).contains(BlockNAME_Uppercase)
                || plugin.getBlacklist().getList("Break.Blacklist", brokenblocks).contains(BlockNAME)
                || plugin.getBlacklist().getList("Break.Blacklist", brokenblocks).contains(BlockNAME_Name))
        {
            if(player.hasPermission("iSafe.break.blacklist.bypass")) {
                //access
            } else {
                if (!event.isCancelled()) 
                {
                    final List<String> Breakworlds = plugin.getBlacklist().getStringList("Break.Worlds");
                
                    if (plugin.getBlacklist().getList("Break.Worlds", Breakworlds).contains(worldname))
                    {
                        event.setCancelled(true);
                    } else {
                        event.setCancelled(false);
                    }
                }     
            }
            
            if (plugin.getBlacklist().getBoolean("Break.Kick-Player", true))
            {
                if (event.isCancelled())
                {
                    player.kickPlayer(ChatColor.RED + "You got kicked for attempting to break: "+ ChatColor.GRAY + block.getType().name().toLowerCase());
                }    
            }
            
            if (plugin.getBlacklist().getBoolean("Place.Kill-Player", true))
            {
                if (event.isCancelled())
                {
                    player.setHealth(0);
                    KillAlertPlayer(player, block, worldname);
                }
            }
            
            if (plugin.getBlacklist().getBoolean("Break.Alert/log.To-console", true))
            {
                if (event.isCancelled()) 
                {
                    AlertConsole(player, block, loc, worldname);
                }
            }
            
            if (plugin.getBlacklist().getBoolean("Break.Alert/log.To-player", true))
            {
                if (event.isCancelled()) 
                {
                    AlertPlayer(player, block);
                }
            }
            
            if (plugin.getBlacklist().getBoolean("Break.Alert/log.To-server-chat", true))
            {
                if (event.isCancelled()) 
                {
                    AlertServer(server, block, worldname, player);
                }
            }
        }
        
        if (plugin.getBlacklist().getBoolean("Break.Complete-Disallow-breaking", true))
        {
            if (player.hasPermission("iSafe.break")) {
                //access
            } else {
                event.setCancelled(true);
                player.sendMessage(ChatColor.RED + "You cannot break objects.");
            }
        }
    }
    
    public void KillAlertPlayer(Player player, Block block, String worldname) {
        player.sendMessage(ChatColor.RED + "You got killed for attempting to break: "+ ChatColor.GRAY + block.getType().name().toLowerCase());
    }
    
    public void AlertPlayer(Player player, Block block) {
        player.sendMessage(ChatColor.RED + "You cannot break: "+ ChatColor.GRAY + (block.getType().name().toLowerCase()));
    }
    
    public void AlertServer(Server server, Block block, String worldname, Player player) {
        server.broadcastMessage(ChatColor.DARK_GRAY + player.getName() + " tried to break: "+ ChatColor.RED + block.getType().name().toLowerCase() + ChatColor.DARK_GRAY + " In the world: "+ ChatColor.RED + worldname);
    }
    
    public void AlertConsole(Player player, Block block, Location loc, String worldname) {
        plugin.log.info("[iSafe] "+ player.getName() + " tried to break: "+ block.getType().name().toLowerCase() + ", At the location: "+ " X: "+ loc.getBlockX() +" Y: "+ loc.getBlockY() +" Z: "+ loc.getBlockZ()+ ", In the world: "+ worldname);
    }
}
