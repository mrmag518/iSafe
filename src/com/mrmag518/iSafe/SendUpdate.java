package com.mrmag518.iSafe;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class SendUpdate implements Listener {
    public static iSafe plugin;
    public SendUpdate(iSafe instance)
    {
        plugin = instance;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    
    //From MilkBowl's Vault. (with a few modifications)
    @EventHandler(priority = EventPriority.MONITOR)
    public void sendUpdate(PlayerJoinEvent event) {
        Player p = event.getPlayer();
        if(plugin.hasPermission(p, "iSafe.admin") || p.isOp()) {
            try {
                String oldVersion = plugin.getDescription().getVersion();
                if (!(plugin.newVersion.contains(oldVersion))) {
                    p.sendMessage(ChatColor.GREEN + "A new version of iSafe is out! ("+ ChatColor.GOLD +  plugin.newVersion + ChatColor.GREEN + ")");
                    p.sendMessage(ChatColor.GREEN + "Current iSafe version running: " + ChatColor.GOLD + "iSafe v" + oldVersion + ChatColor.GREEN + ".");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            //ignored
        }
    }
}
