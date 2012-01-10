package me.mrmag518.iSafe;

import org.bukkit.block.Block;
import org.bukkit.event.inventory.FurnaceBurnEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.event.inventory.InventoryListener;

public class iSafeInventoryListener extends InventoryListener {
    public static iSafe plugin;
    public iSafeInventoryListener(iSafe instance)
    {
        plugin = instance;
    }
    
    @Override
    public void onFurnaceBurn(FurnaceBurnEvent event) {
        if (event.isCancelled())
        {
            return;
        }
        
        Block block = event.getFurnace();
        
        if(plugin.config.getBoolean("Furnace.Disable-furnace-burning", true))
        {
            event.setBurnTime(0);
            event.setCancelled(true);
        }
    }

    @Override
    public void onFurnaceSmelt(FurnaceSmeltEvent event) {
        if (event.isCancelled())
        {
            return;
        }
        
        Block block = event.getFurnace();
        
        if(plugin.config.getBoolean("Furnace.Disable-furnace-smelting", true))
        {
            event.setCancelled(true);
        }
    }
}
