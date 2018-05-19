package me.tylergrissom.angelicdrop;

import com.sk89q.worldedit.bukkit.selections.Selection;
import lombok.Getter;
import me.tylergrissom.angelicdrop.config.MessagesYaml;
import me.tylergrissom.angelicdrop.task.DropPartyTask;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

/**
 * Copyright (c) 2013-2018 Tyler Grissom
 */
public class DropParty {
    
    @Getter
    private AngelicDropController controller;
    
    @Getter
    private int taskId;
    
    @Getter
    private Selection selection;
    
    @Getter
    private Player creator;

    public DropParty(AngelicDropController controller, Player creator, Selection selection) {
        this.controller = controller;
        this.creator = creator;
        this.selection = selection;
    }
    
    public void start() {
        MessagesYaml messages = getController().getMessages();
        
        Bukkit.getLogger().log(Level.INFO, creator.getName() + " has started a drop party at " + selection);

        Map<String, String> replace = new HashMap<>();

        replace.put("player", creator.getDisplayName());
        replace.put("location", (int) creator.getLocation().getX() + " " + (int) creator.getLocation().getY() + " " + (int) creator.getLocation().getZ());

        Bukkit.getOnlinePlayers().forEach(p -> {
            if (p.hasPermission(new Permission("angelicdrop.notify"))) {
                p.sendMessage(messages.getMessage("command.party_started", replace));
            }
        });

        taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(controller.getPlugin(), new DropPartyTask(controller.getPlugin(), selection), 0, 20);

        Bukkit.getScheduler().runTaskLater(controller.getPlugin(), () -> {
            Bukkit.getScheduler().cancelTask(taskId);

            Bukkit.getOnlinePlayers().forEach(p -> {
                if (p.hasPermission(new Permission("angelicdrop.notify"))) {
                    p.sendMessage(messages.getMessage("command.party_ended", replace));
                }
            });
        }, controller.getDuration());
    }
}
