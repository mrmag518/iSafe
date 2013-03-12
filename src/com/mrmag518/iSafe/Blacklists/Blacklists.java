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

import com.mrmag518.iSafe.Files.BlacklistsF;
import com.mrmag518.iSafe.Files.CreatureManager;
import com.mrmag518.iSafe.Files.Messages;
import com.mrmag518.iSafe.Util.Eco;
import com.mrmag518.iSafe.Util.Log;
import com.mrmag518.iSafe.Util.PermHandler;
import com.mrmag518.iSafe.iSafe;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.HumanEntity;
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
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.Recipe;

public class Blacklists implements Listener {
    public static iSafe plugin;
    public Blacklists(iSafe instance)
    {
        plugin = instance;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    
    private void checkBlacklist(String path) {
        if(path == null) {
            return;
        }
        String value = BlacklistsF.getBlacklists().getString(path);
        
        if(value.equalsIgnoreCase("") || value.equalsIgnoreCase(" ")) {
            return;
        } else {
            if(!value.endsWith(",")) {
                BlacklistsF.getBlacklists().set(path, value + ",");
                BlacklistsF.save();
                Log.debug("Blacklist path '" + path + "' did not end with a comma. Added a comma for you.");
            }
        }
    }
    
    /*private boolean isBlacklisted(String blacklist, Block block) {
        int id = block.getTypeId();
        byte data = block.getData();
        
        if(BlacklistsF.getBlacklists().getString(blacklist).contains(id + ",")) {
            return true;
        }
        return false;
    }*/
    
    @EventHandler
    public void BreakBlacklist(BlockBreakEvent event) {
        if (event.isCancelled()){
            return;
        }
        Block b = event.getBlock();
        Player p = event.getPlayer();
        int id = b.getTypeId();
        String name = b.getType().name().toLowerCase();
        
        for(World world : Bukkit.getServer().getWorlds()) {
            String worldname = world.getName();
            String pWorld = p.getWorld().getName();
            String blacklist = "Break." + pWorld + ".Blacklist";
            String state = "Break." + pWorld + ".Enabled";
            
            checkBlacklist(blacklist);
            
            if(pWorld.equalsIgnoreCase(worldname)) 
            {
                if(BlacklistsF.getBlacklists().getBoolean(state) == true) 
                {
                    if(BlacklistsF.getBlacklists().getString(blacklist).contains(id + ",")) //|| BlacklistsF.getBlacklists().getString(blacklist).contains(id + ":" + b.getData() + ","))
                    {
                        if(!PermHandler.hasBlacklistPermission(p, "iSafe.bypass.blacklist.break")) 
                        {
                            if (BlacklistsF.getBlacklists().getBoolean("Break." + pWorld + ".Gamemode.PreventFor.Survival") == true) {
                                if(p.getGameMode().equals(GameMode.SURVIVAL)) {
                                    event.setCancelled(true);
                                }
                            }

                            if (BlacklistsF.getBlacklists().getBoolean("Break." + pWorld + ".Gamemode.PreventFor.Creative") == true) {
                                if(p.getGameMode().equals(GameMode.CREATIVE)) {
                                    event.setCancelled(true);
                                }
                            }
                            
                            if (BlacklistsF.getBlacklists().getBoolean("Break." + pWorld + ".Gamemode.PreventFor.Adventure") == true) {
                                if(p.getGameMode().equals(GameMode.ADVENTURE)) {
                                    event.setCancelled(true);
                                }
                            }
                            
                            if (event.isCancelled()) 
                            {
                                if (BlacklistsF.getBlacklists().getBoolean("Break." + pWorld + ".KickPlayer") == true){
                                    p.kickPlayer(Messages.blacklistBreakKickMsg(b));
                                }
                                
                                if(BlacklistsF.getBlacklists().getBoolean("Break." + pWorld + ".Economy.Enabled") == true) {
                                    int amount = BlacklistsF.getBlacklists().getInt("Break." + pWorld + ".Economy.WithdrawAmount");
                                    Eco.takeMoney(p.getName(), pWorld, "Break", amount);
                                    
                                    if(BlacklistsF.getBlacklists().getBoolean("Break." + pWorld + ".Economy.NotifyPlayer") == true) {
                                        Eco.sendEcoNotify(p, "Break", amount);
                                    }
                                }
                                
                                if(BlacklistsF.getBlacklists().getBoolean("Break." + pWorld + ".Alert/log.ToPlayer") == true) {
                                    p.sendMessage(Messages.blacklistBreakMsg(b));
                                }
                                
                                if (BlacklistsF.getBlacklists().getBoolean("Break." + pWorld + ".Alert/log.ToConsole") == true){
                                    Log.info("[iSafe] " + p.getName() + " was prevented from breaking the " + "blacklisted block '" + name + "' in the world '" + pWorld + "'.");
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    @EventHandler
    public void PlaceBlacklist(BlockPlaceEvent event) {
        if (event.isCancelled()){
            return;
        }
        Block b = event.getBlock();
        Player p = event.getPlayer();
        int id = b.getTypeId();
        String name = b.getType().name().toLowerCase();
        
        for(World world : Bukkit.getServer().getWorlds()) {
            String worldname = world.getName();
            String pWorld = p.getWorld().getName();
            String blacklist = "Place." + pWorld + ".Blacklist";
            String state = "Place." + pWorld + ".Enabled";
            
            checkBlacklist(blacklist);
            
            if(pWorld.equalsIgnoreCase(worldname)) 
            {
                if(BlacklistsF.getBlacklists().getBoolean(state) == true) 
                {
                    if(BlacklistsF.getBlacklists().getString(blacklist).contains("," + id + ",")) //|| BlacklistsF.getBlacklists().getString(blacklist).contains("," + id + ":" + b.getData() + ","))
                    {
                        if(!PermHandler.hasBlacklistPermission(p, "iSafe.bypass.blacklist.place")) 
                        {
                            if (BlacklistsF.getBlacklists().getBoolean("Place." + pWorld + ".Gamemode.PreventFor.Survival") == true) {
                                if(p.getGameMode().equals(GameMode.SURVIVAL)) {
                                    event.setCancelled(true);
                                }
                            } 

                            if (BlacklistsF.getBlacklists().getBoolean("Place." + pWorld + ".Gamemode.PreventFor.Creative") == true) {
                                if(p.getGameMode().equals(GameMode.CREATIVE)) {
                                    event.setCancelled(true);
                                }
                            }
                            
                            if (BlacklistsF.getBlacklists().getBoolean("Place." + pWorld + ".Gamemode.PreventFor.Adventure") == true) {
                                if(p.getGameMode().equals(GameMode.ADVENTURE)) {
                                    event.setCancelled(true);
                                }
                            }
                            
                            if (event.isCancelled()) 
                            {
                                if (BlacklistsF.getBlacklists().getBoolean("Place." + pWorld + ".KickPlayer") == true) {
                                    p.kickPlayer(Messages.blacklistPlaceKickMsg(b));
                                }
                                
                                if(BlacklistsF.getBlacklists().getBoolean("Place." + pWorld + ".Economy.Enabled") == true) {
                                    int amount = BlacklistsF.getBlacklists().getInt("Place." + pWorld + ".Economy.WithdrawAmount");
                                    Eco.takeMoney(p.getName(), pWorld, "Place", amount);
                                    
                                    if(BlacklistsF.getBlacklists().getBoolean("Place." + pWorld + ".Economy.NotifyPlayer") == true) {
                                        Eco.sendEcoNotify(p, "Place", amount);
                                    }
                                }
                                
                                if (BlacklistsF.getBlacklists().getBoolean("Place." + pWorld + ".Alert/log.ToPlayer") == true){
                                    p.sendMessage(Messages.blacklistPlaceMsg(b));
                                }
                                
                                if (BlacklistsF.getBlacklists().getBoolean("Place." + pWorld + ".Alert/log.ToConsole") == true){
                                    Log.info("[iSafe] " + p.getName() + " was prevented from placing the " + "blacklisted block '" + name + "' in the world '" + pWorld + "'.");
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    @EventHandler
    public void CommandBlacklist(PlayerCommandPreprocessEvent event) {
        if (event.isCancelled()){
            return;
        }
        Player p = event.getPlayer();
        String line = event.getMessage().toLowerCase();
        
        for(World world : Bukkit.getServer().getWorlds()) {
            String worldname = world.getName();
            String pWorld = p.getWorld().getName();
            String blacklist = "Command." + pWorld + ".Blacklist";
            String state = "Command." + pWorld + ".Enabled";
            
            for(String command : BlacklistsF.getBlacklists().getStringList(blacklist)) 
            {
                if(pWorld.equalsIgnoreCase(worldname)) 
                {
                    if(BlacklistsF.getBlacklists().getBoolean(state) == true) 
                    {
                        if(line.startsWith(command.toLowerCase())) 
                        {
                            if(!PermHandler.hasBlacklistPermission(p, "iSafe.bypass.blacklist.command")) 
                            {
                                if (BlacklistsF.getBlacklists().getBoolean("Command." + pWorld + ".Gamemode.PreventFor.Survival") == true) {
                                    if(p.getGameMode().equals(GameMode.SURVIVAL)) {
                                        event.setCancelled(true);
                                    }
                                }
                                
                                if (BlacklistsF.getBlacklists().getBoolean("Command." + pWorld + ".Gamemode.PreventFor.Creative") == true) {
                                    if(p.getGameMode().equals(GameMode.CREATIVE)) {
                                        event.setCancelled(true);
                                    }
                                }
                                
                                if (BlacklistsF.getBlacklists().getBoolean("Command." + pWorld + ".Gamemode.PreventFor.Adventure") == true) {
                                    if(p.getGameMode().equals(GameMode.ADVENTURE)) {
                                        event.setCancelled(true);
                                    }
                                }
                                
                                if (event.isCancelled()) 
                                {
                                    if (BlacklistsF.getBlacklists().getBoolean("Command." + pWorld + ".KickPlayer") == true){
                                        p.kickPlayer(Messages.blacklistCommandKickMsg(command, worldname));
                                    }
                                    
                                    if(BlacklistsF.getBlacklists().getBoolean("Command." + pWorld + ".Economy.Enabled") == true) {
                                    int amount = BlacklistsF.getBlacklists().getInt("Command." + pWorld + ".Economy.WithdrawAmount");
                                    Eco.takeMoney(p.getName(), pWorld, "Command", amount);
                                    
                                    if(BlacklistsF.getBlacklists().getBoolean("Command." + pWorld + ".Economy.NotifyPlayer") == true) {
                                        Eco.sendEcoNotify(p, "Command", amount);
                                    }
                                }
                                    
                                    if (BlacklistsF.getBlacklists().getBoolean("Command." + pWorld + ".Alert/log.ToPlayer") == true){
                                        p.sendMessage(Messages.blacklistCommandMsg(command, worldname));
                                    }
                                    
                                    if (BlacklistsF.getBlacklists().getBoolean("Command." + pWorld + ".Alert/log.ToConsole") == true){
                                        Log.info("[iSafe] " + p.getName() + " was prevented from doing the " + "blacklisted command '" + command + "' in the world '" + pWorld + "'.");
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    @EventHandler
    public void CraftBlacklist(CraftItemEvent event) {
        if (event.isCancelled()){
            return;
        }
        Recipe rec = event.getRecipe();
        HumanEntity he = event.getWhoClicked();
        int id = rec.getResult().getTypeId();
        String name = rec.getResult().getType().name().toLowerCase();
        Player p = null;
        
        // Check if the HumanEntity is a Player. (of course he is ..)
        if(he instanceof Player) {
            Log.debug("[CraftBlacklist]" + he.getName() + " is a Player.");
            p = (Player)he;
        } else {
            Log.debug("[CraftBlacklist]" + he.getName() + " is not a Player. say wut?!");
            return;
        }
        
        for(World world : Bukkit.getServer().getWorlds()) {
            String worldname = world.getName();
            String pWorld = p.getWorld().getName();
            String blacklist = "Crafting." + pWorld + ".Blacklist";
            String state = "Crafting." + pWorld + ".Enabled";
            
            checkBlacklist(blacklist);
            
            if(pWorld.equalsIgnoreCase(worldname)) 
            {
                if(BlacklistsF.getBlacklists().getBoolean(state) == true) 
                {
                    if(BlacklistsF.getBlacklists().getString(blacklist).contains(id + ",")) //|| BlacklistsF.getBlacklists().getString(blacklist).contains(id + ":" + rec.getResult().getData().getData() + ","))
                    {
                        if(!PermHandler.hasBlacklistPermission(p, "iSafe.bypass.blacklist.crafting")) 
                        {
                           if (BlacklistsF.getBlacklists().getBoolean("Crafting." + pWorld + ".Gamemode.PreventFor.Survival") == true) {
                                if(he.getGameMode().equals(GameMode.SURVIVAL)) {
                                    event.setCancelled(true);
                                }
                            } 

                            if (BlacklistsF.getBlacklists().getBoolean("Crafting." + pWorld + ".Gamemode.PreventFor.Creative") == true) {
                                if(he.getGameMode().equals(GameMode.CREATIVE)) {
                                    event.setCancelled(true);
                                }
                            }
                            
                            if (BlacklistsF.getBlacklists().getBoolean("Crafting." + pWorld + ".Gamemode.PreventFor.Adventure") == true) {
                                if(p.getGameMode().equals(GameMode.ADVENTURE)) {
                                    event.setCancelled(true);
                                }
                            }
                            
                            if (event.isCancelled()) 
                            {
                                if (BlacklistsF.getBlacklists().getBoolean("Crafting." + pWorld + ".KickPlayer") == true) {
                                    p.kickPlayer(Messages.blacklistCraftingKickMsg(name));
                                }
                                
                                if(BlacklistsF.getBlacklists().getBoolean("Crafting." + pWorld + ".Economy.Enabled") == true) {
                                    int amount = BlacklistsF.getBlacklists().getInt("Crafting." + pWorld + ".Economy.WithdrawAmount");
                                    Eco.takeMoney(p.getName(), pWorld, "Crafting", amount);
                                    
                                    if(BlacklistsF.getBlacklists().getBoolean("Crafting." + pWorld + ".Economy.NotifyPlayer") == true) {
                                        Eco.sendEcoNotify(p, "Crafting", amount);
                                    }
                                }
                                
                                if (BlacklistsF.getBlacklists().getBoolean("Crafting." + pWorld + ".Alert/log.ToPlayer") == true){
                                    p.sendMessage(Messages.blacklistCraftingMsg(name, p.getWorld()));
                                }
                                
                                if (BlacklistsF.getBlacklists().getBoolean("Crafting." + pWorld + ".Alert/log.ToConsole") == true){
                                    Log.info("[iSafe] " + p.getName() + " was prevented from crafting the " + "blacklisted recipe '" + name + "' in the world '" + pWorld + "'.");
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    @EventHandler
    public void DispenseBlacklist(BlockDispenseEvent event) {
        if (event.isCancelled()){
            return;
        }
        Block b = event.getBlock();
        int id = event.getItem().getTypeId();
        String name = event.getItem().getType().name().toLowerCase();
        
        for(World world : Bukkit.getServer().getWorlds()) {
            String worldname = world.getName();
            String bWorld = b.getWorld().getName();
            String blacklist = "Dispense." + bWorld + ".Blacklist";
            String state = "Dispense." + bWorld + ".Enabled";
            
            checkBlacklist(blacklist);
            
            if(bWorld.equalsIgnoreCase(worldname)) 
            {
                if(BlacklistsF.getBlacklists().getBoolean(state) == true) 
                {
                    if(BlacklistsF.getBlacklists().getString(blacklist).contains(id + ",")) //|| BlacklistsF.getBlacklists().getString(blacklist).contains(id + ":" + b.getData() + ","))
                    {
                        event.setCancelled(true);
                        if(BlacklistsF.getBlacklists().getBoolean("Dispense." + bWorld + ".Alert/log-to.Console") == true) {
                            Log.info("[iSafe] " + " The block '" + name + "' was prevented from being dispensed" + " in the world '" + bWorld + "'.");
                        }
                    }
                }
            }
        }
    }
    
    @EventHandler
    public void DropBlacklist(PlayerDropItemEvent event) {
        if (event.isCancelled()){
            return;
        }
        Player p = event.getPlayer();
        int id = event.getItemDrop().getItemStack().getTypeId();
        String name = event.getItemDrop().getItemStack().getType().name().toLowerCase();
        
        for(World world : Bukkit.getServer().getWorlds()) {
            String worldname = world.getName();
            String pWorld = p.getWorld().getName();
            String blacklist = "Drop." + pWorld + ".Blacklist";
            String state = "Drop." + pWorld + ".Enabled";
            
            checkBlacklist(blacklist);
            
            if(pWorld.equalsIgnoreCase(worldname)) 
            {
                if(BlacklistsF.getBlacklists().getBoolean(state) == true) 
                {
                    if(BlacklistsF.getBlacklists().getString(blacklist).contains(id + ",")) //|| BlacklistsF.getBlacklists().getString(blacklist).contains(id + ":" + event.getItemDrop().getItemStack().getData().getData() + ","))
                    {
                        if(!PermHandler.hasBlacklistPermission(p, "iSafe.bypass.blacklist.drop")) 
                        {
                            if (BlacklistsF.getBlacklists().getBoolean("Drop." + pWorld + ".Gamemode.PreventFor.Survival") == true) {
                                if(p.getGameMode().equals(GameMode.SURVIVAL)) {
                                    event.setCancelled(true);
                                }
                            }

                            if (BlacklistsF.getBlacklists().getBoolean("Drop." + pWorld + ".Gamemode.PreventFor.Creative") == true) {
                                if(p.getGameMode().equals(GameMode.CREATIVE)) {
                                    event.setCancelled(true);
                                }
                            }
                            
                            if (BlacklistsF.getBlacklists().getBoolean("Drop." + pWorld + ".Gamemode.PreventFor.Adventure") == true) {
                                if(p.getGameMode().equals(GameMode.ADVENTURE)) {
                                    event.setCancelled(true);
                                }
                            }
                            
                            if (event.isCancelled()) 
                            {
                                if (BlacklistsF.getBlacklists().getBoolean("Drop." + pWorld + ".KickPlayer") == true) {
                                    p.kickPlayer(Messages.blacklistDropKickMsg(event.getItemDrop()));
                                }
                                
                                if(BlacklistsF.getBlacklists().getBoolean("Drop." + pWorld + ".Economy.Enabled") == true) {
                                    int amount = BlacklistsF.getBlacklists().getInt("Drop." + pWorld + ".Economy.WithdrawAmount");
                                    Eco.takeMoney(p.getName(), pWorld, "Drop", amount);
                                    
                                    if(BlacklistsF.getBlacklists().getBoolean("Drop." + pWorld + ".Economy.NotifyPlayer") == true) {
                                        Eco.sendEcoNotify(p, "Drop", amount);
                                    }
                                }
                                
                                if (BlacklistsF.getBlacklists().getBoolean("Drop." + pWorld + ".Alert/log.ToPlayer") == true) {
                                    p.sendMessage(Messages.blacklistDropMsg(name, p.getWorld()));
                                }
                                
                                if (BlacklistsF.getBlacklists().getBoolean("Drop." + pWorld + ".Alert/log.ToConsole") == true) {
                                    Log.info("[iSafe] " + p.getName() + " was prevented from droping the " + "blacklisted item '" + name + "' in the world '" + pWorld + "'.");
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    @EventHandler
    public void InteractBlacklist(PlayerInteractEvent event) {
        if (event.isCancelled()){
            return;
        }
        Action action = event.getAction();
        
        if(action == Action.LEFT_CLICK_BLOCK || action == Action.RIGHT_CLICK_BLOCK) {
            Player p = event.getPlayer();
            Block b = event.getClickedBlock();
            int id = b.getTypeId();
            String name = b.getType().name().toLowerCase();
            
            for(World world : Bukkit.getServer().getWorlds()) {
                String worldname = world.getName();
                String pWorld = p.getWorld().getName();
                String blacklist = "Interact." + pWorld + ".Blacklist";
                String state = "Interact." + pWorld + ".Enabled";
                
                checkBlacklist(blacklist);
                
                if(pWorld.equalsIgnoreCase(worldname)) 
                {
                    if(BlacklistsF.getBlacklists().getBoolean(state) == true) 
                    {
                        if(BlacklistsF.getBlacklists().getString(blacklist).contains(id + ",")) //|| BlacklistsF.getBlacklists().getString(blacklist).contains(id + ":" + b.getData() + ","))
                        {
                            if(!PermHandler.hasBlacklistPermission(p, "iSafe.bypass.blacklist.interact"))
                            {
                                if (BlacklistsF.getBlacklists().getBoolean("Interact." + pWorld + ".Gamemode.PreventFor.Survival") == true) {
                                    if(p.getGameMode().equals(GameMode.SURVIVAL)) {
                                        event.setCancelled(true);
                                    }
                                }

                                if (BlacklistsF.getBlacklists().getBoolean("Interact." + pWorld + ".Gamemode.PreventFor.Creative") == true) {
                                    if(p.getGameMode().equals(GameMode.CREATIVE)) {
                                        event.setCancelled(true);
                                    }
                                }
                                
                                if (BlacklistsF.getBlacklists().getBoolean("Interact." + pWorld + ".Gamemode.PreventFor.Adventure") == true) {
                                    if(p.getGameMode().equals(GameMode.ADVENTURE)) {
                                        event.setCancelled(true);
                                    }
                                }
                                
                                if (event.isCancelled()) 
                                {
                                    if (BlacklistsF.getBlacklists().getBoolean("Interact." + pWorld + ".KickPlayer") == true) {
                                        p.kickPlayer(Messages.blacklistInteractKickMsg(b));
                                    }
                                    
                                    if(BlacklistsF.getBlacklists().getBoolean("Interact." + pWorld + ".Economy.Enabled") == true) {
                                        int amount = BlacklistsF.getBlacklists().getInt("Interact." + pWorld + ".Economy.WithdrawAmount");
                                        Eco.takeMoney(p.getName(), pWorld, "Interact", amount);

                                        if(BlacklistsF.getBlacklists().getBoolean("Interact." + pWorld + ".Economy.NotifyPlayer") == true) {
                                            Eco.sendEcoNotify(p, "Interact", amount);
                                        }
                                    }
                                    
                                    if (BlacklistsF.getBlacklists().getBoolean("Interact." + pWorld + ".Alert/log.ToPlayer") == true) {
                                        p.sendMessage(Messages.blacklistInteractMsg(b));
                                    }
                                    
                                    if (BlacklistsF.getBlacklists().getBoolean("Interact." + pWorld + ".Alert/log.ToConsole") == true) {
                                        Log.info("[iSafe] " + p.getName() + " was prevented from interacting with the " + "blacklisted block '" + name + "' in the world '" + pWorld + "'.");
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    @EventHandler
    public void PickupBlacklist(PlayerPickupItemEvent event) {
        if (event.isCancelled()){
            return;
        }
        Player p = event.getPlayer();
        int id = event.getItem().getItemStack().getTypeId();
        String name = event.getItem().getItemStack().getType().name().toLowerCase();
        
        for(World world : Bukkit.getServer().getWorlds()) {
            String worldname = world.getName();
            String pWorld = p.getWorld().getName();
            String blacklist = "Pickup." + pWorld + ".Blacklist";
            String state = "Pickup." + pWorld + ".Enabled";
            
            checkBlacklist(blacklist);
            
            if(pWorld.equalsIgnoreCase(worldname)) 
            {
                if(BlacklistsF.getBlacklists().getBoolean(state) == true) 
                {
                    if(BlacklistsF.getBlacklists().getString(blacklist).contains(id + ",")) //|| BlacklistsF.getBlacklists().getString(blacklist).contains(id + ":" + event.getItem().getItemStack().getData().getData() + ","))
                    {
                        if(!PermHandler.hasBlacklistPermission(p, "iSafe.bypass.blacklist.pickup")) 
                        {
                            if (BlacklistsF.getBlacklists().getBoolean("Pickup." + pWorld + ".Gamemode.PreventFor.Survival") == true) {
                                if(p.getGameMode().equals(GameMode.SURVIVAL)) {
                                    event.setCancelled(true);
                                }
                            }

                            if (BlacklistsF.getBlacklists().getBoolean("Pickup." + pWorld + ".Gamemode.PreventFor.Creative") == true) {
                                if(p.getGameMode().equals(GameMode.CREATIVE)) {
                                    event.setCancelled(true);
                                }
                            }
                            
                            if (BlacklistsF.getBlacklists().getBoolean("Pickup." + pWorld + ".Gamemode.PreventFor.Adventure") == true) {
                                if(p.getGameMode().equals(GameMode.ADVENTURE)) {
                                    event.setCancelled(true);
                                }
                            }
                            
                            if (event.isCancelled())
                            {
                                if (BlacklistsF.getBlacklists().getBoolean("Pickup." + pWorld + ".KickPlayer") == true) {
                                    p.kickPlayer(Messages.blacklistPickupKickMsg(name));
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    @EventHandler
    public void Censor(AsyncPlayerChatEvent event) {
        if (event.isCancelled()){
            return;
        }
        Player p = event.getPlayer();
        String sentence = event.getMessage().toLowerCase();
        
        for(World world : Bukkit.getServer().getWorlds()) {
            String worldname = world.getName();
            String pWorld = p.getWorld().getName();
            String blacklist = "Chat." + pWorld + ".Blacklist";
            String state = "Chat." + pWorld + ".Enabled";
            boolean useDetailedSearchMode = BlacklistsF.getBlacklists().getBoolean("Chat." + pWorld + ".UseDetailedSearchMode");
            String whitelist = "Chat." + pWorld + ".Whitelist";
            
            for(String word : BlacklistsF.getBlacklists().getStringList(blacklist)) 
            {
                if(pWorld.equalsIgnoreCase(worldname)) 
                {
                    if(BlacklistsF.getBlacklists().getBoolean(state) == true) 
                    {
                        String result;
                        if(useDetailedSearchMode == true) {
                            result = sentence.replaceAll(" ", "");
                        } else {
                            result = sentence;
                        }
                        if(result.contains(word.toLowerCase())) 
                        {
                            boolean shallCancel = false;
                            for(String wWord : BlacklistsF.getBlacklists().getStringList(whitelist)) {
                                if(useDetailedSearchMode == true) {
                                    if(sentence.contains(wWord.toLowerCase())) { // Must use sentence, not result.
                                        return;
                                    } else {
                                        shallCancel = true;
                                    }
                                } else {
                                    if(result.contains(wWord.toLowerCase())) {
                                        return;
                                    } else {
                                        shallCancel = true;
                                    }
                                }
                            }
                            
                            if(shallCancel == true) {
                                event.setCancelled(shallCancel);
                            }
                            
                            if (event.isCancelled()) 
                            {
                                if (BlacklistsF.getBlacklists().getBoolean("Chat." + pWorld + ".KickPlayer") == true) {
                                    p.kickPlayer(Messages.blacklistCensorKickMsg(word));
                                }
                                
                                if(BlacklistsF.getBlacklists().getBoolean("Chat." + pWorld + ".Economy.Enabled") == true) {
                                    int amount = BlacklistsF.getBlacklists().getInt("Chat." + pWorld + ".Economy.WithdrawAmount");
                                    Eco.takeMoney(p.getName(), pWorld, "Chat", amount);
                                    
                                    if(BlacklistsF.getBlacklists().getBoolean("Chat." + pWorld + ".Economy.NotifyPlayer") == true) {
                                        Eco.sendEcoNotify(p, "Censor", amount);
                                    }
                                }
                                
                                if (BlacklistsF.getBlacklists().getBoolean("Chat." + pWorld + ".Alert/log.ToPlayer") == true) {
                                    p.sendMessage(Messages.blacklistCensorMsg(word, p.getWorld()));
                                }
                                
                                if (BlacklistsF.getBlacklists().getBoolean("Chat." + pWorld + ".Alert/log.ToConsole") == true) {
                                    Log.info("[iSafe] " + p.getName() + "'s message contained the blacklisted word: " + word);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    @EventHandler
    public void mobspawnBlacklist(CreatureSpawnEvent event) {
        if (event.isCancelled()){
            return;
        }
        LivingEntity le = event.getEntity();
        int id = le.getEntityId();
        String name = le.getType().getName().toLowerCase();
        
        for(World world : Bukkit.getServer().getWorlds()) {
            String worldname = world.getName();
            String eWorld = le.getWorld().getName();
            String state = "MobSpawn." + eWorld + ".Enabled";
            
            
            if(eWorld.equalsIgnoreCase(worldname)) 
            {
                if(BlacklistsF.getBlacklists().getBoolean(state) == true) 
                {
                    if(event.getSpawnReason() == SpawnReason.NATURAL)
                    {
                        String blacklist = "MobSpawn." + eWorld + ".Natural.Blacklist";
                        checkBlacklist(blacklist);
                        if(CreatureManager.getCreatureManager().getString(blacklist).contains(id + ",")) 
                        {
                            event.setCancelled(true);
                            event.getEntity().remove();
                            if (CreatureManager.getCreatureManager().getBoolean("MobSpawn." + eWorld + ".Natural.Debug.ToConsole") == true){
                                Log.info("[iSafe]" + " A(n) " + name + " was cancelled its spawn, for the spawn reason: Natural; In the world: " + eWorld);
                            }
                        }
                    }
                    
                    if(event.getSpawnReason() == SpawnReason.SPAWNER)
                    {
                        String blacklist = "MobSpawn." + eWorld + ".Spawner.Blacklist";
                        checkBlacklist(blacklist);
                        if(CreatureManager.getCreatureManager().getString(blacklist).contains(id + ",")) 
                        {
                            event.setCancelled(true);
                            event.getEntity().remove();
                            if (CreatureManager.getCreatureManager().getBoolean("MobSpawn." + eWorld + ".Spawner.Debug.ToConsole") == true){
                                Log.info("[iSafe]" + " A(n) " + name + " was cancelled its spawn, for the spawn reason: Spawner; In the world: " + eWorld);
                            }
                        }
                    }
                    
                    if(event.getSpawnReason() == SpawnReason.CUSTOM)
                    {
                        String blacklist = "MobSpawn." + eWorld + ".Custom.Blacklist";
                        checkBlacklist(blacklist);
                        if(CreatureManager.getCreatureManager().getString(blacklist).contains(id + ",")) 
                        {
                            event.setCancelled(true);
                            event.getEntity().remove();
                            if (CreatureManager.getCreatureManager().getBoolean("MobSpawn." + eWorld + ".Custom.Debug.ToConsole") == true){
                                Log.info("[iSafe]" + " A(n) " + name + " was cancelled its spawn, for the spawn reason: Custom; In the world: " + eWorld);
                            }
                        }
                    }
                    
                    if(event.getSpawnReason() == SpawnReason.SPAWNER_EGG)
                    {
                        String blacklist = "MobSpawn." + eWorld + ".SpawnerEgg.Blacklist";
                        checkBlacklist(blacklist);
                        if(CreatureManager.getCreatureManager().getString(blacklist).contains(id + ",")) 
                        {
                            event.setCancelled(true);
                            event.getEntity().remove();
                            if (CreatureManager.getCreatureManager().getBoolean("MobSpawn." + eWorld + ".SpawnerEgg.Debug.ToConsole") == true){
                                Log.info("[iSafe]" + " A(n) " + name + " was cancelled its spawn, for the spawn reason: SpawnerEgg; In the world: " + eWorld);
                            }
                        }
                    }
                    
                    if(event.getSpawnReason() == SpawnReason.CHUNK_GEN)
                    {
                        String blacklist = "MobSpawn." + eWorld + ".ChunkGen.Blacklist";
                        checkBlacklist(blacklist);
                        if(CreatureManager.getCreatureManager().getString(blacklist).contains(id + ",")) 
                        {
                            event.setCancelled(true);
                            event.getEntity().remove();
                            if (CreatureManager.getCreatureManager().getBoolean("MobSpawn." + eWorld + ".ChunkGen.Debug.ToConsole") == true){
                                Log.info("[iSafe]" + " A(n) " + name + " was cancelled its spawn, for the spawn reason: ChunkGen; In the world: " + eWorld);
                            }
                        }
                    }
                    
                    if(event.getSpawnReason() == SpawnReason.BREEDING)
                    {
                        String blacklist = "MobSpawn." + eWorld + ".Breeding.Blacklist";
                        checkBlacklist(blacklist);
                        if(CreatureManager.getCreatureManager().getString(blacklist).contains(id + ",")) 
                        {
                            event.setCancelled(true);
                            event.getEntity().remove();
                            
                            if (CreatureManager.getCreatureManager().getBoolean("MobSpawn." + eWorld + ".Breeding.Debug.ToConsole") == true){
                                Log.info("[iSafe]" + " A(n) " + name + " was cancelled its spawn, for the spawn reason: Breeding; In the world: " + eWorld);
                            }
                        }
                    }
                }
            }
        }
    }
    
    @EventHandler
    public void pistonBlacklist(BlockPistonExtendEvent event) {
        if(event.isCancelled()) {
            return;
        }
        
        for(World world : Bukkit.getServer().getWorlds()) {
            String worldname = world.getName();
            String pWorld = event.getBlock().getWorld().getName();
            String blacklist = "PistonExtend." + pWorld + ".Blacklist";
            String state = "PistonExtend." + pWorld + ".Enabled";
            String sticky = "PistonExtend." + pWorld + ".CheckStickyPistons";
            
            checkBlacklist(blacklist);
            
            if(pWorld.equalsIgnoreCase(worldname)) 
            {
                if(BlacklistsF.getBlacklists().getBoolean(state) == true) 
                {
                    for(Block b : event.getBlocks()) 
                    {
                        int id = b.getTypeId();
                        if(BlacklistsF.getBlacklists().getString(blacklist).contains(id + ",")) 
                        {
                            if(event.isSticky()) {
                                if(BlacklistsF.getBlacklists().getBoolean(sticky) == false) 
                                {
                                    return;
                                } else {
                                    event.setCancelled(true);
                                }
                            } else {
                                event.setCancelled(true);
                            }
                            
                            if (event.isCancelled()) 
                            {
                                if (BlacklistsF.getBlacklists().getBoolean("PistonExtend." + pWorld + ".Alert/log.ToConsole") == true) {
                                     Log.info("[iSafe] " + "A piston was prevented from extending, because it tried to extend the blacklisted block '" + b.getType().name().toLowerCase() 
                                            + "' in the world '" + pWorld + "'. Sticky piston? " + event.isSticky());
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
