package com.mrmag518.iSafe.Util;

import com.mrmag518.iSafe.iSafe;
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
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class UserFileCreator implements Listener {
    public static iSafe plugin;
    public UserFileCreator(iSafe instance) {
        plugin = instance;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    
    @EventHandler
    public void CreateUserFile(PlayerJoinEvent event) {
        Player p = event.getPlayer();
        File userFile = new File("plugins/iSafe/UserFiles/Users/" + p.getName() + ".yml");
        
        if(!userFile.exists()) {
            try {
                FileConfiguration uFile = YamlConfiguration.loadConfiguration(userFile);
                
                uFile.options().header("Please do not change anything inside this file. "
                        + "\nEditing updateable nodes is no harm, but nodes that's not being updated will mostly harm features either now, or in the future."
                        + "\nMost nodes will be editable in the near future.");
                
                uFile.set("Username", p.getName());
                uFile.set("DisplayName", p.getDisplayName());
                uFile.set("IPAddress", event.getPlayer().getAddress().getAddress().toString().replace("/", ""));
                uFile.set("IsBanned", p.isBanned());
                uFile.set("Gamemode", p.getGameMode().name().toLowerCase());
                uFile.set("ExpLevel", p.getLevel());
                
                uFile.save(userFile);
                
                Log.info("[iSafe] Generated user file for " + p.getName() + ".");
            } catch (Exception e) {
                Log.severe("[iSafe] Error generating the user file for: "+ p.getName() + ".");
                e.printStackTrace();
            }
        } else {
            updateNodes(event.getPlayer());
        }
    }
    
    @EventHandler
    public void updateNodesOnKick(PlayerKickEvent event) {
        updateNodes(event.getPlayer());
    }
    
    @EventHandler
    public void updateNodesOnQuit(PlayerQuitEvent event) {
        updateNodes(event.getPlayer());
    }
    
    private void updateNodes(Player p) {
        File userFile = new File("plugins/iSafe/UserFiles/Users/" + p.getName() + ".yml");
        
        if(userFile.exists()) {
             FileConfiguration uFile = YamlConfiguration.loadConfiguration(userFile);
             
             uFile.options().header("Please do not change anything inside this file. "
                    + "\nEditing updateable nodes is no harm, but nodes that's not being updated will mostly harm features either now, or in the future."
                    + "\nMost nodes will be editable in the near future.");
             
             uFile.set("DisplayName", p.getDisplayName());
             //uFile.set("IPAddress", p.getAddress().getAddress().toString().replace("/", ""));
             uFile.set("IsBanned", p.isBanned());
             uFile.set("Gamemode", p.getGameMode().name().toLowerCase());
             if(uFile.get("Level") != null) {
                uFile.set("Level", null);
             }
             uFile.set("ExpLevel", p.getLevel());
             
            try {
                uFile.save(userFile);
            } catch (IOException ex) {
                Logger.getLogger(UserFileCreator.class.getName()).log(Level.SEVERE, "Error trying to save userFile", ex);
            }
        }
    }
}
