package me.tylergrissom.angelicdrop.utility;

import org.bukkit.ChatColor;

/**
 * Copyright (c) 2013-2018 Tyler Grissom
 */
public class ColorUtility {

    public static String translate(String str) {
        return ChatColor.translateAlternateColorCodes('&', str);
    }

    public static String strip(String str) {
        return ChatColor.stripColor(str);
    }

    public static String translateAndStrip(String str) {
        return strip(translate(str));
    }
}
