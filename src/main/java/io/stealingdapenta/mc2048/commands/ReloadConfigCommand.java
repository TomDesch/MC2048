package io.stealingdapenta.mc2048.commands;


import static io.stealingdapenta.mc2048.config.ConfigKey.NO_PERMISSION;
import static io.stealingdapenta.mc2048.config.ConfigKey.RELOADED;
import static io.stealingdapenta.mc2048.config.ConfigurationFileManager.CONFIGURATION_FILE_MANAGER;
import static io.stealingdapenta.mc2048.utils.MessageSender.MESSAGE_SENDER;

import io.stealingdapenta.mc2048.config.Permission;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ReloadConfigCommand implements CommandExecutor {


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {

        if (!sender.hasPermission(Permission.RELOAD.getNode())) {
            MESSAGE_SENDER.sendMessage(sender, NO_PERMISSION);
            return true;
        }

        CONFIGURATION_FILE_MANAGER.reloadConfig();
        MESSAGE_SENDER.sendMessage(sender, RELOADED);
        return true;
    }
}
