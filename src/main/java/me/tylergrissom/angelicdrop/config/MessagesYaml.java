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
        return ChatColor.translateAlternateColorCodes('&', get().getString(entry));
    }

    public String getMessage(String entry, Map<String, String> replacements) {
        String msg = getMessage(entry);

        for (Map.Entry<String, String> mapEntry : replacements.entrySet()) {
            msg = msg.replace("%" + mapEntry.getKey().toLowerCase() + "%", mapEntry.getValue());
        }

        return msg;
    }

    public String[] getMessages(String entry) {
        List<String> messages = get().getStringList(entry);

        for (int i = 0; i < messages.size(); i++) {
            messages.set(i, ChatColor.translateAlternateColorCodes('&', messages.get(i)));
        }

        return messages.toArray(new String[messages.size()]);
    }

    public String[] getMessages(String entry, Map<String, String> replacements) {
        List<String> messages = getMessages().getStringList(entry);

        for (int i = 0; i < messages.size(); i++) {
            String str = ChatColor.translateAlternateColorCodes('&', messages.get(i));

            for (Map.Entry<String, String> replace : replacements.entrySet()) {
                str = str.replace("%" + replace.getKey() + "%", replace.getValue());
            }

            messages.set(i, str);
        }

        return messages.toArray(new String[messages.size()]);
    }
}