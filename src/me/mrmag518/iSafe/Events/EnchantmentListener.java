package me.mrmag518.iSafe.Events;

import me.mrmag518.iSafe.iSafe;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;


public class EnchantmentListener implements Listener {
    public EnchantmentListener() {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    
    public static iSafe plugin;
    public EnchantmentListener(iSafe instance)
    {
        plugin = instance;
    }
    
    @EventHandler
    public void EnchantmentPreventer(EnchantItemEvent event) {
        if (event.isCancelled())
        {
            return;
        }
        
        if(plugin.getConfig().getBoolean("Enchantment.Prevent-Enchantment", true))
        {
            event.setCancelled(true);
            event.getEnchanter().sendMessage(ChatColor.RED + "You cannot enchant.");
        }
        
        /**
         * Todo
         * - Add preventions for specefic level "payments"
         * - Add permissions.
         * - ?
         */
    }
}
