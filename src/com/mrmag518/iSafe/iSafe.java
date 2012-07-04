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

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * TODO:
 * Use new method for no permission output. (plugin.NO_PERMISSION())
 * Fix iSafe commands to use arguments.
 * Change plugin package from com.mrmag518.iSafe.blah --> com | mrmag518 | iSafe | Blah
 * Manage plugin tickets.
 * ...
 */

public class iSafe extends JavaPlugin {
    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    //Remember to change this on every version!
    private String fileversion = "iSafe v2.81";
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
    public DispenseBlacklist dispenseBlacklist = null;
    private Reload reloadcmd = null;
    private Info isafeInfocmd = null;
    private Serverinfo serverinfocmd = null;
    private Superbreak superbreakcmd = null;
    private Stopserver stopServercmd = null;
    public String version = null;
    public String newVersion = null;
    public static iSafe plugin;
    public final Logger log = Logger.getLogger("Minecraft");
    public FileConfiguration entityManager = null;
    public File entityManagerFile = null;
    public FileConfiguration blacklist = null;
    public File blacklistFile = null;
    public FileConfiguration config;
    public Set<Player> superbreak = new HashSet<Player>();
    
    @Override
    public void onLoad() {
        //To smooth the load up, let's manage files while the plugin is loading and not when it's enabling.
        doFileGenerations();
    }
    
    @Override
    public void onDisable() {
        PluginDescriptionFile pdffile = this.getDescription();
        log.info("[" + pdffile.getName() + " :: " + version + "] " + " Disabled succesfully.");
    }
    
    @Override
    public void onEnable() {
        version = this.getDescription().getVersion();
        registerClasses();
        PluginDescriptionFile pdffile = this.getDescription();
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
        checkMatch();
        
        try {
            Metrics metrics = new Metrics(this);
            metrics.start();
        } catch (IOException e) {
            // Failed to submit the stats :-(
        }
        
        log.info("[" + pdffile.getName() + " :: " + version + "] " + " Enabled succesfully.");
    }
    
    public String NO_PERMISSION() {
        ChatColor RED = ChatColor.RED;
        String no_permission = RED + "No permission";
        return no_permission;
    }
    
    private void registerClasses() {
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
        dispenseBlacklist = new DispenseBlacklist(this);
        
        reloadcmd = new Reload(this);
        isafeInfocmd = new Info(this);
        serverinfocmd = new Serverinfo(this);
        superbreakcmd = new Superbreak(this);
        stopServercmd = new Stopserver(this);
    }
    
    private void doFileGenerations() {
        if(!(this.getDataFolder().exists())) 
        {
            log.info("[iSafe]" + " iSafe folder not found, creating a new one.");
            this.getDataFolder().mkdirs(); 
        }
        
        File usersFolder = new File(getDataFolder() + File.separator + "Users");
        if(!(usersFolder.exists())) 
        {
            usersFolder.mkdir();
        }
        
        config = this.getConfig();
        loadConfig();
        reloadConfig();
        
        blacklist = this.getBlacklist();
        loadBlacklist();
        reloadBlacklist();
        
        entityManager = this.getEntityManager();
        loadEntityManager();
        reloadEntityManager();
    }
    
    private void checkMatch() {
        PluginDescriptionFile pdffile = this.getDescription();
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
    }
    
    //Update checker - Old code from MilkBowl's Vault.
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
    
    private void executeCommands() {
        getCommand("iSafe-reload").setExecutor(reloadcmd);
        getCommand("iSafe-info").setExecutor(isafeInfocmd);
        getCommand("serverinfo").setExecutor(serverinfocmd);
        getCommand("superbreak").setExecutor(superbreakcmd);
        getCommand("stopserver").setExecutor(stopServercmd);
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
        
        config.addDefault("Misc.Enable-kick-messages", false);
        config.addDefault("Misc.Disable-LeavesDecay", false);
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
        
        blacklist.addDefault("Dispense.Worlds", Arrays.asList(Data.dispenseWorldsList));
        Data.dispenseWorlds = blacklist.getStringList("Dispense.Worlds");
        blacklist.addDefault("Dispense.Blacklist", Arrays.asList(Data.dispensedBlockList));
        Data.dispensedBlock = blacklist.getStringList("Dispense.Blacklist");
        
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
    
    //MobsConfig re-do:
    public void reloadEntityManager() {
        if (entityManagerFile == null) {
            entityManagerFile = new File(getDataFolder(), "entityManager.yml");
        }
        entityManager = YamlConfiguration.loadConfiguration(entityManagerFile);
        
        InputStream defConfigStream = getResource("entityManager.yml");
        if (defConfigStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            entityManager.setDefaults(defConfig);
        }
    }
    
    public FileConfiguration getEntityManager() {
        if (entityManager == null) {
            reloadEntityManager();
        }
        return entityManager;
    }
    
    public void saveEntityManager() {
        if (entityManager == null || entityManagerFile == null) {
            return;
        }
        try {
            entityManager.save(entityManagerFile);
        } catch (IOException ex) {
            Logger.getLogger(JavaPlugin.class.getName()).log(Level.SEVERE, "Error saving entityManager to " + entityManagerFile, ex);
        }
    }
    
    public void loadEntityManager() {
        entityManager.options().header(Data.setEntityManagerHeader());
        
        entityManager.addDefault("Creatures.CreatureTarget.Disable-closest_player-target", false);
        entityManager.addDefault("Creatures.CreatureTarget.Disable-custom-target", false);
        entityManager.addDefault("Creatures.CreatureTarget.Disable-forgot_target-target", false);
        entityManager.addDefault("Creatures.CreatureTarget.Disable-owner_attacked_target-target", false);
        entityManager.addDefault("Creatures.CreatureTarget.Disable-pig_zombie_target-target", false);
        entityManager.addDefault("Creatures.CreatureTarget.Disable-random_target-target", false);
        entityManager.addDefault("Creatures.CreatureTarget.Disable-target_attacked_entity-target", false);
        entityManager.addDefault("Creatures.CreatureTarget.Disable-target_attacked_owner-target", false);
        entityManager.addDefault("Creatures.CreatureTarget.Disable-target_died-target", false);
        entityManager.addDefault("Creatures.PoweredCreepers.Disable-PowerCase-Lightning", false);
        entityManager.addDefault("Creatures.PoweredCreepers.Disable-PowerCase-Set-Off", false);
        entityManager.addDefault("Creatures.PoweredCreepers.Disable-PowerCase-Set-On", false);
        entityManager.addDefault("Creatures.Endermen.Prevent-endermen-griefing", false);
        entityManager.addDefault("Creatures.Tame.Prevent-taming", false);
        entityManager.addDefault("Creatures.Tame.Prevent-taming-for.Wolf", false);
        entityManager.addDefault("Creatures.Slime.Prevent-SlimeSplit", false);
        entityManager.addDefault("Creatures.Pig.Prevent-PigZap", false);
        entityManager.addDefault("Creatures.DoorBreaking-PreventFor-zombies", false);
        entityManager.addDefault("Creatures.Death.Disable-drops-onDeath", false);
        entityManager.addDefault("Creatures.SheepDyeWool.Completely-Prevent-SheepDyeWool", false);
        entityManager.addDefault("Creatures.SheepDyeWool.Prevent-SheepDyeWool-Color.Black", false);
        entityManager.addDefault("Creatures.SheepDyeWool.Prevent-SheepDyeWool-Color.Blue", false);
        entityManager.addDefault("Creatures.SheepDyeWool.Prevent-SheepDyeWool-Color.Brown", false);
        entityManager.addDefault("Creatures.SheepDyeWool.Prevent-SheepDyeWool-Color.Cyan", false);
        entityManager.addDefault("Creatures.SheepDyeWool.Prevent-SheepDyeWool-Color.Gray", false);
        entityManager.addDefault("Creatures.SheepDyeWool.Prevent-SheepDyeWool-Color.Green", false);
        entityManager.addDefault("Creatures.SheepDyeWool.Prevent-SheepDyeWool-Color.Light_Blue", false);
        entityManager.addDefault("Creatures.SheepDyeWool.Prevent-SheepDyeWool-Color.Lime", false);
        entityManager.addDefault("Creatures.SheepDyeWool.Prevent-SheepDyeWool-Color.Magenta", false);
        entityManager.addDefault("Creatures.SheepDyeWool.Prevent-SheepDyeWool-Color.Orange", false);
        entityManager.addDefault("Creatures.SheepDyeWool.Prevent-SheepDyeWool-Color.Pink", false);
        entityManager.addDefault("Creatures.SheepDyeWool.Prevent-SheepDyeWool-Color.Purple", false);
        entityManager.addDefault("Creatures.SheepDyeWool.Prevent-SheepDyeWool-Color.Red", false);
        entityManager.addDefault("Creatures.SheepDyeWool.Prevent-SheepDyeWool-Color.Silver", false);
        entityManager.addDefault("Creatures.SheepDyeWool.Prevent-SheepDyeWool-Color.White", false);
        entityManager.addDefault("Creatures.SheepDyeWool.Prevent-SheepDyeWool-Color.Yellow", false);
        entityManager.addDefault("Creatures.Combusting.Disable-for-allCreatures", false);
        entityManager.addDefault("Creatures.Combusting.Disable-for.Blaze", false);
        entityManager.addDefault("Creatures.Combusting.Disable-for.CaveSpider", false);
        entityManager.addDefault("Creatures.Combusting.Disable-for.Chicken", false);
        entityManager.addDefault("Creatures.Combusting.Disable-for.Cow", false);
        entityManager.addDefault("Creatures.Combusting.Disable-for.Creeper", false);
        entityManager.addDefault("Creatures.Combusting.Disable-for.EnderDragon", false);
        entityManager.addDefault("Creatures.Combusting.Disable-for.Enderman", false);
        entityManager.addDefault("Creatures.Combusting.Disable-for.Ghast", false);
        entityManager.addDefault("Creatures.Combusting.Disable-for.Giant", false);
        entityManager.addDefault("Creatures.Combusting.Disable-for.Golem", false);
        entityManager.addDefault("Creatures.Combusting.Disable-for.IronGolem", false);
        entityManager.addDefault("Creatures.Combusting.Disable-for.MagmaCube", false);
        entityManager.addDefault("Creatures.Combusting.Disable-for.MushroomCow", false);
        entityManager.addDefault("Creatures.Combusting.Disable-for.Ocelot", false);
        entityManager.addDefault("Creatures.Combusting.Disable-for.Pig", false);
        entityManager.addDefault("Creatures.Combusting.Disable-for.PigZombie", false);
        entityManager.addDefault("Creatures.Combusting.Disable-for.Sheep", false);
        entityManager.addDefault("Creatures.Combusting.Disable-for.Silverfish", false);
        entityManager.addDefault("Creatures.Combusting.Disable-for.Skeleton", false);
        entityManager.addDefault("Creatures.Combusting.Disable-for.Slime", false);
        entityManager.addDefault("Creatures.Combusting.Disable-for.Snowman", false);
        entityManager.addDefault("Creatures.Combusting.Disable-for.Spider", false);
        entityManager.addDefault("Creatures.Combusting.Disable-for.Squid", false);
        entityManager.addDefault("Creatures.Combusting.Disable-for.Villager", false);
        entityManager.addDefault("Creatures.Combusting.Disable-for.Wolf", false);
        entityManager.addDefault("Creatures.Combusting.Disable-for.Zombie", false);
        entityManager.addDefault("Creatures.Prevent-cropTrampling", false);
        
        entityManager.addDefault("Player.Prevent-expBottle-throw", false);
        entityManager.addDefault("Player.Prevent-cropTrampling", false);
        
        //MobSpawn blacklists.
        //Natural
        entityManager.addDefault("MobSpawn.Natural.Debug.To-console", false);
        entityManager.addDefault("MobSpawn.Natural.Worlds", Arrays.asList(Data.worlds1list));
        Data.worlds1 = entityManager.getStringList("MobSpawn.Natural.Worlds");
        entityManager.addDefault("MobSpawn.Natural.Blacklist", Arrays.asList(Data.mobspawnnaturallist));
        Data.mobspawnnatural = entityManager.getStringList("MobSpawn.Natural.Blacklist");
        //Spawner
        entityManager.addDefault("MobSpawn.Spawner.Debug.To-console", false);
        entityManager.addDefault("MobSpawn.Spawner.Worlds", Arrays.asList(Data.worlds2list));
        Data.worlds2 = entityManager.getStringList("MobSpawn.Spawner.Worlds");
        entityManager.addDefault("MobSpawn.Spawner.Blacklist", Arrays.asList(Data.mobspawnspawnerlist));
        Data.mobspawnspawner = entityManager.getStringList("MobSpawn.Spawner.Blacklist");
        //Custom
        entityManager.addDefault("MobSpawn.Custom.Debug.To-console", false);
        entityManager.addDefault("MobSpawn.Custom.Worlds", Arrays.asList(Data.worlds3list));
        Data.worlds3 = entityManager.getStringList("MobSpawn.Custom.Worlds");
        entityManager.addDefault("MobSpawn.Custom.Blacklist", Arrays.asList(Data.mobspawncustomlist));
        Data.mobspawncustom = entityManager.getStringList("MobSpawn.Custom.Blacklist");
        //Egg(Chicken egg)
        entityManager.addDefault("MobSpawn.Egg.Debug.To-console", false);
        entityManager.addDefault("MobSpawn.Egg.Worlds", Arrays.asList(Data.worlds4list));
        Data.worlds4 = entityManager.getStringList("MobSpawn.Egg.Worlds");
        entityManager.addDefault("MobSpawn.Egg.Blacklist", Arrays.asList(Data.mobspawnegglist));
        Data.mobspawnegg = entityManager.getStringList("MobSpawn.Egg.Blacklist");
        //SpawnerEgg
        entityManager.addDefault("MobSpawn.SpawnerEgg.Do_not_insert_the_spawner_egg_ID_here,_Here_you'll_insert_the_Animal_ID_(Like:_Creeper,_60,_PIG)".trim(), "Thanks");
        entityManager.addDefault("MobSpawn.SpawnerEgg.Debug.To-console", false);
        entityManager.addDefault("MobSpawn.SpawnerEgg.Worlds", Arrays.asList(Data.worlds5list));
        Data.worlds5 = entityManager.getStringList("MobSpawn.SpawnerEgg.Worlds");
        entityManager.addDefault("MobSpawn.SpawnerEgg.Blacklist", Arrays.asList(Data.mobspawnspawneregglist));
        Data.mobspawnspawneregg = entityManager.getStringList("MobSpawn.SpawnerEgg.Blacklist");
        
        this.getEntityManager().options().copyDefaults(true);
        saveEntityManager();
    }
}
