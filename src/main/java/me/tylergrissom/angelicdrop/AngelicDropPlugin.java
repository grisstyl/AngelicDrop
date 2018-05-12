package me.tylergrissom.angelicdrop;

import lombok.Getter;
import me.tylergrissom.angelicdrop.command.AngelicDropCommand;
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
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this);
    }
}
