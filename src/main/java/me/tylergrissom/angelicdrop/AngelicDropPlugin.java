package me.tylergrissom.angelicdrop;

import lombok.Getter;
import me.tylergrissom.angelicdrop.command.AngelicDropCommand;
import me.tylergrissom.angelicdrop.listener.InteractListener;
import me.tylergrissom.angelicdrop.listener.ItemListener;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Copyright Tyler Grissom 2018
 */
public class AngelicDropPlugin extends JavaPlugin {

    @Getter
    private AngelicDropPlugin plugin;

    @Getter
    private AngelicDropController controller;

    @Override
    public void onEnable() {
        plugin = this;
        controller = new AngelicDropController(this);

        getCommand("angelicdrop").setExecutor(new AngelicDropCommand(this));

        Bukkit.getPluginManager().registerEvents(new InteractListener(this), this);
        Bukkit.getPluginManager().registerEvents(new ItemListener(this), this);
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this);
    }
}
