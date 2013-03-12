package com.mrmag518.iSafe.Util;

import com.mrmag518.iSafe.Files.Blacklist;
import com.mrmag518.iSafe.Files.Messages;
import com.mrmag518.iSafe.iSafe;

import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class Eco {
    public static void withdraw(String player, World world, double amount) {
        if(amount < 1) {
            return;
        }
        double playerBal = iSafe.economy.getBalance(player);
        FileConfiguration blacklist = Blacklist.getBlacklist(world.getName());
        
        if(amount > playerBal) {
            if(blacklist.getBoolean("Events.Place.Economy.AllowNegativeCashPile")) {
                iSafe.economy.withdrawPlayer(player, amount);
            }
        } else {
            iSafe.economy.withdrawPlayer(player, amount);
        }
    }
    
    public static void sendEcoNotify(Player p, String blacklist, int amount) {
        p.sendMessage(Messages.colorize(Messages.getMessages().getString("Blacklists." + blacklist + ".EcoMessage").replace("%amount%", String.valueOf(amount))));
    }
}
