package com.mrmag518.iSafe.Util;

import java.util.HashSet;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class iSafeExtension {
    
    /*
     * Todo: much.
     */
    
    private static Set<String> extensions = new HashSet<>();
    
    public static void hook() {
        scan();
        if(getExtensionAmount() == 0) {
            Log.verbose("No extensions found.");
        } else {
            Log.verbose(getExtensionAmount() + " extension(s) were found!");
            Log.verbose("Extensions found: " + getEnabledExtensions());
            Log.info("Successfully hooked to " + getExtensionAmount() + " iSafe extensions!");
        }
    }
    
    private static void scan() {
        if(Bukkit.getPluginManager().getPlugin("iSafePvP") != null) {
            addExtension("iSafePvP");
        }
    }
    
    public static void addExtension(String extension) {
        Plugin ePlugin = Bukkit.getPluginManager().getPlugin(extension);
        
        if(ePlugin == null) {
            Log.severe("Tried to to hook into an invalid iSafe extension! (" + extension + ")");
            return;
        }
        
        if(!extension.startsWith("iSafe")) {
            Log.severe("Invalid iSafe extension! (" + extension + ")");
            return;
        }
        
        if(!extensions.contains(extension)) {
            extensions.add(extension);
        }
    }
    
    public static String getEnabledExtensions() {
        if(extensions != null || !extensions.isEmpty()) {
            for(String s : extensions) {
                return s;
            }
        }
        return "none";
    }
    
    public static int getExtensionAmount() {
        return extensions.size();
    }
    
    public static String getExtensionVersion(Plugin extension) {
        if(!extensions.contains(extension.getName())) {
            Log.severe("Tried to hook into an none existing extension!");
            return "null";
        }
        return extension.getDescription().getVersion();
    }
}
