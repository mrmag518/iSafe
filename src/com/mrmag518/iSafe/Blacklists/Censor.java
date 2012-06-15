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

package com.mrmag518.iSafe.Blacklists;

import com.mrmag518.iSafe.iSafe;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;

public class Censor implements Listener {
    public static iSafe plugin;
    public Censor(iSafe instance)
    {
        plugin = instance;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    
    @EventHandler
    public void CommandBlacklist(PlayerChatEvent event) {
        if (event.isCancelled())
        {
            return;
        }
        
        Player p = event.getPlayer();
        Server s = p.getServer();
        
        String word = event.getMessage();
        String word_uppercase = event.getMessage().toUpperCase();
        String word_lowercase = event.getMessage().toLowerCase();
        String word_raw = event.getMessage().toString();
        
        //No need for multi-world support, yet thought.
        final List<String> censoredWords = new ArrayList<String>();
        if (plugin.getBlacklist().getList("Censor.Words/Blacklist", censoredWords).contains(word)
                || plugin.getBlacklist().getList("Censor.Words/Blacklist", censoredWords).contains(word_uppercase) 
                || plugin.getBlacklist().getList("Censor.Words/Blacklist", censoredWords).contains(word_lowercase) 
                || plugin.getBlacklist().getList("Censor.Words/Blacklist", censoredWords).contains(word_raw)) 
        {
            if(!(event.isCancelled())) 
            {
                event.setCancelled(true);
                
                if (plugin.getBlacklist().getBoolean("Censor.Alert/log.To-console", true))
                {
                    if (event.isCancelled()) {
                        plugin.log.info("[iSafe] "+ p.getName() + " tried to say the blacklisted word: "+ word);
                    }
                }
                
                if (plugin.getBlacklist().getBoolean("Censor.Alert/log.To-player", true))
                {
                    if (event.isCancelled()) {
                        p.sendMessage(ChatColor.RED + "The word '"+ ChatColor.GRAY + word + ChatColor.RED + "' is blacklisted.");
                    }
                }
            }
        }
    }
}
