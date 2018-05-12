package me.tylergrissom.angelicdrop.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * Copyright Tyler Grissom 2018
 */
public abstract class CommandBase implements CommandExecutor {

    public abstract void execute(CommandSender sender, Command command, String[] args);

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        execute(sender, command, args);

        return true;
    }
}
