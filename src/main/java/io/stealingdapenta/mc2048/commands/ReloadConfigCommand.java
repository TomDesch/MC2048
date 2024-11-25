package io.stealingdapenta.mc2048.commands;


import static io.stealingdapenta.mc2048.config.ConfigKey.NO_PERMISSION;
import static io.stealingdapenta.mc2048.config.ConfigKey.RELOADED;

import io.stealingdapenta.mc2048.config.ConfigurationFileManager;
import io.stealingdapenta.mc2048.config.Permission;
import io.stealingdapenta.mc2048.utils.MessageSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ReloadConfigCommand implements CommandExecutor {

    private final MessageSender messageSender = MessageSender.getInstance();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {

        if (!sender.hasPermission(Permission.RELOAD.getNode())) {
            messageSender.sendMessage(sender, NO_PERMISSION);
            return true;
        }

        ConfigurationFileManager.getInstance()
                                .reloadConfig();
        messageSender.sendMessage(sender, RELOADED);
        return true;
    }
}
