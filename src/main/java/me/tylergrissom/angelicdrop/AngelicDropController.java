package me.tylergrissom.angelicdrop;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import lombok.Getter;
import me.tylergrissom.angelicdrop.config.MessagesYaml;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import java.io.File;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Copyright Tyler Grissom 2018
 */
public class AngelicDropController {

    @Getter
    private AngelicDropPlugin plugin;

    @Getter
    private FileConfiguration config;

    @Getter
    private MessagesYaml messages;

    @Getter
    private WorldEditPlugin worldEditIntegration;

    AngelicDropController(AngelicDropPlugin plugin) {
        this.plugin = plugin;

        if (!new File(getPlugin().getDataFolder(), "config.yml").exists()) {
            getPlugin().saveDefaultConfig();
        }

        this.config = getPlugin().getConfig();
        this.messages = new MessagesYaml(plugin);

        messages.saveDefaults();

        PluginManager pm = Bukkit.getPluginManager();
        Plugin worldEditPlugin = Bukkit.getPluginManager().getPlugin("WorldEdit");

        if (worldEditPlugin != null) {
            worldEditIntegration = (WorldEditPlugin) worldEditPlugin;

            Bukkit.getLogger().log(Level.INFO, "Registered WorldEdit integration.");
        } else {
            Bukkit.getLogger().log(Level.SEVERE, "WorldEdit not found, disabling plugin.");

            pm.disablePlugin(getPlugin());
        }
    }

    public void reloadPlugin() throws Throwable {
        Logger logger = Bukkit.getLogger();

        logger.log(Level.INFO, "Attempting to reload AngelicDrop...");

        try {
            getPlugin().reloadConfig();
            getMessages().reload();
        } catch (Throwable throwable) {
            logger.log(Level.SEVERE, "Could not reload AngelicDrop: " + throwable.getMessage());

            throwable.printStackTrace();

            throw throwable;
        } finally {
            logger.log(Level.INFO, "Reloaded plugin");
        }
    }

    public ConfigurationSection getDropItemsSection() {
        return getConfig().getConfigurationSection("items");
    }

    public Set<ItemStack> getDropItems() {
        ConfigurationSection section = getConfig().getConfigurationSection("items");
        Set<ItemStack> items = new HashSet<>();

        for (String key : section.getKeys(false)) {
            items.add(createDropItem(section.getConfigurationSection(key)));
        }

        return items;
    }

    public ItemStack createDropItem(String key) {
        return createDropItem(getConfig().getConfigurationSection("items." + key));
    }

    public ItemStack createDropItem(ConfigurationSection section) {
        Material material = Material.valueOf(section.getString("material").toUpperCase());
        int amount = section.getInt("amount", 1);
        byte data = (byte) section.getInt("data", 0);
        short damage = (short) section.getInt("durbility", 0);
        boolean unbreakable = section.getBoolean("unbreakable", false);

        ItemStack is = new ItemStack(material, amount, damage, data);
        ItemMeta meta = is.getItemMeta();

        meta.setUnbreakable(unbreakable);

        if (section.get("display_name") == null) {
            throw new IllegalArgumentException("All items must have a display_name attribute");
        }

        String name = ChatColor.translateAlternateColorCodes('&', section.getString("display_name"));

        meta.setDisplayName(name);

        if (section.get("lore") != null) {
            List<String> originalLore = section.getStringList("lore");
            List<String> lore = new ArrayList<>();

            for (String str : originalLore) {
                lore.add(ChatColor.translateAlternateColorCodes('&', str));
            }

            meta.setLore(lore);
        }

        meta.setUnbreakable(unbreakable);

        is.setItemMeta(meta);

        if (section.getConfigurationSection("enchantments") != null) {
            Map<Enchantment, Integer> enchantments = new HashMap<>();

            for (String key : section.getConfigurationSection("enchantments").getKeys(false)) {
                enchantments.put(Enchantment.getByName(key.toUpperCase()), section.getConfigurationSection("enchantments").getInt(key, 1));
            }

            is.addUnsafeEnchantments(enchantments);
        }

        System.out.println(section);

        if (section.get("color") != null) {
            System.out.println("1");
            
            if (material == Material.LEATHER_HELMET || material == Material.LEATHER_CHESTPLATE || material == Material.LEATHER_LEGGINGS || material == Material.LEATHER_BOOTS) {
                String[] split = section.getString("color").split(",");
                
                System.out.println("2");

                try {
                    int r = Integer.parseInt(split[0]);
                    int g = Integer.parseInt(split[1]);
                    int b = Integer.parseInt(split[2]);

                    LeatherArmorMeta leatherMeta = (LeatherArmorMeta) is.getItemMeta();

                    leatherMeta.setColor(Color.fromRGB(r, g, b));

                    is.setItemMeta(leatherMeta);
                    
                    System.out.println("3");
                } catch (NumberFormatException ignored) {
                    Bukkit.getLogger().log(Level.WARNING, "Invalid RGB color format (Ex. '255,0,0' for red)");
                }
            }
        }

        return is;
    }
}
