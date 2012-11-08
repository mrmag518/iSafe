package com.mrmag518.iSafe.EventManagers;

import com.mrmag518.iSafe.iSafe;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class PotionManager implements Listener {
    public static iSafe plugin;
    public PotionManager(iSafe instance)
    {
        plugin = instance;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    
    @EventHandler
    public void interactListener(PlayerInteractEvent event) {
        if(event.isCancelled()) {
            return;
        }
        Action act = event.getAction();
        
        if(act != Action.RIGHT_CLICK_AIR || act != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        
        
    }
}
