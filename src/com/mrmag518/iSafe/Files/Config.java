package com.mrmag518.iSafe.Files;

import com.mrmag518.iSafe.Util.Data;
import com.mrmag518.iSafe.Util.Log;
import com.mrmag518.iSafe.iSafe;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class Config {
    private static FileConfiguration config = null;
    private static File configFile = null;
    
    public static void load() {
        config = getConfig();
        config.options().header(Data.setConfigHeader());
        
        config.addDefault("ConfigVersion", Double.valueOf(iSafe.ConfigVersion));
        if (config.getDouble("ConfigVersion") != Double.valueOf(iSafe.ConfigVersion)) {
            // If there is anything to modify in the 'new' version, fix that here.
            Log.warning("ConfigVersion was modified! Setting config version to right value ..");
            config.set("ConfigVersion", Double.valueOf(iSafe.ConfigVersion));
        }
        
        config.addDefault("Fire.DisableFireSpread", false);
        config.addDefault("Fire.PreventFlintAndSteelUsage", false);
        config.addDefault("Fire.DisableLavaIgnition", false);
        config.addDefault("Fire.DisableFireballIgnition", false);
        config.addDefault("Fire.DisableLightningIgnition", false);
        config.addDefault("Fire.PreventBlockBurn", false);
        
        config.addDefault("Enchantment.PreventEnchantment", false);
        config.addDefault("Enchantment.PreventCreativeModeEnchanting", false);
        
        config.addDefault("Furnace.DisableFurnaceUsage", false);
        
        config.addDefault("Weather.DisableLightningStrike", false);
        config.addDefault("Weather.DisableThunder", false);
        config.addDefault("Weather.DisableStorm", false);
        
        config.addDefault("World.PreventChunkUnload", false);
        config.addDefault("World.MakeISafeLoadChunks", false);
        config.addDefault("World.DisableStructureGrowth", false);
        config.addDefault("World.PreventBonemealUsage", false);
        config.addDefault("World.DisablePortalGeneration", false);
        config.addDefault("World.DisableExpDrop", false);
        config.addDefault("World.DisableItemSpawn", false);
        config.addDefault("World.EnablePortalCreationPerms", false);
        
        config.addDefault("TreeGrowth.DisableFor.BigTree", false);
        config.addDefault("TreeGrowth.DisableFor.Birch", false);
        config.addDefault("TreeGrowth.DisableFor.BrownMushroom", false);
        config.addDefault("TreeGrowth.DisableFor.Redwood", false);
        config.addDefault("TreeGrowth.DisableFor.RedMushroom", false);
        config.addDefault("TreeGrowth.DisableFor.TallRedwood", false);
        config.addDefault("TreeGrowth.DisableFor.Tree", false);
        config.addDefault("TreeGrowth.DisableFor.Jungle", false);
        
        config.addDefault("Miscellaneous.DisableBlockGrow", false);
        config.addDefault("Miscellaneous.DisableBlockSpreading", false);
        config.addDefault("Miscellaneous.DisableLeavesDecay", false);
        config.addDefault("Miscellaneous.ForceBlocksToBeBuildable", false);
        config.addDefault("Miscellaneous.PreventExpBottleThrow", false);
        config.addDefault("Miscellaneous.ForcePermissionsToUseBed", false);
        config.addDefault("Miscellaneous.ForcePermissionsToFish", false);
        config.addDefault("Miscellaneous.OnlyLetOPsJoin", false);
        config.addDefault("Miscellaneous.DisableHunger", false);
        
        config.addDefault("AntiCheat/Security.LightLevel.PreventFullbright", false);
        config.addDefault("AntiCheat/Security.LightLevel.MinimumLevelBeforeDetection", 1);
        config.addDefault("AntiCheat/Security.LightLevel.CheckCreativeMode", false);
        config.addDefault("AntiCheat/Security.LightLevel.CheckAtNight", true);
        config.addDefault("AntiCheat/Security.KickJoinerIfSameNickIsOnline", false);
        config.addDefault("AntiCheat/Security.Spam.EnableSpamDetector", false);
        config.addDefault("AntiCheat/Security.Spam.MaxLinesPerSecond", 2);
        config.addDefault("AntiCheat/Security.Spam.EnableBypassPermissions", true);
        config.addDefault("AntiCheat/Security.Spam.UseNormalMode", true);
        config.addDefault("AntiCheat/Security.Spam.UseBeastMode", false);
        config.addDefault("AntiCheat/Security.Invisibility.DisablePotionUsage", false);
        config.addDefault("AntiCheat/Security.Invisibility.DisablePotionDispensing", false);
        config.addDefault("AntiCheat/Security.IPManagement.Enabled", false);
        config.addDefault("AntiCheat/Security.IPManagement.LockIpToFirstJoin", false);
        config.addDefault("AntiCheat/Security.IPManagement.AlternateAccounts.EnableChecking", false);
        config.addDefault("AntiCheat/Security.IPManagement.AlternateAccounts.MaxAccountsPerIP", 2);
        config.addDefault("AntiCheat/Security.IPManagement.AlternateAccounts.MaxIPsPerPlayer", 2);
        
        config.addDefault("Explosions.DisablePrimedExplosions", false);
        config.addDefault("Explosions.DisableAllExplosions", false);
        config.addDefault("Explosions.DisableCreeperExplosions", false);
        config.addDefault("Explosions.DisableEnderdragonBlockDamage", false);
        config.addDefault("Explosions.DisableTntExplosions", false);
        config.addDefault("Explosions.DisableFireballExplosions", false);
        config.addDefault("Explosions.DisableEnderCrystalExplosions", false);
        config.addDefault("Explosions.DisableWitherBossExplosions", false);
        config.addDefault("Explosions.PreventFireworksUsage", false);
        config.addDefault("Explosions.DebugExplosions", false);
        
        config.addDefault("Flow.DisableWaterFlow", false);
        config.addDefault("Flow.DisableLavaFlow", false);
        config.addDefault("Flow.DisableAirFlow", false);
        
        config.addDefault("Pistons.DisablePistonExtend", false);
        config.addDefault("Pistons.DisablePistonRetract", false);
        
        config.addDefault("BlockPhysics.DisableSandPhysics", false);
        config.addDefault("BlockPhysics.DisableGravelPhysics", false);
        
        config.addDefault("BlockFade.DisableIceMelting", false);
        config.addDefault("BlockFade.DisableSnowMelting", false);
        
        config.addDefault("ForceDrop.Glass", false);
        config.addDefault("ForceDrop.MobSpawner", false);
        config.addDefault("ForceDrop.Ice", false);
        config.addDefault("ForceDrop.Bedrock", false);
        
        config.addDefault("Buckets.Lava.Prevent", false);
        config.addDefault("Buckets.Lava.CheckedWorlds", Arrays.asList(Data.LavaBucketWorldList));
        Data.LavaBucketWorld = config.getStringList("Buckets.Lava.CheckedWorlds");
        config.addDefault("Buckets.Water.Prevent", false);
        config.addDefault("Buckets.Water.CheckedWorlds", Arrays.asList(Data.WaterBucketWorldList));
        Data.WaterBucketWorld = config.getStringList("Buckets.Water.CheckedWorlds");
        
        config.addDefault("Movement.DisableSprinting", false);
        config.addDefault("Movement.DisableSneaking", false);
        config.addDefault("Movement.PreventCropTrampling", false);
        
        config.addDefault("Gamemode.SwitchToSurvivalOnQuit", false);
        config.addDefault("Gamemode.SwitchToCreativeOnQuit", false);
        config.addDefault("Gamemode.DisableGamemodeChange", false);
        config.addDefault("Gamemode.DisableSurvivalToCreativeChange", false);
        config.addDefault("Gamemode.DisableCreativeToSurvivalChange", false);
        
        config.addDefault("Teleport.DisableAllTeleportCauses", false);
        config.addDefault("Teleport.Disable.CommandCause", false);
        config.addDefault("Teleport.Disable.EnderpearlCause", false);
        config.addDefault("Teleport.Disable.PluginCause", false);
        config.addDefault("Teleport.Disable.UnknownCause", false);
        config.addDefault("Teleport.Disable.NetherportalCause", false);
        config.addDefault("Teleport.Disable.CommandCause", false);
        
        config.addDefault("Chat.ForcePermissionToChat", false);
        config.addDefault("Chat.EnableKickMessages", true);
        config.addDefault("Chat.LogCommands", false);
        
        config.addDefault("VoidFall.TeleportPlayerToSpawn", false);
        config.addDefault("VoidFall.TeleportPlayerBackAndFixHole", true);
        config.addDefault("VoidFall.FixHoleWithGlass", true);
        config.addDefault("VoidFall.FixHoleWithBedrock", false);
        
        config.addDefault("Damage.EnablePermissions", false);
        config.addDefault("Damage.DisableVillagerDamage", false);
        config.addDefault("Damage.DisablePlayerDamage", false);
        config.addDefault("Damage.DisableExplosionDamage", false);
        config.addDefault("Damage.DisableFireDamage", false);
        config.addDefault("Damage.DisableContactDamage", false);
        config.addDefault("Damage.DisableCustomDamage", false);
        config.addDefault("Damage.DisableDrowningDamage", false);
        config.addDefault("Damage.DisableEntityAttackDamage", false);
        config.addDefault("Damage.DisableFallDamage", false);
        config.addDefault("Damage.DisableLavaDamage", false);
        config.addDefault("Damage.DisableLightningDamage", false);
        config.addDefault("Damage.DisableMagicDamage", false);
        config.addDefault("Damage.DisablePoisonDamage", false);
        config.addDefault("Damage.DisableProjectileDamage", false);
        config.addDefault("Damage.DisableStarvationDamage", false);
        config.addDefault("Damage.DisableSuffocationDamage", false);
        config.addDefault("Damage.DisableSuicideDamage", false);
        config.addDefault("Damage.DisableVoidDamage", false);
        
        config.addDefault("HealthRegen.DisableHealthRegeneration", false);
        config.addDefault("HealthRegen.DisableCustomHealthRegen", false);
        config.addDefault("HealthRegen.DisableEatingHealthRegen", false);
        config.addDefault("HealthRegen.DisableNaturalHealthRegen", false);
        config.addDefault("HealthRegen.DisableSatiatedHealthRegen", false);
        config.addDefault("HealthRegen.DisableMagicHealthRegen", false);
        
        getConfig().options().copyDefaults(true);
        save();
    }
    
    public static void reload() {
        if (configFile == null) {
            configFile = new File("plugins/iSafe/config.yml");
        }
        config = YamlConfiguration.loadConfiguration(configFile);
    }
    
    public static FileConfiguration getConfig() {
        if (config == null) {
            reload();
        }
        return config;
    }
    
    public static void save() {
        if (config == null || configFile == null) {
            return;
        }
        try {
            config.save(configFile);
        } catch (IOException ex) {
            Logger.getLogger(JavaPlugin.class.getName()).log(Level.SEVERE, "Could not save configFile to " + configFile, ex);
        }
    }
}
