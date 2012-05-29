package com.mrmag518.iSafe;

import java.io.File;

import java.io.IOException;
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
        File userFile = new File(plugin.getDataFolder() + File.separator + "Users" + File.separator + user.getName() + ".txt");
        
        if (!userFile.exists()) {
            plugin.log.info("[iSafe] Generating user file for " + user.getName() + ".");
            try {
                FileConfiguration uFile = YamlConfiguration.loadConfiguration(userFile);
                uFile.set("Username", user.getName());
                uFile.set("Displayname", user.getDisplayName());
                uFile.set("IPAddress", event.getPlayer().getAddress().getAddress().toString().replace("/", ""));
                uFile.save(userFile);
                plugin.log.info("[iSafe] Generated user file for " + user.getName() + ".");
            } catch (Exception e) {
                plugin.log.severe("[iSafe] Error generating the user file for: "+ user.getName() + ".");
                e.printStackTrace();
            }
        } else {
            String UserIP = event.getPlayer().getAddress().getAddress().toString();
            UserIP = UserIP.replace("/", "");
            UserString(event.getPlayer(), UserIP, "IPAddress");
        }
    }
    
    public static void UserString(OfflinePlayer ofpl, String value, String path) {
        File UserOneData = new File(plugin.getDataFolder() + File.separator + "Users" + File.separator + ofpl.getName() + ".txt");
        if (UserOneData.exists()) {
            FileConfiguration pFile = YamlConfiguration.loadConfiguration(UserOneData);
            pFile.set(path, value);
            try {
                pFile.save(UserOneData);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
