package io.stealingdapenta.mc2048.utils;

import static io.stealingdapenta.mc2048.MC2048.logger;

import io.stealingdapenta.mc2048.MC2048;
import io.stealingdapenta.mc2048.config.ConfigKey;
import java.time.Duration;
import java.util.Objects;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.title.Title.Times;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
        } else if (Objects.isNull(messageConfig)) {
            return;
        }

        Audience audience = getAudiences().sender(sender);
        Component processed = StringUtil.processComponent(messageConfig.getFormattedValue());
        audience.sendMessage(processed);
    }

    public void sendMessage(Player player, ConfigKey messageConfig) {
        sendMessage(player, StringUtil.processComponent(messageConfig.getFormattedValue()));
    }

    public void sendMessage(Player player, Component message) {
        if (Objects.isNull(player)) {
            logger.severe(PLAYER_IS_NULL);
            return;
        } else if (Objects.isNull(message)) {
            return;
        }

        Component processed = StringUtil.processComponent(message);
        String processedStr = LegacyComponentSerializer.legacyAmpersand().serialize(processed).trim();
        if (processedStr.isEmpty()) {
            return; // Don't send a blank message.
        }
        
        Audience audience = getAudiences().player(player);
        audience.sendMessage(processed);
    }


    public void sendTitle(Player player, ConfigKey title, String subtitle) {
        sendTitle(player, StringUtil.processComponent(title.getFormattedValue()), StringUtil.processComponent(Component.text(subtitle)));
    }

    public void sendTitle(Player player, Component titleComponent, Component subtitleComponent) {
        if (Objects.isNull(player)) {
            logger.severe(PLAYER_IS_NULL);
            return;
        } else if (titleComponent == null && subtitleComponent == null) {
            return;
        }
        
        Component processedTitle = titleComponent != null ? StringUtil.processComponent(titleComponent) : null;
        Component processedSubtitle = subtitleComponent != null ? StringUtil.processComponent(subtitleComponent) : null;
        
        Times times = Title.Times.times(Duration.ofSeconds(1), Duration.ofSeconds(3), Duration.ofSeconds(1));
        Title title = Title.title(processedTitle, processedSubtitle, times);
        
        Audience audience = getAudiences().player(player);
        audience.showTitle(title);
    }
}