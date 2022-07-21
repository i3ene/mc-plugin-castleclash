package de.nanoinsel.castleclash;

import de.nanoinsel.castleclash.commands.GameExecutor;
import de.nanoinsel.castleclash.commands.GameTabCompleter;
import de.nanoinsel.castleclash.game.GameLobby;
import de.nanoinsel.castleclash.game.area.Area;
import de.nanoinsel.castleclash.game.area.AreaLocation;
import de.nanoinsel.castleclash.game.area.AreaResource;
import de.nanoinsel.castleclash.game.handler.*;
import de.nanoinsel.castleclash.teams.trade.Trade;
import org.bukkit.*;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCreativeEvent;
import org.bukkit.event.player.*;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public final class CastleClash extends JavaPlugin implements Listener {

    public final String[] configNames = { "config", "stored" };
    private HashMap<String, File> customConfigFiles = new HashMap<>();
    private HashMap<String, FileConfiguration> customConfigs = new HashMap<>();

    public String world;

    public static CastleClash self;
    public static GameLobby gameLobby;


    static {
        ConfigurationSerialization.registerClass(Area.class);
        ConfigurationSerialization.registerClass(AreaLocation.class);
        ConfigurationSerialization.registerClass(AreaResource.class);
        ConfigurationSerialization.registerClass(Trade.class);
    }


    public CastleClash() {
        self = this;
    }


    @Override
    public void onEnable() {
        // Load Config File
        createCustomConfig("config");
        // Load Lobby World (defined in Config File)
        world = getCustomConfig("config").getString("world");
        if (Bukkit.getWorld(world) == null) new WorldCreator(world).createWorld();

        // Load Stored Filed
        createCustomConfig("stored");

        gameLobby = new GameLobby();
        Bukkit.getPluginManager().registerEvents(this, this);
        getCommand("cc").setExecutor(new GameExecutor(gameLobby.teams));
        getCommand("cc").setTabCompleter(new GameTabCompleter());

        getCustomConfig("stored").options().copyDefaults(true);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        PlayerQuitHandler.Event(e);
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent e) {
        PlayerRespawnHandler.Event(e);
    }

    @EventHandler (priority = EventPriority.HIGH)
    public void onInteract(PlayerInteractEvent e) {
        InteractHandler.Event(e);
    }

    @EventHandler
    public void onInvClick(InventoryClickEvent e) {
        InventoryClickHandler.Event(e);
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
        EntityDamageByEntityHandler.Event(e);
    }

    @EventHandler
    public void onEntityCombustEvent(EntityCombustEvent e) {
        EntityCombustHandler.Event(e);
    }

    @EventHandler (priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityDamageEvent(EntityDamageEvent e) {
        EntityDamageHandler.Event(e);
    }

    @EventHandler
    public void onEntityDeathEvent(EntityDeathEvent e) {
        EntityDeathHandler.Event(e);
    }

    @EventHandler
    public void onBlockBreakEvent(BlockBreakEvent e) {
        BlockBreakHandler.Event(e);
    }

    @EventHandler
    public void onBlockPlaceEvent(BlockPlaceEvent e) {
        BlockPlaceHandler.Event(e);
    }

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent e) {
        PlayerJoinHandler.Event(e);
    }

    @EventHandler
    public void onPlayerDropItemEvent(PlayerDropItemEvent e) {
        PlayerDropItemHandler.Event(e);
    }

    @EventHandler
    public void onPlayerBucketEmptyEvent(PlayerBucketEmptyEvent e) {
        PlayerBucketEmptyHandler.Event(e);
    }

    @EventHandler
    public void onPlayerBucketFillEvent(PlayerBucketFillEvent e) {
        PlayerBucketFillHandler.Event(e);
    }

    @EventHandler
    public void onFoodLevelChangeEvent(FoodLevelChangeEvent e) {
        FoodLevelChangeHandler.Event(e);
    }


    public void saveCustomConfig(String name) {
        try {
            customConfigs.get(name).save(customConfigFiles.get(name).getPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public FileConfiguration getCustomConfig(String name) {
        return this.customConfigs.get(name);
    }

    private void createCustomConfig(String name) {
        File customConfigFile = new File(getDataFolder(), name + ".yml");
        FileConfiguration customConfig;
        if (!customConfigFile.exists()) {
            customConfigFile.getParentFile().mkdirs();
            saveResource(name + ".yml", false);
        }

        customConfig = new YamlConfiguration();

        customConfigFiles.put(name, customConfigFile);
        customConfigs.put(name, customConfig);
        try {
            customConfig.load(customConfigFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

}
