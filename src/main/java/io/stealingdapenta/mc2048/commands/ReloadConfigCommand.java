package io.stealingdapenta.mc2048.commands;


import static io.stealingdapenta.mc2048.config.ConfigKey.MSG_NO_PERMS;
import static io.stealingdapenta.mc2048.config.ConfigKey.MSG_RELOADED;
import static io.stealingdapenta.mc2048.config.ConfigurationFileManager.CONFIGURATION_FILE_MANAGER;
import static io.stealingdapenta.mc2048.utils.MessageSender.MESSAGE_SENDER;

import io.stealingdapenta.mc2048.GameManager;
import io.stealingdapenta.mc2048.config.Permission;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ReloadConfigCommand implements CommandExecutor {

    private final GameManager gameManager;

    public ReloadConfigCommand(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {

        if (!sender.hasPermission(Permission.RELOAD.getNode())) {
            MESSAGE_SENDER.sendMessage(sender, MSG_NO_PERMS);
            return true;
        }

        try {
            CONFIGURATION_FILE_MANAGER.reloadConfig(gameManager.hasActiveGames());
            MESSAGE_SENDER.sendMessage(sender, MSG_RELOADED);
        } catch (IllegalArgumentException | IllegalStateException exception) {
            sender.sendMessage("MC2048 config reload failed: " + exception.getMessage());
        }
        return true;
    }
}
