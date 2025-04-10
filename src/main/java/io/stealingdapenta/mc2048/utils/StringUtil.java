package io.stealingdapenta.mc2048.utils;

import java.text.NumberFormat;
import java.util.Locale;

import org.bukkit.ChatColor;

public class StringUtil {

    /**
     * Translates alternate color codes using the '&' symbol.
     */
    public static String translate(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    /**
     * Formats long to "200,000" style
     */
    public static String formatLong(long amount) {
        return NumberFormat.getNumberInstance(Locale.US).format(amount);
    }

    /**
     * Formats int to "200,000" style
     */
    public static String formatInt(int amount) {
        return NumberFormat.getNumberInstance(Locale.US).format(amount);
    }
}
