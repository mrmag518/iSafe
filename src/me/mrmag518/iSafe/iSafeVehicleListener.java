package me.mrmag518.iSafe;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.vehicle.VehicleDestroyEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.event.vehicle.VehicleListener;

public class iSafeVehicleListener extends VehicleListener {
    public static iSafe plugin;
    public iSafeVehicleListener(iSafe instance)
    {
        plugin = instance;
    }

    @Override
    public void onVehicleEnter(VehicleEnterEvent event) {
        if (event.isCancelled())
        {
            return;
        }
        
        Entity entity = event.getEntered();
        Vehicle vec = event.getVehicle();
        Location loc = vec.getLocation();
        Player player = (Player) entity;
        
        if(plugin.config.getBoolean("Vehicle.Prevent.enter.Minecarts", true))
        {
            if(player.hasPermission("iSafe.vehicle.enter.minecart")) {
                //access
            } else {
                if(vec.getEntityId() == 40) {
                    event.setCancelled(true);
                    player.sendMessage(ChatColor.RED + "You do not have access to enter that vehicle");
            }
        }
        if(plugin.config.getBoolean("Vehicle.Prevent.enter.Boats", true))
        {
            if(player.hasPermission("iSafe.vehicle.enter.boat")) {
                //access
            } else {
                if(vec.getEntityId() == 41) {
                    event.setCancelled(true);
                    player.sendMessage(ChatColor.RED + "You do not have access to enter that vehicle");
                }
            }
        }
        }
    }

    @Override
    public void onVehicleDestroy(VehicleDestroyEvent event) {
        if (event.isCancelled())
        {
            return;
        }
        
        Entity entity = event.getAttacker();
        Vehicle vec = event.getVehicle();
        Location loc = vec.getLocation();
        Player player = (Player) entity;
        
        if(plugin.config.getBoolean("Vehicle.Prevent.destroy.Minecarts", true))
        {
            if(player.hasPermission("iSafe.vehicle.destory.minecart")) {
                //access
            } else {
                if(vec.getEntityId() == 40) {
                    event.setCancelled(true);
                    player.sendMessage(ChatColor.RED + "You do not have access to destory that vehicle");
                }
            }
        }
        if(plugin.config.getBoolean("Vehicle.Prevent.destroy.Boats", true))
        {
            if(player.hasPermission("iSafe.vehicle.destory.boat")) {
                //access
            } else {
                if(vec.getEntityId() == 41) {
                    event.setCancelled(true);
                    player.sendMessage(ChatColor.RED + "You do not have access to destory that vehicle");
                }
            }
        }
    }
}
