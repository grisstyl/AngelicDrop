package me.tylergrissom.angelicdrop.command;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.tylergrissom.angelicdrop.AngelicDropController;
import me.tylergrissom.angelicdrop.AngelicDropPlugin;
import me.tylergrissom.angelicdrop.config.MessagesYaml;
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
        AngelicDropController controller = getPlugin().getController();
        MessagesYaml messages = controller.getMessages();
        String[] usage = messages.getMessages("command.usage");

        if (sender.hasPermission("angelicdrop.admin")) {
            if (args.length == 0) {
                sender.sendMessage(usage);
            } else {
                String sub = args[0];

                if (sub.equalsIgnoreCase("start")) {

                } else if (sub.equalsIgnoreCase("reload")) {
                    try {
                        sender.sendMessage(messages.getMessage("command.reloading"));

                        controller.reloadPlugin();

                        sender.sendMessage(messages.getMessage("command.reloaded"));
                    } catch (Throwable ignored) {
                        sender.sendMessage(messages.getMessage("error.reload_failed"));
                    }
                } else {
                    sender.sendMessage(usage);
                }
            }
        } else {
            sender.sendMessage(messages.getMessage("error.no_permission"));
        }
    }
}
