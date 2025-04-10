package io.stealingdapenta.mc2048.utils;

import static io.stealingdapenta.mc2048.MC2048.logger;

import io.stealingdapenta.mc2048.MC2048;
import io.stealingdapenta.mc2048.config.ConfigKey;
import java.time.Duration;
import java.util.Objects;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.title.Title.Times;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public enum MessageSender {
    MESSAGE_SENDER;

    public static final String PLAYER_IS_NULL = "Error sending message, player is null.";
    private BukkitAudiences audiences;

    private BukkitAudiences getAudiences() {
        lazyInitAudiences();
        return audiences;
    }

    private void lazyInitAudiences() {
        if (Objects.isNull(audiences)) {
            audiences = BukkitAudiences.create(MC2048.getInstance());
        }
    }

    public void sendMessage(CommandSender sender, ConfigKey messageConfig) {
        if (Objects.isNull(sender)) {
            logger.severe(PLAYER_IS_NULL);
            return;
        }

        if (Objects.isNull(messageConfig)) {
            return;
        }

        getAudiences().sender(sender)
                      .sendMessage(messageConfig.getFormattedValue());
    }

    public void sendMessage(Player player, ConfigKey messageConfig) {
        sendMessage(player, messageConfig.getFormattedValue());
    }

    public void sendMessage(Player player, Component message) {
        if (Objects.isNull(player)) {
            logger.severe(PLAYER_IS_NULL);
            return;
        }

        if (Objects.isNull(message)) {
            return;
        }

        getAudiences().player(player)
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
            logger.severe(PLAYER_IS_NULL);
            return;
        }
        
        Times times = Title.Times.times(Duration.ofSeconds(1), Duration.ofSeconds(3), Duration.ofSeconds(1));
        Title title = Title.title(titleComponent, subtitleComponent, times);
        
        Audience audience = getAudiences().player(player);
        audience.showTitle(title);
    }
}