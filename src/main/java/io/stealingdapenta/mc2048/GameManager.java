package io.stealingdapenta.mc2048;

import static io.stealingdapenta.mc2048.MC2048.logger;
import static io.stealingdapenta.mc2048.config.ConfigKey.MSG_ATTEMPT_PROTECTION;
import static io.stealingdapenta.mc2048.config.ConfigKey.MSG_GAME_STARTED;
import static io.stealingdapenta.mc2048.config.PlayerConfigField.ATTEMPTS;
import static io.stealingdapenta.mc2048.config.PlayerConfigField.AVERAGE_SCORE;
import static io.stealingdapenta.mc2048.config.PlayerConfigField.HIGH_SCORE;
import static io.stealingdapenta.mc2048.config.PlayerConfigField.PLAYTIME;
import static io.stealingdapenta.mc2048.utils.FileManager.FILE_MANAGER;
import static io.stealingdapenta.mc2048.utils.MessageSender.MESSAGE_SENDER;

import io.stealingdapenta.mc2048.utils.ActiveGame;
import io.stealingdapenta.mc2048.utils.InventoryUtil;
import io.stealingdapenta.mc2048.utils.RepeatingUpdateTask;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class GameManager {

    private static final HashMap<UUID, ActiveGame> activeGames = new HashMap<>();
    private final InventoryUtil inventoryUtil;
    private static final String ERROR_DEACTIVATING = "Error deactivating game for %s; no active game found.";
    private static final long ONE_SECOND_IN_TICKS = 20L;

    public GameManager(InventoryUtil inventoryUtil) {
        this.inventoryUtil = inventoryUtil;
    }

    public void activateGame(Player player) {
        MESSAGE_SENDER.sendMessage(player, MSG_GAME_STARTED);

        ActiveGame activeGame = new ActiveGame(player, createTaskUpdatingPlayerStatItem(player));
        Inventory gameWindow = inventoryUtil.createGameInventory(activeGame);
        player.openInventory(gameWindow);

        activeGames.put(activeGame.getPlayer()
                                  .getUniqueId(), activeGame);
        // 2 starting blocks
        inventoryUtil.spawnNewBlock(gameWindow);
        inventoryUtil.spawnNewBlock(gameWindow);
    }

    public void deactivateGameFor(Player player) {
        ActiveGame activeGame = activeGames.get(player.getUniqueId());
        if (Objects.isNull(activeGame)) {
            logger.warning(ERROR_DEACTIVATING.formatted(player.getName()));
            return;
        }
        saveActiveGame(activeGame);
        activeGame.getRelatedTask()
                  .cancel();

        activeGames.remove(player.getUniqueId());
    }

    private void saveActiveGame(ActiveGame activeGame) {
        if (activeGame.getScore() < 1) {
            MESSAGE_SENDER.sendMessage(activeGame.getPlayer(), MSG_ATTEMPT_PROTECTION);
            return;
        }
        if (activeGame.getScore() >= activeGame.getHighScore()) {
            FILE_MANAGER.setValueByKey(activeGame.getPlayer(), HIGH_SCORE.getKey(), activeGame.getScore());
            // todo new high score fireworks?
        }

        FILE_MANAGER.setValueByKey(activeGame.getPlayer(), ATTEMPTS.getKey(), activeGame.getAttempts() + 1);
        FILE_MANAGER.setValueByKey(activeGame.getPlayer(), PLAYTIME.getKey(), (activeGame.getTotalPlayTime() + activeGame.getMillisecondsSinceStart()));
        FILE_MANAGER.setValueByKey(activeGame.getPlayer(), AVERAGE_SCORE.getKey(), activeGame.calculateNewAverageScore());
    }

    public ActiveGame getActiveGame(Player player) {
        return activeGames.get(player.getUniqueId());
    }

    public RepeatingUpdateTask createTaskUpdatingPlayerStatItem(Player player) {
        return new RepeatingUpdateTask(0, ONE_SECOND_IN_TICKS) {
            public void run() {
                getActiveGame(player).updateStatisticsItem();
            }
        };
    }
}
