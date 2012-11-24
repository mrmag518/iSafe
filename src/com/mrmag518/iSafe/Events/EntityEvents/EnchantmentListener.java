/*
 * iSafe
 * Copyright (C) 2011-2012 mrmag518 <magnusaub@yahoo.no>
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.mrmag518.iSafe.Events.EntityEvents;

import com.mrmag518.iSafe.Util.PermHandler;
import com.mrmag518.iSafe.iSafe;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;


public class EnchantmentListener implements Listener {
    public static iSafe plugin;
    public EnchantmentListener(iSafe instance)
    {
        plugin = instance;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    
    @EventHandler
    public void EnchantmentManager(EnchantItemEvent event) {
        Player p = event.getEnchanter();
        
        if(plugin.getConfig().getBoolean("Enchantment.PreventEnchantment", true))
        {
            if(!PermHandler.hasPermission(p, "iSafe.bypass.enchant")) {
                event.setCancelled(true);
                p.sendMessage(ChatColor.RED + "You do not have access to enchant items.");
                return;
            }
        }
        
        if(plugin.getConfig().getBoolean("Enchantment.PreventCreativeModeEnchanting", true)) {
            if(p.getGameMode().equals(GameMode.CREATIVE)) {
                if(!PermHandler.hasPermission(p, "iSafe.bypass.enchant")) {
                    event.setCancelled(true);
                    p.sendMessage(ChatColor.RED + "You do not have access to enchant items in creative mode.");
                    return;
                }
            }
        }
        
        // TODO: Make an enchantment blacklist, were you can blacklist items and enchantments.
        /*ItemStack itemstack = event.getItem();
        Material item = itemstack.getType();
        String name = item.name().toLowerCase();*/
    }
}
