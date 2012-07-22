package com.mrmag518.iSafe;

import java.io.File;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

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
            
            try {
                FileConfiguration uFile = YamlConfiguration.loadConfiguration(userFile);
                uFile.options().header("Please remember you cannot modify anything in the user files."
                        + "\nYou may be able to edit in the future.");
                
                uFile.set("Username", user.getName());
                uFile.set("DisplayName", user.getDisplayName());
                uFile.set("IPAddress", event.getPlayer().getAddress().getAddress().toString().replace("/", ""));
                uFile.set("Gamemode", user.getGameMode().name().toLowerCase());
                uFile.set("Level", user.getLevel());;
                uFile.save(userFile);
                plugin.log.info("[iSafe] Generated user file for " + user.getName() + ".");
            } catch (Exception e) {
                plugin.log.severe("[iSafe] Error generating the user file for: "+ user.getName() + ".");
                e.printStackTrace();
            }
        }
        // We update the nodes when the player leaves instead :)
    }
    
    @EventHandler
    public void updateNodes(PlayerQuitEvent event) {
        Player user = event.getPlayer();
        File userFile = new File(plugin.getDataFolder() + File.separator + "Users" + File.separator + user.getName() + ".yml");
        
        if(userFile.exists()) {
             FileConfiguration uFile = YamlConfiguration.loadConfiguration(userFile);
             uFile.set("IPAddress", null);
             uFile.set("Gamemode", null);
             uFile.set("Level", null);
             
             uFile.set("IPAddress", user.getAddress().getAddress().toString().replace("/", ""));
             uFile.set("Gamemode", user.getGameMode().name().toLowerCase());
             uFile.set("Level", user.getLevel());
            try {
                uFile.save(userFile);
            } catch (IOException ex) {
                Logger.getLogger(UserFileCreator.class.getName()).log(Level.SEVERE, "Error trying to save userFile", ex);
            }
        }
    }
}
