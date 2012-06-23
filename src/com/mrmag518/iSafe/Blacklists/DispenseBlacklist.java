package com.mrmag518.iSafe.Blacklists;

import com.mrmag518.iSafe.iSafe;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.inventory.ItemStack;

public class DispenseBlacklist implements Listener {
    public static iSafe plugin;
    public DispenseBlacklist(iSafe instance)
    {
        plugin = instance;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    
    @EventHandler
    public void DispenseBlacklist(BlockDispenseEvent event) {
        if (event.isCancelled())
        {
            return;
        }
        Block b = event.getBlock();
        String worldname = b.getWorld().getName();
        
        int itemID = event.getItem().getTypeId();
        String BlockNAME_Lowercase = event.getItem().getType().name().toLowerCase();
        String BlockNAME_Uppercase = event.getItem().getType().name().toUpperCase();
        String BlockNAME_Name = event.getItem().getType().name();
        Material BlockNAME = event.getItem().getType();
        
        final List<ItemStack> dispensedBlock = new ArrayList<ItemStack>();
        if (plugin.getBlacklist().getList("Dispense.Blacklist", dispensedBlock).contains(itemID)
                || plugin.getBlacklist().getList("Dispense.Blacklist", dispensedBlock).contains(BlockNAME_Lowercase)
                || plugin.getBlacklist().getList("Dispense.Blacklist", dispensedBlock).contains(BlockNAME_Uppercase)
                || plugin.getBlacklist().getList("Dispense.Blacklist", dispensedBlock).contains(BlockNAME)
                || plugin.getBlacklist().getList("Dispense.Blacklist", dispensedBlock).contains(BlockNAME_Name))
        {
            if (!event.isCancelled()) 
            {
                final List<String> dispenseWorlds = plugin.getBlacklist().getStringList("Dispense.Worlds");
                if (plugin.getBlacklist().getList("Dispense.Worlds", dispenseWorlds).contains(worldname))
                {
                    event.setCancelled(true);
                    if(plugin.getBlacklist().getBoolean("Dispense.Alert/log-to.Console", true)) {
                        plugin.log.info("[iSafe] A blacklisted block was prevented from dispensing. " + BlockNAME_Name);
                    }
                }
            }
        }
    }
}
