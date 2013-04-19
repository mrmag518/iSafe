package com.mrmag518.iSafe;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

public class Test {
    private static File configFile = null;
    private static YamlConfiguration config = null;
    private static final iSafe plugin = (iSafe) Bukkit.getPluginManager().getPlugin("iSafe");
    
    public static void load() {
        if(configFile == null) {
            configFile = new File("plugins/iSafe/TestConfig.yml");
        }
        if(!configFile.exists()) {
            try {
                configFile.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(Test.class.getName()).log(Level.SEVERE, "Error creating TestConfig.yml", ex);
            }
        }
        config = YamlConfiguration.loadConfiguration(configFile);
        
        InputStream is = plugin.getResource("TestConfig.yml");
        if(is != null) {
            YamlConfiguration con = YamlConfiguration.loadConfiguration(is);
            config.setDefaults(con);
        }
        config.options().copyDefaults(true);
    }
    
    public static void save() {
        
    }
}
