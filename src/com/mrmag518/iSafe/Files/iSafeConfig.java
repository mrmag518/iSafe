package com.mrmag518.iSafe.Files;

import com.mrmag518.iSafe.Util.Data;
import com.mrmag518.iSafe.iSafe;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class iSafeConfig {
    private static FileConfiguration iSafeConfig = null;
    private static File iSafeConfigFile = null;
    
    private static final Logger log = Logger.getLogger("Minecraft");
    
    // Finally found a method that doesn't return in a NPE!
    private static final iSafe plugin = (iSafe) Bukkit.getPluginManager().getPlugin("iSafe");
    private static File datafolder = plugin.getDataFolder();
    
    public static void load() {
        iSafeConfig = getISafeConfig();
        iSafeConfig.options().header(Data.setISafeConfigHeader());

        iSafeConfig.addDefault("ConfigVersion", Double.valueOf(iSafe.ConfigVersion));
        if (iSafeConfig.getDouble("ConfigVersion") != Double.valueOf(iSafe.ConfigVersion)) {
            log.warning("[iSafe] ConfigVersion was modified! Setting config version to right value ..");
            iSafeConfig.set("ConfigVersion", Double.valueOf(iSafe.ConfigVersion));
        }

        iSafeConfig.addDefault("VerboseLogging", true);
        iSafeConfig.addDefault("DebugMode", false);
        iSafeConfig.addDefault("CheckForUpdates", true);
        iSafeConfig.addDefault("UseVaultForPermissions", false);
        iSafeConfig.addDefault("CreateUserFiles", true);
        iSafeConfig.addDefault("TrackUsageStatistics", true);

        getISafeConfig().options().copyDefaults(true);
        saveISafeConfig();
    }
    
    public static void reload() {
        if (iSafeConfigFile == null) {
            iSafeConfigFile = new File(datafolder, "iSafeConfig.yml");
        }
        iSafeConfig = YamlConfiguration.loadConfiguration(iSafeConfigFile);

        // Look for defaults in the jar
        InputStream defConfigStream = plugin.getResource("iSafeConfig.yml");
        if (defConfigStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            iSafeConfig.setDefaults(defConfig);
        }
    }

    public static FileConfiguration getISafeConfig() {
        if (iSafeConfig == null) {
            reload();
        }
        return iSafeConfig;
    }

    public static void saveISafeConfig() {
        if (iSafeConfig == null || iSafeConfigFile == null) {
            return;
        }
        try {
            iSafeConfig.save(iSafeConfigFile);
        } catch (IOException ex) {
            Logger.getLogger(JavaPlugin.class.getName()).log(Level.SEVERE, "Error saving iSafeConfig to " + iSafeConfigFile, ex);
        }
    }
}
