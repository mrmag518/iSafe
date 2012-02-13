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

package me.mrmag518.iSafe.Events;


import java.util.List;
import me.mrmag518.iSafe.*;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.event.player.PlayerToggleSprintEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginDescriptionFile;

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
        
        if(plugin.getConfig().getBoolean("Buckets.Prevent-LavaBucket-empty", true))
        {
            final List<String> lbworlds = plugin.getConfig().getStringList("Buckets.Lava.Worlds");
            if (plugin.getConfig().getList("Buckets.Lava.Worlds", lbworlds).contains(worldname))
            {
                if(player.hasPermission("iSafe.lavabucket.empty")) {
                    //Access
                } else {
                    if (event.getBucket().equals(Material.LAVA_BUCKET))
                    {
                        event.setCancelled(true);
                        event.getPlayer().getInventory().setItemInHand(new ItemStack(Material.LAVA_BUCKET,1));
                        player.sendMessage(ChatColor.RED + "You cannot empty a LavaBucket in the world: "+ ChatColor.GRAY + worldname);
                    } else {
                        event.setCancelled(false);
                    }
                }
            }
        }
        
        if(plugin.getConfig().getBoolean("Buckets.Prevent-WaterBucket-empty", true))
        {
            final List<String> wbworlds = plugin.getConfig().getStringList("Buckets.Water.Worlds");
            if (plugin.getConfig().getList("Buckets.Water.Worlds", wbworlds).contains(worldname))
            {
                if(player.hasPermission("iSafe.waterbucket.empty")) {
                    //Access
                } else {
                    if (event.getBucket().equals(Material.WATER_BUCKET)) 
                    {
                        event.setCancelled(true);
                        event.getPlayer().getInventory().setItemInHand(new ItemStack(Material.WATER_BUCKET,1));
                        player.sendMessage(ChatColor.RED + "You do not have access to empty that");
                    } else {
                        event.setCancelled(false);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (event.isCancelled())
        {
            return;
        }
        
        //TODO: make events soon, else remove this untill usage.
    }
    
    @EventHandler(priority = EventPriority.LOW)
    public void PreventSprinting(PlayerToggleSprintEvent event) {
        if (event.isCancelled())
        {
            return;
        }
        
        Player player = event.getPlayer();
        
        if(plugin.getConfig().getBoolean("Player.Prevent-Sprinting", true))
        {
            if(player.hasPermission("iSafe.sprint")) {
                //access
            } else {
                if (event.isSprinting()) {
                    event.setCancelled(true);
                } else {
                    event.setCancelled(false);
                }
            }
        }
    }
    
    @EventHandler(priority = EventPriority.LOW)
    public void PreventSneaking(PlayerToggleSneakEvent event) {
        if (event.isCancelled())
        {
            return;
        }
        
        Player player = event.getPlayer();
        
        if(plugin.getConfig().getBoolean("Player.Prevent-Sneaking", true))
        {
            if(player.hasPermission("iSafe.sneak")) {
                //access
            } else {
                if (event.isSneaking()) {
                    event.setCancelled(true);
                } else {
                    event.setCancelled(false);
                }
            }
        }
    }
    
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PluginDescriptionFile pdffile = plugin.getDescription();
        
        if(plugin.getConfig().getBoolean("Player.Broadcast-iSafe-message-on-join", true))
        {
            if (message == 0) {
                player.sendMessage(ChatColor.BLUE + "Welcome " + ((player.getName() + (", This server is running " + ChatColor.YELLOW + (pdffile.getFullName() + ChatColor.BLUE + ".")))));
                message = 1;
            }
        } else if (player.hasPlayedBefore()) {
            if (message == 0) {
                player.sendMessage(ChatColor.BLUE + "Welcome back" + ((player.getName() + (", This server is running " + ChatColor.YELLOW + (pdffile.getFullName() + ChatColor.BLUE + ".")))));
                message = 1;
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Server server = player.getServer();
        
        if(!plugin.getConfig().getBoolean("Player.Allow-creative-gamemode-on-player-quit", true))
        {
            if(player.getGameMode() == GameMode.CREATIVE) {
                player.setGameMode(GameMode.SURVIVAL);
                server.broadcastMessage(ChatColor.DARK_GREEN + "[iSafe] " + ChatColor.GRAY + "Defaulting " + ((player.getName() + "'s gamemode to survival.")));
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
        
        if(plugin.getConfig().getBoolean("Teleport.Disallow-Teleporting", true))
        {
            if(player.hasPermission("iSafe.teleport")) {
                //access
            } else {
                event.setCancelled(true);
                player.sendMessage(ChatColor.RED + "You do not have access to teleport.");
            }
        }
        
        if(plugin.getConfig().getBoolean("Teleport.Prevent-TeleportCause.Command", true))
        {
            if(player.hasPermission("iSafe.teleport.command")) {
                //access
            } else {
                if (event.getCause() == TeleportCause.COMMAND) {
                    event.setCancelled(true);
                    player.sendMessage(ChatColor.RED + "You do not have access to teleport trough commands.");
                }
            }
        }
        
        if(plugin.getConfig().getBoolean("Teleport.Prevent-TeleportCause.EnderPearl", true))
        {
            if(player.hasPermission("iSafe.teleport.enderpearl")) {
                //access
            } else {
                if (event.getCause() == TeleportCause.ENDER_PEARL) {
                    event.setCancelled(true);
                    player.sendMessage(ChatColor.RED + "You do not have access to teleport with ender pearls.");
                }
            }
        }
        
        if(plugin.getConfig().getBoolean("Teleport.Prevent-TeleportCause.Plugin", true))
        {
            if(player.hasPermission("iSafe.teleport.plugin")) {
                //access
            } else {
                if (event.getCause() == TeleportCause.PLUGIN) {
                    event.setCancelled(true);
                    player.sendMessage(ChatColor.RED + "You do not have access to teleport trough plugins.");
                }
            }
        }
        
        if(plugin.getConfig().getBoolean("Teleport.Prevent-TeleportCause.Unknown", true))
        {
            if(player.hasPermission("iSafe.teleport.unknown")) {
                //access
            } else {
                if (event.getCause() == TeleportCause.UNKNOWN) {
                    event.setCancelled(true);
                    player.sendMessage(ChatColor.RED + "You do not have access to teleport with an unknown cause.");
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
        
        if(plugin.getConfig().getBoolean("Chat.Enable-Chat-permissions", true))
        {
           if(player.hasPermission("iSafe.chat")) {
               //access
           } else {
               event.setCancelled(true);
               player.sendMessage(ChatColor.RED + "You do not have access to chat.");
           }
        }
        
        if(plugin.getConfig().getBoolean("Chat.Prevent-arrow-to-the-knee-jokes", true))
        {
            if (msg.contains("arrow") && msg.contains("knee")) {
                event.setCancelled(true);
                player.sendMessage(ChatColor.RED + "You made arrow to the knee jokes, untill you took an arrow to the knee.");
            }
        }
        if(plugin.getConfig().getBoolean("Chat.Punish-arrow-to-the-knee-jokes", true))
        {
            if (msg.contains("arrow") && msg.contains("knee")) {
                player.sendMessage(ChatColor.GREEN + "You made arrow to the knee jokes, untill you took an arrow to the knee.");
                world.createExplosion(player.getLocation(), 1);
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
        
        if(plugin.getConfig().getBoolean("Player.Enable-Bed-permissions", true))
        {
            if(player.hasPermission("iSafe.bed-enter")) {
                //access
            } else {
                event.setCancelled(true);
                player.sendMessage(ChatColor.RED + "You do not have access to sleep.");
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerKick(PlayerKickEvent event) {
        if (event.isCancelled())
        {
            return;
        }
        
        Player player = event.getPlayer();
        Server server = player.getServer();
        
        if(plugin.getConfig().getBoolean("Misc.Enable-kick-messages", true))
        {
            server.broadcastMessage(ChatColor.DARK_GRAY + ((player.getName() + " got kicked.")));
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerFish(PlayerFishEvent event) {
        if (event.isCancelled())
        {
            return;
        }
        
        Player player = event.getPlayer();
        
        if(plugin.getConfig().getBoolean("Player.Enable-fishing-permissions", true))
        {
            if(player.hasPermission("iSafe.fish")) {
                //access
            } else {
                event.setCancelled(true);
                player.sendMessage(ChatColor.RED + "You do not have access to fish.");
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.isCancelled())
        {
            return;
        }
        
        Player player = event.getPlayer();
        Block block = event.getClickedBlock();
        
        if(!plugin.getConfig().getBoolean("Player-Interact.Allow-Buttons-Interact", true))
        {
            if(player.hasPermission("iSafe.interact.buttons")) {
                //access
            } else {
                if(block.getTypeId() == 77) {
                    event.setCancelled(true);
                    player.sendMessage(ChatColor.RED + "You do not have access to interact with that.");
                }
            }
        }
        if(!plugin.getConfig().getBoolean("Player-Interact.Allow-WoodenDoors-Interact", true))
        {
            if(player.hasPermission("iSafe.interact.woodendoors")) {
                //access
            } else {
                if(block.getTypeId() == 324) {
                    event.setCancelled(true);
                    player.sendMessage(ChatColor.RED + "You do not have access to interact with that.");
                }
            }
        }
        if(!plugin.getConfig().getBoolean("Player-Interact.Allow-IronDoors-Interact", true))
        {
            if(player.hasPermission("iSafe.interact.irondoors")) {
                //access
            } else {
                if(block.getTypeId() == 330) {
                    event.setCancelled(true);
                    player.sendMessage(ChatColor.RED + "You do not have access to interact with that.");
                }
            }
        }
        if(!plugin.getConfig().getBoolean("Player-Interact.Allow-Levers-Interact", true))
        {
            if(player.hasPermission("iSafe.interact.levers")) {
                //access
            } else {
                if(block.getTypeId() == 69) {
                    event.setCancelled(true);
                    player.sendMessage(ChatColor.RED + "You do not have access to interact with that.");
                }
            }
        }
        if(!plugin.getConfig().getBoolean("Player-Interact.Allow-StonePressurePlate-Interact", true))
        {
            if(player.hasPermission("iSafe.interact.stonepressureplate")) {
                //access
            } else {
                if(block.getTypeId() == 70) {
                    event.setCancelled(true);
                    player.sendMessage(ChatColor.RED + "You do not have access to interact with that.");
                }
            }
        }
        if(!plugin.getConfig().getBoolean("Player-Interact.Allow-WoodenPressurePlate-Interact", true))
        {
            if(player.hasPermission("iSafe.interact.woodenpressureplate")) {
                //access
            } else {
                if(block.getTypeId() == 72) {
                    event.setCancelled(true);
                    player.sendMessage(ChatColor.RED + "You do not have access to interact with that.");
                }
            }
        }
        if(!plugin.getConfig().getBoolean("Player-Interact.Allow-TrapDoor-Interact", true))
        {
            if(player.hasPermission("iSafe.interact.trapdoor")) {
                //access
            } else {
                if(block.getTypeId() == 96) {
                    event.setCancelled(true);
                    player.sendMessage(ChatColor.RED + "You do not have access to interact with that.");
                }
            }
        }
        if(!plugin.getConfig().getBoolean("Player-Interact.Allow-WoodenFenceGate-Interact", true))
        {
            if(player.hasPermission("iSafe.interact.woodenfencegate")) {
                //access
            } else {
                if(block.getTypeId() == 107) {
                    event.setCancelled(true);
                    player.sendMessage(ChatColor.RED + "You do not have access to interact with that.");
                }
            }
        }
        if(!plugin.getConfig().getBoolean("Player-Interact.Allow-Chest-Interact", true))
        {
            if(player.hasPermission("iSafe.interact.chest")) {
                //access
            } else {
                if(block.getTypeId() == 54) {
                    event.setCancelled(true);
                    player.sendMessage(ChatColor.RED + "You do not have access to interact with that.");
                }
            }
        }
        if(!plugin.getConfig().getBoolean("Misc.Prevent-crop-trampling", true))
        {
            if (event.getMaterial() == Material.SOIL && event.getPlayer() instanceof Player) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        
        if(plugin.getConfig().getBoolean("Player.Only-let-OPs-join", true))
        {
            if(!player.isOp()) {
                player.kickPlayer("You cannot join this server, as you are not an OP.");
            }
        }
        
        if(plugin.getConfig().getBoolean("Player.Kick-player-if-anther-user-with-same-username-log's-on", true))
        {
            String name = player.getName();
            
            for (Player user : plugin.getServer().getOnlinePlayers())
            {
                if (user.getName().equalsIgnoreCase(name)) 
                {
                    user.kickPlayer("The username: "+ user.getName() + " logged on from another location.");
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        if (event.isCancelled())
        {
            return;
        }
        
        Player player = event.getPlayer();
        
        if(plugin.getConfig().getBoolean("Player.Log-commands", true))
        {
            if (message == 0) {
                plugin.getServer().getLogger().info("[iSafe] [Command] "+ event.getPlayer().getName() + ": " + event.getMessage());
                message = 1;
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        if (event.isCancelled())
        {
            return;
        }
        
        Player player = event.getPlayer();
        Entity entity = event.getPlayer();
        
        if(plugin.getConfig().getBoolean("PlayerInteractEntity.Prevent-arrow-hitting-player", true))
        {
            if(entity.getEntityId() == 10) {
                event.setCancelled(true);
            }
        }
        if(plugin.getConfig().getBoolean("PlayerInteractEntity.Prevent-snowball-hitting-player", true))
        {
            if(entity.getEntityId() == 11) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerGameModeChange(PlayerGameModeChangeEvent event) {
        if (event.isCancelled())
        {
            return;
        }
        
        Player player = event.getPlayer();
        
        if(plugin.getConfig().getBoolean("Player.Prevent-Gamemode-change", true))
        {
            event.setCancelled(true); 
        }
        
        if(plugin.getConfig().getBoolean("Player.Prevent-Gamemode-to-CreativeMode-change", true))
        {
            if (event.getNewGameMode().equals(GameMode.CREATIVE)) {
                event.setCancelled(true);
            }
        }
        
        if(plugin.getConfig().getBoolean("Player.Prevent-Gamemode-to-SurvivalMode-change", true))
        {
            if (event.getNewGameMode().equals(GameMode.SURVIVAL)) {
                event.setCancelled(true);
            }
        }
    }
}
