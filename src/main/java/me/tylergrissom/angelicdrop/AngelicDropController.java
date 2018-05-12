package me.tylergrissom.angelicdrop;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import lombok.Getter;
import me.tylergrissom.angelicdrop.config.MessagesYaml;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Copyright Tyler Grissom 2018
 */
public class AngelicDropController {

    @Getter
    private AngelicDropPlugin plugin;

    @Getter
    private FileConfiguration config;

    @Getter
    private MessagesYaml messages;

    @Getter
    private WorldEditPlugin worldEditIntegration;

    AngelicDropController(AngelicDropPlugin plugin) {
        this.plugin = plugin;

        if (!new File(getPlugin().getDataFolder(), "config.yml").exists()) {
            getPlugin().saveDefaultConfig();
        }

        this.config = getPlugin().getConfig();
        this.messages = new MessagesYaml(plugin);

        messages.saveDefaults();

        PluginManager pm = Bukkit.getPluginManager();
        Plugin worldEditPlugin = Bukkit.getPluginManager().getPlugin("WorldEdit");

        if (worldEditPlugin != null) {
            worldEditIntegration = (WorldEditPlugin) worldEditPlugin;

            Bukkit.getLogger().log(Level.INFO, "Registered WorldEdit integration.");
        } else {
            Bukkit.getLogger().log(Level.SEVERE, "WorldEdit not found, disabling plugin.");

            pm.disablePlugin(getPlugin());
        }
    }

    public void reloadPlugin() throws Throwable {
        Logger logger = Bukkit.getLogger();

        logger.log(Level.INFO, "Attempting to reload AngelicDrop...");

        try {
            getPlugin().reloadConfig();
            getMessages().reload();
        } catch (Throwable throwable) {
            logger.log(Level.SEVERE, "Could not reload AngelicDrop: " + throwable.getMessage());

            throwable.printStackTrace();

            throw throwable;
        } finally {
            logger.log(Level.INFO, "Reloaded plugin");
        }
    }
}
