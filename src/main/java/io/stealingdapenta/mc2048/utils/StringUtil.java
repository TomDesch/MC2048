package io.stealingdapenta.mc2048.utils;

import java.util.Objects;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;

public class StringUtil {
    
    /**
     * Helper to process a Component through the 'translate' method.
     * @return the translated component, or an empty component if the input is null.
     */
    @NotNull
    public static Component processComponent(Component component) {
        if (Objects.isNull(component)) {
            return Component.empty();
        }

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
