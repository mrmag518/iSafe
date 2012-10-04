package com.mrmag518.iSafe;

import org.bukkit.Bukkit;

public class Permissions {
    private static final iSafe plugin = (iSafe) Bukkit.getPluginManager().getPlugin("iSafe");
    
    public static boolean testNode = plugin.getBlacklists().getBoolean("ha");
    
    public static void test() {
        if(plugin.getConfig().get("Haha") == null) {
            plugin.log.info("haha is null, good!");
        }
        plugin.log.warning(testNode + "");
    }
}
