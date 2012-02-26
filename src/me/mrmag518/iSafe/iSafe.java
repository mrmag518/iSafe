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

package me.mrmag518.iSafe;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.File;
import java.net.URL;

import javax.xml.parsers.DocumentBuilderFactory;

import me.mrmag518.iSafe.Blacklists.*;
import me.mrmag518.iSafe.Commands.*;
import me.mrmag518.iSafe.Events.*;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class iSafe extends JavaPlugin implements Listener {
    private PlayerListener playerListener = null;
    private BlockListener blockListener = null;
    private EntityListener entityListener = null;
    private WeatherListener weatherListener = null;
    private InventoryListener inventoryListener = null;
    private VehicleListener vehicleListener = null;
    private WorldListener worldListener = null;
    private EnchantmentListener enchantmentListener = null;
    private DropListener dropListener = null;
    
    private UserFileCreator UFC = null;
    
    private DropBlacklist dropBlacklist = null;
    private PlaceBlacklist placeBlacklist = null;
    private BreakBlacklist breakBlacklist = null;
    private PickupBlacklist pickupBlacklist = null;
    private CommandBlacklist commandBlacklist = null;
    
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
    public File configFile;
    
    public Set<Player> superbreak = new HashSet<Player>();
    
    List<String> placedblocks = new ArrayList<String>();
    String[] placedblockslist = { "No defaults added." };
    List<String> worlds = new ArrayList<String>();
    String[] worldslist = { "world", "world_nether" };
    
    List<String> brokenblocks = new ArrayList<String>();
    String[] brokenblockslist = { "No defaults added." };
    List<String> Breakworlds = new ArrayList<String>();
    String[] Breakworldslist = { "world", "world_nether" };
    
    List<String> dropedblocks = new ArrayList<String>();
    String[] dropedblockslist = { "No defaults added." };
    List<String> Dropworlds = new ArrayList<String>();
    String[] Dropworldslist = { "world", "world_nether" };
    
    List<String> pickupedblocks = new ArrayList<String>();
    String[] pickupedblockslist = { "No defaults added." };
    List<String> Pickupworlds = new ArrayList<String>();
    String[] Pickupworldslist = { "world", "world_nether" };
    
    List<String> commands = new ArrayList<String>();
    String[] commandslist = { "/nuke" };
    List<String> cmdworlds = new ArrayList<String>();
    String[] cmdworldlist = { "world", "world_nether" };
    
    List<String> lbworlds = new ArrayList<String>();
    String[] lbworldslist = { "world", "world_nether" };
    List<String> wbworlds = new ArrayList<String>();
    String[] wbworldslist = { "world", "world_nether" };
    
    @Override
    public void onDisable() {
        PluginDescriptionFile pdffile = this.getDescription();
        log.info("[" + pdffile.getName() + " :: " + pdffile.getVersion() + "] " + " Unloaded succesfully.");
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
        
        dropBlacklist = new DropBlacklist(this);
        placeBlacklist = new PlaceBlacklist(this);
        breakBlacklist = new BreakBlacklist(this);
        pickupBlacklist = new PickupBlacklist(this);
        commandBlacklist = new CommandBlacklist(this);
        
        reloadcmd = new Reload(this);
        isafeInfocmd = new Info(this);
        serverinfocmd = new Serverinfo(this);
        superbreakcmd = new Superbreak(this);
        stopServercmd = new Stopserver(this);
        cleardropscmd = new ClearDrops(this);
        
        PluginDescriptionFile pdffile = this.getDescription();
        
        getServer().getPluginManager().registerEvents(this, this);
        
        if(!(this.getDataFolder().exists())) {
            this.getDataFolder().mkdir();
        }
        
        //Update checker - From MilkBowl.
        this.getServer().getScheduler().scheduleAsyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                try {
                    newVersion = updateCheck(version);
                    String oldVersion = version;
                    
                    if (!newVersion.contains(oldVersion)) {
                        log.info("A new version of iSafe is out! "+  newVersion + ", You are currently running v" + oldVersion);
                        log.info("Please update iSafe at: http://dev.bukkit.org/server-mods/blockthattnt");
                    }
                } catch (Exception ignored) {
                    //Ignored
                }
            }
        }, 0, 36000);
        
        config = this.getConfig();
        loadConfig();
        reloadConfig();
        
        blacklist = this.getBlacklist();
        loadBlacklist();
        reloadBlacklist();
        
        mobsConfig = this.getMobsConfig();
        loadMobsConfig();
        reloadMobsConfig();
        
        executeCommands();
        
        this.getServer().getPluginManager().getPermissions();
        
        log.info("[" + pdffile.getName() + " :: " + pdffile.getVersion() + "] " + " Loaded succesfully.");
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
    
    //From MilkBowl's Vault.
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (player.hasPermission("iSafe.*")) {
            try {
                String oldVersion = getDescription().getVersion().substring(0, 5);
                if (!newVersion.contains(oldVersion)) {
                    player.sendMessage("A new version of iSafe is out! "+  newVersion + ", You are currently running v" + oldVersion);
                    player.sendMessage("Please update iSafe at: http://dev.bukkit.org/server-mods/blockthattnt");
                }
            } catch (Exception e) {
                //ignore
            }
        } else {
            //nothing
        }
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
        config.options().header("This is the main configuration file in association to iSafe; take a decent look through it to manage your own preferred settings.\n");
        
        config.addDefault("Enchantment.Prevent-Enchantment", false);
        
        config.addDefault("EntityTo-SpawnLocation.On-Void-fall(Player)", true);
        config.addDefault("EntityTo-SpawnLocation.On-Void-fall(Creature)", true);
        
        config.addDefault("Buckets.Prevent-LavaBucket-empty", true);
        config.addDefault("Buckets.Lava.Worlds", Arrays.asList(lbworldslist));
        lbworlds = config.getStringList("Buckets.Lava.Worlds");
        
        config.addDefault("Buckets.Prevent-WaterBucket-empty", false);
        config.addDefault("Buckets.Water.Worlds", Arrays.asList(wbworldslist));
        wbworlds = config.getStringList("Buckets.Water.Worlds");
        
        config.addDefault("Flow.Disable-water-flow", false);
        config.addDefault("Flow.Disable-lava-flow", false);
        
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
        
        config.addDefault("Misc.Enable-kick-messages", true);
        config.addDefault("Misc.Disable-LeavesDecay", false);
        config.addDefault("Misc.Prevent-crop-trampling-by-creature", false);
        config.addDefault("Misc.Prevent-crop-trampling-by-player", false);
        config.addDefault("Misc.Prevent-portal-creation", false);
        config.addDefault("Misc.Prevent-RedStoneTorch-placed-against-tnt", false);
        
        config.addDefault("Explosions.Disable-primed-explosions", false);
        config.addDefault("Explosions.Prevent-creeper-death-on-explosion", false);
        config.addDefault("Explosions.Disable-explosions", false);
        config.addDefault("Explosions.Disable-Creeper-explosions", false);
        config.addDefault("Explosions.Disable-EnderDragon-blockdamage", false);
        config.addDefault("Explosions.Disable-TNT-explosions", false);
        config.addDefault("Explosions.Disable-Fireball-explosions", false);
        config.addDefault("Explosions.Disable-Disable-Block_Explosion-damage", false);
        config.addDefault("Explosions.Disable-Entity_Explosion-damage", false);
        
        config.addDefault("World.Register-world(s)-init", true);
        config.addDefault("World.Register-world(s)-unload", true);
        config.addDefault("World.Register-world(s)-save", true);
        config.addDefault("World.Register-world(s)-load", true);
        config.addDefault("World.Disable-ExpirienceOrbs-drop", false);
        config.addDefault("World.Prevent-items/objects-to-spawn-into-the-world", false);
        config.addDefault("World.Prevent-naturally-object-dispensing", false);
        config.addDefault("World.Force-blocks-to-be-buildable", false);
        config.addDefault("World.Prevent-blocks-spreading", false);
        
        config.addDefault("Structure.Prevent-structure-growth.BIG_TREE", false);
        config.addDefault("Structure.Prevent-structure-growth.BIRCH", false);
        config.addDefault("Structure.Prevent-structure-growth.BROWN_MUSHROOM", false);
        config.addDefault("Structure.Prevent-structure-growth.REDWOOD", false);
        config.addDefault("Structure.Prevent-structure-growth.RED_MUSHROOM", false);
        config.addDefault("Structure.Prevent-structure-growth.TALL_REDWOOD", false);
        config.addDefault("Structure.Prevent-structure-growth.TREE", false);
        config.addDefault("Structure.Prevent-bonemeal-usage", false);
        config.addDefault("Structure.Prevent-strcuture-growth", false);
        
        config.addDefault("Entity-Damage.Disable-Fire-damage", false);
        config.addDefault("Entity-Damage.Disable-Fire-Creature-damage", false);
        config.addDefault("Entity-Damage.Disable-Void-damage", false);
        config.addDefault("Entity-Damage.Disable-Void-Creature-damage", false);
        config.addDefault("Entity-Damage.Disable-Lightning-damage", false);
        config.addDefault("Entity-Damage.Disable-Lightning-Creature-damage", false);
        config.addDefault("Entity-Damage.Disable-Fall-damage", false);
        config.addDefault("Entity-Damage.Disable-Fall-Creature-damage", false);
        config.addDefault("Entity-Damage.Disable-Suffocation-damage", false);
        config.addDefault("Entity-Damage.Disable-Suffocation-Creature-damage", false);
        config.addDefault("Entity-Damage.Disable-Drowning-damage", false);
        config.addDefault("Entity-Damage.Disable-Drowning-Creature-damage", false);
        config.addDefault("Entity-Damage.Disable-Lava-damage", false);
        config.addDefault("Entity-Damage.Disable-Lava-Creature-damage", false);
        config.addDefault("Entity-Damage.Disable-Contact-damage", false);
        config.addDefault("Entity-Damage.Disable-Contact-Creature-damage", false);
        config.addDefault("Entity-Damage.Disable-Projectile-damage", false);
        config.addDefault("Entity-Damage.Disable-Projectile-Creature-damage", false);
        config.addDefault("Entity-Damage.Disable-Starvation-damage", false);
        config.addDefault("Entity-Damage.Disable-Starvation-Creature-damage", false);
        config.addDefault("Entity-Damage.Disable-Suicide-damage", false);
        config.addDefault("Entity-Damage.Disable-Suicide-Creature-damage", false);
        config.addDefault("Entity-Damage.Disable-Entity_Attack-damage", false);
        config.addDefault("Entity-Damage.Disable-Entity_Attack-Creature-damage", false);
        config.addDefault("Entity-Damage.Disable-Magic-damage", false);
        config.addDefault("Entity-Damage.Disable-Magic-Creature-damage", false);
        config.addDefault("Entity-Damage.Disable-Poison-damage", false);
        config.addDefault("Entity-Damage.Disable-Poison-Creature-damage", false);
        config.addDefault("Entity-Damage.Disable-Custom-damage", false);
        config.addDefault("Entity-Damage.Disable-Custom-Creature-damage", false);
        
        config.addDefault("Player.Prevent-Sprinting", false);
        config.addDefault("Player.Prevent-Sneaking", false);
        config.addDefault("Player.Enable-fishing-permissions", false);
        config.addDefault("Player.Broadcast-iSafe-message-on-join", true);
        config.addDefault("Player.Allow-creative-gamemode-on-player-quit", true);
        config.addDefault("Player.Disable-Hunger", false);
        config.addDefault("Player.Enable-Bed-permissions", false);
        config.addDefault("Player.Enable-fishing-permissions", false);
        config.addDefault("Player.Only-let-OPs-join", false);
        config.addDefault("Player.Log-commands", true);
        config.addDefault("Player.Disable-all-commands", false);
        config.addDefault("Player.Prevent-Gamemode-change", false);
        config.addDefault("Player.Prevent-Gamemode-to-CreativeMode-change", false);
        config.addDefault("Player.Prevent-Gamemode-to-SurvivalMode-change", false);
        config.addDefault("Player.Infinite-itemtacks", false);
        config.addDefault("Player.Kick-player-if-anther-user-with-same-username-log's-on", true);
        config.addDefault("Player.Instantbreak", false);
        config.addDefault("Player.Prevent-Bow-usage", false);
        config.addDefault("Player-Interact.Allow-Buttons-Interact", true);
        config.addDefault("Player-Interact.Allow-WoodenDoors-Interact", true);
        config.addDefault("Player-Interact.Allow-IronDoors-Interact", true);
        config.addDefault("Player-Interact.Allow-Levers-Interact", true);
        config.addDefault("Player-Interact.Allow-StonePressurePlate-Interact", true);
        config.addDefault("Player-Interact.Allow-WoodenPressurePlate-Interact", true);
        config.addDefault("Player-Interact.Allow-TrapDoor-Interact", true);
        config.addDefault("Player-Interact.Allow-WoodenFenceGate-Interact", true);
        config.addDefault("Player-Interact.Allow-Chest-Interact", true);
        config.addDefault("Entity/Player.Completely-Prevent.Health-Regeneration", false);
        config.addDefault("Entity/Player.Prevent.Custom-Health-Regeneration", false);
        config.addDefault("Entity/Player.Prevent.Eating-Health-Regeneration", false);
        config.addDefault("Entity/Player.Prevent.Regen-Health-Regeneration", false);
        config.addDefault("Entity/Player.Prevent.Satiated-Health-Regeneration", false);
        config.addDefault("PlayerInteractEntity.Prevent-snowball-hitting-player", false);
        config.addDefault("PlayerInteractEntity.Prevent-arrow-hitting-player", false);
        
        config.addDefault("Drop-configure.Glass.Drop.Glass", false);
        config.addDefault("Drop-configure.Mobspawner.Drop.Mobspawner", false);
        config.addDefault("Drop-configure.Ice.Drop.Ice", false);
        config.addDefault("Drop-configure.Ice.Drop.Ice-options.Prevent-water", false);
        config.addDefault("Drop-configure.Bedrock.Drop.Bedrock", false);
        config.addDefault("Drop-configure.Bookshelf.Drop.Bookshelf", false);
        config.addDefault("Drop-configure.Grass_thingy.Drop.Grass_thingy", false);
        
        this.getConfig().options().copyDefaults(true);
        saveConfig();
        log.info("[iSafe] Loaded configuration file.");
    }
    
    public void loadBlacklist() {
        blacklist.options().header("This is the blacklist config on behalf of iSafe, read the iSafe wiki for assistance.\n");
        
        blacklist.addDefault("Place.Complete-Disallow-placing", false);
        blacklist.addDefault("Place.Kick-Player", false);
        blacklist.addDefault("Place.Kill-Player", false);
        blacklist.addDefault("Place.Alert/log.To-console", true);
        blacklist.addDefault("Place.Alert/log.To-player", true);
        blacklist.addDefault("Place.Alert/log.To-server-chat", false);
        blacklist.addDefault("Place.Worlds", Arrays.asList(worldslist));
        worlds = blacklist.getStringList("Place.Worlds");
        blacklist.addDefault("Place.Blacklist", Arrays.asList(placedblockslist));
        placedblocks = blacklist.getStringList("Place.Blacklist");
        
        blacklist.addDefault("Break.Complete-Disallow-breaking", false);
        blacklist.addDefault("Break.Kick-Player", false);
        blacklist.addDefault("Break.Kill-Player", false);
        blacklist.addDefault("Break.Alert/log.To-console", true);
        blacklist.addDefault("Break.Alert/log.To-player", true);
        blacklist.addDefault("Break.Alert/log.To-server-chat", false);
        blacklist.addDefault("Break.Worlds", Arrays.asList(worldslist));
        worlds = blacklist.getStringList("Break.Worlds");
        blacklist.addDefault("Break.Blacklist", Arrays.asList(brokenblockslist));
        brokenblocks = blacklist.getStringList("Break.Blacklist");
        
        blacklist.addDefault("Drop.Complete-Disallow-droping", false);
        blacklist.addDefault("Drop.Kick-Player", false);
        blacklist.addDefault("Drop.Kill-Player", false);
        blacklist.addDefault("Drop.Alert/log.To-console", true);
        blacklist.addDefault("Drop.Alert/log.To-player", true);
        blacklist.addDefault("Drop.Alert/log.To-server-chat", false);
        blacklist.addDefault("Drop.Worlds", Arrays.asList(worldslist));
        worlds = blacklist.getStringList("Drop.Worlds");
        blacklist.addDefault("Drop.Blacklist", Arrays.asList(dropedblockslist));
        dropedblocks = config.getStringList("Drop.Blacklist");
        
        blacklist.addDefault("Pickup.Complete-Disallow-pickuping", false);
        blacklist.addDefault("Pickup.Kick-Player", false);
        blacklist.addDefault("Pickup.Kill-Player", false);
        blacklist.addDefault("Pickup.Alert/log.To-console", true);
        blacklist.addDefault("Pickup.Alert/log.To-player", true);
        blacklist.addDefault("Pickup.Alert/log.To-server-chat", false);
        blacklist.addDefault("Pickup.Worlds", Arrays.asList(Pickupworldslist));
        Pickupworlds = blacklist.getStringList("Pickup.Worlds");
        blacklist.addDefault("Pickup.Blacklist", Arrays.asList(pickupedblockslist));
        pickupedblocks = config.getStringList("Pickup.Blacklist");
        
        blacklist.addDefault("Command.Disallow-commands", false);
        blacklist.addDefault("Command.Alert/log.To-console", true);
        blacklist.addDefault("Command.Alert/log.To-player", true);
        blacklist.addDefault("Command.Alert/log.To-server-chat", false);
        blacklist.addDefault("Command.Worlds", Arrays.asList(cmdworldlist));
        cmdworlds = blacklist.getStringList("Command.Worlds");
        blacklist.addDefault("Command.Blacklist", Arrays.asList(commandslist));
        commands = config.getStringList("Command.Blacklist");
        
        this.getBlacklist().options().copyDefaults(true);
        saveBlacklist();
        log.info("[iSafe] Loaded blacklist file.");
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
        mobsConfig.options().header("This is the Mob Control config associated to regulatory characteristics aimed at mobs in Minecraft.\n");
        
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
        mobsConfig.addDefault("Misc.Prevent-Object-drop-on-death", false);
        mobsConfig.addDefault("Misc.Prevent-Entity-Combust", false);
        mobsConfig.addDefault("Misc.Tame.Prevent-taming", false);
        mobsConfig.addDefault("Misc.Allow-SlimeSplit", true);
        mobsConfig.addDefault("Misc.Prevent-PigZap", true);
        
        mobsConfig.addDefault("Mob-Spawn.SpawnReason.Natural.Prevent.Blazes", false);
        mobsConfig.addDefault("Mob-Spawn.SpawnReason.Natural.Prevent.Cave_Spiders", false);
        mobsConfig.addDefault("Mob-Spawn.SpawnReason.Natural.Prevent.Chickens", false);
        mobsConfig.addDefault("Mob-Spawn.SpawnReason.Natural.Prevent.Cows", false);
        mobsConfig.addDefault("Mob-Spawn.SpawnReason.Natural.Prevent.Creepers", false);
        mobsConfig.addDefault("Mob-Spawn.SpawnReason.Natural.Prevent.Endermen", false);
        mobsConfig.addDefault("Mob-Spawn.SpawnReason.Natural.Prevent.EnderDragons", false);
        mobsConfig.addDefault("Mob-Spawn.SpawnReason.Natural.Prevent.Ghasts", false);
        mobsConfig.addDefault("Mob-Spawn.SpawnReason.Natural.Prevent.Giants", false);
        mobsConfig.addDefault("Mob-Spawn.SpawnReason.Natural.Prevent.MagmaCubes", false);
        mobsConfig.addDefault("Mob-Spawn.SpawnReason.Natural.Prevent.MushroomCows", false);
        mobsConfig.addDefault("Mob-Spawn.SpawnReason.Natural.Prevent.Pigs", false);
        mobsConfig.addDefault("Mob-Spawn.SpawnReason.Natural.Prevent.PigZombie", false);
        mobsConfig.addDefault("Mob-Spawn.SpawnReason.Natural.Prevent.Sheeps", false);
        mobsConfig.addDefault("Mob-Spawn.SpawnReason.Natural.Prevent.Silverfishes", false);
        mobsConfig.addDefault("Mob-Spawn.SpawnReason.Natural.Prevent.Skeletons", false);
        mobsConfig.addDefault("Mob-Spawn.SpawnReason.Natural.Prevent.Slimes", false);
        mobsConfig.addDefault("Mob-Spawn.SpawnReason.Natural.Prevent.Snowmen", false);
        mobsConfig.addDefault("Mob-Spawn.SpawnReason.Natural.Prevent.Spiders", false);
        mobsConfig.addDefault("Mob-Spawn.SpawnReason.Natural.Prevent.Squids", false);
        mobsConfig.addDefault("Mob-Spawn.SpawnReason.Natural.Prevent.Villagers", false);
        mobsConfig.addDefault("Mob-Spawn.SpawnReason.Natural.Prevent.Wolfs", false);
        mobsConfig.addDefault("Mob-Spawn.SpawnReason.Natural.Prevent.Zombies", false);
        
        mobsConfig.addDefault("Mob-Spawn.SpawnReason.Spawner.Prevent.Blazes", false);
        mobsConfig.addDefault("Mob-Spawn.SpawnReason.Spawner.Prevent.CaveSpiders", false);
        mobsConfig.addDefault("Mob-Spawn.SpawnReason.Spawner.Prevent.Chickens", false);
        mobsConfig.addDefault("Mob-Spawn.SpawnReason.Spawner.Prevent.Cows", false);
        mobsConfig.addDefault("Mob-Spawn.SpawnReason.Spawner.Prevent.Creepers", false);
        mobsConfig.addDefault("Mob-Spawn.SpawnReason.Spawner.Prevent.Endermen", false);
        mobsConfig.addDefault("Mob-Spawn.SpawnReason.Spawner.Prevent.EnderDragons", false);
        mobsConfig.addDefault("Mob-Spawn.SpawnReason.Spawner.Prevent.Ghasts", false);
        mobsConfig.addDefault("Mob-Spawn.SpawnReason.Spawner.Prevent.Giants", false);
        mobsConfig.addDefault("Mob-Spawn.SpawnReason.Spawner.Prevent.MagmaCubes", false);
        mobsConfig.addDefault("Mob-Spawn.SpawnReason.Spawner.Prevent.MushroomCows", false);
        mobsConfig.addDefault("Mob-Spawn.SpawnReason.Spawner.Prevent.Pigs", false);
        mobsConfig.addDefault("Mob-Spawn.SpawnReason.Spawner.Prevent.PigZombies", false);
        mobsConfig.addDefault("Mob-Spawn.SpawnReason.Spawner.Prevent.Sheeps", false);
        mobsConfig.addDefault("Mob-Spawn.SpawnReason.Spawner.Prevent.Silverfishes", false);
        mobsConfig.addDefault("Mob-Spawn.SpawnReason.Spawner.Prevent.Skeletons", false);
        mobsConfig.addDefault("Mob-Spawn.SpawnReason.Spawner.Prevent.Slimes", false);
        mobsConfig.addDefault("Mob-Spawn.SpawnReason.Spawner.Prevent.Snowmen", false);
        mobsConfig.addDefault("Mob-Spawn.SpawnReason.Spawner.Prevent.Spiders", false);
        mobsConfig.addDefault("Mob-Spawn.SpawnReason.Spawner.Prevent.Squids", false);
        mobsConfig.addDefault("Mob-Spawn.SpawnReason.Spawner.Prevent.Villagers", false);
        mobsConfig.addDefault("Mob-Spawn.SpawnReason.Spawner.Prevent.Wolfs", false);
        mobsConfig.addDefault("Mob-Spawn.SpawnReason.Spawner.Prevent.Zombies", false);
        
        mobsConfig.addDefault("Mob-Spawn.SpawnReason.Custom.Prevent.Blazes", false);
        mobsConfig.addDefault("Mob-Spawn.SpawnReason.Custom.Prevent.CaveSpiders", false);
        mobsConfig.addDefault("Mob-Spawn.SpawnReason.Custom.Prevent.Chickens", false);
        mobsConfig.addDefault("Mob-Spawn.SpawnReason.Custom.Prevent.Cows", false);
        mobsConfig.addDefault("Mob-Spawn.SpawnReason.Custom.Prevent.Creepers", false);
        mobsConfig.addDefault("Mob-Spawn.SpawnReason.Custom.Prevent.Endermen", false);
        mobsConfig.addDefault("Mob-Spawn.SpawnReason.Custom.Prevent.EnderDragons", false);
        mobsConfig.addDefault("Mob-Spawn.SpawnReason.Custom.Prevent.Ghasts", false);
        mobsConfig.addDefault("Mob-Spawn.SpawnReason.Custom.Prevent.Giants", false);
        mobsConfig.addDefault("Mob-Spawn.SpawnReason.Custom.Prevent.MagmaCubes", false);
        mobsConfig.addDefault("Mob-Spawn.SpawnReason.Custom.Prevent.MushroomCows", false);
        mobsConfig.addDefault("Mob-Spawn.SpawnReason.Custom.Prevent.Pigs", false);
        mobsConfig.addDefault("Mob-Spawn.SpawnReason.Custom.Prevent.PigZombies", false);
        mobsConfig.addDefault("Mob-Spawn.SpawnReason.Custom.Prevent.Sheeps", false);
        mobsConfig.addDefault("Mob-Spawn.SpawnReason.Custom.Prevent.SilverFishes", false);
        mobsConfig.addDefault("Mob-Spawn.SpawnReason.Custom.Prevent.Skeletons", false);
        mobsConfig.addDefault("Mob-Spawn.SpawnReason.Custom.Prevent.Slimes", false);
        mobsConfig.addDefault("Mob-Spawn.SpawnReason.Custom.Prevent.Snowmen", false);
        mobsConfig.addDefault("Mob-Spawn.SpawnReason.Custom.Prevent.Spiders", false);
        mobsConfig.addDefault("Mob-Spawn.SpawnReason.Custom.Prevent.Squids", false);
        mobsConfig.addDefault("Mob-Spawn.SpawnReason.Custom.Prevent.Villagers", false);
        mobsConfig.addDefault("Mob-Spawn.SpawnReason.Custom.Prevent.Wolfs", false);
        mobsConfig.addDefault("Mob-Spawn.SpawnReason.Custom.Prevent.Zombies", false);
        
        mobsConfig.addDefault("Mob-Spawn.SpawnReason.Egg.Prevent.Blaze", false);
        mobsConfig.addDefault("Mob-Spawn.SpawnReason.Egg.Prevent.Cave_Spider", false);
        mobsConfig.addDefault("Mob-Spawn.SpawnReason.Egg.Prevent.Chicken", false);
        mobsConfig.addDefault("Mob-Spawn.SpawnReason.Egg.Prevent.Cow", false);
        mobsConfig.addDefault("Mob-Spawn.SpawnReason.Egg.Prevent.Creeper", false);
        mobsConfig.addDefault("Mob-Spawn.SpawnReason.Egg.Prevent.Enderman", false);
        mobsConfig.addDefault("Mob-Spawn.SpawnReason.Egg.Prevent.Ender_Dragon", false);
        mobsConfig.addDefault("Mob-Spawn.SpawnReason.Egg.Prevent.Ghast", false);
        mobsConfig.addDefault("Mob-Spawn.SpawnReason.Egg.Prevent.Giant", false);
        mobsConfig.addDefault("Mob-Spawn.SpawnReason.Egg.Prevent.Magma_Cube", false);
        mobsConfig.addDefault("Mob-Spawn.SpawnReason.Egg.Prevent.Mushroom_Cow", false);
        mobsConfig.addDefault("Mob-Spawn.SpawnReason.Egg.Prevent.Pig", false);
        mobsConfig.addDefault("Mob-Spawn.SpawnReason.Egg.Prevent.Pig_Zombie", false);
        mobsConfig.addDefault("Mob-Spawn.SpawnReason.Egg.Prevent.Sheep", false);
        mobsConfig.addDefault("Mob-Spawn.SpawnReason.Egg.Prevent.Silverfish", false);
        mobsConfig.addDefault("Mob-Spawn.SpawnReason.Egg.Prevent.Skeleton", false);
        mobsConfig.addDefault("Mob-Spawn.SpawnReason.Egg.Prevent.Slime", false);
        mobsConfig.addDefault("Mob-Spawn.SpawnReason.Egg.Prevent.Smowman", false);
        mobsConfig.addDefault("Mob-Spawn.SpawnReason.Egg.Prevent.Spider", false);
        mobsConfig.addDefault("Mob-Spawn.SpawnReason.Egg.Prevent.Squid", false);
        mobsConfig.addDefault("Mob-Spawn.SpawnReason.Egg.Prevent.Villager", false);
        mobsConfig.addDefault("Mob-Spawn.SpawnReason.Egg.Prevent.Wolf", false);
        mobsConfig.addDefault("Mob-Spawn.SpawnReason.Egg.Prevent.Zombie", false);
        
        /** (Bugged)
        mobsConfig.addDefault("Mob-Spawn.SpawnReason.SpawnerEgg.Prevent.Cave_Spider", false);
        mobsConfig.addDefault("Mob-Spawn.SpawnReason.SpawnerEgg.Prevent.Chicken", false);
        mobsConfig.addDefault("Mob-Spawn.SpawnReason.SpawnerEgg.Prevent.Cow", false);
        mobsConfig.addDefault("Mob-Spawn.SpawnReason.SpawnerEgg.Prevent.Creeper", false);
        mobsConfig.addDefault("Mob-Spawn.SpawnReason.SpawnerEgg.Prevent.Enderman", false);
        mobsConfig.addDefault("Mob-Spawn.SpawnReason.SpawnerEgg.Prevent.Ender_Dragon", false);
        mobsConfig.addDefault("Mob-Spawn.SpawnReason.SpawnerEgg.Prevent.Ghast", false);
        mobsConfig.addDefault("Mob-Spawn.SpawnReason.SpawnerEgg.Prevent.Giant", false);
        mobsConfig.addDefault("Mob-Spawn.SpawnReason.SpawnerEgg.Prevent.Magma_Cube", false);
        mobsConfig.addDefault("Mob-Spawn.SpawnReason.SpawnerEgg.Prevent.Mushroom_Cow", false);
        mobsConfig.addDefault("Mob-Spawn.SpawnReason.SpawnerEgg.Prevent.Pig", false);
        mobsConfig.addDefault("Mob-Spawn.SpawnReason.SpawnerEgg.Prevent.Pig_Zombie", false);
        mobsConfig.addDefault("Mob-Spawn.SpawnReason.SpawnerEgg.Prevent.Sheep", false);
        mobsConfig.addDefault("Mob-Spawn.SpawnReason.SpawnerEgg.Prevent.Silverfish", false);
        mobsConfig.addDefault("Mob-Spawn.SpawnReason.SpawnerEgg.Prevent.Skeleton", false);
        mobsConfig.addDefault("Mob-Spawn.SpawnReason.SpawnerEgg.Prevent.Slime", false);
        mobsConfig.addDefault("Mob-Spawn.SpawnReason.SpawnerEgg.Prevent.Snowman", false);
        mobsConfig.addDefault("Mob-Spawn.SpawnReason.SpawnerEgg.Prevent.Spider", false);
        mobsConfig.addDefault("Mob-Spawn.SpawnReason.SpawnerEgg.Prevent.Squid", false);
        mobsConfig.addDefault("Mob-Spawn.SpawnReason.SpawnerEgg.Prevent.Villager", false);
        mobsConfig.addDefault("Mob-Spawn.SpawnReason.SpawnerEgg.Prevent.Wolf", false);
        mobsConfig.addDefault("Mob-Spawn.SpawnReason.SpawnerEgg.Prevent.Zombie", false);
        */
        
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
        this.getMobsConfig().options().copyDefaults(true);
        saveMobsConfig();
        log.info("[iSafe] Loaded mobsConfig file.");
    }
}
