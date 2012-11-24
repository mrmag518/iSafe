package com.mrmag518.iSafe.Files;

import com.mrmag518.iSafe.Util.Data;
import com.mrmag518.iSafe.iSafe;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class CreatureManager {
    private static FileConfiguration creatureManager = null;
    private static File creatureManagerFile = null;
    
    private static final Logger log = Logger.getLogger("Minecraft");
    
    // Finally found a method that doesn't return in a NPE!
    private static final iSafe plugin = (iSafe) Bukkit.getPluginManager().getPlugin("iSafe");
    private static File datafolder = plugin.getDataFolder();
    
    public static void load() {
        creatureManager = getCreatureManager();
        creatureManager.options().header(Data.setCreatureManagerHeader());

        creatureManager.addDefault("ConfigVersion", Double.valueOf(iSafe.ConfigVersion));
        if (creatureManager.getDouble("ConfigVersion") != Double.valueOf(iSafe.ConfigVersion)) {
            log.warning("[iSafe] ConfigVersion was modified! Setting config version to right value ..");
            creatureManager.set("ConfigVersion", Double.valueOf(iSafe.ConfigVersion));
        }

        creatureManager.addDefault("Creatures.CreatureTarget.Disable-closest_player-target", false);
        creatureManager.addDefault("Creatures.CreatureTarget.Disable-custom-target", false);
        creatureManager.addDefault("Creatures.CreatureTarget.Disable-forgot_target-target", false);
        creatureManager.addDefault("Creatures.CreatureTarget.Disable-owner_attacked_target-target", false);
        creatureManager.addDefault("Creatures.CreatureTarget.Disable-pig_zombie_target-target", false);
        creatureManager.addDefault("Creatures.CreatureTarget.Disable-random_target-target", false);
        creatureManager.addDefault("Creatures.CreatureTarget.Disable-target_attacked_entity-target", false);
        creatureManager.addDefault("Creatures.CreatureTarget.Disable-target_attacked_owner-target", false);
        creatureManager.addDefault("Creatures.CreatureTarget.Disable-target_died-target", false);

        creatureManager.addDefault("Creatures.PoweredCreepers.DisableLightningCause", false);
        creatureManager.addDefault("Creatures.PoweredCreepers.DisableSetOffCause", false);
        creatureManager.addDefault("Creatures.PoweredCreepers.DisableSetOnCause", false);

        creatureManager.addDefault("Creatures.Endermen.PreventEndermenGriefing", false);
        creatureManager.addDefault("Creatures.Tame.DisableTaming", false);
        creatureManager.addDefault("Creatures.Slime.DisableSlimeSplit", false);
        creatureManager.addDefault("Creatures.Pig.DisabletPigZap", false);
        creatureManager.addDefault("Creatures.Zombie.DisableDoorBreak", false);
        creatureManager.addDefault("Creatures.Death.DisableDrops", false);
        creatureManager.addDefault("Creatures.DisableCropTrampling", false);

        creatureManager.addDefault("Creatures.SheepDyeWool.TotallyDisable", false);
        creatureManager.addDefault("Creatures.SheepDyeWool.DisableColor.Black", false);
        creatureManager.addDefault("Creatures.SheepDyeWool.DisableColor.Blue", false);
        creatureManager.addDefault("Creatures.SheepDyeWool.DisableColor.Brown", false);
        creatureManager.addDefault("Creatures.SheepDyeWool.DisableColor.Cyan", false);
        creatureManager.addDefault("Creatures.SheepDyeWool.DisableColor.Gray", false);
        creatureManager.addDefault("Creatures.SheepDyeWool.DisableColor.Green", false);
        creatureManager.addDefault("Creatures.SheepDyeWool.DisableColor.Light_Blue", false);
        creatureManager.addDefault("Creatures.SheepDyeWool.DisableColor.Lime", false);
        creatureManager.addDefault("Creatures.SheepDyeWool.DisableColor.Magenta", false);
        creatureManager.addDefault("Creatures.SheepDyeWool.DisableColor.Orange", false);
        creatureManager.addDefault("Creatures.SheepDyeWool.DisableColor.Pink", false);
        creatureManager.addDefault("Creatures.SheepDyeWool.DisableColor.Purple", false);
        creatureManager.addDefault("Creatures.SheepDyeWool.DisableColor.Red", false);
        creatureManager.addDefault("Creatures.SheepDyeWool.DisableColor.Silver", false);
        creatureManager.addDefault("Creatures.SheepDyeWool.DisableColor.White", false);
        creatureManager.addDefault("Creatures.SheepDyeWool.DisableColor.Yellow", false);

        creatureManager.addDefault("Creatures.Combusting.DisableFor-allCreatures", false);
        creatureManager.addDefault("Creatures.Combusting.DisableFor.Giant", false);
        creatureManager.addDefault("Creatures.Combusting.DisableFor.PigZombie", false);
        creatureManager.addDefault("Creatures.Combusting.DisableFor.Skeleton", false);
        creatureManager.addDefault("Creatures.Combusting.DisableFor.Zombie", false);

        creatureManager.addDefault("Creatures.Damage.DisableFireDamage", false);
        creatureManager.addDefault("Creatures.Damage.DisableContactDamage", false);
        creatureManager.addDefault("Creatures.Damage.DisableCustomDamage", false);
        creatureManager.addDefault("Creatures.Damage.DisableDrowningDamage", false);
        creatureManager.addDefault("Creatures.Damage.DisableEntityAttackDamage", false);
        creatureManager.addDefault("Creatures.Damage.DisableFallDamage", false);
        creatureManager.addDefault("Creatures.Damage.DisableLavaDamage", false);
        creatureManager.addDefault("Creatures.Damage.DisableLightningDamage", false);
        creatureManager.addDefault("Creatures.Damage.DisableMagicDamage", false);
        creatureManager.addDefault("Creatures.Damage.DisablePoisonDamage", false);
        creatureManager.addDefault("Creatures.Damage.DisableProjectileDamage", false);
        creatureManager.addDefault("Creatures.Damage.DisableStarvationDamage", false);
        creatureManager.addDefault("Creatures.Damage.DisableSuffocationDamage", false);
        creatureManager.addDefault("Creatures.Damage.DisableSuicideDamage", false);
        creatureManager.addDefault("Creatures.Damage.DisableVoidDamage", false);

        String defCreatures = "50,53,63";

        for (World world : Bukkit.getServer().getWorlds()) {
            String worldname = world.getName();

            String naturalBL = "MobSpawn." + worldname + ".Natural.Blacklist";
            String spawnerBL = "MobSpawn." + worldname + ".Spawner.Blacklist";
            String customBL = "MobSpawn." + worldname + ".Custom.Blacklist";
            String spawnereggBL = "MobSpawn." + worldname + ".SpawnerEgg.Blacklist";
            String chunkgenBL = "MobSpawn." + worldname + ".ChunkGen.Blacklist";
            String breedingBL = "MobSpawn." + worldname + ".Breeding.Blacklist";
            String state = "MobSpawn." + worldname + ".Enabled";

            creatureManager.addDefault(state, false);
            creatureManager.addDefault("MobSpawn." + worldname + ".Natural.Debug.ToConsole", true);
            creatureManager.addDefault(naturalBL, defCreatures);

            creatureManager.addDefault(state, false);
            creatureManager.addDefault("MobSpawn." + worldname + ".Spawner.Debug.ToConsole", true);
            creatureManager.addDefault(spawnerBL, defCreatures);

            creatureManager.addDefault(state, false);
            creatureManager.addDefault("MobSpawn." + worldname + ".Custom.Debug.ToConsole", true);
            creatureManager.addDefault(customBL, defCreatures);

            creatureManager.addDefault(state, false);
            creatureManager.addDefault("MobSpawn." + worldname + ".SpawnerEgg.Debug.ToConsole", true);
            creatureManager.addDefault(spawnereggBL, defCreatures);

            creatureManager.addDefault(state, false);
            creatureManager.addDefault("MobSpawn." + worldname + ".ChunkGen.Debug.ToConsole", true);
            creatureManager.addDefault(chunkgenBL, defCreatures);

            creatureManager.addDefault(state, false);
            creatureManager.addDefault("MobSpawn." + worldname + ".Breeding.Debug.ToConsole", true);
            creatureManager.addDefault(breedingBL, defCreatures);
        }

        getCreatureManager().options().copyDefaults(true);
        save();
    }
    
    public static void reload() {
        if (creatureManagerFile == null) {
            creatureManagerFile = new File(datafolder, "creatureManager.yml");
        }
        creatureManager = YamlConfiguration.loadConfiguration(creatureManagerFile);

        InputStream defConfigStream = plugin.getResource("creatureManager.yml");
        if (defConfigStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            creatureManager.setDefaults(defConfig);
        }
    }

    public static FileConfiguration getCreatureManager() {
        if (creatureManager == null) {
            reload();
        }
        return creatureManager;
    }

    public static void save() {
        if (creatureManager == null || creatureManagerFile == null) {
            return;
        }
        try {
            creatureManager.save(creatureManagerFile);
        } catch (IOException ex) {
            Logger.getLogger(JavaPlugin.class.getName()).log(Level.SEVERE, "Error saving creatureManager to " + creatureManagerFile, ex);
        }
    }
}
