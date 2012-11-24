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
import com.mrmag518.iSafe.Files.Messages;
import com.mrmag518.iSafe.Util.Log;
import com.mrmag518.iSafe.Util.PermHandler;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.*;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffectType;

public class PlayerListener implements Listener  {
    public static iSafe plugin;
    public PlayerListener(iSafe instance)
    {
        plugin = instance;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    
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
                if(!PermHandler.hasPermission(p, "iSafe.use.lavabuckets")) {
                    if(event.getBucket() == Material.LAVA_BUCKET) {
                        event.setCancelled(true);
                    }
                }
            }
        }
        
        if(plugin.getConfig().getBoolean("Buckets.Water.Prevent")) {
            if (plugin.getConfig().getList("Buckets.Water.CheckedWorlds").contains(worldname)) {
                if(!PermHandler.hasPermission(p, "iSafe.use.waterbuckets")) {
                    if(event.getBucket() == Material.WATER_BUCKET) {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }
    
    @EventHandler
    public void invisibilityManager(PlayerInteractEvent event) {
        if(event.isCancelled()) {
            return;
        }
        
        if(plugin.getConfig().getBoolean("AntiCheat/Security.Invisibility.DisablePotionUsage") != true) {
            return;
        }
        
        Action action = event.getAction();
        
        if(action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
            Player p = event.getPlayer();
            
            if(p.getItemInHand().getTypeId() == 373) {
                int data = p.getItemInHand().getDurability();
                if(data == 8193 || data == 8206 || data == 16318 || data == 16382) {
                    if(!PermHandler.hasPermission(p, "iSafe.bypass.potion.invisibility")) {
                        event.setCancelled(true);
                        p.setItemInHand(new ItemStack(Material.GLASS_BOTTLE));
                    }
                }
            }
            
            ItemStack hand = p.getItemInHand();
            Material type = hand.getType();
            
            if(type == Material.POTION) {
                Potion potion = Potion.fromItemStack(hand);
                PotionEffectType effect = potion.getType().getEffectType();
                
                if(effect == PotionEffectType.INVISIBILITY) {
                    if(!PermHandler.hasPermission(p, "iSafe.bypass.potion.invisibility")) {
                        event.setCancelled(true);
                        p.setItemInHand(new ItemStack(Material.GLASS_BOTTLE));
                    }
                }
            }
        }
    }
    
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (event.isCancelled()){
            return;
        }
        Player p = event.getPlayer();
        
        if(plugin.getConfig().getBoolean("Movement.DisableSprinting", true)){
            if(p.isSprinting()) {
                if(!PermHandler.hasPermission(p, "iSafe.bypass.sprint")) {
                    event.setCancelled(true);
                }
            }
        }
        
        if(plugin.getConfig().getBoolean("Movement.DisableSneaking", true)){
            if(p.isSneaking()) {
                if(!PermHandler.hasPermission(p, "iSafe.bypass.sneak")) {
                    event.setCancelled(true);
                }
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
        Player p = event.getPlayer();
        
        if(plugin.getConfig().getBoolean("Teleport.DisableAllTeleportCauses", true)){
            if(!(PermHandler.hasPermission(p, "iSafe.bypass.teleport"))) {
                event.setTo(event.getFrom());
            }
        }
        
        if(plugin.getConfig().getBoolean("Teleport.Disable.CommandCause", true)){
            if (event.getCause() == TeleportCause.COMMAND) {
                if(!(PermHandler.hasPermission(p, "iSafe.bypass.teleport.command"))) {
                    event.setTo(event.getFrom());
                }
            }
        }
        
        if(plugin.getConfig().getBoolean("Teleport.Disable.EnderpearlCause", true)){
            if (event.getCause() == TeleportCause.ENDER_PEARL) {
                if(!(PermHandler.hasPermission(p, "iSafe.bypass.teleport.enderpearl"))) {
                    event.setTo(event.getFrom());
                }
            }
        }
        
        if(plugin.getConfig().getBoolean("Teleport.Disable.PluginCause", true)){
            if (event.getCause() == TeleportCause.PLUGIN) {
                if(!(PermHandler.hasPermission(p, "iSafe.bypass.teleport.plugin"))) {
                    event.setTo(event.getFrom());
                }
            }
        }
        
        if(plugin.getConfig().getBoolean("Teleport.Disable.UnknownCause", true)){
            if (event.getCause() == TeleportCause.UNKNOWN) {
                if(!(PermHandler.hasPermission(p, "iSafe.bypass.teleport.unknown"))) {
                    event.setTo(event.getFrom());
                }
            }
        }
        
        if(plugin.getConfig().getBoolean("Teleport.Disable.NetherportalCause", true)){
            if (event.getCause() == TeleportCause.NETHER_PORTAL) {
                if(!(PermHandler.hasPermission(p, "iSafe.bypass.teleport.netherportal"))) {
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
            if(!PermHandler.hasPermission(p, "iSafe.use.chat")) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerBedEnter(PlayerBedEnterEvent event) {
        if (event.isCancelled()){
            return;
        }
        Player p = event.getPlayer();
        
        if(plugin.getConfig().getBoolean("Miscellaneous.ForcePermissionsToUseBed", true)){
            if(!(PermHandler.hasPermission(p, "iSafe.use.bed"))) {
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
        
        if(plugin.getConfig().getBoolean("Chat.EnableKickMessages", true)){
            Messages.sendKickMessage(p);
            event.setLeaveMessage(null);
        }
    }
    
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerFish(PlayerFishEvent event) {
        if (event.isCancelled()){
            return;
        }
        Player p = event.getPlayer();
        
        if(plugin.getConfig().getBoolean("Miscellaneous.ForcePermissionsToFish", true)){
            if(!PermHandler.hasPermission(p, "iSafe.bypass.fish")) {
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
                    event.disallow(PlayerLoginEvent.Result.KICK_OTHER, Messages.sameNickPlaying(joiner));
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player joiner = event.getPlayer();
        
        if(plugin.getConfig().getBoolean("Miscellaneous.OnlyLetOPsJoin", true)) {
            if(!joiner.isOp()) {
                joiner.kickPlayer(Messages.denyNonOpsJoin());
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
            Log.info(Messages.commandLogger(p, event));
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerGameModeChange(PlayerGameModeChangeEvent event) {
        if (event.isCancelled()){
            return;
        }
        Player p = event.getPlayer();
        
        if(plugin.getConfig().getBoolean("Gamemode.DisableGamemodeChange", true)){
            event.setCancelled(true); 
        }
        
        if(plugin.getConfig().getBoolean("Gamemode.DisableCreativeToSurvivalChange", true)){
            if(event.getNewGameMode().equals(GameMode.SURVIVAL)) {
                event.setCancelled(true);
                p.setGameMode(GameMode.SURVIVAL);
            }
        }
        
        if(plugin.getConfig().getBoolean("Gamemode.DisableSurvivalToCreativeChange", true)){
            if(event.getNewGameMode().equals(GameMode.CREATIVE)) {
                event.setCancelled(true);
                p.setGameMode(GameMode.CREATIVE);
            }
        }
    }
    
    @EventHandler
    public void checkSpam(AsyncPlayerChatEvent event) {
        if(event.isCancelled()) {
            return;
        }
        Player p = event.getPlayer();
        final String name = p.getName();
        boolean enabled = plugin.getConfig().getBoolean("AntiCheat/Security.Spam.EnableSpamDetector");
        boolean bypassPerms = plugin.getConfig().getBoolean("AntiCheat/Security.Spam.EnableBypassPermissions");
        boolean normalMode = plugin.getConfig().getBoolean("AntiCheat/Security.Spam.UseNormalMode");
        
        if(enabled == false) {
            return;
        }
        if(bypassPerms == true) {
            plugin.checkingSpamPerms = true;
            if(PermHandler.hasPermission(p, "iSafe.bypass.spamcheck")) {
                return;
            }
        }
        
        if(!plugin.spamDB.containsKey(name)) {
            plugin.spamDB.put(name, 1);
        } else {
            plugin.spamDB.put(name, plugin.spamDB.get(name) + 1);
        }
        
        int maxLines = plugin.getConfig().getInt("AntiCheat/Security.Spam.MaxLinesPerSecond");
        
        if(plugin.spamDB.get(name) > maxLines) {
            event.setCancelled(true);
            p.sendMessage(Messages.colorize(Messages.getMessages().getString("SpamDetection")));
        }
        
        if(normalMode == true) {
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                @Override
                public void run() {
                    plugin.spamDB.put(name, plugin.spamDB.get(name) + - 1);
                }
            }, 20);
        }
    }
}
