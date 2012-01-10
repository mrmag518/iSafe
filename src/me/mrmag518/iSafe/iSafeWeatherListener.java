package me.mrmag518.iSafe;

import org.bukkit.event.weather.LightningStrikeEvent;
import org.bukkit.event.weather.ThunderChangeEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.event.weather.WeatherListener;

public class iSafeWeatherListener extends WeatherListener {
    public static iSafe plugin;
    public iSafeWeatherListener(iSafe instance)
    {
        plugin = instance;
    }
        
    @Override
    public void onLightningStrike(LightningStrikeEvent event) {
        if (event.isCancelled())
        {
            return;
        }
        
        if(plugin.config.getBoolean("Weather.Disable-LightningStrike", true))
        {
           event.setCancelled(true);
        }
    }

    @Override
    public void onThunderChange(ThunderChangeEvent event) {
        if (event.isCancelled())
        {
            return;
        }
        
        if(plugin.config.getBoolean("Weather.Disable-Thunder", true))
        {
            event.toThunderState();
            event.setCancelled(true);
        }
    }

    @Override
    public void onWeatherChange(WeatherChangeEvent event) {
        if (event.isCancelled())
        {
            return;
        }
        
        if(plugin.config.getBoolean("Weather.Disable-Storm", true))
        {
            event.toWeatherState();
            event.setCancelled(true);
        }
    }
}
