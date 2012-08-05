package com.mrmag518.iSafe.Events.EntityEvents;

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



import com.mrmag518.iSafe.*;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleEnterEvent;

public class VehicleListener implements Listener {
    public static iSafe plugin;
    public VehicleListener(iSafe instance)
    {
        plugin = instance;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onVehicleEnter(VehicleEnterEvent event) {
        if (event.isCancelled())
        {
            return;
        }
        
        Entity entity = event.getEntered();
        Vehicle vec = event.getVehicle();
        
        if(vec.getEntityId() == 40) {
            if(plugin.getConfig().getBoolean("Vehicle.DisableEnterMinecarts", true)) {
                if(entity instanceof Player) {
                    Player p = (Player)entity;
                    if(!(plugin.hasPermission(p, "iSafe.use.minecarts"))) {
                        event.setCancelled(true);
                        vec.eject();
                    }
                }
            }
        } else if(vec.getEntityId() == 41) {
            if(plugin.getConfig().getBoolean("Vehicle.DisableEnterBoats", true)) {
                if(entity instanceof Player) {
                    Player p = (Player)entity;
                    if(!(plugin.hasPermission(p, "iSafe.use.boats"))) {
                        event.setCancelled(true);
                        vec.eject();
                    }
                }
            }
        }
    }
}
