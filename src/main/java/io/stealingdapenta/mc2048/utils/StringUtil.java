package io.stealingdapenta.mc2048.utils;

import org.bukkit.ChatColor;

public class StringUtil {

    /**
     * Translates alternate color codes using the '&' symbol.
     */
    public static String translate(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }
}
