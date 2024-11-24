package io.stealingdapenta.mc2048.commands;

import static io.stealingdapenta.mc2048.config.ConfigKey.GOOD_LUCK;
import static io.stealingdapenta.mc2048.config.ConfigKey.NOT_PLAYER;

import io.stealingdapenta.mc2048.GameManager;
import io.stealingdapenta.mc2048.utils.ActiveGame;
import io.stealingdapenta.mc2048.utils.InventoryUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

public class GameCommand implements CommandExecutor {

    private final InventoryUtil inventoryUtil;
    private final GameManager gameManager;

    public GameCommand(InventoryUtil inventoryUtil, GameManager gameManager) {
        this.inventoryUtil = inventoryUtil;
        this.gameManager = gameManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player player) {
            player.sendMessage(GOOD_LUCK.getFormattedStringValue());

            ActiveGame activeGame = new ActiveGame(player, gameManager.createTask(player));
            Inventory gameWindow = inventoryUtil.createGameInventory(activeGame);
            player.openInventory(gameWindow);

            gameManager.activateGame(activeGame);

            // 2 starting blocks
            inventoryUtil.spawnNewBlock(gameWindow);
            inventoryUtil.spawnNewBlock(gameWindow);
            return true;
        }

        sender.sendMessage(NOT_PLAYER.getFormattedStringValue());

        return true;
    }
}