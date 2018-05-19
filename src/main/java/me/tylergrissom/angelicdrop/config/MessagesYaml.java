package me.tylergrissom.angelicdrop.config;

import lombok.Getter;
import me.tylergrissom.angelicdrop.AngelicDropPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

/**
 * Copyright (c) 2013-2018 Tyler Grissom
 */
public class MessagesYaml {

    @Getter
    private AngelicDropPlugin plugin;

    @Getter
    private FileConfiguration messages;

    @Getter
    private File messagesFile;

    public MessagesYaml(AngelicDropPlugin plugin) {
        this.plugin = plugin;

        this.messagesFile = new File(getPlugin().getDataFolder(), "messages.yml");
        this.messages = YamlConfiguration.loadConfiguration(messagesFile);
    }

    public void reload() {
        if (messagesFile == null) {
            messagesFile = new File(getPlugin().getDataFolder(), "messages.yml");
        }

        messages = YamlConfiguration.loadConfiguration(messagesFile);

        try {
            Reader defStream = new InputStreamReader(getPlugin().getResource("messages.yml"), "UTF8");

            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defStream);

            messages.setDefaults(defConfig);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public FileConfiguration get() {
        if (messages == null) {
            reload();
        }

        return messages;
    }

    public FileConfiguration getDefaults() {
        Reader defStream = null;

        try {
            defStream = new InputStreamReader(getPlugin().getResource("messages.yml"), "UTF8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        if (defStream == null) {
            Bukkit.getLogger().log(Level.SEVERE, "Failed to retrieve default messages.yml");

            return null;
        }

        return YamlConfiguration.loadConfiguration(defStream);
    }

    public void save() {
        if (messages == null || messagesFile == null) return;

        try {
            getMessages().save(getMessagesFile());
        } catch (IOException exception) {
            Bukkit.getLogger().log(Level.SEVERE, "Could not save config to " + getMessagesFile(), exception);
        }
    }

    public void saveDefaults() {
        if (messagesFile == null) {
            messagesFile = new File(getPlugin().getDataFolder(), "messages.yml");
        }

        if (!messagesFile.exists()) {
            getPlugin().saveResource("messages.yml", false);
        }
    }

    public String getMessage(String entry) {
        if (entry == null || get().get(entry) == null) {
            return null;
        }

        String msg = get().getString(entry);

        if (msg == null) {
            FileConfiguration def = getDefaults();

            msg = ChatColor.translateAlternateColorCodes('&', def.getString(entry));
        }

        return ChatColor.translateAlternateColorCodes('&', msg);
    }

    public String getMessage(String entry, Map<String, String> replacements) {
        String msg = getMessage(entry);

        for (Map.Entry<String, String> mapEntry : replacements.entrySet()) {
            msg = msg.replace("%" + mapEntry.getKey().toLowerCase() + "%", mapEntry.getValue());
        }

        return msg;
    }

    public String[] getMessages(String entry) {
        List<String> messages;

        if (get().get(entry) == null) {
            FileConfiguration def = getDefaults();

            messages = def.getStringList(entry);
        } else {
            messages = get().getStringList(entry);
        }

        for (int i = 0; i < messages.size(); i++) {
            messages.set(i, ChatColor.translateAlternateColorCodes('&', messages.get(i)));
        }

        return messages.toArray(new String[messages.size()]);
    }

    public String[] getMessages(String entry, Map<String, String> replacements) {
        String[] messages = getMessages(entry);

        for (int i = 0; i < messages.length; i++) {
            String str = ChatColor.translateAlternateColorCodes('&', messages[i]);

            for (Map.Entry<String, String> replace : replacements.entrySet()) {
                str = str.replace("%" + replace.getKey() + "%", replace.getValue());
            }

            messages[i] = str;
        }

        return messages;
    }
}