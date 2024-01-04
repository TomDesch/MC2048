package io.stealingdapenta.mc2048;

import io.stealingdapenta.mc2048.utils.ActiveGame;
import io.stealingdapenta.mc2048.utils.InventoryUtil;
import io.stealingdapenta.mc2048.utils.RepeatingUpdateTask;
import java.util.HashMap;
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
        // todo make NPE proof
        activeGames.get(player.getUniqueId())
                   .getRelatedTask()
                   .cancel();
        activeGames.remove(player.getUniqueId());
    }

    public void deactivateGame(ActiveGame activeGame) {
        // todo make NPE proof
        activeGame.getRelatedTask()
                  .cancel();
        activeGames.remove(activeGame.getPlayer()
                                     .getUniqueId());
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
