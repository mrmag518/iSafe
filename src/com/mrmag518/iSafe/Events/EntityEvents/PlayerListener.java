package com.mrmag518.iSafe.Events.EntityEvents;

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


import com.mrmag518.iSafe.*;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

public class PlayerListener implements Listener  {
    public static iSafe plugin;
    public PlayerListener(iSafe instance)
    {
        plugin = instance;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    int message = 0;
    
    @EventHandler
    public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event) {
        if (event.isCancelled()){
            return;
        }
        Player p = event.getPlayer();
        World world = p.getWorld();
        String worldname = world.getName();
        
        if(plugin.getConfig().getBoolean("Buckets.Lava.Prevent")) {
            if (plugin.getConfig().getList("Buckets.Lava.CheckedWorlds").contains(worldname)) {
                if(!(plugin.hasPermission(p, "iSafe.use.lavabuckets"))) {
                    event.setCancelled(true);
                }
            }
        }
        
        if(plugin.getConfig().getBoolean("Buckets.Water.Prevent")) {
            if (plugin.getConfig().getList("Buckets.Water.CheckedWorlds").contains(worldname)) {
                if(!(plugin.hasPermission(p, "iSafe.use.waterbuckets"))) {
                    event.setCancelled(true);
                }
            }
        }
    }
    
    @EventHandler
    public void PreventSprinting(PlayerMoveEvent event) {
        if (event.isCancelled()){
            return;
        }
        Player player = event.getPlayer();
        
        if(plugin.getConfig().getBoolean("Movement.DisableSprinting", true))
        {
            if(!(plugin.hasPermission(player, "iSafe.bypass.sprint"))) {
                event.setCancelled(true);
            }
        }
        if(plugin.getConfig().getBoolean("Movement.DisableSneaking", true))
        {
            if(!(plugin.hasPermission(player, "iSafe.bypass.sneak"))) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player p = event.getPlayer();
        
        if(plugin.getConfig().getBoolean("Gamemode.SwitchToSurvivalOnQuit", true)) {
            if(p.getGameMode().equals(GameMode.CREATIVE)) {
                p.setGameMode(GameMode.SURVIVAL);
            }
        }
        
        if(plugin.getConfig().getBoolean("Gamemode.SwitchToCreativeOnQuit", true)) {
            if(p.getGameMode().equals(GameMode.SURVIVAL)) {
                p.setGameMode(GameMode.CREATIVE);
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        if (event.isCancelled()){
            return;
        }
        Player player = event.getPlayer();
        
        if(plugin.getConfig().getBoolean("Teleport.DisableAllTeleportCauses", true))
        {
            if(!(plugin.hasPermission(player, "iSafe.bypass.teleport"))) {
                event.setTo(event.getFrom());
            }
        }
        
        if(plugin.getConfig().getBoolean("Teleport.Disable.CommandCause", true))
        {
            if (event.getCause() == TeleportCause.COMMAND) {
                if(!(plugin.hasPermission(player, "iSafe.bypass.teleport.command"))) {
                    event.setTo(event.getFrom());
                }
            }
        }
        
        if(plugin.getConfig().getBoolean("Teleport.Disable.EnderpearlCause", true))
        {
            if (event.getCause() == TeleportCause.ENDER_PEARL) {
                if(!(plugin.hasPermission(player, "iSafe.bypass.teleport.enderpearl"))) {
                    event.setTo(event.getFrom());
                }
            }
        }
        
        if(plugin.getConfig().getBoolean("Teleport.Disable.PluginCause", true))
        {
            if (event.getCause() == TeleportCause.PLUGIN) {
                if(!(plugin.hasPermission(player, "iSafe.bypass.teleport.plugin"))) {
                    event.setTo(event.getFrom());
                }
            }
        }
        
        if(plugin.getConfig().getBoolean("Teleport.Disable.UnknownCause", true))
        {
            if (event.getCause() == TeleportCause.UNKNOWN) {
                if(!(plugin.hasPermission(player, "iSafe.bypass.teleport.unknown"))) {
                    event.setTo(event.getFrom());
                }
            }
        }
        
        if(plugin.getConfig().getBoolean("Teleport.Disable.NetherportalCause", true))
        {
            if (event.getCause() == TeleportCause.NETHER_PORTAL) {
                if(!(plugin.hasPermission(player, "iSafe.bypass.teleport.netherportal"))) {
                    event.setTo(event.getFrom());
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        if (event.isCancelled()){
            return;
        }
        Player p = event.getPlayer();
        
        if(plugin.getConfig().getBoolean("Chat.ForcePermissionToChat", true)) {
            if(!(plugin.hasPermission(p, "iSafe.use.chat"))) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerBedEnter(PlayerBedEnterEvent event) {
        if (event.isCancelled()){
            return;
        }
        Player player = event.getPlayer();
        
        if(plugin.getConfig().getBoolean("Miscellaneous.ForcePermissionsToUseBed", true))
        {
            if(!(plugin.hasPermission(player, "iSafe.use.bed"))) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerKick(PlayerKickEvent event) {
        if (event.isCancelled()){
            return;
        }
        Player p = event.getPlayer();
        
        if(plugin.getConfig().getBoolean("Chat.EnableKickMessages", true))
        {
            plugin.kickMessage(p);
            event.setLeaveMessage(null);
        }
    }
    
    
    
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerFish(PlayerFishEvent event) {
        if (event.isCancelled()){
            return;
        }
        Player player = event.getPlayer();
        
        if(plugin.getConfig().getBoolean("Miscellaneous.ForcePermissionsToFish", true))
        {
            if(!(plugin.hasPermission(player, "iSafe.bypass.fish"))) {
                event.setCancelled(true);
            }
        }
    }
    
    @EventHandler(priority = EventPriority.NORMAL)
    public void loginManagement(PlayerLoginEvent event) {
        Player joiner = event.getPlayer();
        
        if(plugin.getConfig().getBoolean("AntiCheat/Sucurity.KickJoinerIfSameNickIsOnline", true)){
            for(Player onlinePl : Bukkit.getServer().getOnlinePlayers()) {
                if(joiner.getName().equalsIgnoreCase(onlinePl.getName())) {
                    event.disallow(PlayerLoginEvent.Result.KICK_OTHER, plugin.sameNickPlaying(joiner));
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player joiner = event.getPlayer();
        
        if(plugin.getConfig().getBoolean("Miscellaneous.OnlyLetOPsJoin", true))
        {
            if(!joiner.isOp()) {
                joiner.kickPlayer(plugin.denyNonOpsJoin());
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        if (event.isCancelled()){
            return;
        }
        Player p = event.getPlayer();
        
        if(plugin.getConfig().getBoolean("Chat.LogCommands", true)) {
            plugin.log.info("[iSafe] " + plugin.commandLogger(p, event));
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerGameModeChange(PlayerGameModeChangeEvent event) {
        if (event.isCancelled()){
            return;
        }
        Player p = event.getPlayer();
        
        if(plugin.getConfig().getBoolean("Gamemode.DisableGamemodeChange", true))
        {
            event.setCancelled(true); 
        }
        
        if(plugin.getConfig().getBoolean("Gamemode.DisableCreativeToSurvivalChange", true))
        {
            if (event.getNewGameMode().equals(GameMode.SURVIVAL)) 
            {
                event.setCancelled(true);
                p.setGameMode(GameMode.SURVIVAL);
            }
        }
        
        if(plugin.getConfig().getBoolean("Gamemode.DisableSurvivalToCreativeChange", true))
        {
            if (event.getNewGameMode().equals(GameMode.CREATIVE)) 
            {
                event.setCancelled(true);
                p.setGameMode(GameMode.CREATIVE);
            }
        }
    }
}
