package com.mrmag518.iSafe.Files;

import com.mrmag518.iSafe.Util.Log;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class Blacklist {
    private static String[] default_place = {"10", "11", "46", "97:2"};
    private static String[] default_break = {"7", "19", "51", "52:0"};
    private static String[] default_drop = {"7", "397:3", "384"};
    private static String[] default_pickup = {"7", "46", "384"};
    private static String[] default_command = {"/nuke", "/strike"};
    private static String[] default_chat = {"word", "word2", "etc"};
    private static String[] default_chat_whitelist = {"wordd", "word22", "etcc"};
    private static String[] default_interact_block = {"77", "116", "137"};
    private static String[] default_interact_item = {"380", "383:50", "384"};
    
    public static void manageBlacklistDir() {
        for(World world : Bukkit.getWorlds()) {
            String worldname = world.getName();
            
            if(worldname == null) {
                Log.debug("Could not load worldname: " + worldname + ", because it is null.");
                return;
            }
            
            File blacklistDir = new File("plugins/iSafe/Blacklists");
            File worldDir = new File(blacklistDir + "/" + worldname);
            File blacklistFile = new File(worldDir + "/blacklist.yml");
            
            if(!blacklistDir.exists()) {
                blacklistDir.mkdir();
            }
            
            if(!worldDir.exists()) {
                worldDir.mkdir();
            }
            
            if(!blacklistFile.exists()) {
                try {
                    blacklistFile.createNewFile();
                } catch (IOException ex) {
                    Logger.getLogger(Blacklist.class.getName()).log(Level.SEVERE, "Error creating " + blacklistFile + " :(", ex);
                }
            }
        }
    }
    
    public static void loadBlacklists() {
        for(World world : Bukkit.getWorlds()) {
            String worldname = world.getName();
            FileConfiguration blacklist = getBlacklist(worldname);
            
            blacklist.options().header("test");
            
            blacklist.addDefault("Settings.Enabled", true);
            
            blacklist.addDefault("Events.Place.Enabled", false);
            blacklist.addDefault("Events.Place.Penalities.KickPlayer", false);
            blacklist.addDefault("Events.Place.Gamemode.ActiveFor.Survival", true);
            blacklist.addDefault("Events.Place.Gamemode.ActiveFor.Creative", true);
            blacklist.addDefault("Events.Place.Gamemode.ActiveFor.Adventure", true);
            blacklist.addDefault("Events.Place.Report.ToConsole", false);
            blacklist.addDefault("Events.Place.Report.ToPlayer", true);
            blacklist.addDefault("Events.Place.Economy.Enabled", false);
            blacklist.addDefault("Events.Place.Economy.WithdrawAmount", 10.0);
            blacklist.addDefault("Events.Place.Economy.AllowNegativeCashPile", false);
            blacklist.addDefault("Events.Place.Economy.NotifyPlayer", false);
            blacklist.addDefault("Events.Place.Blacklist", Arrays.asList(default_place));
            
            blacklist.addDefault("Events.Break.Enabled", false);
            blacklist.addDefault("Events.Break.Penalities.KickPlayer", false);
            blacklist.addDefault("Events.Break.Gamemode.ActiveFor.Survival", true);
            blacklist.addDefault("Events.Break.Gamemode.ActiveFor.Creative", true);
            blacklist.addDefault("Events.Break.Gamemode.ActiveFor.Adventure", true);
            blacklist.addDefault("Events.Break.Report.ToConsole", false);
            blacklist.addDefault("Events.Break.Report.ToPlayer", true);
            blacklist.addDefault("Events.Break.Economy.Enabled", false);
            blacklist.addDefault("Events.Break.Economy.WithdrawAmount", 10.0);
            blacklist.addDefault("Events.Break.Economy.AllowNegativeCashPile", false);
            blacklist.addDefault("Events.Break.Economy.NotifyPlayer", false);
            blacklist.addDefault("Events.Break.Blacklist", Arrays.asList(default_break));
            
            blacklist.addDefault("Events.Drop.Enabled", false);
            blacklist.addDefault("Events.Drop.Penalities.KickPlayer", false);
            blacklist.addDefault("Events.Drop.Gamemode.ActiveFor.Survival", true);
            blacklist.addDefault("Events.Drop.Gamemode.ActiveFor.Creative", true);
            blacklist.addDefault("Events.Drop.Gamemode.ActiveFor.Adventure", true);
            blacklist.addDefault("Events.Drop.Report.ToConsole", false);
            blacklist.addDefault("Events.Drop.Report.ToPlayer", true);
            blacklist.addDefault("Events.Drop.Economy.Enabled", false);
            blacklist.addDefault("Events.Drop.Economy.WithdrawAmount", 10.0);
            blacklist.addDefault("Events.Drop.Economy.AllowNegativeCashPile", false);
            blacklist.addDefault("Events.Drop.Economy.NotifyPlayer", false);
            blacklist.addDefault("Events.Drop.Blacklist", Arrays.asList(default_drop));
            
            blacklist.addDefault("Events.Pickup.Enabled", false);
            blacklist.addDefault("Events.Pickup.Penalities.KickPlayer", false);
            blacklist.addDefault("Events.Pickup.Gamemode.ActiveFor.Survival", true);
            blacklist.addDefault("Events.Pickup.Gamemode.ActiveFor.Creative", true);
            blacklist.addDefault("Events.Pickup.Gamemode.ActiveFor.Adventure", true);
            blacklist.addDefault("Events.Pickup.Report.ToConsole", false);
            blacklist.addDefault("Events.Pickup.Report.ToPlayer", true);
            blacklist.addDefault("Events.Pickup.Economy.Enabled", false);
            blacklist.addDefault("Events.Pickup.Economy.WithdrawAmount", 10.0);
            blacklist.addDefault("Events.Pickup.Economy.AllowNegativeCashPile", false);
            blacklist.addDefault("Events.Pickup.Economy.NotifyPlayer", false);
            blacklist.addDefault("Events.Pickup.Blacklist", Arrays.asList(default_pickup));
            
            blacklist.addDefault("Events.Command.Enabled", false);
            blacklist.addDefault("Events.Command.Penalities.KickPlayer", false);
            blacklist.addDefault("Events.Command.Gamemode.ActiveFor.Survival", true);
            blacklist.addDefault("Events.Command.Gamemode.ActiveFor.Creative", true);
            blacklist.addDefault("Events.Command.Gamemode.ActiveFor.Adventure", true);
            blacklist.addDefault("Events.Command.Report.ToConsole", false);
            blacklist.addDefault("Events.Command.Report.ToPlayer", true);
            blacklist.addDefault("Events.Command.Economy.Enabled", false);
            blacklist.addDefault("Events.Command.Economy.WithdrawAmount", 10.0);
            blacklist.addDefault("Events.Command.Economy.AllowNegativeCashPile", false);
            blacklist.addDefault("Events.Command.Economy.NotifyPlayer", false);
            blacklist.addDefault("Events.Command.Blacklist", Arrays.asList(default_command));
            
            blacklist.addDefault("Events.Chat.Enabled", false);
            blacklist.addDefault("Events.Chat.Penalities.KickPlayer", false);
            blacklist.addDefault("Events.Chat.Gamemode.ActiveFor.Survival", true);
            blacklist.addDefault("Events.Chat.Gamemode.ActiveFor.Creative", true);
            blacklist.addDefault("Events.Chat.Gamemode.ActiveFor.Adventure", true);
            blacklist.addDefault("Events.Chat.Report.ToConsole", false);
            blacklist.addDefault("Events.Chat.Report.ToPlayer", true);
            blacklist.addDefault("Events.Chat.Economy.Enabled", false);
            blacklist.addDefault("Events.Chat.Economy.WithdrawAmount", 10.0);
            blacklist.addDefault("Events.Chat.Economy.AllowNegativeCashPile", false);
            blacklist.addDefault("Events.Chat.Economy.NotifyPlayer", false);
            blacklist.addDefault("Events.Chat.CheckSettings.RemoveSpaces", false);
            blacklist.addDefault("Events.Chat.CheckSettings.RemovePeriods", false);
            blacklist.addDefault("Events.Chat.CheckSettings.RemoveExclamations", false);
            blacklist.addDefault("Events.Chat.CheckSettings.RemoveQuestonMarks", false);
            blacklist.addDefault("Events.Chat.CheckSettings.SeeNumbersAsLetters", false);
            blacklist.addDefault("Events.Chat.Blacklist", Arrays.asList(default_chat));
            blacklist.addDefault("Events.Chat.Whitelist", Arrays.asList(default_chat_whitelist));
            
            blacklist.addDefault("Events.Interact.Enabled", false);
            blacklist.addDefault("Events.Interact.Penalities.KickPlayer", false);
            blacklist.addDefault("Events.Interact.Gamemode.ActiveFor.Survival", true);
            blacklist.addDefault("Events.Interact.Gamemode.ActiveFor.Creative", true);
            blacklist.addDefault("Events.Interact.Gamemode.ActiveFor.Adventure", true);
            blacklist.addDefault("Events.Interact.Report.ToConsole", false);
            blacklist.addDefault("Events.Interact.Report.ToPlayer", true);
            blacklist.addDefault("Events.Interact.Economy.Enabled", false);
            blacklist.addDefault("Events.Interact.Economy.WithdrawAmount", 10.0);
            blacklist.addDefault("Events.Interact.Economy.AllowNegativeCashPile", false);
            blacklist.addDefault("Events.Interact.Economy.NotifyPlayer", false);
            blacklist.addDefault("Events.Interact.BlockBlacklist", Arrays.asList(default_interact_block));
            blacklist.addDefault("Events.Interact.ItemBlacklist", Arrays.asList(default_interact_item));
            
            blacklist.addDefault("Events.Crafting.Enabled", false);
            blacklist.addDefault("Events.PistonExtend.Enabled", false);
            
            blacklist.options().copyDefaults(true);
            saveBlacklist(blacklist, worldname);
        }
    }
    
    public static File getBlacklistFile(String worldname) {
        return new File("plugins/iSafe/Blacklists/" + worldname + "/blacklist.yml");
    }
    
    public static FileConfiguration getBlacklist(String worldname) {
        File blacklistFile = getBlacklistFile(worldname);
        FileConfiguration blacklist = YamlConfiguration.loadConfiguration(blacklistFile);
        
        if(blacklist == null) {
            return null;
        }
        
        return blacklist;
    }
    
    public static void saveBlacklist(FileConfiguration blacklist, String worldname) {
        try {
            blacklist.save(getBlacklistFile(worldname));
        } catch (IOException ex) {
            Logger.getLogger(Blacklist.class.getName()).log(Level.SEVERE, "Error saving blacklist file " + getBlacklistFile(worldname), ex);
        }
    }
    
    /*public static void saveBlacklist(String worldname) {
        File blacklistFile = getBlacklistFile(worldname);
        FileConfiguration blacklist = getBlacklist(worldname);
        
        try {
            blacklist.save(blacklistFile);
        } catch (IOException ex) {
            Logger.getLogger(Blacklist.class.getName()).log(Level.SEVERE, "Error saving blacklist file " + blacklistFile, ex);
        }
    }*/
}
