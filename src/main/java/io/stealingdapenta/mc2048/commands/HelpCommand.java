package io.stealingdapenta.mc2048.commands;

import static io.stealingdapenta.mc2048.config.ConfigKey.MSG_NOT_PLAYER;
import static io.stealingdapenta.mc2048.utils.MessageSender.MESSAGE_SENDER;

import io.stealingdapenta.mc2048.utils.InventoryUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

public class HelpCommand implements CommandExecutor {

    private final InventoryUtil inventoryUtil;

    public HelpCommand(InventoryUtil inventoryUtil) {
        this.inventoryUtil = inventoryUtil;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player player)) {
            MESSAGE_SENDER.sendMessage(sender, MSG_NOT_PLAYER);
            return true;
        }

        Inventory gameWindow = inventoryUtil.createHelpInventory(player);
        player.openInventory(gameWindow);
        return true;
    }
}
