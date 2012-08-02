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

import org.bukkit.Server;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
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
 * iSafe.use.chat || default: op
 * iSafe.use.minecarts || default: op
 * iSafe.use.boats || default: op
 * iSafe.bypass.blacklist.interact || default: op
 * iSafe.bypass.blacklist.pickup || default: op
 * iSafe.bypass.blacklist.break || default: op
 * iSafe.bypass.blacklist.place || default: op
 * iSafe.bypass.blacklist.drop || default: op
 * 
 */

/*
 * Add: Log file.
 * Add: 'Homemade' log method, which will log into the log file. (optional)
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
    public InteractBlacklist interactBlacklist = null;
    
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
    public boolean checkingUpdatePerms = false;
    public boolean cancelDamagePerms = false;

    @Override
    public void onLoad() {
        fileManagement();
    }

    @Override
    public void onDisable() {
        PluginDescriptionFile pdffile = this.getDescription();
        if (verboseLogging() == true) {
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
        if (debugMode() == true) {
            log.info(DEBUG_PREFIX + "Debug mode is enabled!");
        }

        registerClasses();
        PluginDescriptionFile pdffile = this.getDescription();
        if (getISafeConfig().getBoolean("CheckForUpdates", true)) {
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

        if (iSafeConfig.getBoolean("UseVaultForPermissions", true)) {
            if (getServer().getPluginManager().getPlugin("Vault") != null) {
                setupPermissions();
            } else {
                log.severe("[iSafe] Vault.jar was NOT found in your plugins folder!");
                log.severe("[iSafe] You HAVE to have Vault.jar in the plugins folder if you use Vault for permissions!");
                log.warning("[iSafe] Settings UseVaultForPermissions in your iSafeConfig.yml to false ..");
                getISafeConfig().set("UseVaultForPermissions", false);
                saveISafeConfig();
                reloadISafeConfig();
            }
            if (verboseLogging() == true) {
                log.info("[iSafe] Using Vault for permissions!");
            }
        }

        checkingUpdatePerms = false;

        try {
            Metrics metrics = new Metrics(this);
            metrics.start();
        } catch (IOException e) {
            // Failed to submit the stats :-(
        }

        if (verboseLogging() == true) {
            log.info("[" + pdffile.getName() + " :: " + version + "] " + " Enabled succesfully.");
        } else {
            if (debugMode() == true) {
                log.info(DEBUG_PREFIX + "Verbose logging is off.");
            }
        }
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
            if (debugMode() == true) {
                log.info(DEBUG_PREFIX + "CreateUserFiles in the iSafeConfig.yml was disable, therefor not register the UserFileCreator class.");
            }
        }

        if (getISafeConfig().getBoolean("CheckForUpdates", true)) {
            sendUpdate = new SendUpdate(this);
        } else {
            sendUpdate = null;
            if (debugMode() == true) {
                log.info(DEBUG_PREFIX + "CheckForUpdates in the iSafeConfig.yml was disabled, therefor not registering the sendUpdate class.");
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
        interactBlacklist = new InteractBlacklist(this);

        if (debugMode() == true) {
            log.info(DEBUG_PREFIX + "Registered classes.");
        }
    }

    private void fileManagement() {
        if (!(getDataFolder().exists())) {
            getDataFolder().mkdirs();
        }

        File usersFolder = new File(getDataFolder() + File.separator + "Users");
        if (!(usersFolder.exists())) {
            usersFolder.mkdir();
        }

        File exaFile = new File(usersFolder + File.separator + "_example.yml");
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

        if (verboseLogging() == true) {
            log.info("[iSafe] Loaded all files.");
        }
    }

    /**
     * @param p
     * @param permission
     * @return 
     */
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
                noCmdPermission(p);
                return false;
            }
        } else {
            if (p.hasPermission(permission)) {
                return true;
            } else {
                //Send no perm message in the blacklist instead.
                return false;
            }
        }
    }

    public boolean hasPermission(Player p, String permission) {
        if (iSafeConfig.getBoolean("UseVaultForPermissions", true)) {
            if (perms.has(p, permission)) {
                return true;
            } else {
                if (checkingUpdatePerms == true) {
                    p.sendMessage("");
                } else {
                    noPermission(p);
                }
                return false;
            }
        } else {
            if (p.hasPermission(permission)) {
                return true;
            } else {
                if (checkingUpdatePerms == true) {
                    p.sendMessage("");
                } else {
                    noPermission(p);
                }
                return false;
            }
        }
    }

    public String scanVariables(String configString, String playerName, String cmd, String blockName, String item) {
        String result = configString;
        
        if(playerName != null) {
            result = result.replaceAll("%playername%", playerName);
        }
        if(cmd != null) {
            result = result.replaceAll("%cmd%", cmd);
        }
        if(blockName != null) {
            result = result.replaceAll("%block%", blockName);
        }
        if(item != null) {
            result = result.replaceAll("%item%", item);
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
        s.broadcastMessage(scanVariables(kickMsg, p.getName(), null, null, null));
    }

    public String sameNickPlaying(Player p) {
        String kickMsg = getMessages().getString("SameNickAlreadyPlaying");
        return scanVariables(kickMsg, p.getName(), null, null, null);
    }

    public String denyNonOpsJoin() {
        String kickMsg = getMessages().getString("OnlyOpsCanJoin");
        return scanVariables(kickMsg, null, null, null, null);
    }

    public String commandLogger(Player p, PlayerCommandPreprocessEvent event) {
        String logged = getMessages().getString("CommandLogger");
        return scanVariables(logged, p.getName(), event.getMessage(), null, null);
    }
    
    public String blacklistInteractKickMsg(Block b) {
        String kickMsg = getMessages().getString("Blacklists.Interact.KickMessage");
        return scanVariables(kickMsg, null, null, b.getType().name().toLowerCase(), null);
    }
    
    public String blacklistPlaceKickMsg(Block b) {
        String kickMsg = getMessages().getString("Blacklists.Place.KickMessage");
        return scanVariables(kickMsg, null, null, b.getType().name().toLowerCase(), null);
    }
    
    public String blacklistBreakKickMsg(Block b) {
        String kickMsg = getMessages().getString("Blacklists.Break.KickMessage");
        return scanVariables(kickMsg, null, null, b.getType().name().toLowerCase(), null);
    }
    
    public String blacklistCensorKickMsg(String word) {
        String kickMsg = getMessages().getString("Blacklists.Censor.KickMessage");
        return scanVariables(kickMsg, null, null, null, null);
    }
    
    public String blacklistDropKickMsg(Item i) {
        String kickMsg = getMessages().getString("Blacklists.Drop.KickMessage");
        return scanVariables(kickMsg, null, null, null, i.getItemStack().getType().name().toLowerCase());
    }
    
    public String blacklistPickupKickMsg(Item i) {
        String kickMsg = getMessages().getString("Blacklists.Pickup.KickMessage");
        return scanVariables(kickMsg, null, null, null, i.getItemStack().getType().name().toLowerCase());
    }
    
    public void loadConfig() {
        config = getConfig();
        config.options().header(Data.setConfigHeader());

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
        config.addDefault("Miscellaneous.DisableLeavesDecay", false);
        config.addDefault("Miscellaneous.ForceBlocksToBeBuildable", false);
        config.addDefault("Miscellaneous.PreventExpBottleThrow", false);
        config.addDefault("Miscellaneous.ForcePermissionsToUseBed", false);
        config.addDefault("Miscellaneous.ForcePermissionsToFish", false);
        config.addDefault("Miscellaneous.OnlyLetOPsJoin", false);
        config.addDefault("Miscellaneous.DisableHunger", false);

        config.addDefault("AntiCheat/Sucurity.ForceLightLevel(Fullbright)", false);
        config.addDefault("AntiCheat/Sucurity.KickJoinerIfSameNickIsOnline", false);

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
        config.addDefault("Buckets.Lava.CheckedWorlds", Arrays.asList(Data.lbEnabledWorldList));
        Data.lbEnabledWorlds = config.getStringList("Buckets.Lava.CheckedWorlds");
        config.addDefault("Buckets.Water.Prevent", false);
        config.addDefault("Buckets.Water.CheckedWorlds", Arrays.asList(Data.wbEnabledWorldList));
        Data.wbEnabledWorlds = config.getStringList("Buckets.Water.CheckedWorlds");

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

    public void loadMessages() {
        messages = getMessages();
        messages.options().header(Data.setMessageHeader());

        messages.addDefault("Permissions.DefaultNoPermission", "&cNo permission.");
        messages.addDefault("Permissions.NoCmdPermission", "&cNo permission to do this command.");
        messages.addDefault("KickMessage", "&6%playername% was kicked from the game.");
        messages.addDefault("SameNickAlreadyPlaying", "&cThe username &f+playername &cis already online!");
        messages.addDefault("OnlyOpsCanJoin", "&cOnly OPs can join this server!");
        messages.addDefault("CommandLogger", "+playername did or tried to do the command +command");
        messages.addDefault("Blacklists.Interact.KickMessage", "&cKicked for attempting to interact with &f%block%");
        messages.addDefault("Blacklists.Place.KickMessage", "&cKicked for attempting to place &f%block%");
        messages.addDefault("Blacklists.Break.KickMessage", "&cKicked for attempting to break &f%block%");
        messages.addDefault("Blacklists.Censor.KickMessage", "&cKicked for attempting to say a censored word");
        messages.addDefault("Blacklists.Drop.KickMessage", "&cKicked for attempting to drop &f%item%");
        messages.addDefault("Blacklists.Pickup.KickMessage", "&cKicked for attempting to pickup &f%item%");
        
        //messages.addDefault("Blacklists.Interact.KickMessage", "&cKicked for attemping to place &f+block");

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
        iSafeConfig.addDefault("CreateUserFiles", true);

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
        
        
        blacklist.addDefault("Place.TotallyDisableBlockPlace", false);
        blacklist.addDefault("Place.Kick-Player", false);
        blacklist.addDefault("Place.Alert/log.To-console", true);
        blacklist.addDefault("Place.Alert/log.To-player", true);
        blacklist.addDefault("Place.Alert/log.To-server-chat", false);
        blacklist.addDefault("Place.Gamemode.PreventFor.Survival", true);
        blacklist.addDefault("Place.Gamemode.PreventFor.Creative", true);
        blacklist.addDefault("Place.EnabledWorlds", Arrays.asList(Data.worldslist));
        Data.worlds = blacklist.getStringList("Place.Worlds");
        blacklist.addDefault("Place.Blacklist", Arrays.asList(Data.placedblockslist));
        Data.placedblocks = blacklist.getStringList("Place.Blacklist");
        
        
        blacklist.addDefault("Break.TotallyDisableBlockBreak", false);
        blacklist.addDefault("Break.Kick-Player", false);
        blacklist.addDefault("Break.Alert/log.To-console", true);
        blacklist.addDefault("Break.Alert/log.To-player", true);
        blacklist.addDefault("Break.Alert/log.To-server-chat", false);
        blacklist.addDefault("Break.Gamemode.PreventFor.Survival", true);
        blacklist.addDefault("Break.Gamemode.PreventFor.Creative", true);
        blacklist.addDefault("Break.EnabledWorlds", Arrays.asList(Data.worldslist));
        Data.worlds = blacklist.getStringList("Break.Worlds");
        blacklist.addDefault("Break.Blacklist", Arrays.asList(Data.brokenblockslist));
        Data.brokenblocks = blacklist.getStringList("Break.Blacklist");
        
        
        blacklist.addDefault("Drop.TotallyDisableBlockDrop", false);
        blacklist.addDefault("Drop.Kick-Player", false);
        blacklist.addDefault("Drop.Alert/log.To-console", true);
        blacklist.addDefault("Drop.Alert/log.To-player", true);
        blacklist.addDefault("Drop.Alert/log.To-server-chat", false);
        blacklist.addDefault("Drop.Gamemode.PreventFor.Survival", true);
        blacklist.addDefault("Drop.Gamemode.PreventFor.Creative", true);
        blacklist.addDefault("Drop.EnabledWorlds", Arrays.asList(Data.worldslist));
        Data.worlds = blacklist.getStringList("Drop.Worlds");
        blacklist.addDefault("Drop.Blacklist", Arrays.asList(Data.dropedblockslist));
        Data.dropedblocks = blacklist.getStringList("Drop.Blacklist");
        
        
        blacklist.addDefault("Pickup.TotallyDisableBlockPickup", false);
        blacklist.addDefault("Pickup.Kick-Player", false);
        blacklist.addDefault("Pickup.Alert/log.To-console", true);
        blacklist.addDefault("Pickup.Alert/log.To-player", true);
        blacklist.addDefault("Pickup.Alert/log.To-server-chat", false);
        blacklist.addDefault("Pickup.Gamemode.PreventFor.Survival", true);
        blacklist.addDefault("Pickup.Gamemode.PreventFor.Creative", true);
        blacklist.addDefault("Pickup.EnabledWorlds", Arrays.asList(Data.Pickupworldslist));
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
        
        
        blacklist.addDefault("Interact.KickPlayer", false);
        blacklist.addDefault("Interact.Worlds", Arrays.asList(Data.interactBlacklistedWorldList));
        Data.interactBlacklistedWorlds = blacklist.getStringList("Interact.Worlds");
        blacklist.addDefault("Interact.Blacklist", Arrays.asList(Data.interactBlacklistedBlocksList));
        Data.interactBlacklistedBlocks = blacklist.getStringList("Interact.Blacklist");

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
        if (!(pdffile.getFullName().equals(fileversion))) {
            log.info("-----  iSafe vMatchConflict  -----");
            log.warning("[iSafe] The version in the pdffile is not the same as the file.");
            log.info("[iSafe] pdffile version: " + pdffile.getFullName());
            log.info("[iSafe] File version: " + fileversion);
            log.warning("[iSafe] Please deliver this infomation to " + pdffile.getAuthors() + " at BukkitDev.");
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
