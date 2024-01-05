package io.stealingdapenta.mc2048.listeners;

import io.stealingdapenta.mc2048.GameManager;
import io.stealingdapenta.mc2048.utils.ActiveGame;
import io.stealingdapenta.mc2048.utils.Direction;
import io.stealingdapenta.mc2048.utils.InventoryUtil;
import java.util.Objects;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GameControlsListener implements Listener {

    private static final String INVALID_MOVE = "Sorry! That's not a valid move.";
    private static final String GAME_OVER = "Game over!";
    private static final String GAME_OVER_SUB = "Score: %s | Playtime: %s";
    private final InventoryUtil inventoryUtil;
    private final GameManager gameManager;

    public GameControlsListener(InventoryUtil inventoryUtil, GameManager gameManager) {
        this.inventoryUtil = inventoryUtil;
        this.gameManager = gameManager;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onButtonClick(InventoryClickEvent event) {
        InventoryView clickedInventoryView = event.getView();
        if (!inventoryUtil.isGameWindow(clickedInventoryView)) {
            return;
        }
        Player player = (Player) event.getWhoClicked();

        event.setCancelled(true);

        ItemStack clickedItem = event.getCurrentItem();
        if (Objects.isNull(clickedItem)) {
            return;
        }
        ItemMeta itemMeta = clickedItem.getItemMeta();
        if (Objects.isNull(itemMeta)) {
            return;
        }

        ActiveGame activeGame = gameManager.getActiveGame(player);
        if (Objects.isNull(activeGame) || Objects.isNull(activeGame.getGameWindow())) {
            return;
        }

        String displayName = itemMeta.getDisplayName();
        boolean move;
        if (displayName.contains("UP")) {
            move = inventoryUtil.moveItemsInDirection(activeGame, Direction.UP);
        } else if (displayName.contains("LEFT")) {
            move = inventoryUtil.moveItemsInDirection(activeGame, Direction.LEFT);
        } else if (displayName.contains("RIGHT")) {
            move = inventoryUtil.moveItemsInDirection(activeGame, Direction.RIGHT);
        } else if (displayName.contains("DOWN")) {
            move = inventoryUtil.moveItemsInDirection(activeGame, Direction.DOWN);
        } else {
            return;
        }

        if (move) {
            inventoryUtil.spawnNewBlock(activeGame.getGameWindow());

            if (inventoryUtil.noValidMovesLeft(activeGame.getGameWindow())) {
                gameManager.deactivateGameFor(player);
                activeGame.getPlayer()
                          .getOpenInventory()
                          .close();
                doGameOver(activeGame);
            }

        } else {
            player.sendMessage(ChatColor.DARK_PURPLE + INVALID_MOVE);
        }

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onGameClose(InventoryCloseEvent event) {
        InventoryView clickedInventoryView = event.getView();
        if (!inventoryUtil.isGameWindow(clickedInventoryView)) {
            return;
        }

        Player player = (Player) event.getPlayer();

        ActiveGame activeGame = gameManager.getActiveGame(player);
        if (Objects.isNull(activeGame)) {
            return;
        }

        doGameOver(activeGame);
        gameManager.deactivateGameFor(player);
    }

    private void doGameOver(ActiveGame activeGame) {
        activeGame.getPlayer()
                  .sendTitle(GAME_OVER, GAME_OVER_SUB.formatted(activeGame.getScore(), activeGame.getCurrentPlayTimeFormatted()), 20, 40, 20);
    }
}
