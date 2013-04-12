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

import com.mrmag518.iSafe.Files.Blacklist;
import com.mrmag518.iSafe.Files.CreatureManager;
import com.mrmag518.iSafe.Files.Messages;
import com.mrmag518.iSafe.Util.Eco;
import com.mrmag518.iSafe.Util.Log;
import com.mrmag518.iSafe.Util.PermHandler;
import com.mrmag518.iSafe.iSafe;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

public class Blacklists implements Listener {
    public static iSafe plugin;
    public Blacklists(iSafe instance) {
        plugin = instance;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    public static int delay = 40;
    public static int currDelay = 2;
    
    /**
     * Used in certain conditions where an event is trigged many times in a short period.
     * Example: PlayerPickupItemEvent.
     */
    public static void startDelayTimer() {
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            @Override
            public void run() {
                if(currDelay > 0) {
                    currDelay--;
                }
            }
        }, 0, delay);
    }
    
    @EventHandler
    public void handlePlace(BlockPlaceEvent event) {
        if(event.isCancelled()) {
            return;
        }
        World world = event.getBlock().getWorld();
        FileConfiguration blacklist = Blacklist.getBlacklist(world.getName());
        
        if(blacklist.getBoolean("Events.Place.Enabled") != true) {
            return;
        }
        Block b = event.getBlock();
        int block_id = b.getTypeId();
        byte block_data = b.getData();
        boolean shallContinue = false;
        Player p = event.getPlayer();
        List<String> blacklisted = blacklist.getStringList("Events.Place.Blacklist");
        
        if(blacklisted.isEmpty() || blacklisted == null) {
            return;
        }
        
        if(p.getGameMode() == GameMode.SURVIVAL) {
            if(blacklist.getBoolean("Events.Place.Gamemode.ActiveFor.Survival") != true) {
                return;
            }
        } else if(p.getGameMode() == GameMode.CREATIVE) {
            if(blacklist.getBoolean("Events.Place.Gamemode.ActiveFor.Creative") != true) {
                return;
            }
        } else if(p.getGameMode() == GameMode.ADVENTURE) {
            if(blacklist.getBoolean("Events.Place.Gamemode.ActiveFor.Adventure") != true) {
                return;
            }
        }
        
        for (int i = 0; i < blacklisted.size(); i++) {
            String line = blacklisted.get(i);
            
            if(line == null) {
                continue;
            }
            int blacklisted_id = 0;
            byte blacklisted_data = 0;
            
            if(line.contains(":")) {
                String[] splitted = line.split(":");
                blacklisted_id = Integer.parseInt(splitted[0]);
                blacklisted_data = Byte.parseByte(splitted[1]);
            } else {
                blacklisted_id = Integer.parseInt(line);
            }
            
            if(blacklisted_id < 1 || blacklisted_data < 0) {
                continue;
            }
            
            if(blacklisted_data > 0) {
                if(block_id == blacklisted_id && block_data == blacklisted_data) {
                    shallContinue = true;
                    break;
                }
            } else {
                if(block_id == blacklisted_id) {
                    shallContinue = true;
                    break;
                }
            }
        }
        
        if(shallContinue) {
            if(!PermHandler.hasBlacklistPermission(p, "iSafe.blacklist.place.bypass.*")) {
                if(block_data < 1) {
                    if(!PermHandler.hasBlacklistPermission(p, "iSafe.blacklist.place.bypass." + block_id)) {
                        event.setCancelled(true);
                    } else {
                        return;
                    }
                } else {
                    if(!PermHandler.hasBlacklistPermission(p, "iSafe.blacklist.place.bypass." + block_id + ":" + block_data)) {
                        event.setCancelled(true);
                    } else {
                        return;
                    }
                }
            } else {
                return;
            }
            
            if(blacklist.getBoolean("Events.Place.Economy.Enabled")) {
                int withdrawAmount = blacklist.getInt("Events.Place.Economy.WithdrawAmount");
                
                Eco.withdraw(p.getName(), world, withdrawAmount);
                
                if(blacklist.getBoolean("Events.Place.Economy.NotifyPlayer")) {
                    Eco.sendEcoNotify(p, "Place", withdrawAmount);
                }
            }
            
            if(blacklist.getBoolean("Events.Place.Penalities.KickPlayer")) {
                p.kickPlayer(Messages.scan(Messages.getMessages().getString("Blacklists.Place.KickMessage"), p, null, b.getType().name(), world));
                return;
            }
            
            if(blacklist.getBoolean("Events.Place.Report.ToConsole")) {
                Log.info(p.getName() + " attempted to place " + b.getType().name().toLowerCase() + " in world " + world.getName());
            }
            
            if(blacklist.getBoolean("Events.Place.Report.ToPlayer")) {
                p.sendMessage(Messages.scan(Messages.getMessages().getString("Blacklists.Place.DisallowedMessage"), p, null, b.getType().name(), world));
            }
        }
    }
    
    @EventHandler
    public void handleBreak(BlockBreakEvent event) {
        if(event.isCancelled()) {
            return;
        }
        World world = event.getBlock().getWorld();
        FileConfiguration blacklist = Blacklist.getBlacklist(world.getName());
        
        if(blacklist.getBoolean("Events.Break.Enabled") != true) {
            return;
        }
        Block b = event.getBlock();
        int block_id = b.getTypeId();
        byte block_data = b.getData();
        boolean shallContinue = false;
        Player p = event.getPlayer();
        List<String> blacklisted = blacklist.getStringList("Events.Break.Blacklist");
        
        if(blacklisted.isEmpty() || blacklisted == null) {
            return;
        }
        
        if(p.getGameMode() == GameMode.SURVIVAL) {
            if(blacklist.getBoolean("Events.Break.Gamemode.ActiveFor.Survival") != true) {
                return;
            }
        } else if(p.getGameMode() == GameMode.CREATIVE) {
            if(blacklist.getBoolean("Events.Break.Gamemode.ActiveFor.Creative") != true) {
                return;
            }
        } else if(p.getGameMode() == GameMode.ADVENTURE) {
            if(blacklist.getBoolean("Events.Break.Gamemode.ActiveFor.Adventure") != true) {
                return;
            }
        }
        
        for (int i = 0; i < blacklisted.size(); i++) {
            String line = blacklisted.get(i);
            
            if(line == null) {
                continue;
            }
            int blacklisted_id = 0;
            byte blacklisted_data = 0;
            
            if(line.contains(":")) {
                String[] splitted = line.split(":");
                blacklisted_id = Integer.parseInt(splitted[0]);
                blacklisted_data = Byte.parseByte(splitted[1]);
            } else {
                blacklisted_id = Integer.parseInt(line);
            }
            
            if(blacklisted_id < 1 || blacklisted_data < 0) {
                continue;
            }
            
            if(blacklisted_data > 0) {
                if(block_id == blacklisted_id && block_data == blacklisted_data) {
                    shallContinue = true;
                    break;
                }
            } else {
                if(block_id == blacklisted_id) {
                    shallContinue = true;
                    break;
                }
            }
        }
        
        if(shallContinue) {
            if(!PermHandler.hasBlacklistPermission(p, "iSafe.blacklist.break.bypass.*")) {
                if(block_data < 1) {
                    if(!PermHandler.hasBlacklistPermission(p, "iSafe.blacklist.break.bypass." + block_id)) {
                        event.setCancelled(true);
                    } else {
                        return;
                    }
                } else {
                    if(!PermHandler.hasBlacklistPermission(p, "iSafe.blacklist.break.bypass." + block_id + ":" + block_data)) {
                        event.setCancelled(true);
                    } else {
                        return;
                    }
                }
            } else {
                return;
            }
            
            if(blacklist.getBoolean("Events.Break.Economy.Enabled")) {
                int withdrawAmount = blacklist.getInt("Events.Break.Economy.WithdrawAmount");
                
                Eco.withdraw(p.getName(), world, withdrawAmount);
                
                if(blacklist.getBoolean("Events.Break.Economy.NotifyPlayer")) {
                    Eco.sendEcoNotify(p, "Break", withdrawAmount);
                }
            }
            
            if(blacklist.getBoolean("Events.Break.Penalities.KickPlayer")) {
                p.kickPlayer(Messages.scan(Messages.getMessages().getString("Blacklists.Break.KickMessage"), p, null, b.getType().name(), world));
                return;
            }
            
            if(blacklist.getBoolean("Events.Break.Report.ToConsole")) {
                Log.info(p.getName() + " attempted to break " + b.getType().name().toLowerCase() + " in world " + world.getName());
            }
            
            if(blacklist.getBoolean("Events.Break.Report.ToPlayer")) {
                p.sendMessage(Messages.scan(Messages.getMessages().getString("Blacklists.Break.DisallowedMessage"), p, null, b.getType().name(), world));
            }
        }
    }
    
    @EventHandler
    public void handleDrop(PlayerDropItemEvent event) {
        if(event.isCancelled()) {
            return;
        }
        World world = event.getItemDrop().getWorld();
        FileConfiguration blacklist = Blacklist.getBlacklist(world.getName());
        
        if(blacklist.getBoolean("Events.Drop.Enabled") != true) {
            return;
        }
        ItemStack is = event.getItemDrop().getItemStack();
        int item_id = is.getTypeId();
        byte item_data = is.getData().getData();
        boolean shallContinue = false;
        Player p = event.getPlayer();
        List<String> blacklisted = blacklist.getStringList("Events.Drop.Blacklist");
        
        if(blacklisted.isEmpty() || blacklisted == null) {
            return;
        }
        
        if(p.getGameMode() == GameMode.SURVIVAL) {
            if(blacklist.getBoolean("Events.Drop.Gamemode.ActiveFor.Survival") != true) {
                return;
            }
        } else if(p.getGameMode() == GameMode.CREATIVE) {
            if(blacklist.getBoolean("Events.Drop.Gamemode.ActiveFor.Creative") != true) {
                return;
            }
        } else if(p.getGameMode() == GameMode.ADVENTURE) {
            if(blacklist.getBoolean("Events.Drop.Gamemode.ActiveFor.Adventure") != true) {
                return;
            }
        }
        
        for (int i = 0; i < blacklisted.size(); i++) {
            String line = blacklisted.get(i);
            
            if(line == null) {
                continue;
            }
            int blacklisted_id = 0;
            byte blacklisted_data = 0;
            
            if(line.contains(":")) {
                String[] splitted = line.split(":");
                blacklisted_id = Integer.parseInt(splitted[0]);
                blacklisted_data = Byte.parseByte(splitted[1]);
            } else {
                blacklisted_id = Integer.parseInt(line);
            }
            
            if(blacklisted_id < 1 || blacklisted_data < 0) {
                continue;
            }
            
            if(blacklisted_data > 0) {
                if(item_id == blacklisted_id && item_data == blacklisted_data) {
                    shallContinue = true;
                    break;
                }
            } else {
                if(item_id == blacklisted_id) {
                    shallContinue = true;
                    break;
                }
            }
        }
        
        if(shallContinue) {
            if(!PermHandler.hasBlacklistPermission(p, "iSafe.blacklist.drop.bypass.*")) {
                if(item_data < 1) {
                    if(!PermHandler.hasBlacklistPermission(p, "iSafe.blacklist.drop.bypass." + item_id)) {
                        event.setCancelled(true);
                    } else {
                        return;
                    }
                } else {
                    if(!PermHandler.hasBlacklistPermission(p, "iSafe.blacklist.drop.bypass." + item_id + ":" + item_data)) {
                        event.setCancelled(true);
                    } else {
                        return;
                    }
                }
            } else {
                return;
            }
            
            if(blacklist.getBoolean("Events.Drop.Economy.Enabled")) {
                int withdrawAmount = blacklist.getInt("Events.Drop.Economy.WithdrawAmount");
                
                Eco.withdraw(p.getName(), world, withdrawAmount);
                
                if(blacklist.getBoolean("Events.Drop.Economy.NotifyPlayer")) {
                    Eco.sendEcoNotify(p, "Drop", withdrawAmount);
                }
            }
            
            if(blacklist.getBoolean("Events.Drop.Penalities.KickPlayer")) {
                p.kickPlayer(Messages.scan(Messages.getMessages().getString("Blacklists.Drop.KickMessage"), p, null, is.getType().name(), world));
                return;
            }
            
            if(blacklist.getBoolean("Events.Drop.Report.ToConsole")) {
                Log.info(p.getName() + " attempted to drop " + is.getType().name().toLowerCase() + " in world " + world.getName());
            }
            
            if(blacklist.getBoolean("Events.Drop.Report.ToPlayer")) {
                p.sendMessage(Messages.scan(Messages.getMessages().getString("Blacklists.Drop.DisallowedMessage"), p, null, is.getType().name(), world));
            }
        }
    }
    
    @EventHandler
    public void handlePickup(PlayerPickupItemEvent event) {
        if(event.isCancelled()) {
            return;
        }
        World world = event.getItem().getWorld();
        FileConfiguration blacklist = Blacklist.getBlacklist(world.getName());
        
        if(blacklist.getBoolean("Events.Pickup.Enabled") != true) {
            return;
        }
        ItemStack is = event.getItem().getItemStack();
        int item_id = is.getTypeId();
        byte item_data = is.getData().getData();
        boolean shallContinue = false;
        Player p = event.getPlayer();
        List<String> blacklisted = blacklist.getStringList("Events.Pickup.Blacklist");
        
        if(blacklisted.isEmpty() || blacklisted == null) {
            return;
        }
        
        if(p.getGameMode() == GameMode.SURVIVAL) {
            if(blacklist.getBoolean("Events.Pickup.Gamemode.ActiveFor.Survival") != true) {
                return;
            }
        } else if(p.getGameMode() == GameMode.CREATIVE) {
            if(blacklist.getBoolean("Events.Pickup.Gamemode.ActiveFor.Creative") != true) {
                return;
            }
        } else if(p.getGameMode() == GameMode.ADVENTURE) {
            if(blacklist.getBoolean("Events.Pickup.Gamemode.ActiveFor.Adventure") != true) {
                return;
            }
        }
        
        for (int i = 0; i < blacklisted.size(); i++) {
            String line = blacklisted.get(i);
            
            if(line == null) {
                continue;
            }
            int blacklisted_id = 0;
            byte blacklisted_data = 0;
            
            if(line.contains(":")) {
                String[] splitted = line.split(":");
                blacklisted_id = Integer.parseInt(splitted[0]);
                blacklisted_data = Byte.parseByte(splitted[1]);
            } else {
                blacklisted_id = Integer.parseInt(line);
            }
            
            if(blacklisted_id < 1 || blacklisted_data < 0) {
                continue;
            }
            
            if(blacklisted_data > 0) {
                    if(item_id == blacklisted_id && item_data == blacklisted_data) {
                        shallContinue = true;
                        break;
                    }
                } else {
                    if(item_id == blacklisted_id) {
                        shallContinue = true;
                        break;
                    }
                }
        }
        
        if(shallContinue) {
            if(!PermHandler.hasBlacklistPermission(p, "iSafe.blacklist.pickup.bypass.*")) {
                if(item_data < 1) {
                    if(!PermHandler.hasBlacklistPermission(p, "iSafe.blacklist.pickup.bypass." + item_id)) {
                        event.setCancelled(true);
                    } else {
                        return;
                    }
                } else {
                    if(!PermHandler.hasBlacklistPermission(p, "iSafe.blacklist.pickup.bypass." + item_id + ":" + item_data)) {
                        event.setCancelled(true);
                    } else {
                        return;
                    }
                }
            } else {
                return;
            }
            
            if(currDelay != 0) {
                return;
            } else {
                currDelay = 2;
            }
            
            if(blacklist.getBoolean("Events.Pickup.Economy.Enabled")) {
                int withdrawAmount = blacklist.getInt("Events.Pickup.Economy.WithdrawAmount");
                
                Eco.withdraw(p.getName(), world, withdrawAmount);
                
                if(blacklist.getBoolean("Events.Pickup.Economy.NotifyPlayer")) {
                    Eco.sendEcoNotify(p, "Pickup", withdrawAmount);
                }
            }
            
            if(blacklist.getBoolean("Events.Pickup.Penalities.KickPlayer")) {
                p.kickPlayer(Messages.scan(Messages.getMessages().getString("Blacklists.Pickup.KickMessage"), p, null, is.getType().name(), world));
                return;
            }
            
            if(blacklist.getBoolean("Events.Pickup.Report.ToConsole")) {
                Log.info(p.getName() + " attempted to pcikup " + is.getType().name().toLowerCase() + " in world " + world.getName());
            }
            
            if(blacklist.getBoolean("Events.Pickup.Report.ToPlayer")) {
                p.sendMessage(Messages.scan(Messages.getMessages().getString("Blacklists.Pickup.DisallowedMessage"), p, null, is.getType().name(), world));
            }
        }
    }
    
    @EventHandler
    public void handleCommand(PlayerCommandPreprocessEvent event) {
        if(event.isCancelled()) {
            return;
        }
        World world = event.getPlayer().getWorld();
        FileConfiguration blacklist = Blacklist.getBlacklist(world.getName());
        
        if(blacklist.getBoolean("Events.Command.Enabled") != true) {
            return;
        }
        String cmd = event.getMessage().toLowerCase();
        boolean shallContinue = false;
        Player p = event.getPlayer();
        List<String> blacklisted = blacklist.getStringList("Events.Command.Blacklist");
        
        if(blacklisted.isEmpty() || blacklisted == null) {
            return;
        }
        
        if(p.getGameMode() == GameMode.SURVIVAL) {
            if(blacklist.getBoolean("Events.Command.Gamemode.ActiveFor.Survival") != true) {
                return;
            }
        } else if(p.getGameMode() == GameMode.CREATIVE) {
            if(blacklist.getBoolean("Events.Command.Gamemode.ActiveFor.Creative") != true) {
                return;
            }
        } else if(p.getGameMode() == GameMode.ADVENTURE) {
            if(blacklist.getBoolean("Events.Command.Gamemode.ActiveFor.Adventure") != true) {
                return;
            }
        }
        
        for (int i = 0; i < blacklisted.size(); i++) {
            String line = blacklisted.get(i).toLowerCase();
            
            if(line == null) {
                continue;
            }
            
            if(cmd.startsWith(line)) {
                shallContinue = true;
                break;
            }
        }
        
        if(shallContinue) {
            if(!PermHandler.hasBlacklistPermission(p, "iSafe.blacklist.command.bypass.*")) {
                if(!PermHandler.hasBlacklistPermission(p, "iSafe.blacklist.command.bypass." + cmd)) {
                    event.setCancelled(true);
                } else {
                    return;
                }
            } else {
                return;
            }
            
            if(blacklist.getBoolean("Events.Command.Economy.Enabled")) {
                int withdrawAmount = blacklist.getInt("Events.Command.Economy.WithdrawAmount");
                
                Eco.withdraw(p.getName(), world, withdrawAmount);
                
                if(blacklist.getBoolean("Events.Command.Economy.NotifyPlayer")) {
                    Eco.sendEcoNotify(p, "Command", withdrawAmount);
                }
            }
            
            if(blacklist.getBoolean("Events.Command.Penalities.KickPlayer")) {
                p.kickPlayer(Messages.scan(Messages.getMessages().getString("Blacklists.Command.KickMessage"), p, cmd, null, world));
                return;
            }
            
            if(blacklist.getBoolean("Events.Command.Report.ToConsole")) {
                Log.info(p.getName() + " attempted to do the command " + cmd + " in world " + world.getName());
            }
            
            if(blacklist.getBoolean("Events.Command.Report.ToPlayer")) {
                p.sendMessage(Messages.scan(Messages.getMessages().getString("Blacklists.Command.DisallowedMessage"), p, cmd, null, world));
            }
        }
    }
    
    /*@EventHandler
    public void handleSign(SignChangeEvent event) {
        if(event.isCancelled()) {
            return;
        }
        World world = event.getPlayer().getWorld();
        FileConfiguration blacklist = Blacklist.getBlacklist(world.getName());
        
        if(blacklist.getBoolean("Events.SignChange.Enabled") != true) {
            return;
        }
        boolean shallContinue = false;
        Player p = event.getPlayer();
        List<String> blacklisted = blacklist.getStringList("Events.SignChange.Blacklist");
        List<String> whitelisted = blacklist.getStringList("Events.SignChange.Whitelist");
        
        if(p.getGameMode() == GameMode.SURVIVAL) {
            if(blacklist.getBoolean("Events.SignChange.Gamemode.ActiveFor.Survival") != true) {
                return;
            }
        } else if(p.getGameMode() == GameMode.CREATIVE) {
            if(blacklist.getBoolean("Events.SignChange.Gamemode.ActiveFor.Creative") != true) {
                return;
            }
        } else if(p.getGameMode() == GameMode.ADVENTURE) {
            if(blacklist.getBoolean("Events.SignChange.Gamemode.ActiveFor.Adventure") != true) {
                return;
            }
        }
        String bWord = "";
        
        for(String stringLine : event.getLines()) {
            if(stringLine == null) {
                continue;
            }
            
            for (int i = 0; i < whitelisted.size(); i++) {
                String line = whitelisted.get(i).toLowerCase();
                
                if(line == null) {
                    continue;
                }
                
                if(stringLine.contains(line)) {
                    return;
                }
            }
            
            for (int i = 0; i < blacklisted.size(); i++) {
                String line = blacklisted.get(i).toLowerCase();

                if(line == null) {
                    continue;
                }
                String temp = stringLine;

                if(blacklist.getBoolean("Events.SignChange.CheckSettings.RemoveSpaces")) {
                    if(stringLine.contains(" ")) {
                        temp = stringLine.replaceAll(" ", "");
                    }
                }

                if(blacklist.getBoolean("Events.SignChange.CheckSettings.RemovePeriods")) {
                    if(stringLine.contains(".")) {
                        temp = stringLine.replaceAll("\\.", "");
                    }
                }

                if(blacklist.getBoolean("Events.SignChange.CheckSettings.RemoveExclamations")) {
                    if(stringLine.contains("!")) {
                        temp = stringLine.replaceAll("!", "");
                    }
                }

                if(blacklist.getBoolean("Events.SignChange.CheckSettings.RemoveQuestonMarks")) {
                    if(stringLine.contains("?")) {
                        temp = stringLine.replaceAll("\\?", "");
                    }
                }

                if(blacklist.getBoolean("Events.SignChange.CheckSettings.SeeNumbersAsLetters")) {
                    if(stringLine.contains("0")) {
                        temp = stringLine.replaceAll("0", "o");
                    }
                    if(stringLine.contains("1")) {
                        temp = stringLine.replaceAll("1", "i");
                    }
                    if(stringLine.contains("3")) {
                        temp = stringLine.replaceAll("3", "e");
                    }
                    if(stringLine.contains("4")) {
                        temp = stringLine.replaceAll("4", "a");
                    }
                    if(stringLine.contains("5")) {
                        temp = stringLine.replaceAll("5", "s");
                    }
                    if(stringLine.contains("6")) {
                        temp = stringLine.replaceAll("6", "b");
                    }
                    if(stringLine.contains("7")) {
                        temp = stringLine.replaceAll("7", "t");
                    }
                }

                if(temp.contains(line)) {
                    shallContinue = true;
                    bWord = line;
                    break;
                }
            }
        }
        
        if(shallContinue) {
            if(!PermHandler.hasBlacklistPermission(p, "iSafe.blacklist.signchange.bypass.*")) {
                if(!PermHandler.hasBlacklistPermission(p, "iSafe.blacklist.signchange.bypass." + bWord)) {
                    event.setCancelled(true);
                } else {
                    return;
                }
            } else {
                return;
            }
            
            if(blacklist.getBoolean("Events.SignChange.Economy.Enabled")) {
                int withdrawAmount = blacklist.getInt("Events.SignChange.Economy.WithdrawAmount");
                
                Eco.withdraw(p.getName(), world, withdrawAmount);
                
                if(blacklist.getBoolean("Events.SignChange.Economy.NotifyPlayer")) {
                    Eco.sendEcoNotify(p, "SignChange", withdrawAmount);
                }
            }
            
            if(blacklist.getBoolean("Events.SignChange.Penalities.KickPlayer")) {
                p.kickPlayer(Messages.scan(Messages.getMessages().getString("Blacklists.SignChange.KickMessage"), p, bWord, null, world));
                return;
            }
            
            if(blacklist.getBoolean("Events.SignChange.Report.ToConsole")) {
                Log.info(p.getName() + " attempted to modify a sign to contain the word " + bWord + " in world " + world.getName());
            }
            
            if(blacklist.getBoolean("Events.SignChange.Report.ToPlayer")) {
                p.sendMessage(Messages.scan(Messages.getMessages().getString("Blacklists.SignChange.DisallowedMessage"), p, bWord, null, world));
            }
        }
    }*/
    
    @EventHandler
    public void handleChat(AsyncPlayerChatEvent event) {
        if(event.isCancelled()) {
            return;
        }
        World world = event.getPlayer().getWorld();
        FileConfiguration blacklist = Blacklist.getBlacklist(world.getName());
        
        if(blacklist.getBoolean("Events.Chat.Enabled") != true) {
            return;
        }
        String sentence = event.getMessage().toLowerCase();
        boolean shallContinue = false;
        Player p = event.getPlayer();
        List<String> blacklisted = blacklist.getStringList("Events.Chat.Blacklist");
        List<String> whitelisted = blacklist.getStringList("Events.Chat.Whitelist");
        
        if(blacklisted.isEmpty() || blacklisted == null) {
            return;
        }
        
        if(p.getGameMode() == GameMode.SURVIVAL) {
            if(blacklist.getBoolean("Events.Chat.Gamemode.ActiveFor.Survival") != true) {
                return;
            }
        } else if(p.getGameMode() == GameMode.CREATIVE) {
            if(blacklist.getBoolean("Events.Chat.Gamemode.ActiveFor.Creative") != true) {
                return;
            }
        } else if(p.getGameMode() == GameMode.ADVENTURE) {
            if(blacklist.getBoolean("Events.Chat.Gamemode.ActiveFor.Adventure") != true) {
                return;
            }
        }
        
        for (int i = 0; i < whitelisted.size(); i++) {
            String line = whitelisted.get(i).toLowerCase();
            
            if(line == null) {
                continue;
            }
            
            if(sentence.contains(line)) {
                return;
            }
        }
        String bWord = "";
        
        for (int i = 0; i < blacklisted.size(); i++) {
            String line = blacklisted.get(i).toLowerCase();
            
            if(line == null) {
                continue;
            }
            String temp = sentence;
            
            if(blacklist.getBoolean("Events.Chat.CheckSettings.RemoveSpaces")) {
                if(sentence.contains(" ")) {
                    temp = sentence.replaceAll(" ", "");
                }
            }
            
            if(blacklist.getBoolean("Events.Chat.CheckSettings.RemovePeriods")) {
                if(sentence.contains(".")) {
                    temp = sentence.replaceAll("\\.", "");
                }
            }
            
            if(blacklist.getBoolean("Events.Chat.CheckSettings.RemoveExclamations")) {
                if(sentence.contains("!")) {
                    temp = sentence.replaceAll("!", "");
                }
            }
            
            if(blacklist.getBoolean("Events.Chat.CheckSettings.RemoveQuestonMarks")) {
                if(sentence.contains("?")) {
                    temp = sentence.replaceAll("\\?", "");
                }
            }
            
            if(blacklist.getBoolean("Events.Chat.CheckSettings.SeeNumbersAsLetters")) {
                if(sentence.contains("0")) {
                    temp = sentence.replaceAll("0", "o");
                }
                if(sentence.contains("1")) {
                    temp = sentence.replaceAll("1", "i");
                }
                if(sentence.contains("3")) {
                    temp = sentence.replaceAll("3", "e");
                }
                if(sentence.contains("4")) {
                    temp = sentence.replaceAll("4", "a");
                }
                if(sentence.contains("5")) {
                    temp = sentence.replaceAll("5", "s");
                }
                if(sentence.contains("6")) {
                    temp = sentence.replaceAll("6", "b");
                }
                if(sentence.contains("7")) {
                    temp = sentence.replaceAll("7", "t");
                }
            }
            
            if(temp.contains(line)) {
                shallContinue = true;
                bWord = line;
                break;
            }
        }
        
        if(shallContinue) {
            if(!PermHandler.hasBlacklistPermission(p, "iSafe.blacklist.chat.bypass.*")) {
                if(!PermHandler.hasBlacklistPermission(p, "iSafe.blacklist.chat.bypass." + bWord)) {
                    event.setCancelled(true);
                } else {
                    return;
                }
            } else {
                return;
            }
            
            if(blacklist.getBoolean("Events.Chat.Economy.Enabled")) {
                int withdrawAmount = blacklist.getInt("Events.Chat.Economy.WithdrawAmount");
                
                Eco.withdraw(p.getName(), world, withdrawAmount);
                
                if(blacklist.getBoolean("Events.Chat.Economy.NotifyPlayer")) {
                    Eco.sendEcoNotify(p, "Chat", withdrawAmount);
                }
            }
            
            if(blacklist.getBoolean("Events.Chat.Penalities.KickPlayer")) {
                p.kickPlayer(Messages.scan(Messages.getMessages().getString("Blacklists.Chat.KickMessage"), p, bWord, null, world));
                return;
            }
            
            if(blacklist.getBoolean("Events.Chat.Report.ToConsole")) {
                Log.info(p.getName() + " attempted to chat the word " + bWord + " in world " + world.getName());
            }
            
            if(blacklist.getBoolean("Events.Chat.Report.ToPlayer")) {
                p.sendMessage(Messages.scan(Messages.getMessages().getString("Blacklists.Chat.DisallowedMessage"), p, bWord, null, world));
            }
        }
    }
    
    @EventHandler
    public void handleInteract(PlayerInteractEvent event) {
        if(event.isCancelled()) {
            return;
        }
        Action a = event.getAction();
        
        if(a == Action.LEFT_CLICK_AIR || a == Action.LEFT_CLICK_BLOCK || a == Action.RIGHT_CLICK_AIR || a == Action.RIGHT_CLICK_BLOCK || a == Action.PHYSICAL) {
            World world = event.getPlayer().getWorld();
            FileConfiguration blacklist = Blacklist.getBlacklist(world.getName());
            
            if(blacklist.getBoolean("Events.Interact.Enabled") != true) {
                return;
            }
            Block b = event.getClickedBlock();
            ItemStack is = event.getItem();
            int item_id = 0;
            byte item_data = 0;
            int block_id = b.getTypeId();
            byte block_data = b.getData();
            boolean shallContinue = false;
            Player p = event.getPlayer();
            List<String> block_blacklisted = blacklist.getStringList("Events.Interact.BlockBlacklist");
            List<String> hand_blacklisted = blacklist.getStringList("Events.Interact.ItemBlacklist");
            
            if(is != null) {
                item_id = is.getTypeId();
                item_data = is.getData().getData();
            }
            
            if(block_blacklisted.isEmpty() || block_blacklisted == null && hand_blacklisted.isEmpty() || hand_blacklisted == null) {
                return;
            }

            if(p.getGameMode() == GameMode.SURVIVAL) {
                if(blacklist.getBoolean("Events.Interact.Gamemode.ActiveFor.Survival") != true) {
                    return;
                }
            } else if(p.getGameMode() == GameMode.CREATIVE) {
                if(blacklist.getBoolean("Events.Interact.Gamemode.ActiveFor.Creative") != true) {
                    return;
                }
            } else if(p.getGameMode() == GameMode.ADVENTURE) {
                if(blacklist.getBoolean("Events.Interact.Gamemode.ActiveFor.Adventure") != true) {
                    return;
                }
            }

            for (int i = 0; i < block_blacklisted.size(); i++) {
                String line = block_blacklisted.get(i);

                if(line == null) {
                    continue;
                }
                int blacklisted_id = 0;
                byte blacklisted_data = 0;

                if(line.contains(":")) {
                    String[] splitted = line.split(":");
                    blacklisted_id = Integer.parseInt(splitted[0]);
                    blacklisted_data = Byte.parseByte(splitted[1]);
                } else {
                    blacklisted_id = Integer.parseInt(line);
                }

                if(blacklisted_id < 1 || blacklisted_data < 0) {
                    continue;
                }
                
                if(blacklisted_data > 0) {
                    if(block_id == blacklisted_id && block_data == blacklisted_data) {
                        shallContinue = true;
                        break;
                    }
                } else {
                    if(block_id == blacklisted_id) {
                        shallContinue = true;
                        break;
                    }
                }
            }

            if(shallContinue) {
                if(!PermHandler.hasBlacklistPermission(p, "iSafe.blacklist.interact.bypass.*")) {
                    if(block_data < 1) {
                        if(!PermHandler.hasBlacklistPermission(p, "iSafe.blacklist.interact.bypass." + block_id)) {
                            event.setCancelled(true);
                        } else {
                            return;
                        }
                    } else {
                        if(!PermHandler.hasBlacklistPermission(p, "iSafe.blacklist.interact.bypass." + block_id + ":" + block_data)) {
                            event.setCancelled(true);
                        } else {
                            return;
                        }
                    }
                } else {
                    return;
                }

                if(blacklist.getBoolean("Events.Interact.Economy.Enabled")) {
                    int withdrawAmount = blacklist.getInt("Events.Interact.Economy.WithdrawAmount");

                    Eco.withdraw(p.getName(), world, withdrawAmount);

                    if(blacklist.getBoolean("Events.Interact.Economy.NotifyPlayer")) {
                        Eco.sendEcoNotify(p, "Interact", withdrawAmount);
                    }
                }

                if(blacklist.getBoolean("Events.Interact.Penalities.KickPlayer")) {
                    p.kickPlayer(Messages.scan(Messages.getMessages().getString("Blacklists.Interact.KickMessage"), p, null, b.getType().name(), world));
                    return;
                }

                if(blacklist.getBoolean("Events.Interact.Report.ToConsole")) {
                    Log.info(p.getName() + " attempted to interact with " + b.getType().name().toLowerCase() + " in world " + world.getName());
                }

                if(blacklist.getBoolean("Events.Interact.Report.ToPlayer")) {
                    p.sendMessage(Messages.scan(Messages.getMessages().getString("Blacklists.Interact.DisallowedMessage"), p, null, b.getType().name(), world));
                }
            } else {
                if(p.getItemInHand() == null) {
                    return;
                }
                
                for (int i = 0; i < hand_blacklisted.size(); i++) {
                    String line = hand_blacklisted.get(i);

                    if(line == null) {
                        continue;
                    }
                    int blacklisted_id = 0;
                    byte blacklisted_data = 0;

                    if(line.contains(":")) {
                        String[] splitted = line.split(":");
                        blacklisted_id = Integer.parseInt(splitted[0]);
                        blacklisted_data = Byte.parseByte(splitted[1]);
                    } else {
                        blacklisted_id = Integer.parseInt(line);
                    }

                    if(blacklisted_id < 1 || blacklisted_data < 0) {
                        continue;
                    }

                    if(blacklisted_data > 0) {
                        if(item_id == blacklisted_id && item_data == blacklisted_data) {
                            shallContinue = true;
                            break;
                        }
                    } else {
                        if(item_id == blacklisted_id) {
                            shallContinue = true;
                            break;
                        }
                    }
                }
                
                if(shallContinue) {
                    if(!PermHandler.hasBlacklistPermission(p, "iSafe.blacklist.interact.bypass.*")) {
                        if(item_data < 1) {
                            if(!PermHandler.hasBlacklistPermission(p, "iSafe.blacklist.interact.bypass." + item_id)) {
                                event.setCancelled(true);
                            } else {
                                return;
                            }
                        } else {
                            if(!PermHandler.hasBlacklistPermission(p, "iSafe.blacklist.interact.bypass." + item_id + ":" + item_data)) {
                                event.setCancelled(true);
                            } else {
                                return;
                            }
                        }
                    } else {
                        return;
                    }

                    if(blacklist.getBoolean("Events.Interact.Economy.Enabled")) {
                        int withdrawAmount = blacklist.getInt("Events.Interact.Economy.WithdrawAmount");

                        Eco.withdraw(p.getName(), world, withdrawAmount);

                        if(blacklist.getBoolean("Events.Interact.Economy.NotifyPlayer")) {
                            Eco.sendEcoNotify(p, "Interact", withdrawAmount);
                        }
                    }

                    if(blacklist.getBoolean("Events.Interact.Penalities.KickPlayer")) {
                        p.kickPlayer(Messages.scan(Messages.getMessages().getString("Blacklists.Interact.KickMessage"), p, null, is.getType().name(), world));
                        return;
                    }

                    if(blacklist.getBoolean("Events.Interact.Report.ToConsole")) {
                        Log.info(p.getName() + " attempted to interact with " + is.getType().name().toLowerCase() + " in world " + world.getName());
                    }

                    if(blacklist.getBoolean("Events.Interact.Report.ToPlayer")) {
                        p.sendMessage(Messages.scan(Messages.getMessages().getString("Blacklists.Interact.DisallowedMessage"), p, null, is.getType().name(), world));
                    }
                }
            }
        }
    }
    
    @EventHandler
    public void handleCrafting(CraftItemEvent event) {
        if(event.isCancelled()) {
            return;
        }
        World world = event.getWhoClicked().getWorld();
        FileConfiguration blacklist = Blacklist.getBlacklist(world.getName());
        
        if(blacklist.getBoolean("Events.Crafting.Enabled") != true) {
            return;
        }
        ItemStack is = event.getCurrentItem();
        int item_id = is.getTypeId();
        byte item_data = is.getData().getData();
        boolean shallContinue = false;
        Player p = (Player)event.getWhoClicked();
        List<String> blacklisted = blacklist.getStringList("Events.Crafting.Blacklist");
        
        if(blacklisted.isEmpty() || blacklisted == null) {
            return;
        }
        
        if(p.getGameMode() == GameMode.SURVIVAL) {
            if(blacklist.getBoolean("Events.Crafting.Gamemode.ActiveFor.Survival") != true) {
                return;
            }
        } else if(p.getGameMode() == GameMode.CREATIVE) {
            if(blacklist.getBoolean("Events.Crafting.Gamemode.ActiveFor.Creative") != true) {
                return;
            }
        } else if(p.getGameMode() == GameMode.ADVENTURE) {
            if(blacklist.getBoolean("Events.Crafting.Gamemode.ActiveFor.Adventure") != true) {
                return;
            }
        }
        
        for (int i = 0; i < blacklisted.size(); i++) {
            String line = blacklisted.get(i);
            
            if(line == null) {
                continue;
            }
            int blacklisted_id = 0;
            byte blacklisted_data = 0;
            
            if(line.contains(":")) {
                String[] splitted = line.split(":");
                blacklisted_id = Integer.parseInt(splitted[0]);
                blacklisted_data = Byte.parseByte(splitted[1]);
            } else {
                blacklisted_id = Integer.parseInt(line);
            }
            
            if(blacklisted_id < 1 || blacklisted_data < 0) {
                continue;
            }
            
            if(blacklisted_data > 0) {
                if(item_id == blacklisted_id && item_data == blacklisted_data) {
                    shallContinue = true;
                    break;
                }
            } else {
                if(item_id == blacklisted_id) {
                    shallContinue = true;
                    break;
                }
            }
        }
        
        if(shallContinue) {
            if(!PermHandler.hasBlacklistPermission(p, "iSafe.blacklist.crafting.bypass.*")) {
                if(item_data < 1) {
                    if(!PermHandler.hasBlacklistPermission(p, "iSafe.blacklist.crafting.bypass." + item_id)) {
                        event.setCancelled(true);
                    } else {
                        return;
                    }
                } else {
                    if(!PermHandler.hasBlacklistPermission(p, "iSafe.blacklist.crafting.bypass." + item_id + ":" + item_data)) {
                        event.setCancelled(true);
                    } else {
                        return;
                    }
                }
            } else {
                return;
            }
            
            if(blacklist.getBoolean("Events.Crafting.Economy.Enabled")) {
                int withdrawAmount = blacklist.getInt("Events.Crafting.Economy.WithdrawAmount");
                
                Eco.withdraw(p.getName(), world, withdrawAmount);
                
                if(blacklist.getBoolean("Events.Crafting.Economy.NotifyPlayer")) {
                    Eco.sendEcoNotify(p, "Crafting", withdrawAmount);
                }
            }
            
            if(blacklist.getBoolean("Events.Crafting.Penalities.KickPlayer")) {
                p.kickPlayer(Messages.scan(Messages.getMessages().getString("Blacklists.Crafting.KickMessage"), p, null, is.getType().name(), world));
                return;
            }
            
            if(blacklist.getBoolean("Events.Crafting.Report.ToConsole")) {
                Log.info(p.getName() + " attempted to craft " + is.getType().name().toLowerCase() + " in world " + world.getName());
            }
            
            if(blacklist.getBoolean("Events.Crafting.Report.ToPlayer")) {
                p.sendMessage(Messages.scan(Messages.getMessages().getString("Blacklists.Crafting.DisallowedMessage"), p, null, is.getType().name(), world));
            }
        }
    }
    
    @EventHandler
    public void handleConsume(PlayerItemConsumeEvent event) {
        if(event.isCancelled()) {
            return;
        }
        World world = event.getPlayer().getWorld();
        FileConfiguration blacklist = Blacklist.getBlacklist(world.getName());
        
        if(blacklist.getBoolean("Events.Consume.Enabled") != true) {
            return;
        }
        ItemStack is = event.getItem();
        int item_id = is.getTypeId();
        byte item_data = is.getData().getData();
        boolean shallContinue = false;
        Player p = event.getPlayer();
        List<String> blacklisted = blacklist.getStringList("Events.Consume.Blacklist");
        
        if(blacklisted.isEmpty() || blacklisted == null) {
            return;
        }
        
        for (int i = 0; i < blacklisted.size(); i++) {
            String line = blacklisted.get(i);
            
            if(line == null) {
                continue;
            }
            int blacklisted_id = 0;
            byte blacklisted_data = 0;
            
            if(line.contains(":")) {
                String[] splitted = line.split(":");
                blacklisted_id = Integer.parseInt(splitted[0]);
                blacklisted_data = Byte.parseByte(splitted[1]);
            } else {
                blacklisted_id = Integer.parseInt(line);
            }
            
            if(blacklisted_id < 1 || blacklisted_data < 0) {
                continue;
            }
            
            if(blacklisted_data > 0) {
                if(item_id == blacklisted_id && item_data == blacklisted_data) {
                    shallContinue = true;
                    break;
                }
            } else {
                if(item_id == blacklisted_id) {
                    shallContinue = true;
                    break;
                }
            }
        }
        
        if(shallContinue) {
            if(!PermHandler.hasBlacklistPermission(p, "iSafe.blacklist.consume.bypass.*")) {
                if(item_data < 1) {
                    if(!PermHandler.hasBlacklistPermission(p, "iSafe.blacklist.consume.bypass." + item_id)) {
                        event.setCancelled(true);
                    } else {
                        return;
                    }
                } else {
                    if(!PermHandler.hasBlacklistPermission(p, "iSafe.blacklist.consume.bypass." + item_id + ":" + item_data)) {
                        event.setCancelled(true);
                    } else {
                        return;
                    }
                }
            } else {
                return;
            }
            
            if(blacklist.getBoolean("Events.Consume.Economy.Enabled")) {
                int withdrawAmount = blacklist.getInt("Events.Consume.Economy.WithdrawAmount");
                
                Eco.withdraw(p.getName(), world, withdrawAmount);
                
                if(blacklist.getBoolean("Events.Consume.Economy.NotifyPlayer")) {
                    Eco.sendEcoNotify(p, "Consume", withdrawAmount);
                }
            }
            
            if(blacklist.getBoolean("Events.Consume.Penalities.KickPlayer")) {
                p.kickPlayer(Messages.scan(Messages.getMessages().getString("Blacklists.Consume.KickMessage"), p, null, is.getType().name(), world));
                return;
            }
            
            if(blacklist.getBoolean("Events.Consume.Report.ToConsole")) {
                Log.info(p.getName() + " attempted to consume item " + is.getType().name().toLowerCase() + " in world " + world.getName());
            }
            
            if(blacklist.getBoolean("Events.Consume.Report.ToPlayer")) {
                p.sendMessage(Messages.scan(Messages.getMessages().getString("Blacklists.Consume.DisallowedMessage"), p, null, is.getType().name(), world));
            }
        }
    }
    
    @EventHandler
    public void handlePistonExtend(BlockPistonExtendEvent event) {
        if(event.isCancelled()) {
            return;
        }
        World world = event.getBlock().getWorld();
        FileConfiguration blacklist = Blacklist.getBlacklist(world.getName());
        
        if(blacklist.getBoolean("Events.PistonExtend.Enabled") != true) {
            return;
        }
        int block_id = 0;
        byte block_data = 0;
        boolean shallContinue = false;
        List<String> blacklisted = blacklist.getStringList("Events.PistonExtend.Blacklist");
        
        if(blacklisted.isEmpty() || blacklisted == null) {
            return;
        }
        
        for(Block b : event.getBlocks()) {
            if(b != null) {
                block_id = b.getTypeId();
                block_data = b.getData();
                
                for (int i = 0; i < blacklisted.size(); i++) {
                    String line = blacklisted.get(i);

                    if(line == null) {
                        continue;
                    }
                    int blacklisted_id = 0;
                    byte blacklisted_data = 0;

                    if(line.contains(":")) {
                        String[] splitted = line.split(":");
                        blacklisted_id = Integer.parseInt(splitted[0]);
                        blacklisted_data = Byte.parseByte(splitted[1]);
                    } else {
                        blacklisted_id = Integer.parseInt(line);
                    }

                    if(blacklisted_id < 1 || blacklisted_data < 0) {
                        continue;
                    }

                    if(blacklisted_data > 0) {
                        if(block_id == blacklisted_id && block_data == blacklisted_data) {
                            shallContinue = true;
                            break;
                        }
                    } else {
                        if(block_id == blacklisted_id) {
                            shallContinue = true;
                            break;
                        }
                    }
                }
            }
        }
        
        if(shallContinue) {
            event.setCancelled(true);
            
            if(blacklist.getBoolean("Events.PistonExtend.Report.ToConsole")) {
                Log.info("A piston tried to push a blacklisted block in world " + world.getName());
            }
        }
    }
    
    @EventHandler
    public void handleDispense(BlockDispenseEvent event) {
        if(event.isCancelled()){
            return;
        }
        World world = event.getBlock().getWorld();
        FileConfiguration blacklist = Blacklist.getBlacklist(world.getName());
        
        if(blacklist.getBoolean("Events.Dispense.Enabled") != true) {
            return;
        }
        ItemStack is = event.getItem();
        int item_id = is.getTypeId();
        byte item_data = is.getData().getData();
        boolean shallContinue = false;
        List<String> blacklisted = blacklist.getStringList("Events.Dispense.Blacklist");
        
        if(blacklisted.isEmpty() || blacklisted == null) {
            return;
        }
        
        for (int i = 0; i < blacklisted.size(); i++) {
            String line = blacklisted.get(i);
            
            if(line == null) {
                continue;
            }
            int blacklisted_id = 0;
            byte blacklisted_data = 0;
            
            if(line.contains(":")) {
                String[] splitted = line.split(":");
                blacklisted_id = Integer.parseInt(splitted[0]);
                blacklisted_data = Byte.parseByte(splitted[1]);
            } else {
                blacklisted_id = Integer.parseInt(line);
            }
            
            if(blacklisted_id < 1 || blacklisted_data < 0) {
                continue;
            }
            
            if(blacklisted_data > 0) {
                if(item_id == blacklisted_id && item_data == blacklisted_data) {
                    shallContinue = true;
                    break;
                }
            } else {
                if(item_id == blacklisted_id) {
                    shallContinue = true;
                    break;
                }
            }
        }
        
        if(shallContinue) {
            event.setCancelled(true);
            
            if(blacklist.getBoolean("Events.Dispense.Report.ToConsole")) {
                Log.info("A dispenser tried to dispense a blacklisted item in world " + world.getName());
            }
        }
    }
    
    
    @EventHandler
    public void mobspawnBlacklist(CreatureSpawnEvent event) {
        if (event.isCancelled()){
            return;
        }
        LivingEntity le = event.getEntity();
        short id = le.getType().getTypeId();
        String name = le.getType().getName().toLowerCase();
        
        for(World world : Bukkit.getServer().getWorlds()) {
            String worldname = world.getName();
            String eWorld = le.getWorld().getName();
            String state = "MobSpawn." + eWorld + ".Enabled";
            
            
            if(eWorld.equalsIgnoreCase(worldname)) 
            {
                if(CreatureManager.getCreatureManager().getBoolean(state) == true) 
                {
                    if(event.getSpawnReason() == SpawnReason.NATURAL)
                    {
                        String blacklist = "MobSpawn." + eWorld + ".Natural.Blacklist";
                        if(CreatureManager.getCreatureManager().getString(blacklist).contains(id + ",")) 
                        {
                            event.setCancelled(true);
                            event.getEntity().remove();
                            if (CreatureManager.getCreatureManager().getBoolean("MobSpawn." + eWorld + ".Natural.Debug.ToConsole") == true){
                                Log.info("A(n) " + name + " was cancelled its spawn, for the spawn reason: Natural; In the world: " + eWorld);
                            }
                        }
                    }
                    
                    if(event.getSpawnReason() == SpawnReason.SPAWNER)
                    {
                        String blacklist = "MobSpawn." + eWorld + ".Spawner.Blacklist";
                        if(CreatureManager.getCreatureManager().getString(blacklist).contains(id + ",")) 
                        {
                            event.setCancelled(true);
                            event.getEntity().remove();
                            if (CreatureManager.getCreatureManager().getBoolean("MobSpawn." + eWorld + ".Spawner.Debug.ToConsole") == true){
                                Log.info("A(n) " + name + " was cancelled its spawn, for the spawn reason: Spawner; In the world: " + eWorld);
                            }
                        }
                    }
                    
                    if(event.getSpawnReason() == SpawnReason.CUSTOM)
                    {
                        String blacklist = "MobSpawn." + eWorld + ".Custom.Blacklist";
                        if(CreatureManager.getCreatureManager().getString(blacklist).contains(id + ",")) 
                        {
                            event.setCancelled(true);
                            event.getEntity().remove();
                            if (CreatureManager.getCreatureManager().getBoolean("MobSpawn." + eWorld + ".Custom.Debug.ToConsole") == true){
                                Log.info("A(n) " + name + " was cancelled its spawn, for the spawn reason: Custom; In the world: " + eWorld);
                            }
                        }
                    }
                    
                    if(event.getSpawnReason() == SpawnReason.SPAWNER_EGG)
                    {
                        String blacklist = "MobSpawn." + eWorld + ".SpawnerEgg.Blacklist";
                        if(CreatureManager.getCreatureManager().getString(blacklist).contains(id + ",")) 
                        {
                            event.setCancelled(true);
                            event.getEntity().remove();
                            if (CreatureManager.getCreatureManager().getBoolean("MobSpawn." + eWorld + ".SpawnerEgg.Debug.ToConsole") == true){
                                Log.info("A(n) " + name + " was cancelled its spawn, for the spawn reason: SpawnerEgg; In the world: " + eWorld);
                            }
                        }
                    }
                    
                    if(event.getSpawnReason() == SpawnReason.CHUNK_GEN)
                    {
                        String blacklist = "MobSpawn." + eWorld + ".ChunkGen.Blacklist";
                        if(CreatureManager.getCreatureManager().getString(blacklist).contains(id + ",")) 
                        {
                            event.setCancelled(true);
                            event.getEntity().remove();
                            if (CreatureManager.getCreatureManager().getBoolean("MobSpawn." + eWorld + ".ChunkGen.Debug.ToConsole") == true){
                                Log.info("A(n) " + name + " was cancelled its spawn, for the spawn reason: ChunkGen; In the world: " + eWorld);
                            }
                        }
                    }
                    
                    if(event.getSpawnReason() == SpawnReason.BREEDING)
                    {
                        String blacklist = "MobSpawn." + eWorld + ".Breeding.Blacklist";
                        if(CreatureManager.getCreatureManager().getString(blacklist).contains(id + ",")) 
                        {
                            event.setCancelled(true);
                            event.getEntity().remove();
                            
                            if (CreatureManager.getCreatureManager().getBoolean("MobSpawn." + eWorld + ".Breeding.Debug.ToConsole") == true){
                                Log.info("A(n) " + name + " was cancelled its spawn, for the spawn reason: Breeding; In the world: " + eWorld);
                            }
                        }
                    }
                }
            }
        }
    }
}
