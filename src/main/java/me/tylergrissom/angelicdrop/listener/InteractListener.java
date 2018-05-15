package me.tylergrissom.angelicdrop.listener;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.tylergrissom.angelicdrop.AngelicDropPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

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

    @EventHandler
    public void onInteract(final PlayerInteractEvent event) {
        Player p = event.getPlayer();
        ItemStack item = event.getItem();

        if (item == null || item.getItemMeta() == null || item.getItemMeta().getDisplayName() == null) {
            return;
        }

        ConfigurationSection section = getPlugin().getConfig().getConfigurationSection("items");

        for (String key : section.getKeys(false)) {
            ConfigurationSection subSection = section.getConfigurationSection(key);

            if (item.getItemMeta().getDisplayName().equals(ChatColor.translateAlternateColorCodes('&', subSection.getString("display_name"))) && subSection.get("pickup_commands") != null) {
                event.setCancelled(true);

                List<String> commands = subSection.getStringList("pickup_commands");

                for (int i = 0; i < item.getAmount(); i++) {
                    for (String cmd : commands) {
                        cmd = cmd.replace("%player%", p.getName());

                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
                    }
                }

                p.getInventory().remove(item);

                break;
            }
        }
    }
}
