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


public enum MessageSender {
    MESSAGE_SENDER;

    // MESSAGE_IS_NULL is a feature: admins can disable messages
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
        } else if (Objects.isNull(messageConfig)) {
            return;
        }

        Audience audience = getAudiences().sender(sender);
        audience.sendMessage(messageConfig.getFormattedValue());
    }

    public void sendMessage(Player player, ConfigKey messageConfig) {
        sendMessage(player, messageConfig.getFormattedValue());
    }

    public void sendMessage(Player player, Component message) {
        if (Objects.isNull(player)) {
            logger.severe(PLAYER_IS_NULL);
            return;
        } else if (Objects.isNull(message)) {
            return;
        }

        Audience audience = getAudiences().player(player);
        audience.sendMessage(message);

    }

    public void sendTitle(Player player, ConfigKey title, String subtitle) {
        sendTitle(player, title.getFormattedValue(), Component.text(subtitle));
    }

    public void sendTitle(Player player, Component titleComponent, Component subtitleComponent) {
        if (Objects.isNull(player)) {
            logger.severe(PLAYER_IS_NULL);
            return;
        } else if (Objects.isNull(titleComponent)) {
            return;
        }

        Times times = Title.Times.times(Duration.ofSeconds(1), Duration.ofSeconds(3), Duration.ofSeconds(1));
        Title title = Title.title(titleComponent, subtitleComponent, times);

        Audience audience = getAudiences().player(player);
        audience.showTitle(title);
    }
}