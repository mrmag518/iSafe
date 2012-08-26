package com.mrmag518.iSafe.Blacklists;

import com.mrmag518.iSafe.iSafe;
import org.bukkit.GameMode;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.Recipe;

public class CraftBlacklist implements Listener {
    public static iSafe plugin;
    public CraftBlacklist(iSafe instance)
    {
        plugin = instance;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    
    @EventHandler
    public void CraftBlacklist(CraftItemEvent event) {
        if (event.isCancelled()){
            return;
        }
        Recipe rec = event.getRecipe();
        HumanEntity he = event.getWhoClicked();
        String worldname = he.getWorld().getName();
        int recID = rec.getResult().getTypeId();
        String RecNAME = rec.getResult().getType().name().toLowerCase();
        Player p = null;
        
        // Check if the HumanEntity is a Player. (of course he is ..)
        if(he instanceof Player) {
            plugin.debugLog("[CraftBlacklist]" + he.getName() + " is a Player.");
            p = (Player)he;
        } else {
            plugin.debugLog("[CraftBlacklist]" + he.getName() + " is not a Player. say wut?!");
            return;
        }

        if(plugin.getBlacklist().getList("Crafting.Blacklist").contains(recID)
            || plugin.getBlacklist().getList("Crafting.Blacklist").contains(RecNAME.toLowerCase())) 
        {
            if(!plugin.hasBlacklistPermission(p, "iSafe.bypass.blacklist.crafting")) 
            {
                if(plugin.getBlacklist().getList("Crafting.EnabledWorlds").contains(worldname)) 
                {
                    if (plugin.getBlacklist().getBoolean("Crafting.Gamemode.PreventFor.Survival", true)) {
                        if(he.getGameMode().equals(GameMode.SURVIVAL)) {
                            event.setCancelled(true);
                        }
                    } 

                    if (plugin.getBlacklist().getBoolean("Crafting.Gamemode.PreventFor.Creative", true)) {
                        if(he.getGameMode().equals(GameMode.CREATIVE)) {
                            event.setCancelled(true);
                        }
                    }

                    if (plugin.getBlacklist().getBoolean("Crafting.Alert/log.ToConsole", true)){
                        if (event.isCancelled()) {
                            plugin.log.info("[iSafe]" + he.getName() + " was prevented from crafting the blacklisted recipe: " + RecNAME);
                        }
                    }

                    if (plugin.getBlacklist().getBoolean("Crafting.Alert/log.ToPlayer", true)){
                        if (event.isCancelled()) {
                            p.sendMessage(plugin.blacklistCraftingMsg(RecNAME));
                        }
                    }
                }
            }
        }
    }
}
