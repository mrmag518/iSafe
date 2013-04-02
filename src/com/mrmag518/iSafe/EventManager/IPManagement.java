package com.mrmag518.iSafe.EventManager;

import com.mrmag518.iSafe.Files.Config;
import com.mrmag518.iSafe.Files.Messages;
import com.mrmag518.iSafe.Files.iSafeConfig;
import com.mrmag518.iSafe.Util.Log;
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

public class IPManagement implements Listener {
    public static iSafe plugin;
    public IPManagement(iSafe instance) {
        plugin = instance;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    
    public static void checkIPLogger() {
        File IPLogFile = new File("plugins/iSafe/UserFiles/IPLog.yml");
        FileConfiguration IPLog = YamlConfiguration.loadConfiguration(IPLogFile);
        
        if(!IPLogFile.exists()) {
            try {
                IPLogFile.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(IPManagement.class.getName()).log(Level.SEVERE, "Error creating the IPLog.yml file!", ex);
            }
        }
        
        IPLog.options().header("If you got IPManagement enabled, IP information will be logged here. "
                + "\nThis is mostly just an utility tool file for iSafe."
                + "\n"
                + "\nThis file has two categories. NamesLinkedToIPs and IPsLinkedToNames. "
                + "\nAs you probably can see, there are exact opposite of each other."
                + "\nThis is because NamesLinkedToIPs will log IPs, and link player names to them."
                + "\nWhile IPsLinkedToNames will log player names, and link IPs to them."
                + "\nYou may wonder why on earth it's like this. Well as stated above, this is mostly an utility file, meaning iSafe uses this as a database. "
                + "\nAnd getting how many IPs one player has used, by going with the IPs first is not the greatest idea. "
                + "Because else iSafe would need to check through every single IP logged and check whether the specific player is linked with it."
                + "\n"
                + "\nSince YAML automatically makes sub-categories out of periods, the periods in IPs for NamesLinkedToIPs are replaced with hyphens. "
                + "\nAll names needs to end with a comma. iSafe does this for you thought."
                + "\n");
        
        try {
            IPLog.save(IPLogFile);
        } catch (IOException ex) {
            Logger.getLogger(IPManagement.class.getName()).log(Level.SEVERE, "Error saving IPLog file!", ex);
        }
    }
    
    public static void logIP_WithNameAdded(String ip, String victim) {
        if(ip == null || victim == null) {
            return;
        }
        File IPLogFile = new File("plugins/iSafe/UserFiles/IPLog.yml");
        FileConfiguration IPLog = YamlConfiguration.loadConfiguration(IPLogFile);
        
        IPLog.set("NamesLinkedToIPs." + getIpInYAMLFormat(ip), victim + ",");
        
        try {
            IPLog.save(IPLogFile);
        } catch (IOException ex) {
            Logger.getLogger(IPManagement.class.getName()).log(Level.SEVERE, "Error saving IPLog file!", ex);
        }
    }
    
    public static void logName_WithIPAdded(String victim, String ip) {
        if(ip == null || victim == null) {
            return;
        }
        File IPLogFile = new File("plugins/iSafe/UserFiles/IPLog.yml");
        FileConfiguration IPLog = YamlConfiguration.loadConfiguration(IPLogFile);
        
        IPLog.set("IPsLinkedToNames." + victim, ip + ",");
        
        try {
            IPLog.save(IPLogFile);
        } catch (IOException ex) {
            Logger.getLogger(IPManagement.class.getName()).log(Level.SEVERE, "Error saving IPLog file!", ex);
        }
    }
    
    public static void addNameToIP(String victim, String ip) {
        if(ip == null || victim == null) {
            return;
        }
        File IPLogFile = new File("plugins/iSafe/UserFiles/IPLog.yml");
        FileConfiguration IPLog = YamlConfiguration.loadConfiguration(IPLogFile);
        
        String prevNames = IPLog.getString("NamesLinkedToIPs." + getIpInYAMLFormat(ip));
        
        if(prevNames == null || prevNames.equals("")) {
            IPLog.set("NamesLinkedToIPs." + getIpInYAMLFormat(ip), victim + ",");
        } else {
            IPLog.set("NamesLinkedToIPs." + getIpInYAMLFormat(ip), prevNames + victim + ",");
        }
        
        try {
            IPLog.save(IPLogFile);
        } catch (IOException ex) {
            Logger.getLogger(IPManagement.class.getName()).log(Level.SEVERE, "Error saving IPLog file!", ex);
        }
    }
    
    public static void addIPToName(String ip, String victim) {
        if(ip == null || victim == null) {
            return;
        }
        File IPLogFile = new File("plugins/iSafe/UserFiles/IPLog.yml");
        FileConfiguration IPLog = YamlConfiguration.loadConfiguration(IPLogFile);
        
        String prevIPs = IPLog.getString("IPsLinkedToNames." + victim);
        
        if(prevIPs == null || prevIPs.equals("")) {
             IPLog.set("IPsLinkedToNames." + victim, ip + ",");
        } else {
            IPLog.set("IPsLinkedToNames." + victim, prevIPs + ip + ",");
        }
        
        try {
            IPLog.save(IPLogFile);
        } catch (IOException ex) {
            Logger.getLogger(IPManagement.class.getName()).log(Level.SEVERE, "Error saving IPLog file!", ex);
        }
    }
    
    public static boolean isIPLogged(String ip) {
        File IPLogFile = new File("plugins/iSafe/UserFiles/IPLog.yml");
        FileConfiguration IPLog = YamlConfiguration.loadConfiguration(IPLogFile);
        
        if(IPLog.get("NamesLinkedToIPs." + getIpInYAMLFormat(ip)) != null) {
            return true;
        }
        return false;
    }
    
    public static boolean isNameLogged(String name) {
        File IPLogFile = new File("plugins/iSafe/UserFiles/IPLog.yml");
        FileConfiguration IPLog = YamlConfiguration.loadConfiguration(IPLogFile);
        
        if(IPLog.get("IPsLinkedToNames." + name) != null) {
            return true;
        }
        return false;
    }
    
    public static String getIpInYAMLFormat(String ip) {
        if(!ip.contains(".")) {
            Log.debug("Invalid IP! (" + ip + ")");
            return null;
        }
        String formatted = ip.replaceAll("\\.", "-");
        return formatted;
    }
    
    public static String getIPAddress(String victim) {
        String ip = "null";
        
        File userFile = new File("plugins/iSafe/UserFiles/Users/" + victim + ".yml");
        if(userFile.exists()) {
            FileConfiguration uFile = YamlConfiguration.loadConfiguration(userFile);
            ip = uFile.getString("IPAddress");
        }
        return ip;
    }
    
    /*public String getIpFromYAMLFormat(String ip) {
        if(!ip.contains("-")) {
            Log.debug("'" + ip + "' is an invalid YAML Formatted IP!");
            return null;
        }
        String normalIP = ip.replaceAll("-", ".");
        return normalIP;
    }*/
    
    @EventHandler
    public void handleJoin(PlayerJoinEvent event) {
        Player p = event.getPlayer();
        
        if(Config.getConfig().getBoolean("AntiCheat/Security.IPManagement.Enabled") != true) {
            return;
        }
        
        if(iSafeConfig.getISafeConfig().getBoolean("CreateUserFiles") != true) {
            Log.warning("IPManagement is enabled, but CreateUserFiles is not set to true in your iSafeConfig!");
            return;
        }
        
        /**
         * Needs a lot of expansion! :)
         */
        
        File userFile = new File("plugins/iSafe/UserFiles/Users/" + p.getName() + ".yml");
        
        boolean lockIP = Config.getConfig().getBoolean("AntiCheat/Security.IPManagement.LockIpToFirstJoin");
        boolean checkAlternate = Config.getConfig().getBoolean("AntiCheat/Security.IPManagement.AlternateAccounts.EnableChecking");
        
        if(userFile.exists()) {
            FileConfiguration userOptions = YamlConfiguration.loadConfiguration(userFile);
            
            if(userOptions.get("IPAddress") != null) {
                String userIP = userOptions.getString("IPAddress");
                String joinerIP =  p.getAddress().getAddress().toString().replace("/", "");
                
                if(lockIP == true) {
                    if(!userIP.equalsIgnoreCase(joinerIP)) {
                        p.kickPlayer(Messages.colorize(Messages.getMessages().getString("LockIpKickMessage").replace("%ip%", joinerIP)));
                    }
                }
                
                if(checkAlternate == true) {
                    File IPLogFile = new File("plugins/iSafe/UserFiles/IPLog.yml");
                    FileConfiguration IPLog = YamlConfiguration.loadConfiguration(IPLogFile);
                    
                    if(isIPLogged(joinerIP)) {
                        if(!IPLog.getString("NamesLinkedToIPs." + getIpInYAMLFormat(joinerIP)).contains(p.getName() + ",")) {
                            String names = IPLog.getString("NamesLinkedToIPs." + getIpInYAMLFormat(joinerIP));
                            
                            int namesLinked = 0;
                            for(int i = 0; i < names.length(); i++) {
                                if(names.charAt(i) == ',') {
                                    namesLinked++;
                                } 
                            }
                            
                            int maxAccountsPerIP = Config.getConfig().getInt("AntiCheat/Security.IPManagement.AlternateAccounts.MaxAccountsPerIP");
                            
                            if(maxAccountsPerIP == 0) {
                                return;
                            }
                            
                            if(namesLinked >= maxAccountsPerIP) {
                                p.kickPlayer(Messages.colorize(Messages.getMessages().getString("ToManyAccountsOnThisIPkickMsg").replace("%max%", String.valueOf(maxAccountsPerIP))));
                            } else {
                                addNameToIP(p.getName(), joinerIP);
                            }
                        }
                    } else {
                        logIP_WithNameAdded(joinerIP, p.getName());
                    }
                    
                    if(isNameLogged(p.getName())) {
                        if(!IPLog.getString("IPsLinkedToNames." + p.getName()).contains(joinerIP + ",")) {
                            String IPs = IPLog.getString("IPsLinkedToNames." + p.getName());
                            
                            int ipsLinked = 0;
                            for(int i = 0; i < IPs.length(); i++) {
                                if(IPs.charAt(i) == ',') {
                                    ipsLinked++;
                                } 
                            }
                            
                            int maxIPsPerPlayer = Config.getConfig().getInt("AntiCheat/Security.IPManagement.AlternateAccounts.MaxIPsPerPlayer");
                            
                            if(maxIPsPerPlayer == 0) {
                                return;
                            }
                            
                            if(ipsLinked >= maxIPsPerPlayer) {
                                p.kickPlayer(Messages.colorize(Messages.getMessages().getString("ToManyIPsOnThisAccKickMsg").replace("%max%", String.valueOf(maxIPsPerPlayer))));
                            } else {
                                addIPToName(p.getName(), joinerIP);
                            }
                        }
                    } else {
                        logName_WithIPAdded(p.getName(), joinerIP);
                    }
                }
                
            } else {
                Log.debug("Found no field 'IPAddress' in " + userFile);
            }
        }
    }
}
