package me.mrmag518.iSafe.Commands;

import java.util.List;
import me.mrmag518.iSafe.iSafe;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class Slayall implements CommandExecutor {
    public static iSafe plugin;
    public Slayall(iSafe instance)
    {
        plugin = instance;
    }
    int message = 0;
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){  
         if(cmd.getName().equalsIgnoreCase("slayall")){
             if (args.length > 0) {
                 sender.sendMessage(ChatColor.RED + "To many arguments!");
                 return false;
             }
              if (sender instanceof Player) {
                  Player player = (Player)sender;
                  World world = player.getWorld();
                  if (hasSlayall(player)) { //player
                      killAllEntities(player, world, player);
                  } else {
                     player.sendMessage(ChatColor.RED + "You do not have access to that.");
                  }
             } else {
                  sender.sendMessage("You cannot do that from here.");
             }
             return true;
        }
         return false;
    }
    
    public void killAllEntities(Entity ents, World world, Player player) {
        List<LivingEntity> mobsList = world.getLivingEntities();
        
        for(LivingEntity mobs : mobsList) {
            if (mobs instanceof Player) {
                //nothing
            } else {
                mobs.setHealth(0);
                if (message == 0) {
                    player.sendMessage(ChatColor.AQUA + "Killed all mobs in the world.");
                    message = 1;
                }
            }
        }
    }
    
    public boolean hasSlayall(Player player) {
        if (player.hasPermission("iSafe.slayall")) {
            return true;
        } else if (player.hasPermission("iSafe.*")) {
            return true;
        }
        return false;
    }
}
