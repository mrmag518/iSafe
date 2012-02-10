package me.mrmag518.iSafe.Events;

import me.mrmag518.iSafe.*;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.FurnaceBurnEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;

public class InventoryListener implements Listener {
    public InventoryListener() {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    
    public static iSafe plugin;
    public InventoryListener(iSafe instance)
    {
        plugin = instance;
    }
    
    @EventHandler(priority = EventPriority.NORMAL)
    public void onFurnaceBurn(FurnaceBurnEvent event) {
        if (event.isCancelled())
        {
            return;
        }
        
        if(plugin.getConfig().getBoolean("Furnace.Disable-furnace-burning", true))
        {
            event.setBurnTime(0);
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onFurnaceSmelt(FurnaceSmeltEvent event) {
        if (event.isCancelled())
        {
            return;
        }
        
        
        if(plugin.getConfig().getBoolean("Furnace.Disable-furnace-smelting", true))
        {
            event.setCancelled(true);
        }
    }
}
