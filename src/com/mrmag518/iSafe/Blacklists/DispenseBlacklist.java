package com.mrmag518.iSafe.Blacklists;

import com.mrmag518.iSafe.iSafe;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;

public class DispenseBlacklist implements Listener {
    public static iSafe plugin;
    public DispenseBlacklist(iSafe instance)
    {
        plugin = instance;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    
    @EventHandler
    public void DispenseBlacklist(BlockDispenseEvent event) {
        if (event.isCancelled()){
            return;
        }
        Block b = event.getBlock();
        String worldname = b.getWorld().getName();
        int blockID = event.getItem().getTypeId();
        String BlockNAME = event.getItem().getType().name().toLowerCase();
        
        if(plugin.getBlacklist().getList("Dispense.Blacklist").contains(blockID)
            || plugin.getBlacklist().getList("Dispense.Blacklist").contains(BlockNAME.toLowerCase())) 
        {
            if(plugin.getBlacklist().getList("Dispense.EnabledWorlds").contains(worldname)) 
            {
                event.setCancelled(true);
                if(plugin.getBlacklist().getBoolean("Dispense.Alert/log-to.Console", true)) {
                    plugin.log.info("[iSafe] A blacklisted block was prevented from dispensing. " + BlockNAME.toLowerCase());
                }
            }
        }
    }
}
