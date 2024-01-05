package io.stealingdapenta.mc2048;

import static io.stealingdapenta.mc2048.MC2048.logger;

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

    public GameManager(InventoryUtil inventoryUtil) {
        this.inventoryUtil = inventoryUtil;
    }

    public void activateGame(ActiveGame activeGame) {
        activeGames.put(activeGame.getPlayer()
                                  .getUniqueId(), activeGame);
    }

    public void deactivateGameFor(Player player) {
        ActiveGame activeGame = activeGames.get(player.getUniqueId());
        if (Objects.isNull(activeGame)) {
            logger.warning("Error deactivating game for %s; no active game found.".formatted(player.getName()));
            return;
        }

        activeGame.getRelatedTask()
                  .cancel();
        activeGames.remove(player.getUniqueId());
    }

    public void deactivateGame(ActiveGame activeGame) {
        RepeatingUpdateTask task = activeGame.getRelatedTask();
        Player player = activeGame.getPlayer();
        if (Objects.isNull(task)) {
            logger.warning("Error cancelling related task for active game of %s: task not found.".formatted(player.getName()));
        } else {
            task.cancel();
        }

        activeGames.remove(player.getUniqueId());
    }

    public boolean hasActiveGame(Player player) {
        return activeGames.containsKey(player.getUniqueId());
    }

    public Inventory getGameWindow(Player player) {
        return getActiveGame(player).getGameWindow();
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
