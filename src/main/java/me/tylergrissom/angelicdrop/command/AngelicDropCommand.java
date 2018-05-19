package me.tylergrissom.angelicdrop.command;

import com.sk89q.worldedit.bukkit.selections.Selection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import me.tylergrissom.angelicdrop.AngelicDropController;
import me.tylergrissom.angelicdrop.AngelicDropPlugin;
import me.tylergrissom.angelicdrop.config.MessagesYaml;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

import java.util.Arrays;
import java.util.List;

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

        if (args.length == 0) {
            sender.sendMessage(usage);
        } else {
            String sub = args[0];

            if (sub.equalsIgnoreCase("start")) {
                if (!sender.hasPermission(new Permission("angelicdrop.start"))) {
                    sender.sendMessage(messages.getMessage("error.no_permission"));

                    return;
                }

                if (sender instanceof Player) {
                    Player p = (Player) sender;
                    Selection selection = controller.getWorldEditIntegration().getSelection(p);

                    if (selection != null) {
                        controller.startDropParty(p, selection);
                    } else {
                        p.sendMessage(messages.getMessage("error.make_selection"));
                    }
                } else {
                    sender.sendMessage(messages.getMessage("error.only_players"));
                }
            } else if (sub.equalsIgnoreCase("reload")) {
                if (!sender.hasPermission(new Permission("angelicdrop.reload"))) {
                    sender.sendMessage(messages.getMessage("error.no_permission"));

                    return;
                }

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
    }

    @Override
    public List<String> tab(CommandSender sender, Command command, String[] args) {
        return Arrays.asList("start", "reload");
    }
}
