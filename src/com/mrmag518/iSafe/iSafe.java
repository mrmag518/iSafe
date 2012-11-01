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

import com.mrmag518.iSafe.Util.Data;
import com.mrmag518.iSafe.Events.BlockEvents.*;
import com.mrmag518.iSafe.Events.EntityEvents.*;
import com.mrmag518.iSafe.Events.WorldEvents.*;
import com.mrmag518.iSafe.Blacklists.Blacklists;
import com.mrmag518.iSafe.Commands.Commands;
import com.mrmag518.iSafe.Files.*;
import com.mrmag518.iSafe.Util.Log;
import com.mrmag518.iSafe.Util.iSafeExtension;

import java.io.IOException;
import java.util.Arrays;
import java.io.File;
import java.net.URL;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilderFactory;
import net.milkbowl.vault.economy.Economy;

import net.milkbowl.vault.permission.Permission;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class iSafe extends JavaPlugin {

    /**
     * Note to self:
     * Start working on gamemode 'protection'.
     * More bug hunting...
     * Commands to toggle the enable state of specific worlds in the blacklist.
     * Add adverture mode to anything related to gamemode.
     * Add group thing to blacklists.
     * Add support for ids like, 120:1337
     * PvP handling.
     * mORE utility classes.
     * Eco support for blacklists. (New category: Penalties, put kick option here too)
     * 
     */
    
    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    private String fileversion = "iSafe v3.3";
    public static final Double ConfigVersion = 3.3;
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
    
    public static Permission perms = null;
    public static Economy economy = null;
    
    public FileConfiguration config;
    
    public boolean checkingUpdatePerms = false;
    public boolean cancelDamagePerms = false;
    public boolean checkingSpamPerms = false;
    public boolean checkingFullbrightPerms = false;
    private boolean isStartup = false;
    public HashMap<String, Integer> spamDB = new HashMap<>();

    @Override
    public void onDisable() {
        PluginDescriptionFile pdffile = getDescription();
        Log.verbose("v" + pdffile.getVersion() + " disabled.");
    }

    @Override
    public void onEnable() {
        isStartup = true;
        Log.debug("iSafe startup initialized.");

        currentVersion = Double.valueOf(getDescription().getVersion());

        fileLoadManagement();
        Log.debug("Debug mode is enabled!");

        registerClasses();

        PluginDescriptionFile pdffile = getDescription();
        if (iSafeConfig.getISafeConfig().getBoolean("CheckForUpdates") == true) {
            //Update checker - From MilkBowl.
            getServer().getScheduler().scheduleAsyncRepeatingTask(this, new Runnable() {

                @Override
                public void run() {
                    try {
                        Log.debug("Running update check ..");
                        newVersion = updateCheck(currentVersion);

                        if (newVersion > currentVersion) {
                            Log.debug("A new version was found!");
                            Log.info(" ");
                            Log.info("#######  iSafe UpdateChecker  #######");
                            Log.info("A new update for iSafe was found! " + newVersion);
                            Log.info("You are currently running version: " + currentVersion);
                            Log.info("You can find this new version at BukkitDev.");
                            Log.info("http://dev.bukkit.org/server-mods/blockthattnt/");
                            Log.info("#####################################");
                            Log.info(" ");
                        } else {
                            Log.debug("No new version was found.");
                        }
                    } catch (Exception ignored) {
                        //Ignored
                    }
                }
            }, 0, 36000);
        } else {
            Log.debug("CheckForUpdates is false in the iSafeConfig.yml, will not check for iSafe updates!");
        }

        getCommand("iSafe").setExecutor(new Commands(this));

        checkMatch();
        
        if(iSafeConfig.getISafeConfig().getBoolean("TrackUsageStatistics") == true) {
            try {
                Log.debug("Starting plugin metrics ..");
                MetricsLite metrics = new MetricsLite(this);
                metrics.start();
            } catch (IOException e) {
                // Failed to submit the stats :-(
            }
        }
        
        //iSafeExtension.hook();
        
        Log.verbose("v" + pdffile.getVersion() + " enabled.");
        
        Log.debug("iSafe startup finished.");
        isStartup = false;
    }

    protected final void startSpamTask() {
        Log.debug("Starting spam task ..");
        Log.debug("Running every 20th game tick. (1sec)");
        Log.debug("Note: If the server has lag, the time for each spam loop may go out of sync.");
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                    String name = p.getName();
                    if (spamDB.containsKey(name)) {
                        if (spamDB.get(name).intValue() > 0) {
                            spamDB.put(name, spamDB.get(name) + - 1);
                        }
                    }
                }
            }
        }, 0, 20);
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

        if (iSafeConfig.getISafeConfig().getBoolean("CreateUserFiles") == true) {
            UFC = new UserFileCreator(this);
        } else {
            UFC = null;
            Log.debug("CreateUserFiles in the iSafeConfig.yml was disabled, therefor not register the UserFileCreator class.");
        }

        if (iSafeConfig.getISafeConfig().getBoolean("CheckForUpdates") == true) {
            sendUpdate = new SendUpdate(this);
        } else {
            sendUpdate = null;
            Log.debug("CheckForUpdates in the iSafeConfig.yml was disabled, therefor not registering the sendUpdate class.");
        }

        blacklistClass = new Blacklists(this);

        Log.debug("Registered event classes.");
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
                exampFile.set("Level", Integer.parseInt("50"));
                exampFile.save(exaFile);
            } catch (NumberFormatException | IOException e) {
                Log.info("[iSafe] Error creating example user file. (_example.yml)");
                e.printStackTrace();
            }
        }


        // Always load iSafeConfig first.
        iSafeConfig.reloadISafeConfig();
        iSafeConfig.loadISafeConfig();
        iSafeConfig.reloadISafeConfig();

        Messages.reloadMessages();
        Messages.loadMessages();
        Messages.reloadMessages();

        reloadConfig();
        loadConfig();
        reloadConfig();

        if (isStartup == true) {
            getServer().getScheduler().scheduleAsyncDelayedTask(this, new Runnable() {

                @Override
                public void run() {
                    CreatureManager.reloadCreatureManager();
                    CreatureManager.loadCreatureManager();
                    CreatureManager.reloadCreatureManager();

                    BlacklistsF.reloadBlacklists();
                    BlacklistsF.loadBlacklists();
                    BlacklistsF.reloadBlacklists();

                    setupVault();
                }
            }, 40);
        } else {
            CreatureManager.reloadCreatureManager();
            CreatureManager.loadCreatureManager();
            CreatureManager.reloadCreatureManager();

            BlacklistsF.reloadBlacklists();
            BlacklistsF.loadBlacklists();
            BlacklistsF.reloadBlacklists();

            setupVault();
        }

        boolean beastMode = getConfig().getBoolean("AntiCheat/Security.Spam.UseBeastMode");
        boolean normalMode = getConfig().getBoolean("AntiCheat/Security.Spam.UseNormalMode");

        if (beastMode == true) {
            if (normalMode == true) {
                getConfig().set("AntiCheat/Security.Spam.UseNormalMode", false);
                saveConfig();
            }
            startSpamTask();
        }
    }

    private void setupVault() {
        if (iSafeConfig.getISafeConfig().getBoolean("UseVaultForPermissions", true)) {
            if (getServer().getPluginManager().getPlugin("Vault") != null) {
                Log.verbose("Using Vault for permissions!");
                setupPermissions();
            } else {
                Log.severe(" Vault.jar was NOT found in your plugins folder!");
                Log.severe("You need to have Vault.jar in the plugins folder if you're going to use Vault for permissions!");
                Log.severe("Settings UseVaultForPermissions in your iSafeConfig.yml to false ..");
                iSafeConfig.getISafeConfig().set("UseVaultForPermissions", false);
                iSafeConfig.saveISafeConfig();
            }
        }
        
        if (getServer().getPluginManager().getPlugin("Vault") != null) {
            setupEconomy();
            Log.verbose("Hooked to economy plugin '" + economy.getName() + "'.");
        }
    }

    public boolean hasPermission(CommandSender p, String permission) {
        if (iSafeConfig.getISafeConfig().getBoolean("UseVaultForPermissions", true)) {
            if (perms.has(p, permission)) {
                return true;
            } else {
                Messages.noCmdPermission(p);
                return false;
            }
        } else {
            if (p.hasPermission(permission)) {
                return true;
            } else {
                Messages.noCmdPermission(p);
                return false;
            }
        }
    }

    public boolean hasBlacklistPermission(Player p, String permission) {
        if (iSafeConfig.getISafeConfig().getBoolean("UseVaultForPermissions", true)) {
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
        if (iSafeConfig.getISafeConfig().getBoolean("UseVaultForPermissions", true)) {
            if (perms.has(p, permission)) {
                return true;
            } else {
                if(shallOutputNoPerm() == false) {
                    // ignore.
                } else {
                    Messages.noPermission(p);
                }
                return false;
            }
        } else {
            if (p.hasPermission(permission)) {
                return true;
            } else {
                if(shallOutputNoPerm() == false) {
                    // ignore.
                } else {
                    Messages.noPermission(p);
                }
                return false;
            }
        }
    }
    
    private boolean shallOutputNoPerm() {
        if(checkingUpdatePerms == true
            || checkingSpamPerms == true
            || checkingFullbrightPerms == true) 
        {
            checkingUpdatePerms = false;
            checkingSpamPerms = false;
            checkingFullbrightPerms = false;
            return false;
        } else {
            return true;
        }
    }

    private void loadConfig() {
        config = getConfig();
        config.options().header(Data.setConfigHeader());

        config.addDefault("ConfigVersion", Double.valueOf(ConfigVersion));
        if (config.getDouble("ConfigVersion") != Double.valueOf(ConfigVersion)) {
            // If there is anything to modify in the 'new' version, fix that here.
            Log.warning("[iSafe] ConfigVersion was modified! Setting config version to right value ..");
            config.set("ConfigVersion", Double.valueOf(ConfigVersion));

            if (config.get("AntiCheat/Sucurity.ForceLightLevel(Fullbright)") != null) {
                config.set("AntiCheat/Sucurity.ForceLightLevel(Fullbright)", null);
            }

            if (config.get("AntiCheat/Sucurity.SimpleAntiSpam") != null) {
                config.set("AntiCheat/Sucurity.SimpleAntiSpam", null);
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
        config.addDefault("AntiCheat/Security.KickJoinerIfSameNickIsOnline", false);
        config.addDefault("AntiCheat/Security.Spam.EnableSpamDetector", false);
        config.addDefault("AntiCheat/Security.Spam.MaxLinesPerSecond", 2);
        config.addDefault("AntiCheat/Security.Spam.EnableBypassPermissions", true);
        config.addDefault("AntiCheat/Security.Spam.UseNormalMode", true);
        config.addDefault("AntiCheat/Security.Spam.UseBeastMode", false);

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

        getConfig().options().copyDefaults(true);
        saveConfig();
    }

    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        perms = rsp.getProvider();
        Log.info("Hooked to permissions plugin: " + perms.getName());
        return perms != null;
    }
    
    private boolean setupEconomy() {
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }
        return (economy != null);
    }

    private void checkMatch() {
        PluginDescriptionFile pdffile = getDescription();
        if (!(pdffile.getFullName().equals(fileversion))) {
            Log.warning("-----  iSafe vMatchConflict  -----");
            Log.warning("The version in the pdffile is not the same as the file.");
            Log.warning("pdffile version: " + pdffile.getFullName());
            Log.warning("File version: " + fileversion);
            Log.warning("Please deliver this infomation to " + pdffile.getAuthors() + " at BukkitDev.");
            Log.warning("-----  --------------------  -----");
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
                Element firstElement = (Element) firstNode;
                NodeList firstElementTagName = firstElement.getElementsByTagName("title");
                Element firstNameElement = (Element) firstElementTagName.item(0);
                NodeList firstNodes = firstNameElement.getChildNodes();
                return Double.valueOf(firstNodes.item(0).getNodeValue().replace("iSafe", "").replaceFirst(".", "").replace("v", "").trim());
            }
        } catch (Exception localException) {
        }
        return currentVersion;
    }
}
