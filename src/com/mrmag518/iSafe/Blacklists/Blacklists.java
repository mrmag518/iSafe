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
        String value = plugin.getBlacklists().getString(path);
        
        if(value.equalsIgnoreCase("") || value.equalsIgnoreCase(" ")) {
            return;
        } else {
            if(!value.endsWith(",")) {
                plugin.getBlacklists().set(path, value + ",");
                plugin.saveBlacklists();
            }
        }
    }
    
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
                if(plugin.getBlacklists().getBoolean(state) == true) 
                {
                    if(plugin.getBlacklists().getString(blacklist).contains(id + ",")) 
                    {
                        if(!plugin.hasBlacklistPermission(p, "iSafe.bypass.blacklist.break")) 
                        {
                            if (plugin.getBlacklists().getBoolean("Break." + pWorld + ".Gamemode.PreventFor.Survival", true)) {
                                if(p.getGameMode().equals(GameMode.SURVIVAL)) {
                                    event.setCancelled(true);
                                }
                            }

                            if (plugin.getBlacklists().getBoolean("Break." + pWorld + ".Gamemode.PreventFor.Creative", true)) {
                                if(p.getGameMode().equals(GameMode.CREATIVE)) {
                                    event.setCancelled(true);
                                }
                            }

                            if (plugin.getBlacklists().getBoolean("Break." + pWorld + ".KickPlayer", true)){
                                if (event.isCancelled()){
                                    p.kickPlayer(plugin.blacklistBreakKickMsg(b));
                                }    
                            }

                            if(plugin.getBlacklists().getBoolean("Break." + pWorld + ".Alert/log.ToPlayer", true)) {
                                if(event.isCancelled()) {
                                    p.sendMessage(plugin.blacklistBreakMsg(b));
                                }
                            }

                            if (plugin.getBlacklists().getBoolean("Break." + pWorld + ".Alert/log.ToConsole", true)){
                                if (event.isCancelled()) {
                                    plugin.log.info("[iSafe] " + p.getName() + " was prevented from breaking the "
                                            + "blacklisted block '" + name + "' in the world '" + pWorld + "'.");
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
                if(plugin.getBlacklists().getBoolean(state) == true) 
                {
                    if(plugin.getBlacklists().getString(blacklist).contains(id + ",")) 
                    {
                        if(!plugin.hasBlacklistPermission(p, "iSafe.bypass.blacklist.place")) 
                        {
                            if (plugin.getBlacklists().getBoolean("Place." + pWorld + ".Gamemode.PreventFor.Survival", true)) {
                                if(p.getGameMode().equals(GameMode.SURVIVAL)) {
                                    event.setCancelled(true);
                                }
                            } 

                            if (plugin.getBlacklists().getBoolean("Place." + pWorld + ".Gamemode.PreventFor.Creative", true)) {
                                if(p.getGameMode().equals(GameMode.CREATIVE)) {
                                    event.setCancelled(true);
                                }
                            }

                            if (plugin.getBlacklists().getBoolean("Place." + pWorld + ".KickPlayer", true)){
                                if (event.isCancelled()) {
                                    p.kickPlayer(plugin.blacklistPlaceKickMsg(b));
                                }    
                            }

                            if (plugin.getBlacklists().getBoolean("Place." + pWorld + ".Alert/log.ToConsole", true)){
                                if (event.isCancelled()) {
                                    plugin.log.info("[iSafe] " + p.getName() + " was prevented from placing the "
                                            + "blacklisted block '" + name + "' in the world '" + pWorld + "'.");
                                }
                            }

                            if (plugin.getBlacklists().getBoolean("Place." + pWorld + ".Alert/log.ToPlayer", true)){
                                if (event.isCancelled()) {
                                    p.sendMessage(plugin.blacklistPlaceMsg(b));
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
            
            for(String command : plugin.getBlacklists().getStringList(blacklist)) 
            {
                if(pWorld.equalsIgnoreCase(worldname)) 
                {
                    if(plugin.getBlacklists().getBoolean(state) == true) 
                    {
                        if(line.startsWith(command.toLowerCase())) 
                        {
                            if(!plugin.hasBlacklistPermission(p, "iSafe.bypass.blacklist.command")) 
                            {
                                event.setCancelled(true);
                                
                                if (plugin.getBlacklists().getBoolean("Command." + pWorld + ".Alert/log.ToConsole", true)){
                                    if (event.isCancelled()) {
                                        plugin.log.info("[iSafe] " + p.getName() + " was prevented from doing the "
                                                    + "blacklisted command '" + command + "' in the world '" + pWorld + "'.");
                                    }
                                }

                                if (plugin.getBlacklists().getBoolean("Command." + pWorld + ".Alert/log.ToPlayer", true)){
                                    if (event.isCancelled()) {
                                        p.sendMessage(plugin.blacklistCommandMsg(command, worldname));
                                    }
                                }

                                if (plugin.getBlacklists().getBoolean("Command." + pWorld + ".KickPlayer", true)){
                                    if (event.isCancelled()) {
                                        p.sendMessage(plugin.blacklistCommandKickMsg(command, worldname));
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
            plugin.debugLog("[CraftBlacklist]" + he.getName() + " is a Player.");
            p = (Player)he;
        } else {
            plugin.debugLog("[CraftBlacklist]" + he.getName() + " is not a Player. say wut?!");
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
                if(plugin.getBlacklists().getBoolean(state) == true) 
                {
                    if(plugin.getBlacklists().getString(blacklist).contains(id + ",")) 
                    {
                        if(!plugin.hasBlacklistPermission(p, "iSafe.bypass.blacklist.crafting")) 
                        {
                           if (plugin.getBlacklists().getBoolean("Crafting." + pWorld + ".Gamemode.PreventFor.Survival", true)) {
                                if(he.getGameMode().equals(GameMode.SURVIVAL)) {
                                    event.setCancelled(true);
                                }
                            } 

                            if (plugin.getBlacklists().getBoolean("Crafting." + pWorld + ".Gamemode.PreventFor.Creative", true)) {
                                if(he.getGameMode().equals(GameMode.CREATIVE)) {
                                    event.setCancelled(true);
                                }
                            }

                            if (plugin.getBlacklists().getBoolean("Crafting." + pWorld + ".Alert/log.ToConsole", true)){
                                if (event.isCancelled()) {
                                    plugin.log.info("[iSafe] " + p.getName() + " was prevented from crafting the "
                                                + "blacklisted recipe '" + name + "' in the world '" + pWorld + "'.");
                                }
                            }

                            if (plugin.getBlacklists().getBoolean("Crafting." + pWorld + ".Alert/log.ToPlayer", true)){
                                if (event.isCancelled()) {
                                    p.sendMessage(plugin.blacklistCraftingMsg(name, p.getWorld()));
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
                if(plugin.getBlacklists().getBoolean(state) == true) 
                {
                    if(plugin.getBlacklists().getString(blacklist).contains(id + ",")) 
                    {
                        event.setCancelled(true);
                        if(plugin.getBlacklists().getBoolean("Dispense." + bWorld + ".Alert/log-to.Console", true)) {
                            plugin.log.info("[iSafe] " + " The block '" + name + "' was prevented from being dispensed"
                                    + " in the world '" + bWorld + "'.");
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
                if(plugin.getBlacklists().getBoolean(state) == true) 
                {
                    if(plugin.getBlacklists().getString(blacklist).contains(id + ",")) 
                    {
                        if(!plugin.hasBlacklistPermission(p, "iSafe.bypass.blacklist.drop")) 
                        {
                            if (plugin.getBlacklists().getBoolean("Drop." + pWorld + ".Gamemode.PreventFor.Survival", true)) {
                                if(p.getGameMode().equals(GameMode.SURVIVAL)) {
                                    event.setCancelled(true);
                                }
                            }

                            if (plugin.getBlacklists().getBoolean("Drop." + pWorld + ".Gamemode.PreventFor.Creative", true)) {
                                if(p.getGameMode().equals(GameMode.CREATIVE)) {
                                    event.setCancelled(true);
                                }
                            }

                            if (plugin.getBlacklists().getBoolean("Drop." + pWorld + ".KickPlayer", true))
                            {
                                if (event.isCancelled()){
                                    p.kickPlayer(plugin.blacklistDropKickMsg(event.getItemDrop()));
                                }    
                            }

                            if (plugin.getBlacklists().getBoolean("Drop." + pWorld + ".Alert/log.ToConsole", true))
                            {
                                if (event.isCancelled()) {
                                    plugin.log.info("[iSafe] " + p.getName() + " was prevented from droping the "
                                                + "blacklisted item '" + name + "' in the world '" + pWorld + "'.");
                                }
                            }

                            if (plugin.getBlacklists().getBoolean("Drop." + pWorld + ".Alert/log.ToPlayer", true))
                            {
                                if (event.isCancelled()) {
                                    p.sendMessage(plugin.blacklistDropMsg(name, p.getWorld()));
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
                    if(plugin.getBlacklists().getBoolean(state) == true) 
                    {
                        if(plugin.getBlacklists().getString(blacklist).contains(id + ",")) 
                        {
                            if(!plugin.hasBlacklistPermission(p, "iSafe.bypass.blacklist.interact"))
                            {
                                if (plugin.getBlacklists().getBoolean("Interact." + pWorld + ".Gamemode.PreventFor.Survival", true)) {
                                    if(p.getGameMode().equals(GameMode.SURVIVAL)) {
                                        event.setCancelled(true);
                                    }
                                }

                                if (plugin.getBlacklists().getBoolean("Interact." + pWorld + ".Gamemode.PreventFor.Creative", true)) {
                                    if(p.getGameMode().equals(GameMode.CREATIVE)) {
                                        event.setCancelled(true);
                                    }
                                }

                                if (plugin.getBlacklists().getBoolean("Interact." + pWorld + ".KickPlayer", true)){
                                    if (event.isCancelled()){
                                        p.kickPlayer(plugin.blacklistInteractKickMsg(b));
                                    }
                                }

                                if (plugin.getBlacklists().getBoolean("Interact." + pWorld + ".Alert/log.ToPlayer", true)){
                                    if (event.isCancelled()){
                                        p.sendMessage(plugin.blacklistInteractMsg(b));
                                    }
                                }

                                if (plugin.getBlacklists().getBoolean("Interact." + pWorld + ".Alert/log.ToConsole", true)){
                                    if (event.isCancelled()){
                                        plugin.log.info("[iSafe] " + p.getName() + " was prevented from interacting with the "
                                                + "blacklisted block '" + name + "' in the world '" + pWorld + "'.");
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
                if(plugin.getBlacklists().getBoolean(state) == true) 
                {
                    if(plugin.getBlacklists().getString(blacklist).contains(id + ",")) 
                    {
                        if(!plugin.hasBlacklistPermission(p, "iSafe.bypass.blacklist.pickup")) 
                        {
                            if (plugin.getBlacklists().getBoolean("Pickup." + pWorld + ".Gamemode.PreventFor.Survival", true)) {
                                if(p.getGameMode().equals(GameMode.SURVIVAL)) {
                                    event.setCancelled(true);
                                }
                            }

                            if (plugin.getBlacklists().getBoolean("Pickup." + pWorld + ".Gamemode.PreventFor.Creative", true)) {
                                if(p.getGameMode().equals(GameMode.CREATIVE)) {
                                    event.setCancelled(true);
                                }
                            }

                            if (plugin.getBlacklists().getBoolean("Pickup." + pWorld + ".KickPlayer", true))
                            {
                                if (event.isCancelled())
                                {
                                    p.kickPlayer(plugin.blacklistPickupKickMsg(name));
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
            boolean useDetailedSearchMode = plugin.getBlacklists().getBoolean("Chat." + pWorld + ".UseDetailedSearchMode");
            // Test
            String whitelist = "Chat." + pWorld + ".Whitelist";
            
            for(String word : plugin.getBlacklists().getStringList(blacklist)) 
            {
                if(pWorld.equalsIgnoreCase(worldname)) 
                {
                    if(plugin.getBlacklists().getBoolean(state) == true) 
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
                            for(String wWord : plugin.getBlacklists().getStringList(whitelist)) {
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
                            if (plugin.getBlacklists().getBoolean("Chat." + pWorld + ".Alert/log.ToConsole", true)){
                                if (event.isCancelled()) {
                                    plugin.log.info("[iSafe] " + p.getName() + "'s message contained the blacklisted word: " + word);
                                }
                            }

                            if (plugin.getBlacklists().getBoolean("Chat." + pWorld + ".Alert/log.ToPlayer", true)){
                                if (event.isCancelled()) {
                                    p.sendMessage(plugin.blacklistCensorMsg(word, p.getWorld()));
                                }
                            }

                            if (plugin.getBlacklists().getBoolean("Chat." + pWorld + ".KickPlayer", true)){
                                if (event.isCancelled()) {
                                    p.kickPlayer(plugin.blacklistCensorKickMsg(word));
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
                if(plugin.getBlacklists().getBoolean(state) == true) 
                {
                    if(event.getSpawnReason() == SpawnReason.NATURAL)
                    {
                        String blacklist = "MobSpawn." + eWorld + ".Natural.Blacklist";
                        checkBlacklist(blacklist);
                        if(plugin.getBlacklists().getString(blacklist).contains(id + ",")) 
                        {
                            event.setCancelled(true);
                            event.getEntity().remove();
                            if (plugin.getCreatureManager().getBoolean("MobSpawn." + eWorld + ".Natural.Debug.ToConsole", true)){
                                plugin.log.info("[iSafe]" + " A(n) " + name + " was cancelled its spawn, for the spawn reason: Natural; In the world: " + eWorld);
                            }
                        }
                    }
                    
                    if(event.getSpawnReason() == SpawnReason.SPAWNER)
                    {
                        String blacklist = "MobSpawn." + eWorld + ".Spawner.Blacklist";
                        checkBlacklist(blacklist);
                        if(plugin.getBlacklists().getString(blacklist).contains(id + ",")) 
                        {
                            event.setCancelled(true);
                            event.getEntity().remove();
                            if (plugin.getCreatureManager().getBoolean("MobSpawn." + eWorld + ".Spawner.Debug.ToConsole", true)){
                                plugin.log.info("[iSafe]" + " A(n) " + name + " was cancelled its spawn, for the spawn reason: Spawner; In the world: " + eWorld);
                            }
                        }
                    }
                    
                    if(event.getSpawnReason() == SpawnReason.CUSTOM)
                    {
                        String blacklist = "MobSpawn." + eWorld + ".Custom.Blacklist";
                        checkBlacklist(blacklist);
                        if(plugin.getBlacklists().getString(blacklist).contains(id + ",")) 
                        {
                            event.setCancelled(true);
                            event.getEntity().remove();
                            if (plugin.getCreatureManager().getBoolean("MobSpawn." + eWorld + ".Custom.Debug.ToConsole", true)){
                                plugin.log.info("[iSafe]" + " A(n) " + name + " was cancelled its spawn, for the spawn reason: Custom; In the world: " + eWorld);
                            }
                        }
                    }
                    
                    if(event.getSpawnReason() == SpawnReason.SPAWNER_EGG)
                    {
                        String blacklist = "MobSpawn." + eWorld + ".SpawnerEgg.Blacklist";
                        checkBlacklist(blacklist);
                        if(plugin.getBlacklists().getString(blacklist).contains(id + ",")) 
                        {
                            event.setCancelled(true);
                            event.getEntity().remove();
                            if (plugin.getCreatureManager().getBoolean("MobSpawn." + eWorld + ".SpawnerEgg.Debug.ToConsole", true)){
                                plugin.log.info("[iSafe]" + " A(n) " + name + " was cancelled its spawn, for the spawn reason: SpawnerEgg; In the world: " + eWorld);
                            }
                        }
                    }
                    
                    if(event.getSpawnReason() == SpawnReason.CHUNK_GEN)
                    {
                        String blacklist = "MobSpawn." + eWorld + ".ChunkGen.Blacklist";
                        checkBlacklist(blacklist);
                        if(plugin.getBlacklists().getString(blacklist).contains(id + ",")) 
                        {
                            event.setCancelled(true);
                            event.getEntity().remove();
                            if (plugin.getCreatureManager().getBoolean("MobSpawn." + eWorld + ".ChunkGen.Debug.ToConsole", true)){
                                plugin.log.info("[iSafe]" + " A(n) " + name + " was cancelled its spawn, for the spawn reason: ChunkGen; In the world: " + eWorld);
                            }
                        }
                    }
                    
                    if(event.getSpawnReason() == SpawnReason.BREEDING)
                    {
                        String blacklist = "MobSpawn." + eWorld + ".Breeding.Blacklist";
                        checkBlacklist(blacklist);
                        if(plugin.getBlacklists().getString(blacklist).contains(id + ",")) 
                        {
                            event.setCancelled(true);
                            event.getEntity().remove();
                            if (plugin.getCreatureManager().getBoolean("MobSpawn." + eWorld + ".Breeding.Debug.ToConsole", true)){
                                plugin.log.info("[iSafe]" + " A(n) " + name + " was cancelled its spawn, for the spawn reason: Breeding; In the world: " + eWorld);
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
                if(plugin.getBlacklists().getBoolean(state) == true) 
                {
                    for(Block b : event.getBlocks()) 
                    {
                        int id = b.getTypeId();
                        if(plugin.getBlacklists().getString(blacklist).contains(id + ",")) 
                        {
                            if(event.isSticky()) {
                                if(plugin.getBlacklists().getBoolean(sticky) == false) 
                                {
                                    return;
                                } else {
                                    event.setCancelled(true);
                                }
                            } else {
                                event.setCancelled(true);
                            }
                            
                            if (plugin.getBlacklists().getBoolean("PistonExtend." + pWorld + ".Alert/log.ToConsole", true)){
                                if (event.isCancelled()) {
                                    plugin.log.info("[iSafe] " + "A piston was prevented from extending, "
                                            + "because it tried to extend the blacklisted block '" + b.getType().name().toLowerCase() 
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
