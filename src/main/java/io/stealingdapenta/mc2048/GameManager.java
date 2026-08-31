package io.stealingdapenta.mc2048;

import static io.stealingdapenta.mc2048.MC2048.logger;
import static io.stealingdapenta.mc2048.config.ConfigKey.MSG_GAME_RESUMED;
import static io.stealingdapenta.mc2048.config.ConfigKey.MSG_GAME_STARTED;
import static io.stealingdapenta.mc2048.config.ConfigKey.PLAYER_ITEM_SLOT;
import static io.stealingdapenta.mc2048.config.PlayerConfigField.ATTEMPTS;
import static io.stealingdapenta.mc2048.config.PlayerConfigField.AVERAGE_SCORE;
import static io.stealingdapenta.mc2048.config.PlayerConfigField.HIGH_SCORE;
import static io.stealingdapenta.mc2048.config.PlayerConfigField.PLAYTIME;
import static io.stealingdapenta.mc2048.utils.FileManager.FILE_MANAGER;
import static io.stealingdapenta.mc2048.utils.MessageSender.MESSAGE_SENDER;

import io.stealingdapenta.mc2048.utils.InventoryUtil;
import io.stealingdapenta.mc2048.utils.HighScoreManager;
import io.stealingdapenta.mc2048.utils.data.ActiveGame;
import io.stealingdapenta.mc2048.utils.data.RepeatingUpdateTask;
import io.stealingdapenta.mc2048.utils.data.SavedGame;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class GameManager {

    private static final HashMap<UUID, ActiveGame> activeGames = new HashMap<>();
    private final InventoryUtil inventoryUtil;
    private final HighScoreManager highScoreManager;
    private static final String ERROR_DEACTIVATING = "Error deactivating game for %s; no active game found.";
    private static final long ONE_SECOND_IN_TICKS = 20L;

    public GameManager(InventoryUtil inventoryUtil, HighScoreManager highScoreManager) {
        this.inventoryUtil = inventoryUtil;
        this.highScoreManager = highScoreManager;
    }

    public void activateGame(Player player) {
        ActiveGame existingGame = getActiveGame(player);
        if (existingGame != null) {
            existingGame.setResetConfirmationOpen(false);
            player.openInventory(existingGame.getGameWindow());
            return;
        }

        FILE_MANAGER.updatePlayerName(player);
        Optional<SavedGame> savedGame = FILE_MANAGER.getSavedGame(player);
        MESSAGE_SENDER.sendMessage(player, savedGame.isPresent() ? MSG_GAME_RESUMED : MSG_GAME_STARTED);

        ActiveGame activeGame = new ActiveGame(player, createTaskUpdatingPlayerStatItem(player), savedGame.orElse(null));
        highScoreManager.recordScore(player, activeGame.getHighScore());
        Inventory gameWindow = inventoryUtil.createGameInventory(activeGame);
        activeGames.put(activeGame.getPlayer()
                                  .getUniqueId(), activeGame);

        if (savedGame.isPresent()) {
            inventoryUtil.restoreSavedGame(activeGame, savedGame.get());
        } else {
            inventoryUtil.spawnNewBlock(gameWindow);
            inventoryUtil.spawnNewBlock(gameWindow);
            persistGame(activeGame);
        }

        player.openInventory(gameWindow);
    }

    public void deactivateGameFor(Player player) {
        ActiveGame activeGame = activeGames.get(player.getUniqueId());
        if (Objects.isNull(activeGame)) {
            logger.warning(ERROR_DEACTIVATING.formatted(player.getName()));
            return;
        }

        pauseGame(activeGame);
    }

    public void pauseGame(ActiveGame activeGame) {
        persistGame(activeGame);
        removeActiveGame(activeGame);
    }

    public void completeGame(ActiveGame activeGame) {
        FILE_MANAGER.clearSavedGame(activeGame.getPlayer());
        saveCompletedGame(activeGame);
        removeActiveGame(activeGame);
    }

    public void resetGame(ActiveGame activeGame) {
        FILE_MANAGER.clearSavedGame(activeGame.getPlayer());
        removeActiveGame(activeGame);
    }

    public void persistGame(ActiveGame activeGame) {
        if (activeGames.get(activeGame.getPlayer().getUniqueId()) == activeGame) {
            FILE_MANAGER.saveGame(activeGame.getPlayer(), inventoryUtil.createSavedGame(activeGame));
        }
    }

    private void removeActiveGame(ActiveGame activeGame) {
        if (Objects.nonNull(activeGame.getRelatedTask())) {
            activeGame.getRelatedTask().cancel();
        }

        activeGames.remove(activeGame.getPlayer().getUniqueId(), activeGame);
    }

    private void saveCompletedGame(ActiveGame activeGame) {
        if (activeGame.getScore() >= activeGame.getHighScore()) {
            FILE_MANAGER.setValueByKey(activeGame.getPlayer(), HIGH_SCORE.getKey(), activeGame.getScore());
            highScoreManager.recordScore(activeGame.getPlayer(), activeGame.getScore());
            // todo new high score fireworks?
        }

        FILE_MANAGER.setValueByKey(activeGame.getPlayer(), ATTEMPTS.getKey(), activeGame.getAttempts() + 1);
        FILE_MANAGER.setValueByKey(activeGame.getPlayer(), PLAYTIME.getKey(), (activeGame.getTotalPlayTime() + activeGame.getMillisecondsSinceStart()));
        FILE_MANAGER.setValueByKey(activeGame.getPlayer(), AVERAGE_SCORE.getKey(), activeGame.calculateNewAverageScore());
    }

    public ActiveGame getActiveGame(Player player) {
        return activeGames.get(player.getUniqueId());
    }

    public void deactivateAllGames() {
        new ArrayList<>(activeGames.values()).forEach(this::pauseGame);
    }

    public RepeatingUpdateTask createTaskUpdatingPlayerStatItem(Player player) {
        if (PLAYER_ITEM_SLOT.getIntValue() < 0) {
            return null;
        }

        return new RepeatingUpdateTask(0, ONE_SECOND_IN_TICKS) {
            public void run() {
                try {
                    getActiveGame(player).updateStatisticsItem();
                } catch (NullPointerException e) {
                    cancel();
                }
            }
        };
    }
}
