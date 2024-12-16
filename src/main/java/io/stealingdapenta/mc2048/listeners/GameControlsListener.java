package io.stealingdapenta.mc2048.listeners;

import static io.stealingdapenta.mc2048.config.ConfigKey.GAME_OVER;
import static io.stealingdapenta.mc2048.config.ConfigKey.INVALID_MOVE;
import static io.stealingdapenta.mc2048.config.ConfigKey.UNDID_LAST_MOVE;
import static io.stealingdapenta.mc2048.utils.MessageSender.MESSAGE_SENDER;

import io.stealingdapenta.mc2048.GameManager;
import io.stealingdapenta.mc2048.utils.ActiveGame;
import io.stealingdapenta.mc2048.utils.Direction;
import io.stealingdapenta.mc2048.utils.InventoryUtil;
import java.util.Map;
import java.util.Objects;
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

    private static final String GAME_OVER_SUB = "Score: %s | Playtime: %s";
    private static final Map<String, Direction> DIRECTION_MAP = Map.of("UP", Direction.UP, "LEFT", Direction.LEFT, "RIGHT", Direction.RIGHT, "DOWN", Direction.DOWN, "UNDO", Direction.UNDO);
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

        Direction direction = DIRECTION_MAP.entrySet()
                                           .stream()
                                           .filter(entry -> displayName.contains(entry.getKey()))
                                           .map(Map.Entry::getValue)
                                           .findFirst()
                                           .orElse(null);

        if (Objects.isNull(direction)) {
            invalidMoveMessage(player);
            return;
        }

        boolean somethingMoved = inventoryUtil.moveItemsInDirection(activeGame, direction);
        if (!somethingMoved) {
            invalidMoveMessage(player);
            return;
        }

        if (Direction.UNDO.equals(direction)) {
            MESSAGE_SENDER.sendMessage(player, UNDID_LAST_MOVE);
            return;
        }

        inventoryUtil.spawnNewBlock(activeGame.getGameWindow());

        if (inventoryUtil.noValidMovesLeft(activeGame.getGameWindow()) && activeGame.hasNoUndoLastMoveLeft()) {
            gameManager.deactivateGameFor(player);
            activeGame.getPlayer()
                      .getOpenInventory()
                      .close();
            doGameOver(activeGame);
        }
    }

    private void invalidMoveMessage(Player player) {
        MESSAGE_SENDER.sendMessage(player, INVALID_MOVE);
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
        MESSAGE_SENDER.sendMessage(activeGame.getPlayer(), GAME_OVER);
        MESSAGE_SENDER.sendTitle(activeGame.getPlayer(), GAME_OVER, GAME_OVER_SUB.formatted(activeGame.getScore(), activeGame.getCurrentPlayTimeFormatted()));
    }
}
