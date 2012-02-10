package me.mrmag518.iSafe.Events;

import me.mrmag518.iSafe.iSafe;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class DropListener implements Listener {
    public DropListener() {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    
    public static iSafe plugin;
    public DropListener(iSafe instance)
    {
        plugin = instance;
    }
    
    @EventHandler
    public void onBlockPlace(BlockBreakEvent event) {
        if (event.isCancelled())
        {
            return;
        }
        
        Block block = event.getBlock();
        
        if (plugin.getConfig().getBoolean("Drop-configure.Glass.Drop.Glass", true))
        {
            if (block.getTypeId() == 20) {
                
                ItemStack glass = new ItemStack(event.getBlock().getType(), 1);
                event.getPlayer().getWorld().dropItemNaturally(event.getBlock().getLocation(), glass);
                block.setType(Material.AIR);
            }
        }
        
        if (plugin.getConfig().getBoolean("Drop-configure.Mobspawner.Drop.Mobspawner", true))
        {
            if (block.getTypeId() == 52) {
                
                ItemStack mobspawner = new ItemStack(event.getBlock().getType(), 1);
                event.getPlayer().getWorld().dropItemNaturally(event.getBlock().getLocation(), mobspawner);
                block.setType(Material.AIR);
            }
        }
        
        if (plugin.getConfig().getBoolean("Drop-configure.Ice.Drop.Ice", true))
        {
            if (block.getTypeId() == 79) {
                ItemStack ice = new ItemStack(event.getBlock().getType(), 1);
                event.getPlayer().getWorld().dropItemNaturally(event.getBlock().getLocation(), ice);
                if (plugin.getConfig().getBoolean("Drop-configure.Ice.Drop.Ice-options.Prevent-water", true))
                {
                    block.setType(Material.AIR);
                    block.setType(Material.AIR);
                }
            }
        }
        
        if (plugin.getConfig().getBoolean("Drop-configure.Bedrock.Drop.Bedrock", true))
        {
            if (block.getTypeId() == 7) {
                ItemStack bedrock = new ItemStack(event.getBlock().getType(), 1);
                event.getPlayer().getWorld().dropItemNaturally(event.getBlock().getLocation(), bedrock);
                block.setType(Material.AIR);
            }
        }
        
        if (plugin.getConfig().getBoolean("Drop-configure.Bookshelf.Drop.Bookshelf", true))
        {
            if (block.getTypeId() == 47){
                ItemStack bookshelf = new ItemStack(event.getBlock().getType(), 1);
                event.getPlayer().getWorld().dropItemNaturally(event.getBlock().getLocation(), bookshelf);
                block.setType(Material.AIR);
            }
        }
        
        if (plugin.getConfig().getBoolean("Drop-configure.Grass_thingy.Drop.Grass_thingy", true))
        {      
            if (block.getTypeId() == 31 || block.getTypeId() == 32) {  
                ItemStack grass = new ItemStack(event.getBlock().getType(), 1);
                event.getPlayer().getWorld().dropItemNaturally(event.getBlock().getLocation(), grass);
                block.setType(Material.AIR);
            }
        }
    }
}
