package io.stealingdapenta.mc2048.commands;

import static io.stealingdapenta.mc2048.config.ConfigKey.MSG_NOT_PLAYER;

import io.stealingdapenta.mc2048.GameManager;
import io.stealingdapenta.mc2048.utils.MessageSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class GameCommand implements CommandExecutor {

    private final GameManager gameManager;
    private final MessageSender messageSender;

    public GameCommand(GameManager gameManager, MessageSender messageSender) {
        this.gameManager = gameManager;
        this.messageSender = messageSender;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player player)) {
            messageSender.sendMessage(sender, MSG_NOT_PLAYER);
            return true;
        }

        gameManager.activateGame(player);

        return true;
    }
}