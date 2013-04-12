package com.mrmag518.iSafe.Files;

import com.mrmag518.iSafe.Util.Data;
import com.mrmag518.iSafe.Util.Log;
import com.mrmag518.iSafe.iSafe;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class GMConfig {
    private static FileConfiguration config = null;
    private static File configFile = null;
    
    public static void load() {
        config = getConfig();
        config.options().header(Data.setConfigHeader());
        
        config.addDefault("ConfigVersion", Double.valueOf(iSafe.ConfigVersion));
        if (config.getDouble("ConfigVersion") != Double.valueOf(iSafe.ConfigVersion)) {
            Log.warning("ConfigVersion was modified! Setting config version to right value ..");
            config.set("ConfigVersion", Double.valueOf(iSafe.ConfigVersion));
        }
        
        config.addDefault("Enabled", false);
        config.addDefault("GameModeChange.Allow", true);
        config.addDefault("GameModeChange.DisallowSurvivalToCreative", false);
        config.addDefault("GameModeChange.DisallowCreativeToSurvival", false);
        
        getConfig().options().copyDefaults(true);
        save();
    }
    
    public static void reload() {
        if (configFile == null) {
            configFile = new File("plugins/iSafe/GMConfig.yml");
        }
        config = YamlConfiguration.loadConfiguration(configFile);
    }
    
    public static FileConfiguration getConfig() {
        if (config == null) {
            reload();
        }
        return config;
    }
    
    public static void save() {
        if (config == null || configFile == null) {
            return;
        }
        try {
            config.save(configFile);
        } catch (IOException ex) {
            Logger.getLogger(JavaPlugin.class.getName()).log(Level.SEVERE, "Could not save configFile (GMConfig) to " + configFile, ex);
        }
    }
}
