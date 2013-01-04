package com.mrmag518.iSafe.Util;

import com.mrmag518.iSafe.Util.PermHandler;
import com.mrmag518.iSafe.iSafe;
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
        plugin.checkingUpdatePerms = true;
        Player p = event.getPlayer();
        if(PermHandler.hasPermission(p, "iSafe.admin") || p.isOp()) {
            try {
                if (plugin.newVersion > plugin.currentVersion) {
                    p.sendMessage(ChatColor.GREEN + "A new version of iSafe is out! ("+ ChatColor.WHITE +  plugin.newVersion + ChatColor.GREEN + ")");
                    p.sendMessage(ChatColor.GREEN + "Current iSafe version running: " + ChatColor.WHITE + plugin.currentVersion + ChatColor.GREEN + ".");
                    p.sendMessage(ChatColor.GREEN + "It's recommended updating :)");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
