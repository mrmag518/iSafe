package com.mrmag518.Events.EntityEvents;

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




import java.util.List;

import com.mrmag518.iSafe.*;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.Block;
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
        if (event.isCancelled())
        {
            return;
        }
        Player player = event.getPlayer();
        World world = player.getWorld();
        String worldname = world.getName();
        
        if(plugin.getConfig().getBoolean("Buckets.Lava.Prevent")) {
            final List<String> lbworlds = plugin.getConfig().getStringList("Buckets.Lava.CheckedWorlds");
            
            if (plugin.getConfig().getList("Buckets.Lava.CheckedWorlds", lbworlds).contains(worldname)) {
                if(!(plugin.hasPermission(player, "iSafe.use.lavabuckets"))) {
                    event.setCancelled(true);
                }
            }
        }
        
        if(plugin.getConfig().getBoolean("Buckets.Water.Prevent")) {
            final List<String> lbworlds = plugin.getConfig().getStringList("Buckets.Water.CheckedWorlds");
            
            if (plugin.getConfig().getList("Buckets.Water.CheckedWorlds", lbworlds).contains(worldname)) {
                if(!(plugin.hasPermission(player, "iSafe.use.waterbuckets"))) {
                    event.setCancelled(true);
                }
            }
        }
    }
    
    @EventHandler
    public void PreventSprinting(PlayerMoveEvent event) {
        if (event.isCancelled())
        {
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
        if (event.isCancelled())
        {
            return;
        }
        Player player = event.getPlayer();
        
        if(plugin.getConfig().getBoolean("Teleport.DisableAllTeleportCauses", true))
        {
            if(!(plugin.hasPermission(player, "iSafe.teleport"))) {
                event.setTo(event.getFrom());
            }
        }
        
        if(plugin.getConfig().getBoolean("Teleport.Disable.CommandCause", true))
        {
            if (event.getCause() == TeleportCause.COMMAND) {
                if(!(plugin.hasPermission(player, "iSafe.teleport.command"))) {
                    event.setTo(event.getFrom());
                }
            }
        }
        
        if(plugin.getConfig().getBoolean("Teleport.Disable.EnderpearlCause", true))
        {
            if (event.getCause() == TeleportCause.ENDER_PEARL) {
                if(!(plugin.hasPermission(player, "iSafe.teleport.enderpearl"))) {
                    event.setTo(event.getFrom());
                }
            }
        }
        
        if(plugin.getConfig().getBoolean("Teleport.Disable.PluginCause", true))
        {
            if (event.getCause() == TeleportCause.PLUGIN) {
                if(!(plugin.hasPermission(player, "iSafe.teleport.plugin"))) {
                    event.setTo(event.getFrom());
                }
            }
        }
        
        if(plugin.getConfig().getBoolean("Teleport.Disable.UnknownCause", true))
        {
            if (event.getCause() == TeleportCause.UNKNOWN) {
                if(!(plugin.hasPermission(player, "iSafe.teleport.unknown"))) {
                    event.setTo(event.getFrom());
                }
            }
        }
        
        if(plugin.getConfig().getBoolean("Teleport.Disable.NetherportalCause", true))
        {
            if (event.getCause() == TeleportCause.NETHER_PORTAL) {
                if(!(plugin.hasPermission(player, "iSafe.teleport.netherportal"))) {
                    event.setTo(event.getFrom());
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerChat(PlayerChatEvent event) {
        if (event.isCancelled())
        {
            return;
        }
        String msg = event.getMessage();
        Player player = event.getPlayer();
        World world = player.getWorld();
        
        if(plugin.getConfig().getBoolean("Chat.ForcePermissionToChat", true)) {
            if(!(plugin.hasPermission(player, "iSafe.use.chat"))) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerBedEnter(PlayerBedEnterEvent event) {
        if (event.isCancelled())
        {
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
        if (event.isCancelled())
        {
            return;
        }
        Player p = event.getPlayer();
        Server s = p.getServer();
        
        if(plugin.getConfig().getBoolean("Chat.EnableKickMessages", true))
        {
            plugin.kickMessage(p);
            event.setLeaveMessage(null);
        }
    }
    
    
    
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerFish(PlayerFishEvent event) {
        if (event.isCancelled())
        {
            return;
        }
        Player player = event.getPlayer();
        
        if(plugin.getConfig().getBoolean("Miscellaneous.ForcePermissionsToFish", true))
        {
            if(!(plugin.hasPermission(player, "iSafe.fish"))) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.isCancelled())
        {
            return;
        }
        
        try {
            Player p = event.getPlayer();
            Block block = event.getClickedBlock();
            int ID = block.getTypeId();
            
            //TODO v3.0: Make interaction blacklist.
            
            /**
             * Add all interaction blocks and change the permission nodes.
             */
            if(plugin.getConfig().getBoolean("Player-Interact.Disable.Buttons", true)) {
                if(p.hasPermission("iSafe.interact.buttons")) {
                    //access
                } else if (ID == 77) {
                    event.setCancelled(true);
                    p.sendMessage(ChatColor.RED + "You do not have access to this.");
                }
            }
            if(plugin.getConfig().getBoolean("Player-Interact.Disable.Chests", true)) {
                if(p.hasPermission("iSafe.interact.chest")) {
                    //access
                } else if (ID == 54) {
                    event.setCancelled(true);
                    p.sendMessage(ChatColor.RED + "You do not have access to this.");
                }
            }
            if(plugin.getConfig().getBoolean("Player-Interact.Disable.Dispensers", true)) {
                if(p.hasPermission("iSafe.interact.dispensers")) {
                    //access
                } else if (ID == 23) {
                    event.setCancelled(true);
                    p.sendMessage(ChatColor.RED + "You do not have access to this.");
                }
            }
            if(plugin.getConfig().getBoolean("Player-Interact.Disable.Woodendoors", true)) {
                if(p.hasPermission("iSafe.interact.woodendoors")) {
                    //access
                } else if (ID == 324) {
                    event.setCancelled(true);
                    p.sendMessage(ChatColor.RED + "You do not have access to this.");
                }
            }
            if(plugin.getConfig().getBoolean("Player-Interact.Disable.Irondoors", true)) {
                if(p.hasPermission("iSafe.interact.irondoors")) {
                    //access
                } else if (ID == 330) {
                    event.setCancelled(true);
                    p.sendMessage(ChatColor.RED + "You do not have access to this.");
                }
            }
            if(plugin.getConfig().getBoolean("Player-Interact.Disable.Levers", true)) {
                if(p.hasPermission("iSafe.interact.levers")) {
                    //access
                } else if (ID == 69) {
                    event.setCancelled(true);
                    p.sendMessage(ChatColor.RED + "You do not have access to this.");
                }
            }
            if(plugin.getConfig().getBoolean("Player-Interact.Disable.StonePressurePlates", true)) {
                if(p.hasPermission("iSafe.interact.stonepressureplate")) {
                    //access
                } else if (ID == 70) {
                    event.setCancelled(true);
                    p.sendMessage(ChatColor.RED + "You do not have access to this.");
                }
            }
            if(plugin.getConfig().getBoolean("Player-Interact.Disable.WoodenPressurePlates", true)) {
                if(p.hasPermission("iSafe.interact.woodenpressureplate")) {
                    //access
                } else if (ID == 72) {
                    event.setCancelled(true);
                    p.sendMessage(ChatColor.RED + "You do not have access to this.");
                }
            }
            if(plugin.getConfig().getBoolean("Player-Interact.Disable.Trapdoors", true)) {
                if(p.hasPermission("iSafe.interact.trapdoor")) {
                    //access
                } else if (ID == 96) {
                    event.setCancelled(true);
                    p.sendMessage(ChatColor.RED + "You do not have access to this.");
                }
            }
            if(plugin.getConfig().getBoolean("Player-Interact.Disable.WoodenFenceGates", true)) {
                if(p.hasPermission("iSafe.interact.woodenfencegate")) {
                    //access
                } else if (ID == 107) {
                    event.setCancelled(true);
                    p.sendMessage(ChatColor.RED + "You do not have access to this.");
                }
            }
            
        } catch (NullPointerException npe) {
            //ignored
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
        
        // ¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤&#%"¤%
        if(plugin.getConfig().getBoolean("Miscellaneous.OnlyLetOPsJoin", true))
        {
            if(!joiner.isOp()) {
                joiner.kickPlayer(plugin.denyNonOpsJoin());
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        if (event.isCancelled())
        {
            return;
        }
        Player p = event.getPlayer();
        
        if(plugin.getConfig().getBoolean("Chat.LogCommands", true)) {
            plugin.log.info("[iSafe] " + plugin.commandLogger(p, event));
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerGameModeChange(PlayerGameModeChangeEvent event) {
        if (event.isCancelled())
        {
            return;
        }
        boolean survival = event.getNewGameMode().equals(GameMode.SURVIVAL);
        boolean creative = event.getNewGameMode().equals(GameMode.CREATIVE);
        Player p = event.getPlayer();
        
        if(plugin.getConfig().getBoolean("Gamemode.DisableGamemodeChange", true))
        {
            event.setCancelled(true); 
        }
        
        if(plugin.getConfig().getBoolean("Gamemode.DisableSurvivalToCreativeChange", true))
        {
            if (survival) 
            {
                event.setCancelled(true);
                p.setGameMode(GameMode.SURVIVAL);
            }
        }
        
        if(plugin.getConfig().getBoolean("Gamemode.DisableCreativeToSurvivalChange", true))
        {
            if (creative) 
            {
                event.setCancelled(true);
                p.setGameMode(GameMode.CREATIVE);
            }
        }
    }
}
