package io.stealingdapenta.mc2048;

import java.util.HashMap;
import java.util.UUID;
import org.bukkit.entity.Player;

public class GameManager {

    private static final HashMap<UUID, Player> activeGames = new HashMap<>();

    public void activateGameFor(Player player) {
        activeGames.put(player.getUniqueId(), player);
    }

    public void deactivateGameFor(Player player) {
        activeGames.remove(player.getUniqueId());
    }

    public boolean hasActiveGame(Player player) {
        return activeGames.containsKey(player.getUniqueId());
    }
}
