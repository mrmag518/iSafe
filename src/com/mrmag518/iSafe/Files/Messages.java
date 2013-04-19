package com.mrmag518.iSafe.Files;

import com.mrmag518.iSafe.Util.Data;
import com.mrmag518.iSafe.iSafe;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Messages {
    private static FileConfiguration messages = null;
    private static File messagesFile = null;
    private static final Logger log = Logger.getLogger("Minecraft");
    private static final iSafe plugin = (iSafe) Bukkit.getPluginManager().getPlugin("iSafe");
    private static File datafolder = plugin.getDataFolder();
    
    private Player player = null;
    private OutputType output = null;
    private World world = null;
    private String item_name = null;
    private String other = null; // commands, censored words ..
    
    public Messages(OutputType output, Player player, World world, String itemName, String other) {
        this.player = player;
        this.output = output;
        this.world = world;
        this.item_name = itemName;
        this.other = other;
    }
    
    public enum OutputType {
        FULLBRIGHT_DETECTION(Messages.getMessages().getString("FullbrightDetection"));
        
        private String output;
        
        private OutputType(String msg) {
            output = msg;
        }
        
        public String getRawOutput() {
            return output;
        }
    }
    
    public void send(Player p) {
        if(output != null) {
            String msg = output.getRawOutput();
            String result = msg;
            
            if(player != null) {
                result = msg.replaceAll("%player%", p.getName());
            }
            
            if(world != null) {
                result = msg.replaceAll("%world%", world.getName());
            }
            
            if(item_name != null && !item_name.equals("")) {
                result = msg.replaceAll("%item%", item_name);
            }
            
            if(other != null && !other.equals("")) {
                result = msg.replaceAll("%string%", other);
            }
            result = colorize(result);
            
            p.sendMessage(result);
        }
    }
    
    public static void load() {
        messages = getMessages();
        messages.options().header(Data.setMessageHeader());

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
        messages.addDefault("LockIpKickMessage", "&cYour IP &f'%ip%'&c, does not match the IP in this user's userfile.");
        messages.addDefault("ToManyAccountsOnThisIPkickMsg", "&cReached max accounts for this IP address! (Max: %max%)");
        messages.addDefault("ToManyIPsOnThisAccKickMsg", "&cReached max IPs for this player name! (Max: %max%)");
        
        messages.addDefault("Blacklists.Place.KickMessage" , "&cKicked for attempting to place &f%itemName%");
        messages.addDefault("Blacklists.Place.DisallowedMessage", "&cYou do not have access to place &7%itemName% &cin world &7%world%");
        messages.addDefault("Blacklists.Place.EcoMessage", "&e%amount% &c$ were taken away from your currency, because you tried to place an illegal block.");
        
        messages.addDefault("Blacklists.Break.KickMessage", "&cKicked for attempting to break &f%itemName%");
        messages.addDefault("Blacklists.Break.DisallowedMessage", "&cYou do not have access to break &7%itemName% &cin world &7%world%");
        messages.addDefault("Blacklists.Break.EcoMessage", "&e%amount% &c$ were taken away from your currency, because you tried to break an illegal block.");
        
        messages.addDefault("Blacklists.Drop.KickMessage", "&cKicked for attempting to drop &f%itemName%");
        messages.addDefault("Blacklists.Drop.DisallowedMessage", "&cYou do not have access to drop &7%itemName% in world %world%");
        messages.addDefault("Blacklists.Drop.EcoMessage", "&e%amount% &c$ were taken away from your currency, because you tried to drop an illegal block.");
        
        messages.addDefault("Blacklists.Command.KickMessage", "&cKicked for attempting to do command &f%string%");
        messages.addDefault("Blacklists.Command.DisallowedMessage", "&cThe command %string% is disabled in world %world%!");
        messages.addDefault("Blacklists.Command.EcoMessage", "&e%amount% &$ cwere taken away from your currency, because you tried to do an illegal command.");
        
        messages.addDefault("Blacklists.Pickup.KickMessage", "&cKicked for attempting to pickup &f%itemName%");
        messages.addDefault("Blacklists.Pickup.DisallowedMessage", "&cYou do not have access to pickup &7%itemName% &cin world &7%world%");
        messages.addDefault("Blacklists.Pickup.EcoMessage", "&e%amount% &c$ were taken away from your currency, because you tried to pickup an illegal block.");
        
        messages.addDefault("Blacklists.Chat.KickMessage", "&cKicked for attempting to send a message contaning &7%string%");
        messages.addDefault("Blacklists.Chat.DisallowedMessage", "&cThe word &7%string% &cis censored!");
        messages.addDefault("Blacklists.Chat.EcoMessage", "&e%amount% &c$ were taken away from your currency, because you tried to say an illegal word.");
        
        messages.addDefault("Blacklists.Interact.KickMessage", "&cKicked for attempting to interact with &f%itemName%");
        messages.addDefault("Blacklists.Interact.DisallowedMessage", "&cYou do not have access to interact with &7%itemName% &cin world &7%world%");
        messages.addDefault("Blacklists.Interact.EcoMessage", "&e%amount% &$ cwere taken away from your currency, because you interacted with an illegal block.");
        
        messages.addDefault("Blacklists.Crafting.KickMessage", "&cKicked for attempting to craft &f%itemName%");
        messages.addDefault("Blacklists.Crafting.DisallowedMessage", "&cYou do not have access to craft &7%itemName% &cin world &7%world%");
        messages.addDefault("Blacklists.Crafting.EcoMessage", "&e%amount% &$ cwere taken away from your currency, because you tried to craft an illegal recipe.");
        
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
    
    public static String scan(String configString, Player p, String string, String itemName, World world) {
        String result = configString;
        String worldName = null;
        String playerName = null;
        
        if(world != null) {
            worldName = world.getName();
        }
        
        if(p != null) {
            playerName = p.getName();
        }
        
        if (playerName != null) {
            if (configString.contains("%playername%")) {
                result = result.replaceAll("%playername%", playerName);
            }
        }
        
        if (string != null) {
            if (configString.contains("%string%")) {
                result = result.replace("%string%", string);
            }
        }
        
        if (itemName != null) {
            itemName = itemName.toLowerCase();
            if (configString.contains("%itemName%")) {
                result = result.replaceAll("%itemName%", itemName);
            }
        }
        
        if (world != null) {
            if (configString.contains("%world%")) {
                result = result.replaceAll("%world%", worldName);
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
    

    /*public static void sendKickMessage(Player p) {
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
    }*/
}
