package io.stealingdapenta.mc2048;

import static io.stealingdapenta.mc2048.MC2048.logger;
import static io.stealingdapenta.mc2048.utils.ConfigField.ATTEMPTS;
import static io.stealingdapenta.mc2048.utils.ConfigField.AVERAGE_SCORE;
import static io.stealingdapenta.mc2048.utils.ConfigField.HIGH_SCORE;
import static io.stealingdapenta.mc2048.utils.ConfigField.TOTAL_PLAYTIME;

import io.stealingdapenta.mc2048.utils.ActiveGame;
import io.stealingdapenta.mc2048.utils.FileManager;
import io.stealingdapenta.mc2048.utils.InventoryUtil;
import io.stealingdapenta.mc2048.utils.RepeatingUpdateTask;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;
import org.bukkit.entity.Player;

public class GameManager {

    private static final HashMap<UUID, ActiveGame> activeGames = new HashMap<>();
    private final InventoryUtil inventoryUtil;
    private final FileManager fileManager = FileManager.getInstance();
    private static final String ATTEMPT_PROTECTION = "The game wasn't saved because you didn't do anything. Your average score & attempts were protected.";
    private static final String ERROR_DEACTIVATING = "Error deactivating game for %s; no active game found.";

    public GameManager(InventoryUtil inventoryUtil) {
        this.inventoryUtil = inventoryUtil;
    }

    public void activateGame(ActiveGame activeGame) {
        activeGames.put(activeGame.getPlayer().getUniqueId(), activeGame);
    }

    public void deactivateGameFor(Player player) {
        ActiveGame activeGame = activeGames.get(player.getUniqueId());
        if (Objects.isNull(activeGame)) {
            logger.warning(ERROR_DEACTIVATING.formatted(player.getName()));
            return;
        }
        saveActiveGame(activeGame);
        activeGame.getRelatedTask().cancel();

        activeGames.remove(player.getUniqueId());
    }

    private void saveActiveGame(ActiveGame activeGame) {
        if (activeGame.getScore() < 1) {
            activeGame.getPlayer().sendMessage(ATTEMPT_PROTECTION);
            return;
        }
        if (activeGame.getScore() >= activeGame.getHiScore()) {
            fileManager.setValueByKey(activeGame.getPlayer(), HIGH_SCORE.getKey(), activeGame.getScore());
            // todo new high score fireworks?
        }

        fileManager.setValueByKey(activeGame.getPlayer(), ATTEMPTS.getKey(), activeGame.getAttempts() + 1);
        fileManager.setValueByKey(activeGame.getPlayer(), TOTAL_PLAYTIME.getKey(), (activeGame.getTotalPlayTime() + activeGame.getMillisecondsSinceStart()));
        fileManager.setValueByKey(activeGame.getPlayer(), AVERAGE_SCORE.getKey(), calculateNewAverageScore(activeGame));
    }

    private double calculateNewAverageScore(ActiveGame activeGame) {
        return (activeGame.getAttempts() * activeGame.getAverageScore() + activeGame.getScore()) / (activeGame.getAttempts() + 1);
    }

    public ActiveGame getActiveGame(Player player) {
        return activeGames.get(player.getUniqueId());
    }

    public RepeatingUpdateTask createTask(Player player) {
        return new RepeatingUpdateTask(20L, 20L) {
            public void run() {
                ActiveGame activeGame = getActiveGame(player);
                inventoryUtil.updateStatisticItem(activeGame);
            }
        };
    }
}
