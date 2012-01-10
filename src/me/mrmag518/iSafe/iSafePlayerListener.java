package me.mrmag518.iSafe;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginDescriptionFile;

public class iSafePlayerListener extends PlayerListener {
    public static iSafe plugin;
    public iSafePlayerListener(iSafe instance)
    {
        plugin = instance;
    }
    
    @Override
    public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event) {
        if (event.isCancelled())
        {
            return;
        }
        
        Player player = event.getPlayer();
        Server server = player.getServer();
        Material material = event.getBucket();
        World world = player.getWorld();
        
        if(!plugin.config.getBoolean("Buckets.Allow-LavaBucket-empty", true))
        {
            if(player.hasPermission("iSafe.lavabucket.empty")) {
                //access
            } else {
                if (event.getBucket().equals(Material.LAVA_BUCKET)) {
                    event.setCancelled(true);
                    event.getPlayer().getInventory().setItemInHand(new ItemStack(Material.BUCKET,1));
                    player.sendMessage(ChatColor.RED + "You do not have access to empty that");
            }
        }
    }
        if(!plugin.config.getBoolean("Buckets.Allow-WaterBucket-empty", true))
        {
            if(player.hasPermission("iSafe.waterbucket.empty")) {
                //access
            } else {
                if (event.getBucket().equals(Material.WATER_BUCKET)) {
                    event.setCancelled(true);
                    event.getPlayer().getInventory().setItemInHand(new ItemStack(Material.BUCKET,1));
                    player.sendMessage(ChatColor.RED + "You do not have access to empty that");
                }
            }
        }
    }

    @Override
    public void onPlayerMove(PlayerMoveEvent event) {
        if (event.isCancelled())
        {
            return;
        }
        
        Player player = event.getPlayer();
        Server server = player.getServer();
        World world = player.getWorld();
        
        if(!plugin.config.getBoolean("Player.Allow-Sprinting", true))
        {
            if(player.hasPermission("iSafe.sprint")) {
                //access
            } else {
                if (player.isSprinting()) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @Override
    public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
        if (event.isCancelled())
        {
            return;
        }
        
        Player player = event.getPlayer();
        Server server = player.getServer();
        World world = player.getWorld();
        
        if(!plugin.config.getBoolean("Player.Allow-Sneaking", true))
        {
            if(player.hasPermission("iSafe.sneak")) {
                //access
            } else {
                if (player.isSneaking()) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @Override
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Server server = player.getServer();
        World world = player.getWorld();
        PluginDescriptionFile pdffile = plugin.getDescription();
        
        if(plugin.config.getBoolean("Player.Broadcast-iSafe-message-on-join", true))
        {
            player.sendMessage(ChatColor.BLUE + "Welcome " + ((player.getName() + (", This server is running " + ChatColor.YELLOW + (pdffile.getFullName() + ChatColor.BLUE + ".")))));
        }
    }

    @Override
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Server server = player.getServer();
        World world = player.getWorld();
        
        if(!plugin.config.getBoolean("Player.Allow-creative-gamemode-on-player-quit", true))
        {
            if(player.getGameMode() == GameMode.CREATIVE) {
                player.setGameMode(GameMode.SURVIVAL);
                server.broadcastMessage(ChatColor.DARK_GREEN + "[iSafe] " + ChatColor.GRAY + "Defaulting " + ((player.getName() + "'s gamemode to survival.")));
            }
        }
    }

    @Override
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        if (event.isCancelled())
        {
            return;
        }
        
        Player player = event.getPlayer();
        Item item = event.getItemDrop();
        Server server = player.getServer();
        World world = player.getWorld();
        
        if(!plugin.config.getBoolean("Drops.Allow-droping", true))
        {
            if(player.hasPermission("iSafe.drop")) {
                //access
            } else {
                event.setCancelled(true);
                player.sendMessage(ChatColor.RED + "You do now have access to drop.");
            }
        }
        
        if(!plugin.config.getBoolean("Drops.Allow-TNT-drop", true))
        {
            if(player.hasPermission("iSafe.drop.tnt")) {
                //access
            } else {
                if(event.getItemDrop().getItemStack().getTypeId() == 46) {
                    event.setCancelled(true);
                    player.sendMessage(ChatColor.RED + "You do not have access to drop that.");
                }
            }
        }
        if(!plugin.config.getBoolean("Drops.Allow-DiamonBlock-drop", true))
        {
            if(player.hasPermission("iSafe.drop.diamondblock")) {
                //access
            } else {
                if(event.getItemDrop().getItemStack().getTypeId() == 57) {
                    event.setCancelled(true);
                    player.sendMessage(ChatColor.RED + "You do not have access to drop that.");
                }
            }
        }
        if(!plugin.config.getBoolean("Drops.Allow-IronBlock-drop", true))
        {
            if(player.hasPermission("iSafe.drop.ironblock")) {
                //access
            } else {
                if(event.getItemDrop().getItemStack().getTypeId() == 42) {
                    event.setCancelled(true);
                    player.sendMessage(ChatColor.RED + "You do not have access to drop that.");
                }
            }
        }
        if(!plugin.config.getBoolean("Drops.Allow-GoldBlock-drop", true))
        {
            if(player.hasPermission("iSafe.drop.goldblock")) {
                //access
            } else {
                if(event.getItemDrop().getItemStack().getTypeId() == 41) {
                    event.setCancelled(true);
                    player.sendMessage(ChatColor.RED + "You do not have access to drop that.");
                }
            }
        }
        if(!plugin.config.getBoolean("Drops.Allow-MobSpawner-drop", true))
        {
            if(player.hasPermission("iSafe.drop.mobspawner")) {
                //access
            } else {
                if(event.getItemDrop().getItemStack().getTypeId() == 52) {
                    event.setCancelled(true);
                    player.sendMessage(ChatColor.RED + "You do not have access to drop that.");
                }
            }
        }
        if(!plugin.config.getBoolean("Drops.Allow-Dimondore-drop", true))
        {
            if(player.hasPermission("iSafe.drop.diamondore")) {
                //access
            } else {
                if(event.getItemDrop().getItemStack().getTypeId() == 52) {
                    event.setCancelled(true);
                    player.sendMessage(ChatColor.RED + "You do not have access to drop that.");
                }
            }
        }
        if(!plugin.config.getBoolean("Drops.Allow-Bedrock-drop", true))
        {
            if(player.hasPermission("iSafe.drop.bedrock")) {
                //access
            } else {
                if(event.getItemDrop().getItemStack().getTypeId() == 7) {
                    event.setCancelled(true);
                    player.sendMessage(ChatColor.RED + "You do not have access to drop that.");
                }
            }
        }
        if(!plugin.config.getBoolean("Drops.Allow-un_natural_portal-drop", true))
        {
            if(player.hasPermission("iSafe.drop.un-natural-portal")) {
                //access
            } else {
                if(event.getItemDrop().getItemStack().getTypeId() == 90) {
                    event.setCancelled(true);
                    player.sendMessage(ChatColor.RED + "You do not have access to drop that.");
                }
            }
        }
        if(!plugin.config.getBoolean("Drops.Allow-Diamondingot-drop", true))
        {
            if(player.hasPermission("iSafe.drop.diamondingot")) {
                //access
            } else {
                if(event.getItemDrop().getItemStack().getTypeId() == 264) {
                    event.setCancelled(true);
                    player.sendMessage(ChatColor.RED + "You do not have access to drop that.");
                }
            }
        }
    }

    @Override
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        if (event.isCancelled())
        {
            return;
        }
        
        Player player = event.getPlayer();
        Server server = player.getServer();
        World world = player.getWorld();
        
        if(!plugin.config.getBoolean("Player.Allow-Teleporting-without-iSafe-permissions", true))
        {
            if(player.hasPermission("iSafe.teleport")) {
                //access
            } else {
                event.setCancelled(true);
                player.sendMessage(ChatColor.RED + "You do not have access to teleport.");
            }
        }
    }

    @Override
    public void onPlayerChat(PlayerChatEvent event) {
        if (event.isCancelled())
        {
            return;
        }
        
        String msg = event.getMessage();
        Player player = event.getPlayer();
        World world = player.getWorld();
        Server server = player.getServer();
        
        if(plugin.config.getBoolean("Chat.Enable-Chat-permissions", true))
        {
           if(player.hasPermission("iSafe.chat")) {
               //access
           } else {
               event.setCancelled(true);
               player.sendMessage(ChatColor.RED + "You do not have access to chat.");
           }
        }
    }

    @Override
    public void onPlayerBedEnter(PlayerBedEnterEvent event) {
        if (event.isCancelled())
        {
            return;
        }
        
        Player player = event.getPlayer();
        World world = player.getWorld();
        Server server = player.getServer();
        
        if(plugin.config.getBoolean("Player.Enable-Bed-permissions", true))
        {
            if(player.hasPermission("iSafe.bed-enter")) {
                //access
            } else {
                event.setCancelled(true);
                player.sendMessage(ChatColor.RED + "You do not have access to sleep.");
            }
        }
    }

    @Override
    public void onPlayerKick(PlayerKickEvent event) {
        if (event.isCancelled())
        {
            return;
        }
        
        Player player = event.getPlayer();
        Server server = player.getServer();
        
        if(plugin.config.getBoolean("Misc.Enable-kick-messages", true))
        {
            server.broadcastMessage(ChatColor.DARK_GRAY + ((player.getName() + " got kicked.")));
        }
    }

    @Override
    public void onPlayerFish(PlayerFishEvent event) {
        if (event.isCancelled())
        {
            return;
        }
        
        Player player = event.getPlayer();
        Server server = player.getServer();
        World world = player.getWorld();
        
        if(plugin.config.getBoolean("Player.Enable-fishing-permissions", true))
        {
            if(player.hasPermission("iSafe.fish")) {
                //access
            } else {
                event.setCancelled(true);
                player.sendMessage(ChatColor.RED + "You do not have access to fish.");
            }
        }
    }

    @Override
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.isCancelled())
        {
            return;
        }
        
        Player player = event.getPlayer();
        Server server = player.getServer();
        World world = player.getWorld();
        Block block = event.getClickedBlock();
        
        if(!plugin.config.getBoolean("Player.Interact.Allow-Buttons-Interact", true))
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
        if(!plugin.config.getBoolean("Player.Interact.Allow-WoodenDoors-Interact", true))
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
        if(!plugin.config.getBoolean("Player.Interact.Allow-IronDoors-Interact", true))
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
        if(!plugin.config.getBoolean("Player.Interact.Allow-Levers-Interact", true))
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
        if(!plugin.config.getBoolean("Player.Interact.Allow-StonePressurePlate-Interact", true))
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
        if(!plugin.config.getBoolean("Player.Interact.Allow-WoodenPressurePlate-Interact", true))
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
        if(!plugin.config.getBoolean("Player.Interact.Allow-TrapDoor-Interact", true))
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
        if(!plugin.config.getBoolean("Player.Interact.Allow-WoodenFenceGate-Interact", true))
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
        if(!plugin.config.getBoolean("Player.Interact.Allow-Chest-Interact", true))
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
        if(!plugin.config.getBoolean("Misc.Prevent-crop-trampling", true))
        {
            if (event.getMaterial() == Material.SOIL && event.getPlayer() instanceof Player) {
                event.setCancelled(true);
            }
        }
    }

    @Override
    public void onPlayerLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        Server server = player.getServer();
        World world = player.getWorld();
        
        if(plugin.config.getBoolean("Player.Only-let-OPs-join", true))
        {
            if(!player.isOp()) {
                event.setResult(PlayerLoginEvent.Result.KICK_OTHER);
                event.setKickMessage("Only OPs can join this server.");
            }
        }
    }

    @Override
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        if (event.isCancelled())
        {
            return;
        }
        
        Player player = event.getPlayer();
        Server server = player.getServer();
        World world = player.getWorld();
        
        if(plugin.config.getBoolean("Player.Log-commands", true))
        {
            plugin.getServer().getLogger().info("[iSafe] [COMMAND] "+ event.getPlayer().getName() + ": " + event.getMessage());
        }
        
        if(plugin.config.getBoolean("Player.Disable-all-commands", true))
        {
            event.setCancelled(true);
            player.sendMessage(ChatColor.RED + "All commands are disabled.");
        }
    }

    @Override
    public void onPlayerPickupItem(PlayerPickupItemEvent event) {
        if (event.isCancelled())
        {
            return;
        }
        
        Player player = event.getPlayer();
        Server server = player.getServer();
        World world = player.getWorld();
        
        if(plugin.config.getBoolean("Pickup.Prevent-item-pickup", true))
        {
            if(player.hasPermission("iSafe.pickup")) {
               //access
           } else {
                event.setCancelled(true);
            }
        }
    }

    @Override
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        if (event.isCancelled())
        {
            return;
        }
        
        Player player = event.getPlayer();
        World world = player.getWorld();
        Server server = player.getServer();
        Entity entity = event.getPlayer();
        
        if(plugin.config.getBoolean("PlayerInteractEntity.Prevent-arrow-hitting-player", true))
        {
            if(entity.getEntityId() == 10) {
                event.setCancelled(true);
            }
        }
        if(plugin.config.getBoolean("PlayerInteractEntity.Prevent-snowball-hitting-player", true))
        {
            if(entity.getEntityId() == 11) {
                event.setCancelled(true);
            }
        }
    }

    @Override
    public void onPlayerGameModeChange(PlayerGameModeChangeEvent event) {
        if (event.isCancelled())
        {
            return;
        }
        
        Player player = event.getPlayer();
        World world = player.getWorld();
        Server server = player.getServer();
        
        if(plugin.config.getBoolean("Player.Prevent-Gamemode-change", true))
        {
            event.setCancelled(true);
        }
    }
}