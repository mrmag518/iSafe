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

import com.mrmag518.Events.BlockEvents.*;
import com.mrmag518.Events.EntityEvents.*;
import com.mrmag518.Events.WorldEvents.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.File;
import java.net.URL;

import javax.xml.parsers.DocumentBuilderFactory;

import com.mrmag518.iSafe.Blacklists.*;
import com.mrmag518.iSafe.Commands.*;

import net.milkbowl.vault.permission.Permission;

import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * TODO:
 * Use new method for no permission output. 
 * Manage plugin tickets.
 * Finish Verbose logging.
 * Finish debug mode.
 * Recreate all permission nodes.
 * 
 * New permissions(just to note):
 * iSafe.bypass.fullbright || default: op
 * iSafe.forcedrop.glass || default: true
 * iSafe.forcedrop.mobspawner || default: true
 * iSafe.forcedrop.ice || default: true
 * iSafe.forcedrop.bedrock || default: true
 * iSafe.disablehunger || default: true
 * iSafe.canceltarget.closestplayer || default: true
 * iSafe.canceltarget.custom || default: true
 * iSafe.canceltarget.forgot || default: true
 * iSafe.canceltarget.ownerattacked || default: true
 * iSafe.canceltarget.pigzombie || default: true
 * iSafe.canceltarget.random || default: true
 * iSafe.canceltarget.targetattackedentity || default: true
 * iSafe.canceltarget.targetattackedowner || default: true
 * iSafe.canceltarget.targetdied || default: true
 * iSafe.bypass.croptrampling || default: false
 * iSafe.use.lavabuckets || default: op
 * iSafe.use.waterbuckets || default: op
 * iSafe.use.bed || default: op
 * iSafe.fish || default: op
 * 
 */

public class iSafe extends JavaPlugin {
    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    //Remember to change this on every version!
    private String fileversion = "iSafe v3.0";
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
    public String version = null;
    public String newVersion = null;
    public static iSafe plugin;
    public final Logger log = Logger.getLogger("Minecraft");
    public String DEBUG_PREFIX = "[iSafe] Debug: ";
    public static Permission perms = null;
    
    public FileConfiguration iSafeConfig = null;
    public File iSafeConfigFile = null;
    public FileConfiguration messages = null;
    public File messagesFile = null;
    public FileConfiguration creatureManager = null;
    public File creatureManagerFile = null;
    public FileConfiguration blacklist = null;
    public File blacklistFile = null;
    public FileConfiguration config;
    
    @Override
    public void onLoad() {
        fileManagement();
    }
    
    @Override
    public void onDisable() {
        PluginDescriptionFile pdffile = this.getDescription();
        if(verboseLogging() == true) {
            log.info("[" + pdffile.getName() + " :: " + version + "] " + " Disabled succesfully.");
        } else {
            if (debugMode() == true) {
                log.info(DEBUG_PREFIX + "Verbose logging is off.");
            }
        }
    }
    
    @Override
    public void onEnable() {
        version = this.getDescription().getVersion();
        if(debugMode() == true) {
            log.info(DEBUG_PREFIX + "Debug mode is enabled!");
        }
        
        registerClasses();
        PluginDescriptionFile pdffile = this.getDescription();
        if(getISafeConfig().getBoolean("CheckForUpdates", true)) {
            //Update checker - From MilkBowl.
            this.getServer().getScheduler().scheduleAsyncRepeatingTask(this, new Runnable() {
                @Override
                public void run() {
                    try {
                        newVersion = updateCheck(version);
                        String oldVersion = version;

                        if (!newVersion.contains(oldVersion)) {
                            log.info("#######  iSafe UpdateChecker  #######");
                            log.info("A new update for iSafe was found! " + newVersion);
                            log.info("You are currently running iSafe v" + oldVersion);
                            log.info("You can find this new version at BukkitDev.");
                            log.info("http://dev.bukkit.org/server-mods/blockthattnt/");
                            log.info("#####################################");
                        }
                    } catch (Exception ignored) {
                        //Ignored
                    }
                }
            }, 0, 432000);
        }
        getCommand("iSafe").setExecutor(new Commands(this));
        getServer().getPluginManager().getPermissions();
        checkMatch();
        
        if(iSafeConfig.getBoolean("UseVaultForPermissions", true)) {
            setupPermissions();
            if(verboseLogging() == true) {
                log.info("[iSafe] Using Vault for permissions!");
            }
        }
        
        try {
            Metrics metrics = new Metrics(this);
            metrics.start();
        } catch (IOException e) {
            // Failed to submit the stats :-(
        }
        
        if(verboseLogging() == true) {
            log.info("[" + pdffile.getName() + " :: " + version + "] " + " Enabled succesfully.");
        } else {
            if (debugMode() == true) {
                log.info(DEBUG_PREFIX + "Verbose logging is off.");
            }
        }
    }
    
    public boolean verboseLogging(){return getISafeConfig().getBoolean("VerboseLogging");}
    public boolean debugMode(){return getISafeConfig().getBoolean("DebugMode");}
    
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
        if(getISafeConfig().getBoolean("CheckForUpdates", true)) {
            sendUpdate = new SendUpdate(this);
        } else {
            sendUpdate = null;
            if (debugMode() == true) {
                log.info(DEBUG_PREFIX + "CheckForUpdates in the iSafeConfig.yml was disabled, therefor not registering "
                    + "the sendUpdate class.");
            }
        }
        
        dropBlacklist = new DropBlacklist(this);
        placeBlacklist = new PlaceBlacklist(this);
        breakBlacklist = new BreakBlacklist(this);
        pickupBlacklist = new PickupBlacklist(this);
        commandBlacklist = new CommandBlacklist(this);
        mobSpawnBlacklist = new MobSpawnBlacklist(this);
        censor = new Censor(this);
        dispenseBlacklist = new DispenseBlacklist(this);
        
        if(debugMode() == true) {
            log.info(DEBUG_PREFIX + "Registered classes.");
        }
    }
    
    private void fileManagement() {
        if(!(getDataFolder().exists())) 
        {
            getDataFolder().mkdirs(); 
        }
        
        File usersFolder = new File(getDataFolder() + File.separator + "Users");
        if(!(usersFolder.exists())) 
        {
            usersFolder.mkdir();
        }
        
        File exaFile = new File(usersFolder + File.separator + "_example.yml");
        if(!(exaFile.exists()))
        {
            try {
                FileConfiguration exampFile = YamlConfiguration.loadConfiguration(exaFile);
                exampFile.options().header(Data.setExFileHeader());
                exampFile.set("Username", "example");
                exampFile.set("Displayname", "example");
                exampFile.set("IPAddress", "127.0.0.1");
                exampFile.set("Gamemode", "survival");
                exampFile.set("Level", "50");
                exampFile.save(exaFile);
            } catch (Exception e) {
                log.info("[iSafe] Error creating example user file.");
                e.printStackTrace();
            }
        }
        
        reloadISafeConfig();
        loadISafeConfig();
        reloadISafeConfig();
        
        reloadConfig();
        loadConfig();
        reloadConfig();
        
        reloadBlacklist();
        loadBlacklist();
        reloadBlacklist();

        reloadCreatureManager();
        loadCreatureManager();
        reloadCreatureManager();
        
        reloadMessages();
        loadMessages();
        reloadMessages();
        
        if(verboseLogging() == true) {
            log.info("[iSafe] Loaded all files.");
        }
    }
    
    /**
     * Efficient method for checking whether we use Vault for perms or Bukkit/super perms.
     * 
     * @param p
     * @param permission
     * @return 
     */
    public boolean hasPermission(CommandSender p, String permission) {
        if(iSafeConfig.getBoolean("UseVaultForPermissions", true)) {
            if(perms.has(p, permission)) {
                return true;
            } else {
                noCmdPermission(p);
                return false;
            }
        } else {
            if(p.hasPermission(permission)) {
                return true;
            } else {
                noCmdPermission(p);
                return false;
            }
        }
    }
    
    public boolean hasPermission(Player p, String permission) {
        if(iSafeConfig.getBoolean("UseVaultForPermissions", true)) {
            if(perms.has(p, permission)) {
                return true;
            } else {
                noPermission(p);
                return false;
            }
        } else {
            if(p.hasPermission(permission)) {
                return true;
            } else {
                noPermission(p);
                return false;
            }
        }
    }
    
    public String colorize(String s) {
        if(s == null) return null;
            return s.replaceAll("&([0-9a-f])", "\u00A7$1");
    }
    
    public void noPermission(Player p) {
        String no_permission = getMessages().getString("Permissions.DefaultNoPermission");
        p.sendMessage(colorize(no_permission));
    }
    
    public void noCmdPermission(CommandSender sender) {
        String no_permission = getMessages().getString("Permissions.NoCmdPermission");
        sender.sendMessage(colorize(no_permission));
    }
    
    public void kickMessage(Player p) {
        String kickMsg = getMessages().getString("KickMessage");
        Server s = p.getServer();
        s.broadcastMessage(colorize(variablePName(p, kickMsg)));
    }
    
    public String variablePName(Player p, String s) {
        String pName = p.getName();
        return s.replace("+playername", pName);
    }
    
    public void loadConfig() {
        config = getConfig();
        config.options().header(Data.setConfigHeader());
        
        /*
         * New = New 3.0 nodes.
         * Old = Old 2.80 and lower nodes.
         */
        
        // New
        config.addDefault("Fire.DisableFireSpread", false);
        config.addDefault("Fire.PreventFlintAndSteelUsage", false);
        config.addDefault("Fire.DisableLavaIgnition", false);
        config.addDefault("Fire.DisableFireballIgnition", false);
        config.addDefault("Fire.DisableLightningIgnition", false);
        config.addDefault("Fire.PreventBlockBurn", false);
        
        config.addDefault("Enchantment.PreventEnchantment", false);
        config.addDefault("Enchantment.PreventCreativeModeEnchanting", false);
        
        config.addDefault("Furnace.DisableFurnaceUsage", false);
        
        config.addDefault("Weather.Disable.LightningStrike", false);
        config.addDefault("Weather.Disable.Thunder", false);
        config.addDefault("Weather.Disable.Storm", false);
        
        config.addDefault("World.PreventChunkUnload", false);
        config.addDefault("World.MakeISafeLoadChunks", false);
        config.addDefault("World.DisableStructureGrowth", false);
        config.addDefault("World.PreventBonemealUsage", false);
        config.addDefault("World.DisablePortalGeneration", false);
        
        config.addDefault("TreeGrowth.DisableFor.BigTree", false);
        config.addDefault("TreeGrowth.DisableFor.Birch", false);
        config.addDefault("TreeGrowth.DisableFor.BrownMushroom", false);
        config.addDefault("TreeGrowth.DisableFor.Redwood", false);
        config.addDefault("TreeGrowth.DisableFor.RedMushroom", false);
        config.addDefault("TreeGrowth.DisableFor.TallRedwood", false);
        config.addDefault("TreeGrowth.DisableFor.Tree", false);
        config.addDefault("TreeGrowth.DisableFor.Jungle", false);
        
        config.addDefault("Miscellaneous.DisableBlockGrow", false);
        config.addDefault("Miscellaneous.DisableLeavesDecay", false);
        config.addDefault("Miscellaneous.ForceBlocksToBeBuildable", false);
        config.addDefault("Miscellaneous.PreventExpBottleThrow", false);
        config.addDefault("Miscellaneous.EnableKickMessages", true);
        
        config.addDefault("AntiCheat.ForceLightLevel(Fullbright)", false);
        
        config.addDefault("Explosions.DisablePrimedExplosions", false);
        config.addDefault("Explosions.DisableAllExplosions", false);
        config.addDefault("Explosions.DisableCreeperExplosions", false);
        config.addDefault("Explosions.DisableEnderdragonBlockDamage", false);
        config.addDefault("Explosions.DisableTntExplosions", false);
        config.addDefault("Explosions.DisableFireballExplosions", false);
        config.addDefault("Explosions.DisableEnderCrystalExplosions", false);
        config.addDefault("Explosions.DebugExplosions", false);
        
        config.addDefault("Flow.DisableWaterFlow", false);
        config.addDefault("Flow.DisableLavaFlow", false);
        config.addDefault("Flow.DisableAirFlow", false);
        
        config.addDefault("Pistons.DisablePistonExtend", false);
        config.addDefault("Pistons.DisablePistonRetract", false);
        
        config.addDefault("BlockPhysics.DisableSandPhysics", false);
        config.addDefault("BlockPhysics.DisableGravelPhysics", false);
        
        config.addDefault("BlockFade.DisableIceMelting", false);
        config.addDefault("BlockFade.DisableSnowMelting", false);
        
        config.addDefault("ForceDrop.Glass", false);
        config.addDefault("ForceDrop.MobSpawner", false);
        config.addDefault("ForceDrop.Ice", false);
        config.addDefault("ForceDrop.Bedrock", false);
        
        config.addDefault("TeleportPlayerToSpawn.OnVoidFall", false);
        
        
        // Old
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
        
        config.addDefault("Physics.Disable-sand-physics", false);
        config.addDefault("Physics.Disable-gravel-physics", false);
        
        config.addDefault("Fade.Prevent-Ice-melting", false);
        config.addDefault("Fade.Prevent-Snow-melting", false);
        
        config.addDefault("Chunk.Prevent-chunks-unloading", false);
        config.addDefault("Chunk.Enable-Chunk-emergency-loader", false);
        
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
        
        config.set("Player-Interact.Erase the old values if you haven't already(this node is not an option)", null);
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
        
        config.addDefault("Entity/Player.Completely-Prevent.Health-Regeneration", false);
        config.addDefault("Entity/Player.Prevent.Custom-Health-Regeneration", false);
        config.addDefault("Entity/Player.Prevent.Eating-Health-Regeneration", false);
        config.addDefault("Entity/Player.Prevent.Regen-Health-Regeneration", false);
        config.addDefault("Entity/Player.Prevent.Satiated-Health-Regeneration", false);
        config.addDefault("Entity/Player.Prevent.Magic-Health-Regeneration", false);
        config.addDefault("Entity/Player.Prevent.MagicRegen-Health-Regeneration", false);
        
        config.addDefault("PlayerInteractEntity.Prevent-snowball-hitting-player", false);
        config.addDefault("PlayerInteractEntity.Prevent-arrow-hitting-player", false);
        
        
        // Figured out there's too many nodes to convert.. Lets just erase the old node and add the new node.
        config.addDefault("Fire.DisableFireSpread", false);
        config.addDefault("Fire.PreventFlintAndSteelUsage", false);
        config.addDefault("Fire.DisableLavaIgnition", false);
        config.addDefault("Fire.DisableFireballIgnition", false);
        config.addDefault("Fire.DisableLightningIgnition", false);
        
        this.getConfig().options().copyDefaults(true);
        saveConfig();
    }
    
    public void loadMessages() {
        messages = getMessages();
        messages.options().header(Data.setMessageHeader());
        
        messages.addDefault("Permissions.DefaultNoPermission", "&cNo permission.");
        messages.addDefault("Permissions.NoCmdPermission", "&cNo permission to do this command.");
        messages.addDefault("KickMessage", "&6+playername was kicked from the game.");
        
        this.getMessages().options().copyDefaults(true);
        saveMessages();
    }
    
    public void loadISafeConfig() {
        iSafeConfig = getISafeConfig();
        // Header..
        
        iSafeConfig.addDefault("VerboseLogging", false);
        iSafeConfig.addDefault("DebugMode", false);
        iSafeConfig.addDefault("CheckForUpdates", true);
        iSafeConfig.addDefault("UseVaultForPermissions", false);
        
        this.getISafeConfig().options().copyDefaults(true);
        saveISafeConfig();
    }
    
    public void loadCreatureManager() {
        creatureManager = getCreatureManager();
        //creatureManager.options().header(Data.setEntityManagerHeader());
        
        creatureManager.addDefault("Creatures.CreatureTarget.Disable-closest_player-target", false);
        creatureManager.addDefault("Creatures.CreatureTarget.Disable-custom-target", false);
        creatureManager.addDefault("Creatures.CreatureTarget.Disable-forgot_target-target", false);
        creatureManager.addDefault("Creatures.CreatureTarget.Disable-owner_attacked_target-target", false);
        creatureManager.addDefault("Creatures.CreatureTarget.Disable-pig_zombie_target-target", false);
        creatureManager.addDefault("Creatures.CreatureTarget.Disable-random_target-target", false);
        creatureManager.addDefault("Creatures.CreatureTarget.Disable-target_attacked_entity-target", false);
        creatureManager.addDefault("Creatures.CreatureTarget.Disable-target_attacked_owner-target", false);
        creatureManager.addDefault("Creatures.CreatureTarget.Disable-target_died-target", false);
        creatureManager.addDefault("Creatures.PoweredCreepers.Disable-PowerCase-Lightning", false);
        creatureManager.addDefault("Creatures.PoweredCreepers.Disable-PowerCase-Set-Off", false);
        creatureManager.addDefault("Creatures.PoweredCreepers.Disable-PowerCase-Set-On", false);
        creatureManager.addDefault("Creatures.Endermen.Prevent-endermen-griefing", false);
        creatureManager.addDefault("Creatures.Tame.Prevent-taming", false);
        creatureManager.addDefault("Creatures.Tame.Prevent-taming-for.Wolf", false);
        creatureManager.addDefault("Creatures.Slime.Prevent-SlimeSplit", false);
        creatureManager.addDefault("Creatures.Pig.Prevent-PigZap", false);
        creatureManager.addDefault("Creatures.DoorBreaking-PreventFor-zombies", false);
        creatureManager.addDefault("Creatures.Death.Disable-drops-onDeath", false);
        creatureManager.addDefault("Creatures.SheepDyeWool.Completely-Prevent-SheepDyeWool", false);
        creatureManager.addDefault("Creatures.SheepDyeWool.Prevent-SheepDyeWool-Color.Black", false);
        creatureManager.addDefault("Creatures.SheepDyeWool.Prevent-SheepDyeWool-Color.Blue", false);
        creatureManager.addDefault("Creatures.SheepDyeWool.Prevent-SheepDyeWool-Color.Brown", false);
        creatureManager.addDefault("Creatures.SheepDyeWool.Prevent-SheepDyeWool-Color.Cyan", false);
        creatureManager.addDefault("Creatures.SheepDyeWool.Prevent-SheepDyeWool-Color.Gray", false);
        creatureManager.addDefault("Creatures.SheepDyeWool.Prevent-SheepDyeWool-Color.Green", false);
        creatureManager.addDefault("Creatures.SheepDyeWool.Prevent-SheepDyeWool-Color.Light_Blue", false);
        creatureManager.addDefault("Creatures.SheepDyeWool.Prevent-SheepDyeWool-Color.Lime", false);
        creatureManager.addDefault("Creatures.SheepDyeWool.Prevent-SheepDyeWool-Color.Magenta", false);
        creatureManager.addDefault("Creatures.SheepDyeWool.Prevent-SheepDyeWool-Color.Orange", false);
        creatureManager.addDefault("Creatures.SheepDyeWool.Prevent-SheepDyeWool-Color.Pink", false);
        creatureManager.addDefault("Creatures.SheepDyeWool.Prevent-SheepDyeWool-Color.Purple", false);
        creatureManager.addDefault("Creatures.SheepDyeWool.Prevent-SheepDyeWool-Color.Red", false);
        creatureManager.addDefault("Creatures.SheepDyeWool.Prevent-SheepDyeWool-Color.Silver", false);
        creatureManager.addDefault("Creatures.SheepDyeWool.Prevent-SheepDyeWool-Color.White", false);
        creatureManager.addDefault("Creatures.SheepDyeWool.Prevent-SheepDyeWool-Color.Yellow", false);
        creatureManager.addDefault("Creatures.Combusting.Disable-for-allCreatures", false);
        creatureManager.addDefault("Creatures.Combusting.Disable-for.Blaze", false);
        creatureManager.addDefault("Creatures.Combusting.Disable-for.CaveSpider", false);
        creatureManager.addDefault("Creatures.Combusting.Disable-for.Chicken", false);
        creatureManager.addDefault("Creatures.Combusting.Disable-for.Cow", false);
        creatureManager.addDefault("Creatures.Combusting.Disable-for.Creeper", false);
        creatureManager.addDefault("Creatures.Combusting.Disable-for.EnderDragon", false);
        creatureManager.addDefault("Creatures.Combusting.Disable-for.Enderman", false);
        creatureManager.addDefault("Creatures.Combusting.Disable-for.Ghast", false);
        creatureManager.addDefault("Creatures.Combusting.Disable-for.Giant", false);
        creatureManager.addDefault("Creatures.Combusting.Disable-for.Golem", false);
        creatureManager.addDefault("Creatures.Combusting.Disable-for.IronGolem", false);
        creatureManager.addDefault("Creatures.Combusting.Disable-for.MagmaCube", false);
        creatureManager.addDefault("Creatures.Combusting.Disable-for.MushroomCow", false);
        creatureManager.addDefault("Creatures.Combusting.Disable-for.Ocelot", false);
        creatureManager.addDefault("Creatures.Combusting.Disable-for.Pig", false);
        creatureManager.addDefault("Creatures.Combusting.Disable-for.PigZombie", false);
        creatureManager.addDefault("Creatures.Combusting.Disable-for.Sheep", false);
        creatureManager.addDefault("Creatures.Combusting.Disable-for.Silverfish", false);
        creatureManager.addDefault("Creatures.Combusting.Disable-for.Skeleton", false);
        creatureManager.addDefault("Creatures.Combusting.Disable-for.Slime", false);
        creatureManager.addDefault("Creatures.Combusting.Disable-for.Snowman", false);
        creatureManager.addDefault("Creatures.Combusting.Disable-for.Spider", false);
        creatureManager.addDefault("Creatures.Combusting.Disable-for.Squid", false);
        creatureManager.addDefault("Creatures.Combusting.Disable-for.Villager", false);
        creatureManager.addDefault("Creatures.Combusting.Disable-for.Wolf", false);
        creatureManager.addDefault("Creatures.Combusting.Disable-for.Zombie", false);
        creatureManager.addDefault("Creatures.Prevent-cropTrampling", false);
        
        //MobSpawn blacklists.
        //Natural
        creatureManager.addDefault("MobSpawn.Natural.Debug.To-console", false);
        creatureManager.addDefault("MobSpawn.Natural.Worlds", Arrays.asList(Data.worlds1list));
        Data.worlds1 = creatureManager.getStringList("MobSpawn.Natural.Worlds");
        creatureManager.addDefault("MobSpawn.Natural.Blacklist", Arrays.asList(Data.mobspawnnaturallist));
        Data.mobspawnnatural = creatureManager.getStringList("MobSpawn.Natural.Blacklist");
        //Spawner
        creatureManager.addDefault("MobSpawn.Spawner.Debug.To-console", false);
        creatureManager.addDefault("MobSpawn.Spawner.Worlds", Arrays.asList(Data.worlds2list));
        Data.worlds2 = creatureManager.getStringList("MobSpawn.Spawner.Worlds");
        creatureManager.addDefault("MobSpawn.Spawner.Blacklist", Arrays.asList(Data.mobspawnspawnerlist));
        Data.mobspawnspawner = creatureManager.getStringList("MobSpawn.Spawner.Blacklist");
        //Custom
        creatureManager.addDefault("MobSpawn.Custom.Debug.To-console", false);
        creatureManager.addDefault("MobSpawn.Custom.Worlds", Arrays.asList(Data.worlds3list));
        Data.worlds3 = creatureManager.getStringList("MobSpawn.Custom.Worlds");
        creatureManager.addDefault("MobSpawn.Custom.Blacklist", Arrays.asList(Data.mobspawncustomlist));
        Data.mobspawncustom = creatureManager.getStringList("MobSpawn.Custom.Blacklist");
        //Egg(Chicken egg)
        creatureManager.addDefault("MobSpawn.Egg.Debug.To-console", false);
        creatureManager.addDefault("MobSpawn.Egg.Worlds", Arrays.asList(Data.worlds4list));
        Data.worlds4 = creatureManager.getStringList("MobSpawn.Egg.Worlds");
        creatureManager.addDefault("MobSpawn.Egg.Blacklist", Arrays.asList(Data.mobspawnegglist));
        Data.mobspawnegg = creatureManager.getStringList("MobSpawn.Egg.Blacklist");
        //SpawnerEgg
        creatureManager.addDefault("MobSpawn.SpawnerEgg.Debug.To-console", false);
        creatureManager.addDefault("MobSpawn.SpawnerEgg.Worlds", Arrays.asList(Data.worlds5list));
        Data.worlds5 = creatureManager.getStringList("MobSpawn.SpawnerEgg.Worlds");
        creatureManager.addDefault("MobSpawn.SpawnerEgg.Blacklist", Arrays.asList(Data.mobspawnspawneregglist));
        Data.mobspawnspawneregg = creatureManager.getStringList("MobSpawn.SpawnerEgg.Blacklist");
        
        this.getCreatureManager().options().copyDefaults(true);
        saveCreatureManager();
    }
    
    public void loadBlacklist() {
        blacklist = getBlacklist();
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
    
    public void reloadISafeConfig() {
        if (iSafeConfigFile == null) {
            iSafeConfigFile = new File(getDataFolder(), "iSafeConfig.yml");
        }
        iSafeConfig = YamlConfiguration.loadConfiguration(iSafeConfigFile);
        
        // Look for defaults in the jar
        InputStream defConfigStream = getResource("iSafeConfig.yml");
        if (defConfigStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            iSafeConfig.setDefaults(defConfig);
        }
    }
    
    public FileConfiguration getISafeConfig() {
        if (iSafeConfig == null) {
            reloadBlacklist();
        }
        return iSafeConfig;
    }
    
    public void saveISafeConfig() {
        if (iSafeConfig == null || iSafeConfigFile == null) {
            return;
        }
        try {
            iSafeConfig.save(iSafeConfigFile);
        } catch (IOException ex) {
            Logger.getLogger(JavaPlugin.class.getName()).log(Level.SEVERE, "Error saving iSafeConfig to " + iSafeConfigFile, ex);
        }
    }
    
    public void reloadMessages() {
        if (messagesFile == null) {
            messagesFile = new File(getDataFolder(), "messages.yml");
        }
        messages = YamlConfiguration.loadConfiguration(messagesFile);
        
        // Look for defaults in the jar
        InputStream defConfigStream = getResource("messages.yml");
        if (defConfigStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            messages.setDefaults(defConfig);
        }
    }
    
    public FileConfiguration getMessages() {
        if (messages == null) {
            reloadBlacklist();
        }
        return messages;
    }
    
    public void saveMessages() {
        if (messages == null || messagesFile == null) {
            return;
        }
        try {
            messages.save(messagesFile);
        } catch (IOException ex) {
            Logger.getLogger(JavaPlugin.class.getName()).log(Level.SEVERE, "Error saving Messages to " + messagesFile, ex);
        }
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
    public void reloadCreatureManager() {
        if (creatureManagerFile == null) {
            creatureManagerFile = new File(getDataFolder(), "creatureManager.yml");
        }
        creatureManager = YamlConfiguration.loadConfiguration(creatureManagerFile);
        
        InputStream defConfigStream = getResource("creatureManager.yml");
        if (defConfigStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            creatureManager.setDefaults(defConfig);
        }
    }
    
    public FileConfiguration getCreatureManager() {
        if (creatureManager == null) {
            reloadCreatureManager();
        }
        return creatureManager;
    }
    
    public void saveCreatureManager() {
        if (creatureManager == null || creatureManagerFile == null) {
            return;
        }
        try {
            creatureManager.save(creatureManagerFile);
        } catch (IOException ex) {
            Logger.getLogger(JavaPlugin.class.getName()).log(Level.SEVERE, "Error saving creatureManager to " + creatureManagerFile, ex);
        }
    }
    
    public boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        perms = rsp.getProvider();
        return perms != null;
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
        }
    }
    
    //Update checker
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
}
