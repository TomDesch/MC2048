package io.stealingdapenta.mc2048.commands;


import static io.stealingdapenta.mc2048.config.ConfigKey.NO_PERMISSION;
import static io.stealingdapenta.mc2048.config.ConfigKey.RELOADED;

import io.stealingdapenta.mc2048.config.ConfigurationFileManager;
import io.stealingdapenta.mc2048.config.Permission;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ReloadConfigCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {

        if (!sender.hasPermission(Permission.RELOAD.getNode())) {
            sender.sendMessage(NO_PERMISSION.getStringValue().formatted(Permission.RELOAD.getNode()));
            return true;
        }

        ConfigurationFileManager.getInstance().reloadConfig();
        sender.sendMessage(RELOADED.getStringValue());
        return true;
    }
}
