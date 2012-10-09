package com.mrmag518.iSafe.Files;

import com.mrmag518.iSafe.Util.Data;
import com.mrmag518.iSafe.iSafe;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class BlacklistsF {
    private static FileConfiguration blacklists = null;
    private static File blacklistsFile = null;
    
    private static final Logger log = Logger.getLogger("Minecraft");
    
    // Finally found a method that doesn't return in a NPE!
    private static final iSafe plugin = (iSafe) Bukkit.getPluginManager().getPlugin("iSafe");
    private static File datafolder = plugin.getDataFolder();
    
    public static void loadBlacklists() {
        blacklists = getBlacklists();
        blacklists.options().header(Data.setBlacklistsHeader());

        String defBlocks = "46,51,11,10,";
        String defInteract = "324,330,";
        String defPistonExtend = "56,120,";

        blacklists.addDefault("ConfigVersion", Double.valueOf(iSafe.ConfigVersion));
        if (blacklists.getDouble("ConfigVersion") != Double.valueOf(iSafe.ConfigVersion)) {
            log.warning("[iSafe] ConfigVersion was modified! Setting config version to right value ..");
            blacklists.set("ConfigVersion", Double.valueOf(iSafe.ConfigVersion));
        }

        /*
         * Try making the all the "for World world" into one, 
         * will probably improve performance, and cleanup some shit here.
         */

        for (World world : Bukkit.getServer().getWorlds()) {
            String worldname = world.getName();

            String placeBl = "Place." + worldname + ".Blacklist";
            String state = "Place." + worldname + ".Enabled";

            blacklists.addDefault(state, false);
            blacklists.addDefault("Place." + worldname + ".Gamemode.PreventFor.Survival", true);
            blacklists.addDefault("Place." + worldname + ".Gamemode.PreventFor.Creative", true);
            blacklists.addDefault("Place." + worldname + ".Gamemode.PreventFor.Adventure", true);
            blacklists.addDefault("Place." + worldname + ".Alert/log.ToConsole", true);
            blacklists.addDefault("Place." + worldname + ".Alert/log.ToPlayer", true);
            blacklists.addDefault("Place." + worldname + ".KickPlayer", false);
            blacklists.addDefault(placeBl, defBlocks);
        }


        for (World world : Bukkit.getServer().getWorlds()) {
            String worldname = world.getName();

            String breakBL = "Break." + worldname + ".Blacklist";
            String state = "Break." + worldname + ".Enabled";

            blacklists.addDefault(state, false);
            blacklists.addDefault("Break." + worldname + ".Gamemode.PreventFor.Survival", true);
            blacklists.addDefault("Break." + worldname + ".Gamemode.PreventFor.Creative", true);
            blacklists.addDefault("Break." + worldname + ".Gamemode.PreventFor.Adventure", true);
            blacklists.addDefault("Break." + worldname + ".Alert/log.ToConsole", true);
            blacklists.addDefault("Break." + worldname + ".Alert/log.ToPlayer", true);
            blacklists.addDefault("Break." + worldname + ".KickPlayer", false);
            blacklists.addDefault(breakBL, defBlocks);
        }


        for (World world : Bukkit.getServer().getWorlds()) {
            String worldname = world.getName();

            String dropBL = "Drop." + worldname + ".Blacklist";
            String state = "Drop." + worldname + ".Enabled";

            blacklists.addDefault(state, false);
            blacklists.addDefault("Drop." + worldname + ".Gamemode.PreventFor.Survival", true);
            blacklists.addDefault("Drop." + worldname + ".Gamemode.PreventFor.Creative", true);
            blacklists.addDefault("Drop." + worldname + ".Gamemode.PreventFor.Adventure", true);
            blacklists.addDefault("Drop." + worldname + ".Alert/log.ToConsole", true);
            blacklists.addDefault("Drop." + worldname + ".Alert/log.ToPlayer", true);
            blacklists.addDefault("Drop." + worldname + ".KickPlayer", false);
            blacklists.addDefault(dropBL, defBlocks);
        }


        for (World world : Bukkit.getServer().getWorlds()) {
            String worldname = world.getName();

            String pickupBL = "Pickup." + worldname + ".Blacklist";
            String state = "Pickup." + worldname + ".Enabled";

            blacklists.addDefault(state, false);
            blacklists.addDefault("Pickup." + worldname + ".Gamemode.PreventFor.Survival", true);
            blacklists.addDefault("Pickup." + worldname + ".Gamemode.PreventFor.Creative", true);
            blacklists.addDefault("Pickup." + worldname + ".Gamemode.PreventFor.Adventure", true);
            blacklists.addDefault("Pickup." + worldname + ".Alert/log.ToConsole", true);
            //blacklists.addDefault("Pickup." + worldname + ".Alert/log.ToPlayer", true);
            blacklists.addDefault("Pickup." + worldname + ".KickPlayer", false);
            blacklists.addDefault(pickupBL, defBlocks);
        }


        for (World world : Bukkit.getServer().getWorlds()) {
            String worldname = world.getName();

            String cmdBL = "Command." + worldname + ".Blacklist";
            String state = "Command." + worldname + ".Enabled";

            blacklists.addDefault(state, false);
            blacklists.addDefault("Command." + worldname + ".Gamemode.PreventFor.Survival", true);
            blacklists.addDefault("Command." + worldname + ".Gamemode.PreventFor.Creative", true);
            blacklists.addDefault("Command." + worldname + ".Gamemode.PreventFor.Adventure", true);
            blacklists.addDefault("Command." + worldname + ".Alert/log.ToConsole", true);
            blacklists.addDefault("Command." + worldname + ".Alert/log.ToPlayer", true);
            blacklists.addDefault("Command." + worldname + ".KickPlayer", false);
            blacklists.addDefault(cmdBL, Arrays.asList(Data.CmdBlacklistList));
            Data.CmdBlacklist = blacklists.getStringList(cmdBL);
        }


        for (World world : Bukkit.getServer().getWorlds()) {
            String worldname = world.getName();

            String chatBL = "Chat." + worldname + ".Blacklist";
            String chatWL = "Chat." + worldname + ".Whitelist";
            String state = "Chat." + worldname + ".Enabled";

            blacklists.addDefault(state, false);
            blacklists.addDefault("Chat." + worldname + ".Alert/log.ToConsole", true);
            blacklists.addDefault("Chat." + worldname + ".Alert/log.ToPlayer", true);
            blacklists.addDefault("Chat." + worldname + ".KickPlayer", false);
            blacklists.addDefault("Chat." + worldname + ".UseDetailedSearchMode", false);
            blacklists.addDefault(chatBL, Arrays.asList(Data.WordBlacklistList));
            Data.WordBlacklist = blacklists.getStringList(chatBL);
            blacklists.addDefault(chatWL, Arrays.asList(Data.WordWhitelistList));
            Data.WordWhitelist = blacklists.getStringList(chatWL);
        }


        for (World world : Bukkit.getServer().getWorlds()) {
            String worldname = world.getName();

            String dispenseBL = "Dispense." + worldname + ".Blacklist";
            String state = "Dispense." + worldname + ".Enabled";

            blacklists.addDefault(state, false);
            blacklists.addDefault("Dispense." + worldname + ".Alert/log.ToConsole", true);
            blacklists.addDefault(dispenseBL, defBlocks);
        }


        for (World world : Bukkit.getServer().getWorlds()) {
            String worldname = world.getName();

            String interactBL = "Interact." + worldname + ".Blacklist";
            String state = "Interact." + worldname + ".Enabled";

            blacklists.addDefault(state, false);
            blacklists.addDefault("Interact." + worldname + ".Gamemode.PreventFor.Survival", true);
            blacklists.addDefault("Interact." + worldname + ".Gamemode.PreventFor.Creative", true);
            blacklists.addDefault("Interact." + worldname + ".Gamemode.PreventFor.Adventure", true);
            blacklists.addDefault("Interact." + worldname + ".Alert/log.ToConsole", true);
            blacklists.addDefault("Interact." + worldname + ".Alert/log.ToPlayer", true);
            blacklists.addDefault("Interact." + worldname + ".KickPlayer", false);
            blacklists.addDefault(interactBL, defInteract);
        }


        for (World world : Bukkit.getServer().getWorlds()) {
            String worldname = world.getName();

            String craftBL = "Crafting." + worldname + ".Blacklist";
            String state = "Crafting." + worldname + ".Enabled";

            blacklists.addDefault(state, false);
            blacklists.addDefault("Crafting." + worldname + ".Gamemode.PreventFor.Survival", true);
            blacklists.addDefault("Crafting." + worldname + ".Gamemode.PreventFor.Creative", true);
            blacklists.addDefault("Crafting." + worldname + ".Gamemode.PreventFor.Adventure", true);
            blacklists.addDefault("Crafting." + worldname + ".Alert/log.ToConsole", true);
            blacklists.addDefault("Crafting." + worldname + ".Alert/log.ToPlayer", true);
            blacklists.addDefault("Crafting." + worldname + ".KickPlayer", false);
            blacklists.addDefault(craftBL, defBlocks);
        }

        for (World world : Bukkit.getServer().getWorlds()) {
            String worldname = world.getName();

            String pistonBL = "PistonExtend." + worldname + ".Blacklist";
            String state = "PistonExtend." + worldname + ".Enabled";
            String sticky = "PistonExtend." + worldname + ".CheckStickyPistons";

            blacklists.addDefault(state, false);
            blacklists.addDefault(sticky, true);
            blacklists.addDefault("PistonExtend." + worldname + ".Alert/log.ToConsole", true);
            blacklists.addDefault(pistonBL, defPistonExtend);
        }

        getBlacklists().options().copyDefaults(true);
        saveBlacklists();
    }

    public static void reloadBlacklists() {
        if (blacklistsFile == null) {
            blacklistsFile = new File(datafolder, "blacklists.yml");
        }
        blacklists = YamlConfiguration.loadConfiguration(blacklistsFile);

        // Look for defaults in the jar
        InputStream defConfigStream = plugin.getResource("blacklists.yml");
        if (defConfigStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            blacklists.setDefaults(defConfig);
        }
    }

    public static FileConfiguration getBlacklists() {
        if (blacklists == null) {
            reloadBlacklists();
        }
        return blacklists;
    }

    public static void saveBlacklists() {
        if (blacklists == null || blacklistsFile == null) {
            return;
        }
        try {
            blacklists.save(blacklistsFile);
        } catch (IOException ex) {
            Logger.getLogger(JavaPlugin.class.getName()).log(Level.SEVERE, "Error saving blacklist to " + blacklistsFile, ex);
        }
    }
}
