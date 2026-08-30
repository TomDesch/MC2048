package io.stealingdapenta.mc2048.listeners;

import static io.stealingdapenta.mc2048.MC2048.logger;
import static io.stealingdapenta.mc2048.config.ConfigKey.GAME_GUI_FILLER_ANIMATION;
import static io.stealingdapenta.mc2048.config.ConfigKey.MSG_GAME_OVER;
import static io.stealingdapenta.mc2048.config.ConfigKey.MSG_GAME_PAUSED;
import static io.stealingdapenta.mc2048.config.ConfigKey.MSG_INVALID_MOVE;
import static io.stealingdapenta.mc2048.config.ConfigKey.MSG_UNDID_LAST_MOVE;
import static io.stealingdapenta.mc2048.config.ConfigKey.TITLE_GAME_OVER;
import static io.stealingdapenta.mc2048.config.ConfigKey.TITLE_GAME_OVER_SUB;
import static io.stealingdapenta.mc2048.utils.FileManager.FILE_MANAGER;
import static io.stealingdapenta.mc2048.utils.MessageSender.MESSAGE_SENDER;

import io.stealingdapenta.mc2048.GameManager;
import io.stealingdapenta.mc2048.config.ConfigKey;
import io.stealingdapenta.mc2048.config.PlayerConfigField;
import io.stealingdapenta.mc2048.utils.InventoryUtil;
import io.stealingdapenta.mc2048.utils.StringUtil;
import io.stealingdapenta.mc2048.utils.data.ActiveGame;
import io.stealingdapenta.mc2048.utils.data.ButtonAction;
import java.util.Objects;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.scheduler.BukkitRunnable;

public class GameControlsListener implements Listener {

    private final InventoryUtil inventoryUtil;
    private final GameManager gameManager;

    public GameControlsListener(InventoryUtil inventoryUtil, GameManager gameManager) {
        this.inventoryUtil = inventoryUtil;
        this.gameManager = gameManager;
    }

    // NOTE: We intentionally don't parse actions from display names.
    // Display names are meant to be translatable and shouldn't affect gameplay logic.

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onButtonClick(InventoryClickEvent event) {
        InventoryView clickedInventoryView = event.getView();

        // If it's not a game or help window, do nothing
        if (!inventoryUtil.isAnyGameWindow(clickedInventoryView) && !inventoryUtil.isHelpWindow(clickedInventoryView)) {
            return;
        }

        // Cancel all default interaction behavior if it's any game or help window
        event.setCancelled(true);

        // Only react to clicks inside the top inventory (our GUI), not the player's inventory
        if (event.getClickedInventory() == null || !event.getClickedInventory()
                                                         .equals(clickedInventoryView.getTopInventory())) {
            return;
        }

        Player player = (Player) event.getWhoClicked();

        ButtonAction action;
        if (inventoryUtil.isResetConfirmationWindow(clickedInventoryView)) {
            action = getResetConfirmationAction(event.getSlot());
            handleResetConfirmationAction(player, action);
        } else if (inventoryUtil.isGameWindow(clickedInventoryView)) {
            ActiveGame activeGame = gameManager.getActiveGame(player);

            // If active game is null or locked, do nothing
            if (Objects.isNull(activeGame) || Objects.isNull(activeGame.getGameWindow()) || activeGame.isLocked()) {
                return;
            }

            action = getGameActionBySlot(event.getSlot());
            handleGameWindowActions(player, activeGame, action);
        } else if (inventoryUtil.isHelpWindow(clickedInventoryView)) {
            action = getHelpActionBySlot(event.getSlot());
            if (Objects.nonNull(action)) {
                handleHelpWindowAction(player, action);
            }
        } else {
            logger.warning("The inventory is an MC2048 window, yet not recognized: %s".formatted(clickedInventoryView.toString()));
        }
    }

    private ButtonAction getGameActionBySlot(int slot) {
        if (slot == ConfigKey.MOVE_BUTTON_UP_SLOT.getIntValue()) {
            return ButtonAction.UP;
        }
        if (slot == ConfigKey.MOVE_BUTTON_DOWN_SLOT.getIntValue()) {
            return ButtonAction.DOWN;
        }
        if (slot == ConfigKey.MOVE_BUTTON_LEFT_SLOT.getIntValue()) {
            return ButtonAction.LEFT;
        }
        if (slot == ConfigKey.MOVE_BUTTON_RIGHT_SLOT.getIntValue()) {
            return ButtonAction.RIGHT;
        }
        if (slot == ConfigKey.UNDO_BUTTON_SLOT.getIntValue()) {
            return ButtonAction.UNDO;
        }
        if (slot == ConfigKey.SPEED_BUTTON_SLOT.getIntValue()) {
            return ButtonAction.SPEED;
        }
        if (slot == ConfigKey.RESET_BUTTON_SLOT.getIntValue()) {
            return ButtonAction.RESET;
        }
        return null;
    }

    private ButtonAction getResetConfirmationAction(int slot) {
        if (slot == InventoryUtil.RESET_CONFIRM_SLOT) {
            return ButtonAction.RESET_CONFIRM;
        }
        if (slot == InventoryUtil.RESET_CANCEL_SLOT) {
            return ButtonAction.RESET_CANCEL;
        }
        return null;
    }

    private ButtonAction getHelpActionBySlot(int slot) {
        // Help menu uses hardcoded slots in InventoryUtil#createHelpInventory
        // (playButtonSlot == 49)
        if (slot == 49) {
            return ButtonAction.START;
        }
        return null;
    }

    private void handleHelpWindowAction(Player player, ButtonAction action) {
        if (ButtonAction.START.equals(action)) {
            gameManager.activateGame(player);
        }
    }

    private void handleResetConfirmationAction(Player player, ButtonAction action) {
        ActiveGame activeGame = gameManager.getActiveGame(player);
        if (activeGame == null || action == null) {
            return;
        }

        activeGame.setResetConfirmationOpen(false);
        if (ButtonAction.RESET_CONFIRM.equals(action)) {
            gameManager.resetGame(activeGame);
            gameManager.activateGame(player);
        } else if (ButtonAction.RESET_CANCEL.equals(action)) {
            player.openInventory(activeGame.getGameWindow());
        }
    }


    private void handleGameWindowActions(Player player, ActiveGame activeGame, ButtonAction action) {

        if (Objects.isNull(action)) {
            invalidMoveMessage(player);
            return;
        }

        if (ButtonAction.SPEED.equals(action)) {
            int curr = FILE_MANAGER.getAnimationSpeed(player);
            if (1 <= curr && curr <= 5) {
                curr++;
            } else if (curr == 6) {
                curr = 1;
            } else {
                curr = ConfigKey.SPEED_BUTTON_SPEED_DEFAULT.getIntValue();
            }

            FILE_MANAGER.setValueByKey(player, PlayerConfigField.ANIMATION_SPEED.getKey(), curr);
            inventoryUtil.updateSpeedButton(activeGame, curr);
            return;
        }

        if (ButtonAction.RESET.equals(action)) {
            activeGame.setResetConfirmationOpen(true);
            player.openInventory(inventoryUtil.createResetConfirmationInventory(player));
            return;
        }

        activeGame.setLock(true);

        int tickDelay = inventoryUtil.processGameAction(activeGame, action);
        if (tickDelay == 0) {
            invalidMoveMessage(player);
            activeGame.setLock(false);
            return;
        }

        if (tickDelay == -1 || ButtonAction.UNDO.equals(action)) {
            MESSAGE_SENDER.sendMessage(player, MSG_UNDID_LAST_MOVE);
            inventoryUtil.updateUndoButton(activeGame);
            gameManager.persistGame(activeGame);
            activeGame.setLock(false);
        } else if (tickDelay > 0) {
            final long caclualtedDelay = tickDelay + (2L * FILE_MANAGER.getAnimationDelay(player) + 2);
            new BukkitRunnable() {
                @Override
                public void run() {
                    inventoryUtil.spawnNewBlock(activeGame.getGameWindow());
                    inventoryUtil.updateUndoButton(activeGame);
                    gameManager.persistGame(activeGame);

                    if (inventoryUtil.noValidMovesLeft(activeGame.getGameWindow()) && activeGame.hasNoUndoLastMoveLeft()) {
                        if (activeGame.isCloseRequested()) {
                            gameManager.completeGame(activeGame);
                            doGameOver(activeGame);
                            return;
                        }

                        long endDelay;
                        if (GAME_GUI_FILLER_ANIMATION.getStringValue().contains("enable")) {
                            endDelay = caclualtedDelay*5;
                            inventoryUtil.showEndAnimation(activeGame.getGameWindow(), caclualtedDelay);
                        } else {
                            endDelay = caclualtedDelay*3;
                        }

                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                gameManager.completeGame(activeGame);
                                activeGame.getPlayer()
                                            .getOpenInventory()
                                            .close();
                                doGameOver(activeGame);
                            }
                        }.runTaskLater(inventoryUtil.javaPlugin, endDelay);
                    } else if (activeGame.isCloseRequested()) {
                        MESSAGE_SENDER.sendMessage(activeGame.getPlayer(), MSG_GAME_PAUSED);
                        gameManager.pauseGame(activeGame);
                    } else {
                        activeGame.setLock(false);
                    }
                }
            }.runTaskLater(inventoryUtil.javaPlugin, caclualtedDelay);
        }
    }

    private void invalidMoveMessage(Player player) {
        MESSAGE_SENDER.sendMessage(player, MSG_INVALID_MOVE);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onGameClose(InventoryCloseEvent event) {
        InventoryView clickedInventoryView = event.getView();
        if (inventoryUtil.isResetConfirmationWindow(clickedInventoryView)) {
            pauseFromResetConfirmation(event);
            return;
        }
        if (!inventoryUtil.isGameWindow(clickedInventoryView)) {
            return;
        }

        Player player = (Player) event.getPlayer();

        ActiveGame activeGame = gameManager.getActiveGame(player);
        if (Objects.isNull(activeGame)) {
            return;
        }

        if (activeGame.isResetConfirmationOpen()) {
            return;
        }

        if (activeGame.isLocked()) {
            activeGame.requestClose();
            return;
        }

        MESSAGE_SENDER.sendMessage(player, MSG_GAME_PAUSED);
        gameManager.pauseGame(activeGame);
    }

    private void pauseFromResetConfirmation(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        ActiveGame activeGame = gameManager.getActiveGame(player);
        if (activeGame != null && activeGame.isResetConfirmationOpen()) {
            activeGame.setResetConfirmationOpen(false);
            MESSAGE_SENDER.sendMessage(player, MSG_GAME_PAUSED);
            gameManager.pauseGame(activeGame);
        }
    }

    private void doGameOver(ActiveGame activeGame) {
        MESSAGE_SENDER.sendMessage(activeGame.getPlayer(), MSG_GAME_OVER);
        MESSAGE_SENDER.sendTitle(activeGame.getPlayer(), TITLE_GAME_OVER.getFormattedValue(), TITLE_GAME_OVER_SUB.getFormattedValue(StringUtil.formatInt(activeGame.getScore()), activeGame.getCurrentPlayTimeFormatted()));
    }
}
