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
    
    
    public static List<String> PlaceBlacklist = new ArrayList<String>();
    public static String[] PlaceBlacklistList = { "No defaults added." };
    public static List<String> PlaceBlacklistWorld = new ArrayList<String>();
    public static String[] PlaceBlacklistWorldList = { "world", "world_nether" };
    
    
    public static List<String> BreakBlacklist = new ArrayList<String>();
    public static String[] BreakBlacklistList = { "No defaults added." };
    public static List<String> BreakBlacklistWorld = new ArrayList<String>();
    public static String[] BreakBlacklistWorldList = { "world", "world_nether" };
    
    
    public static List<String> DropBlacklist = new ArrayList<String>();
    public static String[] DropBlacklistList = { "No defaults added." };
    public static List<String> DropBlacklistWorld = new ArrayList<String>();
    public static String[] DropBlacklistWorldList = { "world", "world_nether" };
    
    
    public static List<String> PickupBlacklist = new ArrayList<String>();
    public static String[] PickupBlacklistList = { "No defaults added." };
    public static List<String> PickupBlacklistWorld = new ArrayList<String>();
    public static String[] PickupBlacklistWorldList = { "world", "world_nether" };
    
    
    public static List<String> CmdBlacklist = new ArrayList<String>();
    public static String[] CmdBlacklistList = { "/nuke" };
    public static List<String> CmdBlacklistWorld = new ArrayList<String>();
    public static String[] CmdBlacklistWorldList = { "world", "world_nether" };
    
    
    public static List<String> NaturalMSBlacklist = new ArrayList<String>();
    public static String[] NaturalMSBlacklistList = { "No defaults added." };
    public static List<String> NaturalMSBlacklistWorld = new ArrayList<String>();
    public static String[] NaturalMSBlacklistWorldList = { "world", "world_nether" };
    
    
    public static List<String> SpawnerMSBlacklist = new ArrayList<String>();
    public static String[] SpawnerMSBlacklistList = { "No defaults added." };
    public static List<String> SpawnerMSBlacklistWorld = new ArrayList<String>();
    public static String[] SpawnerMSBlacklistWorldList = { "world", "world_nether" };
    
    
    public static List<String> CustomMSBlacklist = new ArrayList<String>();
    public static String[] CustomMSBlacklistList = { "No defaults added." };
    public static List<String> CustomMSBlacklistWorld = new ArrayList<String>();
    public static String[] CustomMSBlacklistWorldList = { "world", "world_nether" };
    
    
    public static List<String> SpawnerEggMSBlacklist = new ArrayList<String>();
    public static String[] SpawnerEggMSBlacklistList = { "creeper" };
    public static List<String> SpawnerEggMSBlacklistWorld = new ArrayList<String>();
    public static String[] SpawnerEggMSBlacklistWorldList = { "world", "world_nether" };
    
    
    public static List<String> ChunkGenMSBlacklist = new ArrayList<String>();
    public static String[] ChunkGenMSBlacklistList = { "creeper" };
    public static List<String> ChunkGenMSBlacklistWorld = new ArrayList<String>();
    public static String[] ChunkGenMSBlacklistWorldList = { "world", "world_nether" };
    
    
    public static List<String> BreedingMSBlacklist = new ArrayList<String>();
    public static String[] BreedingMSBlacklistList = { "creeper" };
    public static List<String> BreedingMSBlacklistWorld = new ArrayList<String>();
    public static String[] BreedingMSBlacklistWorldList = { "world", "world_nether" };
    
    
    public static List<String> WordBlacklist = new ArrayList<String>();
    public static String[] WordBlacklistList = { "No defaults added." };
    
    
    public static List<String> DispenseBlacklist = new ArrayList<String>();
    public static String[] DispenseBlacklistList = { "No defaults added." };
    public static List<String> DispenseBlacklistWorld = new ArrayList<String>();
    public static String[] DispenseBlacklistWorldList = { "world", "world_nether" };
    
    
    public static List<String> LavaBucketWorld = new ArrayList<String>();
    public static String[] LavaBucketWorldList = { "world", "world_nether" };
    public static List<String> WaterBucketWorld = new ArrayList<String>();
    public static String[] WaterBucketWorldList = { "world", "world_nether" };
    
    
    public static List<String> InteractBlacklist = new ArrayList<String>();
    public static String[] InteractBlacklistList = { "No default added." };
    public static List<String> InteractBlacklistWorld = new ArrayList<String>();
    public static String[] InteractBlacklistWorldList = { "world", "world_nether" };
    
    
    public static List<String> CraftBlacklist = new ArrayList<String>();
    public static String[] CraftBlacklistList = { "No default added." };
    public static List<String> CraftBlacklistWorld = new ArrayList<String>();
    public static String[] CraftBlacklistWorldList = { "world", "world_nether" };
    
    
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
