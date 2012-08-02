package com.mrmag518.iSafe.Blacklists;

import com.mrmag518.iSafe.iSafe;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class InteractBlacklist implements Listener {
    public static iSafe plugin;
    public InteractBlacklist(iSafe instance)
    {
        plugin = instance;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    
    @EventHandler
    public void InteractBlacklist(PlayerInteractEvent event) {
        if (event.isCancelled())
        {
            return;
        }
        Action action = event.getAction();
        
        if(action == Action.LEFT_CLICK_BLOCK || action == Action.RIGHT_CLICK_BLOCK) {
            Player p = event.getPlayer();
            Block b = event.getClickedBlock();
            
            int blockID = b.getTypeId();
            String BlockNAME = b.getType().name().toLowerCase();
            
            World world = p.getWorld();
            //Location loc = p.getLocation();
            String worldname = world.getName();
            //String pName = p.getName();
            
            final List<Block> ineractedBlock = new ArrayList<Block>();
            
            if (plugin.getBlacklist().getList("Interact.Blacklist", ineractedBlock).contains(blockID)
                || plugin.getBlacklist().getList("Interact.Blacklist", ineractedBlock).contains(BlockNAME.toLowerCase()))
            {
                if(!plugin.hasPermission(p, "iSafe.bypass.blacklist.interact"))
                {
                    if (!event.isCancelled()) 
                    {
                        final List<String> worlds = plugin.getBlacklist().getStringList("Interact.Worlds");
                        
                        if (plugin.getBlacklist().getList("Interact.Worlds", worlds).contains(worldname))
                        {
                            if (plugin.getBlacklist().getBoolean("Interact.Gamemode.PreventFor.Survival", true)) {
                                if(p.getGameMode().equals(GameMode.SURVIVAL)) {
                                    event.setCancelled(true);
                                }
                            } else if (plugin.getBlacklist().getBoolean("Interact.Gamemode.PreventFor.Creative", true)) {
                                if(p.getGameMode().equals(GameMode.CREATIVE)) {
                                    event.setCancelled(true);
                                }
                            }
                            
                            if (plugin.getBlacklist().getBoolean("Interact.KickPlayer", true))
                            {
                                if (event.isCancelled())
                                {
                                    p.kickPlayer(plugin.blacklistInteractKickMsg(b));
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
