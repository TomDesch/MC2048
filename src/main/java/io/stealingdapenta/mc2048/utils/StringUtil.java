package io.stealingdapenta.mc2048.utils;

import org.bukkit.ChatColor;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class StringUtil {
    
    /**
     * Helper to process a Component through the translate method.
     */
    public static Component processComponent(Component component) {
        if (component == null) return null;

        String legacy = LegacyComponentSerializer.legacyAmpersand().serialize(component);
        String translated = translate(legacy);

        return LegacyComponentSerializer.legacyAmpersand().deserialize(translated);
    }

    /**
     * Translates alternate color codes using the '&' symbol.
     */
    public static String translate(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }
}
