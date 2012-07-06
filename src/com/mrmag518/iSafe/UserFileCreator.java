package com.mrmag518.iSafe;

import java.io.File;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class UserFileCreator implements Listener {
    public static iSafe plugin;
    public UserFileCreator(iSafe instance)
    {
        plugin = instance;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    
    @EventHandler
    public void CreateUserFile(PlayerJoinEvent event) {
        Player user = event.getPlayer();
        File userFile = new File(plugin.getDataFolder() + File.separator + "Users" + File.separator + user.getName() + ".yml");
        
        if (!userFile.exists()) {
            
            // iSafe v3.0 convertion.
            File oldFile = new File(plugin.getDataFolder() + File.separator + "Users" + File.separator + user.getName() + ".txt");
            if(oldFile.exists()) {
                oldFile.delete();
                plugin.log.info("[iSafe] Detected old userfile format, deleting .. ("+oldFile.getName()+")");
            }
            
            plugin.log.info("[iSafe] Generating user file for " + user.getName() + ".");
            try {
                FileConfiguration uFile = YamlConfiguration.loadConfiguration(userFile);
                uFile.set("Username", user.getName());
                uFile.set("DisplayName", user.getDisplayName());
                uFile.set("IPAddress", event.getPlayer().getAddress().getAddress().toString().replace("/", ""));
                uFile.set("Gamemode", user.getGameMode().name().toLowerCase());
                uFile.save(userFile);
                plugin.log.info("[iSafe] Generated user file for " + user.getName() + ".");
            } catch (Exception e) {
                plugin.log.severe("[iSafe] Error generating the user file for: "+ user.getName() + ".");
                e.printStackTrace();
            }
        } else {
            FileConfiguration uFile = YamlConfiguration.loadConfiguration(userFile);
            uFile.set("IPAddress", null);
            uFile.set("Gamemode", null);
            
            uFile.set("IPAddress", user.getAddress().getAddress().toString().replace("/", ""));
            uFile.set("Gamemode", user.getGameMode().name().toLowerCase());
        }
    }
}
