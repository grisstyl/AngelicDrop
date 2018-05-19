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

import java.util.HashSet;
import java.util.Set;

/**
 * Copyright (c) 2013-2018 Tyler Grissom
 */
@AllArgsConstructor
public class ItemListener implements Listener {

    @Getter
    private AngelicDropPlugin plugin;

    @EventHandler
    public void onPickup(final EntityPickupItemEvent event) {
        if (event.getEntity() instanceof Player) {
            AngelicDropController controller = getPlugin().getController();
            Player p = (Player) event.getEntity();
            ItemStack item = event.getItem().getItemStack();

            Pair<ItemStack, ConfigurationSection> pair = controller.getDropItem(item);

            if (pair != null) {
                ConfigurationSection section = pair.getValue();

                event.setCancelled(true);

                Set<String> commands = new HashSet<>(section.getStringList("pickup_commands"));

                for (int i = 0; i < item.getAmount(); i++) {
                    controller.dispatchCommands(commands, p);
                }

                if (section.getBoolean("remove", true)) {
                    p.getInventory().remove(item);
                }
            }
        }
    }
}
