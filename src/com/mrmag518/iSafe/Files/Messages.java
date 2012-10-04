package com.mrmag518.iSafe.Files;

import com.mrmag518.iSafe.Data;
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

public class Messages {
    private static FileConfiguration messages = null;
    private static File messagesFile = null;
    
    private static final Logger log = Logger.getLogger("Minecraft");
    
    // Finally found a method that doesn't return in a NPE!
    private static final iSafe plugin = (iSafe) Bukkit.getPluginManager().getPlugin("iSafe");
    private static File datafolder = plugin.getDataFolder();
    
    public static void loadMessages() {
        messages = getMessages();
        messages.options().header(Data.setMessageHeader());

        // Note to self; Do not create un-needed variables if not needed. Duh.

        messages.addDefault("ConfigVersion", Double.valueOf(iSafe.ConfigVersion));
        if (messages.getDouble("ConfigVersion") != Double.valueOf(iSafe.ConfigVersion)) {
            log.warning("[iSafe] ConfigVersion was modified! Setting config version to right value ..");
            messages.set("ConfigVersion", Double.valueOf(iSafe.ConfigVersion));
        }

        messages.addDefault("Permissions.DefaultNoPermission", "&cNo permission.");
        messages.addDefault("Permissions.NoCmdPermission", "&cNo permission to do this command.");
        messages.addDefault("KickMessage", "&6%playername% was kicked from the game.");
        messages.addDefault("SameNickAlreadyPlaying", "&cThe username &f%playername% &cis already online!");
        messages.addDefault("OnlyOpsCanJoin", "&cOnly OPs can join this server!");
        messages.addDefault("CommandLogger", "%playername% did or tried to do the command %command%");
        messages.addDefault("SpamDetection", "&cDetected spam! Please calm down.");
        messages.addDefault("FullbrightDetection", "&ePlace a torch! (light source)");
        
        messages.addDefault("Blacklists.Interact.KickMessage", "&cKicked for attempting to interact with &f%block%");
        messages.addDefault("Blacklists.Interact.DisallowedMessage", "&cYou do not have access to interact with &7%block% &cin world &7%world%");
        
        messages.addDefault("Blacklists.Place.KickMessage", "&cKicked for attempting to place &f%block%");
        messages.addDefault("Blacklists.Place.DisallowedMessage", "&cYou do not have access to place &7%block% &cin world &7%world%");
        
        messages.addDefault("Blacklists.Break.KickMessage", "&cKicked for attempting to break &f%block%");
        messages.addDefault("Blacklists.Break.DisallowedMessage", "&cYou do not have access to break &7%block% &cin world &7%world%");
        
        messages.addDefault("Blacklists.Censor.KickMessage", "&cKicked for attempting to send a message contaning &7%word%");
        messages.addDefault("Blacklists.Censor.DisallowedMessage", "&c%word% is censored!");
        
        messages.addDefault("Blacklists.Drop.KickMessage", "&cKicked for attempting to drop &f%item%");
        messages.addDefault("Blacklists.Drop.DisallowedMessage", "&cYou do not have access to drop &7%item% in world %world%");
        
        messages.addDefault("Blacklists.Command.KickMessage", "&cKicked for attempting to do command &f%command%");
        messages.addDefault("Blacklists.Command.DisallowedMessage", "&cThe command %command% is disabled in world %world%!");
        
        messages.addDefault("Blacklists.Crafting.DisallowedMessage", "&cYou do not have access to craft &7%recipe% &cin world %world%");
        
        getMessages().options().copyDefaults(true);
        saveMessages();
    }
    
    public static void reloadMessages() {
        if (messagesFile == null) {
            messagesFile = new File(datafolder, "Messages.yml");
        }
        messages = YamlConfiguration.loadConfiguration(messagesFile);
        
        // Look for defaults in the jar
        InputStream defConfigStream = plugin.getResource("Messages.yml");
        if (defConfigStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            messages.setDefaults(defConfig);
        }
    }

    public static FileConfiguration getMessages() {
        if (messages == null) {
            reloadMessages();
        }
        return messages;
    }

    public static void saveMessages() {
        if (messages == null || messagesFile == null) {
            return;
        }
        try {
            messages.save(messagesFile);
        } catch (IOException ex) {
            Logger.getLogger(JavaPlugin.class.getName()).log(Level.SEVERE, "Error saving Messages to " + messagesFile, ex);
        }
    }
}
