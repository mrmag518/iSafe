package me.mrmag518.iSafe.Events;

import java.util.List;
import me.mrmag518.iSafe.*;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleDestroyEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;

public class VehicleListener implements Listener {
    public VehicleListener() {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    
    public static iSafe plugin;
    public VehicleListener(iSafe instance)
    {
        plugin = instance;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onVehicleEnter(VehicleEnterEvent event) {
        if (event.isCancelled())
        {
            return;
        }
        
        Entity entity = event.getEntered();
        Player player = (Player) entity;
        Vehicle vec = event.getVehicle();
        
           if(plugin.getConfig().getBoolean("Vehicle.Prevent.enter.Minecarts", true))
           {
                if(player.hasPermission("iSafe.vehicle.enter.minecart")) {
                    //access
                } else {
                   if(vec.getEntityId() == 40) {
                       if (entity instanceof LivingEntity) {
                           if (entity instanceof Player) {
                                event.setCancelled(true);
                                player.sendMessage(ChatColor.RED + "You do not have access to enter that vehicle");
                            }
                        }
                    }
                }
            }
            if(plugin.getConfig().getBoolean("Vehicle.Prevent.enter.Boats", true))
            {
                if(player.hasPermission("iSafe.vehicle.enter.boat")) {
                    //access
                } else {
                    if(vec.getEntityId() == 41) {
                        if (entity instanceof LivingEntity) {
                            if (entity instanceof Player) {
                                event.setCancelled(true);
                                player.sendMessage(ChatColor.RED + "You do not have access to enter that vehicle");
                            }
                        }
                    }
                }
            }
        }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onVehicleDestroy(VehicleDestroyEvent event) {
        if (event.isCancelled())
        {
            return;
        }
        
        Entity entity = event.getAttacker();
        Vehicle vec = event.getVehicle();
        Location loc = vec.getLocation();
        Player player = (Player) entity;
        
        if(plugin.getConfig().getBoolean("Vehicle.Prevent.destroy.Minecarts", true))
        {
            if(player.hasPermission("iSafe.vehicle.destory.minecart")) {
                //access
            } else {
                if(vec.getEntityId() == 40) {
                    if (entity instanceof LivingEntity) {
                        if (entity instanceof Player) {
                            event.setCancelled(true);
                            player.sendMessage(ChatColor.RED + "You do not have access to destory that vehicle");
                        }
                    }
                }
            }
        }
            
        if(plugin.getConfig().getBoolean("Vehicle.Prevent.destroy.Boats", true))
        {
            if(player.hasPermission("iSafe.vehicle.destory.boat")) {
                //access
            } else {
                if(vec.getEntityId() == 41) {
                    if (entity instanceof LivingEntity) {
                        if (entity instanceof Player) {
                            event.setCancelled(true);
                            player.sendMessage(ChatColor.RED + "You do not have access to destory that vehicle");
                        }
                    }
                }
            }
        }
    }
}
