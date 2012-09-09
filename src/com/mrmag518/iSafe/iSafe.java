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

import com.mrmag518.iSafe.Events.BlockEvents.*;
import com.mrmag518.iSafe.Events.EntityEvents.*;
import com.mrmag518.iSafe.Events.WorldEvents.*;
import com.mrmag518.iSafe.Blacklists.*;
import com.mrmag518.iSafe.Commands.Commands;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.File;
import java.net.URL;

import java.util.HashMap;
import javax.xml.parsers.DocumentBuilderFactory;

import net.milkbowl.vault.permission.Permission;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class iSafe extends JavaPlugin {
    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    private String fileversion = "iSafe v3.21";
    private Double ConfigVersion = 3.21;
    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    
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
    private SendUpdate sendUpdate = null;
    
    private Blacklists blacklistClass = null;
    
    public double currentVersion;
    public double newVersion;
    
    public static iSafe plugin;
    
    @SuppressWarnings("NonConstantLogger")
    public final Logger log = Logger.getLogger("Minecraft");
    
    public String DEBUG_PREFIX = "[iSafe DEBUG]" + " ";
    public static Permission perms = null;
    
    private FileConfiguration iSafeConfig = null;
    private File iSafeConfigFile = null;
    private FileConfiguration messages = null;
    private File messagesFile = null;
    private FileConfiguration creatureManager = null;
    private File creatureManagerFile = null;
    private FileConfiguration blacklists = null;
    private File blacklistsFile = null;
    private FileConfiguration config;
    
    /*public FileWriter logs;
    public BufferedWriter logsFile;*/
    
    public boolean checkingUpdatePerms = false;
    public boolean cancelDamagePerms = false;
    public boolean checkingSpamPerms = false;
    public boolean checkingFullbrightPerms = false;
    
    private boolean isStartup = false;
    
    public HashMap<String, Integer> spamDB = new HashMap<>();

    @Override
    public void onDisable() {
        PluginDescriptionFile pdffile = this.getDescription();
        if (verboseLogging() == true) {
            verboseLog("v" + pdffile.getVersion() + " disabled.");
        } else {
            debugLog("Verbose logging is off.");
        }
    }

    @Override
    public void onEnable() {
        isStartup = true;
        
        fileLoadManagement();
        
        currentVersion = Double.valueOf(getDescription().getVersion());
        debugLog("Debug mode is enabled!");
        
        registerClasses();
        PluginDescriptionFile pdffile = this.getDescription();
        if (getISafeConfig().getBoolean("CheckForUpdates", true)) {
            //Update checker - From MilkBowl.
            this.getServer().getScheduler().scheduleAsyncRepeatingTask(this, new Runnable() {

                @Override
                public void run() {
                    try {
                        newVersion = updateCheck(currentVersion);
                        
                        if(newVersion > currentVersion) {
                            log.info(" ");
                            log.info("#######  iSafe UpdateChecker  #######");
                            log.info("A new update for iSafe was found! " + newVersion);
                            log.info("You are currently running version: " + currentVersion);
                            log.info("You can find this new version at BukkitDev.");
                            log.info("http://dev.bukkit.org/server-mods/blockthattnt/");
                            log.info("#####################################");
                            log.info(" ");
                        }
                    } catch (Exception ignored) {
                        //Ignored
                    }
                }
            }, 0, 36000);
        }
        getCommand("iSafe").setExecutor(new Commands(this));
        
        checkMatch();
        
        checkPlugins();

        try {
            Metrics metrics = new Metrics(this);
            metrics.start();
        } catch (IOException e) {
            // Failed to submit the stats :-(
        }

        if (verboseLogging() == true) {
            verboseLog("v" + pdffile.getVersion() + " enabled.");
        } else {
            debugLog("Verbose logging is off.");
        }
        
        isStartup = false;
    }

    public boolean verboseLogging() {
        return getISafeConfig().getBoolean("VerboseLogging");
    }

    public boolean debugMode() {
        return getISafeConfig().getBoolean("DebugMode");
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

        if (getISafeConfig().getBoolean("CreateUserFiles", true)) {
            UFC = new UserFileCreator(this);
        } else {
            UFC = null;
            debugLog("CreateUserFiles in the iSafeConfig.yml was disabled, therefor not register the UserFileCreator class.");
        }

        if (getISafeConfig().getBoolean("CheckForUpdates", true)) {
            sendUpdate = new SendUpdate(this);
        } else {
            sendUpdate = null;
            debugLog("CheckForUpdates in the iSafeConfig.yml was disabled, therefor not registering the sendUpdate class.");
        }
        
        blacklistClass = new Blacklists(this);
        
        debugLog("Registered classes.");
    }
    
    public void fileLoadManagement() {
        if (!(getDataFolder().exists())) {
            getDataFolder().mkdirs();
        }
        
        File usersDir = new File(getDataFolder(), "Users");
        if (!(usersDir.exists())) {
            usersDir.mkdir();
        }
        
        File exaFile = new File(usersDir + File.separator + "_example.yml");
        if (!(exaFile.exists())) {
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
                log.info("[iSafe] Error creating example user file. (_example.yml)");
                e.printStackTrace();
            }
        }
        
        
        // Always load iSafeConfig first.
        reloadISafeConfig();
        loadISafeConfig();
        reloadISafeConfig();
        
        reloadMessages();
        loadMessages();
        reloadMessages();
        
        reloadConfig();
        loadConfig();
        reloadConfig();
        
        if(isStartup == true) {
            getServer().getScheduler().scheduleAsyncDelayedTask(this, new Runnable() {
                @Override
                public void run() {
                    reloadCreatureManager();
                    loadCreatureManager();
                    reloadCreatureManager();

                    reloadBlacklists();
                    loadBlacklists();
                    reloadBlacklists();

                    setupVault();
                }
            }, 40);
        } else {
            reloadCreatureManager();
            loadCreatureManager();
            reloadCreatureManager();

            reloadBlacklists();
            loadBlacklists();
            reloadBlacklists();

            setupVault();
        }
        
        if(getConfig().getBoolean("Damage.EnablePermissions", true)) {
            cancelDamagePerms = true;
        } else {
            cancelDamagePerms = false;
        }
        
        // loadLogs();
    }
    
    private void setupVault() {
        if(getISafeConfig().getBoolean("UseVaultForPermissions", true)) {
            if (getServer().getPluginManager().getPlugin("Vault") != null) {
                verboseLog("Using Vault for permissions!");
                setupPermissions();
            } else {
                log.severe("[iSafe] Vault.jar was NOT found in your plugins folder!");
                log.severe("[iSafe] You need to have Vault.jar in the plugins folder if you're going to use Vault for permissions!");
                log.warning("[iSafe] Settings UseVaultForPermissions in your iSafeConfig.yml to false ..");
                getISafeConfig().set("UseVaultForPermissions", false);
                saveISafeConfig();
            }
        }
    }
    
    private void checkPlugins() {
        PluginManager pm = getServer().getPluginManager();
        if(pm.getPlugin("EssentialsProtect") != null) {
            debugLog("You are running EssentialsProtect, cool!");
            debugLog("Have in mind that some of iSafe's and EssentialsProtect's features can 'collide'.");
        }
        if(pm.getPlugin("WorldGuard") != null) {
            debugLog("You are running WorldGuard, cool!");
            debugLog("Have in mind that some of iSafe's and WorldGuards's features can 'collide'.");
        }
    }
    
    public void verboseLog(String string) {
        if(verboseLogging() == true) {
            log.info("[iSafe] " + string);
        }
    }
    
    public void debugLog(String string) {
        if(debugMode() == true) {
            log.info(DEBUG_PREFIX + string);
        }
    }

    public boolean hasPermission(CommandSender p, String permission) {
        if (iSafeConfig.getBoolean("UseVaultForPermissions", true)) {
            if (perms.has(p, permission)) {
                return true;
            } else {
                noCmdPermission(p);
                return false;
            }
        } else {
            if (p.hasPermission(permission)) {
                return true;
            } else {
                noCmdPermission(p);
                return false;
            }
        }
    }
    
    public boolean hasBlacklistPermission(Player p, String permission) {
        if (iSafeConfig.getBoolean("UseVaultForPermissions", true)) {
            if (perms.has(p, permission)) {
                return true;
            } else {
                return false;
            }
        } else {
            if (p.hasPermission(permission)) {
                return true;
            } else {
                return false;
            }
        }
    }

    public boolean hasPermission(Player p, String permission) {
        if (iSafeConfig.getBoolean("UseVaultForPermissions", true)) {
            if (perms.has(p, permission)) {
                return true;
            } else {
                if (checkingUpdatePerms == true 
                        || checkingSpamPerms == true
                        || checkingFullbrightPerms == true) 
                {
                    checkingUpdatePerms = false;
                    checkingSpamPerms = false;
                    checkingFullbrightPerms = false;
                } else {
                    noPermission(p);
                }
                return false;
            }
        } else {
            if (p.hasPermission(permission)) {
                return true;
            } else {
                if (checkingUpdatePerms == true 
                        || checkingSpamPerms == true
                        || checkingFullbrightPerms == true) 
                {
                    checkingUpdatePerms = false;
                    checkingSpamPerms = false;
                    checkingFullbrightPerms = false;
                } else {
                    noPermission(p);
                }
                return false;
            }
        }
    }

    private String scanVariables(
            String configString, String playerName, 
            String cmd, String blockName, 
            String item, String world, 
            String word, String recipe) {
        
        String result = configString;
        
        if(playerName != null) {
            if(configString.contains("%playername%")) {
                result = result.replaceAll("%playername%", playerName);
            }
        }
        if(cmd != null) {
            if(configString.contains("%command%")) {
                result = result.replace("%command%", cmd);
            }
        }
        if(blockName != null) {
            if(configString.contains("%block%")) {
                result = result.replaceAll("%block%", blockName);
            }
        }
        if(item != null) {
            if(configString.contains("%item%")) {
                result = result.replaceAll("%item%", item);
            }
        }
        if(world != null) {
            if(configString.contains("%world%")) {
                result = result.replaceAll("%world%", world);
            }
        }
        if(word != null) {
            if(configString.contains("%word%")) {
                result = result.replaceAll("%word%", word);
            }
        }
        if(recipe != null) {
            if(configString.contains("%recipe%")) {
                result = result.replaceAll("%recipe%", recipe);
            }
        }
        
        result = colorize(result);
        return result;
    }
    
    public String colorize(String s) {
        if (s == null) {
            return null;
        }
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
        s.broadcastMessage(scanVariables(kickMsg, p.getName(), 
                null, null, 
                null, p.getWorld().getName(),
                null, null));
    }

    public String sameNickPlaying(Player p) {
        String kickMsg = getMessages().getString("SameNickAlreadyPlaying");
        return scanVariables(kickMsg, p.getName(), 
                null, null, 
                null, p.getWorld().getName(), 
                null, null);
    }

    public String denyNonOpsJoin() {
        String kickMsg = getMessages().getString("OnlyOpsCanJoin");
        return scanVariables(kickMsg, null, 
                null, null, 
                null, null, 
                null, null);
    }

    public String commandLogger(Player p, PlayerCommandPreprocessEvent event) {
        String logged = getMessages().getString("CommandLogger");
        return scanVariables(logged, p.getName(), event.getMessage(), 
                null, null, 
                p.getWorld().getName(), 
                null, null);
    }
    
    public String blacklistInteractKickMsg(Block b) {
        String kickMsg = getMessages().getString("Blacklists.Interact.KickMessage");
        return scanVariables(kickMsg, null, 
                null, b.getType().name().toLowerCase(), 
                null, b.getWorld().getName(), 
                null, null);
    }
    
    public String blacklistPlaceKickMsg(Block b) {
        String kickMsg = getMessages().getString("Blacklists.Place.KickMessage");
        return scanVariables(kickMsg, null, 
                null, b.getType().name().toLowerCase(), 
                null, b.getWorld().getName(), 
                null, null);
    }
    
    public String blacklistBreakKickMsg(Block b) {
        String kickMsg = getMessages().getString("Blacklists.Break.KickMessage");
        return scanVariables(kickMsg, null, 
                null, b.getType().name().toLowerCase(), 
                null, b.getWorld().getName(), 
                null, null);
    }
    
    public String blacklistCensorKickMsg(String word) {
        String kickMsg = getMessages().getString("Blacklists.Censor.KickMessage");
        return scanVariables(kickMsg, null, 
                null, null, 
                null, null, 
                word, null);
    }
    
    public String blacklistDropKickMsg(Item i) {
        String kickMsg = getMessages().getString("Blacklists.Drop.KickMessage");
        return scanVariables(kickMsg, null, 
                null, null, 
                i.getItemStack().getType().name().toLowerCase(), i.getWorld().getName(),
                null, null);
    }
    
    public String blacklistPickupKickMsg(String item) {
        String kickMsg = getMessages().getString("Blacklists.Pickup.KickMessage");
        return scanVariables(kickMsg, null,
                null, null, 
                item, null, 
                null, null);
    }
    
    public String blacklistCommandKickMsg(String cmd, String world) {
        String kickMsg = getMessages().getString("Blacklists.Command.KickMessage");
        return scanVariables(kickMsg, null, 
                cmd, null,
                null, world, 
                null, null);
    }
    
    
    public String blacklistInteractMsg(Block b) {
        String disallowedMsg = getMessages().getString("Blacklists.Interact.DisallowedMessage");
        return scanVariables(disallowedMsg, null, 
                null, b.getType().name().toLowerCase(), 
                null, b.getWorld().getName(), 
                null, null);
    }
    
    public String blacklistPlaceMsg(Block b) {
        String disallowedMsg = getMessages().getString("Blacklists.Place.DisallowedMessage");
        return scanVariables(disallowedMsg, null,
                null, b.getType().name().toLowerCase(), 
                null, b.getWorld().getName(), 
                null, null);
    }
    
    public String blacklistBreakMsg(Block b) {
        String disallowedMsg = getMessages().getString("Blacklists.Break.DisallowedMessage");
        return scanVariables(disallowedMsg, null, 
                null, b.getType().name().toLowerCase(), 
                null, b.getWorld().getName(), 
                null, null);
    }
    
    public String blacklistCensorMsg(String word, World world) {
        String disallowedMsg = getMessages().getString("Blacklists.Censor.DisallowedMessage");
        return scanVariables(disallowedMsg, null, 
                null, null, 
                null, world.getName(), 
                word, null);
    }
    
    public String blacklistDropMsg(String item, World world) {
        String disallowedMsg = getMessages().getString("Blacklists.Drop.DisallowedMessage");
        return scanVariables(disallowedMsg, null,
                null, null, 
                item, world.getName(), 
                null, null);
    }
    
    /*public String blacklistPickupMsg(Item i) {
        String disallowedMsg = getMessages().getString("Blacklists.Pickup.DisallowedMessage");
        return scanVariables(disallowedMsg, null, null, null, i.getItemStack().getType().name().toLowerCase(), i.getWorld().getName(), null);
    }*/
    
    public String blacklistCraftingMsg(String recipe, World world) {
        String disallowedMsg = getMessages().getString("Blacklists.Crafting.DisallowedMessage");
        return scanVariables(disallowedMsg, null, 
                null, null, 
                null, world.getName(), 
                null, recipe);
    }
    
    public String blacklistCommandMsg(String cmd, String world) {
        String disallowedMsg = getMessages().getString("Blacklists.Command.DisallowedMessage");
        return scanVariables(disallowedMsg, null, 
                cmd, null, 
                null, world, 
                null, null);
    }
    
    /*private void loadLogs() {
        File LOG = new File(getDataFolder(), "iSafe.log");
        
        if(!LOG.exists()) {
            try {
                LOG.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(iSafe.class.getName()).log(Level.SEVERE, "An error ocurred while trying to create iSafe.log", ex);
            }
        }
        
        try{
            logs = new FileWriter(getDataFolder() + File.separator + "iSafe.log");
        }catch(IOException ioe) {
            ioe.printStackTrace();
        }
                
        logsFile = new BufferedWriter(logs);
        
        try {
            logsFile.write(Data.getDate() + ": 2 test \n");
            logsFile.write(Data.getDate() + ": 89503 MRMAG518 IS PURE OWNAGE");
            logsFile.flush();
        } catch (IOException ex) {
            Logger.getLogger(iSafe.class.getName()).log(Level.SEVERE, "test", ex);
        }
    }*/
    
    private void loadConfig() {
        config = getConfig();
        config.options().header(Data.setConfigHeader());
        
        config.addDefault("ConfigVersion", Double.valueOf(ConfigVersion));
        if(config.getDouble("ConfigVersion") != Double.valueOf(ConfigVersion)) {
            // If there is anything to modify in the 'new' version, fix that here.
            log.warning("[iSafe] ConfigVersion was modified! Setting config version to right value ..");
            config.set("ConfigVersion", Double.valueOf(ConfigVersion));
            
            if(config.get("AntiCheat/Sucurity.ForceLightLevel(Fullbright)") != null) {
                config.set("AntiCheat/Sucurity.ForceLightLevel(Fullbright)", null);
            }
        }
        
        config.addDefault("Fire.DisableFireSpread", false);
        config.addDefault("Fire.PreventFlintAndSteelUsage", false);
        config.addDefault("Fire.DisableLavaIgnition", false);
        config.addDefault("Fire.DisableFireballIgnition", false);
        config.addDefault("Fire.DisableLightningIgnition", false);
        config.addDefault("Fire.PreventBlockBurn", false);

        config.addDefault("Enchantment.PreventEnchantment", false);
        config.addDefault("Enchantment.PreventCreativeModeEnchanting", false);

        config.addDefault("Furnace.DisableFurnaceUsage", false);

        config.addDefault("Weather.DisableLightningStrike", false);
        config.addDefault("Weather.DisableThunder", false);
        config.addDefault("Weather.DisableStorm", false);

        config.addDefault("World.PreventChunkUnload", false);
        config.addDefault("World.MakeISafeLoadChunks", false);
        config.addDefault("World.DisableStructureGrowth", false);
        config.addDefault("World.PreventBonemealUsage", false);
        config.addDefault("World.DisablePortalGeneration", false);
        config.addDefault("World.DisableExpDrop", false);
        config.addDefault("World.DisableItemSpawn", false);
        config.addDefault("World.EnablePortalCreationPerms", false);

        config.addDefault("TreeGrowth.DisableFor.BigTree", false);
        config.addDefault("TreeGrowth.DisableFor.Birch", false);
        config.addDefault("TreeGrowth.DisableFor.BrownMushroom", false);
        config.addDefault("TreeGrowth.DisableFor.Redwood", false);
        config.addDefault("TreeGrowth.DisableFor.RedMushroom", false);
        config.addDefault("TreeGrowth.DisableFor.TallRedwood", false);
        config.addDefault("TreeGrowth.DisableFor.Tree", false);
        config.addDefault("TreeGrowth.DisableFor.Jungle", false);

        config.addDefault("Miscellaneous.DisableBlockGrow", false);
        config.addDefault("Miscellaneous.DisableBlockSpreading", false);
        config.addDefault("Miscellaneous.DisableLeavesDecay", false);
        config.addDefault("Miscellaneous.ForceBlocksToBeBuildable", false);
        config.addDefault("Miscellaneous.PreventExpBottleThrow", false);
        config.addDefault("Miscellaneous.ForcePermissionsToUseBed", false);
        config.addDefault("Miscellaneous.ForcePermissionsToFish", false);
        config.addDefault("Miscellaneous.OnlyLetOPsJoin", false);
        config.addDefault("Miscellaneous.DisableHunger", false);

        config.addDefault("AntiCheat/Security.LightLevel.PreventFullbright", false);
        config.addDefault("AntiCheat/Security.LightLevel.MinimumLevelBeforeDetection", 1);
        config.addDefault("AntiCheat/Security.LightLevel.CheckCreativeMode", false);
        config.addDefault("AntiCheat/Security.LightLevel.CheckAtNight", true);
        config.addDefault("AntiCheat/Sucurity.KickJoinerIfSameNickIsOnline", false);
        config.addDefault("AntiCheat/Sucurity.SimpleAntiSpam", false);

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

        config.addDefault("Buckets.Lava.Prevent", false);
        config.addDefault("Buckets.Lava.CheckedWorlds", Arrays.asList(Data.LavaBucketWorldList));
        Data.LavaBucketWorld = config.getStringList("Buckets.Lava.CheckedWorlds");
        config.addDefault("Buckets.Water.Prevent", false);
        config.addDefault("Buckets.Water.CheckedWorlds", Arrays.asList(Data.WaterBucketWorldList));
        Data.WaterBucketWorld = config.getStringList("Buckets.Water.CheckedWorlds");

        config.addDefault("Movement.DisableSprinting", false);
        config.addDefault("Movement.DisableSneaking", false);
        config.addDefault("Movement.PreventCropTrampling", false);

        config.addDefault("Gamemode.SwitchToSurvivalOnQuit", false);
        config.addDefault("Gamemode.SwitchToCreativeOnQuit", false);
        config.addDefault("Gamemode.DisableGamemodeChange", false);
        config.addDefault("Gamemode.DisableSurvivalToCreativeChange", false);
        config.addDefault("Gamemode.DisableCreativeToSurvivalChange", false);

        config.addDefault("Teleport.DisableAllTeleportCauses", false);
        config.addDefault("Teleport.Disable.CommandCause", false);
        config.addDefault("Teleport.Disable.EnderpearlCause", false);
        config.addDefault("Teleport.Disable.PluginCause", false);
        config.addDefault("Teleport.Disable.UnknownCause", false);
        config.addDefault("Teleport.Disable.NetherportalCause", false);
        config.addDefault("Teleport.Disable.CommandCause", false);

        config.addDefault("Chat.ForcePermissionToChat", false);
        config.addDefault("Chat.EnableKickMessages", true);
        config.addDefault("Chat.LogCommands", true);

        config.addDefault("VoidFall.TeleportPlayerToSpawn", false);
        config.addDefault("VoidFall.TeleportPlayerBackAndFixHole", true);
        config.addDefault("VoidFall.FixHoleWithGlass", true);
        config.addDefault("VoidFall.FixHoleWithBedrock", false);

        config.addDefault("Damage.EnablePermissions", false);
        config.addDefault("Damage.DisableVillagerDamage", false);
        config.addDefault("Damage.DisablePlayerDamage", false);
        config.addDefault("Damage.DisableExplosionDamage", false);
        config.addDefault("Damage.DisableFireDamage", false);
        config.addDefault("Damage.DisableContactDamage", false);
        config.addDefault("Damage.DisableCustomDamage", false);
        config.addDefault("Damage.DisableDrowningDamage", false);
        config.addDefault("Damage.DisableEntityAttackDamage", false);
        config.addDefault("Damage.DisableFallDamage", false);
        config.addDefault("Damage.DisableLavaDamage", false);
        config.addDefault("Damage.DisableLightningDamage", false);
        config.addDefault("Damage.DisableMagicDamage", false);
        config.addDefault("Damage.DisablePoisonDamage", false);
        config.addDefault("Damage.DisableProjectileDamage", false);
        config.addDefault("Damage.DisableStarvationDamage", false);
        config.addDefault("Damage.DisableSuffocationDamage", false);
        config.addDefault("Damage.DisableSuicideDamage", false);
        config.addDefault("Damage.DisableVoidDamage", false);

        config.addDefault("HealthRegen.DisableHealthRegeneration", false);
        config.addDefault("HealthRegen.DisableCustomHealthRegen", false);
        config.addDefault("HealthRegen.DisableEatingHealthRegen", false);
        config.addDefault("HealthRegen.DisableNaturalHealthRegen", false);
        config.addDefault("HealthRegen.DisableSatiatedHealthRegen", false);
        config.addDefault("HealthRegen.DisableMagicHealthRegen", false);

        this.getConfig().options().copyDefaults(true);
        saveConfig();
    }

    private void loadMessages() {
        messages = getMessages();
        messages.options().header(Data.setMessageHeader());
        
        // Note to self; Do not create un-needed variables if not needed. Duh.
        
        messages.addDefault("ConfigVersion", Double.valueOf(ConfigVersion));
        if(messages.getDouble("ConfigVersion") != Double.valueOf(ConfigVersion)) {
            log.warning("[iSafe] ConfigVersion was modified! Setting config version to right value ..");
            messages.set("ConfigVersion", Double.valueOf(ConfigVersion));
        }

        messages.addDefault("Permissions.DefaultNoPermission", "&cNo permission.");
        messages.addDefault("Permissions.NoCmdPermission", "&cNo permission to do this command.");
        messages.addDefault("KickMessage", "&6%playername% was kicked from the game.");
        messages.addDefault("SameNickAlreadyPlaying", "&cThe username &f%playername% &cis already online!");
        messages.addDefault("OnlyOpsCanJoin", "&cOnly OPs can join this server!");
        messages.addDefault("CommandLogger", "%playername% did or tried to do the command %command%");
        messages.addDefault("SpamDetection", "&cDetected spam! Please calm down.");
        
        //----
        messages.addDefault("Blacklists.Interact.KickMessage", "&cKicked for attempting to interact with &f%block%");
        messages.addDefault("Blacklists.Interact.DisallowedMessage", "&cYou do not have access to interact with &7%block% &cin world &7%world%");
        //----
        messages.addDefault("Blacklists.Place.KickMessage", "&cKicked for attempting to place &f%block%");
        messages.addDefault("Blacklists.Place.DisallowedMessage", "&cYou do not have access to place &7%block% &cin world &7%world%");
        //----
        messages.addDefault("Blacklists.Break.KickMessage", "&cKicked for attempting to break &f%block%");
        messages.addDefault("Blacklists.Break.DisallowedMessage", "&cYou do not have access to break &7%block% &cin world &7%world%");
        //----
        messages.addDefault("Blacklists.Censor.KickMessage", "&cKicked for attempting to send a message contaning &7%word%");
        messages.addDefault("Blacklists.Censor.DisallowedMessage", "&c%word% is censored!");
        //----
        messages.addDefault("Blacklists.Drop.KickMessage", "&cKicked for attempting to drop &f%item%");
        messages.addDefault("Blacklists.Drop.DisallowedMessage", "&cYou do not have access to drop &7%item% in world %world%");
        //----
        messages.addDefault("Blacklists.Command.KickMessage", "&cKicked for attempting to do command &f%command%");
        messages.addDefault("Blacklists.Command.DisallowedMessage", "&cThe command %command% is disabled in world %world%!");
        //----
        messages.addDefault("Blacklists.Crafting.DisallowedMessage", "&cYou do not have access to craft &7%recipe% &cin world %world%");
        
        this.getMessages().options().copyDefaults(true);
        saveMessages();
    }

    private void loadISafeConfig() {
        iSafeConfig = getISafeConfig();
        iSafeConfig.options().header(Data.setISafeConfigHeader());
        
        iSafeConfig.addDefault("ConfigVersion", Double.valueOf(ConfigVersion));
        if(iSafeConfig.getDouble("ConfigVersion") != Double.valueOf(ConfigVersion)) {
            log.warning("[iSafe] ConfigVersion was modified! Setting config version to right value ..");
            iSafeConfig.set("ConfigVersion", Double.valueOf(ConfigVersion));
        }
        
        iSafeConfig.addDefault("VerboseLogging", false);
        iSafeConfig.addDefault("DebugMode", false);
        iSafeConfig.addDefault("CheckForUpdates", true);
        iSafeConfig.addDefault("UseVaultForPermissions", false);
        iSafeConfig.addDefault("CreateUserFiles", true);

        this.getISafeConfig().options().copyDefaults(true);
        saveISafeConfig();
    }

    private void loadCreatureManager() {
        creatureManager = getCreatureManager();
        creatureManager.options().header(Data.setCreatureManagerHeader());
        
        creatureManager.addDefault("ConfigVersion", Double.valueOf(ConfigVersion));
        if(creatureManager.getDouble("ConfigVersion") != Double.valueOf(ConfigVersion)) {
            log.warning("[iSafe] ConfigVersion was modified! Setting config version to right value ..");
            creatureManager.set("ConfigVersion", Double.valueOf(ConfigVersion));
        }
        
        creatureManager.addDefault("Creatures.CreatureTarget.Disable-closest_player-target", false);
        creatureManager.addDefault("Creatures.CreatureTarget.Disable-custom-target", false);
        creatureManager.addDefault("Creatures.CreatureTarget.Disable-forgot_target-target", false);
        creatureManager.addDefault("Creatures.CreatureTarget.Disable-owner_attacked_target-target", false);
        creatureManager.addDefault("Creatures.CreatureTarget.Disable-pig_zombie_target-target", false);
        creatureManager.addDefault("Creatures.CreatureTarget.Disable-random_target-target", false);
        creatureManager.addDefault("Creatures.CreatureTarget.Disable-target_attacked_entity-target", false);
        creatureManager.addDefault("Creatures.CreatureTarget.Disable-target_attacked_owner-target", false);
        creatureManager.addDefault("Creatures.CreatureTarget.Disable-target_died-target", false);
        
        creatureManager.addDefault("Creatures.PoweredCreepers.DisableLightningCause", false);
        creatureManager.addDefault("Creatures.PoweredCreepers.DisableSetOffCause", false);
        creatureManager.addDefault("Creatures.PoweredCreepers.DisableSetOnCause", false);
        
        creatureManager.addDefault("Creatures.Endermen.PreventEndermenGriefing", false);
        creatureManager.addDefault("Creatures.Tame.DisableTaming", false);
        creatureManager.addDefault("Creatures.Slime.DisableSlimeSplit", false);
        creatureManager.addDefault("Creatures.Pig.DisabletPigZap", false);
        creatureManager.addDefault("Creatures.Zombie.DisableDoorBreak", false);
        creatureManager.addDefault("Creatures.Death.DisableDrops", false);
        creatureManager.addDefault("Creatures.DisableCropTrampling", false);
        
        creatureManager.addDefault("Creatures.SheepDyeWool.TotallyDisable", false);
        creatureManager.addDefault("Creatures.SheepDyeWool.DisableColor.Black", false);
        creatureManager.addDefault("Creatures.SheepDyeWool.DisableColor.Blue", false);
        creatureManager.addDefault("Creatures.SheepDyeWool.DisableColor.Brown", false);
        creatureManager.addDefault("Creatures.SheepDyeWool.DisableColor.Cyan", false);
        creatureManager.addDefault("Creatures.SheepDyeWool.DisableColor.Gray", false);
        creatureManager.addDefault("Creatures.SheepDyeWool.DisableColor.Green", false);
        creatureManager.addDefault("Creatures.SheepDyeWool.DisableColor.Light_Blue", false);
        creatureManager.addDefault("Creatures.SheepDyeWool.DisableColor.Lime", false);
        creatureManager.addDefault("Creatures.SheepDyeWool.DisableColor.Magenta", false);
        creatureManager.addDefault("Creatures.SheepDyeWool.DisableColor.Orange", false);
        creatureManager.addDefault("Creatures.SheepDyeWool.DisableColor.Pink", false);
        creatureManager.addDefault("Creatures.SheepDyeWool.DisableColor.Purple", false);
        creatureManager.addDefault("Creatures.SheepDyeWool.DisableColor.Red", false);
        creatureManager.addDefault("Creatures.SheepDyeWool.DisableColor.Silver", false);
        creatureManager.addDefault("Creatures.SheepDyeWool.DisableColor.White", false);
        creatureManager.addDefault("Creatures.SheepDyeWool.DisableColor.Yellow", false);
        
        creatureManager.addDefault("Creatures.Combusting.DisableFor-allCreatures", false);
        creatureManager.addDefault("Creatures.Combusting.DisableFor.Giant", false);
        creatureManager.addDefault("Creatures.Combusting.DisableFor.PigZombie", false);
        creatureManager.addDefault("Creatures.Combusting.DisableFor.Skeleton", false);
        creatureManager.addDefault("Creatures.Combusting.DisableFor.Zombie", false);
        
        creatureManager.addDefault("Creatures.Damage.DisableFireDamage", false);
        creatureManager.addDefault("Creatures.Damage.DisableContactDamage", false);
        creatureManager.addDefault("Creatures.Damage.DisableCustomDamage", false);
        creatureManager.addDefault("Creatures.Damage.DisableDrowningDamage", false);
        creatureManager.addDefault("Creatures.Damage.DisableEntityAttackDamage", false);
        creatureManager.addDefault("Creatures.Damage.DisableFallDamage", false);
        creatureManager.addDefault("Creatures.Damage.DisableLavaDamage", false);
        creatureManager.addDefault("Creatures.Damage.DisableLightningDamage", false);
        creatureManager.addDefault("Creatures.Damage.DisableMagicDamage", false);
        creatureManager.addDefault("Creatures.Damage.DisablePoisonDamage", false);
        creatureManager.addDefault("Creatures.Damage.DisableProjectileDamage", false);
        creatureManager.addDefault("Creatures.Damage.DisableStarvationDamage", false);
        creatureManager.addDefault("Creatures.Damage.DisableSuffocationDamage", false);
        creatureManager.addDefault("Creatures.Damage.DisableSuicideDamage", false);
        creatureManager.addDefault("Creatures.Damage.DisableVoidDamage", false);
        
        String defCreatures = "50,53,63";
        
        for(World world : Bukkit.getServer().getWorlds()) {
            String worldname = world.getName();
            
            String naturalBL = "MobSpawn." + worldname + ".Natural.Blacklist";
            String spawnerBL = "MobSpawn." + worldname + ".Spawner.Blacklist";
            String customBL = "MobSpawn." + worldname + ".Custom.Blacklist";
            String spawnereggBL = "MobSpawn." + worldname + ".SpawnerEgg.Blacklist";
            String chunkgenBL = "MobSpawn." + worldname + ".ChunkGen.Blacklist";
            String breedingBL = "MobSpawn." + worldname + ".Breeding.Blacklist";
            String state = "MobSpawn." + worldname + ".Enabled";
            
            creatureManager.addDefault(state, false);
            creatureManager.addDefault("MobSpawn." + worldname + ".Natural.Debug.ToConsole", true);
            creatureManager.addDefault(naturalBL, defCreatures);
            
            creatureManager.addDefault(state, false);
            creatureManager.addDefault("MobSpawn." + worldname + ".Spawner.Debug.ToConsole", true);
            creatureManager.addDefault(spawnerBL, defCreatures);
            
            creatureManager.addDefault(state, false);
            creatureManager.addDefault("MobSpawn." + worldname + ".Custom.Debug.ToConsole", true);
            creatureManager.addDefault(customBL, defCreatures);
            
            creatureManager.addDefault(state, false);
            creatureManager.addDefault("MobSpawn." + worldname + ".SpawnerEgg.Debug.ToConsole", true);
            creatureManager.addDefault(spawnereggBL, defCreatures);
            
            creatureManager.addDefault(state, false);
            creatureManager.addDefault("MobSpawn." + worldname + ".ChunkGen.Debug.ToConsole", true);
            creatureManager.addDefault(chunkgenBL, defCreatures);
            
            creatureManager.addDefault(state, false);
            creatureManager.addDefault("MobSpawn." + worldname + ".Breeding.Debug.ToConsole", true);
            creatureManager.addDefault(breedingBL, defCreatures);
        }
        
        this.getCreatureManager().options().copyDefaults(true);
        saveCreatureManager();
    }

    private void loadBlacklists() {
        blacklists = getBlacklists();
        blacklists.options().header(Data.setBlacklistsHeader());
        
        String defBlocks = "46,51,11,10,";
        String defInteract = "324,330,";
        String defPistonExtend = "56,120,";
        
        blacklists.addDefault("ConfigVersion", Double.valueOf(ConfigVersion));
        if(blacklists.getDouble("ConfigVersion") != Double.valueOf(ConfigVersion)) {
            log.warning("[iSafe] ConfigVersion was modified! Setting config version to right value ..");
            blacklists.set("ConfigVersion", Double.valueOf(ConfigVersion));
        }
        
        /*
         * Try making the all the "for World world" into one, 
         * will probably improve performance, and cleanup some shit here.
         */
        
        for(World world : Bukkit.getServer().getWorlds()) {
            String worldname = world.getName();
            
            String placeBl = "Place." + worldname + ".Blacklist";
            String state = "Place." + worldname + ".Enabled";
            
            blacklists.addDefault(state, false);
            blacklists.addDefault("Place." + worldname + ".Gamemode.PreventFor.Survival", true);
            blacklists.addDefault("Place." + worldname + ".Gamemode.PreventFor.Creative", true);
            blacklists.addDefault("Place." + worldname + ".Alert/log.ToConsole", true);
            blacklists.addDefault("Place." + worldname + ".Alert/log.ToPlayer", true);
            blacklists.addDefault("Place." + worldname + ".KickPlayer", false);
            blacklists.addDefault(placeBl, defBlocks);
        }
        
        
        for(World world : Bukkit.getServer().getWorlds()) {
            String worldname = world.getName();
            
            String breakBL = "Break." + worldname + ".Blacklist";
            String state = "Break." + worldname + ".Enabled";
            
            blacklists.addDefault(state, false);
            blacklists.addDefault("Break." + worldname + ".Gamemode.PreventFor.Survival", true);
            blacklists.addDefault("Break." + worldname + ".Gamemode.PreventFor.Creative", true);
            blacklists.addDefault("Break." + worldname + ".Alert/log.ToConsole", true);
            blacklists.addDefault("Break." + worldname + ".Alert/log.ToPlayer", true);
            blacklists.addDefault("Break." + worldname + ".KickPlayer", false);
            blacklists.addDefault(breakBL, defBlocks);
        }
        
        
        for(World world : Bukkit.getServer().getWorlds()) {
            String worldname = world.getName();
            
            String dropBL = "Drop." + worldname + ".Blacklist";
            String state = "Drop." + worldname + ".Enabled";
            
            blacklists.addDefault(state, false);
            blacklists.addDefault("Drop." + worldname + ".Gamemode.PreventFor.Survival", true);
            blacklists.addDefault("Drop." + worldname + ".Gamemode.PreventFor.Creative", true);
            blacklists.addDefault("Drop." + worldname + ".Alert/log.ToConsole", true);
            blacklists.addDefault("Drop." + worldname + ".Alert/log.ToPlayer", true);
            blacklists.addDefault("Drop." + worldname + ".KickPlayer", false);
            blacklists.addDefault(dropBL, defBlocks);
        }
        
        
        for(World world : Bukkit.getServer().getWorlds()) {
            String worldname = world.getName();
            
            String pickupBL = "Pickup." + worldname + ".Blacklist";
            String state = "Pickup." + worldname + ".Enabled";
            
            blacklists.addDefault(state, false);
            blacklists.addDefault("Pickup." + worldname + ".Gamemode.PreventFor.Survival", true);
            blacklists.addDefault("Pickup." + worldname + ".Gamemode.PreventFor.Creative", true);
            blacklists.addDefault("Pickup." + worldname + ".Alert/log.ToConsole", true);
            //blacklists.addDefault("Pickup." + worldname + ".Alert/log.ToPlayer", true);
            blacklists.addDefault("Pickup." + worldname + ".KickPlayer", false);
            blacklists.addDefault(pickupBL, defBlocks);
        }
        
        
        for(World world : Bukkit.getServer().getWorlds()) {
            String worldname = world.getName();
            
            String cmdBL = "Command." + worldname + ".Blacklist";
            String state = "Command." + worldname + ".Enabled";
            
            blacklists.addDefault(state, false);
            blacklists.addDefault("Command." + worldname + ".Alert/log.ToConsole", true);
            blacklists.addDefault("Command." + worldname + ".Alert/log.ToPlayer", true);
            blacklists.addDefault("Command." + worldname + ".KickPlayer", false);
            blacklists.addDefault(cmdBL, Arrays.asList(Data.CmdBlacklistList));
            Data.CmdBlacklist = blacklists.getStringList(cmdBL);
        }
        
        
        for(World world : Bukkit.getServer().getWorlds()) {
            String worldname = world.getName();
            
            String chatBL = "Chat." + worldname + ".Blacklist";
            String state = "Chat." + worldname + ".Enabled";
            
            blacklists.addDefault(state, false);
            blacklists.addDefault("Chat." + worldname + ".Alert/log.ToConsole", true);
            blacklists.addDefault("Chat." + worldname + ".Alert/log.ToPlayer", true);
            blacklists.addDefault("Chat." + worldname + ".KickPlayer", false);
            blacklists.addDefault(chatBL, Arrays.asList(Data.WordBlacklistList));
            Data.WordBlacklist = blacklists.getStringList(chatBL);
        }
        
        
        for(World world : Bukkit.getServer().getWorlds()) {
            String worldname = world.getName();
            
            String dispenseBL = "Dispense." + worldname + ".Blacklist";
            String state = "Dispense." + worldname + ".Enabled";
            
            blacklists.addDefault(state, false);
            blacklists.addDefault("Dispense." + worldname + ".Alert/log.ToConsole", true);
            blacklists.addDefault(dispenseBL, defBlocks);
        }
        
        
        for(World world : Bukkit.getServer().getWorlds()) {
            String worldname = world.getName();
            
            String interactBL = "Interact." + worldname + ".Blacklist";
            String state = "Interact." + worldname + ".Enabled";
            
            blacklists.addDefault(state, false);
            blacklists.addDefault("Interact." + worldname + ".Gamemode.PreventFor.Survival", true);
            blacklists.addDefault("Interact." + worldname + ".Gamemode.PreventFor.Creative", true);
            blacklists.addDefault("Interact." + worldname + ".Alert/log.ToConsole", true);
            blacklists.addDefault("Interact." + worldname + ".Alert/log.ToPlayer", true);
            blacklists.addDefault("Interact." + worldname + ".KickPlayer", false);
            blacklists.addDefault(interactBL, defInteract);
        }
        
        
        for(World world : Bukkit.getServer().getWorlds()) {
            String worldname = world.getName();
            
            String craftBL = "Crafting." + worldname + ".Blacklist";
            String state = "Crafting." + worldname + ".Enabled";
            
            blacklists.addDefault(state, false);
            blacklists.addDefault("Crafting." + worldname + ".Gamemode.PreventFor.Survival", true);
            blacklists.addDefault("Crafting." + worldname + ".Gamemode.PreventFor.Creative", true);
            blacklists.addDefault("Crafting." + worldname + ".Alert/log.ToConsole", true);
            blacklists.addDefault("Crafting." + worldname + ".Alert/log.ToPlayer", true);
            blacklists.addDefault("Crafting." + worldname + ".KickPlayer", false);
            blacklists.addDefault(craftBL, defBlocks);
        }
        
        for(World world : Bukkit.getServer().getWorlds()) {
            String worldname = world.getName();
            
            String pistonBL = "PistonExtend." + worldname + ".Blacklist";
            String state = "PistonExtend." + worldname + ".Enabled";
            String sticky = "PistonExtend." + worldname + ".CheckStickyPistons";
            
            blacklists.addDefault(state, false);
            blacklists.addDefault(sticky, true);
            blacklists.addDefault("PistonExtend." + worldname + ".Alert/log.ToConsole", true);
            blacklists.addDefault(pistonBL, defPistonExtend);
        }

        this.getBlacklists().options().copyDefaults(true);
        saveBlacklists();
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
            reloadISafeConfig();
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
            messagesFile = new File(getDataFolder(), "Messages.yml");
        }
        messages = YamlConfiguration.loadConfiguration(messagesFile);

        // Look for defaults in the jar
        InputStream defConfigStream = getResource("Messages.yml");
        if (defConfigStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            messages.setDefaults(defConfig);
        }
    }

    public FileConfiguration getMessages() {
        if (messages == null) {
            reloadMessages();
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

    public void reloadBlacklists() {
        if (blacklistsFile == null) {
            blacklistsFile = new File(getDataFolder(), "blacklists.yml");
        }
        blacklists = YamlConfiguration.loadConfiguration(blacklistsFile);

        // Look for defaults in the jar
        InputStream defConfigStream = getResource("blacklists.yml");
        if (defConfigStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            blacklists.setDefaults(defConfig);
        }
    }

    public FileConfiguration getBlacklists() {
        if (blacklists == null) {
            reloadBlacklists();
        }
        return blacklists;
    }

    public void saveBlacklists() {
        if (blacklists == null || blacklistsFile == null) {
            return;
        }
        try {
            blacklists.save(blacklistsFile);
        } catch (IOException ex) {
            Logger.getLogger(JavaPlugin.class.getName()).log(Level.SEVERE, "Error saving blacklist to " + blacklistsFile, ex);
        }
    }
    
    
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

    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        perms = rsp.getProvider();
        log.info("[iSafe] Hooked to permissions plugin: " + perms.getName());
        return perms != null;
    }

    private void checkMatch() {
        PluginDescriptionFile pdffile = this.getDescription();
        if (!(pdffile.getFullName().equals(fileversion))) {
            log.info("-----  iSafe vMatchConflict  -----");
            log.warning("[iSafe] The version in the pdffile is not the same as the file.");
            log.info("[iSafe] pdffile version: " + pdffile.getFullName());
            log.info("[iSafe] File version: " + fileversion);
            log.warning("[iSafe] Please deliver this infomation to " + pdffile.getAuthors() + " at BukkitDev.");
            log.info("-----  --------------------  -----");
        }
    }

    //Update checker (from MilkBowl's Vault, all credit to him)
    public double updateCheck(double currentVersion) throws Exception {
        String pluginUrlString = "http://dev.bukkit.org/server-mods/blockthattnt/files.rss";
        try {
            URL url = new URL(pluginUrlString);
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(url.openConnection().getInputStream());
            doc.getDocumentElement().normalize();
            NodeList nodes = doc.getElementsByTagName("item");
            Node firstNode = nodes.item(0);
            if (firstNode.getNodeType() == 1) {
                Element firstElement = (Element)firstNode;
                NodeList firstElementTagName = firstElement.getElementsByTagName("title");
                Element firstNameElement = (Element) firstElementTagName.item(0);
                NodeList firstNodes = firstNameElement.getChildNodes();
                return Double.valueOf(firstNodes.item(0).getNodeValue().replace("iSafe", "").replaceFirst(".", "").replace("v", "").trim());
            }
        }
        catch (Exception localException) {
        }
        return currentVersion;
    }
}
