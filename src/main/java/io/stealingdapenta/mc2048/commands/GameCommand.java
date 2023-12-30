package io.stealingdapenta.mc2048.commands;

import io.stealingdapenta.mc2048.GameManager;
import io.stealingdapenta.mc2048.utils.InventoryUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GameCommand implements CommandExecutor {
    private final InventoryUtil inventoryUtil;
    private final GameManager gameManager;
    private static final String NOT_PLAYER = "You can only execute this command as a player.";

    public GameCommand(InventoryUtil inventoryUtil, GameManager gameManager) {
        this.inventoryUtil = inventoryUtil;
        this.gameManager = gameManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            player.sendMessage(ChatColor.RED + "Good luck!");
            player.openInventory(inventoryUtil.getGameInventory(player));
            gameManager.activateGameFor(player);

            return true;
        }

        sender.sendMessage(ChatColor.RED + NOT_PLAYER);

        return true;
    }
}