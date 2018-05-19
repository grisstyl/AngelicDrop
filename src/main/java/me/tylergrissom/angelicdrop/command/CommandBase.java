package me.tylergrissom.angelicdrop.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.List;

/**
 * Copyright Tyler Grissom 2018
 */
public abstract class CommandBase implements TabExecutor {

    public abstract void execute(CommandSender sender, Command command, String[] args);

    public abstract List<String> tab(CommandSender sender, Command command, String[] args);

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        execute(sender, command, args);

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return tab(sender, command, args);
    }
}
