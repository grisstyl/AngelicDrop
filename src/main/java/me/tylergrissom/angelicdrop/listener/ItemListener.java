package me.tylergrissom.angelicdrop.listener;

import javafx.util.Pair;
import lombok.AllArgsConstructor;
import lombok.Getter;
import me.tylergrissom.angelicdrop.AngelicDropController;
import me.tylergrissom.angelicdrop.AngelicDropPlugin;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * Copyright (c) 2013-2018 Tyler Grissom
 */
@AllArgsConstructor
public class ItemListener implements Listener {

    @Getter
    private AngelicDropPlugin plugin;

    @EventHandler
    public void onPickup(final EntityPickupItemEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        AngelicDropController controller = getPlugin().getController();
        Player p = (Player) event.getEntity();
        ItemStack item = event.getItem().getItemStack();
        Pair<ItemStack, ConfigurationSection> pair = controller.getDropItem(item);

        if (pair == null) {
            return;
        }

        ConfigurationSection section = pair.getValue();

        if (section.get("pickup_commands") == null) {
            return;
        }

        List<String> commands = section.getStringList("pickup_commands");

        controller.dispatchCommands(commands, p);

        event.setCancelled(true);
        event.getItem().remove();
    }
}
