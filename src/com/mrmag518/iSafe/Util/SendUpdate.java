package com.mrmag518.iSafe.Util;

import com.mrmag518.iSafe.iSafe;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class SendUpdate implements Listener {
    public static iSafe plugin;
    public SendUpdate(iSafe instance) {
        plugin = instance;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void sendUpdate(PlayerJoinEvent event) {
        Player p = event.getPlayer();
        if(PermHandler.hasPermission(p, "iSafe.admin", false) || p.isOp()) {
            if(plugin.updateFound) {
                p.sendMessage(ChatColor.GREEN + "A new version of iSafe is out! ("+ ChatColor.WHITE +  plugin.versionFound + ChatColor.GREEN + ")");
                p.sendMessage(ChatColor.GREEN + "Current iSafe version running: " + ChatColor.WHITE + plugin.getDescription().getFullName() + ChatColor.GREEN + ".");
                p.sendMessage(ChatColor.GREEN + "It's highly recommended to update, as there may be important fixes or improvements to the plugin!");
            }
        }
    }
}
