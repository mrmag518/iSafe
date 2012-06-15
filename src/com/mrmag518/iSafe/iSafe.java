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

package com.mrmag518.iSafe;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.File;
import java.net.URL;

import javax.xml.parsers.DocumentBuilderFactory;

import com.mrmag518.iSafe.Blacklists.*;
import com.mrmag518.iSafe.Commands.*;
import com.mrmag518.iSafe.Events.Block.*;
import com.mrmag518.iSafe.Events.Entity.*;
import com.mrmag518.iSafe.Events.Various.*;
import com.mrmag518.iSafe.Events.World.*;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class iSafe extends JavaPlugin {
    
    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    //Remember to change this on every version!
    
    public String fileversion = "iSafe v2.71";
    
    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    
    public PlayerListener playerListener = null;
    public BlockListener blockListener = null;
    public EntityListener entityListener = null;
    public WeatherListener weatherListener = null;
    public InventoryListener inventoryListener = null;
    public VehicleListener vehicleListener = null;
    public WorldListener worldListener = null;
    public EnchantmentListener enchantmentListener = null;
    public DropListener dropListener = null;
    
    public UserFileCreator UFC = null;
    public SendUpdate sendUpdate = null;
    
    public DropBlacklist dropBlacklist = null;
    public PlaceBlacklist placeBlacklist = null;
    public BreakBlacklist breakBlacklist = null;
    public PickupBlacklist pickupBlacklist = null;
    public CommandBlacklist commandBlacklist = null;
    public MobSpawnBlacklist mobSpawnBlacklist = null;
    public Censor censor = null;
    
    private Reload reloadcmd = null;
    private Info isafeInfocmd = null;
    private Serverinfo serverinfocmd = null;
    private Superbreak superbreakcmd = null;
    private Stopserver stopServercmd = null;
    private ClearDrops cleardropscmd = null;
    
    public String version = null;
    public String newVersion = null;
    
    public static iSafe plugin;
    
    public final Logger log = Logger.getLogger("Minecraft");
    
    public FileConfiguration mobsConfig = null;
    public File mobsConfigFile = null;
    public FileConfiguration blacklist = null;
    public File blacklistFile = null;
    public FileConfiguration config;
    
    public Set<Player> superbreak = new HashSet<Player>();
    
    @Override
    public void onDisable() {
        PluginDescriptionFile pdffile = this.getDescription();
        log.info("[" + pdffile.getName() + " :: " + version + "] " + " Disabled succesfully.");
    }
    
    @Override
    public void onEnable() {
        version = this.getDescription().getVersion();
        
        playerListener = new PlayerListener(this);
        blockListener = new BlockListener(this);
        entityListener = new EntityListener(this);
        worldListener = new WorldListener(this);
        vehicleListener = new VehicleListener(this);
        weatherListener = new WeatherListener(this);
        inventoryListener = new InventoryListener(this);
        enchantmentListener = new EnchantmentListener(this);
        dropListener = new DropListener(this);
        
        UFC = new UserFileCreator(this);
        sendUpdate = new SendUpdate(this);
        
        dropBlacklist = new DropBlacklist(this);
        placeBlacklist = new PlaceBlacklist(this);
        breakBlacklist = new BreakBlacklist(this);
        pickupBlacklist = new PickupBlacklist(this);
        commandBlacklist = new CommandBlacklist(this);
        mobSpawnBlacklist = new MobSpawnBlacklist(this);
        censor = new Censor(this);
        
        reloadcmd = new Reload(this);
        isafeInfocmd = new Info(this);
        serverinfocmd = new Serverinfo(this);
        superbreakcmd = new Superbreak(this);
        stopServercmd = new Stopserver(this);
        cleardropscmd = new ClearDrops(this);
        
        PluginDescriptionFile pdffile = this.getDescription();
        
        if(!(this.getDataFolder().exists())) {
            log.info("[iSafe]" + " iSafe folder not found, creating a new one.");
            this.getDataFolder().mkdirs(); 
        }
        
        File usersFolder = new File(getDataFolder() + File.separator + "Users");
        if(!(usersFolder.exists())) {
            usersFolder.mkdir();
        }
        
        config = this.getConfig();
        loadConfig();
        reloadConfig();
        
        blacklist = this.getBlacklist();
        loadBlacklist();
        reloadBlacklist();
        
        mobsConfig = this.getMobsConfig();
        loadMobsConfig();
        reloadMobsConfig();
        
        //Update checker - From MilkBowl.
        this.getServer().getScheduler().scheduleAsyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                try {
                    newVersion = updateCheck(version);
                    String oldVersion = version;
                    
                    if (!newVersion.contains(oldVersion)) {
                        log.info("-----  iSafe UpdateChecker  -----");
                        log.info("You are not using the recommended build of iSafe; "+ newVersion);
                        log.info("You are currently using v" + oldVersion);
                        log.info("Please use the latest recommended build of iSafe.("+newVersion+")");
                        log.info("You can find this version at: http://dev.bukkit.org/server-mods/blockthattnt/files/");
                        log.info("-----  -------------------  -----");
                    }
                } catch (Exception ignored) {
                    //Ignored
                }
            }
        }, 0, 432000);
        
        executeCommands();
        getServer().getPluginManager().getPermissions();
        
        if(!(pdffile.getFullName().equals(fileversion))) {
            log.info("-----  iSafe vMatchConflict  -----");
            log.warning("[iSafe] The version in the pdffile is not the same as the file.");
            log.info("[iSafe] pdffile version: "+ pdffile.getFullName());
            log.info("[iSafe] File version: "+ fileversion);
            log.warning("[iSafe] Please deliver this infomation to "+ pdffile.getAuthors() +" at BukkitDev.");
            log.info("-----  --------------------  -----");
        } else {
            log.info("[iSafe] The file and pdffile versions matched eachother correctly.");
        }
        
        //blacklistDebug();
        
        log.info("[" + pdffile.getName() + " :: " + version + "] " + " Enabled succesfully.");
    }
    
    //Update checker - From MilkBowl's Vault.
    public String updateCheck(String currentVersion) throws Exception {
        String pluginUrlString = "http://dev.bukkit.org/server-mods/blockthattnt/files.rss";
        try {
            URL url = new URL(pluginUrlString);
             Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(url.openConnection().getInputStream());
             doc.getDocumentElement().normalize();
             NodeList nodes = doc.getElementsByTagName("item");
             Node firstNode = nodes.item(0);
             if (firstNode.getNodeType() == 1) {
                 Element firstElement = (Element) firstNode;
                 NodeList firstElementTagName = firstElement.getElementsByTagName("title");
                 Element firstNameElement = (Element) firstElementTagName.item(0);
                 NodeList firstNodes = firstNameElement.getChildNodes();
                 return firstNodes.item(0).getNodeValue();
             }
        } catch (Exception ignored) {
            //Ingored
        }
        return currentVersion;
    }
    
    public void executeCommands() {
        getCommand("iSafe-reload").setExecutor(reloadcmd);
        getCommand("iSafe-info").setExecutor(isafeInfocmd);
        getCommand("serverinfo").setExecutor(serverinfocmd);
        getCommand("superbreak").setExecutor(superbreakcmd);
        getCommand("stopserver").setExecutor(stopServercmd);
        getCommand("cleardrops").setExecutor(cleardropscmd);
    }
    
    public void loadConfig() {
        config = getConfig();
        config.options().header(Data.setConfigHeader());
        
        config.addDefault("Enchantment.Prevent-Enchantment", false);
        config.addDefault("Enchantment.Prevent-creativeEnchanting", false);
        
        config.addDefault("EntityTo-SpawnLocation.On-Void-fall(Player)", false);
        config.addDefault("EntityTo-SpawnLocation.On-Void-fall(Creature)", false);
        
        config.addDefault("Buckets.Prevent-LavaBucket-empty", true);
        config.addDefault("Buckets.Lava.Worlds", Arrays.asList(Data.lbworldslist));
        Data.lbworlds = config.getStringList("Buckets.Lava.Worlds");
        
        config.addDefault("Buckets.Prevent-WaterBucket-empty", false);
        config.addDefault("Buckets.Water.Worlds", Arrays.asList(Data.wbworldslist));
        Data.wbworlds = config.getStringList("Buckets.Water.Worlds");
        
        config.addDefault("Flow.Disable-water-flow", false);
        config.addDefault("Flow.Disable-lava-flow", false);
        config.addDefault("Flow.Disable-air-flow", false);
        
        config.addDefault("Piston.Prevent-piston-Extend", false);
        config.addDefault("Piston.Prevent-piston-Retract", false);
        
        config.addDefault("Furnace.Disable-furnace-burning", false);
        config.addDefault("Furnace.Disable-furnace-smelting", false);
        
        config.addDefault("Physics.Disable-sand-physics", false);
        config.addDefault("Physics.Disable-gravel-physics", false);
        
        config.addDefault("Fade.Prevent-Ice-melting", false);
        config.addDefault("Fade.Prevent-Snow-melting", false);
        
        config.addDefault("Chunk.Prevent-chunks-unloading", false);
        config.addDefault("Chunk.Enable-Chunk-emergency-loader", false);
        
        config.addDefault("Enviroment-Damage.Prevent-Fire-spread", false);
        config.addDefault("Enviroment-Damage.Allow-Flint_and_steel-usage", true);
        config.addDefault("Enviroment-Damage.Allow-Enviroment-ignition", true);
        
        config.addDefault("Chat.Enable-Chat-permissions", false);
        config.addDefault("Chat.Prevent-arrow-to-the-knee-jokes", false);
        config.addDefault("Chat.Punish-arrow-to-the-knee-jokes", false);
        
        config.addDefault("Weather.Disable-LightningStrike", false);
        config.addDefault("Weather.Disable-Storm", false);
        config.addDefault("Weather.Disable-Thunder", false);
        
        config.addDefault("Vehicle.Prevent.enter.Minecarts", false);
        config.addDefault("Vehicle.Prevent.enter.Boats", false);
        config.addDefault("Vehicle.Prevent.destroy.Minecarts", false);
        config.addDefault("Vehicle.Prevent.destroy.Boats", false);
        
        config.addDefault("Teleport.Disallow-Teleporting", false);
        config.addDefault("Teleport.Prevent-TeleportCause.Command", false);
        config.addDefault("Teleport.Prevent-TeleportCause.EnderPearl", false);
        config.addDefault("Teleport.Prevent-TeleportCause.Plugin", false);
        config.addDefault("Teleport.Prevent-TeleportCause.Unknown", false);
        
        config.addDefault("Misc.Prevent-expBottle-throw", false);
        config.addDefault("Misc.Enable-kick-messages", false);
        config.addDefault("Misc.Disable-LeavesDecay", false);
        config.addDefault("Misc.Prevent-crop-trampling-by-creature", false);
        config.addDefault("Misc.Prevent-crop-trampling-by-player", false);
        config.addDefault("Misc.Prevent-portal-creation", false);
        config.addDefault("Misc.Prevent-BlockGrow", false);
        
        config.addDefault("Gamemode.Prevent-Gamemode-change", false);
        config.addDefault("Gamemode.Prevent-Gamemode-to-CreativeMode-change", false);
        config.addDefault("Gamemode.Prevent-Gamemode-to-SurvivalMode-change", false);
        config.addDefault("Gamemode.Change-to-SurvivalMode-onQuit", false);
        config.addDefault("Gamemode.Change-to-CreativeMode-onQuit", false);
        
        config.addDefault("World.Register-world(s)-init", true);
        config.addDefault("World.Register-world(s)-unload", true);
        config.addDefault("World.Register-world(s)-save", true);
        config.addDefault("World.Register-world(s)-load", true);
        config.addDefault("World.Disable-ExperienceOrbs-drop", false);
        config.addDefault("World.Prevent-items/objects-to-spawn-into-the-world", false);
        config.addDefault("World.Prevent-items/objects-spawning-inside-vehicles", false);
        config.addDefault("World.Prevent-naturally-object-dispensing", false);
        config.addDefault("World.Force-blocks-to-be-buildable", false);
        config.addDefault("World.Prevent-blocks-spreading", false);
        
        config.addDefault("Explosions.Debug-explosions", false);
        config.addDefault("Explosions.Disable-primed-explosions", false);
        config.addDefault("Explosions.Prevent-creeper-death-on-explosion", false);
        config.addDefault("Explosions.Disable-explosions", false);
        config.addDefault("Explosions.Disable-Creeper-explosions", false);
        config.addDefault("Explosions.Disable-EnderDragon-blockdamage", false);
        config.addDefault("Explosions.Disable-TNT-explosions", false);
        config.addDefault("Explosions.Disable-Fireball-explosions", false);
        config.addDefault("Explosions.Disable-EnderCrystal-explosions", false);
        config.addDefault("Explosions.Disable-(Block)Explosion-damage.To-Players", false);
        config.addDefault("Explosions.Disable-(Block)Explosion-damage.To-Creatures", false);
        config.addDefault("Explosions.Disable-(Entity)Explosion-damage.To-Players", false);
        config.addDefault("Explosions.Disable-(Entity)Explosion-damage.To-Creatures", false);
        
        config.addDefault("Structure.Prevent-structure-growth.BIG_TREE", false);
        config.addDefault("Structure.Prevent-structure-growth.BIRCH", false);
        config.addDefault("Structure.Prevent-structure-growth.BROWN_MUSHROOM", false);
        config.addDefault("Structure.Prevent-structure-growth.REDWOOD", false);
        config.addDefault("Structure.Prevent-structure-growth.RED_MUSHROOM", false);
        config.addDefault("Structure.Prevent-structure-growth.TALL_REDWOOD", false);
        config.addDefault("Structure.Prevent-structure-growth.TREE", false);
        config.addDefault("Structure.Prevent-structure-growth.JUNGLE", false);
        config.addDefault("Structure.Prevent-structure-growth.TALL_REDWOOD", false);
        config.addDefault("Structure.Prevent-bonemeal-usage", false);
        config.addDefault("Structure.Prevent-strcuture-growth", false);
        
        config.addDefault("Entity-Damage.Disable-npc(Villagers)-death/damage", false);
        config.addDefault("Entity-Damage.Disable-player-death/damage", false);
        config.addDefault("Entity-Damage.Enable-permissions", false);
        //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
        config.addDefault("Entity-Damage.Players.Disable-Fire-damage", false);
        config.addDefault("Entity-Damage.Players.Disable-Contact-damage", false);
        config.addDefault("Entity-Damage.Players.Disable-Custom-damage", false);
        config.addDefault("Entity-Damage.Players.Disable-Drowning-damage", false);
        config.addDefault("Entity-Damage.Players.Disable-EntityAttack-damage", false);
        config.addDefault("Entity-Damage.Players.Disable-Fall-damage", false);
        config.addDefault("Entity-Damage.Players.Disable-Lava-damage", false);
        config.addDefault("Entity-Damage.Players.Disable-Lightning-damage", false);
        config.addDefault("Entity-Damage.Players.Disable-Magic-damage", false);
        config.addDefault("Entity-Damage.Players.Disable-Poison-damage", false);
        config.addDefault("Entity-Damage.Players.Disable-Projectile-damage", false);
        config.addDefault("Entity-Damage.Players.Disable-Starvation-damage", false);
        config.addDefault("Entity-Damage.Players.Disable-Suffocation-damage", false);
        config.addDefault("Entity-Damage.Players.Disable-Suicide-damage", false);
        config.addDefault("Entity-Damage.Players.Disable-Void-damage", false);
        //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
        config.addDefault("Entity-Damage.Creatures.Disable-Fire-damage", false);
        config.addDefault("Entity-Damage.Creatures.Disable-Contact-damage", false);
        config.addDefault("Entity-Damage.Creatures.Disable-Custom-damage", false);
        config.addDefault("Entity-Damage.Creatures.Disable-Drowning-damage", false);
        config.addDefault("Entity-Damage.Creatures.Disable-EntityAttack-damage", false);
        config.addDefault("Entity-Damage.Creatures.Disable-Fall-damage", false);
        config.addDefault("Entity-Damage.Creatures.Disable-Lava-damage", false);
        config.addDefault("Entity-Damage.Creatures.Disable-Lightning-damage", false);
        config.addDefault("Entity-Damage.Creatures.Disable-Magic-damage", false);
        config.addDefault("Entity-Damage.Creatures.Disable-Poison-damage", false);
        config.addDefault("Entity-Damage.Creatures.Disable-Projectile-damage", false);
        config.addDefault("Entity-Damage.Creatures.Disable-Starvation-damage", false);
        config.addDefault("Entity-Damage.Creatures.Disable-Suffocation-damage", false);
        config.addDefault("Entity-Damage.Creatures.Disable-Suicide-damage", false);
        config.addDefault("Entity-Damage.Creatures.Disable-Void-damage", false);
        
        config.addDefault("Player.Prevent-fullbright-hacking(force lightlevel)", false);
        config.addDefault("Player.Prevent-Sprinting", false);
        config.addDefault("Player.Prevent-Sneaking", false);
        config.addDefault("Player.Enable-fishing-permissions", false);
        config.addDefault("Player.Broadcast-iSafe-message-on-join", false);
        config.addDefault("Player.Allow-creative-gamemode-on-player-quit", true);
        config.addDefault("Player.Disable-Hunger", false);
        config.addDefault("Player.Enable-Bed-permissions", false);
        config.addDefault("Player.Enable-fishing-permissions", false);
        config.addDefault("Player.Only-let-OPs-join", false);
        config.addDefault("Player.Log-commands", true);
        config.addDefault("Player.Disable-all-commands", false);
        config.addDefault("Player.Infinite-itemtacks", false);
        config.addDefault("Player.Kick-player-if-anther-user-with-same-username-log's-on", true);
        config.addDefault("Player.Instantbreak", false);
        //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
        //--
        config.addDefault("Player-Interact.Erase the old values if you haven't already(this node is not an option)", "#");
        //--
        config.addDefault("Player-Interact.Disable.Buttons", false);
        config.addDefault("Player-Interact.Disable.Chests", false);
        config.addDefault("Player-Interact.Disable.Dispensers", false);
        config.addDefault("Player-Interact.Disable.Woodendoors", false);
        config.addDefault("Player-Interact.Disable.Irondoors", false);
        config.addDefault("Player-Interact.Disable.Levers", false);
        config.addDefault("Player-Interact.Disable.StonePressurePlates", false);
        config.addDefault("Player-Interact.Disable.WoodenPressurePlates", false);
        config.addDefault("Player-Interact.Disable.Trapdoors", false);
        config.addDefault("Player-Interact.Disable.WoodenFenceGates", false);
        //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
        config.addDefault("Entity/Player.Completely-Prevent.Health-Regeneration", false);
        config.addDefault("Entity/Player.Prevent.Custom-Health-Regeneration", false);
        config.addDefault("Entity/Player.Prevent.Eating-Health-Regeneration", false);
        config.addDefault("Entity/Player.Prevent.Regen-Health-Regeneration", false);
        config.addDefault("Entity/Player.Prevent.Satiated-Health-Regeneration", false);
        config.addDefault("Entity/Player.Prevent.Magic-Health-Regeneration", false);
        config.addDefault("Entity/Player.Prevent.MagicRegen-Health-Regeneration", false);
        //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
        config.addDefault("PlayerInteractEntity.Prevent-snowball-hitting-player", false);
        config.addDefault("PlayerInteractEntity.Prevent-arrow-hitting-player", false);
        //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
        config.addDefault("Drop-configure.Glass.Drop.Glass", false);
        config.addDefault("Drop-configure.Mobspawner.Drop.Mobspawner", false);
        config.addDefault("Drop-configure.Ice.Drop.Ice", false);
        config.addDefault("Drop-configure.Ice.Drop.Ice-options.Prevent-water", false);
        config.addDefault("Drop-configure.Bedrock.Drop.Bedrock", false);
        config.addDefault("Drop-configure.Bookshelf.Drop.Bookshelf", false);
        config.addDefault("Drop-configure.Grass_thingy.Drop.Grass_thingy", false);
        
        this.getConfig().options().copyDefaults(true);
        saveConfig();
    }
    
    public void loadBlacklist() {
        blacklist.options().header(Data.setBlacklistHeader());
        
        blacklist.addDefault("Place.Complete-Disallow-placing", false);
        blacklist.addDefault("Place.Kick-Player", false);
        blacklist.addDefault("Place.Kill-Player", false);
        blacklist.addDefault("Place.Alert/log.To-console", true);
        blacklist.addDefault("Place.Alert/log.To-player", true);
        blacklist.addDefault("Place.Alert/log.To-server-chat", false);
        blacklist.addDefault("Place.Gamemode.PreventFor.Survival", true);
        blacklist.addDefault("Place.Gamemode.PreventFor.Creative", true);
        blacklist.addDefault("Place.Worlds", Arrays.asList(Data.worldslist));
        Data.worlds = blacklist.getStringList("Place.Worlds");
        blacklist.addDefault("Place.Blacklist", Arrays.asList(Data.placedblockslist));
        Data.placedblocks = blacklist.getStringList("Place.Blacklist");
        
        blacklist.addDefault("Break.Complete-Disallow-breaking", false);
        blacklist.addDefault("Break.Kick-Player", false);
        blacklist.addDefault("Break.Kill-Player", false);
        blacklist.addDefault("Break.Alert/log.To-console", true);
        blacklist.addDefault("Break.Alert/log.To-player", true);
        blacklist.addDefault("Break.Alert/log.To-server-chat", false);
        blacklist.addDefault("Break.Gamemode.PreventFor.Survival", true);
        blacklist.addDefault("Break.Gamemode.PreventFor.Creative", true);
        blacklist.addDefault("Break.Worlds", Arrays.asList(Data.worldslist));
        Data.worlds = blacklist.getStringList("Break.Worlds");
        blacklist.addDefault("Break.Blacklist", Arrays.asList(Data.brokenblockslist));
        Data.brokenblocks = blacklist.getStringList("Break.Blacklist");
        
        blacklist.addDefault("Drop.Complete-Disallow-droping", false);
        blacklist.addDefault("Drop.Kick-Player", false);
        blacklist.addDefault("Drop.Kill-Player", false);
        blacklist.addDefault("Drop.Alert/log.To-console", true);
        blacklist.addDefault("Drop.Alert/log.To-player", true);
        blacklist.addDefault("Drop.Alert/log.To-server-chat", false);
        blacklist.addDefault("Drop.Gamemode.PreventFor.Survival", true);
        blacklist.addDefault("Drop.Gamemode.PreventFor.Creative", true);
        blacklist.addDefault("Drop.Worlds", Arrays.asList(Data.worldslist));
        Data.worlds = blacklist.getStringList("Drop.Worlds");
        blacklist.addDefault("Drop.Blacklist", Arrays.asList(Data.dropedblockslist));
        Data.dropedblocks = blacklist.getStringList("Drop.Blacklist");
        
        blacklist.addDefault("Pickup.Complete-Disallow-pickuping", false);
        blacklist.addDefault("Pickup.Kick-Player", false);
        blacklist.addDefault("Pickup.Kill-Player", false);
        blacklist.addDefault("Pickup.Alert/log.To-console", true);
        blacklist.addDefault("Pickup.Alert/log.To-player", true);
        blacklist.addDefault("Pickup.Alert/log.To-server-chat", false);
        blacklist.addDefault("Pickup.Gamemode.PreventFor.Survival", true);
        blacklist.addDefault("Pickup.Gamemode.PreventFor.Creative", true);
        blacklist.addDefault("Pickup.Worlds", Arrays.asList(Data.Pickupworldslist));
        Data.Pickupworlds = blacklist.getStringList("Pickup.Worlds");
        blacklist.addDefault("Pickup.Blacklist", Arrays.asList(Data.pickupedblockslist));
        Data.pickupedblocks = blacklist.getStringList("Pickup.Blacklist");
        
        blacklist.addDefault("Command.Disallow-commands", false);
        blacklist.addDefault("Command.Alert/log.To-console", true);
        blacklist.addDefault("Command.Alert/log.To-player", true);
        blacklist.addDefault("Command.Alert/log.To-server-chat", false);
        blacklist.addDefault("Command.Worlds", Arrays.asList(Data.cmdworldlist));
        Data.cmdworlds = blacklist.getStringList("Command.Worlds");
        blacklist.addDefault("Command.Blacklist", Arrays.asList(Data.commandslist));
        Data.commands = blacklist.getStringList("Command.Blacklist");
        
        blacklist.addDefault("Censor.Alert/log.To-console", false);
        blacklist.addDefault("Censor.Alert/log.To-player", true);
        blacklist.addDefault("Censor.Words/Blacklist", Arrays.asList(Data.censoredWordsList));
        Data.censoredWords = blacklist.getStringList("Censor.Words/Blacklist");
        
        this.getBlacklist().options().copyDefaults(true);
        saveBlacklist();
    }
    
    public void reloadBlacklist() {
        if (blacklistFile == null) {
            blacklistFile = new File(getDataFolder(), "blacklist.yml");
        }
        blacklist = YamlConfiguration.loadConfiguration(blacklistFile);
        
        // Look for defaults in the jar
        InputStream defConfigStream = getResource("blacklist.yml");
        if (defConfigStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            blacklist.setDefaults(defConfig);
        }
    }
    
    public FileConfiguration getBlacklist() {
        if (blacklist == null) {
            reloadBlacklist();
        }
        return blacklist;
    }
    
    public void saveBlacklist() {
        if (blacklist == null || blacklistFile == null) {
            return;
        }
        try {
            blacklist.save(blacklistFile);
        } catch (IOException ex) {
            Logger.getLogger(JavaPlugin.class.getName()).log(Level.SEVERE, "Error saving blacklist to " + blacklistFile, ex);
        }
    }
    
    public void reloadMobsConfig() {
        if (mobsConfigFile == null) {
            mobsConfigFile = new File(getDataFolder(), "mobsConfig.yml");
        }
        mobsConfig = YamlConfiguration.loadConfiguration(mobsConfigFile);
        
        // Look for defaults in the jar
        InputStream defConfigStream = getResource("mobsConfig.yml");
        if (defConfigStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            mobsConfig.setDefaults(defConfig);
        }
    }
    
    public FileConfiguration getMobsConfig() {
        if (mobsConfig == null) {
            reloadMobsConfig();
        }
        return mobsConfig;
    }
    
    public void saveMobsConfig() {
        if (mobsConfig == null || mobsConfigFile == null) {
            return;
        }
        try {
            mobsConfig.save(mobsConfigFile);
        } catch (IOException ex) {
            Logger.getLogger(JavaPlugin.class.getName()).log(Level.SEVERE, "Error saving blacklist to " + mobsConfigFile, ex);
        }
    }
    
    public void loadMobsConfig() {
        mobsConfig.options().header(Data.setmobsConfigHeader());
        
        mobsConfig.addDefault("EntityTarget.Disable-closest_player-target", false);
        mobsConfig.addDefault("EntityTarget.Disable-custom-target", false);
        mobsConfig.addDefault("EntityTarget.Disable-forgot_target-target", false);
        mobsConfig.addDefault("EntityTarget.Disable-owner_attacked_target-target", false);
        mobsConfig.addDefault("EntityTarget.Disable-pig_zombie_target-target", false);
        mobsConfig.addDefault("EntityTarget.Disable-random_target-target", false);
        mobsConfig.addDefault("EntityTarget.Disable-target_attacked_entity-target", false);
        mobsConfig.addDefault("EntityTarget.Disable-target_attacked_owner-target", false);
        mobsConfig.addDefault("EntityTarget.Disable-target_died-target", false);
        
        mobsConfig.addDefault("Powered-Creepers.Prevent-PowerCause.Lightning", false);
        mobsConfig.addDefault("Powered-Creepers.Prevent-PowerCause.Set-Off", false);
        mobsConfig.addDefault("Powered-Creepers.Prevent-PowerCause.Set-On", false);
        
        mobsConfig.addDefault("Misc.Endermen.Prevent-Endermen-griefing", false);
        mobsConfig.addDefault("Misc.DeathDrop.Disable-drops-onDeath", false);
        mobsConfig.addDefault("Misc.Tame.Prevent-taming", false);
        mobsConfig.addDefault("Misc.Prevent-SlimeSplit", false);
        mobsConfig.addDefault("Misc.Prevent-PigZap", false);
        mobsConfig.addDefault("Misc.Prevent-mobs-breaking-doors", false);
        
        mobsConfig.addDefault("MobSpawn.Natural.Debug.To-console", false);
        mobsConfig.addDefault("MobSpawn.Natural.Worlds", Arrays.asList(Data.worlds1list));
        Data.worlds1 = mobsConfig.getStringList("MobSpawn.Natural.Worlds");
        mobsConfig.addDefault("MobSpawn.Natural.Blacklist", Arrays.asList(Data.mobspawnnaturallist));
        Data.mobspawnnatural = mobsConfig.getStringList("MobSpawn.Natural.Blacklist");
        
        mobsConfig.addDefault("MobSpawn.Spawner.Debug.To-console", false);
        mobsConfig.addDefault("MobSpawn.Spawner.Worlds", Arrays.asList(Data.worlds2list));
        Data.worlds2 = mobsConfig.getStringList("MobSpawn.Spawner.Worlds");
        mobsConfig.addDefault("MobSpawn.Spawner.Blacklist", Arrays.asList(Data.mobspawnspawnerlist));
        Data.mobspawnspawner = mobsConfig.getStringList("MobSpawn.Spawner.Blacklist");
        
        mobsConfig.addDefault("MobSpawn.Custom.Debug.To-console", false);
        mobsConfig.addDefault("MobSpawn.Custom.Worlds", Arrays.asList(Data.worlds3list));
        Data.worlds3 = mobsConfig.getStringList("MobSpawn.Custom.Worlds");
        mobsConfig.addDefault("MobSpawn.Custom.Blacklist", Arrays.asList(Data.mobspawncustomlist));
        Data.mobspawncustom = mobsConfig.getStringList("MobSpawn.Custom.Blacklist");
        
        mobsConfig.addDefault("MobSpawn.Egg.Debug.To-console", false);
        mobsConfig.addDefault("MobSpawn.Egg.Worlds", Arrays.asList(Data.worlds4list));
        Data.worlds4 = mobsConfig.getStringList("MobSpawn.Egg.Worlds");
        mobsConfig.addDefault("MobSpawn.Egg.Blacklist", Arrays.asList(Data.mobspawnegglist));
        Data.mobspawnegg = mobsConfig.getStringList("MobSpawn.Egg.Blacklist");
        
        mobsConfig.addDefault("MobSpawn.SpawnerEgg.Do_not_insert_the_spawner_egg_ID_here,_Here_you'll_insert_the_Animal_ID_(Like:_Creeper,_60,_PIG)".trim(), "Thanks");
        mobsConfig.addDefault("MobSpawn.SpawnerEgg.Debug.To-console", false);
        mobsConfig.addDefault("MobSpawn.SpawnerEgg.Worlds", Arrays.asList(Data.worlds5list));
        Data.worlds5 = mobsConfig.getStringList("MobSpawn.SpawnerEgg.Worlds");
        mobsConfig.addDefault("MobSpawn.SpawnerEgg.Blacklist", Arrays.asList(Data.mobspawnspawneregglist));
        Data.mobspawnspawneregg = mobsConfig.getStringList("MobSpawn.SpawnerEgg.Blacklist");
        
        mobsConfig.addDefault("Completely-Prevent-SheepDyeWool", false);
        mobsConfig.addDefault("Prevent-SheepDyeWool-Color.Black", false);
        mobsConfig.addDefault("Prevent-SheepDyeWool-Color.Blue", false);
        mobsConfig.addDefault("Prevent-SheepDyeWool-Color.Brown", false);
        mobsConfig.addDefault("Prevent-SheepDyeWool-Color.Cyan", false);
        mobsConfig.addDefault("Prevent-SheepDyeWool-Color.Gray", false);
        mobsConfig.addDefault("Prevent-SheepDyeWool-Color.Green", false);
        mobsConfig.addDefault("Prevent-SheepDyeWool-Color.Light_Blue", false);
        mobsConfig.addDefault("Prevent-SheepDyeWool-Color.Lime", false);
        mobsConfig.addDefault("Prevent-SheepDyeWool-Color.Magenta", false);
        mobsConfig.addDefault("Prevent-SheepDyeWool-Color.Orange", false);
        mobsConfig.addDefault("Prevent-SheepDyeWool-Color.Pink", false);
        mobsConfig.addDefault("Prevent-SheepDyeWool-Color.Purple", false);
        mobsConfig.addDefault("Prevent-SheepDyeWool-Color.Red", false);
        mobsConfig.addDefault("Prevent-SheepDyeWool-Color.Silver", false);
        mobsConfig.addDefault("Prevent-SheepDyeWool-Color.White", false);
        mobsConfig.addDefault("Prevent-SheepDyeWool-Color.Yellow", false);
        
        mobsConfig.addDefault("Entity-Combust.Disable-Entity-Combust", false);
        mobsConfig.addDefault("Entity-Combust.Disable-for.Blaze", false);
        mobsConfig.addDefault("Entity-Combust.Disable-for.CaveSpider", false);
        mobsConfig.addDefault("Entity-Combust.Disable-for.Chicken", false);
        mobsConfig.addDefault("Entity-Combust.Disable-for.Cow", false);
        mobsConfig.addDefault("Entity-Combust.Disable-for.Creeper", false);
        mobsConfig.addDefault("Entity-Combust.Disable-for.EnderDragon", false);
        mobsConfig.addDefault("Entity-Combust.Disable-for.Enderman", false);
        mobsConfig.addDefault("Entity-Combust.Disable-for.Ghast", false);
        mobsConfig.addDefault("Entity-Combust.Disable-for.Giant", false);
        mobsConfig.addDefault("Entity-Combust.Disable-for.Golem", false);
        mobsConfig.addDefault("Entity-Combust.Disable-for.IronGolem", false);
        mobsConfig.addDefault("Entity-Combust.Disable-for.MagmaCube", false);
        mobsConfig.addDefault("Entity-Combust.Disable-for.MushroomCow", false);
        mobsConfig.addDefault("Entity-Combust.Disable-for.Ocelot", false);
        mobsConfig.addDefault("Entity-Combust.Disable-for.Pig", false);
        mobsConfig.addDefault("Entity-Combust.Disable-for.PigZombie", false);
        mobsConfig.addDefault("Entity-Combust.Disable-for.Sheep", false);
        mobsConfig.addDefault("Entity-Combust.Disable-for.Silverfish", false);
        mobsConfig.addDefault("Entity-Combust.Disable-for.Skeleton", false);
        mobsConfig.addDefault("Entity-Combust.Disable-for.Slime", false);
        mobsConfig.addDefault("Entity-Combust.Disable-for.Snowman", false);
        mobsConfig.addDefault("Entity-Combust.Disable-for.Spider", false);
        mobsConfig.addDefault("Entity-Combust.Disable-for.Squid", false);
        mobsConfig.addDefault("Entity-Combust.Disable-for.Villager", false);
        mobsConfig.addDefault("Entity-Combust.Disable-for.Wolf", false);
        mobsConfig.addDefault("Entity-Combust.Disable-for.Zombie", false);
        
        this.getMobsConfig().options().copyDefaults(true);
        saveMobsConfig();
    }
    
    /**
    private void blacklistDebug() {
        log.info("[iSafe] Blacklist debug mode is on.");
        
        log.info("[iSafe] -----  Place Blacklist  -----");
        if(Data.worlds != null) {
            log.info("[iSafe] Blacklisiting in the world(s): " + Data.worlds);
            log.info("[iSafe] World(s) blacklisting in: " + Data.placedblocks.size());
        } else {
            if(Data.placedblocks != null) {
                log.warning("[iSafe] You have objects in the blacklist list, but no worlds to blacklist in!");
            } else {
                log.info("[iSafe] Blacklisting in the world(s): " + Data.worlds);
            }
        }
        if(Data.placedblocks != null) {
            log.info("[iSafe] Blacklisted blocks: " + Data.placedblocks);
            log.info("[iSafe] Total blacklisted blocks: " + Data.placedblocks.size());
        } else {
            log.info("[iSafe] Blacklisted blocks: " + "None");
            log.info("[iSafe] Total blacklisted blocks: " + "0");
        }
        log.info("[iSafe] Kick player: " + this.getBlacklist().getBoolean("Place.Kick-Player"));
        log.info("[iSafe] Kill player: " + this.getBlacklist().getBoolean("Place.Kill-Player"));
        log.info("[iSafe] Logging to: ");
        log.info("[iSafe] - Console: " + this.getBlacklist().getBoolean("Place.Alert/log.To-console"));
        log.info("[iSafe] - Player: "+ this.getBlacklist().getBoolean("Place.Alert/log.To-player"));
        log.info("[iSafe] - ServerChat: "+ this.getBlacklist().getBoolean("Place.Alert/log.To-server-chat"));
        log.info("[iSafe] -----------------------------");
        
        log.info("[iSafe] -----  Break Blacklist  -----");
        if(Data.worlds != null) {
            log.info("[iSafe] Blacklisiting in the world(s): " + Data.Breakworlds);
            log.info("[iSafe] World(s) blacklisting in: " + Data.Breakworlds.size());
        } else {
            if(Data.placedblocks != null) {
                log.warning("[iSafe] You have objects in the blacklist list, but no worlds to blacklist in!");
            } else {
                log.info("[iSafe] Blacklisting in the world(s): " + Data.Breakworlds);
            }
        }
        if(Data.placedblocks != null) {
            log.info("[iSafe] Blacklisted blocks: " + Data.brokenblocks);
            log.info("[iSafe] Total blacklisted blocks: " + Data.brokenblocks.size());
        } else {
            log.info("[iSafe] Blacklisted blocks: " + "None");
            log.info("[iSafe] Total blacklisted blocks: " + "0");
        }
        log.info("[iSafe] Kick player: " + this.getBlacklist().getBoolean("Break.Kick-Player"));
        log.info("[iSafe] Kill player: " + this.getBlacklist().getBoolean("Break.Kill-Player"));
        log.info("[iSafe] Logging to: ");
        log.info("[iSafe] - Console: " + this.getBlacklist().getBoolean("Break.Alert/log.To-console"));
        log.info("[iSafe] - Player: "+ this.getBlacklist().getBoolean("Break.Alert/log.To-player"));
        log.info("[iSafe] - ServerChat: "+ this.getBlacklist().getBoolean("Break.Alert/log.To-server-chat"));
        log.info("[iSafe] -----------------------------");
        
        log.info("[iSafe] -----  Drop Blacklist  -----");
        if(Data.worlds != null) {
            log.info("[iSafe] Blacklisiting in the world(s): " + Data.Dropworlds);
            log.info("[iSafe] World(s) blacklisting in: " + Data.Dropworlds.size());
        } else {
            if(Data.placedblocks != null) {
                log.warning("[iSafe] You have objects in the blacklist list, but no worlds to blacklist in!");
            } else {
                log.info("[iSafe] Blacklisting in the world(s): " + Data.Dropworlds);
            }
        }
        if(Data.placedblocks != null) {
            log.info("[iSafe] Blacklisted blocks: " + Data.dropedblocks);
            log.info("[iSafe] Total blacklisted blocks: " + Data.dropedblocks.size());
        } else {
            log.info("[iSafe] Blacklisted blocks: " + "None");
            log.info("[iSafe] Total blacklisted blocks: " + "0");
        }
        log.info("[iSafe] Kick player: " + this.getBlacklist().getBoolean("Drop.Kick-Player"));
        log.info("[iSafe] Kill player: " + this.getBlacklist().getBoolean("Drop.Kill-Player"));
        log.info("[iSafe] Logging to: ");
        log.info("[iSafe] - Console: " + this.getBlacklist().getBoolean("Drop.Alert/log.To-console"));
        log.info("[iSafe] - Player: "+ this.getBlacklist().getBoolean("Drop.Alert/log.To-player"));
        log.info("[iSafe] - ServerChat: "+ this.getBlacklist().getBoolean("Drop.Alert/log.To-server-chat"));
        log.info("[iSafe] ----------------------------");
    }*/
}
