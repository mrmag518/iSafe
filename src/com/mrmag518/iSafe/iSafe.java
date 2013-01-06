/*
 * iSafe
 * Copyright (C) 2011-2012 mrmag518 <magnusaub@yahoo.no>
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.mrmag518.iSafe;

import com.mrmag518.iSafe.Util.SendUpdate;
import com.mrmag518.iSafe.Util.UserFileCreator;
import com.mrmag518.iSafe.Util.Data;
import com.mrmag518.iSafe.Events.BlockEvents.*;
import com.mrmag518.iSafe.Events.EntityEvents.*;
import com.mrmag518.iSafe.Events.WorldEvents.*;
import com.mrmag518.iSafe.Blacklists.Blacklists;
import com.mrmag518.iSafe.Commands.Commands;
import com.mrmag518.iSafe.Files.*;
import com.mrmag518.iSafe.Util.Log;

import java.io.IOException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilderFactory;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class iSafe extends JavaPlugin {
    
    /**
     * Note to self:
     * Start working on gamemode 'protection'.
     * More bug hunting...
     * Commands to toggle the enable state of specific worlds in the blacklist.
     * Add group thing to blacklists.
     * Potion management.
     * Move all Events classes to the a new EventManagers package.
     * Reorganize the config files?
     * Extend IPManagement and UserFiles features & support.
     */
    
    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    private final String fileversion = "iSafe v3.41";
    public static final Double ConfigVersion = 3.41;
    public final String MCVersion = "1.4.6";
    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    private PlayerListener playerListener = null;
    private BlockListener blockListener = null;
    private EntityListener entityListener = null;
    private WeatherListener weatherListener = null;
    private InventoryListener inventoryListener = null;
    private VehicleListener vehicleListener = null;
    private WorldListener worldListener = null;
    private EnchantmentListener enchantmentListener = null;
    private DropListener dropListener = null;
    private UserFileCreator UFC = null;
    private SendUpdate sendUpdate = null;
    private Blacklists blacklistClass = null;
    private IPManagement IPM = null;
    
    public double currentVersion;
    public double newVersion;
    
    public static Permission perms = null;
    public static Economy economy = null;
    
    public boolean checkingUpdatePerms = false;
    public boolean cancelDamagePerms = false;
    public boolean checkingSpamPerms = false;
    public boolean checkingFullbrightPerms = false;
    private boolean isStartup = false;
    public HashMap<String, Integer> spamDB = new HashMap<>();
    
    @Override
    public void onDisable() {
        PluginDescriptionFile pdffile = getDescription();
        Log.verbose("v" + pdffile.getVersion() + " disabled.");
    }

    @Override
    public void onEnable() {
        isStartup = true;
        Log.debug("iSafe startup initialized.");
        
        currentVersion = Double.valueOf(getDescription().getVersion());
        
        fileLoadManagement();
        Log.debug("Debug mode is enabled!");
        
        registerClasses();
        
        PluginDescriptionFile pdffile = getDescription();
        if (iSafeConfig.getISafeConfig().getBoolean("CheckForUpdates") == true) {
            //Update checker - From MilkBowl.
            getServer().getScheduler().runTaskTimerAsynchronously(this, new Runnable() {

                @Override
                public void run() {
                    try {
                        Log.debug("Running update check ..");
                        newVersion = updateCheck(currentVersion);

                        if (newVersion > currentVersion) {
                            Log.debug("A new version was found!");
                            Log.info(" ");
                            Log.info("#######  iSafe UpdateChecker  #######");
                            Log.info("A new update for iSafe was found! " + newVersion);
                            Log.info("You are currently running version: " + currentVersion);
                            Log.info("You can find this new version at BukkitDev.");
                            Log.info("http://dev.bukkit.org/server-mods/blockthattnt/");
                            Log.info("#####################################");
                            Log.info(" ");
                        } else {
                            Log.debug("No new version was found.");
                        }
                    } catch (Exception ignored) {
                        //Ignored
                    }
                }
            }, 0, 36000);
        } else {
            Log.debug("CheckForUpdates is false in the iSafeConfig.yml, will not check for iSafe updates!");
        }
        
        getCommand("iSafe").setExecutor(new Commands(this));
        
        checkMatch();
        
        if(iSafeConfig.getISafeConfig().getBoolean("TrackUsageStatistics") == true) {
            try {
                Log.debug("Starting plugin metrics ..");
                MetricsLite metrics = new MetricsLite(this);
                metrics.start();
            } catch (IOException e) {
                // Failed to submit the stats :-(
            }
        }
        
        //iSafeExtension.hook();
        
        Log.verbose("v" + pdffile.getVersion() + " enabled.");
        
        Log.debug("iSafe startup finished.");
        isStartup = false;
    }

    private void startSpamTask() {
        Log.debug("Starting spam task ..");
        Log.debug("Running every 20th game tick. (1sec)");
        Log.debug("Note: If the server has lag, the time for each spam loop may go out of sync.");
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                    String name = p.getName();
                    if (spamDB.containsKey(name)) {
                        if (spamDB.get(name).intValue() > 0) {
                            spamDB.put(name, spamDB.get(name) + - 1);
                        }
                    }
                }
            }
        }, 0, 20);
    }

    private void registerClasses() {
        playerListener = new PlayerListener(this);
        blockListener = new BlockListener(this);
        entityListener = new EntityListener(this);
        worldListener = new WorldListener(this);
        vehicleListener = new VehicleListener(this);
        weatherListener = new WeatherListener(this);
        inventoryListener = new InventoryListener(this);
        enchantmentListener = new EnchantmentListener(this);
        dropListener = new DropListener(this);
        IPM = new IPManagement(this);
        
        if (iSafeConfig.getISafeConfig().getBoolean("CreateUserFiles") == true) {
            UFC = new UserFileCreator(this);
        } else {
            UFC = null;
            Log.debug("CreateUserFiles in the iSafeConfig.yml was disabled, therefor not register the UserFileCreator class.");
        }
        
        if (iSafeConfig.getISafeConfig().getBoolean("CheckForUpdates") == true) {
            sendUpdate = new SendUpdate(this);
        } else {
            sendUpdate = null;
            Log.debug("CheckForUpdates in the iSafeConfig.yml was disabled, therefor not registering the sendUpdate class.");
        }
        
        blacklistClass = new Blacklists(this);
        
        Log.debug("Registered event classes.");
    }

    public void fileLoadManagement() {
        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }
        
        iSafeConfig.reload();
        iSafeConfig.load();
        iSafeConfig.reload();

        Messages.reload();
        Messages.load();
        Messages.reload();

        Config.reload();
        Config.load();
        Config.reload();
        
        File UserFiles = new File("plugins/iSafe/UserFiles/Users");
        UserFiles.mkdirs();
        
        File exaFile = new File(UserFiles + File.separator + "_example.yml");
        if(!exaFile.exists()) {
            try {
                FileConfiguration exampFile = YamlConfiguration.loadConfiguration(exaFile);
                exampFile.options().header(Data.setExFileHeader());
                exampFile.set("Username", "example");
                exampFile.set("Displayname", "example");
                exampFile.set("IPAddress", "127.0.0.1");
                exampFile.set("Gamemode", "survival");
                exampFile.set("Level", Integer.parseInt("50"));
                exampFile.save(exaFile);
            } catch (NumberFormatException | IOException e) {
                Log.info("[iSafe] Error creating example user file. (_example.yml)");
                e.printStackTrace();
            }
        }
        
        moveUserFiles();
        
        IPManagement.checkIPLogger();
        
        if (isStartup == true) {
            getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
                @Override
                public void run() {
                    
                    /*try {
                        // Test
                        manageWorlds();
                    } catch (IOException ex) {
                        Logger.getLogger(iSafe.class.getName()).log(Level.SEVERE, "Could not run the manageWorlds() method.", ex);
                    }*/
                    
                    CreatureManager.reload();
                    CreatureManager.load();
                    CreatureManager.reload();

                    BlacklistsF.reload();
                    BlacklistsF.load();
                    BlacklistsF.reload();

                    setupVault();
                }
            }, 40);
        } else {
            
            /*try {
                // Test
                manageWorlds();
            } catch (IOException ex) {
                Logger.getLogger(iSafe.class.getName()).log(Level.SEVERE, "Could not run the manageWorlds() method.", ex);
            }*/
            
            CreatureManager.reload();
            CreatureManager.load();
            CreatureManager.reload();
            
            BlacklistsF.reload();
            BlacklistsF.load();
            BlacklistsF.reload();
            
            setupVault();
        }
        
        boolean beastMode = getConfig().getBoolean("AntiCheat/Security.Spam.UseBeastMode");
        boolean normalMode = getConfig().getBoolean("AntiCheat/Security.Spam.UseNormalMode");
        
        if (beastMode == true) {
            if (normalMode == true) {
                getConfig().set("AntiCheat/Security.Spam.UseNormalMode", false);
                Config.save();
            }
            startSpamTask();
        }
    }
    
    private void moveUserFiles() {
        File oldDir = new File(getDataFolder() + File.separator + "Users");
        
        if(!oldDir.exists()) {
            return;
        }
        
        Log.info("Moving all user files inside 'plugins/iSafe/Users' to 'plugins/iSafe/UserFiles/Users' ..");
        
        for(File oldFile : oldDir.listFiles()) {
            try {
                File newFile = new File("plugins/iSafe/UserFiles/Users/" + oldFile.getName());
                
                FileOutputStream outStream;
                try (FileInputStream inStream = new FileInputStream(oldFile)) {
                    outStream = new FileOutputStream(newFile);
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = inStream.read(buffer)) > 0) {
                        outStream.write(buffer, 0, length);
                    }
                }
                outStream.close();
                oldFile.delete();
                
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
        oldDir.delete();
        
        Log.info("Done!");
    }
    
    /*private void manageWorlds() throws IOException {
        for(World world : getServer().getWorlds()) {
            String worldname = world.getName();
            
            if(worldname == null) {
                Log.debug("Could not load worldname: " + worldname + ", because it is null.");
                return;
            }
            
            File worldDir = new File("plugins/iSafe/worlds/" + worldname);
            
            if(!worldDir.exists()) {
                worldDir.mkdirs();
            }
        }
        
        for(World world : getServer().getWorlds()) {
            String worldname = world.getName();
            
            if(worldname == null) {
                Log.debug("Could not load worldname: " + worldname + ", because it is null.");
                return;
            }
            
            File worldDir = new File("plugins/iSafe/worlds/" + worldname);
            File configFile = new File(worldDir + "/config.yml");
            File blacklistsFile = new File(worldDir + "/blacklists.yml");
            File CMFile = new File(worldDir + "/creatureManager.yml");
            
            if(!configFile.exists()) {
                configFile.createNewFile();
            }
            if(!blacklistsFile.exists()) {
                blacklistsFile.createNewFile();
            }
            if(!CMFile.exists()) {
                CMFile.createNewFile();
            }
        }
    }*/

    private void setupVault() {
        if (iSafeConfig.getISafeConfig().getBoolean("UseVaultForPermissions", true)) {
            if (getServer().getPluginManager().getPlugin("Vault") != null) {
                Log.verbose("Using Vault for permissions!");
                setupPermissions();
            } else {
                Log.severe(" Vault.jar was NOT found in your plugins folder!");
                Log.severe("You need to have Vault.jar in the plugins folder if you're going to use Vault for permissions!");
                Log.severe("Settings UseVaultForPermissions in your iSafeConfig.yml to false ..");
                iSafeConfig.getISafeConfig().set("UseVaultForPermissions", false);
                iSafeConfig.saveISafeConfig();
            }
        }
        
        if (getServer().getPluginManager().getPlugin("Vault") != null) {
            setupEconomy();
            Log.verbose("Hooked to economy plugin '" + economy.getName() + "'.");
        }
    }

    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        perms = rsp.getProvider();
        Log.info("Hooked to permissions plugin: " + perms.getName());
        return perms != null;
    }
    
    private boolean setupEconomy() {
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }
        return (economy != null);
    }

    private void checkMatch() {
        PluginDescriptionFile pdffile = getDescription();
        if (!pdffile.getFullName().equals(fileversion)) {
            Log.warning("-----  iSafe vMatchConflict  -----");
            Log.warning("The version in the pdffile is not the same as the file.");
            Log.warning("pdffile version: " + pdffile.getFullName());
            Log.warning("File version: " + fileversion);
            Log.warning("Please deliver this infomation to " + pdffile.getAuthors() + " at BukkitDev.");
            Log.warning("-----  --------------------  -----");
        }
    }

    //Update checker (from MilkBowl's Vault, all credit to him)
    public double updateCheck(double currentVersion) throws Exception {
        String pluginUrlString = "http://dev.bukkit.org/server-mods/blockthattnt/files.rss";
        try {
            URL url = new URL(pluginUrlString);
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(url.openConnection().getInputStream());
            doc.getDocumentElement().normalize();
            NodeList nodes = doc.getElementsByTagName("item");
            Node firstNode = nodes.item(0);
            if (firstNode.getNodeType() == 1) {
                Element firstElement = (Element) firstNode;
                NodeList firstElementTagName = firstElement.getElementsByTagName("title");
                Element firstNameElement = (Element) firstElementTagName.item(0);
                NodeList firstNodes = firstNameElement.getChildNodes();
                return Double.valueOf(firstNodes.item(0).getNodeValue().replace("iSafe", "").replaceFirst(".", "").replace("v", "").trim());
            }
        } catch (Exception localException) {
        }
        return currentVersion;
    }
}
