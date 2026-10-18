package io.stealingdapenta.mc2048.utils;

import io.stealingdapenta.mc2048.config.ConfigKey;
import java.time.Duration;
import java.util.Objects;
import java.util.logging.Logger;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.title.Title.Times;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public final class MessageSender implements AutoCloseable {

    public static final String ERROR_IS_NULL = "Error sending message, %s is null.";
    private final LegacyComponentSerializer serializer = LegacyComponentSerializer.legacySection();
    private final BukkitAudiences audiences;
    private final Logger logger;

    public MessageSender(JavaPlugin plugin) {
        audiences = BukkitAudiences.create(plugin);
        logger = plugin.getLogger();
    }

    @Override
    public void close() {
        audiences.close();
    }

    public void sendMessage(CommandSender sender, ConfigKey messageConfig) {
        if (Objects.isNull(sender)) {
            logger.severe(ERROR_IS_NULL.formatted("Sender"));
            return;
        }

        if (Objects.isNull(messageConfig)) {
            return;
        }

        audiences.sender(sender)
                 .sendMessage(messageConfig.getFormattedValue());
    }

    public void sendMessage(Player player, ConfigKey messageConfig) {
        sendMessage(player, messageConfig.getFormattedValue());
    }

    public void sendMessage(Player player, Component message) {
        if (Objects.isNull(player)) {
            logger.severe(ERROR_IS_NULL.formatted("Player"));
            return;
        }

        if (Objects.isNull(message)) {
            return;
        }

        String processedStr = serializer.serialize(message).trim();
        // Empty configured messages intentionally disable that notification.
        if (processedStr.isEmpty()) {
            return;
        }

        audiences.player(player)
                 .sendMessage(message);
    }

    /**
     * Sends a {@link Title} and subtitle to the specified {@link Player} with default fade-in, stay, and fade-out durations.
     *
     * @param player            the {@link Player} to whom the title should be sent
     * @param titleComponent    the main title {@link Component} to display
     * @param subtitleComponent the subtitle {@link Component} to display beneath the main title
     */
    public void sendTitle(Player player, @NotNull Component titleComponent, @NotNull Component subtitleComponent) {
        if (Objects.isNull(player)) {
            logger.severe(ERROR_IS_NULL.formatted("Player"));
            return;
        }
        
        Times times = Title.Times.times(Duration.ofSeconds(1), Duration.ofSeconds(3), Duration.ofSeconds(1));
        Title title = Title.title(titleComponent, subtitleComponent, times);
        
        Audience audience = audiences.player(player);
        audience.showTitle(title);
    }
}