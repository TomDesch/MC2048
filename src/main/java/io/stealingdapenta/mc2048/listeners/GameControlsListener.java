package io.stealingdapenta.mc2048.listeners;

import static io.stealingdapenta.mc2048.MC2048.logger;
import static io.stealingdapenta.mc2048.config.ConfigKey.DOWN_BUTTON_NAME;
import static io.stealingdapenta.mc2048.config.ConfigKey.GAME_OVER;
import static io.stealingdapenta.mc2048.config.ConfigKey.HELP_GUI_PLAY_BUTTON_NAME;
import static io.stealingdapenta.mc2048.config.ConfigKey.INVALID_MOVE;
import static io.stealingdapenta.mc2048.config.ConfigKey.LEFT_BUTTON_NAME;
import static io.stealingdapenta.mc2048.config.ConfigKey.RIGHT_BUTTON_NAME;
import static io.stealingdapenta.mc2048.config.ConfigKey.UNDID_LAST_MOVE;
import static io.stealingdapenta.mc2048.config.ConfigKey.UNDO_BUTTON_NAME;
import static io.stealingdapenta.mc2048.config.ConfigKey.UP_BUTTON_NAME;
import static io.stealingdapenta.mc2048.utils.MessageSender.MESSAGE_SENDER;

import io.stealingdapenta.mc2048.GameManager;
import io.stealingdapenta.mc2048.utils.ActiveGame;
import io.stealingdapenta.mc2048.utils.Direction;
import io.stealingdapenta.mc2048.utils.InventoryUtil;
import java.util.Map;
import java.util.Objects;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
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

    private static final String GAME_OVER_SUB = "Score: %s | Playtime: %s"; // fixme potentially move this to configs
    private Map<String, Direction> DIRECTION_MAP;
    private final InventoryUtil inventoryUtil;
    private final GameManager gameManager;

    public GameControlsListener(InventoryUtil inventoryUtil, GameManager gameManager) {
        this.inventoryUtil = inventoryUtil;
        this.gameManager = gameManager;
    }

    private void initDirectionMap() {
        DIRECTION_MAP = Map.of(LegacyComponentSerializer.legacySection()
                                                        .serialize(UP_BUTTON_NAME.getFormattedValue()), Direction.UP, LegacyComponentSerializer.legacySection()
                                                                                                                                               .serialize(LEFT_BUTTON_NAME.getFormattedValue()), Direction.LEFT, LegacyComponentSerializer.legacySection()
                                                                                                                                                                                                                                          .serialize(
                                                                                                                                                                                                                                                  RIGHT_BUTTON_NAME.getFormattedValue()),
                               Direction.RIGHT, LegacyComponentSerializer.legacySection()
                                                                         .serialize(DOWN_BUTTON_NAME.getFormattedValue()), Direction.DOWN, LegacyComponentSerializer.legacySection()
                                                                                                                                                                    .serialize(UNDO_BUTTON_NAME.getFormattedValue()), Direction.UNDO);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onButtonClick(InventoryClickEvent event) {
        InventoryView clickedInventoryView = event.getView();

        // Don't care if it's not a game window
        if (!inventoryUtil.isAnyGameWindow(clickedInventoryView)) {
            return;
        }

        // Cancel all default interaction behaviour if it's any game window
        event.setCancelled(true);

        if (inventoryUtil.isGameWindow(clickedInventoryView)) {
            handleGameWindowActions(event);
        } else if (inventoryUtil.isHelpWindow(clickedInventoryView)) {
            handleHelpWindowAction(event);
        } else {
            logger.warning("The inventory is an MC2048 window, yet not recognized: %s".formatted(clickedInventoryView.toString()));
        }
    }

    private void handleHelpWindowAction(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        String clickedItemDisplayName = getItemDisplayName(event.getCurrentItem());
        if (Objects.isNull(clickedItemDisplayName)) {
            return;
        }

        // Currently the only performable action in the help menu, is to launch the game
        if (clickedItemDisplayName.contains(HELP_GUI_PLAY_BUTTON_NAME.getStringValue())) {
            gameManager.activateGame(player);
        }
    }

    /**
     * @param itemStack the item to get the name from
     * @return the items display name or null
     */
    private String getItemDisplayName(ItemStack itemStack) {
        if (Objects.isNull(itemStack)) {
            return null;
        }

        ItemMeta itemMeta = itemStack.getItemMeta();
        if (Objects.isNull(itemMeta)) {
            return null;
        }

        return itemMeta.getDisplayName();
    }

    private void handleGameWindowActions(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        ActiveGame activeGame = gameManager.getActiveGame(player);
        if (Objects.isNull(activeGame) || Objects.isNull(activeGame.getGameWindow())) {
            return;
        }

        String clickedItemDisplayName = getItemDisplayName(event.getCurrentItem());
        if (Objects.isNull(clickedItemDisplayName)) {
            return;
        }

        initDirectionMap();
        Direction direction = DIRECTION_MAP.entrySet()
                                           .stream()
                                           .filter(entry -> clickedItemDisplayName.contains(entry.getKey()))
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
