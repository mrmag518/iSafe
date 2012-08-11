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

import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class DropBlacklist implements Listener {
    
    public static iSafe plugin;
    public DropBlacklist(iSafe instance)
    {
        plugin = instance;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    
    @EventHandler
    public void BlacklistDrop(PlayerDropItemEvent event) {
        if (event.isCancelled()){
            return;
        }
        Player p = event.getPlayer();
        World world = p.getWorld();
        int itemID = event.getItemDrop().getItemStack().getTypeId();
        String ItemNAME = event.getItemDrop().getItemStack().getType().name().toLowerCase();
        String worldname = world.getName();
        
        final List<Item> dropedblocks = new ArrayList<Item>();
        
        if (plugin.getBlacklist().getList("Drop.Blacklist", dropedblocks).contains(itemID)
                || plugin.getBlacklist().getList("Drop.Blacklist", dropedblocks).contains(ItemNAME.toLowerCase()))
        {
            if(!plugin.hasBlacklistPermission(p, "iSafe.bypass.blacklist.drop")) 
            {
                if (!event.isCancelled()) 
                {
                    final List<String> Dropworlds = plugin.getBlacklist().getStringList("Drop.Worlds");
                
                    if (plugin.getBlacklist().getList("Drop.EnabledWorlds", Dropworlds).contains(worldname))
                    {
                        if (plugin.getBlacklist().getBoolean("Drop.Gamemode.PreventFor.Survival", true)) {
                            if(p.getGameMode().equals(GameMode.SURVIVAL)) {
                                event.setCancelled(true);
                            }
                        }
                        
                        if (plugin.getBlacklist().getBoolean("Drop.Gamemode.PreventFor.Creative", true)) {
                            if(p.getGameMode().equals(GameMode.CREATIVE)) {
                                event.setCancelled(true);
                            }
                        }
                        
                        if (plugin.getBlacklist().getBoolean("Drop.KickPlayer", true))
                        {
                            if (event.isCancelled()){
                                p.kickPlayer(plugin.blacklistDropKickMsg(event.getItemDrop()));
                            }    
                        }

                        if (plugin.getBlacklist().getBoolean("Drop.Alert/log.ToConsole", true))
                        {
                            if (event.isCancelled()) {
                                plugin.log.info("[iSafe]" + p.getName() + " was prevented from dropping the blacklisted item: " + ItemNAME);
                            }
                        }

                        if (plugin.getBlacklist().getBoolean("Drop.Alert/log.ToPlayer", true))
                        {
                            if (event.isCancelled()) {
                                p.sendMessage(plugin.blacklistDropMsg(ItemNAME));
                            }
                        }
                    }
                }
            }
        }
        
        if (plugin.getBlacklist().getBoolean("Drop.TotallyDisableBlockDrop", true))
        {
            if(!plugin.hasPermission(p, "iSafe.bypass.drop")) {
                event.setCancelled(true);
            }
        }
    }
}
