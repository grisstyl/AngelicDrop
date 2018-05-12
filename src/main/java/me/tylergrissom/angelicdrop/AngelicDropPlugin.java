package me.tylergrissom.angelicdrop;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import lombok.Getter;
import me.tylergrissom.angelicdrop.command.AngelicDropCommand;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.logging.Level;

/**
 * Copyright Tyler Grissom 2018
 */
public class AngelicDropPlugin extends JavaPlugin {

    @Getter
    private AngelicDropPlugin plugin;

    @Getter
    private WorldEditPlugin worldEdit;

    private void setupConfiguration() {
        if (!new File(getDataFolder(), "config.yml").exists()) {
            Bukkit.getLogger().log(Level.INFO, "config.yml not found, regenerating.");

            saveDefaultConfig();
        }
    }

    private void setupWorldEditIntegration() {
        PluginManager pm = Bukkit.getPluginManager();
        Plugin worldEditPlugin = Bukkit.getPluginManager().getPlugin("WorldEdit");

        if (worldEditPlugin != null) {
            worldEdit = (WorldEditPlugin) worldEditPlugin;

            Bukkit.getLogger().log(Level.INFO, "Registered WorldEdit integration.");
        } else {
            Bukkit.getLogger().log(Level.SEVERE, "WorldEdit not found, disabling plugin.");

            pm.disablePlugin(this);
        }
    }

    @Override
    public void onEnable() {
        plugin = this;

        setupConfiguration();
        setupWorldEditIntegration();

        getCommand("angelicdrop").setExecutor(new AngelicDropCommand(this));
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this);
    }
}
