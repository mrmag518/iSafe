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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Data {
    public static iSafe plugin;
    public Data(iSafe instance)
    {
        plugin = instance;
    }
    
    public static List<String> placedblocks = new ArrayList<String>();
    public static String[] placedblockslist = { "No defaults added." };
    public static List<String> worlds = new ArrayList<String>();
    public static String[] worldslist = { "world", "world_nether" };
    
    
    public static List<String> brokenblocks = new ArrayList<String>();
    public static String[] brokenblockslist = { "No defaults added." };
    public static List<String> Breakworlds = new ArrayList<String>();
    public static String[] Breakworldslist = { "world", "world_nether" };
    
    
    public static List<String> dropedblocks = new ArrayList<String>();
    public static String[] dropedblockslist = { "No defaults added." };
    public static List<String> Dropworlds = new ArrayList<String>();
    public static String[] Dropworldslist = { "world", "world_nether" };
    
    
    public static List<String> pickupedblocks = new ArrayList<String>();
    public static String[] pickupedblockslist = { "No defaults added." };
    public static List<String> Pickupworlds = new ArrayList<String>();
    public static String[] Pickupworldslist = { "world", "world_nether" };
    
    
    public static List<String> commands = new ArrayList<String>();
    public static String[] commandslist = { "/nuke" };
    public static List<String> cmdworlds = new ArrayList<String>();
    public static String[] cmdworldlist = { "world", "world_nether" };
    
    
    public static List<String> mobspawnnatural = new ArrayList<String>();
    public static String[] mobspawnnaturallist = { "No defaults added." };
    public static List<String> worlds1 = new ArrayList<String>();
    public static String[] worlds1list = { "world", "world_nether" };
    
    
    public static List<String> mobspawnspawner = new ArrayList<String>();
    public static String[] mobspawnspawnerlist = { "No defaults added." };
    public static List<String> worlds2 = new ArrayList<String>();
    public static String[] worlds2list = { "world", "world_nether" };
    
    
    public static List<String> mobspawncustom = new ArrayList<String>();
    public static String[] mobspawncustomlist = { "No defaults added." };
    public static List<String> worlds3 = new ArrayList<String>();
    public static String[] worlds3list = { "world", "world_nether" };
    
    
    public static List<String> mobspawnegg = new ArrayList<String>();
    public static String[] mobspawnegglist = { "No defaults added." };
    public static List<String> worlds4 = new ArrayList<String>();
    public static String[] worlds4list = { "world", "world_nether" };
    
    
    public static List<String> mobspawnspawneregg = new ArrayList<String>();
    public static String[] mobspawnspawneregglist = { "No defaults added." };
    public static List<String> worlds5 = new ArrayList<String>();
    public static String[] worlds5list = { "world", "world_nether" };
    
    
    public static List<String> censoredWords = new ArrayList<String>();
    public static String[] censoredWordsList = { "No defaults added." };
    
    
    public static List<String> dispensedBlock = new ArrayList<String>();
    public static String[] dispensedBlockList = { "No defaults added." };
    public static List<String> dispenseWorlds = new ArrayList<String>();
    public static String[] dispenseWorldsList = { "world", "world_nether" };
    
    public static List<String> lbEnabledWorlds = new ArrayList<String>();
    public static String[] lbEnabledWorldList = { "world", "world_nether" };
    public static List<String> wbEnabledWorlds = new ArrayList<String>();
    public static String[] wbEnabledWorldList = { "world", "world_nether" };
    
    public static List<String> interactBlacklistedBlocks = new ArrayList<String>();
    public static String[] interactBlacklistedBlocksList = { "No defaults added." };
    public static List<String> interactBlacklistedWorlds = new ArrayList<String>();
    public static String[] interactBlacklistedWorldList = { "world", "world_nether" };
    
    public static String getDate() {
        String date;
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH) + 1;
        date = Integer.toString(month);
        date += "/";
        date += calendar.get(Calendar.DAY_OF_MONTH) + "/";
        date += calendar.get(Calendar.YEAR) + " ";
        date += calendar.get(Calendar.HOUR_OF_DAY) + ":";
        date += calendar.get(Calendar.MINUTE) + ".";
        date += calendar.get(Calendar.SECOND) + "";
        return date;
    }
    
    public static String setConfigHeader() {
        String header = "This is the main configuration file in association to iSafe; take a decent look through it to manage your own preferred settings." 
        + "\nIf you need assistance you can search up it in the iSafe wiki or contact mrmag518."
        + "\nThis file was last modified: " + getDate() + "\n";
        return header;
    }
    
    public static String setBlacklistHeader() {
        String header = "This is the blacklist config on behalf of iSafe, read the iSafe wiki for assistance." 
        + "\nRemember that the world listing is case sensetive."
        + "\nBlacklists related to creatures is found in the creatureManager.yml"
        + "\nThis file was last modified: " + getDate() + "\n";
        return header;
    }
    
    public static String setCreatureManagerHeader() {
        String header = "This is the createManager config associated to creatures in Minecraft."
        + "\nA list of entity IDs can be found at the minercaft wiki, http://www.minecraftwiki.net/wiki/Data_values In the section Entity IDs"
        + "\nThis file was last modified: " + getDate() + "\n";
        return header;
    }
    
    public static String setMessageHeader() {
        String header = "This is the file where you can alter messages sent by iSafe."
        + "\nFor an example the 'No permission.' message."
        + "\nThis file was last modified: " + getDate() + "\n";
        return header;
    }
    
    public static String setExFileHeader() {
        String header = "This is an example file of a User File. Everything is being automatically updated, except the Username."
        + "\n'Username' = The name of the player."
        + "\n'Displayname' = The name displayed in the chat."
        + "\n'IPAddress' = The IP Address the player logged in with."
        + "\n'Gamemode' = The gamemode the player is in."
        + "\n'Level' = The exp level the player has."
        + "\nThis file was last modified: " + getDate() + "\n";
        return header;
    }
    
    public static String setISafeConfigHeader() {
        String header = "This is the configuration file where you can manage settings directly related to iSafe."
        + "\nThis file was last modified: " + getDate() + "\n";
        return header;
    }
}
