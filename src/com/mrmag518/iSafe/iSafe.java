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

import com.mrmag518.iSafe.EventManager.WorldListener;
import com.mrmag518.iSafe.EventManager.WeatherListener;
import com.mrmag518.iSafe.EventManager.PlayerListener;
import com.mrmag518.iSafe.EventManager.InventoryListener;
import com.mrmag518.iSafe.EventManager.EntityListener;
import com.mrmag518.iSafe.EventManager.EnchantmentListener;
import com.mrmag518.iSafe.EventManager.DropListener;
import com.mrmag518.iSafe.EventManager.BlockListener;
import com.mrmag518.iSafe.EventManager.IPManagement;
import com.mrmag518.iSafe.Util.SendUpdate;
import com.mrmag518.iSafe.Util.UserFileCreator;
import com.mrmag518.iSafe.Util.Data;
import com.mrmag518.iSafe.Blacklists.Blacklists;
import com.mrmag518.iSafe.Commands.Commands;
import com.mrmag518.iSafe.Files.*;
import com.mrmag518.iSafe.Util.Log;

import java.io.IOException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class iSafe extends JavaPlugin {
    
    /**
     * Note to self:

     * Start working on gamemode 'protection'.
     * More bug hunting...
     * Add group thing to blacklists.
     * Potion management.
     * Clean-up in listener classes. (Combine some classes together, such as the old InventoryListener)
     * Extend IPManager and UserFiles features & support.
     */
    
    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    private final String fileversion = "iSafe v3.5";
    public static final Double ConfigVersion = 3.5;
    public final String MCVersion = "1.5.1";
    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    private PlayerListener playerListener = null;
    private BlockListener blockListener = null;
    private EntityListener entityListener = null;
    private WeatherListener weatherListener = null;
    private InventoryListener inventoryListener = null;
    private WorldListener worldListener = null;
    private EnchantmentListener enchantmentListener = null;
    private DropListener dropListener = null;
    private UserFileCreator UFC = null;
    private SendUpdate sendUpdate = null;
    private Blacklists blacklistClass = null;
    private IPManagement IPM = null;
    //private GMManager gmManager = null;
    
    public double currentVersion = 0.0;
    public String versionFound = "";
    public static Permission perms = null;
    public static Economy economy = null;
    private boolean isStartup = false;
    public boolean updateFound = false;
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
        PluginDescriptionFile pdffile = getDescription();
        currentVersion = Double.valueOf(pdffile.getVersion());
        fileLoadManagement();
        Log.debug("Debug mode is enabled!");
        loadListeners();
        if (iSafeConfig.getISafeConfig().getBoolean("CheckForUpdates") == true) {
            Log.verbose("Checking for updates ..");
            Updater updater = new Updater(this, "blockthattnt", this.getFile(), Updater.UpdateType.NO_DOWNLOAD, false);
            Updater.UpdateResult result = updater.getResult();
            switch(result) {
                case SUCCESS:
                    break;
                case NO_UPDATE:
                    Log.verbose("No update was found.");
                    break;
                case FAIL_DOWNLOAD:
                    break;
                case FAIL_DBO:
                    Log.warning("Failed to contact dev.bukkkit.org!");
                    break;
                case FAIL_NOVERSION:
                    break;
                case FAIL_BADSLUG:
                    break;
                case UPDATE_AVAILABLE:
                    updateFound = true;
                    versionFound = updater.getLatestVersionString();
                    Log.info("########## iSafe update ##########");
                    Log.info("A new version of iSafe was found!");
                    Log.info("New version: " + versionFound);
                    Log.info("Current version running: " + pdffile.getFullName());
                    Log.info("It's highly recommended to update, as there may be important fixes or improvements to the plugin!");
                    Log.info("#####################################");
            }
        } else {
            Log.debug("CheckForUpdates is false in the iSafeConfig.yml, will not check for iSafe updates!");
        }
        getCommand("iSafe").setExecutor(new Commands(this));
        
        checkMatch();
        
        if(iSafeConfig.getISafeConfig().getBoolean("TrackUsageStatistics")) {
            try {
                Log.debug("Starting plugin metrics ..");
                MetricsLite metrics = new MetricsLite(this);
                metrics.start();
            } catch (IOException e) {
                // Failed to submit the stats :-(
            }
        }
        //iSafeExtension.hook();
        
        Blacklists.startDelayTimer();
        
        Log.verbose("v" + pdffile.getVersion() + " enabled.");
        
        Log.debug("iSafe startup finished.");
        isStartup = false;
    }

    private void startSpamTask() {
        Log.debug("Starting spam task ..");
        Log.debug("Running every 20th game tick. (1sec)");
        Log.debug("Note: If the server has TPS lag, the time for each spam loop may go out of sync.");
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

    private void loadListeners() {
        playerListener = new PlayerListener(this);
        blockListener = new BlockListener(this);
        entityListener = new EntityListener(this);
        worldListener = new WorldListener(this);
        weatherListener = new WeatherListener(this);
        inventoryListener = new InventoryListener(this);
        enchantmentListener = new EnchantmentListener(this);
        dropListener = new DropListener(this);
        IPM = new IPManagement(this);
        blacklistClass = new Blacklists(this);
        
        if (iSafeConfig.getISafeConfig().getBoolean("CreateUserFiles")) {
            UFC = new UserFileCreator(this);
        } else {
            Log.debug("CreateUserFiles in the iSafeConfig.yml was disabled, therefor not register the UserFileCreator class.");
        }
        
        if (iSafeConfig.getISafeConfig().getBoolean("CheckForUpdates")) {
            sendUpdate = new SendUpdate(this);
        } else {
            Log.debug("CheckForUpdates in the iSafeConfig.yml was disabled, therefor not registering the sendUpdate class.");
        }
        
        /*if(GMConfig.getConfig().getBoolean("Enabled")) {
            gmManager = new GMManager(this);
        }*/
        
        Log.debug("Registered event classes.");
    }

    public void fileLoadManagement() {
        if(!getDataFolder().exists()) {
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
        
        File usersDir = new File("plugins/iSafe/UserFiles/Users");
        usersDir.mkdirs();
        
        File exaFile = new File(usersDir + File.separator + "_example.yml");
        if(!exaFile.exists()) {
            try {
                FileConfiguration exampFile = YamlConfiguration.loadConfiguration(exaFile);
                exampFile.options().header(Data.setExFileHeader());
                exampFile.set("Username", "example");
                exampFile.set("Displayname", "example");
                exampFile.set("IPAddress", "127.0.0.1");
                exampFile.set("Gamemode", "survival");
                exampFile.set("Level", Integer.parseInt("30"));
                exampFile.save(exaFile);
            } catch (NumberFormatException | IOException e) {
                Log.info("[iSafe] Error creating example user file. (_example.yml)");
                e.printStackTrace();
            }
        }
        
        moveUserFiles();
        
        IPManagement.checkIPLogger();
        
        if (isStartup) {
            /**
            * Any file being dependent on recieving the list of worlds on startup needs to load after a small delay.
            */
            getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
                @Override
                public void run() {
                    CreatureManager.reload();
                    CreatureManager.load();
                    CreatureManager.reload();

                    Blacklist.manageBlacklistDir();
                    Blacklist.loadBlacklists();

                    setupVault();
                }
            }, 40);
        } else {
            CreatureManager.reload();
            CreatureManager.load();
            CreatureManager.reload();
            
            Blacklist.manageBlacklistDir();
            Blacklist.loadBlacklists();
            
            setupVault();
        }
        boolean beastMode = getConfig().getBoolean("AntiCheat/Security.Spam.UseBeastMode");
        boolean normalMode = getConfig().getBoolean("AntiCheat/Security.Spam.UseNormalMode");
        
        if(beastMode) {
            if(normalMode != false) {
                getConfig().set("AntiCheat/Security.Spam.UseNormalMode", false);
                Config.save();
            }
            startSpamTask();
        }
        
        File f = new File("plugins/iSafe/blacklists.yml");
        if(f.exists()) {
            Log.warning("Your iSafe folder contains an outdated blacklist file! (blacklists.yml)");
            Log.warning("iSafe's blacklist system has been changed. Look for a folder called 'Blacklists', from there you will be able to easily play with the new blacklist system =)");
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
}
