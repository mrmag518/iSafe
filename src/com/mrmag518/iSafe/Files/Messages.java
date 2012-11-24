package com.mrmag518.iSafe.Files;

import com.mrmag518.iSafe.Util.Data;
import com.mrmag518.iSafe.iSafe;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Messages {
    private static FileConfiguration messages = null;
    private static File messagesFile = null;
    
    private static final Logger log = Logger.getLogger("Minecraft");
    
    // Finally found a method that doesn't return in a NPE!
    private static final iSafe plugin = (iSafe) Bukkit.getPluginManager().getPlugin("iSafe");
    private static File datafolder = plugin.getDataFolder();
    
    public static void load() {
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
        
        messages.addDefault("Blacklists.Break.KickMessage", "&cKicked for attempting to break &f%block%");
        messages.addDefault("Blacklists.Break.DisallowedMessage", "&cYou do not have access to break &7%block% &cin world &7%world%");
        messages.addDefault("Blacklists.Break.EcoMessage", "&e%amount% &c$ were taken away from your currency, because you tried to break an illegal block.");
        
        messages.addDefault("Blacklists.Place.KickMessage" , "&cKicked for attempting to place &f%block%");
        messages.addDefault("Blacklists.Place.DisallowedMessage", "&cYou do not have access to place &7%block% &cin world &7%world%");
        messages.addDefault("Blacklists.Place.EcoMessage", "&e%amount% &c$ were taken away from your currency, because you tried to place an illegal block.");
        
        messages.addDefault("Blacklists.Command.KickMessage", "&cKicked for attempting to do command &f%command%");
        messages.addDefault("Blacklists.Command.DisallowedMessage", "&cThe command %command% is disabled in world %world%!");
        messages.addDefault("Blacklists.Command.EcoMessage", "&e%amount% &$ cwere taken away from your currency, because you tried to do an illegal command.");
        
        messages.addDefault("Blacklists.Crafting.KickMessage", "&cKicked for attempting to craft &f%recipe%");
        messages.addDefault("Blacklists.Crafting.DisallowedMessage", "&cYou do not have access to craft &7%recipe% &cin world %world%");
        messages.addDefault("Blacklists.Crafting.EcoMessage", "&e%amount% &$ cwere taken away from your currency, because you tried to craft an illegal recipe.");
        
        messages.addDefault("Blacklists.Drop.KickMessage", "&cKicked for attempting to drop &f%item%");
        messages.addDefault("Blacklists.Drop.DisallowedMessage", "&cYou do not have access to drop &7%item% in world %world%");
        messages.addDefault("Blacklists.Drop.EcoMessage", "&e%amount% &c$ were taken away from your currency, because you tried to drop an illegal block.");
        
        messages.addDefault("Blacklists.Interact.KickMessage", "&cKicked for attempting to interact with &f%block%");
        messages.addDefault("Blacklists.Interact.DisallowedMessage", "&cYou do not have access to interact with &7%block% &cin world &7%world%");
        messages.addDefault("Blacklists.Interact.EcoMessage", "&e%amount% &$ cwere taken away from your currency, because you interacted with an illegal block.");
        
        messages.addDefault("Blacklists.Censor.KickMessage", "&cKicked for attempting to send a message contaning &7%word%");
        messages.addDefault("Blacklists.Censor.DisallowedMessage", "&c%word% is censored!");
        messages.addDefault("Blacklists.Censor.EcoMessage", "&e%amount% &c$ were taken away from your currency, because you tried to say an illegal word.");
        
        getMessages().options().copyDefaults(true);
        save();
    }
    
    public static void reload() {
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
            reload();
        }
        return messages;
    }

    public static void save() {
        if (messages == null || messagesFile == null) {
            return;
        }
        try {
            messages.save(messagesFile);
        } catch (IOException ex) {
            Logger.getLogger(JavaPlugin.class.getName()).log(Level.SEVERE, "Error saving Messages to " + messagesFile, ex);
        }
    }
    
    public static String scanVariables(
            String configString, String playerName,
            String cmd, String blockName,
            String item, String world,
            String word, String recipe) 
    {
        String result = configString;
        
        if (playerName != null) {
            if (configString.contains("%playername%")) {
                result = result.replaceAll("%playername%", playerName);
            }
        }
        if (cmd != null) {
            if (configString.contains("%command%")) {
                result = result.replace("%command%", cmd);
            }
        }
        if (blockName != null) {
            if (configString.contains("%block%")) {
                result = result.replaceAll("%block%", blockName);
            }
        }
        if (item != null) {
            if (configString.contains("%item%")) {
                result = result.replaceAll("%item%", item);
            }
        }
        if (world != null) {
            if (configString.contains("%world%")) {
                result = result.replaceAll("%world%", world);
            }
        }
        if (word != null) {
            if (configString.contains("%word%")) {
                result = result.replaceAll("%word%", word);
            }
        }
        if (recipe != null) {
            if (configString.contains("%recipe%")) {
                result = result.replaceAll("%recipe%", recipe);
            }
        }
        
        result = colorize(result);
        return result;
    }

    public static String colorize(String s) {
        if (s == null) {
            return null;
        }
        return s.replaceAll("&([0-9a-f])", "\u00A7$1");
    }
    
    public static void sendNoPermissionNotify(Player player) {
        player.sendMessage(colorize(getMessages().getString("Permissions.DefaultNoPermission")));
    }
    
    public static void sendNoPermissionNotify(CommandSender sender) {
        sender.sendMessage(colorize(getMessages().getString("Permissions.NoCmdPermission")));
    }
    

    public static void sendKickMessage(Player p) {
        String kickMsg = getMessages().getString("KickMessage");
        Server s = p.getServer();
        s.broadcastMessage(scanVariables(kickMsg, p.getName(),
                null, null,
                null, p.getWorld().getName(),
                null, null));
    }

    public static String sameNickPlaying(Player p) {
        String kickMsg = getMessages().getString("SameNickAlreadyPlaying");
        return scanVariables(kickMsg, p.getName(),
                null, null,
                null, p.getWorld().getName(),
                null, null);
    }

    public static String denyNonOpsJoin() {
        String kickMsg = getMessages().getString("OnlyOpsCanJoin");
        return scanVariables(kickMsg, null,
                null, null,
                null, null,
                null, null);
    }

    public static String commandLogger(Player p, PlayerCommandPreprocessEvent event) {
        String logged = getMessages().getString("CommandLogger");
        return scanVariables(logged, p.getName(), event.getMessage(),
                null, null,
                p.getWorld().getName(),
                null, null);
    }

    public static String blacklistInteractKickMsg(Block b) {
        String kickMsg = getMessages().getString("Blacklists.Interact.KickMessage");
        return scanVariables(kickMsg, null,
                null, b.getType().name().toLowerCase(),
                null, b.getWorld().getName(),
                null, null);
    }

    public static String blacklistPlaceKickMsg(Block b) {
        String kickMsg = getMessages().getString("Blacklists.Place.KickMessage");
        return scanVariables(kickMsg, null,
                null, b.getType().name().toLowerCase(),
                null, b.getWorld().getName(),
                null, null);
    }

    public static String blacklistBreakKickMsg(Block b) {
        String kickMsg = getMessages().getString("Blacklists.Break.KickMessage");
        return scanVariables(kickMsg, null,
                null, b.getType().name().toLowerCase(),
                null, b.getWorld().getName(),
                null, null);
    }

    public static String blacklistCensorKickMsg(String word) {
        String kickMsg = getMessages().getString("Blacklists.Censor.KickMessage");
        return scanVariables(kickMsg, null,
                null, null,
                null, null,
                word, null);
    }

    public static String blacklistDropKickMsg(Item i) {
        String kickMsg = getMessages().getString("Blacklists.Drop.KickMessage");
        return scanVariables(kickMsg, null,
                null, null,
                i.getItemStack().getType().name().toLowerCase(), i.getWorld().getName(),
                null, null);
    }

    public static String blacklistPickupKickMsg(String item) {
        String kickMsg = getMessages().getString("Blacklists.Pickup.KickMessage");
        return scanVariables(kickMsg, null,
                null, null,
                item, null,
                null, null);
    }
    
    public static String blacklistCraftingKickMsg(String recipe) {
        String kickMsg = getMessages().getString("Blacklists.Crafting.KickMessage");
        return scanVariables(kickMsg, null,
                null, null,
                null, null,
                null, recipe);
    }

    public static String blacklistCommandKickMsg(String cmd, String world) {
        String kickMsg = getMessages().getString("Blacklists.Command.KickMessage");
        return scanVariables(kickMsg, null,
                cmd, null,
                null, world,
                null, null);
    }

    public static String blacklistInteractMsg(Block b) {
        String disallowedMsg = getMessages().getString("Blacklists.Interact.DisallowedMessage");
        return scanVariables(disallowedMsg, null,
                null, b.getType().name().toLowerCase(),
                null, b.getWorld().getName(),
                null, null);
    }

    public static String blacklistPlaceMsg(Block b) {
        String disallowedMsg = getMessages().getString("Blacklists.Place.DisallowedMessage");
        return scanVariables(disallowedMsg, null,
                null, b.getType().name().toLowerCase(),
                null, b.getWorld().getName(),
                null, null);
    }

    public static String blacklistBreakMsg(Block b) {
        String disallowedMsg = getMessages().getString("Blacklists.Break.DisallowedMessage");
        return scanVariables(disallowedMsg, null,
                null, b.getType().name().toLowerCase(),
                null, b.getWorld().getName(),
                null, null);
    }

    public static String blacklistCensorMsg(String word, World world) {
        String disallowedMsg = getMessages().getString("Blacklists.Censor.DisallowedMessage");
        return scanVariables(disallowedMsg, null,
                null, null,
                null, world.getName(),
                word, null);
    }

    public static String blacklistDropMsg(String item, World world) {
        String disallowedMsg = getMessages().getString("Blacklists.Drop.DisallowedMessage");
        return scanVariables(disallowedMsg, null,
                null, null,
                item, world.getName(),
                null, null);
    }
    
    public static String blacklistCraftingMsg(String recipe, World world) {
        String disallowedMsg = getMessages().getString("Blacklists.Crafting.DisallowedMessage");
        return scanVariables(disallowedMsg, null,
                null, null,
                null, world.getName(),
                null, recipe);
    }

    public static String blacklistCommandMsg(String cmd, String world) {
        String disallowedMsg = getMessages().getString("Blacklists.Command.DisallowedMessage");
        return scanVariables(disallowedMsg, null,
                cmd, null,
                null, world,
                null, null);
    }
}
