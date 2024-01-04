package io.stealingdapenta.mc2048;

import io.stealingdapenta.mc2048.utils.ActiveGame;
import java.util.HashMap;
import java.util.UUID;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class GameManager {

    private static final HashMap<UUID, ActiveGame> activeGames = new HashMap<>();

    public void activateGameFor(Player player, Inventory inventory) {
        activeGames.put(player.getUniqueId(), new ActiveGame(player, inventory));
    }

    public void deactivateGameFor(Player player) {
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
}
