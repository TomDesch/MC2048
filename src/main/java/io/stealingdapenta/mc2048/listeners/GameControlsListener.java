package io.stealingdapenta.mc2048.listeners;

import static io.stealingdapenta.mc2048.config.ConfigKey.MOVE_BUTTON_DOWN_NAME;
import static io.stealingdapenta.mc2048.config.ConfigKey.MOVE_BUTTON_LEFT_NAME;
import static io.stealingdapenta.mc2048.config.ConfigKey.MOVE_BUTTON_RIGHT_NAME;
import static io.stealingdapenta.mc2048.config.ConfigKey.MOVE_BUTTON_UP_NAME;
import static io.stealingdapenta.mc2048.config.ConfigKey.MSG_GAME_OVER;
import static io.stealingdapenta.mc2048.config.ConfigKey.MSG_GAME_OVER_SUB;
import static io.stealingdapenta.mc2048.config.ConfigKey.MSG_INVALID_MOVE;
import static io.stealingdapenta.mc2048.config.ConfigKey.MSG_UNDID_LAST_MOVE;
import static io.stealingdapenta.mc2048.config.ConfigKey.START_BUTTON_NAME;
import static io.stealingdapenta.mc2048.config.ConfigKey.SPEED_BUTTON_NAME;
import static io.stealingdapenta.mc2048.config.ConfigKey.UNDO_BUTTON_UNUSED_NAME;
import static io.stealingdapenta.mc2048.utils.FileManager.FILE_MANAGER;
import static io.stealingdapenta.mc2048.utils.MessageSender.MESSAGE_SENDER;

import io.stealingdapenta.mc2048.GameManager;
import io.stealingdapenta.mc2048.config.ConfigKey;
import io.stealingdapenta.mc2048.config.PlayerConfigField;
import io.stealingdapenta.mc2048.utils.InventoryUtil;
import io.stealingdapenta.mc2048.utils.data.ActiveGame;
import io.stealingdapenta.mc2048.utils.data.ButtonAction;

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
import org.bukkit.scheduler.BukkitRunnable;

public class GameControlsListener implements Listener {

    private Map<String, ButtonAction> ACTION_MAP;
    private final InventoryUtil inventoryUtil;
    private final GameManager gameManager;

    public GameControlsListener(InventoryUtil inventoryUtil, GameManager gameManager) {
        this.inventoryUtil = inventoryUtil;
        this.gameManager = gameManager;
    }

    private void initActionMap() {
        if (Objects.nonNull(ACTION_MAP) && !ACTION_MAP.isEmpty()) {
            return;
        }
        ACTION_MAP = Map.of(LegacyComponentSerializer.legacySection()
                                                     .serialize(MOVE_BUTTON_UP_NAME.getFormattedValue()), ButtonAction.UP, LegacyComponentSerializer.legacySection()
                                                                                                                                                    .serialize(MOVE_BUTTON_LEFT_NAME.getFormattedValue()), ButtonAction.LEFT,
                            LegacyComponentSerializer.legacySection()
                                                     .serialize(MOVE_BUTTON_RIGHT_NAME.getFormattedValue()), ButtonAction.RIGHT, LegacyComponentSerializer.legacySection()
                                                                                                                                                          .serialize(MOVE_BUTTON_DOWN_NAME.getFormattedValue()), ButtonAction.DOWN,
                            LegacyComponentSerializer.legacySection()
                                                     .serialize(UNDO_BUTTON_UNUSED_NAME.getFormattedValue()), ButtonAction.UNDO, LegacyComponentSerializer.legacySection()
                                                                                                                                                          .serialize(SPEED_BUTTON_NAME.getFormattedValue()), ButtonAction.SPEED,
                            LegacyComponentSerializer.legacySection()
                                                     .serialize(START_BUTTON_NAME.getFormattedValue()), ButtonAction.START);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onButtonClick(InventoryClickEvent event) {
        InventoryView clickedInventoryView = event.getView();

        // If it's not a game or help window, do nothing
        if (!inventoryUtil.isAnyGameWindow(clickedInventoryView) && !inventoryUtil.isHelpWindow(clickedInventoryView)) {
            return;
        }

        // Cancel all default interaction behavior if it's any game or help window
        event.setCancelled(true);

        // If clicked item doesn't have a name, do nothing
        String clickedItemDisplayName = getItemDisplayName(event.getCurrentItem());
        if (Objects.isNull(clickedItemDisplayName)) {
            return;
        }

        initActionMap();
        ButtonAction action = ACTION_MAP.entrySet()
                                .stream()
                                .filter(entry -> clickedItemDisplayName.contains(entry.getKey()))
                                .map(Map.Entry::getValue)
                                .findFirst()
                                .orElse(null);

        // If parsed action isn't defined, do nothing
        if (Objects.isNull(action)) {
            return;
        }

        Player player = (Player) event.getWhoClicked();

        if (inventoryUtil.isGameWindow(clickedInventoryView)) {
            ActiveGame activeGame = gameManager.getActiveGame(player);

            // If active game is null or locked, do nothing
            if (Objects.isNull(activeGame) || Objects.isNull(activeGame.getGameWindow()) || activeGame.isLocked()) {
                return;
            }

            handleGameWindowActions(player, activeGame, action);
        } else if (inventoryUtil.isHelpWindow(clickedInventoryView)) {
            handleHelpWindowAction(player, action);
        } else {
            inventoryUtil.getPlugin().getLogger().warning("The inventory is an MC2048 window, yet not recognized: %s".formatted(clickedInventoryView.toString()));
        }
    }

    private void handleHelpWindowAction(Player player, ButtonAction action) {
        if (ButtonAction.START.equals(action)) {
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

    private void handleGameWindowActions(Player player, ActiveGame activeGame, ButtonAction action) {

        if (Objects.isNull(action)) {
            invalidMoveMessage(player);
            return;
        }

        if (ButtonAction.SPEED.equals(action)) {
            int curr = FILE_MANAGER.getIntByKey(player, PlayerConfigField.ANIMATION_SPEED.getKey());
            if (0 <= curr && curr <= 4) {
                curr++;
            } else if (curr == 5) {
                curr = 0;
            } else {
                curr = ConfigKey.DEFAULT_ANIMATION_SPEED.getIntValue();
            }

            FILE_MANAGER.setValueByKey(player, PlayerConfigField.ANIMATION_SPEED.getKey(), curr);
            inventoryUtil.updateSpeedButton(activeGame, curr);
            return;
        }

        activeGame.setLock(true);

        int tickDelay = inventoryUtil.moveItemsInDirection(activeGame, action);
        if (tickDelay == 0) {
            invalidMoveMessage(player);
            activeGame.setLock(false);
            return;
        }

        if (tickDelay == -1 || ButtonAction.UNDO.equals(action)) {
            MESSAGE_SENDER.sendMessage(player, MSG_UNDID_LAST_MOVE);
            activeGame.setLock(false);
        } else if (tickDelay > 0) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    inventoryUtil.spawnNewBlock(activeGame.getGameWindow());
                    activeGame.setLock(false);

                    if (inventoryUtil.noValidMovesLeft(activeGame.getGameWindow()) && activeGame.hasNoUndoLastMoveLeft()) {
                        gameManager.deactivateGameFor(player);
                        activeGame.getPlayer()
                                  .getOpenInventory()
                                  .close();
                        doGameOver(activeGame);
                    }
                }
            }.runTaskLater(inventoryUtil.javaPlugin, tickDelay + (2L * FILE_MANAGER.getIntByKey(player, PlayerConfigField.ANIMATION_SPEED.getKey()) + 2));
        }
    }

    private void invalidMoveMessage(Player player) {
        MESSAGE_SENDER.sendMessage(player, MSG_INVALID_MOVE);
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

        if (activeGame.isLocked()) {
            return;
        }

        doGameOver(activeGame);
        gameManager.deactivateGameFor(player);
    }

    private void doGameOver(ActiveGame activeGame) {
        MESSAGE_SENDER.sendMessage(activeGame.getPlayer(), MSG_GAME_OVER);
        MESSAGE_SENDER.sendTitle(activeGame.getPlayer(), MSG_GAME_OVER.getFormattedValue(), MSG_GAME_OVER_SUB.getFormattedValue(String.valueOf(activeGame.getScore()), activeGame.getCurrentPlayTimeFormatted()));
    }
}
