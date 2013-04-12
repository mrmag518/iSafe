package com.mrmag518.iSafe.EventManager;

import com.mrmag518.iSafe.Files.GMConfig;
import com.mrmag518.iSafe.iSafe;
import org.bukkit.GameMode;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerGameModeChangeEvent;

public class GMManager implements Listener {
    public static iSafe plugin;
    public GMManager(iSafe instance) {
        plugin = instance;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    
    /**
     * Todo: 
     * Add permissions!
     */
    
    /*@EventHandler
    public void handleGamemodeChange(PlayerGameModeChangeEvent event) {
        if(event.isCancelled()) {
            return;
        }
        Player p = event.getPlayer();
        
        if(!GMConfig.getConfig().getBoolean("GameModeChange.Allow")) {
            event.setCancelled(true);
            p.sendMessage("Gamemode changing is disallowed.");
            return;
        }
        
        if(p.getGameMode() == GameMode.SURVIVAL) {
            if(event.getNewGameMode() == GameMode.CREATIVE) {
                if(GMConfig.getConfig().getBoolean("GameModeChange.DisallowSurvivalToCreative")) {
                    event.setCancelled(true);
                    p.sendMessage("Switching from Survival to Creative is disallowed."); 
                }
            }
        } else if(p.getGameMode() == GameMode.CREATIVE) {
            if(event.getNewGameMode() == GameMode.SURVIVAL) {
                if(GMConfig.getConfig().getBoolean("GameModeChange.DisallowCreativeToSurvival")) {
                    event.setCancelled(true);
                    p.sendMessage("Switching from Creative to Survival is disallowed."); 
                }
            }
        }
    }*/
}
