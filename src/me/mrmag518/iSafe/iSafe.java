package me.mrmag518.iSafe;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.entity.EntityListener;
import org.bukkit.event.inventory.InventoryListener;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.vehicle.VehicleListener;
import org.bukkit.event.weather.WeatherListener;
import org.bukkit.event.world.WorldListener;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class iSafe extends JavaPlugin {
    //Read the listener classes
    private BlockListener blockListener = new iSafeBlockListener(this);
    private EntityListener entityListener = new iSafeEntityListener(this);
    private PlayerListener playerListener = new iSafePlayerListener(this);
    private WeatherListener weatherListener = new iSafeWeatherListener(this);
    private InventoryListener inventoryListener = new iSafeInventoryListener(this);
    private VehicleListener vehicleListener = new iSafeVehicleListener(this);
    private WorldListener worldListener = new iSafeWorldListener(this);
    //Read the CommandExecutor class
    private iSafeCommandExecutor CmdExecutor;
    //this is a plugin right?
    public static iSafe plugin;
    //The logger
    public final Logger log = Logger.getLogger("Minecraft");
    //Configuration
    public FileConfiguration config;
    public Boolean configBoolean;
    public String configString;
    public Integer configInt;
    //InstantBreak HashMap
    public Set<Player> superbreak = new HashSet<Player>();
        
    //iSafe disable
    @Override
    public void onDisable() {
        PluginDescriptionFile pdffile = this.getDescription();
        log.info("[iSafe] " + pdffile.getFullName() + " disabled succesfully.");
    }
    
    //iSafe enable (not messy at all, right?)
    @Override
    public void onEnable() {
        PluginDescriptionFile pdffile = this.getDescription();
                //Configuration
                this.getConfig().options().copyDefaults(true);
                config = getConfig();
                //Buckets
                config.addDefault("Buckets.Allow-LavaBucket-empty", false);
                config.addDefault("Buckets.Allow-WaterBucket-empty", true);
                //Entity
                config.addDefault("Entity-Damage.Disable-Void-damage", false);
                config.addDefault("Entity-Damage.Disable-Lightning-damage", false);
                config.addDefault("Entity-Damage.Disable-Fall-damage", false);
                config.addDefault("Entity-Damage.Disable-Suffocation-damage", false);
                config.addDefault("Entity-Damage.Disable-Drowning-damage", false);
                config.addDefault("Entity-Damage.Disable-Lava-damage", false);
                config.addDefault("Entity-Damage.Disable-Contact-damage", false);
                config.addDefault("Entity-Damage.Disable-Projectile-damage", false);
                config.addDefault("Entity-Damage.Disable-Starvation-damage", false);
                config.addDefault("Entity-Damage.Disable-Suicide-damage", false);
                config.addDefault("Entity-Damage.Disable-Entity_Attack-damage", false);
                config.addDefault("Entity-Damage.Disable-Custom-damage", false);
                //Explosions
                config.addDefault("Explosions.Prevent-primed-explosions", false);
                config.addDefault("Explosions.Prevent-explosions", false);
                config.addDefault("Explosions.Prevent-Creeper-explosions", false);
                config.addDefault("Explosions.Prevent-EnderDragon-blockdamage", false);
                config.addDefault("Explosions.Prevent-TNT-explosions", false);
                config.addDefault("Explosions.Prevent-Fireball-explosions", false);
                //Enviroment-Damage
                config.addDefault("Enviroment-Damage.Allow-Fire-spread", false);
                config.addDefault("Enviroment-Damage.Allow-Flint_and_steel-usage", false);
                config.addDefault("Enviroment-Damage.Allow-Enviroment-ignition", false);
                //Chat
                config.addDefault("Chat.Enable-Chat-permissions", false);
                //Breaking (rewritte soon)
                config.addDefault("Breaking.Allow-TNT-breaking", false);
                config.addDefault("Breaking.Allow-MobSpawner-breaking", false);
                config.addDefault("Breaking.Allow-Obsidian-breaking", true);
                config.addDefault("Breaking.Allow-Pistons-breaking", true);
                config.addDefault("Breaking.Allow-Sponge-breaking", true);
                config.addDefault("Breaking.Allow-Ice-breaking", true);
                config.addDefault("Breaking.Allow-SoulSand-breaking", true);
                //Placement
                config.addDefault("Placement.Allow-TNT-placement", false);
                config.addDefault("Placement.Allow-MobSpawner-placement", false);
                config.addDefault("Placement.Allow-Bedrock-placement", true);
                config.addDefault("Placement.Allow-FireBlock-placement", false);
                config.addDefault("Placement.Allow-Obsidian-placement", true);
                config.addDefault("Placement.Allow-Water-blocks", true);
                config.addDefault("Placement.Allow-Lava-blocks", false);
                config.addDefault("Placement.Allow-Pistons-placement", true);
                config.addDefault("Placement.Allow-Sponge-placement", true);
                config.addDefault("Placement.Allow-Ice-placement", true);
                config.addDefault("Placement.Allow-un_natural_portal-placement", false);
                config.addDefault("Placement.Allow-SoulSand-placement", true);
                //Drops
                config.addDefault("Drops.Allow-TNT-drop", false);
                config.addDefault("Drops.Allow-DiamonBlock-drop", true);
                config.addDefault("Drops.Allow-IronBlock-drop", true);
                config.addDefault("Drops.Allow-GoldBlock-drop", true);
                config.addDefault("Drops.Allow-Dimondore-drop", true);
                config.addDefault("Drops.Allow-Bedrock-drop", true);
                config.addDefault("Drops.Allow-un_natural_portal-drop", false);
                config.addDefault("Drops.Allow-Diamondingot-drop", true);
                config.addDefault("Drops.Allow-droping", true);
                //Flow
                config.addDefault("Flow.Disable-water-flow", false);
                config.addDefault("Flow.Disable-lava-flow", false);
                //Piston
                config.addDefault("Piston.Prevent-piston-Extend", false);
                config.addDefault("Piston.Prevent-piston-Retract", false);
                //Weather
                config.addDefault("Weather.Disable-LightningStrike", false);
                config.addDefault("Weather.Disable-Storm", false);
                config.addDefault("Weather.Disable-Thunder", false);
                //Furnace
                config.addDefault("Furnace.Disable-furnace-burning", false);
                config.addDefault("Furnace.Disable-furnace-smelting", false);
                //Misc
                config.addDefault("Misc.Enable-kick-messages", true);
                config.addDefault("Misc.Disable-LeavesDecay", false);
                config.addDefault("Misc.Prevent-crop-trampling", true);
                config.addDefault("Misc.Prevent-portal-creation", false);
                //Pickup (un-finished code)
                config.addDefault("Pickup.Prevent-item-pickup", false);
                //Mobs (needs a little more cleanup)
                config.addDefault("Mobs.Spawn.Allow-Slime-spawn", true);
                config.addDefault("Mobs.Spawn.Allow-Ghast-spawn", true);
                config.addDefault("Mobs.Spawn.Allow-Zombie-spawn", true);
                config.addDefault("Mobs.Spawn.Allow-Creeper-spawn", true);
                config.addDefault("Mobs.Spawn.Allow-Skeleton-spawn", true);
                config.addDefault("Mobs.Spawn.Allow-Enderman-spawn", true);
                config.addDefault("Mobs.Spawn.Allow-Giant-spawn", true);
                config.addDefault("Mobs.Enderman-grief.Prevent-Enderman-Pickup", false);
                config.addDefault("Mobs.Enderman-grief.Prevent-Enderman-Place", false);
                config.addDefault("Mobs.Spawn.Allow-Silverfish-spawn", true);
                config.addDefault("Mobs.Spawn.Allow-PigZombie-spawn", true);
                config.addDefault("Mobs.Spawn.Allow-Spider-spawn", true);
                config.addDefault("Mobs.Spawn.Allow-Squid-spawn", true);
                config.addDefault("Mobs.Spawn.Allow-Wolf-spawn", true);
                config.addDefault("Mobs.Allow-SlimeSplit", true);
                config.addDefault("Mobs.Spawn.Allow-Pig-spawn", true);
                config.addDefault("Mobs.Spawn.Allow-Cow-spawn", true);
                config.addDefault("Mobs.Spawn.Allow-EnderDragon-spawn", true);
                config.addDefault("Mobs.Spawn.Allow-Sheep-spawn", true);
                config.addDefault("Mobs.EntityTarget.Disable-closest_player-target", false);
                config.addDefault("Mobs.EntityTarget.Disable-custom-target", false);
                config.addDefault("Mobs.EntityTarget.Disable-forgot_target-target", false);
                config.addDefault("Mobs.EntityTarget.Disable-owner_attacked_target-target", false);
                config.addDefault("Mobs.EntityTarget.Disable-pig_zombie_target-target", false);
                config.addDefault("Mobs.EntityTarget.Disable-random_target-target", false);
                config.addDefault("Mobs.EntityTarget.Disable-target_attacked_entity-target", false);
                config.addDefault("Mobs.EntityTarget.Disable-target_attacked_owner-target", false);
                config.addDefault("Mobs.EntityTarget.Disable-target_died-target", false);
                config.addDefault("Mobs.Prevent-PigZap(Pig transformation to ZombiePig)", true);
                config.addDefault("Mobs.Spawn.Allow-Blaze-spawn", true);
                config.addDefault("Mobs.Spawn.Allow-MagmaCube-spawn", true);
                config.addDefault("Mobs.Spawn.Allow-Chicken-spawn", true);
                config.addDefault("Mobs.Spawn.Allow-Monster/human-spawn", true);
                config.addDefault("Mobs.Spawn.Allow-MuchroomCow-spawn", true);
                config.addDefault("Mobs.Spawn.Allow-Snowman-spawn", true);
                config.addDefault("Mobs.Spawn.Allow-Squid-spawn", true);
                config.addDefault("Mobs.Spawn.Allow-Villager-spawn", true);
                config.addDefault("Mobs.Powered-Creepers.Prevent-PowerCause.Lightning", false);
                config.addDefault("Mobs.Powered-Creepers.Prevent-PowerCause.Set-Off", false);
                config.addDefault("Mobs.Powered-Creepers.Prevent-PowerCause.Set-On", false);
                config.addDefault("Mobs.Prevent-Object-drop-on-death", false);
                config.addDefault("Mobs.Prevent-Entity-Combust", false);
                config.addDefault("Mobs.Tame.Prevent-taming", false);
                //Player
                config.addDefault("Player.Allow-Sprinting", true);
                config.addDefault("Player.Allow-Sneaking", true);
                config.addDefault("Player.Enable-fishing-permissions", false);
                config.addDefault("Player.Broadcast-iSafe-message-on-join", true);
                config.addDefault("Player.Allow-creative-gamemode-on-player-quit", true);
                config.addDefault("Player.Disable-Hunger", false);
                config.addDefault("Player.Allow-Teleporting-without-iSafe-permissions", true);
                config.addDefault("Player.Enable-Bed-permissions", false);
                config.addDefault("Player.Enable-fishing-permissions", false);
                config.addDefault("Player.Interact.Allow-Buttons-Interact", true);
                config.addDefault("Player.Interact.Allow-WoodenDoors-Interact", true);
                config.addDefault("Player.Interact.Allow-IronDoors-Interact", true);
                config.addDefault("Player.Interact.Allow-Levers-Interact", true);
                config.addDefault("Player.Interact.Allow-StonePressurePlate-Interact", true);
                config.addDefault("Player.Interact.Allow-WoodenPressurePlate-Interact", true);
                config.addDefault("Player.Interact.Allow-TrapDoor-Interact", true);
                config.addDefault("Player.Interact.Allow-WoodenFenceGate-Interact", true);
                config.addDefault("Player.Interact.Allow-Chest-Interact", true);
                config.addDefault("Player.Only-let-OPs-join", false);
                config.addDefault("Player.Log-commands", true);
                config.addDefault("Player.Disable-all-commands", false);
                config.addDefault("Player.Entity/Player.Completely-Prevent.Health-Regeneration", false);
                config.addDefault("Player.Entity/Player.Prevent.Custom-Health-Regeneration", false);
                config.addDefault("Player.Entity/Player.Prevent.Eating-Health-Regeneration", false);
                config.addDefault("Player.Entity/Player.Prevent.Regen-Health-Regeneration", false);
                config.addDefault("Player.Entity/Player.Prevent.Satiated-Health-Regeneration", false);
                config.addDefault("PlayerInteractEntity.Prevent-snowball-hitting-player", false);
                config.addDefault("PlayerInteractEntity.Prevent-arrow-hitting-player", false);
                config.addDefault("Player.Prevent-Gamemode-change", false);
                //World
                config.addDefault("World.Disable-ExpirienceOrbs-drop", true);
                config.addDefault("World.Register-world(s)-init", true);
                config.addDefault("World.Prevent-items/objects-to-spawn-into-the-world", false);
                config.addDefault("World.Register-world(s)-unload", true);
                config.addDefault("World.Register-world(s)-save", true);
                config.addDefault("World.Register-world(s)-load", true);
                //Physics
                config.addDefault("Physics.Disable.sand-physics", false);
                config.addDefault("Physics.Disable.gravel-physics", false);
                //Fade
                config.addDefault("Fade.Prevent-Ice-melting", false);
                config.addDefault("Fade.Prevent-Snow-melting", false);
                //Vehicles
                config.addDefault("Vehicle.Prevent.enter.Minecrafts", false);
                config.addDefault("Vehicle.Prevent.enter.Boats", false);
                config.addDefault("Vehicle.Prevent.destroy.Minecarts", false);
                config.addDefault("Vehicle.Prevent.destroy.Boats", false);
                //Chunks
                config.addDefault("Chunk.Prevent.unload-chunks(Use with caution)", false);
                config.addDefault("Chunk.Enable-Chunk-emergency-loader", false);
                saveConfig();
                log.info("[iSafe] "+ pdffile.getFullName() + " Loaded configuration file.");
            //Register events    
            PluginManager pm = getServer().getPluginManager();
            //PlayerListener
            pm.registerEvent(Event.Type.PLAYER_JOIN, playerListener, Event.Priority.Normal, this);
            pm.registerEvent(Event.Type.PLAYER_BUCKET_EMPTY, playerListener, Event.Priority.Normal, this);
            pm.registerEvent(Event.Type.PLAYER_MOVE, playerListener, Event.Priority.Normal, this);
            pm.registerEvent(Event.Type.PLAYER_TOGGLE_SPRINT, playerListener, Event.Priority.Normal, this);
            pm.registerEvent(Event.Type.PLAYER_TOGGLE_SNEAK, playerListener, Event.Priority.Normal, this);
            pm.registerEvent(Event.Type.PLAYER_QUIT, playerListener, Event.Priority.Normal, this);
            pm.registerEvent(Event.Type.PLAYER_DROP_ITEM, playerListener, Event.Priority.Normal, this);
            pm.registerEvent(Event.Type.PLAYER_MOVE, playerListener, Event.Priority.Normal, this);
            pm.registerEvent(Event.Type.PLAYER_TELEPORT, playerListener, Event.Priority.Normal, this);
            pm.registerEvent(Event.Type.PLAYER_CHAT, playerListener, Event.Priority.Normal, this);
            pm.registerEvent(Event.Type.PLAYER_BED_ENTER, playerListener, Event.Priority.Normal, this);
            pm.registerEvent(Event.Type.PLAYER_KICK, playerListener, Event.Priority.Normal, this);
            pm.registerEvent(Event.Type.PLAYER_FISH, playerListener, Event.Priority.Normal, this);
            pm.registerEvent(Event.Type.PLAYER_INTERACT, playerListener, Event.Priority.Normal, this);
            pm.registerEvent(Event.Type.PLAYER_LOGIN, playerListener, Event.Priority.Normal, this);
            pm.registerEvent(Event.Type.PLAYER_COMMAND_PREPROCESS, playerListener, Event.Priority.Normal, this);
            pm.registerEvent(Event.Type.PLAYER_PICKUP_ITEM, playerListener, Event.Priority.Normal, this);
            pm.registerEvent(Event.Type.PLAYER_INTERACT_ENTITY, playerListener, Event.Priority.Normal, this);
            pm.registerEvent(Event.Type.PLAYER_GAME_MODE_CHANGE, playerListener, Event.Priority.Normal, this);
            //BlockListener
            pm.registerEvent(Event.Type.BLOCK_PLACE, blockListener, Event.Priority.Normal, this);
            pm.registerEvent(Event.Type.BLOCK_BREAK, blockListener, Event.Priority.Normal, this);
            pm.registerEvent(Event.Type.BLOCK_DAMAGE, blockListener, Event.Priority.Normal, this);
            pm.registerEvent(Event.Type.BLOCK_IGNITE, blockListener, Event.Priority.Normal, this);
            pm.registerEvent(Event.Type.BLOCK_BURN, blockListener, Event.Priority.Normal, this);
            pm.registerEvent(Event.Type.BLOCK_FORM, blockListener, Event.Priority.Normal, this);
            pm.registerEvent(Event.Type.BLOCK_FROMTO, blockListener, Event.Priority.Normal, this);
            pm.registerEvent(Event.Type.BLOCK_PISTON_EXTEND, blockListener, Event.Priority.Normal, this);
            pm.registerEvent(Event.Type.BLOCK_PISTON_RETRACT, blockListener, Event.Priority.Normal, this);
            pm.registerEvent(Event.Type.LEAVES_DECAY, blockListener, Event.Priority.Normal, this);
            //EntityListener
            pm.registerEvent(Event.Type.EXPLOSION_PRIME, entityListener, Event.Priority.Normal, this);
            pm.registerEvent(Event.Type.ENTITY_EXPLODE, entityListener, Event.Priority.Normal, this);
            pm.registerEvent(Event.Type.CREATURE_SPAWN, entityListener, Event.Priority.Normal, this);
            pm.registerEvent(Event.Type.ENDERMAN_PICKUP, entityListener, Event.Priority.Normal, this);
            pm.registerEvent(Event.Type.ENDERMAN_PLACE, entityListener, Event.Priority.Normal, this);
            pm.registerEvent(Event.Type.FOOD_LEVEL_CHANGE, entityListener, Event.Priority.Normal, this);
            pm.registerEvent(Event.Type.ENTITY_DEATH, entityListener, Event.Priority.Normal, this);
            pm.registerEvent(Event.Type.SLIME_SPLIT, entityListener, Event.Priority.Normal, this);
            pm.registerEvent(Event.Type.ENTITY_TARGET, entityListener, Event.Priority.Normal, this);
            pm.registerEvent(Event.Type.ENTITY_INTERACT, entityListener, Event.Priority.Normal, this);
            pm.registerEvent(Event.Type.PIG_ZAP, entityListener, Event.Priority.Normal, this);
            pm.registerEvent(Event.Type.ENTITY_TAME, entityListener, Event.Priority.Normal, this);
            pm.registerEvent(Event.Type.ITEM_SPAWN, entityListener, Event.Priority.Normal, this);
            pm.registerEvent(Event.Type.CREEPER_POWER, entityListener, Event.Priority.Normal, this);
            pm.registerEvent(Event.Type.ENTITY_PORTAL_ENTER, entityListener, Event.Priority.Normal, this);
            pm.registerEvent(Event.Type.ENTITY_COMBUST, entityListener, Event.Priority.Low, this);
            pm.registerEvent(Event.Type.ENTITY_REGAIN_HEALTH, entityListener, Event.Priority.Low, this);
            //WeatherListener
            pm.registerEvent(Event.Type.WEATHER_CHANGE, weatherListener, Event.Priority.Normal, this);
            pm.registerEvent(Event.Type.THUNDER_CHANGE, weatherListener, Event.Priority.Normal, this);
            pm.registerEvent(Event.Type.LIGHTNING_STRIKE, weatherListener, Event.Priority.Normal, this);
            //InventoryListener
            pm.registerEvent(Event.Type.FURNACE_BURN, inventoryListener, Event.Priority.Normal, this);
            pm.registerEvent(Event.Type.FURNACE_SMELT, inventoryListener, Event.Priority.Normal, this);
            //WorldListener
            pm.registerEvent(Event.Type.WORLD_UNLOAD, worldListener, Event.Priority.Normal, this);
            pm.registerEvent(Event.Type.WORLD_LOAD, worldListener, Event.Priority.Normal, this);
            pm.registerEvent(Event.Type.WORLD_SAVE, worldListener, Event.Priority.Normal, this);
            pm.registerEvent(Event.Type.CHUNK_UNLOAD, worldListener, Event.Priority.Normal, this);
            pm.registerEvent(Event.Type.CHUNK_LOAD, worldListener, Event.Priority.Normal, this);
            //VehicleListener
            pm.registerEvent(Event.Type.VEHICLE_ENTER, vehicleListener, Event.Priority.Normal, this);
            pm.registerEvent(Event.Type.VEHICLE_DESTROY, vehicleListener, Event.Priority.Normal, this);
            
            //Read the command in the command executor
            CmdExecutor = new iSafeCommandExecutor(this);
            //And now register them .
            getCommand("iSafe-reload").setExecutor(CmdExecutor);
            getCommand("iSafe-info").setExecutor(CmdExecutor);
            getCommand("iSafe-commands").setExecutor(CmdExecutor);
            getCommand("serverinfo").setExecutor(CmdExecutor);
            getCommand("superbreak").setExecutor(CmdExecutor);
            getCommand("healme").setExecutor(CmdExecutor);
            //Log the rest.
            log.info("[iSafe] "+ pdffile.getFullName() + " Registered events.");
            log.info("[iSafe] "+ "Bukkit version: "+ getServer().getBukkitVersion());
            log.info("[iSafe] "+ pdffile.getFullName() + " enabled succesfully.");
    }
}
