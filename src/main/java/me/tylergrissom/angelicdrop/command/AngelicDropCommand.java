package me.tylergrissom.angelicdrop.command;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.tylergrissom.angelicdrop.AngelicDropPlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 * Copyright Tyler Grissom 2018
 */
@AllArgsConstructor
public class AngelicDropCommand extends CommandBase {

    @Getter
    private AngelicDropPlugin plugin;

    @Override
    public void execute(CommandSender sender, Command command, String[] args) {
        sender.sendMessage(ChatColor.RED + "AngelicDrop");
    }
}
