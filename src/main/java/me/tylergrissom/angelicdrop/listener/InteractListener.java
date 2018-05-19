package me.tylergrissom.angelicdrop.listener;

import javafx.util.Pair;
import lombok.AllArgsConstructor;
import lombok.Getter;
import me.tylergrissom.angelicdrop.AngelicDropController;
import me.tylergrissom.angelicdrop.AngelicDropPlugin;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.Permission;

import java.util.HashSet;
import java.util.Set;

/**
 * Copyright Tyler Grissom 2018
 */
@AllArgsConstructor
public class InteractListener implements Listener {

    @Getter
    private AngelicDropPlugin plugin;

    private String translateAndStrip(String str) {
        return ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', str));
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onInteract(final PlayerInteractEvent event) {
        Player p = event.getPlayer();
        AngelicDropController controller = getPlugin().getController();

        if (!p.hasPermission(new Permission("angelicdrop.interact"))) {
            event.setCancelled(true);

            return;
        }

        if (event.isCancelled()) {
            return;
        }

        ItemStack item = event.getItem();

        Pair<ItemStack, ConfigurationSection> pair = controller.getDropItem(item);

        if (pair != null) {
            ConfigurationSection section = pair.getValue();

            if (section.getStringList("interact_commands") == null) {
                return;
            }

            event.setCancelled(true);

            Set<String> commands = new HashSet<>(section.getStringList("interact_commands"));

            for (int i = 0; i < item.getAmount(); i++) {
                controller.dispatchCommands(commands, p);
            }

            if (section.getBoolean("remove", true)) {
                p.getInventory().remove(item);
            }
        }
    }
}
